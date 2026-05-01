package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;


@XmlType(name = "DTOSend")
public class DTOSend extends DTO {
	// Table name : CUD_SEND
	Integer sendID; // SEND_ID NUMBER(10,0)
	Integer IDOdberatelObjekt; // ID_ODBERATEL_OBJEKT NUMBER(10,0)
	Date casVytvorenia; // CAS_VYTVORENIA TIMESTAMP(6)
	String spravaUuid; // SPRAVA_UUID NVARCHAR2(36 CHAR)
	String spravaTyp; // SPRAVA_TYP NVARCHAR2(4 CHAR)
	Long idTransakciaZapisane; // ID_TRANSAKCIA_ZAPISANE NUMBER(20,0)
	Long idTransakciaZrusene; // ID_TRANSAKCIA_ZRUSENE NUMBER(20,0)

	@Override
	public String toString() {
		String s = "DTOSend: {";
		s += "\n sendID=" + sendID;
		s += "\n IDOdberatelObjekt=" + IDOdberatelObjekt;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n spravaUuid=" + spravaUuid;
		s += "\n spravaTyp=" + spravaTyp;
		s += "\n idTransakciaZapisane=" + idTransakciaZapisane;
		s += "\n idTransakciaZrusene=" + idTransakciaZrusene;
		return s;
	}

	public Integer getSendID() {
		return sendID;
	}

	public void setSendID(Integer sendID) {
		this.sendID = sendID;
	}

	public Integer getIDOdberatelObjekt() {
		return IDOdberatelObjekt;
	}

	public void setIDOdberatelObjekt(Integer iDOdberatelObjekt) {
		IDOdberatelObjekt = iDOdberatelObjekt;
	}

	public Date getCasVytvorenia() {
		return casVytvorenia;
	}

	public void setCasVytvorenia(Date casVytvorenia) {
		this.casVytvorenia = casVytvorenia;
	}

	public String getSpravaUuid() {
		return spravaUuid;
	}

	public void setSpravaUuid(String spravaUuid) {
		this.spravaUuid = spravaUuid;
	}

	public String getSpravaTyp() {
		return spravaTyp;
	}

	public void setSpravaTyp(String spravaTyp) {
		this.spravaTyp = spravaTyp;
	}

	public Long getIdTransakciaZapisane() {
		return idTransakciaZapisane;
	}



	public void setIdTransakciaZapisane(Long idTransakciaZapisane) {
		this.idTransakciaZapisane = idTransakciaZapisane;
	}



	public Long getIdTransakciaZrusene() {
		return idTransakciaZrusene;
	}



	public void setIdTransakciaZrusene(Long idTransakciaZrusene) {
		this.idTransakciaZrusene = idTransakciaZrusene;
	}

}
