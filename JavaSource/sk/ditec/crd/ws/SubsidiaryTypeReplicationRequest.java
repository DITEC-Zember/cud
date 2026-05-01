
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
    "replicationVolume"
})
@XmlRootElement(name = "SubsidiaryTypeReplicationRequest")
public class SubsidiaryTypeReplicationRequest
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ReplicationVolume")
    protected ReplicationVolume replicationVolume;

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
