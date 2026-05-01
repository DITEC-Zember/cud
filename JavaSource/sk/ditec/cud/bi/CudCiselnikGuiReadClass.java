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
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOCiselnikGuiLD;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikGuiPeer;
import sk.ditec.dao.meta.CudCiselnikPeer;

import com.workingdogs.village.Record;

public class CudCiselnikGuiReadClass extends _CudBaseClass {

	public Integer count(AuthInfo auth, Integer ciselnikGuiID, Integer ciselnikID, String stav) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudCiselnikGuiPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudCiselnikGuiPeer.STAV, stav, false);

			if (StringUtils.isValid(ciselnikGuiID)) {
				crit.add(CudCiselnikGuiPeer.CISELNIK_GUI_ID, ciselnikGuiID, MyCriteria2.NOT_EQUAL);
			}

			crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, "pocet").asIntegerObj();
			}

			return 0;

		} catch (Throwable t) {
			handleException(t, "count.error", auth);
			return null;
		}
	}

	public DTOCiselnikGui read(AuthInfo auth, Integer ciselnikGuiID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikGuiID)) {
				return null;
			}

			DTOCiselnikGui dtoF = new DTOCiselnikGui();
			dtoF.setCiselnikGuiID(ciselnikGuiID);
			List<DTOCiselnikGui> listDTO = listLight(auth, dtoF);

			return listDTO.isEmpty() ? null : listDTO.get(0);

		} catch (Throwable t) {
			handleException(t, "read.error", auth);
			return null;
		}
	}

	public List<DTOCiselnikGui> listLight(AuthInfo auth, DTOCiselnikGui dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnikGui();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, dtoF);

			crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);
			crit.addSelectColumn(CudCiselnikGuiPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikGuiPeer.STAV);
			crit.addSelectColumn(CudCiselnikGuiPeer.PLATNOST_OD);
			crit.addSelectColumn(CudCiselnikGuiPeer.PLATNOST_DO);
			crit.addSelectColumn(CudCiselnikGuiPeer.CAS_PUBLIKOVANIA);
			crit.addSelectColumn(CudCiselnikGuiPeer.POPIS);

			crit.addConditional(CudCiselnikGuiPeer.CISELNIK_GUI_ID, dtoF.getCiselnikGuiID());
			crit.addConditional(CudCiselnikGuiPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudCiselnikGuiPeer.STAV, dtoF.getStav(), false);
			crit.addConditional(CudCiselnikGuiPeer.PLATNOST_OD, dtoF.getPlatnostOdOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudCiselnikGuiPeer.PLATNOST_OD, dtoF.getPlatnostOdDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudCiselnikGuiPeer.PLATNOST_DO, dtoF.getPlatnostDoOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudCiselnikGuiPeer.PLATNOST_DO, dtoF.getPlatnostDoDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudCiselnikGuiPeer.CAS_PUBLIKOVANIA, dtoF.getCasPublikovaniaOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudCiselnikGuiPeer.CAS_PUBLIKOVANIA, dtoF.getCasPublikovaniaDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudCiselnikGuiPeer.POPIS, dtoF.getPopis(), false);

			crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikGui> listDTO = new ArrayList<DTOCiselnikGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikGui dto = new DTOCiselnikGui();
				dto.setCiselnikGuiID(rVal(r, CudCiselnikGuiPeer.CISELNIK_GUI_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikGuiPeer.ID_CISELNIK).asIntegerObj());
				dto.setStav(rVal(r, CudCiselnikGuiPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudCiselnikGuiPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudCiselnikGuiPeer.PLATNOST_DO).asUtilDate());
				dto.setCasPublikovania(rVal(r, CudCiselnikGuiPeer.CAS_PUBLIKOVANIA).asUtilDate());
				dto.setPopis(rVal(r, CudCiselnikGuiPeer.POPIS).asString());

				dto.setListSize(lp.size());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOCiselnikGui[] list(AuthInfo auth, Page page, DTOCiselnikGui dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnikGui();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, dtoF);

			crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);
			crit.addSelectColumn(CudCiselnikGuiPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikGuiPeer.STAV);
			crit.addSelectColumn(CudCiselnikGuiPeer.PLATNOST_OD);
			crit.addSelectColumn(CudCiselnikGuiPeer.PLATNOST_DO);
			crit.addSelectColumn(CudCiselnikGuiPeer.CAS_PUBLIKOVANIA);
			crit.addSelectColumn(CudCiselnikGuiPeer.POPIS);

			// join Cud_CISELNIK
			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addJoin(CudCiselnikGuiPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudCiselnikGuiPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudCiselnikGuiPeer.STAV, dtoF.getStav(), false);
			crit.addConditional(CudCiselnikPeer.AKTIVNY, "T", false);

			crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudCiselnikGuiPeer.CISELNIK_GUI_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOCiselnikGui> listDTO = new ArrayList<DTOCiselnikGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikGui dto = new DTOCiselnikGui();
				dto.setCiselnikGuiID(rVal(r, CudCiselnikGuiPeer.CISELNIK_GUI_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikGuiPeer.ID_CISELNIK).asIntegerObj());
				dto.setStav(rVal(r, CudCiselnikGuiPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudCiselnikGuiPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudCiselnikGuiPeer.PLATNOST_DO).asUtilDate());
				dto.setCasPublikovania(rVal(r, CudCiselnikGuiPeer.CAS_PUBLIKOVANIA).asUtilDate());
				dto.setPopis(rVal(r, CudCiselnikGuiPeer.POPIS).asString());

				dto.setCiselnikNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikGui[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "ciselnikGuiList.error", auth);
			return null;
		}
	}

	private Map<Integer, DTOCiselnikGui> mapLight(AuthInfo auth, Integer[] ciselnikGuiIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikGuiIDs)) {
				return new HashMap<Integer, DTOCiselnikGui>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

			crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);
			crit.addSelectColumn(CudCiselnikGuiPeer.CAS_ZMENY);
			crit.addSelectColumn(CudCiselnikGuiPeer.ID_UCET);

			if (ciselnikGuiIDs.length == 1) {
				crit.addConditional(CudCiselnikGuiPeer.CISELNIK_GUI_ID, ciselnikGuiIDs[0]);
			} else {
				crit.addIn(CudCiselnikGuiPeer.CISELNIK_GUI_ID, ciselnikGuiIDs);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOCiselnikGui> mapDTO = new HashMap<Integer, DTOCiselnikGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikGui dto = new DTOCiselnikGui();
				dto.setCiselnikGuiID(rVal(r, CudCiselnikGuiPeer.CISELNIK_GUI_ID).asIntegerObj());
				dto.setCasZmeny(rVal(r, CudCiselnikGuiPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudCiselnikGuiPeer.ID_UCET).asIntegerObj());

				mapDTO.put(dto.getCiselnikGuiID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public DTOCiselnikGuiLD loadData(AuthInfo auth, DTOCiselnikGuiLD dtoF) throws AppException {

		try {
			Integer[] ids = (StringUtils.isValid(dtoF.getIDCiselnik())) ? new Integer[] { dtoF.getIDCiselnik() } : null;
			Map<Integer, DTOCiselnik> ciselnikMap = getDelegate().getCiselnikRead().mapLight(auth, ids);

			ids = (StringUtils.isValid(dtoF.getCiselnikGuiID())) ? new Integer[] { dtoF.getCiselnikGuiID() } : null;
			Map<Integer, DTOCiselnikGui> ciselnikGuiMap = mapLight(auth, ids);

			DTOCiselnikGuiLD resultDTO = new DTOCiselnikGuiLD();
			resultDTO.setCiselnikDTO(ciselnikMap.get(dtoF.getIDCiselnik()));
			resultDTO.setCiselnikGuiDTO(ciselnikGuiMap.get(dtoF.getCiselnikGuiID()));

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public String updateKontrola(AuthInfo auth, DTOCiselnikGui dto) throws AppException {

		try {
			Integer pocet = count(auth, dto.getCiselnikGuiID(), dto.getIDCiselnik(), _CudConsts.CISELNIK_GUI_STAV_DRAFT);
			if (StringUtils.isValid(pocet) && pocet.intValue() != 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_600);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	public DTOCiselnikGui readLast(AuthInfo auth, Integer ciselnikID, String stav) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

			crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);
			crit.addSelectColumn(CudCiselnikGuiPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikGuiPeer.STAV);
			crit.addSelectColumn(CudCiselnikGuiPeer.PLATNOST_OD);
			crit.addSelectColumn(CudCiselnikGuiPeer.PLATNOST_DO);
			crit.addSelectColumn(CudCiselnikGuiPeer.CAS_PUBLIKOVANIA);

			crit.addConditional(CudCiselnikGuiPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudCiselnikGuiPeer.STAV, stav, false);

			crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addDescendingOrderByColumn(CudCiselnikGuiPeer.CAS_PUBLIKOVANIA);

			String sql = "SELECT * FROM ( " + crit.getSQL() + ") WHERE rownum = 1";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			DTOCiselnikGui resultDTO = null;

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				resultDTO = new DTOCiselnikGui();
				resultDTO.setCiselnikGuiID(rVal(r, CudCiselnikGuiPeer.CISELNIK_GUI_ID).asIntegerObj());
				resultDTO.setIDCiselnik(rVal(r, CudCiselnikGuiPeer.ID_CISELNIK).asIntegerObj());
				resultDTO.setStav(rVal(r, CudCiselnikGuiPeer.STAV).asString());
				resultDTO.setPlatnostOd(rVal(r, CudCiselnikGuiPeer.PLATNOST_OD).asUtilDate());
				resultDTO.setPlatnostDo(rVal(r, CudCiselnikGuiPeer.PLATNOST_DO).asUtilDate());
				resultDTO.setCasPublikovania(rVal(r, CudCiselnikGuiPeer.CAS_PUBLIKOVANIA).asUtilDate());
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "readLast.error", auth);
			return null;
		}
	}

	public DTOCiselnikGui[] listLight(AuthInfo auth, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

			crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);
			crit.addSelectColumn(CudCiselnikGuiPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikGuiPeer.STAV);
			crit.addSelectColumn(CudCiselnikGuiPeer.PLATNOST_OD);
			crit.addSelectColumn(CudCiselnikGuiPeer.PLATNOST_DO);
			crit.addSelectColumn(CudCiselnikGuiPeer.CAS_PUBLIKOVANIA);
			crit.addSelectColumn(CudCiselnikGuiPeer.POPIS);

			crit.addConditional(CudCiselnikGuiPeer.PLATNOST_OD, platnostOd, MyCriteria2.LESS_EQUAL);

			Criterion c1 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, platnostOd, MyCriteria2.GREATER_EQUAL);
			Criterion c2 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			crit.add(c1.or(c2));

			crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikGui> listDTO = new ArrayList<DTOCiselnikGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikGui dto = new DTOCiselnikGui();
				dto.setCiselnikGuiID(rVal(r, CudCiselnikGuiPeer.CISELNIK_GUI_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikGuiPeer.ID_CISELNIK).asIntegerObj());
				dto.setStav(rVal(r, CudCiselnikGuiPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudCiselnikGuiPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudCiselnikGuiPeer.PLATNOST_DO).asUtilDate());
				dto.setCasPublikovania(rVal(r, CudCiselnikGuiPeer.CAS_PUBLIKOVANIA).asUtilDate());
				dto.setPopis(rVal(r, CudCiselnikGuiPeer.POPIS).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikGui[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public Set<Integer> ciselnikGuiIDs(AuthInfo auth, Integer ciselnikID, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

			crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);

			crit.addConditional(CudCiselnikGuiPeer.ID_CISELNIK, ciselnikID);

			crit.addConditional(CudCiselnikGuiPeer.STAV, _CudConsts.CISELNIK_GUI_STAV_PUB, false);

			if (StringUtils.isValid(platnostOd)) {
				crit.addConditional(CudCiselnikGuiPeer.PLATNOST_OD, platnostOd, MyCriteria2.LESS_EQUAL);

				Criterion c1 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, platnostOd, MyCriteria2.GREATER_EQUAL);
				Criterion c2 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
				crit.add(c1.or(c2));

			} else {
				String s = CudCiselnikGuiPeer.PLATNOST_OD + " <= " + CudCiselnikGuiPeer.PLATNOST_DO;
				Criterion c1 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_OD, s, MyCriteria2.CUSTOM);
				Criterion c2 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
				crit.add(c1.or(c2));
			}

			crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Set<Integer> resultSet = new HashSet<Integer>();

			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				resultSet.add(rVal(r, CudCiselnikGuiPeer.CISELNIK_GUI_ID).asIntegerObj());
			}

			return resultSet;

		} catch (Throwable t) {
			handleException(t, "ciselnikGuiIDs.error", auth);
			return null;
		}
	}

}
