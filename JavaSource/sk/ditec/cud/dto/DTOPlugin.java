package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPluginWS")
public class DTOPlugin extends DTO {

	Integer pluginID;
	Integer IDCiselnik;
	Integer IDPluginClassName;
	String typ;
	Date platnostOd;
	Date platnostDo;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String ciselnikNazov;
	String ciselnikTabulka;

	String pluginClassNameClassName;

	String pluginStlpecCiselnikStlpecNadpis;

	DTOPluginStlpec[] pluginStlpecList;
	String bolZmenenyPluginStlpecList;

	String zdroj;

	@Override
	public String toString() {
		String s = "DTOPlugin: {";
		s += "\n pluginID=" + pluginID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n IDPluginClassName=" + IDPluginClassName;
		s += "\n typ=" + typ;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n pluginClassNameClassName=" + pluginClassNameClassName;
		s += "\n pluginStlpecCiselnikStlpecNadpis=" + pluginStlpecCiselnikStlpecNadpis;
		s += "\n bolZmenenyPluginStlpecList=" + bolZmenenyPluginStlpecList;
		s += "\n zdroj=" + zdroj;
		return s;
	}

	public Integer getPluginID() {
		return pluginID;
	}

	public void setPluginID(Integer pluginID) {
		this.pluginID = pluginID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getIDPluginClassName() {
		return IDPluginClassName;
	}

	public void setIDPluginClassName(Integer iDPluginClassName) {
		IDPluginClassName = iDPluginClassName;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
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

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public String getPluginClassNameClassName() {
		return pluginClassNameClassName;
	}

	public void setPluginClassNameClassName(String pluginClassNameClassName) {
		this.pluginClassNameClassName = pluginClassNameClassName;
	}

	public String getPluginStlpecCiselnikStlpecNadpis() {
		return pluginStlpecCiselnikStlpecNadpis;
	}

	public void setPluginStlpecCiselnikStlpecNadpis(String pluginStlpecCiselnikStlpecNadpis) {
		this.pluginStlpecCiselnikStlpecNadpis = pluginStlpecCiselnikStlpecNadpis;
	}

	public DTOPluginStlpec[] getPluginStlpecList() {
		return pluginStlpecList;
	}

	public void setPluginStlpecList(DTOPluginStlpec[] pluginStlpecList) {
		this.pluginStlpecList = pluginStlpecList;
	}

	public String getBolZmenenyPluginStlpecList() {
		return bolZmenenyPluginStlpecList;
	}

	public void setBolZmenenyPluginStlpecList(String bolZmenenyPluginStlpecList) {
		this.bolZmenenyPluginStlpecList = bolZmenenyPluginStlpecList;
	}

	public String getZdroj() {
		return zdroj;
	}

	public void setZdroj(String zdroj) {
		this.zdroj = zdroj;
	}

}
