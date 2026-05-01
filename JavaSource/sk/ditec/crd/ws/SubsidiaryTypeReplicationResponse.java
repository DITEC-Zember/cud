
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
 *         &lt;element ref="{http://schema.refdata.li.cc.uic.org/types/v1}SubsidiaryType" maxOccurs="unbounded" minOccurs="0"/>
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
    "subsidiaryType"
})
@XmlRootElement(name = "SubsidiaryTypeReplicationResponse")
public class SubsidiaryTypeReplicationResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "SubsidiaryType", namespace = "http://schema.refdata.li.cc.uic.org/types/v1")
    protected sk.ditec.crd.ws.SubsidiaryType[] subsidiaryType;

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link sk.ditec.crd.ws.SubsidiaryType }
     *     
     */
    public sk.ditec.crd.ws.SubsidiaryType[] getSubsidiaryType() {
        if (this.subsidiaryType == null) {
            return new sk.ditec.crd.ws.SubsidiaryType[ 0 ] ;
        }
        sk.ditec.crd.ws.SubsidiaryType[] retVal = new sk.ditec.crd.ws.SubsidiaryType[this.subsidiaryType.length] ;
        System.arraycopy(this.subsidiaryType, 0, retVal, 0, this.subsidiaryType.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link sk.ditec.crd.ws.SubsidiaryType }
     *     
     */
    public sk.ditec.crd.ws.SubsidiaryType getSubsidiaryType(int idx) {
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
     *     {@link sk.ditec.crd.ws.SubsidiaryType }
     *     
     */
    public void setSubsidiaryType(sk.ditec.crd.ws.SubsidiaryType[] values) {
        int len = values.length;
        this.subsidiaryType = ((sk.ditec.crd.ws.SubsidiaryType[]) new sk.ditec.crd.ws.SubsidiaryType[len] );
        for (int i = 0; (i<len); i ++) {
            this.subsidiaryType[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link sk.ditec.crd.ws.SubsidiaryType }
     *     
     */
    public sk.ditec.crd.ws.SubsidiaryType setSubsidiaryType(int idx, sk.ditec.crd.ws.SubsidiaryType value) {
        return this.subsidiaryType[idx] = value;
    }

}
