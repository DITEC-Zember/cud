package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;


@XmlType(name = "DTOCudSendSuborWS")
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

	// lookup field
	Integer IDOdberatel;
	String odberatelNazov;

	Integer IDObjekt;
	String objektNazov;

	Integer IDOdberatelObjekt;

	String ciselnikNazov;

	Date casVytvoreniaOd;
	Date casVytvoreniaDo;
	Date casOdoslaniaOd;
	Date casOdoslaniaDo;

	String odberatelObjektExportFormatKod;
	String odberatelObjektExportFormatNazov;

	String sendSpravaUuid;

	String lenChybne;
	String lenOdoslane;

	@Override
	public String toString() {
		return "DTOSendSubor{" +
				"sendSuborID=" + sendSuborID +
				", IDSend=" + IDSend +
				", IDCiselnik=" + IDCiselnik +
				", rowIdExt=" + rowIdExt +
				", nazovSuboru='" + nazovSuboru + '\'' +
				", subor='" + subor + '\'' +
				", poradoveCislo=" + poradoveCislo +
				", pocetPokusov=" + pocetPokusov +
				", casVytvorenia=" + casVytvorenia +
				", casOdoslania=" + casOdoslania +
				", navratKod='" + navratKod + '\'' +
				", navratText='" + navratText + '\'' +
				", errorSprava='" + errorSprava + '\'' +
				", errorCas=" + errorCas +
				", odpovedUuid='" + odpovedUuid + '\'' +
				", odpovedTyp='" + odpovedTyp + '\'' +
				", odpovedSubor='" + odpovedSubor + '\'' +
				", IDTransakciaZapisane=" + IDTransakciaZapisane +
				", IDTransakciaZrusene=" + IDTransakciaZrusene +
				", IDOdberatel=" + IDOdberatel +
				", odberatelNazov='" + odberatelNazov + '\'' +
				", IDObjekt=" + IDObjekt +
				", objektNazov='" + objektNazov + '\'' +
				", IDOdberatelObjekt=" + IDOdberatelObjekt +
				", ciselnikNazov='" + ciselnikNazov + '\'' +
				", casVytvoreniaOd=" + casVytvoreniaOd +
				", casVytvoreniaDo=" + casVytvoreniaDo +
				", casOdoslaniaOd=" + casOdoslaniaOd +
				", casOdoslaniaDo=" + casOdoslaniaDo +
				", odberatelObjektExportFormatKod='" + odberatelObjektExportFormatKod + '\'' +
				", odberatelObjektExportFormatNazov='" + odberatelObjektExportFormatNazov + '\'' +
				", sendSpravaUuid='" + sendSpravaUuid + '\'' +
				", lenChybne='" + lenChybne + '\'' +
				", lenOdoslane='" + lenOdoslane + '\'' +
				'}';
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

	public Integer getIDOdberatel() {
		return IDOdberatel;
	}

	public void setIDOdberatel(Integer IDOdberatel) {
		this.IDOdberatel = IDOdberatel;
	}

	public String getOdberatelNazov() {
		return odberatelNazov;
	}

	public void setOdberatelNazov(String odberatelNazov) {
		this.odberatelNazov = odberatelNazov;
	}

	public Integer getIDObjekt() {
		return IDObjekt;
	}

	public void setIDObjekt(Integer IDObjekt) {
		this.IDObjekt = IDObjekt;
	}

	public String getObjektNazov() {
		return objektNazov;
	}

	public void setObjektNazov(String objektNazov) {
		this.objektNazov = objektNazov;
	}

	public Integer getIDOdberatelObjekt() {
		return IDOdberatelObjekt;
	}

	public void setIDOdberatelObjekt(Integer IDOdberatelObjekt) {
		this.IDOdberatelObjekt = IDOdberatelObjekt;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public Date getCasVytvoreniaOd() {
		return casVytvoreniaOd;
	}

	public void setCasVytvoreniaOd(Date casVytvoreniaOd) {
		this.casVytvoreniaOd = casVytvoreniaOd;
	}

	public Date getCasVytvoreniaDo() {
		return casVytvoreniaDo;
	}

	public void setCasVytvoreniaDo(Date casVytvoreniaDo) {
		this.casVytvoreniaDo = casVytvoreniaDo;
	}

	public Date getCasOdoslaniaOd() {
		return casOdoslaniaOd;
	}

	public void setCasOdoslaniaOd(Date casOdoslaniaOd) {
		this.casOdoslaniaOd = casOdoslaniaOd;
	}

	public Date getCasOdoslaniaDo() {
		return casOdoslaniaDo;
	}

	public void setCasOdoslaniaDo(Date casOdoslaniaDo) {
		this.casOdoslaniaDo = casOdoslaniaDo;
	}

	public String getOdberatelObjektExportFormatKod() {
		return odberatelObjektExportFormatKod;
	}

	public void setOdberatelObjektExportFormatKod(String odberatelObjektExportFormatKod) {
		this.odberatelObjektExportFormatKod = odberatelObjektExportFormatKod;
	}

	public String getOdberatelObjektExportFormatNazov() {
		return odberatelObjektExportFormatNazov;
	}

	public void setOdberatelObjektExportFormatNazov(String odberatelObjektExportFormatNazov) {
		this.odberatelObjektExportFormatNazov = odberatelObjektExportFormatNazov;
	}

	public String getSendSpravaUuid() {
		return sendSpravaUuid;
	}

	public void setSendSpravaUuid(String sendSpravaUuid) {
		this.sendSpravaUuid = sendSpravaUuid;
	}

	public String getLenChybne() {
		return lenChybne;
	}

	public void setLenChybne(String lenChybne) {
		this.lenChybne = lenChybne;
	}

	public String getLenOdoslane() {
		return lenOdoslane;
	}

	public void setLenOdoslane(String lenOdoslane) {
		this.lenOdoslane = lenOdoslane;
	}
}
