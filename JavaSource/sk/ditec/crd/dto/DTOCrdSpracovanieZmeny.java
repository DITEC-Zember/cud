package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;


@XmlType(name = "DTOCrdSpracTabuliek")
public class DTOCrdSpracovanieZmeny extends DTO {
	// Table name : CRD_SPRACOVANIE_ZMENY

	Integer crdSpracovanieZmenyID; // CRD_SPRACOVANIE_ZMENY_ID
	Integer IDZmena; // ID_ZMENA
	Date datumVolania; // DATUM_VOLANIA
	String chybovaSprava; // CHYBOVA_SPRAVA
	String chyba; // CHYBA

	public String toString() {
		String s = "DTO: {";
		s += "\n crdSpracovanieZmenyID=" + crdSpracovanieZmenyID;
		s += "\n IDZmena=" + IDZmena;
		s += "\n datumVolania=" + datumVolania;
		s += "\n chybovaSprava=" + chybovaSprava;
		s += "\n chyba=" + chyba;
		s += "}";
		return s;
	}

	public Integer getCrdSpracovanieZmenyID() {
		return crdSpracovanieZmenyID;
	}

	public void setCrdSpracovanieZmenyID(Integer crdSpracovanieZmenyID) {
		this.crdSpracovanieZmenyID = crdSpracovanieZmenyID;
	}

	public Integer getIDZmena() {
		return IDZmena;
	}

	public void setIDZmena(Integer iDZmena) {
		IDZmena = iDZmena;
	}

	public Date getDatumVolania() {
		return datumVolania;
	}

	public void setDatumVolania(Date datumVolania) {
		this.datumVolania = datumVolania;
	}

	public String getChybovaSprava() {
		return chybovaSprava;
	}

	public void setChybovaSprava(String chybovaSprava) {
		this.chybovaSprava = chybovaSprava;
	}

	public String getChyba() {
		return chyba;
	}

	public void setChyba(String chyba) {
		this.chyba = chyba;
	}


}
