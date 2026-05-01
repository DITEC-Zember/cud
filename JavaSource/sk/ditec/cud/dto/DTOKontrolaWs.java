package sk.ditec.cud.dto;

import sk.ditec.common.utils.StringUtils;

public class DTOKontrolaWs {

	String zdrojUrl;
	String testUrl;

	DTOKontrolaWsDatum[] kontrolaWsDatumList;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOKontrolaWs: {";
		s += "\n zdrojUrl=" + zdrojUrl;
		s += "\n testUrl=" + testUrl;
		s += "\n kontrolaWsDatumList.count=" + (StringUtils.isValid(kontrolaWsDatumList) ? kontrolaWsDatumList.length : 0);
		s += "}";
		return s;

	}

	public String getZdrojUrl() {
		return zdrojUrl;
	}

	public void setZdrojUrl(String zdrojUrl) {
		this.zdrojUrl = zdrojUrl;
	}

	public String getTestUrl() {
		return testUrl;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public DTOKontrolaWsDatum[] getKontrolaWsDatumList() {
		return kontrolaWsDatumList;
	}

	public void setKontrolaWsDatumList(DTOKontrolaWsDatum[] kontrolaWsDatumList) {
		this.kontrolaWsDatumList = kontrolaWsDatumList;
	}
}