package sk.ditec.cud.procvys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

/*
 * Logovanie SOAP sprav
 */
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {


    private Logger log = LoggerFactory.getLogger(SOAPLoggingHandler.class);

    public Set<QName> getHeaders() {
        return null;
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        logToSystem(smc);
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        logToSystem(smc);
        return true;
    }

    // nothing to clean up
    public void close(MessageContext messageContext) {
        Integer code = (Integer)messageContext.get(MessageContext.HTTP_RESPONSE_CODE);
        HttpServletResponse response = (HttpServletResponse)messageContext.get(MessageContext.SERVLET_RESPONSE);
        String address = (String)messageContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        QName qname = (QName)messageContext.get(MessageContext.WSDL_INTERFACE);
        String service = getAction(qname);

        if (code == 0 && response == null) {
            log.debug("Detekovana chyba: RESPONSE_CODE je " + code.toString()  +", SERVLET_RESPONSE je prazdny. Zla URL servisu?  ENDPOINT_ADDRESS_PROPERTY je " + address);
        } else {
            log.debug("RESPONSE_CODE je " + code.toString());
        }
    }

    /*
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context
     * to see if this is an outgoing or incoming message.
     * Write a brief message to the print stream and
     * output the message. The writeTo() method can throw
     * SOAPException or IOException
     */
    private void logToSystem(SOAPMessageContext messageContext) {
        Boolean outboundProperty = (Boolean)messageContext.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        String address = (String)messageContext.get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        QName qname = (QName)messageContext.get(MessageContext.WSDL_INTERFACE);
        String service = getAction(qname);

        qname = (QName)messageContext.get(MessageContext.WSDL_OPERATION);
        String operation = getAction(qname);

        SOAPMessage message = messageContext.getMessage();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            message.writeTo(out);

            int limit = 10240; // maximalna velkost pola, ktore spracovavame cele

            String soapString;
            if (out.size() > limit) {
                soapString = new String(out.toByteArray(), 0, Math.min(out.size(), limit), "UTF-8");
                soapString = soapString.replaceAll("\\s+", "");
                soapString += "... (prekroceny limit " + limit +  ")";
            } else {
//                soapString = prettyFormat(out.toString("UTF-8"));
                soapString = out.toString("UTF-8");
            }

            if (address == null) {
                if (outboundProperty) {
                    log.debug("Odchadzajuca sprava z WS EndPoint pre operaciu " + service + "/" + operation + "\n" + soapString);
                } else {
                    log.debug("Prichadzajuca sprava na WS EndPoint pre operaciu " + service + "/" + operation + "\n" + soapString);
                }
            } else {
                if (outboundProperty) {
                    log.debug("Odchadzajuca sprava do WS " + address + "\n" + soapString);
                } else {
                    log.debug("Prichadzajuca sprava z WS: " + address+ "\n" + soapString);
                }
            }
        } catch (Throwable e) {
            log.debug("Pri logovani sa vyskytla chyba: ", e);
        }
    }

    private String getAction(QName qname) {
        if (qname == null) {
            return "unknown";
        }

        return qname.getLocalPart();
    }


    public String prettyFormat(String input, int indent) {
        try {
            Source xmlInput = new StreamSource(new StringReader(input));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", indent);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(xmlInput, xmlOutput);
            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            log.debug("Nepodarilo sa naformatiovat XML, tento bude prezentovany bez formatovania.", e);
            return input;
        }
    }

    public String prettyFormat(String input) {
        return prettyFormat(input, 2);
    }
}