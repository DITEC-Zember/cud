package sk.ditec.crd;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.dto.DTOCrdSpracTabuliek;
import sk.ditec.crd.dto.DTOTSubsidiaryType;
import sk.ditec.crd.ws.ReplicationVolume;
import sk.ditec.crd.ws.SubsidiaryType;
import sk.ditec.crd.ws.SubsidiaryTypeReplicationRequest;
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
import sk.ditec.cud.proc.CudPauClass;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.process.BaseProcess;
import sk.ditec.zsr.common.server._NovyPISBaseClass;




public class SpracujCrdSubsidiaryTypeClass extends _NovyPISBaseClass {
	private _CudCrdDelegate dlg = new _CudCrdDelegate();
	private _CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private CudPauClass cudPau = new CudPauClass();

	public ActionResult spracujSubsidiaryType(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			BaseProcess caller)
			throws AppException {
		ActionResult actRes = new ActionResult();
		// 2. /* T_SUBSIDIARY_TYPE */
		// Systém vytvorí vstupnú èas správyReplicationRequest a ReplicationVolume:
		// DateFilterDeletedRecords = vst. datumPoslednehoSpracovania
		// ReplicateFromDate = vst. datumPoslednehoSpracovania
		// ReplicateAll -nesme ist do volania
		// (žiada sa o všetky pridané, zmenené a zmazané dáta od poslednej aktualizácie)
		SubsidiaryTypeReplicationRequest replicationRequest = new SubsidiaryTypeReplicationRequest();
		ReplicationVolume volume = new ReplicationVolume();
		volume.setReplicateFromDate(datumPoslSprac);
		replicationRequest.setReplicationVolume(volume);
		String stringRequest = CudVysielanieUtils.marshal(replicationRequest);
		try {
			// 6. Systém prijme výstupnú èas správy - CountryReplicationResponse
			SubsidiaryTypeReplicationResponse replicationResponse // = new CountryReplicationResponse();
			= dlg.getCrdWS().subsidiaryTypeReplication(replicationRequest);

			// 7.Systém zapíše do údajov o výsledku spracovania výstupné XML
			// Systém aktualizuje údaje v CUD_SPRACOVANIE_TABULIEK:
			// .VYSTUPNE_XML= vst. XML
			// kde .SPRACOVANIE_TABULIEK_ID = vst. SpracovanieTabuliekID
			// DTOCrdSpracTabuliek dtoSprac = new DTOCrdSpracTabuliek();
			// dtoSprac.setVystupneXml(JaxbUtils.marshal(countryReplicationResponse));
			// dtoSprac.setIdCrdSpracovanie(spracovanieTabuliekId);
			int pocet = 0;
			if (replicationResponse != null) {
				pocet = replicationResponse.getSubsidiaryTypeLength();
			}
			String popisSpracovania = "SUBSIDIARY_TYPE počet záznamov na spracovanie " + pocet;
			String stringResponse = CudVysielanieUtils.marshal(replicationResponse);
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

			return updateSubsidiaryType(auth, datumPoslSprac, spracovanieTabuliekId, stringRequest,
					replicationResponse, caller);
		} catch (Throwable e) {
			handleException(e, "spracujSubsidiaryTypeClass.spracujSubsidiaryType.error", auth);
			actRes.setError(true);
			actRes.setKeyErrorMsg(e.getMessage());
			return actRes;
		}
	}

