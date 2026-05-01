package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.security.Rola;
import sk.ditec.common.utils.DateUtils;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.data.ws.dto.DTOCiselnikDataWS;
import sk.ditec.cud.data.ws.dto.DTOCiselnikMetaWS;
import sk.ditec.cud.data.ws.dto.DTOCiselnikStlpecMetaWS;
import sk.ditec.cud.data.ws.dto.DTOPageWS;
import sk.ditec.cud.data.ws.dto.DTORecordWS;
import sk.ditec.cud.data.ws.dto.DTOUpdStlpecWS;
import sk.ditec.cud.data.ws.dto.DTOUpdZmenaResponseWS;
import sk.ditec.cud.data.ws.dto.DTOUpdZmenaWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaCiselnikArrayWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaStlpecWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaWrapperWS;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynCiselnikLD;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.dto.DTOZmenaStavHist;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudZmenaPeer;
import sk.ditec.dao.meta.CudZmenaStavHistPeer;
import sk.ditec.dao.meta.CudZmenaStlpecPeer;
import sk.ditec.zsr.common.server.auth.ZSRAuthInfo;

import com.workingdogs.village.Record;
import com.workingdogs.village.Value;

public class CudDataOldWSClass extends _CudBaseClass {

	private String lookupPreklad(Map<Integer, Map<String, String>> prekladMap, Integer zaznamID, String colName, String oldValue) {

		if (StringUtils.isValid(prekladMap)) {
			Map<String, String> rowMap = prekladMap.get(zaznamID);
			if (StringUtils.isValid(rowMap)) {
				String newValue = rowMap.get(colName);
				if (StringUtils.isValid(newValue)) {
					return newValue;
				}
			}
		}
		return oldValue;
	}

	private DTOCiselnikMetaWS copyDTO(DTOCiselnik dto, Map<String, Map<Integer, Map<String, String>>> prekladMap) throws AppException {

		if (!StringUtils.isValid(dto)) {
			return null;
		}

		try {
			DTOCiselnikMetaWS dtoWS = new DTOCiselnikMetaWS();
			dtoWS.setCiselnikID(dto.getCiselnikID());
			dtoWS.setTabulka(lookupPreklad(prekladMap.get(CudCiselnikPeer.TABLE_NAME), dto.getCiselnikID(), trimColumnName(CudCiselnikPeer.TABULKA), dto.getTabulka()));
			dtoWS.setNazov(lookupPreklad(prekladMap.get(CudCiselnikPeer.TABLE_NAME), dto.getCiselnikID(), trimColumnName(CudCiselnikPeer.NAZOV), dto.getNazov()));
			dtoWS.setPopis(lookupPreklad(prekladMap.get(CudCiselnikPeer.TABLE_NAME), dto.getCiselnikID(), trimColumnName(CudCiselnikPeer.POPIS), dto.getPopis()));
			return dtoWS;

		} catch (Throwable t) {
			handleException(t, "copyDTO.error");
			return null;
		}
	}

	private DTOCiselnikStlpecMetaWS copyDTO(DTOCiselnikStlpec dto, Map<String, Map<Integer, Map<String, String>>> prekladMap) throws AppException {

		if (!StringUtils.isValid(dto)) {
			return null;
		}

		try {
			DTOCiselnikStlpecMetaWS dtoWS = new DTOCiselnikStlpecMetaWS();
			dtoWS.setCiselnikStlpecID(dto.getCiselnikStlpecID());
			dtoWS.setIDCiselnik(dto.getIDCiselnik());
			dtoWS.setTyp(lookupPreklad(prekladMap.get(CudCiselnikStlpecPeer.TABLE_NAME), dto.getCiselnikStlpecID(), trimColumnName(CudCiselnikStlpecPeer.TYP), dto.getTyp()));
			dtoWS.setNazov(lookupPreklad(prekladMap.get(CudCiselnikStlpecPeer.TABLE_NAME), dto.getCiselnikStlpecID(), trimColumnName(CudCiselnikStlpecPeer.NAZOV), dto.getNazov()));
			dtoWS.setNadpis(lookupPreklad(prekladMap.get(CudCiselnikStlpecPeer.TABLE_NAME), dto.getCiselnikStlpecID(), trimColumnName(CudCiselnikStlpecPeer.NADPIS), dto.getNadpis()));
			dtoWS.setPovinny(lookupPreklad(prekladMap.get(CudCiselnikStlpecPeer.TABLE_NAME), dto.getCiselnikStlpecID(), trimColumnName(CudCiselnikStlpecPeer.POVINNY), dto.getPovinny()));
			dtoWS.setDbTyp(lookupPreklad(prekladMap.get(CudCiselnikStlpecPeer.TABLE_NAME), dto.getCiselnikStlpecID(), trimColumnName(CudCiselnikStlpecPeer.DB_TYP), dto.getDbTyp()));
			dtoWS.setDlzka(dto.getDlzka());
			dtoWS.setDecimals(dto.getDecimals());
			dtoWS.setJedinecny(lookupPreklad(prekladMap.get(CudCiselnikStlpecPeer.TABLE_NAME), dto.getCiselnikStlpecID(), trimColumnName(CudCiselnikStlpecPeer.JEDINECNY), dto.getJedinecny()));
			dtoWS.setPopis(lookupPreklad(prekladMap.get(CudCiselnikStlpecPeer.TABLE_NAME), dto.getCiselnikStlpecID(), trimColumnName(CudCiselnikStlpecPeer.POPIS), dto.getPopis()));
			dtoWS.setFk1IDCiselnik(dto.getFk1IDCiselnik());
			dtoWS.setFk1PkNazov(lookupPreklad(prekladMap.get(CudCiselnikStlpecPeer.TABLE_NAME), dto.getCiselnikStlpecID(), trimColumnName(CudCiselnikStlpecPeer.FK1_PK_NAZOV), dto.getFk1PkNazov()));
			dtoWS.setFk1Tabulka(lookupPreklad(prekladMap.get(CudCiselnikPeer.TABLE_NAME), dto.getFk1IDCiselnik(), trimColumnName(CudCiselnikStlpecPeer.FK1_ID_CISELNIK), dto.getFk1CiselnikTabulka()));
			return dtoWS;

		} catch (Throwable t) {
			handleException(t, "copyDTO.error");
			return null;
		}
	}

	private DTOCiselnikStlpec copyDTO(DTOCiselnikStlpecMetaWS dtoWS) throws AppException {

		try {
			DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
			dto.setCiselnikStlpecID(dtoWS.getCiselnikStlpecID());
			dto.setIDCiselnik(dtoWS.getIDCiselnik());
			dto.setTyp(dtoWS.getTyp());
			dto.setNazov(dtoWS.getNazov());
			dto.setNadpis(dtoWS.getNadpis());
			dto.setPovinny(dtoWS.getPovinny());
			dto.setDbTyp(dtoWS.getDbTyp());
			dto.setDlzka(dtoWS.getDlzka());
			dto.setDecimals(dtoWS.getDecimals());
			dto.setJedinecny(dtoWS.getJedinecny());
			dto.setPopis(dtoWS.getPopis());
			dto.setFk1IDCiselnik(dtoWS.getFk1IDCiselnik());
			dto.setFk1PkNazov(dtoWS.getFk1PkNazov());
			dto.setFk1CiselnikNazov(dtoWS.getFk1Tabulka());
			return dto;

		} catch (Throwable t) {
			handleException(t, "copy.error");
			return null;
		}
	}

	private DTOObjektStlpec[] opravnenieList(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			List<Rola> rolaList = FrameworkUtils.getAuthMod().rolaListByAccount(auth.getAccountName());
			Set<String> kodRolySet = new HashSet<String>();
			for (Rola dto : rolaList) {
				if (_CudConsts.ROLA_MODUL_KODs.contains(dto.getKodRoly())) {
					kodRolySet.add(dto.getKodRoly());
				}
			}
			String[] poleRola = kodRolySet.toArray(new String[kodRolySet.size()]);

			List<DTOObjektStlpec> resultList = new ArrayList<DTOObjektStlpec>();

			List<DTOOdberatelObjekt> ooList = getDelegate().getOdberatelObjektRead().list(auth, new Date(), _CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_WS, poleRola);

			Set<Integer> objektIDs = new HashSet<Integer>();
			for (DTOOdberatelObjekt dto : ooList) {
				if ("T".equals(dto.getVsetkyCiselniky())) {
					resultList.addAll(getDelegate().getObjektStlpecRead().listByCiselnik(auth, ciselnikID));
					objektIDs.clear();
					break;
				} else if (StringUtils.isValid(dto.getIDObjekt())) {
					objektIDs.add(dto.getIDObjekt());
				}
			}

			List<DTOObjektCiselnik> ocList = getDelegate().getObjektCiselnikRead().list(auth, ciselnikID, objektIDs.toArray(new Integer[objektIDs.size()]));

			Set<Integer> objektCiselnikIDs = new HashSet<Integer>();
			for (DTOObjektCiselnik dto : ocList) {
				if ("T".equals(dto.getVsetky())) {
					resultList.addAll(getDelegate().getObjektStlpecRead().listByCiselnik(auth, ciselnikID));
					objektCiselnikIDs.clear();
					break;
				} else {
					objektCiselnikIDs.add(dto.getObjektCiselnikID());
				}
			}
			if (!objektCiselnikIDs.isEmpty()) {
				List<DTOObjektStlpec> osList = getDelegate().getObjektStlpecRead().list(auth, objektCiselnikIDs.toArray(new Integer[objektCiselnikIDs.size()]));
				if (!osList.isEmpty()) {
					resultList.addAll(osList);
				}
			}

			return resultList.toArray(new DTOObjektStlpec[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "opravnenieList.error", auth);
			return null;
		}
	}

