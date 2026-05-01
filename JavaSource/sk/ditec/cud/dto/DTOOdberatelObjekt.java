package sk.ditec.cud.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;

@XmlType(name = "DTOCudOdberatelObjektWS")
public class DTOOdberatelObjekt extends DTO {

	Integer odberatelObjektID;
	Integer IDOdberatel;
	Integer IDObjekt;
	Date platnostOd;
	Date platnostDo;
	String typPristupu;
	String opakovanie;
	String exportDovod;
	String exportRozsah;
	String exportFormat;
	String exportTypPodlaOdberatela;
	String exportTyp;
	String exportCesta;
	String vsetkyCiselniky;
	Date casPoslExportuZmena;
	Date casPoslExportu;
	Date casPoslExportuPlan;
	Date casZmeny;
	Integer IDUcet;
	String login;
	String heslo;
	String aktivny;

	// lookup field
	String odberatelNazov;
	String odberatelAktivny;

	String objektNazov;
	String objektPlatny;

	Date platnostOdOd;
	Date platnostOdDo;
	Date platnostDoOd;
	Date platnostDoDo;

	String typPristupuNazov;

	String priznakZmeny;

	String jeAdmin;

	String objektSystemovyKanal;

	@Override
	public String toString() {
		String s = "DTOOdberatelObjekt: {";
		s += "\n odberatelObjektID=" + odberatelObjektID;
		s += "\n IDOdberatel=" + IDOdberatel;
		s += "\n IDObjekt=" + IDObjekt;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n typPristupu=" + typPristupu;
		s += "\n opakovanie=" + opakovanie;
		s += "\n exportDovod=" + exportDovod;
		s += "\n exportRozsah=" + exportRozsah;
		s += "\n exportFormat=" + exportFormat;
		s += "\n exportTypPodlaOdberatela=" + exportTypPodlaOdberatela;
		s += "\n exportTyp=" + exportTyp;
		s += "\n exportCesta=" + exportCesta;
		s += "\n vsetkyCiselniky=" + vsetkyCiselniky;
		s += "\n casPoslExportuZmena=" + casPoslExportuZmena;
		s += "\n casPoslExportu=" + casPoslExportu;
		s += "\n casPoslExportuPlan=" + casPoslExportuPlan;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDUcet=" + IDUcet;
		s += "\n login=" + login;
		s += "\n heslo=" + heslo;
		s += "\n aktivny=" + aktivny;
		s += "\n odberatelNazov=" + odberatelNazov;
		s += "\n odberatelAktivny=" + odberatelAktivny;
		s += "\n objektNazov=" + objektNazov;
		s += "\n objektPlatny=" + objektPlatny;
		s += "\n platnostOdOd=" + platnostOdOd;
		s += "\n platnostOdDo=" + platnostOdDo;
		s += "\n platnostDoOd=" + platnostDoOd;
		s += "\n platnostDoDo=" + platnostDoDo;
		s += "\n typPristupuNazov=" + typPristupuNazov;
		s += "\n priznakZmeny=" + priznakZmeny;
		s += "\n jeAdmin=" + jeAdmin;
		s += "\n objektSystemovyKanal=" + objektSystemovyKanal;
		return s;
	}

	public String getAktivny() {
		return aktivny;
	}

	public void setAktivny(String aktivny) {
		this.aktivny = aktivny;
	}

	public Integer getOdberatelObjektID() {
		return odberatelObjektID;
	}

	public void setOdberatelObjektID(Integer odberatelObjektID) {
		this.odberatelObjektID = odberatelObjektID;
	}

	public Integer getIDOdberatel() {
		return IDOdberatel;
	}

	public void setIDOdberatel(Integer iDOdberatel) {
		IDOdberatel = iDOdberatel;
	}

	public Integer getIDObjekt() {
		return IDObjekt;
	}

