package sk.ditec.crd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.dto.DTOCrdNenajdeneZaznamy;
import sk.ditec.crd.dto.DTOCrdSpracTabuliek;
import sk.ditec.crd.dto.DTOTCompany;
import sk.ditec.crd.dto.DTOTCountry;
import sk.ditec.crd.ws.Company;
import sk.ditec.crd.ws.CompanyReplicationRequest;
import sk.ditec.crd.ws.CompanyReplicationResponse;
import sk.ditec.crd.ws.Country;
import sk.ditec.crd.ws.CountryReplicationResponse;
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
import sk.ditec.process.BaseProcess;
import sk.ditec.zsr.common.server._NovyPISBaseClass;
import sk.ditec.zsr.common.server.utils.DateUtils;

public class SpracujCrdCompanyClass extends _NovyPISBaseClass {
	private _CudCrdDelegate dlg = new _CudCrdDelegate();
	private _CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private CudPauClass cudPau = new CudPauClass();
	private Logger log = LoggerFactory.getLogger(CudCrdProcess.class);

	public ActionResult spracujCompany(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			BaseProcess caller) throws AppException {
		ActionResult actRes = new ActionResult();

		try {

			// 2 /*CompanyReplicationRequest */
			// Systém vytvorí vstupnú èas správy CompanyReplicationRequest a ReplicationVolume:
			// DateFilterForDeletedRecords = vst. datumPoslednehoSpracovania
			// ReplicateFromDate = vst. datumPoslednehoSpracovania
			// ReplicateAll -nesme ist do volania
			// (žiada sa o všetky pridané, zmenené a zmazané dáta od poslednej aktualizácie)

			CompanyReplicationRequest replicationRequest = new CompanyReplicationRequest();
			ReplicationVolume volume = new ReplicationVolume();
			volume.setReplicateFromDate(datumPoslSprac);
			replicationRequest.setReplicationVolume(volume);


			// 6. Systém prijme výstupnú èas správy
			CompanyReplicationResponse replicationResponse = dlg.getCrdWS().companyReplication(replicationRequest);

			Integer pocet = 0;
			if (replicationResponse != null) {
				pocet = replicationResponse.getCompanyLength();
			}
			String popisSpracovania = "COMPANY počet záznamov na spracovanie " + pocet;
			String stringRequest = CudVysielanieUtils.marshal(replicationRequest);
			String stringResponse = CudVysielanieUtils.marshal(replicationResponse);
			DTOCrdSpracTabuliek dtosprac = new DTOCrdSpracTabuliek();
			dtosprac.setIdCrdSpracovanie(spracovanieTabuliekId);
			dtosprac.setIdCiselnik(spracovanieTabuliekId);
			dtosprac.setVstupneXml(stringRequest);
			dtosprac.setVystupneXml(stringResponse);
			dtosprac.setPopisSpracovania(popisSpracovania);
			dtosprac.setNavratovyKod(0);
			dtosprac.setReplicateFromDate(datumPoslSprac);
			dtosprac.setReplicateAll("Y");

			dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().update(auth, dtosprac).getResult();

			return updateCompany(auth, datumPoslSprac, spracovanieTabuliekId, stringRequest,

			replicationResponse, caller);

		} catch (Throwable e) {
			handleException(e, "SpracujCrdCountryClass.spracujCountry.error", auth);
			actRes.setError(true);
			actRes.setKeyErrorMsg(e.getMessage());
			return actRes;
		}
	}

