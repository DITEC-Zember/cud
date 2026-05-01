package sk.ditec.cud.data.ws.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "UdpZaznam")
public class DTOUpdZaznamWS {

	Integer ID;
	String operacia;
	Date platnostOd;
	Date datumSchvalenia;
	String poznamka;

	DTOUpdStlpecWS[] stlpce;

	public Integer getID() {
		return ID;
	}

	public void setID(Integer iD) {
		ID = iD;
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

	public Date getDatumSchvalenia() {
		return datumSchvalenia;
	}

	public void setDatumSchvalenia(Date datumSchvalenia) {
		this.datumSchvalenia = datumSchvalenia;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}

	public DTOUpdStlpecWS[] getStlpce() {
		return stlpce;
	}

	public void setStlpce(DTOUpdStlpecWS[] stlpce) {
		this.stlpce = stlpce;
	}

}
