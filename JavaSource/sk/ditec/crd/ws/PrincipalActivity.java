
package sk.ditec.crd.ws;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrincipalActivity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrincipalActivity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CE"/>
 *     &lt;enumeration value="NE"/>
 *     &lt;enumeration value="IM"/>
 *     &lt;enumeration value="PRU"/>
 *     &lt;enumeration value="FRU"/>
 *     &lt;enumeration value="OTHERS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PrincipalActivity")
@XmlEnum
public enum PrincipalActivity {

    CE,
    NE,
    IM,
    PRU,
    FRU,
    OTHERS;

    public String value() {
        return name();
    }

    public static PrincipalActivity fromValue(String v) {
        return valueOf(v);
    }

}