	private Map<Integer, Set<Integer>> opravnenieMap(AuthInfo auth, Integer[] ciselnikIDs) throws AppException {

		try {
			List<Rola> rolaList = FrameworkUtils.getAuthMod().rolaListByAccount(auth.getAccountName());
			Set<String> kodRolySet = new HashSet<String>();
			for (Rola dto : rolaList) {
				if (_CudConsts.ROLA_MODUL_KODs.contains(dto.getKodRoly())) {
					kodRolySet.add(dto.getKodRoly());
				}
			}
			String[] poleRola = kodRolySet.toArray(new String[kodRolySet.size()]);

			List<DTOOdberatelObjekt> ooList = getDelegate().getOdberatelObjektRead().list(auth, new Date(), _CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_WS, poleRola);

			Set<Integer> objektIDs = new HashSet<Integer>();
			boolean vsetky = false;
			for (DTOOdberatelObjekt dto : ooList) {
				if ("T".equals(dto.getVsetkyCiselniky())) {
					vsetky = true;
					break;
				} else if (StringUtils.isValid(dto.getIDObjekt())) {
					objektIDs.add(dto.getIDObjekt());
				}
			}

			Map<Integer, Set<Integer>> resultMap = new HashMap<Integer, Set<Integer>>();

			if (vsetky) {

				for (Integer ciselnikID : ciselnikIDs) {
					Set<Integer> set = getDelegate().getObjektStlpecRead().ciselnikStlpecIDsByCiselnik(auth, ciselnikID);
					if (!set.isEmpty()) {
						resultMap.put(ciselnikID, set);
					}
				}

			} else {

				Map<Integer, List<DTOObjektCiselnik>> ocMap = getDelegate().getObjektCiselnikRead().map(auth, ciselnikIDs, objektIDs.toArray(new Integer[objektIDs.size()]));

				Set<Integer> objektCiselnikIDs = new HashSet<Integer>();
				for (Integer ciselnikID : ciselnikIDs) {

					List<DTOObjektCiselnik> ocList = ocMap.get(ciselnikID);
					if (!StringUtils.isValid(ocList)) {
						continue;
					}

					boolean b = false;
					objektCiselnikIDs.clear();
					for (DTOObjektCiselnik dto : ocList) {
						if ("T".equals(dto.getVsetky())) {
							b = true;
							break;
						} else {
							objektCiselnikIDs.add(dto.getObjektCiselnikID());
						}
					}

					if (b) {
						Set<Integer> set = getDelegate().getObjektStlpecRead().ciselnikStlpecIDsByCiselnik(auth, ciselnikID);
						if (!set.isEmpty()) {
							resultMap.put(ciselnikID, set);
						}

					} else {
						Set<Integer> set = getDelegate().getObjektStlpecRead().ciselnikStlpecIDsSet(auth, objektCiselnikIDs.toArray(new Integer[objektCiselnikIDs.size()]));
						if (!set.isEmpty()) {
							resultMap.put(ciselnikID, set);
						}
					}
				}
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "opravnenieMap.error", auth);
			return null;
		}
	}

	private Set<Integer> opravnenieSet(AuthInfo auth) throws AppException {

		try {
			List<Rola> rolaList = FrameworkUtils.getAuthMod().rolaListByAccount(auth.getAccountName());
			Set<String> kodRolySet = new HashSet<String>();
			for (Rola dto : rolaList) {
				if (_CudConsts.ROLA_MODUL_KODs.contains(dto.getKodRoly())) {
					kodRolySet.add(dto.getKodRoly());
				}
			}
			String[] poleRola = kodRolySet.toArray(new String[kodRolySet.size()]);

			Set<Integer> objektIDs = new HashSet<Integer>();

			List<DTOOdberatelObjekt> ooList = getDelegate().getOdberatelObjektRead().list(auth, new Date(), _CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_WS, poleRola);
			for (DTOOdberatelObjekt dto : ooList) {
				if ("T".equals(dto.getVsetkyCiselniky())) {
					DTOCiselnik dtoF = new DTOCiselnik();
					dtoF.setAktivny("T");
					dtoF.setTyp(_CudConsts.CISELNIK_TYP_TECHNICKY);
					return getDelegate().getCiselnikRead().ids(auth, dtoF);

				} else if (StringUtils.isValid(dto.getIDObjekt())) {
					objektIDs.add(dto.getIDObjekt());
				}
			}

			return getDelegate().getObjektCiselnikRead().ciselnikIDsSet(auth, objektIDs.toArray(new Integer[objektIDs.size()]));

		} catch (Throwable t) {
			handleException(t, "opravnenieSet.error", auth);
			return null;
		}
	}

	public DTOCiselnikMetaWS ciselnikMetaRead(AuthInfo auth, Integer ciselnikID, String jazyk, Date d, boolean b) throws AppException {

		try {
			DTOObjektStlpec[] perns = opravnenieList(auth, ciselnikID);
			return ciselnikMetaRead(auth, ciselnikID, jazyk, d, perns);

		} catch (Exception e) {
			handleException(e, "ciselnikMetaRead.error");
			return null;
		}
	}

	private DTOCiselnikMetaWS ciselnikMetaRead(AuthInfo auth, Integer ciselnikID, String jazyk, Date d, DTOObjektStlpec[] perns) throws AppException {

		try {
			DTOCiselnikMetaWS resultDTO = null;

			if (!StringUtils.isValid(ciselnikID)) {
				resultDTO = new DTOCiselnikMetaWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "ciselnikID"));
				return resultDTO;
			}

			DTOCiselnik dtoF = new DTOCiselnik();
			dtoF.setCiselnikID(ciselnikID);
			dtoF.setTyp(_CudConsts.CISELNIK_TYP_TECHNICKY);
			List<DTOCiselnik> listDTO = getDelegate().getCiselnikRead().ciselnikListLight(auth, dtoF, null);

