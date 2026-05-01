
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
 * Company Information
 * 
 * <p>Java class for Company complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Company">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Company_Name" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255"/>
 *         &lt;element name="Company_Name_ASCII" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255" minOccurs="0"/>
 *         &lt;element name="Company_UIC_Code" type="{http://schema.refdata.li.cc.uic.org/types/v1}CompanyCode"/>
 *         &lt;element name="Company_URL" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-100" minOccurs="0"/>
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}Country"/>
 *         &lt;element name="Start_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime"/>
 *         &lt;element name="End_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime" minOccurs="0"/>
 *         &lt;element name="Company_Short_Name" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-50"/>
 *         &lt;element name="Free_Text" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255" minOccurs="0"/>
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}Contact_Details"/>
 *         &lt;element name="Passenger_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Freight_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Infrastructure_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Other_Company_flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="NE_Entity_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="CE_Entity_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "companyName",
    "companyNameASCII",
    "companyUICCode",
    "companyURL",
    "country",
    "startValidity",
    "endValidity",
    "companyShortName",
    "freeText",
    "contactDetails",
    "passengerFlag",
    "freightFlag",
    "infrastructureFlag",
    "otherCompanyFlag",
    "neEntityFlag",
    "ceEntityFlag",
    "addDate",
    "modifiedDate"
})
@XmlRootElement(name = "Company")
public class Company
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Company_Name", required = true)
    protected String companyName;
    @XmlElement(name = "Company_Name_ASCII", nillable = true)
    protected String companyNameASCII;
    @XmlElement(name = "Company_UIC_Code", required = true)
    protected String companyUICCode;
    @XmlElement(name = "Company_URL", nillable = true)
    protected String companyURL;
    @XmlElement(name = "Country", namespace = "http://schema.refdata.li.cc.uic.org/types/v1", required = true)
    protected Country country;
    @XmlElement(name = "Start_Validity", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date startValidity;
    @XmlElement(name = "End_Validity", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date endValidity;
    @XmlElement(name = "Company_Short_Name", required = true)
    protected String companyShortName;
    @XmlElement(name = "Free_Text", nillable = true)
    protected String freeText;
    @XmlElement(name = "Contact_Details", namespace = "http://schema.refdata.li.cc.uic.org/types/v1", required = true)
    protected ContactDetails contactDetails;
    @XmlElement(name = "Passenger_Flag")
    protected boolean passengerFlag;
    @XmlElement(name = "Freight_Flag")
    protected boolean freightFlag;
    @XmlElement(name = "Infrastructure_Flag")
    protected boolean infrastructureFlag;
    @XmlElement(name = "Other_Company_flag")
    protected boolean otherCompanyFlag;
    @XmlElement(name = "NE_Entity_Flag")
    protected boolean neEntityFlag;
    @XmlElement(name = "CE_Entity_Flag")
    protected boolean ceEntityFlag;
    @XmlElement(name = "Add_Date", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date addDate;
    @XmlElement(name = "Modified_Date", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date modifiedDate;

    /**
     * Gets the value of the companyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Sets the value of the companyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyName(String value) {
        this.companyName = value;
    }

    /**
     * Gets the value of the companyNameASCII property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyNameASCII() {
        return companyNameASCII;
    }

    /**
     * Sets the value of the companyNameASCII property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyNameASCII(String value) {
        this.companyNameASCII = value;
    }

    /**
     * Gets the value of the companyUICCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyUICCode() {
        return companyUICCode;
    }

    /**
     * Sets the value of the companyUICCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyUICCode(String value) {
        this.companyUICCode = value;
    }

    /**
     * Gets the value of the companyURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyURL() {
        return companyURL;
    }

    /**
     * Sets the value of the companyURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyURL(String value) {
        this.companyURL = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link Country }
     *     
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link Country }
     *     
     */
    public void setCountry(Country value) {
        this.country = value;
    }

    /**
     * Gets the value of the startValidity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getStartValidity() {
        return startValidity;
    }

    /**
     * Sets the value of the startValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartValidity(Date value) {
        this.startValidity = value;
    }

    /**
     * Gets the value of the endValidity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getEndValidity() {
        return endValidity;
    }

    /**
     * Sets the value of the endValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndValidity(Date value) {
        this.endValidity = value;
    }

    /**
     * Gets the value of the companyShortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompanyShortName() {
        return companyShortName;
    }

    /**
     * Sets the value of the companyShortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompanyShortName(String value) {
        this.companyShortName = value;
    }

    /**
     * Gets the value of the freeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFreeText() {
        return freeText;
    }

    /**
     * Sets the value of the freeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreeText(String value) {
        this.freeText = value;
    }

    /**
     * Gets the value of the contactDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ContactDetails }
     *     
     */
    public ContactDetails getContactDetails() {
        return contactDetails;
    }

    /**
     * Sets the value of the contactDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactDetails }
     *     
     */
    public void setContactDetails(ContactDetails value) {
        this.contactDetails = value;
    }

    /**
     * Gets the value of the passengerFlag property.
     * 
     */
    public boolean isPassengerFlag() {
        return passengerFlag;
    }

    /**
     * Sets the value of the passengerFlag property.
     * 
     */
    public void setPassengerFlag(boolean value) {
        this.passengerFlag = value;
    }

    /**
     * Gets the value of the freightFlag property.
     * 
     */
    public boolean isFreightFlag() {
        return freightFlag;
    }

    /**
     * Sets the value of the freightFlag property.
     * 
     */
    public void setFreightFlag(boolean value) {
        this.freightFlag = value;
    }

    /**
     * Gets the value of the infrastructureFlag property.
     * 
     */
    public boolean isInfrastructureFlag() {
        return infrastructureFlag;
    }

    /**
     * Sets the value of the infrastructureFlag property.
     * 
     */
    public void setInfrastructureFlag(boolean value) {
        this.infrastructureFlag = value;
    }

    /**
     * Gets the value of the otherCompanyFlag property.
     * 
     */
    public boolean isOtherCompanyFlag() {
        return otherCompanyFlag;
    }

    /**
     * Sets the value of the otherCompanyFlag property.
     * 
     */
    public void setOtherCompanyFlag(boolean value) {
        this.otherCompanyFlag = value;
    }

    /**
     * Gets the value of the neEntityFlag property.
     * 
     */
    public boolean isNEEntityFlag() {
        return neEntityFlag;
    }

    /**
     * Sets the value of the neEntityFlag property.
     * 
     */
    public void setNEEntityFlag(boolean value) {
        this.neEntityFlag = value;
    }

    /**
     * Gets the value of the ceEntityFlag property.
     * 
     */
    public boolean isCEEntityFlag() {
        return ceEntityFlag;
    }

    /**
     * Sets the value of the ceEntityFlag property.
     * 
     */
    public void setCEEntityFlag(boolean value) {
        this.ceEntityFlag = value;
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
