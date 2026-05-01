package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;





@XmlType(name = "DTOTCountry")
public class DTOTCountry extends DTO {

	// Table name : T_COUNTRY

	Integer histID; // HIST_ID
	Date platnostOd; // PLATNOST_OD
	Date platnostDo; // PLATNOST_DO
	Date casVytvorenia; // CAS_VYTVORENIA
	Date casZmeny; // CAS_ZMENY
	Integer IDZmena; // ID_ZMENA
	String zmaz; // ZMAZ
	Integer countryID; // COUNTRY_ID
	String countryCodeIso; // COUNTRY_CODE_ISO
	String countryUicCode; // COUNTRY_UIC_CODE
	String countryNameEn; // COUNTRY_NAME_EN
	String countryNameFr; // COUNTRY_NAME_FR
	String countryNameDe; // COUNTRY_NAME_DE
	String subLocCodeFlag; // SUB_LOC_CODE_FLAG

	public String toString() {
		String s = "DTO: {";
		s += "\n histID=" + histID;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDZmena=" + IDZmena;
		s += "\n zmaz=" + zmaz;
		s += "\n countryID=" + countryID;
		s += "\n countryCodeIso=" + countryCodeIso;
		s += "\n countryUicCode=" + countryUicCode;
		s += "\n countryNameEn=" + countryNameEn;
		s += "\n countryNameFr=" + countryNameFr;
		s += "\n countryNameDe=" + countryNameDe;
		s += "\n subLocCodeFlag=" + subLocCodeFlag;
		s += "}";
		return s;
	}

	public Integer getHistID() {
		return histID;
	}

	public void setHistID(Integer histID) {
		this.histID = histID;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public Date getPlatnostDo() {
		return platnostDo;
	}

	public void setPlatnostDo(Date platnostDo) {
		this.platnostDo = platnostDo;
	}

	public Date getCasVytvorenia() {
		return casVytvorenia;
	}

	public void setCasVytvorenia(Date casVytvorenia) {
		this.casVytvorenia = casVytvorenia;
	}

	public Date getCasZmeny() {
		return casZmeny;
	}

	public void setCasZmeny(Date casZmeny) {
		this.casZmeny = casZmeny;
	}

	public Integer getIDZmena() {
		return IDZmena;
	}

	public void setIDZmena(Integer iDZmena) {
		IDZmena = iDZmena;
	}

	public String getZmaz() {
		return zmaz;
	}

	public void setZmaz(String zmaz) {
		this.zmaz = zmaz;
	}

	public Integer getCountryID() {
		return countryID;
	}

	public void setCountryID(Integer countryID) {
		this.countryID = countryID;
	}

	public String getCountryCodeIso() {
		return countryCodeIso;
	}

	public void setCountryCodeIso(String countryCodeIso) {
		this.countryCodeIso = countryCodeIso;
	}

	public String getCountryUicCode() {
		return countryUicCode;
	}

	public void setCountryUicCode(String countryUicCode) {
		this.countryUicCode = countryUicCode;
	}

	public String getCountryNameEn() {
		return countryNameEn;
	}

	public void setCountryNameEn(String countryNameEn) {
		this.countryNameEn = countryNameEn;
	}

	public String getCountryNameFr() {
		return countryNameFr;
	}

	public void setCountryNameFr(String countryNameFr) {
		this.countryNameFr = countryNameFr;
	}

	public String getCountryNameDe() {
		return countryNameDe;
	}

	public void setCountryNameDe(String countryNameDe) {
		this.countryNameDe = countryNameDe;
	}

	public String getSubLocCodeFlag() {
		return subLocCodeFlag;
	}

	public void setSubLocCodeFlag(String subLocCodeFlag) {
		this.subLocCodeFlag = subLocCodeFlag;
	}


}
