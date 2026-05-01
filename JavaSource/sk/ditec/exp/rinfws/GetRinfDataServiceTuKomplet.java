
package sk.ditec.exp.rinfws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.ws.AuthInfoWS;


/**
 * <p>Java class for getRinfDataServiceTuKomplet complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getRinfDataServiceTuKomplet">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuthInfoWS" type="{urn:rinf.pis.ws.ditec.sk}authInfoWS" minOccurs="0"/>
 *         &lt;element name="CisloTu" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getRinfDataServiceTuKomplet", propOrder = {
    "authInfoWS",
    "cisloTu"
})
public class GetRinfDataServiceTuKomplet
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AuthInfoWS")
    protected AuthInfoWS authInfoWS;
    @XmlElement(name = "CisloTu")
    protected String cisloTu;

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
     * Gets the value of the cisloTu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCisloTu() {
        return cisloTu;
    }

    /**
     * Sets the value of the cisloTu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCisloTu(String value) {
        this.cisloTu = value;
    }

}
