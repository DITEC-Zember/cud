package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "delCiselnik")
public class DTODelCiselnikWS {

	//delCiselnik.tabulka
	private String[] tabulka;

	public String[] getTabulka() {
		return tabulka;
	}

	public void setTabulka(String[] tabulka) {
		this.tabulka = tabulka;
	}
}
