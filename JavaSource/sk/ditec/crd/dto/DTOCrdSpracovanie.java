package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;




@XmlType(name = "DTOCrdSpracovanie")
public class DTOCrdSpracovanie extends DTO {

	// Table name : CRD_SPRACOVANIE

	Integer crdSpracovanieId; // CRD_SPRACOVANIE_ID
	Date datumVolania; // DATUM_VOLANIA
	String kodSpracovania; // KOD_SPRACOVANIA
	Date posledneUspesneSpracovanie; // POSLEDNE_USPESNE_SPRACOVANIE

	public String toString() {
		String s = "DTO: {";
		s += "\n crdSpracovanieID=" + crdSpracovanieId;
		s += "\n datumVolania=" + datumVolania;
		s += "\n kodSpracovania=" + kodSpracovania;
		s += "\n posledneUspesneSpracovanie=" + posledneUspesneSpracovanie;
		s += "}";
		return s;
	}

	public Integer getCrdSpracovanieId() {
		return crdSpracovanieId;
	}

	public void setCrdSpracovanieId(Integer crdSpracovanieId) {
		this.crdSpracovanieId = crdSpracovanieId;
	}

	public Date getDatumVolania() {
		return datumVolania;
	}

	public void setDatumVolania(Date datumVolania) {
		this.datumVolania = datumVolania;
	}

	public String getKodSpracovania() {
		return kodSpracovania;
	}

	public void setKodSpracovania(String kodSpracovania) {
		this.kodSpracovania = kodSpracovania;
	}

	public Date getPosledneUspesneSpracovanie() {
		return posledneUspesneSpracovanie;
	}

	public void setPosledneUspesneSpracovanie(Date posledneUspesneSpracovanie) {
		this.posledneUspesneSpracovanie = posledneUspesneSpracovanie;
	}


}
