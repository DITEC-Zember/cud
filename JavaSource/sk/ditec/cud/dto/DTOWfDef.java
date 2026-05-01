package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudWfDefWS")
public class DTOWfDef extends DTO {

	Integer wfDefID;
	Integer IDCiselnik;
	Integer IDWfDefNasl;
	String nazov;
	String typ;
	String zodpovednost;
	String emailList;
	String emailText;
	String emailSubject;
	String emailSend;
	Integer hodiny;
	Integer IDSkupina;
	String skupinaNazov;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String ciselnikNazov;

	DTOWfDefCiselnikStlpec[] wfDefCiselnikStlpecList;

	@Override
	public String toString() {
		String s = "DTOWfDef: {";
		s += "\n wfDefID=" + wfDefID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n IDWfDefNasl=" + IDWfDefNasl;
		s += "\n nazov=" + nazov;
		s += "\n typ=" + typ;
		s += "\n zodpovednost=" + zodpovednost;
		s += "\n emailList=" + emailList;
		s += "\n emailText=" + emailText;
		s += "\n emailSubject=" + emailSubject;
		s += "\n emailSend=" + emailSend;
		s += "\n hodiny=" + hodiny;
		s += "\n IDSkupina=" + IDSkupina;
		s += "\n skupinaNazov=" + skupinaNazov;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		return s;
	}

	public Integer getWfDefID() {
		return wfDefID;
	}

	public void setWfDefID(Integer wfDefID) {
		this.wfDefID = wfDefID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getIDWfDefNasl() {
		return IDWfDefNasl;
	}

	public void setIDWfDefNasl(Integer iDWfDefNasl) {
		IDWfDefNasl = iDWfDefNasl;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getZodpovednost() {
		return zodpovednost;
	}

	public void setZodpovednost(String zodpovednost) {
		this.zodpovednost = zodpovednost;
	}

	public String getEmailList() {
		return emailList;
	}

	public void setEmailList(String emailList) {
		this.emailList = emailList;
	}

	public String getEmailText() {
		return emailText;
	}

	public void setEmailText(String emailText) {
		this.emailText = emailText;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailSend() {
		return emailSend;
	}

	public void setEmailSend(String emailSend) {
		this.emailSend = emailSend;
	}

	public Integer getHodiny() {
		return hodiny;
	}

	public void setHodiny(Integer hodiny) {
		this.hodiny = hodiny;
	}

	public Integer getIDSkupina() {
		return IDSkupina;
	}

	public void setIDSkupina(Integer iDSkupina) {
		IDSkupina = iDSkupina;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
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

	public String getSkupinaNazov() {
		return skupinaNazov;
	}

	public void setSkupinaNazov(String skupinaNazov) {
		this.skupinaNazov = skupinaNazov;
	}

	public DTOWfDefCiselnikStlpec[] getWfDefCiselnikStlpecList() {
		return wfDefCiselnikStlpecList;
	}

	public void setWfDefCiselnikStlpecList(DTOWfDefCiselnikStlpec[] wfDefCiselnikStlpecList) {
		this.wfDefCiselnikStlpecList = wfDefCiselnikStlpecList;
	}

}
