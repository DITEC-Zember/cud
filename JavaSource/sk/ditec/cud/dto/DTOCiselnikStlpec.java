package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudCiselnikStlpecWS")
public class DTOCiselnikStlpec extends DTO {

	Integer ciselnikStlpecID;
	Integer IDCiselnik;
	String nazov;
	String nadpis;
	String typ;
	Integer poradie;
	Integer dlzka;
	Integer decimals;
	String dbTyp;
	String povinny;
	String jedinecny;
	Integer fk1IDCiselnik;
	String fk1PkNazov;
	String fk1FkNazov;
	String popis;
	Date casZmeny;
	Integer IDUcet;
	String aktivny;
	String jeDbString;

	// lookup field
	String ciselnikTabulka;
	String ciselnikNazov;
	String ciselnikAktivny;
	String ciselnikTyp;

	String fk1CiselnikNazov;
	String fk1CiselnikTabulka;

	String technicky;
	String jeUzamknuty;

	String[] notInNazovList;

	@Override
	public String toString() {
		String s = "DTOCiselnikStlpec: {";
		s += "\n ciselnikStlpecID=" + ciselnikStlpecID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n nazov=" + nazov;
		s += "\n nadpis=" + nadpis;
		s += "\n typ=" + typ;
		s += "\n poradie=" + poradie;
		s += "\n dlzka=" + dlzka;
		s += "\n decimals=" + decimals;
		s += "\n dbTyp=" + dbTyp;
		s += "\n povinny=" + povinny;
		s += "\n jedinecny=" + jedinecny;
		s += "\n fk1IDCiselnik=" + fk1IDCiselnik;
		s += "\n fk1PkNazov=" + fk1PkNazov;
		s += "\n fk1FkNazov=" + fk1FkNazov;
		s += "\n popis=" + popis;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n aktivny=" + aktivny;
		s += "\n jeDbString=" + jeDbString;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n ciselnikAktivny=" + ciselnikAktivny;
		s += "\n ciselnikTyp=" + ciselnikTyp;
		s += "\n fk1CiselnikNazov=" + fk1CiselnikNazov;
		s += "\n fk1CiselnikTabulka=" + fk1CiselnikTabulka;
		s += "\n technicky=" + technicky;
		s += "\n jeUzamknuty=" + jeUzamknuty;
		return s;
	}

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

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public String getFk1CiselnikNazov() {
		return fk1CiselnikNazov;
	}

	public void setFk1CiselnikNazov(String fk1CiselnikNazov) {
		this.fk1CiselnikNazov = fk1CiselnikNazov;
	}

	public String getTechnicky() {
		return technicky;
	}

	public void setTechnicky(String technicky) {
		this.technicky = technicky;
	}

	public Integer getPoradie() {
		return poradie;
	}

	public void setPoradie(Integer poradie) {
		this.poradie = poradie;
	}

	public String getNadpis() {
		return nadpis;
	}

	public void setNadpis(String nadpis) {
		this.nadpis = nadpis;
	}

	public String getFk1FkNazov() {
		return fk1FkNazov;
	}

	public void setFk1FkNazov(String fk1FkNazov) {
		this.fk1FkNazov = fk1FkNazov;
	}

	public String getCiselnikAktivny() {
		return ciselnikAktivny;
	}

	public void setCiselnikAktivny(String ciselnikAktivny) {
		this.ciselnikAktivny = ciselnikAktivny;
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

	public String getAktivny() {
		return aktivny;
	}

	public void setAktivny(String aktivny) {
		this.aktivny = aktivny;
	}

	public String getJeDbString() {
		return jeDbString;
	}

	public void setJeDbString(String jeDbString) {
		this.jeDbString = jeDbString;
	}

	public String getFk1CiselnikTabulka() {
		return fk1CiselnikTabulka;
	}

	public void setFk1CiselnikTabulka(String fk1CiselnikTabulka) {
		this.fk1CiselnikTabulka = fk1CiselnikTabulka;
	}

	public String[] getNotInNazovList() {
		return notInNazovList;
	}

	public void setNotInNazovList(String[] notInNazovList) {
		this.notInNazovList = notInNazovList;
	}

	public String getCiselnikTyp() {
		return ciselnikTyp;
	}

	public void setCiselnikTyp(String ciselnikTyp) {
		this.ciselnikTyp = ciselnikTyp;
	}

	public String getJeUzamknuty() {
		return jeUzamknuty;
	}

	public void setJeUzamknuty(String jeUzamknuty) {
		this.jeUzamknuty = jeUzamknuty;
	}

}
