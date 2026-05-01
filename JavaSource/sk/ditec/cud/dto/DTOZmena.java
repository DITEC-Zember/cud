package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudZmenaWS")
public class DTOZmena extends DTO {

	Integer zmenaID;
	Integer IDCiselnik;
	Integer rowID;
	String operacia;
	String stav;
	Date platnostOd;
	Date platnostDo;
	Date casSchvaleniaGr;

	// lookup field
	String ciselnikNazov;
	String ciselnikTabulka;

	Date platnostOdOd;
	Date platnostOdDo;
	Date platnostDoOd;
	Date platnostDoDo;
	Date casSchvaleniaGrOd;
	Date casSchvaleniaGrDo;

	Date zmenaStavHistCasVytvorenia;
	Date zmenaEskalaciaCasVytvorenia;

	// pridane atributy pre aktializaciu localit z db
	String operaciaPrimLoc;
	String operaciaSubLoc;
	String index;

	@Override
	public String toString() {
		String s = "DTOZmena: {";
		s += "\n zmenaID=" + zmenaID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n rowID=" + rowID;
		s += "\n operacia=" + operacia;
		s += "\n stav=" + stav;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casSchvaleniaGr=" + casSchvaleniaGr;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n platnostOdOd=" + platnostOdOd;
		s += "\n platnostOdDo=" + platnostOdDo;
		s += "\n platnostDoOd=" + platnostDoOd;
		s += "\n platnostDoDo=" + platnostDoDo;
		s += "\n casSchvaleniaGrOd=" + casSchvaleniaGrOd;
		s += "\n casSchvaleniaGrDo=" + casSchvaleniaGrDo;
		s += "\n zmenaStavHistCasVytvorenia=" + zmenaStavHistCasVytvorenia;
		s += "\n zmenaEskalaciaCasVytvorenia=" + zmenaEskalaciaCasVytvorenia;
		s += "\n operaciaPrimLoc=" + operaciaPrimLoc;
		s += "\n operaciaSubLoc=" + operaciaSubLoc;
		s += "\n index=" + index;
		return s;
	}

	public Integer getZmenaID() {
		return zmenaID;
	}

	public void setZmenaID(Integer zmenaID) {
		this.zmenaID = zmenaID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getRowID() {
		return rowID;
	}

	public void setRowID(Integer rowID) {
		this.rowID = rowID;
	}

	public String getOperacia() {
		return operacia;
	}

	public void setOperacia(String operacia) {
		this.operacia = operacia;
	}

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public Date getPlatnostOdOd() {
		return platnostOdOd;
	}

	public void setPlatnostOdOd(Date platnostOdOd) {
		this.platnostOdOd = platnostOdOd;
	}

	public Date getPlatnostOdDo() {
		return platnostOdDo;
	}

	public void setPlatnostOdDo(Date platnostOdDo) {
		this.platnostOdDo = platnostOdDo;
	}

	public Date getPlatnostDo() {
		return platnostDo;
	}

	public void setPlatnostDo(Date platnostDo) {
		this.platnostDo = platnostDo;
	}

	public Date getPlatnostDoOd() {
		return platnostDoOd;
	}

	public void setPlatnostDoOd(Date platnostDoOd) {
		this.platnostDoOd = platnostDoOd;
	}

	public Date getPlatnostDoDo() {
		return platnostDoDo;
	}

	public void setPlatnostDoDo(Date platnostDoDo) {
		this.platnostDoDo = platnostDoDo;
	}

	public Date getCasSchvaleniaGr() {
		return casSchvaleniaGr;
	}

	public void setCasSchvaleniaGr(Date casSchvaleniaGr) {
		this.casSchvaleniaGr = casSchvaleniaGr;
	}

	public Date getCasSchvaleniaGrOd() {
		return casSchvaleniaGrOd;
	}

	public void setCasSchvaleniaGrOd(Date casSchvaleniaGrOd) {
		this.casSchvaleniaGrOd = casSchvaleniaGrOd;
	}

	public Date getCasSchvaleniaGrDo() {
		return casSchvaleniaGrDo;
	}

	public void setCasSchvaleniaGrDo(Date casSchvaleniaGrDo) {
		this.casSchvaleniaGrDo = casSchvaleniaGrDo;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public Date getZmenaStavHistCasVytvorenia() {
		return zmenaStavHistCasVytvorenia;
	}

	public void setZmenaStavHistCasVytvorenia(Date zmenaStavHistCasVytvorenia) {
		this.zmenaStavHistCasVytvorenia = zmenaStavHistCasVytvorenia;
	}

	public Date getZmenaEskalaciaCasVytvorenia() {
		return zmenaEskalaciaCasVytvorenia;
	}

	public void setZmenaEskalaciaCasVytvorenia(Date zmenaEskalaciaCasVytvorenia) {
		this.zmenaEskalaciaCasVytvorenia = zmenaEskalaciaCasVytvorenia;
	}


	public String getOperaciaPrimLoc() {
		return operaciaPrimLoc;
	}

	public void setOperaciaPrimLoc(String operaciaPrimLoc) {
		this.operaciaPrimLoc = operaciaPrimLoc;
	}

	public String getOperaciaSubLoc() {
		return operaciaSubLoc;
	}

	public void setOperaciaSubLoc(String operaciaSubLoc) {
		this.operaciaSubLoc = operaciaSubLoc;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

}
