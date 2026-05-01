package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudDynCiselnikWS")
public class DTODynCiselnik extends DTO {

	Integer ciselnikID;
	String tabulka;

	Integer histID;
	Integer rowID;
	Date platnostOd;
	Date platnostDo;
	Date casVytvorenia;
	Date casZmeny;
	Integer IDZmena;
	String zmaz;
	String pkName;

	DTODynValue[] values;

	// lookup field

	String lookupValueShort;
	String lookupValueLong;
	String lookupColumnName;

	String listZobrazenie;
	String popupZobrazenie;
	String formZobrazenie;

	DTOObjektStlpec[] objektStlpecList;

	Integer ciselnikStlpecGuiID;

	String zobrazitUzamknutie;
	String zobrazitOdomknutie;

	String dynFilterTyp;

	@Override
	public String toString() {
		String s = "DTODynCiselnik: {";
		s += "\n ciselnikID=" + ciselnikID;
		s += "\n tabulka=" + tabulka;
		s += "\n histID=" + histID;
		s += "\n rowID=" + rowID;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDZmena=" + IDZmena;
		s += "\n zmaz=" + zmaz;
		s += "\n pkName=" + pkName;
		s += "\n lookupValueShort=" + lookupValueShort;
		s += "\n lookupValueLong=" + lookupValueLong;
		s += "\n lookupColumnName=" + lookupColumnName;
		s += "\n listZobrazenie=" + listZobrazenie;
		s += "\n popupZobrazenie=" + popupZobrazenie;
		s += "\n formZobrazenie=" + formZobrazenie;
		s += "\n ciselnikStlpecGuiID=" + ciselnikStlpecGuiID;
		s += "\n zobrazitUzamknutie=" + zobrazitUzamknutie;
		s += "\n zobrazitOdomknutie=" + zobrazitOdomknutie;
		s += "\n dynFilterTyp=" + dynFilterTyp;
		return s;
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getTabulka() {
		return tabulka;
	}

	public void setTabulka(String tabulka) {
		this.tabulka = tabulka;
	}

	public Integer getHistID() {
		return histID;
	}

	public void setHistID(Integer histID) {
		this.histID = histID;
	}

	public Integer getRowID() {
		return rowID;
	}

	public void setRowID(Integer rowID) {
		this.rowID = rowID;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public String getLookupValueShort() {
		return lookupValueShort;
	}

	public void setLookupValueShort(String lookupValueShort) {
		this.lookupValueShort = lookupValueShort;
	}

	public String getLookupValueLong() {
		return lookupValueLong;
	}

	public void setLookupValueLong(String lookupValueLong) {
		this.lookupValueLong = lookupValueLong;
	}

	public String getLookupColumnName() {
		return lookupColumnName;
	}

	public void setLookupColumnName(String lookupColumnName) {
		this.lookupColumnName = lookupColumnName;
	}

	public Date getPlatnostDo() {
		return platnostDo;
	}

	public void setPlatnostDo(Date platnostDo) {
		this.platnostDo = platnostDo;
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

	public String getPopupZobrazenie() {
		return popupZobrazenie;
	}

	public void setPopupZobrazenie(String popupZobrazenie) {
		this.popupZobrazenie = popupZobrazenie;
	}

	public String getListZobrazenie() {
		return listZobrazenie;
	}

	public void setListZobrazenie(String listZobrazenie) {
		this.listZobrazenie = listZobrazenie;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public DTODynValue[] getValues() {
		return values;
	}

	public void setValues(DTODynValue[] values) {
		this.values = values;
	}

	public Date getCasZmeny() {
		return casZmeny;
	}

	public void setCasZmeny(Date casZmeny) {
		this.casZmeny = casZmeny;
	}

	public Date getCasVytvorenia() {
		return casVytvorenia;
	}

	public void setCasVytvorenia(Date casVytvorenia) {
		this.casVytvorenia = casVytvorenia;
	}

	public String getFormZobrazenie() {
		return formZobrazenie;
	}

	public void setFormZobrazenie(String formZobrazenie) {
		this.formZobrazenie = formZobrazenie;
	}

	public DTOObjektStlpec[] getObjektStlpecList() {
		return objektStlpecList;
	}

	public void setObjektStlpecList(DTOObjektStlpec[] objektStlpecList) {
		this.objektStlpecList = objektStlpecList;
	}

	public Integer getCiselnikStlpecGuiID() {
		return ciselnikStlpecGuiID;
	}

	public void setCiselnikStlpecGuiID(Integer ciselnikStlpecGuiID) {
		this.ciselnikStlpecGuiID = ciselnikStlpecGuiID;
	}

	public String getZobrazitUzamknutie() {
		return zobrazitUzamknutie;
	}

	public void setZobrazitUzamknutie(String zobrazitUzamknutie) {
		this.zobrazitUzamknutie = zobrazitUzamknutie;
	}

	public String getZobrazitOdomknutie() {
		return zobrazitOdomknutie;
	}

	public void setZobrazitOdomknutie(String zobrazitOdomknutie) {
		this.zobrazitOdomknutie = zobrazitOdomknutie;
	}

	public String getDynFilterTyp() {
		return dynFilterTyp;
	}

	public void setDynFilterTyp(String dynFilterTyp) {
		this.dynFilterTyp = dynFilterTyp;
	}

}
