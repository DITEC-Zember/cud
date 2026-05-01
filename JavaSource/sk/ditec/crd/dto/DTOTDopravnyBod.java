package sk.ditec.crd.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import sk.ditec.common.bi.DTO;





@XmlType(name = "DTOTCountry")
public class DTOTDopravnyBod extends DTO {

	// Table name : T_DOPRAVNY_BOD

	Integer histID; // HIST_ID
	Date platnostOd; // PLATNOST_OD
	Date platnostDo; // PLATNOST_DO
	Date casVytvorenia; // CAS_VYTVORENIA
	Date casZmeny; // CAS_ZMENY
	Integer IDZmena; // ID_ZMENA
	String zmaz; // ZMAZ
	Integer dopravnyBodID; // DOPRAVNY_BOD_ID
	Integer IDTypDopravne; // ID_TYP_DOPRAVNE
	Integer IDDopravnyNazov; // ID_DOPRAVNY_NAZOV
	Integer IDDopravnyBod; // ID_DOPRAVNY_BOD
	Integer IDDefinicnyUsek; // ID_DEFINICNY_USEK
	Integer IDOblastneRiaditelstvo; // ID_OBLASTNE_RIADITELSTVO
	Integer IDDopravnyNazovZpps; // ID_DOPRAVNY_NAZOV_ZPPS
	String cislo; // CISLO
	String nazov; // NAZOV
	String obsadenie; // OBSADENIE
	Double kmPoloha1; // KM_POLOHA_1
	String pps; // PPS
	String cestovnePoriadky; // CESTOVNE_PORIADKY
	String lenPreIs; // LEN_PRE_IS
	Double kmPoloha2; // KM_POLOHA_2
	String skratka2; // SKRATKA_2
	String skratka4; // SKRATKA_4
	Integer IDTrat1; // ID_TRAT_1
	Integer IDTrat2; // ID_TRAT_2
	Integer IDTrat3; // ID_TRAT_3
	Integer IDTrat4; // ID_TRAT_4
	Integer IDTrat5; // ID_TRAT_5
	Double kmPoloha3; // KM_POLOHA_3
	Double kmPoloha4; // KM_POLOHA_4
	Double kmPoloha5; // KM_POLOHA_5
	Integer IDVyssiUzemnyCelok; // ID_VYSSI_UZEMNY_CELOK
	Integer IDKategoriaDbOd; // ID_KATEGORIA_DB_OD
	Integer IDKategoriaDbNd; // ID_KATEGORIA_DB_ND
	Double gpsSirka; // GPS_SIRKA
	Double gpsDlzka; // GPS_DLZKA
	Integer mapaPolohaX; // MAPA_POLOHA_X
	Integer mapaPolohaY; // MAPA_POLOHA_Y
	String ttp; // TTP
	String email; // EMAIL
	Integer IDStavDopravy; // ID_STAV_DOPRAVY
	Integer IDObec; // ID_OBEC
	String mimoObec; // MIMO_OBEC
	Integer IDPristupKObjektu; // ID_PRISTUP_K_OBJEKTU
	Integer IDStavObjektuBudova; // ID_STAV_OBJEKTU_BUDOVA
	Integer IDStavObjektuCakaren; // ID_STAV_OBJEKTU_CAKAREN
	String pristresok; // PRISTRESOK
	String pristupPrm; // PRISTUP_PRM
	String obmedzeniePrm; // OBMEDZENIE_PRM
	String pomocPrm; // POMOC_PRM
	Integer pomocPrmMin; // POMOC_PRM_MIN
	Integer IDDopravnyBodPrm1; // ID_DOPRAVNY_BOD_PRM_1
	Integer IDDopravnyBodPrm2; // ID_DOPRAVNY_BOD_PRM_2
	String stanicaPrmPoznamka; // STANICA_PRM_POZNAMKA
	String kontaktPrm; // KONTAKT_PRM
	String inaDraha; // INA_DRAHA
	String telefonZts; // TELEFON_ZTS
	String telefonVts; // TELEFON_VTS
	Integer IDTratovyUsek1; // ID_TRATOVY_USEK_1
	Integer IDTratovyUsek2; // ID_TRATOVY_USEK_2
	Integer IDTratovyUsek3; // ID_TRATOVY_USEK_3
	Integer IDTratovyUsek4; // ID_TRATOVY_USEK_4
	Integer IDTratovyUsek5; // ID_TRATOVY_USEK_5
	String zeleznicaStanica; // ZELEZNICA_STANICA
	String nastupiste; // NASTUPISTE
	String crd; // CRD
	Integer IDSubsidiaryType; // ID_SUBSIDIARY_TYPE
	Integer IDCompany; // ID_COMPANY
	String manipulaciaSKontajnermi; // MANIPULACIA_S_KONTAJNERMI
	String otvorenyPreOd; // OTVORENY_PRE_OD
	String otvorenyPreNd; // OTVORENY_PRE_ND
	String ulica; // ULICA
	String orientacneCislo; // ORIENTACNE_CISLO
	String mestoPsc; // MESTO_PSC
	String ps; // PS
	String stykDrah; // STYK_DRAH
	Integer IDNadradenaPrimarna; // ID_NADRADENA_PRIMARNA
	Date crdZac; // CRD_ZAC
	Date crdKon; // CRD_KON
	Date otvorenyPreOdZac; // OTVORENY_PRE_OD_ZAC
	Date otvorenyPreOdKon; // OTVORENY_PRE_OD_KON
	Date otvorenyPreNdZac; // OTVORENY_PRE_ND_ZAC
	Date otvorenyPreNdKon; // OTVORENY_PRE_ND_KON
	String poznamka; // POZNAMKA

