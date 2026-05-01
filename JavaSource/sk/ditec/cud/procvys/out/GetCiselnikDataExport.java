package sk.ditec.cud.procvys.out;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "getCiselnikDataExport")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class GetCiselnikDataExport implements Serializable {

    @XmlElement(name = "ciselnikData")
    private CiselnikData ciselnikData;

    public CiselnikData getCiselnikData() {
        return ciselnikData;
    }

    public void setCiselnikData(CiselnikData ciselnikData) {
        this.ciselnikData = ciselnikData;
    }
}
