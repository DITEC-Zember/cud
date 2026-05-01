package sk.ditec.cud.proc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.crd.dto.DTOCrdSpracovanieZmeny;
import sk.ditec.crd.dto.DTOTCompany;
import sk.ditec.crd.dto.DTOTCountry;
import sk.ditec.crd.dto.DTOTDopravnyBod;
import sk.ditec.crd.dto.DTOTStanicnaKolaj;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.notif.NotifUtils;
import sk.ditec.process.BaseProcess;
import sk.ditec.zsr.common.server.utils.DateUtils;

public class CudAktLocZDbProcess extends BaseProcess {

	private Logger log = LoggerFactory.getLogger(CudAktLocZDbProcess.class);
	
	private _CudCrdDelegate dlg = new _CudCrdDelegate();
	private static String StringdateFormat = "dd.MM.yyyy HH:mm:ss";
	private static DateFormat dateFormat = new SimpleDateFormat(StringdateFormat, new Locale("sk"));

	@Override
	protected void process() throws Throwable {
		// process 67 Aktualizácia Primárnych lokalít z T_DOPRAVNY_BOD
		// Automatický proces spúšaný každú hodinu
		log.info("Start - Som proces CudAktLocZDbProcess a bezim");
		DTOCrdSpracovanieZmeny dtoZmenaCRD = new DTOCrdSpracovanieZmeny();
			AuthInfo auth = AuthInfo.system();
		String email_text = " CHYBA pri CudAktLocZDbProcess ";
		String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.crd.mail");
			try {

			// ActionResult actionres =
			// (new AktualizaciaLocZDbClass()).aktualizuj(auth);

			ActionResult actRes = new ActionResult();
			Date datumCasPoslAkt;

			Date datumACasAktualizacie = new Date();
			// Automatický proces spúšaný každú hodinu
			// cudCiselnik = èíselník T_DOPRAVNY_BOD

			// datumACasVytvorenia = aktuálny dátum a èas

			String stringDatumCasPoslSprac = dlg.getCudParametreClass().getValue(auth, "aktualizaciaLocZDb");
			// format 07.03.2024 20:07:49
			// pre ucely ladenia
			// stringDatumCasPoslSprac = "08.03.2024 00:00:00";
			try {
				datumCasPoslAkt = dateFormat.parse(stringDatumCasPoslSprac);
			} catch (Throwable e) {
				datumCasPoslAkt = dateFormat.parse("01.01.2025 00:00:00");
			}
			// datumCasPoslAkt = dateFormat.parse("28.11.2025 10:00:00");
			// $SubOperacia = NULL - parameter pridane do dtozmena
			// $Operacia = NULL - parameter pridane do dtozmena

			// 6. /*ZmenaList[] = CUD_ZMENA[]/*

			// DTOCiselnik dtoCis = dlgcud.getCiselnikRead().readLight(auth, "T_DOPRAVNY_BOD");
			// Systém vyh¾adá údaje o zmene èíselníka T_PRIMARY_LOCATION a vyh¾adá najstarší záznam
			// Systém vyhľadá CUD_ZMENA, kde:
			// CUD_ZMENA.CUD_CISELNIK = vstup:Číselník
			// A zároveň
			// CUD_ZMENA.PLATNOST_OD je MAX
			// Systém vráti MAX CUD_ZMENA.PLATNOST_OD
			// Date maxDatumZmeny = readMaxDatumZmeny(auth, idDopravnyBod);
			// if (maxDatumZmeny == null) {
			// koniec spracovania
			// return actRes;
			// }

			// 9. /*ZmenaList[] = CUD_ZMENA[]/*
			// Systém vyh¾adá Zmeny pre Èíselník a naèíta Zmeny do ZmenaList
			Integer idDopravnyBod = dlg.getTCudCiselnikyClass().getIdCiselnik(auth, "T_DOPRAVNY_BOD");
			Integer idStanicaKolaj = dlg.getTCudCiselnikyClass().getIdCiselnik(auth, "T_STANICNA_KOLAJ");

			Integer[] idCiselnikovList = new Integer[] { idDopravnyBod, idStanicaKolaj };

			ArrayList<DTOZmena> zmenaListDb = dlg.getAktualizaciaLocZDbClass().getZmenyList(auth, datumCasPoslAkt, idCiselnikovList, "PAU");

			if (zmenaListDb == null || zmenaListDb.size() == 0) {
				// nie su data na spracovanie
				String error = "CudAktLocZDbProcess pocet zaznamov na spracovanie = 0";
				log.info(error);			
				//email_text = error;
				//NotifUtils.sendNotif("", mailingList, "Chyba pri CUD aktualizacia lokalit z DB", email_text);
				dlg.getCudParametreClass().update(auth, "aktualizaciaLocZDb",
						DateUtils.formatDate(datumACasAktualizacie, "dd.MM.yyyy HH:mm:ss"));
				return ;
			}
			// 6.1.1. /* $IDCompanyZSR */
			// uiccode = "0056"
			log.info("CudAktLocZDbProcess pocet zaznamov na spracovanie = " + zmenaListDb.size());
			DTOTCompany tCompany = dlg.getTCudCiselnikyClass().getCompany(auth, "0056");
			// Integer idCrdStlpec = getIdStlpec(auth, idDopravnyBod, "CRD");

			//
			// if (tCompany == null || tCompany.getCompanyID() == null) { // nie je povinna polozka

			// }

			/* $IDCountrySK */
			// codeIso = "SVK"
			// Systém vyh¾adá ID country z èíselníka T_COUNTRY platný k aktuálnemu dátumu
			DTOTCountry tCountry = dlg.getTCudCiselnikyClass().getCountryByIso(auth, "SK");
			if (tCountry == null || tCountry.getCountryID() == null) {
				String error = "CudAktLocZDbProcess nebolo najdene tCountry pre kod SK";
				log.info(error);			
				email_text = error;
				NotifUtils.sendNotif("", mailingList, "Chyba pri CUD aktualizacia lokalit z DB", email_text);
				return ;
			}

			boolean chyba = false;
			Integer idSubLoc = dlg.getTCudCiselnikyClass().getIdCiselnik(auth, "T_SUBSIDIARY_LOCATION");
			Integer idPrimLoc = dlg.getTCudCiselnikyClass().getIdCiselnik(auth, "T_PRIMARY_LOCATION");

			// 6.1.3. PRE každý záznam $Zmena zo ZmenaList[]
			for (DTOZmena zmena : zmenaListDb) {
				if (!statusOKnotify()) {
					log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudAktLocZDbProcess konci.");
					return;
				}

				// 6.1.3.1./*zmenaCRD*/
				// Systém nájde CUD_SPRACOVANIE_ZMENY_CRD pre $zmena.ZMENA_ID and zmenaCRD.ERROR is null
				// // ked je v chybe null, tak je to spracovane uz nespracujem.
				dtoZmenaCRD = dlg.getCrdSpracovanieZmenyClass().getSpracovaneDto(auth,
						zmena.getZmenaID());
				// Ak neexistuje spracovanie v zmenaCRD
				if (dtoZmenaCRD == null) {

					// Systém vytvorí záznam CUD_SPRACOVANIE_ZMENY_CRD
					// kde ID_ZMENA= vst. Id
					// DATUM_VOLANIA= aktuálny dátum a čas
					// ERROR="Spracovávam záznam"
					dtoZmenaCRD = new DTOCrdSpracovanieZmeny();
					dtoZmenaCRD.setIDZmena(zmena.getZmenaID());
					dtoZmenaCRD.setDatumVolania(new Date());
					dtoZmenaCRD.setChybovaSprava("Spracovávam záznam");
					dlg.getCrdSpracovanieZmenyClass().update(auth, dtoZmenaCRD);

					// 6.1.3.2.2 Ak Zmena.CUD_CISELNIK.TABULKA='T_DOPRAVNY_BOD'
					if (idDopravnyBod.equals(zmena.getIDCiselnik())) {
						/* $dataRow - dáta z tabu¾ky */
						// Systém naèíta z T_DOPRAVNY_BOD záznam kde DOPRAVNY_BOD_ID=Zmena.ROW_ID a zároveò
						// ID_ZMENA=Zmena.ZMENA_ID

						DTOTDopravnyBod dtoDataRow = dlg.getTCudCiselnikyClass().getDopravnyBodByZmenaId(auth,
								zmena.getRowID(), zmena.getZmenaID());
						// Spracuj primárnu Lokalitu
						actRes = dlg.getAktualizaciaPrimLocZDbClass().aktualizujPrimLoc(auth, zmena,
								datumACasAktualizacie, dtoDataRow, tCountry, tCompany, idPrimLoc, idSubLoc);
						if (actRes.isError()) {
							dtoZmenaCRD.setChybovaSprava(actRes.getErrorMsg() + dtoDataRow.toString());
							if ("T".equals(actRes.getErrorSubj())) {
								dtoZmenaCRD.setChyba("F");
								chyba = true;
							} else {
								dtoZmenaCRD.setChyba("F");
							}

							// email_text += " neuspesna aktualizacia: " + actRes.getErrorMsg() + dtoDataRow.toString()
							// + "<br>" + "<br>";
						} else {
							dtoZmenaCRD.setChybovaSprava(null);
							dtoZmenaCRD.setChyba(null);
						}
					} else if (idStanicaKolaj.equals(zmena.getIDCiselnik())) {
						DTOTStanicnaKolaj dtoDataRow = dlg.getTCudCiselnikyClass().getStanicnaKolajList(auth,
								zmena.getRowID(), zmena.getZmenaID());
						actRes = dlg.getAktualizaciaSubLocZDbClass().aktualizujSubLoc(
								// auth, zmena, vsupOperacia, vstupPlatnostOd, dataRowDb, dataRowKolaj, tCountry,
								// tCompany)
								auth, zmena, null, datumACasAktualizacie, null, dtoDataRow, tCountry, tCompany,
								idPrimLoc, idSubLoc);
						if (actRes.isError()) {
							dtoZmenaCRD.setChybovaSprava(actRes.getErrorMsg() + dtoDataRow.toString());
							if ("T".equals(actRes.getErrorSubj())) {
								dtoZmenaCRD.setChyba("F");
								chyba = true;
							} else {
								dtoZmenaCRD.setChyba("F");
							}
							email_text += " neuspesna aktualizacia: " + actRes.getErrorMsg() + dtoDataRow.toString()
									+ "<br>" + "<br>";
						} else {
							dtoZmenaCRD.setChyba(null);
							dtoZmenaCRD.setChybovaSprava(null);
						}

					}
					dlg.getCrdSpracovanieZmenyClass().update(auth, dtoZmenaCRD);
				} // if (dtoZmenaCRD==null )

			}// end for

			if (chyba) {
				log.info("CudAktLocZDbProcess.error: " + actRes.getErrorMsg());
				NotifUtils.sendNotif("", mailingList, "Chyba pri CUD aktualizacia lokalit z DB",
						email_text + actRes.getErrorMsg());
			} else {
				dlg.getCudParametreClass().update(auth, "aktualizaciaLocZDb",
						DateUtils.formatDate(datumACasAktualizacie, "dd.MM.yyyy HH:mm:ss"));
			}



		} catch (Exception e) {
			email_text = " Chyba pri CUD aktualizacia lokalit z DB <br>" + e.getMessage() + "<br>";
			NotifUtils.sendNotif("", mailingList, "Chyba CudAktLocZDbProcess", email_text);
			dtoZmenaCRD.setChyba("T");
			dlg.getCrdSpracovanieZmenyClass().update(auth, dtoZmenaCRD);
			log.error(email_text);
		}




		log.info("End - Som proces CudAktLocZDbProcess a koncim");
	}

	@Override
	protected String getLogName() {
		return "aktualizacia.lokalit";
	}
}
