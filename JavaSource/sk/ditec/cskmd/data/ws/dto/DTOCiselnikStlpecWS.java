package sk.ditec.cskmd.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CiselnikStlpecMeta")
public class DTOCiselnikStlpecWS {

	Integer ciselnikStlpecID;
	Integer IDCiselnik;
	String nazov;
	String typ;
	String dbTyp;
	String nadpis;
	Integer dlzka;
	Integer decimals;
	String povinny;
	String jedinecny;
	String popis;
	Integer fkIDCiselnik;
	String fkTabulka;
	String fkPkNazov;

	public Integer getCiselnikStlpecID() {
		return ciselnikStlpecID;
	}

	public void setCiselnikStlpecID(Integer ciselnikStlpecID) {
		this.ciselnikStlpecID = ciselnikStlpecID;
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

	public String getDbTyp() {
		return dbTyp;
	}

	public void setDbTyp(String dbTyp) {
		this.dbTyp = dbTyp;
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

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public String getPovinny() {
		return povinny;
	}

	public void setPovinny(String povinny) {
		this.povinny = povinny;
	}

	public String getJedinecny() {
		return jedinecny;
	}

	public void setJedinecny(String jedinecny) {
		this.jedinecny = jedinecny;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public Integer getFkIDCiselnik() {
		return fkIDCiselnik;
	}

	public void setFkIDCiselnik(Integer fkIDCiselnik) {
		this.fkIDCiselnik = fkIDCiselnik;
	}

	public String getFkTabulka() {
		return fkTabulka;
	}

	public void setFkTabulka(String fkTabulka) {
		this.fkTabulka = fkTabulka;
	}

	public String getFkPkNazov() {
		return fkPkNazov;
	}

	public void setFkPkNazov(String fkPkNazov) {
		this.fkPkNazov = fkPkNazov;
	}

}
