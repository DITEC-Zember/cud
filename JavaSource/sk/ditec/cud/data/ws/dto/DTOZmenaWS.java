package sk.ditec.cud.data.ws.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Zmena")
public class DTOZmenaWS {

	private Integer zmenaID;
	private Date schvalenie;
	private Date publikovanie;
	private Integer ciselnikID;
	private String operacia;
	private Date platnostOd;
	private Integer rowID;
	private String stav;
	private String tabulka;

	private DTOZmenaStlpecWS[] zmenaStlpecList;

	public Integer getZmenaID() {
		return zmenaID;
	}

	public void setZmenaID(Integer zmenaID) {
		this.zmenaID = zmenaID;
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

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getOperacia() {
		return operacia;
	}

	public void setOperacia(String operacia) {
		this.operacia = operacia;
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

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
	}

	public String getTabulka() {
		return tabulka;
	}

	public void setTabulka(String tabulka) {
		this.tabulka = tabulka;
	}

	public DTOZmenaStlpecWS[] getZmenaStlpecList() {
		return zmenaStlpecList;
	}

	public void setZmenaStlpecList(DTOZmenaStlpecWS[] zmenaStlpecList) {
		this.zmenaStlpecList = zmenaStlpecList;
	}

}
