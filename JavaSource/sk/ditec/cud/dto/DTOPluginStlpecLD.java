package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPluginStlpecLDWS")
public class DTOPluginStlpecLD extends DTO {

	Integer IDPluginAlias;
	Integer IDCiselnikStlpec;

	DTOPluginAlias pluginAliasDTO;
	DTOCiselnikStlpec ciselnikStlpecDTO;

	public Integer getIDPluginAlias() {
		return IDPluginAlias;
	}

	public void setIDPluginAlias(Integer iDPluginAlias) {
		IDPluginAlias = iDPluginAlias;
	}

	public Integer getIDCiselnikStlpec() {
		return IDCiselnikStlpec;
	}

	public void setIDCiselnikStlpec(Integer iDCiselnikStlpec) {
		IDCiselnikStlpec = iDCiselnikStlpec;
	}

	public DTOPluginAlias getPluginAliasDTO() {
		return pluginAliasDTO;
	}

	public void setPluginAliasDTO(DTOPluginAlias pluginAliasDTO) {
		this.pluginAliasDTO = pluginAliasDTO;
	}

	public DTOCiselnikStlpec getCiselnikStlpecDTO() {
		return ciselnikStlpecDTO;
	}

	public void setCiselnikStlpecDTO(DTOCiselnikStlpec ciselnikStlpecDTO) {
		this.ciselnikStlpecDTO = ciselnikStlpecDTO;
	}

}
