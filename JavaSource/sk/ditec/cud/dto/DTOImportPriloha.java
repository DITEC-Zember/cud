package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudImportPrilohaWS")
public class DTOImportPriloha extends DTO {

	Integer importPrilohaID;
	Integer IDImport;
	String fileName;
	byte[] priloha;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOImportPriloha: {";
		s += "\n importPrilohaID=" + importPrilohaID;
		s += "\n IDImport=" + IDImport;
		s += "\n fileName=" + fileName;
		return s;
	}

	public Integer getImportPrilohaID() {
		return importPrilohaID;
	}

	public void setImportPrilohaID(Integer importPrilohaID) {
		this.importPrilohaID = importPrilohaID;
	}

	public Integer getIDImport() {
		return IDImport;
	}

	public void setIDImport(Integer iDImport) {
		IDImport = iDImport;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getPriloha() {
		return priloha;
	}

	public void setPriloha(byte[] priloha) {
		this.priloha = priloha;
	}

}
