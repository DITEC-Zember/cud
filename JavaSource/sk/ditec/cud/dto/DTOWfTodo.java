package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudWfTodoWS")
public class DTOWfTodo extends DTO {

	Integer wfTodoID;
	Integer IDCiselnik;
	Integer IDZmena;
	Integer IDWfDef;
	String potvrdeny;
	String poznamka;
	Integer IDUcet;

	// lookup field
	String wfDefNazov;
	String wfDefTyp;

	Date zmenaStavHistCasVytvorenia;

	String zmenaOperacia;
	String zmenaStav;
	Integer zmenaRowID;
	Date zmenaCasSchvaleniaGr;
	Date zmenaPlatnostOd;

	String ciselnikNazov;
	String ciselnikTabulka;

	@Override
	public String toString() {
		String s = "DTOWfTodo: {";
		s += "\n wfTodoID=" + wfTodoID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n IDZmena=" + IDZmena;
		s += "\n IDWfDef=" + IDWfDef;
		s += "\n potvrdeny=" + potvrdeny;
		s += "\n poznamka=" + poznamka;
		s += "\n IDUcet=" + IDUcet;
		s += "\n wfDefNazov=" + wfDefNazov;
		s += "\n wfDefTyp=" + wfDefTyp;
		s += "\n zmenaOperacia=" + zmenaOperacia;
		s += "\n zmenaStav=" + zmenaStav;
		s += "\n zmenaRowID=" + zmenaRowID;
		s += "\n zmenaCasSchvaleniaGr=" + zmenaCasSchvaleniaGr;
		s += "\n zmenaPlatnostOd=" + zmenaPlatnostOd;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		return s;
	}

	public Integer getWfTodoID() {
		return wfTodoID;
	}

	public void setWfTodoID(Integer wfTodoID) {
		this.wfTodoID = wfTodoID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getIDZmena() {
		return IDZmena;
	}

	public void setIDZmena(Integer iDZmena) {
		IDZmena = iDZmena;
	}

	public Integer getIDWfDef() {
		return IDWfDef;
	}

	public void setIDWfDef(Integer iDWfDef) {
		IDWfDef = iDWfDef;
	}

	public String getPotvrdeny() {
		return potvrdeny;
	}

	public void setPotvrdeny(String potvrdeny) {
		this.potvrdeny = potvrdeny;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}

	public Integer getIDUcet() {
		return IDUcet;
	}

	public void setIDUcet(Integer iDUcet) {
		IDUcet = iDUcet;
	}

	public String getWfDefNazov() {
		return wfDefNazov;
	}

	public void setWfDefNazov(String wfDefNazov) {
		this.wfDefNazov = wfDefNazov;
	}

	public String getWfDefTyp() {
		return wfDefTyp;
	}

	public void setWfDefTyp(String wfDefTyp) {
		this.wfDefTyp = wfDefTyp;
	}

	public Date getZmenaStavHistCasVytvorenia() {
		return zmenaStavHistCasVytvorenia;
	}

	public void setZmenaStavHistCasVytvorenia(Date zmenaStavHistCasVytvorenia) {
		this.zmenaStavHistCasVytvorenia = zmenaStavHistCasVytvorenia;
	}

	public String getZmenaOperacia() {
		return zmenaOperacia;
	}

	public void setZmenaOperacia(String zmenaOperacia) {
		this.zmenaOperacia = zmenaOperacia;
	}

	public String getZmenaStav() {
		return zmenaStav;
	}

	public void setZmenaStav(String zmenaStav) {
		this.zmenaStav = zmenaStav;
	}

	public Integer getZmenaRowID() {
		return zmenaRowID;
	}

	public void setZmenaRowID(Integer zmenaRowID) {
		this.zmenaRowID = zmenaRowID;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public Date getZmenaCasSchvaleniaGr() {
		return zmenaCasSchvaleniaGr;
	}

	public void setZmenaCasSchvaleniaGr(Date zmenaCasSchvaleniaGr) {
		this.zmenaCasSchvaleniaGr = zmenaCasSchvaleniaGr;
	}

	public Date getZmenaPlatnostOd() {
		return zmenaPlatnostOd;
	}

	public void setZmenaPlatnostOd(Date zmenaPlatnostOd) {
		this.zmenaPlatnostOd = zmenaPlatnostOd;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

}
