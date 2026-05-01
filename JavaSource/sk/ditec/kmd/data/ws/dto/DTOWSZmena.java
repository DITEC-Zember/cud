package sk.ditec.kmd.data.ws.dto;

import java.util.ArrayList;
import java.util.Date;

public class DTOWSZmena {
	Integer zmenaID;
	Integer rowID;
	String stav;
	String operacia;
	Integer ciselnikID;
	String tabulka;
	Date platnostOd;

	ArrayList<DTOWSAtributZmeny> atributy;

	public Integer getZmenaID() {
		return zmenaID;
	}

	public void setZmenaID(Integer zmenaID) {
		this.zmenaID = zmenaID;
	}

	public Integer getRowID() {
		return rowID;
	}

	public void setRowID(Integer rowID) {
		this.rowID = rowID;
	}

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
	}

	public String getOperacia() {
		return operacia;
	}

	public void setOperacia(String operacia) {
		this.operacia = operacia;
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getTabulka() {
		return tabulka;
	}

	public void setTabulka(String tabulka) {
		this.tabulka = tabulka;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public ArrayList<DTOWSAtributZmeny> getAtributy() {
		return atributy;
	}

	public void setAtributy(ArrayList<DTOWSAtributZmeny> atributy) {
		this.atributy = atributy;
	}

}
