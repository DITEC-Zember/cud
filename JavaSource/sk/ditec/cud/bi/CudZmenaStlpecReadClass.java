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
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.dto.DTOZmenaStlpecLD;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudWfDefPeer;
import sk.ditec.dao.meta.CudWfTodoPeer;
import sk.ditec.dao.meta.CudZmenaPeer;
import sk.ditec.dao.meta.CudZmenaStlpecPeer;

import com.workingdogs.village.Record;

public class CudZmenaStlpecReadClass extends _CudBaseClass {

	public List<DTOZmenaStlpec> listLight(AuthInfo auth, Integer ciselnikID, Integer zmenaID) throws AppException {

		try {
			DTOZmenaStlpec dtoF = new DTOZmenaStlpec();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setIDZmena(zmenaID);
			return listLight(auth, dtoF);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	private List<DTOZmenaStlpec> listLight(AuthInfo auth, DTOZmenaStlpec dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOZmenaStlpec();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, dtoF);

			crit.addSelectColumn(CudZmenaStlpecPeer.ZMENA_STLPEC_ID);
			crit.addSelectColumn(CudZmenaStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudZmenaStlpecPeer.ID_ZMENA);
			crit.addSelectColumn(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudZmenaStlpecPeer.OLD_VALUE);
			crit.addSelectColumn(CudZmenaStlpecPeer.NEW_VALUE);
			crit.addSelectColumn(CudZmenaStlpecPeer.OLD_VALUE_EXT);
			crit.addSelectColumn(CudZmenaStlpecPeer.NEW_VALUE_EXT);

			crit.addConditional(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, dtoF.getZmenaStlpecID());
			crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudZmenaStlpecPeer.ID_ZMENA, dtoF.getIDZmena());
			crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, dtoF.getIDCiselnikStlpec());
			crit.addConditional(CudZmenaStlpecPeer.OLD_VALUE, dtoF.getOldValue(), false);
			crit.addConditional(CudZmenaStlpecPeer.NEW_VALUE, dtoF.getNewValue(), false);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOZmenaStlpec> listDTO = new ArrayList<DTOZmenaStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmenaStlpec dto = new DTOZmenaStlpec();
				dto.setZmenaStlpecID(rVal(r, CudZmenaStlpecPeer.ZMENA_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDZmena(rVal(r, CudZmenaStlpecPeer.ID_ZMENA).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudZmenaStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setOldValue(rVal(r, CudZmenaStlpecPeer.OLD_VALUE).asString());
				dto.setNewValue(rVal(r, CudZmenaStlpecPeer.NEW_VALUE).asString());

				String valueExt = rVal(r, CudZmenaStlpecPeer.OLD_VALUE_EXT).asString();
				if (StringUtils.isValid(valueExt)) {
					dto.setOldValue(valueExt);
				}

				valueExt = rVal(r, CudZmenaStlpecPeer.NEW_VALUE_EXT).asString();
				if (StringUtils.isValid(valueExt)) {
					dto.setNewValue(valueExt);
				}

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	/**
	 * Funkcia vrati pocet (aj nepotvrdenych aj nezamietnutych) zaznamov z tabulky CUD_ZMENA_STLPEC, ktore maju pre dany stlpec ciselnikStlpecID zadanu hodnotu newValue. Cize
	 * funkcia zisti ci sa dana hodnota newValue uz nachadza v registry zmien.
	 * 
	 * @param auth
	 * @param ciselnikStlpecID
	 * @param newValue
	 * @return
	 * @throws AppException
	 */
	public Integer getPocetNepublikovanychZaznamov(AuthInfo auth, Integer ciselnikID, Integer ciselnikStlpecID, String newValue) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());
				crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);

				crit.add(CudWfTodoPeer.ID_CISELNIK, ciselnikID);
				crit.add(CudWfTodoPeer.POTVRDENY, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, new DTOZmenaStlpec());
			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, ciselnikStlpecID);
			crit.addConditional(CudZmenaStlpecPeer.NEW_VALUE, newValue, false);

