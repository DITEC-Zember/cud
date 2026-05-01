package sk.ditec.cskmd.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ZmenaWrapper")
public class DTOZmenaWrapperWS {

	DTOZmenaWS[] zmenaList;
	Integer totalCount;

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

}
