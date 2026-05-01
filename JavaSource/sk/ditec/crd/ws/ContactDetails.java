
package sk.ditec.crd.ws;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="Contact_Person" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255"/>
 *         &lt;element name="Email" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-70" minOccurs="0"/>
 *         &lt;element name="Phone_Number" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-70" minOccurs="0"/>
 *         &lt;element name="FAX_Number" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-70" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-255" minOccurs="0"/>
 *         &lt;element name="City" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-50" minOccurs="0"/>
 *         &lt;element name="Mobile_Number" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-70" minOccurs="0"/>
 *         &lt;element name="Postal_Code" type="{http://schema.refdata.li.cc.uic.org/types/v1}String-10" minOccurs="0"/>
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
    "contactPerson",
    "email",
    "phoneNumber",
    "faxNumber",
    "address",
    "city",
    "mobileNumber",
    "postalCode"
})
@XmlRootElement(name = "Contact_Details")
public class ContactDetails
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Contact_Person", required = true)
    protected String contactPerson;
    @XmlElement(name = "Email", nillable = true)
    protected String email;
    @XmlElement(name = "Phone_Number", nillable = true)
    protected String phoneNumber;
    @XmlElement(name = "FAX_Number", nillable = true)
    protected String faxNumber;
    @XmlElement(name = "Address", nillable = true)
    protected String address;
    @XmlElement(name = "City", nillable = true)
    protected String city;
    @XmlElement(name = "Mobile_Number", nillable = true)
    protected String mobileNumber;
    @XmlElement(name = "Postal_Code", nillable = true)
    protected String postalCode;

    /**
     * Gets the value of the contactPerson property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Sets the value of the contactPerson property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactPerson(String value) {
        this.contactPerson = value;
    }

    /**
     * Gets the value of the email property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the value of the email property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Gets the value of the phoneNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value of the phoneNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    /**
     * Gets the value of the faxNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFAXNumber() {
        return faxNumber;
    }

    /**
     * Sets the value of the faxNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFAXNumber(String value) {
        this.faxNumber = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the mobileNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Sets the value of the mobileNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileNumber(String value) {
        this.mobileNumber = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

}
