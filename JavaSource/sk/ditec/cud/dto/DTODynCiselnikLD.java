package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudDynCiselnikLDWS")
public class DTODynCiselnikLD extends DTO {

	Integer histID;
	Integer ciselnikID;
	Date platnostOd;
	String ciselnikTabulka;
	String lookupColumnName;

	DTOCiselnikStlpecGui[] metaList;
	DTODynCiselnik valueDTO;

	public Integer getHistID() {
		return histID;
	}

	public void setHistID(Integer histID) {
		this.histID = histID;
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public String getLookupColumnName() {
		return lookupColumnName;
	}

	public void setLookupColumnName(String lookupColumnName) {
		this.lookupColumnName = lookupColumnName;
	}

	public DTOCiselnikStlpecGui[] getMetaList() {
		return metaList;
	}

	public void setMetaList(DTOCiselnikStlpecGui[] metaList) {
		this.metaList = metaList;
	}

	public DTODynCiselnik getValueDTO() {
		return valueDTO;
	}

	public void setValueDTO(DTODynCiselnik valueDTO) {
		this.valueDTO = valueDTO;
	}

}
