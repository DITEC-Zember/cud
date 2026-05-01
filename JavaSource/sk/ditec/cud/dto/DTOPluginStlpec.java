package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPluginStlpecWS")
public class DTOPluginStlpec extends DTO {

	Integer pluginStlpecID;
	Integer IDPlugin;
	Integer IDCiselnikStlpec;
	Integer IDPluginAlias;
	String hodnota;

	// lookup field
	Integer IDCiselnik;
	String ciselnikNazov;
	String ciselnikTabulka;

	String ciselnikStlpecNazov;
	String ciselnikStlpecNadpis;

	String pluginAliasNazovAliasu;

	String bolZmeneny;
	String operacia;

	@Override
	public String toString() {
		String s = "DTOPlugin: {";
		s += "\n pluginStlpecID=" + pluginStlpecID;
		s += "\n IDPlugin=" + IDPlugin;
		s += "\n IDCiselnikStlpec=" + IDCiselnikStlpec;
		s += "\n IDPluginAlias=" + IDPluginAlias;
		s += "\n hodnota=" + hodnota;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n ciselnikStlpecNazov=" + ciselnikStlpecNazov;
		s += "\n ciselnikStlpecNadpis=" + ciselnikStlpecNadpis;
		s += "\n pluginAliasNazovAliasu=" + pluginAliasNazovAliasu;
		s += "\n bolZmeneny=" + bolZmeneny;
		s += "\n operacia=" + operacia;
		return s;
	}

	public Integer getPluginStlpecID() {
		return pluginStlpecID;
	}

	public void setPluginStlpecID(Integer pluginStlpecID) {
		this.pluginStlpecID = pluginStlpecID;
	}

	public Integer getIDPlugin() {
		return IDPlugin;
	}

	public void setIDPlugin(Integer iDPlugin) {
		IDPlugin = iDPlugin;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getIDCiselnikStlpec() {
		return IDCiselnikStlpec;
	}

	public void setIDCiselnikStlpec(Integer iDCiselnikStlpec) {
		IDCiselnikStlpec = iDCiselnikStlpec;
	}

	public Integer getIDPluginAlias() {
		return IDPluginAlias;
	}

	public void setIDPluginAlias(Integer iDPluginAlias) {
		IDPluginAlias = iDPluginAlias;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public String getCiselnikStlpecNazov() {
		return ciselnikStlpecNazov;
	}

	public void setCiselnikStlpecNazov(String ciselnikStlpecNazov) {
		this.ciselnikStlpecNazov = ciselnikStlpecNazov;
	}

	public String getCiselnikStlpecNadpis() {
		return ciselnikStlpecNadpis;
	}

	public void setCiselnikStlpecNadpis(String ciselnikStlpecNadpis) {
		this.ciselnikStlpecNadpis = ciselnikStlpecNadpis;
	}

	public String getPluginAliasNazovAliasu() {
		return pluginAliasNazovAliasu;
	}

	public void setPluginAliasNazovAliasu(String pluginAliasNazovAliasu) {
		this.pluginAliasNazovAliasu = pluginAliasNazovAliasu;
	}

	public String getBolZmeneny() {
		return bolZmeneny;
	}

	public void setBolZmeneny(String bolZmeneny) {
		this.bolZmeneny = bolZmeneny;
	}

	public String getOperacia() {
		return operacia;
	}

	public void setOperacia(String operacia) {
		this.operacia = operacia;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public String getHodnota() {
		return hodnota;
	}

	public void setHodnota(String hodnota) {
		this.hodnota = hodnota;
	}

}
