package sk.ditec.cud.procvys;

import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;

/**
 * Sluzi na manipulaciu s odchadzajucou spravou
 * Zmeny su testovane pre JAVA 8 u202 a Tomcat 6
 * Implemenovane:
 * - ak element obsahuje text <![CDATA[ tak je modifikovany aby obsahoval CDATASection sekciu, tym sa efektivne zabrani escapovaniu CDATA pri finalnej serializacii
 */
public class SOAPManipulationHandler implements SOAPHandler<SOAPMessageContext> {
    private Logger log = LoggerFactory.getLogger(SOAPLoggingHandler.class);

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        try {
            Boolean outboundProperty = (Boolean)messageContext.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);
            if (outboundProperty) { // ak je odchadzajuca sprava, manipuju s nou a nastav kazdy element s CDATA ako CDATASekciu, ak uz taku nema
                SOAPMessage message = messageContext.getMessage();
                SOAPPart soapPart = message.getSOAPPart();
                Document doc = soapPart.getEnvelope().getOwnerDocument();

                // prejdi cely strom a uprav ho podla zelania
                traverseAndConvertNodes(doc.getDocumentElement());
            }
        } catch (Throwable t) {
            log.error("Chyba pri manipulacii sa CDATA", t);
            throw new RuntimeException(t);
        }
        return true;
    }

    private void traverseAndConvertNodes(Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node child = childNodes.item(i);

                // Preskočiť, ak je to už CDATASection
                if (child.getNodeType() == Node.CDATA_SECTION_NODE) {
                    continue;
                }

                // Ak je to textový uzol a obsahuje CDATA formát
				if (child.getNodeType() == Node.TEXT_NODE && child.getNodeValue().startsWith(_CudConsts.CDATA_START)
						&& child.getNodeValue().endsWith(_CudConsts.CDATA_END)) {
					String cdataContent = CudVysielanieUtils.extractCDataContent(child.getNodeValue());

                    // nastav value ako prazdnu
                    node.setNodeValue("");

                    // pridaj novu CDATASection
                    CDATASection cdataSection = node.getOwnerDocument().createCDATASection(cdataContent);
                    node.replaceChild(cdataSection, child);

                    log.info("prebehla manilupacia s obsahom, bola nastavena ako CDATA sekcia pre " + child.getLocalName());

                } else {
                    // Rekurzívne prechádzajte ďalšie uzly
                    traverseAndConvertNodes(child);
                }
            }
        }
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleFault(SOAPMessageContext context) {
        return true;
    }

    @Override
    public void close(MessageContext context) {

    }
}
