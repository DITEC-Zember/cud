package sk.ditec.cud.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "UpdZmenaHodnotCiselnikaResponse")
public class DTOUpdZmenaResponseWS {

	Integer kodSpracovania;
	String popisSpracovania;

	public Integer getKodSpracovania() {
		return kodSpracovania;
	}

	public void setKodSpracovania(Integer kodSpracovania) {
		this.kodSpracovania = kodSpracovania;
	}

	public String getPopisSpracovania() {
		return popisSpracovania;
	}

	public void setPopisSpracovania(String popisSpracovania) {
		this.popisSpracovania = popisSpracovania;
	}

}
