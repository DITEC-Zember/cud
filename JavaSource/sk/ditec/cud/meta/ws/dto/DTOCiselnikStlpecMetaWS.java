package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CiselnikStlpec", propOrder = {
		"dbTyp",
		"decimals",
		"dlzka",
		"fk1IDCiselnik",
		"fk1PkNazov",
		"fk1Tabulka",
		"IDCiselnik",
		"jedinecny",
		"nadpis",
		"nazov",
		"popis",
		"ciselnikStlpecID",
		"povinny",
		"typ",
		"dtoPrekladWS"
})
public class DTOCiselnikStlpecMetaWS {

	//CUD_CISELNIK_STLPEC
	private Integer ciselnikStlpecID;
	private Integer IDCiselnik;
	private String nazov;
	private String nadpis;
	private String typ;
	private Integer dlzka;
	private Integer decimals;
	private String dbTyp;
	private String povinny;
	private String jedinecny;
	private Integer fk1IDCiselnik;
	private String fk1PkNazov;
	private String fk1Tabulka;
	private String popis;

	DTOPrekladWS dtoPrekladWS[];

	@XmlElement(name = "stlpecID")
	public Integer getCiselnikStlpecID() {
		return ciselnikStlpecID;
	}

	public void setCiselnikStlpecID(Integer ciselnikStlpecID) {
		this.ciselnikStlpecID = ciselnikStlpecID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer IDCiselnik) {
		this.IDCiselnik = IDCiselnik;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getNadpis() {
		return nadpis;
	}

	public void setNadpis(String nadpis) {
		this.nadpis = nadpis;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public Integer getDlzka() {
		return dlzka;
	}

	public void setDlzka(Integer dlzka) {
		this.dlzka = dlzka;
	}

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public String getDbTyp() {
		return dbTyp;
	}

	public void setDbTyp(String dbTyp) {
		this.dbTyp = dbTyp;
	}

	@XmlElement(name = "povinne")
	public String getPovinny() {
		return povinny;
	}

	public void setPovinny(String povinny) {
		this.povinny = povinny;
	}

	@XmlElement(name = "jedinecne")
	public String getJedinecny() {
		return jedinecny;
	}

	public void setJedinecny(String jedinecny) {
		this.jedinecny = jedinecny;
	}

	public Integer getFk1IDCiselnik() {
		return fk1IDCiselnik;
	}

	public void setFk1IDCiselnik(Integer fk1IDCiselnik) {
		this.fk1IDCiselnik = fk1IDCiselnik;
	}

	public String getFk1PkNazov() {
		return fk1PkNazov;
	}

	public void setFk1PkNazov(String fk1PkNazov) {
		this.fk1PkNazov = fk1PkNazov;
	}

	public String getFk1Tabulka() {
		return fk1Tabulka;
	}

	public void setFk1Tabulka(String fk1Tabulka) {
		this.fk1Tabulka = fk1Tabulka;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	@XmlElement(name = "CiselnikStlpecPreklad")
	public DTOPrekladWS[] getDtoPrekladWS() {
		return dtoPrekladWS;
	}

	public void setDtoPrekladWS(DTOPrekladWS[] dtoPrekladWS) {
		this.dtoPrekladWS = dtoPrekladWS;
	}
}