	public void setIDObjekt(Integer iDObjekt) {
		IDObjekt = iDObjekt;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

	public Date getPlatnostDo() {
		return platnostDo;
	}

	public void setPlatnostDo(Date platnostDo) {
		this.platnostDo = platnostDo;
	}

	public String getTypPristupu() {
		return typPristupu;
	}

	public void setTypPristupu(String typPristupu) {
		this.typPristupu = typPristupu;
	}

	public String getOpakovanie() {
		return opakovanie;
	}

	public void setOpakovanie(String opakovanie) {
		this.opakovanie = opakovanie;
	}

	public String getExportDovod() {
		return exportDovod;
	}

	public void setExportDovod(String exportDovod) {
		this.exportDovod = exportDovod;
	}

	public String getExportRozsah() {
		return exportRozsah;
	}

	public void setExportRozsah(String exportRozsah) {
		this.exportRozsah = exportRozsah;
	}

	public String getExportFormat() {
		return exportFormat;
	}

	public void setExportFormat(String exportFormat) {
		this.exportFormat = exportFormat;
	}

	public String getExportTyp() {
		return exportTyp;
	}

	public void setExportTyp(String exportTyp) {
		this.exportTyp = exportTyp;
	}

	public String getExportCesta() {
		return exportCesta;
	}

	public void setExportCesta(String exportCesta) {
		this.exportCesta = exportCesta;
	}

	public String getVsetkyCiselniky() {
		return vsetkyCiselniky;
	}

	public void setVsetkyCiselniky(String vsetkyCiselniky) {
		this.vsetkyCiselniky = vsetkyCiselniky;
	}

	public Date getCasPoslExportuZmena() {
		return casPoslExportuZmena;
	}

	public void setCasPoslExportuZmena(Date casPoslExportuZmena) {
		this.casPoslExportuZmena = casPoslExportuZmena;
	}

	public Date getCasPoslExportu() {
		return casPoslExportu;
	}

	public void setCasPoslExportu(Date casPoslExportu) {
		this.casPoslExportu = casPoslExportu;
	}

	public Date getCasPoslExportuPlan() {
		return casPoslExportuPlan;
	}

	public void setCasPoslExportuPlan(Date casPoslExportuPlan) {
		this.casPoslExportuPlan = casPoslExportuPlan;
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

	public String getOdberatelNazov() {
		return odberatelNazov;
	}

	public void setOdberatelNazov(String odberatelNazov) {
		this.odberatelNazov = odberatelNazov;
	}

	public String getOdberatelAktivny() {
		return odberatelAktivny;
	}

	public void setOdberatelAktivny(String odberatelAktivny) {
		this.odberatelAktivny = odberatelAktivny;
	}

	public String getObjektNazov() {
		return objektNazov;
	}

	public void setObjektNazov(String objektNazov) {
		this.objektNazov = objektNazov;
	}

	public String getObjektPlatny() {
		return objektPlatny;
	}

	public void setObjektPlatny(String objektPlatny) {
		this.objektPlatny = objektPlatny;
	}

	public Date getPlatnostOdOd() {
		return platnostOdOd;
	}

	public void setPlatnostOdOd(Date platnostOdOd) {
		this.platnostOdOd = platnostOdOd;
	}

	public Date getPlatnostOdDo() {
		return platnostOdDo;
	}

	public void setPlatnostOdDo(Date platnostOdDo) {
		this.platnostOdDo = platnostOdDo;
	}

	public Date getPlatnostDoOd() {
		return platnostDoOd;
	}

	public void setPlatnostDoOd(Date platnostDoOd) {
		this.platnostDoOd = platnostDoOd;
	}

	public Date getPlatnostDoDo() {
		return platnostDoDo;
	}

	public void setPlatnostDoDo(Date platnostDoDo) {
		this.platnostDoDo = platnostDoDo;
	}

	public String getTypPristupuNazov() {
		return typPristupuNazov;
	}

	public void setTypPristupuNazov(String typPristupuNazov) {
		this.typPristupuNazov = typPristupuNazov;
	}

	public String getPriznakZmeny() {
		return priznakZmeny;
	}

	public void setPriznakZmeny(String priznakZmeny) {
		this.priznakZmeny = priznakZmeny;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getHeslo() {
		return heslo;
	}

	public void setHeslo(String heslo) {
		this.heslo = heslo;
	}

	public String getJeAdmin() {
		return jeAdmin;
	}

	public void setJeAdmin(String jeAdmin) {
		this.jeAdmin = jeAdmin;
	}

	public String getObjektSystemovyKanal() {
		return objektSystemovyKanal;
	}

	public void setObjektSystemovyKanal(String objektSystemovyKanal) {
		this.objektSystemovyKanal = objektSystemovyKanal;
	}

	public String getExportTypPodlaOdberatela() {
		return exportTypPodlaOdberatela;
	}

	public void setExportTypPodlaOdberatela(String exportTypPodlaOdberatela) {
		this.exportTypPodlaOdberatela = exportTypPodlaOdberatela;
	}

}
