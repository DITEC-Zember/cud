package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudCiselnikWS")
public class DTOCiselnik extends DTO {

	Integer ciselnikID;
	String tabulka;
	String nazov;
	String popis;
	String printClass;
	String printZahlavie;
	String aktivny;
	String predpis;
	String prilohaKapitola;
	String hlavny;
	String typ;
	String kategoria;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String ciselnikStlpecPkNazov;

	String pluginValidacia;

	String jeUzamknuty;

	@Override
	public String toString() {
		String s = "DTOCiselnik: {";
		s += "\n ciselnikID=" + ciselnikID;
		s += "\n tabulka=" + tabulka;
		s += "\n nazov=" + nazov;
		s += "\n popis=" + popis;
		s += "\n printClass=" + printClass;
		s += "\n printZahlavie=" + printZahlavie;
		s += "\n aktivny=" + aktivny;
		s += "\n predpis=" + predpis;
		s += "\n prilohaKapitola=" + prilohaKapitola;
		s += "\n hlavny=" + hlavny;
		s += "\n typ=" + typ;
		s += "\n kategoria=" + kategoria;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n ciselnikStlpecPkNazov=" + ciselnikStlpecPkNazov;
		s += "\n pluginValidacia=" + pluginValidacia;
		s += "\n jeUzamknuty=" + jeUzamknuty;
		return s;
	}

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

	public String getPrintClass() {
		return printClass;
	}

	public void setPrintClass(String printClass) {
		this.printClass = printClass;
	}

	public String getPrintZahlavie() {
		return printZahlavie;
	}

	public void setPrintZahlavie(String printZahlavie) {
		this.printZahlavie = printZahlavie;
	}

	public String getAktivny() {
		return aktivny;
	}

	public void setAktivny(String aktivny) {
		this.aktivny = aktivny;
	}

	public String getPredpis() {
		return predpis;
	}

	public void setPredpis(String predpis) {
		this.predpis = predpis;
	}

	public String getPrilohaKapitola() {
		return prilohaKapitola;
	}

	public void setPrilohaKapitola(String prilohaKapitola) {
		this.prilohaKapitola = prilohaKapitola;
	}

	public String getHlavny() {
		return hlavny;
	}

	public void setHlavny(String hlavny) {
		this.hlavny = hlavny;
	}

	public String getCiselnikStlpecPkNazov() {
		return ciselnikStlpecPkNazov;
	}

	public void setCiselnikStlpecPkNazov(String ciselnikStlpecPkNazov) {
		this.ciselnikStlpecPkNazov = ciselnikStlpecPkNazov;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getKategoria() {
		return kategoria;
	}

	public void setKategoria(String kategoria) {
		this.kategoria = kategoria;
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

	public String getJeUzamknuty() {
		return jeUzamknuty;
	}

	public void setJeUzamknuty(String jeUzamknuty) {
		this.jeUzamknuty = jeUzamknuty;
	}

	public String getPluginValidacia() {
		return pluginValidacia;
	}

	public void setPluginValidacia(String pluginValidacia) {
		this.pluginValidacia = pluginValidacia;
	}

}
