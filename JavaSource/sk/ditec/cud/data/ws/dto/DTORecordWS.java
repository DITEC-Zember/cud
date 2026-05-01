package sk.ditec.cud.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Record")
public class DTORecordWS {

	private Object[] values;

	public Object[] getValues() {
		return values;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

}
