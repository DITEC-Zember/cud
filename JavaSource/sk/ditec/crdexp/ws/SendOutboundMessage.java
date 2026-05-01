
package sk.ditec.crdexp.ws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>
 * Java class for SendOutboundMessage complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SendOutboundMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Message" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * Ocakavany je serializovana struktura v CDATA sekcii alebo ako escapovany retazec
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SendOutboundMessage", propOrder = {
    "message"
})

public class SendOutboundMessage
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Message")
	protected String message;

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
	public String getMessage() {
        return message;
    }

    /**
	 * Sets the value of the message property.
	 * 
	 * @param messageElement
	 *            allowed object is {@link Object }
	 * 
	 */
	public void setMessage(String value) {
		this.message = value;
	}

}
