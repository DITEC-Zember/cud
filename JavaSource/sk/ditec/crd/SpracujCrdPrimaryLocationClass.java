package sk.ditec.crd;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
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
import sk.ditec.crd.ws.Company;
import sk.ditec.crd.ws.CompanyReplicationResponse;
import sk.ditec.crd.ws.Country;
import sk.ditec.crd.ws.CountryReplicationResponse;
import sk.ditec.crd.ws.PrimaryLocation;
import sk.ditec.crd.ws.PrimaryLocationReplicationRequest;
import sk.ditec.crd.ws.PrimaryLocationReplicationResponse;
import sk.ditec.crd.ws.ReplicationVolume;
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

public class SpracujCrdPrimaryLocationClass extends _NovyPISBaseClass {
	private _CudCrdDelegate dlg = new _CudCrdDelegate();
	private _CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private CudPauClass cudPau = new CudPauClass();
	private Logger log = LoggerFactory.getLogger(CudCrdProcess.class);

	public ActionResult spracujPrimaryLocation(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			BaseProcess caller) throws AppException, MalformedURLException {

		// Ak Priznak ajSk nie je nastaveny
		// /*KrajinyList*/
		// Systém z KrajinyList vyhodi SK
		String spracujSK = dlg.getCudParametreClass().getValue(auth, "crd.spracujSK");
		// 2.
		// Systém vytvorí vstupnú èas yReplicationRequest a ReplicationVolume:
		// DateFilterForDeletedRecords = vst. datumPoslednehoSpracovania
		// ReplicateFromDate = vst. datumPoslednehoSpracovania
		// ReplicateAll -nesme ist do volania
		// (žiada sa o všetky pridané, zmenené a zmazané dáta od poslednej aktualizácie)
		PrimaryLocationReplicationRequest request = new PrimaryLocationReplicationRequest();
		ReplicationVolume volume = new ReplicationVolume();

		volume.setReplicateFromDate(datumPoslSprac);

		request.setReplicationVolume(volume);


		// 6. Systém prijme výstupnú èas správy -
		// Batch, ked spadne, tak davkove spracovanie po krajinach
		// ked spadne nacitanie na timeoute
		PrimaryLocationReplicationResponse response = null;
		String stringRequest = "";

		try {
			// ked je interval dlhsi ako tyzden, pojdem po krajinach
			String spracujStat = dlg.getCudParametreClass().getValue(auth, "crd.spracuj.country");
			if ((new Date()).getTime() - datumPoslSprac.getTime() > 604800000) {
				return spracujPrimaryLocationPoCountry(auth, datumPoslSprac, spracovanieTabuliekId, request, spracujSK,
						caller, spracujStat);
			} else {
				response = dlg.getCrdWS().primaryLocationReplication(request);
				stringRequest = CudVysielanieUtils.marshal(request);
				return spracujPrimaryLocationBatch(auth, datumPoslSprac, response, stringRequest,
						spracovanieTabuliekId, "", spracujSK, caller);
			}

		} catch (Throwable e) {
			// ries po krajinach
			log.info(" CHYBA pri Import CudCrdProcess T_PRIMARY_LOCATION, volam po Country <br>" + e.getMessage());
			return spracujPrimaryLocationPoCountry(auth, datumPoslSprac, spracovanieTabuliekId, request, spracujSK,
					caller, null);
		}

	}

