package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPrekladJazykWS")
public class DTOPrekladJazyk extends DTO {

	Integer prekladJazykID;
	String kod;
	String nazovSk;
	String nazovEn;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOPrekladJazyk: {";
		s += "\n prekladJazykID=" + prekladJazykID;
		s += "\n kod=" + kod;
		s += "\n nazovSk=" + nazovSk;
		s += "\n nazovEn=" + nazovEn;
		return s;
	}

	public Integer getPrekladJazykID() {
		return prekladJazykID;
	}

	public void setPrekladJazykID(Integer prekladJazykID) {
		this.prekladJazykID = prekladJazykID;
	}

	public String getKod() {
		return kod;
	}

	public void setKod(String kod) {
		this.kod = kod;
	}

	public String getNazovSk() {
		return nazovSk;
	}

	public void setNazovSk(String nazovSk) {
		this.nazovSk = nazovSk;
	}

	public String getNazovEn() {
		return nazovEn;
	}

	public void setNazovEn(String nazovEn) {
		this.nazovEn = nazovEn;
	}

}
