
package sk.ditec.exp.rinfws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.ws.AuthInfoWS;


/**
 * <p>Java class for getRinfDataServiceSol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getRinfDataServiceSol">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuthInfoWS" type="{urn:rinf.pis.ws.ditec.sk}authInfoWS" minOccurs="0"/>
 *         &lt;element name="OpOd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OpDo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getRinfDataServiceSol", propOrder = {
    "authInfoWS",
    "opOd",
    "opDo"
})
public class GetRinfDataServiceSol
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AuthInfoWS")
    protected AuthInfoWS authInfoWS;
    @XmlElement(name = "OpOd")
    protected String opOd;
    @XmlElement(name = "OpDo")
    protected String opDo;

    /**
     * Gets the value of the authInfoWS property.
     * 
     * @return
     *     possible object is
     *     {@link AuthInfoWS }
     *     
     */
    public AuthInfoWS getAuthInfoWS() {
        return authInfoWS;
    }

    /**
     * Sets the value of the authInfoWS property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthInfoWS }
     *     
     */
    public void setAuthInfoWS(AuthInfoWS value) {
        this.authInfoWS = value;
    }

    /**
     * Gets the value of the opOd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpOd() {
        return opOd;
    }

    /**
     * Sets the value of the opOd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpOd(String value) {
        this.opOd = value;
    }

    /**
     * Gets the value of the opDo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOpDo() {
        return opDo;
    }

    /**
     * Sets the value of the opDo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOpDo(String value) {
        this.opDo = value;
    }

}
