package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudSuborWS")
public class DTOSubor extends DTO {

	Integer suborID;
	String nazovSuboru;
	byte[] subor;

	// lookup field
	String tabulka;

	@Override
	public String toString() {
		String s = "DTOSubor: {";
		s += "\n suborID=" + suborID;
		s += "\n nazovSuboru=" + nazovSuboru;
		s += "\n tabulka=" + nazovSuboru;
		return s;
	}

	public Integer getSuborID() {
		return suborID;
	}

	public void setSuborID(Integer suborID) {
		this.suborID = suborID;
	}

	public String getNazovSuboru() {
		return nazovSuboru;
	}

	public void setNazovSuboru(String nazovSuboru) {
		this.nazovSuboru = nazovSuboru;
	}

	public byte[] getSubor() {
		return subor;
	}

	public void setSubor(byte[] subor) {
		this.subor = subor;
	}

	public String getTabulka() {
		return tabulka;
	}

	public void setTabulka(String tabulka) {
		this.tabulka = tabulka;
	}

}
