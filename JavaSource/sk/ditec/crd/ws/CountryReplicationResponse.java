
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
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}Country" maxOccurs="unbounded" minOccurs="0"/>
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
    "country"
})
@XmlRootElement(name = "CountryReplicationResponse")
public class CountryReplicationResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Country", namespace = "http://schema.refdata.li.cc.uic.org/types/v1")
    protected sk.ditec.crd.ws.Country[] country;

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link sk.ditec.crd.ws.Country }
     *     
     */
    public sk.ditec.crd.ws.Country[] getCountry() {
        if (this.country == null) {
            return new sk.ditec.crd.ws.Country[ 0 ] ;
        }
        sk.ditec.crd.ws.Country[] retVal = new sk.ditec.crd.ws.Country[this.country.length] ;
        System.arraycopy(this.country, 0, retVal, 0, this.country.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link sk.ditec.crd.ws.Country }
     *     
     */
    public sk.ditec.crd.ws.Country getCountry(int idx) {
        if (this.country == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.country[idx];
    }

    public int getCountryLength() {
        if (this.country == null) {
            return  0;
        }
        return this.country.length;
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link sk.ditec.crd.ws.Country }
     *     
     */
    public void setCountry(sk.ditec.crd.ws.Country[] values) {
        int len = values.length;
        this.country = ((sk.ditec.crd.ws.Country[]) new sk.ditec.crd.ws.Country[len] );
        for (int i = 0; (i<len); i ++) {
            this.country[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link sk.ditec.crd.ws.Country }
     *     
     */
    public sk.ditec.crd.ws.Country setCountry(int idx, sk.ditec.crd.ws.Country value) {
        return this.country[idx] = value;
    }

}
