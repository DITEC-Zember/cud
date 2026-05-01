package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPluginClassNameWS")
public class DTOPluginClassName extends DTO {

	Integer pluginClassNameID;
	String className;
	String popis;

	// lookup field
	Integer IDPluginKontrola;

	@Override
	public String toString() {
		String s = "DTOPluginClassName: {";
		s += "\n pluginClassNameID=" + pluginClassNameID;
		s += "\n className=" + className;
		s += "\n popis=" + popis;
		s += "\n IDPluginKontrola=" + IDPluginKontrola;
		return s;
	}

	public Integer getPluginClassNameID() {
		return pluginClassNameID;
	}

	public void setPluginClassNameID(Integer pluginClassNameID) {
		this.pluginClassNameID = pluginClassNameID;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public Integer getIDPluginKontrola() {
		return IDPluginKontrola;
	}

	public void setIDPluginKontrola(Integer iDPluginKontrola) {
		IDPluginKontrola = iDPluginKontrola;
	}

}
