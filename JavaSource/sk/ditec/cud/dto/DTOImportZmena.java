package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudImportZmenaWS")
public class DTOImportZmena extends DTO {

	Integer importZmenaID;
	Integer IDImport;
	Integer rowID;
	Integer xlsRowID;
	String operacia;
	Date platnostOd;
	Date casSchvaleniaGr;
	String poznamka;
	String spracovany;

	// lookup field
	DTOImportZmenaStlpec[] importZmenaStlpecList;
	DTOImportMsg[] importMsgList;

	String errors;
	String warnings;
	String columns;

	String obnova;

	@Override
	public String toString() {
		String s = "DTOImportZmena: {";
		s += "\n importZmenaID=" + importZmenaID;
		s += "\n IDImport=" + IDImport;
		s += "\n rowID=" + rowID;
		s += "\n xlsRowID=" + xlsRowID;
		s += "\n operacia=" + operacia;
		s += "\n platnostOd=" + platnostOd;
		s += "\n casSchvaleniaGr=" + casSchvaleniaGr;
		s += "\n spracovany=" + spracovany;
		s += "\n poznamka=" + poznamka;
		s += "\n errors=" + errors;
		s += "\n warnings=" + warnings;
		s += "\n columns=" + columns;
		s += "\n obnova=" + obnova;
		return s;
	}

	public Integer getImportZmenaID() {
		return importZmenaID;
	}

	public void setImportZmenaID(Integer importZmenaID) {
		this.importZmenaID = importZmenaID;
	}

	public Integer getIDImport() {
		return IDImport;
	}

	public void setIDImport(Integer iDImport) {
		IDImport = iDImport;
	}

	public Integer getRowID() {
		return rowID;
	}

	public void setRowID(Integer rowID) {
		this.rowID = rowID;
	}

	public Integer getXlsRowID() {
		return xlsRowID;
	}

	public void setXlsRowID(Integer xlsRowID) {
		this.xlsRowID = xlsRowID;
	}

	public String getOperacia() {
		return operacia;
	}

	public void setOperacia(String operacia) {
		this.operacia = operacia;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public DTOImportZmenaStlpec[] getImportZmenaStlpecList() {
		return importZmenaStlpecList;
	}

	public void setImportZmenaStlpecList(DTOImportZmenaStlpec[] importZmenaStlpecList) {
		this.importZmenaStlpecList = importZmenaStlpecList;
	}

	public DTOImportMsg[] getImportMsgList() {
		return importMsgList;
	}

	public void setImportMsgList(DTOImportMsg[] importMsgList) {
		this.importMsgList = importMsgList;
	}

	public Date getCasSchvaleniaGr() {
		return casSchvaleniaGr;
	}

	public void setCasSchvaleniaGr(Date casSchvaleniaGr) {
		this.casSchvaleniaGr = casSchvaleniaGr;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public String getWarnings() {
		return warnings;
	}

	public void setWarnings(String warnings) {
		this.warnings = warnings;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public String getSpracovany() {
		return spracovany;
	}

	public void setSpracovany(String spracovany) {
		this.spracovany = spracovany;
	}

	public String getObnova() {
		return obnova;
	}

	public void setObnova(String obnova) {
		this.obnova = obnova;
	}

}
