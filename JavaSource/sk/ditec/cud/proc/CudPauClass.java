package sk.ditec.cud.proc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOMeta;
import sk.ditec.cud.dto.DTOPau;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfNotif;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOWorkflow;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.dto.DTOZmenaStavHist;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

public class CudPauClass extends _NovyPISBaseClass {

	private Logger log = LoggerFactory.getLogger(CudPauClass.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	public void lookupAttributes(AuthInfo auth, DTOPau dtoPA, DTOMeta dtoMeta) throws AppException {

		try {
			DTOCiselnik dtoCis = _CudLookupUtils.lookupDTOCiselnik(dtoMeta.getCiselnikPole(), dtoPA.getZmena().getIDCiselnik());
			if (!StringUtils.isValid(dtoCis)) {
				throw new AppException("V tabulke CUD_CISELNIK neexistuje zaznam pre ciselnikID=" + dtoPA.getZmena().getIDCiselnik() + ", vykonavanie procesu konci.");
			}
			dtoPA.setCiselnik(dtoCis);

			DTOCiselnikGui dtoCisGui = _CudLookupUtils.lookupDTOCiselnikGuiByFk(dtoMeta.getCiselnikGuiPole(), dtoCis.getCiselnikID());
			if (!StringUtils.isValid(dtoCisGui)) {
				throw new AppException("V tabulke CUD_CISELNIK_GUI neexistuje zaznam pre ciselnikID=" + dtoCis.getCiselnikID() + ", vykonavanie procesu konci.");
			}
			dtoPA.setCiselnikGui(dtoCisGui);

			List<DTOCiselnikStlpec> ciselnikStlpecList = dtoMeta.getCiselnikStlpecMap().get(dtoPA.getZmena().getIDCiselnik());
			if (!StringUtils.isValid(ciselnikStlpecList) || ciselnikStlpecList.isEmpty()) {
				throw new AppException("V tabulke CUD_CISELNIK_STLPEC neexistuju zaznamy pre ciselnikID=" + dtoPA.getZmena().getIDCiselnik() + ", vykonavanie procesu konci.");

			}
			dtoPA.setCiselnikStlpecList(ciselnikStlpecList);

			List<DTOCiselnikStlpecGui> ciselnikStlpecGuiList = dtoMeta.getCiselnikStlpecGuiMap().get(dtoCisGui.getCiselnikGuiID());
			if (!StringUtils.isValid(ciselnikStlpecGuiList) || ciselnikStlpecGuiList.isEmpty()) {
				throw new AppException("V tabulke CUD_CISELNIK_STLPEC_GUI neexistuju zaznamy pre ciselnikGuiID=" + dtoCisGui.getCiselnikGuiID() + ", vykonavanie procesu konci.");

			}
			dtoPA.setCiselnikStlpecGuiList(ciselnikStlpecGuiList);

			dtoPA.setCiselnikStlpecPk(_CudLookupUtils.lookupDTOCiselnikStlpecPk(ciselnikStlpecList));
			if (!StringUtils.isValid(dtoPA.getCiselnikStlpecPk())) {
				throw new AppException("V tabulke CUD_CISELNIK_STLPEC nie je definovany primarny kluc pre ciselnik ciselnikID=" + dtoPA.getZmena().getIDCiselnik()
						+ ", vykonavanie procesu konci.");
			}

			dtoPA.setCiselnikStlpecGuiPk(_CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(ciselnikStlpecGuiList, dtoPA.getCiselnikStlpecPk().getCiselnikStlpecID()));

			dtoPA.setCiselnikStlpecJedinecny(_CudLookupUtils.lookupDTOCiselnikStlpecJedinecny(ciselnikStlpecList));

			if (StringUtils.isValid(dtoPA.getCiselnikStlpecJedinecny())) {
				dtoPA.setCiselnikStlpecGuiJedinecny(_CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(ciselnikStlpecGuiList, dtoPA.getCiselnikStlpecJedinecny().getCiselnikStlpecID()));
			}

			if (StringUtils.isValid(dtoPA.getZmena().getRowID())) {
				Map<String, String> rowMap = dlg.getDynCiselnikRead().readLight(auth, dtoCis.getTabulka(), ciselnikStlpecList, dtoPA.getCiselnikStlpecPk().getNazov(),
						dtoPA.getZmena().getRowID().toString(), dtoPA.getCiselnikStlpecPk().getDbTyp(), dtoPA.getZmena().getPlatnostOd(), null);
				dtoPA.setRowOldMap(rowMap);

			} else if (StringUtils.isValid(dtoPA.getCiselnikStlpecJedinecny())) {
				Map<String, String> rowMap = dynCiselnikReadJedinecny(auth, dtoPA);
				dtoPA.setRowOldMap(rowMap);

			} else {
				dtoPA.setRowOldMap(new HashMap<String, String>());
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupAttributes.error");
		}
	}

	public void kontrolaNaZmenuDopravnehoNazvu(AuthInfo auth, DTOPau dtoPA, List<DTOPau> paList, DTOMeta dtoMeta, Set<String> ciselnikNazovSpecSet,
			Set<Integer> ciselnikStlpecSpecSet, Set<Integer> ciselnikStlpecSpecFkSet) throws AppException {

		try {
			if (kontrolaNaObnovenieDopravnehoNazvuPriObnoveniZaznamu(auth, dtoPA, paList, dtoMeta, ciselnikNazovSpecSet, ciselnikStlpecSpecSet)) {
				return;
			}

			Integer ciselnikStlpecSpecialID = lookupFkCiselnikStlpecID(dtoPA.getZmenaStlpecList(), ciselnikStlpecSpecSet);

			// kontrola ci ide o zmenu v ciselniku T_DOPRAVNY_NAZOV
			if (!StringUtils.isValid(ciselnikStlpecSpecialID)) {
				return;
			}

			log.info("Ide o zmenu so specialnou vazbou na ciselnik T_DOPRAVNY_NAZOV, ciselnikStlpecID==" + ciselnikStlpecSpecialID);

			Integer dopravnyNazovOldValueID = lookupDopravnyNazovID(dtoPA, ciselnikStlpecSpecFkSet);

			if (_CudConsts.ZMENA_OPERACIA_N.equals(dtoPA.getZmena().getOperacia()) || _CudConsts.ZMENA_OPERACIA_U.equals(dtoPA.getZmena().getOperacia())) {

				String nazovNewValue = lookupNewValue(dtoPA.getZmenaStlpecList(), ciselnikStlpecSpecSet);
				if (!StringUtils.isValid(nazovNewValue)) {
					throw new AppException("Zmena nazvu na NULL, chybna hodonota v tabulke CUD_ZMENA_STLPEC, vykonavanie procesu konci.");
				}

				Map<String, String> rowDopravnyNazovMap = ciselnikDopravnyNazovReadByNazov(auth, dtoMeta, nazovNewValue, dtoPA.getZmena().getPlatnostOd());
				String dopravnyNazovNewValueIDStr = rowDopravnyNazovMap.get(_CudConsts.NAZOV_PK_KEY);
				boolean jeDopravnyNazovNewValueZmazany = "T".equals(rowDopravnyNazovMap.get(_CudConsts.NAZOV_ZMAZ));

				if (StringUtils.isValid(dopravnyNazovNewValueIDStr)) {

					if (jeNezamietnutaPoziadavkaNaPrimarnyKlucDopravnehoNazovu(auth, dopravnyNazovNewValueIDStr)) {
						throw new AppException("Pokus o priradenie zaznamu T_DOPRAVNY_NAZOV.NAZOV==" + dopravnyNazovNewValueIDStr
								+ ", pricom tento zaznam caka na spracovanie v registry zmien, vykonavanie procesu konci.");

					} else if (!jeDopravnyNazovNewValueZmazany) {
						Integer dopravnyNazovNewValueID = Integer.parseInt(dopravnyNazovNewValueIDStr);
						if (!dopravnyNazovNewValueID.equals(dopravnyNazovOldValueID)) {
							// pre danu zmenu sa dogeneruje zmena stlpca ID_DOPRAVNY_BOD
							String dopravnyNazovOldValueIDStr = StringUtils.isValid(dopravnyNazovOldValueID) ? dopravnyNazovOldValueID.toString() : null;
							Integer ciselnikStlpecID = lookupCiselnikStlpecID(dtoPA.getCiselnikStlpecList(), ciselnikStlpecSpecFkSet);
							generujZmenaStlpecDTO(dtoPA, ciselnikStlpecID, dopravnyNazovOldValueIDStr, dopravnyNazovNewValueIDStr);
						}

					} else {
						// zaznam v ciselnik T_DOPRAVNY_NAZOV existuje ale je zmazany
						Integer dopravnyNazovNewValueID = Integer.parseInt(dopravnyNazovNewValueIDStr);
						DTOZmena dtoZmenaDopravnyNazov = createDTOZmena(_CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.ZMENA_OPERACIA_U, dtoPA.getZmena().getPlatnostOd(),
								dopravnyNazovNewValueID);
						Integer ciselnikStlpecID = getCiselnikStlpecID(dtoMeta.getCiselnikStlpecMap(), _CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_NAZOV);
						List<DTOZmenaStlpec> zmenaStlpecDopravnyNazovList = createListZmenaStlpec(_CudConsts.ID_T_DOPRAVNY_NAZOV, null, ciselnikStlpecID, nazovNewValue,
								nazovNewValue);
						DTOPau dtoNew = createDTOPrimarnaAktualizacia(dtoZmenaDopravnyNazov, zmenaStlpecDopravnyNazovList, dtoPA.getCasVytvorenia());
						dtoPA.setPrev(dtoNew);
						dtoNew.setNext(dtoPA);
						paList.add(dtoNew);

						// // pre danu zmenu sa dogeneruje zmena stlpca ID_DOPRAVNY_BOD
						ciselnikStlpecID = getCiselnikStlpecID(dtoMeta.getCiselnikStlpecMap(), dtoPA.getZmena().getIDCiselnik(), _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV);
						String dopravnyNazovOldValueIDStr = StringUtils.isValid(dopravnyNazovOldValueID) ? dopravnyNazovOldValueID.toString() : null;
						generujZmenaStlpecDTO(dtoPA, ciselnikStlpecID, dopravnyNazovOldValueIDStr, dopravnyNazovNewValueIDStr);
					}

				} else {

					if (jeNezamietnutaPoziadavkaNaNazovDopravnehoNazovu(auth, dtoMeta.getCiselnikStlpecMap(), nazovNewValue)) {
						throw new AppException("Pokus o zmenu nazvu zaznamu T_DOPRAVNY_NAZOV.NAZOV==" + nazovNewValue
								+ ", pricom tato zmena uz caka na spracovanie v registry zmien, vykonavanie procesu konci.");
					}

					Integer pocetVazieb = getPocetVaziebNaZaznam(auth, dopravnyNazovOldValueID, ciselnikNazovSpecSet, dtoPA.getZmena().getPlatnostOd());

					if (pocetVazieb.intValue() == 1 && StringUtils.isValid(dopravnyNazovOldValueID)) {
						DTOZmena dtoZmenaDopravnyNazov = createDTOZmena(_CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.ZMENA_OPERACIA_U, dtoPA.getZmena().getPlatnostOd(),
								dopravnyNazovOldValueID);
						Integer ciselnikStlpecID = getCiselnikStlpecID(dtoMeta.getCiselnikStlpecMap(), _CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_NAZOV);
						String nazovOldValue = lookupOldValue(dtoPA.getZmenaStlpecList(), ciselnikStlpecSpecialID);
						List<DTOZmenaStlpec> zmenaStlpecDopravnyNazovList = createListZmenaStlpec(_CudConsts.ID_T_DOPRAVNY_NAZOV, null, ciselnikStlpecID, nazovOldValue,
								nazovNewValue);
						DTOPau dtoNew = createDTOPrimarnaAktualizacia(dtoZmenaDopravnyNazov, zmenaStlpecDopravnyNazovList, dtoPA.getCasVytvorenia());
						dtoNew.setNext(dtoPA);
						dtoPA.setPrev(dtoNew);
						paList.add(dtoNew);
					}

					// kontrola ci na povodny zaznam su naviazane ine zaznamy alebo ci vlastne povodny zaznam existuje
					if (pocetVazieb.intValue() > 1 || !StringUtils.isValid(dopravnyNazovOldValueID)) {
						// vygeneruju nova zmena s danym nazvom
						DTOZmena dtoZmenaNew = createDTOZmena(_CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.ZMENA_OPERACIA_N, dtoPA.getZmena().getPlatnostOd(), null);
						List<DTOZmenaStlpec> zmenaStlpecListNew = createListZmenaStlpec(_CudConsts.ID_T_DOPRAVNY_NAZOV, null, _CudConsts.ID_T_DOPRAVNY_NAZOV_NAZOV, null,
								nazovNewValue);
						DTOPau dtoNew = createDTOPrimarnaAktualizacia(dtoZmenaNew, zmenaStlpecListNew, dtoPA.getCasVytvorenia());
						dtoPA.setPrev(dtoNew);
						dtoNew.setNext(dtoPA);
						paList.add(dtoNew);

						// pre danu zmenu sa dogeneruje zmena stlpca ID_DOPRAVNY_BOD
						String dopravnyNazovOldValueIDStr = StringUtils.isValid(dopravnyNazovOldValueID) ? dopravnyNazovOldValueID.toString() : null;
						Integer ciselnikStlpecID = getCiselnikStlpecID(dtoMeta.getCiselnikStlpecMap(), dtoPA.getZmena().getIDCiselnik(), _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV);
						generujZmenaStlpecDTO(dtoPA, ciselnikStlpecID, dopravnyNazovOldValueIDStr, _CudConsts.FK_VALUE);
					}

					if (pocetVazieb.intValue() == 0 && StringUtils.isValid(dopravnyNazovOldValueID)) {
						// tato situacia riesi specialny pripad, ze mam dopravny bod a dopravny nazov 1:1, oba su zmazane, a urobi sa insert co bude vlastne update nad dopravnym
						// bodom

						// vygeneruju nova zmena s danym nazvom
						DTOZmena dtoZmenaNew = createDTOZmena(_CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.ZMENA_OPERACIA_N, dtoPA.getZmena().getPlatnostOd(), null);
						List<DTOZmenaStlpec> zmenaStlpecListNew = createListZmenaStlpec(_CudConsts.ID_T_DOPRAVNY_NAZOV, null, _CudConsts.ID_T_DOPRAVNY_NAZOV_NAZOV, null,
								nazovNewValue);
						DTOPau dtoNew = createDTOPrimarnaAktualizacia(dtoZmenaNew, zmenaStlpecListNew, dtoPA.getCasVytvorenia());
						dtoPA.setPrev(dtoNew);
						dtoNew.setNext(dtoPA);
						paList.add(dtoNew);

						// pre danu zmenu sa dogeneruje zmena stlpca ID_DOPRAVNY_BOD
						String dopravnyNazovOldValueIDStr = StringUtils.isValid(dopravnyNazovOldValueID) ? dopravnyNazovOldValueID.toString() : null;
						Integer ciselnikStlpecID = getCiselnikStlpecID(dtoMeta.getCiselnikStlpecMap(), dtoPA.getZmena().getIDCiselnik(), _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV);
						generujZmenaStlpecDTO(dtoPA, ciselnikStlpecID, dopravnyNazovOldValueIDStr, _CudConsts.FK_VALUE);
					}
				}

			} else if (_CudConsts.ZMENA_OPERACIA_D.equals(dtoPA.getZmena().getOperacia()) || _CudConsts.ZMENA_OPERACIA_Z.equals(dtoPA.getZmena().getOperacia())) {

				Integer pocetVazieb = getPocetVaziebNaZaznam(auth, _CudConsts.ID_T_DOPRAVNY_NAZOV, dopravnyNazovOldValueID, null, null, null, dtoMeta, dtoPA.getZmena()
						.getPlatnostOd());

				if (pocetVazieb.intValue() == 1) {
					DTOZmena dtoZmenaNew = createDTOZmena(_CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.ZMENA_OPERACIA_D, dtoPA.getZmena().getPlatnostOd(), dopravnyNazovOldValueID);
					List<DTOZmenaStlpec> zmenaStlpecListNew = generujZmenaStlpecList(auth, _CudConsts.ID_T_DOPRAVNY_NAZOV, dopravnyNazovOldValueID, dtoMeta,
							ciselnikStlpecSpecFkSet, dtoPA.getZmena().getPlatnostOd());
					DTOPau dtoNew = createDTOPrimarnaAktualizacia(dtoZmenaNew, zmenaStlpecListNew, dtoPA.getCasVytvorenia());
					dtoNew.setNext(dtoPA);
					dtoPA.setPrev(dtoNew);
					paList.add(dtoNew);
				}

			} else {
				throw new AppException("Nedefinovany typ operacie, vykonavanie procesu konci.");
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaNaZmenuDopravnehoNazvu.error");
		}
	}

	public void kontrolaPredPublikovanimZmeny(AuthInfo auth, DTOPau dtoPA, List<DTOPau> paList, DTOMeta dtoMeta, Set<Integer> ciselnikStlpecSpecFkSet, Set<String> specialValuesSet)
			throws AppException {

		try {
			if (_CudConsts.ZMENA_OPERACIA_N.equals(dtoPA.getZmena().getOperacia())) {

				Map<String, String> rowJedinecny = dynCiselnikReadJedinecny(auth, dtoPA);

				if ("F".equals(rowJedinecny.get(_CudConsts.NAZOV_ZMAZ))) {
					throw new AppException("Zmenu zmenaID==" + dtoPA.getZmena().getZmenaID()
							+ " nemozno vlozit, pretoze takyto zaznam sa uz v DB nachadza, porusena jedinecnost zaznamu! Vykonavanie procesu konci!");

				} else if ("T".equals(rowJedinecny.get(_CudConsts.NAZOV_ZMAZ))) {

					// nastavia sa informacie pre CUD_ZMENA
					dtoPA.getZmena().setOperacia(_CudConsts.ZMENA_OPERACIA_U);
					String pkValue = rowJedinecny.get(_CudConsts.NAZOV_PK_KEY);
					dtoPA.getZmena().setRowID(Integer.parseInt(pkValue));

					// dogeneruju sa zaznamy pre CUD_ZMENA_STLPEC
					List<DTOZmenaStlpec> zmenaStlpecUpdateList = getZmenaStlpecList(dtoPA, rowJedinecny);
					if (StringUtils.isValid(zmenaStlpecUpdateList) && !zmenaStlpecUpdateList.isEmpty()) {
						if (!StringUtils.isValid(dtoPA.getZmenaStlpecList())) {
							dtoPA.setZmenaStlpecList(new ArrayList<DTOZmenaStlpec>());
						}
						dtoPA.getZmenaStlpecList().addAll(zmenaStlpecUpdateList);
					}

					kontrolaPlatnostiVaziebMimoZmeny(auth, dtoPA, dtoMeta, ciselnikStlpecSpecFkSet);

					// sql prikaze pre update uz zmazaneho zaznamu
					dtoPA.setSql(generujUpdateSql(dtoPA, rowJedinecny, ciselnikStlpecSpecFkSet));

				} else {
					dtoPA.setSql(generujInsertSql(dtoPA));
				}

			} else if (_CudConsts.ZMENA_OPERACIA_U.equals(dtoPA.getZmena().getOperacia())) {

				if (dtoPA.getRowOldMap().keySet().isEmpty()) {
					throw new AppException("Zmenu zmenaID==" + dtoPA.getZmena().getZmenaID()
							+ " nemozno uplatnit, pretoze zaznam s tymto ID sa v DB nenachadza! Vykonavanie procesu konci!");
				}

				Map<String, String> rowJedinecny = dynCiselnikReadJedinecny(auth, dtoPA);

				if ("F".equals(rowJedinecny.get(_CudConsts.NAZOV_ZMAZ))) {
					Integer ciselnikRowID = Integer.parseInt(rowJedinecny.get(_CudConsts.NAZOV_PK_KEY));
					if (ciselnikRowID.intValue() != dtoPA.getZmena().getRowID().intValue()) {
						throw new AppException("Zmenu zmenaID==" + dtoPA.getZmena().getZmenaID()
								+ " nemozno uplatnit, pretoze takyto zaznam sa uz v DB nachadza, porusena jedinecnost zaznamu! Vykonavanie procesu konci!");
					}
				}

				kontrolaOldValues(dtoPA);
				kontrolaPlatnostiVaziebMimoZmeny(auth, dtoPA, dtoMeta, ciselnikStlpecSpecFkSet);

				Map<String, String> rowMap = StringUtils.isValid(rowJedinecny) && !rowJedinecny.keySet().isEmpty() ? rowJedinecny : dtoPA.getRowOldMap();
				dtoPA.setSql(generujUpdateSql(dtoPA, rowMap, ciselnikStlpecSpecFkSet));

			} else if (_CudConsts.ZMENA_OPERACIA_D.equals(dtoPA.getZmena().getOperacia()) || _CudConsts.ZMENA_OPERACIA_Z.equals(dtoPA.getZmena().getOperacia())) {

				if (dtoPA.getRowOldMap().keySet().isEmpty()) {
					throw new AppException("Zmenu zmenaID==" + dtoPA.getZmena().getZmenaID()
							+ " nemozno uplatnit, pretoze zaznam s tymto ID sa v DB nenachadza! Vykonavanie procesu konci!");
				}

				Integer pocetVazieb = null;
				if (StringUtils.isValid(dtoPA.getNext())) {
					pocetVazieb = getPocetVaziebNaZaznam(auth, dtoPA.getZmena().getIDCiselnik(), dtoPA.getZmena().getRowID(), dtoPA.getNext().getZmena().getIDCiselnik(), dtoPA
							.getNext().getZmena().getRowID(), dtoPA.getNext().getCiselnikStlpecPk().getNazov(), dtoMeta, dtoPA.getZmena().getPlatnostOd());
				} else {
					pocetVazieb = getPocetVaziebNaZaznam(auth, dtoPA.getZmena().getIDCiselnik(), dtoPA.getZmena().getRowID(), null, null, null, dtoMeta, dtoPA.getZmena()
							.getPlatnostOd());
				}
				if (pocetVazieb.intValue() > 0) {
					throw new AppException("Zmenu zmenaID==" + dtoPA.getZmena().getZmenaID()
							+ " nemozno uskutocnit (operazia=Z), pretoze na zaznam, ktory sa pokusa zmazat su naviazane ine zaznamy! Vykonavanie procesu konci!");
				}

				kontrolaOldValues(dtoPA);

				dtoPA.setSql(generujZneplatnenieSql(dtoPA));
			}

			// kontrola ci v zmene nie je zmena na zmazanu hodnotu
			if (!_CudConsts.ZMENA_OPERACIA_D.equals(dtoPA.getZmena().getOperacia()) && !_CudConsts.ZMENA_OPERACIA_Z.equals(dtoPA.getZmena().getOperacia())) {
				if (jeVZmeneStlpecReferencujuciZmazanuHodnotu(auth, dtoPA, dtoMeta.getCiselnikPole(), ciselnikStlpecSpecFkSet, specialValuesSet)) {
					throw new AppException("V zmene sa nachadza zaznam referencujuci na zmazanu hodnotu! Vykonavanie procesu konci!");
				}
			}

			// update posledneho zaznamu
			String zmenaIDStr = dtoPA.getRowOldMap().get(_CudConsts.NAZOV_ID_ZMENA);
			if (StringUtils.isValid(zmenaIDStr)) {
				DTOZmena dtoZmenaLast = dlg.getZmenaRead().readLight(auth, Integer.parseInt(zmenaIDStr));
				Date platnostDo = getPosunDatum(dtoPA.getZmena().getPlatnostOd(), -1);
				dtoZmenaLast.setPlatnostDo(platnostDo);
				DTOPau dtoNew = createDTOPrimarnaAktualizacia(dtoZmenaLast, null, dtoPA.getCasVytvorenia());
				dtoNew.setRowOldMap(dtoPA.getRowOldMap());
				dtoNew.setCiselnik(dtoPA.getCiselnik());
				dtoNew.setSql(generujDeleteSql(dtoNew));
				dtoNew.setLenPlatnostDo("T");
				paList.add(dtoNew);
			}

			dtoPA.getZmena().setStav(_CudConsts.ZMENA_STAV_PAU);

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaPredPublikovanimZmeny.error");
		}
	}

	public void kontrolaNaNadradenuDopravnu(DTOPau dtoPA) throws AppException {

		try {
			if (!_CudConsts.TABULKA_T_DOPRAVNY_BOD.equals(dtoPA.getCiselnik().getTabulka())) {
				return;
			}
			if (_CudConsts.ZMENA_OPERACIA_D.equals(dtoPA.getZmena().getOperacia())) {
				return;
			}
			if (_CudConsts.ZMENA_OPERACIA_Z.equals(dtoPA.getZmena().getOperacia())) {
				return;
			}

			DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), _CudConsts.NAZOV_ID_DOPRAVNY_BOD);
			DTOZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOZmenaStlpecByFk(dtoPA.getZmenaStlpecList(), dtoCS.getCiselnikStlpecID());

			if (_CudConsts.ZMENA_OPERACIA_N.equals(dtoPA.getZmena().getOperacia())) {
				if (!StringUtils.isValid(dtoZS)) {
					generujZmenaStlpecDTO(dtoPA, dtoCS.getCiselnikStlpecID(), null, _CudConsts.PK_VALUE);
				}
			}

			if (_CudConsts.ZMENA_OPERACIA_U.equals(dtoPA.getZmena().getOperacia())) {
				if (StringUtils.isValid(dtoZS) && !StringUtils.isValid(dtoZS.getNewValue())) {
					dtoZS.setNewValue(dtoPA.getZmena().getRowID().toString());
				}
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaNaNadradenuDopravnu.error");
		}
	}

	public boolean kontrolaNaObnovenieDopravnehoNazvuPriObnoveniZaznamu(AuthInfo auth, DTOPau dtoPA, List<DTOPau> paList, DTOMeta dtoMeta, Set<String> ciselnikNazovSpecSet,
			Set<Integer> ciselnikStlpecSpecSet) throws AppException {

		try {
			if (!ciselnikNazovSpecSet.contains(dtoPA.getCiselnik().getTabulka())) {
				return false;
			}
			if (!"T".equals(dtoPA.getRowOldMap().get(_CudConsts.NAZOV_ZMAZ))) {
				return false;
			}
			if (StringUtils.isValid(lookupFkCiselnikStlpecID(dtoPA.getZmenaStlpecList(), ciselnikStlpecSpecSet))) {
				return false;
			}

			String dopravnyNazovIDStr = null;
			String pkNazov = null;
			List<DTOCiselnikStlpec> csList = dtoMeta.getCiselnikStlpecMap().get(dtoPA.getCiselnik().getCiselnikID());
			for (DTOCiselnikStlpec dto : csList) {
				if (StringUtils.isValid(dto.getFk1IDCiselnik()) && _CudConsts.ID_T_DOPRAVNY_NAZOV.intValue() == dto.getFk1IDCiselnik().intValue()) {
					dopravnyNazovIDStr = dtoPA.getRowOldMap().get(dto.getNazov());
					pkNazov = dto.getFk1PkNazov();
					break;
				}
			}

			DTOCiselnik dtoCis = _CudLookupUtils.lookupDTOCiselnik(dtoMeta.getCiselnikPole(), _CudConsts.ID_T_DOPRAVNY_NAZOV);
			List<DTOCiselnikStlpec> csList2 = dtoMeta.getCiselnikStlpecMap().get(dtoCis.getCiselnikID());

			Map<String, String> rowDNMap = dlg.getDynCiselnikRead().readLight(auth, dtoCis.getTabulka(), csList2, pkNazov, dopravnyNazovIDStr, _CudConsts.DB_TYP_INTEGER,
					dtoPA.getZmena().getPlatnostOd(), null);

			if ("T".equals(rowDNMap.get(_CudConsts.NAZOV_ZMAZ))) {

				DTOZmena dtoZmenaDopravnyNazov = createDTOZmena(_CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.ZMENA_OPERACIA_U, dtoPA.getZmena().getPlatnostOd(), new Integer(
						dopravnyNazovIDStr));
				String value = rowDNMap.get(_CudConsts.NAZOV_NAZOV);
				Integer ciselnikStlpecID = getCiselnikStlpecID(dtoMeta.getCiselnikStlpecMap(), _CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_NAZOV);
				List<DTOZmenaStlpec> zmenaStlpecList = createListZmenaStlpec(_CudConsts.ID_T_DOPRAVNY_NAZOV, null, ciselnikStlpecID, value, value);
				DTOPau dtoNew = createDTOPrimarnaAktualizacia(dtoZmenaDopravnyNazov, zmenaStlpecList, dtoPA.getCasVytvorenia());
				dtoPA.setPrev(dtoNew);
				dtoNew.setNext(dtoPA);
				paList.add(dtoNew);

				dtoPA.setObnovenieDopravnehoNazvu("T");

				return true;
			}

			return false;

		} catch (Throwable t) {
			handleException(t, "kontrolaNaObnovenieDopravnehoNazvuPriObnoveniZaznamu.error", auth);
			return false;
		}
	}

	public Set<String> getSpecialValueSet() throws AppException {

		try {
			Set<String> set = new HashSet<String>();
			set.add(_CudConsts.PK_VALUE);
			set.add(_CudConsts.FK_VALUE);

			return set;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getSpecialValueSet.error");
			return null;
		}
	}

	/**
	 * Funkcia vrati zoznam nazvov ciselnikov, ktore maju specialnu vazbu na tabulku T_DOPRAVNY_NAZOV.
	 * 
	 * @return
	 * @throws AppException
	 */
	public Set<String> getCiselnikNazovSpecialSet() throws AppException {

		try {
			Set<String> set = new HashSet<String>();
			set.add(_CudConsts.TABULKA_T_DOPRAVNY_BOD);
			set.add(_CudConsts.TABULKA_T_HRANICNY_PRIECHOD);

			return set;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getCiselnikNazovSpecialSet.error");
			return null;
		}
	}

	/**
	 * Funckia vrati mnozinu ID atributov, ktore maju specialnu vazbu na ciselnik T_DOPRAVNY_NAZOV. Ked sa menia tieto atributy, potom sa musi menit aj cudzi kluc, ktory ukazuje do
	 * ciselnika T_DOPRAVNY_NAZOV. Ide o atributy T_DOPRAVNY_BOD.NAZOV a T_HRANICNY_PRIECHOD.ZAHRANICNA_PPS.
	 * 
	 * @param ciselnikPole
	 * @param ciselnikStlpecMap
	 * @return
	 * @throws AppException
	 */
	public Set<Integer> getCiselnikStlpecSpecialSetID(DTOMeta dtoMeta) throws AppException {

		try {
			Set<Integer> resultSet = new HashSet<Integer>();

			for (DTOCiselnik dto : dtoMeta.getCiselnikPole()) {

				if (_CudConsts.TABULKA_T_DOPRAVNY_BOD.equals(dto.getTabulka())) {

					List<DTOCiselnikStlpec> listCS = dtoMeta.getCiselnikStlpecMap().get(dto.getCiselnikID());
					DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(listCS, _CudConsts.NAZOV_NAZOV);
					DTOCiselnikGui dtoCisGui = _CudLookupUtils.lookupDTOCiselnikGuiByFk(dtoMeta.getCiselnikGuiPole(), dto.getCiselnikID());
					List<DTOCiselnikStlpecGui> listGui = dtoMeta.getCiselnikStlpecGuiMap().get(dtoCisGui.getCiselnikGuiID());
					DTOCiselnikStlpecGui dtoCSGui = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(listGui, dtoCS.getCiselnikStlpecID());
					if ("T".equals(dtoCSGui.getFormZobrazenie()) && "T".equals(dtoCSGui.getZmena())) {
						resultSet.add(dtoCS.getCiselnikStlpecID());
					}

				} else if (_CudConsts.TABULKA_T_HRANICNY_PRIECHOD.equals(dto.getTabulka())) {

					List<DTOCiselnikStlpec> listCS = dtoMeta.getCiselnikStlpecMap().get(dto.getCiselnikID());
					DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(listCS, _CudConsts.NAZOV_ZAHRANICNA_PPS);
					DTOCiselnikGui dtoCisGui = _CudLookupUtils.lookupDTOCiselnikGuiByFk(dtoMeta.getCiselnikGuiPole(), dto.getCiselnikID());
					List<DTOCiselnikStlpecGui> listGui = dtoMeta.getCiselnikStlpecGuiMap().get(dtoCisGui.getCiselnikGuiID());
					DTOCiselnikStlpecGui dtoCSGui = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(listGui, dtoCS.getCiselnikStlpecID());
					if ("T".equals(dtoCSGui.getFormZobrazenie()) && "T".equals(dtoCSGui.getZmena())) {
						resultSet.add(dtoCS.getCiselnikStlpecID());
					}
				}
			}

			return resultSet;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getCiselnikStlpecSpecialSetID.error");
			return null;
		}
	}

	public Set<Integer> getCiselnikStlpecSpecialFkSetID(DTOMeta dtoMeta) throws AppException {

		try {
			Set<Integer> resultSet = new HashSet<Integer>();

			for (DTOCiselnik dtoCis : dtoMeta.getCiselnikPole()) {
				if (_CudConsts.TABULKA_T_DOPRAVNY_BOD.equals(dtoCis.getTabulka())) {
					List<DTOCiselnikStlpec> list = dtoMeta.getCiselnikStlpecMap().get(dtoCis.getCiselnikID());
					DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(list, _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV);
					resultSet.add(dtoCS.getCiselnikStlpecID());

				} else if (_CudConsts.TABULKA_T_HRANICNY_PRIECHOD.equals(dtoCis.getTabulka())) {
					List<DTOCiselnikStlpec> list = dtoMeta.getCiselnikStlpecMap().get(dtoCis.getCiselnikID());
					DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(list, _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV);
					resultSet.add(dtoCS.getCiselnikStlpecID());
				}
			}

			return resultSet;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getCiselnikStlpecSpecialFkSetID.error");
			return null;
		}
	}

	public DTOCiselnik[] ciselnikList(AuthInfo auth) throws AppException {

		try {
			DTOCiselnik dtoF = new DTOCiselnik();
			dtoF.setTyp(_CudConsts.CISELNIK_TYP_TECHNICKY);

			return dlg.getCiselnikRead().listLight(auth, dtoF);

		} catch (Throwable t) {
			handleException(t, "ciselnikList.error", auth);
			return null;
		}
	}

	public List<DTOZmena> zmenaListSchvalene(AuthInfo auth) throws AppException {

		try {
			DTOZmena dtoF = new DTOZmena();
			dtoF.setStav(_CudConsts.ZMENA_STAV_SCH);

			return dlg.getZmenaRead().listLight(auth, dtoF);

		} catch (Throwable t) {
			DBUtils.handleException(t, "zmenaListSchvalene.error");
			return null;
		}
	}

	public Integer lookupFkCiselnikStlpecID(List<DTOZmenaStlpec> list, Set<Integer> set) throws AppException {

		try {
			for (DTOZmenaStlpec dto : list) {
				if (set.contains(dto.getIDCiselnikStlpec())) {
					return dto.getIDCiselnikStlpec();
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupFkCiselnikStlpecID.error");
			return null;
		}
	}

	public Integer lookupCiselnikStlpecID(List<DTOCiselnikStlpec> list, Set<Integer> set) throws AppException {

		try {
			for (DTOCiselnikStlpec dto : list) {
				if (set.contains(dto.getCiselnikStlpecID())) {
					return dto.getCiselnikStlpecID();
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupCiselnikStlpecID.error");
			return null;
		}
	}

	public Integer lookupDopravnyNazovID(DTOPau dtoPA, Set<Integer> set) throws AppException {

		try {
			String stlpecNazov = null;
			for (DTOCiselnikStlpec dto : dtoPA.getCiselnikStlpecList()) {
				if (set.contains(dto.getCiselnikStlpecID())) {
					stlpecNazov = dto.getNazov();
					break;
				}
			}

			String rowID = dtoPA.getRowOldMap().get(stlpecNazov);
			if (StringUtils.isValid(rowID)) {
				return Integer.parseInt(rowID);
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDopravnyNazovIDgetIDDopravnyNazovOldValue.error");
			return null;
		}
	}

	public void kontrolaWfTodoList(List<DTOWfDef> wfDefList, List<DTOWfTodo> wfTodoList) throws AppException {

		try {
			// kontroluje ci vlastne existuje nejaky todo list
			if (!StringUtils.isValid(wfTodoList) || wfTodoList.isEmpty()) {
				throw new AppException("V tabulke CUD_WF_TODO nie su definovane zaznamy pre tuto zmenu, vykonavanie procesu konci.");
			}

			// kontrola ci su vygenerovane vsetky zaznamy v tabulke CUD_WF_TODO podla tabulky CUD_WF_DEF
			for (DTOWfDef dtoDef : wfDefList) {
				boolean b = false;
				for (DTOWfTodo dtoTodo : wfTodoList) {
					if (dtoDef.getWfDefID().intValue() == dtoTodo.getIDWfDef().intValue()) {
						b = true;
					}
				}
				if (!b) {
					throw new AppException("Zle vygenerovany workflow v tabulke CUD_WF_TODO, chyba zaznam pre wfDefID==" + dtoDef.getWfDefID() + ", vykonavanie procesu konci.");
				}
			}

			// kontrola ci su vykonane vsetky potrebne kroky pre PAU, to ze sa nejdna o PAU je identifikovane tak ze CUD_WF_DEF.TYP == OV
			List<DTOWfTodo> list = new ArrayList<DTOWfTodo>();
			for (DTOWfTodo dto : wfTodoList) {
				if (!"T".equals(dto.getPotvrdeny())) {
					list.add(dto);
				}
			}
			for (DTOWfTodo dto : list) {
				DTOWfDef dtoDef = _CudLookupUtils.lookupDTOWfDef(wfDefList, dto.getIDWfDef());
				if (!_CudConsts.WF_DEF_TYP_OV.equals(dtoDef.getTyp())) {
					throw new AppException("V tabulke CUD_WF_TODO sa nachadza nepotvrdeny zaznam wfDefID==" + dtoDef.getWfDefID()
							+ ", chyba vo workflow vykonavanie procesu konci.");
				}
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaWfTodoList.error");
		}
	}

	public String lookupOldValue(List<DTOZmenaStlpec> zmenaStlpecList, Integer ciselnikStlpecID) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikStlpecID)) {
				for (DTOZmenaStlpec dto : zmenaStlpecList) {
					if (ciselnikStlpecID.equals(dto.getIDCiselnikStlpec())) {
						return dto.getOldValue();
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupOldValue.error");
			return null;
		}
	}

	public String lookupNewValue(List<DTOZmenaStlpec> zmenaStlpecList, Set<Integer> set) throws AppException {

		try {
			for (DTOZmenaStlpec dto : zmenaStlpecList) {
				if (set.contains(dto.getIDCiselnikStlpec())) {
					return dto.getNewValue();
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupNewValue.error");
			return null;
		}
	}

	public String lookupPkNazov(List<DTOCiselnikStlpec> list) throws AppException {

		try {
			DTOCiselnikStlpec dto = _CudLookupUtils.lookupDTOCiselnikStlpecPk(list);
			return StringUtils.isValid(dto) ? dto.getNazov() : null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupPkNazov.error");
			return null;
		}
	}

	/**
	 * Funkcia zisti ci sa v registry zmien nachadza nejaky zaznam z ciselnika T_DOPRAVNY_NAZOV v stave VPO alebo SCH.
	 * 
	 * @param auth
	 * @param rowID
	 * @return
	 * @throws AppException
	 */
	public boolean jeNezamietnutaPoziadavkaNaPrimarnyKlucDopravnehoNazovu(AuthInfo auth, String rowID) throws AppException {

		try {
			Integer pocet = dlg.getZmenaRead().getPocetNepublikovanychZaznamov(auth, _CudConsts.ID_T_DOPRAVNY_NAZOV, new Integer(rowID), null);
			return pocet.intValue() > 0;

		} catch (Throwable t) {
			handleException(t, "jeNezamietnutaPoziadavkaNaPrimarnyKlucDopravnehoNazovu.error", auth);
			return false;
		}
	}

	public DTOPau createDTOPrimarnaAktualizacia(DTOZmena dto, List<DTOZmenaStlpec> zmenaStlpecList, Date d) throws AppException {

		try {
			DTOPau dtoPA = new DTOPau();
			dtoPA.setZmena(dto);
			dtoPA.setZmenaStlpecList(zmenaStlpecList);
			dtoPA.setCasVytvorenia(d);
			return dtoPA;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOPrimarnaAktualizacia.error");
			return null;
		}
	}

	public DTOZmena createDTOZmena(Integer ciselnikID, String operacia, Date platnostOd, Integer rowID) throws AppException {

		try {
			DTOZmena dtoNew = new DTOZmena();
			dtoNew.setIDCiselnik(ciselnikID);
			dtoNew.setOperacia(operacia);
			dtoNew.setStav(_CudConsts.ZMENA_STAV_VPO);
			dtoNew.setPlatnostOd(platnostOd);
			dtoNew.setRowID(rowID);
			return dtoNew;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOZmena.error");
			return null;
		}
	}

	public DTOZmenaStlpec createDTOZmenaStlpec(Integer ciselnikID, Integer zmenaID, Integer ciselnikStlpecID, String oldValue, String newValue) throws AppException {

		try {
			DTOZmenaStlpec dtoNew = new DTOZmenaStlpec();
			dtoNew.setIDCiselnik(ciselnikID);
			dtoNew.setIDZmena(zmenaID);
			dtoNew.setIDCiselnikStlpec(ciselnikStlpecID);
			dtoNew.setOldValue(oldValue);
			dtoNew.setNewValue(newValue);
			return dtoNew;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOZmenaStlpec.error");
			return null;
		}
	}

	public List<DTOZmenaStlpec> createListZmenaStlpec(Integer ciselnikID, Integer zmenaID, Integer ciselnikStlpecID, String oldValue, String newValue) throws AppException {

		try {
			List<DTOZmenaStlpec> list = new ArrayList<DTOZmenaStlpec>();
			list.add(createDTOZmenaStlpec(ciselnikID, zmenaID, ciselnikStlpecID, oldValue, newValue));
			return list;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createListZmenaStlpec.error");
			return null;
		}
	}

	public void generujZmenaStlpecDTO(DTOPau dtoPA, Integer ciselnikStlpecID, String oldValue, String newValue) throws AppException {

		try {
			DTOZmenaStlpec dto = createDTOZmenaStlpec(dtoPA.getCiselnik().getCiselnikID(), dtoPA.getZmena().getZmenaID(), ciselnikStlpecID, oldValue, newValue);
			if (!StringUtils.isValid(dtoPA.getZmenaStlpecList())) {
				dtoPA.setZmenaStlpecList(new ArrayList<DTOZmenaStlpec>());
			}
			dtoPA.getZmenaStlpecList().add(dto);

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujZmenaStlpecDTO.error");
		}
	}

	/**
	 * Tato funkcia dogeneruje zaznamy do tabulky CUD_ZMENA_STLPEC, pre ciselnik ciselniID. Tato zmena je dynamicka, cize neexistuje ziaden zaznam v tabulke CUD_ZMENA. Najskor sa
	 * zisti ktore atributy sa maju pridadit do zmeny, potom sa nacitaju aktulne hodnoty v tabulke (T_), a az potom sa vygeneruju zmeny pre "vsetky" stlpce.
	 * 
	 * @param auth
	 * @param ciselnikID
	 *            ciselnik, pre ktory sa maju vygenerovat zmeny
	 * @param rowID
	 *            ID zaznamu
	 * @param dtoMeta
	 *            meta data vsetkych ciselnikov
	 * @param ciselnikStlpecSpecFkSet
	 *            zoznam ID-ciek z tabulky CUD_CISELNIK_STLPEC, ktore zodpovedaju stlpcu ID_DOPRAVNY_NAZOV. Pre tento stlpec sa nemusia generovat zmeny, tieto zmeny sa dogeneruju v
	 *            inom kroku
	 * @return
	 * @throws AppException
	 */
	public List<DTOZmenaStlpec> generujZmenaStlpecList(AuthInfo auth, Integer ciselnikID, Integer rowID, DTOMeta dtoMeta, Set<Integer> ciselnikStlpecSpecFkSet, Date platnostOd)
			throws AppException {

		try {

			DTOCiselnikGui dtoCisGui = _CudLookupUtils.lookupDTOCiselnikGuiByFk(dtoMeta.getCiselnikGuiPole(), ciselnikID);
			List<DTOCiselnikStlpecGui> metaList = dtoMeta.getCiselnikStlpecGuiMap().get(dtoCisGui.getCiselnikGuiID());

			List<DTOCiselnikStlpec> csList = dtoMeta.getCiselnikStlpecMap().get(ciselnikID);
			DTOCiselnikStlpec dtoCsPk = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList);
			DTOCiselnik dtoCiselnik = _CudLookupUtils.lookupDTOCiselnik(dtoMeta.getCiselnikPole(), ciselnikID);

			Set<String> set = new HashSet<String>();
			for (DTOCiselnikStlpecGui dto : metaList) {

				if (ciselnikStlpecSpecFkSet.contains(dto.getIDCiselnikStlpec())) {
					continue;
				}

				if ("T".equals(dto.getFormZobrazenie())) {
					DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(csList, dto.getIDCiselnikStlpec());
					set.add(dtoCS.getNazov());
				}
			}

			List<DTOZmenaStlpec> resultList = new ArrayList<DTOZmenaStlpec>();

			Map<String, String> rowMap = dlg.getDynCiselnikRead().readLight(auth, dtoCiselnik.getTabulka(), csList, dtoCsPk.getNazov(), rowID.toString(), dtoCsPk.getDbTyp(),
					platnostOd, "F");

			for (String stlpecNazov : rowMap.keySet()) {

				if (!set.contains(stlpecNazov)) {
					continue;
				}

				String value = rowMap.get(stlpecNazov);
				if (!StringUtils.isValid(value)) {
					continue;
				}

				DTOCiselnikStlpec dto = _CudLookupUtils.lookupDTOCiselnikStlpec(csList, stlpecNazov);
				if (!StringUtils.isValid(dto)) {
					continue;
				}

				DTOZmenaStlpec dtoNew = createDTOZmenaStlpec(ciselnikID, null, dto.getCiselnikStlpecID(), value, null);
				resultList.add(dtoNew);
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujZmenaStlpecList.error");
			return null;
		}
	}

	public List<DTOZmenaStlpec> getZmenaStlpecList(DTOPau dtoPA, Map<String, String> rowOld) throws AppException {

		try {
			List<DTOZmenaStlpec> resultList = new ArrayList<DTOZmenaStlpec>();

			Set<String> set = new HashSet<String>();
			set.add(_CudConsts.NAZOV_PK_KEY);
			set.add(_CudConsts.NAZOV_ZMAZ);
			set.add(_CudConsts.NAZOV_HIST_ID);
			set.add(_CudConsts.NAZOV_ID_ZMENA);
			set.add(_CudConsts.NAZOV_PLATNOST_OD);
			set.add(_CudConsts.NAZOV_PLATNOST_DO);
			set.add(_CudConsts.NAZOV_CAS_VYTVORENIA);
			set.add(_CudConsts.NAZOV_CAS_ZMENY);
			set.add(lookupPkNazov(dtoPA.getCiselnikStlpecList()));

			// ak existuje povodna hodnota, tak sa priradi ako CUD_ZMENA_STLPEC.OLD_VALUE
			for (DTOZmenaStlpec dto : dtoPA.getZmenaStlpecList()) {
				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), dto.getIDCiselnikStlpec());
				String oldValue = rowOld.get(dtoCS.getNazov());
				if (StringUtils.isValid(oldValue) && !"T".equals(dtoCS.getJedinecny())) {
					dto.setOldValue(oldValue);
					resultList.add(dto);
				}
				set.add(dtoCS.getNazov());
			}

			for (String stlpecNazov : rowOld.keySet()) {
				if (!set.contains(stlpecNazov)) {
					DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), stlpecNazov);
					if (!"T".equals(dtoCS.getJedinecny())) {
						DTOZmenaStlpec dtoNew = createDTOZmenaStlpec(dtoPA.getZmena().getIDCiselnik(), dtoPA.getZmena().getZmenaID(), dtoCS.getCiselnikStlpecID(),
								rowOld.get(stlpecNazov), null);
						resultList.add(dtoNew);
					}
				}
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getZmenaStlpecList.error");
			return null;
		}
	}

	public Integer getCiselnikStlpecID(Map<Integer, List<DTOCiselnikStlpec>> ciselnikStlpecMap, Integer ciselnikID, String stlpecNazov) throws AppException {

		try {
			List<DTOCiselnikStlpec> ciselnikStlpecList = ciselnikStlpecMap.get(ciselnikID);
			DTOCiselnikStlpec dto = _CudLookupUtils.lookupDTOCiselnikStlpec(ciselnikStlpecList, stlpecNazov);
			if (StringUtils.isValid(dto)) {
				return dto.getCiselnikStlpecID();
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getCiselnikStlpecID.error");
			return null;
		}
	}

	public boolean jeNezamietnutaPoziadavkaNaNazovDopravnehoNazovu(AuthInfo auth, Map<Integer, List<DTOCiselnikStlpec>> ciselnikStlpecMap, String newValue) throws AppException {

		try {
			Integer ciselnikStlpecID = getCiselnikStlpecID(ciselnikStlpecMap, _CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_NAZOV);
			Integer pocet = dlg.getZmenaStlpecRead().getPocetNepublikovanychZaznamov(auth, _CudConsts.ID_T_DOPRAVNY_NAZOV, ciselnikStlpecID, newValue);
			return pocet.intValue() > 0;

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeNezamietnutaPoziadavkaNaNazovDopravnehoNazovu.error");
			return false;
		}
	}

	public Integer getPocetVaziebNaZaznam(AuthInfo auth, Integer rowID, Set<String> set, Date platnostOd) throws AppException {

		try {
			if (!StringUtils.isValid(rowID)) {
				return 0;
			}

			Map<String, String> mapa = new HashMap<String, String>();

			String date = sk.ditec.zsr.common.server.utils.DateUtils.formatDateDDMMYYYY(platnostOd);
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " <= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			for (String tabulkaNazov : set) {
				String s = _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV + " = " + rowID + " AND " + _CudConsts.NAZOV_ZMAZ + " = \'F\' AND " + dateConditions;
				mapa.put(tabulkaNazov, s);
			}

			return dlg.getDynCiselnikRead().getPocetVaziebNaZaznam(auth, mapa);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getPocetVaziebNaZaznam.error");
			return null;
		}
	}

	public Integer getPocetVaziebNaZaznam(AuthInfo auth, Integer ciselnikID, Integer rowID, Integer ciselnikIDNext, Integer rowIDNext, String pkNazovNext, DTOMeta dtoMeta,
			Date platnostOd) throws AppException {

		try {
			Map<String, String> mapa = new HashMap<String, String>();

			// vytvorenie podmienky
			String date = sk.ditec.zsr.common.server.utils.DateUtils.formatDateDDMMYYYY(platnostOd);
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " <= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			for (Integer ciselnikIDkey : dtoMeta.getCiselnikStlpecMap().keySet()) {

				String conditional = "";

				int pocet = 0;
				List<DTOCiselnikStlpec> ciselnikStlpecList = dtoMeta.getCiselnikStlpecMap().get(ciselnikIDkey);
				for (DTOCiselnikStlpec dto : ciselnikStlpecList) {
					if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dto.getTyp())) {
						if (ciselnikID.intValue() == dto.getFk1IDCiselnik().intValue()) {
							DTOCiselnikGui dtoCisGui = _CudLookupUtils.lookupDTOCiselnikGuiByFk(dtoMeta.getCiselnikGuiPole(), ciselnikIDkey);
							List<DTOCiselnikStlpecGui> metaList = dtoMeta.getCiselnikStlpecGuiMap().get(dtoCisGui.getCiselnikGuiID());
							DTOCiselnikStlpecGui dtoCSGUI = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, dto.getCiselnikStlpecID());
							if ("T".equals(dtoCSGUI.getFormZobrazenie())) {
								String s = dto.getNazov() + " = " + rowID;
								conditional += StringUtils.isValid(conditional) ? " OR " + s : s;
								pocet++;
							}
						}
					}
				}

				if (pocet == 0) {
					continue;
				}

				if (pocet == 1) {
					conditional += " AND " + _CudConsts.NAZOV_ZMAZ + " = \'F\' AND " + dateConditions;
				} else {
					conditional = "(" + conditional + ") AND " + _CudConsts.NAZOV_ZMAZ + " = \'F\' AND " + dateConditions;
				}
				if (ciselnikIDkey.intValue() == ciselnikID.intValue()) {
					String pkNazov = lookupPkNazov(ciselnikStlpecList);
					conditional += " AND " + pkNazov + " <> " + rowID;

				} else if (StringUtils.isValid(ciselnikIDNext) && ciselnikIDNext.intValue() == ciselnikIDkey.intValue()) {
					conditional += " AND " + pkNazovNext + " <> " + rowIDNext;
				}

				DTOCiselnik dtoCiselnik = _CudLookupUtils.lookupDTOCiselnik(dtoMeta.getCiselnikPole(), ciselnikIDkey);
				mapa.put(dtoCiselnik.getTabulka(), conditional);
			}

			return dlg.getDynCiselnikRead().getPocetVaziebNaZaznam(auth, mapa);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getPocetVaziebNaZaznam.error");
			return null;
		}
	}

	public boolean jeVZmeneStlpecReferencujuciZmazanuHodnotu(AuthInfo auth, DTOPau dtoPA, DTOCiselnik[] ciselnikPole, Set<Integer> ciselnikStlpecSpecFkSet,
			Set<String> specialValuesSet) throws AppException {

		try {
			for (DTOZmenaStlpec dto : dtoPA.getZmenaStlpecList()) {

				// ak ide o specialny pripad, nemusim kontrolovat vazbu na existujuci zaznam
				if (ciselnikStlpecSpecFkSet.contains(dto.getIDCiselnikStlpec())) {
					continue;
				}

				// ak sa meni na NULL, nemusim kontrolovat vazbu na existujuci zaznam
				if (!StringUtils.isValid(dto.getNewValue())) {
					continue;
				}

				// ak sa meni na hodnotu ktoru este neviem, mam pouzity specialny znak, nemusi sa kontrolovat
				if (specialValuesSet.contains(dto.getNewValue())) {
					continue;
				}

				// samotna kontrola na existujuci zaznam
				DTOCiselnikStlpec dtoCiselnikStlpec = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), dto.getIDCiselnikStlpec());
				if (StringUtils.isValid(dtoCiselnikStlpec.getFk1IDCiselnik())) {
					DTOCiselnik dtoCiselnik = _CudLookupUtils.lookupDTOCiselnik(ciselnikPole, dtoCiselnikStlpec.getFk1IDCiselnik());
					if (dlg.getDynCiselnikRead().jeZaznamZmazany(auth, dtoCiselnik.getTabulka(), dtoCiselnikStlpec.getFk1PkNazov(), Integer.parseInt(dto.getNewValue()),
							dtoPA.getZmena().getPlatnostOd())) {
						log.error("V atribute zmeny zmenaStlpecID=={} sa nachadza zaznam referencujuci na zmazanu hodnotu!", dto.getZmenaStlpecID());
						return true;
					}
				}
			}

			return false;

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeVZmeneStlpecReferencujuciZmazanuHodnotu.error");
			return false;
		}
	}

