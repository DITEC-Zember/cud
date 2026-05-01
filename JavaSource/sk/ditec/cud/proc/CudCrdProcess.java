package sk.ditec.cud.proc;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.security.AuthTrustManager;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.crd.dto.DTOCrdSpracovanie;
import sk.ditec.notif.NotifUtils;
import sk.ditec.process.BaseProcess;

public class CudCrdProcess extends BaseProcess {

	private static Logger log = LoggerFactory.getLogger(CudCrdProcess.class);

	private _CudCrdDelegate dlg = new _CudCrdDelegate();

	

	@Override
	protected void process() throws Throwable {
		// process 64 Aktualizácia dát z CRD proc 64
		log.info("Start - Som proces IMPORT CudCrdProcess a bezim");
		String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.crd.mail");
		String email_text = "";
		try {
			boolean lenTest = false;
			AuthInfo auth = AuthInfo.system();
			String navratovyKod = "";

			trustAllManager();
			// 2. /*datumPoslednehoSpracovania = CUD_SPRACOVANIE_CRD.POSLEDNE_USPESNE_SPRACOVANIE*/
			// Systém naèíta dátum posledného spracovania z CUD_SPRACOVANIE_CRD
			/* datumPoslednehoSpracovania = CUD_SPRACOVANIE_CRD.POSLEDNE_USPESNE_SPRACOVANIE */

			Date datumPoslSprac = dlg.getCrdSpracovanieClass().getDatumPoslSpracovania(auth);
			String spracujCiselnik = dlg.getCudParametreClass().getValue(auth, "crd.spracuj.tciselniknazov");
			/* datumVolania - dátum a èas */
			// Systém nastaví datumVolania = aktuálny dátum a cas
			Date datumVolania = new Date();
			// Ak datumPoslednehoSpracovania = NULL
			// Systém nastaví datumPoslednehoSpracovania = 1.1.1900
			if (datumPoslSprac == null) {
				datumPoslSprac = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2023");
			}

			String stringDatumSprac = dlg.getCudParametreClass().getValue(auth, "crd.spracuj.odDatumu");
			if (stringDatumSprac != null) {
				if (stringDatumSprac.length() < 10) {
					log.info("nespravny format datumu v parametri crd.spracuj.odDatumu, spravny format dd.MM.yyyy, zadane:"
							+ datumPoslSprac);
					log.info("End - Som proces CudCrdProcessTest a koncim");
					return;
				}
				String StringdateFormat = "dd.MM.yyyy";
				DateFormat dateFormat = new SimpleDateFormat(StringdateFormat, new Locale("sk"));
				try {
					datumPoslSprac = dateFormat.parse(stringDatumSprac);
				} catch (Throwable e) {
					log.info("nespravny format datumu v parametri crd.spracuj.odDatumu, spravny format dd.MM.yyyy, zadane:"
							+ datumPoslSprac);
					log.info("End - Som proces CudCrdProcessTest a koncim");
					return;
				}
			}
			if (lenTest) {
				datumPoslSprac = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2023");
			}
			//datumPoslSprac = new SimpleDateFormat("dd/MM/yyyy").parse("01/11/2023");
			ActionResult spracovanie = new ActionResult();
			// spracovanie.setResult("-");
			log.info("datumPoslSprac: " + datumPoslSprac);
			// 3. /*SpracovanieCRDCud = CUD_SPRACOVANIE_CRD*/
			// Systém vytvorí nový záznam v CUD_SPRACOVANIE_CRD - zapíše sa informácia o aktuálnom spracovávaní
			DTOCrdSpracovanie dtoSprac = new DTOCrdSpracovanie();
			dtoSprac.setDatumVolania(datumVolania);
			dtoSprac.setPosledneUspesneSpracovanie(datumPoslSprac);
			dlg.getCrdSpracovanieClass().update(auth, dtoSprac);

			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudCrdProcess konci.");
				return;
			}

			if (lenTest) {
				try {
					spracovanie = dlg.getSpracujCrdPrimaryLocationClass().spracujPrimaryLocationTest(auth,
							datumPoslSprac, dtoSprac.getCrdSpracovanieId(), this);
					// spracovanie = dlg.getSpracujCrdSubsidiaryLocationClass().spracujSubsidiaryLocationTest(auth,
					// datumPoslSprac, dtoSprac.getCrdSpracovanieId(), this);

					// spracovanie = dlg.getSpracujCrdCompanyClass().spracujCompany(auth, datumPoslSprac,
					// dtoSprac.getCrdSpracovanieId(), this);

				} catch (Exception e) {
					log.error(" chyba volania WS", e);
					navratovyKod += ",X";
					String chyba = " CHYBA pri Import CudCrdProcess T_PRIMARY_LOCATION  <br>" + e.getMessage();
					email_text += chyba + "<br>";
					log.info(chyba);
				}

				log.info("End - Som proces CudCrdProcessTest a koncim");
				return;
			}

			if (spracujCiselnik == null || "ALL".equals(spracujCiselnik) || spracujCiselnik.contains("T_COUNTRY")) {
			try {
				// ***Country - T_COUNTRY***
				spracovanie = dlg.getSpracujCrdCountryClass().spracujCountry(auth, datumPoslSprac,
						dtoSprac.getCrdSpracovanieId(), this);
				navratovyKod += (spracovanie.getResult() == null ? "-" : spracovanie.getResult());
			} catch (Exception e) {
				navratovyKod += ",X";
				String chyba = " CHYBA pri Import CudCrdProcess T_COUNTR  <br>" + e.getMessage();
				email_text += chyba + "<br>";
				log.info(chyba);
			}
			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudCrdProcess konci.");
				return;
			}
			} else {
				navratovyKod += ",-";
			}

