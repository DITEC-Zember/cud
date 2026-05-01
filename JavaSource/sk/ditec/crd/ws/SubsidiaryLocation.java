
package sk.ditec.crd.ws;

import java.io.Serializable;
import java.math.BigDecimal;
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
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}CountryCodeISO" minOccurs="0"/>
 *         &lt;element name="Responsible_IM_Code" type="{http://schema.refdata.li.cc.uic.org/types/v1}CompanyCode" minOccurs="0"/>
 *         &lt;element name="Subsidiary_Location_Code" type="{http://schema.refdata.li.cc.uic.org/types/v1}String1-10"/>
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}Primary_Location"/>
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}SubsidiaryType"/>
 *         &lt;element name="Subsidiary_Location_Name" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="Start_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime"/>
 *         &lt;element name="End_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime" minOccurs="0"/>
 *         &lt;element name="AllocationCompany" type="{http://schema.refdata.li.cc.uic.org/types/v1}Company"/>
 *         &lt;element name="Longitude" type="{http://schema.refdata.li.cc.uic.org/types/v1}Decimal9-6" minOccurs="0"/>
 *         &lt;element name="Latitude" type="{http://schema.refdata.li.cc.uic.org/types/v1}Decimal8-6" minOccurs="0"/>
 *         &lt;element name="Free_Text" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255" minOccurs="0"/>
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
    "responsibleIMCode",
    "subsidiaryLocationCode",
    "primaryLocation",
    "subsidiaryType",
    "subsidiaryLocationName",
    "startValidity",
    "endValidity",
    "allocationCompany",
    "longitude",
    "latitude",
    "freeText",
    "addDate",
    "modifiedDate"
})
@XmlRootElement(name = "Subsidiary_Location")
public class SubsidiaryLocation
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CountryCodeISO", namespace = "http://schema.refdata.li.cc.uic.org/types/v1")
    protected CountryCodeISO countryCodeISO;
    @XmlElement(name = "Responsible_IM_Code", nillable = true)
    protected String responsibleIMCode;
    @XmlElement(name = "Subsidiary_Location_Code", required = true)
    protected String subsidiaryLocationCode;
    @XmlElement(name = "Primary_Location", namespace = "http://schema.refdata.li.cc.uic.org/types/v1", required = true)
    protected PrimaryLocation primaryLocation;
    @XmlElement(name = "SubsidiaryType", namespace = "http://schema.refdata.li.cc.uic.org/types/v1", required = true)
    protected SubsidiaryType subsidiaryType;
    @XmlElement(name = "Subsidiary_Location_Name", required = true)
	protected String subsidiaryLocationName;
    @XmlElement(name = "Start_Validity", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date startValidity;
    @XmlElement(name = "End_Validity", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date endValidity;
    @XmlElement(name = "AllocationCompany", required = true)
    protected Company allocationCompany;
    @XmlElement(name = "Longitude", nillable = true)
    protected BigDecimal longitude;
    @XmlElement(name = "Latitude", nillable = true)
    protected BigDecimal latitude;
    @XmlElement(name = "Free_Text", nillable = true)
    protected String freeText;
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
     * Gets the value of the responsibleIMCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponsibleIMCode() {
        return responsibleIMCode;
    }

    /**
     * Sets the value of the responsibleIMCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponsibleIMCode(String value) {
        this.responsibleIMCode = value;
    }

    /**
     * Gets the value of the subsidiaryLocationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubsidiaryLocationCode() {
        return subsidiaryLocationCode;
    }

    /**
     * Sets the value of the subsidiaryLocationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubsidiaryLocationCode(String value) {
        this.subsidiaryLocationCode = value;
    }

    /**
     * Gets the value of the primaryLocation property.
     * 
     * @return
     *     possible object is
     *     {@link PrimaryLocation }
     *     
     */
    public PrimaryLocation getPrimaryLocation() {
        return primaryLocation;
    }

    /**
     * Sets the value of the primaryLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrimaryLocation }
     *     
     */
    public void setPrimaryLocation(PrimaryLocation value) {
        this.primaryLocation = value;
    }

    /**
     * Gets the value of the subsidiaryType property.
     * 
     * @return
     *     possible object is
     *     {@link SubsidiaryType }
     *     
     */
    public SubsidiaryType getSubsidiaryType() {
        return subsidiaryType;
    }

    /**
     * Sets the value of the subsidiaryType property.
     * 
     * @param value
     *     allowed object is
     *     {@link SubsidiaryType }
     *     
     */
    public void setSubsidiaryType(SubsidiaryType value) {
        this.subsidiaryType = value;
    }

    /**
     * Gets the value of the subsidiaryLocationName property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
	public String getSubsidiaryLocationName() {
        return subsidiaryLocationName;
    }

    /**
     * Sets the value of the subsidiaryLocationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
	public void setSubsidiaryLocationName(String value) {
        this.subsidiaryLocationName = value;
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
     * Gets the value of the allocationCompany property.
     * 
     * @return
     *     possible object is
     *     {@link Company }
     *     
     */
    public Company getAllocationCompany() {
        return allocationCompany;
    }

    /**
     * Sets the value of the allocationCompany property.
     * 
     * @param value
     *     allowed object is
     *     {@link Company }
     *     
     */
    public void setAllocationCompany(Company value) {
        this.allocationCompany = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLongitude(BigDecimal value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setLatitude(BigDecimal value) {
        this.latitude = value;
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
