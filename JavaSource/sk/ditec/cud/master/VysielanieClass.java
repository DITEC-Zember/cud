package sk.ditec.cud.master;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.Base64;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudBaseClass;
import sk.ditec.cud.dto.DTOSubor;

public class VysielanieClass extends _CudBaseClass {

	static Logger log = LoggerFactory.getLogger(VysielanieClass.class);

	public static Integer maxLogID() throws AppException {

		AuthInfo auth = AuthInfo.system();

		CallableStatement stmt = null;
		ResultSet rs = null;

		try {

			String sql = "select max(LOG_ID) as max_log_id from COMM_LOG where ws = \'InboundMessage\'";

			getConnection(auth);
			stmt = auth.T.prepareCall(sql);
			rs = stmt.executeQuery();

			Integer logID = null;

			if (rs.next()) {
				logID = rs.getInt("max_log_id");
			}

			cleanUp(stmt, rs);
			returnConnection(auth);

			return logID;

		} catch (Exception e) {
			DBUtils.handleException(e, "Exception", auth);
			cleanUp(stmt, rs);
			returnConnection(auth);
			return null;
		}

	}

	public static List<DTOSubor> list(long minLogID, int maxPocet) throws AppException {

		AuthInfo auth = AuthInfo.system();

		CallableStatement stmt = null;
		ResultSet rs = null;

		try {

			String sql = "select LOG_ID, REQUEST from COMM_LOG where log_id > " + minLogID + " and ws = \'InboundMessage\'  order by log_id asc ";

			sql = "select * from ( " + sql + ") where rownum <= " + maxPocet;

			getConnection(auth);
			stmt = auth.T.prepareCall(sql);
			rs = stmt.executeQuery();

			List<DTOSubor> listDTO = new ArrayList<DTOSubor>();

			while (rs.next()) {

				DTOSubor dtoNew = new DTOSubor();
				dtoNew.setSuborID(rs.getInt("LOG_ID"));
				Clob rc = rs.getClob("REQUEST");
				if (null == rc) {
					dtoNew.setSubor(null);
				} else {
					String record = rc.getSubString(1L, new Long(rc.length()).intValue());
					dtoNew.setSubor(record.getBytes());
				}
				listDTO.add(dtoNew);
			}

			cleanUp(stmt, rs);
			returnConnection(auth);

			return listDTO;

		} catch (Exception e) {
			DBUtils.handleException(e, "Exception", auth);
			cleanUp(stmt, rs);
			returnConnection(auth);
			return null;
		}

	}

	protected static void postDataToUrl(String targetURL, String login, String pass, byte[] data) throws Exception {

		URL url;
		HttpURLConnection connection = null;

		try {

			// if (targetURL.indexOf("novy_zp")>-1){
			// log.debug("breakpoint");
			// }

			// Create connection
			url = new URL(targetURL);
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			connection.setRequestProperty("Connection", "Keep-Alive");

			// connection.setRequestProperty("Accept-Encoding", "gzip,deflate");

			if (StringUtils.isValid(login)) {
				String encoding = Base64.encodeToString((login + ":" + pass).getBytes(), false);
				connection.setRequestProperty("Authorization", "Basic " + encoding);
			}

			connection.setRequestProperty("Content-Length", "" + data.length);

			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);

			// Send request
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.write(data);
			wr.flush();
			wr.close();

			InputStream is = null;
			BufferedReader rd = null;

			if (connection.getResponseCode() == 200) {
				rd = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			} else {
				rd = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
			}

			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();

		} catch (Exception e) {
			log.error("postDataToUrl.error", e);

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static void main(String[] args) throws AppException {

		try {
			int maxPocet = 500;
			DBUtils.init();

			int minLogID = maxLogID();

			List<DTOSubor> dataList = list(minLogID, maxPocet);

			while (true) {

				log.info("Pocet zaznamov na poslanie: {}", dataList.size());

				if (dataList.isEmpty()) {
					try {
						log.debug("Nemam co spracovavat, cakam 5min, nech nevyrabam zataz");
						Thread.sleep(60 * 1000 * 5); // (1)
					} catch (Exception e) {
					}
				}

				int index = 1;
				for (DTOSubor dto : dataList) {
					minLogID = dto.getSuborID();

					log.info("Posielanie spravy {}, ({})", dto.getSuborID(), index++ + "/" + dataList.size());

					Date start = new Date();

					postDataToUrl("http://kiptest.intra.zsr.sk/comm_pis/InboundMessage?wsdl", "PIS_COMM", "PIS_COMM", dto.getSubor());

					// postDataToUrl("http://kist.intra.zsr.sk/trasy/MkHlpPathTTWS?wsdl", null, null, dto.getSubor());
					// postDataToUrl("http://localhost:8095/trasy/MkHlpPathTTWS?wsdl", null, null, dto.getSubor());

					log.info("Trvanie: {}", Math.ceil((new Date().getTime() - start.getTime()) / 1000));
				}

				dataList = list(minLogID, maxPocet);
			}

			// log.info("Nemam co spracovavat, ukoncujem");

		} catch (Exception e) {
			DBUtils.handleException(e, "Exception");
		}
	}

}
