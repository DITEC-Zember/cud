package sk.ditec.cud.meta.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.cud.bi._CudBaseClass;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.dto.DTOPreklad;
import sk.ditec.cud.dto.DTOPrekladJazyk;
import sk.ditec.cud.dto.DTOPrekladStlpec;
import sk.ditec.cud.meta.ws.dto.DTOCiselnikStlpecMetaWS;
import sk.ditec.cud.meta.ws.dto.DTOCiselnikWS;
import sk.ditec.cud.meta.ws.dto.DTOOpravneniaListResponse;
import sk.ditec.cud.meta.ws.dto.DTOOpravnenieAtributWS;
import sk.ditec.cud.meta.ws.dto.DTOOpravnenieWS;
import sk.ditec.cud.meta.ws.dto.DTOPrekladWS;
import sk.ditec.cud.meta.ws.dto.DTOUpdCiselnikMetaResponse;
import sk.ditec.cud.meta.ws.dto.DTOUpdCiselnikMetaWS;
import sk.ditec.cud.meta.ws.dto.DTOUpdCiselnikWS;
import sk.ditec.cud.meta.ws.dto.DTOUpdStlpecWS;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.zsr.common.server.auth.ZSRAuthInfo;

public class CudMetaWSClass extends _CudBaseClass {

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	public static final String BOOL_FALSE = "F";
	public static final String BOOL_TRUE = "T";
	public static final int TABULKA_CISELNIK = 1;
	public static final String TABULKA_CISELNIK_NAZOV_DB = "CUD_CISELNIK";
	public static final int TABULKA_CISELNIK_STLPEC = 2;
	public static final String TABULKA_CISELNIK_STLPEC_NAZOV_DB = "CUD_CISELNIK_STLPEC";

