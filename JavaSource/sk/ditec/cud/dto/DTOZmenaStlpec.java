package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudZmenaStlpecWS")
public class DTOZmenaStlpec extends DTO {

	Integer zmenaStlpecID;
	Integer IDCiselnik;
	Integer IDZmena;
	Integer IDCiselnikStlpec;
	String oldValue;
	String newValue;

	// lookup field
	String ciselnikStlpecNazov;

	Integer zmenaRowID;
	String zmenaOperacia;
	Date zmenaPlatnostOd;

	@Override
	public String toString() {
		String s = "DTOZmenaStlpec: {";
		s += "\n zmenaStlpecID=" + zmenaStlpecID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n IDZmena=" + IDZmena;
		s += "\n IDCiselnikStlpec=" + IDCiselnikStlpec;
		s += "\n oldValue=" + oldValue;
		s += "\n newValue=" + newValue;
		s += "\n ciselnikStlpecNazov=" + ciselnikStlpecNazov;
		s += "\n zmenaRowID=" + zmenaRowID;
		s += "\n zmenaOperacia=" + zmenaOperacia;
		s += "\n zmenaPlatnostOd=" + zmenaPlatnostOd;
		return s;
	}

	public Integer getZmenaStlpecID() {
		return zmenaStlpecID;
	}

	public void setZmenaStlpecID(Integer zmenaStlpecID) {
		this.zmenaStlpecID = zmenaStlpecID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getIDZmena() {
		return IDZmena;
	}

	public void setIDZmena(Integer iDZmena) {
		IDZmena = iDZmena;
	}

	public Integer getIDCiselnikStlpec() {
		return IDCiselnikStlpec;
	}

	public void setIDCiselnikStlpec(Integer iDCiselnikStlpec) {
		IDCiselnikStlpec = iDCiselnikStlpec;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getCiselnikStlpecNazov() {
		return ciselnikStlpecNazov;
	}

	public void setCiselnikStlpecNazov(String ciselnikStlpecNazov) {
		this.ciselnikStlpecNazov = ciselnikStlpecNazov;
	}

	public Integer getZmenaRowID() {
		return zmenaRowID;
	}

	public void setZmenaRowID(Integer zmenaRowID) {
		this.zmenaRowID = zmenaRowID;
	}

	public String getZmenaOperacia() {
		return zmenaOperacia;
	}

	public void setZmenaOperacia(String zmenaOperacia) {
		this.zmenaOperacia = zmenaOperacia;
	}

	public Date getZmenaPlatnostOd() {
		return zmenaPlatnostOd;
	}

	public void setZmenaPlatnostOd(Date zmenaPlatnostOd) {
		this.zmenaPlatnostOd = zmenaPlatnostOd;
	}

}
