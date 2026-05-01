package sk.ditec.crd.testcrd;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.generated.tsi.LocationFileDatasetMessage;
import sk.ditec.crdexp.ws.OutboundConnectorService;
import sk.ditec.crdexp.ws.OutboundConnectorService_Service;
import sk.ditec.crdexp.ws.SendOutboundMessage;
import sk.ditec.crdexp.ws.SendOutboundMessageResponse;
import sk.ditec.cud.utils._CudConsts;

import com.sun.xml.ws.developer.WSBindingProvider;

public class TestCrdSluzbyServlet extends HttpServlet {

	// static Logger log = LoggerFactory.getLogger(TestCrdSluzbyServlet.class);

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		processAction(req, resp);
	}

	private void processAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html;charset=UTF-8");
		PrintWriter w = resp.getWriter();

		w.println("start");

			

			String urlString = "http://location01service-zsr-ipaas.apps.ocptest.intra.zsr.sk/service/location01service?wsdl";
			URL url = new URL(urlString);
			if (!StringUtils.isValid(url)) {
				w.println("WSDL URL pre CRD nenacitana! ");
				return;
			}
			

			//
			//
			// // JAXBContext context = JAXBContext.newInstance(SendOutboundMessage.class);
			// // Marshaller m = context.createMarshaller();
			// // m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// // StringWriter sw = new StringWriter();
			// // m.marshal(dataSend, sw);
			// // String xml = sw.toString();
			//
			//
			// SendOutboundMessage message = new SendOutboundMessage();
			// // nastavte polia objektu message


			urlString = "https://location01service-zsr-ipaas.apps.ocptest.intra.zsr.sk/service/location01service?wsdl";
			url = new URL(urlString);
			QName qname = new QName("http://uic.cc.org/li/messageprocessing/outbound/", "OutboundConnectorService");
			// SendOutboundMessageResponse ret;
			// service.sendOutboundMessage(dataSend, true); // vynimka null pointer

			String stringXmlReceiptConfirmationMessage =

			" <ns1:LocationFileDatasetMessage xmlns:ns1=\"http://www.era.europa.eu/schemes/TAFTSI/3.2\"> "
					+ "  <ns1:MessageHeader>"
					+ "  <ns1:MessageReference>"
					+ "    <ns1:MessageType>6002</ns1:MessageType>"
					+ "   <ns1:MessageTypeVersion>3.2.0.0</ns1:MessageTypeVersion>"
					+ "   <ns1:MessageIdentifier>f14e8a2d-d409-4dc9-b3de-e6e99aff45d4</ns1:MessageIdentifier>"
					+ "   <ns1:MessageDateTime>2025-10-02T09:11:54.654+02:00</ns1:MessageDateTime>"
					+ "  </ns1:MessageReference>"
					+ " <ns1:Sender ns1:CI_InstanceNumber=\"1\">0056</ns1:Sender>"
					+ "  <ns1:Recipient ns1:CI_InstanceNumber=\"1\">3178</ns1:Recipient>"
					+ " </ns1:MessageHeader>"
					+ "  <ns1:MessageStatus>1</ns1:MessageStatus>"
					+ "  <ns1:CountryCodeISO>SK</ns1:CountryCodeISO>"
					+ "  <ns1:LocationPrimaryCode>99945</ns1:LocationPrimaryCode>"
					+ "  <ns1:LocationPrimaryInformation>"
					+ "   <ns1:LocationPrimaryName xsi:type=\"xs:string\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" "
					+ "	xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">Test_44_Prim</ns1:LocationPrimaryName> "
					+ "   <ns1:ResponsibleIM>0056</ns1:ResponsibleIM> "
					+ "   <ns1:PrimaryLocationNameASCII>Test_44_Prim</ns1:PrimaryLocationNameASCII> "
					+ "   <ns1:LocationValidityPeriod>" + "      <ns1:StartDate>2025-10-15</ns1:StartDate>"
					+ "   </ns1:LocationValidityPeriod>" + "  <ns1:FreightFlag>false</ns1:FreightFlag>"
					+ "   <ns1:PassengerFlag>false</ns1:PassengerFlag>" + " </ns1:LocationPrimaryInformation>"
					+ " </ns1:LocationFileDatasetMessage>";

		String WSDL_TCR = "location01service?wsdl";
		// OutboundConnectorService_Service tcrOutboundService = new OutboundConnectorService_Service(
		// ApplicationContext.getResource(WSDL_TCR));
		OutboundConnectorService_Service stub = new OutboundConnectorService_Service(url, qname);
		OutboundConnectorService tcrOutboundServicePort = stub.getOutboundConnectorServicePort();
		// WSBindingProvider bindingProvider = (WSBindingProvider) tcrOutboundServicePort;
		// bindingProvider.setAddress(urlString);
		// Binding binding = bindingProvider.getBinding();
		// List<Handler> handlerChain = binding.getHandlerChain();
		// handlerChain.add(new CdataSoapHandler());
		// binding.setHandlerChain(handlerChain);
		try {
			JAXBContext context = JAXBContext.newInstance(LocationFileDatasetMessage.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			StringReader reader = new StringReader(stringXmlReceiptConfirmationMessage);
			LocationFileDatasetMessage dtoMess = (LocationFileDatasetMessage) unmarshaller.unmarshal(reader);



		SendOutboundMessage sendOutboundMessage = new SendOutboundMessage();
		// byte[] bytes = stringXmlReceiptConfirmationMessage.getBytes(StandardCharsets.UTF_8);

			//sendOutboundMessage.setMessage(createElementWithCDATA(stringXmlReceiptConfirmationMessage));

			// sendOutboundMessage.setMessage(createElementWithCDATA(_CudConsts.CDATA_START
			// + stringXmlReceiptConfirmationMessage
			// // .replaceAll("<\\?[^>]*\\?>", "")
			// + _CudConsts.CDATA_END));



			// SendOutboundMessageResponse response = null;
			// SOAPFault fault = null;
			// boolean hasFault = false;

			// response = tcrOutboundServicePort.sendOutboundMessage(sendOutboundMessage, false);
			// System.out.println(response.getResponse().toString());

			// LocationFileDatasetMessage mess = new LocationFileDatasetMessage();
			// Recipient rec = new Recipient();
			// rec.setCIInstanceNumber(1);
			// rec.setValue("3178");
			// MessageHeader mh = new MessageHeader();
			// mh.setRecipient(rec);
			// String uuid = java.util.UUID.randomUUID().toString();
			// String value = uuid;
			// MessageReference mr = new MessageReference();
			// mr.setMessageIdentifier(value);
			// mh.setMessageReference(mr);
			// mess.setMessageStatus("test");
			//
			// mess.setMessageHeader(mh);

			// System.out.println(xmlReceiptConfirmationMessage);
			// w.println("xmlReceiptConfirmationMessage:" + xmlReceiptConfirmationMessage);
			// w.println("\n");

			// GetCiselnikDataExport result = new GetCiselnikDataExport();

			// QName qname2 = new QName("http://uic.cc.org/li/messageprocessing/outbound/", "OutboundConnectorService");
			// xmlReceiptConfirmationMessage = "Received XML: " + "<![CDATA[" + xmlReceiptConfirmationMessage + "]]>";
			// xmlReceiptConfirmationMessage = "Received XML: " + xmlReceiptConfirmationMessage;
			// dataSend.setMessage(xmlReceiptConfirmationMessage);
			OutboundConnectorService_Service stub2 = new OutboundConnectorService_Service(url, qname);
			SendOutboundMessage dataSend = new SendOutboundMessage();
			// xmlReceiptConfirmationMessage = "<![CDATA[ Received XML:" + xmlReceiptConfirmationMessage + "]]>";

			// ///////////////////////////////////////////////

			// String innerXml =
			// "<ns1:LocationFileDatasetMessage xmlns:ns1=\"http://www.era.europa.eu/schemes/TAFTSI/3.2\">...</ns1:LocationFileDatasetMessage>";

			// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// DocumentBuilder builder = factory.newDocumentBuilder();
			// Document doc = builder.newDocument();
			//
			// Element messageElement = doc.createElement("Message");
			// CDATASection cdataSection = doc.createCDATASection(stringXmlReceiptConfirmationMessage);
			// messageElement.appendChild(cdataSection);

			// MessageFactory messageFactory = MessageFactory.newInstance();
			// SOAPMessage soapMessage = messageFactory.createMessage();

			// Získanie častí správy
			// SOAPPart soapPart = soapMessage.getSOAPPart();
			// SOAPEnvelope envelope = soapPart.getEnvelope();
			// SOAPBody body = envelope.getBody();
			//
			// // Pridanie vlastného elementu
			// // SOAPElement sendOutboundMessage = body.addChildElement("SendOutboundMessage", "",
			// // "http://www.era.europa.eu/schemes/TAFTSI/3.2");
			// SOAPElement sendOutboundMessage = body.addChildElement("Message");
			//
			// // Vloženie CDATA ako textového uzla
			// String innerXml = stringXmlReceiptConfirmationMessage;
			// sendOutboundMessage.addTextNode("<![CDATA[" + innerXml + "]]>");
			//
			// // Uloženie zmien
			// soapMessage.saveChanges();
			//
			// // Výpis správy do konzoly (voliteľné)
			// soapMessage.writeTo(System.out);

			//
			// SOAPElement sendOutboundMessage = body.addChildElement("SendOutboundMessage");
		// byte[] byteArray = stringXmlReceiptConfirmationMessage.getBytes();

		// String suborMessageTypeVersion = SerializationUtil.getMessageTypeVersion(byteArray);
			// // Vytvorenie CDATA sekci


			OutboundConnectorService_Service stubCdata = new OutboundConnectorService_Service(url, qname);
			OutboundConnectorService port = stubCdata.getOutboundConnectorServicePort();

			// pripojenie handlera

			WSBindingProvider bindingProvider = (WSBindingProvider) port;
			bindingProvider.setAddress(urlString);
			// Binding binding = bindingProvider.getBinding();

			Binding binding = ((BindingProvider) port).getBinding();
			List<Handler> handlerChain = binding.getHandlerChain();
			handlerChain.add(new sk.ditec.cud.procvys.SOAPManipulationHandler());
			// handlerChain.add(new SOAPExternalClientLoggingDBHandler());
			// handlerChain.add(new SOAPLoggingHandler());
			binding.setHandlerChain(handlerChain);


			// DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			// DocumentBuilder builder = factory.newDocumentBuilder();
			// Document doc = builder.parse(new InputSource(new StringReader(stringXmlReceiptConfirmationMessage)));
			// org.w3c.dom.Element element = doc.getDocumentElement();
			//
			// sendOutboundMessage = new SendOutboundMessage();
			// sendOutboundMessage.setMessage(element);

			byte[] messageBytes = stringXmlReceiptConfirmationMessage.getBytes();
		// SendOutboundMessage sendOutboundMessage = new SendOutboundMessage();
			sendOutboundMessage.setMessage(_CudConsts.CDATA_START + "<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
					+ stringXmlReceiptConfirmationMessage
			// new String(messageBytes) // , StandardCharsets.UTF_8)
					+ _CudConsts.CDATA_END);

		// VylConstant.CDATA_START +
		// new String(tcrBinary, StandardCharsets.UTF_8)
		// // .replaceAll("<\\?[^>]*\\?>", "")
				// + VylConstant.CDATA_END


			// SOAPFault fault = null;
			// boolean hasFault = false;
			// SendOutboundMessageResponse response = null;
			//
			SendOutboundMessageResponse response = port.sendOutboundMessage(sendOutboundMessage, true);
			// SendOutboundMessageResponse response = tcrOutboundServicePort.sendOutboundMessage(sendOutboundMessage,
			// false);

			System.out.println(response.getResponse().toString());
			// Pripojenie handlera

			// import javax.xml.ws.Binding;
			// import javax.xml.ws.BindingProvider;
			// import javax.xml.ws.handler.Handler;
			// //import java.util.ArrayList;
			// import java.util.List;

			// Pripojenie handlera
			// Binding binding = ((BindingProvider) port).getBinding();

			System.out.println(response.getResponse().toString());
			w.println(response.getResponse().toString());

		} catch (Throwable e) {
			w.println(e);
			e.printStackTrace();
			// log.error("", e);
		} finally {
			w.flush();
			w.close();
		}
	}



	public static Element createElementWithCDATA(String cdataContent) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();

		// Vytvor koreňový element
		Element root = doc.createElement("Message");

		// Vytvor CDATA sekciu
		CDATASection cdata = doc.createCDATASection(cdataContent);

		// Pripoj CDATA do elementu
		root.appendChild(cdata);

		return root;
	}

	public static Element convertStringToElement(String xmlString) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(new InputSource(new StringReader(xmlString)));
		return document.getDocumentElement();
	}




}
