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

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.security.Rola;
import sk.ditec.common.utils.DateUtils;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudObjektCiselnikPeer;
import sk.ditec.dao.meta.CudObjektPeer;
import sk.ditec.dao.meta.CudOdberatelObjektPeer;
import sk.ditec.dao.meta.CudOdberatelPeer;

import com.workingdogs.village.Record;

public class CudObjektCiselnikReadClass extends _CudBaseClass {

	public DTOObjektCiselnik[] list(AuthInfo auth, Page page, DTOObjektCiselnik dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOObjektCiselnik();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, dtoF);

			crit.addSelectColumn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_OBJEKT);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);
			crit.addSelectColumn(CudObjektCiselnikPeer.VSETKY);
			crit.addSelectColumn(CudObjektCiselnikPeer.PLATNY);

			crit.addAlias("t1", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("cis_nazov", "t1.NAZOV");
			crit.addJoin(CudObjektCiselnikPeer.ID_CISELNIK, "t1.CISELNIK_ID", MyCriteria2.LEFT_JOIN);
			crit.addConditional("t1.NAZOV", dtoF.getCiselnikNazov(), true);

			crit.addAlias("t2", CudObjektPeer.TABLE_NAME);
			crit.addAsColumn("obj_nazov", "t2.NAZOV");
			crit.addAsColumn("obj_platny", "t2.PLATNY");
			crit.addJoin(CudObjektCiselnikPeer.ID_OBJEKT, "t2.OBJEKT_ID", MyCriteria2.LEFT_JOIN);
			crit.addConditional("t2.NAZOV", dtoF.getObjektNazov(), true);
			crit.addConditional("t2.PLATNY", dtoF.getObjektPlatny(), false);

			crit.addConditional(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, dtoF.getObjektCiselnikID());
			crit.addConditional(CudObjektCiselnikPeer.ID_OBJEKT, StringUtils.isValid(dtoF.getIDObjekt()) ? dtoF.getIDObjekt().toString() : null, true);
			crit.addConditional(CudObjektCiselnikPeer.ID_CISELNIK, StringUtils.isValid(dtoF.getIDCiselnik()) ? dtoF.getIDCiselnik().toString() : null, true);

			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOObjektCiselnik> listDTO = new ArrayList<DTOObjektCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjektCiselnik dto = new DTOObjektCiselnik();
				dto.setObjektCiselnikID(rVal(r, CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID).asIntegerObj());
				dto.setIDObjekt(rVal(r, CudObjektCiselnikPeer.ID_OBJEKT).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj());
				dto.setVsetky(rVal(r, CudObjektCiselnikPeer.VSETKY).asString());
				dto.setPlatny(rVal(r, CudObjektCiselnikPeer.PLATNY).asString());

				dto.setCiselnikNazov(rVal(r, "cis_nazov").asString());

				dto.setObjektNazov(rVal(r, "obj_nazov").asString());
				dto.setObjektPlatny(rVal(r, "obj_platny").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOObjektCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTOObjektCiselnik[] list(AuthInfo auth, DTOObjektCiselnik dtoF, DTOObjektCiselnik dto, DTOObjektCiselnik[] data) throws AppException {

		try {
			if (!StringUtils.isValid(dtoF)) {
				dtoF = new DTOObjektCiselnik();
			}

			if ("I".equals(dtoF.getOperacia())) {
				return insertList(auth, dto, data);

			} else if ("U".equals(dtoF.getOperacia())) {
				return updateList(auth, dtoF.getIDCiselnik(), dto, data);

			} else if ("D".equals(dtoF.getOperacia())) {
				return deleteList(auth, dtoF.getIDCiselnik(), data);

			} else if ("list".equals(dtoF.getOperacia())) {
				return list(auth, dtoF.getIDObjekt());

			} else if ("copy".equals(dtoF.getOperacia())) {
				return copyList(auth, data);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	private DTOObjektCiselnik[] insertList(AuthInfo auth, DTOObjektCiselnik dto, DTOObjektCiselnik[] dataList) throws AppException {

		try {
			List<DTOObjektCiselnik> listDTO = new ArrayList<DTOObjektCiselnik>(Arrays.asList(dataList));
			listDTO.add(dto);
			return listDTO.toArray(new DTOObjektCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "insertList.error", auth);
			return null;
		}
	}

	private DTOObjektCiselnik[] updateList(AuthInfo auth, Integer ciselnikID, DTOObjektCiselnik dto, DTOObjektCiselnik[] dataList) throws AppException {

		try {
			List<DTOObjektCiselnik> listDTO = new ArrayList<DTOObjektCiselnik>();

			for (DTOObjektCiselnik dtoItem : dataList) {
				if (dtoItem.getIDCiselnik().intValue() == ciselnikID.intValue()) {
					listDTO.add(dto);
				} else {
					listDTO.add(dtoItem);
				}
			}

			return listDTO.toArray(new DTOObjektCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "updateList.error", auth);
			return null;
		}
	}

	private DTOObjektCiselnik[] deleteList(AuthInfo auth, Integer ciselnikID, DTOObjektCiselnik[] dataList) throws AppException {

		try {
			List<DTOObjektCiselnik> listDTO = new ArrayList<DTOObjektCiselnik>();

			for (DTOObjektCiselnik dtoItem : dataList) {
				if (dtoItem.getIDCiselnik().intValue() != ciselnikID.intValue()) {
					listDTO.add(dtoItem);
				}
			}

			return listDTO.toArray(new DTOObjektCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "deleteList.error", auth);
			return null;
		}
	}

	private DTOObjektCiselnik[] copyList(AuthInfo auth, DTOObjektCiselnik[] dataList) throws AppException {

		try {
			Set<Integer> set = new HashSet<Integer>();
			for (DTOObjektCiselnik dto : dataList) {
				set.add(dto.getObjektCiselnikID());
			}
			Map<Integer, List<DTOObjektStlpec>> osMap = getDelegate().getObjektStlpecRead().map(auth, set.toArray(new Integer[set.size()]));

			for (DTOObjektCiselnik dto : dataList) {
				int pocet = -1;
				List<DTOObjektStlpec> osList = osMap.get(dto.getObjektCiselnikID());
				if (StringUtils.isValid(osList) && !osList.isEmpty()) {
					for (DTOObjektStlpec dtoOS : osList) {
						dtoOS.setObjektStlpecID(pocet--);
						dtoOS.setIDObjektCiselnik(null);
						dtoOS.setBolZmeneny("T");
					}
					dto.setObjektStlpecList(osList.toArray(new DTOObjektStlpec[osList.size()]));
				}

				dto.setObjektCiselnikID(null);
				dto.setIDObjekt(null);
				dto.setCasZmeny(null);
				dto.setIDUcet(null);
				dto.setBolZmeneny("T");
			}

			return dataList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "copyList.error");
			return null;
		}
	}

	public DTOObjektCiselnik[] list(AuthInfo auth, Integer objektID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(objektID)) {
				return new DTOObjektCiselnik[0];
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, new DTOObjektCiselnik());

			crit.addSelectColumn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_OBJEKT);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);
			crit.addSelectColumn(CudObjektCiselnikPeer.VSETKY);
			crit.addSelectColumn(CudObjektCiselnikPeer.PLATNY);

			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addJoin(CudObjektCiselnikPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudObjektCiselnikPeer.ID_OBJEKT, objektID);

			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOObjektCiselnik> listDTO = new ArrayList<DTOObjektCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjektCiselnik dto = new DTOObjektCiselnik();
				dto.setObjektCiselnikID(rVal(r, CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID).asIntegerObj());
				dto.setIDObjekt(rVal(r, CudObjektCiselnikPeer.ID_OBJEKT).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj());
				dto.setVsetky(rVal(r, CudObjektCiselnikPeer.VSETKY).asString());
				dto.setPlatny(rVal(r, CudObjektCiselnikPeer.PLATNY).asString());

				dto.setCiselnikNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());
				dto.setCiselnikTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOObjektCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	private Map<Integer, DTOObjektCiselnik> map(AuthInfo auth, Integer[] ids) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ids)) {
				return new HashMap<Integer, DTOObjektCiselnik>();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, new DTOObjektCiselnik());

			crit.addSelectColumn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_OBJEKT);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);
			crit.addSelectColumn(CudObjektCiselnikPeer.VSETKY);
			crit.addSelectColumn(CudObjektCiselnikPeer.PLATNY);
			crit.addSelectColumn(CudObjektCiselnikPeer.CAS_ZMENY);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_UCET);

			crit.addAlias("t1", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("cis_nazov", "t1.NAZOV");
			crit.addJoin(CudObjektCiselnikPeer.ID_CISELNIK, "t1.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			crit.addAlias("t2", CudObjektPeer.TABLE_NAME);
			crit.addAsColumn("obj_nazov", "t2.NAZOV");
			crit.addAsColumn("obj_platny", "t2.PLATNY");
			crit.addJoin(CudObjektCiselnikPeer.ID_OBJEKT, "t2.OBJEKT_ID", MyCriteria2.LEFT_JOIN);

			if (ids.length == 1) {
				crit.addConditional(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, ids[0]);
			} else {
				crit.addIn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, ids);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOObjektCiselnik> mapDTO = new HashMap<Integer, DTOObjektCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjektCiselnik dto = new DTOObjektCiselnik();
				dto.setObjektCiselnikID(rVal(r, CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID).asIntegerObj());
				dto.setIDObjekt(rVal(r, CudObjektCiselnikPeer.ID_OBJEKT).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj());
				dto.setVsetky(rVal(r, CudObjektCiselnikPeer.VSETKY).asString());
				dto.setPlatny(rVal(r, CudObjektCiselnikPeer.PLATNY).asString());
				dto.setCasZmeny(rVal(r, CudObjektCiselnikPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudObjektCiselnikPeer.ID_UCET).asIntegerObj());

				dto.setCiselnikNazov(rVal(r, "cis_nazov").asString());

				dto.setObjektNazov(rVal(r, "obj_nazov").asString());
				dto.setObjektPlatny(rVal(r, "obj_platny").asString());

				mapDTO.put(dto.getObjektCiselnikID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "map.error", auth);
			return null;
		}
	}

	public DTOObjektCiselnik loadData(AuthInfo auth, DTOObjektCiselnik dtoF) throws AppException {

		try {
			Map<Integer, DTOObjektCiselnik> mapa = map(auth, new Integer[] { dtoF.getObjektCiselnikID() });
			return mapa.get(dtoF.getObjektCiselnikID());

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public Set<Integer> ids(AuthInfo auth, Integer objektID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(objektID)) {
				return new HashSet<Integer>();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, new DTOObjektCiselnik());

			crit.addSelectColumn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID);

			crit.addConditional(CudObjektCiselnikPeer.ID_OBJEKT, objektID);

			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID).asIntegerObj());
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}

	public Set<Integer> ciselnikIds(AuthInfo auth, DTOOdberatelObjekt dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, new DTOOdberatelObjekt());

				crit.addSelectColumn(CudOdberatelObjektPeer.ID_OBJEKT);

				crit.addConditional(CudOdberatelObjektPeer.ID_ODBERATEL, dtoF.getIDOdberatel());
				crit.addConditional(CudOdberatelObjektPeer.TYP_PRISTUPU, dtoF.getTypPristupu(), false);
				crit.add(CudOdberatelObjektPeer.ID_OBJEKT, dtoF.getIDObjekt(), MyCriteria2.NOT_EQUAL);
				crit.add(CudOdberatelObjektPeer.PLATNOST_OD, dtoF.getPlatnostOd(), MyCriteria2.LESS_EQUAL);

				crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

				Criterion c1 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, dtoF.getPlatnostOd(), MyCriteria2.GREATER_EQUAL);
				Criterion c2 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
				crit.add(c1.or(c2));

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, new DTOObjektCiselnik());

			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);

			crit.addCustomSql(CudObjektCiselnikPeer.ID_OBJEKT, CudObjektCiselnikPeer.ID_OBJEKT + " IN ( " + subSql + " )");

			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj());
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "ciselnikIds.error", auth);
			return null;
		}
	}

	public Set<Integer> ciselnikIDsSet(AuthInfo auth, Integer[] objektIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(objektIDs)) {
				return new HashSet<Integer>();
			}

			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, new DTOCiselnik());

				crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);

				crit.addConditional(CudCiselnikPeer.AKTIVNY, "T", false);
				crit.addConditional(CudCiselnikPeer.TYP, _CudConsts.CISELNIK_TYP_TECHNICKY, false);

				crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();

			}

			MyCriteria2 crit = new MyCriteria2(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, new DTOObjektCiselnik());

			crit.setDistinct();
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);

			if (objektIDs.length == 1) {
				crit.addConditional(CudObjektCiselnikPeer.ID_OBJEKT, objektIDs[0]);
			} else {
				crit.addIn(CudObjektCiselnikPeer.ID_OBJEKT, objektIDs);
			}

			crit.addCustomSql(CudObjektCiselnikPeer.ID_CISELNIK, CudObjektCiselnikPeer.ID_CISELNIK + " IN ( " + subSql + " ) ");

			crit.addConditional(CudObjektCiselnikPeer.PLATNY, "T", false);

			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> resultSet = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				resultSet.add(rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj());
			}

			return resultSet;

		} catch (Throwable t) {
			handleException(t, "ciselnikIDsSet.error", auth);
			return null;
		}
	}

	public Map<Integer, Integer> pocetnostCiselnikyMap(AuthInfo auth, Integer[] objektIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(objektIDs)) {
				return new HashMap<Integer, Integer>();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, new DTOObjektCiselnik());

			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);

			if (objektIDs.length == 1) {
				crit.addConditional(CudObjektCiselnikPeer.ID_OBJEKT, objektIDs[0]);
			} else {
				crit.addIn(CudObjektCiselnikPeer.ID_OBJEKT, objektIDs);
			}

			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Integer ciselnikID = rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj();
				if (!StringUtils.isValid(resultMap.get(ciselnikID))) {
					resultMap.put(ciselnikID, 0);
				}
				resultMap.put(ciselnikID, resultMap.get(ciselnikID) + 1);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "pocetnostCiselnikyMap.error", auth);
			return null;
		}
	}

	public List<DTOObjektCiselnik> list(AuthInfo auth, Integer ciselnikID, Integer[] objektIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikID) || !StringUtils.isValid(objektIDs)) {
				return new ArrayList<DTOObjektCiselnik>();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, new DTOObjektCiselnik());

			crit.addSelectColumn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_OBJEKT);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);
			crit.addSelectColumn(CudObjektCiselnikPeer.VSETKY);

			crit.addJoin(CudObjektCiselnikPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudCiselnikPeer.AKTIVNY, "T", false);

			if (objektIDs.length == 1) {
				crit.addConditional(CudObjektCiselnikPeer.ID_OBJEKT, objektIDs[0]);
			} else {
				crit.addIn(CudObjektCiselnikPeer.ID_OBJEKT, objektIDs);
			}

			crit.addConditional(CudObjektCiselnikPeer.ID_CISELNIK, ciselnikID);

			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOObjektCiselnik> listDTO = new ArrayList<DTOObjektCiselnik>();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjektCiselnik dto = new DTOObjektCiselnik();
				dto.setObjektCiselnikID(rVal(r, CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj());
				dto.setVsetky(rVal(r, CudObjektCiselnikPeer.VSETKY).asString());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Map<Integer, List<DTOObjektCiselnik>> map(AuthInfo auth, Integer[] ciselnikIDs, Integer[] objektIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikIDs) || !StringUtils.isValid(objektIDs)) {
				return new HashMap<Integer, List<DTOObjektCiselnik>>();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, new DTOObjektCiselnik());

			crit.addSelectColumn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_OBJEKT);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);
			crit.addSelectColumn(CudObjektCiselnikPeer.VSETKY);

			crit.addJoin(CudObjektCiselnikPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudCiselnikPeer.AKTIVNY, "T", false);

			if (objektIDs.length == 1) {
				crit.addConditional(CudObjektCiselnikPeer.ID_OBJEKT, objektIDs[0]);
			} else {
				crit.addIn(CudObjektCiselnikPeer.ID_OBJEKT, objektIDs);
			}

			if (ciselnikIDs.length == 1) {
				crit.addConditional(CudObjektCiselnikPeer.ID_CISELNIK, ciselnikIDs[0]);
			} else {
				crit.addIn(CudObjektCiselnikPeer.ID_CISELNIK, ciselnikIDs);
			}

			crit.addConditional(CudObjektCiselnikPeer.PLATNY, "T", false);

			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOObjektCiselnik>> resultMap = new HashMap<Integer, List<DTOObjektCiselnik>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjektCiselnik dto = new DTOObjektCiselnik();
				dto.setObjektCiselnikID(rVal(r, CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj());
				dto.setVsetky(rVal(r, CudObjektCiselnikPeer.VSETKY).asString());

				if (!StringUtils.isValid(resultMap.get(dto.getIDCiselnik()))) {
					resultMap.put(dto.getIDCiselnik(), new ArrayList<DTOObjektCiselnik>());
				}
				resultMap.get(dto.getIDCiselnik()).add(dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "map.error", auth);
			return null;
		}
	}

	public List<DTOObjektCiselnik> vratPriradeneCiselnikyOdberatelovi2(AuthInfo auth, Integer[] ids, String typPristupu, String[] typCiselnikaList, Date kDatumu)
			throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			List<Rola> rolaList = FrameworkUtils.getAuthMod().rolaListByAccount(auth.getAccountName());
			Set<String> kodRolySet = new HashSet<String>();
			for (Rola dto : rolaList) {
				if (_CudConsts.ROLA_MODUL_KODs.contains(dto.getKodRoly())) {
					kodRolySet.add(dto.getKodRoly());
				}
			}
			String[] poleRola = kodRolySet.toArray(new String[kodRolySet.size()]);

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, new DTOOdberatelObjekt());
			crit.addSelectColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);

			// CUD_OBJEKT_CISELNIK
			crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, CudObjektCiselnikPeer.ID_OBJEKT, MyCriteria2.LEFT_JOIN);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);
			crit.addSelectColumn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID);
			crit.addSelectColumn(CudObjektCiselnikPeer.VSETKY);

			// CUD_CISELNIK
			crit.addJoin(CudObjektCiselnikPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);

			// CUD_OBJEKT
			crit.addJoin(CudObjektCiselnikPeer.ID_OBJEKT, CudObjektPeer.OBJEKT_ID, MyCriteria2.LEFT_JOIN);
			crit.addSelectColumn(CudObjektPeer.NAZOV);

			// CUD_ODBERATEL
			crit.addJoin(CudOdberatelObjektPeer.ID_ODBERATEL, CudOdberatelPeer.ODBERATEL_ID, MyCriteria2.LEFT_JOIN);

			crit.add(CudObjektCiselnikPeer.PLATNY, "T");
			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);
			crit.add(CudCiselnikPeer.AKTIVNY, "T");

			if (ids != null && ids.length > 0) {
				if (ids.length == 1) {
					crit.add(CudObjektCiselnikPeer.ID_CISELNIK, ids[0]);
				} else {
					crit.addIn(CudObjektCiselnikPeer.ID_CISELNIK, ids);
				}
			}

			if (typCiselnikaList != null && typCiselnikaList.length > 0) {
				if (typCiselnikaList.length == 1) {
					crit.add(CudCiselnikPeer.TYP, typCiselnikaList[0]);
				} else {
					crit.addIn(CudCiselnikPeer.TYP, typCiselnikaList);
				}
			}

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			Criterion cA = crit.getNewCriterion(CudOdberatelPeer.OBM_UCET_NAZOV, auth.getAccountName(), MyCriteria2.EQUAL);
			if (StringUtils.isValid(poleRola)) {
				Criterion cB = null;
				if (poleRola.length == 1) {
					cB = crit.getNewCriterion(CudOdberatelPeer.ROLA_KOD, poleRola[0], MyCriteria2.EQUAL);
				} else {
					cB = crit.getNewCriterion(CudOdberatelPeer.ROLA_KOD, poleRola, MyCriteria2.IN);
				}
				Criterion cAB = cA.or(cB);
				crit.add(cAB);
			} else {
				crit.add(cA);
			}

			crit.add(CudOdberatelPeer.AKTIVNY, "T");
			crit.add(CudOdberatelPeer.ID_TRANSAKCIA_ZRUSENE, null);

			if (kDatumu == null) {
				kDatumu = DateUtils.removeTime(new Date());
			}

			Criterion cE = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, kDatumu, MyCriteria2.LESS_EQUAL);
			Criterion cF = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, kDatumu, MyCriteria2.GREATER_EQUAL);
			Criterion cG = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			Criterion cEFG = cE.and(cF.or(cG));
			crit.add(cEFG);

			crit.addConditional(CudOdberatelObjektPeer.TYP_PRISTUPU, typPristupu, false);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOObjektCiselnik> listDTO = new ArrayList<DTOObjektCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjektCiselnik dto = new DTOObjektCiselnik();
				dto.setObjektCiselnikID(rVal(r, CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj());
				dto.setCiselnikTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());
				dto.setCiselnikNazov(rVal(r, CudObjektPeer.NAZOV).asString());
				dto.setVsetky(rVal(r, CudObjektCiselnikPeer.VSETKY).asString());

				listDTO.add(dto);
			}

			return listDTO;
		} catch (Throwable t) {
			handleException(t, "vratPriradeneCiselnikyOdberatelovi2.error", auth);
			return null;
		}
	}

	public List<DTOObjektCiselnik> vratCiselnikyKObjektu(AuthInfo auth, Integer idObjekt, String[] typCiselnikaList) throws AppException {
		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, new DTOOdberatelObjekt());

			// CUD_OBJEKT_CISELNIK
			crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, CudObjektCiselnikPeer.ID_OBJEKT, MyCriteria2.LEFT_JOIN);
			crit.addSelectColumn(CudObjektCiselnikPeer.ID_CISELNIK);
			crit.addSelectColumn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID);
			crit.addSelectColumn(CudObjektCiselnikPeer.VSETKY);

			// CUD_CISELNIK
			crit.addJoin(CudObjektCiselnikPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);

			// CUD_OBJEKT
			crit.addJoin(CudObjektCiselnikPeer.ID_OBJEKT, CudObjektPeer.OBJEKT_ID, MyCriteria2.LEFT_JOIN);
			crit.addSelectColumn(CudObjektPeer.NAZOV);

			crit.add(CudObjektCiselnikPeer.ID_OBJEKT, idObjekt);
			crit.add(CudObjektCiselnikPeer.PLATNY, "T");
			crit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);
			crit.add(CudObjektPeer.PLATNY, "T");
			crit.add(CudObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			if (typCiselnikaList != null && typCiselnikaList.length > 0) {
				if (typCiselnikaList.length == 1) {
					crit.add(CudCiselnikPeer.TYP, typCiselnikaList[0]);
				} else {
					crit.addIn(CudCiselnikPeer.TYP, typCiselnikaList);
				}
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOObjektCiselnik> listDTO = new ArrayList<DTOObjektCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjektCiselnik dto = new DTOObjektCiselnik();
				dto.setObjektCiselnikID(rVal(r, CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudObjektCiselnikPeer.ID_CISELNIK).asIntegerObj());
				dto.setCiselnikTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());
				dto.setCiselnikNazov(rVal(r, CudObjektPeer.NAZOV).asString());
				dto.setVsetky(rVal(r, CudObjektCiselnikPeer.VSETKY).asString());

				listDTO.add(dto);
			}

			return listDTO;
		} catch (Throwable t) {
			handleException(t, "vratCiselnikyKObjektu.error", auth);
			return null;
		}
	}

}
