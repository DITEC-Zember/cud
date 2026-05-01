package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudWfDefLDWS")
public class DTOWfDefLD extends DTO {

	Integer wfDefID;
	Integer IDCiselnik;
	Integer IDWfDefNasl;

	DTOWfDef wfDefDTO;
	DTOCiselnik ciselnikDTO;
	DTOWfDef wfDefNaslDTO;

	public Integer getWfDefID() {
		return wfDefID;
	}

	public void setWfDefID(Integer wfDefID) {
		this.wfDefID = wfDefID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getIDWfDefNasl() {
		return IDWfDefNasl;
	}

	public void setIDWfDefNasl(Integer iDWfDefNasl) {
		IDWfDefNasl = iDWfDefNasl;
	}

	public DTOWfDef getWfDefDTO() {
		return wfDefDTO;
	}

	public void setWfDefDTO(DTOWfDef wfDefDTO) {
		this.wfDefDTO = wfDefDTO;
	}

	public DTOCiselnik getCiselnikDTO() {
		return ciselnikDTO;
	}

	public void setCiselnikDTO(DTOCiselnik ciselnikDTO) {
		this.ciselnikDTO = ciselnikDTO;
	}

	public DTOWfDef getWfDefNaslDTO() {
		return wfDefNaslDTO;
	}

	public void setWfDefNaslDTO(DTOWfDef wfDefNaslDTO) {
		this.wfDefNaslDTO = wfDefNaslDTO;
	}

}
