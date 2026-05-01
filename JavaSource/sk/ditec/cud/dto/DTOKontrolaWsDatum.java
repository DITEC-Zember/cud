package sk.ditec.cud.dto;

import sk.ditec.common.utils.StringUtils;

public class DTOKontrolaWsDatum {

	String datum;
	DTOKontrolaWsSoapAction[] soapActionList;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOKontrolaWsDatum: {";
		s += "\n datum=" + datum;
		s += "\n soapActionList.count=" + (StringUtils.isValid(soapActionList) ? soapActionList.length : 0);
		s += "}";
		return s;
	}

	public String getDatum() {
		return datum;
	}

	public void setDatum(String datum) {
		this.datum = datum;
	}

	public DTOKontrolaWsSoapAction[] getSoapActionList() {
		return soapActionList;
	}

	public void setSoapActionList(DTOKontrolaWsSoapAction[] soapActionList) {
		this.soapActionList = soapActionList;
	}

}
