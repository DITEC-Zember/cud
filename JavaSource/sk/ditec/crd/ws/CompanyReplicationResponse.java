
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
 *         &lt;element name="Company" type="{http://schema.refdata.li.cc.uic.org/types/v1}Company" maxOccurs="unbounded" minOccurs="0"/>
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
    "company"
})
@XmlRootElement(name = "CompanyReplicationResponse")
public class CompanyReplicationResponse
    implements Serializable
{

    private final static long serialVersionUID = 1L;
    @XmlElement(name = "Company")
    protected sk.ditec.crd.ws.Company[] company;

    /**
     * 
     * 
     * @return
     *     array of
     *     {@link sk.ditec.crd.ws.Company }
     *     
     */
    public sk.ditec.crd.ws.Company[] getCompany() {
        if (this.company == null) {
            return new sk.ditec.crd.ws.Company[ 0 ] ;
        }
        sk.ditec.crd.ws.Company[] retVal = new sk.ditec.crd.ws.Company[this.company.length] ;
        System.arraycopy(this.company, 0, retVal, 0, this.company.length);
        return (retVal);
    }

    /**
     * 
     * 
     * @return
     *     one of
     *     {@link sk.ditec.crd.ws.Company }
     *     
     */
    public sk.ditec.crd.ws.Company getCompany(int idx) {
        if (this.company == null) {
            throw new IndexOutOfBoundsException();
        }
        return this.company[idx];
    }

    public int getCompanyLength() {
        if (this.company == null) {
            return  0;
        }
        return this.company.length;
    }

    /**
     * 
     * 
     * @param values
     *     allowed objects are
     *     {@link sk.ditec.crd.ws.Company }
     *     
     */
    public void setCompany(sk.ditec.crd.ws.Company[] values) {
        int len = values.length;
        this.company = ((sk.ditec.crd.ws.Company[]) new sk.ditec.crd.ws.Company[len] );
        for (int i = 0; (i<len); i ++) {
            this.company[i] = values[i];
        }
    }

    /**
     * 
     * 
     * @param value
     *     allowed object is
     *     {@link sk.ditec.crd.ws.Company }
     *     
     */
    public sk.ditec.crd.ws.Company setCompany(int idx, sk.ditec.crd.ws.Company value) {
        return this.company[idx] = value;
    }

}
