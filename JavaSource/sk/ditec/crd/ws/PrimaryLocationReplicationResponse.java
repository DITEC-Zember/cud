
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
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}Primary_Location" maxOccurs="unbounded" minOccurs="0"/>
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
    "primaryLocation"
})
@XmlRootElement(name = "PrimaryLocationReplicationResponse")
public class PrimaryLocationReplicationResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Primary_Location", namespace = "http://schema.refdata.li.cc.uic.org/types/v1")
    protected sk.ditec.crd.ws.PrimaryLocation[] primaryLocation;

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link sk.ditec.crd.ws.PrimaryLocation }
     *     
     */
    public sk.ditec.crd.ws.PrimaryLocation[] getPrimaryLocation() {
        if (this.primaryLocation == null) {
            return new sk.ditec.crd.ws.PrimaryLocation[ 0 ] ;
        }
        sk.ditec.crd.ws.PrimaryLocation[] retVal = new sk.ditec.crd.ws.PrimaryLocation[this.primaryLocation.length] ;
        System.arraycopy(this.primaryLocation, 0, retVal, 0, this.primaryLocation.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link sk.ditec.crd.ws.PrimaryLocation }
     *     
     */
    public sk.ditec.crd.ws.PrimaryLocation getPrimaryLocation(int idx) {
        if (this.primaryLocation == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.primaryLocation[idx];
    }

    public int getPrimaryLocationLength() {
        if (this.primaryLocation == null) {
            return  0;
        }
        return this.primaryLocation.length;
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link sk.ditec.crd.ws.PrimaryLocation }
     *     
     */
    public void setPrimaryLocation(sk.ditec.crd.ws.PrimaryLocation[] values) {
        int len = values.length;
        this.primaryLocation = ((sk.ditec.crd.ws.PrimaryLocation[]) new sk.ditec.crd.ws.PrimaryLocation[len] );
        for (int i = 0; (i<len); i ++) {
            this.primaryLocation[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link sk.ditec.crd.ws.PrimaryLocation }
     *     
     */
    public sk.ditec.crd.ws.PrimaryLocation setPrimaryLocation(int idx, sk.ditec.crd.ws.PrimaryLocation value) {
        return this.primaryLocation[idx] = value;
    }

}
