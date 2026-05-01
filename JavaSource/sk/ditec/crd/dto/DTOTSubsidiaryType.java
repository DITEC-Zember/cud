package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOTSubsidiaryType")
public class DTOTSubsidiaryType extends DTO {

	// Table name : T_SUBSIDIARY_TYPE

	Integer histID; // HIST_ID
	Date platnostOd; // PLATNOST_OD
	Date platnostDo; // PLATNOST_DO
	Date casVytvorenia; // CAS_VYTVORENIA
	Date casZmeny; // CAS_ZMENY
	Integer IDZmena; // ID_ZMENA
	String zmaz; // ZMAZ
	Integer subsidiaryTypeID; // SUBSIDIARY_TYPE_ID
	String subsidiaryTypeCode; // SUBSIDIARY_TYPE_CODE
	String subsidiaryTypeName; // SUBSIDIARY_TYPE_NAME
	String imFlag; // IM_FLAG
	String freightRuFlag; // FREIGHT_RU_FLAG
	String passengerRuFlag; // PASSENGER_RU_FLAG
	String centralEntityFlag; // CENTRAL_ENTITY_FLAG
	String nationalEntityFlag; // NATIONAL_ENTITY_FLAG
	String othersFlag; // OTHERS_FLAG
	String freeText; // FREE_TEXT
	Integer zmenaID; // ZMENA_ID

	public String toString() {
		String s = "DTO: {";
		s += "\n histID=" + histID;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDZmena=" + IDZmena;
		s += "\n zmaz=" + zmaz;
		s += "\n subsidiaryTypeID=" + subsidiaryTypeID;
		s += "\n subsidiaryTypeCode=" + subsidiaryTypeCode;
		s += "\n subsidiaryTypeName=" + subsidiaryTypeName;
		s += "\n imFlag=" + imFlag;
		s += "\n freightRuFlag=" + freightRuFlag;
		s += "\n passengerRuFlag=" + passengerRuFlag;
		s += "\n centralEntityFlag=" + centralEntityFlag;
		s += "\n nationalEntityFlag=" + nationalEntityFlag;
		s += "\n othersFlag=" + othersFlag;
		s += "\n freeText=" + freeText;
		s += "\n zmenaID=" + zmenaID;
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

	public Integer getSubsidiaryTypeID() {
		return subsidiaryTypeID;
	}

	public void setSubsidiaryTypeID(Integer subsidiaryTypeID) {
		this.subsidiaryTypeID = subsidiaryTypeID;
	}

	public String getSubsidiaryTypeCode() {
		return subsidiaryTypeCode;
	}

	public void setSubsidiaryTypeCode(String subsidiaryTypeCode) {
		this.subsidiaryTypeCode = subsidiaryTypeCode;
	}

	public String getSubsidiaryTypeName() {
		return subsidiaryTypeName;
	}

	public void setSubsidiaryTypeName(String subsidiaryTypeName) {
		this.subsidiaryTypeName = subsidiaryTypeName;
	}

	public String getImFlag() {
		return imFlag;
	}

	public void setImFlag(String imFlag) {
		this.imFlag = imFlag;
	}

	public String getFreightRuFlag() {
		return freightRuFlag;
	}

	public void setFreightRuFlag(String freightRuFlag) {
		this.freightRuFlag = freightRuFlag;
	}

	public String getPassengerRuFlag() {
		return passengerRuFlag;
	}

	public void setPassengerRuFlag(String passengerRuFlag) {
		this.passengerRuFlag = passengerRuFlag;
	}

	public String getCentralEntityFlag() {
		return centralEntityFlag;
	}

	public void setCentralEntityFlag(String centralEntityFlag) {
		this.centralEntityFlag = centralEntityFlag;
	}

	public String getNationalEntityFlag() {
		return nationalEntityFlag;
	}

	public void setNationalEntityFlag(String nationalEntityFlag) {
		this.nationalEntityFlag = nationalEntityFlag;
	}

	public String getOthersFlag() {
		return othersFlag;
	}

	public void setOthersFlag(String othersFlag) {
		this.othersFlag = othersFlag;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public Integer getZmenaID() {
		return zmenaID;
	}

	public void setZmenaID(Integer zmenaID) {
		this.zmenaID = zmenaID;
	}

}
