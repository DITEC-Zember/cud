package sk.ditec.cud.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "UpdZmenaHodnotCiselnika")
public class DTOUpdZmenaWS {

	String ciselnikNazov;
	String zapisatZmeny;

	DTOUpdZaznamWS zaznam;

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}

	public String getZapisatZmeny() {
		return zapisatZmeny;
	}

	public void setZapisatZmeny(String zapisatZmeny) {
		this.zapisatZmeny = zapisatZmeny;
	}

	public DTOUpdZaznamWS getZaznam() {
		return zaznam;
	}

	public void setZaznam(DTOUpdZaznamWS zaznam) {
		this.zaznam = zaznam;
	}

}
