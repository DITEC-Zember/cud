package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Ciselnik", propOrder = {
		"ciselnikID",
		"nazov",
		"popis",
		"tabulka",
		"predpis",
		"prilohaKapitola",
		"printZahlavie",
		"dtoPrekladWS",
		"dtoCiselnikStlpecMetaWS"
})
public class DTOCiselnikWS {

	private Integer ciselnikID;
	private String nazov;
	private String popis;
	private String tabulka;
	private String predpis;
	private String prilohaKapitola;
	private String printZahlavie;

	private DTOPrekladWS[] dtoPrekladWS;

	DTOCiselnikStlpecMetaWS[]  dtoCiselnikStlpecMetaWS;

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

	public String getPrintZahlavie() {
		return printZahlavie;
	}

	public void setPrintZahlavie(String printZahlavie) {
		this.printZahlavie = printZahlavie;
	}

	@XmlElement(name = "CiselnikPreklad")
	public DTOPrekladWS[] getDtoPrekladWS() {
		return dtoPrekladWS;
	}

	public void setDtoPrekladWS(DTOPrekladWS[] dtoPrekladWS) {
		this.dtoPrekladWS = dtoPrekladWS;
	}

	@XmlElement(name = "CiselnikStlpec")
	public DTOCiselnikStlpecMetaWS[] getDtoCiselnikStlpecMetaWS() {
		return dtoCiselnikStlpecMetaWS;
	}

	public void setDtoCiselnikStlpecMetaWS(DTOCiselnikStlpecMetaWS[] dtoCiselnikStlpecMetaWS) {
		this.dtoCiselnikStlpecMetaWS = dtoCiselnikStlpecMetaWS;
	}
}
