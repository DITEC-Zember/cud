package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPluginLDWS")
public class DTOPluginLD extends DTO {

	Integer pluginID;
	Integer IDCiselnik;
	Integer IDPluginClassName;

	DTOPlugin pluginDTO;
	DTOCiselnik ciselnikDTO;
	DTOPluginClassName pluginClassNameDTO;

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

	public DTOPlugin getPluginDTO() {
		return pluginDTO;
	}

	public void setPluginDTO(DTOPlugin pluginDTO) {
		this.pluginDTO = pluginDTO;
	}

	public DTOCiselnik getCiselnikDTO() {
		return ciselnikDTO;
	}

	public void setCiselnikDTO(DTOCiselnik ciselnikDTO) {
		this.ciselnikDTO = ciselnikDTO;
	}

	public Integer getIDPluginClassName() {
		return IDPluginClassName;
	}

	public void setIDPluginClassName(Integer iDPluginClassName) {
		IDPluginClassName = iDPluginClassName;
	}

	public DTOPluginClassName getPluginClassNameDTO() {
		return pluginClassNameDTO;
	}

	public void setPluginClassNameDTO(DTOPluginClassName pluginClassNameDTO) {
		this.pluginClassNameDTO = pluginClassNameDTO;
	}

}
