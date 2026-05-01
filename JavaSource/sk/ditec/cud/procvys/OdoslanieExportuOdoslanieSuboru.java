package sk.ditec.cud.procvys;

import static sk.ditec.cud.hlp.HlpOdosliSpravu.sendErrorMail;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.crd.dto.DTOSend;
import sk.ditec.crd.dto.DTOSendSubor;
import sk.ditec.crd.generated.tsi.LocationFileDatasetMessage;
import sk.ditec.crdexp.ws.SendOutboundMessage;
import sk.ditec.crdexp.ws.SendOutboundMessageResponse;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOObjekt;
import sk.ditec.cud.dto.DTOOdberatel;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.enums.SkupinaPrijemcov;
import sk.ditec.cud.utils._CudConsts;

public class OdoslanieExportuOdoslanieSuboru {
    private static final Logger log = LoggerFactory.getLogger(ExportVytvorenieSuboruRINF.class);
    public static final String EXPORT_LOKACII_CRD = "ExportLokaciiCRD";
    public static final String EXPORT_TYP_URI = "URI";
    public static final String EXPORT_TYP_DIR = "DIR";
    public static final String SYSTEMOVY_KANAL_OBM = "OBM";
    public static final String BOOL_FALSE = "F";
    private final _CudCrdDelegate dlgCrd = new _CudCrdDelegate();
    private final _CudDelegateBi dlgBi = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

    public VysledokOdoslaniaSuborov odoslanieSuboru(AuthInfo auth, sk.ditec.cud.dto.DTOSendSubor dtoSendSubor, DTOOdberatelObjekt cudOdberatelObjekt) throws AppException {
        DTOSendSubor cudSubor = dlgCrd.getCudSendSuborClass().vratSuborPreExport(auth, dtoSendSubor.getSendSuborID());
        return odoslanieSuboru(auth, cudSubor, cudOdberatelObjekt);
    }

