package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudMainHeadWS")
public class DTOMainHead extends DTO {

	Integer ciselnikID;
	String ciselnikTabulka;
	Date platnostOd;

	// lookup field
	DTOObjektStlpec[] objektStlpecList;
	String readObjektStlpecList;

	Integer pocet;
	Integer histID;

	@Override
	public String toString() {
		String s = "DTOMainHead: {";
		s += "\n ciselnikID=" + ciselnikID;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n platnostOd=" + platnostOd;
		s += "\n readObjektStlpecList=" + readObjektStlpecList;
		s += "\n pocet=" + pocet;
		s += "\n histID=" + histID;
		return s;
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public DTOObjektStlpec[] getObjektStlpecList() {
		return objektStlpecList;
	}

	public void setObjektStlpecList(DTOObjektStlpec[] objektStlpecList) {
		this.objektStlpecList = objektStlpecList;
	}

	public Integer getPocet() {
		return pocet;
	}

	public void setPocet(Integer pocet) {
		this.pocet = pocet;
	}

	public Integer getHistID() {
		return histID;
	}

	public void setHistID(Integer histID) {
		this.histID = histID;
	}

	public String getReadObjektStlpecList() {
		return readObjektStlpecList;
	}

	public void setReadObjektStlpecList(String readObjektStlpecList) {
		this.readObjektStlpecList = readObjektStlpecList;
	}

}
