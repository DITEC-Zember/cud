package sk.ditec.cud.dto;

import java.util.Date;

public class DTOWfNotif {

	Integer ciselnikID;
	String ciselnikNazov;

	String zmenaOperacia;

	String poznamka;

	Date platnostOd;

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public String getZmenaOperacia() {
		return zmenaOperacia;
	}

	public void setZmenaOperacia(String zmenaOperacia) {
		this.zmenaOperacia = zmenaOperacia;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

}
