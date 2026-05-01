package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudCiselnikStlpecGuiWS")
public class DTOCiselnikStlpecGui extends DTO {

	Integer ciselnikStlpecGuiID;
	Integer IDCiselnikGui;
	Integer IDCiselnikStlpec;
	String nadpis;
	Integer poradie;
	Integer dlzka;
	Integer decimals;
	String zmena;
	String povinny;
	String zarovnanie;
	String fk1FkNazov;
	Integer fk2IDCiselnik;
	String fk2PkNazov;
	String fk2FkNazov;
	String listZobrazenie;
	Integer listSirka;
	String listSirkaChange;
	String formZobrazenie;
	Integer formSirka;
	String popupZobrazenie;
	Integer popupSirka;
	String popupSirkaChange;
	String lookupZobrazenie;
	String editControl;
	String regExp;
	String popis;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String ciselnikStlpecNazov;
	String ciselnikStlpecTyp;
	String ciselnikStlpecJedinecny;
	String ciselnikStlpecPovinny;
	String ciselnikStlpecDbTyp;
	Integer ciselnikStlpecFk1IDCiselnik;
	String ciselnikStlpecFk1CiselnikNazov;
	String ciselnikStlpecFk1CiselnikTabulka;
	String ciselnikStlpecFk1PkNazov;
	String ciselnikStlpecFk1FkNazov;
	Integer ciselnikStlpecIDCiselnik;
	String ciselnikStlpecJeDbString;

	String ciselnikTabulka;

	String fk2CiselnikNazov;
	String fk2CiselnikTabulka;

	Date platnostOd;

	String lookupColumnName;

	String alias;

	String[] regExpValues;

	@Override
	public String toString() {
		String s = "DTOCiselnikStlpecGui: {";
		s += "\n ciselnikStlpecGuiID=" + ciselnikStlpecGuiID;
		s += "\n IDCiselnikGui=" + IDCiselnikGui;
		s += "\n IDCiselnikStlpec=" + IDCiselnikStlpec;
		s += "\n nadpis=" + nadpis;
		s += "\n poradie=" + poradie;
		s += "\n dlzka=" + dlzka;
		s += "\n decimals=" + decimals;
		s += "\n zmena=" + zmena;
		s += "\n povinny=" + povinny;
		s += "\n zarovnanie=" + zarovnanie;
		s += "\n fk1FkNazov=" + fk1FkNazov;
		s += "\n fk2IDCiselnik=" + fk2IDCiselnik;
		s += "\n fk2PkNazov=" + fk2PkNazov;
		s += "\n listZobrazenie=" + listZobrazenie;
		s += "\n listSirka=" + listSirka;
		s += "\n listSirkaChange=" + listSirkaChange;
		s += "\n formZobrazenie=" + formZobrazenie;
		s += "\n formSirka=" + formSirka;
		s += "\n popupZobrazenie=" + popupZobrazenie;
		s += "\n popupSirka=" + popupSirka;
		s += "\n popupSirkaChange=" + popupSirkaChange;
		s += "\n lookupZobrazenie=" + lookupZobrazenie;
		s += "\n editControl=" + editControl;
		s += "\n regExp=" + regExp;
		s += "\n popis=" + popis;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n ciselnikStlpecJeDbString=" + ciselnikStlpecJeDbString;
		s += "\n ciselnikStlpecNazov=" + ciselnikStlpecNazov;
		s += "\n ciselnikStlpecTyp=" + ciselnikStlpecTyp;
		s += "\n ciselnikStlpecJedinecny=" + ciselnikStlpecJedinecny;
		s += "\n ciselnikStlpecPovinny=" + ciselnikStlpecPovinny;
		s += "\n ciselnikStlpecDbTyp=" + ciselnikStlpecDbTyp;
		s += "\n ciselnikStlpecFk1IDCiselnik=" + ciselnikStlpecFk1IDCiselnik;
		s += "\n ciselnikStlpecFk1CiselnikNazov=" + ciselnikStlpecFk1CiselnikNazov;
		s += "\n ciselnikStlpecFk1CiselnikTabulka=" + ciselnikStlpecFk1CiselnikTabulka;
		s += "\n ciselnikStlpecFk1PkNazov=" + ciselnikStlpecFk1PkNazov;
		s += "\n ciselnikStlpecFk1FkNazov=" + ciselnikStlpecFk1FkNazov;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n fk2CiselnikNazov=" + fk2CiselnikNazov;
		s += "\n fk2CiselnikTabulka=" + fk2CiselnikTabulka;
		s += "\n ciselnikStlpecIDCiselnik=" + ciselnikStlpecIDCiselnik;
		s += "\n platnostOd=" + platnostOd;
		s += "\n lookupColumnName=" + lookupColumnName;
		s += "\n alias=" + alias;
		return s;
	}

