package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPluginKontrolaRowWS")
public class DTOPluginKontrolaRow extends DTO {

	Integer pluginKontrolaRowID;
	Integer IDPluginKontrola;
	Integer IDPlugin;
	Integer rowID;
	String stav;
	String popis;

	// lookup field

	String pluginClassNameClassName;

	Integer IDCiselnikStlpec;

	String kontrolaUspesna;

	@Override
	public String toString() {
		String s = "DTOPluginKontrolaRow: {";
		s += "\n pluginKontrolaRowID=" + pluginKontrolaRowID;
		s += "\n IDPluginKontrola=" + IDPluginKontrola;
		s += "\n IDPlugin=" + IDPlugin;
		s += "\n rowID=" + rowID;
		s += "\n stav=" + stav;
		s += "\n popis=" + popis;
		s += "\n pluginClassNameClassName=" + pluginClassNameClassName;
		s += "\n IDCiselnikStlpec=" + IDCiselnikStlpec;
		s += "\n kontrolaUspesna=" + kontrolaUspesna;
		return s;
	}

	public Integer getPluginKontrolaRowID() {
		return pluginKontrolaRowID;
	}

	public void setPluginKontrolaRowID(Integer pluginKontrolaRowID) {
		this.pluginKontrolaRowID = pluginKontrolaRowID;
	}

	public Integer getIDPluginKontrola() {
		return IDPluginKontrola;
	}

	public void setIDPluginKontrola(Integer iDPluginKontrola) {
		IDPluginKontrola = iDPluginKontrola;
	}

	public Integer getIDPlugin() {
		return IDPlugin;
	}

	public void setIDPlugin(Integer iDPlugin) {
		IDPlugin = iDPlugin;
	}

	public Integer getRowID() {
		return rowID;
	}

	public void setRowID(Integer rowID) {
		this.rowID = rowID;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public String getPluginClassNameClassName() {
		return pluginClassNameClassName;
	}

	public void setPluginClassNameClassName(String pluginClassNameClassName) {
		this.pluginClassNameClassName = pluginClassNameClassName;
	}

	public Integer getIDCiselnikStlpec() {
		return IDCiselnikStlpec;
	}

	public void setIDCiselnikStlpec(Integer iDCiselnikStlpec) {
		IDCiselnikStlpec = iDCiselnikStlpec;
	}

	public String getKontrolaUspesna() {
		return kontrolaUspesna;
	}

	public void setKontrolaUspesna(String kontrolaUspesna) {
		this.kontrolaUspesna = kontrolaUspesna;
	}

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
	}

}
