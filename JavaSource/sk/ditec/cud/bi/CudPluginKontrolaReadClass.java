package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.DateUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOPluginKontrola;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudPluginKontrolaPeer;

import com.workingdogs.village.Record;

public class CudPluginKontrolaReadClass extends _CudBaseClass {

	private Date addDate(Date d, Integer minutePosun) throws AppException {

		try {
			if (!StringUtils.isValid(d)) {
				return d;
			}

			Calendar cal = Calendar.getInstance(new Locale("sk_SK"));
			cal.setTime(d);

			if (StringUtils.isValid(minutePosun)) {
				cal.add(Calendar.MINUTE, minutePosun);
			}

			return cal.getTime();

		} catch (Throwable t) {
			DBUtils.handleException(t, "addDate.error");
			return null;
		}
	}

	public DTOPluginKontrola[] listLight(AuthInfo auth, Page page, DTOPluginKontrola dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOPluginKontrola();
			}

			MyCriteria2 crit = new MyCriteria2(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID, dtoF);

			crit.addSelectColumn(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID);
			crit.addSelectColumn(CudPluginKontrolaPeer.ID_CISELNIK);
			crit.addSelectColumn(CudPluginKontrolaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudPluginKontrolaPeer.CAS_KONTROLA_ZAC);
			crit.addSelectColumn(CudPluginKontrolaPeer.CAS_KONTROLA_KON);
			crit.addSelectColumn(CudPluginKontrolaPeer.STAV);

			String colSort = "CASE";
			colSort += " WHEN stav = \'" + _CudConsts.PLUGIN_KONTROLA_STAV_INSERT + "\' AND cas_kontrola_zac IS NULL AND cas_kontrola_kon IS NULL THEN \'vlozenydodb\'";
			colSort += " WHEN stav = \'" + _CudConsts.PLUGIN_KONTROLA_STAV_CONTROL + "\' AND cas_kontrola_zac IS NOT NULL AND cas_kontrola_kon IS NULL THEN \'kontrolaprebieha\'";
			colSort += " WHEN stav = \'" + _CudConsts.PLUGIN_KONTROLA_STAV_SUCCESS + "\' AND cas_kontrola_kon IS NOT NULL THEN \'kontrolaukoncenauspesne\'";
			colSort += " WHEN stav = \'" + _CudConsts.PLUGIN_KONTROLA_STAV_ERROR + "\' THEN \'kontrolaukoncenaschybou\'";
			colSort += " END";
			crit.addAsColumn("stav_sort", colSort);

			crit.addConditional(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID, dtoF.getPluginKontrolaID());
			crit.addConditional(CudPluginKontrolaPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudPluginKontrolaPeer.STAV, dtoF.getStav(), false);
			crit.addConditional(CudPluginKontrolaPeer.PLATNOST_OD, dtoF.getPlatnostOdOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudPluginKontrolaPeer.PLATNOST_OD, dtoF.getPlatnostOdDo(), MyCriteria2.LESS_EQUAL);

			Criterion critOd = null;
			if ("F".equals(dtoF.getCasKontrolaZacOdTimeValid())) {
				String col1 = "trunc(" + CudPluginKontrolaPeer.CAS_KONTROLA_ZAC + ")";
				String col2 = "trunc(to_date(\'" + DateUtils.parseDate(dtoF.getCasKontrolaZacOd(), "dd-MM-yyyy") + "\', \'DD-MM-YYYY\'))";
				critOd = crit.getNewCriterion(CudPluginKontrolaPeer.CAS_KONTROLA_ZAC, col1 + " >= " + col2, MyCriteria2.CUSTOM);

			} else if (StringUtils.isValid(dtoF.getCasKontrolaZacOd())) {
				critOd = crit.getNewCriterion(CudPluginKontrolaPeer.CAS_KONTROLA_ZAC, dtoF.getCasKontrolaZacOd(), MyCriteria2.GREATER_EQUAL);
			}

			Criterion critDo = null;
			if ("F".equals(dtoF.getCasKontrolaZacDoTimeValid())) {
				String col1 = "trunc(" + CudPluginKontrolaPeer.CAS_KONTROLA_ZAC + ")";
				String col2 = "trunc(to_date(\'" + DateUtils.parseDate(dtoF.getCasKontrolaZacDo(), "dd-MM-yyyy") + "\', \'DD-MM-YYYY\'))";
				critDo = crit.getNewCriterion(CudPluginKontrolaPeer.CAS_KONTROLA_ZAC, col1 + " <= " + col2, MyCriteria2.CUSTOM);

			} else if (StringUtils.isValid(dtoF.getCasKontrolaZacDo())) {
				critDo = crit.getNewCriterion(CudPluginKontrolaPeer.CAS_KONTROLA_ZAC, addDate(dtoF.getCasKontrolaZacDo(), 1), MyCriteria2.LESS_THAN);
			}

			if (StringUtils.isValid(critOd) && StringUtils.isValid(critDo)) {
				crit.add(critOd.and(critDo));
			} else if (StringUtils.isValid(critOd)) {
				crit.add(critOd);
			} else if (StringUtils.isValid(critDo)) {
				crit.add(critDo);
			}