	public Integer getCiselnikStlpecGuiID() {
		return ciselnikStlpecGuiID;
	}

	public void setCiselnikStlpecGuiID(Integer ciselnikStlpecGuiID) {
		this.ciselnikStlpecGuiID = ciselnikStlpecGuiID;
	}

	public Integer getIDCiselnikGui() {
		return IDCiselnikGui;
	}

	public void setIDCiselnikGui(Integer iDCiselnikGui) {
		IDCiselnikGui = iDCiselnikGui;
	}

	public Integer getIDCiselnikStlpec() {
		return IDCiselnikStlpec;
	}

	public void setIDCiselnikStlpec(Integer iDCiselnikStlpec) {
		IDCiselnikStlpec = iDCiselnikStlpec;
	}

	public String getNadpis() {
		return nadpis;
	}

	public void setNadpis(String nadpis) {
		this.nadpis = nadpis;
	}

	public Integer getPoradie() {
		return poradie;
	}

	public void setPoradie(Integer poradie) {
		this.poradie = poradie;
	}

	public Integer getDlzka() {
		return dlzka;
	}

	public void setDlzka(Integer dlzka) {
		this.dlzka = dlzka;
	}

	public Integer getDecimals() {
		return decimals;
	}

	public void setDecimals(Integer decimals) {
		this.decimals = decimals;
	}

	public String getZmena() {
		return zmena;
	}

	public void setZmena(String zmena) {
		this.zmena = zmena;
	}

	public String getPovinny() {
		return povinny;
	}

	public void setPovinny(String povinny) {
		this.povinny = povinny;
	}

	public String getZarovnanie() {
		return zarovnanie;
	}

	public void setZarovnanie(String zarovnanie) {
		this.zarovnanie = zarovnanie;
	}

	public String getFk1FkNazov() {
		return fk1FkNazov;
	}

	public void setFk1FkNazov(String fk1FkNazov) {
		this.fk1FkNazov = fk1FkNazov;
	}

	public Integer getFk2IDCiselnik() {
		return fk2IDCiselnik;
	}

	public void setFk2IDCiselnik(Integer fk2idCiselnik) {
		fk2IDCiselnik = fk2idCiselnik;
	}

	public String getFk2PkNazov() {
		return fk2PkNazov;
	}

	public void setFk2PkNazov(String fk2PkNazov) {
		this.fk2PkNazov = fk2PkNazov;
	}

	public String getFk2FkNazov() {
		return fk2FkNazov;
	}

	public void setFk2FkNazov(String fk2FkNazov) {
		this.fk2FkNazov = fk2FkNazov;
	}

	public String getListZobrazenie() {
		return listZobrazenie;
	}

	public void setListZobrazenie(String listZobrazenie) {
		this.listZobrazenie = listZobrazenie;
	}

	public Integer getListSirka() {
		return listSirka;
	}

	public void setListSirka(Integer listSirka) {
		this.listSirka = listSirka;
	}

	public String getListSirkaChange() {
		return listSirkaChange;
	}

	public void setListSirkaChange(String listSirkaChange) {
		this.listSirkaChange = listSirkaChange;
	}

	public String getFormZobrazenie() {
		return formZobrazenie;
	}

	public void setFormZobrazenie(String formZobrazenie) {
		this.formZobrazenie = formZobrazenie;
	}

	public Integer getFormSirka() {
		return formSirka;
	}

	public void setFormSirka(Integer formSirka) {
		this.formSirka = formSirka;
	}

	public String getPopupZobrazenie() {
		return popupZobrazenie;
	}

	public void setPopupZobrazenie(String popupZobrazenie) {
		this.popupZobrazenie = popupZobrazenie;
	}

	public Integer getPopupSirka() {
		return popupSirka;
	}

	public void setPopupSirka(Integer popupSirka) {
		this.popupSirka = popupSirka;
	}

	public String getPopupSirkaChange() {
		return popupSirkaChange;
	}

	public void setPopupSirkaChange(String popupSirkaChange) {
		this.popupSirkaChange = popupSirkaChange;
	}

	public String getLookupZobrazenie() {
		return lookupZobrazenie;
	}

	public void setLookupZobrazenie(String lookupZobrazenie) {
		this.lookupZobrazenie = lookupZobrazenie;
	}

	public String getEditControl() {
		return editControl;
	}

	public void setEditControl(String editControl) {
		this.editControl = editControl;
	}

	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
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

