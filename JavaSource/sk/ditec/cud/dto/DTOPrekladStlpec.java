package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudPrekladStlpecWS")
public class DTOPrekladStlpec extends DTO {

	Integer prekladStlpecID;
	Integer IDPrekladTabulka;
	String nazovDb;
	String nadpis;

	// lookup field
	String prekladTabulkaNazovDB;

	@Override
	public String toString() {
		String s = "DTOPrekladStlpec: {";
		s += "\n prekladStlpecID=" + prekladStlpecID;
		s += "\n IDPrekladTabulka=" + IDPrekladTabulka;
		s += "\n nazovDb=" + nazovDb;
		s += "\n nadpis=" + nadpis;
		s += "\n prekladTabulkaNazovDB=" + prekladTabulkaNazovDB;
		return s;
	}

	public Integer getPrekladStlpecID() {
		return prekladStlpecID;
	}

	public void setPrekladStlpecID(Integer prekladStlpecID) {
		this.prekladStlpecID = prekladStlpecID;
	}

	public Integer getIDPrekladTabulka() {
		return IDPrekladTabulka;
	}

	public void setIDPrekladTabulka(Integer iDPrekladTabulka) {
		IDPrekladTabulka = iDPrekladTabulka;
	}

	public String getNazovDb() {
		return nazovDb;
	}

	public void setNazovDb(String nazovDb) {
		this.nazovDb = nazovDb;
	}

	public String getNadpis() {
		return nadpis;
	}

	public void setNadpis(String nadpis) {
		this.nadpis = nadpis;
	}

	public String getPrekladTabulkaNazovDB() {
		return prekladTabulkaNazovDB;
	}

	public void setPrekladTabulkaNazovDB(String prekladTabulkaNazovDB) {
		this.prekladTabulkaNazovDB = prekladTabulkaNazovDB;
	}

}