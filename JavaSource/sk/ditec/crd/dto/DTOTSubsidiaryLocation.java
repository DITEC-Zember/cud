package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOTSubsidiaryLocation")
public class DTOTSubsidiaryLocation extends DTO {

	// Table name : T_SUBSIDIARY_LOCATION

	Integer histID; // HIST_ID
	Date platnostOd; // PLATNOST_OD
	Date platnostDo; // PLATNOST_DO
	Date casVytvorenia; // CAS_VYTVORENIA
	Date casZmeny; // CAS_ZMENY
	Integer IDZmena; // ID_ZMENA
	String zmaz; // ZMAZ
	Integer subsidiaryLocationID; // SUBSIDIARY_LOCATION_ID
	Integer IDSubsidiaryType; // ID_SUBSIDIARY_TYPE
	Integer IDCompany; // ID_COMPANY
	Integer IDCountry; // ID_COUNTRY
	Integer responsibleImCode; // RESPONSIBLE_IM_CODE
	Integer idsubsidiaryLocationCode; // SUBSIDIARY_LOCATION_CODE
	String subsidiaryLocationCode; // SUBSIDIARY_LOCATION_CODE
	String subsidiaryLocationName; // SUBSIDIARY_LOCATION_NAME
	Integer IDPrimaryLocation; // ID_PRIMARY_LOCATION
	Date startValidity; // START_VALIDITY
	Date endValidity; // END_VALIDITY
	Double longitude; // LONGITUDE
	Double latitude; // LATITUDE
	String freeText; // FREE_TEXT
	Integer zmenaID; // ZMENA_ID
	String activeFlag; // ACTIVE_FLAG

	public String toString() {
		String s = "DTO: {";
		s += "\n histID=" + histID;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDZmena=" + IDZmena;
		s += "\n zmaz=" + zmaz;
		s += "\n subsidiaryLocationID=" + subsidiaryLocationID;
		s += "\n IDSubsidiaryType=" + IDSubsidiaryType;
		s += "\n IDCompany=" + IDCompany;
		s += "\n IDCountry=" + IDCountry;
		s += "\n responsibleImCode=" + responsibleImCode;
		s += "\n subsidiaryLocationCode=" + subsidiaryLocationCode;
		s += "\n subsidiaryLocationName=" + subsidiaryLocationName;
		s += "\n IDPrimaryLocation=" + IDPrimaryLocation;
		s += "\n startValidity=" + startValidity;
		s += "\n endValidity=" + endValidity;
		s += "\n longitude=" + longitude;
		s += "\n latitude=" + latitude;
		s += "\n freeText=" + freeText;
		s += "\n zmenaID=" + zmenaID;
		s += "\n activeFlag=" + activeFlag;
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

	public Integer getSubsidiaryLocationID() {
		return subsidiaryLocationID;
	}

	public void setSubsidiaryLocationID(Integer subsidiaryLocationID) {
		this.subsidiaryLocationID = subsidiaryLocationID;
	}

	public Integer getIDSubsidiaryType() {
		return IDSubsidiaryType;
	}

	public void setIDSubsidiaryType(Integer iDSubsidiaryType) {
		IDSubsidiaryType = iDSubsidiaryType;
	}

	public Integer getIDCompany() {
		return IDCompany;
	}

	public void setIDCompany(Integer iDCompany) {
		IDCompany = iDCompany;
	}

	public Integer getIDCountry() {
		return IDCountry;
	}

	public void setIDCountry(Integer iDCountry) {
		IDCountry = iDCountry;
	}

	public Integer getResponsibleImCode() {
		return responsibleImCode;
	}

	public void setResponsibleImCode(Integer responsibleImCode) {
		this.responsibleImCode = responsibleImCode;
	}

	public String getSubsidiaryLocationCode() {
		return subsidiaryLocationCode;
	}

	public void setSubsidiaryLocationCode(String subsidiaryLocationCode) {
		this.subsidiaryLocationCode = subsidiaryLocationCode;
	}

	public String getSubsidiaryLocationName() {
		return subsidiaryLocationName;
	}

	public void setSubsidiaryLocationName(String subsidiaryLocationName) {
		this.subsidiaryLocationName = subsidiaryLocationName;
	}

	public Integer getIDPrimaryLocation() {
		return IDPrimaryLocation;
	}

	public void setIDPrimaryLocation(Integer iDPrimaryLocation) {
		IDPrimaryLocation = iDPrimaryLocation;
	}

	public Date getStartValidity() {
		return startValidity;
	}

	public void setStartValidity(Date startValidity) {
		this.startValidity = startValidity;
	}

	public Date getEndValidity() {
		return endValidity;
	}

	public void setEndValidity(Date endValidity) {
		this.endValidity = endValidity;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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

	public Integer getIdsubsidiaryLocationCode() {
		return idsubsidiaryLocationCode;
	}

	public void setIdsubsidiaryLocationCode(Integer idsubsidiaryLocationCode) {
		this.idsubsidiaryLocationCode = idsubsidiaryLocationCode;
	}

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

}
