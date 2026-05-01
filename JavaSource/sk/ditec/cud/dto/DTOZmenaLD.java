package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "DTOCudZmenaLDWS")
public class DTOZmenaLD extends DTOZmenaStlpecLD {

	Date platnostOd;

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

}
