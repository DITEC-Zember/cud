package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "delStlpec")
public class DTODelStlpecWS {

	//delStlpec.nazov
	private String[] nazov;

	public String[] getNazov() {
		return nazov;
	}

	public void setNazov(String[] nazov) {
		this.nazov = nazov;
	}
}
