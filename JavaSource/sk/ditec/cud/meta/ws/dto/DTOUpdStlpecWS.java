package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "updStlpec")
public class DTOUpdStlpecWS {

	//updStlpec
	private String nazov;
	private String nadpis;
	private String typ;
	private String dbTyp;
	private int decimals;
	private int dlzka;
	private int fk1IDCiselnik;
	private String fk1PkNazov;
	private String fk1Tabulka;
	private String jedinecne;
	private String popis;
	private String povinne;

	private DTOUpdStlpecPrekladWS[] dtoUpdPrekladStlpec;

	@XmlElement(name = "updStlpecPreklad")
	public DTOUpdStlpecPrekladWS[] getDtoUpdPrekladStlpec() {
		return dtoUpdPrekladStlpec;
	}

	public void setDtoUpdPrekladStlpec(DTOUpdStlpecPrekladWS[] dtoUpdPrekladStlpec) {
		this.dtoUpdPrekladStlpec = dtoUpdPrekladStlpec;
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

	public String getDbTyp() {
		return dbTyp;
	}

	public void setDbTyp(String dbTyp) {
		this.dbTyp = dbTyp;
	}

	public int getDecimals() {
		return decimals;
	}

	public void setDecimals(int decimals) {
		this.decimals = decimals;
	}

	public int getDlzka() {
		return dlzka;
	}

	public void setDlzka(int dlzka) {
		this.dlzka = dlzka;
	}

	public int getFk1IDCiselnik() {
		return fk1IDCiselnik;
	}

	public void setFk1IDCiselnik(int fk1IDCiselnik) {
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

	public String getJedinecne() {
		return jedinecne;
	}

	public void setJedinecne(String jedinecne) {
		this.jedinecne = jedinecne;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public String getPovinne() {
		return povinne;
	}

	public void setPovinne(String povinne) {
		this.povinne = povinne;
	}
}
