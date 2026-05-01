package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;


@XmlType(name = "DTOTStanicnaKolaj")
public class DTOTStanicnaKolaj extends DTO {
	// Table name : T_STANICNA_KOLAJ

	Integer histID; // HIST_ID
	Date platnostOd; // PLATNOST_OD
	Date platnostDo; // PLATNOST_DO
	Date casVytvorenia; // CAS_VYTVORENIA
	Date casZmeny; // CAS_ZMENY
	Integer IDZmena; // ID_ZMENA
	String zmaz; // ZMAZ
	Integer stanicnaKolajID; // STANICNA_KOLAJ_ID
	Integer IDDopravnyBod; // ID_DOPRAVNY_BOD
	String cislo; // CISLO
	Integer IDUrcenieKolaje; // ID_URCENIE_KOLAJE
	Integer IDDruhKolaje; // ID_DRUH_KOLAJE
	Integer uzitocnaDlzka; // UZITOCNA_DLZKA
	Integer stavebnaDlzka; // STAVEBNA_DLZKA
	Integer IDElektrickaTrakcia; // ID_ELEKTRICKA_TRAKCIA
	Integer dlzkaNastupista; // DLZKA_NASTUPISTA
	String poznamka; // POZNAMKA
	String neprevadzkovana; // NEPREVADZKOVANA
	String vlecka; // VLECKA
	String cisloNastupista; // CISLO_NASTUPISTA
	String poradieOdDk; // PORADIE_OD_DK
	String sok; // SOK
	Date crdZac; // CRD_ZAC
	Date crdKon; // CRD_KON
	String nazov; // NAZOV

	public String toString() {
		String s = "DTO: {";
		s += "\n histID=" + histID;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDZmena=" + IDZmena;
		s += "\n zmaz=" + zmaz;
		s += "\n stanicnaKolajID=" + stanicnaKolajID;
		s += "\n IDDopravnyBod=" + IDDopravnyBod;
		s += "\n cislo=" + cislo;
		s += "\n IDUrcenieKolaje=" + IDUrcenieKolaje;
		s += "\n IDDruhKolaje=" + IDDruhKolaje;
		s += "\n uzitocnaDlzka=" + uzitocnaDlzka;
		s += "\n stavebnaDlzka=" + stavebnaDlzka;
		s += "\n IDElektrickaTrakcia=" + IDElektrickaTrakcia;
		s += "\n dlzkaNastupista=" + dlzkaNastupista;
		s += "\n poznamka=" + poznamka;
		s += "\n neprevadzkovana=" + neprevadzkovana;
		s += "\n vlecka=" + vlecka;
		s += "\n cisloNastupista=" + cisloNastupista;
		s += "\n poradieOdDk=" + poradieOdDk;
		s += "\n sok=" + sok;
		s += "\n crdZac=" + crdZac;
		s += "\n crdKon=" + crdKon;
		s += "\n nazov=" + nazov;
		s += "}";
		return s;
	}

	public Integer getHistID() {
		return histID;
	}

	public void setHistID(Integer histID) {
		this.histID = histID;
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

	public Date getCasVytvorenia() {
		return casVytvorenia;
	}

	public void setCasVytvorenia(Date casVytvorenia) {
		this.casVytvorenia = casVytvorenia;
	}

	public Date getCasZmeny() {
		return casZmeny;
	}

	public void setCasZmeny(Date casZmeny) {
		this.casZmeny = casZmeny;
	}

	public Integer getIDZmena() {
		return IDZmena;
	}

	public void setIDZmena(Integer iDZmena) {
		IDZmena = iDZmena;
	}

	public String getZmaz() {
		return zmaz;
	}

	public void setZmaz(String zmaz) {
		this.zmaz = zmaz;
	}

	public Integer getStanicnaKolajID() {
		return stanicnaKolajID;
	}

	public void setStanicnaKolajID(Integer stanicnaKolajID) {
		this.stanicnaKolajID = stanicnaKolajID;
	}

	public Integer getIDDopravnyBod() {
		return IDDopravnyBod;
	}

	public void setIDDopravnyBod(Integer iDDopravnyBod) {
		IDDopravnyBod = iDDopravnyBod;
	}

	public String getCislo() {
		return cislo;
	}

	public void setCislo(String cislo) {
		this.cislo = cislo;
	}

	public Integer getIDUrcenieKolaje() {
		return IDUrcenieKolaje;
	}

	public void setIDUrcenieKolaje(Integer iDUrcenieKolaje) {
		IDUrcenieKolaje = iDUrcenieKolaje;
	}

	public Integer getIDDruhKolaje() {
		return IDDruhKolaje;
	}

	public void setIDDruhKolaje(Integer iDDruhKolaje) {
		IDDruhKolaje = iDDruhKolaje;
	}

	public Integer getUzitocnaDlzka() {
		return uzitocnaDlzka;
	}

	public void setUzitocnaDlzka(Integer uzitocnaDlzka) {
		this.uzitocnaDlzka = uzitocnaDlzka;
	}

	public Integer getStavebnaDlzka() {
		return stavebnaDlzka;
	}

	public void setStavebnaDlzka(Integer stavebnaDlzka) {
		this.stavebnaDlzka = stavebnaDlzka;
	}

	public Integer getIDElektrickaTrakcia() {
		return IDElektrickaTrakcia;
	}

	public void setIDElektrickaTrakcia(Integer iDElektrickaTrakcia) {
		IDElektrickaTrakcia = iDElektrickaTrakcia;
	}

	public Integer getDlzkaNastupista() {
		return dlzkaNastupista;
	}

	public void setDlzkaNastupista(Integer dlzkaNastupista) {
		this.dlzkaNastupista = dlzkaNastupista;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}

	public String getNeprevadzkovana() {
		return neprevadzkovana;
	}

	public void setNeprevadzkovana(String neprevadzkovana) {
		this.neprevadzkovana = neprevadzkovana;
	}

	public String getVlecka() {
		return vlecka;
	}

	public void setVlecka(String vlecka) {
		this.vlecka = vlecka;
	}

	public String getCisloNastupista() {
		return cisloNastupista;
	}

	public void setCisloNastupista(String cisloNastupista) {
		this.cisloNastupista = cisloNastupista;
	}

	public String getPoradieOdDk() {
		return poradieOdDk;
	}

	public void setPoradieOdDk(String poradieOdDk) {
		this.poradieOdDk = poradieOdDk;
	}

	public String getSok() {
		return sok;
	}

	public void setSok(String sok) {
		this.sok = sok;
	}

	public Date getCrdZac() {
		return crdZac;
	}

	public void setCrdZac(Date crdZac) {
		this.crdZac = crdZac;
	}

	public Date getCrdKon() {
		return crdKon;
	}

	public void setCrdKon(Date crdKon) {
		this.crdKon = crdKon;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

}