			crit.addCustomSql(CudZmenaStlpecPeer.ID_ZMENA, CudZmenaStlpecPeer.ID_ZMENA + " IN (" + subSql + ")");

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
			handleException(t, "getPocetNepublikovanychZaznamov.error", auth);
			return null;
		}
	}

	public Integer getPocetVaziebNaZaznam(AuthInfo auth, Set<Integer> ciselnikSet, Set<Integer> ciselnikStlpecSet, String newValue, Set<String> operaciaSet) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

				crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);

				if (ciselnikSet.size() > 1) {
					crit.addIn(CudWfTodoPeer.ID_CISELNIK, ciselnikSet.toArray(new Integer[ciselnikSet.size()]));
				} else {
					crit.addConditional(CudWfTodoPeer.ID_CISELNIK, ciselnikSet.iterator().next());
				}

				crit.add(CudWfTodoPeer.POTVRDENY, null);

				subSql = crit.getSQL();
			}

			{
				MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

				crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);

				crit.addIn(CudZmenaPeer.OPERACIA, (String[]) operaciaSet.toArray(new String[operaciaSet.size()]));

				if (operaciaSet.size() > 1) {
					crit.addIn(CudZmenaPeer.OPERACIA, (String[]) operaciaSet.toArray(new String[operaciaSet.size()]));
				} else {
					crit.addConditional(CudZmenaPeer.OPERACIA, operaciaSet.iterator().next(), false);
				}

				if (ciselnikSet.size() > 1) {
					crit.addIn(CudZmenaPeer.ID_CISELNIK, ciselnikSet.toArray(new Integer[ciselnikSet.size()]));
				} else {
					crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikSet.iterator().next());
				}

				crit.add(CudZmenaPeer.STAV, (Object) _CudConsts.ZMENA_STAV_ZAM, MyCriteria2.NOT_EQUAL);

				crit.addCustomSql(CudZmenaPeer.ZMENA_ID, CudZmenaPeer.ZMENA_ID + " IN (" + subSql + ")");

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, new DTOZmenaStlpec());
			crit.addAsColumn("pocet", "count(*)");

			if (ciselnikStlpecSet.size() > 1) {
				crit.addIn(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, (Integer[]) ciselnikStlpecSet.toArray(new Integer[ciselnikStlpecSet.size()]));
			} else {
				crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, ciselnikStlpecSet.iterator().next());
			}

			if (ciselnikSet.size() > 1) {
				crit.addIn(CudZmenaStlpecPeer.ID_CISELNIK, ciselnikSet.toArray(new Integer[ciselnikSet.size()]));
			} else {
				crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK, ciselnikSet.iterator().next());
			}

			crit.addConditional(CudZmenaStlpecPeer.NEW_VALUE, newValue, false);

			crit.addCustomSql(CudZmenaStlpecPeer.ID_ZMENA, CudZmenaStlpecPeer.ID_ZMENA + " IN (" + subSql + ")");

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
			handleException(t, "getPocetVaziebNaZaznam.error", auth);
			return null;
		}
	}

	private String createWhenClauseSql(DTOCiselnikStlpec dtoCS, int index, String zmenaValueNazov) throws AppException {

		try {
			String subSql = " WHEN " + CudCiselnikStlpecPeer.FK1_ID_CISELNIK + " = " + dtoCS.getFk1IDCiselnik() + " AND " + zmenaValueNazov + " IS NOT NULL THEN (";
			subSql += " SELECT to_char(tt" + index + "." + dtoCS.getFk1FkNazov() + ") FROM " + dtoCS.getFk1CiselnikTabulka() + " tt" + index + " WHERE ";
			subSql += "tt" + index + "." + dtoCS.getFk1PkNazov() + " = to_number(" + zmenaValueNazov + ") AND ( tt" + index + "." + _CudConsts.NAZOV_PLATNOST_DO + " >= "
					+ CudZmenaPeer.PLATNOST_OD;
			subSql += " OR tt" + index + "." + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL )";
			subSql += " AND tt" + index + "." + _CudConsts.NAZOV_PLATNOST_OD + " <= " + CudZmenaPeer.PLATNOST_OD + " ) ";

			return subSql;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createWhenClauseSql.error");
			return null;
		}

	}

	public List<DTOZmenaStlpec> listForPrint(AuthInfo auth, Page page, Integer ciselnikID, String wfTodoPotvrdeny, Date zmenaPlatnostOd, String wfDefTyp,
			List<DTOCiselnikStlpec> fkList) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDef());

				crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);

				crit.addConditional(CudWfDefPeer.TYP, wfDefTyp, false);
				crit.addConditional(CudWfDefPeer.ID_CISELNIK, ciselnikID);

				crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			{
				MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

				crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);

				crit.addConditional(CudWfTodoPeer.ID_CISELNIK, ciselnikID);

				crit.addCustomSql(CudWfTodoPeer.ID_WF_DEF, CudWfTodoPeer.ID_WF_DEF + " IN ( " + subSql + " ) ");

				if (StringUtils.isValid(wfTodoPotvrdeny)) {
					crit.addConditional(CudWfTodoPeer.POTVRDENY, wfTodoPotvrdeny, false);
				} else {
					crit.add(CudWfTodoPeer.POTVRDENY, null);
				}

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, new DTOZmenaStlpec());

			crit.addSelectColumn(CudZmenaStlpecPeer.ZMENA_STLPEC_ID);
			crit.addSelectColumn(CudZmenaStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudZmenaStlpecPeer.ID_ZMENA);
			crit.addSelectColumn(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC);
			// crit.addSelectColumn(CudZmenaStlpecPeer.OLD_VALUE);
			// crit.addSelectColumn(CudZmenaStlpecPeer.NEW_VALUE);

			String valueSql = "";
			int index = 1;
			for (DTOCiselnikStlpec dtoCS : fkList) {
				valueSql += createWhenClauseSql(dtoCS, index, CudZmenaStlpecPeer.OLD_VALUE);
				index++;
			}
			if (StringUtils.isValid(valueSql)) {
				valueSql += " ELSE to_char(" + CudZmenaStlpecPeer.OLD_VALUE + ")";
				valueSql = " CASE " + valueSql + " END ";
			} else {
				valueSql = CudZmenaStlpecPeer.OLD_VALUE;
			}
			crit.addAsColumn("old_value", valueSql);

			valueSql = "";
			for (DTOCiselnikStlpec dtoCS : fkList) {
				valueSql += createWhenClauseSql(dtoCS, index, CudZmenaStlpecPeer.NEW_VALUE);
				index++;
			}
			if (StringUtils.isValid(valueSql)) {
				valueSql += " ELSE to_char(" + CudZmenaStlpecPeer.NEW_VALUE + ")";
				valueSql = " CASE " + valueSql + " END ";
			} else {
				valueSql = CudZmenaStlpecPeer.NEW_VALUE;
			}
			crit.addAsColumn("new_value", valueSql);

			// join CUD_ZMENA
			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addJoin(CudZmenaStlpecPeer.ID_ZMENA, CudZmenaPeer.ZMENA_ID, MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK_STLPEC
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);
			crit.addJoin(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);

			crit.addCustomSql(CudZmenaStlpecPeer.ID_ZMENA, CudZmenaStlpecPeer.ID_ZMENA + " IN ( " + subSql + " ) ");
			crit.addConditional(CudZmenaPeer.PLATNOST_OD, zmenaPlatnostOd, MyCriteria2.GREATER_EQUAL);
			crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);

			crit.addDescendingOrderByColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addAscendingOrderByColumn(CudZmenaPeer.ROW_ID);
			crit.addDescendingOrderByColumn(CudZmenaPeer.ZMENA_ID);

			String sql = "SELECT rownum as cud_row_num, t.* FROM (" + crit.getSQL() + " ) t ";

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, "cud_row_num", auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOZmenaStlpec> listDTO = new ArrayList<DTOZmenaStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmenaStlpec dto = new DTOZmenaStlpec();
				dto.setZmenaStlpecID(rVal(r, CudZmenaStlpecPeer.ZMENA_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDZmena(rVal(r, CudZmenaStlpecPeer.ID_ZMENA).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudZmenaStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setOldValue(rVal(r, "old_value").asString());
				dto.setNewValue(rVal(r, "new_value").asString());

				dto.setZmenaRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setZmenaOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setZmenaPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());

				dto.setCiselnikStlpecNazov(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listForPrint.error", auth);
			return null;
		}
	}

	public Integer countForPrint(AuthInfo auth, Integer ciselnikID, String wfTodoPotvrdeny, Date zmenaPlatnostOd, String wfDefTyp) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDef());

				crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);

				crit.addConditional(CudWfDefPeer.TYP, wfDefTyp, false);
				crit.addConditional(CudWfDefPeer.ID_CISELNIK, ciselnikID);

				crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			{
				MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

				crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);

				crit.addConditional(CudWfTodoPeer.ID_CISELNIK, ciselnikID);

				crit.addCustomSql(CudWfTodoPeer.ID_WF_DEF, CudWfTodoPeer.ID_WF_DEF + " IN ( " + subSql + " ) ");

				if (StringUtils.isValid(wfTodoPotvrdeny)) {
					crit.addConditional(CudWfTodoPeer.POTVRDENY, wfTodoPotvrdeny, false);
				} else {
					crit.add(CudWfTodoPeer.POTVRDENY, null);
				}

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, new DTOZmenaStlpec());

			crit.addAsColumn("pocet", "count(*)");

			// join CUD_ZMENA
