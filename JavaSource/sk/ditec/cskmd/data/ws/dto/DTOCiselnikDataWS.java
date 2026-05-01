package sk.ditec.cskmd.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "CiselnikData")
public class DTOCiselnikDataWS {

	Integer ciselnikID;
	String ciselnikName;
	String ciselnikNazov;

	DTOCiselnikStlpecWS[] ciselnikStlpecList;

	DTORecordWS[] data;

	Integer totalCount;

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

	public DTOCiselnikStlpecWS[] getCiselnikStlpecList() {
		return ciselnikStlpecList;
	}

	public void setCiselnikStlpecList(DTOCiselnikStlpecWS[] ciselnikStlpecList) {
		this.ciselnikStlpecList = ciselnikStlpecList;
	}

	public DTORecordWS[] getData() {
		return data;
	}

	public void setData(DTORecordWS[] data) {
		this.data = data;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

}