	public ActionResult updateCompany(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			String stringVstupneXml, CompanyReplicationResponse replicationResponse, BaseProcess caller)
			throws AppException {
		ActionResult actRes = new ActionResult();
		String chyba = "";
		String spracovane = "";
		String bezZmien = "";
		Integer navratovyKod = 0; // nie su zaznamy na spracovanie
		Integer pocet = null;
		if (replicationResponse != null) {
			pocet = replicationResponse.getCompanyLength();
		}
		String popisSpracovania = "COMPANY počet záznamov na spracovanie " + pocet;
		try {

			String tabulka = "T_COMPANY";
			// nacitam ciselnik
			DTOCiselnik dtoCis = dlgcud.getCiselnikRead().readLight(auth, tabulka);
			if (!StringUtils.isValid(dtoCis)) {
				System.out.println(tabulka + " sa nenachaza v zozname ciselnikov!");
				// tu osetrenie
			}


			// 8.Systém nastaví pomocnú premennú bolaZmena = False
			boolean bolaZmena = false;

			// 9.Ak je záznam vo výstupnej èasti správy Company alebo je hodnota v deletedCompany

			// anika tej podmienke uplne nechapem asi ked nie res null
			// DTOImportZmena[] importZmenaList = new DTOImportZmena[1];
			DTOImportZmena dtoZmena = new DTOImportZmena();
			DTOImport dtoImport = new DTOImport();
			dtoImport.setIDCiselnik(dtoCis.getCiselnikID());
			dtoImport.setCiselnikTabulka(tabulka);
			dtoImport.setStav("CRD");

			if (replicationResponse != null && replicationResponse.getCompany() != null
					&& replicationResponse.getCompany().length > 0) {
				// 9.1. Pre každý záznam zo sekcie Company z výstupnej správy
				navratovyKod = 1; // existuju zaznamy na spracovanie
				popisSpracovania = "";
				for (Company wsCompany : replicationResponse.getCompany()) {
					if (!caller.statusOKnotify()) { // prerusenie processu
						return actRes;
					}
					bolaZmena = false;
					DTOCrdSpracTabuliek dtosprac = new DTOCrdSpracTabuliek();
					Map<String, String> rowMap = new HashMap<String, String>();
					String spracovanyZaznam = "CompanyUICCode=" + wsCompany.getCompanyUICCode();
					dtosprac.setZmenoveXmlVstup(CudVysielanieUtils.marshal(wsCompany));
					dtosprac.setNavratovyKod(navratovyKod);
					dtosprac.setReplicateAll("N");
					dtosprac.setIdCiselnik(dtoCis.getCiselnikID());
					dtosprac.setReplicateFromDate(datumPoslSprac);
					dtosprac.setIdCrdSpracovanie(spracovanieTabuliekId);
					dtosprac.setPopisSpracovania(" COMPANY spracovávam záznam " + spracovanyZaznam);
					dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().update(auth, dtosprac).getResult();
					// 9.1.1. /*tCountryCud = T_COUNTRY*/
					// Systém vyhľadá záznam o krajine k primary location
					// neberie sa max HIS_ID ale PLATNOST_DO is null

					dtoZmena.setPlatnostOd(new Date());
					DTOTCountry tCountry = dlg.getTCudCiselnikyClass().getCountryByIso(auth,
							wsCompany.getCountry().getCountryCodeISO().getValue());
					if (tCountry == null || tCountry.getCountryID() == null) {
						CountryReplicationResponse countryReplicationResponse = new CountryReplicationResponse();
						countryReplicationResponse.setCountry(new Country[] { wsCompany.getCountry() });

						dlg.getSpracujCrdCountryClass().updateCountry(auth, datumPoslSprac, spracovanieTabuliekId,
								stringVstupneXml, countryReplicationResponse, caller);

						tCountry = dlg.getTCudCiselnikyClass().getCountryByIso(auth,
								wsCompany.getCountry().getCountryCodeISO().getValue());
						if (tCountry == null || tCountry.getCountryID() == null) {
							// Systém nastaví :
							// navratovyKod = 3
							// popisSpracovania = "Chyba - nebol nájdený záznam väzobného èíselníka."
							navratovyKod = 3;
							popisSpracovania += "Chyba - nebol nájdený záznam väzobného číselníka  Country k Company "
									+ wsCompany.getCountry().getCountryCodeISO();
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
					// 9.1.2 /*tAllocationCompanyCud = T_COMPANY*/
					// Systém vyhľadá záznam o spoločnosti k subsidiary location
					// volanie rozšírené o parameter startValidity
					// neberie sa max HIS_ID ale PLATNOST_DO is null
					ArrayList<DTOTCompany> tCompanyList = dlg.getTCudCiselnikyClass().getCompanyList(auth,
							wsCompany.getCompanyUICCode());
					DTOTCompany tCompany = getCompanyBystartValidity(tCompanyList, wsCompany.getStartValidity());
					// ZNEAKTIVNY AK SU NOVSIE
					if (wsCompany.getEndValidity() == null) {
						// Pre každý deaktivácia z deaktiváciaList
						ArrayList<DTOTCompany> deaktList = getCompanyForDeakt(tCompanyList,
								wsCompany.getStartValidity());
						for (DTOTCompany dto : deaktList) {
							// Systém vytvorí výstupnú èas správy Zaznamy a naviaže ju na ZmenaHodnotCiselnika, prièom
							// ID =deaktivacia.COMPANY_ID
							// operacia = "U"
							// platnostOd = aktualny dátum a èas
							// datumSchvalenia = Primary_Location.Modified_Date

							Map<String, String> rowMapOld = new HashMap<String, String>();
							rowMapOld.put("COMPANY_ID", dto.getCompanyID().toString());
							rowMapOld.put("XLS_OPERACIA", "U");
							rowMapOld.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(new Date()));
							rowMapOld.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));
							rowMapOld.put("ACTIVE_FLAG", "F");
							dtosprac.setZmenoveXmlVystup(rowMapOld.toString());
							dtosprac.setPopisSpracovania(spracovanyZaznam + " ACTIVE_FLAG=F");
							dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
									.updateANulujZmenoveXML(auth, dtosprac).getResult();

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

						}
					}

					DTOTCompany oldCompany = getCompanyOld(tCompanyList, wsCompany.getStartValidity());

					if (oldCompany != null) {
						Map<String, String> rowMapOld = new HashMap<String, String>();
						rowMapOld.put("COMPANY_ID", oldCompany.getCompanyID().toString());
						rowMapOld.put("XLS_OPERACIA", "U");
						rowMapOld.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(new Date()));
						rowMapOld.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));

						rowMapOld.put("END_VALIDITY",
								CudVysielanieUtils.getStringDatum(DateUtils.plusDay(wsCompany.getStartValidity(), -1)));
						dtosprac.setZmenoveXmlVystup(rowMapOld.toString());
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
								.updateANulujZmenoveXML(auth, dtosprac).getResult();
						// zrus stare zaznamy
						DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, new Date(), null,
								null);
						String stringRowMap = rowMapOld.toString();
						dtosprac.setZmenoveXmlVystup(stringRowMap);
						Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();
						List<DTOCiselnikStlpec> csList = dlgcud.getCiselnikStlpecRead().listLight(auth,
								dtoImport.getIDCiselnik());
						dlgcud.getValidation().validateMaster(auth, dtoVal, metaMap, rowMapOld, csList);

