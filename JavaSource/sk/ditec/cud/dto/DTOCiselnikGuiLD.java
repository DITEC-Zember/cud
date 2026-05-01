package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudCiselnikGuiLDWS")
public class DTOCiselnikGuiLD extends DTO {

	Integer ciselnikGuiID;
	Integer IDCiselnik;

	DTOCiselnikGui ciselnikGuiDTO;
	DTOCiselnik ciselnikDTO;

	public Integer getCiselnikGuiID() {
		return ciselnikGuiID;
	}

	public void setCiselnikGuiID(Integer ciselnikGuiID) {
		this.ciselnikGuiID = ciselnikGuiID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public DTOCiselnikGui getCiselnikGuiDTO() {
		return ciselnikGuiDTO;
	}

	public void setCiselnikGuiDTO(DTOCiselnikGui ciselnikGuiDTO) {
		this.ciselnikGuiDTO = ciselnikGuiDTO;
	}

	public DTOCiselnik getCiselnikDTO() {
		return ciselnikDTO;
	}

	public void setCiselnikDTO(DTOCiselnik ciselnikDTO) {
		this.ciselnikDTO = ciselnikDTO;
	}

}
