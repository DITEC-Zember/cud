package sk.ditec.cud.procvys;

import static sk.ditec.cud.hlp.HlpOdosliSpravu.sendErrorMail;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_DENNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_MESACNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_STVRTROCNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_TYZDENNE;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.torque.TorqueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.Page;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.crd.dto.DTOSendSubor;
import sk.ditec.cud.bi._CudBaseClass;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.enums.SkupinaPrijemcov;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.process.BaseProcess;

public class CudDzOdoslanieExportuProcess extends BaseProcess {
    public static final long KONTROLNY_INTERVAL = 1000 * 60 * 5; // 5min

    public static final long INTERVAL_DEN = 1000L * 60 * 60 * 24;
    public static final long INTERVAL_TYZDEN = INTERVAL_DEN * 7;
    public static final long INTERVAL_MESIAC = INTERVAL_DEN * 30;
    public static final long INTERVAL_3_MESIACE = INTERVAL_MESIAC * 3;

    public static final Locale DEFAULT_LOCALE = new Locale("sk"); //$NON-NLS-1$

    private Logger log = LoggerFactory.getLogger(CudDzOdoslanieExportuProcess.class);
    private _CudDelegateBi dlgBi = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
    private _CudCrdDelegate dlgCrd = new _CudCrdDelegate();

    private final OdoslanieExportuOdoslanieSuboru odoslanieExportu = new OdoslanieExportuOdoslanieSuboru();

    private static Integer lastOdberatelObjektId = null;

    @Override
    protected String getLogName() {
        return "cud.dz.process";
    }

    /**
     * Pri spusteni procesu obsahuje Id OdberatelObjektu ktoreho spracovavanie bolo pri poslednom behu procesu
     * prerusene pre timeout. Ak boli spracovane vsetky OdberatelObjekty tak je rovne null.
     * @throws Throwable
     */
    @Override
    protected void process() throws Throwable {
        AuthInfo auth = AuthInfo.system();
        String classname = this.getClass().getSimpleName();
        log.info("Start - Som proces Odoslanie exportu z CudDzOdoslanieExportuProcess a bezim");

        try {

            List<Integer> odberatelObjektIdsList = dlgBi.getOdberatelObjektRead().najdiOdberatelObjektIdsPreOdoslanieExportu(auth);

            if (odberatelObjektIdsList.isEmpty()) {
                log.warn(classname + ": nebol najdeny ziadne platny CudOdberatelObjekt");
                setLastOdberatelObjektId(null);
                return;
            }

            if (getLastOdberatelObjektId() != null
                    && getLastOdberatelObjektId().equals(odberatelObjektIdsList.get(odberatelObjektIdsList.size() - 1))) {
                setLastOdberatelObjektId(null);
            }

//            odberatelObjektIdsList.sort(Integer::compare);

            log.debug(classname + ": Zacinam od odberatelId " + getLastOdberatelObjektId());
            for (Integer odberatelObjektId : odberatelObjektIdsList) {
                if (getLastOdberatelObjektId() != null && odberatelObjektId <= getLastOdberatelObjektId()) {
                    log.debug(classname + ": OdberatelObjekt id="
                            + odberatelObjektId
                            + " bude preskoceny, hlada sa lastOdberatelObjektId = " + getLastOdberatelObjektId());
                    continue;
                }
                statusOKnotify();
                setLastOdberatelObjektId(odberatelObjektId);
                spracujOdberatelObjekt(auth, odberatelObjektId);
            }
            setLastOdberatelObjektId(null);
        } catch (Throwable t) {
            log.error("chyba procesu " + classname, t);
        }
    }

