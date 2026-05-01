package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudDynCiselnikMetaWS")
public class DTODynCiselnikMeta extends DTO {

	Integer ciselnikID;
	String ciselnikTabulka;
	Date platnostOd;
	String listZobrazenie;
	String popupZobrazenie;

	// lookup field
	DTOCiselnikStlpecGui[] metaList;
	DTOObjektStlpec[] objektStlpecList;
	DTOPlugin[] pluginList;

	String ciselnikReadOnly;
	String errMsg;

	@Override
	public String toString() {
		String s = "DTODynCiselnikMeta: {";
		s += "\n ciselnikID=" + ciselnikID;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n platnostOd=" + platnostOd;
		s += "\n listZobrazenie=" + listZobrazenie;
		s += "\n popupZobrazenie=" + popupZobrazenie;
		s += "\n ciselnikReadOnly=" + ciselnikReadOnly;
		s += "\n errMsg=" + errMsg;
		return s;
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public DTOCiselnikStlpecGui[] getMetaList() {
		return metaList;
	}

	public void setMetaList(DTOCiselnikStlpecGui[] metaList) {
		this.metaList = metaList;
	}

	public String getCiselnikReadOnly() {
		return ciselnikReadOnly;
	}

	public void setCiselnikReadOnly(String ciselnikReadOnly) {
		this.ciselnikReadOnly = ciselnikReadOnly;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public DTOObjektStlpec[] getObjektStlpecList() {
		return objektStlpecList;
	}

	public void setObjektStlpecList(DTOObjektStlpec[] objektStlpecList) {
		this.objektStlpecList = objektStlpecList;
	}

	public String getListZobrazenie() {
		return listZobrazenie;
	}

	public void setListZobrazenie(String listZobrazenie) {
		this.listZobrazenie = listZobrazenie;
	}

	public String getPopupZobrazenie() {
		return popupZobrazenie;
	}

	public void setPopupZobrazenie(String popupZobrazenie) {
		this.popupZobrazenie = popupZobrazenie;
	}

	public DTOPlugin[] getPluginList() {
		return pluginList;
	}

	public void setPluginList(DTOPlugin[] pluginList) {
		this.pluginList = pluginList;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

}