	public String getCiselnikStlpecNazov() {
		return ciselnikStlpecNazov;
	}

	public void setCiselnikStlpecNazov(String ciselnikStlpecNazov) {
		this.ciselnikStlpecNazov = ciselnikStlpecNazov;
	}

	public String getCiselnikStlpecTyp() {
		return ciselnikStlpecTyp;
	}

	public void setCiselnikStlpecTyp(String ciselnikStlpecTyp) {
		this.ciselnikStlpecTyp = ciselnikStlpecTyp;
	}

	public String getCiselnikStlpecJedinecny() {
		return ciselnikStlpecJedinecny;
	}

	public void setCiselnikStlpecJedinecny(String ciselnikStlpecJedinecny) {
		this.ciselnikStlpecJedinecny = ciselnikStlpecJedinecny;
	}

	public String getCiselnikStlpecPovinny() {
		return ciselnikStlpecPovinny;
	}

	public void setCiselnikStlpecPovinny(String ciselnikStlpecPovinny) {
		this.ciselnikStlpecPovinny = ciselnikStlpecPovinny;
	}

	public Integer getCiselnikStlpecFk1IDCiselnik() {
		return ciselnikStlpecFk1IDCiselnik;
	}

	public void setCiselnikStlpecFk1IDCiselnik(Integer ciselnikStlpecFk1IDCiselnik) {
		this.ciselnikStlpecFk1IDCiselnik = ciselnikStlpecFk1IDCiselnik;
	}

	public String getCiselnikStlpecFk1CiselnikNazov() {
		return ciselnikStlpecFk1CiselnikNazov;
	}

	public void setCiselnikStlpecFk1CiselnikNazov(String ciselnikStlpecFk1CiselnikNazov) {
		this.ciselnikStlpecFk1CiselnikNazov = ciselnikStlpecFk1CiselnikNazov;
	}

	public String getCiselnikStlpecFk1CiselnikTabulka() {
		return ciselnikStlpecFk1CiselnikTabulka;
	}

	public void setCiselnikStlpecFk1CiselnikTabulka(String ciselnikStlpecFk1CiselnikTabulka) {
		this.ciselnikStlpecFk1CiselnikTabulka = ciselnikStlpecFk1CiselnikTabulka;
	}

	public String getCiselnikStlpecFk1PkNazov() {
		return ciselnikStlpecFk1PkNazov;
	}

	public void setCiselnikStlpecFk1PkNazov(String ciselnikStlpecFk1PkNazov) {
		this.ciselnikStlpecFk1PkNazov = ciselnikStlpecFk1PkNazov;
	}

	public Integer getCiselnikStlpecIDCiselnik() {
		return ciselnikStlpecIDCiselnik;
	}

	public void setCiselnikStlpecIDCiselnik(Integer ciselnikStlpecIDCiselnik) {
		this.ciselnikStlpecIDCiselnik = ciselnikStlpecIDCiselnik;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public String getFk2CiselnikNazov() {
		return fk2CiselnikNazov;
	}

	public void setFk2CiselnikNazov(String fk2CiselnikNazov) {
		this.fk2CiselnikNazov = fk2CiselnikNazov;
	}

	public String getFk2CiselnikTabulka() {
		return fk2CiselnikTabulka;
	}

	public void setFk2CiselnikTabulka(String fk2CiselnikTabulka) {
		this.fk2CiselnikTabulka = fk2CiselnikTabulka;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public String getLookupColumnName() {
		return lookupColumnName;
	}

	public void setLookupColumnName(String lookupColumnName) {
		this.lookupColumnName = lookupColumnName;
	}

	public String getCiselnikStlpecFk1FkNazov() {
		return ciselnikStlpecFk1FkNazov;
	}

	public void setCiselnikStlpecFk1FkNazov(String ciselnikStlpecFk1FkNazov) {
		this.ciselnikStlpecFk1FkNazov = ciselnikStlpecFk1FkNazov;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String[] getRegExpValues() {
		return regExpValues;
	}

	public void setRegExpValues(String[] regExpValues) {
		this.regExpValues = regExpValues;
	}

	public String getCiselnikStlpecDbTyp() {
		return ciselnikStlpecDbTyp;
	}

	public void setCiselnikStlpecDbTyp(String ciselnikStlpecDbTyp) {
		this.ciselnikStlpecDbTyp = ciselnikStlpecDbTyp;
	}

	public String getCiselnikStlpecJeDbString() {
		return ciselnikStlpecJeDbString;
	}

	public void setCiselnikStlpecJeDbString(String ciselnikStlpecJeDbString) {
		this.ciselnikStlpecJeDbString = ciselnikStlpecJeDbString;
	}

}