	public Map<String, String> dynCiselnikReadJedinecny(AuthInfo auth, DTOPau dtoPA) throws AppException {

		try {
			if (!StringUtils.isValid(dtoPA.getCiselnikStlpecJedinecny())) {
				return new HashMap<String, String>();
			}

			String value = null;
			for (DTOZmenaStlpec dto : dtoPA.getZmenaStlpecList()) {
				if (dto.getIDCiselnikStlpec().intValue() == dtoPA.getCiselnikStlpecJedinecny().getCiselnikStlpecID().intValue()) {
					// je uplne jedno ci NEW_VALUE alebo OLD_VALUE, musia byt rovnake
					value = dto.getNewValue();
					break;
				}
			}

			if (!StringUtils.isValid(value)) {
				return new HashMap<String, String>();
			}

			return dlg.getDynCiselnikRead().readLight(auth, dtoPA.getCiselnik().getTabulka(), dtoPA.getCiselnikStlpecList(), dtoPA.getCiselnikStlpecJedinecny().getNazov(), value,
					dtoPA.getCiselnikStlpecJedinecny().getDbTyp(), dtoPA.getZmena().getPlatnostOd(), null);

		} catch (Throwable t) {
			DBUtils.handleException(t, "dynCiselnikReadJedinecny.error");
			return null;
		}
	}

