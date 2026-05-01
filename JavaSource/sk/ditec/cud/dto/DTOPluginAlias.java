package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPluginAliasWS")
public class DTOPluginAlias extends DTO {

	Integer pluginAliasID;
	Integer IDPluginClassName;
	String typ;
	String nazovAliasu;
	String popis;
	String konstanta;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOPluginAlias: {";
		s += "\n pluginAliasID=" + pluginAliasID;
		s += "\n IDPluginClassName=" + IDPluginClassName;
		s += "\n typ=" + typ;
		s += "\n nazovAliasu=" + nazovAliasu;
		s += "\n popis=" + popis;
		s += "\n konstanta=" + konstanta;
		return s;
	}

	public Integer getPluginAliasID() {
		return pluginAliasID;
	}

	public void setPluginAliasID(Integer pluginAliasID) {
		this.pluginAliasID = pluginAliasID;
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

	public String getNazovAliasu() {
		return nazovAliasu;
	}

	public void setNazovAliasu(String nazovAliasu) {
		this.nazovAliasu = nazovAliasu;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public String getKonstanta() {
		return konstanta;
	}

	public void setKonstanta(String konstanta) {
		this.konstanta = konstanta;
	}

}
