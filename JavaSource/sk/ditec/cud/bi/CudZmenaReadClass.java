package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.dto.DTOZmenaLD;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudWfDefPeer;
import sk.ditec.dao.meta.CudWfTodoPeer;
import sk.ditec.dao.meta.CudZmenaEskalaciaPeer;
import sk.ditec.dao.meta.CudZmenaPeer;
import sk.ditec.dao.meta.CudZmenaStavHistPeer;

import com.workingdogs.village.Record;

public class CudZmenaReadClass extends _CudBaseClass {

	public boolean existujeZaznam(AuthInfo auth, String ciselnikTabulka, String rowSql, String[] stavPole) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSq = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, new DTOCiselnik());

				crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);

				crit.addConditional(CudCiselnikPeer.TABULKA, ciselnikTabulka, false);

				crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSq = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addAsColumn("pocet", "count(*)");

			crit.addCustomSql(CudZmenaPeer.ID_CISELNIK, CudZmenaPeer.ID_CISELNIK + " = ( " + subSq + " )");
			crit.addCustomSql(CudZmenaPeer.ROW_ID, CudZmenaPeer.ROW_ID + " IN (" + rowSql + " )");

			if (stavPole.length == 1) {
				crit.addConditional(CudZmenaPeer.STAV, stavPole[0], false);
			} else {
				crit.addIn(CudZmenaPeer.STAV, stavPole);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				Integer pocet = rVal(r, "pocet").asIntegerObj();
				if (StringUtils.isValid(pocet)) {
					return pocet.intValue() != 0;
				}
			}

			return false;

		} catch (Throwable t) {
			handleException(t, "existujeZaznam.error", auth);
			return false;
		}
	}

	public Integer count(AuthInfo auth, Integer ciselnikID, Date platnostOdOd, String[] stavPole) throws AppException {

		try {
			DTOZmena dtoF = new DTOZmena();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setPlatnostOdOd(platnostOdOd);

			return count(auth, dtoF, stavPole);

		} catch (Throwable t) {
			handleException(t, "count.error", auth);
			return 0;
		}
	}

	public Integer count(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			DTOZmena dtoF = new DTOZmena();
			dtoF.setIDCiselnik(ciselnikID);

			return count(auth, dtoF, null);

		} catch (Throwable t) {
			handleException(t, "count.error", auth);
			return 0;
		}
	}

	private Integer count(AuthInfo auth, DTOZmena dtoF, String[] stavPole) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudZmenaPeer.ZMENA_ID, dtoF.getZmenaID());
			crit.addConditional(CudZmenaPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudZmenaPeer.ROW_ID, dtoF.getRowID());
			crit.addConditional(CudZmenaPeer.OPERACIA, dtoF.getOperacia(), false);
			crit.addConditional(CudZmenaPeer.STAV, dtoF.getStav(), false);
			crit.addConditional(CudZmenaPeer.PLATNOST_OD, dtoF.getPlatnostOdOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.PLATNOST_OD, dtoF.getPlatnostOdDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudZmenaPeer.PLATNOST_DO, dtoF.getPlatnostDoOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.PLATNOST_DO, dtoF.getPlatnostDoDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudZmenaPeer.CAS_SCHVALENIA_GR, dtoF.getCasSchvaleniaGrOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.CAS_SCHVALENIA_GR, dtoF.getCasSchvaleniaGrDo(), MyCriteria2.LESS_EQUAL);

			if (StringUtils.isValid(stavPole)) {
				if (stavPole.length == 1) {
					crit.addConditional(CudZmenaPeer.STAV, stavPole[0], false);
				} else {
					crit.addIn(CudZmenaPeer.STAV, stavPole);
				}
			}

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
			return 0;
		}
	}

	public DTOZmena[] listForDynCiselnikDetail(AuthInfo auth, Page page, DTOZmena dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOZmena();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, dtoF);

			crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);
			crit.addSelectColumn(CudZmenaPeer.ID_CISELNIK);
			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudZmenaPeer.STAV);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_DO);
			crit.addSelectColumn(CudZmenaPeer.CAS_SCHVALENIA_GR);

			crit.addAsColumn("my_platnost_od", "to_char(" + CudZmenaPeer.PLATNOST_OD + ", 'YYYY_MM_DD') || '_' || " + CudZmenaPeer.ZMENA_ID);
			crit.addAsColumn("my_platnost_do", "CASE WHEN " + CudZmenaPeer.PLATNOST_DO + " IS NOT NULL THEN to_char(" + CudZmenaPeer.PLATNOST_DO + ", 'YYYY_MM_DD') || '_' || " + CudZmenaPeer.ZMENA_ID + " ELSE NULL END");

			crit.addConditional(CudZmenaPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudZmenaPeer.ROW_ID, dtoF.getRowID());

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudZmenaPeer.ZMENA_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOZmena> listDTO = new ArrayList<DTOZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmena dto = new DTOZmena();
				dto.setZmenaID(rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setStav(rVal(r, CudZmenaPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudZmenaPeer.PLATNOST_DO).asUtilDate());
				dto.setCasSchvaleniaGr(rVal(r, CudZmenaPeer.CAS_SCHVALENIA_GR).asUtilDate());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOZmena[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForDynCiselnikDetail.error", auth);
			return null;
		}
	}

	public Date readPlatnostOd(AuthInfo auth, Integer ciselnikID, Integer zmenaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);

			crit.addConditional(CudZmenaPeer.ZMENA_ID, zmenaID);
			crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "platnostOdRead.error", auth);
			return null;
		}
	}

	public Integer getPocetNepublikovanychZaznamov(AuthInfo auth, Integer ciselnikID, Integer rowID, String operacia) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

				crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);

				crit.addConditional(CudWfTodoPeer.ID_CISELNIK, ciselnikID);
				crit.add(CudWfTodoPeer.POTVRDENY, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudZmenaPeer.ROW_ID, rowID);
			crit.addConditional(CudZmenaPeer.OPERACIA, operacia);

			crit.addCustomSql(CudZmenaPeer.ZMENA_ID, CudZmenaPeer.ZMENA_ID + " IN (" + subSql + ")");

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

	public Date readMaxPlatnostOd(AuthInfo auth, Integer ciselnikID, Integer rowID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(ciselnikID) || !StringUtils.isValid(rowID)) {
			return null;
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addAsColumn("max", "max(" + CudZmenaPeer.PLATNOST_OD + ")");

			crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudZmenaPeer.ROW_ID, rowID);

			crit.add(crit.getNewCriterion(CudZmenaPeer.STAV, _CudConsts.ZMENA_STAV_ZAM, MyCriteria2.NOT_EQUAL));

			String s = CudZmenaPeer.PLATNOST_OD + " <= " + CudZmenaPeer.PLATNOST_DO;
			Criterion c1 = crit.getNewCriterion(CudZmenaPeer.PLATNOST_OD, s, MyCriteria2.CUSTOM);
			Criterion c2 = crit.getNewCriterion(CudZmenaPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			crit.add(c1.or(c2));

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, "max").asUtilDate();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "readMaxPlatnostOd.error", auth);
			return null;
		}
	}

	public List<DTOZmena> listLight(AuthInfo auth, DTOZmena dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOZmena();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, dtoF);

			crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);
			crit.addSelectColumn(CudZmenaPeer.ID_CISELNIK);
			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudZmenaPeer.STAV);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_DO);
			crit.addSelectColumn(CudZmenaPeer.CAS_SCHVALENIA_GR);

			crit.addConditional(CudZmenaPeer.ZMENA_ID, dtoF.getZmenaID());
			crit.addConditional(CudZmenaPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudZmenaPeer.ROW_ID, dtoF.getRowID());
			crit.addConditional(CudZmenaPeer.OPERACIA, dtoF.getOperacia(), false);
			crit.addConditional(CudZmenaPeer.STAV, dtoF.getStav(), false);
			crit.addConditional(CudZmenaPeer.PLATNOST_OD, dtoF.getPlatnostOdOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.PLATNOST_OD, dtoF.getPlatnostOdDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudZmenaPeer.PLATNOST_DO, dtoF.getPlatnostDoOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.PLATNOST_DO, dtoF.getPlatnostDoDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudZmenaPeer.CAS_SCHVALENIA_GR, dtoF.getCasSchvaleniaGrOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.CAS_SCHVALENIA_GR, dtoF.getCasSchvaleniaGrDo(), MyCriteria2.LESS_EQUAL);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOZmena> listDTO = new ArrayList<DTOZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmena dto = new DTOZmena();
				dto.setZmenaID(rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setStav(rVal(r, CudZmenaPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudZmenaPeer.PLATNOST_DO).asUtilDate());
				dto.setCasSchvaleniaGr(rVal(r, CudZmenaPeer.CAS_SCHVALENIA_GR).asUtilDate());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOZmena readLight(AuthInfo auth, Integer zmenaID) throws AppException {

		try {
			if (!StringUtils.isValid(zmenaID)) {
				return null;
			}

			DTOZmena dtoF = new DTOZmena();
			dtoF.setZmenaID(zmenaID);
			List<DTOZmena> listDTO = listLight(auth, dtoF);

			return !listDTO.isEmpty() ? listDTO.get(0) : null;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public List<DTOZmena> listForPau(AuthInfo auth, Integer zmenaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);
			crit.addSelectColumn(CudZmenaPeer.ID_CISELNIK);
			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudZmenaPeer.STAV);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_DO);
			crit.addSelectColumn(CudZmenaPeer.CAS_SCHVALENIA_GR);

			crit.addConditional(CudZmenaPeer.STAV, _CudConsts.ZMENA_STAV_SCH, false);

			if (StringUtils.isValid(zmenaID)) {
				crit.add(CudZmenaPeer.ZMENA_ID, zmenaID, MyCriteria2.GREATER_THAN);
			}

			crit.addAscendingOrderByColumn(CudZmenaPeer.ZMENA_ID);

			String sql = "SELECT * FROM (" + crit.getSQL() + ") WHERE rownum <= 1000";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOZmena> listDTO = new ArrayList<DTOZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmena dto = new DTOZmena();
				dto.setZmenaID(rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setStav(rVal(r, CudZmenaPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudZmenaPeer.PLATNOST_DO).asUtilDate());
				dto.setCasSchvaleniaGr(rVal(r, CudZmenaPeer.CAS_SCHVALENIA_GR).asUtilDate());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listForPau.error", auth);
			return null;
		}
	}

	public DTOZmena[] list(AuthInfo auth, Page page, DTOZmena dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOZmena();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, dtoF);

			crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);
			crit.addSelectColumn(CudZmenaPeer.ID_CISELNIK);
			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudZmenaPeer.STAV);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_DO);
			crit.addSelectColumn(CudZmenaPeer.CAS_SCHVALENIA_GR);

			// join CUD_CISELNIK
			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addJoin(CudZmenaPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);

			String s1 = " WHEN " + CudZmenaPeer.OPERACIA + " = \'" + _CudConsts.ZMENA_OPERACIA_N + "\' THEN \'novy\'";
			String s2 = " WHEN " + CudZmenaPeer.OPERACIA + " = \'" + _CudConsts.ZMENA_OPERACIA_U + "\' THEN \'zmen\'";
			String s3 = " WHEN " + CudZmenaPeer.OPERACIA + " = \'" + _CudConsts.ZMENA_OPERACIA_D + "\' THEN \'znep\'";
			String s4 = " WHEN " + CudZmenaPeer.OPERACIA + " = \'" + _CudConsts.ZMENA_OPERACIA_Z + "\' THEN \'zmaz\'";
			crit.addAsColumn("operacia_lookup", "CASE " + s1 + s2 + s3 + s4 + " END");

			crit.addConditional(CudZmenaPeer.ZMENA_ID, dtoF.getZmenaID());
			crit.addConditional(CudZmenaPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudZmenaPeer.ROW_ID, dtoF.getRowID());
			crit.addConditional(CudZmenaPeer.OPERACIA, dtoF.getOperacia(), false);
			crit.addConditional(CudZmenaPeer.STAV, dtoF.getStav(), false);
			crit.addConditional(CudZmenaPeer.PLATNOST_OD, dtoF.getPlatnostOdOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.PLATNOST_OD, dtoF.getPlatnostOdDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudZmenaPeer.PLATNOST_DO, dtoF.getPlatnostDoOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.PLATNOST_DO, dtoF.getPlatnostDoDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudZmenaPeer.CAS_SCHVALENIA_GR, dtoF.getCasSchvaleniaGrOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.CAS_SCHVALENIA_GR, dtoF.getCasSchvaleniaGrDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudCiselnikPeer.NAZOV, dtoF.getCiselnikNazov(), true);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudZmenaPeer.ZMENA_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOZmena> listDTO = new ArrayList<DTOZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmena dto = new DTOZmena();
				dto.setZmenaID(rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setStav(rVal(r, CudZmenaPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudZmenaPeer.PLATNOST_DO).asUtilDate());
				dto.setCasSchvaleniaGr(rVal(r, CudZmenaPeer.CAS_SCHVALENIA_GR).asUtilDate());

				dto.setCiselnikNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());
				dto.setCiselnikTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOZmena[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTOZmenaLD loadData(AuthInfo auth, DTOZmenaLD dtoF) throws AppException {

		try {
			return (DTOZmenaLD) getDelegate().getZmenaStlpecRead().loadData(auth, dtoF, dtoF.getPlatnostOd());

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public Integer readZmenaIDPrev(AuthInfo auth, Integer ciselnikID, Integer rowID, Integer zmenaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikID) || !StringUtils.isValid(rowID) || !StringUtils.isValid(zmenaID)) {
				return null;
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);
			crit.addSelectColumn(CudZmenaPeer.STAV);

			crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudZmenaPeer.ROW_ID, rowID);

			crit.add(CudZmenaPeer.STAV, (Object) _CudConsts.ZMENA_STAV_ZAM, MyCriteria2.NOT_EQUAL);

			crit.addAscendingOrderByColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addAscendingOrderByColumn(CudZmenaPeer.ZMENA_ID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Integer oldValue = null;

			while (iter.hasNext()) {

				Record r = (Record) iter.next();
				Integer newValue = rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj();
				String stav = rVal(r, CudZmenaPeer.STAV).asString();

				if (newValue.intValue() == zmenaID.intValue()) {
					break;
				}

				if (_CudConsts.ZMENA_STAV_PAU.equals(stav)) {
					oldValue = newValue;
				}
			}

			return oldValue;

		} catch (Throwable t) {
			handleException(t, "readZmenaIDPrev.error", auth);
			return null;
		}
	}

	public List<DTOZmena> listForPrintLight(AuthInfo auth, DTOZmena dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOZmena();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, dtoF);

			crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);
			crit.addSelectColumn(CudZmenaPeer.ID_CISELNIK);
			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudZmenaPeer.STAV);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_DO);

			crit.addConditional(CudZmenaPeer.ZMENA_ID, dtoF.getZmenaID());
			crit.addConditional(CudZmenaPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudZmenaPeer.ROW_ID, dtoF.getRowID());
			crit.addConditional(CudZmenaPeer.OPERACIA, dtoF.getOperacia(), false);
			crit.addConditional(CudZmenaPeer.STAV, dtoF.getStav(), false);
			crit.addConditional(CudZmenaPeer.PLATNOST_OD, dtoF.getPlatnostOdOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.PLATNOST_OD, dtoF.getPlatnostOdDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudZmenaPeer.PLATNOST_DO, dtoF.getPlatnostDoOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.PLATNOST_DO, dtoF.getPlatnostDoDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudZmenaPeer.CAS_SCHVALENIA_GR, dtoF.getCasSchvaleniaGrOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudZmenaPeer.CAS_SCHVALENIA_GR, dtoF.getCasSchvaleniaGrDo(), MyCriteria2.LESS_EQUAL);

			crit.addDescendingOrderByColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addAscendingOrderByColumn(CudZmenaPeer.ROW_ID);

			String sql = "SELECT * FROM (" + crit.getSQL() + " ) WHERE rownum <= 1000";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOZmena> listDTO = new ArrayList<DTOZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmena dto = new DTOZmena();
				dto.setZmenaID(rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setStav(rVal(r, CudZmenaPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudZmenaPeer.PLATNOST_DO).asUtilDate());
				dto.setCasSchvaleniaGr(rVal(r, CudZmenaPeer.CAS_SCHVALENIA_GR).asUtilDate());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public List<DTOZmena> listForEskalacia(AuthInfo auth, Integer zmenaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql1 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDef());

				crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);

				crit.addConditional(CudWfDefPeer.TYP, _CudConsts.WF_DEF_TYP_SC, false);

				crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql1 = crit.getSQL();
			}

			String subSql2 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

				crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);

				crit.add(CudWfTodoPeer.POTVRDENY, null);

				crit.addConditional(CudWfTodoPeer.ID_ZMENA, zmenaID, MyCriteria2.GREATER_THAN);

				crit.addCustomSql(CudWfTodoPeer.ID_WF_DEF, CudWfTodoPeer.ID_WF_DEF + " IN (" + subSql1 + ")");

				subSql2 = crit.getSQL();
			}

			String subSql3 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudZmenaEskalaciaPeer.ZMENA_ESKALACIA_ID, new DTOZmena());

				crit.addAsColumn("max_cas_vytvorenia", "MAX(" + CudZmenaEskalaciaPeer.CAS_VYTVORENIA + ")");

				crit.addConditional(CudZmenaEskalaciaPeer.ID_CISELNIK, 1111);
				crit.addConditional(CudZmenaEskalaciaPeer.ID_ZMENA, 2222);

				subSql3 = crit.getSQL();
				subSql3 = StringUtils.replaceAll(subSql3, "1111", CudZmenaPeer.ID_CISELNIK);
				subSql3 = StringUtils.replaceAll(subSql3, "2222", CudZmenaPeer.ZMENA_ID);
			}

			String subSql4 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDef());

				crit.addSelectColumn(CudWfDefPeer.ID_CISELNIK);

				crit.addConditional(CudWfDefPeer.TYP, _CudConsts.WF_DEF_TYP_ES, false);

				crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql4 = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);
			crit.addSelectColumn(CudZmenaPeer.ID_CISELNIK);
			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudZmenaPeer.STAV);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_DO);
			crit.addAsColumn("cas_eskalacie", "(" + subSql3 + ")");

			crit.addSelectColumn(CudZmenaStavHistPeer.CAS_VYTVORENIA);
			crit.addJoin(CudZmenaPeer.ZMENA_ID, CudZmenaStavHistPeer.ID_ZMENA, MyCriteria2.LEFT_JOIN);

			crit.addCustomSql(CudZmenaPeer.ZMENA_ID, CudZmenaPeer.ZMENA_ID + " IN (" + subSql2 + ")");

			crit.addCustomSql(CudZmenaPeer.ID_CISELNIK, CudZmenaPeer.ID_CISELNIK + " IN (" + subSql4 + ")");

			crit.addAscendingOrderByColumn(CudZmenaPeer.ZMENA_ID);

			String pattern = CudZmenaPeer.ZMENA_ID + "=" + CudZmenaStavHistPeer.ID_ZMENA;
			String value = CudZmenaStavHistPeer.ID_CISELNIK + "=" + CudZmenaPeer.ID_CISELNIK + " AND ";
			value += CudZmenaStavHistPeer.ID_ZMENA + "=" + CudZmenaPeer.ZMENA_ID + " AND ";
			value += CudZmenaStavHistPeer.STAV + "=" + CudZmenaPeer.STAV;

			String sql = "SELECT * FROM (" + crit.getSQL() + " ) WHERE rownum <= 500";
			sql = StringUtils.replaceAll(sql, pattern, value);

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOZmena> listDTO = new ArrayList<DTOZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmena dto = new DTOZmena();
				dto.setZmenaID(rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setStav(rVal(r, CudZmenaPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudZmenaPeer.PLATNOST_DO).asUtilDate());

				dto.setZmenaStavHistCasVytvorenia(rVal(r, CudZmenaStavHistPeer.CAS_VYTVORENIA).asUtilDate());

				dto.setZmenaEskalaciaCasVytvorenia(rVal(r, "cas_eskalacie").asUtilDate());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listForEskalacia.error", auth);
			return null;
		}
	}

}
