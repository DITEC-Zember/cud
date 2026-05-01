package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPocetnostWS")
public class DTOPocetnost extends DTO {

	Integer pocetnostID;
	String ciselnikNazov;
	Integer pocet;

	// lookup field
	Integer IDCiselnik;
	String ciselnikTabulka;
	Date platnostOd;
	Date platnostDo;
	Integer rowID;

	DTOCiselnikStlpec[] ciselnikStlpecList;

	@Override
	public String toString() {
		String s = "DTOPocetnost: {";
		s += "\n pocetnostID=" + pocetnostID;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n pocet=" + pocet;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n rowID=" + rowID;
		return s;
	}

	public Integer getPocetnostID() {
		return pocetnostID;
	}

	public void setPocetnostID(Integer pocetnostID) {
		this.pocetnostID = pocetnostID;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public Integer getPocet() {
		return pocet;
	}

	public void setPocet(Integer pocet) {
		this.pocet = pocet;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
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

	public Date getPlatnostDo() {
		return platnostDo;
	}

	public void setPlatnostDo(Date platnostDo) {
		this.platnostDo = platnostDo;
	}

	public DTOCiselnikStlpec[] getCiselnikStlpecList() {
		return ciselnikStlpecList;
	}

	public void setCiselnikStlpecList(DTOCiselnikStlpec[] ciselnikStlpecList) {
		this.ciselnikStlpecList = ciselnikStlpecList;
	}

}