			critOd = null;
			if ("F".equals(dtoF.getCasKontrolaKonOdTimeValid())) {
				String col1 = "trunc(" + CudPluginKontrolaPeer.CAS_KONTROLA_KON + ")";
				String col2 = "trunc(to_date(\'" + DateUtils.parseDate(dtoF.getCasKontrolaKonOd(), "dd-MM-yyyy") + "\', \'DD-MM-YYYY\'))";
				critOd = crit.getNewCriterion(CudPluginKontrolaPeer.CAS_KONTROLA_KON, col1 + " >= " + col2, MyCriteria2.CUSTOM);

			} else if (StringUtils.isValid(dtoF.getCasKontrolaKonOd())) {
				critOd = crit.getNewCriterion(CudPluginKontrolaPeer.CAS_KONTROLA_KON, dtoF.getCasKontrolaKonOd(), MyCriteria2.GREATER_EQUAL);
			}

			critDo = null;
			if ("F".equals(dtoF.getCasKontrolaKonDoTimeValid())) {
				String col1 = "trunc(" + CudPluginKontrolaPeer.CAS_KONTROLA_KON + ")";
				String col2 = "trunc(to_date(\'" + DateUtils.parseDate(dtoF.getCasKontrolaKonDo(), "dd-MM-yyyy") + "\', \'DD-MM-YYYY\'))";
				critDo = crit.getNewCriterion(CudPluginKontrolaPeer.CAS_KONTROLA_KON, col1 + " <= " + col2, MyCriteria2.CUSTOM);

			} else if (StringUtils.isValid(dtoF.getCasKontrolaKonDo())) {
				critDo = crit.getNewCriterion(CudPluginKontrolaPeer.CAS_KONTROLA_KON, addDate(dtoF.getCasKontrolaKonDo(), 1), MyCriteria2.LESS_THAN);
			}

			if (StringUtils.isValid(critOd) && StringUtils.isValid(critDo)) {
				crit.add(critOd.and(critDo));
			} else if (StringUtils.isValid(critOd)) {
				crit.add(critOd);
			} else if (StringUtils.isValid(critDo)) {
				crit.add(critDo);
			}

			crit.add(CudPluginKontrolaPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOPluginKontrola> listDTO = new ArrayList<DTOPluginKontrola>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPluginKontrola dto = new DTOPluginKontrola();
				dto.setPluginKontrolaID(rVal(r, CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudPluginKontrolaPeer.ID_CISELNIK).asIntegerObj());
				dto.setPlatnostOd(rVal(r, CudPluginKontrolaPeer.PLATNOST_OD).asUtilDate());
				dto.setCasKontrolaZac(rVal(r, CudPluginKontrolaPeer.CAS_KONTROLA_ZAC).asUtilDate());
				dto.setCasKontrolaKon(rVal(r, CudPluginKontrolaPeer.CAS_KONTROLA_KON).asUtilDate());
				dto.setStav(rVal(r, CudPluginKontrolaPeer.STAV).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPluginKontrola[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOPluginKontrola readFirst(AuthInfo auth) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID, new DTOPluginKontrola());

			crit.addSelectColumn(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID);
			crit.addSelectColumn(CudPluginKontrolaPeer.ID_CISELNIK);
			crit.addSelectColumn(CudPluginKontrolaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudPluginKontrolaPeer.CAS_KONTROLA_ZAC);
			crit.addSelectColumn(CudPluginKontrolaPeer.CAS_KONTROLA_KON);
			crit.addSelectColumn(CudPluginKontrolaPeer.STAV);

			crit.addIn(CudPluginKontrolaPeer.STAV, new String[] { _CudConsts.PLUGIN_KONTROLA_STAV_INSERT, _CudConsts.PLUGIN_KONTROLA_STAV_CONTROL });

			crit.add(CudPluginKontrolaPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID);

			String sql = "SELECT * FROM (" + crit.getSQL() + ") WHERE rownum = 1";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			DTOPluginKontrola resultDTO = null;

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				resultDTO = new DTOPluginKontrola();
				resultDTO.setPluginKontrolaID(rVal(r, CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID).asIntegerObj());
				resultDTO.setIDCiselnik(rVal(r, CudPluginKontrolaPeer.ID_CISELNIK).asIntegerObj());
				resultDTO.setPlatnostOd(rVal(r, CudPluginKontrolaPeer.PLATNOST_OD).asUtilDate());
				resultDTO.setCasKontrolaZac(rVal(r, CudPluginKontrolaPeer.CAS_KONTROLA_ZAC).asUtilDate());
				resultDTO.setCasKontrolaKon(rVal(r, CudPluginKontrolaPeer.CAS_KONTROLA_KON).asUtilDate());
				resultDTO.setStav(rVal(r, CudPluginKontrolaPeer.STAV).asString());
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "readFirst.error", auth);
			return null;
		}
	}

	public Integer[] idsForDelete(AuthInfo auth) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID, new DTOPluginKontrola());

			crit.addSelectColumn(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID);

			crit.add(CudPluginKontrolaPeer.ID_TRANSAKCIA_ZRUSENE, (Object) null, MyCriteria2.ISNOTNULL);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID).asIntegerObj());
			}

			return set.toArray(new Integer[set.size()]);

		} catch (Throwable t) {
			handleException(t, "idsForDelete.error", auth);
			return null;
		}
	}

	public String kontrola(AuthInfo auth, DTOPluginKontrola dto) throws AppException {

		try {
			if (!StringUtils.isValid(dto) || !StringUtils.isValid(dto.getIDCiselnik())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "IDCiselnik");
			}

			if (!getDelegate().getIam().jeUcetZoSkupiny(auth, dto.getIDCiselnik(), _CudConsts.WF_DEF_TYP_SC)) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_127);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "kontrola.error", auth);
			return null;
		}
	}

}
