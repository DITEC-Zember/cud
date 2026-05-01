package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudUcetWS")
public class DTOUcet extends DTO {

	Integer ucetID;
	String ucetNazov;
	String pouzivatelNazov;

	@Override
	public String toString() {
		String s = "DTOUcet: {";
		s += "\n ucetID=" + ucetID;
		s += "\n ucetNazov=" + ucetNazov;
		s += "\n pouzivatelNazov=" + pouzivatelNazov;
		return s;
	}

	public Integer getUcetID() {
		return ucetID;
	}

	public void setUcetID(Integer ucetID) {
		this.ucetID = ucetID;
	}

	public String getUcetNazov() {
		return ucetNazov;
	}

	public void setUcetNazov(String ucetNazov) {
		this.ucetNazov = ucetNazov;
	}

	public String getPouzivatelNazov() {
		return pouzivatelNazov;
	}

	public void setPouzivatelNazov(String pouzivatelNazov) {
		this.pouzivatelNazov = pouzivatelNazov;
	}

}
