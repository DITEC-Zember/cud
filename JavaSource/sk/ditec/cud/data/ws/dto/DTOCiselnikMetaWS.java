package sk.ditec.cud.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CiselnikMeta")
public class DTOCiselnikMetaWS {

	private Integer ciselnikID;
	private String nazov;
	private String popis;
	private String tabulka;

	private String errorMsg;

	private DTOCiselnikStlpecMetaWS[] ciselnikStlpecList;

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
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

	public String getTabulka() {
		return tabulka;
	}

	public void setTabulka(String tabulka) {
		this.tabulka = tabulka;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public DTOCiselnikStlpecMetaWS[] getCiselnikStlpecList() {
		return ciselnikStlpecList;
	}

	public void setCiselnikStlpecList(DTOCiselnikStlpecMetaWS[] ciselnikStlpecList) {
		this.ciselnikStlpecList = ciselnikStlpecList;
	}

}
