
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
 *         &lt;element name="Subsidiary_Type_Code" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-2"/>
 *         &lt;element name="Subsidiary_Type_Name" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255"/>
 *         &lt;element name="IM_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Freight_RU_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Passenger_RU_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Central_Entity_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="National_Entity_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Others_Flag" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "subsidiaryTypeCode",
    "subsidiaryTypeName",
    "imFlag",
    "freightRUFlag",
    "passengerRUFlag",
    "centralEntityFlag",
    "nationalEntityFlag",
    "othersFlag",
    "freeText",
    "addDate",
    "modifiedDate"
})
@XmlRootElement(name = "SubsidiaryType")
public class SubsidiaryType
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Subsidiary_Type_Code", required = true)
    protected String subsidiaryTypeCode;
    @XmlElement(name = "Subsidiary_Type_Name", required = true)
    protected String subsidiaryTypeName;
    @XmlElement(name = "IM_Flag")
    protected boolean imFlag;
    @XmlElement(name = "Freight_RU_Flag")
    protected boolean freightRUFlag;
    @XmlElement(name = "Passenger_RU_Flag")
    protected boolean passengerRUFlag;
    @XmlElement(name = "Central_Entity_Flag")
    protected boolean centralEntityFlag;
    @XmlElement(name = "National_Entity_Flag")
    protected boolean nationalEntityFlag;
    @XmlElement(name = "Others_Flag")
    protected boolean othersFlag;
    @XmlElement(name = "Free_Text", nillable = true)
    protected String freeText;
    @XmlElement(name = "Add_Date", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date addDate;
    @XmlElement(name = "Modified_Date", type = String.class, nillable = true)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date modifiedDate;

    /**
     * Gets the value of the subsidiaryTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubsidiaryTypeCode() {
        return subsidiaryTypeCode;
    }

    /**
     * Sets the value of the subsidiaryTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubsidiaryTypeCode(String value) {
        this.subsidiaryTypeCode = value;
    }

    /**
     * Gets the value of the subsidiaryTypeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubsidiaryTypeName() {
        return subsidiaryTypeName;
    }

    /**
     * Sets the value of the subsidiaryTypeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubsidiaryTypeName(String value) {
        this.subsidiaryTypeName = value;
    }

    /**
     * Gets the value of the imFlag property.
     * 
     */
    public boolean isIMFlag() {
        return imFlag;
    }

    /**
     * Sets the value of the imFlag property.
     * 
     */
    public void setIMFlag(boolean value) {
        this.imFlag = value;
    }

    /**
     * Gets the value of the freightRUFlag property.
     * 
     */
    public boolean isFreightRUFlag() {
        return freightRUFlag;
    }

    /**
     * Sets the value of the freightRUFlag property.
     * 
     */
    public void setFreightRUFlag(boolean value) {
        this.freightRUFlag = value;
    }

    /**
     * Gets the value of the passengerRUFlag property.
     * 
     */
    public boolean isPassengerRUFlag() {
        return passengerRUFlag;
    }

    /**
     * Sets the value of the passengerRUFlag property.
     * 
     */
    public void setPassengerRUFlag(boolean value) {
        this.passengerRUFlag = value;
    }

    /**
     * Gets the value of the centralEntityFlag property.
     * 
     */
    public boolean isCentralEntityFlag() {
        return centralEntityFlag;
    }

    /**
     * Sets the value of the centralEntityFlag property.
     * 
     */
    public void setCentralEntityFlag(boolean value) {
        this.centralEntityFlag = value;
    }

    /**
     * Gets the value of the nationalEntityFlag property.
     * 
     */
    public boolean isNationalEntityFlag() {
        return nationalEntityFlag;
    }

    /**
     * Sets the value of the nationalEntityFlag property.
     * 
     */
    public void setNationalEntityFlag(boolean value) {
        this.nationalEntityFlag = value;
    }

    /**
     * Gets the value of the othersFlag property.
     * 
     */
    public boolean isOthersFlag() {
        return othersFlag;
    }

    /**
     * Sets the value of the othersFlag property.
     * 
     */
    public void setOthersFlag(boolean value) {
        this.othersFlag = value;
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
