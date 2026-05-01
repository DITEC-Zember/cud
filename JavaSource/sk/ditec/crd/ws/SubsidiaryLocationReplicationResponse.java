
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
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}Subsidiary_Location" maxOccurs="unbounded" minOccurs="0"/>
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
    "subsidiaryLocation"
})
@XmlRootElement(name = "SubsidiaryLocationReplicationResponse")
public class SubsidiaryLocationReplicationResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Subsidiary_Location", namespace = "http://schema.refdata.li.cc.uic.org/types/v1")
    protected sk.ditec.crd.ws.SubsidiaryLocation[] subsidiaryLocation;

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link sk.ditec.crd.ws.SubsidiaryLocation }
     *     
     */
    public sk.ditec.crd.ws.SubsidiaryLocation[] getSubsidiaryLocation() {
        if (this.subsidiaryLocation == null) {
            return new sk.ditec.crd.ws.SubsidiaryLocation[ 0 ] ;
        }
        sk.ditec.crd.ws.SubsidiaryLocation[] retVal = new sk.ditec.crd.ws.SubsidiaryLocation[this.subsidiaryLocation.length] ;
        System.arraycopy(this.subsidiaryLocation, 0, retVal, 0, this.subsidiaryLocation.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link sk.ditec.crd.ws.SubsidiaryLocation }
     *     
     */
    public sk.ditec.crd.ws.SubsidiaryLocation getSubsidiaryLocation(int idx) {
        if (this.subsidiaryLocation == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.subsidiaryLocation[idx];
    }

    public int getSubsidiaryLocationLength() {
        if (this.subsidiaryLocation == null) {
            return  0;
        }
        return this.subsidiaryLocation.length;
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link sk.ditec.crd.ws.SubsidiaryLocation }
     *     
     */
    public void setSubsidiaryLocation(sk.ditec.crd.ws.SubsidiaryLocation[] values) {
        int len = values.length;
        this.subsidiaryLocation = ((sk.ditec.crd.ws.SubsidiaryLocation[]) new sk.ditec.crd.ws.SubsidiaryLocation[len] );
        for (int i = 0; (i<len); i ++) {
            this.subsidiaryLocation[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link sk.ditec.crd.ws.SubsidiaryLocation }
     *     
     */
    public sk.ditec.crd.ws.SubsidiaryLocation setSubsidiaryLocation(int idx, sk.ditec.crd.ws.SubsidiaryLocation value) {
        return this.subsidiaryLocation[idx] = value;
    }

}
