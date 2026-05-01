package sk.ditec.kmd.data.ws.dto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DTOWSCiselnik {

	Integer ciselnikID;
	String ciselnikName;
	String ciselnikNazov;

	ArrayList<DTOWSStlpec> stlpce;

	ArrayList<DTOCiselnikRecord> data;

	public Object clone() throws CloneNotSupportedException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			return super.clone();
		}
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getCiselnikName() {
		return ciselnikName;
	}

	public void setCiselnikName(String ciselnikName) {
		this.ciselnikName = ciselnikName;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public ArrayList<DTOWSStlpec> getStlpce() {
		return stlpce;
	}

	public void setStlpce(ArrayList<DTOWSStlpec> stlpce) {
		this.stlpce = stlpce;
	}

	public ArrayList<DTOCiselnikRecord> getData() {
		return data;
	}

	public void setData(ArrayList<DTOCiselnikRecord> data) {
		this.data = data;
	}


}
