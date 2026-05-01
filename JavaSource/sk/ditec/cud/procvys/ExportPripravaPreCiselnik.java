package sk.ditec.cud.procvys;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.hlp.HlpOpravneniaNaAtributy;
import sk.ditec.cud.procvys.dto.DTOStlpecInValue;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.notif.NotifUtils;

public class ExportPripravaPreCiselnik {

    private final _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

    public void pripravaPreCiselnik(
            DTOOdberatelObjekt odberatelObjekt,
            DTOCiselnik dtoCiselnik,
            DTOObjektCiselnik objektCiselnik,
            Date datumACasNacitaniaDat) {
        AuthInfo auth = AuthInfo.system();

        try {
            LinkedHashMap<String, List<String>> dataList = null;
            List<DTOStlpecInValue> stlpecInValueList = null;
            List<DTOCiselnikStlpec> stlpceMetaList = dlg.getCiselnikStlpecRead().vratStlpcePreCiselnik(auth, dtoCiselnik.getCiselnikID());
            if ("F".equals(odberatelObjekt.getVsetkyCiselniky()) && objektCiselnik != null) {
                // Uprava stlpceMetaList podla opravneni
                stlpecInValueList = new HlpOpravneniaNaAtributy().preCudCiselnikStlpec(objektCiselnik.getIDCiselnik(),
                        objektCiselnik, true, stlpceMetaList);
            }

            // Vyber dat pre poskytnutie
            Integer objektCiselnikId = objektCiselnik != null ? objektCiselnik.getObjektCiselnikID() : null;

            if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_ROZSAH_ZMENENE.equals(odberatelObjekt.getExportRozsah())) {
                // System vyhlada Zmeny pre Ciselniky zo vstupu
                List<Integer> zmenaStavList = dlg.getZmenaStavHistRead()
                        .vratZmenyKDatumuIdList(auth, dtoCiselnik.getCiselnikID(), odberatelObjekt.getCasPoslExportu(), datumACasNacitaniaDat, _CudConsts.ZMENA_STAV_PAU);
                if (zmenaStavList != null && !zmenaStavList.isEmpty()) {
                    // System vypyta data
                    dataList = dlg.getCiselnikRead().vratDataZmeneneOdDatumu(auth, objektCiselnikId,
                            odberatelObjekt.getCasPoslExportu(), datumACasNacitaniaDat, stlpceMetaList, stlpecInValueList, true);
                }
            } else if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_ROZSAH_VSETKY.equals(odberatelObjekt.getExportRozsah())) {
                // System vypyta data, system vracia strankovane data podla parametrov v Page
                dataList = dlg.getCiselnikRead().vratDataCiselnika(auth, objektCiselnikId, stlpceMetaList, stlpecInValueList, false);
            }

            // Poskytnutie dat
            if (dataList != null) {
                new ExportVytvorenieSuboru().pripravaSuborovPreExportVytvorenieSuboru(auth, odberatelObjekt, dtoCiselnik,
                        dataList, stlpceMetaList, datumACasNacitaniaDat);
            }

        } catch (Throwable t) {
            String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.crd.mail");
            String emailText = CudVysielanieUtils.getEmailText(
                    "OdberatelObjektID = " + odberatelObjekt.getOdberatelObjektID() +
                            "CiselnikID = " + dtoCiselnik.getCiselnikID(),
                    "ExportDat");
            NotifUtils.sendNotif("", mailingList, "Pri príprave súborov pre export došlo k chybe", emailText);
        }
    }
}
