package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudObjektCiselnikWS")
public class DTOObjektCiselnik extends DTO {

	Integer objektCiselnikID;
	Integer IDObjekt;
	Integer IDCiselnik;
	String vsetky;
	String platny;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String ciselnikTabulka;
	String ciselnikNazov;

	String objektNazov;
	String objektPlatny;

	DTOObjektStlpec[] objektStlpecList;
	String bolZmenenyObjektStlpecList;

	String bolZmeneny;
	String operacia;

	@Override
	public String toString() {
		String s = "DTOObjektCiselnik: {";
		s += "\n objektCiselnikID=" + objektCiselnikID;
		s += "\n IDObjekt=" + IDObjekt;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n vsetky=" + vsetky;
		s += "\n platny=" + platny;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n objektNazov=" + objektNazov;
		s += "\n objektPlatny=" + objektPlatny;
		s += "\n bolZmenenyObjektStlpecList=" + bolZmenenyObjektStlpecList;
		s += "\n bolZmeneny=" + bolZmeneny;
		s += "\n operacia=" + operacia;
		return s;
	}

	public Integer getObjektCiselnikID() {
		return objektCiselnikID;
	}

	public void setObjektCiselnikID(Integer objektCiselnikID) {
		this.objektCiselnikID = objektCiselnikID;
	}

	public Integer getIDObjekt() {
		return IDObjekt;
	}

	public void setIDObjekt(Integer iDObjekt) {
		IDObjekt = iDObjekt;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public String getVsetky() {
		return vsetky;
	}

	public void setVsetky(String vsetky) {
		this.vsetky = vsetky;
	}

	public String getPlatny() {
		return platny;
	}

	public void setPlatny(String platny) {
		this.platny = platny;
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

	public String getObjektNazov() {
		return objektNazov;
	}

	public void setObjektNazov(String objektNazov) {
		this.objektNazov = objektNazov;
	}

	public String getObjektPlatny() {
		return objektPlatny;
	}

	public void setObjektPlatny(String objektPlatny) {
		this.objektPlatny = objektPlatny;
	}

	public DTOObjektStlpec[] getObjektStlpecList() {
		return objektStlpecList;
	}

	public void setObjektStlpecList(DTOObjektStlpec[] objektStlpecList) {
		this.objektStlpecList = objektStlpecList;
	}

	public String getBolZmeneny() {
		return bolZmeneny;
	}

	public void setBolZmeneny(String bolZmeneny) {
		this.bolZmeneny = bolZmeneny;
	}

	public String getOperacia() {
		return operacia;
	}

	public void setOperacia(String operacia) {
		this.operacia = operacia;
	}

	public String getBolZmenenyObjektStlpecList() {
		return bolZmenenyObjektStlpecList;
	}

	public void setBolZmenenyObjektStlpecList(String bolZmenenyObjektStlpecList) {
		this.bolZmenenyObjektStlpecList = bolZmenenyObjektStlpecList;
	}

}
