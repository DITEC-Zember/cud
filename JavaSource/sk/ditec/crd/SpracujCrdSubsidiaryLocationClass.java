package sk.ditec.crd;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.dto.DTOCrdNenajdeneZaznamy;
import sk.ditec.crd.dto.DTOCrdSpracTabuliek;
import sk.ditec.crd.dto.DTOTCompany;
import sk.ditec.crd.dto.DTOTCountry;
import sk.ditec.crd.dto.DTOTPrimaryLocation;
import sk.ditec.crd.dto.DTOTSubsidiaryLocation;
import sk.ditec.crd.dto.DTOTSubsidiaryType;
import sk.ditec.crd.ws.Company;
import sk.ditec.crd.ws.CompanyReplicationResponse;
import sk.ditec.crd.ws.Country;
import sk.ditec.crd.ws.CountryReplicationResponse;
import sk.ditec.crd.ws.ReplicationVolume;
import sk.ditec.crd.ws.SubsidiaryLocation;
import sk.ditec.crd.ws.SubsidiaryLocationReplicationRequest;
import sk.ditec.crd.ws.SubsidiaryLocationReplicationResponse;
import sk.ditec.crd.ws.SubsidiaryType;
import sk.ditec.crd.ws.SubsidiaryTypeReplicationResponse;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOValidate;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWorkflow;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.proc.CudCrdProcess;
import sk.ditec.cud.proc.CudPauClass;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.notif.NotifUtils;
import sk.ditec.process.BaseProcess;
import sk.ditec.zsr.common.server._NovyPISBaseClass;
import sk.ditec.zsr.common.server.utils.DateUtils;

public class SpracujCrdSubsidiaryLocationClass extends _NovyPISBaseClass {
	private _CudCrdDelegate dlg = new _CudCrdDelegate();
	private _CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private CudPauClass cudPau = new CudPauClass();
	private Logger log = LoggerFactory.getLogger(CudCrdProcess.class);

	// DTOCiselnik[] ciselnikList = null;
	// nacitam ciselnik

	public ActionResult spracujSubsidiaryLocation(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			BaseProcess caller) throws AppException, MalformedURLException {

		// 2. /* T_SUBSIDIARY_LOCATION */
		// Systém vytvorí vstupnú èas správy a ReplicationVolume:
		// DateFilterForDeletedRecords = vst. datumPoslednehoSpracovania
		// ReplicateFromDate = vst. datumPoslednehoSpracovania
		// ReplicateAll -nesme ist do volania
		// (žiada sa o všetky pridané, zmenené a zmazané dáta od poslednej aktualizácie)
		SubsidiaryLocationReplicationRequest request = new SubsidiaryLocationReplicationRequest();
		ReplicationVolume volume = new ReplicationVolume();
		volume.setReplicateFromDate(datumPoslSprac);
		request.setReplicationVolume(volume);
		// aktualizujCiselnik();
		String spracujSK = dlg.getCudParametreClass().getValue(auth, "crd.spracujSK");
		// 6. Systém prijme výstupnú èas správy -

		SubsidiaryLocationReplicationResponse response = null;
		String stringRequest = "";
		try {

			if (!caller.statusOKnotify()) { // koncim
				return new ActionResult();
			}
			String spracujStat = dlg.getCudParametreClass().getValue(auth, "crd.spracuj.country");
			//ked uz vysse tyzdna neprebehlo spracovanie idem po country
			if ((new Date()).getTime() - datumPoslSprac.getTime() > 604800000 || spracujStat != null) {
				return spracujSubsidiaryLocationPoCountry(auth, datumPoslSprac, spracovanieTabuliekId, request,
						spracujSK, caller, spracujStat);
			} else {
				response = dlg.getCrdWS().subsidiaryLocationReplication(request);
				stringRequest = CudVysielanieUtils.marshal(request);
				return spracujResSubsidiaryLocationBatch(auth, datumPoslSprac, response, stringRequest,
						spracovanieTabuliekId, "", spracujSK, caller);

			}
			// ;sidiaryLocation(auth, datumPoslSprac, response, stringRequest, spracovanieTabuliekId,
			// statusOKnotify);
		} catch (Throwable e) {
			// ries po krajinach
			log.info(" CHYBA pri Import CudCrdProcess, volam T_SUBSIDIARY_LOCATION po Country <br>" + e.getMessage());
			return spracujSubsidiaryLocationPoCountry(auth, datumPoslSprac, spracovanieTabuliekId, request, spracujSK,
					caller, null);
		}//

	}

	public ActionResult spracujSubsidiaryLocationPoCountry(AuthInfo auth, Date datumPoslSprac,
			Integer spracovanieTabuliekId, SubsidiaryLocationReplicationRequest request, String spracujSK,
			BaseProcess caller, String spracujStat) throws AppException {

			// java.net.SocketTimeoutException: Connection timed out
			// if (e.getMessage().indexOf("timed out")>=0) {
			// request.setCountryCodeISO(CountryCodeISO[] );
			// 3.1.2. urc krajinaList
			// Systém vyh¾adá ID country z èíselníka T_COUNTRY platný k aktuálnemu dátumu
			// ***Country - T_COUNTRY***
		// String tabulka = "T_COUNTRY";
			// DTOCiselnik[] ciselnikList = null;
			// nacitam ciselnik
			ArrayList<DTOTCountry> countryList = dlg.getTCudCiselnikyClass().getCountryList(auth, new Date());

			// countryList = new ArrayList<DTOTCountry>();
			// DTOTCountry country = new DTOTCountry();
			// country.setCountryCodeIso("FR");
			// countryList.add(country);

			log.info(" Prebieha Import CudCrdProcess T_SUBSIDIARY_LOCATION po Country");
			int pocetChyb = 0;
		SubsidiaryLocationReplicationResponse response;

			for (DTOTCountry dtoCountry : countryList) {
				if (!caller.statusOKnotify()) { // koncim
					return new ActionResult();
				}
			// pre ucely ladenia konkretnej country
			if (spracujStat != null && !"ALL".equals(spracujStat)
					&& !spracujStat.contains(dtoCountry.getCountryCodeIso())) {
				// if (!"DE".equals(dtoCountry.getCountryCodeIso())) {
				continue;
			}

				sk.ditec.crd.ws.CountryCodeISO[] isoList = new sk.ditec.crd.ws.CountryCodeISO[1];
				sk.ditec.crd.ws.CountryCodeISO iso = new sk.ditec.crd.ws.CountryCodeISO();
				iso.setValue(dtoCountry.getCountryCodeIso());
				isoList[0] = iso;
				request.setCountryCodeISO(isoList);
				if (!caller.statusOKnotify()) { // koncim
					return new ActionResult();
				}
				boolean isSpracovane = false;
				int pocet = 0;

				while (!isSpracovane) {
					if (!caller.statusOKnotify()) { // koncim
						return new ActionResult();
					}
					if (pocet > 5) {
						break;
					}
					pocet++;
					try {
					if (pocet > 1) {
						Thread.sleep(60000); // cakaj minutu, ked je pretazena WS, je nedostupna chyba 503
					}

						response = dlg.getCrdWS().subsidiaryLocationReplication(request);
						String stringRequest = CudVysielanieUtils.marshal(request);
						spracujResSubsidiaryLocationBatch(auth, datumPoslSprac, response, stringRequest,
								spracovanieTabuliekId, dtoCountry.getCountryCodeIso(), spracujSK, caller);
						isSpracovane = true;
					} catch (Throwable t) {
						pocetChyb++;
						log.info("chyba po sleeep  pocet=" + pocet + "    " + t);
						String chyba = " CHYBA pri Import CudCrdProcess T_SUBSIDIARY_LOCATION  nefunkcna WS nacitanie dat"
								+ t.getMessage();
						log.info(chyba);
						if (pocetChyb > 5) {
							String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.crd.mail");
							log.info(chyba);
							NotifUtils.sendNotif("", mailingList, "Chyba pri CUD CRD IMPORT", chyba);
						}
					}
				} // end while
			} // end for


		return new ActionResult();
	}

