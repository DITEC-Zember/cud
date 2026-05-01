package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudDynValueWS")
public class DTODynValue extends DTO {

	Integer valueID;
	String valueStr;

	// lookup field

	@Override
	public String toString() {
		String s = "DTODynValue: {";
		s += "\n valueID=" + valueID;
		s += "\n valueStr=" + valueStr;
		return s;
	}

	public Integer getValueID() {
		return valueID;
	}

	public void setValueID(Integer valueID) {
		this.valueID = valueID;
	}

	public String getValueStr() {
		return valueStr;
	}

	public void setValueStr(String valueStr) {
		this.valueStr = valueStr;
	}

}
