package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;


@XmlType(name = "DTOSendSubor")
public class DTOSendSubor extends DTO {
	// Table name : CUD_SEND_SUBOR

	Integer sendSuborID; // SEND_SUBOR_ID
	Integer IDSend; // ID_SEND
	Integer IDCiselnik; // ID_CISELNIK
	Integer rowIdExt; // ROW_ID_EXT
	String nazovSuboru; // NAZOV_SUBORU
	String subor; // SUBOR
	Integer poradoveCislo; // PORADOVE_CISLO
	Integer pocetPokusov; // POCET_POKUSOV
	Date casVytvorenia; // CAS_VYTVORENIA
	Date casOdoslania; // CAS_ODOSLANIA
	String navratKod; // NAVRAT_KOD
	String navratText; // NAVRAT_TEXT
	String errorSprava; // ERROR_SPRAVA
	Date errorCas; // ERROR_CAS
	String odpovedUuid; // ODPOVED_UUID
	String odpovedTyp; // ODPOVED_TYP
	String odpovedSubor; // ODPOVED_SUBOR
	Long IDTransakciaZapisane; // ID_TRANSAKCIA_ZAPISANE
	Long IDTransakciaZrusene; // ID_TRANSAKCIA_ZRUSENE

	public String toString() {
		String s = "DTO: {";
		s += "\n sendSuborID=" + sendSuborID;
		s += "\n IDSend=" + IDSend;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n rowIdExt=" + rowIdExt;
		s += "\n nazovSuboru=" + nazovSuboru;
		s += "\n subor=" + subor;
		s += "\n poradoveCislo=" + poradoveCislo;
		s += "\n pocetPokusov=" + pocetPokusov;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casOdoslania=" + casOdoslania;
		s += "\n navratKod=" + navratKod;
		s += "\n navratText=" + navratText;
		s += "\n errorSprava=" + errorSprava;
		s += "\n errorCas=" + errorCas;
		s += "\n odpovedUuid=" + odpovedUuid;
		s += "\n odpovedTyp=" + odpovedTyp;
		s += "\n odpovedSubor=" + odpovedSubor;
		s += "\n IDTransakciaZapisane=" + IDTransakciaZapisane;
		s += "\n IDTransakciaZrusene=" + IDTransakciaZrusene;
		s += "}";
		return s;
	}

	public Integer getSendSuborID() {
		return sendSuborID;
	}

	public void setSendSuborID(Integer sendSuborID) {
		this.sendSuborID = sendSuborID;
	}

	public Integer getIDSend() {
		return IDSend;
	}

	public void setIDSend(Integer iDSend) {
		IDSend = iDSend;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getRowIdExt() {
		return rowIdExt;
	}

	public void setRowIdExt(Integer rowIdExt) {
		this.rowIdExt = rowIdExt;
	}

	public String getNazovSuboru() {
		return nazovSuboru;
	}

	public void setNazovSuboru(String nazovSuboru) {
		this.nazovSuboru = nazovSuboru;
	}

	public String getSubor() {
		return subor;
	}

	public void setSubor(String subor) {
		this.subor = subor;
	}

	public Integer getPoradoveCislo() {
		return poradoveCislo;
	}

	public void setPoradoveCislo(Integer poradoveCislo) {
		this.poradoveCislo = poradoveCislo;
	}

	public Integer getPocetPokusov() {
		return pocetPokusov;
	}

	public void setPocetPokusov(Integer pocetPokusov) {
		this.pocetPokusov = pocetPokusov;
	}

	public Date getCasVytvorenia() {
		return casVytvorenia;
	}

	public void setCasVytvorenia(Date casVytvorenia) {
		this.casVytvorenia = casVytvorenia;
	}

	public Date getCasOdoslania() {
		return casOdoslania;
	}

	public void setCasOdoslania(Date casOdoslania) {
		this.casOdoslania = casOdoslania;
	}

	public String getNavratKod() {
		return navratKod;
	}

	public void setNavratKod(String navratKod) {
		this.navratKod = navratKod;
	}

	public String getNavratText() {
		return navratText;
	}

	public void setNavratText(String navratText) {
		this.navratText = navratText;
	}

	public String getErrorSprava() {
		return errorSprava;
	}

	public void setErrorSprava(String errorSprava) {
		this.errorSprava = errorSprava;
	}

	public Date getErrorCas() {
		return errorCas;
	}

	public void setErrorCas(Date errorCas) {
		this.errorCas = errorCas;
	}

	public String getOdpovedUuid() {
		return odpovedUuid;
	}

	public void setOdpovedUuid(String odpovedUuid) {
		this.odpovedUuid = odpovedUuid;
	}

	public String getOdpovedTyp() {
		return odpovedTyp;
	}

	public void setOdpovedTyp(String odpovedTyp) {
		this.odpovedTyp = odpovedTyp;
	}

	public String getOdpovedSubor() {
		return odpovedSubor;
	}

	public void setOdpovedSubor(String odpovedSubor) {
		this.odpovedSubor = odpovedSubor;
	}

	public Long getIDTransakciaZapisane() {
		return IDTransakciaZapisane;
	}

	public void setIDTransakciaZapisane(Long iDTransakciaZapisane) {
		IDTransakciaZapisane = iDTransakciaZapisane;
	}

	public Long getIDTransakciaZrusene() {
		return IDTransakciaZrusene;
	}

	public void setIDTransakciaZrusene(Long iDTransakciaZrusene) {
		IDTransakciaZrusene = iDTransakciaZrusene;
	}

}