	// batch
	public ActionResult spracujResSubsidiaryLocationBatch(AuthInfo auth, Date datumPoslSprac,
			SubsidiaryLocationReplicationResponse response, String stringVstupneXml, Integer spracovanieTabuliekId,
			String countryIso, String spracujSK, BaseProcess caller) {
		ActionResult actRes = new ActionResult();
		String chyba = "";
		String spracovane = "";
		String bezZmien = "";

		Integer pocet = null;
		if (response != null) {
			pocet = response.getSubsidiaryLocationLength();
		}
		String popisSpracovania = "SUBSIDIARY_LOCATION počet záznamov na spracovanie " + pocet + " pre Country "
				+ countryIso;
		Integer navratovyKod = 0; // nie su zaznamy na spracovanie

		try {
			// 7.Systém zapíše do údajov o výsledku spracovania výstupné XML
			// Systém aktualizuje údaje v CUD_SPRACOVANIE_TABULIEK:
			// .VYSTUPNE_XML= vst. XML
			// kde .SPRACOVANIE_TABULIEK_ID = vst. SpracovanieTabuliekID
			// DTOCrdSpracTabuliek dtoSprac = new DTOCrdSpracTabuliek();
			// dtoSprac.setVystupneXml(JaxbUtils.marshal(countryReplicationResponse));
			// dtoSprac.setIdCrdSpracovanie(spracovanieTabuliekId);

			String tabulka = "T_SUBSIDIARY_LOCATION";
			// DTOCiselnik[] ciselnikList = null;
			// nacitam ciselnik
			DTOCiselnik dtoCis = dlgcud.getCiselnikRead().readLight(auth, tabulka);
			if (!StringUtils.isValid(dtoCis)) {
				chyba += tabulka + " sa nenachaza v zozname ciselnikov!";
				return actRes;
			}

			DTOCrdSpracTabuliek dtospracParent = new DTOCrdSpracTabuliek();
			dtospracParent.setIdCrdSpracovanie(spracovanieTabuliekId);
			dtospracParent.setIdCiselnik(dtoCis.getCiselnikID());
			dtospracParent.setVstupneXml(stringVstupneXml);
			dtospracParent.setVystupneXml(CudVysielanieUtils.marshal(response));
			dtospracParent.setPopisSpracovania(popisSpracovania);
			// dtosprac.setDateFilterForDeleted();
			dtospracParent.setReplicateFromDate(datumPoslSprac);
			dtospracParent.setReplicateAll("Y");
			dtospracParent.setNavratovyKod(navratovyKod);

			dtospracParent = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().updateANuluj(auth, dtospracParent)
					.getResult();

			// 8.Systém nastaví pomocnú premennú bolaZmena = False
			boolean bolaZmena = false;
			// 9.Ak je záznam vo výstupnej èasti správy Country alebo je vyplnené
			DTOImportZmena[] importZmenaList = new DTOImportZmena[1];
			DTOImportZmena dtoZmena = new DTOImportZmena();
			DTOImport dtoImport = new DTOImport();
			dtoImport.setIDCiselnik(dtoCis.getCiselnikID());
			dtoImport.setCiselnikTabulka(tabulka);
			dtoImport.setStav("CRD");

			if (response != null && response.getSubsidiaryLocation() != null
					&& response.getSubsidiaryLocation().length > 0) {
				// 9.1. Pre každý záznam zo sekcie z výstupnej správy

				navratovyKod = 1; // existuju zaznamy na spracovanie

				for (SubsidiaryLocation ws : response.getSubsidiaryLocation()) {
					DTOCrdSpracTabuliek dtosprac = new DTOCrdSpracTabuliek();
					dtosprac.setIdCrdSpracovanie(spracovanieTabuliekId);
					dtosprac.setIdCiselnik(dtoCis.getCiselnikID());
					dtosprac.setZmenoveXmlVstup(CudVysielanieUtils.marshal(ws));
					dtosprac.setReplicateAll("N");
					dtosprac.setReplicateFromDate(datumPoslSprac);
					dtosprac.setNavratovyKod(navratovyKod);
					String spracovanyZaznam = " LocSubCode=" + ws.getSubsidiaryLocationCode();
					dtosprac.setPopisSpracovania("spracovavam zaznam " + spracovanyZaznam);
					dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().update(auth, dtosprac).getResult();
					try {
						if (!"T".equals(spracujSK)) {
							if (_CudConsts.COUNTRY_CODE_ISO_SK.equals(ws.getPrimaryLocation().getCountry()
									.getCountryCodeISO().getValue())
									&& _CudConsts.COMPANY_UIC_CODE_ZSR.equals(ws.getPrimaryLocation()
											.getResponsibleIM().getCompanyUICCode())) {
								continue;
							}
						}
						bolaZmena = false;
						popisSpracovania = "";
						Map<String, String> rowMap = new HashMap<String, String>();



						// 9.1.1.
						DTOTCountry tCountry = dlg.getTCudCiselnikyClass().getCountryByIso(auth,
								ws.getCountryCodeISO().getValue());

						if (tCountry == null || tCountry.getCountryID() == null) {
							CountryReplicationResponse countryReplicationResponse = new CountryReplicationResponse();
							countryReplicationResponse
									.setCountry(new Country[] { ws.getPrimaryLocation().getCountry() });

							dlg.getSpracujCrdCountryClass().updateCountry(auth, datumPoslSprac, spracovanieTabuliekId,
									stringVstupneXml, countryReplicationResponse, caller);

							tCountry = dlg.getTCudCiselnikyClass().getCountryByIso(auth,
									ws.getCountryCodeISO().getValue());

							if (tCountry == null || tCountry.getCountryID() == null) {
								// Systém nastaví :
								// navratovyKod = 3
								// popisSpracovania = "Chyba - nebol nájdený záznam väzobného èíselníka."
								navratovyKod = 3;
								popisSpracovania += "Chyba - nebol nájdený záznam väzobného číselníka Country "
										+ ws.getCountryCodeISO().getValue();
								dtosprac.setPopisSpracovania(spracovanyZaznam + popisSpracovania);
								dtosprac.setNavratovyKod(navratovyKod);
								dtosprac.setReplicateAll("N");
								dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
										.updateANuluj(auth, dtosprac).getResult();
								chyba += " Country " + tCountry.getCountryID() + " k sublocationId = "
										+ ws.getSubsidiaryLocationCode();
								continue;

							}
						}
						DTOTCompany tCompany = dlg.getTCudCiselnikyClass().getCompany(auth,
								ws.getAllocationCompany().getCompanyUICCode(),
								ws.getAllocationCompany().getStartValidity());
						// // Systém vyh¾adá èi záznam zo vstupu je zapísaný v T_COMPANY Vrát záznam pod¾a kódu
						if (tCompany == null || tCompany.getCompanyID() == null) { // nie je povinna polozka
							CompanyReplicationResponse companyReplicationResponse = new CompanyReplicationResponse();
							companyReplicationResponse.setCompany(new Company[] { ws.getAllocationCompany() });

							dlg.getSpracujCrdCompanyClass().updateCompany(auth, datumPoslSprac, spracovanieTabuliekId,
									stringVstupneXml, companyReplicationResponse, caller);

							tCompany = dlg.getTCudCiselnikyClass().getCompany(auth,
									ws.getAllocationCompany().getCompanyUICCode());
							if (tCountry == null || tCountry.getCountryID() == null) {

								// // Systém nastaví :
								// // navratovyKod = 3
								// popisSpracovania = "Chyba - nebol nájdený záznam väzobného èíselníka."
								navratovyKod = 3;
								popisSpracovania += "Chyba - nebol nájdený záznam väzobného číselníka company k SubLoc"
										+ ws.getAllocationCompany().getCompanyUICCode();
								chyba += popisSpracovania;
								dtosprac.setPopisSpracovania(spracovanyZaznam + popisSpracovania);
								dtosprac.setNavratovyKod(navratovyKod);
								dtosprac.setReplicateAll("N");
								dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
										.updateANuluj(auth, dtosprac).getResult();
								DTOCiselnik dtoCisCountry = dlgcud.getCiselnikRead().readLight(auth,
										_CudConsts.TABULKA_T_COUNTRY);
								DTOCrdNenajdeneZaznamy dton = new DTOCrdNenajdeneZaznamy();
								dton.setIdCrdSpracTabuliek(dtoCisCountry.getCiselnikID());
								dton.setPopis(spracovanyZaznam);
								dton.setChyba("T");
								dton.setVarovanie("F");
								dton.setChybovaSprava(popisSpracovania);
								dlg.getCrdNenajdeneZaznamyClass().update(auth, dton);
								continue;
							}
						}
						DTOTSubsidiaryType tSubType = dlg.getTCudCiselnikyClass().getSubsidiaryType(auth,
								ws.getSubsidiaryType().getSubsidiaryTypeCode());
						// // Systém vyh¾adá èi záznam zo vstupu je zapísaný v Vrát záznam pod¾a kódu
						if (tSubType == null || tSubType.getSubsidiaryTypeID() == null) { // nie je povinna polozka
							SubsidiaryTypeReplicationResponse subsTypeRes = new SubsidiaryTypeReplicationResponse();
							subsTypeRes.setSubsidiaryType(new SubsidiaryType[] { ws.getSubsidiaryType() });

							dlg.getSpracujCrdSubsidiaryTypeClass().updateSubsidiaryType(auth, datumPoslSprac,
									spracovanieTabuliekId, stringVstupneXml, subsTypeRes, caller);
							if (tSubType == null || tSubType.getSubsidiaryTypeID() == null) {
								// // Systém nastaví :
								// // navratovyKod = 3
								// popisSpracovania = "Chyba - nebol nájdený záznam väzobného èíselníka."
								navratovyKod = 3;
								popisSpracovania += "Chyba - nebol nájdený záznam väzobného číselníka SubsidiaryType k Subloc "
										+ tSubType.getSubsidiaryTypeID();
								chyba += popisSpracovania;
								dtosprac.setPopisSpracovania(spracovanyZaznam + popisSpracovania);
								dtosprac.setNavratovyKod(navratovyKod);
								dtosprac.setReplicateAll("N");
								dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
										.updateANuluj(auth, dtosprac).getResult();

								DTOCrdNenajdeneZaznamy dton = new DTOCrdNenajdeneZaznamy();
								dton.setIdCrdSpracTabuliek(dtosprac.getCrdSpracTabuliekId());
								dton.setPopis(spracovanyZaznam);
								dton.setChyba("T");
								dton.setVarovanie("F");
								dton.setChybovaSprava(popisSpracovania);
								dlg.getCrdNenajdeneZaznamyClass().update(auth, dton);
								continue;

							}
						}

						DTOTPrimaryLocation tPrimLoc = dlg.getTCudCiselnikyClass()
								.getPrimaryLocationByLocCodeAndCountry(auth, ws.getPrimaryLocation().getLocationCode(),
										tCountry.getCountryID(), ws.getPrimaryLocation().getStartValidity());
						// // Systém vyh¾adá èi záznam zo vstupu je zapísaný v Vrát záznam pod¾a kódu
						if (tPrimLoc == null || tPrimLoc.getPrimaryLocationID() == null) { // nie je povinna polozka
							// response = dlg.getCrdWS().primaryLocationReplication(request);
							// String stringRequest = CudVysielanieUtils.marshal(request);
							// dlg.getSpracujCrdPrimaryLocationClass().spracujPrimaryLocationBatch(auth, datumPoslSprac,
							// ws.getPrimaryLocation(), stringVstupneXml, spracovanieTabuliekId,
							// tCountry.getCountryCodeIso(), spracujSK, caller);
							tPrimLoc = dlg.getTCudCiselnikyClass().getPrimaryLocationByLocCodeAndCountry(auth,
									ws.getPrimaryLocation().getLocationCode(), tCountry.getCountryID(), null);
							if (tPrimLoc == null || tPrimLoc.getPrimaryLocationID() == null) { // nie je povinna polozka
								// // Systém nastaví :
								// // navratovyKod = 3

								navratovyKod = 3;
								popisSpracovania += "Chyba - nebol nájdený záznam väzobného číselníka PrimaryLocation k SubLoc "
										+ ws.getPrimaryLocation().getLocationCode();
								chyba += popisSpracovania;
								dtosprac.setPopisSpracovania(spracovanyZaznam + popisSpracovania);
								dtosprac.setNavratovyKod(navratovyKod);
								dtosprac.setReplicateAll("N");
								dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
										.updateANuluj(auth, dtosprac).getResult();

								DTOCiselnik ciselnik = dlgcud.getCiselnikRead().readLight(auth,
										_CudConsts.TABULKA_T_PRIMARY_LOCATION);
								DTOCrdNenajdeneZaznamy dton = new DTOCrdNenajdeneZaznamy();
								dton.setIdCrdSpracTabuliek(ciselnik.getCiselnikID());
								dton.setPopis(spracovanyZaznam);
								dton.setChyba("T");
								dton.setVarovanie("F");
								dton.setChybovaSprava(popisSpracovania);
								dlg.getCrdNenajdeneZaznamyClass().update(auth, dton);
								continue;
							}
						}

						if (ws.getStartValidity() == null) {
							ws.setStartValidity(DateUtils.withoutHHMMSS(new Date()));
						}
						// DTOTSubsidiaryLocation tSubLocation =
						// dlg.getTCudCiselnikyClass().getSubsidiaryLocationBySubCode(
						// auth, ws.getSubsidiaryLocationCode(), tPrimLoc.getPrimaryLocationID(),
						// tSubType.getSubsidiaryTypeID(), tCompany.getCompanyID(), tCountry.getCountryID(),
						// ws.getStartValidity());
						ArrayList<DTOTSubsidiaryLocation> tSubLocationList = dlg.getTCudCiselnikyClass()
								.getSubsidiaryLocationList(auth, ws.getSubsidiaryLocationCode(),
										ws.getPrimaryLocation().getLocationCode(),
										ws.getSubsidiaryType().getSubsidiaryTypeCode(),
										ws.getCountryCodeISO().getValue(), tCompany.getCompanyUicCode());

						// Ak END_VALIDITY došlého záznamy je null
						if (ws.getEndValidity() == null) {
							// Pre každý deaktivácia z deaktiváciaList
							ArrayList<DTOTSubsidiaryLocation> deaktList = getLocationForDeakt(tSubLocationList,
									ws.getStartValidity());
							for (DTOTSubsidiaryLocation dto : deaktList) {
								// Systém vytvorí výstupnú èas správy Zaznamy a naviaže ju na ZmenaHodnotCiselnika,
								// prièom
								// ID =deaktivacia.PRIMARY_LOCATION_ID
								// operacia = "U"
								// platnostOd = aktualny dátum a èas
								// datumSchvalenia = Primary_Location.Modified_Date
								// Systém vytvorí výstupnú èas správy Stlpce a naviaže ju na Zaznamy, prièom
								// nazovStlpca = ACTIVE_FLAG
								// novaHodnota = FALSE

								Map<String, String> rowMapOld = new HashMap<String, String>();
								rowMapOld.put("SUBSIDIARY_LOCATION_ID", dto.getSubsidiaryLocationID().toString());
								rowMapOld.put("XLS_OPERACIA", "U");
								rowMapOld.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(new Date()));
								rowMapOld.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));
								rowMapOld.put("ACTIVE_FLAG", "F");
								dtosprac.setZmenoveXmlVystup(rowMapOld.toString());
								dtosprac.setPopisSpracovania(spracovanyZaznam + " ACTIVE_FLAG=F");

								try {
									dlg.getCrdSpracovanieClass().zapisLokCezZmenoveProcesy(auth, rowMapOld, dtoImport,
											navratovyKod, popisSpracovania);
									dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
											.updateANulujZmenoveXML(auth, dtosprac).getResult();
								} catch (Throwable e) {
									log.error(popisSpracovania + e);
									dtosprac.setKod("1");
									dtosprac.setPopisSpracovania(spracovanyZaznam + " CHYBA:" + e.getMessage());
									dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
											.updateANulujZmenoveXML(auth, dtosprac).getResult();
									continue;
								}
							}
							// if (deaktList.size() > 0) {
							// tSubLocationList = dlg.getTCudCiselnikyClass().getSubsidiaryLocationList(auth,
							// ws.getSubsidiaryLocationCode(), ws.getPrimaryLocation().getLocationCode(),
							// ws.getSubsidiaryType().getSubsidiaryTypeCode(),
							// ws.getCountryCodeISO().getValue(), tCompany.getCompanyUicCode());
							// }
						}



						ArrayList<DTOTSubsidiaryLocation> oldSubLocationList = getLocationOldList(tSubLocationList,
								ws.getStartValidity());

						// if (oldSubLocation != null) {
						for (DTOTSubsidiaryLocation dto : oldSubLocationList) {
							Map<String, String> rowMapOld = new HashMap<String, String>();

							rowMapOld.put("SUBSIDIARY_LOCATION_ID", dto.getSubsidiaryLocationID().toString());
							rowMapOld.put("XLS_OPERACIA", "U");
							rowMapOld.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(new Date()));
							rowMapOld.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));

