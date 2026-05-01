package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudObjektStlpecWS")
public class DTOObjektStlpec extends DTO {

	Integer objektStlpecID;
	Integer IDObjektCiselnik;
	Integer IDCiselnikStlpec;
	String zmena;
	String hodnota;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String ciselnikStlpecNazov;
	String ciselnikStlpecNadpis;

	String bolZmeneny;
	String operacia;

	Integer IDCiselnik;

	@Override
	public String toString() {
		String s = "DTOObjektStlpec: {";
		s += "\n objektStlpecID=" + objektStlpecID;
		s += "\n IDObjektCiselnik=" + IDObjektCiselnik;
		s += "\n IDCiselnikStlpec=" + IDCiselnikStlpec;
		s += "\n zmena=" + zmena;
		s += "\n hodnota=" + hodnota;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n ciselnikStlpecNazov=" + ciselnikStlpecNazov;
		s += "\n ciselnikStlpecNadpis=" + ciselnikStlpecNadpis;
		s += "\n bolZmeneny=" + bolZmeneny;
		s += "\n operacia=" + operacia;
		s += "\n IDCiselnik=" + IDCiselnik;
		return s;
	}

	public Integer getObjektStlpecID() {
		return objektStlpecID;
	}

	public void setObjektStlpecID(Integer objektStlpecID) {
		this.objektStlpecID = objektStlpecID;
	}

	public Integer getIDObjektCiselnik() {
		return IDObjektCiselnik;
	}

	public void setIDObjektCiselnik(Integer iDObjektCiselnik) {
		IDObjektCiselnik = iDObjektCiselnik;
	}

	public Integer getIDCiselnikStlpec() {
		return IDCiselnikStlpec;
	}

	public void setIDCiselnikStlpec(Integer iDCiselnikStlpec) {
		IDCiselnikStlpec = iDCiselnikStlpec;
	}

	public String getZmena() {
		return zmena;
	}

	public void setZmena(String zmena) {
		this.zmena = zmena;
	}

	public String getHodnota() {
		return hodnota;
	}

	public void setHodnota(String hodnota) {
		this.hodnota = hodnota;
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

	public String getCiselnikStlpecNazov() {
		return ciselnikStlpecNazov;
	}

	public void setCiselnikStlpecNazov(String ciselnikStlpecNazov) {
		this.ciselnikStlpecNazov = ciselnikStlpecNazov;
	}

	public String getCiselnikStlpecNadpis() {
		return ciselnikStlpecNadpis;
	}

	public void setCiselnikStlpecNadpis(String ciselnikStlpecNadpis) {
		this.ciselnikStlpecNadpis = ciselnikStlpecNadpis;
	}

	public String getOperacia() {
		return operacia;
	}

	public void setOperacia(String operacia) {
		this.operacia = operacia;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public String getBolZmeneny() {
		return bolZmeneny;
	}

	public void setBolZmeneny(String bolZmeneny) {
		this.bolZmeneny = bolZmeneny;
	}

}
