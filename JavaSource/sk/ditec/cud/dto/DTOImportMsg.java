package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudImportMsgWS")
public class DTOImportMsg extends DTO {

	Integer importMsgID;
	Integer IDImport;
	Integer IDImportZmena;
	String typ;
	String msg;

	// lookup field
	Integer IDCiselnikStlpecGui;
	String ciselnikStlpecNazov;

	@Override
	public String toString() {
		String s = "DTOImportMsg: {";
		s += "\n importMsgID=" + importMsgID;
		s += "\n IDImport=" + IDImport;
		s += "\n IDImportZmena=" + IDImportZmena;
		s += "\n typ=" + typ;
		s += "\n msg=" + msg;
		s += "\n IDCiselnikStlpecGui=" + IDCiselnikStlpecGui;
		s += "\n ciselnikStlpecNazov=" + ciselnikStlpecNazov;
		return s;
	}

	public Integer getImportMsgID() {
		return importMsgID;
	}

	public void setImportMsgID(Integer importMsgID) {
		this.importMsgID = importMsgID;
	}

	public Integer getIDImport() {
		return IDImport;
	}

	public void setIDImport(Integer iDImport) {
		IDImport = iDImport;
	}

	public Integer getIDImportZmena() {
		return IDImportZmena;
	}

	public void setIDImportZmena(Integer iDImportZmena) {
		IDImportZmena = iDImportZmena;
	}

	public String getTyp() {
		return typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getIDCiselnikStlpecGui() {
		return IDCiselnikStlpecGui;
	}

	public void setIDCiselnikStlpecGui(Integer iDCiselnikStlpecGui) {
		IDCiselnikStlpecGui = iDCiselnikStlpecGui;
	}

	public String getCiselnikStlpecNazov() {
		return ciselnikStlpecNazov;
	}

	public void setCiselnikStlpecNazov(String ciselnikStlpecNazov) {
		this.ciselnikStlpecNazov = ciselnikStlpecNazov;
	}

}
