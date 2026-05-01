package sk.ditec.cud.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ZmenaCiselnikArray")
public class DTOZmenaCiselnikArrayWS {

	private Integer[] ids;
	private String errorMsg;

	public Integer[] getIds() {
		return ids;
	}

	public void setIds(Integer[] ids) {
		this.ids = ids;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