	public String toString() {
		String s = "DTO: {";
		s += "\n histID=" + histID;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casVytvorenia=" + casVytvorenia;
		s += "\n casZmeny=" + casZmeny;
		s += "\n IDZmena=" + IDZmena;
		s += "\n zmaz=" + zmaz;
		s += "\n dopravnyBodID=" + dopravnyBodID;
		s += "\n IDTypDopravne=" + IDTypDopravne;
		s += "\n IDDopravnyNazov=" + IDDopravnyNazov;
		s += "\n IDDopravnyBod=" + IDDopravnyBod;
		s += "\n IDDefinicnyUsek=" + IDDefinicnyUsek;
		s += "\n IDOblastneRiaditelstvo=" + IDOblastneRiaditelstvo;
		s += "\n IDDopravnyNazovZpps=" + IDDopravnyNazovZpps;
		s += "\n cislo=" + cislo;
		s += "\n nazov=" + nazov;
		s += "\n obsadenie=" + obsadenie;
		s += "\n kmPoloha1=" + kmPoloha1;
		s += "\n pps=" + pps;
		s += "\n cestovnePoriadky=" + cestovnePoriadky;
		s += "\n lenPreIs=" + lenPreIs;
		s += "\n kmPoloha2=" + kmPoloha2;
		s += "\n skratka2=" + skratka2;
		s += "\n skratka4=" + skratka4;
		s += "\n IDTrat1=" + IDTrat1;
		s += "\n IDTrat2=" + IDTrat2;
		s += "\n IDTrat3=" + IDTrat3;
		s += "\n IDTrat4=" + IDTrat4;
		s += "\n IDTrat5=" + IDTrat5;
		s += "\n kmPoloha3=" + kmPoloha3;
		s += "\n kmPoloha4=" + kmPoloha4;
		s += "\n kmPoloha5=" + kmPoloha5;
		s += "\n IDVyssiUzemnyCelok=" + IDVyssiUzemnyCelok;
		s += "\n IDKategoriaDbOd=" + IDKategoriaDbOd;
		s += "\n IDKategoriaDbNd=" + IDKategoriaDbNd;
		s += "\n gpsSirka=" + gpsSirka;
		s += "\n gpsDlzka=" + gpsDlzka;
		s += "\n mapaPolohaX=" + mapaPolohaX;
		s += "\n mapaPolohaY=" + mapaPolohaY;
		s += "\n ttp=" + ttp;
		s += "\n email=" + email;
		s += "\n IDStavDopravy=" + IDStavDopravy;
		s += "\n IDObec=" + IDObec;
		s += "\n mimoObec=" + mimoObec;
		s += "\n IDPristupKObjektu=" + IDPristupKObjektu;
		s += "\n IDStavObjektuBudova=" + IDStavObjektuBudova;
		s += "\n IDStavObjektuCakaren=" + IDStavObjektuCakaren;
		s += "\n pristresok=" + pristresok;
		s += "\n pristupPrm=" + pristupPrm;
		s += "\n obmedzeniePrm=" + obmedzeniePrm;
		s += "\n pomocPrm=" + pomocPrm;
		s += "\n pomocPrmMin=" + pomocPrmMin;
		s += "\n IDDopravnyBodPrm1=" + IDDopravnyBodPrm1;
		s += "\n IDDopravnyBodPrm2=" + IDDopravnyBodPrm2;
		s += "\n stanicaPrmPoznamka=" + stanicaPrmPoznamka;
		s += "\n kontaktPrm=" + kontaktPrm;
		s += "\n inaDraha=" + inaDraha;
		s += "\n telefonZts=" + telefonZts;
		s += "\n telefonVts=" + telefonVts;
		s += "\n IDTratovyUsek1=" + IDTratovyUsek1;
		s += "\n IDTratovyUsek2=" + IDTratovyUsek2;
		s += "\n IDTratovyUsek3=" + IDTratovyUsek3;
		s += "\n IDTratovyUsek4=" + IDTratovyUsek4;
		s += "\n IDTratovyUsek5=" + IDTratovyUsek5;
		s += "\n zeleznicaStanica=" + zeleznicaStanica;
		s += "\n nastupiste=" + nastupiste;
		s += "\n crd=" + crd;
		s += "\n IDSubsidiaryType=" + IDSubsidiaryType;
		s += "\n IDCompany=" + IDCompany;
		s += "\n manipulaciaSKontajnermi=" + manipulaciaSKontajnermi;
		s += "\n otvorenyPreOd=" + otvorenyPreOd;
		s += "\n otvorenyPreNd=" + otvorenyPreNd;
		s += "\n ulica=" + ulica;
		s += "\n orientacneCislo=" + orientacneCislo;
		s += "\n mestoPsc=" + mestoPsc;
		s += "\n ps=" + ps;
		s += "\n stykDrah=" + stykDrah;
		s += "\n IDNadradenaPrimarna=" + IDNadradenaPrimarna;
		s += "\n crdZac=" + crdZac;
		s += "\n crdKon=" + crdKon;
		s += "\n otvorenyPreOdZac=" + otvorenyPreOdZac;
		s += "\n otvorenyPreOdKon=" + otvorenyPreOdKon;
		s += "\n otvorenyPreNdZac=" + otvorenyPreNdZac;
		s += "\n otvorenyPreNdKon=" + otvorenyPreNdKon;
		s += "\n poznamka=" + poznamka;
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

	public Integer getDopravnyBodID() {
		return dopravnyBodID;
	}

	public void setDopravnyBodID(Integer dopravnyBodID) {
		this.dopravnyBodID = dopravnyBodID;
	}

	public Integer getIDTypDopravne() {
		return IDTypDopravne;
	}

	public void setIDTypDopravne(Integer iDTypDopravne) {
		IDTypDopravne = iDTypDopravne;
	}

	public Integer getIDDopravnyNazov() {
		return IDDopravnyNazov;
	}

	public void setIDDopravnyNazov(Integer iDDopravnyNazov) {
		IDDopravnyNazov = iDDopravnyNazov;
	}

	public Integer getIDDopravnyBod() {
		return IDDopravnyBod;
	}

	public void setIDDopravnyBod(Integer iDDopravnyBod) {
		IDDopravnyBod = iDDopravnyBod;
	}

	public Integer getIDDefinicnyUsek() {
		return IDDefinicnyUsek;
	}

	public void setIDDefinicnyUsek(Integer iDDefinicnyUsek) {
		IDDefinicnyUsek = iDDefinicnyUsek;
	}

	public Integer getIDOblastneRiaditelstvo() {
		return IDOblastneRiaditelstvo;
	}

	public void setIDOblastneRiaditelstvo(Integer iDOblastneRiaditelstvo) {
		IDOblastneRiaditelstvo = iDOblastneRiaditelstvo;
	}

	public Integer getIDDopravnyNazovZpps() {
		return IDDopravnyNazovZpps;
	}

	public void setIDDopravnyNazovZpps(Integer iDDopravnyNazovZpps) {
		IDDopravnyNazovZpps = iDDopravnyNazovZpps;
	}

	public String getCislo() {
		return cislo;
	}

	public void setCislo(String cislo) {
		this.cislo = cislo;
	}

	public String getNazov() {
		return nazov;
	}

	public void setNazov(String nazov) {
		this.nazov = nazov;
	}

	public String getObsadenie() {
		return obsadenie;
	}

	public void setObsadenie(String obsadenie) {
		this.obsadenie = obsadenie;
	}

	public Double getKmPoloha1() {
		return kmPoloha1;
	}

	public void setKmPoloha1(Double kmPoloha1) {
		this.kmPoloha1 = kmPoloha1;
	}

	public String getPps() {
		return pps;
	}

	public void setPps(String pps) {
		this.pps = pps;
	}

	public String getCestovnePoriadky() {
		return cestovnePoriadky;
	}

	public void setCestovnePoriadky(String cestovnePoriadky) {
		this.cestovnePoriadky = cestovnePoriadky;
	}

	public String getLenPreIs() {
		return lenPreIs;
	}

	public void setLenPreIs(String lenPreIs) {
		this.lenPreIs = lenPreIs;
	}

	public Double getKmPoloha2() {
		return kmPoloha2;
	}

	public void setKmPoloha2(Double kmPoloha2) {
		this.kmPoloha2 = kmPoloha2;
	}

	public String getSkratka2() {
		return skratka2;
	}

	public void setSkratka2(String skratka2) {
		this.skratka2 = skratka2;
	}

	public String getSkratka4() {
		return skratka4;
	}

	public void setSkratka4(String skratka4) {
		this.skratka4 = skratka4;
	}

	public Integer getIDTrat1() {
		return IDTrat1;
	}

	public void setIDTrat1(Integer iDTrat1) {
		IDTrat1 = iDTrat1;
	}

	public Integer getIDTrat2() {
		return IDTrat2;
	}

	public void setIDTrat2(Integer iDTrat2) {
		IDTrat2 = iDTrat2;
	}

	public Integer getIDTrat3() {
		return IDTrat3;
	}

	public void setIDTrat3(Integer iDTrat3) {
		IDTrat3 = iDTrat3;
	}

	public Integer getIDTrat4() {
		return IDTrat4;
	}

	public void setIDTrat4(Integer iDTrat4) {
		IDTrat4 = iDTrat4;
	}

	public Integer getIDTrat5() {
		return IDTrat5;
	}

	public void setIDTrat5(Integer iDTrat5) {
		IDTrat5 = iDTrat5;
	}

	public Double getKmPoloha3() {
		return kmPoloha3;
	}

	public void setKmPoloha3(Double kmPoloha3) {
		this.kmPoloha3 = kmPoloha3;
	}

	public Double getKmPoloha4() {
		return kmPoloha4;
	}

	public void setKmPoloha4(Double kmPoloha4) {
		this.kmPoloha4 = kmPoloha4;
	}

	public Double getKmPoloha5() {
		return kmPoloha5;
	}

	public void setKmPoloha5(Double kmPoloha5) {
		this.kmPoloha5 = kmPoloha5;
	}

	public Integer getIDVyssiUzemnyCelok() {
		return IDVyssiUzemnyCelok;
	}

	public void setIDVyssiUzemnyCelok(Integer iDVyssiUzemnyCelok) {
		IDVyssiUzemnyCelok = iDVyssiUzemnyCelok;
	}

	public Integer getIDKategoriaDbOd() {
		return IDKategoriaDbOd;
	}

	public void setIDKategoriaDbOd(Integer iDKategoriaDbOd) {
		IDKategoriaDbOd = iDKategoriaDbOd;
	}

	public Integer getIDKategoriaDbNd() {
		return IDKategoriaDbNd;
	}

	public void setIDKategoriaDbNd(Integer iDKategoriaDbNd) {
		IDKategoriaDbNd = iDKategoriaDbNd;
	}

	public Double getGpsSirka() {
		return gpsSirka;
	}

	public void setGpsSirka(Double gpsSirka) {
		this.gpsSirka = gpsSirka;
	}

	public Double getGpsDlzka() {
		return gpsDlzka;
	}

	public void setGpsDlzka(Double gpsDlzka) {
		this.gpsDlzka = gpsDlzka;
	}

	public Integer getMapaPolohaX() {
		return mapaPolohaX;
	}

	public void setMapaPolohaX(Integer mapaPolohaX) {
		this.mapaPolohaX = mapaPolohaX;
	}

	public Integer getMapaPolohaY() {
		return mapaPolohaY;
	}

	public void setMapaPolohaY(Integer mapaPolohaY) {
		this.mapaPolohaY = mapaPolohaY;
	}

	public String getTtp() {
		return ttp;
	}

	public void setTtp(String ttp) {
		this.ttp = ttp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getIDStavDopravy() {
		return IDStavDopravy;
	}

	public void setIDStavDopravy(Integer iDStavDopravy) {
		IDStavDopravy = iDStavDopravy;
	}

	public Integer getIDObec() {
		return IDObec;
	}

	public void setIDObec(Integer iDObec) {
		IDObec = iDObec;
	}

	public String getMimoObec() {
		return mimoObec;
	}

	public void setMimoObec(String mimoObec) {
		this.mimoObec = mimoObec;
	}

	public Integer getIDPristupKObjektu() {
		return IDPristupKObjektu;
	}

	public void setIDPristupKObjektu(Integer iDPristupKObjektu) {
		IDPristupKObjektu = iDPristupKObjektu;
	}

	public Integer getIDStavObjektuBudova() {
		return IDStavObjektuBudova;
	}

	public void setIDStavObjektuBudova(Integer iDStavObjektuBudova) {
		IDStavObjektuBudova = iDStavObjektuBudova;
	}

	public Integer getIDStavObjektuCakaren() {
		return IDStavObjektuCakaren;
	}

	public void setIDStavObjektuCakaren(Integer iDStavObjektuCakaren) {
		IDStavObjektuCakaren = iDStavObjektuCakaren;
	}

	public String getPristresok() {
		return pristresok;
	}

	public void setPristresok(String pristresok) {
		this.pristresok = pristresok;
	}

	public String getPristupPrm() {
		return pristupPrm;
	}

	public void setPristupPrm(String pristupPrm) {
		this.pristupPrm = pristupPrm;
	}

	public String getObmedzeniePrm() {
		return obmedzeniePrm;
	}

	public void setObmedzeniePrm(String obmedzeniePrm) {
		this.obmedzeniePrm = obmedzeniePrm;
	}

	public String getPomocPrm() {
		return pomocPrm;
	}

	public void setPomocPrm(String pomocPrm) {
		this.pomocPrm = pomocPrm;
	}

	public Integer getPomocPrmMin() {
		return pomocPrmMin;
	}

	public void setPomocPrmMin(Integer pomocPrmMin) {
		this.pomocPrmMin = pomocPrmMin;
	}

	public Integer getIDDopravnyBodPrm1() {
		return IDDopravnyBodPrm1;
	}

	public void setIDDopravnyBodPrm1(Integer iDDopravnyBodPrm1) {
		IDDopravnyBodPrm1 = iDDopravnyBodPrm1;
	}

	public Integer getIDDopravnyBodPrm2() {
		return IDDopravnyBodPrm2;
	}

	public void setIDDopravnyBodPrm2(Integer iDDopravnyBodPrm2) {
		IDDopravnyBodPrm2 = iDDopravnyBodPrm2;
	}

	public String getStanicaPrmPoznamka() {
		return stanicaPrmPoznamka;
	}

	public void setStanicaPrmPoznamka(String stanicaPrmPoznamka) {
		this.stanicaPrmPoznamka = stanicaPrmPoznamka;
	}

	public String getKontaktPrm() {
		return kontaktPrm;
	}

	public void setKontaktPrm(String kontaktPrm) {
		this.kontaktPrm = kontaktPrm;
	}

	public String getInaDraha() {
		return inaDraha;
	}

	public void setInaDraha(String inaDraha) {
		this.inaDraha = inaDraha;
	}

	public String getTelefonZts() {
		return telefonZts;
	}

	public void setTelefonZts(String telefonZts) {
		this.telefonZts = telefonZts;
	}

	public String getTelefonVts() {
		return telefonVts;
	}

	public void setTelefonVts(String telefonVts) {
		this.telefonVts = telefonVts;
	}

	public Integer getIDTratovyUsek1() {
		return IDTratovyUsek1;
	}

	public void setIDTratovyUsek1(Integer iDTratovyUsek1) {
		IDTratovyUsek1 = iDTratovyUsek1;
	}

	public Integer getIDTratovyUsek2() {
		return IDTratovyUsek2;
	}

	public void setIDTratovyUsek2(Integer iDTratovyUsek2) {
		IDTratovyUsek2 = iDTratovyUsek2;
	}

	public Integer getIDTratovyUsek3() {
		return IDTratovyUsek3;
	}

	public void setIDTratovyUsek3(Integer iDTratovyUsek3) {
		IDTratovyUsek3 = iDTratovyUsek3;
	}

	public Integer getIDTratovyUsek4() {
		return IDTratovyUsek4;
	}

	public void setIDTratovyUsek4(Integer iDTratovyUsek4) {
		IDTratovyUsek4 = iDTratovyUsek4;
	}

	public Integer getIDTratovyUsek5() {
		return IDTratovyUsek5;
	}

	public void setIDTratovyUsek5(Integer iDTratovyUsek5) {
		IDTratovyUsek5 = iDTratovyUsek5;
	}

	public String getZeleznicaStanica() {
		return zeleznicaStanica;
	}

	public void setZeleznicaStanica(String zeleznicaStanica) {
		this.zeleznicaStanica = zeleznicaStanica;
	}

	public String getNastupiste() {
		return nastupiste;
	}

	public void setNastupiste(String nastupiste) {
		this.nastupiste = nastupiste;
	}

	public String getCrd() {
		return crd;
	}

	public void setCrd(String crd) {
		this.crd = crd;
	}

	public Integer getIDSubsidiaryType() {
		return IDSubsidiaryType;
	}

	public void setIDSubsidiaryType(Integer iDSubsidiaryType) {
		IDSubsidiaryType = iDSubsidiaryType;
	}

	public Integer getIDCompany() {
		return IDCompany;
	}

	public void setIDCompany(Integer iDCompany) {
		IDCompany = iDCompany;
	}

	public String getManipulaciaSKontajnermi() {
		return manipulaciaSKontajnermi;
	}

	public void setManipulaciaSKontajnermi(String manipulaciaSKontajnermi) {
		this.manipulaciaSKontajnermi = manipulaciaSKontajnermi;
	}

	public String getOtvorenyPreOd() {
		return otvorenyPreOd;
	}

	public void setOtvorenyPreOd(String otvorenyPreOd) {
		this.otvorenyPreOd = otvorenyPreOd;
	}

	public String getOtvorenyPreNd() {
		return otvorenyPreNd;
	}

	public void setOtvorenyPreNd(String otvorenyPreNd) {
		this.otvorenyPreNd = otvorenyPreNd;
	}

	public String getUlica() {
		return ulica;
	}

	public void setUlica(String ulica) {
		this.ulica = ulica;
	}

	public String getOrientacneCislo() {
		return orientacneCislo;
	}

	public void setOrientacneCislo(String orientacneCislo) {
		this.orientacneCislo = orientacneCislo;
	}

	public String getMestoPsc() {
		return mestoPsc;
	}

	public void setMestoPsc(String mestoPsc) {
		this.mestoPsc = mestoPsc;
	}

	public String getPs() {
		return ps;
	}

	public void setPs(String ps) {
		this.ps = ps;
	}

	public String getStykDrah() {
		return stykDrah;
	}

	public void setStykDrah(String stykDrah) {
		this.stykDrah = stykDrah;
	}

	public Integer getIDNadradenaPrimarna() {
		return IDNadradenaPrimarna;
	}

	public void setIDNadradenaPrimarna(Integer iDNadradenaPrimarna) {
		IDNadradenaPrimarna = iDNadradenaPrimarna;
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

	public Date getOtvorenyPreOdZac() {
		return otvorenyPreOdZac;
	}

	public void setOtvorenyPreOdZac(Date otvorenyPreOdZac) {
		this.otvorenyPreOdZac = otvorenyPreOdZac;
	}

	public Date getOtvorenyPreOdKon() {
		return otvorenyPreOdKon;
	}

	public void setOtvorenyPreOdKon(Date otvorenyPreOdKon) {
		this.otvorenyPreOdKon = otvorenyPreOdKon;
	}

	public Date getOtvorenyPreNdZac() {
		return otvorenyPreNdZac;
	}

	public void setOtvorenyPreNdZac(Date otvorenyPreNdZac) {
		this.otvorenyPreNdZac = otvorenyPreNdZac;
	}

	public Date getOtvorenyPreNdKon() {
		return otvorenyPreNdKon;
	}

	public void setOtvorenyPreNdKon(Date otvorenyPreNdKon) {
		this.otvorenyPreNdKon = otvorenyPreNdKon;
	}

	public String getPoznamka() {
		return poznamka;
	}

	public void setPoznamka(String poznamka) {
		this.poznamka = poznamka;
	}
	
}