    private void spracujOdberatelObjekt(AuthInfo auth, Integer odberatelObjektId) throws AppException {
        String classname = this.getClass().getSimpleName();

        // kazdy odberatel uzatvoreny vo vlastnej tranzakcii
        AuthInfo newAuth = _CudBaseClass.cloneAuthInfoForNewTransaction(auth);

        try {
            startTransaction(newAuth, "CUDdataModify");
            getConnection(newAuth);

            log.info(classname + ": Zacina spracovanie OdberatelObjekt id=" + odberatelObjektId);
            DTOOdberatelObjekt dtoF = new DTOOdberatelObjekt();
            dtoF.setOdberatelObjektID(odberatelObjektId);
            DTOOdberatelObjekt[] cudOdberatelObjektList = dlgBi.getOdberatelObjektRead().list(newAuth, new Page(), dtoF);
            DTOOdberatelObjekt cudOdberatelObjekt = (cudOdberatelObjektList == null || cudOdberatelObjektList.length != 1) ? null : cudOdberatelObjektList[0];

            if (cudOdberatelObjekt == null) {
                throw new AppException("Nenasiel sa odberatel objekt pre id = " + odberatelObjektId);
            }

            distribuciaSprav(newAuth, cudOdberatelObjekt);

            returnConnection(newAuth);
            endTransaction(newAuth, true);
        } catch (Throwable t) {
            String errorSubj = classname + ": Odoslanie emailu zlyhalo alebo prislo k inej chybe";
            log.warn(errorSubj, t);
            sendErrorMail(t.toString(), errorSubj, SkupinaPrijemcov.CUD);
            rollbackConnection(newAuth);
            endTransaction(newAuth, false);
        }
//        } finally {
//            VylUtils.dbProtectLeak(newAuth);
//        }
    }