	public DTOUpdCiselnikMetaResponse updCiselnikMeta(ZSRAuthInfo auth, DTOUpdCiselnikMetaWS dtoUpdCiselnikMetaWS) throws AppException {

		startTransaction(auth, "CUDWSMIDMetaSprava");

		try {
			getConnection(auth);

			DTOUpdCiselnikMetaResponse result = new DTOUpdCiselnikMetaResponse();

			// 2 - deaktivuje ciselnik a stlpce
			if (dtoUpdCiselnikMetaWS.getDtoDelCiselnikWS() != null && dtoUpdCiselnikMetaWS.getDtoDelCiselnikWS().getTabulka() != null)
				for (String tabulka : dtoUpdCiselnikMetaWS.getDtoDelCiselnikWS().getTabulka()) {
					DTOCiselnik ciselnik = dlg.getCiselnikRead().readLight(auth, tabulka);

					if (ciselnik == null || !_CudConsts.CISELNIK_TYP_INY.equals(ciselnik.getTyp())) {
						result.setSprava(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_201, tabulka));
						result.setKod(1);
						rollbackConnection(auth);
						endTransaction(auth, false);
						return result;
					}

					ciselnik.setAktivny(BOOL_FALSE);
					// 2.2
					dlg.getCiselnikModify().update(auth, ciselnik);

					DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
					dtoF.setIDCiselnik(ciselnik.getCiselnikID());
					dtoF.setAktivny(BOOL_TRUE);

					List<DTOCiselnikStlpec> ciselnikStlpecList = dlg.getCiselnikStlpecRead().listLight(auth, dtoF);

					// 2.3
					if (ciselnikStlpecList != null)
						for (DTOCiselnikStlpec stlpec : ciselnikStlpecList) {
							stlpec.setAktivny(BOOL_FALSE);
							dlg.getCiselnikStlpecModify().update(auth, stlpec);
						}
				}

			// 3
			if (dtoUpdCiselnikMetaWS.getDtoUpdCiselnikWS() != null)
				for (DTOUpdCiselnikWS updCiselnik : dtoUpdCiselnikMetaWS.getDtoUpdCiselnikWS()) {
					DTOCiselnik ciselnik = dlg.getCiselnikRead().readLight(auth, updCiselnik.getTabulka());
					if (ciselnik != null) {
						if (!_CudConsts.CISELNIK_TYP_INY.equals(ciselnik.getTyp())) {
							result.setSprava(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_201, updCiselnik.getTabulka()));
							result.setKod(1);
							rollbackConnection(auth);
							endTransaction(auth, false);
							return result;
						}

						// 3.1.1.2 aktualizacia ciselniku
						ciselnik.setAktivny(BOOL_TRUE);
						vytvorCiselnik(auth, updCiselnik, ciselnik);

						// vyhladaj stlpce pre vytvoreny ciselnik
						DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
						dtoF.setIDCiselnik(ciselnik.getCiselnikID());
						dtoF.setAktivny(BOOL_TRUE);
						// 3.1.1.3
						DTOCiselnikStlpec[] stlpceList = dlg.getCiselnikStlpecRead().list(auth, new Page(), dtoF);

						// stlpecNazov[] - delStlpec.Nazov
						if (updCiselnik.getDtoDelStlpecWS() != null && updCiselnik.getDtoDelStlpecWS().getNazov() != null)
							for (String stlpecNazov : updCiselnik.getDtoDelStlpecWS().getNazov()) {
								// 3.1.1.3.1.1 - deaktivuje stlpce kt sa idu menit
								DTOCiselnikStlpec najdenyStlpecKDeaktivovaniu = vyhladajStlpec(stlpceList, stlpecNazov);
								if (najdenyStlpecKDeaktivovaniu != null) {
									najdenyStlpecKDeaktivovaniu.setAktivny(BOOL_FALSE);
									dlg.getCiselnikStlpecModify().update(auth, najdenyStlpecKDeaktivovaniu);
								}
							}

						// 3.1.1.3.2
						if (updCiselnik.getDtoUpdStlpecWS() != null) {
							int poradie = 1;
							for (DTOUpdStlpecWS updStlpec : updCiselnik.getDtoUpdStlpecWS()) {
								// poradie nie je urcene requestom. Tz davam ho automaticky
								DTOCiselnikStlpec najdenyStlpec = vyhladajStlpec(stlpceList, updStlpec.getNazov());
								najdenyStlpec = vytvorStlpec(auth, updStlpec, ciselnik, najdenyStlpec, poradie++);
								// 3.1.1.3.2.2
								vytvorPreklad(auth, najdenyStlpec.getCiselnikStlpecID(), updStlpec.getDtoUpdPrekladStlpec(), TABULKA_CISELNIK_STLPEC);
							}
						}

					} else {
						// 3.1.1.4
						ciselnik = vytvorCiselnik(auth, updCiselnik, null);
					}
					// 3.2
					vytvorPreklad(auth, ciselnik.getCiselnikID(), updCiselnik.getDtoUpdCiselnikPreklad(), TABULKA_CISELNIK);
				}

			// class diagram - Návratová hodntoa ak je 0 tak operácia preebehla úspešne
			result.setKod(0);

