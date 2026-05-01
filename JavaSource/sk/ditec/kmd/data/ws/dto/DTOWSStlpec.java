package sk.ditec.kmd.data.ws.dto;

public class DTOWSStlpec {

	Integer stlpecID;
	Integer IDCiselnik;
	String nazov;
	String typ;
	String nadpis;
	Integer dlzka;
	String dbTyp;
	String povinne;
	String jedinecne;
	String popis;
	Integer fk1IDCiselnik;
	String fk1Tabulka;
	String fk1PkNazov;
	Integer decimals;

	public Integer getStlpecID() {
		return stlpecID;
	}

	public void setStlpecID(Integer stlpecID) {
		this.stlpecID = stlpecID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getNadpis() {
		return nadpis;
	}

	public void setNadpis(String nadpis) {
		this.nadpis = nadpis;
	}

	public Integer getDlzka() {
		return dlzka;
	}

	public void setDlzka(Integer dlzka) {
		this.dlzka = dlzka;
	}

	public String getDbTyp() {
		return dbTyp;
	}

	public void setDbTyp(String dbTyp) {
		this.dbTyp = dbTyp;
	}

	public String getPovinne() {
		return povinne;
	}

	public void setPovinne(String povinne) {
		this.povinne = povinne;
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

	public Integer getFk1IDCiselnik() {
		return fk1IDCiselnik;
	}

	public void setFk1IDCiselnik(Integer fk1idCiselnik) {
		fk1IDCiselnik = fk1idCiselnik;
	}

	public String getFk1PkNazov() {
		return fk1PkNazov;
	}

	public void setFk1PkNazov(String fk1PkNazov) {
		this.fk1PkNazov = fk1PkNazov;
	}

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public String getFk1Tabulka() {
		return fk1Tabulka;
	}

	public void setFk1Tabulka(String fk1CiselnikTabulka) {
		this.fk1Tabulka = fk1CiselnikTabulka;
	}



}