    private void distribuciaSprav(AuthInfo auth, DTOOdberatelObjekt cudOdberatelObjekt)
            throws AppException, TorqueException {
        long currentTimeMillis = System.currentTimeMillis();
        String classname = this.getClass().getSimpleName();

        // 2.1 Ak CudOdberatelObjekt.CAS_POSL_EXPORTU_PLAN je null
        // inicializuj planovany export: Systém nastaví v CUD_ODBERATEL_OBJEKT.CAS_POSL_EXPORTU_PLAN=.PLATNOST_OD
        if (cudOdberatelObjekt.getCasPoslExportu() == null || cudOdberatelObjekt.getCasPoslExportuPlan() == null) {
            // podla mail konumikacie sa inicializacia CAS_POSL_EXPORT ma inicializovat uz v tomto bode
            if (cudOdberatelObjekt.getCasPoslExportu() == null) {
                cudOdberatelObjekt.setCasPoslExportu(new Date(cudOdberatelObjekt.getPlatnostOd().getTime()));
            }

            if (cudOdberatelObjekt.getCasPoslExportuPlan() == null) {
                cudOdberatelObjekt.setCasPoslExportuPlan(new Date(cudOdberatelObjekt.getPlatnostOd().getTime()));
            }

            dlgBi.getOdberatelObjektModify().update(auth, cudOdberatelObjekt);	//podla mail komunikacie sa inicializovat ma do databazy
        }

        // 3. system zrata nasledujuci cas exportu
        Date nasledujuciCasExportu = vratNasledujuciCasExportu(cudOdberatelObjekt, currentTimeMillis, KONTROLNY_INTERVAL);

        if (nasledujuciCasExportu == null)
            throw new AppException("Nie je mozne vypocitat nasledujuciCasExportu. Su spravne nastavene data (napr. OPAKOVANIE) pre odberatel objekt id = " + cudOdberatelObjekt.getOdberatelObjektID());

        log.debug(classname + ": Pre OdberatelObjekt id=" + cudOdberatelObjekt.getOdberatelObjektID()
                + " je nasledujuciCasExportu = " + formatDateTimeMillisecond(nasledujuciCasExportu)
                + " pri currentTimeMillis = " + formatDateTimeMillisecond(currentTimeMillis));

        // 3.1.1 Systém zaspí a spustí sa od ZAÈIATKU SQD za 10 min
        if (nasledujuciCasExportu.getTime() + KONTROLNY_INTERVAL > currentTimeMillis) {
            log.info(classname + ": Spracovanie OdberatelObjektu id=" + cudOdberatelObjekt.getOdberatelObjektID()
                    + " sa odklada lebo nasledujuciCasExportu + KONTROLNY_INTERVAL > currentTimeMillis ");
            return;
        }

        // 4 /*datumACasNacitaniaDat */
        Date datumACasNacitaniaDat = new Date(nasledujuciCasExportu.getTime());

        // 5 Systém naèíta záznamy z CUD_SEND_SUBOR ktoré ešte neboli exportované
        List<DTOSendSubor> suborList = dlgCrd.getCudSendSuborClass().vratZaznamyPreExport(auth, cudOdberatelObjekt, false);
        log.debug(classname + ": Bolo najdenych " + suborList.size() + " zaznamov [CudSendSubor] pre export pre "
                + "odberatelObjekt id=" + cudOdberatelObjekt.getOdberatelObjektID());
        for (DTOSendSubor cudSendSubor : suborList) {
            // vycitame cely sendSubor vratane blob-ov
            DTOSendSubor dto = dlgCrd.getCudSendSuborClass().vratSuborPreExport(auth, cudSendSubor.getSendSuborID());
            VysledokOdoslaniaSuborov result = odoslanieExportu.odoslanieSuboru(auth, dto, cudOdberatelObjekt);
            if (result.isBolaChyba()) {
                log.warn("Pri odoslani exportu prislo k chybe, spracovanie prerusene. " + result.getErrorMsg());
                return;
            }
        }
        suborList = null;

        // 6 Systém zistí ci záznam CudOdberatelObjekt má vykonat export
        List<DTOOdberatelObjekt> cudOdberatelObjektList = dlgBi.getOdberatelObjektRead().vratOdberatelObjektSoZmenamiPreExport(
                auth,
                cudOdberatelObjekt,
                datumACasNacitaniaDat);
        if (cudOdberatelObjektList != null && !cudOdberatelObjektList.isEmpty()) {
            DTOOdberatelObjekt cudOdberatelObjektZmeny = cudOdberatelObjektList.get(0);


            if (cudOdberatelObjektZmeny == null) {
                log.warn(classname + ": Pre OdberatelObjekt id=" + cudOdberatelObjektZmeny.getOdberatelObjektID() + " sa nenasli ziadne zmeny pre export, aj napriek tomu ze mali");
            }

            // 6.1.1 Systém pripraví dáta pre export odberate¾a
            try {
                ExportPripravaSuborovPreExport pripravaSuborovPreExport = new ExportPripravaSuborovPreExport(cudOdberatelObjekt, datumACasNacitaniaDat);
                pripravaSuborovPreExport.process();
            } catch(Throwable t) {
                String errorMsg = "Pri prípave súboru pre export došlo k chybe: " + t.getMessage();
                log.error(errorMsg, t);
                sendErrorMail(errorMsg, "Pri prípave súboru pre export došlo k chybe", SkupinaPrijemcov.CUD);
                return;
            }

            // 6.1.1 Systém naèíta záznamy z CUD_SEND_SUBOR ktoré ešte neboli exportované
            suborList = dlgCrd.getCudSendSuborClass().vratZaznamyPreExport(auth, cudOdberatelObjekt, false);
            log.debug(classname + ": Bolo najdenych " + suborList.size() + " zaznamov [CudSendSubor] pre export pre "
                    + "odberatelObjekt id=" + cudOdberatelObjekt.getOdberatelObjektID());

            if (suborList.isEmpty()) {
                log.warn(classname + ": Nevytvoril sa list suborov na odoslanie pre OdberatelObjekt id=" + cudOdberatelObjekt.getOdberatelObjektID());
                return;
            }
            for (DTOSendSubor cudSendSubor : suborList) {
                // 6.1.1.1.3.1
                DTOSendSubor dto = dlgCrd.getCudSendSuborClass().vratSuborPreExport(auth, cudSendSubor.getSendSuborID());
                VysledokOdoslaniaSuborov result = odoslanieExportu.odoslanieSuboru(auth, dto, cudOdberatelObjekt);
                if (result.isBolaChyba()) {
                    log.warn("Pri odoslani suboru prislo k chybe, spracovanie prerusene. " + result.getErrorMsg());
                    return;
                }
            }
        } else {
            log.info(classname + ": Pre OdberatelObjekt id=" + cudOdberatelObjekt.getOdberatelObjektID() + " nie su ziadne zmeny pre export");
        }

        // 7 System nastavi posledny planovany export
        dlgBi.getOdberatelObjektModify().nastavPoslednyPlanovanyExport(auth, cudOdberatelObjekt);
        log.info(classname + ": Spracovanie OdberatelObjekt id=" + cudOdberatelObjekt.getOdberatelObjektID() + " bolo dokoncene."
                + " PoslednyPlanovanyExport = " + formatDateTimeMillisecond(cudOdberatelObjekt.getCasPoslExportuPlan()));
    }

