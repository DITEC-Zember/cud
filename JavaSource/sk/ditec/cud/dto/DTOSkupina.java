package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudSkupinaWS")
public class DTOSkupina extends DTO {

	Integer skupinaID;
	String nazov;
	String blokovanie;

	@Override
	public String toString() {
		String s = "DTOSkupina: {";
		s += "\n skupinaID=" + skupinaID;
		s += "\n nazov=" + nazov;
		s += "\n blokovanie=" + blokovanie;
		return s;
	}

	public Integer getSkupinaID() {
		return skupinaID;
	}

	public void setSkupinaID(Integer skupinaID) {
		this.skupinaID = skupinaID;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getBlokovanie() {
		return blokovanie;
	}

	public void setBlokovanie(String blokovanie) {
		this.blokovanie = blokovanie;
	}

}
