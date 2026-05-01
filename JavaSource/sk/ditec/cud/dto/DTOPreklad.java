package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPrekladWS")
public class DTOPreklad extends DTO {

	Integer prekladID;
	Integer IDPrekladJazyk;
	Integer IDPrekladStlpec;
	Integer zaznamID;
	String preklad;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String ucetNazov;

	String prekladJazykKod;

	Integer prekladStlpecIDPrekladTabulka;
	String prekladStlpecPrekladTabulkaNazovDB;

	String prekladStlpecNazovDB;
	String prekladStlpecNadpis;

	@Override
	public String toString() {
		String s = "DTOPreklad: {";
		s += "\n prekladID=" + prekladID;
		s += "\n IDPrekladJazyk=" + IDPrekladJazyk;
		s += "\n IDPrekladStlpec=" + IDPrekladStlpec;
		s += "\n zaznamID=" + zaznamID;
		s += "\n preklad=" + preklad;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n ucetNazov=" + ucetNazov;
		s += "\n prekladJazykKod=" + prekladJazykKod;
		s += "\n prekladStlpecIDPrekladTabulka=" + prekladStlpecIDPrekladTabulka;
		s += "\n prekladStlpecPrekladTabulkaNazovDB=" + prekladStlpecPrekladTabulkaNazovDB;
		s += "\n prekladStlpecNazovDB=" + prekladStlpecNazovDB;
		s += "\n prekladStlpecNadpis=" + prekladStlpecNadpis;
		return s;
	}

	public Integer getPrekladID() {
		return prekladID;
	}

	public void setPrekladID(Integer prekladID) {
		this.prekladID = prekladID;
	}

	public Integer getIDPrekladJazyk() {
		return IDPrekladJazyk;
	}

	public void setIDPrekladJazyk(Integer iDPrekladJazyk) {
		IDPrekladJazyk = iDPrekladJazyk;
	}

	public Integer getIDPrekladStlpec() {
		return IDPrekladStlpec;
	}

	public void setIDPrekladStlpec(Integer iDPrekladStlpec) {
		IDPrekladStlpec = iDPrekladStlpec;
	}

	public Integer getZaznamID() {
		return zaznamID;
	}

	public void setZaznamID(Integer zaznamID) {
		this.zaznamID = zaznamID;
	}

	public String getPreklad() {
		return preklad;
	}

	public void setPreklad(String preklad) {
		this.preklad = preklad;
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

	public String getUcetNazov() {
		return ucetNazov;
	}

	public void setUcetNazov(String ucetNazov) {
		this.ucetNazov = ucetNazov;
	}

	public String getPrekladJazykKod() {
		return prekladJazykKod;
	}

	public void setPrekladJazykKod(String prekladJazykKod) {
		this.prekladJazykKod = prekladJazykKod;
	}

	public Integer getPrekladStlpecIDPrekladTabulka() {
		return prekladStlpecIDPrekladTabulka;
	}

	public void setPrekladStlpecIDPrekladTabulka(Integer prekladStlpecIDPrekladTabulka) {
		this.prekladStlpecIDPrekladTabulka = prekladStlpecIDPrekladTabulka;
	}

	public String getPrekladStlpecPrekladTabulkaNazovDB() {
		return prekladStlpecPrekladTabulkaNazovDB;
	}

	public void setPrekladStlpecPrekladTabulkaNazovDB(String prekladStlpecPrekladTabulkaNazovDB) {
		this.prekladStlpecPrekladTabulkaNazovDB = prekladStlpecPrekladTabulkaNazovDB;
	}

	public String getPrekladStlpecNazovDB() {
		return prekladStlpecNazovDB;
	}

	public void setPrekladStlpecNazovDB(String prekladStlpecNazovDB) {
		this.prekladStlpecNazovDB = prekladStlpecNazovDB;
	}

	public String getPrekladStlpecNadpis() {
		return prekladStlpecNadpis;
	}

	public void setPrekladStlpecNadpis(String prekladStlpecNadpis) {
		this.prekladStlpecNadpis = prekladStlpecNadpis;
	}

}
