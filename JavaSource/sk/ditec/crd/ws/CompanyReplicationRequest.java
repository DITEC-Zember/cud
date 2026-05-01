
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
 *         &lt;element name="PrincipalActivity" type="{http://schema.refdata.li.cc.uic.org/types/v1}PrincipalActivity" maxOccurs="unbounded" minOccurs="0"/>
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
    "principalActivity",
    "replicationVolume"
})
@XmlRootElement(name = "CompanyReplicationRequest")
public class CompanyReplicationRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "CountryCodeISO", namespace = "http://schema.refdata.li.cc.uic.org/types/v1")
    protected sk.ditec.crd.ws.CountryCodeISO[] countryCodeISO;
    @XmlElement(name = "PrincipalActivity")
    protected sk.ditec.crd.ws.PrincipalActivity[] principalActivity;
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
     *     {@link sk.ditec.crd.ws.PrincipalActivity }
     *     
     */
    public sk.ditec.crd.ws.PrincipalActivity[] getPrincipalActivity() {
        if (this.principalActivity == null) {
            return new sk.ditec.crd.ws.PrincipalActivity[ 0 ] ;
        }
        sk.ditec.crd.ws.PrincipalActivity[] retVal = new sk.ditec.crd.ws.PrincipalActivity[this.principalActivity.length] ;
        System.arraycopy(this.principalActivity, 0, retVal, 0, this.principalActivity.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link sk.ditec.crd.ws.PrincipalActivity }
     *     
     */
    public sk.ditec.crd.ws.PrincipalActivity getPrincipalActivity(int idx) {
        if (this.principalActivity == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.principalActivity[idx];
    }

    public int getPrincipalActivityLength() {
        if (this.principalActivity == null) {
            return  0;
        }
        return this.principalActivity.length;
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link sk.ditec.crd.ws.PrincipalActivity }
     *     
     */
    public void setPrincipalActivity(sk.ditec.crd.ws.PrincipalActivity[] values) {
        int len = values.length;
        this.principalActivity = ((sk.ditec.crd.ws.PrincipalActivity[]) new sk.ditec.crd.ws.PrincipalActivity[len] );
        for (int i = 0; (i<len); i ++) {
            this.principalActivity[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link sk.ditec.crd.ws.PrincipalActivity }
     *     
     */
    public sk.ditec.crd.ws.PrincipalActivity setPrincipalActivity(int idx, sk.ditec.crd.ws.PrincipalActivity value) {
        return this.principalActivity[idx] = value;
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
