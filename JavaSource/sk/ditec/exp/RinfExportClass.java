package sk.ditec.exp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.ws.AuthInfoWS;
import sk.ditec.common.ws.WSUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.zsr.common.server._NovyPISBaseClass;


public class RinfExportClass extends _NovyPISBaseClass {

	//private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private static final Logger log = LoggerFactory.getLogger(RinfExportClass.class);

	private _CudCrdDelegate dlgCrd = new _CudCrdDelegate();

	public ActionResult exportRinfDat(AuthInfo auth) {
		ActionResult actRes = new ActionResult();
		try {

			AuthInfoWS authWS = WSUtils.toAuthWS(auth);

			String res = dlgCrd.getRinfExportWS().getRinfDataWSPort().getRinfDataServiceKomplet(authWS);
			actRes.setResult(res);
			// if (res == null || res.isEmpty()) {
			if (!"OK".equals(res.substring(0, 2))) {
				actRes.setError(true);
				String errorText = "Služba getRinfDataServiceKomplet neexportovala súbor.";
				log.error(errorText);
				// processu
				actRes.setKeyErrorSubj("Pri príprave súborov pre export RINF došlo k chybe.");
				actRes.setKeyErrorMsg(errorText);
				return actRes;

			}

		} catch (Throwable e) {
			//DBUtils.handleException(e, "CudRinfExportProcess.error");
			//handleException(e, "exportRinfDat.error", auth);
			log.error("exportRinfDat: chyba volania sluzby getRinfDataServiceKomplet", e);
			actRes.setError(true);
			String errorText = "ExportRinfDat: " + e.getMessage();
			actRes.setKeyErrorSubj("Chyba pri tvorbe getRinfDataServiceKomplet");																					// processu
			actRes.setKeyErrorMsg(errorText);
			return actRes;
		}
		return actRes;
	}



}