						if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
							navratovyKod = 4;
							for (DTOImportMsg dto : dtoVal.getImportZmenaDTO().getImportMsgList()) {
								chyba += dto.getMsg();
								popisSpracovania += dto.getMsg();
							}
							chyba += rowMap.toString();
							log.info(chyba);
						}
						dtosprac.setZmenoveXmlVystup(rowMapOld.toString());
						dtosprac.setKod("0");
						dtosprac.setPopisSpracovania(spracovanyZaznam + " Deactivacia ");
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
								.updateANulujZmenoveXML(auth, dtosprac).getResult();
						try {
							dlg.getCrdSpracovanieClass().zapisLokCezZmenoveProcesy(auth, rowMapOld, dtoImport,
									navratovyKod, popisSpracovania);
						} catch (Throwable e) {
							log.error(popisSpracovania + e);
							dtosprac.setKod("1");
							dtosprac.setPopisSpracovania(spracovanyZaznam + " CHYBA:" + e.getMessage());
							dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass()
									.updateANulujZmenoveXML(auth, dtosprac).getResult();
						}

					}

					if (tCompany == null) {
						// ked nebol najdeny, zisti, ci existuju platne zaznamy a ukonci ich

						dtoZmena.setOperacia("N");
						rowMap.put("COMPANY_UIC_CODE", wsCompany.getCompanyUICCode());
						rowMap.put("COMPANY_NAME", CudVysielanieUtils.refactorApostrof(wsCompany.getCompanyName()));
						rowMap.put("COMPANY_NAME_ASCI", wsCompany.getCompanyNameASCII());
						rowMap.put("COMPANY_SHORT_NAME",
								CudVysielanieUtils.refactorApostrof(wsCompany.getCompanyShortName()));
						rowMap.put("COMPANY_URL", wsCompany.getCompanyURL());
						rowMap.put("FREE_TEXT", CudVysielanieUtils.refactorApostrof(wsCompany.getFreeText()));
						rowMap.put("ID_COUNTRY", tCountry.getCountryID().toString());

						rowMap.put("ADDRESS",
								CudVysielanieUtils.refactorApostrof(wsCompany.getContactDetails().getAddress()));
						rowMap.put("CITY", CudVysielanieUtils.refactorApostrof(wsCompany.getContactDetails().getCity()));
						rowMap.put("CONTACT_PERSON",
								CudVysielanieUtils.refactorApostrof(wsCompany.getContactDetails().getContactPerson()));
						rowMap.put("EMAIL", wsCompany.getContactDetails().getEmail());
						rowMap.put("FAX_NUMBER", wsCompany.getContactDetails().getFAXNumber());
						rowMap.put("MOBILE_NUMBER", wsCompany.getContactDetails().getMobileNumber());
						rowMap.put("PHONE_NUMBER", wsCompany.getContactDetails().getPhoneNumber());
						rowMap.put("POSTAL_CODE", wsCompany.getContactDetails().getPostalCode());

						rowMap.put("XLS_OPERACIA", "N");
						rowMap.put("INFRASTRUCTURE_FLAG", (wsCompany.isInfrastructureFlag() ? "T" : "F"));
						rowMap.put("OTHER_COMPANY_FLAG", (wsCompany.isOtherCompanyFlag() ? "T" : "F"));
						rowMap.put("FREIGHT_FLAG", (wsCompany.isFreightFlag() ? "T" : "F"));
						rowMap.put("PASSENGER_FLAG", (wsCompany.isPassengerFlag() ? "T" : "F"));
						rowMap.put("NE_ENTITY_FLAG", (wsCompany.isNEEntityFlag() ? "T" : "F"));
						rowMap.put("CE_ENTITY_FLAG", (wsCompany.isCEEntityFlag() ? "T" : "F"));
						rowMap.put("ACTIVE_FLAG", "T");

						dtoZmena.setOperacia("N");
						bolaZmena = true;
					} else { // update
						rowMap.put("COMPANY_ID", tCompany.getCompanyID().toString());
						rowMap.put("COUNTRY_ID", tCountry.getCountryID().toString());
						if (!CudVysielanieUtils.isEqual(wsCompany.getCompanyUICCode(), tCompany.getCompanyUicCode())) {
							bolaZmena = true;
							rowMap.put("COMPANY_UIC_CODE", wsCompany.getCompanyUICCode());
						}
						if (!"T".equals(tCompany.getActiveFlag())) {
							bolaZmena = true;
							rowMap.put("ACTIVE_FLAG", "T");
						}
						if (!CudVysielanieUtils.isEqual(wsCompany.isInfrastructureFlag(),
								tCompany.getInfrastructureFlag())) {
							bolaZmena = true;
							rowMap.put("INFRASTRUCTURE_FLAG", (wsCompany.isInfrastructureFlag() ? "T" : "F"));
						}

						if (!CudVysielanieUtils.isEqual(wsCompany.isOtherCompanyFlag(), tCompany.getOtherCompanyFlag())) {
							bolaZmena = true;
							rowMap.put("OTHER_COMPANY_FLAG", (wsCompany.isOtherCompanyFlag() ? "T" : "F"));
						}
						if (!CudVysielanieUtils.isEqual(wsCompany.isFreightFlag(), tCompany.getFreightFlag())) {
							bolaZmena = true;
							rowMap.put("FREIGHT_FLAG", (wsCompany.isFreightFlag() ? "T" : "F"));
						}
						if (!CudVysielanieUtils.isEqual(wsCompany.isPassengerFlag(), tCompany.getPassengerFlag())) {
							bolaZmena = true;
							rowMap.put("PASSENGER_FLAG", (wsCompany.isPassengerFlag() ? "T" : "F"));
						}
						if (!CudVysielanieUtils.isEqual(wsCompany.isNEEntityFlag(), tCompany.getNeEntityFlag())) {
							bolaZmena = true;
							rowMap.put("NE_ENTITY_FLAG", (wsCompany.isNEEntityFlag() ? "T" : "F"));
						}
						if (!CudVysielanieUtils.isEqual(wsCompany.isCEEntityFlag(), tCompany.getCeEntityFlag())) {
							bolaZmena = true;
							rowMap.put("CE_ENTITY_FLAG", (wsCompany.isCEEntityFlag() ? "T" : "F"));
						}
						// rowMap.put("ACTIVE_FLAG", "T"); nemeni sa

						// rowMap.put("COMPANY_NAME", wsCompany.getCompanyName());
						if (!CudVysielanieUtils.isEqual(wsCompany.getCompanyName(), tCompany.getCompanyName())) {
							bolaZmena = true;
							rowMap.put("COMPANY_NAME", CudVysielanieUtils.refactorApostrof(wsCompany.getCompanyName()));
						}
						// rowMap.put("COMPANY_NAME_ASCI", wsCompany.getCompanyNameASCII());
						if (!CudVysielanieUtils
								.isEqual(wsCompany.getCompanyNameASCII(), tCompany.getCompanyNameAscii())) {
							bolaZmena = true;
							rowMap.put("COMPANY_NAME_ASCI", wsCompany.getCompanyNameASCII());
						}
						// rowMap.put("COMPANY_SHORT_NAME", wsCompany.getCompanyShortName());
						if (!CudVysielanieUtils
								.isEqual(wsCompany.getCompanyShortName(), tCompany.getCompanyShortName())) {
							bolaZmena = true;
							rowMap.put("COMPANY_SHORT_NAME",
									CudVysielanieUtils.refactorApostrof(wsCompany.getCompanyShortName()));
						}
						// rowMap.put("COMPANY_URL", wsCompany.getCompanyURL());
						if (!CudVysielanieUtils.isEqual(wsCompany.getCompanyURL(), tCompany.getCompanyUrl())) {
							bolaZmena = true;
							rowMap.put("COMPANY_URL", wsCompany.getCompanyURL());
						}
						// rowMap.put("FREE_TEXT", wsCompany.getFreeText());
						if (!CudVysielanieUtils.isEqual(wsCompany.getFreeText(),
								CudVysielanieUtils.refactorApostrof(tCompany.getFreeText()))) {
							bolaZmena = true;
							rowMap.put("FREE_TEXT", wsCompany.getFreeText());
						}
						// rowMap.put("ID_COUNTRY", tCountry.getCountryID().toString());
						if (!CudVysielanieUtils.isEqual(tCountry.getCountryID().toString(), tCountry.getCountryID()
								.toString())) {
							bolaZmena = true;
							rowMap.put("ID_COUNTRY", tCountry.getCountryID().toString());
						}

						// rowMap.put("ADDRESS", wsCompany.getContactDetails().getAddress());
						if (!CudVysielanieUtils.isEqual(wsCompany.getContactDetails().getAddress(),
								tCompany.getAddress())) {
							bolaZmena = true;
							rowMap.put("ADDRESS",
									CudVysielanieUtils.refactorApostrof(wsCompany.getContactDetails().getAddress()));
						}
						// rowMap.put("CITY", wsCompany.getContactDetails().getCity());
						if (!CudVysielanieUtils.isEqual(wsCompany.getContactDetails().getCity(), tCompany.getCity())) {
							bolaZmena = true;
							rowMap.put("CITY",
									CudVysielanieUtils.refactorApostrof(wsCompany.getContactDetails().getCity()));
						}
						// rowMap.put("CONTACT_PERSON", wsCompany.getContactDetails().getContactPerson());
						if (!CudVysielanieUtils.isEqual(wsCompany.getContactDetails().getContactPerson(),
								tCompany.getContactPerson())) {
							bolaZmena = true;
							rowMap.put("CONTACT_PERSON", CudVysielanieUtils.refactorApostrof(wsCompany
									.getContactDetails().getContactPerson()));
						}
						// rowMap.put("EMAIL", wsCompany.getContactDetails().getEmail());
						if (!CudVysielanieUtils.isEqual(wsCompany.getContactDetails().getEmail(), tCompany.getEmail())) {
							bolaZmena = true;
							rowMap.put("EMAIL", wsCompany.getContactDetails().getEmail());
						}
						// rowMap.put("FAX_NUMBER", wsCompany.getContactDetails().getFAXNumber());
						if (!CudVysielanieUtils.isEqual(wsCompany.getContactDetails().getFAXNumber(),
								tCompany.getFaxNumber())) {
							bolaZmena = true;
							rowMap.put("FAX_NUMBER", wsCompany.getContactDetails().getFAXNumber());
						}
						// rowMap.put("MOBILE_NUMBER", wsCompany.getContactDetails().getMobileNumber());
						if (!CudVysielanieUtils.isEqual(wsCompany.getContactDetails().getMobileNumber(),
								tCompany.getMobileNumber())) {
							bolaZmena = true;
							rowMap.put("MOBILE_NUMBER", wsCompany.getContactDetails().getMobileNumber());
						}
						// rowMap.put("PHONE_NUMBER", wsCompany.getContactDetails().getPhoneNumber());
						if (!CudVysielanieUtils.isEqual(wsCompany.getContactDetails().getPhoneNumber(),
								tCompany.getPhoneNumber())) {
							bolaZmena = true;
							rowMap.put("PHONE_NUMBER", wsCompany.getContactDetails().getPhoneNumber());
						}
						// rowMap.put("POSTAL_CODE", wsCompany.getContactDetails().getPostalCode());
						if (!CudVysielanieUtils.isEqual(wsCompany.getContactDetails().getPostalCode(),
								tCompany.getPostalCode())) {
							bolaZmena = true;
							rowMap.put("POSTAL_CODE", wsCompany.getContactDetails().getPostalCode());
						}
						rowMap.put("XLS_OPERACIA", "U");
						if (bolaZmena) {
							dtoZmena.setOperacia("U");
						}
					}
					if (!bolaZmena) {
						bezZmien += "Company=" + wsCompany.toString() + " ; ";
						dtosprac.setPopisSpracovania("Bez zmien " + spracovanyZaznam);
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().updateANuluj(auth, dtosprac)
								.getResult();
						continue;
					}

					if (bolaZmena) {
						Date $vlozDatum = new Date();
						// if (bolaZmena) {
						// $vlozDatum = CudVysielanieUtils.getMax(wsCompany.getModifiedDate(), $vlozDatum);
						// } else {
						// $vlozDatum = CudVysielanieUtils.getMax(wsCompany.getAddDate(), $vlozDatum);
						// }
						rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum($vlozDatum));
						// wsCompany.getModifiedDate()
						rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));

						// Systém vytvorí výstupnú èas správy Stlpce a naviaže ju na Zaznamy, prièom
						// nazovStlpca =END_VALIDITY
						// novaHodnota = tCompanyCud.START_VALIDITY-1
						// staraHodnota=tCompanyCud..END_VALIDITY

						if (wsCompany.getEndValidity() != null) {
							rowMap.put("END_VALIDITY", CudVysielanieUtils.getStringDatum(wsCompany.getEndValidity()));
						}
						if (wsCompany.getStartValidity() != null) {
							rowMap.put("START_VALIDITY",
									CudVysielanieUtils.getStringDatum(wsCompany.getStartValidity()));
						}
					}

					// ///////////////////////////////////////////////////////////////////////////////////////

					// importZmenaList[0] = dtoZmena;
					String stringRowMap = rowMap.toString();
					dtosprac.setZmenoveXmlVystup(stringRowMap);
					DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, new Date(), null, null);
					boolean errors = false;
					// DTOZmena dtoZmenaZap = null;

					Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();
					List<DTOCiselnikStlpec> csList = dlgcud.getCiselnikStlpecRead().listLight(auth,
							dtoImport.getIDCiselnik());
					dlgcud.getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);

					if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
						errors = true;
						navratovyKod = 4;

						for (DTOImportMsg dto : dtoVal.getImportZmenaDTO().getImportMsgList()) {
							chyba += dto.getMsg();
						}
						chyba += rowMap.toString();
						dtosprac.setPopisSpracovania(spracovanyZaznam + chyba);
						dtosprac.setReplicateAll("N");
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
							DTOZmena dtoZmenaZap = (DTOZmena) res.getResult();
							// 10.
							spracovane += "CompanyUICCode=" + wsCompany.getCompanyUICCode() + " ; ";
							spracovane += spracovanyZaznam + " ; ";
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
				} // end for

			} // end if

			actRes.setResult(navratovyKod);
			if (chyba.length() > 0) {
				actRes.setError(true);
				actRes.setKeyErrorMsg(chyba);
				return actRes;
			}
			return actRes;

		} catch (Throwable e) {
			handleException(e, "SpracujCrdCountryClass.updateCompany.error", auth);
			actRes.setError(true);
			actRes.setKeyErrorMsg(e.getMessage());
			return actRes;
		}
	}

	private DTOTCompany getCompanyBystartValidity(ArrayList<DTOTCompany> list, Date startValidity) {
		// " AND pl.START_VALIDITY =" + CudVysielanieUtils.dateCritFormat(startValidity);

		for (DTOTCompany dto : list) {
			if (CudVysielanieUtils.isEqual(startValidity, dto.getStartValidity())) {
				return dto;
			}
		}
		return null;
	}

	private DTOTCompany getCompanyOld(ArrayList<DTOTCompany> list, Date startValidity) {
		// sql += " AND pl.START_VALIDITY < " + CudVysielanieUtils.dateCritFormat(startValidity) + " AND ("
		// + " pl.END_VALIDITY IS NULL " + "  OR pl.END_VALIDITY >= "
		// + CudVysielanieUtils.dateCritFormat(startValidity) + " )";
		Long startValidityTime = (startValidity == null ? 0 : startValidity.getTime());
		for (DTOTCompany dto : list) {
			Long dtostartValidityTime = (dto.getStartValidity() == null ? 0 : dto.getStartValidity().getTime());
			Long dtoendValidityTime = (dto.getEndValidity() == null ? 0 : dto.getEndValidity().getTime());
			if (dtostartValidityTime < startValidityTime
					&& (dtoendValidityTime == 0 || dtoendValidityTime >= startValidityTime)) {
				return dto;
			}
		}
		return null;
	}

	private ArrayList<DTOTCompany> getCompanyForDeakt(ArrayList<DTOTCompany> list, Date startValidity) {
		// .START_VALIDITY > vst. StartValidity a zároveň
		// .ACTIVE_FLAG=T a zároveň
		Long startValidityTime = (startValidity == null ? 0 : startValidity.getTime());
		ArrayList<DTOTCompany> listnew = new ArrayList<DTOTCompany>();
		for (DTOTCompany dto : list) {
			Long dtostartValidityTime = (dto.getStartValidity() == null ? 0 : dto.getStartValidity().getTime());
			if ("T".equals(dto.getActiveFlag()) && dtostartValidityTime > startValidityTime) {
				listnew.add(dto);
			}
		}
		return listnew;
	}

}