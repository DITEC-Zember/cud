package sk.ditec.crd.dto;
import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;



@XmlType(name = "DTOCrdNenajdeneZaznamy")
public class DTOCrdNenajdeneZaznamy extends DTO {

	Integer crdNenajdeneZaznamyID; // CRD_NENAJDENE_ZAZNAMY_ID
	Integer idCrdSpracTabuliek; // ID_CRD_SPRAC_TABULIEK
	String popis; // POPIS
	String chybovaSprava; // CHYBOVA_SPRAVA
	String chyba; // CHYBA
	String varovanie; // VAROVANIE

	public String toString() {
		String s = "DTO: {";
		s += "\n crdNenajdeneZaznamyID=" + crdNenajdeneZaznamyID;
		s += "\n IDCrdSpracTabuliek=" + idCrdSpracTabuliek;
		s += "\n popis=" + popis;
		s += "\n chybovaSprava=" + chybovaSprava;
		s += "\n chyba=" + chyba;
		s += "\n varovanie=" + varovanie;
		s += "}";
		return s;
	}

	public Integer getCrdNenajdeneZaznamyID() {
		return crdNenajdeneZaznamyID;
	}

	public void setCrdNenajdeneZaznamyID(Integer crdNenajdeneZaznamyID) {
		this.crdNenajdeneZaznamyID = crdNenajdeneZaznamyID;
	}

	public Integer getIdCrdSpracTabuliek() {
		return idCrdSpracTabuliek;
	}

	public void setIdCrdSpracTabuliek(Integer idCrdSpracTabuliek) {
		this.idCrdSpracTabuliek = idCrdSpracTabuliek;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
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

	public String getVarovanie() {
		return varovanie;
	}

	public void setVarovanie(String varovanie) {
		this.varovanie = varovanie;
	}

}
