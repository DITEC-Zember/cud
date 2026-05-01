package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "OpravnenieAtribut", propOrder = {
		"stlpecID",
		"stlpec",
		"hodnota",
		"editovatelny"
})
public class DTOOpravnenieAtributWS {

	private Integer stlpecID;
	private String stlpec;
	private String hodnota;
	private Boolean editovatelny;

	@XmlElement(required = true)
	public Integer getStlpecID() {
		return stlpecID;
	}

	public void setStlpecID(Integer stlpecID) {
		this.stlpecID = stlpecID;
	}

	@XmlElement(required = true)
	public String getStlpec() {
		return stlpec;
	}

	public void setStlpec(String stlpec) {
		this.stlpec = stlpec;
	}

	public String getHodnota() {
		return hodnota;
	}

	public void setHodnota(String hodnota) {
		this.hodnota = hodnota;
	}

	@XmlElement(required = true)
	public Boolean isEditovatelny() {
		return editovatelny;
	}

	public void setEditovatelny(Boolean editovatelny) {
		this.editovatelny = editovatelny;
	}
}