			if (listDTO.isEmpty()) {
				resultDTO = new DTOCiselnikMetaWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ciselnikID.toString()));
				return resultDTO;
			}

			DTOCiselnik ciselnikDTO = listDTO.get(0);
			if ("F".equals(ciselnikDTO.getAktivny())) {
				resultDTO = new DTOCiselnikMetaWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ciselnikID.toString()));
				return resultDTO;
			}

			if (!StringUtils.isValid(perns)) {
				resultDTO = new DTOCiselnikMetaWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ciselnikID.toString()));
				return resultDTO;
			}

			Map<String, Map<Integer, Map<String, String>>> prekladMap = new HashMap<String, Map<Integer, Map<String, String>>>();
			List<DTOCiselnikStlpecMetaWS> listWS = new ArrayList<DTOCiselnikStlpecMetaWS>();

			Set<Integer> ciselnikGuiIDs = getDelegate().getCiselnikGuiRead().ciselnikGuiIDs(auth, ciselnikID, d);
			if (!ciselnikGuiIDs.isEmpty()) {

				Set<Integer> ciselnikIDs = new HashSet<Integer>();
				Set<Integer> ciselnikStlpecIDs = new HashSet<Integer>();

				Set<Integer> guiCiselnikStlpecIDs = getDelegate().getCiselnikStlpecGuiRead().ciselnikStlpecIDs(auth, ciselnikGuiIDs);
				List<DTOCiselnikStlpec> csList = getDelegate().getCiselnikStlpecRead().list(auth, ciselnikID);

				for (DTOCiselnikStlpec dto : csList) {
					ciselnikIDs.add(dto.getIDCiselnik());
					ciselnikStlpecIDs.add(dto.getCiselnikStlpecID());
					if (StringUtils.isValid(dto.getFk1IDCiselnik())) {
						ciselnikIDs.add(dto.getFk1IDCiselnik());
					}
					if ("T".equals(dto.getJeDbString())) {
						dto.setDbTyp(_CudConsts.DB_TYP_STRING);
					}
				}

				prekladMap.put(CudCiselnikPeer.TABLE_NAME, getDelegate().getPrekladRead().map(auth, jazyk, CudCiselnikPeer.TABLE_NAME, ciselnikIDs.toArray(new Integer[ciselnikIDs.size()])));
				prekladMap.put(CudCiselnikStlpecPeer.TABLE_NAME, getDelegate().getPrekladRead().map(auth, jazyk, CudCiselnikStlpecPeer.TABLE_NAME, ciselnikStlpecIDs.toArray(new Integer[ciselnikStlpecIDs.size()])));

				for (DTOCiselnikStlpec dto : csList) {
					if (_CudConsts.CISELNIK_STLPEC_TYP_HK.equals(dto.getTyp()) || _CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getTyp())) {
						listWS.add(copyDTO(dto, prekladMap));
					} else if (_CudConsts.NAZOV_PLATNOST_OD.equals(dto.getNazov()) || _CudConsts.NAZOV_PLATNOST_DO.equals(dto.getNazov()) || _CudConsts.NAZOV_ZMAZ.equals(dto.getNazov())) {
						listWS.add(copyDTO(dto, prekladMap));
					} else if (StringUtils.isValid(_CudLookupUtils.lookupDTOObjektStlpec(perns, dto.getCiselnikStlpecID()))) {
						if (!_CudKontrolaUtils.jeAtributTechnicky(dto) && guiCiselnikStlpecIDs.contains(dto.getCiselnikStlpecID())) {
							listWS.add(copyDTO(dto, prekladMap));
						}
					}
				}
			}

			resultDTO = copyDTO(ciselnikDTO, prekladMap);
			resultDTO.setCiselnikStlpecList(listWS.toArray(new DTOCiselnikStlpecMetaWS[listWS.size()]));

			return resultDTO;

		} catch (Exception e) {
			handleException(e, "ciselnikMetaRead.error");
			return null;
		}
	}

	private String ciselnikMetaLoad(AuthInfo auth, Integer[] ciselnikIDs, Map<Integer, DTOCiselnik> ciselnikMap, Map<Integer, Set<Integer>> pernMap) throws AppException {

		try {
			Set<Integer> set = new HashSet<Integer>();
			for (Integer ciselnikID : ciselnikIDs) {
				if (StringUtils.isValid(ciselnikID)) {
					set.add(ciselnikID);
				}
			}

			if (set.isEmpty()) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "ids");
			}

			if (set.size() > 10) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_117, "10");
			}

			DTOCiselnik dtoF = new DTOCiselnik();
			dtoF.setTyp(_CudConsts.CISELNIK_TYP_TECHNICKY);
			List<DTOCiselnik> listDTO = getDelegate().getCiselnikRead().ciselnikListLight(auth, dtoF, set.toArray(new Integer[set.size()]));
			for (DTOCiselnik dto : listDTO) {
				ciselnikMap.put(dto.getCiselnikID(), dto);
			}

			for (Integer ciselnikID : ciselnikIDs) {

				if (!StringUtils.isValid(ciselnikID)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "ids");
				}

				DTOCiselnik dtoCis = ciselnikMap.get(ciselnikID);
				if (!StringUtils.isValid(dtoCis)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ciselnikID.toString());
				}

				if ("F".equals(dtoCis.getAktivny())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ciselnikID.toString());
				}

				if (!StringUtils.isValid(pernMap.get(ciselnikID))) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_700, ciselnikID.toString());
				}
			}

			return null;

		} catch (Exception e) {
			handleException(e, "ciselnikMetaLoad.error");
			return null;
		}
	}

	public DTOCiselnikMetaWS[] ciselnikMetaList(AuthInfo auth, String jazyk) throws AppException {

		try {
			Set<Integer> perns = opravnenieSet(auth);

			DTOCiselnik dtoF = new DTOCiselnik();
			dtoF.setAktivny("T");
			dtoF.setTyp(_CudConsts.CISELNIK_TYP_TECHNICKY);
			List<DTOCiselnik> listDTO = getDelegate().getCiselnikRead().ciselnikListLight(auth, dtoF, null);

			Set<Integer> ciselnikIDs = new HashSet<Integer>();

			for (DTOCiselnik dto : listDTO) {
				ciselnikIDs.add(dto.getCiselnikID());
			}

			Map<String, Map<Integer, Map<String, String>>> prekladMap = new HashMap<String, Map<Integer, Map<String, String>>>();
			prekladMap.put(CudCiselnikPeer.TABLE_NAME, getDelegate().getPrekladRead().map(auth, jazyk, CudCiselnikPeer.TABLE_NAME, ciselnikIDs.toArray(new Integer[ciselnikIDs.size()])));

			List<DTOCiselnikMetaWS> listWS = new ArrayList<DTOCiselnikMetaWS>();
			for (DTOCiselnik dto : listDTO) {
				if (perns.contains(dto.getCiselnikID())) {
					listWS.add(copyDTO(dto, prekladMap));
				}
			}

			return listWS.toArray(new DTOCiselnikMetaWS[listWS.size()]);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikMetaList.error");
			return null;
		}
	}

	public DTOCiselnikStlpecMetaWS[] ciselnikStlpecMetaList(AuthInfo auth, Integer ciselnikID, String jazyk, Date d) throws AppException {

		try {
			DTOObjektStlpec[] perns = opravnenieList(auth, ciselnikID);

			DTOCiselnikMetaWS dtoWS = ciselnikMetaRead(auth, ciselnikID, jazyk, d, perns);
			if (StringUtils.isValid(dtoWS.getErrorMsg())) {
				DTOCiselnikStlpecMetaWS resultDTO = new DTOCiselnikStlpecMetaWS();
				resultDTO.setErrorMsg(dtoWS.getErrorMsg());
				return new DTOCiselnikStlpecMetaWS[] { resultDTO };
			}

			return dtoWS.getCiselnikStlpecList();

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecMetaList.error");
			return null;
		}
	}

	private Object lookupJavaType(String dbTyp, Value value) throws AppException {

		try {
			if (value.isNull()) {
				return null;
			}

			Object obj = null;

			if (_CudConsts.DB_TYP_STRING.equalsIgnoreCase(dbTyp)) {
				obj = value.asString();

			} else if (_CudConsts.DB_TYP_INTEGER.equalsIgnoreCase(dbTyp)) {
				obj = value.asIntegerObj();

			} else if (_CudConsts.DB_TYP_DOUBLE.equalsIgnoreCase(dbTyp)) {
				obj = value.asDoubleObj();

			} else if (_CudConsts.DB_TYP_DATE.equalsIgnoreCase(dbTyp)) {
				obj = value.asUtilDate();

			} else if (_CudConsts.DB_TYP_BOOLEAN.equalsIgnoreCase(dbTyp)) {
				obj = value.asString();
			}

			return obj;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupJavaType.error");
			return null;
		}
	}

	private DTOCiselnikDataWS dynCiselnikListOdDo(AuthInfo auth, Page page, String tabulka, List<DTOCiselnikStlpec> csList, Date datumOd, Date datumDo, Map<Integer, List<String>> obmMap) throws AppException {

		try {
			if (!StringUtils.isValid(csList) || csList.isEmpty()) {
				return new DTOCiselnikDataWS();
			}

			String pkNazov = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList).getNazov();

			String columns = "to_number(" + pkNazov + " || to_char(" + _CudConsts.NAZOV_PLATNOST_OD + ", \'YYYYMMDD\') || trim(to_char(" + _CudConsts.NAZOV_ID_ZMENA + ", \'0000000000\'))) as x1y1";

			for (DTOCiselnikStlpec dtoCS : csList) {
				columns += ", " + dtoCS.getNazov();
			}

			String conditionals = "";

			if (StringUtils.isValid(datumOd)) {
				conditionals = _CudConsts.NAZOV_PLATNOST_OD + " <= " + formatDateToTimestamp(datumOd);
			}

			if (StringUtils.isValid(datumDo)) {
				if (StringUtils.isValid(conditionals)) {
					conditionals += " AND ";
				}
				conditionals += "(" + formatDateToTimestamp(datumDo) + " <= " + _CudConsts.NAZOV_PLATNOST_DO + " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";
			}

			for (DTOCiselnikStlpec dtoCS : csList) {

				List<String> obmList = obmMap.get(dtoCS.getCiselnikStlpecID());
				if (StringUtils.isValid(obmList) && !obmList.isEmpty()) {

					String conditional = "";

					for (String obmValue : obmList) {

						String s = null;
						if (_CudConsts.DB_TYP_STRING.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_INTEGER.equals(dtoCS.getDbTyp())) {
							s = dtoCS.getNazov() + " = \'" + obmValue + "\'";

						} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getDbTyp())) {
							if ("ano".equalsIgnoreCase(obmValue) || "Áno".equalsIgnoreCase(obmValue)) {
								s = dtoCS.getNazov() + " = \'T\'";
							} else if ("nie".equalsIgnoreCase(obmValue)) {
								s = dtoCS.getNazov() + " = \'F\'";
							} else {
								s = dtoCS.getNazov() + " = \'" + obmValue + "\'";
							}

						} else if (_CudConsts.DB_TYP_DATE.equals(dtoCS.getDbTyp())) {
							s = "to_char(" + dtoCS.getNazov() + ", 'DD.MM.YYYY')" + " = " + obmValue;
						}

						conditional += StringUtils.isValid(conditional) ? " OR " + s : s;
					}

					conditionals += StringUtils.isValid(conditionals) ? " AND (" + conditional + " ) " : "(" + conditional + " )";
				}
			}

			String sql = "SELECT " + columns + " FROM " + tabulka + (StringUtils.isValid(conditionals) ? " WHERE " + conditionals : "");

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, "x1y1", auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTORecordWS> listDTO = new ArrayList<DTORecordWS>();

			if (lp.total_count >= ((page.PAGE - 1) * page.PAGE_SIZE)) {
				while (iter.hasNext() && listDTO.size() < page.PAGE_SIZE) {
					Record r = (Record) iter.next();

					List<Object> valueList = new ArrayList<Object>();

					for (DTOCiselnikStlpec dtoCS : csList) {
						Value value = rVal(r, dtoCS.getNazov());
						valueList.add(lookupJavaType(dtoCS.getDbTyp(), value));
					}

					DTORecordWS dtoWS = new DTORecordWS();
					dtoWS.setValues(valueList.toArray(new Object[valueList.size()]));

					listDTO.add(dtoWS);
				}
			}

			DTOCiselnikDataWS resultDTO = new DTOCiselnikDataWS();
			resultDTO.setRecordList(listDTO.toArray(new DTORecordWS[listDTO.size()]));
			resultDTO.setTotalCount(lp.total_count);

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "dynCiselnikListOdDo.error", auth);
			return null;
		}
	}

	private String validate(DTOPageWS dto, int maxPageSize) throws AppException {

		try {
			if (!StringUtils.isValid(dto)) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Page");
			}
			if (!StringUtils.isValid(dto.getPage())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Page.page");
			}
			if (!StringUtils.isValid(dto.getPageSize())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Page.pageSize");
			}
			if (dto.getPage().intValue() < 1) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_701, "Page.page");
			}
			if (dto.getPageSize().intValue() < 1 || dto.getPageSize().intValue() > maxPageSize) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3018, "Page.pageSize", "1", Integer.toString(maxPageSize));
			}

			return null;

		} catch (Exception e) {
			DBUtils.handleException(e, "validate.error");
			return null;
		}
	}

	public DTOCiselnikDataWS getCiselnikDataToDate(ZSRAuthInfo auth, Integer ciselnikID, Date d, String jazyk, DTOPageWS pageWS) throws AppException {

		try {
			if (!StringUtils.isValid(d)) {
				DTOCiselnikDataWS resultDTO = new DTOCiselnikDataWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datum"));
				return resultDTO;
			}

			d = DateUtils.removeTime(d);

			return ciselnikDataListOdDo(auth, ciselnikID, jazyk, pageWS, d, d, d);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataToDate.error");
			return null;
		}
	}

	public DTOCiselnikDataWS getCiselnikDataZmena(ZSRAuthInfo auth, Integer ciselnikID, String jazyk, DTOPageWS pageWS, Date d) throws AppException {

		try {
			if (!StringUtils.isValid(d)) {
				DTOCiselnikDataWS resultDTO = new DTOCiselnikDataWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "zmenaOd"));
				return resultDTO;
			}

			d = DateUtils.removeTime(d);

			return ciselnikDataListToZmena(auth, ciselnikID, jazyk, pageWS, d);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataZmena.error");
			return null;
		}
	}

	public DTOCiselnikDataWS ciselnikDataListOdDo(AuthInfo auth, Integer ciselnikID, String jazyk, DTOPageWS pageWS, Date d, Date datumOd, Date datumDo) throws AppException {

		try {
			DTOObjektStlpec[] perns = opravnenieList(auth, ciselnikID);

			DTOCiselnikMetaWS dtoCis = ciselnikMetaRead(auth, ciselnikID, jazyk, d, perns);
			if (StringUtils.isValid(dtoCis.getErrorMsg())) {
				DTOCiselnikDataWS resultDTO = new DTOCiselnikDataWS();
				resultDTO.setErrorMsg(dtoCis.getErrorMsg());
				return resultDTO;
			}

			List<DTOCiselnikStlpec> csList = new ArrayList<DTOCiselnikStlpec>();
			for (DTOCiselnikStlpecMetaWS dtoWS : dtoCis.getCiselnikStlpecList()) {
				csList.add(copyDTO(dtoWS));
			}

			String s = validate(pageWS, _CudConsts.WS_MAX_POCET);
			if (StringUtils.isValid(s)) {
				DTOCiselnikDataWS resultDTO = new DTOCiselnikDataWS();
				resultDTO.setErrorMsg(s);
				return resultDTO;
			}

			Map<Integer, List<String>> obmMap = new HashMap<Integer, List<String>>();
			for (DTOObjektStlpec dto : perns) {
				if (StringUtils.isValid(dto.getHodnota())) {
					if (!StringUtils.isValid(obmMap.get(dto.getIDCiselnikStlpec()))) {
						obmMap.put(dto.getIDCiselnikStlpec(), new ArrayList<String>());
					}
					obmMap.get(dto.getIDCiselnikStlpec()).add(dto.getHodnota());
				}
			}

			Page page = new Page(pageWS.getPage(), pageWS.getPageSize(), "1_ASC");
			DTOCiselnikDataWS resultDTO = dynCiselnikListOdDo(auth, page, dtoCis.getTabulka(), csList, datumOd, datumDo, obmMap);

			resultDTO.setCiselnikID(dtoCis.getCiselnikID());
			resultDTO.setCiselnikName(dtoCis.getTabulka());
			resultDTO.setCiselnikNazov(dtoCis.getNazov());
			resultDTO.setCiselnikStlpecList(dtoCis.getCiselnikStlpecList());

			return resultDTO;

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikDataListOdDo.error");
			return null;
		}
	}

	private DTOCiselnikDataWS dynCiselnikListToZmena(AuthInfo auth, Page page, String tabulka, List<DTOCiselnikStlpec> csList, Date d, Map<Integer, List<String>> obmMap) throws AppException {

		try {
			DTOCiselnikStlpec dtoCSPK = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList);

			String columns = "to_number(" + dtoCSPK.getNazov() + " || to_char(" + _CudConsts.NAZOV_PLATNOST_OD + ", \'YYYYMMDD\') || trim(to_char(" + _CudConsts.NAZOV_ID_ZMENA + ", \'0000000000\'))) as x1y1";

			for (DTOCiselnikStlpec dtoCS : csList) {
				columns += ", " + dtoCS.getNazov();
			}

			String conditionals = "(" + formatDateToTimestamp(d) + " <= " + _CudConsts.NAZOV_CAS_VYTVORENIA + " OR " + formatDateToTimestamp(d) + " <= " + _CudConsts.NAZOV_CAS_ZMENY + ")";

			for (DTOCiselnikStlpec dtoCS : csList) {

				List<String> obmList = obmMap.get(dtoCS.getCiselnikStlpecID());
				if (StringUtils.isValid(obmList) && !obmList.isEmpty()) {

					String conditional = "";

					for (String obmValue : obmList) {

						String s = null;
						if (_CudConsts.DB_TYP_STRING.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_INTEGER.equals(dtoCS.getDbTyp())) {
							s = dtoCS.getNazov() + " = \'" + obmValue + "\'";

						} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getDbTyp())) {
							if ("ano".equalsIgnoreCase(obmValue) || "Áno".equalsIgnoreCase(obmValue)) {
								s = dtoCS.getNazov() + " = \'T\'";
							} else if ("nie".equalsIgnoreCase(obmValue)) {
								s = dtoCS.getNazov() + " = \'F\'";
							} else {
								s = dtoCS.getNazov() + " = \'" + obmValue + "\'";
							}

						} else if (_CudConsts.DB_TYP_DATE.equals(dtoCS.getDbTyp())) {
							s = "to_char(" + dtoCS.getNazov() + ", 'DD.MM.YYYY')" + " = " + obmValue;
						}

						conditional += StringUtils.isValid(conditional) ? " OR " + s : s;
					}

					conditionals += StringUtils.isValid(conditionals) ? " AND (" + conditional + " ) " : "(" + conditional + " )";
				}
			}

			String sql = "SELECT " + columns + " FROM " + tabulka + " WHERE " + conditionals;

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, "x1y1", auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTORecordWS> listDTO = new ArrayList<DTORecordWS>();

			if (lp.total_count >= ((page.PAGE - 1) * page.PAGE_SIZE)) {
				while (iter.hasNext() && listDTO.size() < page.PAGE_SIZE) {
					Record r = (Record) iter.next();

					List<Object> valueList = new ArrayList<Object>();

					for (DTOCiselnikStlpec dtoCS : csList) {
						Value value = rVal(r, dtoCS.getNazov());
						valueList.add(lookupJavaType(dtoCS.getDbTyp(), value));
					}

					DTORecordWS dtoWS = new DTORecordWS();
					dtoWS.setValues(valueList.toArray(new Object[valueList.size()]));

					listDTO.add(dtoWS);
				}
			}

			DTOCiselnikDataWS resultDTO = new DTOCiselnikDataWS();
			resultDTO.setRecordList(listDTO.toArray(new DTORecordWS[listDTO.size()]));
			resultDTO.setTotalCount(lp.total_count);

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "dynCiselnikListToZmena.error", auth);
			return null;
		}
	}

	private String formatDateToTimestamp(Date d) throws AppException {

		try {
			if (!StringUtils.isValid(d)) {
				return null;
			}
			return " to_timestamp(\'" + sk.ditec.zsr.common.server.utils.DateUtils.formatDateDDMMYYYY(d) + "\', \'DD.MM.YYYY\')";

		} catch (Throwable t) {
			DBUtils.handleException(t, "formatDateToTimestamp.error");
			return null;
		}
	}

	private DTOCiselnikDataWS ciselnikDataListToZmena(AuthInfo auth, Integer ciselnikID, String jazyk, DTOPageWS pageWS, Date d) throws AppException {

		try {
			DTOObjektStlpec[] perns = opravnenieList(auth, ciselnikID);

			DTOCiselnikMetaWS dtoCis = ciselnikMetaRead(auth, ciselnikID, jazyk, null, perns);
			if (StringUtils.isValid(dtoCis.getErrorMsg())) {
				DTOCiselnikDataWS resultDTO = new DTOCiselnikDataWS();
				resultDTO.setErrorMsg(dtoCis.getErrorMsg());
				return resultDTO;
			}

			String s = validate(pageWS, _CudConsts.WS_MAX_POCET);
			if (StringUtils.isValid(s)) {
				DTOCiselnikDataWS resultDTO = new DTOCiselnikDataWS();
				resultDTO.setErrorMsg(s);
				return resultDTO;
			}

			List<DTOCiselnikStlpec> csList = new ArrayList<DTOCiselnikStlpec>();
			for (DTOCiselnikStlpecMetaWS dtoWS : dtoCis.getCiselnikStlpecList()) {
				csList.add(copyDTO(dtoWS));
			}

			Map<Integer, List<String>> obmMap = new HashMap<Integer, List<String>>();
			for (DTOObjektStlpec dto : perns) {
				if (StringUtils.isValid(dto.getHodnota())) {
					if (!StringUtils.isValid(obmMap.get(dto.getIDCiselnikStlpec()))) {
						obmMap.put(dto.getIDCiselnikStlpec(), new ArrayList<String>());
					}
					obmMap.get(dto.getIDCiselnikStlpec()).add(dto.getHodnota());
				}
			}

			Page page = new Page(pageWS.getPage(), pageWS.getPageSize(), "1_ASC");
			DTOCiselnikDataWS resultDTO = dynCiselnikListToZmena(auth, page, dtoCis.getTabulka(), csList, d, obmMap);

			resultDTO.setCiselnikID(dtoCis.getCiselnikID());
			resultDTO.setCiselnikName(dtoCis.getTabulka());
			resultDTO.setCiselnikNazov(dtoCis.getNazov());
			resultDTO.setCiselnikStlpecList(dtoCis.getCiselnikStlpecList());

			return resultDTO;

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikDataListToZmena.error");
			return null;
		}
	}

	private DTOZmenaWrapperWS zmenaList(AuthInfo auth, Page page, Integer[] ciselnikIDs, String stav, Date datumOd, Date datumDo, Integer[] ciselnikStlpecIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql1 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudZmenaStavHistPeer.ZMENA_STAV_HIST_ID, new DTOZmenaStavHist());

				crit.addSelectColumn(CudZmenaStavHistPeer.ID_ZMENA);
				crit.addAsColumn("min_cas_vytvorenia", "min(" + CudZmenaStavHistPeer.CAS_VYTVORENIA + ")");

				crit.addGroupByColumn(CudZmenaStavHistPeer.ID_ZMENA);

				if (ciselnikIDs.length == 1) {
					crit.addConditional(CudZmenaStavHistPeer.ID_CISELNIK, ciselnikIDs[0]);
				} else {
					crit.addIn(CudZmenaStavHistPeer.ID_CISELNIK, ciselnikIDs);
				}

				if (_CudConsts.ZMENA_STAV_SCH.equals(stav)) {
					crit.addIn(CudZmenaStavHistPeer.STAV, new String[] { _CudConsts.ZMENA_STAV_SCH, _CudConsts.ZMENA_STAV_PAU });

				} else if (_CudConsts.ZMENA_STAV_PAU.equals(stav)) {
					crit.addConditional(CudZmenaStavHistPeer.STAV, _CudConsts.ZMENA_STAV_PAU);
				}

				String where = formatDateToTimestamp(datumOd) + " <= min_cas_vytvorenia";
				if (StringUtils.isValid(datumDo)) {
					where += " AND min_cas_vytvorenia <= " + formatDateToTimestamp(datumDo);
				}

				// subSql1 = "SELECT id_zmena FROM (" + crit.getSQL() + ") WHERE " + where;
				subSql1 = CudZmenaPeer.ZMENA_ID + " IN (SELECT id_zmena FROM (" + crit.getSQL() + ") WHERE " + where + ")";
			}

			String subSql2 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudZmenaStavHistPeer.ZMENA_STAV_HIST_ID, new DTOZmenaStavHist());

				crit.addSelectColumn(CudZmenaStavHistPeer.ID_ZMENA);
				crit.addAsColumn("min_cas", "min(" + CudZmenaStavHistPeer.CAS_VYTVORENIA + ")");

				crit.addGroupByColumn(CudZmenaStavHistPeer.ID_ZMENA);

				if (ciselnikIDs.length == 1) {
					crit.addConditional(CudZmenaStavHistPeer.ID_CISELNIK, ciselnikIDs[0]);
				} else {
					crit.addIn(CudZmenaStavHistPeer.ID_CISELNIK, ciselnikIDs);
				}

				crit.addConditional(CudZmenaStavHistPeer.STAV, _CudConsts.ZMENA_STAV_SCH, false);

				subSql2 = crit.getSQL();
				subSql2 = StringUtils.replaceAll(subSql2, CudZmenaStavHistPeer.TABLE_NAME, "tt1");
				subSql2 = StringUtils.replaceAll(subSql2, "tt1 ", CudZmenaStavHistPeer.TABLE_NAME + " tt1 ");
			}

			String subSql3 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudZmenaStavHistPeer.ZMENA_STAV_HIST_ID, new DTOZmenaStavHist());

				crit.addSelectColumn(CudZmenaStavHistPeer.ID_ZMENA);
				crit.addAsColumn("min_cas", "min(" + CudZmenaStavHistPeer.CAS_VYTVORENIA + ")");

				crit.addGroupByColumn(CudZmenaStavHistPeer.ID_ZMENA);

				if (ciselnikIDs.length == 1) {
					crit.addConditional(CudZmenaStavHistPeer.ID_CISELNIK, ciselnikIDs[0]);
				} else {
					crit.addIn(CudZmenaStavHistPeer.ID_CISELNIK, ciselnikIDs);
				}

				crit.addConditional(CudZmenaStavHistPeer.STAV, _CudConsts.ZMENA_STAV_PAU, false);

				subSql3 = crit.getSQL();
				subSql3 = StringUtils.replaceAll(subSql3, CudZmenaStavHistPeer.TABLE_NAME, "tt2");
				subSql3 = StringUtils.replaceAll(subSql3, "tt2 ", CudZmenaStavHistPeer.TABLE_NAME + " tt2 ");
			}

			String subSql4 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, new DTOZmenaStlpec());

				crit.addSelectColumn(CudZmenaStlpecPeer.ID_ZMENA);

				if (ciselnikIDs.length == 1) {
					crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK, ciselnikIDs[0]);
				} else {
					crit.addIn(CudZmenaStlpecPeer.ID_CISELNIK, ciselnikIDs);
				}

				if (StringUtils.isValid(ciselnikStlpecIDs)) {
					if (ciselnikStlpecIDs.length == 1) {
						crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, ciselnikStlpecIDs[0]);
					} else {
						crit.addIn(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, ciselnikStlpecIDs);
					}
				}

				// subSql4 = crit.getSQL();
				subSql4 = CudZmenaPeer.ZMENA_ID + " IN (" + crit.getSQL() + ")";
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);
			crit.addSelectColumn(CudZmenaPeer.ID_CISELNIK);
			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudZmenaPeer.STAV);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);

			// join CUD_CISELNIK
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addJoin(CudZmenaPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);

			// join CUD_ZMENA_STAV_HIST
			crit.addAlias("t1", CudZmenaStavHistPeer.TABLE_NAME);
			crit.addAsColumn("cas_sch", "t1.min_cas");
			crit.addJoin(CudZmenaPeer.ZMENA_ID, "t1.id_zmena", MyCriteria2.LEFT_JOIN);

			// join CUD_ZMENA_STAV_HIST
			crit.addAlias("t2", CudZmenaStavHistPeer.TABLE_NAME);
			crit.addAsColumn("cas_pau", "t2.min_cas");
			crit.addJoin(CudZmenaPeer.ZMENA_ID, "t2.id_zmena", MyCriteria2.LEFT_JOIN);

			if (ciselnikIDs.length == 1) {
				crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikIDs[0]);
			} else {
				crit.addIn(CudZmenaPeer.ID_CISELNIK, ciselnikIDs);
			}

			// crit.addCustomSql(CudZmenaPeer.ZMENA_ID, CudZmenaPeer.ZMENA_ID + " IN (" + subSql1 + " UNION " + subSql4 + ")");
			crit.addCustomSql(CudZmenaPeer.ZMENA_ID, subSql1 + " AND " + subSql4);

			String sql = crit.getSQL();
			sql = StringUtils.replaceAll(sql, "CUD_ZMENA_STAV_HIST t1", "(" + subSql2 + ") t1");
			sql = StringUtils.replaceAll(sql, "CUD_ZMENA_STAV_HIST t2", "(" + subSql3 + ") t2");

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudZmenaPeer.ZMENA_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOZmenaWS> listDTO = new ArrayList<DTOZmenaWS>();

			if (lp.total_count >= ((page.PAGE - 1) * page.PAGE_SIZE)) {
				while (iter.hasNext() && listDTO.size() < page.PAGE_SIZE) {
					Record r = (Record) iter.next();

					DTOZmenaWS dto = new DTOZmenaWS();
					dto.setZmenaID(rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj());
					dto.setCiselnikID(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
					dto.setRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
					dto.setOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
					dto.setStav(rVal(r, CudZmenaPeer.STAV).asString());
					dto.setPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());

					dto.setTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());

					dto.setSchvalenie(rVal(r, "cas_sch").asUtilDate());
					dto.setPublikovanie(rVal(r, "cas_pau").asUtilDate());

					listDTO.add(dto);
				}
			}

			DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
			resultDTO.setZmenaList(listDTO.toArray(new DTOZmenaWS[listDTO.size()]));
			resultDTO.setTotalCount(lp.total_count);

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "zmenaList.error", auth);
			return null;
		}
	}

	private Map<Integer, List<DTOZmenaStlpecWS>> zmenaStlpecMap(AuthInfo auth, Integer[] zmenaIDs, Integer[] ciselnikIDs, Integer[] ciselnikStlpecIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(zmenaIDs)) {
				return new HashMap<Integer, List<DTOZmenaStlpecWS>>();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, new DTOZmenaStlpec());

			crit.addSelectColumn(CudZmenaStlpecPeer.ZMENA_STLPEC_ID);
			crit.addSelectColumn(CudZmenaStlpecPeer.ID_ZMENA);
			crit.addSelectColumn(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudZmenaStlpecPeer.OLD_VALUE);
			crit.addSelectColumn(CudZmenaStlpecPeer.NEW_VALUE);

			// join CUD_CISELNIK_STLPEC
			crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JE_DB_STRING);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_FK_NAZOV);
			crit.addJoin(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addJoin(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);

			if (zmenaIDs.length == 1) {
				crit.addConditional(CudZmenaStlpecPeer.ID_ZMENA, zmenaIDs[0]);
			} else {
				crit.addIn(CudZmenaStlpecPeer.ID_ZMENA, zmenaIDs);
			}

			if (ciselnikIDs.length == 1) {
				crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK, ciselnikIDs[0]);
			} else {
				crit.addIn(CudZmenaStlpecPeer.ID_CISELNIK, ciselnikIDs);
			}

			if (StringUtils.isValid(ciselnikStlpecIDs)) {
				if (ciselnikStlpecIDs.length == 1) {
					crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, ciselnikStlpecIDs[0]);
				} else {
					crit.addIn(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, ciselnikStlpecIDs);
				}
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOZmenaStlpecWS>> resultMap = new HashMap<Integer, List<DTOZmenaStlpecWS>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Integer zmenaID = rVal(r, CudZmenaStlpecPeer.ID_ZMENA).asIntegerObj();

				DTOZmenaStlpecWS dto = new DTOZmenaStlpecWS();
				dto.setZmenaStlpecID(rVal(r, CudZmenaStlpecPeer.ZMENA_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudZmenaStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setOldValue(rVal(r, CudZmenaStlpecPeer.OLD_VALUE).asString());
				dto.setNewValue(rVal(r, CudZmenaStlpecPeer.NEW_VALUE).asString());

				dto.setTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setDbDlzka(rVal(r, CudCiselnikStlpecPeer.DLZKA).asIntegerObj());
				dto.setDbTyp("T".equals(rVal(r, CudCiselnikStlpecPeer.JE_DB_STRING).asString()) ? _CudConsts.DB_TYP_STRING : rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setFkPk(rVal(r, CudCiselnikStlpecPeer.FK1_PK_NAZOV).asString());
				dto.setFkStlpec(rVal(r, CudCiselnikStlpecPeer.FK1_FK_NAZOV).asString());

				dto.setFkTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());

				if (!StringUtils.isValid(resultMap.get(zmenaID))) {
					resultMap.put(zmenaID, new ArrayList<DTOZmenaStlpecWS>());
				}
				resultMap.get(zmenaID).add(dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "zmenaStlpecMap.error", auth);
			return null;
		}
	}

	private DTOZmenaWrapperWS zmenaList(AuthInfo auth, Integer[] ciselnikIDs, String jazyk, DTOPageWS pageWS, Date datumOd, Date datumDo, String stav) throws AppException {

		try {
			Map<Integer, Set<Integer>> pernMap = opravnenieMap(auth, ciselnikIDs);

			Map<Integer, DTOCiselnik> ciselnikMap = new HashMap<Integer, DTOCiselnik>();
			String err = ciselnikMetaLoad(auth, ciselnikIDs, ciselnikMap, pernMap);
			if (StringUtils.isValid(err)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(err);
				return resultDTO;
			}

			err = validate(pageWS, 100);
			if (StringUtils.isValid(err)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(err);
				return resultDTO;
			}

			Set<Integer> set = new HashSet<Integer>();
			for (Integer ciselnikID : pernMap.keySet()) {
				set.addAll(pernMap.get(ciselnikID));
			}
			Integer[] ciselnikStlpecIDs = set.toArray(new Integer[set.size()]);

			Page page = new Page(pageWS.getPage(), pageWS.getPageSize(), "6_ASC");
			DTOZmenaWrapperWS resultDTO = zmenaList(auth, page, ciselnikIDs, stav, datumOd, datumDo, ciselnikStlpecIDs);

			set.clear();
			for (DTOZmenaWS dtoWS : resultDTO.getZmenaList()) {
				set.add(dtoWS.getZmenaID());
			}
			Integer[] zmenaIDs = set.toArray(new Integer[set.size()]);

			Map<Integer, List<DTOZmenaStlpecWS>> zmenaStlpecMap = zmenaStlpecMap(auth, zmenaIDs, ciselnikIDs, ciselnikStlpecIDs);

			Map<String, Map<Integer, Map<String, String>>> prekladMap = new HashMap<String, Map<Integer, Map<String, String>>>();
			prekladMap.put(CudCiselnikStlpecPeer.TABLE_NAME, getDelegate().getPrekladRead().map(auth, jazyk, CudCiselnikStlpecPeer.TABLE_NAME, ciselnikStlpecIDs));

			for (DTOZmenaWS dtoWS : resultDTO.getZmenaList()) {
				List<DTOZmenaStlpecWS> listWS = zmenaStlpecMap.get(dtoWS.getZmenaID());
				if (StringUtils.isValid(listWS)) {
					for (DTOZmenaStlpecWS dtoStlpecWS : listWS) {
						dtoStlpecWS.setNadpis(lookupPreklad(prekladMap.get(CudCiselnikStlpecPeer.TABLE_NAME), dtoStlpecWS.getIDCiselnikStlpec(), trimColumnName(CudCiselnikStlpecPeer.NADPIS), dtoStlpecWS.getNadpis()));
					}
					dtoWS.setZmenaStlpecList(listWS.toArray(new DTOZmenaStlpecWS[listWS.size()]));
				}
			}

			return resultDTO;

		} catch (Exception e) {
			DBUtils.handleException(e, "zmenaList.error");
			return null;
		}
	}

	private DTOZmenaWrapperWS zmenaList(AuthInfo auth, DTOPageWS pageWS, Date datumOd, Date datumDo, String stav) throws AppException {

		try {
			Set<Integer> opravnenieCiselnikIDs = opravnenieSet(auth);
			if (opravnenieCiselnikIDs.isEmpty()) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setTotalCount(0);
				return resultDTO;
			}

			Integer[] zmenaCiselnikIDs = getDelegate().getZmenaStavHistRead().ciselnikIDs(auth, datumOd, datumDo, stav);

			Set<Integer> set = new HashSet<Integer>();
			for (Integer ciselnikID : zmenaCiselnikIDs) {
				if (opravnenieCiselnikIDs.contains(ciselnikID)) {
					set.add(ciselnikID);
				}
			}
			Integer[] ciselnikIDs = set.toArray(new Integer[set.size()]);

			Page page = new Page(pageWS.getPage(), pageWS.getPageSize(), "1_DESC");
			DTOZmenaWrapperWS resultDTO = zmenaList(auth, page, ciselnikIDs, stav, datumOd, datumDo, null);

			set.clear();
			for (DTOZmenaWS dtoWS : resultDTO.getZmenaList()) {
				set.add(dtoWS.getZmenaID());
			}
			Integer[] zmenaIDs = set.toArray(new Integer[set.size()]);

			Map<Integer, List<DTOZmenaStlpecWS>> zmenaStlpecMap = zmenaStlpecMap(auth, zmenaIDs, ciselnikIDs, null);

			for (DTOZmenaWS dtoWS : resultDTO.getZmenaList()) {
				List<DTOZmenaStlpecWS> listWS = zmenaStlpecMap.get(dtoWS.getZmenaID());
				if (StringUtils.isValid(listWS)) {
					dtoWS.setZmenaStlpecList(listWS.toArray(new DTOZmenaStlpecWS[listWS.size()]));
				}
			}

			return resultDTO;

		} catch (Exception e) {
			DBUtils.handleException(e, "zmenaList.error");
			return null;
		}
	}

	public DTOZmenaWrapperWS getZmenyListDatumOd(AuthInfo auth, Integer[] ciselnikIDs, Date d, String jazyk, DTOPageWS pageWS) throws AppException {

		try {
			if (!StringUtils.isValid(d)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumOd"));
				return resultDTO;
			}

			d = DateUtils.removeTime(d);

			return zmenaList(auth, ciselnikIDs, jazyk, pageWS, d, null, _CudConsts.ZMENA_STAV_PAU);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListDatumOd.error");
			return null;
		}
	}

	public DTOZmenaWrapperWS getZmenyListDatumOdDo(AuthInfo auth, Integer[] ciselnikIDs, Date datumOd, Date datumDo, String jazyk, DTOPageWS pageWS) throws AppException {

		try {
			if (!StringUtils.isValid(datumOd)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumOd"));
				return resultDTO;
			}
			if (!StringUtils.isValid(datumDo)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumDo"));
				return resultDTO;
			}

			datumOd = DateUtils.removeTime(datumOd);
			datumDo = DateUtils.removeTime(datumDo);

			return zmenaList(auth, ciselnikIDs, jazyk, pageWS, datumOd, datumDo, _CudConsts.ZMENA_STAV_PAU);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListDatumOdDo.error");
			return null;
		}
	}

	public DTOZmenaWrapperWS getZmenyListSchvaleneDatumOd(AuthInfo auth, Integer[] ciselnikIDs, Date d, String jazyk, DTOPageWS pageWS) throws AppException {

		try {
			if (!StringUtils.isValid(d)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumOd"));
				return resultDTO;
			}

			d = DateUtils.removeTime(d);

			return zmenaList(auth, ciselnikIDs, jazyk, pageWS, d, null, _CudConsts.ZMENA_STAV_SCH);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListSchvaleneDatumOd.error");
			return null;
		}
	}

	public DTOZmenaWrapperWS getZmenyListSchvaleneDatumOdDo(AuthInfo auth, Integer[] ciselnikIDs, Date datumOd, Date datumDo, String jazyk, DTOPageWS pageWS) throws AppException {

		try {
			if (!StringUtils.isValid(datumOd)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumOd"));
				return resultDTO;
			}
			if (!StringUtils.isValid(datumDo)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumDo"));
				return resultDTO;
			}

			datumOd = DateUtils.removeTime(datumOd);
			datumDo = DateUtils.removeTime(datumDo);

			return zmenaList(auth, ciselnikIDs, jazyk, pageWS, datumOd, datumDo, _CudConsts.ZMENA_STAV_SCH);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListSchvaleneDatumOdDo.error");
			return null;
		}
	}

	private Integer[] zmenaCiselnikIDs(AuthInfo auth, Integer[] ciselnikIDs, Date datumOd, Integer[] ciselnikStlpecIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql1 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudZmenaStavHistPeer.ZMENA_STAV_HIST_ID, new DTOZmenaStavHist());

				crit.addSelectColumn(CudZmenaStavHistPeer.ID_ZMENA);
				crit.addAsColumn("min_cas_vytvorenia", "min(" + CudZmenaStavHistPeer.CAS_VYTVORENIA + ")");

				crit.addGroupByColumn(CudZmenaStavHistPeer.ID_ZMENA);

				if (ciselnikIDs.length == 1) {
					crit.addConditional(CudZmenaStavHistPeer.ID_CISELNIK, ciselnikIDs[0]);
				} else {
					crit.addIn(CudZmenaStavHistPeer.ID_CISELNIK, ciselnikIDs);
				}

				crit.addConditional(CudZmenaStavHistPeer.STAV, _CudConsts.ZMENA_STAV_PAU);

				String where = formatDateToTimestamp(datumOd) + " <= min_cas_vytvorenia";

				subSql1 = CudZmenaPeer.ZMENA_ID + " IN (SELECT id_zmena FROM (" + crit.getSQL() + ") WHERE " + where + ")";
			}

			String subSql2 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, new DTOZmenaStlpec());

				crit.addSelectColumn(CudZmenaStlpecPeer.ID_ZMENA);

				if (ciselnikIDs.length == 1) {
					crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK, ciselnikIDs[0]);
				} else {
					crit.addIn(CudZmenaStlpecPeer.ID_CISELNIK, ciselnikIDs);
				}

				if (ciselnikStlpecIDs.length == 1) {
					crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, ciselnikStlpecIDs[0]);
				} else {
					crit.addIn(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, ciselnikStlpecIDs);
				}

				subSql2 = CudZmenaPeer.ZMENA_ID + " IN (" + crit.getSQL() + ")";
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.setDistinct();
			crit.addSelectColumn(CudZmenaPeer.ID_CISELNIK);

			if (ciselnikIDs.length == 1) {
				crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikIDs[0]);
			} else {
				crit.addIn(CudZmenaPeer.ID_CISELNIK, ciselnikIDs);
			}

			crit.addCustomSql(CudZmenaPeer.ZMENA_ID, subSql1 + " AND " + subSql2);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> resultSet = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				resultSet.add(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
			}

			return resultSet.toArray(new Integer[resultSet.size()]);

		} catch (Exception e) {
			DBUtils.handleException(e, "zmenaList.error");
			return null;
		}
	}

	private DTOZmenaCiselnikArrayWS zmenaCiselnikIDs(AuthInfo auth, Integer[] ciselnikIDs, Date datumOd) throws AppException {

		try {
			Map<Integer, Set<Integer>> pernMap = opravnenieMap(auth, ciselnikIDs);

			Map<Integer, DTOCiselnik> ciselnikMap = new HashMap<Integer, DTOCiselnik>();
			String err = ciselnikMetaLoad(auth, ciselnikIDs, ciselnikMap, pernMap);
			if (StringUtils.isValid(err)) {
				DTOZmenaCiselnikArrayWS resultDTO = new DTOZmenaCiselnikArrayWS();
				resultDTO.setErrorMsg(err);
				return resultDTO;
			}

			Set<Integer> set = new HashSet<Integer>();
			for (Integer ciselnikID : pernMap.keySet()) {
				set.addAll(pernMap.get(ciselnikID));
			}
			Integer[] ciselnikStlpecIDs = set.toArray(new Integer[set.size()]);

			Integer[] poleIDs = zmenaCiselnikIDs(auth, ciselnikIDs, datumOd, ciselnikStlpecIDs);

			DTOZmenaCiselnikArrayWS resultDTO = new DTOZmenaCiselnikArrayWS();
			resultDTO.setIds(poleIDs);

			return resultDTO;

		} catch (Exception e) {
			DBUtils.handleException(e, "zmenaCiselnikIDs.error");
			return null;
		}
	}

	public DTOZmenaCiselnikArrayWS getCiselnikZmena(AuthInfo auth, Integer[] ciselnikIDs, Date d) throws AppException {

		try {
			if (!StringUtils.isValid(d)) {
				DTOZmenaCiselnikArrayWS resultDTO = new DTOZmenaCiselnikArrayWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumOd"));
				return resultDTO;
			}

			d = DateUtils.removeTime(d);

			return zmenaCiselnikIDs(auth, ciselnikIDs, d);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikZmena.error");
			return null;
		}
	}

	public DTOZmenaWrapperWS getZmeny(AuthInfo auth, Date datumOd, DTOPageWS pageWS, String stav) throws AppException {

		try {
			if (!StringUtils.isValid(datumOd)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumOd"));
				return resultDTO;
			}

			datumOd = DateUtils.removeTime(datumOd);

			return zmenaList(auth, pageWS, datumOd, null, stav);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmeny.error");
			return null;
		}
	}

	public DTOZmenaWrapperWS getZmenyDatumDo(AuthInfo auth, Date datumOd, Date datumDo, DTOPageWS pageWS, String stav) throws AppException {

		try {
			if (!StringUtils.isValid(datumOd)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumOd"));
				return resultDTO;
			}

			if (!StringUtils.isValid(datumDo)) {
				DTOZmenaWrapperWS resultDTO = new DTOZmenaWrapperWS();
				resultDTO.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "datumDo"));
				return resultDTO;
			}

			datumOd = DateUtils.removeTime(datumOd);
			datumDo = DateUtils.removeTime(datumDo);

			return zmenaList(auth, pageWS, datumOd, datumDo, stav);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyDatumDo.error");
			return null;
		}
	}

	private String validate(DTOUpdZmenaWS dto) throws AppException {

		try {
			if (StringUtils.isValid(dto.getCiselnikNazov())) {
				_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "ciselnikNazov");
			}
			if (StringUtils.isValid(dto.getZapisatZmeny())) {
				_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "zapisatZmeny");
			}

			if (StringUtils.isValid(dto.getZaznam())) {
				_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "operacia");
			}
			if (StringUtils.isValid(dto.getZaznam().getOperacia())) {
				_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "operacia");
			}
			if (!_CudConsts.ZMENA_OPERACIA_N.equals(dto.getZaznam().getOperacia())) {
				if (!_CudConsts.ZMENA_OPERACIA_U.equals(dto.getZaznam().getOperacia())) {
					if (!_CudConsts.ZMENA_OPERACIA_Z.equals(dto.getZaznam().getOperacia())) {
						if (!_CudConsts.ZMENA_OPERACIA_D.equals(dto.getZaznam().getOperacia())) {
							_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3031);
						}
					}
				}
			}
			if (_CudConsts.ZMENA_OPERACIA_U.equals(dto.getZaznam().getOperacia()) || _CudConsts.ZMENA_OPERACIA_D.equals(dto.getZaznam().getOperacia()) || _CudConsts.ZMENA_OPERACIA_Z.equals(dto.getZaznam().getOperacia())) {
				if (StringUtils.isValid(dto.getZaznam().getID())) {
					_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "ID");
				}
			}

			if (StringUtils.isValid(dto.getZaznam().getPlatnostOd())) {
				_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "platnostOd");
			}

			return null;

		} catch (Exception e) {
			DBUtils.handleException(e, "validate.error");
			return null;
		}
	}

	private DTOUpdZmenaResponseWS createDTOUpdZmenaResponseWS(DTOUpdZmenaWS dto, String err) throws AppException {

		try {
			DTOUpdZmenaResponseWS dtoRes = new DTOUpdZmenaResponseWS();

			if (!StringUtils.isValid(err)) {
				dtoRes.setKodSpracovania(0);
				dtoRes.setPopisSpracovania("Uspesne vybavenie sluzby");

			} else {
				if ("T".equals(dto.getZapisatZmeny())) {
					dtoRes.setKodSpracovania(203);
					dtoRes.setPopisSpracovania(err);
				} else {
					dtoRes.setKodSpracovania(204);
					dtoRes.setPopisSpracovania(err);
				}
			}

			return dtoRes;

		} catch (Exception e) {
			DBUtils.handleException(e, "createDTOUpdZmenaResponseWS.error");
			return null;
		}
	}

	private DTOUpdZmenaResponseWS createDTOUpdZmenaResponseWS(DTOUpdZmenaWS dto, DTOImport dtoImport) throws AppException {

		try {
			String err = null;

			if (StringUtils.isValid(dtoImport.getImportZmenaList())) {
				if (StringUtils.isValid(dtoImport.getImportZmenaList()[0].getImportMsgList())) {
					for (DTOImportMsg dtoMsg : dtoImport.getImportZmenaList()[0].getImportMsgList()) {
						if (_CudConsts.IMPORT_MSG_TYP_ERROR.equals(dtoMsg.getTyp())) {
							err = dtoMsg.getMsg();
						}
					}
				}
			}

			return createDTOUpdZmenaResponseWS(dto, err);

		} catch (Exception e) {
			DBUtils.handleException(e, "createDTOUpdZmenaResponseWS.error");
			return null;
		}
	}

	public DTOUpdZmenaResponseWS updZmenaHodnotCiselnikaUpdate(AuthInfo auth, DTOUpdZmenaWS dto) throws AppException {

		try {
			String err = validate(dto);
			if (StringUtils.isValid(err)) {
				return createDTOUpdZmenaResponseWS(dto, err);
			}
			DTOCiselnik dtoCis = getDelegate().getCiselnikRead().readLight(auth, dto.getCiselnikNazov().toUpperCase());
			if (!StringUtils.isValid(dtoCis)) {
				return createDTOUpdZmenaResponseWS(dto, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_607));
			}

			List<DTOCiselnikStlpec> csList = new ArrayList<DTOCiselnikStlpec>();
			csList.add(getDelegate().getCiselnikStlpecRead().readLight(auth, dtoCis.getCiselnikID(), _CudConsts.NAZOV_HIST_ID));

			String pkNazov = getDelegate().getCiselnikStlpecRead().readByPrimaryKey(auth, dtoCis.getCiselnikID());

			Map<String, String> rowMap = null;
			if (StringUtils.isValid(dto.getZaznam().getID())) {
				rowMap = getDelegate().getDynCiselnikRead().readLight(auth, dtoCis.getTabulka(), csList, pkNazov, dto.getZaznam().getID().toString(), _CudConsts.DB_TYP_INTEGER, dto.getZaznam().getPlatnostOd(), "F");
			} else {
				rowMap = new HashMap<String, String>();
			}
			DTODynCiselnikLD dtoDynF = new DTODynCiselnikLD();
			dtoDynF.setHistID(StringUtils.isValid(rowMap.get(_CudConsts.NAZOV_HIST_ID)) ? Integer.parseInt(rowMap.get(_CudConsts.NAZOV_HIST_ID)) : null);
			dtoDynF.setCiselnikID(dtoCis.getCiselnikID());
			dtoDynF.setCiselnikTabulka(dtoCis.getTabulka());
			dtoDynF.setPlatnostOd(dto.getZaznam().getPlatnostOd());
			DTODynCiselnikLD dtoDyn = getDelegate().getDynCiselnikRead().loadData(auth, dtoDynF);

			DTOImport dtoImport = new DTOImport();
			dtoImport.setIDCiselnik(dtoCis.getCiselnikID());
			dtoImport.setCiselnikNazov(dtoCis.getNazov());
			dtoImport.setCiselnikTabulka(dtoCis.getTabulka());

			DTOImportZmena dtoIZ = new DTOImportZmena();
			dtoIZ.setRowID(dto.getZaznam().getID());
			dtoIZ.setOperacia(dto.getZaznam().getOperacia());
			dtoIZ.setPlatnostOd(dto.getZaznam().getPlatnostOd());
			dtoIZ.setCasSchvaleniaGr(dto.getZaznam().getDatumSchvalenia());
			dtoIZ.setPoznamka(dto.getZaznam().getPoznamka());

			dtoImport.setImportZmenaList(new DTOImportZmena[] { dtoIZ });

			List<DTOImportZmenaStlpec> izsList = new ArrayList<DTOImportZmenaStlpec>();
			for (DTOUpdStlpecWS dtoStlpec : dto.getZaznam().getStlpce()) {

				DTOCiselnikStlpecGui dtoGui = null;

				for (DTOCiselnikStlpecGui dtoGuiItem : dtoDyn.getMetaList()) {
					if (dtoStlpec.getNazovStlpca().toUpperCase().equals(dtoGuiItem.getCiselnikStlpecNazov())) {
						dtoGui = dtoGuiItem;
						break;
					}
				}

				if (!StringUtils.isValid(dtoGui)) {
					continue;
				}

				DTOImportZmenaStlpec dtoIZS = new DTOImportZmenaStlpec();
				dtoIZS.setIDCiselnikStlpec(dtoGui.getIDCiselnikStlpec());
				dtoIZS.setCiselnikStlpecNazov(dtoStlpec.getNazovStlpca().toUpperCase());
				dtoIZS.setNewValue(dtoStlpec.getNovaHodnota());
				izsList.add(dtoIZS);
			}
			dtoIZ.setImportZmenaStlpecList(izsList.toArray(new DTOImportZmenaStlpec[izsList.size()]));

			getDelegate().getDynCiselnikRead().updateKontrola(auth, dtoDyn.getMetaList(), dtoImport);
			if (_CudConsts.IMPORT_STAV_ERROR.equals(dtoImport.getStav())) {
				return createDTOUpdZmenaResponseWS(dto, dtoImport);
			}

			getDelegate().getDynCiselnikModify().update(auth, dtoImport, dtoDyn.getValueDTO().getValues(), dtoDyn.getMetaList());

			return createDTOUpdZmenaResponseWS(dto, (String) null);

		} catch (Exception e) {
			DBUtils.handleException(e, "updZmenaHodnotCiselnikaUpdate.error");
			return null;
		}
	}

}
