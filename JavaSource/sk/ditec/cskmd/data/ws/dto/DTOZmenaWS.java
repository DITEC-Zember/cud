package sk.ditec.cskmd.data.ws.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Zmena")
public class DTOZmenaWS {

	Integer zmenaID;
	Integer rowID;
	String stav;
	String operacia;
	Integer IDCiselnik;
	String tabulka;
	Date platnostOd;
	Date schvalenie;
	Date publikovanie;

	DTOZmenaStlpecWS[] zmenaStlpecList;

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

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
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

	public DTOZmenaStlpecWS[] getZmenaStlpecList() {
		return zmenaStlpecList;
	}

	public void setZmenaStlpecList(DTOZmenaStlpecWS[] zmenaStlpecList) {
		this.zmenaStlpecList = zmenaStlpecList;
	}

	public Date getSchvalenie() {
		return schvalenie;
	}

	public void setSchvalenie(Date schvalenie) {
		this.schvalenie = schvalenie;
	}

	public Date getPublikovanie() {
		return publikovanie;
	}

	public void setPublikovanie(Date publikovanie) {
		this.publikovanie = publikovanie;
	}

}