    /*
    Systém zráta nasledujúci čas exportu

    AK cudOdberatelObjekt.OPAKOVANIE = "Denne"  tak INTERVAL=1 den  END AK
    AK cudOdberatelObjekt.OPAKOVANIE = "Týždenne" tak  INTERVAL=7 dní  END AK
    AK cudOdberatelObjekt.OPAKOVANIE = "Mesačne"  tak  INTERVAL=1 mesiac  END AK
    AK cudOdberatelObjekt.OPAKOVANIE = "3 mesiace" tak  INTERVAL=3 mesiace END AK
    AK cudOdberatelObjekt.OPAKOVANIE = "Pri zmene" tak
        INTERVAL=0,
        AK cudOdberatelObjekt.CAS_POSL_EXPORTU je null
            NASLEDUJÚCI_EXPORT= cudOdberatelObjekt.PLATNOST_OD+KONTROLNY_INTERVAL
        INAK
            NASLEDUJÚCI_EXPORT = aktuálny dátum a čas - KONTROLNY_INTERVAL
       END
    END AK

    AK INTERVAL<>0
        AK cudOdberatelObjekt.CAS_POSL_EXPORTU je null
                 NASLEDUJÚCI_EXPORT=cudOdberatelObjekt.PLATNOST_OD+INTERVAL
        ELSE
               NASLEDUJÚCI_EXPORT=cudOdberatelObjekt.CAS_POSL_EXPORTU_PLAN+INTERVAL
     END AK

    Systém vráti hodnotu NASLEDUJÚCI_EXPORT
*/
    private Date vratNasledujuciCasExportu(DTOOdberatelObjekt cudOdberatelObjekt, long currentTimeMillis, long kontrolnyInterval) {

        long interval = 0;
        Date nasledujuciExport = null;

        String opakovanie = cudOdberatelObjekt.getOpakovanie();
        if (ODBERATEL_OBJEKT_OPAKOVANIE_DENNE.equals(opakovanie)) {
            interval = INTERVAL_DEN;
        } else if (ODBERATEL_OBJEKT_OPAKOVANIE_TYZDENNE.equals(opakovanie)) {
            interval = INTERVAL_TYZDEN;
        } else if (ODBERATEL_OBJEKT_OPAKOVANIE_MESACNE.equals(opakovanie)) {
            interval = INTERVAL_MESIAC;
        } else if (ODBERATEL_OBJEKT_OPAKOVANIE_STVRTROCNE.equals(opakovanie)) {
            interval = INTERVAL_3_MESIACE;
        } else if (ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE.equals(opakovanie)) {
            if (cudOdberatelObjekt.getCasPoslExportu() == null) {
                nasledujuciExport = new Date(cudOdberatelObjekt.getPlatnostOd().getTime() + kontrolnyInterval);
            } else {
                nasledujuciExport = new Date(currentTimeMillis - kontrolnyInterval);
            }
        }

        if (interval != 0) {
            if (cudOdberatelObjekt.getCasPoslExportu() == null) {
                nasledujuciExport = new Date(cudOdberatelObjekt.getPlatnostOd().getTime() + interval);
            } else {
                nasledujuciExport = new Date(cudOdberatelObjekt.getCasPoslExportuPlan().getTime() + interval);
            }
        }

        return nasledujuciExport;
    }

    public void setLastOdberatelObjektId(Integer lastOdberatelObjektId) {
        log.debug("Ukladam si odberatelID " + lastOdberatelObjektId);
        this.lastOdberatelObjektId = lastOdberatelObjektId;
    }

    public static Integer getLastOdberatelObjektId() {
        return lastOdberatelObjektId;
    }

    public static String formatDateTimeMillisecond(Date date) {
        String pattern = "dd.MM.yyyy HH:mm:ss.SSS";
        DateFormat dateFormat = new SimpleDateFormat(pattern, DEFAULT_LOCALE);
        return dateFormat.format(date);
    }

    public static String formatDateTimeMillisecond(long time) {
        Date date = new Date(time);
        return formatDateTimeMillisecond(date);
    }
}
