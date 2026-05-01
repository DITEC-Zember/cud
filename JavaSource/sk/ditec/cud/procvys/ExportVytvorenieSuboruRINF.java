package sk.ditec.cud.procvys;

import static sk.ditec.cud.utils._CudConsts.GROUP_RINF;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.crd.dto.DTOSend;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.enums.SkupinaPrijemcov;
import sk.ditec.cud.hlp.HlpOdosliSpravu;
import sk.ditec.cud.procvys.dto.DTOExportnySubor;
import sk.ditec.exp.RinfExportClass;

public class ExportVytvorenieSuboruRINF {
    private static final Logger log = LoggerFactory.getLogger(ExportVytvorenieSuboruRINF.class);
    private final _CudCrdDelegate dlgCrd = new _CudCrdDelegate();

    public ActionResult exportVytvorenieSuboruRINF(AuthInfo auth, DTOOdberatelObjekt cudOdberatelObjekt, Date datumAcasNacitaniaDat) {

        try {
            // 3. Volanie metódy služby RINF WS DataService getRinfKomplet
            ActionResult result = (new RinfExportClass()).exportRinfDat(auth);
            if (result.isError()) {
                String errorMsg = "Pri príprave súboru RINFData.xml pre export došlo k chybe: Odberate_objekt_ID = " + cudOdberatelObjekt.getOdberatelObjektID() + " "
                        + "ErrorMsg = " + result.getKeyErrorMsg();
                log.error(result.getKeyErrorSubj(), errorMsg);
                HlpOdosliSpravu.sendErrorMail(errorMsg, "Pri príprave súborov pre export RINF došlo k chybe", SkupinaPrijemcov.RINF);
                return result;
            }

            String adresar = FrameworkUtils.getConfigProperty(GROUP_RINF, "rinf.dataservice.path");
			String nazovSuboru = result.getResult().toString(); // generujNazovSuboru("RINFData",
																// datumAcasNacitaniaDat);

           // String subor = result.getResult().toString();
            UUID guid = UUID.randomUUID();
			// File file = new File(adresar, nazovSuboru);
			// saveFile(subor, file);

            // 6 Štruktúra:exportnySuborList[]
            List<DTOExportnySubor> exportnySuborList = new ArrayList<DTOExportnySubor>();
            DTOExportnySubor exportnySubor = new DTOExportnySubor();
            exportnySubor.setSubor(null);
            exportnySubor.setIdCiselnik(null);
            exportnySubor.setNazovSuboru(nazovSuboru);
            exportnySubor.setPoradoveCislo(1);
            exportnySubor.setSpravaUuid(guid);
            exportnySubor.setCasVytvorenia(datumAcasNacitaniaDat);
            exportnySuborList.add(exportnySubor);
			if (cudOdberatelObjekt != null && cudOdberatelObjekt.getOdberatelObjektID() != null) {
            vytvorZaznamOOdoslaniSuboru(auth, cudOdberatelObjekt, exportnySuborList.toArray(new DTOExportnySubor[0]), guid);
			}
        } catch (Throwable t) {
            String errorMsg = "Pri príprave súboru RINFData.xml pre export došlo k chybe: Odberate_objekt_ID = " + cudOdberatelObjekt.getOdberatelObjektID();
            log.error(errorMsg, t);
            HlpOdosliSpravu.sendErrorMail(errorMsg, "Pri príprave súborov pre export RINF došlo k chybe", SkupinaPrijemcov.RINF);
        }
        return new ActionResult();
    }

    private void vytvorZaznamOOdoslaniSuboru(AuthInfo auth, DTOOdberatelObjekt dtoOdberatelObjekt, DTOExportnySubor[] exportnySuborList, UUID guid) throws AppException {
        // 6.1 Systém vytvorí záznam v údajoch o odoslaní súboru
        DTOSend dtoSend = dlgCrd.getCudSendClass().vytvorZaznam(auth, dtoOdberatelObjekt.getOdberatelObjektID(), guid.toString(), null);

        // 6.2 Systém vytvorí záznam s pripraveným súborom
        dlgCrd.getCudSendSuborClass().vytvorZaznam(auth, dtoSend.getSendID(), exportnySuborList);
    }

    static String generujNazovSuboru(String nazov, Date datumACasNacitaniaDat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy_MM_dd");
        return nazov + "_" + simpleDateFormat.format(datumACasNacitaniaDat) + ".xml";
    }

    private void saveFile(String text, File file) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(text);
            writer.flush();
        } catch (IOException e) {
            log.error("Neuspesne vytvorenie suboru", e);
            throw e;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    log.error("Nepodarilo sa zavriet subor", e);
                }
            }
        }
    }
}
