package sk.ditec.cud.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ZmenaStlpec")
public class DTOZmenaStlpecWS {

	private Integer zmenaStlpecID;
	private String oldValue;
	private String newValue;

	private Integer IDCiselnikStlpec;
	private String nazov;
	private String nadpis;
	private String typ;
	private String dbTyp;
	private Integer dbDlzka;

	private String fkTabulka;
	private String fkPk;
	private String fkStlpec;

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

	public String getFkPk() {
		return fkPk;
	}

	public void setFkPk(String fkPk) {
		this.fkPk = fkPk;
	}

	public String getFkStlpec() {
		return fkStlpec;
	}

	public void setFkStlpec(String fkStlpec) {
		this.fkStlpec = fkStlpec;
	}

}
