package sk.ditec.cud.procvys;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;

public class SOAPExternalClientLoggingDBHandler implements SOAPHandler<SOAPMessageContext> {
    static Logger log = LoggerFactory.getLogger(SOAPExternalClientLoggingDBHandler.class);
    static boolean logFile;
    static boolean logDb;
    static String logFileIgnore;
    static String logDbIgnore;
    static Date lastLogReinitialize = null;
    static String encFrom = "windows-1250";
    static String encTo = "UTF-8";
    static String appClientSuffix = "_ws_client";

    public SOAPExternalClientLoggingDBHandler() {
    }

    protected synchronized void reinitializeLog() {
        String logFileStr = this.getPramValue("ws.log.file");
        if (!StringUtils.isValid(logFileStr)) {
            logFile = false;
        } else if ("false".equalsIgnoreCase(logFileStr)) {
            logFile = false;
        } else if ("true".equalsIgnoreCase(logFileStr)) {
            logFile = true;
        }

        logFileIgnore = this.getPramValue("ws.log.file.ignore");
        String logDbStr = this.getPramValue("ws.log.db");
        if (!StringUtils.isValid(logDbStr)) {
            logDb = true;
        } else if ("true".equalsIgnoreCase(logDbStr)) {
            logDb = true;
        } else if ("false".equalsIgnoreCase(logDbStr)) {
            logDb = false;
        }

        logDbIgnore = this.getPramValue("ws.log.db.ignore");
        encFrom = this.getPramValue("ws.log.db.encodingFrom");
        if (!StringUtils.isValid(encFrom)) {
            encFrom = "windows-1250";
        }

        encTo = this.getPramValue("ws.log.db.encodingTo");
        if (!StringUtils.isValid(encTo)) {
            encTo = "UTF-8";
        }
    }

    protected String getPramValue(String paramName) {
        String paramValue = FrameworkUtils.getConfigProperty("vyluky", paramName);
        if (StringUtils.isValid(paramValue))
            return paramValue;

		String app = sk.ditec.cud.procvys.ApplicationContext.getContextPath();
        app = app.replaceAll("/", "");
        paramValue = FrameworkUtils.getConfigProperty(app, paramName);
        if (StringUtils.isValid(paramValue))
            return paramValue;

        paramValue = FrameworkUtils.getConfigProperty("framework", paramName);
        return paramValue;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        try {
            this.processMessage(messageContext);
        } catch (Exception var3) {
            log.error("Vynimka pri logovani WS volania", var3);
        }

        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.EMPTY_SET;
    }

    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        try {
            this.processMessage(messageContext);
        } catch (Exception var3) {
            log.error("Vynimka pri logovani WS volania", var3);
        }

