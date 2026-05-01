package sk.ditec.cud.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ZmenaWrapper")
public class DTOZmenaWrapperWS {

	private DTOZmenaWS[] zmenaList;
	private Integer totalCount;
	private String errorMsg;

	public DTOZmenaWS[] getZmenaList() {
		return zmenaList;
	}

	public void setZmenaList(DTOZmenaWS[] zmenaList) {
		this.zmenaList = zmenaList;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
