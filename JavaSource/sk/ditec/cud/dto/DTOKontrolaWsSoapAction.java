package sk.ditec.cud.dto;

import java.util.Arrays;

import sk.ditec.common.utils.StringUtils;

public class DTOKontrolaWsSoapAction {

	String soapActionNazov;;
	String[] errList;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOKontrolaWsSoapAction: {";
		s += "\n soapActionNazov=" + soapActionNazov;
		s += "\n errList=" + (StringUtils.isValid(errList) ? Arrays.toString(errList) : "");
		s += "}";
		return s;
	}

	public String getSoapActionNazov() {
		return soapActionNazov;
	}

	public void setSoapActionNazov(String soapActionNazov) {
		this.soapActionNazov = soapActionNazov;
	}

	public String[] getErrList() {
		return errList;
	}

	public void setErrList(String[] errList) {
		this.errList = errList;
	}

}
