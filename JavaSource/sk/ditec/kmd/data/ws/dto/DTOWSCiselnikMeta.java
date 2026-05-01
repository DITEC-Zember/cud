package sk.ditec.kmd.data.ws.dto;

import java.util.List;

public class DTOWSCiselnikMeta {

	Integer ciselnikID;
	String tabulka;
	String nazov;
	String popis;
	String aktivny;

	List<DTOWSStlpec> stlpce;

	public String toString() {

		String s = "DTOCiselnik: {";
		s += "\n ciselnikID=" + ciselnikID;
		s += "\n tabulka=" + tabulka;
		s += "\n nazov=" + nazov;
		s += "\n popis=" + popis;
		s += "\n aktivny=" + aktivny;

		s += "}";
		return s;
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getTabulka() {
		return tabulka;
	}

	public void setTabulka(String tabulka) {
		this.tabulka = tabulka;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getPopis() {
		return popis;
	}

	public void setPopis(String popis) {
		this.popis = popis;
	}

	public String getAktivny() {
		return aktivny;
	}

	public void setAktivny(String aktivny) {
		this.aktivny = aktivny;
	}

	public List<DTOWSStlpec> getStlpce() {
		return stlpce;
	}

	public void setStlpce(List<DTOWSStlpec> stlpce) {
		this.stlpce = stlpce;
	}


}
