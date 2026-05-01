
package sk.ditec.crdexp.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sk.ditec.crdexp.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Encoded_QNAME = new QName("http://uic.cc.org/li/messageprocessing/header", "Encoded");
    private final static QName _SendOutboundMessageResponse_QNAME = new QName("http://uic.cc.org/li/messageprocessing/outbound/", "SendOutboundMessageResponse");
    private final static QName _SendOutboundMessage_QNAME = new QName("http://uic.cc.org/li/messageprocessing/outbound/", "SendOutboundMessage");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sk.ditec.crdexp.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendOutboundMessage }
     * 
     */
    public SendOutboundMessage createSendOutboundMessage() {
        return new SendOutboundMessage();
    }

    /**
     * Create an instance of {@link SendOutboundMessageResponse }
     * 
     */
    public SendOutboundMessageResponse createSendOutboundMessageResponse() {
        return new SendOutboundMessageResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uic.cc.org/li/messageprocessing/header", name = "Encoded")
    public JAXBElement<Boolean> createEncoded(Boolean value) {
        return new JAXBElement<Boolean>(_Encoded_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOutboundMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uic.cc.org/li/messageprocessing/outbound/", name = "SendOutboundMessageResponse")
    public JAXBElement<SendOutboundMessageResponse> createSendOutboundMessageResponse(SendOutboundMessageResponse value) {
        return new JAXBElement<SendOutboundMessageResponse>(_SendOutboundMessageResponse_QNAME, SendOutboundMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendOutboundMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://uic.cc.org/li/messageprocessing/outbound/", name = "SendOutboundMessage")
    public JAXBElement<SendOutboundMessage> createSendOutboundMessage(SendOutboundMessage value) {
        return new JAXBElement<SendOutboundMessage>(_SendOutboundMessage_QNAME, SendOutboundMessage.class, null, value);
    }

}
