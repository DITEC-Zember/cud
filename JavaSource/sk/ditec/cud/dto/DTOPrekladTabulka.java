package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPrekladTabulkaWS")
public class DTOPrekladTabulka extends DTO {

	Integer prekladTabulkaID;
	String nazovDb;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOPrekladTabulka: {";
		s += "\n prekladTabulkaID=" + prekladTabulkaID;
		s += "\n nazovDb=" + nazovDb;
		return s;
	}

	public Integer getPrekladTabulkaID() {
		return prekladTabulkaID;
	}

	public void setPrekladTabulkaID(Integer prekladTabulkaID) {
		this.prekladTabulkaID = prekladTabulkaID;
	}

	public String getNazovDb() {
		return nazovDb;
	}

	public void setNazovDb(String nazovDb) {
		this.nazovDb = nazovDb;
	}

}
