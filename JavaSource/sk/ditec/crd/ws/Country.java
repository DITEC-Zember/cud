
package sk.ditec.crd.ws;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}CountryCodeISO"/>
 *         &lt;element name="Country_UIC_Code" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-2" minOccurs="0"/>
 *         &lt;element name="Country_Name_EN" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255"/>
 *         &lt;element name="Country_Name_FR" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255" minOccurs="0"/>
 *         &lt;element name="Country_Name_DE" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255" minOccurs="0"/>
 *         &lt;element name="Sub_Loc_Code_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Add_Date" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime"/>
 *         &lt;element name="Modified_Date" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "countryCodeISO",
    "countryUICCode",
    "countryNameEN",
    "countryNameFR",
    "countryNameDE",
    "subLocCodeFlag",
    "addDate",
    "modifiedDate"
})
@XmlRootElement(name = "Country")
public class Country
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CountryCodeISO", namespace = "http://schema.refdata.li.cc.uic.org/types/v1", required = true)
    protected CountryCodeISO countryCodeISO;
    @XmlElement(name = "Country_UIC_Code", nillable = true)
    protected String countryUICCode;
    @XmlElement(name = "Country_Name_EN", required = true)
    protected String countryNameEN;
    @XmlElement(name = "Country_Name_FR", nillable = true)
    protected String countryNameFR;
    @XmlElement(name = "Country_Name_DE", nillable = true)
    protected String countryNameDE;
    @XmlElement(name = "Sub_Loc_Code_Flag")
    protected boolean subLocCodeFlag;
    @XmlElement(name = "Add_Date", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date addDate;
    @XmlElement(name = "Modified_Date", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date modifiedDate;

    /**
     * Gets the value of the countryCodeISO property.
     * 
     * @return
     *     possible object is
     *     {@link CountryCodeISO }
     *     
     */
    public CountryCodeISO getCountryCodeISO() {
        return countryCodeISO;
    }

    /**
     * Sets the value of the countryCodeISO property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountryCodeISO }
     *     
     */
    public void setCountryCodeISO(CountryCodeISO value) {
        this.countryCodeISO = value;
    }

    /**
     * Gets the value of the countryUICCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryUICCode() {
        return countryUICCode;
    }

    /**
     * Sets the value of the countryUICCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryUICCode(String value) {
        this.countryUICCode = value;
    }

    /**
     * Gets the value of the countryNameEN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryNameEN() {
        return countryNameEN;
    }

    /**
     * Sets the value of the countryNameEN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryNameEN(String value) {
        this.countryNameEN = value;
    }

    /**
     * Gets the value of the countryNameFR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryNameFR() {
        return countryNameFR;
    }

    /**
     * Sets the value of the countryNameFR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryNameFR(String value) {
        this.countryNameFR = value;
    }

    /**
     * Gets the value of the countryNameDE property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountryNameDE() {
        return countryNameDE;
    }

    /**
     * Sets the value of the countryNameDE property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountryNameDE(String value) {
        this.countryNameDE = value;
    }

    /**
     * Gets the value of the subLocCodeFlag property.
     * 
     */
    public boolean isSubLocCodeFlag() {
        return subLocCodeFlag;
    }

    /**
     * Sets the value of the subLocCodeFlag property.
     * 
     */
    public void setSubLocCodeFlag(boolean value) {
        this.subLocCodeFlag = value;
    }

    /**
     * Gets the value of the addDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getAddDate() {
        return addDate;
    }

    /**
     * Sets the value of the addDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddDate(Date value) {
        this.addDate = value;
    }

    /**
     * Gets the value of the modifiedDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getModifiedDate() {
        return modifiedDate;
    }

    /**
     * Sets the value of the modifiedDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModifiedDate(Date value) {
        this.modifiedDate = value;
    }

}
