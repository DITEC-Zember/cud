package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudCiselnikGuiWS")
public class DTOCiselnikGui extends DTO {

	Integer ciselnikGuiID;
	Integer IDCiselnik;
	String stav;
	Date platnostOd;
	Date platnostDo;
	Date casPublikovania;
	String popis;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	String ciselnikNazov;
	String ciselnikAktivny;

	Date platnostOdOd;
	Date platnostOdDo;
	Date platnostDoOd;
	Date platnostDoDo;
	Date casPublikovaniaOd;
	Date casPublikovaniaDo;

	@Override
	public String toString() {
		String s = "DTOCiselnikGui: {";
		s += "\n ciselnikGuiID=" + ciselnikGuiID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n stav=" + stav;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casPublikovania=" + casPublikovania;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n ciselnikAktivny=" + ciselnikAktivny;
		s += "\n platnostOdOd=" + platnostOdOd;
		s += "\n platnostOdDo=" + platnostOdDo;
		s += "\n platnostDoOd=" + platnostDoOd;
		s += "\n platnostDoDo=" + platnostDoDo;
		s += "\n casPublikovaniaOd=" + casPublikovaniaOd;
		s += "\n casPublikovaniaDo=" + casPublikovaniaDo;
		return s;
	}

	public Integer getCiselnikGuiID() {
		return ciselnikGuiID;
	}

	public void setCiselnikGuiID(Integer ciselnikGuiID) {
		this.ciselnikGuiID = ciselnikGuiID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public Date getPlatnostOdOd() {
		return platnostOdOd;
	}

	public void setPlatnostOdOd(Date platnostOdOd) {
		this.platnostOdOd = platnostOdOd;
	}

	public Date getPlatnostOdDo() {
		return platnostOdDo;
	}

	public void setPlatnostOdDo(Date platnostOdDo) {
		this.platnostOdDo = platnostOdDo;
	}

	public Date getPlatnostDo() {
		return platnostDo;
	}

	public void setPlatnostDo(Date platnostDo) {
		this.platnostDo = platnostDo;
	}

	public Date getPlatnostDoOd() {
		return platnostDoOd;
	}

	public void setPlatnostDoOd(Date platnostDoOd) {
		this.platnostDoOd = platnostDoOd;
	}

	public Date getPlatnostDoDo() {
		return platnostDoDo;
	}

	public void setPlatnostDoDo(Date platnostDoDo) {
		this.platnostDoDo = platnostDoDo;
	}

	public Date getCasPublikovania() {
		return casPublikovania;
	}

	public void setCasPublikovania(Date casPublikovania) {
		this.casPublikovania = casPublikovania;
	}

	public Date getCasPublikovaniaOd() {
		return casPublikovaniaOd;
	}

	public void setCasPublikovaniaOd(Date casPublikovaniaOd) {
		this.casPublikovaniaOd = casPublikovaniaOd;
	}

	public Date getCasPublikovaniaDo() {
		return casPublikovaniaDo;
	}

	public void setCasPublikovaniaDo(Date casPublikovaniaDo) {
		this.casPublikovaniaDo = casPublikovaniaDo;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public String getCiselnikAktivny() {
		return ciselnikAktivny;
	}

	public void setCiselnikAktivny(String ciselnikAktivny) {
		this.ciselnikAktivny = ciselnikAktivny;
	}

	public Date getCasZmeny() {
		return casZmeny;
	}

	public void setCasZmeny(Date casZmeny) {
		this.casZmeny = casZmeny;
	}

	public Integer getIDUcet() {
		return IDUcet;
	}

	public void setIDUcet(Integer iDUcet) {
		IDUcet = iDUcet;
	}

}
