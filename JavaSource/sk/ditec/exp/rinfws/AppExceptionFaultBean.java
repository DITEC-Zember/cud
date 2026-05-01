
package sk.ditec.exp.rinfws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for appExceptionFaultBean complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="appExceptionFaultBean">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sprava" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "appExceptionFaultBean", propOrder = {
    "sprava",
    "subject"
})
public class AppExceptionFaultBean
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    protected String sprava;
    protected String subject;

    /**
     * Gets the value of the sprava property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSprava() {
        return sprava;
    }

    /**
     * Sets the value of the sprava property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSprava(String value) {
        this.sprava = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
    }

}
