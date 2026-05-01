package sk.ditec.cud.procvys;

import java.io.StringWriter;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.torque.util.MyCriteria2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.crd.generated.tsi.LocationFileDatasetMessage;
import sk.ditec.crdexp.ws.OutboundConnectorService_Service;
import sk.ditec.crdexp.ws.SendOutboundMessage;
import sk.ditec.crdexp.ws.SendOutboundMessageResponse;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOObjekt;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.notif.NotifUtils;
import sk.ditec.process.BaseProcess;

public class CudExportCrdProcess3_5 extends BaseProcess {

	private Logger log = LoggerFactory.getLogger(CudExportCrdProcess3_5.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private _CudCrdDelegate dlgCrd = new _CudCrdDelegate();
	private final String companyCode = _CudConsts.COMPANY_UIC_CODE_ZSR;
	private final String messageType = "6002";
	private final String version = "3.5.1.0";
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	@Override
	protected String getLogName() {
		return "export.crd";
	}

	@Override
	@Deprecated
	// ma sa pouzivat ExportPripravaSuborovPreExport
	protected void process() throws Throwable {

		// process process 603 ma sa volat iny export
		// CUD DZ Príprava súborov pre export
		// CUD CAS DZ Odoslanie exportu
		log.info("Start - Som proces CudExportProcess a bezim");
		// String mailingList = FrameworkUtils.getConfigProperty("cud", "export.procesy.info.mail");
		// String email_text = "";

		try {
			AuthInfo auth = AuthInfo.system();
			// 1. vstup
			// CudOdberatelObjekt:CUD_ODBERATEL_OBJEKT nazov = ExportLokaciiCRD

			DTOObjekt dtoF = new DTOObjekt();
			// zatial riesim len Export CRD, preto naplnim nazov
			dtoF.setNazov("ExportLokaciiCRD");
			dtoF.setPlatny("T");
			DTOObjekt[] listObj = dlg.getObjektRead().listForList(auth, new Page(), dtoF);

			if (listObj == null || listObj.length == 0) {
				// chyba
				log.info("Nie je definovany objekt pre Export CRD data, vykonavanie CudExportProcess konci.");
				return;

			}
			Date datumACasNacitaniaDat = new Date();
			for (DTOObjekt dtoobj : listObj) {
				Integer idObjekt = dtoobj.getObjektID();
				List<DTOOdberatelObjekt> listoo = dlgCrd.getTCudCiselnikyClass().getOOlist(auth, datumACasNacitaniaDat,
						idObjekt);
				// listByObjekt(auth, idObjekt);
				// anika kontroly na cas musi byt vo vnutri, lebo len tam su k tomu potrebne casy exportu
				for (DTOOdberatelObjekt dtooo : listoo) {
					// 2. KONTROLNY_INTERVAL=5min
					// AK CudOdberatelObjekt.POSLEDNY_PLANOVANY_EXPORT je null
					// Systém stupustí inicializáciu POSLEDNEHO EXPORTU
					// Systém nastaví v CUD_ODBERATEL_OBJEKT.POSLEDNY_PLANOVANY_EXPORT=.PLATNOST_OD
					if (dtooo.getCasPoslExportu() == null) {
						// anika ma byt platnost_od ale boli by prilis velke objekty, tak nastavim na 1.3.2023
						dtooo.setCasPoslExportu(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2024"));
					}
					//dtooo.setCasPoslExportu(new SimpleDateFormat("dd/MM/yyyy").parse("15/02/2024"));
					// pre ucely ladenia
					// dtooo.setCasPoslExportu(new SimpleDateFormat("dd/MM/yyyy").parse("01/03/2023"));
					// Systém zráta nasledujúci čas exportu
					// AK cudOdberatelObjekt.POSLEDNY_EXPORT nie je null
					// AK cudOdberatelObjekt.POSLEDNY_EXPORT<cudOdberatelObjekt.POSLEDNY_PLANOVANY_EXPORT
					// Systém vrátu cudOdberatelObjekt.POSLEDNY_EXPORT
					// END AK

					Date datumACasPoslPlanImport = (new Date(datumACasNacitaniaDat.getTime() + (10 * 1000 * 60)));
					dtooo.setCasPoslExportu(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("05.09.2025 12:01"));
					if (dtooo.getCasPoslExportu().getTime() > datumACasPoslPlanImport.getTime()) {
						// nevykonam zmenu
						continue;
					}

					// CUD DZ Príprava súborov pre export
					// 2.1.1.3.2 Inak Ak cudObjekt.NAZOV = "ExportlokaciiCRD"
					// /*ObjektCiselnik= CUD_OBJEKT_CISELNIK*/
					// Systém naèita èiselníky naviazané na exportný objekt

					//

					if (!statusOKnotify()) {
						log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudExportCrdProcess konci.");
						return;
					}

					ActionResult actionres = exportCrdDat(auth, dtooo, datumACasPoslPlanImport);
					if (actionres != null && actionres.getErrorMsg() != null) {
						log.error("CudExportCrdProcess.error: " + actionres.getErrorMsg());
						return;
					}

					// Systém nastaví
					// POSLEDNY_EXPORT = vst.DatumACasExportu
					// kde ODBERATEL_OBJEKT_ID = vst. OdberatelObjektID
					dtooo.setCasPoslExportu(datumACasNacitaniaDat);

					dtooo.setCasPoslExportuPlan(CudVysielanieUtils.getCasNaslImp(dtooo, datumACasNacitaniaDat));
					dlg.getOdberatelObjektModify().update(auth, dtooo);

					if (!statusOKnotify()) {
						log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudCrdProcess konci.");
						return;
					}

				} // end for ( DTOOdberatelObjekt dtooo : listoo )
			} // end for ( DTOObjekt dtoobj: listObj)

		} catch (Exception e) {
			// Ak poèas vytvárania súboru nastane chyba
			// ErrorMsg = "Pri príprave súborov pre export došlo k chybe: Odberate_objekt_ID = " +
			// cudOdberatelObjekt.ODEBRATEL_OBJEKT_ID + ", ¡¡Císelník_ID = cudCiselnik.CISELNIK_ID
			// Systém odošle chybovú správu a vráti sa do volajúceho procesu

			DBUtils.handleException(e, "CudExportProcess.error");
		}

		log.info("End - Som proces CudExportProcess a koncim");
	}

	public ActionResult exportCrdDat(AuthInfo auth, DTOOdberatelObjekt dtooo, Date datumAcasNacitania)
			throws AppException {

		ActionResult actRes = new ActionResult();
		String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.crd.mail");
		String email_text = " CHYBA pri EXPORT CudCrdProcess ";
		String vysielaneData = "";

		// test email textu
		// NotifUtils.sendNotif("", mailingList, "Info CUD CRD EXPORT",
		// CudVysielanieUtils.getEmailText("text chyby: aaaa ..", "exportCrdDat"));

		// try {
		//
		// // if (!statusOKnotify()) {
		// // break;
		// // }
		// // 2.1.1.3.2 /*Èíselník = CUD_CISELNIK/*
		// // Systém vyh¾adá Èíselník
		// // Popis: Systém vráti zoznam CUD_OBJEKT_CISELNIK naviazané na CUD_OBJEKT pre ktorý sa majú byť pripraviť
		// // súbory pre export
		// // Algoritmus : Systém vyhľadá CUD_OBJEKT_CISELNIK kde
		// // CUD_OBJEKT_CISELNIK.ID_OBJECT = vst.ObjectId a zároveň
		// // CUD_OBJEKT_CISELNIK.PLATNY = True a zároveň
		// // CUD_OBJEKT_CISELNIK.(ID_OBJEKT).PLATNY = True
		// // ak na vstupe TypČíselníkaList musí platiť CUD_OBJEKT_CISELNIK.CUD_CISELNIK.TYP_CISELNIKA in
		// // CUD_OBJEKT_CISELNIK
		//
		// // ov pre export_Vytvorenie súboru CRD LocationPrimary
		// // 2. datumACasVytvorenia = aktuálny dátum a èas
		// // Date datumACasVytvorenia = new Date();
		// // 3. /*$IDCountrySK*/
		// // Systém vyh¾adá ID country z èíselníka T_COUNTRY platný k aktuálnemu dátumu
		// // ***Country - T_COUNTRY***
		// // String tabulka = "T_COUNTRY";
		// // DTOCiselnik[] ciselnikList = null;
		// // nacitam ciselnik
		// // DTOCiselnik dtoCis = dlg.getCiselnikRead().readLight(auth, tabulka);
		// // if (!StringUtils.isValid(dtoCis)) {
		// // actRes.setErrorMsg(tabulka + " sa nenachaza v zozname ciselnikov!");
		// // actRes.setError(true);
		// // // rollbackConnection(auth);
		// // return actRes;
		// // }
		// DTOCiselnik dtoCisPrimLoc = dlg.getCiselnikRead().readLight(auth, "T_PRIMARY_LOCATION");
		// DTOCiselnik dtoCisSubLoc = dlg.getCiselnikRead().readLight(auth, "T_SUBSIDIARY_LOCATION");
		//
		// // 4 / idCountrySk countryKodIso = "SVK"
		// DTOTCountry tCountry = dlgCrd.getTCudCiselnikyClass().getCountry(auth, "SK");
		//
		// // 5. /*$PrimaryList*/
		// // vyh¾adám všeky záznamy, ktoré vznikli medzi posledným exportom a aktuálnym dátumom
		// // Systém vyh¾adá aktuálne platné T_PRIMARY_LOCATION
		// Date datumAcasPoslSprac = dtooo.getCasPoslExportu();
		// // pre ucely ladenia
		// // datumAcasNacitania = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("20.04.2023 14:00");
		//
		// if (datumAcasPoslSprac == null) { // este nebol spusteny export, uvazujem s datumom 1.3.2023
		// datumAcasPoslSprac = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("01.07.2024 00:01");
		// }
		// // datumAcasPoslSprac = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("10.07.2025 00:01");
		// // 6. /*$PrimaryList
		// ArrayList<DTOTPrimaryLocation> listPrimaryLocation = dlgCrd.getTCudCiselnikyClass()
		// .getPrimaryLocationListForExport(auth, datumAcasNacitania, datumAcasPoslSprac,
		// tCountry.getCountryID());
		// //
		// // 6. /*$PrimaryListBuduce*/
		// // vyh¾adám všeky záznamy, ktoré vznikli medzi posledným exportom a aktuálnym dátumom
		// // Systém vyh¾adá T_PRIMARY_LOCATION platné v budúcnosti
		// // ArrayList<DTOTPrimaryLocation> listPrimaryLocationBuduce = dlgCrd.getTCudCiselnikyClass()
		// // .getPrimaryLocationListBuduceForExport(auth, datumAcasNacitania, datumAcasPoslSprac,
		// // tCountry.getCountryID());
		//
		// // 8. /*$PrimaryList - spojenie zoznamov*/
		// // Systém spojí $PrimaryList A $PrimaryListBuduce do jedného spoloèného zoznamu $PrimaryList
		// // listPrimaryLocation.addAll(listPrimaryLocationBuduce);
		//
		// vysielaneData += " pocet vybranych PrimaryLocation = ";
		//
		// // 9. PRE každý $PrimLok z $PrimaryList
		// Integer oldPrimLocId = null;
		// Integer pocetZaznamovNaSpracovanie = 0;
		// for (DTOTPrimaryLocation dtopl : listPrimaryLocation) {
		// if (!statusOKnotify()) {
		// log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudExportCrdProcess konci.");
		// return actRes;
		// }
		// // 9.1/*$PredchadzajucaPlatnaLokalita*/
		// // Systém vyh¾adá,èi pre $PrimLok existujú starašie záznamy ako dátum a èas posledného exportu
		// // zapis sa robi u nas na 2x. Zneplatni sa, potom sa zapise novy zaznam p.primary_location_id
		// if (dtopl.getPrimaryLocationID().equals(oldPrimLocId)) {
		// // vyskytlo sa viacero zmien, predchadzajuce preskocim
		// continue;
		// }
		// oldPrimLocId = dtopl.getPrimaryLocationID();
		// pocetZaznamovNaSpracovanie += 1;
		// DTOTPrimaryLocation dtoplPredch = dlgCrd.getTCudCiselnikyClass().getPrimaryLocationListPredch(auth,
		// dtopl.getLocationCode(), dtopl.getIDCountry(), datumAcasPoslSprac);
		//
		// LocationFileDatasetMessage mess = new LocationFileDatasetMessage();
		// // 8.1.1.1 AK NEnašlo záznam Alebo
		// // našlo a PredchadzajucaPlatnaLokalita.ZMAZ=true
		// int messageStatus;
		// if (dtoplPredch == null || "T".equals(dtoplPredch.getZmaz())) {
		// // $MessageStatus = 1 (NEW)
		// messageStatus = 1;
		// } else if ("F".equals(dtopl.getZmaz())) {
		// // AK $PrimLok.ZMAZ = FALSE
		// // $MessageStatus = 2 (UPDATE)
		// messageStatus = 2;
		//
		// } else {
		// // $MessageStatus = 3 (DELETE)
		// messageStatus = 3;
		//
		// }
		// // 8.2 /* identifikatorSpravy = jendoznaèný identifikátor (Guid))
		// MessageHeader mh = new MessageHeader(); // rsdqm.getMessageHeader();
		// // Systém vygeneruje jednoznaèný identifikátor do identifikatorSpravy
		//
		// // mheader.setMessageRoutingID((new Date ()).getTime());
		//
		// // 8.3. Systém vytvorí správu (súbor) LocationFileDatasetMessage a naplní ju nasledovne:
		// // LocationFileDatasetMessage.LocationPrimaryCode = $PrimaryList.LOCATION_CODE
		// // Sekcia LocationFileDatasetMessage:
		// // MessageStatus = $MessageStatus
		// // LocationPrimaryCode = prvých 5 èíslic z $PrimLok.LOCATION_CODE
		//
		// mess.setLocationPrimaryCode(Integer.parseInt(dtopl.getLocationCode()));
		// // 8.3.3. Sekcia MessageHeader
		//
		// // MessageRoutingID - nepoužíva sa
		// // SenderReference - nepouživa sa
		// // MessageDateTimeCreated = aktuálny dátum a èas
		//
		// // 8.3.3.2 Sekcia Recipient
		// // CI_InstanceNumber = "01"
		// // ELEMENT_VALUE = "0056"
		// // (<Sender n1:CI_InstanceNumber="01">0056</Sender> )
		//
		// Sender ms = new Sender();
		// ms.setCIInstanceNumber(1);
		// ms.setValue(companyCode);
		// mh.setSender(ms);
		//
		// // 8.3.3.3. - nemam kde zapisat
		// // Sekcia Recipient
		// // CI_InstanceNumber = "01"
		// // ELEMENT_VALUE = "3178"
		// // (<Recipient n1:CI_InstanceNumber="01">3178</Recipient>)
		// Recipient rec = new Recipient();
		// rec.setCIInstanceNumber(1);
		// rec.setValue("3178");
		//
		// mh.setRecipient(rec);
		// // 8.3.3.4 Sekcia MessageReference
		// // MessageDateTime = aktuálny dátum a èas
		// // MessageType = 6002
		// // MessageTypeVersion = "3.1.0.1"
		// // MessageIdentifier = vygenerovany identifikatorSpravy
		// MessageReference mr = new MessageReference();
		// mr.setMessageDateTime(CudVysielanieUtils.convertToXMLGregorian(new Date()));
		// mr.setMessageType(messageType);
		// mr.setMessageTypeVersion(version);
		//
		//
		// // hodnotu je potrebne generovat
		// String uuid = java.util.UUID.randomUUID().toString();
		// String value = uuid;
		// mr.setMessageIdentifier(value);
		// mh.setMessageReference(mr);
		// mess.setMessageStatus("" + messageStatus);
		// mess.setMessageHeader(mh);
		//
		// // 8.3.5. Sekcia CountryCodeISO
		// // ELEMENT_VALUE = "SK"
		// CountryCodeISO country = new CountryCodeISO();
		// country.setValue("SK");
		// mess.setCountryCodeISO(country);
		// // 8.3.6. Sekcia LocationPrimaryInformation
		//
		// LocationPrimaryInformation lpi = new LocationPrimaryInformation();
		//
		// // ResponsibleIM = 3178
		// // PrimaryLocationNameASCII = $PrimLok.LOCATION_NAME_ASCII v ascii
		// // FreightFlag - AK je zadané $PrimLok.FREIGHT_POSSIBLE_FLAG=1 TAK True, INAK False
		// // PassengerFlag - AK je zadané $PrimLok.PASSENGER_POSSIBLE_FLAG=1 TAK True, INAK False
		// lpi.setResponsibleIM(companyCode);
		// lpi.setLocationPrimaryName(dtopl.getLocationName());
		// lpi.setPrimaryLocationNameASCII(dtopl.getLocationNameAscii());
		// lpi.setFreightFlag(("T".equals(dtopl.getFreightPossibleFlag()) ? true : false));
		//
		// lpi.setPassengerFlag(("T".equals(dtopl.getPassengerPossibleFlag()) ? true : false));
		// if (dtopl.getFreightStartValidity() != null || dtopl.getFreightEndValidity() != null) {
		// ValidityPeriod freightFlagValidityPeriod = new ValidityPeriod();
		// freightFlagValidityPeriod.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(dtopl
		// .getFreightStartValidity()));
		// freightFlagValidityPeriod.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(dtopl
		// .getFreightEndValidity()));
		// lpi.setFreightValidityPeriod(freightFlagValidityPeriod);
		// }
		//
		// ValidityPeriod passengerValidityPeriod = new ValidityPeriod();
		//
		// if (dtopl.getPassengerStartValidity() != null || dtopl.getPassengerEndValidity() != null) {
		// passengerValidityPeriod.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(dtopl
		// .getPassengerStartValidity()));
		// passengerValidityPeriod.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(dtopl
		// .getPassengerEndValidity()));
		// lpi.setPassengerValidityPeriod(passengerValidityPeriod);
		// }
		// GeographicCoordinates coordinates = new GeographicCoordinates();
		// if (dtopl.getLatitude() != null) {
		// coordinates.setLatitude(dtopl.getLatitude().floatValue());
		// }
		// if (dtopl.getLongitude() != null) {
		// coordinates.setLongitude(dtopl.getLongitude().floatValue());
		// }
		// lpi.setGeographicCoordinates(coordinates);
		// // 8.3.6.2. Sekcia LocationValidityPeriod
		// // Ak $MessageStatus = 3 (DELETE)
		// // StartDateTime = $PredchadzajucaPlatnaLokalita.START_VALIDITY
		// // EndDateTime = $PredchadzajucaPlatnaLokalita.PLATNOST_DO
		// // Inak
		// // StartDateTime = $PrimLok.PLATNOST_OD
		// // EndDateTime = $PrimLok.PLATNOST_DO.setLocationPrimaryInformation(lpi);
		//
		// ValidityPeriod v = new ValidityPeriod();
		// if (messageStatus == 3) // delete
		// {
		// v.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(dtoplPredch.getStartValidity()));
		// v.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(dtoplPredch.getEndValidity()));
		// } else {
		// v.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(dtopl.getStartValidity()));
		// v.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(dtopl.getEndValidity()));
		// }
		//
		// lpi.setLocationValidityPeriod(v);
		// mess.setLocationPrimaryInformation(lpi);
		//
		// // List<AffectedSection> affectedSectionList = rcm.getAffectedSection();
		// // AffectedSection affectedSection = new AffectedSection();
		// //StringWriter sprava = vratSpravuXMLCData(mess);
		// //getCdata(mess);
		//
		// String xmlReceiptConfirmationMessage = vratSpravuXML(mess);
		//
		//
		// // 8.4. Systém vytvorí štruktúru //zapis do tabuliek cud_send a cud_send_subor
		//
		// Date cas = new Date();
		// DTOSend dtoSend = new DTOSend();
		// dtoSend.setIDOdberatelObjekt(dtooo.getOdberatelObjektID());
		// dtoSend.setCasVytvorenia(cas);
		// dtoSend.setSpravaUuid(uuid);
		// dtoSend.setSpravaTyp("" + mr.getMessageType());
		// dtoSend.setIdTransakciaZapisane(auth.getTransakciaID());
		// dlgCrd.getCudSendClass().update(auth, dtoSend);
		//
		// // exportnySuborList{SUBOR:Reazec,
		// // ID_CISELNIK:Èíslo,
		// // NAZOV_SUBORU:Reazec,
		// // PORADOVE_CISLO_SUBORU_K_CISELNIKU:Èislo,
		// // IDENTIFIKATOR_SPRAVY:Reazec,
		// // DATUM_CAS_NACITANIA:dátum a èas}
		// // (vytvorený súbor,
		// // cudCiselnik.CISELNIK_ID,
		// // cudCiselnik.NAZOV + "_" + string(datumACasNacitaniaDat-format:YYYYMMDD),
		// // 1,
		// // identifikatorSpravy,
		// // datumACasNacitaniaDat )
		// DTOSendSubor dtoSendSubor = new DTOSendSubor();
		// // dtoSendSubor.setSendSuborID(rVal(r, CudSendSuborPeer.SEND_SUBOR_ID).asIntegerObj());
		// dtoSendSubor.setIDSend(dtoSend.getSendID());
		// dtoSendSubor.setIDCiselnik(dtoCisPrimLoc.getCiselnikID());
		// // dtoSendSubor.setRowIdExt(dtoCisPrimLoc.getCiselnikID());
		// dtoSendSubor.setNazovSuboru(dtoCisPrimLoc.getTabulka() + "_" + sdf.format(cas));
		// // pada DateUtils.formatDate(cas, "YYYYMMDD"));
		// dtoSendSubor.setSubor(xmlReceiptConfirmationMessage);
		// dtoSendSubor.setPoradoveCislo(1);
		// dtoSendSubor.setPocetPokusov(1);
		// dtoSendSubor.setCasVytvorenia(cas);
		// dtoSendSubor.setCasOdoslania(cas);
		// dtoSendSubor.setOdpovedUuid(uuid);
		// dtoSendSubor.setIDTransakciaZapisane(auth.getTransakciaID());
		// dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
		// // dtoSendSubor.setIDTransakciaZrusene(rVal(r, CudSendSuborPeer.ID_TRANSAKCIA_ZRUSENE).asString());
		//
		// // dtoSend.setIdTransakciaZrusene(rVal(r, CudSendPeer.ID_TRANSAKCIA_ZRUSENE).asLong());
		//
		// // doplnUdajeDoWsPrijemTSI(auth, wsPrijem, doplnUdajeDoWsPrijemTSIPohybKod,
		// // doplnUdajeDoWsPrijemTSIRusenieKod);
		// // dlg.getVlakCommonClass().zapisSOKTSI(auth, wsPrijem, xmlReceiptConfirmationMessage);
		// // if ("zapisSupisAktivit".indexOf(sluzba) < 0) {
		// // }
		// try {
		// SendOutboundMessage dataSend = new SendOutboundMessage();
		// dataSend.setMessage(xmlReceiptConfirmationMessage); // vratSpravuXMLCData(mess).toString());
		// SendOutboundMessageResponse replicationResponsePrimLoc = getWsCrdSend(dataSend, dtooo);
		//
		// if (replicationResponsePrimLoc != null
		// && !"SUCCESS".equals(replicationResponsePrimLoc.getResponse())) {
		// Object res = replicationResponsePrimLoc.getResponse();
		// actRes.setError(true);
		// actRes.setErrorMsg("Chyba pri volani ws/primarylocation_file_dataset "
		// + replicationResponsePrimLoc.getResponse());
		// email_text = CudVysielanieUtils.getEmailText(actRes.getErrorMsg(), "ExportCrdDat");
		// NotifUtils.sendNotif("", mailingList, "Pri príprave súborov pre export CRD došlo k chybe",
		// CudVysielanieUtils.getEmailText(email_text, "exportCrdDat"));
		//
		// // anika navratovy objekt replicationResponsePrimLoc.getResponse() by nemalo byt typu
		// // objekt, je potrebne pretypovat, neviem naco
		// // ReceiptConfirmationMessage resrep = (ReceiptConfirmationMessage) replicationResponsePrimLoc
		// // .getResponse();
		//
		// dtoSendSubor.setNavratKod("1");
		// // dtoSendSubor.setNavratText(res);
		// dtoSendSubor.setErrorSprava(actRes.getErrorMsg());
		// dtoSendSubor.setErrorCas(new Date());
		// // dtoSendSubor.setOdpovedTyp(res);
		// // dtoSendSubor.setOdpovedSubor(replicationResponsePrimLoc.);
		// dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
		//
		// return actRes;
		// }
		//
		// } catch (Throwable e) {
		// // handleException(e, "exportCrdDat.exportCrdDat.error", auth);
		// actRes.setError(true);
		// String chyba = "Chyba pri volani ws/primarylocation_file_dataset url=" + dtooo.getExportCesta()
		// + " ERROR:" + e.getMessage();
		// log.info(chyba);
		// actRes.setErrorMsg(chyba);
		// email_text = CudVysielanieUtils.getEmailText(chyba, "ExportCrdDat");
		// NotifUtils.sendNotif("", mailingList, "Pri príprave súborov pre export CRD došlo k chybe",
		// email_text);
		// dtoSendSubor.setNavratKod("1");
		// // dtoSendSubor.setNavratText(res);
		// dtoSendSubor.setErrorSprava(actRes.getErrorMsg());
		// dtoSendSubor.setErrorCas(new Date());
		// // dtoSendSubor.setOdpovedTyp(res);
		// // dtoSendSubor.setOdpovedSubor(replicationResponsePrimLoc.);
		// dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
		// return actRes;
		// }
		// // zapis do wsSend
		// dtoSendSubor.setNavratKod("00");
		// dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
		//
		// }
		// vysielaneData += "" + pocetZaznamovNaSpracovanie;
		//
		// // ////////////////////////////// subsudieary location
		// ArrayList<DTOTSubsidiaryLocation> listSubsidiaryLocation = dlgCrd.getTCudCiselnikyClass()
		// .getSubsidiaryLocationListForExport(auth, datumAcasNacitania, datumAcasPoslSprac,
		// tCountry.getCountryID());
		//
		// // vysielaneData += " pocet vybranych SubsidiaryLocation = " + pocetZaznamovNaSpracovanie;
		//
		// // vysielaneData += "" + (listSubsidiaryLocation == null ? 0 : listSubsidiaryLocation.size());
		// pocetZaznamovNaSpracovanie = 0;
		// //
		// // 6. /*$PrimaryListBuduce*/
		// // vyh¾adám všeky záznamy, ktoré vznikli medzi posledným exportom a aktuálnym dátumom
		// // Systém vyh¾adá platné v budúcnosti
		// // ArrayList<DTOTSubsidiaryLocation> listSubsidiaryLocationBuduce = dlgCrd.getTCudCiselnikyClass()
		// // .getSubsidiaryLocationBuduceForExport(auth, datumAcasNacitania, datumAcasPoslSprac,
		// // tCountry.getCountryID());
		// // listSubsidiaryLocation.addAll(listSubsidiaryLocationBuduce);
		// Integer oldSubLocId = 0;
		// for (DTOTSubsidiaryLocation dtosl : listSubsidiaryLocation) {
		// // 8.1 vrat nadradenu primaryLocation
		// //
		// if (!statusOKnotify()) {
		// log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudExportCrdProcess konci.");
		// return actRes;
		// }
		// if (dtosl.getSubsidiaryLocationID().equals(oldSubLocId)) {
		// // vyskytlo sa viacero zmien, predchadzajuce preskocim
		// continue;
		// }
		// pocetZaznamovNaSpracovanie += 1;
		// oldSubLocId = dtosl.getSubsidiaryLocationID();
		// DTOTPrimaryLocation tPrimLoc = dlgCrd.getTCudCiselnikyClass().getPrimaryLocationByIdPrimLoc(auth,
		// dtosl.getIDPrimaryLocation(), dtosl.getPlatnostOd());
		//
		// // // Systém vyh¾adá èi záznam zo vstupu je zapísaný v Vrát záznam pod¾a kódu
		// if (tPrimLoc == null || tPrimLoc.getPrimaryLocationID() == null) { // nie je povinna polozka
		//
		// // navratovyKod = 3;
		// // popisSpracovania += ;
		// // chyba += "nebol nájdený PrimaryLocation = " + ws.getPrimaryLocation().getLocationCode();
		// log.error("CudExportProcess.error: "
		// + "Chyba - nebol nájdený záznam väzobného číselníka PrimaryLocation. IDPrimaryLocation="
		// + dtosl.getIDPrimaryLocation());
		// continue;
		// }
		// // 8.2. /*$PredchadzajucaPlatnaLokalita*/
		// // Systém vyh¾adá,èi pre $SubsLok existujú starašie záznamy ako dátum a èas posledného exportu
		//
		// DTOTSubsidiaryLocation dtoplPredch = dlgCrd.getTCudCiselnikyClass().getSubLocationListPredch(auth,
		// dtosl.getSubsidiaryLocationCode(), dtosl.getIDCountry(), datumAcasPoslSprac);
		//
		// LocationFileDatasetMessage mess = new LocationFileDatasetMessage();
		// // 8.2.1 AK NEnašlo záznam Alebo
		// // AK NE našlo záznam alebo naslo a PredchadzajucaPlatnaLokalita.ZMAZ=true
		// int messageStatus;
		// if (dtoplPredch == null || "T".equals(dtoplPredch.getZmaz())) {
		// // $MessageStatus = 1 (NEW)
		// messageStatus = 1;
		// } else if ("F".equals(dtosl.getZmaz())) {
		// // AK $PrimLok.ZMAZ = FALSE
		// // $MessageStatus = 2 (UPDATE)
		// messageStatus = 2;
		//
		// } else {
		// // $MessageStatus = 3 (DELETE)
		// messageStatus = 3;
		//
		// }
		// // 8.3 /* identifikatorSpravy = jendoznaèný identifikátor (Guid))
		// MessageHeader mh = new MessageHeader(); // rsdqm.getMessageHeader();
		// // Systém vygeneruje jednoznaèný identifikátor do identifikatorSpravy
		// String uuid = java.util.UUID.randomUUID().toString();
		//
		// // 8.4.Systém vytvorí správu (súbor) LocationFileDatasetMessage a naplní ju nasledovne:
		// // LocationFileDatasetMessage.LocationPrimaryCode = $NadradenaLokalita.LOCATION_CODE
		// // Sekcia LocationFileDatasetMessage:
		// // MessageStatus = $MessageStatus
		// // LocationPrimaryCode = prvých 5 èíslic z $PrimLok.LOCATION_CODE
		//
		// mess.setLocationPrimaryCode(Integer.parseInt(tPrimLoc.getLocationCode()));
		// mess.setMessageStatus("" + messageStatus);
		//
		// // 8.4.3. Sekcia MessageHeader
		// // Sekcia MessageHeader
		// // MessageRoutingID - nepoužíva sa
		// // SenderReference - nepouživa sa
		// // MessageDateTimeCreated = aktuálny dátum a èas
		// // Sekcia Sender
		// // CI_InstanceNumber = "01"
		// // ELEMENT_VALUE = "0056"
		// // (<Sender n1:CI_InstanceNumber="01">0056</Sender> )
		//
		// Sender ms = new Sender();
		// ms.setCIInstanceNumber(1);
		// ms.setValue(companyCode);
		// mh.setSender(ms);
		//
		// // 8.3.3.3. - nemam kde zapisat
		// // Sekcia Recipient
		// // CI_InstanceNumber = "01"
		// // ELEMENT_VALUE = "3178"
		// // (<Recipient n1:CI_InstanceNumber="01">3178</Recipient>)
		// Recipient rec = new Recipient();
		// rec.setCIInstanceNumber(1);
		// rec.setValue("3178");
		//
		// mh.setRecipient(rec);
		// // // 8.4.4 Sekcia MessageReference
		// // MessageDateTime = aktuálny dátum a èas
		// // MessageType = 6002
		// // MessageTypeVersion = "3.1.0.1" - podla GX ma byt 2.2.3
		// // MessageIdentifier = vygenerovany identifikatorSpravy
		//
		// MessageReference mr = new MessageReference();
		// mr.setMessageDateTime(CudVysielanieUtils.convertToXMLGregorian(new Date()));
		// mr.setMessageType(messageType);
		// mr.setMessageTypeVersion(version);
		// mr.setMessageIdentifier(uuid);
		// mh.setMessageReference(mr);
		//
		// mess.setMessageHeader(mh);
		//
		// // 8.4.5. Sekcia CountryCodeISO
		// // ELEMENT_VALUE = "SK"
		// CountryCodeISO country = new CountryCodeISO();
		// country.setValue("SK");
		// mess.setCountryCodeISO(country);
		//
		// // 8.4.6. Sekcia LocationSubsidiaryInformation
		//
		// LocationSubsidiaryInformation lsi = new LocationSubsidiaryInformation();
		// // LocationSubsidiaryName = $SubsLok.LOCATION_NAME
		// // LocationSubsidiaryCode.LocationSubsidiaryTypeCode = $SubsLok.SUBSIDIARY_LOCATION_CODE
		//
		// lsi.setLocationSubsidiaryName(dtosl.getSubsidiaryLocationName());
		// lsi.setAllocationCompany(companyCode);
		// LocationSubsidiaryCode scvalue = new LocationSubsidiaryCode();
		//
		// String subType = dlgCrd.getTCudCiselnikyClass().getSubsidiaryType(auth, dtosl.getIDSubsidiaryType());
		// if (subType != null) {
		// scvalue.setLocationSubsidiaryTypeCode("" + Integer.valueOf(subType));
		// }
		// scvalue.setValue(dtosl.getSubsidiaryLocationCode());
		// // scvalue.setLocationSubsidiaryTypeCode(subType);
		// lsi.setLocationSubsidiaryCode(scvalue);
		//
		// // Sekcia LocationValidityPeriod
		// // Ak $MessageStatus = 3 (DELETE)
		// // StartDateTime = $PredchadzajucaPlatnaLokalita.START_VALIDITY
		// // EndDateTime = $PredchadzajucaPlatnaLokalita.PLATNOST_DO
		// // Inak
		// // StartDateTime = $SubsLo.PLATNOST_OD
		// // EndDateTime = $SubsLo.PLATNOST_DO
		// ValidityPeriod v = new ValidityPeriod();
		// if (messageStatus == 3) // delete
		// {
		// v.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(dtoplPredch.getStartValidity()));
		// v.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(dtoplPredch.getEndValidity()));
		// } else {
		// v.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(dtosl.getStartValidity()));
		// v.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(dtosl.getEndValidity()));
		// }
		//
		// lsi.setValidityPeriod(v);
		// mess.setLocationSubsidiaryInformation(lsi);
		//
		// // List<AffectedSection> affectedSectionList = rcm.getAffectedSection();
		// // AffectedSection affectedSection = new AffectedSection();
		//
		// // anika podla mna tu je potrebne zavolat aj WS
		// String xmlReceiptConfirmationMessage = vratSpravuXML(mess);
		// SendOutboundMessage parameters = new SendOutboundMessage();
		// parameters.setMessage(xmlReceiptConfirmationMessage);
		// // doplnUdajeDoWsPrijemTSI(auth, wsPrijem, doplnUdajeDoWsPrijemTSIPohybKod,
		// // doplnUdajeDoWsPrijemTSIRusenieKod);
		// // dlg.getVlakCommonClass().zapisSOKTSI(auth, wsPrijem, xmlReceiptConfirmationMessage);
		// // if ("zapisSupisAktivit".indexOf(sluzba) < 0) {
		// // }
		// // 8.4. Systém vytvorí štruktúru //zapis do tabuliek cud_send a cud_send_subor
		//
		// Date cas = new Date();
		// DTOSend dtoSend = new DTOSend();
		// dtoSend.setIDOdberatelObjekt(dtooo.getOdberatelObjektID());
		// dtoSend.setCasVytvorenia(cas);
		// dtoSend.setSpravaUuid(uuid);
		// dtoSend.setSpravaTyp("" + mr.getMessageType());
		// dtoSend.setIdTransakciaZapisane(auth.getTransakciaID());
		// dlgCrd.getCudSendClass().update(auth, dtoSend);
		//
		// // exportnySuborList{SUBOR:Reazec,
		// // ID_CISELNIK:Èíslo,
		// // NAZOV_SUBORU:Reazec,
		// // PORADOVE_CISLO_SUBORU_K_CISELNIKU:Èislo,
		// // IDENTIFIKATOR_SPRAVY:Reazec,
		// // DATUM_CAS_NACITANIA:dátum a èas}
		// // (vytvorený súbor,
		// // cudCiselnik.CISELNIK_ID,
		// // cudCiselnik.NAZOV + "_" + string(datumACasNacitaniaDat-format:YYYYMMDD),
		// // 1,
		// // identifikatorSpravy,
		// // datumACasNacitaniaDat )
		// DTOSendSubor dtoSendSubor = new DTOSendSubor();
		// // dtoSendSubor.setSendSuborID(rVal(r, CudSendSuborPeer.SEND_SUBOR_ID).asIntegerObj());
		// dtoSendSubor.setIDSend(dtoSend.getSendID());
		// dtoSendSubor.setIDCiselnik(dtoCisSubLoc.getCiselnikID());
		// // dtoSendSubor.setRowIdExt(dtoCisSubLoc.getCiselnikID());
		// dtoSendSubor.setNazovSuboru(dtoCisSubLoc.getTabulka() + "_" + sdf.format(cas));
		// // pada DateUtils.formatDate(cas, "YYYYMMDD"));
		// dtoSendSubor.setSubor(xmlReceiptConfirmationMessage);
		// dtoSendSubor.setPoradoveCislo(1);
		// dtoSendSubor.setPocetPokusov(1);
		// dtoSendSubor.setCasVytvorenia(cas);
		// dtoSendSubor.setCasOdoslania(cas);
		// dtoSendSubor.setOdpovedUuid(uuid);
		// dtoSendSubor.setIDTransakciaZapisane(auth.getTransakciaID());
		// dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
		//
		// try {
		// //
		// SendOutboundMessageResponse replicationResponsePrimLoc = getWsCrdSend(parameters, dtooo);
		// // dlgCrd
		// // .getCrdExportWSFromOdberatelExportCesta(dtooo.getExportCesta())
		// // .getOutboundConnectorServicePort().sendOutboundMessage(parameters, false);
		// if (replicationResponsePrimLoc != null
		// && !"SUCCESS".equals(replicationResponsePrimLoc.getResponse())) {
		//
		// Object res = replicationResponsePrimLoc.getResponse();
		// actRes.setError(true);
		// actRes.setErrorMsg("Chyba pri volani ws/primarylocation_file_dataset "
		// + replicationResponsePrimLoc.getResponse());
		// email_text = CudVysielanieUtils.getEmailText(actRes.getErrorMsg(), "ExportCrdDat");
		// NotifUtils.sendNotif("", mailingList, "Pri príprave súborov pre export CRD došlo k chybe",
		// CudVysielanieUtils.getEmailText(email_text, "exportCrdDat"));
		// dtoSendSubor.setNavratKod("1");
		// // dtoSendSubor.setNavratText(res);
		// dtoSendSubor.setErrorSprava(actRes.getErrorMsg());
		// dtoSendSubor.setErrorCas(new Date());
		// // dtoSendSubor.setOdpovedTyp(res);
		// // dtoSendSubor.setOdpovedSubor(replicationResponsePrimLoc.);
		// dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
		// return actRes;
		// }
		//
		// } catch (Throwable e) {
		// // handleException(e, "exportCrdDat.exportCrdDat.error", auth);
		// actRes.setError(true);
		// String chyba ="Chyba pri volani ws/primarylocation_file_dataset " + e.getMessage();
		// actRes.setErrorMsg(chyba);
		// email_text = CudVysielanieUtils.getEmailText(chyba, "ExportCrdDat");
		// NotifUtils.sendNotif("", mailingList, "Pri príprave súborov pre export CRD došlo k chybe",
		// email_text);
		// dtoSendSubor.setNavratKod("1");
		// // dtoSendSubor.setNavratText(res);
		// dtoSendSubor.setErrorSprava(actRes.getErrorMsg());
		// dtoSendSubor.setErrorCas(new Date());
		//
		// dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
		// return actRes;
		// }
		// // doplnit zapis do wsSend
		// // 8.4. Systém vytvorí štruktúru //zapis do tabuliek cud_send a cud_send_subor
		// dtoSendSubor.setNavratKod("00");
		// dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
		// // List<AffectedSection> affectedSectionList = rcm.getAffectedSection();
		// }
		// vysielaneData += " pocet vybranych SubsidiaryLocation = " + pocetZaznamovNaSpracovanie;
		//
		// log.info(" CudExportProcess " + vysielaneData);
		//
		// } catch (Throwable e) {
		//
		// // handleException(e, "exportCrdDat.exportCrdDat.error", auth);
		// actRes.setError(true);
		// actRes.setErrorMsg("Chyba pri volani ws/location_file_dataset " + e.getMessage());
		//
		// email_text = CudVysielanieUtils.getEmailText(actRes.getErrorMsg() + vysielaneData, "ExportCrdDat");
		// NotifUtils.sendNotif("", mailingList, "Pri príprave súborov pre export CRD došlo k chybe", email_text);
		// return actRes;
		// }

		email_text = CudVysielanieUtils.getEmailText(actRes.getErrorMsg(), "ExportCrdDat");
		NotifUtils.sendNotif("", mailingList, "Info CUD CRD EXPORT",
				CudVysielanieUtils.getEmailText(vysielaneData, "exportCrdDat"));

		log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudExportCrdProcess konci.");
		return new ActionResult();
	}

	public static String getCritGreaterEqualsActDateOrNull(String atribut, Date datum) {
		String sql = "";

		if (null != atribut) {
			MyCriteria2 crit = new MyCriteria2();
			crit.addConditional(atribut, datum, MyCriteria2.GREATER_EQUAL);
			sql = crit.getCriterion(atribut).toString();

			sql = "( " + sql + " OR " + atribut + " IS NULL)";
		}
		return sql;
	}


	public String vratSpravuXML(LocationFileDatasetMessage mess) throws AppException {

		StringWriter sw = new StringWriter();

		try {
			JAXBContext context = JAXBContext.newInstance(LocationFileDatasetMessage.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.marshal(mess, sw);
			// sprava = "<![CDATA[" + sw.toString() + "]]>";
			// sprava = sw.toString();
			return sw.toString();
		} catch (Throwable t) {
			handleException(t, "vratSpravuXML.error");
		}

		return null;
	}

	public SendOutboundMessageResponse getWsCrd(SendOutboundMessage data, DTOOdberatelObjekt dtooo)
			throws AppException, MalformedURLException {
		SendOutboundMessageResponse res = null;
		log.info("CudExportCrdProcess WSSend:" + data.getMessage());
		res = dlgCrd.getCrdExportWSFromOdberatelExportCesta(dtooo.getExportCesta()).getOutboundConnectorServicePort()
				.sendOutboundMessage(data, false);
		return res;
	}



	public SendOutboundMessageResponse getWsCrdSend(SendOutboundMessage data, DTOOdberatelObjekt dtooo)
			throws AppException, MalformedURLException {
		SendOutboundMessageResponse res = null;
		log.info("CudExportCrdProcess WSSend:" + data.getMessage());
		OutboundConnectorService_Service stub2 = dlgCrd.getCrdExportWSFromOdberatelExportCesta(dtooo.getExportCesta());
		res = stub2.getOutboundConnectorServicePort().sendOutboundMessage(data, true);
		return res;
	}

}
