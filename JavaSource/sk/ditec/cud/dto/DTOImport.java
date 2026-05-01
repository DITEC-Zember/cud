package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudImportWS")
public class DTOImport extends DTO {

	Integer importID;
	Integer IDCiselnik;
	String ciselnikTabulka;
	String ciselnikNazov;
	String stav;
	Date casVytvorenia;
	Date casKontrolaZac;
	Date casKontrolaKon;
	Date casImportZac;
	Date casImportKon;

	// lookup field
	String importPrilohaFileName;

	String errors;
	String columns;

	DTOImportZmena[] importZmenaList;

	String zdroj;

	@Override
	public String toString() {
		String s = "DTOImport: {";
		s += "\n importID=" + importID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n stav=" + stav;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casKontrolaZac=" + casKontrolaZac;
		s += "\n casKontrolaKon=" + casKontrolaKon;
		s += "\n casImportZac=" + casImportZac;
		s += "\n casImportKon=" + casImportKon;
		s += "\n importPrilohaFileName=" + importPrilohaFileName;
		s += "\n errorPocet=" + errors;
		s += "\n zmenyPocet=" + columns;
		s += "\n zdroj=" + zdroj;
		return s;
	}

	public Integer getImportID() {
		return importID;
	}

	public void setImportID(Integer importID) {
		this.importID = importID;
	}

	public Integer getIDCiselnik() {
		return IDCiselnik;
	}

	public void setIDCiselnik(Integer iDCiselnik) {
		IDCiselnik = iDCiselnik;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public String getStav() {
		return stav;
	}

	public void setStav(String stav) {
		this.stav = stav;
	}

	public Date getCasVytvorenia() {
		return casVytvorenia;
	}

	public void setCasVytvorenia(Date casVytvorenia) {
		this.casVytvorenia = casVytvorenia;
	}

	public Date getCasKontrolaZac() {
		return casKontrolaZac;
	}

	public void setCasKontrolaZac(Date casKontrolaZac) {
		this.casKontrolaZac = casKontrolaZac;
	}

	public Date getCasKontrolaKon() {
		return casKontrolaKon;
	}

	public void setCasKontrolaKon(Date casKontrolaKon) {
		this.casKontrolaKon = casKontrolaKon;
	}

	public Date getCasImportZac() {
		return casImportZac;
	}

	public void setCasImportZac(Date casImportZac) {
		this.casImportZac = casImportZac;
	}

	public Date getCasImportKon() {
		return casImportKon;
	}

	public void setCasImportKon(Date casImportKon) {
		this.casImportKon = casImportKon;
	}

	public String getImportPrilohaFileName() {
		return importPrilohaFileName;
	}

	public void setImportPrilohaFileName(String importPrilohaFileName) {
		this.importPrilohaFileName = importPrilohaFileName;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public DTOImportZmena[] getImportZmenaList() {
		return importZmenaList;
	}

	public void setImportZmenaList(DTOImportZmena[] importZmenaList) {
		this.importZmenaList = importZmenaList;
	}

	public String getZdroj() {
		return zdroj;
	}

	public void setZdroj(String zdroj) {
		this.zdroj = zdroj;
	}

	public String getCiselnikNazov() {
		return ciselnikNazov;
	}

	public void setCiselnikNazov(String ciselnikNazov) {
		this.ciselnikNazov = ciselnikNazov;
	}
}
