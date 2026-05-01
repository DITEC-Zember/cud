package sk.ditec.cskmd.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ZmenaStlpec")
public class DTOZmenaStlpecWS {

	Integer zmenaStlpecID;
	String oldValue;
	String newValue;
	Integer IDCiselnikStlpec;

	String nazov;
	String nadpis;
	String dbTyp;
	String typ;
	Integer dbDlzka;
	String fkTabulka;
	String fkPK;
	String fkStlpec;

	public Integer getZmenaStlpecID() {
		return zmenaStlpecID;
	}

	public void setZmenaStlpecID(Integer zmenaStlpecID) {
		this.zmenaStlpecID = zmenaStlpecID;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public Integer getIDCiselnikStlpec() {
		return IDCiselnikStlpec;
	}

	public void setIDCiselnikStlpec(Integer iDCiselnikStlpec) {
		IDCiselnikStlpec = iDCiselnikStlpec;
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

	public String getDbTyp() {
		return dbTyp;
	}

	public void setDbTyp(String dbTyp) {
		this.dbTyp = dbTyp;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public Integer getDbDlzka() {
		return dbDlzka;
	}

	public void setDbDlzka(Integer dbDlzka) {
		this.dbDlzka = dbDlzka;
	}

	public String getFkTabulka() {
		return fkTabulka;
	}

	public void setFkTabulka(String fkTabulka) {
		this.fkTabulka = fkTabulka;
	}

	public String getFkPK() {
		return fkPK;
	}

	public void setFkPK(String fkPK) {
		this.fkPK = fkPK;
	}

	public String getFkStlpec() {
		return fkStlpec;
	}

	public void setFkStlpec(String fkStlpec) {
		this.fkStlpec = fkStlpec;
	}

}