package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudKompatibilitaWS")
public class DTOKompatibilita extends DTO {

	Integer kompatibilitaID;
	String nadpis;
	String kompatibilita;
	Integer ciselnikGuiID;
	Integer ciselnikID;
	String ciselnikTabulka;
	String zdroj;
	String publishActual;

	String[] errorMsgList;
	String[] warnMsgList;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOKompatibilita: {";
		s += "\n kompatibilitaID=" + kompatibilitaID;
		s += "\n nadpis=" + nadpis;
		s += "\n kompatibilita=" + kompatibilita;
		s += "\n ciselnikGuiID=" + ciselnikGuiID;
		s += "\n ciselnikID=" + ciselnikID;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n zdroj=" + zdroj;
		s += "\n publishActual=" + publishActual;
		s += "\n zdroj=" + zdroj;
		return s;
	}

	public Integer getKompatibilitaID() {
		return kompatibilitaID;
	}

	public void setKompatibilitaID(Integer kompatibilitaID) {
		this.kompatibilitaID = kompatibilitaID;
	}

	public String getNadpis() {
		return nadpis;
	}

	public void setNadpis(String nadpis) {
		this.nadpis = nadpis;
	}

	public String getKompatibilita() {
		return kompatibilita;
	}

	public void setKompatibilita(String kompatibilita) {
		this.kompatibilita = kompatibilita;
	}

	public Integer getCiselnikGuiID() {
		return ciselnikGuiID;
	}

	public void setCiselnikGuiID(Integer ciselnikGuiID) {
		this.ciselnikGuiID = ciselnikGuiID;
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public String getZdroj() {
		return zdroj;
	}

	public void setZdroj(String zdroj) {
		this.zdroj = zdroj;
	}

	public String getPublishActual() {
		return publishActual;
	}

	public void setPublishActual(String publishActual) {
		this.publishActual = publishActual;
	}

	public String[] getErrorMsgList() {
		return errorMsgList;
	}

	public void setErrorMsgList(String[] errorMsgList) {
		this.errorMsgList = errorMsgList;
	}

	public String[] getWarnMsgList() {
		return warnMsgList;
	}

	public void setWarnMsgList(String[] warnMsgList) {
		this.warnMsgList = warnMsgList;
	}
}
