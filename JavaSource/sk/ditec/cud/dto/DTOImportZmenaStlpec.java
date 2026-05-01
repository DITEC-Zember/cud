package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudImportZmenaStlpecWS")
public class DTOImportZmenaStlpec extends DTO {

	Integer importZmenaStlpecID;
	Integer IDImport;
	Integer IDImportZmena;
	Integer IDCiselnikStlpec;
	Integer IDImportZmenaStlpecPriloha;
	String ciselnikStlpecNazov;
	String oldValue;
	String newValue;

	// lookup field
	String subor;

	@Override
	public String toString() {
		String s = "DTOImportZmenaStlpec: {";
		s += "\n importZmenaStlpecID=" + importZmenaStlpecID;
		s += "\n IDImport=" + IDImport;
		s += "\n IDImportZmena=" + IDImportZmena;
		s += "\n IDCiselnikStlpec=" + IDCiselnikStlpec;
		s += "\n IDImportZmenaStlpecPriloha=" + IDImportZmenaStlpecPriloha;
		s += "\n ciselnikStlpecNazov=" + ciselnikStlpecNazov;
		s += "\n oldValue=" + oldValue;
		s += "\n newValue=" + newValue;
		s += "\n subor=" + subor;
		return s;
	}

	public Integer getImportZmenaStlpecID() {
		return importZmenaStlpecID;
	}

	public void setImportZmenaStlpecID(Integer importZmenaStlpecID) {
		this.importZmenaStlpecID = importZmenaStlpecID;
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

	public Integer getIDCiselnikStlpec() {
		return IDCiselnikStlpec;
	}

	public void setIDCiselnikStlpec(Integer iDCiselnikStlpec) {
		IDCiselnikStlpec = iDCiselnikStlpec;
	}

	public String getCiselnikStlpecNazov() {
		return ciselnikStlpecNazov;
	}

	public void setCiselnikStlpecNazov(String ciselnikStlpecNazov) {
		this.ciselnikStlpecNazov = ciselnikStlpecNazov;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public Integer getIDImportZmenaStlpecPriloha() {
		return IDImportZmenaStlpecPriloha;
	}

	public void setIDImportZmenaStlpecPriloha(Integer iDImportZmenaStlpecPriloha) {
		IDImportZmenaStlpecPriloha = iDImportZmenaStlpecPriloha;
	}

	public String getSubor() {
		return subor;
	}

	public void setSubor(String subor) {
		this.subor = subor;
	}

}