			if (spracujCiselnik == null || "ALL".equals(spracujCiselnik) || spracujCiselnik.contains("T_COMPANY")) {
			try {
				// ***Company - T_COMPANY***
				spracovanie = new ActionResult();
				spracovanie = dlg.getSpracujCrdCompanyClass().spracujCompany(auth, datumPoslSprac,
						dtoSprac.getCrdSpracovanieId(), this);
				navratovyKod += "," + (spracovanie.getResult() == null ? "-" : spracovanie.getResult());

			} catch (Exception e) {
				navratovyKod += ",X";
				String chyba = " CHYBA pri Import CudCrdProcess T_COMPANY  <br>" + e.getMessage();
				email_text += chyba + "<br>";
				log.info(chyba);
			}
			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudCrdProcess konci.");
				return;
			}
			} else {
				navratovyKod += ",-";
			}

			if (spracujCiselnik == null || "ALL".equals(spracujCiselnik)
					|| spracujCiselnik.contains("T_SUBSIDIARY_TYPE")) {
			try {
				// *** T_SUBSIDIARY_TYPE***
				spracovanie = new ActionResult();
				spracovanie = dlg.getSpracujCrdSubsidiaryTypeClass().spracujSubsidiaryType(auth, datumPoslSprac,
						dtoSprac.getCrdSpracovanieId(), this);
				navratovyKod += "," + (spracovanie.getResult() == null ? "-" : spracovanie.getResult());

			} catch (Exception e) {
				navratovyKod += ",X";
				String chyba = " CHYBA pri Import CudCrdProcess T_SUBSIDIARY_TYPE  <br>" + e.getMessage();
				email_text += chyba + "<br>";
				log.info(chyba);

			}
			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudCrdProcess konci.");
				return;
			}
			} else {
				navratovyKod += ",-";
			}

			if (spracujCiselnik == null || "ALL".equals(spracujCiselnik)
					|| spracujCiselnik.contains("T_PRIMARY_LOCATION")) {

			// *** T_PRIMARY_LOCATION***
			try {
				spracovanie = new ActionResult();
				spracovanie = dlg.getSpracujCrdPrimaryLocationClass().spracujPrimaryLocation(auth, datumPoslSprac,
						dtoSprac.getCrdSpracovanieId(), this);
				navratovyKod += "," + (spracovanie.getResult() == null ? "-" : spracovanie.getResult());

			} catch (Exception e) {
				navratovyKod += ",X";
				String chyba = " CHYBA pri Import CudCrdProcess T_PRIMARY_LOCATION  <br>" + e.getMessage();
				email_text += chyba + "<br>";
				log.info(chyba);
			}
			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudCrdProcess konci.");
				return;
			}
			} else {
				navratovyKod += ",-";
			}

			if (spracujCiselnik == null || "ALL".equals(spracujCiselnik)
					|| spracujCiselnik.contains("T_SUBSIDIARY_LOCATION")) {
			// ***T_SUBSIDIARY_LOCATION**
			try {
				spracovanie = new ActionResult();
				spracovanie = dlg.getSpracujCrdSubsidiaryLocationClass().spracujSubsidiaryLocation(auth,
						datumPoslSprac, dtoSprac.getCrdSpracovanieId(), this);
				navratovyKod += "," + (spracovanie.getResult() == null ? "-" : spracovanie.getResult());
			} catch (Exception e) {
				navratovyKod += ",X";
				String chyba = " CHYBA pri Import CudCrdProcess T_SUBSIDIARY_LOCATION  <br>" + e.getMessage();
				email_text += chyba + "<br>";
				log.info(chyba);
			}

			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudCrdProcess konci.");
				return;
			}

			} else {
				navratovyKod += ",-";
			}
			// /////////////////////////////////////////////////////////////////////////////////////////////

			dlg.getCrdSpracovanieClass().update(auth, dtoSprac);
			// Date startTime = new Date();

			dtoSprac.setPosledneUspesneSpracovanie(new Date());
			dtoSprac.setKodSpracovania(navratovyKod);
			dlg.getCrdSpracovanieClass().update(auth, dtoSprac);

		} catch (Exception e) {
			// sendNotifikaciaError(getStackTraceToString(e));
			DBUtils.handleException(e, "process.error");
			email_text += " CHYBA pri Import CudCrdProcess <br>" + e.getMessage() + "<br>";
		}

		if (!"".equals(email_text)) {
			NotifUtils.sendNotif("", mailingList, "Chyba pri CUD CRD IMPORT", email_text);
		}

		log.info("End - Som proces CudCrdProcess a koncim");
	}
	
	@Override
	protected String getLogName() {
		return "crd.aktualizacia";
	}

	protected static void trustAllManager() {
		TrustManager[] trustAllCerts = new TrustManager[] { new AuthTrustManager() };

		HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String urlHostName, SSLSession session) {
				System.out.println("Warning: URL Host: " + urlHostName + " vs. " + session.getPeerHost());
				return true;
			}
		};

		HttpsURLConnection.setDefaultHostnameVerifier(hv);

		// Install the trust manager
		try {
			SSLContext sslc = SSLContext.getInstance("TLS");
			sslc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
		} catch (Exception e) {
			log.error("", e);
		}
	};
}
