package sk.ditec.cud.hlp;

import static sk.ditec.cud.utils._CudConsts.GROUP_CUD;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.enums.SkupinaPrijemcov;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.notif.NotifUtils;

public class HlpOdosliSpravu {

    static Logger log = LoggerFactory.getLogger(HlpOdosliSpravu.class);

    public static void sendErrorMail(String errorMsg, String predmet, SkupinaPrijemcov skupinaPrijemcov) {
        try {
            ArrayList<String> zoznamPrijemcov = new ArrayList<String>();
            String sysConfigItem = null;
            if (SkupinaPrijemcov.CRD.equals(skupinaPrijemcov) || SkupinaPrijemcov.ExportLokaciiCRD.equals(skupinaPrijemcov)) {
                sysConfigItem = "cud.hlp.crd.mail";
            } else if (SkupinaPrijemcov.RINF.equals(skupinaPrijemcov)) {
                sysConfigItem = "cud.hlp.rinf.mail";
            } else {
                sysConfigItem = "cud.hlp.cud.mail";
            }

            String valueFromSysConfig = FrameworkUtils.getConfigProperty(GROUP_CUD, sysConfigItem);
            if (!StringUtils.isValid(valueFromSysConfig)) {
                String popisChyby = "V tabulke SYS_CONFIG sa nenachadza konfiguracny parameter: " + sysConfigItem + "!";
                log.error(popisChyby);
                throw new AppException(popisChyby);
            }

            for (String value : valueFromSysConfig.split("[,;]")) {
                zoznamPrijemcov.add(value);
            }

            String predmetFinal = predmet;
            String htmlSprava = CudVysielanieUtils.getEmailText(errorMsg, "Príprava a odoslanie súborov");

            log.error(predmet + ": " + errorMsg);
            NotifUtils.sendNotif("", zoznamPrijemcov.toArray(new String[zoznamPrijemcov.size()]), predmetFinal, htmlSprava);
        } catch(Throwable t) {
            log.warn("Odoslanie emailu zlyhalo", t);
        }
    }
}
