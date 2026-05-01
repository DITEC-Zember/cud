package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudZmenaStlpecLDWS")
public class DTOZmenaStlpecLD extends DTO {

	Integer zmenaID;
	Integer rowID;

	Integer ciselnikID;
	String ciselnikTabulka;

	String poznamka;

	Integer prevHistID;

	DTOCiselnikStlpecGui[] metaList;
	DTOZmenaStlpec[] zmenaStlpecList;

	public Integer getZmenaID() {
		return zmenaID;
	}

	public void setZmenaID(Integer zmenaID) {
		this.zmenaID = zmenaID;
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}

	public DTOCiselnikStlpecGui[] getMetaList() {
		return metaList;
	}

	public void setMetaList(DTOCiselnikStlpecGui[] metaList) {
		this.metaList = metaList;
	}

	public DTOZmenaStlpec[] getZmenaStlpecList() {
		return zmenaStlpecList;
	}

	public void setZmenaStlpecList(DTOZmenaStlpec[] zmenaStlpecList) {
		this.zmenaStlpecList = zmenaStlpecList;
	}

	public Integer getPrevHistID() {
		return prevHistID;
	}

	public void setPrevHistID(Integer prevHistID) {
		this.prevHistID = prevHistID;
	}

	public Integer getRowID() {
		return rowID;
	}

	public void setRowID(Integer rowID) {
		this.rowID = rowID;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

}
