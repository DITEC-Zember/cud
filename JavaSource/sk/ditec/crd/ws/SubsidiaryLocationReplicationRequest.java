
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
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}CountryCodeISO" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ResponsibleIM" type="{http://schema.refdata.li.cc.uic.org/types/v1}CompanyCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SubsidiaryType" type="{http://schema.refdata.li.cc.uic.org/types/v1}SubsidiaryTypeCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="AllocationCompany" type="{http://schema.refdata.li.cc.uic.org/types/v1}CompanyCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ReplicationVolume" type="{http://schema.refdata.li.cc.uic.org/types/v1}ReplicationVolume" minOccurs="0"/>
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
    "responsibleIM",
    "subsidiaryType",
    "allocationCompany",
    "replicationVolume"
})
@XmlRootElement(name = "SubsidiaryLocationReplicationRequest")
public class SubsidiaryLocationReplicationRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CountryCodeISO", namespace = "http://schema.refdata.li.cc.uic.org/types/v1")
    protected sk.ditec.crd.ws.CountryCodeISO[] countryCodeISO;
    @XmlElement(name = "ResponsibleIM")
    protected String[] responsibleIM;
    @XmlElement(name = "SubsidiaryType")
    protected sk.ditec.crd.ws.SubsidiaryTypeCode[] subsidiaryType;
    @XmlElement(name = "AllocationCompany")
    protected String[] allocationCompany;
    @XmlElement(name = "ReplicationVolume")
    protected ReplicationVolume replicationVolume;

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link sk.ditec.crd.ws.CountryCodeISO }
     *     
     */
    public sk.ditec.crd.ws.CountryCodeISO[] getCountryCodeISO() {
        if (this.countryCodeISO == null) {
            return new sk.ditec.crd.ws.CountryCodeISO[ 0 ] ;
        }
        sk.ditec.crd.ws.CountryCodeISO[] retVal = new sk.ditec.crd.ws.CountryCodeISO[this.countryCodeISO.length] ;
        System.arraycopy(this.countryCodeISO, 0, retVal, 0, this.countryCodeISO.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link sk.ditec.crd.ws.CountryCodeISO }
     *     
     */
    public sk.ditec.crd.ws.CountryCodeISO getCountryCodeISO(int idx) {
        if (this.countryCodeISO == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.countryCodeISO[idx];
    }

    public int getCountryCodeISOLength() {
        if (this.countryCodeISO == null) {
            return  0;
        }
        return this.countryCodeISO.length;
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link sk.ditec.crd.ws.CountryCodeISO }
     *     
     */
    public void setCountryCodeISO(sk.ditec.crd.ws.CountryCodeISO[] values) {
        int len = values.length;
        this.countryCodeISO = ((sk.ditec.crd.ws.CountryCodeISO[]) new sk.ditec.crd.ws.CountryCodeISO[len] );
        for (int i = 0; (i<len); i ++) {
            this.countryCodeISO[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link sk.ditec.crd.ws.CountryCodeISO }
     *     
     */
    public sk.ditec.crd.ws.CountryCodeISO setCountryCodeISO(int idx, sk.ditec.crd.ws.CountryCodeISO value) {
        return this.countryCodeISO[idx] = value;
    }

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link String }
     *     
     */
    public String[] getResponsibleIM() {
        if (this.responsibleIM == null) {
            return new String[ 0 ] ;
        }
        String[] retVal = new String[this.responsibleIM.length] ;
        System.arraycopy(this.responsibleIM, 0, retVal, 0, this.responsibleIM.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link String }
     *     
     */
    public String getResponsibleIM(int idx) {
        if (this.responsibleIM == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.responsibleIM[idx];
    }

    public int getResponsibleIMLength() {
        if (this.responsibleIM == null) {
            return  0;
        }
        return this.responsibleIM.length;
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link String }
     *     
     */
    public void setResponsibleIM(String[] values) {
        int len = values.length;
        this.responsibleIM = ((String[]) new String[len] );
        for (int i = 0; (i<len); i ++) {
            this.responsibleIM[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public String setResponsibleIM(int idx, String value) {
        return this.responsibleIM[idx] = value;
    }

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link sk.ditec.crd.ws.SubsidiaryTypeCode }
     *     
     */
    public sk.ditec.crd.ws.SubsidiaryTypeCode[] getSubsidiaryType() {
        if (this.subsidiaryType == null) {
            return new sk.ditec.crd.ws.SubsidiaryTypeCode[ 0 ] ;
        }
        sk.ditec.crd.ws.SubsidiaryTypeCode[] retVal = new sk.ditec.crd.ws.SubsidiaryTypeCode[this.subsidiaryType.length] ;
        System.arraycopy(this.subsidiaryType, 0, retVal, 0, this.subsidiaryType.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link sk.ditec.crd.ws.SubsidiaryTypeCode }
     *     
     */
    public sk.ditec.crd.ws.SubsidiaryTypeCode getSubsidiaryType(int idx) {
        if (this.subsidiaryType == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.subsidiaryType[idx];
    }

    public int getSubsidiaryTypeLength() {
        if (this.subsidiaryType == null) {
            return  0;
        }
        return this.subsidiaryType.length;
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link sk.ditec.crd.ws.SubsidiaryTypeCode }
     *     
     */
    public void setSubsidiaryType(sk.ditec.crd.ws.SubsidiaryTypeCode[] values) {
        int len = values.length;
        this.subsidiaryType = ((sk.ditec.crd.ws.SubsidiaryTypeCode[]) new sk.ditec.crd.ws.SubsidiaryTypeCode[len] );
        for (int i = 0; (i<len); i ++) {
            this.subsidiaryType[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link sk.ditec.crd.ws.SubsidiaryTypeCode }
     *     
     */
    public sk.ditec.crd.ws.SubsidiaryTypeCode setSubsidiaryType(int idx, sk.ditec.crd.ws.SubsidiaryTypeCode value) {
        return this.subsidiaryType[idx] = value;
    }

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link String }
     *     
     */
    public String[] getAllocationCompany() {
        if (this.allocationCompany == null) {
            return new String[ 0 ] ;
        }
        String[] retVal = new String[this.allocationCompany.length] ;
        System.arraycopy(this.allocationCompany, 0, retVal, 0, this.allocationCompany.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link String }
     *     
     */
    public String getAllocationCompany(int idx) {
        if (this.allocationCompany == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.allocationCompany[idx];
    }

    public int getAllocationCompanyLength() {
        if (this.allocationCompany == null) {
            return  0;
        }
        return this.allocationCompany.length;
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link String }
     *     
     */
    public void setAllocationCompany(String[] values) {
        int len = values.length;
        this.allocationCompany = ((String[]) new String[len] );
        for (int i = 0; (i<len); i ++) {
            this.allocationCompany[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public String setAllocationCompany(int idx, String value) {
        return this.allocationCompany[idx] = value;
    }

    /**
     * Gets the value of the replicationVolume property.
     * 
     * @return
     *     possible object is
     *     {@link ReplicationVolume }
     *     
     */
    public ReplicationVolume getReplicationVolume() {
        return replicationVolume;
    }

    /**
     * Sets the value of the replicationVolume property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReplicationVolume }
     *     
     */
    public void setReplicationVolume(ReplicationVolume value) {
        this.replicationVolume = value;
    }

}
