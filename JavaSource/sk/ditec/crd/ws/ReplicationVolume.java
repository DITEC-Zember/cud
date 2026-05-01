
package sk.ditec.crd.ws;

import java.io.Serializable;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * Replication volume with the last modified date
 * 			
 * 
 * <p>Java class for ReplicationVolume complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReplicationVolume">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="ReplicateFromDate" type="{http://schema.refdata.li.cc.uic.org/types/v1}DateTime"/>
 *         &lt;element name="ReplicateAll" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReplicationVolume", propOrder = {
    "replicateFromDate",
    "replicateAll"
})
public class ReplicationVolume
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "ReplicateFromDate", type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    protected Date replicateFromDate;
    @XmlElement(name = "ReplicateAll")
    protected String replicateAll;

    /**
     * Gets the value of the replicateFromDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getReplicateFromDate() {
        return replicateFromDate;
    }

    /**
     * Sets the value of the replicateFromDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplicateFromDate(Date value) {
        this.replicateFromDate = value;
    }

    /**
     * Gets the value of the replicateAll property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReplicateAll() {
        return replicateAll;
    }

    /**
     * Sets the value of the replicateAll property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReplicateAll(String value) {
        this.replicateAll = value;
    }

}
