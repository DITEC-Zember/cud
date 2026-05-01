package sk.ditec.crd;

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
import sk.ditec.crd.dto.DTOCrdSpracTabuliek;
import sk.ditec.crd.dto.DTOTCountry;
import sk.ditec.crd.ws.Country;
import sk.ditec.crd.ws.CountryReplicationRequest;
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




public class SpracujCrdCountryClass extends CudCrdProcess {
	private _CudCrdDelegate dlg = new _CudCrdDelegate();
	private _CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private CudPauClass cudPau = new CudPauClass();
	private boolean ukonci = true;
	private Logger log = LoggerFactory.getLogger(CudCrdProcess.class);

	public ActionResult spracujCountry(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			BaseProcess caller)
			throws AppException {
		ActionResult actRes = new ActionResult();

		try {
			
			// 2. /* CountryReplicationRequest */
			// Systém vytvorí vstupnú èas správy CountryReplicationRequest a ReplicationVolume:
			// DateFilterForDeletedRecords = vst. datumPoslednehoSpracovania
			// ReplicateFromDate = vst. datumPoslednehoSpracovania
			// ReplicateAll -nesme ist do volania
			// (žiada sa o všetky pridané, zmenené a zmazané dáta od poslednej aktualizácie)
			CountryReplicationRequest countryReplicationRequest = new CountryReplicationRequest();
			ReplicationVolume volume = new ReplicationVolume();
			volume.setReplicateFromDate(datumPoslSprac);
			countryReplicationRequest.setReplicationVolume(volume);
			String stringVstupXml = CudVysielanieUtils.marshal(countryReplicationRequest);
			// aktualizujCiselnik();

			// if (ukonci) {
			// return null;
			// }
			// 6. Systém prijme výstupnú èas správy - CountryReplicationResponse
			CountryReplicationResponse countryReplicationResponse //= new CountryReplicationResponse();
			 = dlg.getCrdWS().countryReplication(countryReplicationRequest);
			Integer pocet = 0;
			if (countryReplicationResponse != null) {
				pocet = countryReplicationResponse.getCountryLength();
			}
			String popisSpracovania = "COUNTRY počet záznamov na spracovanie " + pocet;
			String stringRequest = CudVysielanieUtils.marshal(countryReplicationRequest);
			String stringResponse = CudVysielanieUtils.marshal(countryReplicationResponse);
			DTOCrdSpracTabuliek dtosprac = new DTOCrdSpracTabuliek();
			dtosprac.setIdCrdSpracovanie(spracovanieTabuliekId);
			dtosprac.setIdCiselnik(spracovanieTabuliekId);
			dtosprac.setVstupneXml(stringRequest);
			dtosprac.setVystupneXml(stringResponse);
			dtosprac.setPopisSpracovania(popisSpracovania);
			dtosprac.setNavratovyKod(0);
			// dtosprac.setDateFilterForDeleted();
			dtosprac.setReplicateFromDate(datumPoslSprac);
			dtosprac.setReplicateAll("Y");

			dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().update(auth, dtosprac).getResult();

			dlg.getSpracujCrdCountryClass().updateCountry(auth, datumPoslSprac, spracovanieTabuliekId, "",
					countryReplicationResponse, caller);
			return actRes;
		} catch (Throwable e) {
			handleException(e, "SpracujCrdCountryClass.spracujCountry.error", auth);
			actRes.setError(true);
			actRes.setKeyErrorMsg(e.getMessage());
			return actRes;
		}
	}

