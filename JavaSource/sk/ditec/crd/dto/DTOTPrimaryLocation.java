package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOTPrimaryLocation")
public class DTOTPrimaryLocation extends DTO {

	// Table name : T_PRIMARY_LOCATION

	Integer histID; // HIST_ID
	Date platnostOd; // PLATNOST_OD
	Date platnostDo; // PLATNOST_DO
	Date casVytvorenia; // CAS_VYTVORENIA
	Date casZmeny; // CAS_ZMENY
	Integer IDZmena; // ID_ZMENA
	String zmaz; // ZMAZ
	Integer primaryLocationID; // PRIMARY_LOCATION_ID
	Integer IDCountry; // ID_COUNTRY
	String locationCode; // LOCATION_CODE
	Date startValidity; // START_VALIDITY
	Date endValidity; // END_VALIDITY
	Integer IDCompany; // ID_COMPANY
	String locationName; // LOCATION_NAME
	String locationNameAscii; // LOCATION_NAME_ASCII
	String nutsCode; // NUTS_CODE
	String containerHandlingFlag; // CONTAINER_HANDLING_FLAG
	String handoverPointFlag; // HANDOVER_POINT_FLAG
	String freightPossibleFlag; // FREIGHT_POSSIBLE_FLAG
	Date freightStartValidity; // FREIGHT_START_VALIDITY
	Date freightEndValidity; // FREIGHT_END_VALIDITY
	String passengerPossibleFlag; // PASSENGER_POSSIBLE_FLAG
	Date passengerStartValidity; // PASSENGER_START_VALIDITY
	Date passengerEndValidity; // PASSENGER_END_VALIDITY
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
		s += "\n primaryLocationID=" + primaryLocationID;
		s += "\n IDCountry=" + IDCountry;
		s += "\n locationCode=" + locationCode;
		s += "\n startValidity=" + startValidity;
		s += "\n endValidity=" + endValidity;
		s += "\n IDCompany=" + IDCompany;
		s += "\n locationName=" + locationName;
		s += "\n locationNameAscii=" + locationNameAscii;
		s += "\n nutsCode=" + nutsCode;
		s += "\n containerHandlingFlag=" + containerHandlingFlag;
		s += "\n handoverPointFlag=" + handoverPointFlag;
		s += "\n freightPossibleFlag=" + freightPossibleFlag;
		s += "\n freightStartValidity=" + freightStartValidity;
		s += "\n freightEndValidity=" + freightEndValidity;
		s += "\n passengerPossibleFlag=" + passengerPossibleFlag;
		s += "\n passengerStartValidity=" + passengerStartValidity;
		s += "\n passengerEndValidity=" + passengerEndValidity;
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

	public Integer getPrimaryLocationID() {
		return primaryLocationID;
	}

	public void setPrimaryLocationID(Integer primaryLocationID) {
		this.primaryLocationID = primaryLocationID;
	}

	public Integer getIDCountry() {
		return IDCountry;
	}

	public void setIDCountry(Integer iDCountry) {
		IDCountry = iDCountry;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
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

	public Integer getIDCompany() {
		return IDCompany;
	}

	public void setIDCompany(Integer iDCompany) {
		IDCompany = iDCompany;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationNameAscii() {
		return locationNameAscii;
	}

	public void setLocationNameAscii(String locationNameAscii) {
		this.locationNameAscii = locationNameAscii;
	}

	public String getNutsCode() {
		return nutsCode;
	}

	public void setNutsCode(String nutsCode) {
		this.nutsCode = nutsCode;
	}

	public String getContainerHandlingFlag() {
		return containerHandlingFlag;
	}

	public void setContainerHandlingFlag(String containerHandlingFlag) {
		this.containerHandlingFlag = containerHandlingFlag;
	}

	public String getHandoverPointFlag() {
		return handoverPointFlag;
	}

	public void setHandoverPointFlag(String handoverPointFlag) {
		this.handoverPointFlag = handoverPointFlag;
	}

	public String getFreightPossibleFlag() {
		return freightPossibleFlag;
	}

	public void setFreightPossibleFlag(String freightPossibleFlag) {
		this.freightPossibleFlag = freightPossibleFlag;
	}

	public Date getFreightStartValidity() {
		return freightStartValidity;
	}

	public void setFreightStartValidity(Date freightStartValidity) {
		this.freightStartValidity = freightStartValidity;
	}

	public Date getFreightEndValidity() {
		return freightEndValidity;
	}

	public void setFreightEndValidity(Date freightEndValidity) {
		this.freightEndValidity = freightEndValidity;
	}

	public String getPassengerPossibleFlag() {
		return passengerPossibleFlag;
	}

	public void setPassengerPossibleFlag(String passengerPossibleFlag) {
		this.passengerPossibleFlag = passengerPossibleFlag;
	}

	public Date getPassengerStartValidity() {
		return passengerStartValidity;
	}

	public void setPassengerStartValidity(Date passengerStartValidity) {
		this.passengerStartValidity = passengerStartValidity;
	}

	public Date getPassengerEndValidity() {
		return passengerEndValidity;
	}

	public void setPassengerEndValidity(Date passengerEndValidity) {
		this.passengerEndValidity = passengerEndValidity;
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

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

}
