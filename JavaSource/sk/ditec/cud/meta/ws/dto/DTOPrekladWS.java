package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Preklad")
public class DTOPrekladWS {

	//CUD_PREKLAD_STLPEC - NAZOV_DB
	private String polozka;
	//CUD_PREKLAD_JAZYK - KOD
	private String jazyk;
	//CUD_PREKLAD - PREKLAD
	private String preklad;

	public String getPolozka() {
		return polozka;
	}

	public void setPolozka(String polozka) {
		this.polozka = polozka;
	}

	public String getJazyk() {
		return jazyk;
	}

	public void setJazyk(String jazyk) {
		this.jazyk = jazyk;
	}

	public String getPreklad() {
		return preklad;
	}

	public void setPreklad(String preklad) {
		this.preklad = preklad;
	}

}