			returnConnection(auth);
			endTransaction(auth, true);
			return result;

		} catch (Exception t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	private void vytvorPreklad(ZSRAuthInfo auth, Integer zaznamId, DTOPrekladWS[] updPreklady, int tabulka) throws AppException {
		DTOPreklad[] prekladyCiselnik = vyhladajPrekladPodlaIdZaznam(auth, zaznamId, tabulka);
		if (updPreklady != null && updPreklady.length > 0)
			for (DTOPrekladWS updCiselnikPreklad : updPreklady) {
				DTOPreklad najdenyCiselnikPreklad = vyhladajPreklad(prekladyCiselnik, updCiselnikPreklad);
				if (najdenyCiselnikPreklad != null) {
					if (updCiselnikPreklad.getPreklad() == null || updCiselnikPreklad.getPreklad().isEmpty()) {
						// vymaz preklad
						dlg.getPrekladModify().delete(auth, najdenyCiselnikPreklad.getPrekladID());
					} else {
						// aktualizuj preklad
						najdenyCiselnikPreklad.setPreklad(updCiselnikPreklad.getPreklad());
						// jazyk a stlpec sa NEmohli zmenit! -> idPrekladJazyk, idPrekladStlpec ostavaju rovnake
						// najdenyCiselnikPreklad.setPrekladStlpecNazovDB(updCiselnikPreklad.getPolozka());
						// najdenyCiselnikPreklad.setPrekladJazykKod(updCiselnikPreklad.getJazyk());
						dlg.getPrekladModify().update(auth, najdenyCiselnikPreklad);
					}
				} else {
					// vytvor preklad
					DTOPreklad dto = new DTOPreklad();
					dto.setPreklad(updCiselnikPreklad.getPreklad());
					dto.setPrekladStlpecNazovDB(updCiselnikPreklad.getPolozka());
					dto.setPrekladJazykKod(updCiselnikPreklad.getJazyk());

					dto.setIDPrekladJazyk(vyhladajJazykPodlaKodu(auth, updCiselnikPreklad.getJazyk()));
					dto.setIDPrekladStlpec(vyhladajStlpecPodlaPolozka(auth, updCiselnikPreklad, tabulka));
					dto.setZaznamID(zaznamId);

					dlg.getPrekladModify().update(auth, dto);
				}
			}
	}

	private Integer vyhladajStlpecPodlaPolozka(ZSRAuthInfo auth, DTOPrekladWS updStlpecPreklad, int tabulkaId) throws AppException {
		DTOPrekladStlpec dtoF = new DTOPrekladStlpec();
		dtoF.setNazovDb(updStlpecPreklad.getPolozka());
		dtoF.setIDPrekladTabulka(tabulkaId);
		DTOPrekladStlpec[] prekladStlpecList = dlg.getPrekladStlpecRead().listLight(auth, dtoF);

		if (prekladStlpecList != null && prekladStlpecList.length > 0)
			return prekladStlpecList[0].getPrekladStlpecID();

		return null;
	}

	private Integer vyhladajJazykPodlaKodu(ZSRAuthInfo auth, String kod) throws AppException {
		DTOPrekladJazyk[] jazyky = dlg.getPrekladJazykRead().listLight(auth);
		for (DTOPrekladJazyk jazyk : jazyky) {
			if (jazyk.getKod().equals(kod)) {
				return jazyk.getPrekladJazykID();
			}
		}
		return null;
	}

	// updStlpec - zo spravy, cislenik - na previazanie s ciselnikom, najdenyStlpec - najdeny v db
	private DTOCiselnikStlpec vytvorStlpec(ZSRAuthInfo auth, DTOUpdStlpecWS updStlpec, DTOCiselnik ciselnik, DTOCiselnikStlpec najdenyStlpec, int poradie) throws AppException {
		DTOCiselnikStlpec stlpec;

		if (najdenyStlpec == null)
			stlpec = new DTOCiselnikStlpec();
		else
			stlpec = najdenyStlpec;

		stlpec.setNazov(updStlpec.getNazov());
		stlpec.setNadpis(updStlpec.getNadpis());
		stlpec.setTyp(updStlpec.getTyp());
		stlpec.setDbTyp(updStlpec.getDbTyp());
		stlpec.setDecimals(updStlpec.getDecimals());
		stlpec.setDlzka(updStlpec.getDlzka());
		stlpec.setFk1PkNazov(updStlpec.getFk1PkNazov());
		stlpec.setFk1CiselnikTabulka(updStlpec.getFk1Tabulka());
		stlpec.setFk1IDCiselnik(updStlpec.getFk1IDCiselnik());
		stlpec.setJedinecny(updStlpec.getJedinecne());
		stlpec.setPopis(updStlpec.getPopis());
		stlpec.setPovinny(updStlpec.getPovinne());

		stlpec.setIDCiselnik(ciselnik.getCiselnikID());
		stlpec.setPoradie(poradie);
		stlpec.setAktivny(BOOL_TRUE);

		dlg.getCiselnikStlpecModify().update(auth, stlpec);
		return stlpec;
	}

	private DTOCiselnik vytvorCiselnik(ZSRAuthInfo auth, DTOUpdCiselnikWS updCiselnik, DTOCiselnik najdenyCiselnik) throws AppException {
		DTOCiselnik dtoCiselnik;

		if (najdenyCiselnik == null)
			dtoCiselnik = new DTOCiselnik();
		else
			dtoCiselnik = najdenyCiselnik;

		if (updCiselnik.getPopis() == null || updCiselnik.getPopis().isEmpty()) {
			throw new AppException(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_611, "updCiselnik.popis"));
		}

		dtoCiselnik.setNazov(updCiselnik.getNazov());
		dtoCiselnik.setPopis(updCiselnik.getPopis());
		dtoCiselnik.setTabulka(updCiselnik.getTabulka());
		dtoCiselnik.setPredpis(updCiselnik.getPredpis());
		dtoCiselnik.setPrilohaKapitola(updCiselnik.getPrilohaKapitola());
		dtoCiselnik.setPrintZahlavie(updCiselnik.getPrintZahlavie());
		dtoCiselnik.setAktivny(updCiselnik.getAktivny());

		dtoCiselnik.setTyp("INY");
		dtoCiselnik.setKategoria("INFRA");

		dlg.getCiselnikModify().update(auth, dtoCiselnik);
		return dtoCiselnik;
	}

	private DTOCiselnikStlpec vyhladajStlpec(DTOCiselnikStlpec[] stlpceListLoad, String stlpecNazov) {
		for (DTOCiselnikStlpec stlpecLoad : stlpceListLoad) {
			if (stlpecLoad.getNazov().equals(stlpecNazov)) {
				return stlpecLoad;
			}
		}
		return null;
	}

	private DTOPreklad vyhladajPreklad(DTOPreklad[] prekladyStlpec, DTOPrekladWS updStlpecPreklad) {
		for (DTOPreklad stlpecPrekladLoad : prekladyStlpec) {
			if (stlpecPrekladLoad.getPrekladJazykKod().equals(updStlpecPreklad.getJazyk()) && stlpecPrekladLoad.getPrekladStlpecNazovDB().equals(updStlpecPreklad.getPolozka())) {
				return stlpecPrekladLoad;
			}
		}
		return null;
	}

	public DTOCiselnikWS[] getMetaList(ZSRAuthInfo auth) throws AppException {
		DBUtils.checkPermission(auth, "CUDWSMIDMetaRead");

		DTOCiselnik dtoF = new sk.ditec.cud.dto.DTOCiselnik();
		dtoF.setAktivny(BOOL_TRUE);
		dtoF.setTyp(_CudConsts.CISELNIK_TYP_INY);

		DTOCiselnik[] listDTO = dlg.getCiselnikRead().listLight(auth, new Page(), dtoF);

		List<DTOCiselnikWS> listWS = new ArrayList<DTOCiselnikWS>();
		for (DTOCiselnik dto : listDTO) {
			listWS.add(copyDTO(dto));
		}

		return listWS.toArray(new DTOCiselnikWS[0]);
	}

	public DTOCiselnikWS getMeta(ZSRAuthInfo auth, Integer ciselnikId) throws AppException {
		DBUtils.checkPermission(auth, "CUDWSMIDMetaRead");

		DTOCiselnikWS resultDTO = new DTOCiselnikWS();

		// if (!StringUtils.isValid(ciselnikId)) {
		// resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "ciselnikId"));
		// return resultDTO;
		// }

		// 4
		DTOCiselnik dtoCiselnik = dlg.getCiselnikRead().readLight(auth, ciselnikId);

		if (dtoCiselnik == null || !_CudConsts.CISELNIK_TYP_INY.equals(dtoCiselnik.getTyp())) {
			throw new AppException(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ciselnikId.toString()));
		} else if (BOOL_FALSE.equals(dtoCiselnik.getAktivny())) {
			throw new AppException(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ciselnikId.toString()));
		}

		// nastavenie - CUD_CISELNIK
		resultDTO.setCiselnikID(dtoCiselnik.getCiselnikID());
		resultDTO.setNazov(dtoCiselnik.getNazov());
		resultDTO.setPopis(dtoCiselnik.getPopis());
		resultDTO.setTabulka(dtoCiselnik.getTabulka());
		resultDTO.setPredpis(dtoCiselnik.getPredpis());
		resultDTO.setPrilohaKapitola(dtoCiselnik.getPrilohaKapitola());
		resultDTO.setPrintZahlavie(dtoCiselnik.getPrintZahlavie());

		// 5.2 - preklady CUD_CISELNIK
		DTOPreklad[] preklad = vyhladajPrekladPodlaIdZaznam(auth, dtoCiselnik.getCiselnikID(), TABULKA_CISELNIK);
		ArrayList<DTOPrekladWS> ciselnikPreklady = new ArrayList<DTOPrekladWS>();
		for (int var = 0; var < preklad.length; var++)
			ciselnikPreklady.add(vytvorPrekladWS(preklad[var]));

		resultDTO.setDtoPrekladWS(ciselnikPreklady.toArray(new DTOPrekladWS[0]));

		// 5.3 - CUD_CISELNIK_STLPEC + preklady
		DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
		dtoF.setIDCiselnik(dtoCiselnik.getCiselnikID());
		dtoF.setAktivny(BOOL_TRUE);

		DTOCiselnikStlpec[] ciselnikStlpecList = dlg.getCiselnikStlpecRead().list(auth, new Page(), dtoF);
		List<DTOCiselnikStlpecMetaWS> dtoCiselnikStlpecMetaWSList = new ArrayList<DTOCiselnikStlpecMetaWS>();
		for (int var1 = 0; var1 < ciselnikStlpecList.length; var1++) {
			DTOCiselnikStlpecMetaWS dtoCiselnikStlpec = naplnCiselnikStlpec(auth, ciselnikStlpecList[var1]);
			dtoCiselnikStlpecMetaWSList.add(dtoCiselnikStlpec);
		}
		resultDTO.setDtoCiselnikStlpecMetaWS(dtoCiselnikStlpecMetaWSList.toArray(new DTOCiselnikStlpecMetaWS[0]));

		return resultDTO;
	}

	private DTOPrekladWS vytvorPrekladWS(DTOPreklad preklad) {
		DTOPrekladWS dtoPrekladWS = new DTOPrekladWS();

		dtoPrekladWS.setPolozka(preklad.getPrekladStlpecNazovDB());
		dtoPrekladWS.setJazyk(preklad.getPrekladJazykKod());
		dtoPrekladWS.setPreklad(preklad.getPreklad());

		return dtoPrekladWS;
	}

	private DTOCiselnikStlpecMetaWS naplnCiselnikStlpec(ZSRAuthInfo auth, DTOCiselnikStlpec dtoCiselnikStlpec) throws AppException {
		DTOCiselnikStlpecMetaWS dto = new DTOCiselnikStlpecMetaWS();

		// ciselnikStlpec
		dto.setCiselnikStlpecID(dtoCiselnikStlpec.getCiselnikStlpecID());
		dto.setIDCiselnik(dtoCiselnikStlpec.getIDCiselnik());
		dto.setNazov(dtoCiselnikStlpec.getNazov());
		dto.setNadpis(dtoCiselnikStlpec.getNadpis());
		dto.setTyp(dtoCiselnikStlpec.getTyp());
		dto.setDlzka(dtoCiselnikStlpec.getDlzka());
		dto.setDecimals(dtoCiselnikStlpec.getDecimals());
		dto.setDbTyp(dtoCiselnikStlpec.getDbTyp());
		dto.setPovinny(dtoCiselnikStlpec.getPovinny());
		dto.setJedinecny(dtoCiselnikStlpec.getJedinecny());
		dto.setPopis(dtoCiselnikStlpec.getPopis());
		dto.setFk1IDCiselnik(dtoCiselnikStlpec.getFk1IDCiselnik());
		dto.setFk1PkNazov(dtoCiselnikStlpec.getFk1PkNazov());

		// preklad
		DTOPreklad[] preklad = vyhladajPrekladPodlaIdZaznam(auth, dtoCiselnikStlpec.getCiselnikStlpecID(), TABULKA_CISELNIK_STLPEC);

		ArrayList<DTOPrekladWS> ciselnikPreklady = new ArrayList<DTOPrekladWS>();
		for (int var = 0; var < preklad.length; var++)
			ciselnikPreklady.add(vytvorPrekladWS(preklad[var]));

		dto.setDtoPrekladWS(ciselnikPreklady.toArray(new DTOPrekladWS[0]));

		return dto;
	}

	private DTOPreklad[] vyhladajPrekladPodlaIdZaznam(ZSRAuthInfo auth, int zaznamId, int tabulkaId) throws AppException {
		DTOPreklad dtoP = new DTOPreklad();
		dtoP.setZaznamID(zaznamId);
		dtoP.setPrekladStlpecIDPrekladTabulka(tabulkaId);
		DTOPreklad[] prekladList = dlg.getPrekladRead().list(auth, new Page(), dtoP);

		return prekladList;
	}

	private DTOPreklad vyhladajPrekladPodlaIdZaznam(ZSRAuthInfo auth, Integer zaznamId, String jazykKod, String tabulkaNazovDb) throws AppException {
		DTOPreklad dtoP = new DTOPreklad();
		Map<Integer, Map<String, String>> prekladList = dlg.getPrekladRead().map(auth, jazykKod, tabulkaNazovDb, new Integer[] { zaznamId });

		if (prekladList != null && !prekladList.isEmpty()) {
			Map.Entry<String, String> entry = prekladList.get(zaznamId).entrySet().iterator().next();
			String prekladStlpecNazovDB = entry.getKey();
			String preklad = entry.getValue();

			dtoP.setPrekladStlpecNazovDB(prekladStlpecNazovDB);
			dtoP.setPrekladJazykKod(jazykKod);
			dtoP.setPreklad(preklad);

			return dtoP;
		}

		return null;
	}

	private DTOCiselnikWS copyDTO(DTOCiselnik dto) {

		DTOCiselnikWS dtoWS = new DTOCiselnikWS();
		dtoWS.setCiselnikID(dto.getCiselnikID());
		dtoWS.setNazov(dto.getNazov());
		dtoWS.setPopis(dto.getPopis());
		dtoWS.setTabulka(dto.getTabulka());
		dtoWS.setPredpis(dto.getPredpis());
		dtoWS.setPrilohaKapitola(dto.getPrilohaKapitola());
		dtoWS.setPrintZahlavie(dto.getPrintZahlavie());

		return dtoWS;

	}

	public DTOOpravneniaListResponse getOpravneniaList(ZSRAuthInfo auth, String[] tabulkaList, String typPristupu) throws AppException {
		DBUtils.checkPermission(auth, "CUDWSMIDMetaRead");

		DTOOpravneniaListResponse response = new DTOOpravneniaListResponse();

		List<DTOCiselnik> ciselnikList = new ArrayList<DTOCiselnik>();
		DTOCiselnik dtoF = new DTOCiselnik();
		for (String tabulka : tabulkaList) {
			dtoF.setTabulka(tabulka);
			DTOCiselnik[] ciselnikListResult = dlg.getCiselnikRead().listLight(auth, dtoF);
			if (ciselnikListResult != null && ciselnikListResult.length > 0) {
				for (DTOCiselnik ciselnik : ciselnikListResult) {
					if (!ciselnik.getTyp().equals(_CudConsts.CISELNIK_TYP_INY)) {
						throw new AppException(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ciselnik.getCiselnikID().toString()));
					}
				}
			}

			ciselnikList.addAll(Arrays.asList(ciselnikListResult));
		}

		if (ciselnikList.isEmpty()) {
			throw new AppException(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ""));
		}

		DTOOdberatelObjekt dtoOdberatelF = new DTOOdberatelObjekt();
		dtoOdberatelF.setVsetkyCiselniky("T");
		boolean opravnenieVsetkyCiselniky = dlg.getOdberatelObjektRead().existujeOpravnenieNaVsetkyCiselnikyOdberatela(auth, dtoOdberatelF, null);
		if (opravnenieVsetkyCiselniky) {
			response.setVsetkyCiselniky(true);
			return response;
		}

		Integer[] ids = vratIdCiselnikov(ciselnikList);
		List<DTOOpravnenieWS> opravnenieList = new ArrayList<DTOOpravnenieWS>();
		List<DTOObjektCiselnik> ciselnikObjektList = dlg.getObjektCiselnikRead().vratPriradeneCiselnikyOdberatelovi2(auth, ids, typPristupu, new String[] { _CudConsts.CISELNIK_TYP_INY }, null);
		// 8.1
		for (DTOObjektCiselnik dtoObjekt : ciselnikObjektList) {
			DTOOpravnenieWS opravnenieWS = new DTOOpravnenieWS();
			napln(opravnenieWS, dtoObjekt);

			if (!"T".equals(dtoObjekt.getVsetky())) {
				List<DTOOpravnenieAtributWS> opravnenieAtributWSList = new ArrayList<DTOOpravnenieAtributWS>();
				DTOObjektStlpec[] ciselnikAtributList = dlg.getObjektStlpecRead().list(auth, dtoObjekt.getObjektCiselnikID());
				for (DTOObjektStlpec stlpecMeta : ciselnikAtributList) {
					boolean jeStlpecAktivny = dlg.getCiselnikStlpecRead().jeCiselnikStlpecAktivny(auth, stlpecMeta.getIDCiselnikStlpec());
					if (jeStlpecAktivny) {
						DTOOpravnenieAtributWS atributWS = new DTOOpravnenieAtributWS();
						napln(atributWS, stlpecMeta);
						opravnenieAtributWSList.add(atributWS);
					}
				}

				opravnenieWS.setOpravnenieAtributList(opravnenieAtributWSList.toArray(new DTOOpravnenieAtributWS[0]));
			}

			opravnenieList.add(opravnenieWS);
		}

		response.setOpravnenieList(opravnenieList.toArray(new DTOOpravnenieWS[0]));

		return response;
	}

	private Integer[] vratIdCiselnikov(List<DTOCiselnik> ciselnikList) {
		Integer[] result = new Integer[ciselnikList.size()];
		for (int i = 0; i < ciselnikList.size(); i++) {
			result[i] = ciselnikList.get(i).getCiselnikID();
		}
		return result;
	}

	private void napln(DTOOpravnenieWS opravnenieWS, DTOObjektCiselnik ciselnik) {
		opravnenieWS.setCiselnikID(ciselnik.getIDCiselnik());
		opravnenieWS.setTabulka(ciselnik.getCiselnikTabulka());
		opravnenieWS.setNazov(ciselnik.getCiselnikNazov());
		opravnenieWS.setVsetkyAtributy("T".equals(ciselnik.getVsetky()));
	}

	private void napln(DTOOpravnenieAtributWS atributWS, DTOObjektStlpec stlpec) {
		atributWS.setStlpecID(stlpec.getIDCiselnikStlpec());
		atributWS.setStlpec(stlpec.getCiselnikStlpecNazov());
		atributWS.setHodnota(stlpec.getHodnota());
		atributWS.setEditovatelny("T".equals(stlpec.getZmena()));
	}
}
