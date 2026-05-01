package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOTCompany")
public class DTOTCompany extends DTO {

	// Table name : T_COMPANY

	Integer histID; // HIST_ID
	Date platnostOd; // PLATNOST_OD
	Date platnostDo; // PLATNOST_DO
	Date casVytvorenia; // CAS_VYTVORENIA
	Date casZmeny; // CAS_ZMENY
	Integer IDZmena; // ID_ZMENA
	String zmaz; // ZMAZ
	Integer companyID; // COMPANY_ID
	Integer IDCountry; // ID_COUNTRY
	String companyName; // COMPANY_NAME
	String companyNameAscii; // COMPANY_NAME_ASCII
	String companyUicCode; // COMPANY_UIC_CODE
	String companyUrl; // COMPANY_URL
	Date startValidity; // START_VALIDITY
	Date endValidity; // END_VALIDITY
	String companyShortName; // COMPANY_SHORT_NAME
	String freeText; // FREE_TEXT
	String freightFlag; // FREIGHT_FLAG
	String passengerFlag; // PASSENGER_FLAG
	String infrastructureFlag; // INFRASTRUCTURE_FLAG
	String otherCompanyFlag; // OTHER_COMPANY_FLAG
	String neEntityFlag; // NE_ENTITY_FLAG
	String ceEntityFlag; // CE_ENTITY_FLAG
	String contactPerson; // CONTACT_PERSON
	String email; // EMAIL
	String phoneNumber; // PHONE_NUMBER
	String faxNumber; // FAX_NUMBER
	String address; // ADDRESS
	String city; // CITY
	String mobileNumber; // MOBILE_NUMBER
	String postalCode; // POSTAL_CODE
	String activeFlag; // ACTIVE_FLAG

	public String getActiveFlag() {
		return activeFlag;
	}

	public void setActiveFlag(String activeFlag) {
		this.activeFlag = activeFlag;
	}

	public String toString() {
		String s = "DTO: {";
		s += "\n histID=" + histID;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDZmena=" + IDZmena;
		s += "\n zmaz=" + zmaz;
		s += "\n companyID=" + companyID;
		s += "\n IDCountry=" + IDCountry;
		s += "\n companyName=" + companyName;
		s += "\n companyNameAscii=" + companyNameAscii;
		s += "\n companyUicCode=" + companyUicCode;
		s += "\n companyUrl=" + companyUrl;
		s += "\n startValidity=" + startValidity;
		s += "\n endValidity=" + endValidity;
		s += "\n companyShortName=" + companyShortName;
		s += "\n freeText=" + freeText;
		s += "\n freightFlag=" + freightFlag;
		s += "\n passengerFlag=" + passengerFlag;
		s += "\n infrastructureFlag=" + infrastructureFlag;
		s += "\n otherCompanyFlag=" + otherCompanyFlag;
		s += "\n neEntityFlag=" + neEntityFlag;
		s += "\n ceEntityFlag=" + ceEntityFlag;
		s += "\n contactPerson=" + contactPerson;
		s += "\n email=" + email;
		s += "\n phoneNumber=" + phoneNumber;
		s += "\n faxNumber=" + faxNumber;
		s += "\n address=" + address;
		s += "\n city=" + city;
		s += "\n mobileNumber=" + mobileNumber;
		s += "\n postalCode=" + postalCode;
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

	public Integer getCompanyID() {
		return companyID;
	}

	public void setCompanyID(Integer companyID) {
		this.companyID = companyID;
	}

	public Integer getIDCountry() {
		return IDCountry;
	}

	public void setIDCountry(Integer iDCountry) {
		IDCountry = iDCountry;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyNameAscii() {
		return companyNameAscii;
	}

	public void setCompanyNameAscii(String companyNameAscii) {
		this.companyNameAscii = companyNameAscii;
	}

	public String getCompanyUicCode() {
		return companyUicCode;
	}

	public void setCompanyUicCode(String companyUicCode) {
		this.companyUicCode = companyUicCode;
	}

	public String getCompanyUrl() {
		return companyUrl;
	}

	public void setCompanyUrl(String companyUrl) {
		this.companyUrl = companyUrl;
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

	public String getCompanyShortName() {
		return companyShortName;
	}

	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}

	public String getFreeText() {
		return freeText;
	}

	public void setFreeText(String freeText) {
		this.freeText = freeText;
	}

	public String getFreightFlag() {
		return freightFlag;
	}

	public void setFreightFlag(String freightFlag) {
		this.freightFlag = freightFlag;
	}

	public String getPassengerFlag() {
		return passengerFlag;
	}

	public void setPassengerFlag(String passengerFlag) {
		this.passengerFlag = passengerFlag;
	}

	public String getInfrastructureFlag() {
		return infrastructureFlag;
	}

	public void setInfrastructureFlag(String infrastructureFlag) {
		this.infrastructureFlag = infrastructureFlag;
	}

	public String getOtherCompanyFlag() {
		return otherCompanyFlag;
	}

	public void setOtherCompanyFlag(String otherCompanyFlag) {
		this.otherCompanyFlag = otherCompanyFlag;
	}

	public String getNeEntityFlag() {
		return neEntityFlag;
	}

	public void setNeEntityFlag(String neEntityFlag) {
		this.neEntityFlag = neEntityFlag;
	}

	public String getCeEntityFlag() {
		return ceEntityFlag;
	}

	public void setCeEntityFlag(String ceEntityFlag) {
		this.ceEntityFlag = ceEntityFlag;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}


}
