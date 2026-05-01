package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudCiselnikStlpecLDWS")
public class DTOCiselnikStlpecLD extends DTO {

	Integer IDCiselnik;
	Integer fk1IDCiselnik;

	Integer ciselnikStlpecID;

	String fk1FkNazov;

	DTOCiselnik ciselnikDTO;
	DTOCiselnik fk1CiselnikDTO;

	DTOCiselnikStlpec ciselnikStlpecDTO;
	DTOCiselnikStlpec fk1FkDTO;

	public DTOCiselnik getCiselnikDTO() {
		return ciselnikDTO;
	}

	public void setCiselnikDTO(DTOCiselnik ciselnikDTO) {
		this.ciselnikDTO = ciselnikDTO;
	}

	public DTOCiselnik getFk1CiselnikDTO() {
		return fk1CiselnikDTO;
	}

	public void setFk1CiselnikDTO(DTOCiselnik fk1CiselnikDTO) {
		this.fk1CiselnikDTO = fk1CiselnikDTO;
	}

	public DTOCiselnikStlpec getFk1FkDTO() {
		return fk1FkDTO;
	}

	public void setFk1FkDTO(DTOCiselnikStlpec fk1FkDTO) {
		this.fk1FkDTO = fk1FkDTO;
	}

	public DTOCiselnikStlpec getCiselnikStlpecDTO() {
		return ciselnikStlpecDTO;
	}

	public void setCiselnikStlpecDTO(DTOCiselnikStlpec ciselnikStlpecDTO) {
		this.ciselnikStlpecDTO = ciselnikStlpecDTO;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getFk1IDCiselnik() {
		return fk1IDCiselnik;
	}

	public void setFk1IDCiselnik(Integer fk1idCiselnik) {
		fk1IDCiselnik = fk1idCiselnik;
	}

	public Integer getCiselnikStlpecID() {
		return ciselnikStlpecID;
	}

	public void setCiselnikStlpecID(Integer ciselnikStlpecID) {
		this.ciselnikStlpecID = ciselnikStlpecID;
	}

	public String getFk1FkNazov() {
		return fk1FkNazov;
	}

	public void setFk1FkNazov(String fk1FkNazov) {
		this.fk1FkNazov = fk1FkNazov;
	}

}