    public VysledokOdoslaniaSuborov odoslanieSuboru(AuthInfo auth, DTOSendSubor cudSubor, DTOOdberatelObjekt cudOdberatelObjekt) throws AppException {
        DTOSend dtoSendF = new DTOSend();
        dtoSendF.setSendID(cudSubor.getIDSend());
        List<DTOSend> cudOdoslanieList = dlgCrd.getCudSendClass().getList(auth, dtoSendF);
        DTOSend cudOdoslanie = (cudOdoslanieList == null || cudOdoslanieList.size() != 1) ? null : cudOdoslanieList.get(0);
        if (cudOdoslanie == null) {
            throw new AppException("Pre SEND_SUBOR_ID = " + cudSubor.getSendSuborID() + " sa nenasiel prave jeden objekt CUD_SEND", "Odoslanie suboru - nenasiel sa objekt CUD_SEND");
        }

        // 2
        Date errorCas = cudSubor.getErrorCas() != null ? new Date(cudSubor.getErrorCas().getTime()) : null;

        // 2.1
        Date errorCasVratanePozdrzania = null;

        // Rozlozenie podmienky v bode 3.1, kde ma zmysel vycitavat nastavenie len pre subor uz raz oznaceny chybovym datumom.
        // a rovno si spocitame o kolko minut sa by sa mala notifikacia oneskorit pre tento subor
        if (errorCas != null) {
            // vycitanie z configu
            Integer pozdrzanieVysielaniaVMinutach = null;
            String sysConfigItem = "pozdrzanievysielania";
            String sysConfigValue = null;
            try {
                sysConfigValue = FrameworkUtils.getConfigProperty("cud", sysConfigItem);
                if (sysConfigValue == null || sysConfigValue.isEmpty()) {
                    pozdrzanieVysielaniaVMinutach = 0;
                } else {
                    pozdrzanieVysielaniaVMinutach = Integer.valueOf(sysConfigValue);
                }
            } catch (NumberFormatException e) {
                String popisChyby = "V tabulke SYS_CONFIG pre konfiguracny parameter " + sysConfigItem + " sa nachadza hodnota nespravneho ciselneho formatu: " + sysConfigValue + "!";
                log.error(popisChyby);
                throw new AppException(popisChyby, e);
            } catch (Exception e) {
                String popisChyby = "V tabulke SYS_CONFIG sa nenachadza konfiguracny parameter alebo spravna hodnota parametra: " + sysConfigItem + "!";
                log.error(popisChyby);
                throw new AppException(popisChyby, e);
            }

            // spocitanie celkoveho poctu minut pozdrzania vysielania
            Calendar cal = Calendar.getInstance();
            cal.setTime(errorCas);
            if (pozdrzanieVysielaniaVMinutach > 1) {
                if (cudSubor.getPocetPokusov() > 0) {
                    cal.add(Calendar.MINUTE, cudSubor.getPocetPokusov() * pozdrzanieVysielaniaVMinutach);
                } else {
                    cal.add(Calendar.MINUTE, pozdrzanieVysielaniaVMinutach);
                }
            }
            errorCasVratanePozdrzania = cal.getTime();
        }

        Date aktualnyDatumACas = new Date();
        if ((cudSubor.getPocetPokusov() > 20) ||
                (errorCasVratanePozdrzania != null && errorCasVratanePozdrzania.after(aktualnyDatumACas))) {

            // 3.1.1 Ak cudSubor.POCET_POKUSOV =21
            if (cudSubor.getPocetPokusov() == 21) {
                String errorMsg = "Vysielanie skončilo na súbore SUBOR_ID = " + cudSubor.getSendSuborID();

                String odberatel = "";
                DTOOdberatel cudOdberatel = dlgBi.getOdberatelRead().getByOdberatelId(auth, cudOdberatelObjekt.getIDOdberatel());
                if (cudOdberatel != null) {
                    odberatel = cudOdberatel.getNazov();
                }

                String vysielanie = "";
                DTOObjekt objektDTO = dlgBi.getObjektRead().getObjektById(auth, cudOdberatelObjekt.getIDObjekt());
                if (objektDTO != null) {
                    vysielanie = objektDTO.getNazov();
                }

                cudOdberatelObjekt.setAktivny(BOOL_FALSE);
                dlgBi.getOdberatelObjektModify().update(auth, cudOdberatelObjekt);

                String predmet = "Systém čaká na manuálny zásah pre obnovenie vysielania {odberatel:" + odberatel + ",vysielanie:" + vysielanie + "}";

                cudSubor.setPocetPokusov((cudSubor.getPocetPokusov() + 1));
                dlgCrd.getCudSendSuborClass().update(auth, cudSubor);
                sendErrorMail(errorMsg, predmet, SkupinaPrijemcov.CUD);
                return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
            } else if (cudSubor.getPocetPokusov() > 21) {
                // 3.1.1.6 Systém zresetuje pokusy cudSubor.POCET_POKUSOV =1
                cudSubor.setPocetPokusov(1);
                dlgCrd.getCudSendSuborClass().update(auth, cudSubor);
            } else {
                return VysledokOdoslaniaSuborov.bolaChyba(cudSubor.getErrorSprava());
            }
        }

        // 4.1 Ak cudOdberatelObjekt nebol vrateny
        if (cudOdberatelObjekt == null) {
            String errorMsg = "Pri odosielaní súborov pre export došlo k chybe: nenašiel sa platný odberateľ k súboru Súbor_ID = " + cudSubor.getSendSuborID();
            dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "1", null, errorMsg, null, null, null);
            sendErrorMail(errorMsg, "CudOdberatelObjekt nebol nájdený", SkupinaPrijemcov.CUD);
            return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
        }

