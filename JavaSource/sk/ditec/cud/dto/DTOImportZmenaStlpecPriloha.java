package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudImportZmenaStlpecPrilohaWS")
public class DTOImportZmenaStlpecPriloha extends DTO {

	Integer importZmenaStlpecPrilohaID;
	Integer IDImport;
	Integer IDImportZmena;
	String oldValuePriloha;
	String newValuePriloha;

	// lookup field

	@Override
	public String toString() {
		String s = "DTOImportZmenaStlpecPriloha: {";
		s += "\n importZmenaStlpecPrilohaID=" + importZmenaStlpecPrilohaID;
		s += "\n IDImport=" + IDImport;
		s += "\n IDImportZmena=" + IDImportZmena;
		s += "\n oldValuePriloha=" + oldValuePriloha;
		s += "\n newValuePriloha=" + newValuePriloha;
		return s;
	}

	public Integer getImportZmenaStlpecPrilohaID() {
		return importZmenaStlpecPrilohaID;
	}

	public void setImportZmenaStlpecPrilohaID(Integer importZmenaStlpecPrilohaID) {
		this.importZmenaStlpecPrilohaID = importZmenaStlpecPrilohaID;
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

	public String getOldValuePriloha() {
		return oldValuePriloha;
	}

	public void setOldValuePriloha(String oldValuePriloha) {
		this.oldValuePriloha = oldValuePriloha;
	}

	public String getNewValuePriloha() {
		return newValuePriloha;
	}

	public void setNewValuePriloha(String newValuePriloha) {
		this.newValuePriloha = newValuePriloha;
	}

}
