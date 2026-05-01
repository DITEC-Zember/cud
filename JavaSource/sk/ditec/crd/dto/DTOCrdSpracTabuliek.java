package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;


@XmlType(name = "DTOCrdSpracTabuliek")
public class DTOCrdSpracTabuliek extends DTO {
	// Table name : CRD_SPRAC_TABULIEK

	Integer crdSpracTabuliekId; // CRD_SPRAC_TABULIEK_ID
	Integer IdCrdSpracovanie; // ID_CRD_SPRACOVANIE
	Integer IdCiselnik; // ID_CISELNIK
	Date dateFilterForDeleted; // DATE_FILTER_FOR_DELETED
	Date replicateFromDate; // REPLICATE_FROM_DATE
	String replicateAll; // REPLICATE_ALL
	Integer navratovyKod; // NAVRATOVY_KOD
	String popisSpracovania; // POPIS_SPRACOVANIA
	String vstupneXml; // VSTUPNE_XML
	String vystupneXml; // VYSTUPNE_XML
	String zmenoveXmlVstup; // ZMENOVE_XML_VSTUP
	String zmenoveXmlVystup; // ZMENOVE_XML_VYSTUP

	public String toString() {
		String s = "DTO: {";
		s += "\n crdSpracTabuliekID=" + crdSpracTabuliekId;
		s += "\n IDCrdSpracovanie=" + IdCrdSpracovanie;
		s += "\n IDCiselnik=" + IdCiselnik;
		s += "\n dateFilterForDeleted=" + dateFilterForDeleted;
		s += "\n replicateFromDate=" + replicateFromDate;
		s += "\n replicateAll=" + replicateAll;
		s += "\n navratovyKod=" + navratovyKod;
		s += "\n popisSpracovania=" + popisSpracovania;
		s += "\n vstupneXml=" + vstupneXml;
		s += "\n vystupneXml=" + vystupneXml;
		s += "\n zmenoveXmlVstup=" + zmenoveXmlVstup;
		s += "\n zmenoveXmlVystup=" + zmenoveXmlVystup;
		s += "}";
		return s;
	}

	public Integer getCrdSpracTabuliekId() {
		return crdSpracTabuliekId;
	}

	public void setCrdSpracTabuliekId(Integer crdSpracTabuliekId) {
		this.crdSpracTabuliekId = crdSpracTabuliekId;
	}

	public Integer getIdCrdSpracovanie() {
		return IdCrdSpracovanie;
	}

	public void setIdCrdSpracovanie(Integer idCrdSpracovanie) {
		IdCrdSpracovanie = idCrdSpracovanie;
	}

	public Integer getIdCiselnik() {
		return IdCiselnik;
	}

	public void setIdCiselnik(Integer idCiselnik) {
		IdCiselnik = idCiselnik;
	}

	public Date getDateFilterForDeleted() {
		return dateFilterForDeleted;
	}

	public void setDateFilterForDeleted(Date dateFilterForDeleted) {
		this.dateFilterForDeleted = dateFilterForDeleted;
	}

	public Date getReplicateFromDate() {
		return replicateFromDate;
	}

	public void setReplicateFromDate(Date replicateFromDate) {
		this.replicateFromDate = replicateFromDate;
	}

	public String getReplicateAll() {
		return replicateAll;
	}

	public void setReplicateAll(String replicateAll) {
		this.replicateAll = replicateAll;
	}

	public Integer getNavratovyKod() {
		return navratovyKod;
	}

	public void setNavratovyKod(Integer navratovyKod) {
		this.navratovyKod = navratovyKod;
	}

	public String getPopisSpracovania() {
		return popisSpracovania;
	}

	public void setPopisSpracovania(String popisSpracovania) {
		this.popisSpracovania = popisSpracovania;
	}

	public String getVstupneXml() {
		return vstupneXml;
	}

	public void setVstupneXml(String vstupneXml) {
		this.vstupneXml = vstupneXml;
	}

	public String getVystupneXml() {
		return vystupneXml;
	}

	public void setVystupneXml(String vystupneXml) {
		this.vystupneXml = vystupneXml;
	}

	public String getZmenoveXmlVstup() {
		return zmenoveXmlVstup;
	}

	public void setZmenoveXmlVstup(String zmenoveXmlVstup) {
		this.zmenoveXmlVstup = zmenoveXmlVstup;
	}

	public String getZmenoveXmlVystup() {
		return zmenoveXmlVystup;
	}

	public void setZmenoveXmlVystup(String zmenoveXmlVystup) {
		this.zmenoveXmlVystup = zmenoveXmlVystup;
	}


}