        // 4.2 Ak cudOdberatelObjekt.EXPORT_CESTA nie je zadaná
        if (cudOdberatelObjekt.getExportCesta() == null || cudOdberatelObjekt.getExportCesta().isEmpty()) {
            String errorMsg = "Pri ukladaní exportného súboru do adresára/alebo odosielaní na ws došlo k chybe. Nie je zadaná cesta pre uloženie excel súboru. Súbor_ID = " + cudSubor.getSendSuborID();
            dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "1", null, errorMsg, null, null, null);
            sendErrorMail(errorMsg, "Nie je zadaná cesta pre uloženie excel súboru", SkupinaPrijemcov.CUD);
            return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
        }

        // 6 /*cudOdberate¾ = CUD_ODBERATEL*/ Systém naèíta údaje odberate¾a
        DTOOdberatel cudOdberatel = dlgBi.getOdberatelRead().getByOdberatelId(auth, cudOdberatelObjekt.getIDOdberatel());
        if (cudOdberatel == null) {
            String errorMsg = "Pri odosielaní súborov pre export došlo k chybe: nenašiel sa platný odberateľ k súboru Súbor_ID = " + cudSubor.getSendSuborID();
            dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "1", null, errorMsg, null, null, null);
            sendErrorMail(errorMsg, "CudOdberateľ nebol nájdený", SkupinaPrijemcov.CUD);
            return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
        }

        // 6 /*cudObjekt= CUD_OBJEKT/* //Systém naèíta údaje objektu
        DTOObjekt cudObjekt = dlgBi.getObjektRead().getObjektById(auth, cudOdberatelObjekt.getIDObjekt());
        if (cudObjekt == null) {
            String errorMsg = "Pri odosielaní súborov pre export došlo k chybe: nenašiel sa platný objekt k súboru Súbor_ID = " + cudSubor.getSendSuborID();
            dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "1", null, errorMsg, null, null, null);
            sendErrorMail(errorMsg, "CudObjekt nebol vrátený", SkupinaPrijemcov.CUD);
            return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
        }

        // 6.2 Ak cudObjekt.SYSTEMOVY_KANAL je "OBM"
        if (SYSTEMOVY_KANAL_OBM.equals(cudObjekt.getSystemovyKanal())) {
            String errorMsg = "Posielanie objektu " + cudObjekt.getNazov() + " nie je podporované";
            sendErrorMail(errorMsg, "cudObjekt nepodporovaný vo vysielaní", SkupinaPrijemcov.CUD);
            return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
        }

        /* Skupina prijemcov */
        SkupinaPrijemcov skupinaPrijemcov = null;
        // 6.3 Ak cudObjekt.SYSTEMOVY_KANAL nie je null
        if (cudObjekt.getSystemovyKanal() != null) {
            skupinaPrijemcov = SkupinaPrijemcov.get(cudObjekt.getSystemovyKanal());
        } else {
            skupinaPrijemcov = SkupinaPrijemcov.CUD;
        }

        log.info("Prebieha odoslanie suboru sendSuborId = " + cudSubor.getSendSuborID());

        // 7. Realizacia exportov
        if (EXPORT_LOKACII_CRD.equalsIgnoreCase(cudObjekt.getNazov())) {
            boolean vysledokExportu = false;

            if (!StringUtils.isValid(cudSubor.getSubor()))
                throw new AppException("Prazdny subor k odoslianiu");

            try {

				// TODO: anika parameters.setMessage(cudSubor.getSubor());
				JAXBContext context = JAXBContext.newInstance(LocationFileDatasetMessage.class);
				Unmarshaller unmarshaller = context.createUnmarshaller();
				String stringXmlReceiptConfirmationMessage = cudSubor.getSubor().replace("\"", "\\\"");
				// StringReader reader = new StringReader(stringXmlReceiptConfirmationMessage);
				// LocationFileDatasetMessage dtoMess = (LocationFileDatasetMessage) unmarshaller.unmarshal(reader);

				SendOutboundMessage dataSend = new SendOutboundMessage();
				dataSend.setMessage(stringXmlReceiptConfirmationMessage);

				CudExportCrdProcess cudExportCrdProcess = new CudExportCrdProcess();
				SendOutboundMessageResponse replicationResponsePrimLoc = cudExportCrdProcess.getWsCrdSend(dataSend,
						cudOdberatelObjekt);

                // 7.1.2 Systém odošle správu s na adresu cudOdberatelObjekt.CESTA_PRE_EXPORT
                vysledokExportu = true;  //TODO: treba naplnit vystupnu premennu sluzby
            } catch (Exception e) {
                log.error("odoslanieSuboru.error", e);
                // 7.1.2.1 Ak pri odosielani doslo k chybe
                String errorMsg = "Pri odosielaní exportného súboru došlo k chybe. Súbor_ID = " + cudSubor.getSendSuborID();
                dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "1", null, errorMsg, null, null, null);
                sendErrorMail(errorMsg, "Pri odosielaní exportného súboru došlo k chybe", skupinaPrijemcov);
                return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
            }

            // 7.1.3 System aktualizuje datum odoslania
            dlgCrd.getCudSendSuborClass().aktualizujDatumOdoslania(auth, cudSubor, new Date());

            // 7.1.4 System prijme vystupnu cast operacie
            if(vysledokExportu) {
                // TODO: subor naplnime z response vysledku exportu; rovnako identifikatorOdpoved=LocationUpdateResponse.MessageIddentifier
                dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "0", "Odoslanie úspešné", null, cudSubor.getSubor(), null, null);
            } else {
                // TODO naplnenie z chyboveho vystupu LocationUpdateFault; navratKod=LocationUpdateFault.FaultCode
                dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "999", "LocationUpdateFault.FaultDescription", null, cudSubor.getSubor(), null, null);
                //return VysledokOdoslaniaSuborov.bolaChyba("LocationUpdateFault.FaultDescription");
            }

        } else if (EXPORT_TYP_DIR.equalsIgnoreCase(cudOdberatelObjekt.getExportTyp())) {
            // 7.1.5 Systém uloží súbor cudSubor.SUBOR  do adresára  cudOdberatelObjekt.EXPORT_CESTA
            try {
                String path = cudOdberatelObjekt.getExportCesta();
                if (!StringUtils.isValid(path))
                    throw new AppException("Nenastavena cesta pre export");

                String filename = cudSubor.getNazovSuboru();
                if (!StringUtils.isValid(filename))
                    throw new AppException("Nenastaveny nazov suboru pre export");

                File dir = new File(path);
                if (dir.isDirectory() && dir.canWrite()) {
                    FileOutputStream outputStream = new FileOutputStream(path + "\\" + filename);
                    outputStream.write(cudSubor.getSubor().getBytes());
                    outputStream.close();
                } else {
                    throw new AppException(path + " nie je adresar alebo nie je do neho mozne zapisovat.");
                }
            } catch (Exception e) {
                log.error("odoslanieSuboru.error", e);
                // 7.1.5.1 Ak pri ukladaní došlo k chybe
                String errorMsg = "Pri ukladaní exportného súboru do adresára došlo k chybe. Súbor_ID = " + cudSubor.getSendSuborID();
                dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "1", null, errorMsg, null, null, null);
                sendErrorMail(errorMsg, "Pri odosielaní exportného súboru došlo k chybe", skupinaPrijemcov);
                return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
            }

            // 7.1.6 System aktualizuje datum odoslania
            dlgCrd.getCudSendSuborClass().aktualizujDatumOdoslania(auth, cudSubor, new Date());
            dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "0", "odoslanie úspešné", null, null, null, null);

        } else if (EXPORT_TYP_URI.equalsIgnoreCase(cudOdberatelObjekt.getExportTyp())) {
            // 7.1.9 Systém odošle správu s cudSubor.SUBOR na adresu cudOdberatelObjekt.EXPORT_CESTA
            try {
                // neimplementovat
                if (true) throw new AppException("neimplementovane");
            } catch (Exception e) {
                log.error("odoslanieSuboru.error", e);
                // 7.1.9.1 Ak pri ukladaní došlo k chybe
                String errorMsg = "Pri odosielaní exportného súboru došlo k chybe. Súbor_ID = " + cudSubor.getSendSuborID();
                dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "1", null, errorMsg, null, null, null);
                sendErrorMail(errorMsg, "Pri odosielaní exportného súboru došlo k chybe", skupinaPrijemcov);
                return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
            }
            // 7.1.10 System aktualizuje datum odoslania
            dlgCrd.getCudSendSuborClass().aktualizujDatumOdoslania(auth, cudSubor, new Date());
            dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, "0", "odoslanie úspešné", null, null, null, null);
        } else {
            String errorMsg = "Pri odoslaní exportného súboru došlo k chybe. Nie je zadaná cesta pre odoslanie. Súbor_ID = " + cudSubor.getSendSuborID();
            dlgCrd.getCudSendSuborClass().aktualizujZaznam(auth, cudSubor, null, null, errorMsg, null, null, null);
            sendErrorMail(errorMsg, "Nie je zadaná cesta pre odoslanie", skupinaPrijemcov);
            return VysledokOdoslaniaSuborov.bolaChyba(errorMsg);
        }

        log.info("Prebieha distribucia sendSuborId = " + cudSubor.getSendSuborID());
        return VysledokOdoslaniaSuborov.nebolaChyba();
    }
}
