package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudCiselnikStlpecGuiLDWS")
public class DTOCiselnikStlpecGuiLD extends DTO {

	Integer ciselnikStlpecGuiID;
	Integer ciselnikStlpecID;

	Integer fk1IDCiselnik;
	String fk1FkNazov;

	Integer fk2IDCiselnik;
	String fk2FkNazov;

	DTOCiselnikStlpec ciselnikStlpecDTO;
	DTOCiselnikStlpec fk1FkCiselnikStlpecDTO;
	DTOCiselnikStlpec fk2FkCiselnikStlpecDTO;

	DTOCiselnik fk1CiselnikDTO;
	DTOCiselnik fk2CiselnikDTO;

	DTOCiselnikStlpecGui ciselnikStlpecGuiDTO;

	public Integer getCiselnikStlpecID() {
		return ciselnikStlpecID;
	}

	public void setCiselnikStlpecID(Integer ciselnikStlpecID) {
		this.ciselnikStlpecID = ciselnikStlpecID;
	}

	public Integer getFk1IDCiselnik() {
		return fk1IDCiselnik;
	}

	public void setFk1IDCiselnik(Integer fk1idCiselnik) {
		fk1IDCiselnik = fk1idCiselnik;
	}

	public String getFk1FkNazov() {
		return fk1FkNazov;
	}

	public void setFk1FkNazov(String fk1FkNazov) {
		this.fk1FkNazov = fk1FkNazov;
	}

	public Integer getFk2IDCiselnik() {
		return fk2IDCiselnik;
	}

	public void setFk2IDCiselnik(Integer fk2idCiselnik) {
		fk2IDCiselnik = fk2idCiselnik;
	}

	public String getFk2FkNazov() {
		return fk2FkNazov;
	}

	public void setFk2FkNazov(String fk2FkNazov) {
		this.fk2FkNazov = fk2FkNazov;
	}

	public DTOCiselnikStlpec getCiselnikStlpecDTO() {
		return ciselnikStlpecDTO;
	}

	public void setCiselnikStlpecDTO(DTOCiselnikStlpec ciselnikStlpecDTO) {
		this.ciselnikStlpecDTO = ciselnikStlpecDTO;
	}

	public DTOCiselnikStlpec getFk1FkCiselnikStlpecDTO() {
		return fk1FkCiselnikStlpecDTO;
	}

	public void setFk1FkCiselnikStlpecDTO(DTOCiselnikStlpec fk1FkCiselnikStlpecDTO) {
		this.fk1FkCiselnikStlpecDTO = fk1FkCiselnikStlpecDTO;
	}

	public DTOCiselnikStlpec getFk2FkCiselnikStlpecDTO() {
		return fk2FkCiselnikStlpecDTO;
	}

	public void setFk2FkCiselnikStlpecDTO(DTOCiselnikStlpec fk2FkCiselnikStlpecDTO) {
		this.fk2FkCiselnikStlpecDTO = fk2FkCiselnikStlpecDTO;
	}

	public DTOCiselnik getFk1CiselnikDTO() {
		return fk1CiselnikDTO;
	}

	public void setFk1CiselnikDTO(DTOCiselnik fk1CiselnikDTO) {
		this.fk1CiselnikDTO = fk1CiselnikDTO;
	}

	public DTOCiselnik getFk2CiselnikDTO() {
		return fk2CiselnikDTO;
	}

	public void setFk2CiselnikDTO(DTOCiselnik fk2CiselnikDTO) {
		this.fk2CiselnikDTO = fk2CiselnikDTO;
	}

	public Integer getCiselnikStlpecGuiID() {
		return ciselnikStlpecGuiID;
	}

	public void setCiselnikStlpecGuiID(Integer ciselnikStlpecGuiID) {
		this.ciselnikStlpecGuiID = ciselnikStlpecGuiID;
	}

	public DTOCiselnikStlpecGui getCiselnikStlpecGuiDTO() {
		return ciselnikStlpecGuiDTO;
	}

	public void setCiselnikStlpecGuiDTO(DTOCiselnikStlpecGui ciselnikStlpecGuiDTO) {
		this.ciselnikStlpecGuiDTO = ciselnikStlpecGuiDTO;
	}

}