	public ActionResult updateSubsidiaryType(AuthInfo auth, Date datumPoslSprac, Integer spracovanieTabuliekId,
			String stringRequest, SubsidiaryTypeReplicationResponse replicationResponse,
			BaseProcess caller) throws AppException {
		ActionResult actRes = new ActionResult();
		String chyba = "";
		String spracovane = "";
		String bezZmien = "";

		Integer navratovyKod = 0; // nie su zaznamy na spracovanie

		Integer pocet = null;
		if (replicationResponse != null) {
			pocet = replicationResponse.getSubsidiaryTypeLength();
		}
		String popisSpracovania = "SUBSIDIARY_TYPE počet záznamov na spracovanie " + pocet;
		try {
			


			String tabulka = "T_SUBSIDIARY_TYPE";
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
			


			if (replicationResponse != null && replicationResponse.getSubsidiaryType() != null
					&& replicationResponse.getSubsidiaryType().length > 0) {
				// 9.1. Pre každý záznam zo sekcie z výstupnej správy
				navratovyKod = 1; // existuju zaznamy na spracovanie
				popisSpracovania = "";
				for (SubsidiaryType wsSubType : replicationResponse.getSubsidiaryType()) {
					if (!caller.statusOKnotify()) { // koncim
						new ActionResult();
					}
					bolaZmena = false;
					Map<String, String> rowMap = new HashMap<String, String>();
					DTOCrdSpracTabuliek dtosprac = new DTOCrdSpracTabuliek();
					String spracovanyZaznam = "SubsidiaryTypeCode=" + wsSubType.getSubsidiaryTypeCode();
					dtosprac.setZmenoveXmlVstup(CudVysielanieUtils.marshal(wsSubType));
					dtosprac.setPopisSpracovania("spracovávam záznam " + spracovanyZaznam);
					dtosprac.setIdCrdSpracovanie(spracovanieTabuliekId);
					dtosprac.setIdCiselnik(dtoCis.getCiselnikID());
					dtosprac.setReplicateFromDate(datumPoslSprac);
					dtosprac.setReplicateAll("N");
					dtosprac.setNavratovyKod(navratovyKod);
					dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().update(auth, dtosprac).getResult();
					// 9.1.1. /*tSubType= T_SUBSIDIARY_TYPE*/
					// Systém vyh¾adá èi záznam zo vstupu je zapísaný v T_COUNTRY Vrát záznam pod¾a kódu
					DTOTSubsidiaryType tSubType = dlg.getTCudCiselnikyClass().getSubsidiaryType(auth,
							wsSubType.getSubsidiaryTypeCode());


					// 9.1.2 Ak záznam nebol nájdený - nový záznam
					dtoZmena.setPlatnostOd(new Date());
					if (tSubType == null) {
						// Systém vytvorí výstupnú èas správy Zaznamy a naviaže ju na ZmenaHodnotCiselnika, prièom
						// ID = NULL
						// operacia = "N"
						// platnostOd = Country.DateAdd
						// datumSchvalenia = Country.DateAdd
						// SUBSIDIARY_TYPE_ID
						// SUBSIDIARY_TYPE_CODE
						// SUBSIDIARY_TYPE_NAME
						// IM_FLAG
						// FREIGHT_RU_FLAG
						// PASSENGER_RU_FLAG
						// CENTRAL_ENTITY_FLAG
						// NATIONAL_ENTITY_FLAG
						// OTHERS_FLAG
						//

						dtoZmena.setOperacia("N");
						rowMap.put("SUBSIDIARY_TYPE_CODE", wsSubType.getSubsidiaryTypeCode());
						rowMap.put("SUBSIDIARY_TYPE_NAME", wsSubType.getSubsidiaryTypeName());
						rowMap.put("IM_FLAG", wsSubType.isIMFlag() ? "T" : "F");
						rowMap.put("FREIGHT_RU_FLAG", wsSubType.isFreightRUFlag() ? "T" : "F");
						rowMap.put("PASSENGER_RU_FLAG", wsSubType.isPassengerRUFlag() ? "T" : "F");
						rowMap.put("CENTRAL_ENTITY_FLAG", wsSubType.isCentralEntityFlag() ? "T" : "F");
						rowMap.put("NATIONAL_ENTITY_FLAG", wsSubType.isNationalEntityFlag() ? "T" : "F");
						rowMap.put("OTHERS_FLAG", wsSubType.isOthersFlag() ? "T" : "F");

						rowMap.put("FREE_TEXT", wsSubType.getFreeText());

						rowMap.put("XLS_OPERACIA", "N");
						bolaZmena = true;
					} else { // update
						rowMap.put("SUBSIDIARY_TYPE_ID", tSubType.getSubsidiaryTypeID().toString());
						if (!CudVysielanieUtils.isEqual(wsSubType.getSubsidiaryTypeCode(),
								tSubType.getSubsidiaryTypeCode())) {
							bolaZmena = true;
							rowMap.put("COUNTRY_CODE_ISO", wsSubType.getSubsidiaryTypeCode());
						}
						if (!CudVysielanieUtils.isEqual(wsSubType.getSubsidiaryTypeName(),
								tSubType.getSubsidiaryTypeName())) {
							bolaZmena = true;
							rowMap.put("SUBSIDIARY_TYPE_NAME", wsSubType.getSubsidiaryTypeName());
						}
						if (!CudVysielanieUtils.isEqual(wsSubType.isIMFlag(), tSubType.getImFlag())) {
							bolaZmena = true;
							rowMap.put("IM_FLAG", wsSubType.isFreightRUFlag() ? "T" : "F");
						}
						if (!CudVysielanieUtils.isEqual(wsSubType.isFreightRUFlag(), tSubType.getFreightRuFlag())) {
							bolaZmena = true;
							rowMap.put("FREIGHT_RU_FLAG", wsSubType.isFreightRUFlag() ? "T" : "F");
						}
						if (!CudVysielanieUtils.isEqual(wsSubType.isPassengerRUFlag(), tSubType.getPassengerRuFlag())) {
							bolaZmena = true;
							rowMap.put("PASSENGER_RU_FLAG", wsSubType.isPassengerRUFlag() ? "T" : "F");
						}
						if (!CudVysielanieUtils.isEqual(wsSubType.isCentralEntityFlag(),
								tSubType.getCentralEntityFlag())) {
							bolaZmena = true;
							rowMap.put("CENTRAL_ENTITY_FLAG", wsSubType.isCentralEntityFlag() ? "T" : "F");
						}
						if (!CudVysielanieUtils.isEqual(wsSubType.isNationalEntityFlag(),
								tSubType.getNationalEntityFlag())) {
							bolaZmena = true;
							rowMap.put("NATIONAL_ENTITY_FLAG", wsSubType.isNationalEntityFlag() ? "T" : "F");
						}
						if (!CudVysielanieUtils.isEqual(wsSubType.isOthersFlag(), tSubType.getOthersFlag())) {
							bolaZmena = true;
							rowMap.put("OTHERS_FLAG", wsSubType.isOthersFlag() ? "T" : "F");
						}
						if (!CudVysielanieUtils.isEqual(wsSubType.getFreeText(), tSubType.getFreeText())) {
							bolaZmena = true;
							rowMap.put("FREE_TEXT", wsSubType.getFreeText());
						}
						rowMap.put("XLS_OPERACIA", "U");
						if (bolaZmena) {
							dtoZmena.setOperacia("U");
						}
					}


						Date $vlozDatum = new Date();
					// if (bolaZmena) {
					// $vlozDatum = CudVysielanieUtils.getMax(wsSubType.getModifiedDate(), $vlozDatum);
					// } else {
					// $vlozDatum = CudVysielanieUtils.getMax(wsSubType.getAddDate(), $vlozDatum);
					// }
						rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum($vlozDatum));
						// wsCompany.getModifiedDate()
						rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(new Date()));


					// validate nema

					// ///////////////////////////////////////////////////////////////////////////////////////

					if (!bolaZmena) {
						bezZmien += "SubsidiaryTypeCode=" + wsSubType.getSubsidiaryTypeCode() + " ; ";
						dtosprac.setPopisSpracovania("Bez zmien " + spracovanyZaznam);
						dtosprac = (DTOCrdSpracTabuliek) dlg.getCrdSpracTabuliekClass().updateANuluj(auth, dtosprac)
								.getResult();
						continue;
					}

					importZmenaList[0] = dtoZmena;

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
						for (DTOImportMsg dto : dtoVal.getImportZmenaDTO().getImportMsgList()) {
							chyba += dto.getMsg();
							popisSpracovania += dto.getMsg();
						}
						chyba += rowMap.toString();
						popisSpracovania += rowMap.toString();
						chyba += rowMap.toString();
						dtosprac.setPopisSpracovania(spracovanyZaznam + chyba);
						dtosprac.setNavratovyKod(navratovyKod);
						dtosprac.setReplicateAll("N");
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
							// sendNotif(auth, dtoCis, dtoVal, dtoWf, wfDefList, metaMapSend, fkMetaMap);
							dtoZmenaZap = (DTOZmena) res.getResult();
							// 10.
							spracovane += "SubsidiaryTypeCode=" + wsSubType.getSubsidiaryTypeCode() + " ; ";
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

				} // end

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
			handleException(e, "spracujSubsidiaryTypeClass.updateSubsidiaryType.error", auth);
			actRes.setError(true);
			actRes.setKeyErrorMsg(e.getMessage());
			return actRes;
		}
	}



	

}
