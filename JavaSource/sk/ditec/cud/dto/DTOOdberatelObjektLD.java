package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudOdberatelObjektLDWS")
public class DTOOdberatelObjektLD extends DTO {

	Integer odberatelObjektID;
	Integer IDObjekt;
	Integer IDOdberatel;

	DTOOdberatelObjekt odberatelObjektDTO;
	DTOObjekt objektDTO;
	DTOOdberatel odberatelDTO;

	public Integer getOdberatelObjektID() {
		return odberatelObjektID;
	}

	public void setOdberatelObjektID(Integer odberatelObjektID) {
		this.odberatelObjektID = odberatelObjektID;
	}

	public Integer getIDObjekt() {
		return IDObjekt;
	}

	public void setIDObjekt(Integer iDObjekt) {
		IDObjekt = iDObjekt;
	}

	public Integer getIDOdberatel() {
		return IDOdberatel;
	}

	public void setIDOdberatel(Integer iDOdberatel) {
		IDOdberatel = iDOdberatel;
	}

	public DTOOdberatelObjekt getOdberatelObjektDTO() {
		return odberatelObjektDTO;
	}

	public void setOdberatelObjektDTO(DTOOdberatelObjekt odberatelObjektDTO) {
		this.odberatelObjektDTO = odberatelObjektDTO;
	}

	public DTOObjekt getObjektDTO() {
		return objektDTO;
	}

	public void setObjektDTO(DTOObjekt objektDTO) {
		this.objektDTO = objektDTO;
	}

	public DTOOdberatel getOdberatelDTO() {
		return odberatelDTO;
	}

	public void setOdberatelDTO(DTOOdberatel odberatelDTO) {
		this.odberatelDTO = odberatelDTO;
	}

}
