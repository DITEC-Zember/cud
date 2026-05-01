package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudRolaWS")
public class DTORola extends DTO {

	Integer rolaID;
	String rolaKod;
	String rolaNazov;

	// lookup field
	String modulKod;

	@Override
	public String toString() {
		String s = "DTORola: {";
		s += "\n rolaID=" + rolaID;
		s += "\n rolaKod=" + rolaKod;
		s += "\n rolaNazov=" + rolaNazov;
		s += "\n modulKod=" + modulKod;
		return s;
	}

	public Integer getRolaID() {
		return rolaID;
	}

	public void setRolaID(Integer rolaID) {
		this.rolaID = rolaID;
	}

	public String getRolaKod() {
		return rolaKod;
	}

	public void setRolaKod(String rolaKod) {
		this.rolaKod = rolaKod;
	}

	public String getRolaNazov() {
		return rolaNazov;
	}

	public void setRolaNazov(String rolaNazov) {
		this.rolaNazov = rolaNazov;
	}

	public String getModulKod() {
		return modulKod;
	}

	public void setModulKod(String modulKod) {
		this.modulKod = modulKod;
	}

}
