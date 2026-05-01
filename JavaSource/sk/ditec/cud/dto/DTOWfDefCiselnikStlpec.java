package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudWfDefCiselnikStlpecWS")
public class DTOWfDefCiselnikStlpec extends DTO {

	Integer wfDefCiselnikStlpecID;
	Integer IDWfDef;
	Integer IDCiselnikStlpec;

	// lookup field
	String ciselnikStlpecNazov;
	String ciselnikStlpecNadpis;

	String operacia;

	@Override
	public String toString() {
		String s = "DTOWfDefCiselnikStlpec: {";
		s += "\n wfDefCiselnikStlpecID=" + wfDefCiselnikStlpecID;
		s += "\n IDWfDef=" + IDWfDef;
		s += "\n IDCiselnikStlpec=" + IDCiselnikStlpec;
		s += "\n ciselnikStlpecNazov=" + ciselnikStlpecNazov;
		s += "\n ciselnikStlpecNadpis=" + ciselnikStlpecNadpis;
		s += "\n operacia=" + operacia;
		return s;
	}

	public Integer getWfDefCiselnikStlpecID() {
		return wfDefCiselnikStlpecID;
	}

	public void setWfDefCiselnikStlpecID(Integer wfDefCiselnikStlpecID) {
		this.wfDefCiselnikStlpecID = wfDefCiselnikStlpecID;
	}

	public Integer getIDWfDef() {
		return IDWfDef;
	}

	public void setIDWfDef(Integer iDWfDef) {
		IDWfDef = iDWfDef;
	}

	public Integer getIDCiselnikStlpec() {
		return IDCiselnikStlpec;
	}

	public void setIDCiselnikStlpec(Integer iDCiselnikStlpec) {
		IDCiselnikStlpec = iDCiselnikStlpec;
	}

	public String getOperacia() {
		return operacia;
	}

	public void setOperacia(String operacia) {
		this.operacia = operacia;
	}

	public String getCiselnikStlpecNadpis() {
		return ciselnikStlpecNadpis;
	}

	public void setCiselnikStlpecNadpis(String ciselnikStlpecNadpis) {
		this.ciselnikStlpecNadpis = ciselnikStlpecNadpis;
	}

	public String getCiselnikStlpecNazov() {
		return ciselnikStlpecNazov;
	}

	public void setCiselnikStlpecNazov(String ciselnikStlpecNazov) {
		this.ciselnikStlpecNazov = ciselnikStlpecNazov;
	}

}
