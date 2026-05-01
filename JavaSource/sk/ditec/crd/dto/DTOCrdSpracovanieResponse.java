package sk.ditec.crd.dto;
import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;




@XmlType(name = "DTOCrdSpracovanieResponse")
public class DTOCrdSpracovanieResponse extends DTO {

	String spracovane;
	String bezZmien;
	String chyba;

	public String toString() {
		String s = "DTO: {";
		s += "\n spracovane=" + spracovane;
		s += "\n bezZmien=" + bezZmien;
		s += "\n chyba=" + chyba;
		s += "}";
		return s;
	}

	public String getSpracovane() {
		return spracovane;
	}


	public void setSpracovane(String spracovane) {
		this.spracovane = spracovane;
	}


	public String getBezZmien() {
		return bezZmien;
	}


	public void setBezZmien(String bezZmien) {
		this.bezZmien = bezZmien;
	}


	public String getChyba() {
		return chyba;
	}


	public void setChyba(String chyba) {
		this.chyba = chyba;
	}



}
