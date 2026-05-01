package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudZmenaStavHistWS")
public class DTOZmenaStavHist extends DTO {

	Integer	zmenaStavHistID;
	Integer	IDCiselnik;
	Integer	IDZmena;
	String	stav;
	Date	casVytvorenia;
	Date	casVytvoreniaOd;
	Date	casVytvoreniaDo;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOZmenaStavHist: {";
		s += "\n zmenaStavHistID=" + zmenaStavHistID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n IDZmena=" + IDZmena;
		s += "\n stav=" + stav;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casVytvoreniaOd=" + casVytvoreniaOd;
		s += "\n casVytvoreniaDo=" + casVytvoreniaDo;
		return s;
	}

	public Integer getZmenaStavHistID() {
		return zmenaStavHistID;
	}

	public void setZmenaStavHistID(Integer zmenaStavHistID) {
		this.zmenaStavHistID = zmenaStavHistID;
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

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
	}

	public Date getCasVytvorenia() {
		return casVytvorenia;
	}

	public void setCasVytvorenia(Date casVytvorenia) {
		this.casVytvorenia = casVytvorenia;
	}

	public Date getCasVytvoreniaOd() {
		return casVytvoreniaOd;
	}

	public void setCasVytvoreniaOd(Date casVytvoreniaOd) {
		this.casVytvoreniaOd = casVytvoreniaOd;
	}

	public Date getCasVytvoreniaDo() {
		return casVytvoreniaDo;
	}

	public void setCasVytvoreniaDo(Date casVytvoreniaDo) {
		this.casVytvoreniaDo = casVytvoreniaDo;
	}

}
