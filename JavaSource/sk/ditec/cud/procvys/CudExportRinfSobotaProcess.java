package sk.ditec.cud.procvys;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOObjekt;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.notif.NotifUtils;
import sk.ditec.process.BaseProcess;
import sk.ditec.zsr.common.server.utils.DateUtils;

public class CudExportRinfSobotaProcess extends BaseProcess {

	private Logger log = LoggerFactory.getLogger(CudExportRinfSobotaProcess.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private _CudCrdDelegate dlgCrd = new _CudCrdDelegate();

	 
    @Override
	protected String getLogName() {
		return "export.crd.sobota";
	}

	@Override
	protected void process() throws Throwable {

		// process 601 Export dát Rinf
		// CUD CAS DZ Odoslanie exportu
		log.info("Start - Som proces CudExportRinfSobotaProcess a bezim");
		Date datumACasNacitaniaDat = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(datumACasNacitaniaDat);
		int numberDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		// The resulting number ranges from 1 (Sunday) to 7 (Saturday).
		if (numberDayOfWeek != 7) {
			log.info("End - Som proces CudExportRinfSobotaProcess, nie je sobota koncim");
			return;
		}

		String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.rinf.mail");
		String email_text = " CHYBA pri EXPORT CudExportRinfSobotaProcess ";

		try {


			AuthInfo auth = AuthInfo.system();
			// 1. vstup

			DTOObjekt dtoF = new DTOObjekt();
			// naplnim nazov
			dtoF.setNazov("RINF");
			dtoF.setPlatny("T");
			DTOObjekt[] listObj = dlg.getObjektRead().listForList(auth, new Page(), dtoF);
			
			if (listObj == null || listObj.length == 0) {
				// chyba
				log.info("Nie je definovany objekt pre Export Rinf data, vykonavanie CudRinfExportProcess konci.");
				email_text += "Nie je definovany objekt pre Export Rinf data, vykonavanie CudRinfExportProcess konci.";
				NotifUtils.sendNotif("", mailingList, "Chyba pri CUD RINF EXPORT", email_text);
				log.info("End - Som proces CudExportRinfSobotaProcess koncim chyba " + email_text);
				return;

			}

			for ( DTOObjekt dtoobj: listObj) {
			Integer idObjekt = dtoobj.getObjektID();
				List<DTOOdberatelObjekt> listoo = dlgCrd.getTCudCiselnikyClass().getOOlist(auth, datumACasNacitaniaDat,
						idObjekt);
				if (listoo == null || listoo.size() == 0) {
					log.info("Nie je definovany platny objekt v CUD_ODBERATEL_OBJEKT pre Export Rinf data, vykonavanie CudRinfExportProcess konci.");
					email_text += "Nie je definovany objekt pre Export Rinf data, vykonavanie CudRinfExportProcess konci.";
					NotifUtils.sendNotif("", mailingList, "Chyba pri CUD RINF EXPORT", email_text);
					log.info("End - Som proces CudExportRinfSobotaProcess koncim chyba " + email_text);
					return;
				}
		for ( DTOOdberatelObjekt  dtooo : listoo ) {
			// 2. KONTROLNY_INTERVAL=5min
			// AK CudOdberatelObjekt.POSLEDNY_PLANOVANY_EXPORT je null
			// Systém stupustí inicializáciu POSLEDNEHO EXPORTU
			// Systém nastaví v CUD_ODBERATEL_OBJEKT.POSLEDNY_PLANOVANY_EXPORT=.PLATNOST_OD
			if (dtooo.getCasPoslExportu()==null ) {
				//anika ma byt platnost_od ale boli by prilis velke objekty, tak nastavim na 1.3.2023
						dtooo.setCasPoslExportu(new SimpleDateFormat("dd/MM/yyyy").parse("01/04/2023"));
			}
			
					// pre ucely ladenia
					// dtooo.setCasPoslExportu(new SimpleDateFormat("dd/MM/yyyy").parse("01/03/2023"));
			// Systém zráta nasledujúci čas exportu
			// AK cudOdberatelObjekt.POSLEDNY_EXPORT nie je null
			// AK cudOdberatelObjekt.POSLEDNY_EXPORT<cudOdberatelObjekt.POSLEDNY_PLANOVANY_EXPORT
			// Systém vrátu cudOdberatelObjekt.POSLEDNY_EXPORT
			// END AK
            
			Date datumACasPoslPlanImport = ( new Date(datumACasNacitaniaDat.getTime() + (10 * 1000 * 60)) );
					if (dtooo.getCasPoslExportu().getTime() > datumACasPoslPlanImport.getTime()) {
					 //nevykonam zmenu  
					 continue;
				 }

			// CUD DZ Príprava súborov pre export
			//2.1.1.3.2  Inak Ak cudObjekt.NAZOV = "ExportlokaciiCRD"
//				/*ObjektCiselnik= CUD_OBJEKT_CISELNIK*/
//				Systém naèita èiselníky naviazané na exportný objekt

			//

			if (!statusOKnotify()) {
						log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudExportRinfSobotaProcess konci.");
				return;
			}

			try {
						ActionResult actionres = (new ExportVytvorenieSuboruRINF()).exportVytvorenieSuboruRINF(auth,
 dtooo,
						datumACasPoslPlanImport);
						if (actionres != null && actionres.getKeyErrorMsg() != null) {
							// poslanie emailu je osetrene aj v casti Rinf
							// return; //pokracujem, lebo je to vo for, dalsie pokusy mozu byt uspesne
						}
				
			} catch (Exception e) {
						DBUtils.handleException(e, "CudExportRinfSobotaProcess");
						email_text += e.getMessage();
						NotifUtils.sendNotif("", mailingList, "Chyba pri CUD RINF EXPORT", email_text);
						log.info(" CudExportRinfSobotaProcess  chyba " + email_text);

			}

			//Systém nastaví 
			//POSLEDNY_EXPORT  = vst.DatumACasExportu
			//kde ODBERATEL_OBJEKT_ID = vst. OdberatelObjektID
			dtooo.setCasPoslExportu(datumACasNacitaniaDat);

					dtooo.setCasPoslExportuPlan(CudVysielanieUtils.getCasNaslImp(dtooo, datumACasNacitaniaDat));
					dlg.getOdberatelObjektModify().update(auth, dtooo);
			
			
			if (!statusOKnotify()) {
						log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudExportRinfSobotaProcess konci.");
						return;
			}
			
			} //end for ( DTOOdberatelObjekt  dtooo : listoo )
			} //end for ( DTOObjekt dtoobj: listObj)

		} catch (Exception e) {
			// Ak poèas vytvárania súboru nastane chyba
			// ErrorMsg = "Pri príprave súborov pre export došlo k chybe: Odberate_objekt_ID = " +
			// cudOdberatelObjekt.ODEBRATEL_OBJEKT_ID + ", ¡¡Císelník_ID = cudCiselnik.CISELNIK_ID
			// Systém odošle chybovú správu a vráti sa do volajúceho procesu

			DBUtils.handleException(e, "CudExportRinfSobotaProcess.error");
			email_text += e.getMessage();
			NotifUtils.sendNotif("", mailingList, "Chyba pri CUD RINF EXPORT", email_text);
			log.info("End - Som proces CudExportRinfSobotaProcess koncim chyba " + email_text);
			return;
		}
		String name = "RINFK_" + DateUtils.formatDateYY_MM_DD(new Date());
		email_text = "Súbor " + name + " bol vygenerovaný";
		NotifUtils.sendNotif("", mailingList, "Automatický mail o vygenerovaní RinfDataServiceKomplete", email_text);

		log.info("End - Som proces CudExportRinfSobotaProcess a koncim");
	}


}
