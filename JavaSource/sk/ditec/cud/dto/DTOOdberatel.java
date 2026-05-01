package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudOdberatelWS")
public class DTOOdberatel extends DTO {

	Integer odberatelID;
	Integer IDHistDopravca;
	String nazov;
	String obmUcetNazov;
	String rolaKod;
	String rolaNazov;
	String exportTyp;
	String exportCesta;
	String aktivny;
	String interny;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String histDopravcaNazov;
	Integer histDopravcaID;
	String webstranka;

	@Override
	public String toString() {
		String s = "DTOOdberatel: {";
		s += "\n odberatelID=" + odberatelID;
		s += "\n IDHistDopravca=" + IDHistDopravca;
		s += "\n nazov=" + nazov;
		s += "\n obmUcetNazov=" + obmUcetNazov;
		s += "\n rolaKod=" + rolaKod;
		s += "\n rolaNazov=" + rolaNazov;
		s += "\n exportTyp=" + exportTyp;
		s += "\n exportCesta=" + exportCesta;
		s += "\n aktivny=" + aktivny;
		s += "\n interny=" + interny;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n histDopravcaNazov=" + histDopravcaNazov;
		s += "\n histDopravcaID=" + histDopravcaID;
		s += "\n webstranka=" + webstranka;
		return s;
	}

	public Integer getOdberatelID() {
		return odberatelID;
	}

	public void setOdberatelID(Integer odberatelID) {
		this.odberatelID = odberatelID;
	}

	public Integer getIDHistDopravca() {
		return IDHistDopravca;
	}

	public void setIDHistDopravca(Integer iDHistDopravca) {
		IDHistDopravca = iDHistDopravca;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getObmUcetNazov() {
		return obmUcetNazov;
	}

	public void setObmUcetNazov(String obmUcetNazov) {
		this.obmUcetNazov = obmUcetNazov;
	}

	public String getRolaKod() {
		return rolaKod;
	}

	public void setRolaKod(String rolaKod) {
		this.rolaKod = rolaKod;
	}

	public String getRolaNazov() {
		return rolaNazov;
	}

	public void setRolaNazov(String rolaNazov) {
		this.rolaNazov = rolaNazov;
	}

	public String getExportTyp() {
		return exportTyp;
	}

	public void setExportTyp(String exportTyp) {
		this.exportTyp = exportTyp;
	}

	public String getExportCesta() {
		return exportCesta;
	}

	public void setExportCesta(String exportCesta) {
		this.exportCesta = exportCesta;
	}

	public String getAktivny() {
		return aktivny;
	}

	public void setAktivny(String aktivny) {
		this.aktivny = aktivny;
	}

	public String getInterny() {
		return interny;
	}

	public void setInterny(String interny) {
		this.interny = interny;
	}

	public Date getCasZmeny() {
		return casZmeny;
	}

	public void setCasZmeny(Date casZmeny) {
		this.casZmeny = casZmeny;
	}

	public Integer getIDUcet() {
		return IDUcet;
	}

	public void setIDUcet(Integer iDUcet) {
		IDUcet = iDUcet;
	}

	public String getHistDopravcaNazov() {
		return histDopravcaNazov;
	}

	public void setHistDopravcaNazov(String histDopravcaNazov) {
		this.histDopravcaNazov = histDopravcaNazov;
	}

	public Integer getHistDopravcaID() {
		return histDopravcaID;
	}

	public void setHistDopravcaID(Integer histDopravcaID) {
		this.histDopravcaID = histDopravcaID;
	}

	public String getWebstranka() {
		return webstranka;
	}

	public void setWebstranka(String webstranka) {
		this.webstranka = webstranka;
	}
}
