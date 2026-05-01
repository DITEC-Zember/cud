package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "updCiselnik")
public class DTOUpdCiselnikWS {

	//delStlpec.nazov
	DTODelStlpecWS dtoDelStlpecWS;

	//updCiselnik
	private String nazov;
	private String popis;
	private String tabulka;
	private String predpis;
	private String prilohaKapitola;
	private String printZahlavie;
	private String aktivny;

	private DTOUpdCiselnikPrekladWS[] dtoUpdCiselnikPreklad;

	//updStlpec
	DTOUpdStlpecWS[] dtoUpdStlpecWS;

	@XmlElement(name = "delStlpec")
	public DTODelStlpecWS getDtoDelStlpecWS() {
		return dtoDelStlpecWS;
	}

	public void setDtoDelStlpecWS(DTODelStlpecWS dtoDelStlpecWS) {
		this.dtoDelStlpecWS = dtoDelStlpecWS;
	}

	@XmlElement(name = "updCiselnikPreklad")
	public DTOUpdCiselnikPrekladWS[] getDtoUpdCiselnikPreklad() {
		return dtoUpdCiselnikPreklad;
	}

	public void setDtoUpdCiselnikPreklad(DTOUpdCiselnikPrekladWS[] dtoUpdCiselnikPreklad) {
		this.dtoUpdCiselnikPreklad = dtoUpdCiselnikPreklad;
	}

	@XmlElement()
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

	public String getPredpis() {
		return predpis;
	}

	public void setPredpis(String predpis) {
		this.predpis = predpis;
	}

	@XmlElement(name = "priloha_kapitola")
	public String getPrilohaKapitola() {
		return prilohaKapitola;
	}

	public void setPrilohaKapitola(String prilohaKapitola) {
		this.prilohaKapitola = prilohaKapitola;
	}

	@XmlElement(name = "print_zahlavie")
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

	@XmlElement(name = "updStlpec")
	public DTOUpdStlpecWS[] getDtoUpdStlpecWS() {
		return dtoUpdStlpecWS;
	}

	public void setDtoUpdStlpecWS(DTOUpdStlpecWS[] dtoUpdStlpecWS) {
		this.dtoUpdStlpecWS = dtoUpdStlpecWS;
	}
}
