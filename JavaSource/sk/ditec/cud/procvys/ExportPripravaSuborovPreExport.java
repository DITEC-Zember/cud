package sk.ditec.cud.procvys;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOObjekt;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.notif.NotifUtils;
import sk.ditec.process.BaseProcess;

public class ExportPripravaSuborovPreExport extends BaseProcess {

    private final _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

    private final DTOOdberatelObjekt odberatelObjekt;
    private final Date datumACasNacitaniaDat;

    public ExportPripravaSuborovPreExport(DTOOdberatelObjekt dtooo, Date date) {
        odberatelObjekt = dtooo;
        datumACasNacitaniaDat = date;
    }

    @Override
    protected void process() throws Throwable {
        AuthInfo auth = AuthInfo.system();

        // vrat typy ciselnikov pre podporovany vystup
        String[] typCiselnikaList = dlg.getCiselnikRead().vratTypyCiselnikovSHistoriouVZakladnejScheme();

        try {
            if (odberatelObjekt != null) {
                if ("T".equals(odberatelObjekt.getVsetkyCiselniky())) {
                    List<DTOCiselnik> ciselnikList = dlg.getCiselnikRead().vratAktivneCiselniky(auth, typCiselnikaList, null);
                    if (ciselnikList != null) {
                        for (DTOCiselnik dtoCiselnik : ciselnikList) {
                            // System zavola pripravu suboru pre export ciselnika
                            new ExportPripravaPreCiselnik().pripravaPreCiselnik(odberatelObjekt, dtoCiselnik, null, datumACasNacitaniaDat);
                        }
                    }
                } else if ("F".equals(odberatelObjekt.getVsetkyCiselniky())) {
                    // System nacita objekt pre export
                    DTOObjekt dtoF = new DTOObjekt();
                    dtoF.setObjektID(odberatelObjekt.getIDObjekt());
                    DTOObjekt cudObjekt = dlg.getObjektRead().loadData(auth, dtoF);
                    if ("RINF".equals(cudObjekt.getNazov())) {
                        // System zavola vytvorenie exportneho suboru pre RINF
                        new ExportVytvorenieSuboruRINF().exportVytvorenieSuboruRINF(auth, odberatelObjekt, datumACasNacitaniaDat);

                    } else if ("ExportLokaciiCRD".equals(cudObjekt.getNazov())) {
                        // System nacita ciselniky naviazane na exportny objekt
                        DTOObjektCiselnik dtoObjektCiselnikF = new DTOObjektCiselnik();
                        dtoObjektCiselnikF.setObjektCiselnikID(odberatelObjekt.getIDObjekt());
                        List<DTOObjektCiselnik> cudObjektCiselnikList = dlg.getObjektCiselnikRead()
                                .vratCiselnikyKObjektu(auth, odberatelObjekt.getIDObjekt(), null);
                        ExportVytvorenieSuboruCRD exportCRD = new ExportVytvorenieSuboruCRD(odberatelObjekt, datumACasNacitaniaDat);
                        for (DTOObjektCiselnik objektCiselnik : cudObjektCiselnikList) {
                            // System vyhlada ciselnik
                            List<DTOCiselnik> ciselnikListLight = dlg.getCiselnikRead()
                                    .ciselnikListLight(auth, null, new Integer[] {objektCiselnik.getIDCiselnik()});
                            if (ciselnikListLight != null && ciselnikListLight.size() == 1) {
                                DTOCiselnik cudCiselnik = ciselnikListLight.get(0);
                                exportCRD.vytvorenieSuboruCrdLocationPrimary(auth, cudCiselnik);
                                exportCRD.vytvorenieSuboruCrdLocationSubsidiary(auth, cudCiselnik);
                            }
                        }

                    } else {
                        // System nacita ciselniky naviazane na exportny objekt
                        List<DTOObjektCiselnik> cudObjektCiselnikList = dlg.getObjektCiselnikRead()
                                .vratCiselnikyKObjektu(auth, odberatelObjekt.getIDObjekt(), typCiselnikaList);
                        for (DTOObjektCiselnik objektCiselnik : cudObjektCiselnikList) {
                            // System vyhlada ciselnik
                            List<DTOCiselnik> ciselnikListLight = dlg.getCiselnikRead()
                                    .ciselnikListLight(auth, null, new Integer[] {objektCiselnik.getIDCiselnik()});
                            if (ciselnikListLight != null && ciselnikListLight.size() == 1) {
                                DTOCiselnik cudCiselnik = ciselnikListLight.get(0);
                                new ExportPripravaPreCiselnik().pripravaPreCiselnik(odberatelObjekt, cudCiselnik, objektCiselnik, datumACasNacitaniaDat);
                            }
                        }
                    }
                }

                // System aktualizuje datum posledneho exportu
                odberatelObjekt.setCasPoslExportu(datumACasNacitaniaDat);
                dlg.getOdberatelObjektModify().update(auth, odberatelObjekt);
            }
        } catch (Throwable t) {
            String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.crd.mail");
            String emailText = CudVysielanieUtils.getEmailText("OdberatelObjektID = " + odberatelObjekt.getOdberatelObjektID(), "ExportDat");
            NotifUtils.sendNotif("", mailingList, "Pri príprave súborov pre export došlo k chybe", emailText);
        }
    }

    static String generujNazovSuboru(String nazov, Date datumACasNacitaniaDat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return nazov + "_" + simpleDateFormat.format(datumACasNacitaniaDat);
    }
}
