package sk.ditec.cud.master;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.Base64;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;

public class VysielanieTestClass {

	static Logger log = LoggerFactory.getLogger(VysielanieClass.class);

	private static String postDataToUrl(String targetURL, String login, String pass, byte[] data) throws Exception {

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

			return response.toString();

		} catch (Exception e) {
			log.error("postDataToUrl.error", e);
			return null;

		} finally {

			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public static void main(String[] args) throws AppException {

		try {
			DBUtils.init();

			String xml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:ws.cud.zsr.ditec.sk\">";
			xml += "<soapenv:Header/>";
			xml += "<soapenv:Body>";
			xml += "<urn:getCiselnikMetaList>";
			xml += "</urn:getCiselnikMetaList>";
			xml += "</soapenv:Body>";
			xml += "</soapenv:Envelope>";

			String response = postDataToUrl("http://kist.intra.zsr.sk/cud/CudDataWS?wsdl", "PIS_COMM", "PIS_COMM", xml.getBytes("UTF-8"));
			log.info(response);

			byte[] bytes = response.getBytes("UTF-8");
			InputStream stream = new ByteArrayInputStream(bytes);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(stream);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("return"); // <ns2:getCiselnikMetaListResponse
			log.info("" + nodeList.getLength());

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;

					// 3. Získanie textového obsahu vnútorných elementov podľa názvu tagu
					String ciselnikID = element.getElementsByTagName("ciselnikID").item(0).getTextContent();
					String nazov = element.getElementsByTagName("nazov").item(0).getTextContent();
					String tabulka = element.getElementsByTagName("tabulka").item(0).getTextContent();
					// String popis = element.getElementsByTagName("popis").item(0).getTextContent();

					System.out.print("ciselnikID ID: " + ciselnikID);
					System.out.print("  nazov: " + nazov);
					System.out.println("  tabulka: " + tabulka);
					// System.out.println("  popis: " + popis);
				}
			}

		} catch (Exception e) {
			DBUtils.handleException(e, "Exception");
		}

		try {
			String xmlRequest = FrameworkUtils.getConfigProperty("cud", "kontrola.ws.csMetaList");
			xmlRequest = StringUtils.replaceAll(xmlRequest, "{ciselnikID}", "25");
			xmlRequest = StringUtils.replaceAll(xmlRequest, "{datum}", "2025-11-05");

			String xmlResponse = postDataToUrl("http://kist.intra.zsr.sk/cud/CudDataWS?wsdl", "PIS_COMM", "PIS_COMM", xmlRequest.getBytes("UTF-8"));
			log.info(xmlResponse);

			byte[] bytes = xmlResponse.getBytes("UTF-8");
			InputStream stream = new ByteArrayInputStream(bytes);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(stream);
			doc.getDocumentElement().normalize();

			NodeList nodeList = doc.getElementsByTagName("return"); // <ns2:getCiselnikMetaListResponse
			log.info("" + nodeList.getLength());

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element element = (Element) node;

					// 3. Získanie textového obsahu vnútorných elementov podľa názvu tagu
					String nazov = element.getElementsByTagName("nazov").item(0).getTextContent();
					String dbTyp = element.getElementsByTagName("dbTyp").item(0).getTextContent();
					String dlzka = element.getElementsByTagName("dlzka").item(0).getTextContent();
					// String popis = element.getElementsByTagName("popis").item(0).getTextContent();

					System.out.print("nazov: " + nazov);
					System.out.print("  dbTyp: " + dbTyp);
					System.out.println("  dlzka: " + dlzka);
					// System.out.println("  popis: " + popis);
				}
			}

		} catch (Exception e) {
			DBUtils.handleException(e, "Exception");
		}

	}

}
