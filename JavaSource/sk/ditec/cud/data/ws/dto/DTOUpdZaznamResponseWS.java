package sk.ditec.cud.data.ws.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "UpdZmenaHodnotCiselnikaResponse")
public class DTOUpdZaznamResponseWS {

	Date datumSpracovania;
	Integer navratovyKod;
	String popisSpracovania;

	DTOUpdZmenaResponseWS[] zaznamy;

	public Date getDatumSpracovania() {
		return datumSpracovania;
	}

	public void setDatumSpracovania(Date datumSpracovania) {
		this.datumSpracovania = datumSpracovania;
	}

	public Integer getNavratovyKod() {
		return navratovyKod;
	}

	public void setNavratovyKod(Integer navratovyKod) {
		this.navratovyKod = navratovyKod;
	}

	public String getPopisSpracovania() {
		return popisSpracovania;
	}

	public void setPopisSpracovania(String popisSpracovania) {
		this.popisSpracovania = popisSpracovania;
	}

	public DTOUpdZmenaResponseWS[] getZaznamy() {
		return zaznamy;
	}

	public void setZaznamy(DTOUpdZmenaResponseWS[] zaznamy) {
		this.zaznamy = zaznamy;
	}

}
