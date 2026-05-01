package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.DateUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOKompatibilita;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikGuiPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecGuiPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;

import com.workingdogs.village.Record;

public class CudKompatibilitaReadClass extends _CudBaseClass {

	private List<DTOCiselnikStlpecGui> createNovyStlpecList(List<DTOCiselnikStlpecGui> oldList, List<DTOCiselnikStlpecGui> newList) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			for (DTOCiselnikStlpecGui dtoNew : newList) {
				if (_CudKontrolaUtils.jeAtributTechnicky(dtoNew)) {
					continue;
				}
				if (!"T".equals(dtoNew.getFormZobrazenie())) {
					continue;
				}
				boolean b = true;
				for (DTOCiselnikStlpecGui dtoOld : oldList) {
					if (dtoOld.getIDCiselnikStlpec().intValue() == dtoNew.getIDCiselnikStlpec().intValue()) {
						if ("T".equals(dtoOld.getFormZobrazenie())) {
							b = false;
							break;
						}
					}
				}
				if (b) {
					resultList.add(dtoNew);
				}
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createNovyStlpecList.error");
			return null;
		}
	}

	private List<DTOCiselnikStlpecGui[]> createZmenenyStlpecList(List<DTOCiselnikStlpecGui> oldList, List<DTOCiselnikStlpecGui> newList) throws AppException {

		try {
			List<DTOCiselnikStlpecGui[]> resultList = new ArrayList<DTOCiselnikStlpecGui[]>();

			for (DTOCiselnikStlpecGui dtoNew : newList) {
				if (_CudKontrolaUtils.jeAtributTechnicky(dtoNew)) {
					continue;
				}
				if (!"T".equals(dtoNew.getFormZobrazenie())) {
					continue;
				}
				DTOCiselnikStlpecGui dtoOld = null;
				for (DTOCiselnikStlpecGui dto : oldList) {
					if (dto.getIDCiselnikStlpec().intValue() == dtoNew.getIDCiselnikStlpec().intValue()) {
						if ("T".equals(dto.getFormZobrazenie())) {
							dtoOld = dto;
							break;
						}
					}
				}
				if (StringUtils.isValid(dtoOld)) {
					if (!_CudKontrolaUtils.equals(dtoOld.getPovinny(), dtoNew.getPovinny()) || !_CudKontrolaUtils.equals(dtoOld.getDlzka(), dtoNew.getDlzka()) || !_CudKontrolaUtils.equals(dtoOld.getDecimals(), dtoNew.getDecimals()))
						resultList.add(new DTOCiselnikStlpecGui[] { dtoOld, dtoNew });
				}
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createZmenenyStlpecList.error");
			return null;
		}
	}

	private List<DTOCiselnikStlpecGui> createZrusenyStlpecList(List<DTOCiselnikStlpecGui> oldList, List<DTOCiselnikStlpecGui> newList) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			for (DTOCiselnikStlpecGui dtoOld : oldList) {
				if (_CudKontrolaUtils.jeAtributTechnicky(dtoOld)) {
					continue;
				}
				if (!"T".equals(dtoOld.getFormZobrazenie())) {
					continue;
				}
				boolean b = true;
				for (DTOCiselnikStlpecGui dtoNew : newList) {
					if (dtoNew.getIDCiselnikStlpec().intValue() == dtoOld.getIDCiselnikStlpec().intValue()) {
						if ("T".equals(dtoNew.getFormZobrazenie())) {
							b = false;
							break;
						}
					}
				}
				if (b) {
					resultList.add(dtoOld);
				}
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createZrusenyStlpecList.error");
			return null;
		}
	}

	private Map<String, Set<Integer>> nazovMap(AuthInfo auth, Set<String> set) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (set.isEmpty()) {
				return new HashMap<String, Set<Integer>>();
			}

			String subSql1 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

				crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);

				crit.addConditional(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, "T", false);

				crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql1 = crit.getSQL();
			}

			String subSql2 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

				crit.addSelectColumn(CudCiselnikGuiPeer.ID_CISELNIK);

				crit.addConditional(CudCiselnikGuiPeer.STAV, _CudConsts.CISELNIK_GUI_STAV_PUB, false);

				crit.add(CudCiselnikGuiPeer.PLATNOST_DO, null);

				crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql2 = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);

			if (set.size() == 1) {
				crit.addConditional(CudCiselnikStlpecPeer.NAZOV, set.iterator().next(), false);
			} else {
				crit.addIn(CudCiselnikStlpecPeer.NAZOV, set.toArray(new String[set.size()]));
			}

			crit.addCustomSql(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID + " IN (" + subSql1 + ")");
			crit.addCustomSql(CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikStlpecPeer.ID_CISELNIK + " IN (" + subSql2 + ")");

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<String, Set<Integer>> resultMap = new HashMap<String, Set<Integer>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Integer ciselnikID = rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj();
				String nazov = rVal(r, CudCiselnikStlpecPeer.NAZOV).asString();

				if (!StringUtils.isValid(resultMap.get(nazov))) {
					resultMap.put(nazov, new HashSet<Integer>());
				}
				resultMap.get(nazov).add(ciselnikID);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "nazovMap.error", auth);
			return null;
		}
	}

	public DTOKompatibilita kontrolaReferencia(AuthInfo auth, List<DTOCiselnikStlpecGui> guiList, Map<Integer, DTOCiselnikStlpec> csMap, Map<Integer, DTOCiselnik> cisMap, String zdroj) throws AppException {

		try {
			Set<String> set = new HashSet<String>();
			for (DTOCiselnikStlpecGui dto : guiList) {
				DTOCiselnikStlpec dtoCS = csMap.get(dto.getIDCiselnikStlpec());
				if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getTyp())) {
					if (StringUtils.isValid(dto.getFk1FkNazov())) {
						set.add(dto.getFk1FkNazov());
					}
					if (StringUtils.isValid(dto.getFk2FkNazov())) {
						set.add(dto.getFk2FkNazov());
					}
				}
			}
			Map<String, Set<Integer>> mapaIds = nazovMap(auth, set);

			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> errorSet = new HashSet<String>();

			for (DTOCiselnikStlpecGui dtoGui : guiList) {

				DTOCiselnikStlpec dtoCS = csMap.get(dtoGui.getIDCiselnikStlpec());

				if (StringUtils.isValid(dtoCS) && _CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getTyp())) {

					Set<Integer> ids = mapaIds.get(dtoGui.getFk1FkNazov());
					if (!StringUtils.isValid(ids) || !ids.contains(dtoCS.getFk1IDCiselnik())) {
						String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dtoGui.getNadpis() : dtoCS.getNazov();
						errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3005, colName));
					}

					if (StringUtils.isValid(dtoGui.getFk2IDCiselnik())) {

						ids = mapaIds.get(dtoGui.getFk2FkNazov());
						if (!StringUtils.isValid(ids) || !ids.contains(dtoGui.getFk2IDCiselnik())) {
							String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dtoGui.getNadpis() : dtoCS.getNazov();
							errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3005, colName));
						}
					}
				}
			}

			if (!errorSet.isEmpty()) {
				resultDTO.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				resultDTO.setKompatibilita("F");
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaReferencia.error", auth);
			return null;
		}
	}

	private String formatValue(Object oldValue, Object newValue) throws AppException {

		try {
			String s1 = StringUtils.isValid(oldValue) ? oldValue.toString() : "0";
			String s2 = StringUtils.isValid(newValue) ? newValue.toString() : "0";
			return s1 + " => " + s2;

		} catch (Throwable t) {
			DBUtils.handleException(t, "formatValue.error");
			return null;
		}
	}

	private DTOKompatibilita kontrolaNovyStlpec(AuthInfo auth, DTOKompatibilita dtoF, List<DTOCiselnikStlpecGui> list, Map<Integer, DTOCiselnikStlpec> csMap) throws AppException {

		try {
			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> errorSet = new HashSet<String>();
			Set<String> warnSet = new HashSet<String>();

			for (DTOCiselnikStlpecGui dto : list) {

				DTOCiselnikStlpec dtoCS = csMap.get(dto.getIDCiselnikStlpec());

				Integer pocet = getDelegate().getDynCiselnikRead().count(auth, dtoF.getCiselnikTabulka(), dtoCS.getNazov(), false, true);
				if (StringUtils.isValid(pocet) && pocet.intValue() != 0) {
					String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
					warnSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_610, colName));
				}

				if ("T".equals(dto.getPovinny())) {
					pocet = getDelegate().getDynCiselnikRead().count(auth, dtoF.getCiselnikTabulka(), dtoCS.getNazov(), true, false);
					if (StringUtils.isValid(pocet) && pocet.intValue() != 0) {
						String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
						warnSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_611, colName));
					}
				}

				if ("T".equals(dtoF.getPublishActual())) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_603));
				}
			}

			if (!errorSet.isEmpty()) {
				resultDTO.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				resultDTO.setKompatibilita("F");
			}

			if (!warnSet.isEmpty()) {
				resultDTO.setWarnMsgList(warnSet.toArray(new String[warnSet.size()]));
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaNovyStlpec.error", auth);
			return null;
		}
	}

	private DTOKompatibilita kontrolaZmenenyStlpec(AuthInfo auth, DTOKompatibilita dtoF, List<DTOCiselnikStlpecGui[]> updateList, Map<Integer, DTOCiselnikStlpec> csMap) throws AppException {

		try {
			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> warnSet = new HashSet<String>();

			for (DTOCiselnikStlpecGui[] dto : updateList) {

				DTOCiselnikStlpec dtoCS = csMap.get(dto[0].getIDCiselnikStlpec());

				if (!_CudKontrolaUtils.equals(dto[0].getDlzka(), dto[1].getDlzka())) {
					if (dto[0].getDlzka().intValue() > dto[1].getDlzka().intValue()) {
						String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto[0].getNadpis() : dtoCS.getNazov();
						String value = formatValue(dto[0].getDlzka(), dto[1].getDlzka());
						warnSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_613, colName, value));
					}
				}

				if (!_CudKontrolaUtils.equals(dto[0].getDecimals(), dto[1].getDecimals())) {
					if (!StringUtils.isValid(dto[1].getDecimals()) || dto[0].getDecimals().intValue() > dto[1].getDecimals().intValue()) {
						String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto[0].getNadpis() : dtoCS.getNazov();
						String value = formatValue(dto[0].getDecimals(), dto[1].getDecimals());
						warnSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_613, colName, value));
					}
				}

				if (!_CudKontrolaUtils.equals(dto[0].getPovinny(), dto[1].getPovinny())) {
					String s1 = "T".equals(dto[0].getPovinny()) ? "Áno" : "Nie";
					String s2 = "T".equals(dto[1].getPovinny()) ? "Áno" : "Nie";
					if ("T".equals(dto[1].getPovinny())) {
						Integer pocet = getDelegate().getDynCiselnikRead().count(auth, dtoF.getCiselnikTabulka(), dtoCS.getNazov(), true, false);
						if (StringUtils.isValid(pocet) && pocet.intValue() != 0) {
							String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto[0].getNadpis() : dtoCS.getNazov();
							String value = formatValue(s1, s2);
							warnSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_614, colName, value));
						}
					}
				}
			}

			if (!warnSet.isEmpty()) {
				resultDTO.setWarnMsgList(warnSet.toArray(new String[warnSet.size()]));
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaZmenenyStlpec.error", auth);
			return null;
		}
	}

	private Integer referenciaCount(AuthInfo auth, Integer ciselnikID, String ciselnikStlpecNazov) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql1 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

				crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);

				crit.add(CudCiselnikGuiPeer.ID_CISELNIK, ciselnikID, MyCriteria2.NOT_EQUAL);
				crit.add(CudCiselnikGuiPeer.PLATNOST_DO, null);
				crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql1 = crit.getSQL();
			}

			String subSql2 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

				crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);

				crit.add(CudCiselnikStlpecPeer.ID_CISELNIK, ciselnikID, MyCriteria2.NOT_EQUAL);
				crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);
				crit.addConditional(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, ciselnikID);
				crit.addConditional(CudCiselnikStlpecPeer.FK1_FK_NAZOV, ciselnikStlpecNazov, false);

				subSql2 = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addAsColumn("pocet", "count(*)");

			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " IN (" + subSql1 + ")");

			String s = CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC + " IN (" + subSql2 + ")";
			Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, s, MyCriteria2.CUSTOM);
			Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, ciselnikID, MyCriteria2.EQUAL);
			Criterion c3 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV, ciselnikStlpecNazov, MyCriteria2.EQUAL);
			crit.add(c1.or(c2.and(c3)));

			c1 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
			c2 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
			c3 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
			Criterion c4 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
			crit.add(c1.or(c2).or(c3).or(c4));

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, "pocet").asIntegerObj();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "referenciaCount.error", auth);
			return null;
		}
	}

	private DTOKompatibilita kontrolaZrusenyStlpec(AuthInfo auth, DTOKompatibilita dtoF, List<DTOCiselnikStlpecGui> list, Map<Integer, DTOCiselnikStlpec> csMap) throws AppException {

		try {
			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> errorSet = new HashSet<String>();
			Set<String> warnSet = new HashSet<String>();

			for (DTOCiselnikStlpecGui dto : list) {

				DTOCiselnikStlpec dtoCS = csMap.get(dto.getIDCiselnikStlpec());

				Integer pocet = getDelegate().getDynCiselnikRead().count(auth, dtoF.getCiselnikTabulka(), dtoCS.getNazov(), false, true);
				if (StringUtils.isValid(pocet) && pocet.intValue() != 0) {
					String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
					warnSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_610, colName));
				}

				pocet = referenciaCount(auth, dtoCS.getIDCiselnik(), dtoCS.getNazov());
				if (StringUtils.isValid(pocet) && pocet.intValue() != 0) {
					String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
					warnSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_612, colName));
				}

				if ("T".equals(dtoF.getPublishActual())) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_603));
				}
			}

			if (!errorSet.isEmpty()) {
				resultDTO.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				resultDTO.setKompatibilita("F");
			}

			if (!warnSet.isEmpty()) {
				resultDTO.setWarnMsgList(warnSet.toArray(new String[warnSet.size()]));
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaZrusenyStlpec.error", auth);
			return null;
		}
	}

	public DTOKompatibilita kontrolaCiselnika(AuthInfo auth, DTOKompatibilita dtoF, Integer lastCiselnikGuiID, String stav) throws AppException {

		try {
			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> errorSet = new HashSet<String>();

			DTOCiselnik dtoCis = getDelegate().getCiselnikRead().readLight(auth, dtoF.getCiselnikID());
			if (!StringUtils.isValid(dtoCis) || !"T".equals(dtoCis.getAktivny())) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_609));
			}

			if (!StringUtils.isValid(dtoCis) || !_CudConsts.CISELNIK_TYP_TECHNICKY.equals(dtoCis.getTyp())) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_620));
			}

			DTOCiselnikGui dtoGui = getDelegate().getCiselnikGuiRead().read(auth, dtoF.getCiselnikGuiID());
			if (!StringUtils.isValid(dtoGui) || !stav.equals(dtoGui.getStav())) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_604));
			}

			if (!StringUtils.isValid(lastCiselnikGuiID) && "T".equals(dtoF.getPublishActual())) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_601));
			}

			if (!errorSet.isEmpty()) {
				resultDTO.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				resultDTO.setKompatibilita("F");
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaCiselnika.error", auth);
			return null;
		}
	}

	private DTOKompatibilita kontrolaZmena(AuthInfo auth, DTOKompatibilita dtoF) throws AppException {

		try {
			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> errorSet = new HashSet<String>();

			Date platnostOd = DateUtils.removeTime(new Date());
			String[] stavPole = new String[] { _CudConsts.ZMENA_STAV_VPO, _CudConsts.ZMENA_STAV_SCH, _CudConsts.ZMENA_STAV_PAU };
			Integer pocet = getDelegate().getZmenaRead().count(auth, dtoF.getCiselnikID(), platnostOd, stavPole);
			if (StringUtils.isValid(pocet) && pocet.intValue() != 0) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_602));
			}

			if (!errorSet.isEmpty()) {
				resultDTO.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				resultDTO.setKompatibilita("F");
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaCiselnika.error", auth);
			return null;
		}
	}

	private Map<Integer, DTOCiselnikStlpec> ciselnikStlpecMap(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
			dtoF.setIDCiselnik(ciselnikID);
			return getDelegate().getCiselnikStlpecRead().mapLight(auth, dtoF, null, null);

		} catch (Throwable t) {
			handleException(t, "ciselnikStlpecMap.error", auth);
			return null;
		}
	}

	private List<DTOCiselnikStlpec> userTabColsListLight(AuthInfo auth, String tabulka) throws AppException {

		try {
			DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
			dtoF.setCiselnikTabulka(tabulka);

			DTOCiselnikStlpec[] listDTO = getDelegate().getGuiRead().userTabColsListLight(auth, dtoF, false);
			if (StringUtils.isValid(listDTO)) {
				return new ArrayList<DTOCiselnikStlpec>(Arrays.asList(listDTO));
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "userTabColsListLight.error", auth);
			return null;
		}
	}

	private Map<Integer, DTOCiselnik> ciselnikMap(AuthInfo auth, List<DTOCiselnikStlpecGui> guiList, Map<Integer, DTOCiselnikStlpec> csMap) throws AppException {

		try {
			Set<Integer> ciselnikIDs = new HashSet<Integer>();
			for (DTOCiselnikStlpecGui dtoGuiCS : guiList) {
				if (StringUtils.isValid(dtoGuiCS.getFk2IDCiselnik())) {
					ciselnikIDs.add(dtoGuiCS.getFk2IDCiselnik());
				}
				DTOCiselnikStlpec dtoCS = csMap.get(dtoGuiCS.getIDCiselnikStlpec());
				if (StringUtils.isValid(dtoCS) && StringUtils.isValid(dtoCS.getFk1IDCiselnik())) {
					ciselnikIDs.add(dtoCS.getFk1IDCiselnik());
				}
			}
			return getDelegate().getCiselnikRead().mapLight(auth, ciselnikIDs.toArray(new Integer[ciselnikIDs.size()]));

		} catch (Throwable t) {
			handleException(t, "ciselnikMap.error", auth);
			return null;
		}
	}

	private boolean compareTech(DTOCiselnikStlpec dto1, DTOCiselnikStlpec dto2) throws AppException {

		try {
			if (!_CudKontrolaUtils.equals(dto1.getTyp(), dto2.getTyp())) {
				return false;
			} else if (!_CudKontrolaUtils.equals(dto1.getPoradie(), dto2.getPoradie())) {
				return false;
			} else if (!_CudKontrolaUtils.equals(dto1.getDlzka(), dto2.getDlzka())) {
				return false;
			} else if (!_CudKontrolaUtils.equals(dto1.getDbTyp(), dto2.getDbTyp())) {
				return false;
			} else if (!_CudKontrolaUtils.equals(dto1.getPovinny(), dto2.getPovinny())) {
				return false;
			} else if (!_CudKontrolaUtils.equals(dto1.getJedinecny(), dto2.getJedinecny())) {
				return false;
			}
			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "compareTech.error");
			return false;
		}
	}

	private boolean compareDB(DTOCiselnikStlpec dtoDB, DTOCiselnikStlpec dtoCS) throws AppException {

		try {
			String dbTyp = "T".equals(dtoCS.getJeDbString()) ? _CudConsts.DB_TYP_STRING : dtoCS.getDbTyp();

			if (!_CudKontrolaUtils.equals(dtoDB.getNazov(), dtoCS.getNazov())) {
				return false;
			} else if (_CudConsts.DB_TYP_INTEGER.equals(dtoDB.getDbTyp()) && !_CudKontrolaUtils.equals(dtoDB.getDlzka(), dtoCS.getDlzka()) && _CudKontrolaUtils.lessThen(dtoDB.getDlzka(), dtoCS.getDlzka())) {
				return false;
			} else if (_CudConsts.DB_TYP_STRING.equals(dtoDB.getDbTyp()) && _CudConsts.DB_TYP_STRING.equals(dbTyp) && dtoDB.getDlzka().intValue() > _CudConsts.MAX_LENGTH_STRING && dtoCS.getDlzka().intValue() > _CudConsts.MAX_LENGTH_STRING) {
				return true;
			} else if (!_CudConsts.DB_TYP_INTEGER.equals(dtoDB.getDbTyp()) && !_CudKontrolaUtils.equals(dtoDB.getDlzka(), dtoCS.getDlzka())) {
				return false;
			} else if (!_CudKontrolaUtils.equals(dtoDB.getDbTyp(), dbTyp)) {
				if (dtoDB.getDlzka().intValue() != 1 || dtoCS.getDlzka().intValue() != 1 || !_CudConsts.DB_TYP_BOOLEAN.equals(dtoDB.getDbTyp()) || !_CudConsts.DB_TYP_STRING.equals(dbTyp)) {
					return false;
				}
			} else if (!_CudKontrolaUtils.equals(dtoDB.getDecimals(), dtoCS.getDecimals())) {
				return false;
			}
			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "compareDB.error");
			return false;
		}
	}

	public DTOKompatibilita kontrolaCiselnikStlpec(AuthInfo auth, DTOKompatibilita dtoF, Map<Integer, DTOCiselnikStlpec> csMap, List<DTOCiselnikStlpec> userTabColsList) throws AppException {

		try {
			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> errorSet = new HashSet<String>();

			List<DTOCiselnikStlpec> csList = new ArrayList<DTOCiselnikStlpec>();
			for (DTOCiselnikStlpec dtoCS : csMap.values()) {
				if (dtoF.getCiselnikID().intValue() == dtoCS.getIDCiselnik().intValue()) {
					csList.add(dtoCS);
				}
			}

			if (csList.isEmpty()) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_619));
			}

			Map<String, Integer> pocetMap = new HashMap<String, Integer>();
			for (DTOCiselnikStlpec dtoCS : csList) {
				if (!StringUtils.isValid(pocetMap.get(dtoCS.getNazov()))) {
					pocetMap.put(dtoCS.getNazov(), 0);
				}
				pocetMap.put(dtoCS.getNazov(), pocetMap.get(dtoCS.getNazov()) + 1);
			}
			for (String nazov : pocetMap.keySet()) {
				if (pocetMap.get(nazov).intValue() > 1) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3041, nazov));
				}
			}

			for (String nazov : new String[] { _CudConsts.NAZOV_HIST_ID, _CudConsts.NAZOV_ID_ZMENA, _CudConsts.NAZOV_PLATNOST_OD, _CudConsts.NAZOV_PLATNOST_DO, _CudConsts.NAZOV_ZMAZ, _CudConsts.NAZOV_CAS_VYTVORENIA, _CudConsts.NAZOV_CAS_ZMENY }) {
				if (!pocetMap.keySet().contains(nazov)) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3042, nazov));
				}
			}

			for (DTOCiselnikStlpec dtoCS : csList) {
				if (_CudKontrolaUtils.jeAtributTechnicky(dtoCS)) {
					DTOCiselnikStlpec dto = getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dtoCS.getTyp()) ? _CudConsts.NAZOV_PK_KEY : dtoCS.getNazov());
					if (!compareTech(dto, dtoCS)) {
						errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3043, dtoCS.getNazov()));
					}
				}
			}

			if (StringUtils.isValid(userTabColsList)) {

				for (DTOCiselnikStlpec dtoCS : csList) {
					DTOCiselnikStlpec dtoDB = _CudLookupUtils.lookupDTOCiselnikStlpec(userTabColsList, dtoCS.getNazov());
					if (!StringUtils.isValid(dtoDB)) {
						errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3048, dtoCS.getNazov()));
						continue;
					}
					dtoDB.setNadpis(dtoCS.getNadpis());
					dtoDB.setTyp(dtoCS.getTyp());
					if (!compareDB(dtoDB, dtoCS)) {
						errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3045, dtoCS.getNazov()));
					}
				}

			} else {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3044));
			}

			pocetMap = new HashMap<String, Integer>();
			for (DTOCiselnikStlpec dtoCS : csList) {
				if (_CudConsts.CISELNIK_STLPEC_TYP_HK.equals(dtoCS.getTyp())) {
					if (!StringUtils.isValid(pocetMap.get(_CudConsts.CISELNIK_STLPEC_TYP_HK))) {
						pocetMap.put(_CudConsts.CISELNIK_STLPEC_TYP_HK, 0);
					}
					pocetMap.put(_CudConsts.CISELNIK_STLPEC_TYP_HK, pocetMap.get(_CudConsts.CISELNIK_STLPEC_TYP_HK) + 1);
				}
				if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dtoCS.getTyp())) {
					if (!StringUtils.isValid(pocetMap.get(_CudConsts.CISELNIK_STLPEC_TYP_PK))) {
						pocetMap.put(_CudConsts.CISELNIK_STLPEC_TYP_PK, 0);
					}
					pocetMap.put(_CudConsts.CISELNIK_STLPEC_TYP_PK, pocetMap.get(_CudConsts.CISELNIK_STLPEC_TYP_PK) + 1);
				}
				if ("T".equals(dtoCS.getJedinecny())) {
					if (!StringUtils.isValid(pocetMap.get("jedinecny"))) {
						pocetMap.put("jedinecny", 0);
					}
					pocetMap.put("jedinecny", pocetMap.get("jedinecny") + 1);
				}
			}
			if (!StringUtils.isValid(pocetMap.get(_CudConsts.CISELNIK_STLPEC_TYP_HK)) || pocetMap.get(_CudConsts.CISELNIK_STLPEC_TYP_HK).intValue() != 1) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3046));
			}
			if (!StringUtils.isValid(pocetMap.get(_CudConsts.CISELNIK_STLPEC_TYP_PK)) || pocetMap.get(_CudConsts.CISELNIK_STLPEC_TYP_PK).intValue() != 1) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3047));
			}
			if (StringUtils.isValid(pocetMap.get("jedinecny")) && pocetMap.get("jedinecny").intValue() > 1) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3060));
			}

			for (DTOCiselnikStlpec dtoCS : csList) {
				if (_CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getDbTyp()) && !StringUtils.isValid(dtoCS.getDecimals())) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3050, dtoCS.getNazov()));
				}
				if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getDbTyp()) && (!StringUtils.isValid(dtoCS.getDlzka())) && dtoCS.getDlzka().intValue() != 1) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3051, dtoCS.getNazov()));
				}
			}

			Map<Integer, Integer> poradieMap = new HashMap<Integer, Integer>();
			for (DTOCiselnikStlpec dtoCS : csList) {
				if (!StringUtils.isValid(poradieMap.get(dtoCS.getPoradie()))) {
					poradieMap.put(dtoCS.getPoradie(), 0);
				}
				poradieMap.put(dtoCS.getPoradie(), poradieMap.get(dtoCS.getPoradie()) + 1);
			}
			for (Integer pocet : poradieMap.values()) {
				if (pocet != 1) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3058));
					break;
				}
			}

			if (!errorSet.isEmpty()) {
				resultDTO.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				resultDTO.setKompatibilita("F");
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaCiselnikStlpec.error", auth);
			return null;
		}
	}

	public DTOKompatibilita kontrolaCiselnikStlpecGui(AuthInfo auth, DTOKompatibilita dtoF, List<DTOCiselnikStlpecGui> guiList, Map<Integer, DTOCiselnikStlpec> csMap) throws AppException {

		try {
			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> errorSet = new HashSet<String>();

			if (guiList.isEmpty()) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_619));
			}

			Map<String, Integer> pocetMap = new HashMap<String, Integer>();
			for (DTOCiselnikStlpecGui dtoCS : guiList) {
				if ("T".equals(dtoCS.getListZobrazenie())) {
					if (!StringUtils.isValid(pocetMap.get(dtoCS.getNadpis()))) {
						pocetMap.put(dtoCS.getNadpis(), 0);
					}
					pocetMap.put(dtoCS.getNadpis(), pocetMap.get(dtoCS.getNadpis()) + 1);
				}
			}
			for (String nadpis : pocetMap.keySet()) {
				if (pocetMap.get(nadpis).intValue() > 1) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3049, nadpis));
				}
			}

			pocetMap = new HashMap<String, Integer>();
			for (DTOCiselnikStlpecGui dtoCS : guiList) {
				if ("T".equals(dtoCS.getFormZobrazenie())) {
					if (!StringUtils.isValid(pocetMap.get(dtoCS.getNadpis()))) {
						pocetMap.put(dtoCS.getNadpis(), 0);
					}
					pocetMap.put(dtoCS.getNadpis(), pocetMap.get(dtoCS.getNadpis()) + 1);
				}
			}
			for (String nadpis : pocetMap.keySet()) {
				if (pocetMap.get(nadpis).intValue() > 1) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3049, nadpis));
				}
			}

			if (guiList.isEmpty()) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_619));
			}

			pocetMap = new HashMap<String, Integer>();
			for (DTOCiselnikStlpecGui dto : guiList) {

				DTOCiselnikStlpec dtoCS = csMap.get(dto.getIDCiselnikStlpec());
				if (!StringUtils.isValid(dtoCS)) {
					continue;
				}

				if (!_CudKontrolaUtils.equals(dto.getDlzka(), dtoCS.getDlzka())) {
					if (!StringUtils.isValid(dto.getDlzka()) || !StringUtils.isValid(dtoCS.getDlzka()) || dtoCS.getDlzka().intValue() < dto.getDlzka().intValue()) {
						String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
						String attrName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? "Dátová dĺžka" : _CudConsts.NAZOV_DLZKA;
						errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3053, colName, attrName));
					}
				}

				if (!_CudKontrolaUtils.equals(dto.getDecimals(), dtoCS.getDecimals())) {
					if (!StringUtils.isValid(dto.getDecimals()) || !StringUtils.isValid(dtoCS.getDecimals()) || dtoCS.getDecimals().intValue() < dto.getDecimals().intValue()) {
						String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
						String attrName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? "Počet desatinných miest" : _CudConsts.NAZOV_DECIMALS;
						errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3053, colName, attrName));
					}
				}

				if ("T".equals(dto.getListZobrazenie()) && !StringUtils.isValid(dto.getListSirka())) {
					String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
					String attrName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? "Šírka v zozname" : _CudConsts.NAZOV_LIST_SIRKA;
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3054, colName, attrName));
				}
				if ("T".equals(dto.getFormZobrazenie()) && !StringUtils.isValid(dto.getFormSirka())) {
					String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
					String attrName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? "Šírka vo formuláry" : _CudConsts.NAZOV_FORM_SIRKA;
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3054, colName, attrName));
				}
				if ("T".equals(dto.getPopupZobrazenie()) && !StringUtils.isValid(dto.getPopupSirka())) {
					String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
					String attrName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? "Šírka v popup v okne" : _CudConsts.NAZOV_POPUP_SIRKA;
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3054, colName, attrName));
				}

				if ("T".equals(dto.getListZobrazenie())) {
					if (!StringUtils.isValid(pocetMap.get("list"))) {
						pocetMap.put("list", 0);
					}
					if ("T".equals(dto.getListSirkaChange())) {
						pocetMap.put("list", pocetMap.get("list") + 1);
					}

				} else if ("T".equals(dto.getListSirkaChange())) {
					String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
					String attrName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? "Šírka variabilná" : _CudConsts.NAZOV_LIST_SIRKA_CHANGE;
					String attrName2 = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? "Zobr. v zozname" : _CudConsts.NAZOV_LIST_ZOBRAZENIE;
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3055, colName, attrName, attrName2));
				}

				if ("T".equals(dto.getPopupZobrazenie())) {
					if (!StringUtils.isValid(pocetMap.get("popup"))) {
						pocetMap.put("popup", 0);
					}
					if ("T".equals(dto.getPopupSirkaChange())) {
						pocetMap.put("popup", pocetMap.get("popup") + 1);
					}

				} else if ("T".equals(dto.getPopupSirkaChange())) {
					String colName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? dto.getNadpis() : dtoCS.getNazov();
					String attrName = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? "Šírka variabilná" : _CudConsts.NAZOV_POPUP_SIRKA_CHANGE;
					String attrName2 = _CudConsts.ZDROJ_FORM.equals(dtoF.getZdroj()) ? "Zobr. v popup okne" : _CudConsts.NAZOV_POPUP_ZOBRAZENIE;
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3055, colName, attrName, attrName2));
				}
			}

			if (pocetMap.keySet().contains("list") && pocetMap.get("list").intValue() == 0) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3056));
			}
			if (pocetMap.keySet().contains("popup") && pocetMap.get("popup").intValue() == 0) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3057));
			}

			Map<Integer, Integer> poradieMap = new HashMap<Integer, Integer>();
			for (DTOCiselnikStlpecGui dto : guiList) {
				if (!StringUtils.isValid(poradieMap.get(dto.getPoradie()))) {
					poradieMap.put(dto.getPoradie(), 0);
				}
				poradieMap.put(dto.getPoradie(), poradieMap.get(dto.getPoradie()) + 1);
			}
			for (Integer pocet : poradieMap.values()) {
				if (pocet != 1) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3058));
					break;
				}
			}

			if (!errorSet.isEmpty()) {
				resultDTO.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				resultDTO.setKompatibilita("F");
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaCiselnikStlpecGui.error", auth);
			return null;
		}
	}

	public DTOKompatibilita kontrolaWfDef(AuthInfo auth, DTOKompatibilita dtoF) throws AppException {

		try {
			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> errorSet = new HashSet<String>();

			List<DTOWfDef> listDTO = getDelegate().getWfDefRead().listLight(auth, dtoF.getCiselnikID());
			if (listDTO.isEmpty()) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_617));
			}

			DTOWfDef dtoIn = _CudLookupUtils.lookupDTOWfDef(listDTO, _CudConsts.WF_DEF_TYP_IN);
			DTOWfDef dtoSc = _CudLookupUtils.lookupDTOWfDef(listDTO, _CudConsts.WF_DEF_TYP_SC);
			DTOWfDef dtoOv = _CudLookupUtils.lookupDTOWfDef(listDTO, _CudConsts.WF_DEF_TYP_OV);

			if (!StringUtils.isValid(dtoIn) || !StringUtils.isValid(dtoSc) || !StringUtils.isValid(dtoOv)) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_618));
			} else {
				if (!StringUtils.isValid(dtoIn.getIDWfDefNasl()) || dtoIn.getIDWfDefNasl().intValue() != dtoSc.getWfDefID().intValue()) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_618));
				}
				if (!StringUtils.isValid(dtoSc.getIDWfDefNasl()) || dtoSc.getIDWfDefNasl().intValue() != dtoOv.getWfDefID().intValue()) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_618));
				}
			}

			if (!errorSet.isEmpty()) {
				resultDTO.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				resultDTO.setKompatibilita("F");
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaCiselnikStlpec.error", auth);
			return null;
		}
	}

	public DTOKompatibilita[] kontrola(AuthInfo auth, DTOKompatibilita dtoF) throws AppException {

		try {
			DTOCiselnikGui dtoLast = getDelegate().getCiselnikGuiRead().readLast(auth, dtoF.getCiselnikID(), _CudConsts.CISELNIK_GUI_STAV_PUB);
			if (!StringUtils.isValid(dtoLast)) {
				dtoLast = new DTOCiselnikGui();
			}

			Map<Integer, List<DTOCiselnikStlpecGui>> metaMap = getDelegate().getCiselnikStlpecGuiRead().mapLight(auth, dtoLast.getCiselnikGuiID(), dtoF.getCiselnikGuiID());

			List<DTOCiselnikStlpecGui> oldList = metaMap.get(dtoLast.getCiselnikGuiID());
			if (!StringUtils.isValid(oldList)) {
				oldList = new ArrayList<DTOCiselnikStlpecGui>();
			}

			List<DTOCiselnikStlpecGui> newList = metaMap.get(dtoF.getCiselnikGuiID());
			if (!StringUtils.isValid(newList)) {
				newList = new ArrayList<DTOCiselnikStlpecGui>();
			}

			Map<Integer, DTOCiselnikStlpec> csMap = ciselnikStlpecMap(auth, dtoF.getCiselnikID());

			Map<Integer, DTOCiselnik> ciselnikMap = ciselnikMap(auth, newList, csMap);

			List<DTOKompatibilita> resultList = new ArrayList<DTOKompatibilita>();

			DTOKompatibilita resultDTO = kontrolaCiselnika(auth, dtoF, dtoLast.getCiselnikGuiID(), _CudConsts.CISELNIK_GUI_STAV_DRAFT);
			resultDTO.setNadpis("Kontrola číselníka");
			resultList.add(resultDTO);
			if ("F".equals(resultDTO.getKompatibilita())) {
				return resultList.toArray(new DTOKompatibilita[resultList.size()]);
			}

			resultDTO = kontrolaReferencia(auth, newList, csMap, ciselnikMap, dtoF.getZdroj());
			resultDTO.setNadpis("Kontrola referencií");
			resultList.add(resultDTO);

			if ("F".equals(dtoF.getPublishActual())) {
				resultDTO = kontrolaZmena(auth, dtoF);
				resultDTO.setNadpis("Kontrola registra zmien");
				resultList.add(resultDTO);
			}

			resultDTO = kontrolaZrusenyStlpec(auth, dtoF, createZrusenyStlpecList(oldList, newList), csMap);
			resultDTO.setNadpis("Kontrola zrušených atribútov");
			resultList.add(resultDTO);

			resultDTO = kontrolaNovyStlpec(auth, dtoF, createNovyStlpecList(oldList, newList), csMap);
			resultDTO.setNadpis("Kontrola nových atribútov");
			resultList.add(resultDTO);

			resultDTO = kontrolaZmenenyStlpec(auth, dtoF, createZmenenyStlpecList(oldList, newList), csMap);
			resultDTO.setNadpis("Kontrola zmenených atribútov");
			resultList.add(resultDTO);

			List<DTOCiselnikStlpec> userTabColsList = userTabColsListLight(auth, dtoF.getCiselnikTabulka());
			resultDTO = kontrolaCiselnikStlpec(auth, dtoF, csMap, userTabColsList);
			resultDTO.setNadpis("Kontrola atribútov v zozname stĺpcov");
			resultList.add(resultDTO);

			resultDTO = kontrolaCiselnikStlpecGui(auth, dtoF, newList, csMap);
			resultDTO.setNadpis("Kontrola atribútov v definícií obrazovky");
			resultList.add(resultDTO);

			resultDTO = kontrolaWfDef(auth, dtoF);
			resultDTO.setNadpis("Kontrola definície procesov");
			resultList.add(resultDTO);

			return resultList.toArray(new DTOKompatibilita[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "kontrolaForGui.error", auth);
			return null;
		}
	}

}