        return true;
    }

    protected void processMessage(SOAPMessageContext messageContext) throws Exception {
        boolean logReinitialize = false;
        if (lastLogReinitialize == null) {
            logReinitialize = true;
            lastLogReinitialize = new Date();
        } else if ((new Date()).getTime() - lastLogReinitialize.getTime() > 600000L) {
            logReinitialize = true;
            lastLogReinitialize = new Date();
        }

        if (logReinitialize) {
            Date start = new Date();
            this.reinitializeLog();
            Date end = new Date();
            log.debug("reinitializeLog trvanie: " + (end.getTime() - start.getTime()) + " ms");
        }

        SOAPMessage msg = messageContext.getMessage();
        String soapAction = "neznama";
        String ucet = "neznamy";

        try {
            soapAction = msg.getSOAPBody().getFirstChild().getNodeName();
        } catch (Exception var18) {
            log.debug("Vynimka pri zistovani soapAction");
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        msg.writeTo(out);

        Boolean outboundProperty = (Boolean)messageContext.get("javax.xml.ws.handler.message.outbound");
        String ws = (String)messageContext.get("javax.xml.ws.service.endpoint.address");

        String soapActionBezResponse;
        if (outboundProperty) { // opacne ako pri WS endpointe, najprv je request smerom von a potom response smerom dnu

			String app = sk.ditec.cud.procvys.ApplicationContext.getContextPath();
            app = app.replaceAll("/", "");


            Random rnd = new Random();

            int log_id = rnd.nextInt(9);
            soapActionBezResponse = (new Long((new Date()).getTime() + (long)log_id)).toString();
            Date requestDt = new Date();
            DTOMsg dto = new DTOMsg();
            dto.setApp(app + appClientSuffix);
            dto.setMsgID(soapActionBezResponse.toString());
            dto.setReqMsg(out.toString());
            dto.setSoapAction(soapAction);
            dto.setStart(requestDt);
            dto.setStatus("R");
            dto.setUcet(ucet);
            dto.setWs(ws);
            messageContext.put("log_handler_request_dt", requestDt);
            messageContext.put("MSG_ID", soapActionBezResponse);
            messageContext.put(soapActionBezResponse, dto);
        } else {
            Integer code = (Integer)messageContext.get(MessageContext.HTTP_RESPONSE_CODE);
            String msgID = (String)messageContext.get("MSG_ID");
            DTOMsg dto = (DTOMsg)messageContext.get(msgID);
            Date end = new Date();
            dto.setRespMsg(out.toString());
            dto.setStatus("P");
            dto.setEnd(end);
            int rozdiel = (int)(dto.end.getTime() - dto.start.getTime());
            rozdiel /= 1000;

            dto.setTrvanie(rozdiel);
            dto.setPoznamka("HTTP_RESPONSE_CODE=" + code);

            if (logFile && (logFileIgnore == null || logFileIgnore.length() <= 1 || logFileIgnore.indexOf(soapAction) < 0)) {
                this.logFile(dto);
            }

            if (logDb) {
                soapActionBezResponse = soapAction.replaceAll("Response", "");
                if (logDbIgnore == null || logDbIgnore.length() <= 1 || logDbIgnore.indexOf(soapActionBezResponse) < 0) {
                    this.logDb(dto);
                }
            }



            log.debug("Dokoncene WS volanie: " + dto.getSoapAction() + " trvanie: " + rozdiel + "s");

            messageContext.remove("MSG_ID");
            messageContext.remove(msgID);
        }

    }

    @Override
    public void close(MessageContext messageContext) {
        Integer code = (Integer)messageContext.get(MessageContext.HTTP_RESPONSE_CODE);
//        HttpServletResponse response = (HttpServletResponse)messageContext.get(MessageContext.SERVLET_RESPONSE);
        String ws = (String)messageContext.get("javax.xml.ws.service.endpoint.address");


//        if (!(code == 0 && response == null))
//            return;

        String msgID = (String)messageContext.get("MSG_ID");
        if (msgID == null)
            return;

        DTOMsg dto = (DTOMsg)messageContext.get(msgID);
        if (dto == null)
            return;

        String poznamka = "pravdepodobne nastala chyba, zkontrolujte log. Nie je uzatvorene spracovanie. HTTP_RESPONSE_CODE=" + code;
        Date end = new Date();
        dto.setRespMsg(null);
        dto.setStatus("E");
        dto.setPoznamka(poznamka);
        dto.setEnd(end);

        int rozdiel = (int)(dto.end.getTime() - dto.start.getTime());
        rozdiel /= 1000;

        dto.setTrvanie(rozdiel);

        String soapAction = dto.getSoapAction();

        if (logFile && (logFileIgnore == null || logFileIgnore.length() <= 1 || logFileIgnore.indexOf(soapAction) < 0)) {
            this.logFile(dto);
        }

        if (logDb) {
            String soapActionBezResponse = soapAction.replaceAll("Response", "");
            if (logDbIgnore == null || logDbIgnore.length() <= 1 || logDbIgnore.indexOf(soapActionBezResponse) < 0) {
                this.logDb(dto);
            }
        }


        log.warn(poznamka);
        log.debug("Dokoncene WS volanie: " + dto.getSoapAction() + " / " + ws + " trvanie: " + rozdiel + "s");

        messageContext.remove("MSG_ID");
        messageContext.remove(msgID);
    }

    protected void logFile(DTOMsg dto) {
        Date start = new Date();

        try {
            String dir = "..\\logs\\archiv\\";
            Date now = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyMMdd", new Locale("sk"));
            String day = dateFormat.format(now);
            File f = new File(dir + day);
            if (!f.exists() || !f.isDirectory()) {
                boolean result = f.mkdir();
                log.debug("Vytvaram adresar: " + dir + day + " vysledok: " + result);
            }

            DateFormat dateFormat2 = new SimpleDateFormat("HHmmssSSS", new Locale("sk"));
            String time = dateFormat2.format(now);
            Random rnd = new Random();
            int r = rnd.nextInt(9);
            String name = time + r + "_" + dto.getApp();
            String fName = dir + day + "\\" + name + ".xml";
            FileOutputStream fos = new FileOutputStream(fName);
            DateFormat dateFormat3 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", new Locale("sk"));
            String reqDt = dateFormat3.format(dto.getStart());
            String respDt = dateFormat3.format(dto.getEnd());
            double trvanie = (double)((int)(dto.getEnd().getTime() - dto.getStart().getTime()));
            String info = "<info>ucet=" + dto.getUcet() + ",app=" + dto.getApp() + ",action=" + dto.getSoapAction() + ",reqDt=" + reqDt + ",respDt=" + respDt + ",trvanie=" + trvanie + "ms" + ",status=" + dto.getStatus() + ",poznamka=" + dto.getPoznamka() + "</info>\n";
            String request = "<request>" + dto.getReqMsg() + "</request>\n";
            String response = "<response>" + dto.getRespMsg() + "</response>\n";
            String data = "<log>" + info + request + response + "</log>";
            fos.write(data.getBytes("windows-1250"));
            fos.close();
        } catch (Exception var24) {
            log.error("Vynimka pri logovani WS volania do suboru", var24);
        }

        Date end = new Date();
        log.debug("logFile trvanie: " + (end.getTime() - start.getTime()) + " ms");
    }

    protected int logDb(DTOMsg dto) {
        int log_id = 0;
        Date start = new Date();

        try {
            Connection con = null;

            try {
                String vlk_id = "vlk_id";
                if (StringUtils.isValid(dto.getVlkId())) {
                    vlk_id = dto.getVlkId();
                }

                con = DBUtils.getConnection("NOLOG");
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COMM_LOG_SEQ.NEXTVAL FROM DUAL");
                rs.next();
                log_id = rs.getInt(1);
                rs.close();
                stmt.close();
                Long msgID = new Long((new Date()).getTime());
                msgID = msgID + (long)log_id;
                PreparedStatement pstm = con.prepareStatement("INSERT INTO COMM_LOG (LOG_ID,UCET,APP,SOAP_ACTION,MSG_ID,REQUEST,REQUEST_DT,STAV,RESPONSE,RESPONSE_DT,VLK_ID,WS,POZNAMKA,TRVANIE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                pstm.setInt(1, log_id);
                pstm.setString(2, truncateString(dto.getUcet(), 50));
                pstm.setString(3, truncateString(dto.getApp(), 20));
                pstm.setString(4, truncateString(dto.getSoapAction(), 50));
                pstm.setLong(5, msgID);

                String encReq = null;

                try {
                    if (dto.getReqMsg() != null)
                        encReq = new String(dto.getReqMsg().getBytes(encFrom), encTo);
                } catch (Exception e) {
                    encReq = "Vynimka pri konvertovani kodovania requestu. data: " + dto.getReqMsg() + " encFrom=" + encFrom + " encTo=" + encTo;
                    log.error(encReq, e);
                }

                String encResp = null;

                try {
                    if (dto.getRespMsg() != null)
                        encResp = new String(dto.getRespMsg().getBytes(encFrom), encTo);
                } catch (Exception e) {
                    encResp = "Vynimka pri konvertovani kodovania responsu. data: " + dto.getRespMsg() + " encFrom=" + encFrom + " encTo=" + encTo;
                    log.error(encResp, e);
                }

                pstm.setString(6, encReq);
                pstm.setTimestamp(7, new Timestamp(dto.getStart().getTime()));
                pstm.setString(8, dto.getStatus());
                pstm.setString(9, encResp);
                pstm.setTimestamp(10, new Timestamp(dto.getEnd().getTime()));
                pstm.setString(11, vlk_id);
                pstm.setString(12, truncateString(dto.getWs(), 100));
                pstm.setString(13, dto.getPoznamka());
                pstm.setString(14, dto.getTrvanie() == null ? null : dto.getTrvanie().toString());

                pstm.execute();
                pstm.close();
                con.commit();
            } catch (Exception e) {
                con.rollback();
                log.error("Vynimka pri logovani WS volania do databazy", e);
            } finally {
                DBUtils.returnConnection(con);
            }
        } catch (Throwable e) {
            log.error("", e);
        }

        Date end = new Date();
        long trv = end.getTime() - start.getTime();
        if (trv > 100L) {
            log.debug("logDb do COMM_LOG trvanie: " + trv + " ms");
        }

        return log_id;
    }

    private String truncateString(String s, int l) {
        if (s == null || s.length() <= l)
            return s;

        return s.substring(0, l-1);
    }

    class DTOMsg {
        String ucet;
        String app;
        String soapAction;
        String msgID;
        String reqMsg;
        String respMsg;
        Date start;
        Date end;
        String status;
        String vlkId;
        String logId;
        String ws;
        String poznamka;
        Integer trvanie;

        DTOMsg() {
        }

        public String getUcet() {
            return this.ucet;
        }

        public String getApp() {
            return this.app;
        }

        public String getSoapAction() {
            return this.soapAction;
        }

        public String getReqMsg() {
            return this.reqMsg;
        }

        public String getRespMsg() {
            return this.respMsg;
        }

        public Date getStart() {
            return this.start;
        }

        public Date getEnd() {
            return this.end;
        }

        public String getStatus() {
            return this.status;
        }

        public void setUcet(String ucet) {
            this.ucet = ucet;
        }

        public void setApp(String app) {
            this.app = app;
        }

        public void setSoapAction(String soapAction) {
            this.soapAction = soapAction;
        }

        public void setReqMsg(String reqMsg) {
            this.reqMsg = reqMsg;
        }

        public void setRespMsg(String respMsg) {
            this.respMsg = respMsg;
        }

        public void setStart(Date start) {
            this.start = start;
        }

        public void setEnd(Date end) {
            this.end = end;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMsgID() {
            return this.msgID;
        }

        public void setMsgID(String msgID) {
            this.msgID = msgID;
        }

        public String getVlkId() {
            return this.vlkId;
        }

        public void setVlkId(String vlkId) {
            this.vlkId = vlkId;
        }

        public String getWs() {
            return this.ws;
        }

        public void setWs(String ws) {
            this.ws = ws;
        }

        public String getPoznamka() {
            return poznamka;
        }

        public void setPoznamka(String poznamka) {
            this.poznamka = poznamka;
        }

        public Integer getTrvanie() {
            return trvanie;
        }

        public void setTrvanie(Integer trvanie) {
            this.trvanie = trvanie;
        }
    }
}