//			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
//			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
//			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addJoin(CudZmenaStlpecPeer.ID_ZMENA, CudZmenaPeer.ZMENA_ID, MyCriteria2.LEFT_JOIN);

			crit.addCustomSql(CudZmenaStlpecPeer.ID_ZMENA, CudZmenaStlpecPeer.ID_ZMENA + " IN ( " + subSql + " ) ");

			crit.addConditional(CudZmenaPeer.PLATNOST_OD, zmenaPlatnostOd, MyCriteria2.GREATER_EQUAL);
			crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);

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
			handleException(t, "countForPrint.error", auth);
			return null;
		}
	}

	public DTOZmenaStlpecLD loadData(AuthInfo auth, DTOZmenaStlpecLD dtoF, Date platnostOd) throws AppException {

		try {
			Integer zmenaIDPrev = getDelegate().getZmenaRead().readZmenaIDPrev(auth, dtoF.getCiselnikID(), dtoF.getRowID(), dtoF.getZmenaID());
			Integer histIDPrev = getDelegate().getDynCiselnikRead().getHistID(auth, dtoF.getCiselnikTabulka(), zmenaIDPrev);

			List<DTOZmenaStlpec> zmenaStlpecList = getDelegate().getZmenaStlpecRead().listLight(auth, dtoF.getCiselnikID(), dtoF.getZmenaID());

			Set<Integer> ciselnikStlpecIDs = new HashSet<Integer>();
			for (DTOZmenaStlpec dto : zmenaStlpecList) {
				if (StringUtils.isValid(dto.getIDCiselnikStlpec())) {
					ciselnikStlpecIDs.add(dto.getIDCiselnikStlpec());
				}
			}

			DTOCiselnikStlpecGui[] metaList = getDelegate().getCiselnikStlpecGuiRead().listForZmena(auth, dtoF.getCiselnikID(), platnostOd, ciselnikStlpecIDs);
			Map<Integer, DTOCiselnikStlpecGui> metaMap = new HashMap<Integer, DTOCiselnikStlpecGui>();
			Set<Integer> ciselnikIDs = new HashSet<Integer>();
			for (DTOCiselnikStlpecGui dto : metaList) {
				metaMap.put(dto.getIDCiselnikStlpec(), dto);
				if (StringUtils.isValid(dto.getCiselnikStlpecFk1IDCiselnik())) {
					ciselnikIDs.add(dto.getCiselnikStlpecFk1IDCiselnik());
				}
			}

			Map<Integer, List<DTOCiselnikStlpecGui>> lookupMetaMap = getDelegate().getCiselnikStlpecGuiRead().mapForLookup(auth, ciselnikIDs, platnostOd);

			for (DTOZmenaStlpec dto : zmenaStlpecList) {
				DTOCiselnikStlpecGui dtoCS = metaMap.get(dto.getIDCiselnikStlpec());
				if (!StringUtils.isValid(dtoCS)) {
					continue;
				}
				if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
					if (StringUtils.isValid(dto.getOldValue())) {
						dto.setOldValue(getDelegate().getDynCiselnikRead().lookupValueFormat(auth, lookupMetaMap, dtoCS.getCiselnikStlpecFk1IDCiselnik(), dto.getOldValue(),
								platnostOd));
					}
					if (StringUtils.isValid(dto.getNewValue())) {
						dto.setNewValue(getDelegate().getDynCiselnikRead().lookupValueFormat(auth, lookupMetaMap, dtoCS.getCiselnikStlpecFk1IDCiselnik(), dto.getNewValue(),
								platnostOd));
					}
				} else if (_CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getCiselnikStlpecDbTyp())) {
					if (StringUtils.isValid(dto.getOldValue())) {
						dto.setOldValue(getDelegate().getDynCiselnikRead().doubleValueFormat(dto.getOldValue(), dtoCS.getDecimals()));
					}
					if (StringUtils.isValid(dto.getNewValue())) {
						dto.setNewValue(getDelegate().getDynCiselnikRead().doubleValueFormat(dto.getNewValue(), dtoCS.getDecimals()));
					}
				} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getCiselnikStlpecDbTyp())) {
					if (StringUtils.isValid(dto.getOldValue())) {
						dto.setOldValue("T".equals(dto.getOldValue()) ? "Áno" : "Nie");
					}
					if (StringUtils.isValid(dto.getNewValue())) {
						dto.setNewValue("T".equals(dto.getNewValue()) ? "Áno" : "Nie");
					}
				} else if (_CudConsts.CISELNIK_STLPEC_GUI_EDIT_CONTROL_FILE.equals(dtoCS.getEditControl())) {
					String tabulka = "T_" + dtoCS.getCiselnikStlpecNazov().substring(3);
					if (StringUtils.isValid(dto.getOldValue())) {
						dto.setOldValue(getDelegate().getDynCiselnikRead().suborReadLookupValue(auth, tabulka, Integer.parseInt(dto.getOldValue())));
					}
					if (StringUtils.isValid(dto.getNewValue())) {
						dto.setNewValue(getDelegate().getDynCiselnikRead().suborReadLookupValue(auth, tabulka, Integer.parseInt(dto.getNewValue())));
					}
				}
			}

			DTOZmenaStlpec[] zmenaStlpecPole = new DTOZmenaStlpec[zmenaStlpecList.size()];
			int index = 0;
			for (DTOCiselnikStlpecGui dto : metaList) {
				zmenaStlpecPole[index++] = _CudLookupUtils.lookupDTOZmenaStlpecByFk(zmenaStlpecList, dto.getIDCiselnikStlpec());
			}

			DTOZmenaStlpecLD resultDTO = dtoF.getClass().newInstance();
			resultDTO.setMetaList(metaList);
			resultDTO.setZmenaStlpecList(zmenaStlpecPole);
			resultDTO.setPrevHistID(histIDPrev);

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

}
