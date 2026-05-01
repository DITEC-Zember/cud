package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPluginKontrolaWS")
public class DTOPluginKontrola extends DTO {

	Integer pluginKontrolaID;
	Integer IDCiselnik;
	Date platnostOd;
	Date platnostOdOd;
	Date platnostOdDo;
	Date casKontrolaZac;
	Date casKontrolaZacOd;
	Date casKontrolaZacDo;
	Date casKontrolaKon;
	Date casKontrolaKonOd;
	Date casKontrolaKonDo;
	String stav;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String ucetNazov;

	String casKontrolaZacOdTimeValid;
	String casKontrolaZacDoTimeValid;
	String casKontrolaKonOdTimeValid;
	String casKontrolaKonDoTimeValid;

	@Override
	public String toString() {
		String s = "DTOPluginKontrola: {";
		s += "\n pluginKontrolaID=" + pluginKontrolaID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostOdOd=" + platnostOdOd;
		s += "\n platnostOdDo=" + platnostOdDo;
		s += "\n casKontrolaZac=" + casKontrolaZac;
		s += "\n casKontrolaZacOd=" + casKontrolaZacOd;
		s += "\n casKontrolaZacDo=" + casKontrolaZacDo;
		s += "\n casKontrolaKon=" + casKontrolaKon;
		s += "\n casKontrolaKonOd=" + casKontrolaKonOd;
		s += "\n casKontrolaKonDo=" + casKontrolaKonDo;
		s += "\n stav=" + stav;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n ucetNazov=" + ucetNazov;
		s += "\n casKontrolaZacOdTimeValid=" + casKontrolaZacOdTimeValid;
		s += "\n casKontrolaZacDoTimeValid=" + casKontrolaZacDoTimeValid;
		s += "\n casKontrolaKonOdTimeValid=" + casKontrolaKonOdTimeValid;
		s += "\n casKontrolaKonDoTimeValid=" + casKontrolaKonDoTimeValid;
		return s;
	}

	public Integer getPluginKontrolaID() {
		return pluginKontrolaID;
	}

	public void setPluginKontrolaID(Integer pluginKontrolaID) {
		this.pluginKontrolaID = pluginKontrolaID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
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

	public Date getCasKontrolaZac() {
		return casKontrolaZac;
	}

	public void setCasKontrolaZac(Date casKontrolaZac) {
		this.casKontrolaZac = casKontrolaZac;
	}

	public Date getCasKontrolaZacOd() {
		return casKontrolaZacOd;
	}

	public void setCasKontrolaZacOd(Date casKontrolaZacOd) {
		this.casKontrolaZacOd = casKontrolaZacOd;
	}

	public Date getCasKontrolaZacDo() {
		return casKontrolaZacDo;
	}

	public void setCasKontrolaZacDo(Date casKontrolaZacDo) {
		this.casKontrolaZacDo = casKontrolaZacDo;
	}

	public Date getCasKontrolaKon() {
		return casKontrolaKon;
	}

	public void setCasKontrolaKon(Date casKontrolaKon) {
		this.casKontrolaKon = casKontrolaKon;
	}

	public Date getCasKontrolaKonOd() {
		return casKontrolaKonOd;
	}

	public void setCasKontrolaKonOd(Date casKontrolaKonOd) {
		this.casKontrolaKonOd = casKontrolaKonOd;
	}

	public Date getCasKontrolaKonDo() {
		return casKontrolaKonDo;
	}

	public void setCasKontrolaKonDo(Date casKontrolaKonDo) {
		this.casKontrolaKonDo = casKontrolaKonDo;
	}

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
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

	public String getCasKontrolaZacOdTimeValid() {
		return casKontrolaZacOdTimeValid;
	}

	public void setCasKontrolaZacOdTimeValid(String casKontrolaZacOdTimeValid) {
		this.casKontrolaZacOdTimeValid = casKontrolaZacOdTimeValid;
	}

	public String getCasKontrolaZacDoTimeValid() {
		return casKontrolaZacDoTimeValid;
	}

	public void setCasKontrolaZacDoTimeValid(String casKontrolaZacDoTimeValid) {
		this.casKontrolaZacDoTimeValid = casKontrolaZacDoTimeValid;
	}

	public String getCasKontrolaKonOdTimeValid() {
		return casKontrolaKonOdTimeValid;
	}

	public void setCasKontrolaKonOdTimeValid(String casKontrolaKonOdTimeValid) {
		this.casKontrolaKonOdTimeValid = casKontrolaKonOdTimeValid;
	}

	public String getCasKontrolaKonDoTimeValid() {
		return casKontrolaKonDoTimeValid;
	}

	public void setCasKontrolaKonDoTimeValid(String casKontrolaKonDoTimeValid) {
		this.casKontrolaKonDoTimeValid = casKontrolaKonDoTimeValid;
	}

}