	/**
	 * Funkcia nacita zaznam z ciselnika T_DOPRAVNY_NAZOV, filtruje sa podla atributu T_DOPRAVNY_NAZOV.NAZOV.
	 * 
	 * @param auth
	 * @param dtoMeta
	 *            metadata ciselnikov
	 * @param value
	 *            hodnota podla ktorej sa filtruje, kontretne nazov z tabulku T_DOPRAVNY_NAZOV
	 * @return
	 * @throws AppException
	 */
	public Map<String, String> ciselnikDopravnyNazovReadByNazov(AuthInfo auth, DTOMeta dtoMeta, String value, Date platnostOd) throws AppException {

		try {
			DTOCiselnik dtoCis = _CudLookupUtils.lookupDTOCiselnik(dtoMeta.getCiselnikPole(), _CudConsts.TABULKA_T_DOPRAVNY_NAZOV);
			List<DTOCiselnikStlpec> csList = dtoMeta.getCiselnikStlpecMap().get(dtoCis.getCiselnikID());

			return dlg.getDynCiselnikRead().readLight(auth, dtoCis.getTabulka(), csList, _CudConsts.NAZOV_NAZOV, value, _CudConsts.DB_TYP_STRING, platnostOd, null);

		} catch (Throwable t) {
			DBUtils.handleException(t, "ciselnikDopravnyNazovReadByNazov.error");
			return null;
		}
	}

