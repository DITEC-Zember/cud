package sk.ditec.crd.dto;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;


@XmlType(name = "DTOZmenaStlpecCrd")
public class DTOZmenaStlpecCrd extends DTO {
	Integer idZmenaStlpca;
	String oldValue;
	String newValue;
	String nazov;

	public String toString() {
		String s = "DTO: {";
		s += "\n idZmenaStlpca=" + idZmenaStlpca;
		s += "\n oldValue=" + oldValue;
		s += "\n newValue=" + newValue;
		s += "\n nazov=" + nazov;
		return s;
	}

	public Integer getIdZmenaStlpca() {
		return idZmenaStlpca;
	}

	public void setIdZmenaStlpca(Integer idZmenaStlpca) {
		this.idZmenaStlpca = idZmenaStlpca;
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

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

}
