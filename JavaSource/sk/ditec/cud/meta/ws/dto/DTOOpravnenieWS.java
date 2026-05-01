package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Opravnenie", propOrder = {
		"ciselnikID",
		"tabulka",
		"nazov",
		"vsetkyAtributy",
		"opravnenieAtributList"
})
public class DTOOpravnenieWS {

	private Integer ciselnikID;
	private String tabulka;
	private String nazov;
	private Boolean vsetkyAtributy;

	private DTOOpravnenieAtributWS[] opravnenieAtributList;

	@XmlElement(required = true)
	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	@XmlElement(required = true)
	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	@XmlElement(required = true)
	public String getTabulka() {
		return tabulka;
	}

	public void setTabulka(String tabulka) {
		this.tabulka = tabulka;
	}

	public void setVsetkyAtributy(Boolean vsetkyAtributy) {
		this.vsetkyAtributy = vsetkyAtributy;
	}

	@XmlElement(required = true)
	public Boolean isVsetkyAtributy() {
		return vsetkyAtributy;
	}

	@XmlElement(name = "OpravnenieAtribut")
	public DTOOpravnenieAtributWS[] getOpravnenieAtributList() {
		return opravnenieAtributList;
	}

	public void setOpravnenieAtributList(DTOOpravnenieAtributWS[] opravnenieAtributList) {
		this.opravnenieAtributList = opravnenieAtributList;
	}
}
