package sk.ditec.cud.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CiselnikData")
public class DTOCiselnikDataWS {

	private Integer ciselnikID;
	private String ciselnikName;
	private String ciselnikNazov;

	private Integer totalCount;
	private String errorMsg;

	private DTOCiselnikStlpecMetaWS[] ciselnikStlpecList;

	private DTORecordWS[] recordList;

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getCiselnikName() {
		return ciselnikName;
	}

	public void setCiselnikName(String ciselnikName) {
		this.ciselnikName = ciselnikName;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public DTOCiselnikStlpecMetaWS[] getCiselnikStlpecList() {
		return ciselnikStlpecList;
	}

	public void setCiselnikStlpecList(DTOCiselnikStlpecMetaWS[] ciselnikStlpecList) {
		this.ciselnikStlpecList = ciselnikStlpecList;
	}

	public DTORecordWS[] getRecordList() {
		return recordList;
	}

	public void setRecordList(DTORecordWS[] recordList) {
		this.recordList = recordList;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
