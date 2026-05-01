
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
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}Country"/>
 *         &lt;element name="Location_Code" type="{http://schema.refdata.li.cc.uic.org/types/v1}String1-5"/>
 *         &lt;element name="Start_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime"/>
 *         &lt;element name="End_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime" minOccurs="0"/>
 *         &lt;element name="ResponsibleIM" type="{http://schema.refdata.li.cc.uic.org/types/v1}Company"/>
 *         &lt;element name="Location_Name" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255"/>
 *         &lt;element name="Location_Name_ASCII" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255"/>
 *         &lt;element name="NUTS_Code" type="{http://schema.refdata.li.cc.uic.org/types/v1}String5" minOccurs="0"/>
 *         &lt;element name="Container_Handling_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Handover_Point_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Freight_Possible_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Freight_Start_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime" minOccurs="0"/>
 *         &lt;element name="Freight_End_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime" minOccurs="0"/>
 *         &lt;element name="Passenger_Possible_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Passenger_Start_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime" minOccurs="0"/>
 *         &lt;element name="Passenger_End_Validity" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime" minOccurs="0"/>
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
    "country",
    "locationCode",
    "startValidity",
    "endValidity",
    "responsibleIM",
    "locationName",
    "locationNameASCII",
    "nutsCode",
    "containerHandlingFlag",
    "handoverPointFlag",
    "freightPossibleFlag",
    "freightStartValidity",
    "freightEndValidity",
    "passengerPossibleFlag",
    "passengerStartValidity",
    "passengerEndValidity",
    "longitude",
    "latitude",
    "freeText",
    "addDate",
    "modifiedDate"
})
@XmlRootElement(name = "Primary_Location")
public class PrimaryLocation
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Country", namespace = "http://schema.refdata.li.cc.uic.org/types/v1", required = true)
    protected Country country;
    @XmlElement(name = "Location_Code", required = true)
    protected String locationCode;
    @XmlElement(name = "Start_Validity", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date startValidity;
    @XmlElement(name = "End_Validity", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date endValidity;
    @XmlElement(name = "ResponsibleIM", required = true)
    protected Company responsibleIM;
    @XmlElement(name = "Location_Name", required = true)
    protected String locationName;
    @XmlElement(name = "Location_Name_ASCII", required = true)
    protected String locationNameASCII;
    @XmlElement(name = "NUTS_Code", nillable = true)
    protected String nutsCode;
    @XmlElement(name = "Container_Handling_Flag", nillable = true)
    protected Boolean containerHandlingFlag;
    @XmlElement(name = "Handover_Point_Flag", nillable = true)
    protected Boolean handoverPointFlag;
    @XmlElement(name = "Freight_Possible_Flag", nillable = true)
    protected Boolean freightPossibleFlag;
    @XmlElement(name = "Freight_Start_Validity", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date freightStartValidity;
    @XmlElement(name = "Freight_End_Validity", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date freightEndValidity;
    @XmlElement(name = "Passenger_Possible_Flag", nillable = true)
    protected Boolean passengerPossibleFlag;
    @XmlElement(name = "Passenger_Start_Validity", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date passengerStartValidity;
    @XmlElement(name = "Passenger_End_Validity", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date passengerEndValidity;
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
     * Gets the value of the locationCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationCode() {
        return locationCode;
    }

    /**
     * Sets the value of the locationCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationCode(String value) {
        this.locationCode = value;
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
     * Gets the value of the responsibleIM property.
     * 
     * @return
     *     possible object is
     *     {@link Company }
     *     
     */
    public Company getResponsibleIM() {
        return responsibleIM;
    }

    /**
     * Sets the value of the responsibleIM property.
     * 
     * @param value
     *     allowed object is
     *     {@link Company }
     *     
     */
    public void setResponsibleIM(Company value) {
        this.responsibleIM = value;
    }

    /**
     * Gets the value of the locationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationName() {
        return locationName;
    }

    /**
     * Sets the value of the locationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationName(String value) {
        this.locationName = value;
    }

    /**
     * Gets the value of the locationNameASCII property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationNameASCII() {
        return locationNameASCII;
    }

    /**
     * Sets the value of the locationNameASCII property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationNameASCII(String value) {
        this.locationNameASCII = value;
    }

    /**
     * Gets the value of the nutsCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNUTSCode() {
        return nutsCode;
    }

    /**
     * Sets the value of the nutsCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNUTSCode(String value) {
        this.nutsCode = value;
    }

    /**
     * Gets the value of the containerHandlingFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isContainerHandlingFlag() {
        return containerHandlingFlag;
    }

    /**
     * Sets the value of the containerHandlingFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setContainerHandlingFlag(Boolean value) {
        this.containerHandlingFlag = value;
    }

    /**
     * Gets the value of the handoverPointFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHandoverPointFlag() {
        return handoverPointFlag;
    }

    /**
     * Sets the value of the handoverPointFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHandoverPointFlag(Boolean value) {
        this.handoverPointFlag = value;
    }

    /**
     * Gets the value of the freightPossibleFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isFreightPossibleFlag() {
        return freightPossibleFlag;
    }

    /**
     * Sets the value of the freightPossibleFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setFreightPossibleFlag(Boolean value) {
        this.freightPossibleFlag = value;
    }

    /**
     * Gets the value of the freightStartValidity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getFreightStartValidity() {
        return freightStartValidity;
    }

    /**
     * Sets the value of the freightStartValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreightStartValidity(Date value) {
        this.freightStartValidity = value;
    }

    /**
     * Gets the value of the freightEndValidity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getFreightEndValidity() {
        return freightEndValidity;
    }

    /**
     * Sets the value of the freightEndValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFreightEndValidity(Date value) {
        this.freightEndValidity = value;
    }

    /**
     * Gets the value of the passengerPossibleFlag property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPassengerPossibleFlag() {
        return passengerPossibleFlag;
    }

    /**
     * Sets the value of the passengerPossibleFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPassengerPossibleFlag(Boolean value) {
        this.passengerPossibleFlag = value;
    }

    /**
     * Gets the value of the passengerStartValidity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getPassengerStartValidity() {
        return passengerStartValidity;
    }

    /**
     * Sets the value of the passengerStartValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassengerStartValidity(Date value) {
        this.passengerStartValidity = value;
    }

    /**
     * Gets the value of the passengerEndValidity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getPassengerEndValidity() {
        return passengerEndValidity;
    }

    /**
     * Sets the value of the passengerEndValidity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassengerEndValidity(Date value) {
        this.passengerEndValidity = value;
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