							rowMapOld.put("END_VALIDITY",
									CudVysielanieUtils.getStringDatum(DateUtils.plusDay(ws.getStartValidity(), -1)));

							// zrus stare zaznamy
							// zapis cez procesy zmien
							dtosprac.setPopisSpracovania(spracovanyZaznam + " Deaktivacia ");
							dtosprac.setZmenoveXmlVystup(rowMapOld.toString());
							dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
									.updateANulujZmenoveXML(auth, dtosprac).getResult();

							try {
								zapisLokCezZmenoveProcesy(auth, rowMapOld, dtoImport, navratovyKod, popisSpracovania);
							} catch (Throwable e) {
								log.error(popisSpracovania + e);
								dtosprac.setKod("1");
								dtosprac.setPopisSpracovania(spracovanyZaznam + " CHYBA:" + e.getMessage());
								dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
										.updateANulujZmenoveXML(auth, dtosprac).getResult();
								// continue;
							}
						}

						// 9.1.2 Ak záznam nebol nájdený - nový záznam
						ArrayList<DTOTSubsidiaryLocation> subLocationList = getLocationBystartValidityList(
								tSubLocationList, ws.getStartValidity());
						DTOTSubsidiaryLocation tSubLocation = null;
						boolean novyZaznam = true;
						ArrayList<DTOTSubsidiaryLocation> deaktList = new ArrayList<DTOTSubsidiaryLocation>();
						if (subLocationList.size() == 1) {
							 tSubLocation = subLocationList.get(0);
							 novyZaznam = false;
						} else if (subLocationList.size() > 1) {
							DTOTSubsidiaryLocation subLocationListByCompany = getLocationByCompany(subLocationList,
									tCompany.getCompanyID());
							novyZaznam = false;
							if (subLocationListByCompany != null) {
								tSubLocation = subLocationListByCompany;
							} else {
								tSubLocation = subLocationList.get(0);
							}
							deaktList = getSubListBezDto(tSubLocationList, tSubLocation);
						}
						
						
						//duplicitne zaznamy nastavim na active-flag = false
								if (subLocationList.size() > 1) {

							for (DTOTSubsidiaryLocation dto : deaktList) {
										// zneplatneni

								if ("T".equals(dto.getActiveFlag())) {
										Map<String, String> rowMapOld = new HashMap<String, String>();
										rowMapOld.put("SUBSIDIARY_LOCATION_ID", dto.getSubsidiaryLocationID().toString());
										rowMapOld.put("XLS_OPERACIA", "U");
										rowMapOld.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(new Date()));
										rowMapOld.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));
										rowMapOld.put("ACTIVE_FLAG", "F");
										dtosprac.setZmenoveXmlVystup(rowMapOld.toString());
										dtosprac.setPopisSpracovania(spracovanyZaznam + " ACTIVE_FLAG=F");

										try {
											dlg.getCrdSpracovanieClass().zapisLokCezZmenoveProcesy(auth, rowMapOld, dtoImport,
													navratovyKod, popisSpracovania);
											dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
													.updateANulujZmenoveXML(auth, dtosprac).getResult();
										} catch (Throwable e) {
											log.error(popisSpracovania + e);
											dtosprac.setKod("1");
											dtosprac.setPopisSpracovania(spracovanyZaznam + " CHYBA:" + e.getMessage());
											dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
													.updateANulujZmenoveXML(auth, dtosprac).getResult();
											continue;
										}
										}
									} // end for
								}
								
								
								
						dtoZmena.setPlatnostOd(new Date());
						if (novyZaznam) {

							// ked nebol najdeny, zisti, ci existuju platne zaznamy a ukonci ich

							// Systém vytvorí výstupnú èas správy Zaznamy a naviaže ju na ZmenaHodnotCiselnika, prièom
							// ID = NULL
							// operacia = "N"
							// platnostOd = Country.DateAdd
							// datumSchvalenia = Country.DateAdd
							dtoZmena.setOperacia("N");

							// ID_SUBSIDIARY_TYPE
							// ID_COMPANY
							// ID_COUNTRY
							// ID_PRIMARY_LOCATION
							// RESPONSIBLE_IM_CODE
							// SUBSIDIARY_LOCATION_CODE
							// SUBSIDIARY_LOCATION_NAME

							// START_VALIDITY
							// END_VALIDITY
							// LONGITUDE
							// LATITUDE
							// FREE_TEXT
							rowMap.put("ACTIVE_FLAG", "T");
							rowMap.put("ID_SUBSIDIARY_TYPE", tSubType.getSubsidiaryTypeID().toString());
							rowMap.put("ID_COMPANY", tCompany.getCompanyID().toString());
							rowMap.put("ID_COUNTRY", tCountry.getCountryID().toString());
							rowMap.put("ID_PRIMARY_LOCATION", tPrimLoc.getPrimaryLocationID().toString());
							rowMap.put("RESPONSIBLE_IM_CODE", ws.getResponsibleIMCode());
							rowMap.put("SUBSIDIARY_LOCATION_CODE", ws.getSubsidiaryLocationCode());
							rowMap.put("SUBSIDIARY_LOCATION_NAME",
									CudVysielanieUtils.refactorApostrof(ws.getSubsidiaryLocationName()));

							rowMap.put(
									"START_VALIDITY",
									(ws.getStartValidity() != null ? DateUtils.formatDate(ws.getStartValidity(),
											"dd.MM.yyyy") : null));

							rowMap.put(
									"END_VALIDITY",
									(ws.getEndValidity() != null ? DateUtils.formatDate(ws.getEndValidity(),
											"dd.MM.yyyy") : null));

							rowMap.put("LATITUDE", (ws.getLatitude() != null ? ws.getLatitude().toString() : ""));
							rowMap.put("LONGITUDE", (ws.getLongitude() != null ? ws.getLongitude().toString() : ""));
							rowMap.put("FREE_TEXT", CudVysielanieUtils.refactorApostrof(ws.getFreeText()));
							rowMap.put("XLS_OPERACIA", "N");
							dtoZmena.setOperacia("N");
							bolaZmena = true;
						} else { // update

							int poradie = 0;

							
							

							// SUBSIDIARY_LOCATION_ID
							rowMap.put("SUBSIDIARY_LOCATION_ID", tSubLocation.getSubsidiaryLocationID().toString());
							// rowMap.put("ID_SUBSIDIARY_TYPE", tSubType.getSubsidiaryTypeID().toString());
							if (!CudVysielanieUtils.isEqual(tSubType.getSubsidiaryTypeID(),
									tSubLocation.getIDSubsidiaryType())) {
								bolaZmena = true;
								rowMap.put("ID_SUBSIDIARY_TYPE", tSubType.getSubsidiaryTypeID().toString());
							}
							// rowMap.put("ID_COMPANY", tCompany.getCompanyID().toString());
							if (!CudVysielanieUtils.isEqual(tCompany.getCompanyID(), tSubLocation.getIDCompany())) {
								bolaZmena = true;
								rowMap.put("ID_COMPANY", tCompany.getCompanyID().toString());
							}
							// rowMap.put("ID_COUNTRY", tCountry.getCountryID().toString());
							if (!CudVysielanieUtils.isEqual(tCountry.getCountryID(), tSubLocation.getIDCountry())) {
								bolaZmena = true;
								rowMap.put("ID_COUNTRY", tCountry.getCountryID().toString());
							}
							// rowMap.put("ID_PRIMARY_LOCATION", tPrimLoc.getPrimaryLocationID().toString());
							if (!CudVysielanieUtils.isEqual(tPrimLoc.getPrimaryLocationID(),
									tSubLocation.getIDPrimaryLocation())) {
								bolaZmena = true;
								rowMap.put("ID_PRIMARY_LOCATION", tPrimLoc.getPrimaryLocationID().toString());
							}
							// rowMap.put("RESPONSIBLE_IM_CODE", ws.getResponsibleIMCode());

							if (!CudVysielanieUtils.isEqual(ws.getResponsibleIMCode(),
									tSubLocation.getResponsibleImCode())) {
								bolaZmena = true;
								rowMap.put("RESPONSIBLE_IM_CODE", ws.getResponsibleIMCode());
							}
							// rowMap.put("SUBSIDIARY_LOCATION_CODE", ws.getSubsidiaryLocationCode());
							if (!CudVysielanieUtils.isEqual(ws.getSubsidiaryLocationCode(),
									tSubLocation.getSubsidiaryLocationCode())) {
								bolaZmena = true;
								rowMap.put("SUBSIDIARY_LOCATION_CODE", ws.getSubsidiaryLocationCode());
							}

							if (!CudVysielanieUtils.isEqual((String) ws.getSubsidiaryLocationName(),
									tSubLocation.getSubsidiaryLocationName())) {
								bolaZmena = true;
								rowMap.put("SUBSIDIARY_LOCATION_NAME",
										CudVysielanieUtils.refactorApostrof(ws.getSubsidiaryLocationName()));
							}
							// rowMap.put("START_VALIDITY", DateUtils.formatDate(ws.getStartValidity(), "dd.MM.yyyy"));
							if (!CudVysielanieUtils.isEqual(ws.getStartValidity(), tSubLocation.getStartValidity())) {
								bolaZmena = true;
								rowMap.put(
										"START_VALIDITY",
										(ws.getStartValidity() != null ? DateUtils.formatDate(ws.getStartValidity(),
												"dd.MM.yyyy") : null));
							}
							// rowMap.put("END_VALIDITY", DateUtils.formatDate(ws.getEndValidity(), "dd.MM.yyyy"));
							if (!CudVysielanieUtils.isEqual(ws.getEndValidity(), tSubLocation.getEndValidity())) {
								bolaZmena = true;
								rowMap.put(
										"END_VALIDITY",
										(ws.getEndValidity() != null ? DateUtils.formatDate(ws.getEndValidity(),
												"dd.MM.yyyy") : null));
							}

							if (!"T".equals(tSubLocation.getActiveFlag())) {
								bolaZmena = true;
								rowMap.put("ACTIVE_FLAG", "T");
							}
							// rowMap.put("LATITUDE", ws.getLatitude().toString());
							if (!CudVysielanieUtils.isEqual(ws.getLatitude(), tSubLocation.getLatitude())) {
								bolaZmena = true;
								rowMap.put("LATITUDE", (ws.getLatitude() != null ? ws.getLatitude().toString() : ""));
							}
							// rowMap.put("LONGITUDE", ws.getLongitude().toString());
							if (!CudVysielanieUtils.isEqual(ws.getLongitude(), tSubLocation.getLongitude())) {
								bolaZmena = true;
								rowMap.put("LONGITUDE", (ws.getLongitude() != null ? ws.getLongitude().toString() : ""));
							}

							// rowMap.put("FREE_TEXT", ws.getFreeText());
							if (!CudVysielanieUtils.isEqual(ws.getFreeText(), tSubLocation.getFreeText())) {
								bolaZmena = true;
								rowMap.put("FREE_TEXT",
										CudVysielanieUtils.refactorApostrof(ws.getFreeText() != null ? ws.getFreeText()
												: ""));
							}

							// if (bolaZmena) {
							dtoZmena.setOperacia("U");
							// }
							rowMap.put("XLS_OPERACIA", "U");
						}
						
						// pre odstranenie duplicit
						

						if (!bolaZmena) {
							// bezZmien += "SubsidiaryLocationID=" + tSubLocation.getSubsidiaryLocationID() + " ; ";
							dtosprac.setPopisSpracovania("Bez zmien " + spracovanyZaznam);
							dtosprac.setNavratovyKod(0);
							dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
									.updateANuluj(auth, dtosprac).getResult();
							continue;
						}
						Date $vlozDatum = new Date();

						// if (bolaZmena) {
						// $vlozDatum = CudVysielanieUtils.getMax(ws.getModifiedDate(), $vlozDatum);
						// } else {
						// $vlozDatum = CudVysielanieUtils.getMax(ws.getAddDate(), $vlozDatum);
						// }
						rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum($vlozDatum));
						rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));

						// ///////////////////////////////////////////////////////////////////////////////////////

						importZmenaList[0] = dtoZmena;

						DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, new Date(), null,
								null);
						boolean errors = false;
						DTOZmena dtoZmenaZap = null;
						String stringRowMap = rowMap.toString();
						dtosprac.setZmenoveXmlVystup(stringRowMap);

						Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();
						List<DTOCiselnikStlpec> csList = dlgcud.getCiselnikStlpecRead().listLight(auth,
								dtoImport.getIDCiselnik());
						dlgcud.getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);

						if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
							errors = true;
							navratovyKod = 4;
							for (DTOImportMsg dto : dtoVal.getImportZmenaDTO().getImportMsgList()) {
								chyba += dto.getMsg();
								popisSpracovania += dto.getMsg();
							}
							chyba += rowMap.toString();
							popisSpracovania += rowMap.toString();
							dtosprac.setPopisSpracovania(spracovanyZaznam + chyba);
							dtosprac.setNavratovyKod(navratovyKod);
							dtosprac.setReplicateAll("N");
							dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
									.updateANuluj(auth, dtosprac).getResult();

							continue;
						}

						Map<Integer, DTOUcet[]> ucetMap = new HashMap<Integer, DTOUcet[]>();

						List<DTOWfDef> wfDefList = dlgcud.getWfDefRead().list(auth, dtoImport.getIDCiselnik());

						if (StringUtils.isValid(dtoImport.getIDCiselnik())) {

							DTOWorkflow dtoWf = dlgcud.getWorkflow().generujWorkflowAll(auth,
									dtoImport.getIDCiselnik(), dtoVal.getImportZmenaDTO(), wfDefList, ucetMap);
							if (StringUtils.isValid(dtoWf)) {
								ActionResult res = cudPau.workflowUpdateCrd(auth, dtoWf, dtoVal.getImportZmenaDTO(),
										datumPoslSprac);
								// sendNotif(auth, dtoCis, dtoVal, dtoWf, wfDefList, metaMapForSend, fkMetaMap);
								dtoZmenaZap = (DTOZmena) res.getResult();
								// 10.
								spracovane += "SubsidiaryLocationCode=" + ws.getSubsidiaryLocationCode() + " ; ";
								if (dtoZmenaZap != null) {
									dlg.getCrdAktualizujCiselnikClass().aktualizujCiselnik(auth, dtoZmenaZap);
									popisSpracovania = "Úspešné spracovanie " + spracovanyZaznam;
								}

							} else { // dtoWf not valid
								popisSpracovania = "NeÚspešné spracovanie DTOWorkflow is null " + spracovanyZaznam;
							}

						}

						dtosprac.setPopisSpracovania(popisSpracovania);
						dtosprac.setNavratovyKod(0);
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().updateANuluj(auth, dtosprac)
								.getResult();
						



						
					} catch (Throwable e) {
						// handleException(e, "SpracujCrdSubsidiaryLocationClass.spracujSubsidiaryLocation.error",
						// auth);
						navratovyKod = 4;
						dtosprac.setPopisSpracovania("Neuspesne spracovanie " + e.getMessage());
						dtosprac.setNavratovyKod(navratovyKod);

							dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
									.updateANuluj(auth, dtosprac).getResult();

						log.error("SpracujCrdSubsidiaryLocationClass.spracujSubsidiaryLocation.error", e);
						continue;
					}

				} // end for

			}

			// 10.1. Systém zapíše do údajov o výsledku spracovania vstupné zmenové XML

			actRes.setResult(navratovyKod);
			if (chyba.length() > 0) {
				actRes.setError(true);
				actRes.setKeyErrorMsg(chyba);
				return actRes;
			}
			return actRes;
		} catch (Throwable e) {
			// handleException(e, "SpracujCrdSubsidiaryLocationClass.spracujSubsidiaryLocation.error", auth);
			
			log.error("SpracujCrdSubsidiaryLocationClass.spracujSubsidiaryLocation.error", e);
			actRes.setError(true);
			actRes.setKeyErrorMsg(e.getMessage());
			return actRes;
		}
	}


	private ArrayList<DTOTSubsidiaryLocation> getLocationBystartValidityList(
			ArrayList<DTOTSubsidiaryLocation> tlocationlist, Date startValidity) {
		// " AND pl.START_VALIDITY =" + CudVysielanieUtils.dateCritFormat(startValidity);
		ArrayList<DTOTSubsidiaryLocation> list = new ArrayList<DTOTSubsidiaryLocation>();
		for (DTOTSubsidiaryLocation dto : tlocationlist) {
			if (CudVysielanieUtils.isEqual(startValidity, dto.getStartValidity())) {
				list.add(dto);
			}
		}
		return list;
	}

	private DTOTSubsidiaryLocation getLocationByCompany(ArrayList<DTOTSubsidiaryLocation> tSubLocationList,
			Integer companyID) {
		Collections.sort(tSubLocationList, compareByHistDesc);
		for (DTOTSubsidiaryLocation dto : tSubLocationList) {
			if (CudVysielanieUtils.isEqual(companyID, dto.getIDCompany())) {
				return dto;
			}
		}
		return null;
	}

	private ArrayList<DTOTSubsidiaryLocation> getSubListBezDto(ArrayList<DTOTSubsidiaryLocation> tSubLocationList,
			DTOTSubsidiaryLocation dto) {
		ArrayList<DTOTSubsidiaryLocation> list = new ArrayList<DTOTSubsidiaryLocation>();
		for (DTOTSubsidiaryLocation subdto : tSubLocationList) {
			if (!CudVysielanieUtils.isEqual(dto.getHistID(), subdto.getHistID())) {
				list.add(dto);
			}
		}
		return list;
	}

	public ActionResult spracujSubsidiaryLocationTest(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			BaseProcess caller) throws AppException, MalformedURLException {

		File file = new File("c:/JAVA/git/CUD/cud/xml/subloc.xml");

		SubsidiaryLocationReplicationResponse response = new SubsidiaryLocationReplicationResponse();
		try {
			JAXBContext context = JAXBContext.newInstance(SubsidiaryLocationReplicationResponse.class);

			Unmarshaller unmarshaller = context.createUnmarshaller();

			response = (SubsidiaryLocationReplicationResponse) unmarshaller.unmarshal(file);

		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		
		// 2. /* T_SUBSIDIARY_LOCATION */
		// Systém vytvorí vstupnú èas správy a ReplicationVolume:
		// DateFilterForDeletedRecords = vst. datumPoslednehoSpracovania
		// ReplicateFromDate = vst. datumPoslednehoSpracovania
		// ReplicateAll -nesme ist do volania
		// (žiada sa o všetky pridané, zmenené a zmazané dáta od poslednej aktualizácie)
		SubsidiaryLocationReplicationRequest request = new SubsidiaryLocationReplicationRequest();
		//ReplicationVolume volume = new ReplicationVolume();
		//volume.setReplicateFromDate(datumPoslSprac);
		//request.setReplicationVolume(volume);
		// aktualizujCiselnik();
		String spracujSK = dlg.getCudParametreClass().getValue(auth, "crd.spracujSK");
		// 6. Systém prijme výstupnú èas správy -

		//SubsidiaryLocationReplicationResponse response = null;

		try {
			//response = dlg.getCrdWS().subsidiaryLocationReplication(request);
			String stringRequest = "";// CudVysielanieUtils.marshal(response);
			if (!caller.statusOKnotify()) { // koncim
				return new ActionResult();
			}
			return spracujResSubsidiaryLocationBatch(auth, datumPoslSprac, response, stringRequest,
					spracovanieTabuliekId, "", spracujSK, caller);
			// ;sidiaryLocation(auth, datumPoslSprac, response, stringRequest, spracovanieTabuliekId,
			// statusOKnotify);
		} catch (Throwable e) {
			log.info(" CHYBA pri Import CudCrdProcess T_SUBSIDIARY_LOCATION  <br>" + e.getMessage());
			// java.net.SocketTimeoutException: Connection timed out
			// if (e.getMessage().indexOf("timed out")>=0) {
			// request.setCountryCodeISO(CountryCodeISO[] );
			// 3.1.2. urc krajinaList
			// Systém vyh¾adá ID country z èíselníka T_COUNTRY platný k aktuálnemu dátumu
			// ***Country - T_COUNTRY***
			String tabulka = "T_COUNTRY";
			// DTOCiselnik[] ciselnikList = null;
			// nacitam ciselnik
			ArrayList<DTOTCountry> countryList = dlg.getTCudCiselnikyClass().getCountryList(auth, new Date());
			log.info(" Prebieha Import CudCrdProcess T_SUBSIDIARY_LOCATION po Country");
			int pocetChyb = 0;
			for (DTOTCountry dtoCountry : countryList) {
				if (!caller.statusOKnotify()) { // koncim
					return new ActionResult();
				}
				sk.ditec.crd.ws.CountryCodeISO[] isoList = new sk.ditec.crd.ws.CountryCodeISO[1];
				sk.ditec.crd.ws.CountryCodeISO iso = new sk.ditec.crd.ws.CountryCodeISO();
				iso.setValue(dtoCountry.getCountryCodeIso());
				isoList[0] = iso;
				request.setCountryCodeISO(isoList);
				if (!caller.statusOKnotify()) { // koncim
					return new ActionResult();
				}
				boolean isSpracovane = false;
				int pocet = 1;

				while (!isSpracovane) {
					if (!caller.statusOKnotify()) { // koncim
						return new ActionResult();
					}
					if (pocet > 5) {
						break;
					}
					pocet++;
					try {
						if (pocet > 0) {
							Thread.sleep(12000); // cakaj, ked je pretazena WS, je nedostupna chyba 503
						}
						response = dlg.getCrdWS().subsidiaryLocationReplication(request);
						String stringRequest = CudVysielanieUtils.marshal(request);
						spracujResSubsidiaryLocationBatch(auth, datumPoslSprac, response, stringRequest,
								spracovanieTabuliekId, dtoCountry.getCountryCodeIso(), spracujSK, caller);
						isSpracovane = true;
					} catch (Throwable t) {
						pocetChyb++;
						log.info("chyba po sleeep  pocet=" + pocet + "    " + t);
						log.info(" CHYBA pri Import CudCrdProcess T_SUBSIDIARY_LOCATION  <br>" + t.getMessage());
						if (pocetChyb > 5) {
							log.info(" CHYBA pri Import CudCrdProcess T_SUBSIDIARY_LOCATION  nefunkcna WS nacitanie dat ");
						}
					}
				} // end while
			} // end for

			for (DTOTCountry dtoCountry : countryList) {
				sk.ditec.crd.ws.CountryCodeISO[] iso = new sk.ditec.crd.ws.CountryCodeISO[1];
				iso[1].setValue(dtoCountry.getCountryCodeIso());
				request.setCountryCodeISO(iso);
				response = dlg.getCrdWS().subsidiaryLocationReplication(request);
				String stringRequest = CudVysielanieUtils.marshal(request);
				if (!caller.statusOKnotify()) { // koncim
					return new ActionResult();
				}
				spracujResSubsidiaryLocationBatch(auth, datumPoslSprac, response, stringRequest, spracovanieTabuliekId,
						dtoCountry.getCountryCodeIso(), spracujSK,
						caller);
			}
		} // end catch
			// 3.1.3. PRe každú Krajinu z KrajinyList

		if (!caller.statusOKnotify()) { // koncim
			return new ActionResult();
		}

		return new ActionResult();
	}



	private ArrayList<DTOTSubsidiaryLocation> getLocationOldList(ArrayList<DTOTSubsidiaryLocation> tlocationlist,
			Date startValidity) {
		// sql += " AND pl.START_VALIDITY < " + CudVysielanieUtils.dateCritFormat(startValidity) + " AND ("
		// + " pl.END_VALIDITY IS NULL " + "  OR pl.END_VALIDITY >= "
		// + CudVysielanieUtils.dateCritFormat(startValidity) + " )";
		ArrayList<DTOTSubsidiaryLocation> list = new ArrayList<DTOTSubsidiaryLocation>();
		Long startValidityTime = (startValidity == null ? 0 : startValidity.getTime());
		for (DTOTSubsidiaryLocation dto : tlocationlist) {
			Long dtostartValidityTime = (dto.getStartValidity() == null ? 0 : dto.getStartValidity().getTime());
			Long dtoendValidityTime = (dto.getEndValidity() == null ? 0 : dto.getEndValidity().getTime());
			if (dtostartValidityTime < startValidityTime
					&& (dtoendValidityTime == 0 || dtoendValidityTime >= startValidityTime)) {
				list.add(dto);
			}
		}
		return list;
	}

	private ArrayList<DTOTSubsidiaryLocation> getLocationForDeakt(ArrayList<DTOTSubsidiaryLocation> tlocationlist,
			Date startValidity) {
		// .START_VALIDITY > vst. StartValidity a zároveň
		// .ACTIVE_FLAG=T a zároveň
		Long startValidityTime = (startValidity == null ? 0 : startValidity.getTime());
		ArrayList<DTOTSubsidiaryLocation> list = new ArrayList<DTOTSubsidiaryLocation>();
		for (DTOTSubsidiaryLocation dto : tlocationlist) {
			Long dtostartValidityTime = (dto.getStartValidity() == null ? 0 : dto.getStartValidity().getTime());
			if ("T".equals(dto.getActiveFlag()) && dtostartValidityTime > startValidityTime) {
				list.add(dto);
			}
		}
		return list;
	}

	private void zapisLokCezZmenoveProcesy(AuthInfo auth, Map<String, String> rowMap, DTOImport dtoImport,
			int navratovyKod, String popisSpracovania) throws Throwable {

		DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, new Date(), null, null);
		DTOZmena dtoZmenaZap = null;

		String stringRowMap = rowMap.toString();
		Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();

		List<DTOCiselnikStlpec> csList = dlgcud.getCiselnikStlpecRead().listLight(auth, dtoImport.getIDCiselnik());
		dlgcud.getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);
		String chyba = "";
		if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
			navratovyKod = 4;
			for (DTOImportMsg dto : dtoVal.getImportZmenaDTO().getImportMsgList()) {
				chyba += dto.getMsg();
				popisSpracovania += dto.getMsg();
			}
			chyba += rowMap.toString();
			log.info(chyba + "  " + stringRowMap);
		}

		Map<Integer, DTOUcet[]> ucetMap = new HashMap<Integer, DTOUcet[]>();

		List<DTOWfDef> wfDefList = dlgcud.getWfDefRead().list(auth, dtoImport.getIDCiselnik());
		if (StringUtils.isValid(dtoImport.getIDCiselnik())) {
			DTOWorkflow dtoWf = dlgcud.getWorkflow().generujWorkflowAll(auth, dtoImport.getIDCiselnik(),
					dtoVal.getImportZmenaDTO(), wfDefList, ucetMap);
			if (StringUtils.isValid(dtoWf)) {
				ActionResult res = cudPau.workflowUpdateCrd(auth, dtoWf, dtoVal.getImportZmenaDTO(), new Date());
				dtoZmenaZap = (DTOZmena) res.getResult();
				if (dtoZmenaZap != null) {
					dlg.getCrdAktualizujCiselnikClass().aktualizujCiselnik(auth, dtoZmenaZap);
				}
			} else { // dtoWf not valid
				popisSpracovania = "NeÚspešné spracovanie OLDPRIMLOC DTOWorkflow is null ";

				log.debug(popisSpracovania);
			}
		}

	}

	static Comparator<DTOTSubsidiaryLocation> compareByHistDesc = new Comparator<DTOTSubsidiaryLocation>() {
		@Override
		public int compare(DTOTSubsidiaryLocation o1, DTOTSubsidiaryLocation o2) {
			return o2.getHistID().compareTo(o1.getHistID());
		}
	};
}