	public void kontrolaOldValues(DTOPau dtoPA) throws AppException {

		try {
			for (DTOZmenaStlpec dto : dtoPA.getZmenaStlpecList()) {
				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), dto.getIDCiselnikStlpec());
				String oldValue = dtoPA.getRowOldMap().get(dtoCS.getNazov());
				if (_CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getDbTyp()) && StringUtils.isValid(oldValue)) {
					oldValue = new Double(Double.parseDouble(oldValue)).toString();
				}

				if (StringUtils.isValid(oldValue) && !oldValue.equals(dto.getOldValue())) {
					throw new AppException("V zmene zmenaStlpecID==" + dto.getZmenaStlpecID() + " je neplatna hodnota s povodnou hodnotou v ciselniku! Vykonavanie procesu konci!");
				} else if (StringUtils.isValid(dto.getOldValue()) && !dto.getOldValue().equals(oldValue)) {
					throw new AppException("V zmene zmenaStlpecID==" + dto.getZmenaStlpecID() + " je neplatna hodnota s povodnou hodnotou v ciselniku! Vykonavanie procesu konci!");
				}
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaOldValues.error");
		}
	}

	public void kontrolaPlatnostiVaziebMimoZmeny(AuthInfo auth, DTOPau dtoPA, DTOMeta dtoMeta, Set<Integer> ciselnikStlpecSpecialFkSet) throws AppException {

		try {
			// ziska sa zoznam stlpcov, ktore su FK, zobrazuju sa vo formulary a nie su zahrnute v zmene
			List<DTOCiselnikStlpec> csList = new ArrayList<DTOCiselnikStlpec>();
			for (DTOCiselnikStlpec dto : dtoPA.getCiselnikStlpecList()) {
				if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dto.getTyp())) {

					if (ciselnikStlpecSpecialFkSet.contains(dto.getCiselnikStlpecID())) {
						if ("T".equals(dtoPA.getObnovenieDopravnehoNazvu())) {
							continue;
						}
					}

					DTOCiselnikStlpecGui dtoCSGui = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(dtoPA.getCiselnikStlpecGuiList(), dto.getCiselnikStlpecID());
					if (StringUtils.isValid(dtoCSGui) && "T".equals(dtoCSGui.getFormZobrazenie())) {
						if (StringUtils.isValid(dtoPA.getRowOldMap().get(dto.getNazov()))) {
							DTOZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOZmenaStlpecByFk(dtoPA.getZmenaStlpecList(), dto.getCiselnikStlpecID());
							if (!StringUtils.isValid(dtoZS)) {
								csList.add(dto);
							}
						}
					}
				}
			}
			if (csList.isEmpty()) {
				return;
			}

			// vytvorenie podmienky
			String date = sk.ditec.zsr.common.server.utils.DateUtils.formatDateDDMMYYYY(dtoPA.getZmena().getPlatnostOd());
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " <= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			Map<String, String> mapa = new HashMap<String, String>();
			for (DTOCiselnikStlpec dto : csList) {
				DTOCiselnik dtoCis = _CudLookupUtils.lookupDTOCiselnik(dtoMeta.getCiselnikPole(), dto.getFk1IDCiselnik());
				String value = dtoPA.getRowOldMap().get(dto.getNazov());
				if (dto.getIDCiselnik().intValue() == dto.getFk1IDCiselnik().intValue()) {
					if (dtoPA.getZmena().getRowID().intValue() == Integer.parseInt(value)) {
						// tu je osetrena situalcia, ked zaznam ukazuje sam na seba => nekontrolovat
						continue;
					}
				}
				String sql = "(SELECT count(*) FROM " + dtoCis.getTabulka() + " WHERE " + dto.getFk1PkNazov() + " = " + value + " AND ZMAZ = \'F\' AND " + dateConditions
						+ " ) AS " + dto.getNazov();
				mapa.put(dto.getNazov(), sql);
			}

			if (mapa.keySet().isEmpty()) {
				return;
			}

			String[] pole = dlg.getDynCiselnikRead().getZoznamZmazanychAtributov(auth, mapa);
			if (StringUtils.isValid(pole)) {
				throw new AppException("V atributoch(FK): " + Arrays.toString(pole) + " sa nachadzaju hodnoty, ktore su oznacene za zmazane. Vykonavanie procesu konci!");
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaPlatnostiVaziebMimoZmeny.error");
		}
	}

	public String generujUpdateSql(DTOPau dtoPA, Map<String, String> row, Set<Integer> ciselnikStlpecSpecFkSet) throws AppException {

		try {
			Map<String, String> mapa = new HashMap<String, String>();

			for (String stlpecNazov : row.keySet()) {

				if (_CudConsts.NAZOV_PK_KEY.equals(stlpecNazov)) {
					continue;
				}

				mapa.put(stlpecNazov, row.get(stlpecNazov));
			}

			mapa.put(_CudConsts.NAZOV_HIST_ID, null);
			mapa.put(_CudConsts.NAZOV_ZMAZ, "F");
			mapa.put(_CudConsts.NAZOV_PLATNOST_OD, _CudConsts.DATE_FORMAT.format(dtoPA.getZmena().getPlatnostOd()));
			mapa.put(_CudConsts.NAZOV_PLATNOST_DO, null);
			mapa.put(_CudConsts.NAZOV_CAS_VYTVORENIA, _CudConsts.DATE_TIME_FORMAT.format(dtoPA.getCasVytvorenia()));
			mapa.put(_CudConsts.NAZOV_ID_ZMENA, _CudConsts.PK_ZMENA);
			mapa.put(_CudConsts.NAZOV_ZMAZ, "F");

			for (DTOZmenaStlpec dto : dtoPA.getZmenaStlpecList()) {
				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), dto.getIDCiselnikStlpec());
				mapa.put(dtoCS.getNazov(), dto.getNewValue());
			}

			String expressions = "";
			String columns = "";
			Set<String> set = new HashSet<String>();
			for (DTOCiselnikStlpec dto : dtoPA.getCiselnikStlpecList()) {

				if (set.contains(dto.getNazov())) {
					continue;
				}
				set.add(dto.getNazov());

				String value = mapa.get(dto.getNazov());
				if (!StringUtils.isValid(value)) {
					continue;
				}

				if (StringUtils.isValid(columns)) {
					columns += ", ";
				}
				columns += dto.getNazov();

				if (StringUtils.isValid(expressions)) {
					expressions += ", ";
				}

				if (_CudConsts.DB_TYP_INTEGER.equals(dto.getDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dto.getDbTyp()) || _CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dto.getTyp())) {
					expressions += value;

				} else if (_CudConsts.DB_TYP_STRING.equals(dto.getDbTyp()) || _CudConsts.DB_TYP_BOOLEAN.equals(dto.getDbTyp())) {
					expressions += "\'" + value + "\'";

				} else if (_CudConsts.NAZOV_CAS_VYTVORENIA.equals(dto.getNazov())) {
					expressions += "to_timestamp(\'" + value + "\', \'DD.MM.YYYY HH24:MI:SS.FF\')";

				} else if (_CudConsts.DB_TYP_DATE.equals(dto.getDbTyp())) {
					expressions += "to_timestamp(\'" + value + "\', \'DD.MM.YYYY\')";
				}
			}

			return "INSERT INTO " + dtoPA.getCiselnik().getTabulka() + " (" + columns + ") VALUES (" + expressions + ")";

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujUpdateSql.error");
			return null;
		}
	}

	public String generujInsertSql(DTOPau dtoPA) throws AppException {

		try {
			Map<String, String> mapa = new HashMap<String, String>();

			mapa.put(_CudConsts.NAZOV_PLATNOST_OD, _CudConsts.DATE_FORMAT.format(dtoPA.getZmena().getPlatnostOd()));
			mapa.put(_CudConsts.NAZOV_CAS_VYTVORENIA, _CudConsts.DATE_TIME_FORMAT.format(dtoPA.getCasVytvorenia()));
			mapa.put(_CudConsts.NAZOV_ID_ZMENA, _CudConsts.PK_ZMENA);
			mapa.put(_CudConsts.NAZOV_ZMAZ, "F");
			mapa.put(dtoPA.getCiselnikStlpecPk().getNazov(), _CudConsts.PK_VALUE);

			for (DTOZmenaStlpec dto : dtoPA.getZmenaStlpecList()) {
				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), dto.getIDCiselnikStlpec());
				if (mapa.keySet().contains(dtoCS.getNazov())) {
					continue;
				}
				mapa.put(dtoCS.getNazov(), dto.getNewValue());
			}

			String columns = "";
			String expressions = "";
			for (String stlpecNazov : mapa.keySet()) {

				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), stlpecNazov);
				String value = mapa.get(stlpecNazov);
				if (value.contains("\'")) {
					value = value.replaceAll("\'", "\'\'");
				}

				if (StringUtils.isValid(columns)) {
					columns += ", ";
				}
				columns += dtoCS.getNazov();

				if (StringUtils.isValid(expressions)) {
					expressions += ", ";
				}
				if (_CudConsts.DB_TYP_INTEGER.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getDbTyp())
						|| _CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getTyp())) {
					expressions += value;

				} else if (_CudConsts.DB_TYP_STRING.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getDbTyp())) {
					expressions += "\'" + value + "\'";

				} else if (_CudConsts.NAZOV_CAS_VYTVORENIA.equals(dtoCS.getNazov())) {
					expressions += "to_timestamp(\'" + value + "\', \'DD.MM.YYYY HH24:MI:SS.FF\')";

				} else if (_CudConsts.DB_TYP_DATE.equals(dtoCS.getDbTyp())) {
					expressions += "to_timestamp(\'" + value + "\', \'DD.MM.YYYY\')";
				}
			}

			return "INSERT INTO " + dtoPA.getCiselnik().getTabulka() + " (" + columns + ") VALUES (" + expressions + ")";

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujInsertSql.error");
			return null;
		}
	}

	public String generujZneplatnenieSql(DTOPau dtoPA) throws AppException {

		try {
			Map<String, String> mapa = new HashMap<String, String>();

			mapa.put(_CudConsts.NAZOV_PLATNOST_OD, _CudConsts.DATE_FORMAT.format(dtoPA.getZmena().getPlatnostOd()));
			mapa.put(_CudConsts.NAZOV_CAS_VYTVORENIA, _CudConsts.DATE_TIME_FORMAT.format(dtoPA.getCasVytvorenia()));
			mapa.put(_CudConsts.NAZOV_ID_ZMENA, _CudConsts.PK_ZMENA);
			mapa.put(_CudConsts.NAZOV_ZMAZ, "T");
			mapa.put(dtoPA.getCiselnikStlpecPk().getNazov(), dtoPA.getRowOldMap().get(dtoPA.getCiselnikStlpecPk().getNazov()));

			for (DTOZmenaStlpec dto : dtoPA.getZmenaStlpecList()) {
				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), dto.getIDCiselnikStlpec());
				if (mapa.keySet().contains(dtoCS.getNazov())) {
					continue;
				}
				mapa.put(dtoCS.getNazov(), dto.getOldValue());
			}

			String columns = "";
			String expressions = "";
			for (String stlpecNazov : mapa.keySet()) {

				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(dtoPA.getCiselnikStlpecList(), stlpecNazov);
				String value = mapa.get(stlpecNazov);

				if (StringUtils.isValid(columns)) {
					columns += ", ";
				}
				columns += dtoCS.getNazov();

				if (StringUtils.isValid(expressions)) {
					expressions += ", ";
				}
				if (_CudConsts.DB_TYP_INTEGER.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getDbTyp())
						|| _CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getTyp())) {
					expressions += value;

				} else if (_CudConsts.DB_TYP_STRING.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getDbTyp())) {
					expressions += "\'" + value + "\'";

				} else if (_CudConsts.NAZOV_CAS_VYTVORENIA.equals(dtoCS.getNazov())) {
					expressions += "to_timestamp(\'" + value + "\', \'DD.MM.YYYY HH24:MI:SS.FF\')";

				} else if (_CudConsts.DB_TYP_DATE.equals(dtoCS.getDbTyp())) {
					expressions += "to_timestamp(\'" + value + "\', \'DD.MM.YYYY\')";
				}
			}

			return "INSERT INTO " + dtoPA.getCiselnik().getTabulka() + " (" + columns + ") VALUES (" + expressions + ")";

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujZneplatnenieSql.error");
			return null;
		}
	}

	public String generujDeleteSql(DTOPau dtoPA) throws AppException {

		try {
			String histID = dtoPA.getRowOldMap().get(_CudConsts.NAZOV_HIST_ID);
			String column1 = _CudConsts.NAZOV_PLATNOST_DO + " = to_timestamp(\'" + _CudConsts.DATE_FORMAT.format(dtoPA.getZmena().getPlatnostDo())
					+ "\', \'DD.MM.YYYY HH24:MI:SS.FF\')";
			String column2 = _CudConsts.NAZOV_CAS_ZMENY + " = to_timestamp(\'" + _CudConsts.DATE_TIME_FORMAT.format(dtoPA.getCasVytvorenia()) + "\', \'DD.MM.YYYY HH24:MI:SS.FF\')";
			String condition = _CudConsts.NAZOV_HIST_ID + " = " + histID;
			return "UPDATE " + dtoPA.getCiselnik().getTabulka() + " SET " + column1 + ", " + column2 + " WHERE " + condition;

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujDeleteSql.error");
			return null;
		}
	}

	public Date getPosunDatum(Date d, Integer posun) throws AppException {

		try {
			Calendar cal = Calendar.getInstance(new Locale("sk", "SK"));
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_YEAR, posun);
			return cal.getTime();

		} catch (Throwable t) {
			DBUtils.handleException(t, "getPosunDatum.error");
			return null;
		}
	}

	public DTOWfTodo lookupDTOWfTodoByStav(List<DTOWfDef> wfDefList, List<DTOWfTodo> wfTodoList, String typ) throws AppException {

		try {
			DTOWfDef dtoPAU = null;
			for (DTOWfDef dto : wfDefList) {
				if (typ.equals(dto.getTyp())) {
					dtoPAU = dto;
					break;
				}
			}

			if (!StringUtils.isValid(dtoPAU)) {
				return null;
			}

			for (DTOWfTodo dto : wfTodoList) {
				if (dto.getIDWfDef().intValue() == dtoPAU.getWfDefID().intValue()) {
					return dto;
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOWfTodoByStav.error");
			return null;
		}
	}

	public void wfDefSort(Map<Integer, List<DTOWfDef>> wfDefMap) throws AppException {

		try {
			List<DTOWfDef> wfDefListSort = new ArrayList<DTOWfDef>();

			for (Integer ciselnikID : wfDefMap.keySet()) {

				wfDefListSort.clear();

				List<DTOWfDef> wfDefList = wfDefMap.get(ciselnikID);

				// najskor vlozim ten, ktory nema definovaneho nasledovnika
				for (DTOWfDef dto : wfDefList) {
					if (!StringUtils.isValid(dto.getIDWfDefNasl())) {
						wfDefListSort.add(dto);
						break;
					}
				}

				for (int i = 0; i < wfDefListSort.size(); i++) {
					DTOWfDef dto = wfDefListSort.get(i);
					for (DTOWfDef dtoPom : wfDefList) {
						if (StringUtils.isValid(dtoPom.getIDWfDefNasl()) && dto.getWfDefID().intValue() == dtoPom.getIDWfDefNasl().intValue()) {
							wfDefListSort.add(dtoPom);
							break;
						}
					}
				}

				Collections.reverse(wfDefListSort);
				wfDefMap.get(ciselnikID).clear();
				wfDefMap.get(ciselnikID).addAll(wfDefListSort);

			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "wfDefSort.error");
		}
	}

	public DTOZmenaStavHist createDTOZmenaStavHist(Integer ciselnikID, Integer zmenaID, String stav) throws AppException {

		try {
			DTOZmenaStavHist dto = new DTOZmenaStavHist();
			dto.setIDCiselnik(ciselnikID);
			dto.setIDZmena(zmenaID);
			dto.setStav(stav);
			return dto;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOZmenaStavHist.error");
			return null;
		}
	}

	public List<DTOZmenaStavHist> createListDTOZmenaStavHist(AuthInfo auth, DTOPau dtoPA, Map<Integer, List<DTOWfDef>> wfDefMap) throws AppException {

		try {
			List<DTOZmenaStavHist> resultList = new ArrayList<DTOZmenaStavHist>();

			List<DTOWfDef> wfDefList = wfDefMap.get(dtoPA.getCiselnik().getCiselnikID());
			for (DTOWfDef dto : wfDefList) {
				DTOZmenaStavHist dtoNew = new DTOZmenaStavHist();
				dtoNew.setIDCiselnik(dtoPA.getCiselnik().getCiselnikID());
				dtoNew.setIDZmena(dtoPA.getZmena().getZmenaID());
				dtoNew.setStav(_CudLookupUtils.lookupZmenaStavKod(dto.getTyp()));

				resultList.add(dtoNew);
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createListDTOZmenaStavHist.error");
			return null;
		}
	}

	public List<DTOWfTodo> createListWfTodo(AuthInfo auth, DTOPau dtoPA, Map<Integer, List<DTOWfDef>> wfDefMap) throws AppException {

		try {
			List<DTOWfTodo> resultList = new ArrayList<DTOWfTodo>();

			List<DTOWfDef> wfDefList = wfDefMap.get(dtoPA.getCiselnik().getCiselnikID());
			for (DTOWfDef dto : wfDefList) {
				DTOWfTodo dtoNew = new DTOWfTodo();
				dtoNew.setIDCiselnik(dtoPA.getCiselnik().getCiselnikID());
				dtoNew.setIDWfDef(dto.getWfDefID());
				dtoNew.setPotvrdeny("T");
				String s = StringUtils.replaceAll(_CudConsts.PAU_POZNAMKA, "{1}", dtoPA.getCiselnik().getNazov());
				s = StringUtils.replaceAll(s, "{2}", dtoPA.getNext().getCiselnik().getNazov());
				dtoNew.setPoznamka(s);
				dtoNew.setIDUcet(auth.getAccountId());

				resultList.add(dtoNew);
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createListWfTodo.error");
			return null;
		}
	}

	public void generujWorkFlow(AuthInfo auth, List<DTOPau> paList, List<DTOWfDef> wfDefList, List<DTOWfTodo> wfTodoList, Map<Integer, List<DTOWfDef>> wfDefMap)
			throws AppException {

		try {
			for (DTOPau dtoPA : paList) {

				if ("T".equals(dtoPA.getLenPlatnostDo())) {
					// ak nema nacitane ziadne pomocne udaje, viem ze ide len o zmenu atributu CUD_ZMENA.PLATNOST_DO, nie je potreba generovat workflow
					continue;
				}

				if (StringUtils.isValid(dtoPA.getZmena().getZmenaID())) {
					// ide o zmenu, ktoru si klikol pouzivatel, cize workflow je vygenerovany, ziskam a nastavim len posledny zaznam z workflow

					// tabulka CUD_WF_TODO
					DTOWfTodo dtoTodo = lookupDTOWfTodoByStav(wfDefList, wfTodoList, _CudConsts.WF_DEF_TYP_OV);
					dtoTodo.setPotvrdeny("T");
					dtoTodo.setIDUcet(auth.getAccountId());
					dtoPA.setWfTodoList(new ArrayList<DTOWfTodo>());
					dtoPA.getWfTodoList().add(dtoTodo);

					// tabulka CUD_ZMENA_STAV_HIST
					dtoPA.setZmenaStavHistList(new ArrayList<DTOZmenaStavHist>());
					dtoPA.getZmenaStavHistList().add(createDTOZmenaStavHist(dtoPA.getCiselnik().getCiselnikID(), dtoPA.getZmena().getZmenaID(), _CudConsts.ZMENA_STAV_PAU));

				} else {
					// ide o zmeny ktore su navyse (vygenerovane), povodna zmena sposobila dalsie zmeny

					// tabulka CUD_WF_TODO
					dtoPA.setWfTodoList(createListWfTodo(auth, dtoPA, wfDefMap));

					// tabulka CUD_WF_TODO
					dtoPA.setZmenaStavHistList(createListDTOZmenaStavHist(auth, dtoPA, wfDefMap));
				}
			}

		} catch (Throwable t) {
			handleException(t, "generujWorkFlow.error", auth);
		}
	}

	public List<DTOZmenaStlpec> getZmenaStlpecListPrePrimarnuAktualizaciu(List<DTOZmenaStlpec> list) throws AppException {

		try {
			List<DTOZmenaStlpec> resultList = new ArrayList<DTOZmenaStlpec>();
			for (DTOZmenaStlpec dto : list) {
				if (!StringUtils.isValid(dto.getZmenaStlpecID())) {
					resultList.add(dto);
				}
			}
			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getZmenaStlpecListPrePrimarnuAktualizaciu.error");
			return null;
		}
	}

	public void replaceKeyValue(List<DTOZmenaStlpec> list, String key, String newValue) throws AppException {

		try {
			for (DTOZmenaStlpec dto : list) {
				if (key.equals(dto.getNewValue())) {
					dto.setNewValue(newValue);
				}
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "replaceKeyValue.error");
		}
	}

	public void primarnaAktualizaciaUpdate(AuthInfo auth, List<DTOPau> paList) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			getConnection(auth);

			for (DTOPau dto : paList) {

				Integer pkValue = null;

				if ("T".equals(dto.getLenPlatnostDo())) {
					dlg.getZmenaModify().updatePlatnostDo(auth, dto.getZmena().getZmenaID(), dto.getZmena().getPlatnostDo());
					dlg.getDynCiselnikModify().update(auth, dto.getSql());
					continue;

				} else if (_CudConsts.ZMENA_OPERACIA_N.equals(dto.getZmena().getOperacia())) {
					pkValue = dlg.getDynCiselnikRead().getNewID(auth, dto.getCiselnik().getTabulka(), dto.getCiselnikStlpecPk().getNazov());
					replaceKeyValue(dto.getZmenaStlpecList(), _CudConsts.PK_VALUE, pkValue.toString());
				}

				if (StringUtils.isValid(pkValue)) {
					dto.getZmena().setRowID(pkValue);
					String s = StringUtils.replaceAll(dto.getSql(), _CudConsts.PK_VALUE, pkValue.toString());
					dto.setSql(s);
				}

				if (dto.getSql().contains(_CudConsts.FK_VALUE) && StringUtils.isValid(dto.getPrev())) {
					String fkValue = dto.getPrev().getZmena().getRowID().toString();
					String s = StringUtils.replaceAll(dto.getSql(), _CudConsts.FK_VALUE, fkValue);
					dto.setSql(s);

					replaceKeyValue(dto.getZmenaStlpecList(), _CudConsts.FK_VALUE, fkValue);
				}

				dlg.getZmenaModify().updateSoft(auth, dto.getZmena());
				dlg.getZmenaStlpecModify().update(auth, getZmenaStlpecListPrePrimarnuAktualizaciu(dto.getZmenaStlpecList()), dto.getZmena().getZmenaID());
				dlg.getWfTodoModify().update(auth, dto.getWfTodoList(), dto.getZmena().getZmenaID());
				dlg.getZmenaStavHistModify().update(auth, dto.getZmenaStavHistList(), dto.getZmena().getZmenaID(), dto.getCasVytvorenia());

				if (dto.getSql().contains(_CudConsts.PK_ZMENA)) {
					Integer zmenaID = dto.getZmena().getZmenaID();
					String s = StringUtils.replaceAll(dto.getSql(), _CudConsts.PK_ZMENA, zmenaID.toString());
					dto.setSql(s);
				}

				dlg.getDynCiselnikModify().update(auth, dto.getSql());

				log.info("Update zmeny zmenaID=={} ukonceny.", dto.getZmena().getZmenaID());
			}

			returnConnection(auth);

			endTransaction(auth, true);

		} catch (Throwable t) {
			handleException(t, "primarnaAktualizaciaUpdate.error", auth);
		}
	}

	public String getStackTraceToString(Exception e) {

		StringBuilder result = new StringBuilder();
		result.append(e.toString());
		String NEW_LINE = System.getProperty("line.separator");
		result.append(NEW_LINE);

		for (StackTraceElement element : e.getStackTrace()) {
			result.append(element);
			result.append(NEW_LINE);
		}

		return "<pre>" + result.toString() + "</pre>";
	}

	public String lookupPoznamka(AuthInfo auth, List<DTOPau> paList, Map<Integer, List<DTOWfDef>> wfDefMap, Map<Integer, DTOUcet> ucetMap) throws AppException {

		try {
			DTOPau dtoPaLast = paList.get(paList.size() - 1);
			if (paList.get(0).getZmena().getZmenaID().intValue() == dtoPaLast.getZmena().getZmenaID().intValue()) {
				return null;
			}

			List<DTOWfDef> wfDefList = wfDefMap.get(dtoPaLast.getZmena().getIDCiselnik());
			DTOWfDef dtoDef = _CudLookupUtils.lookupDTOWfDef(wfDefList, _CudConsts.WF_DEF_TYP_IN);

			DTOWfTodo dtoTodo = dlg.getWfTodoRead().readLight(auth, dtoPaLast.getCiselnik().getCiselnikID(), dtoPaLast.getZmena().getZmenaID(), dtoDef.getWfDefID());

			DTOUcet dtoUcet = ucetMap.get(dtoTodo.getIDUcet());
			if (!StringUtils.isValid(dtoUcet)) {
				AuthInfo au = FrameworkUtils.getAuthMod().accountRead(dtoTodo.getIDUcet());
				dtoUcet = new DTOUcet();
				dtoUcet.setUcetID(au.getAccountId());
				dtoUcet.setUcetNazov(au.getAccountName());
				dtoUcet.setPouzivatelNazov(au.getPouzivatel());
				ucetMap.put(dtoUcet.getUcetID(), dtoUcet);
			}

			return "Zmena je spôsobená zmenou v číselníku " + dtoPaLast.getCiselnik().getNazov() + ", ktorú inicioval používateľ " + dtoUcet.getUcetNazov();

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupPoznamka.error");
			return null;
		}
	}

	public void sendNotif(AuthInfo auth, List<DTOPau> paList, Map<Integer, List<DTOWfDef>> wfDefMap, Map<Integer, DTOUcet> ucetMap,
			Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> metaMap, Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> fkMetaMap) throws AppException {

		try {
			String poznamka = lookupPoznamka(auth, paList, wfDefMap, ucetMap);

			int pocet = 0;
			for (DTOPau dtoPA : paList) {

				if (++pocet == paList.size()) {
					poznamka = null;
				}

				if ("T".equals(dtoPA.getLenPlatnostDo())) {
					continue;
				}

				List<DTOWfDef> wfDefList = wfDefMap.get(dtoPA.getZmena().getIDCiselnik());
				DTOWfDef dtoDef = _CudLookupUtils.lookupDTOWfDef(wfDefList, _CudConsts.WF_DEF_TYP_OV);

				if ("F".equals(dtoDef.getEmailSend())) {
					return;
				}

				if (!StringUtils.isValid(metaMap.get(dtoPA.getZmena().getPlatnostOd()))) {
					metaMap.put(dtoPA.getZmena().getPlatnostOd(), new HashMap<Integer, List<DTOCiselnikStlpecGui>>());
				}
				if (!StringUtils.isValid(fkMetaMap.get(dtoPA.getZmena().getPlatnostOd()))) {
					fkMetaMap.put(dtoPA.getZmena().getPlatnostOd(), new HashMap<Integer, List<DTOCiselnikStlpecGui>>());
				}

				Map<String, String> rowMap = dlg.getDynCiselnikRead().readLookupValues(auth, dtoPA.getCiselnik().getCiselnikID(), dtoPA.getZmena().getPlatnostOd(), metaMap,
						fkMetaMap, dtoPA.getRowOldMap());

				DTOWfNotif dtoNotif = new DTOWfNotif();
				dtoNotif.setCiselnikID(dtoPA.getCiselnik().getCiselnikID());
				dtoNotif.setCiselnikNazov(dtoPA.getCiselnik().getNazov());
				dtoNotif.setZmenaOperacia(dtoPA.getZmena().getOperacia());
				dtoNotif.setPoznamka(poznamka);
				dtoNotif.setPlatnostOd(dtoPA.getZmena().getPlatnostOd());

				List<DTOCiselnikStlpecGui> metaList = metaMap.get(dtoPA.getZmena().getPlatnostOd()).get(dtoPA.getCiselnik().getCiselnikID());

				Set<Integer> ciselnikIDs = new HashSet<Integer>();
				for (DTOCiselnikStlpecGui dto : metaList) {
					if (StringUtils.isValid(dto.getCiselnikStlpecFk1IDCiselnik())) {
						if (!fkMetaMap.get(dtoPA.getZmena().getPlatnostOd()).keySet().contains(dto.getCiselnikStlpecFk1IDCiselnik())) {
							ciselnikIDs.add(dto.getCiselnikStlpecFk1IDCiselnik());
						}
					}
				}

				Map<Integer, List<DTOCiselnikStlpecGui>> pomFkMetaMap = dlg.getCiselnikStlpecGuiRead().mapForLookup(auth, ciselnikIDs, dtoPA.getZmena().getPlatnostOd());
				for (Integer ciselnikID : pomFkMetaMap.keySet()) {
					fkMetaMap.get(dtoPA.getZmena().getPlatnostOd()).put(ciselnikID, pomFkMetaMap.get(ciselnikID));
				}

				for (DTOZmenaStlpec dto : dtoPA.getZmenaStlpecList()) {
					DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, dto.getIDCiselnikStlpec());
					if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
						if (StringUtils.isValid(dto.getOldValue())) {
							dto.setOldValue(dlg.getDynCiselnikRead().lookupValueFormat(auth, fkMetaMap.get(dtoPA.getZmena().getPlatnostOd()),
									dtoCS.getCiselnikStlpecFk1IDCiselnik(), dto.getOldValue(), dtoPA.getZmena().getPlatnostOd()));
						}
						if (StringUtils.isValid(dto.getNewValue())) {
							dto.setNewValue(dlg.getDynCiselnikRead().lookupValueFormat(auth, fkMetaMap.get(dtoPA.getZmena().getPlatnostOd()),
									dtoCS.getCiselnikStlpecFk1IDCiselnik(), dto.getNewValue(), dtoPA.getZmena().getPlatnostOd()));
						}
					} else if (_CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getCiselnikStlpecDbTyp())) {
						if (StringUtils.isValid(dto.getOldValue())) {
							dto.setOldValue(dlg.getDynCiselnikRead().doubleValueFormat(dto.getOldValue(), dtoCS.getDecimals()));
						}
						if (StringUtils.isValid(dto.getNewValue())) {
							dto.setNewValue(dlg.getDynCiselnikRead().doubleValueFormat(dto.getNewValue(), dtoCS.getDecimals()));
						}
					} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getCiselnikStlpecDbTyp())) {
						if (StringUtils.isValid(dto.getOldValue())) {
							dto.setOldValue("T".equals(dto.getOldValue()) ? "Áno" : "Nie");
						}
						if (StringUtils.isValid(dto.getNewValue())) {
							dto.setNewValue("T".equals(dto.getNewValue()) ? "Áno" : "Nie");
						}
					}
				}

				DTOWfTodo dtoTodo = _CudLookupUtils.lookupDTOWfTodo(dtoPA.getWfTodoList(), dtoDef.getWfDefID());

				dlg.getWfNotif().sendNotif(auth, dtoNotif, dtoDef, dtoTodo, metaList, dtoPA.getZmenaStlpecList(), rowMap);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "sendNotifikacie.error");
		}
	}

	public ActionResult workflowUpdateCrd(AuthInfo auth, DTOWorkflow dtoWf, DTOImportZmena dtoZmena, Date d) throws AppException {

		try {
			getConnection(auth);

			ActionResult res = dlg.getWorkflow().workflowUpdateSoftCrd(auth, dtoWf, dtoZmena, d);

			returnConnection(auth);

			endTransaction(auth, true);
			return res;

		} catch (Throwable t) {
			handleException(t, "workflowUpdate.error", auth);
			return null;
		}
	}

}
