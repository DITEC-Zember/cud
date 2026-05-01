package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudUzamknutieWS")
public class DTOUzamknutie extends DTO {

	Integer uzamknutieID;
	Integer IDCiselnik;
	Integer rowID;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOUzamknutie: {";
		s += "\n uzamknutieID=" + uzamknutieID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n rowID=" + rowID;
		return s;
	}

	public Integer getUzamknutieID() {
		return uzamknutieID;
	}

	public void setUzamknutieID(Integer uzamknutieID) {
		this.uzamknutieID = uzamknutieID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public Integer getRowID() {
		return rowID;
	}

	public void setRowID(Integer rowID) {
		this.rowID = rowID;
	}

}
