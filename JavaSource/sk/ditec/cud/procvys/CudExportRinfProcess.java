package sk.ditec.cud.procvys;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.notif.NotifUtils;
import sk.ditec.process.BaseProcess;
import sk.ditec.zsr.common.server.utils.DateUtils;

public class CudExportRinfProcess extends BaseProcess {

	private Logger log = LoggerFactory.getLogger(CudExportRinfProcess.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private _CudCrdDelegate dlgCrd = new _CudCrdDelegate();

	 
    @Override
	protected String getLogName() {
		return "export.rinf";
	}

	@Override
	protected void process() throws Throwable {

		// process 602 Export dát Rinf
		// CUD CAS DZ Odoslanie exportu
		log.info("Start - Som proces CudRinfExportProcess a bezim");
		String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.rinf.mail");
		String email_text = " CHYBA pri EXPORT CudRinfProcess ";

		try {
			Date datumACasNacitaniaDat = new Date();

			AuthInfo auth = AuthInfo.system();
			// 1. vstup



			if (!statusOKnotify()) {
						log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudRinfExportProcess konci.");
				return;
			}

			try {
				ActionResult actionres = (new ExportVytvorenieSuboruRINF()).exportVytvorenieSuboruRINF(auth, new DTOOdberatelObjekt(),
						datumACasNacitaniaDat);
				if (actionres != null && actionres.getKeyErrorMsg() != null) {
					// poslanie emailu je osetrene aj v casti Rinf
					log.info("End - Som proces RINFExportProcess koncim chyba ");
					return;
				}
				
			} catch (Exception e) {
						DBUtils.handleException(e, "CudRinfExportProcess.error");
						email_text += e.getMessage();
						NotifUtils.sendNotif("", mailingList, "Chyba pri CUD RINF EXPORT", email_text);
				log.info("End - Som proces RINFExportProcess koncim chyba " + email_text);
				return;
			}



		} catch (Exception e) {
			// Ak poèas vytvárania súboru nastane chyba
			// ErrorMsg = "Pri príprave súborov pre export došlo k chybe: Odberate_objekt_ID = " +
			// cudOdberatelObjekt.ODEBRATEL_OBJEKT_ID + ", ¡¡Císelník_ID = cudCiselnik.CISELNIK_ID
			// Systém odošle chybovú správu a vráti sa do volajúceho procesu

			DBUtils.handleException(e, "CudRinfExportProcess.error");
			email_text += e.getMessage();
			NotifUtils.sendNotif("", mailingList, "Chyba pri CUD RINF EXPORT", email_text);
			log.info("End - Som proces RINFExportProcess koncim chyba " + email_text);
			return;
		}
		String name = "RINFK_" + DateUtils.formatDateYY_MM_DD(new Date());
		email_text = "Súbor " + name + " bol vygenerovaný";
		NotifUtils.sendNotif("", mailingList, "Automatický mail o vygenerovaní RinfDataServiceKomplete", email_text);
		log.info("End - Som proces RINFExportProcess a koncim");
	}


}
