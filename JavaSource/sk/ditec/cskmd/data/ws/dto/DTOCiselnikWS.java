package sk.ditec.cskmd.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CiselnikMeta")
public class DTOCiselnikWS {

	Integer ciselnikID;
	String tabulka;
	String nazov;
	String popis;

	DTOCiselnikStlpecWS[] ciselnikStlpecList;

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getTabulka() {
		return tabulka;
	}

	public void setTabulka(String tabulka) {
		this.tabulka = tabulka;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public DTOCiselnikStlpecWS[] getCiselnikStlpecList() {
		return ciselnikStlpecList;
	}

	public void setCiselnikStlpecList(DTOCiselnikStlpecWS[] ciselnikStlpecList) {
		this.ciselnikStlpecList = ciselnikStlpecList;
	}

}
