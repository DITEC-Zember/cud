package sk.ditec.cud.proc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfNotif;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.process.BaseProcess;

public class CudEskalaciaProcess extends BaseProcess {

	private Logger log = LoggerFactory.getLogger(CudEskalaciaProcess.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	@Override
	protected void process() throws Throwable {

		log.info("Start - Som proces CudEskalaciaProcess a bezim");

		try {
			AuthInfo auth = AuthInfo.system();

			List<DTOZmena> zmenaList = dlg.getZmenaRead().listForEskalacia(auth, null);
			log.info("Pocet zaznamov na eskalovanie {}", zmenaList.size());

			Map<Integer, DTOCiselnik> ciselnikMap = new HashMap<Integer, DTOCiselnik>();

			Map<Integer, DTOWfDef> escWfDefMap = new HashMap<Integer, DTOWfDef>();

			Map<Integer, AuthInfo> authMap = new HashMap<Integer, AuthInfo>();

			Map<Integer, Map<Integer, Set<Integer>>> opravnenieMap = new HashMap<Integer, Map<Integer, Set<Integer>>>();

			Map<Integer, List<DTOCiselnikStlpec>> csMap = new HashMap<Integer, List<DTOCiselnikStlpec>>();

			Map<Integer, Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>> metaMap = new HashMap<Integer, Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>>();
			Map<Integer, Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>> metaMapForLookup = new HashMap<Integer, Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>>();

			Calendar cal = Calendar.getInstance();

			while (!zmenaList.isEmpty()) {

				Date d = new Date();

				if (!statusOKnotify()) {
					log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudEskalaciaProcess konci.");
					return;
				}

				for (DTOZmena dto : zmenaList) {

					log.info("Spracovanie zmeny, zmenaID=={}", dto.getZmenaID());

					if (!StringUtils.isValid(escWfDefMap.get(dto.getIDCiselnik()))) {
						escWfDefMap.put(dto.getIDCiselnik(), dlg.getWfDefRead().readLastLight(auth, dto.getIDCiselnik(), _CudConsts.WF_DEF_TYP_ES));
					}

					DTOWfDef dtoDef = escWfDefMap.get(dto.getIDCiselnik());
					if ("F".equals(dtoDef.getEmailSend())) {
						log.info("EMAIL_SEND=F, eskalovanie zmeny preskakujem");
						continue;
					}

					cal.setTime(dto.getZmenaStavHistCasVytvorenia());
					cal.add(Calendar.HOUR_OF_DAY, dtoDef.getHodiny());
					if (!d.after(cal.getTime())) {
						log.info("Cas od vytvorenia VPO je mensi ako je definovany cas v eskalacii, eskalovanie zmeny preskakujem");
						continue;
					}

					if (StringUtils.isValid(dto.getZmenaEskalaciaCasVytvorenia())) {
						cal.setTime(dto.getZmenaEskalaciaCasVytvorenia());
						cal.add(Calendar.HOUR_OF_DAY, dtoDef.getHodiny());
						if (!d.after(cal.getTime())) {
							log.info("Cas od poslednej eskalacie je mensi ako je definovany cas v eskalacii, eskalovanie zmeny preskakujem");
							continue;
						}
					}

					if (!StringUtils.isValid(ciselnikMap.get(dto.getIDCiselnik()))) {
						ciselnikMap.put(dto.getIDCiselnik(), dlg.getCiselnikRead().read(auth, dto.getIDCiselnik()));
					}
					DTOCiselnik dtoCis = ciselnikMap.get(dto.getIDCiselnik());

					DTOWfTodo dtoTodo = dlg.getWfTodoRead().readLast(auth, dto.getIDCiselnik(), dto.getZmenaID());
					if (!StringUtils.isValid(authMap.get(dtoTodo.getIDUcet()))) {
						AuthInfo au = FrameworkUtils.getAuthMod().accountRead(dtoTodo.getIDUcet());
						if (StringUtils.isValid(au)) {
							authMap.put(dtoTodo.getIDUcet(), au);
						}
					}
					AuthInfo au = authMap.get(dtoTodo.getIDUcet());
					if (!StringUtils.isValid(au) || dtoTodo.getIDUcet().intValue() != au.getAccountId()) {
						String s = StringUtils.replaceAll("Chyba pri nacitavani udajov o pouzivatelovi z IAM, zmenaID=={1}, IDUcet=={2}, eskalovanie zmeny preskakujem", "{1}", dto.getZmenaID().toString());
						s = StringUtils.replaceAll(s, "{2}", dtoTodo.getIDUcet().toString());
						log.error(s);
						dlg.getWfNotif().sendNotifError(_CudConsts.TEXT_NOTIF_SUBJ_ES_JUNP, s);
						continue;
					}

					if (!StringUtils.isValid(opravnenieMap.get(dtoTodo.getIDUcet()))) {
						opravnenieMap.put(dtoTodo.getIDUcet(), new HashMap<Integer, Set<Integer>>());
					}
					if (!StringUtils.isValid(opravnenieMap.get(dtoTodo.getIDUcet()).get(dto.getIDCiselnik()))) {
						opravnenieMap.get(dtoTodo.getIDUcet()).put(dto.getIDCiselnik(), opravnenieSet(au, dto.getIDCiselnik()));
					}
					Set<Integer> perm = opravnenieMap.get(dtoTodo.getIDUcet()).get(dto.getIDCiselnik());
					if (!StringUtils.isValid(perm) || perm.isEmpty()) {
						String s = StringUtils.replaceAll("Chyba pri nacitavani opravnenia, zmenaID=={}, IDUcet=={2}, eskalovanie zmeny preskakujem", "{1}", dto.getZmenaID().toString());
						s = StringUtils.replaceAll(s, "{2}", dtoTodo.getIDUcet().toString());
						log.error(s);
						dlg.getWfNotif().sendNotifError(_CudConsts.TEXT_NOTIF_SUBJ_ES_JUNP, s);
						continue;
					}

					if (!StringUtils.isValid(csMap.get(dto.getIDCiselnik()))) {
						csMap.put(dto.getIDCiselnik(), dlg.getCiselnikStlpecRead().listLight(auth, dto.getIDCiselnik()));
					}
					List<DTOCiselnikStlpec> csList = csMap.get(dto.getIDCiselnik());

					Map<String, String> rowMap = new HashMap<String, String>();
					if (StringUtils.isValid(dto.getRowID())) {
						DTOCiselnikStlpec dtoPK = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList);
						rowMap = dlg.getDynCiselnikRead().readLight(auth, dtoCis.getTabulka(), csList, dtoPK.getNazov(), dto.getRowID().toString(), dtoPK.getDbTyp(), dto.getPlatnostOd(), "F");
					}

					if (!StringUtils.isValid(metaMap.get(dtoTodo.getIDUcet()))) {
						metaMap.put(dtoTodo.getIDUcet(), new HashMap<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>());
					}
					if (!StringUtils.isValid(metaMap.get(dtoTodo.getIDUcet()).get(dto.getPlatnostOd()))) {
						metaMap.get(dtoTodo.getIDUcet()).put(dto.getPlatnostOd(), new HashMap<Integer, List<DTOCiselnikStlpecGui>>());
					}
					if (!StringUtils.isValid(metaMap.get(dtoTodo.getIDUcet()).get(dto.getPlatnostOd()).get(dto.getIDCiselnik()))) {
						DTOCiselnikStlpecGui[] metaPole = dlg.getCiselnikStlpecGuiRead().listForForm(auth, dto.getIDCiselnik(), dto.getPlatnostOd());
						List<DTOCiselnikStlpecGui> metaList = new ArrayList<DTOCiselnikStlpecGui>();
						for (DTOCiselnikStlpecGui dtoCS : metaPole) {
							if (perm.contains(dtoCS.getIDCiselnikStlpec())) {
								metaList.add(dtoCS);
							}
						}
						metaMap.get(dtoTodo.getIDUcet()).get(dto.getPlatnostOd()).put(dto.getIDCiselnik(), metaList);
					}

					if (!StringUtils.isValid(metaMapForLookup.get(dtoTodo.getIDUcet()))) {
						metaMapForLookup.put(dtoTodo.getIDUcet(), new HashMap<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>());
					}
					if (!StringUtils.isValid(metaMapForLookup.get(dtoTodo.getIDUcet()).get(dto.getPlatnostOd()))) {
						metaMapForLookup.get(dtoTodo.getIDUcet()).put(dto.getPlatnostOd(), new HashMap<Integer, List<DTOCiselnikStlpecGui>>());
					}

					rowMap = dlg.getDynCiselnikRead().readLookupValues(au, dto.getIDCiselnik(), dto.getPlatnostOd(), metaMap.get(dtoTodo.getIDUcet()), metaMapForLookup.get(dtoTodo.getIDUcet()), rowMap);

					List<DTOCiselnikStlpecGui> metaList = metaMap.get(dtoTodo.getIDUcet()).get(dto.getPlatnostOd()).get(dto.getIDCiselnik());

					Set<Integer> ciselnikIDs = new HashSet<Integer>();
					for (DTOCiselnikStlpecGui dtoCS : metaList) {
						if (StringUtils.isValid(dtoCS.getCiselnikStlpecFk1IDCiselnik())) {
							if (!metaMapForLookup.get(dtoTodo.getIDUcet()).get(dto.getPlatnostOd()).keySet().contains(dtoCS.getCiselnikStlpecFk1IDCiselnik())) {
								ciselnikIDs.add(dtoCS.getCiselnikStlpecFk1IDCiselnik());
							}
						}
					}

					Map<Integer, List<DTOCiselnikStlpecGui>> lookupMetaMap = dlg.getCiselnikStlpecGuiRead().mapForLookup(auth, ciselnikIDs, dto.getPlatnostOd());
					for (Integer ciselnikID : lookupMetaMap.keySet()) {
						metaMapForLookup.get(dtoTodo.getIDUcet()).get(dto.getPlatnostOd()).put(ciselnikID, lookupMetaMap.get(ciselnikID));
					}

					List<DTOZmenaStlpec> zsList = dlg.getZmenaStlpecRead().listLight(auth, dto.getIDCiselnik(), dto.getZmenaID());

					for (DTOZmenaStlpec dtoZS : zsList) {
						DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, dtoZS.getIDCiselnikStlpec());
						if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
							if (StringUtils.isValid(dtoZS.getOldValue())) {
								dtoZS.setOldValue(dlg.getDynCiselnikRead().lookupValueFormat(auth, metaMapForLookup.get(dtoTodo.getIDUcet()).get(dto.getPlatnostOd()), dtoCS.getCiselnikStlpecFk1IDCiselnik(), dtoZS.getOldValue(), dto.getPlatnostOd()));
							}
							if (StringUtils.isValid(dtoZS.getNewValue())) {
								dtoZS.setNewValue(dlg.getDynCiselnikRead().lookupValueFormat(auth, metaMapForLookup.get(dtoTodo.getIDUcet()).get(dto.getPlatnostOd()), dtoCS.getCiselnikStlpecFk1IDCiselnik(), dtoZS.getNewValue(), dto.getPlatnostOd()));
							}
						} else if (_CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getCiselnikStlpecDbTyp())) {
							if (StringUtils.isValid(dtoZS.getOldValue())) {
								dtoZS.setOldValue(dlg.getDynCiselnikRead().doubleValueFormat(dtoZS.getOldValue(), dtoCS.getDecimals()));
							}
							if (StringUtils.isValid(dtoZS.getNewValue())) {
								dtoZS.setNewValue(dlg.getDynCiselnikRead().doubleValueFormat(dtoZS.getNewValue(), dtoCS.getDecimals()));
							}
						} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getCiselnikStlpecDbTyp())) {
							if (StringUtils.isValid(dtoZS.getOldValue())) {
								dtoZS.setOldValue("T".equals(dtoZS.getOldValue()) ? "Áno" : "Nie");
							}
							if (StringUtils.isValid(dtoZS.getNewValue())) {
								dtoZS.setNewValue("T".equals(dtoZS.getNewValue()) ? "Áno" : "Nie");
							}
						}
					}

					DTOWfNotif dtoNotif = new DTOWfNotif();
					dtoNotif.setCiselnikID(dtoCis.getCiselnikID());
					dtoNotif.setCiselnikNazov(dtoCis.getNazov());
					dtoNotif.setZmenaOperacia(dto.getOperacia());
					dtoNotif.setPoznamka(dtoTodo.getPoznamka());
					dtoNotif.setPlatnostOd(dto.getPlatnostOd());

					dlg.getWfNotif().sendNotif(auth, dtoNotif, dtoDef, dtoTodo, metaList, zsList, rowMap);
					dlg.getZmenaEskalaciaMofify().update(auth, dto.getIDCiselnik(), dto.getZmenaID());
				}

				zmenaList = dlg.getZmenaRead().listForEskalacia(auth, zmenaList.get(zmenaList.size() - 1).getZmenaID());
				log.info("Pocet zaznamov na eskalovanie {}", zmenaList.size());
			}

		} catch (Exception e) {
			dlg.getWfNotif().sendNotifError(_CudConsts.TEXT_NOTIF_SUBJ_ES, getStackTraceToString(e));
			DBUtils.handleException(e, "process.error");

		} finally {
			log.info("End - Som proces CudEskalaciaProcess a koncim");
		}
	}

	private Set<Integer> opravnenieSet(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			DTOObjektStlpec[] osList = dlg.getGuiRead().opravnenieList(auth, ciselnikID, null);

			Set<Integer> set = new HashSet<Integer>();
			for (DTOObjektStlpec dtoOS : osList) {
				set.add(dtoOS.getIDCiselnikStlpec());
			}
			return set;

		} catch (Throwable t) {
			handleException(t, "opravnenieSet.error", auth);
			return null;
		}
	}

	private String getStackTraceToString(Exception e) {

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

	@Override
	protected String getLogName() {
		return "eskalacia";
	}

}
