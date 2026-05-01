package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudObjektWS")
public class DTOObjekt extends DTO {

	Integer objektID;
	String nazov;
	String platny;
	String systemovy;
	String systemovyKanal;
	String systemovyVsetkyCiselniky;
	String systemovyExportFormat;
	Date casZmeny;
	Integer IDUcet;

	// lookup field
	DTOObjektCiselnik[] objektCiselnikList;

	@Override
	public String toString() {
		String s = "DTOObjekt: {";
		s += "\n objektID=" + objektID;
		s += "\n nazov=" + nazov;
		s += "\n platny=" + platny;
		s += "\n systemovy=" + systemovy;
		s += "\n systemovyKanal=" + systemovyKanal;
		s += "\n systemovyVsetkyCiselniky=" + systemovyVsetkyCiselniky;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		return s;
	}

	public Integer getObjektID() {
		return objektID;
	}

	public void setObjektID(Integer objektID) {
		this.objektID = objektID;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getPlatny() {
		return platny;
	}

	public void setPlatny(String platny) {
		this.platny = platny;
	}

	public String getSystemovy() {
		return systemovy;
	}

	public void setSystemovy(String systemovy) {
		this.systemovy = systemovy;
	}

	public String getSystemovyKanal() {
		return systemovyKanal;
	}

	public void setSystemovyKanal(String systemovyKanal) {
		this.systemovyKanal = systemovyKanal;
	}

	public String getSystemovyVsetkyCiselniky() {
		return systemovyVsetkyCiselniky;
	}

	public void setSystemovyVsetkyCiselniky(String systemovyVsetkyCiselniky) {
		this.systemovyVsetkyCiselniky = systemovyVsetkyCiselniky;
	}

	public String getSystemovyExportFormat() {
		return systemovyExportFormat;
	}

	public void setSystemovyExportFormat(String systemovyExportFormat) {
		this.systemovyExportFormat = systemovyExportFormat;
	}

	public Date getCasZmeny() {
		return casZmeny;
	}

	public void setCasZmeny(Date casZmeny) {
		this.casZmeny = casZmeny;
	}

	public Integer getIDUcet() {
		return IDUcet;
	}

	public void setIDUcet(Integer iDUcet) {
		IDUcet = iDUcet;
	}

	public DTOObjektCiselnik[] getObjektCiselnikList() {
		return objektCiselnikList;
	}

	public void setObjektCiselnikList(DTOObjektCiselnik[] objektCiselnikList) {
		this.objektCiselnikList = objektCiselnikList;
	}

}