	public ActionResult updateCountry(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			String stringVstupXml, CountryReplicationResponse countryReplicationResponse, BaseProcess caller)
			throws AppException {
		ActionResult actRes = new ActionResult();
		String chyba = "";
		
		Integer navratovyKod = 0; // nie su zaznamy na spracovanie

		try {


			String tabulka = "T_COUNTRY";
			// DTOCiselnik[] ciselnikList = null;
			// nacitam ciselnik
			DTOCiselnik dtoCis = dlgcud.getCiselnikRead().readLight(auth, tabulka);
			if (!StringUtils.isValid(dtoCis)) {
				chyba += tabulka + " sa nenachaza v zozname ciselnikov!";
				return actRes;
			}


			// 8.Systém nastaví pomocnú premennú bolaZmena = False
			boolean bolaZmena = false;
			//9.Ak je záznam vo výstupnej èasti správy Country alebo je vyplnené CountryReplicationResponse.deletedCountry
			// anika tej podmienke uplne nechapem asi ked nie res null
			DTOImportZmena[] importZmenaList = new DTOImportZmena[1];
			DTOImportZmena dtoZmena = new DTOImportZmena();
			DTOImport dtoImport = new DTOImport();
			dtoImport.setIDCiselnik(dtoCis.getCiselnikID());
			dtoImport.setCiselnikTabulka(tabulka);
			dtoImport.setStav("CRD");
			

			// Map<String, String> rowMap = new HashMap<String, String>();
			if (countryReplicationResponse != null && countryReplicationResponse.getCountry() != null
					&& countryReplicationResponse.getCountry().length > 0) {
				// 9.1. Pre každý záznam zo sekcie Country z výstupnej správy
				navratovyKod = 1; // existuju zaznamy na spracovanie
				
				for (Country wsCauntry : countryReplicationResponse.getCountry()) {
					if (!caller.statusOKnotify()) { // prerusenie processu
						return actRes;
					}
					bolaZmena = false;
					DTOCrdSpracTabuliek dtosprac = new DTOCrdSpracTabuliek();
					
					Map<String, String> rowMap = new HashMap<String, String>();
					String spracovanyZaznam = "CountryUICCode=" + wsCauntry.getCountryUICCode() + " ,"
							+ wsCauntry.getCountryNameEN();
					dtosprac.setZmenoveXmlVstup(CudVysielanieUtils.marshal(wsCauntry));
					dtosprac.setNavratovyKod(navratovyKod);
					dtosprac.setReplicateAll("N");
					dtosprac.setReplicateFromDate(datumPoslSprac);
					dtosprac.setIdCiselnik(dtoCis.getCiselnikID());
					dtosprac.setIdCrdSpracovanie(spracovanieTabuliekId);
					dtosprac.setPopisSpracovania("COUNTRY spracovávam záznam " + spracovanyZaznam);
					dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().update(auth, dtosprac).getResult();
					// 9.1.1. /*tCountryCud = T_COUNTRY*/
					// Systém vyh¾adá èi záznam zo vstupu je zapísaný v T_COUNTRY Vrát záznam pod¾a kódu
					DTOTCountry tCountry = dlg.getTCudCiselnikyClass().getCountryByIso(auth,
							wsCauntry.getCountryCodeISO().getValue());

					// 9.1.2 Ak záznam nebol nájdený - nový záznam
					dtoZmena.setPlatnostOd(new Date());
					if (tCountry == null) {
						// Systém vytvorí výstupnú èas správy Zaznamy a naviaže ju na ZmenaHodnotCiselnika, prièom
						// ID = NULL
						// operacia = "N"
						// platnostOd = Country.DateAdd
						// datumSchvalenia = Country.DateAdd

						dtoZmena.setOperacia("N");
						rowMap.put("COUNTRY_CODE_ISO", wsCauntry.getCountryCodeISO().getValue());
						rowMap.put("COUNTRY_NAME_DE", wsCauntry.getCountryNameDE());
						rowMap.put("COUNTRY_NAME_EN", wsCauntry.getCountryNameEN());
						rowMap.put("COUNTRY_NAME_FR", wsCauntry.getCountryNameFR());
						rowMap.put("COUNTRY_UIC_CODE", wsCauntry.getCountryUICCode());
						rowMap.put("SUB_LOC_CODE_FLAG", wsCauntry.isSubLocCodeFlag() ? "T" : "F");
						Date $vlozDatum = new Date(); // CudVysielanieUtils.getMax(wsCauntry.getAddDate(),
														// dtoZmena.getPlatnostOd());
						rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum($vlozDatum));
						rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));
						dtoZmena.setOperacia("N");
						bolaZmena = true;
					} else { //update
						rowMap.put("COUNTRY_ID", tCountry.getCountryID().toString());

						Date $vlozDatum = CudVysielanieUtils.getMax(wsCauntry.getModifiedDate(),
								dtoZmena.getPlatnostOd());
						rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum($vlozDatum));
						rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));

						if (!CudVysielanieUtils.isEqual(wsCauntry.getCountryCodeISO().getValue(),
								tCountry.getCountryCodeIso())) {
							bolaZmena = true;
							rowMap.put("COUNTRY_CODE_ISO", wsCauntry.getCountryCodeISO().getValue());
						}
						if (!CudVysielanieUtils.isEqual(wsCauntry.getCountryNameDE(), tCountry.getCountryNameDe())) {
							bolaZmena = true;
							rowMap.put("COUNTRY_NAME_DE", wsCauntry.getCountryNameDE());
						}
						if (!CudVysielanieUtils.isEqual(wsCauntry.getCountryNameEN(), tCountry.getCountryNameEn())) {
							bolaZmena = true;
							rowMap.put("COUNTRY_NAME_EN", wsCauntry.getCountryNameEN());
						}
						if (!CudVysielanieUtils.isEqual(wsCauntry.getCountryNameFR(), tCountry.getCountryNameFr())) {
							bolaZmena = true;
							rowMap.put("COUNTRY_NAME_FR", wsCauntry.getCountryNameFR());
						}
						if (!CudVysielanieUtils.isEqual(wsCauntry.getCountryUICCode(), tCountry.getCountryUicCode())) {
							bolaZmena = true;
							rowMap.put("COUNTRY_UIC_CODE", wsCauntry.getCountryUICCode());
						}

						if (!CudVysielanieUtils.isEqual(wsCauntry.isSubLocCodeFlag(), tCountry.getSubLocCodeFlag())) {
							bolaZmena = true;
							rowMap.put("SUB_LOC_CODE_FLAG", wsCauntry.isSubLocCodeFlag() ? "T" : "F");
						}
						rowMap.put("XLS_OPERACIA", "U");
						if (bolaZmena) {
							dtoZmena.setOperacia("U");
						}
					}
					if (!bolaZmena) {
						// bezZmien += "Country=" + wsCauntry.getCountryUICCode() + " ; ";
						dtosprac.setPopisSpracovania("Bez zmien " + spracovanyZaznam);
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().updateANuluj(auth, dtosprac)
								.getResult();
						continue;

					}


					// hodnoty validate - validity nema

					// ///////////////////////////////////////////////////////////////////////////////////////
			
			importZmenaList[0] = dtoZmena;
					String stringRowMap = rowMap.toString();
					dtosprac.setZmenoveXmlVystup(stringRowMap);

			DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, new Date(), null, null);
			boolean errors = false;
			DTOZmena dtoZmenaZap = null;

				Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();
				List<DTOCiselnikStlpec> csList = dlgcud.getCiselnikStlpecRead().listLight(auth,
						dtoImport.getIDCiselnik());
				dlgcud.getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);

				if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
					errors = true;
						navratovyKod = 4;
						String popisSpracovania = "";
						for (DTOImportMsg dto : dtoVal.getImportZmenaDTO().getImportMsgList()) {
						chyba += dto.getMsg();
							popisSpracovania += dto.getMsg();
					}
						chyba += rowMap.toString();
						popisSpracovania += rowMap.toString();
						dtosprac.setPopisSpracovania(spracovanyZaznam + chyba);
						dtosprac.setNavratovyKod(navratovyKod);
						dtosprac.setReplicateAll("N");
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().updateANuluj(auth, dtosprac)
								.getResult();
						continue;

				}



				Map<Integer, DTOUcet[]> ucetMap = new HashMap<Integer, DTOUcet[]>();

				List<DTOWfDef> wfDefList = dlgcud.getWfDefRead().list(auth, dtoImport.getIDCiselnik());

				Integer totalCount = dlgcud.getImportZmenaRead().pocet(auth, dtoImport.getImportID());
					String popisSpracovania = "";
					if (StringUtils.isValid(dtoImport.getIDCiselnik())) {

						DTOWorkflow dtoWf = dlgcud.getWorkflow().generujWorkflowAll(auth, dtoImport.getIDCiselnik(),
								dtoVal.getImportZmenaDTO(), wfDefList, ucetMap);
						if (StringUtils.isValid(dtoWf)) {
							ActionResult res = cudPau.workflowUpdateCrd(auth, dtoWf, dtoVal.getImportZmenaDTO(),
									datumPoslSprac);
							// sendNotif(auth, dtoCis, dtoVal, dtoWf, wfDefList, metaMapForSend, fkMetaMap);
							dtoZmenaZap = (DTOZmena) res.getResult();
							// 10.
							// spracovane += "CountrUICCode=" + wsCauntry.getCountryUICCode() + " ; ";
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
			handleException(e, "SpracujCrdCountryClass.updateCountry.error", auth);
			actRes.setError(true);
			actRes.setKeyErrorMsg(e.getMessage());
			return actRes;
		}
	}



	

}
