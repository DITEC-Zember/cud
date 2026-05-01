package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPrekladLDWS")
public class DTOPrekladLD extends DTO {

	Integer prekladID;
	Integer IDPrekladStlpec;

	DTOPreklad prekladDTO;
	DTOPrekladStlpec prekladStlpecDTO;

	public Integer getPrekladID() {
		return prekladID;
	}

	public void setPrekladID(Integer prekladID) {
		this.prekladID = prekladID;
	}

	public Integer getIDPrekladStlpec() {
		return IDPrekladStlpec;
	}

	public void setIDPrekladStlpec(Integer iDPrekladStlpec) {
		IDPrekladStlpec = iDPrekladStlpec;
	}

	public DTOPreklad getPrekladDTO() {
		return prekladDTO;
	}

	public void setPrekladDTO(DTOPreklad prekladDTO) {
		this.prekladDTO = prekladDTO;
	}

	public DTOPrekladStlpec getPrekladStlpecDTO() {
		return prekladStlpecDTO;
	}

	public void setPrekladStlpecDTO(DTOPrekladStlpec prekladStlpecDTO) {
		this.prekladStlpecDTO = prekladStlpecDTO;
	}

}
