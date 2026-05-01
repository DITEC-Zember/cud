package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "TupdCiselnikMeta")
public class DTOUpdCiselnikMetaWS {

	//delCiselnik.tabulka
	private DTODelCiselnikWS dtoDelCiselnikWS;

	//updCiselnik
	private DTOUpdCiselnikWS[] dtoUpdCiselnikWS;

	@XmlElement(name = "delCiselnik")
	public DTODelCiselnikWS getDtoDelCiselnikWS() {
		return dtoDelCiselnikWS;
	}

	public void setDtoDelCiselnikWS(DTODelCiselnikWS dtoDelCiselnikWS) {
		this.dtoDelCiselnikWS = dtoDelCiselnikWS;
	}

	@XmlElement(name = "updCiselnik")
	public DTOUpdCiselnikWS[] getDtoUpdCiselnikWS() {
		return dtoUpdCiselnikWS;
	}

	public void setDtoUpdCiselnikWS(DTOUpdCiselnikWS[] dtoUpdCiselnikWS) {
		this.dtoUpdCiselnikWS = dtoUpdCiselnikWS;
	}

}
