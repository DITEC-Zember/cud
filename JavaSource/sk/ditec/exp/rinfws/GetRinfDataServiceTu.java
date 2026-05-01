
package sk.ditec.exp.rinfws;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.ws.AuthInfoWS;


/**
 * <p>Java class for getRinfDataServiceTu complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getRinfDataServiceTu">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AuthInfoWS" type="{urn:rinf.pis.ws.ditec.sk}authInfoWS" minOccurs="0"/>
 *         &lt;element name="IdCskmdOdOpOd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IdCskmdOdOpDo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "getRinfDataServiceTu", propOrder = {
    "authInfoWS",
    "idCskmdOdOpOd",
    "idCskmdOdOpDo",
    "cisloTu"
})
public class GetRinfDataServiceTu
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "AuthInfoWS")
    protected AuthInfoWS authInfoWS;
    @XmlElement(name = "IdCskmdOdOpOd")
    protected String idCskmdOdOpOd;
    @XmlElement(name = "IdCskmdOdOpDo")
    protected String idCskmdOdOpDo;
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
     * Gets the value of the idCskmdOdOpOd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCskmdOdOpOd() {
        return idCskmdOdOpOd;
    }

    /**
     * Sets the value of the idCskmdOdOpOd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCskmdOdOpOd(String value) {
        this.idCskmdOdOpOd = value;
    }

    /**
     * Gets the value of the idCskmdOdOpDo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCskmdOdOpDo() {
        return idCskmdOdOpDo;
    }

    /**
     * Sets the value of the idCskmdOdOpDo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCskmdOdOpDo(String value) {
        this.idCskmdOdOpDo = value;
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