	public ActionResult spracujPrimaryLocationPoCountry(AuthInfo auth, Date datumPoslSprac,
			Integer spracovanieTabuliekId, PrimaryLocationReplicationRequest request, String spracujSK,
			BaseProcess caller, String spracujStat) throws AppException {

		ArrayList<DTOTCountry> countryList = dlg.getTCudCiselnikyClass().getCountryList(auth, new Date());
		// countryList = new ArrayList<DTOTCountry>();
		// DTOTCountry country = new DTOTCountry();
		// country.setCountryCodeIso("FR");
		// countryList.add(country);

		PrimaryLocationReplicationResponse response = null;
		int pocetChyb = 0;

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
						Thread.sleep(60000); // cakaj, ked je pretazena WS, je nedostupna chyba 503
					}
					response = dlg.getCrdWS().primaryLocationReplication(request);
					String stringRequest = CudVysielanieUtils.marshal(request);
					spracujPrimaryLocationBatch(auth, datumPoslSprac, response, stringRequest, spracovanieTabuliekId,
							dtoCountry.getCountryCodeIso(), spracujSK, caller);
					isSpracovane = true;
				} catch (Throwable t) {
					pocetChyb++;
					log.info("chyba po sleeep  pocet=" + pocet + "    " + t);
					String chyba = " CHYBA pri Import CudCrdProcess T_PRIMARY_LOCATION  <br> nefunkcna WS nacitanie dat"
							+ t.getMessage();
					log.info(chyba);
					if (pocetChyb > 5) {
						String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.crd.mail");
						log.info(chyba);
						NotifUtils.sendNotif("", mailingList, "Chyba pri CUD CRD IMPORT", chyba);
					}

					if (pocetChyb > 5) {
						log.info(" CHYBA pri Import CudCrdProcess T_PRIMARY_LOCATION  nefunkcna WS nacitanie dat ");
					}
				}
			} // end whille
		} // end for
		return new ActionResult();
		}

	public void spracujPrimaryLocationBatch(AuthInfo auth, Date datumPoslSprac, PrimaryLocation primaryLocation,
			String stringRequest, Integer spracovanieTabuliekId, String countryIso, String spracujSK, BaseProcess caller)
			throws AppException {
		PrimaryLocationReplicationResponse response = new PrimaryLocationReplicationResponse();
		response.setPrimaryLocation(new PrimaryLocation[] { primaryLocation });
		spracujPrimaryLocationBatch(auth, datumPoslSprac, response, stringRequest, spracovanieTabuliekId, countryIso,
				spracujSK,
				caller);
	}

	// CUD DZ RD_Primary location - Batch
	public ActionResult spracujPrimaryLocationBatch(AuthInfo auth, Date datumPoslSprac,
			PrimaryLocationReplicationResponse response, String stringVstupneXml, Integer spracovanieTabuliekId,
			String countryIso, String spracujSK,
 BaseProcess caller) {
		ActionResult actRes = new ActionResult();
		String chyba = "";
		String spracovane = "";
		String bezZmien = "";

		Integer pocet = 0;
		if (response != null) {
			pocet = response.getPrimaryLocationLength();
		}
		String popisSpracovania = "PRIMARY_LOCATION počet záznamov na spracovanie " + pocet + " pre country "
				+ countryIso;
		Integer navratovyKod = 0; // nie su zaznamy na spracovanie

		// 7.Systém zapíše do údajov o výsledku spracovania výstupné XML
		// Systém aktualizuje údaje v CUD_SPRACOVANIE_TABULIEK:
		// .VYSTUPNE_XML= vst. XML
		// kde .SPRACOVANIE_TABULIEK_ID = vst. SpracovanieTabuliekID
		try {
			// 2.
			String tabulka = "T_PRIMARY_LOCATION";
			// DTOCiselnik[] ciselnikList = null;
			// nacitam ciselnik
			DTOCiselnik dtoCis = dlgcud.getCiselnikRead().readLight(auth, tabulka);
			if (!StringUtils.isValid(dtoCis)) {
				chyba += tabulka + " sa nenachaza v zozname ciselnikov!";

				return actRes;
			}
			// 3 /*SpracovanieTabuliekCud = CUD_SPRACOVANIE_TABULIEK*/
			// Vytvorí sa záznam o spracovaní tabu¾ky - T_PRIMARY_LOCATION

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

			// 5.Systém nastaví pomocnú premennú bolaZmena = False
			boolean bolaZmena = false;
			boolean zapisatZmeny = false;

			DTOImportZmena[] importZmenaList = new DTOImportZmena[1];
			DTOImportZmena dtoZmena = new DTOImportZmena();
			DTOImport dtoImport = new DTOImport();
			dtoImport.setIDCiselnik(dtoCis.getCiselnikID());
			dtoImport.setCiselnikTabulka(tabulka);
			dtoImport.setStav("CRD");

			if (response != null && response.getPrimaryLocation() != null && response.getPrimaryLocation().length > 0) {
				// 6.1. Pre každý záznam zo sekcie z výstupnej správy

				navratovyKod = 1; // existuju zaznamy na spracovanie

				for (PrimaryLocation ws : response.getPrimaryLocation()) {
					bolaZmena = false;
					popisSpracovania = "";
					if (!caller.statusOKnotify()) { // koncim
						return new ActionResult();
					}
					try {
					String spracovanyZaznam = " LocPrimCode=" + ws.getLocationCode();
					String stringLokVstupXml = CudVysielanieUtils.marshal(ws);
						DTOCrdSpracTabuliek dtosprac = new DTOCrdSpracTabuliek();
						dtosprac.setIdCrdSpracovanie(spracovanieTabuliekId);
						dtosprac.setIdCiselnik(dtoCis.getCiselnikID());
						dtosprac.setZmenoveXmlVstup(CudVysielanieUtils.marshal(ws));
						dtosprac.setReplicateAll("N");
						dtosprac.setReplicateFromDate(datumPoslSprac);
						dtosprac.setNavratovyKod(navratovyKod);
						dtosprac.setPopisSpracovania(spracovanyZaznam);
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().update(auth, dtosprac)
								.getResult();

					dtosprac.setZmenoveXmlVstup(stringLokVstupXml);
						dtosprac.setPopisSpracovania("spracovavam zaznam " + spracovanyZaznam);
					dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().update(auth, dtosprac).getResult();
					dtosprac.setNavratovyKod(navratovyKod);

					// 6.1.1. /*tCountryCud = T_COUNTRY*/
					// if (ws.getCountry().getCountryCodeISO() != null
					// && ws.getCountry().getCountryCodeISO().getValue().length() > 0) {

					// Ak Priznak ajSk nie je nastaveny
					// System z PimaryLocationReplicationRequest
					// vyhodi zaznamy kde Country.Country_UIC_Code=SK a Company.Company_UIC_Code=056
					if (!"T".equals(spracujSK)) {
						if (_CudConsts.COUNTRY_CODE_ISO_SK.equals(ws.getCountry().getCountryCodeISO().getValue())
								&& _CudConsts.COMPANY_UIC_CODE_ZSR.equals(ws.getResponsibleIM().getCompanyUICCode())) {
							continue;
						}
					}
					DTOTCountry tCountry = dlg.getTCudCiselnikyClass().getCountryByIso(auth,
							ws.getCountry().getCountryCodeISO().getValue());
					// Systém vyh¾adá èi záznam zo vstupu je zapísaný v T_COUNTRY Vrát záznam pod¾a kódu


					if (tCountry == null || tCountry.getCountryID() == null) {
						CountryReplicationResponse countryReplicationResponse = new CountryReplicationResponse();
						countryReplicationResponse.setCountry(new Country[] { ws.getCountry() });

						dlg.getSpracujCrdCountryClass().updateCountry(auth, datumPoslSprac, spracovanieTabuliekId,
								stringLokVstupXml, countryReplicationResponse, caller);

						tCountry = dlg.getTCudCiselnikyClass().getCountryByIso(auth,
								ws.getCountry().getCountryCodeISO().getValue());
						if (tCountry == null || tCountry.getCountryID() == null) {
						// Systém nastaví :
						// navratovyKod = 3
						// popisSpracovania = "Chyba - nebol nájdený záznam väzobného èíselníka."
						navratovyKod = 3;
							popisSpracovania += "Chyba - nebol nájdený záznam väzobného číselníka Country k PrimLoc"
								+ ws.getCountry().getCountryCodeISO().getValue();
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
					/* tResponsibleIMCud = T_COMPANY */
					// Systém vyh¾adá záznam o spoloènosti k primary location

					DTOTCompany tCompany = dlg.getTCudCiselnikyClass().getCompany(auth,
								ws.getResponsibleIM().getCompanyUICCode(), ws.getResponsibleIM().getStartValidity());
					// // Systém vyh¾adá èi záznam zo vstupu je zapísaný v T_COMPANY Vrát záznam pod¾a kódu
					//
					if (tCompany == null || tCompany.getCompanyID() == null) { // nie je povinna polozka
						CompanyReplicationResponse companyReplicationResponse = new CompanyReplicationResponse();
						companyReplicationResponse.setCompany(new Company[] { ws.getResponsibleIM() });

						dlg.getSpracujCrdCompanyClass().updateCompany(auth, datumPoslSprac, spracovanieTabuliekId,
								stringVstupneXml, companyReplicationResponse, caller);

						tCompany = dlg.getTCudCiselnikyClass().getCompany(auth,
								ws.getResponsibleIM().getCompanyUICCode());
						if (tCompany == null || tCompany.getCompanyID() == null) { // nie je povinna polozka
						// // Systém nastaví :
						// // navratovyKod = 3
						// popisSpracovania = "Chyba - nebol nájdený záznam väzobného èíselníka."
						navratovyKod = 3;
							popisSpracovania += "Chyba - nebol nájdený záznam väzobného číselníka company k PrimLoc"
								+ ws.getResponsibleIM().getCompanyUICCode();
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
					// 6.1.3
						ArrayList<DTOTPrimaryLocation> tlocationlist = dlg.getTCudCiselnikyClass()
								.getPrimaryLocationList(auth, ws.getLocationCode(),
										ws.getCountry().getCountryCodeISO().getValue());



						// Ak END_VALIDITY došlého záznamy je null
						if (ws.getEndValidity() == null) {
							// Pre každý deaktivácia z deaktiváciaList



					// Integer idCountry = tCountry.getCountryID();
					dtoZmena.setPlatnostOd(new Date());
					// 6.1.4 Ak záznam nebol nájdený - nový záznam
						ArrayList<DTOTPrimaryLocation> deaktList = getLocationForDeakt(tlocationlist,
								ws.getStartValidity());
						for (DTOTPrimaryLocation dto : deaktList) {
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
							rowMapOld.put("PRIMARY_LOCATION_ID", dto.getPrimaryLocationID().toString());
							rowMapOld.put("XLS_OPERACIA", "U");
							rowMapOld.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(new Date()));
							rowMapOld.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));
							rowMapOld.put("ACTIVE_FLAG", "F");
							dtosprac.setZmenoveXmlVystup(rowMapOld.toString());
							dtosprac.setPopisSpracovania(spracovanyZaznam + " ACTIVE_FLAG=F");


							try {
								dlg.getCrdSpracovanieClass().zapisLokCezZmenoveProcesy(auth, rowMapOld, dtoImport,
										navratovyKod, popisSpracovania);
							} catch (Throwable e) {
								log.error(popisSpracovania + e);
								dtosprac.setKod("1");
								dtosprac.setPopisSpracovania(spracovanyZaznam + " CHYBA:" + e.getMessage());
								dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
										.updateANulujZmenoveXML(auth, dtosprac).getResult();
								continue;
							}
							dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
									.updateANulujZmenoveXML(auth, dtosprac).getResult();
						}
						}

						// ked nebol najdeny, zisti, ci existuju platne zaznamy a ukonci ich
						DTOTPrimaryLocation olPrimary = getLocationOld(tlocationlist, ws.getStartValidity());
						if (olPrimary != null) {

							Map<String, String> rowMapOld = new HashMap<String, String>();
							rowMapOld.put("PRIMARY_LOCATION_ID", olPrimary.getPrimaryLocationID().toString());
							rowMapOld.put("XLS_OPERACIA", "U");
							rowMapOld.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(new Date()));
							rowMapOld.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));

							rowMapOld.put("END_VALIDITY",
									CudVysielanieUtils.getStringDatum(DateUtils.plusDay(ws.getStartValidity(), -1)));
							dtosprac.setZmenoveXmlVystup(rowMapOld.toString());
							dtosprac.setKod("0");
							dtosprac.setPopisSpracovania(spracovanyZaznam + " Deactivacia");


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
								// continue;
							}

						}

						DTOTPrimaryLocation tlocation = getLocationBystartValidity(tlocationlist, ws.getStartValidity());

					Map<String, String> rowMap = new HashMap<String, String>();
					if (tlocation == null) {

					
						// 6.1.4.1. Ak bolaZmena = False
						
						// if (!bolaZmena) {
						// // Systém vytvorí vstupnú èas správy na zmenu hodnôt èíselníka , prièom
						// // ciselnikNazov = T_PRIMARY_LOCATION
						//
						// // zapisatZmeny = True
						// zapisatZmeny = true;
						// // Systém nastaví pomocnú premennú bolaZmena = True
						// bolaZmena = true;
						// }
						// Systém vytvorí výstupnú èas správy Zaznamy a naviaže ju na ZmenaHodnotCiselnika, prièom
						// ID = NULL
						// operacia = "N"
						// platnostOd = Country.DateAdd
						// datumSchvalenia = Country.DateAdd
						// anika tieto casti aj s naplnenim IdCountry a IdCompany je spolocne, tak je na konci
						dtoZmena.setOperacia("N");

						// rowMap.put("PRIMARY_LOCATION_ID", tCountry.getCountryID().toString()); pri novom riadku sa
						// neudava
						rowMap.put("FREE_TEXT", CudVysielanieUtils.refactorApostrof(ws.getFreeText()));
						rowMap.put("LOCATION_NAME", CudVysielanieUtils.refactorApostrof(ws.getLocationName()));
						rowMap.put("LOCATION_CODE", ws.getLocationCode());
						rowMap.put("LOCATION_NAME_ASCII",
								CudVysielanieUtils.refactorApostrof(ws.getLocationNameASCII()));
						rowMap.put("NUTS_CODE", ws.getNUTSCode());
						rowMap.put("CONTAINER_HANDLING_FLAG", ws.isContainerHandlingFlag() ? "T" : "F");
						rowMap.put("HANDOVER_POINT_FLAG", ws.isHandoverPointFlag() ? "T" : "F");
						rowMap.put("ACTIVE_FLAG", "T");

						rowMap.put("ID_COUNTRY", tCountry.getCountryID().toString());

						rowMap.put("ID_COMPANY", tCompany.getCompanyID().toString());

						rowMap.put(
								"START_VALIDITY",
								(ws.getStartValidity() != null ? DateUtils.formatDate(ws.getStartValidity(),
										"dd.MM.yyyy") : null));

						rowMap.put("END_VALIDITY",
								(ws.getEndValidity() != null ? DateUtils.formatDate(ws.getEndValidity(),
									"dd.MM.yyyy") : null));									

						rowMap.put("FREIGHT_POSSIBLE_FLAG", ws.isFreightPossibleFlag() ? "T" : "F");
						rowMap.put(
								"FREIGHT_END_VALIDITY",
									(ws.getFreightEndValidity() != null ? DateUtils.formatDate(ws.getFreightEndValidity(),
											"dd.MM.yyyy") : null));
					
						rowMap.put(
								"FREIGHT_START_VALIDITY",
									(ws.getFreightStartValidity() != null ? DateUtils.formatDate(ws.getFreightStartValidity(),
											"dd.MM.yyyy") : null));
						
						rowMap.put("LATITUDE", (ws.getLatitude() != null ? ws.getLatitude().toString() : null));
						rowMap.put("LONGITUDE", (ws.getLongitude() != null ? ws.getLongitude().toString() : null));

						rowMap.put("PASSENGER_POSSIBLE_FLAG", ws.isPassengerPossibleFlag() ? "T" : "F");
						
						rowMap.put("PASSENGER_END_VALIDITY",
											(ws.getPassengerEndValidity()!= null ? DateUtils.formatDate(ws.getPassengerEndValidity(),"dd.MM.yyyy") : null ));
						
						rowMap.put(
								"PASSENGER_START_VALIDITY",
								(ws.getPassengerStartValidity() != null ? DateUtils.formatDate(
										ws.getPassengerStartValidity(), "dd.MM.yyyy") : null));

						rowMap.put("XLS_OPERACIA", "N");
						dtoZmena.setOperacia("N");
						bolaZmena = true;
					} else { // update

						rowMap.put("PRIMARY_LOCATION_ID", tlocation.getPrimaryLocationID().toString());

						if (!CudVysielanieUtils.isEqual(tCountry.getCountryID(), tlocation.getIDCountry())) {
							rowMap.put("ID_COUNTRY", tCountry.getCountryID().toString());
							bolaZmena = true;
						}
						if (!CudVysielanieUtils.isEqual(tCompany.getCompanyID(), tlocation.getIDCompany())) {
							rowMap.put("ID_COMPANY", tCompany.getCompanyID().toString());
							bolaZmena = true;
						}
						// rowMap.put("FREE_TEXT", ws.getFreeText());
						if (!CudVysielanieUtils.isEqual(ws.getFreeText(), tlocation.getFreeText())) {
							bolaZmena = true;
							rowMap.put("FREE_TEXT", CudVysielanieUtils.refactorApostrof(ws.getFreeText()));
						}
						// rowMap.put("LOCATION_NAME", ws.getLocationName());
						if (!CudVysielanieUtils.isEqual(ws.getLocationName(), tlocation.getLocationName())) {
							bolaZmena = true;
							rowMap.put("LOCATION_NAME", CudVysielanieUtils.refactorApostrof(ws.getLocationName()));
						}
						// rowMap.put("LOCATION_CODE", ws.getLocationCode());
						if (!CudVysielanieUtils.isEqual(ws.getLocationCode(), tlocation.getLocationCode())) {
							bolaZmena = true;
							rowMap.put("LOCATION_CODE", ws.getLocationCode());
						}

						// rowMap.put("LOCATION_NAME_ASCII", ws.getLocationNameASCII());
						if (!CudVysielanieUtils.isEqual(ws.getLocationNameASCII(), tlocation.getLocationNameAscii())) {
							bolaZmena = true;
							rowMap.put("LOCATION_NAME_ASCII",
									CudVysielanieUtils.refactorApostrof(ws.getLocationNameASCII()));
						}
						
							if (!CudVysielanieUtils.isEqual(ws.getFreeText(), tlocation.getFreeText())) {
							bolaZmena = true;
							rowMap.put("FREE_TEXT", CudVysielanieUtils.refactorApostrof(ws.getFreeText()));
						}

							if (!"T".equals(tlocation.getActiveFlag())) {
								bolaZmena = true;
								rowMap.put("ACTIVE_FLAG", "T");
							}

						// rowMap.put("NUTS_CODE", ws.getNUTSCode());
						if (!CudVysielanieUtils.isEqual(ws.getNUTSCode(), tlocation.getNutsCode())) {
							bolaZmena = true;
							rowMap.put("NUTS_CODE", ws.getNUTSCode());
						}
						// rowMap.put("START_VALIDITY", DateUtils.formatDate(ws.getStartValidity(), "dd.MM.yyyy"));
						if (!CudVysielanieUtils.isEqual(ws.getStartValidity(), tlocation.getStartValidity())) {
							bolaZmena = true;
							rowMap.put(
									"START_VALIDITY",
									(ws.getStartValidity() != null ? DateUtils.formatDate(ws.getStartValidity(),
											"dd.MM.yyyy") : null));
						}
						// rowMap.put("END_VALIDITY", DateUtils.formatDate(ws.getEndValidity(), "dd.MM.yyyy"));
						if (!CudVysielanieUtils.isEqual(ws.getEndValidity(), tlocation.getEndValidity())) {
							bolaZmena = true;
							rowMap.put(
									"END_VALIDITY",
									(ws.getEndValidity() != null ? DateUtils.formatDate(ws.getEndValidity(),
											"dd.MM.yyyy") : null));
						}

						if (!CudVysielanieUtils.isEqual(ws.isContainerHandlingFlag(),
								tlocation.getContainerHandlingFlag())) {
							bolaZmena = true;
							rowMap.put("CONTAINER_HANDLING_FLAG", ws.isContainerHandlingFlag() ? "T" : "F");
						}

						if (!CudVysielanieUtils.isEqual(ws.isHandoverPointFlag(), tlocation.getHandoverPointFlag())) {
							bolaZmena = true;
							rowMap.put("HANDOVER_POINT_FLAG", ws.isHandoverPointFlag() ? "T" : "F");
						}

						if (!CudVysielanieUtils.isEqual(ws.isFreightPossibleFlag(), tlocation.getFreightPossibleFlag())) {
							bolaZmena = true;
							rowMap.put("FREIGHT_POSSIBLE_FLAG", ws.isFreightPossibleFlag() ? "T" : "F");
						}
						if (!CudVysielanieUtils.isEqual(ws.isFreightPossibleFlag(), tlocation.getFreightPossibleFlag())) {
							bolaZmena = true;
							rowMap.put("FREIGHT_POSSIBLE_FLAG", ws.isFreightPossibleFlag() ? "T" : "F");
						}
						// rowMap.put("FREIGHT_END_VALIDITY",
						// DateUtils.formatDate(ws.getFreightEndValidity(), "dd.MM.yyyy"));
						if (!CudVysielanieUtils.isEqual(ws.getFreightEndValidity(), tlocation.getFreightEndValidity())) {
							bolaZmena = true;
							rowMap.put(
									"FREIGHT_END_VALIDITY",
									(ws.getFreightEndValidity() != null ? DateUtils.formatDate(
											ws.getFreightEndValidity(), "dd.MM.yyyy") : null));
						}
						// rowMap.put("FREIGHT_START_VALIDITY",
						// DateUtils.formatDate(ws.getFreightStartValidity(), "dd.MM.yyyy"));
						if (!CudVysielanieUtils.isEqual(ws.getFreightStartValidity(),
								tlocation.getFreightStartValidity())) {
							bolaZmena = true;
							rowMap.put(
									"FREIGHT_START_VALIDITY",
									(ws.getFreightStartValidity() != null ? DateUtils.formatDate(
											ws.getFreightStartValidity(), "dd.MM.yyyy") : null));
						}
						// rowMap.put("LATITUDE", ws.getLatitude().toString());
						if (!CudVysielanieUtils.isEqual(ws.getLatitude(), tlocation.getLatitude())) {
							bolaZmena = true;
							rowMap.put("LATITUDE", (ws.getLatitude() != null ? ws.getLatitude().toString() : null));
						}
						// rowMap.put("LONGITUDE", ws.getLongitude().toString());
						if (!CudVysielanieUtils.isEqual(ws.getLongitude(), tlocation.getLongitude())) {
							bolaZmena = true;
							rowMap.put("LONGITUDE", (ws.getLongitude() != null ? ws.getLongitude().toString() : null));
						}
						// rowMap.put("PASSENGER_END_VALIDITY",
						// DateUtils.formatDate(ws.getPassengerEndValidity(), "dd.MM.yyyy"));
						if (!CudVysielanieUtils.isEqual(ws.isPassengerPossibleFlag(),
								tlocation.getPassengerPossibleFlag())) {
							bolaZmena = true;
							rowMap.put("PASSENGER_POSSIBLE_FLAG", ws.isPassengerPossibleFlag() ? "T" : "F");
						}

						if (!CudVysielanieUtils.isEqual(ws.getPassengerEndValidity(),
								tlocation.getPassengerEndValidity())) {
							bolaZmena = true;
							rowMap.put(
									"PASSENGER_END_VALIDITY",
									(ws.getPassengerEndValidity() != null ? DateUtils.formatDate(
											ws.getPassengerEndValidity(), "dd.MM.yyyy") : null));
						}
						// rowMap.put("PASSENGER_START_VALIDITY",
						// DateUtils.formatDate(ws.getPassengerStartValidity(), "dd.MM.yyyy"))
						if (!CudVysielanieUtils.isEqual(ws.getPassengerStartValidity(),
								tlocation.getPassengerStartValidity())) {
							bolaZmena = true;
							rowMap.put(
									"PASSENGER_START_VALIDITY",
									(ws.getPassengerStartValidity() != null ? DateUtils.formatDate(
											ws.getPassengerStartValidity(), "dd.MM.yyyy") : null));
						}
						if (bolaZmena) {
							dtoZmena.setOperacia("U");
						}
						rowMap.put("XLS_OPERACIA", "U");
					}
					// 6.1.4.1.
					if (!bolaZmena) {
						bezZmien += "LocPrimCode=" + ws.getLocationCode() + " ; ";
						dtosprac.setPopisSpracovania("Bez zmien " + spracovanyZaznam);
						dtosprac.setNavratovyKod(0);
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().updateANuluj(auth, dtosprac)
								.getResult();
						continue;
					}

					Date $vlozDatum = new Date();

						// if (bolaZmena) {
						// $vlozDatum = CudVysielanieUtils.getMax(ws.getModifiedDate(), $vlozDatum);
						// } else {
						// $vlozDatum = CudVysielanieUtils.getMax(ws.getAddDate(), $vlozDatum);
						// }

						rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum($vlozDatum));
					// wsCompany.getModifiedDate()
					rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));

					// ///////////////////////////////////////////////////////////////////////////////////////

					importZmenaList[0] = dtoZmena;
					

					DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, new Date(), null, null);
					DTOZmena dtoZmenaZap = null;
					String stringRowMap = rowMap.toString();
					dtosprac.setZmenoveXmlVystup(stringRowMap);
					Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();
					List<DTOCiselnikStlpec> csList = dlgcud.getCiselnikStlpecRead().listLight(auth,
							dtoImport.getIDCiselnik());
					dlgcud.getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);

					if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
						navratovyKod = 4;
						for (DTOImportMsg dto : dtoVal.getImportZmenaDTO().getImportMsgList()) {
							chyba += dto.getMsg();
							popisSpracovania += dto.getMsg();
						}
						chyba += rowMap.toString();
						dtosprac.setPopisSpracovania(spracovanyZaznam + chyba);
						dtosprac.setNavratovyKod(navratovyKod);
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().updateANuluj(auth, dtosprac)
								.getResult();
						continue;
					}
						

					Map<Integer, DTOUcet[]> ucetMap = new HashMap<Integer, DTOUcet[]>();

					List<DTOWfDef> wfDefList = dlgcud.getWfDefRead().list(auth, dtoImport.getIDCiselnik());

					// Integer totalCount = dlgcud.getImportZmenaRead().pocet(auth, dtoImport.getImportID());
					// int page = 0;
					// Integer pocet = 0;

					if (StringUtils.isValid(dtoImport.getIDCiselnik())) {

						DTOWorkflow dtoWf = dlgcud.getWorkflow().generujWorkflowAll(auth, dtoImport.getIDCiselnik(),
								dtoVal.getImportZmenaDTO(), wfDefList, ucetMap);
						if (StringUtils.isValid(dtoWf)) {
							ActionResult res = cudPau.workflowUpdateCrd(auth, dtoWf, dtoVal.getImportZmenaDTO(),
									datumPoslSprac);
							// sendNotif(auth, dtoCis, dtoVal, dtoWf, wfDefList, metaMapForSend, fkMetaMap);
							dtoZmenaZap = (DTOZmena) res.getResult();
							// 10.
							spracovane += "PrimaryLocation=" + ws.getLocationCode() + " ; ";
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
						dtospracParent.setPopisSpracovania("Neuspesne spracovanie" + e.getMessage());
						dtospracParent.setNavratovyKod(navratovyKod);
						
						dtospracParent = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
								.updateANuluj(auth, dtospracParent)
									.getResult();
						
						log.error("spracujPrimaryLocationClass.spracujSubsidiaryLocation.error", e);
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
			// handleException(e, "spracujPrimaryLocationClass.spracujPrimaryLocation.error", auth);
			rollbackConnection(auth);
			log.error("spracujPrimaryLocationClass.spracujPrimaryLocation.error", e);
			actRes.setError(true);
			actRes.setKeyErrorMsg(e.getMessage());
			return actRes;
		}
	}




	public ActionResult spracujPrimaryLocationTest(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			BaseProcess caller) throws AppException, MalformedURLException {

		File file = new File("c:/JAVA/git/CUD/cud/xml/primloc.xml");

		PrimaryLocationReplicationResponse response = new PrimaryLocationReplicationResponse();
		try {
			JAXBContext context = JAXBContext.newInstance(PrimaryLocationReplicationResponse.class);

			Unmarshaller unmarshaller = context.createUnmarshaller();

			response = (PrimaryLocationReplicationResponse) unmarshaller.unmarshal(file);

		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String stringRequest = "";
		String spracujSK = dlg.getCudParametreClass().getValue(auth, "crd.spracujSK");
		try {

			String countryIso = "";
			// PrimaryLocationReplicationResponse response = new PrimaryLocationReplicationResponse();
			// response.setPrimaryLocation(new PrimaryLocation[] { primaryLocation });
			return spracujPrimaryLocationBatch(auth, datumPoslSprac, response, stringRequest, spracovanieTabuliekId,
					countryIso, spracujSK, caller);
		} catch (Throwable e) {
			// ries po krajinach
			log.info(" CHYBA pri Import CudCrdProcess T_PRIMARY_LOCATION, volam po Country <br>" + e.getMessage());
			return null;
		}
	}


	private DTOTPrimaryLocation getLocationBystartValidity(ArrayList<DTOTPrimaryLocation> tlocationlist,
			Date startValidity) {
		// " AND pl.START_VALIDITY =" + CudVysielanieUtils.dateCritFormat(startValidity);

		for (DTOTPrimaryLocation dto : tlocationlist) {
			if (CudVysielanieUtils.isEqual(startValidity, dto.getStartValidity())) {
				return dto;
			}
		}
		return null;
	}

	private DTOTPrimaryLocation getLocationOld(ArrayList<DTOTPrimaryLocation> tlocationlist, Date startValidity) {
		// sql += " AND pl.START_VALIDITY < " + CudVysielanieUtils.dateCritFormat(startValidity) + " AND ("
		// + " pl.END_VALIDITY IS NULL " + "  OR pl.END_VALIDITY >= "
		// + CudVysielanieUtils.dateCritFormat(startValidity) + " )";
		Long startValidityTime = (startValidity == null ? 0 : startValidity.getTime());
		for (DTOTPrimaryLocation dto : tlocationlist) {
			Long dtostartValidityTime = (dto.getStartValidity() == null ? 0 : dto.getStartValidity().getTime());
			Long dtoendValidityTime = (dto.getEndValidity() == null ? 0 : dto.getEndValidity().getTime());
			if (dtostartValidityTime < startValidityTime
					&& (dtoendValidityTime == 0 || dtoendValidityTime >= startValidityTime)) {
				return dto;
			}
		}
		return null;
	}

	private ArrayList<DTOTPrimaryLocation> getLocationForDeakt(ArrayList<DTOTPrimaryLocation> tlocationlist,
			Date startValidity) {
		// .START_VALIDITY > vst. StartValidity a zároveň
		// .ACTIVE_FLAG=T a zároveň
		Long startValidityTime = (startValidity == null ? 0 : startValidity.getTime());
		ArrayList<DTOTPrimaryLocation> list = new ArrayList<DTOTPrimaryLocation>();
		for (DTOTPrimaryLocation dto : tlocationlist) {
			Long dtostartValidityTime = (dto.getStartValidity() == null ? 0 : dto.getStartValidity().getTime());
			if ("T".equals(dto.getActiveFlag()) && dtostartValidityTime > startValidityTime) {
				list.add(dto);
			}
		}
		return list;
	}
}
