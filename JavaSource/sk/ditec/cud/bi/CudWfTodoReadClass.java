package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOWfTodoLD;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudWfDefPeer;
import sk.ditec.dao.meta.CudWfTodoPeer;
import sk.ditec.dao.meta.CudZmenaPeer;

import com.workingdogs.village.Record;

public class CudWfTodoReadClass extends _CudBaseClass {

	public DTOWfTodo[] list(AuthInfo auth, Integer ciselnikID, Integer zmenaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

			crit.addSelectColumn(CudWfTodoPeer.WF_TODO_ID);
			crit.addSelectColumn(CudWfTodoPeer.POZNAMKA);
			crit.addSelectColumn(CudWfTodoPeer.ID_UCET);

			// join CUD_WF_DEF
			crit.addSelectColumn(CudWfDefPeer.NAZOV);
			crit.addSelectColumn(CudWfDefPeer.TYP);
			crit.addJoin(CudWfTodoPeer.ID_WF_DEF, CudWfDefPeer.WF_DEF_ID, MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudWfTodoPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudWfTodoPeer.ID_ZMENA, zmenaID);
			crit.addConditional(CudWfTodoPeer.POTVRDENY, "T", false);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOWfTodo> listDTO = new ArrayList<DTOWfTodo>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfTodo dto = new DTOWfTodo();
				dto.setWfTodoID(rVal(r, CudWfTodoPeer.WF_TODO_ID).asIntegerObj());
				dto.setPoznamka(rVal(r, CudWfTodoPeer.POZNAMKA).asString());
				dto.setIDUcet(rVal(r, CudWfTodoPeer.ID_UCET).asIntegerObj());
				dto.setWfDefNazov(rVal(r, CudWfDefPeer.NAZOV).asString());
				dto.setWfDefTyp(rVal(r, CudWfDefPeer.TYP).asString());

				dto.setListSize(lp.size());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOWfTodo[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTOWfTodo[] list(AuthInfo auth, Page page, DTOWfTodo dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

			crit.addSelectColumn(CudWfTodoPeer.WF_TODO_ID);
			crit.addSelectColumn(CudWfTodoPeer.ID_CISELNIK);
			crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);
			crit.addSelectColumn(CudWfTodoPeer.ID_WF_DEF);
			crit.addSelectColumn(CudWfTodoPeer.POTVRDENY);
			crit.addSelectColumn(CudWfTodoPeer.POZNAMKA);
			crit.addSelectColumn(CudWfTodoPeer.ID_UCET);

			// join CUD_CISELNIK
			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addJoin(CudWfTodoPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);

			// join CUD_ZMENA
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addJoin(CudWfTodoPeer.ID_ZMENA, CudZmenaPeer.ZMENA_ID, MyCriteria2.LEFT_JOIN);

			// join CUD_WF_DEF
			crit.addSelectColumn(CudWfDefPeer.TYP);
			crit.addJoin(CudWfTodoPeer.ID_WF_DEF, CudWfDefPeer.WF_DEF_ID, MyCriteria2.LEFT_JOIN);

			String s = "CASE WHEN " + CudWfTodoPeer.POTVRDENY + " = \'T\' THEN \'P\' WHEN " + CudWfTodoPeer.POTVRDENY + " = \'F\' THEN \'Z\' ELSE NULL END";
			crit.addAsColumn("potvrdeny_lookup", s);

			// where
			crit.addConditional(CudWfTodoPeer.WF_TODO_ID, dtoF.getWfTodoID());
			crit.addConditional(CudWfTodoPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudWfTodoPeer.ID_ZMENA, dtoF.getIDZmena());
			crit.addConditional(CudWfTodoPeer.ID_WF_DEF, dtoF.getIDWfDef());
			crit.addConditional(CudWfTodoPeer.POTVRDENY, dtoF.getPotvrdeny(), false);
			crit.addConditional(CudWfTodoPeer.ID_UCET, dtoF.getIDUcet());

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudWfTodoPeer.WF_TODO_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOWfTodo> listDTO = new ArrayList<DTOWfTodo>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfTodo dto = new DTOWfTodo();
				dto.setWfTodoID(rVal(r, CudWfTodoPeer.WF_TODO_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudWfTodoPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDZmena(rVal(r, CudWfTodoPeer.ID_ZMENA).asIntegerObj());
				dto.setIDWfDef(rVal(r, CudWfTodoPeer.ID_WF_DEF).asIntegerObj());
				dto.setPotvrdeny(rVal(r, CudWfTodoPeer.POTVRDENY).asString());
				dto.setPoznamka(rVal(r, CudWfTodoPeer.POZNAMKA).asString());
				dto.setIDUcet(rVal(r, CudWfTodoPeer.ID_UCET).asIntegerObj());

				dto.setCiselnikNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());

				dto.setZmenaOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());

				dto.setWfDefTyp(rVal(r, CudWfDefPeer.TYP).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOWfTodo[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTOWfTodo[] ulohaList(AuthInfo auth, Page page, DTOWfTodo dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOWfTodo();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

			crit.addSelectColumn(CudWfTodoPeer.WF_TODO_ID);
			crit.addSelectColumn(CudWfTodoPeer.ID_CISELNIK);
			crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);
			crit.addSelectColumn(CudWfTodoPeer.ID_WF_DEF);
			crit.addSelectColumn(CudWfTodoPeer.POTVRDENY);
			crit.addSelectColumn(CudWfTodoPeer.ID_UCET);

			// join CUD_ZMENA
			crit.addSelectColumn(CudZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudZmenaPeer.STAV);
			crit.addSelectColumn(CudZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudZmenaPeer.PLATNOST_OD);
			crit.addJoin(CudWfTodoPeer.ID_ZMENA, CudZmenaPeer.ZMENA_ID, MyCriteria2.LEFT_JOIN);

			// join CUD_WF_DEF
			crit.addAsColumn("wf_def_nazov", CudWfDefPeer.NAZOV);
			crit.addAsColumn("wf_def_typ", CudWfDefPeer.TYP);
			crit.addJoin(CudWfTodoPeer.ID_WF_DEF, CudWfDefPeer.WF_DEF_ID, MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addAsColumn("ciselnik_nazov", CudCiselnikPeer.NAZOV);
			crit.addAsColumn("ciselnik_tab", CudCiselnikPeer.TABULKA);
			crit.addJoin(CudWfTodoPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);

			String s1 = " WHEN " + CudZmenaPeer.OPERACIA + " = \'" + _CudConsts.ZMENA_OPERACIA_N + "\' THEN \'novy\'";
			String s2 = " WHEN " + CudZmenaPeer.OPERACIA + " = \'" + _CudConsts.ZMENA_OPERACIA_U + "\' THEN \'zmen\'";
			String s3 = " WHEN " + CudZmenaPeer.OPERACIA + " = \'" + _CudConsts.ZMENA_OPERACIA_D + "\' THEN \'znep\'";
			String s4 = " WHEN " + CudZmenaPeer.OPERACIA + " = \'" + _CudConsts.ZMENA_OPERACIA_Z + "\' THEN \'zmaz\'";
			crit.addAsColumn("operacia_lookup", "CASE " + s1 + s2 + s3 + s4 + " END");

			// where
			crit.addConditional(CudWfTodoPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudZmenaPeer.OPERACIA, dtoF.getZmenaOperacia(), false);
			crit.addConditional(CudWfDefPeer.NAZOV, dtoF.getWfDefNazov(), true);
			crit.addConditional(CudWfTodoPeer.ID_UCET, auth.getAccountId());

			crit.add(CudWfTodoPeer.POTVRDENY, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudWfTodoPeer.WF_TODO_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOWfTodo> listDTO = new ArrayList<DTOWfTodo>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfTodo dto = new DTOWfTodo();
				dto.setWfTodoID(rVal(r, CudWfTodoPeer.WF_TODO_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudWfTodoPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDZmena(rVal(r, CudWfTodoPeer.ID_ZMENA).asIntegerObj());
				dto.setIDWfDef(rVal(r, CudWfTodoPeer.ID_WF_DEF).asIntegerObj());
				dto.setPotvrdeny(rVal(r, CudWfTodoPeer.POTVRDENY).asString());
				dto.setIDUcet(rVal(r, CudWfTodoPeer.ID_UCET).asIntegerObj());

				// zmena
				dto.setZmenaOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setZmenaStav(rVal(r, CudZmenaPeer.STAV).asString());
				dto.setZmenaRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setZmenaPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());

				// wf_def
				dto.setWfDefNazov(rVal(r, "wf_def_nazov").asString());
				dto.setWfDefTyp(rVal(r, "wf_def_typ").asString());

				// ciselnik
				dto.setCiselnikNazov(rVal(r, "ciselnik_nazov").asString());
				dto.setCiselnikTabulka(rVal(r, "ciselnik_tab").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return (DTOWfTodo[]) listDTO.toArray(new DTOWfTodo[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "ulohaList.error", auth);
			return null;
		}
	}

	private String readPoznamka(AuthInfo auth, Integer ciselnikID, Integer zmenaID, String wfDefTyp) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

			crit.addSelectColumn(CudWfTodoPeer.POZNAMKA);

			// join KMD_WF_DEF
			crit.addJoin(CudWfTodoPeer.ID_WF_DEF, CudWfDefPeer.WF_DEF_ID, MyCriteria2.LEFT_JOIN);

			// where
			crit.addConditional(CudWfTodoPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudWfTodoPeer.ID_ZMENA, zmenaID);
			crit.addConditional(CudWfTodoPeer.POTVRDENY, "T", false);

			crit.addConditional(CudWfDefPeer.TYP, wfDefTyp, false);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, CudWfTodoPeer.POZNAMKA).asString();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "readPoznamka.error", auth);
			return null;
		}
	}

	public DTOWfTodoLD loadData(AuthInfo auth, DTOWfTodoLD dtoF) throws AppException {

		try {
			Date platnostOd = getDelegate().getZmenaRead().readPlatnostOd(auth, dtoF.getCiselnikID(), dtoF.getZmenaID());

			String poznamka = readPoznamka(auth, dtoF.getCiselnikID(), dtoF.getZmenaID(), _CudConsts.WF_DEF_TYP_IN);

			DTOWfTodoLD resultDTO = (DTOWfTodoLD) getDelegate().getZmenaStlpecRead().loadData(auth, dtoF, platnostOd);
			resultDTO.setPoznamka(poznamka);

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	/**
	 * Funkcia vrati pocet zaznamov z registra zmien, pre dany ciselnik <b>ciselnikID</b> a zaznam <b>rowID</b>. Ak <b>potvrdeny</b> je NULL, funkcia vrati pocet zaznamov z
	 * registra zmien, ktore cakaju na schvalenie/zamietnutie. Ak <b>potvrdeny</b> je T alebo F, podla toto vrati pocet schvalenych alebo zamietnutych zaznamov z restra zmien.
	 * 
	 * @param auth
	 * @param ciselnikID
	 *            dany ciselnik
	 * @param rowID
	 *            zaznam ID
	 * @param potvrdeny
	 *            moze byt NULL, alebo T alebo F
	 * @return
	 * @throws AppException
	 */
	public Integer count(AuthInfo auth, Integer ciselnikID, Integer rowID, String potvrdeny) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikID) || !StringUtils.isValid(rowID)) {
				return null;
			}

			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

				crit.addSelectColumn(CudZmenaPeer.ZMENA_ID);

				crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);
				crit.addConditional(CudZmenaPeer.ROW_ID, rowID);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudWfTodoPeer.ID_CISELNIK, ciselnikID);

			if (StringUtils.isValid(potvrdeny)) {
				crit.addConditional(CudWfTodoPeer.POTVRDENY, potvrdeny, false);
			} else {
				crit.add(CudWfTodoPeer.POTVRDENY, null);
			}

			crit.addCustomSql(CudWfTodoPeer.ID_ZMENA, CudWfTodoPeer.ID_ZMENA + " IN (" + subSql + ")");

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

	public DTOWfTodo readLight(AuthInfo auth, Integer ciselnikID, Integer zmenaID, Integer wfDefID) throws AppException {

		try {
			DTOWfTodo dtoF = new DTOWfTodo();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setIDZmena(zmenaID);
			dtoF.setIDWfDef(wfDefID);

			List<DTOWfTodo> listDTO = listLight(auth, dtoF);

			return StringUtils.isValid(listDTO) ? listDTO.get(0) : null;

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	private List<DTOWfTodo> listLight(AuthInfo auth, DTOWfTodo dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOWfTodo();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, dtoF);

			crit.addSelectColumn(CudWfTodoPeer.WF_TODO_ID);
			crit.addSelectColumn(CudWfTodoPeer.ID_CISELNIK);
			crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);
			crit.addSelectColumn(CudWfTodoPeer.ID_WF_DEF);
			crit.addSelectColumn(CudWfTodoPeer.POTVRDENY);
			crit.addSelectColumn(CudWfTodoPeer.POZNAMKA);
			crit.addSelectColumn(CudWfTodoPeer.ID_UCET);

			crit.addConditional(CudWfTodoPeer.WF_TODO_ID, dtoF.getWfTodoID());
			crit.addConditional(CudWfTodoPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudWfTodoPeer.ID_ZMENA, dtoF.getIDZmena());
			crit.addConditional(CudWfTodoPeer.ID_WF_DEF, dtoF.getIDWfDef());
			crit.addConditional(CudWfTodoPeer.POTVRDENY, dtoF.getPotvrdeny());
			crit.addConditional(CudWfTodoPeer.POZNAMKA, dtoF.getPoznamka(), false);
			crit.addConditional(CudWfTodoPeer.ID_UCET, dtoF.getIDUcet());

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOWfTodo> listDTO = new ArrayList<DTOWfTodo>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfTodo dto = new DTOWfTodo();
				dto.setWfTodoID(rVal(r, CudWfTodoPeer.WF_TODO_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudWfTodoPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDZmena(rVal(r, CudWfTodoPeer.ID_ZMENA).asIntegerObj());
				dto.setIDWfDef(rVal(r, CudWfTodoPeer.ID_WF_DEF).asIntegerObj());
				dto.setPotvrdeny(rVal(r, CudWfTodoPeer.POTVRDENY).asString());
				dto.setPoznamka(rVal(r, CudWfTodoPeer.POZNAMKA).asString());
				dto.setIDUcet(rVal(r, CudWfTodoPeer.ID_UCET).asIntegerObj());

				listDTO.add(dto);
			}
			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public List<DTOWfTodo> listLight(AuthInfo auth, Integer ciselnikID, Integer zmenaID) throws AppException {

		try {
			DTOWfTodo dtoF = new DTOWfTodo();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setIDZmena(zmenaID);

			return listLight(auth, dtoF);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOWfTodo readLast(AuthInfo auth, Integer ciselnikID, Integer zmenaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

			crit.addSelectColumn(CudWfTodoPeer.WF_TODO_ID);
			crit.addSelectColumn(CudWfTodoPeer.ID_CISELNIK);
			crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);
			crit.addSelectColumn(CudWfTodoPeer.ID_WF_DEF);
			crit.addSelectColumn(CudWfTodoPeer.POZNAMKA);
			crit.addSelectColumn(CudWfTodoPeer.ID_UCET);

			crit.addConditional(CudWfTodoPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudWfTodoPeer.ID_ZMENA, zmenaID);

			crit.addConditional(CudWfTodoPeer.POTVRDENY, "T", false);

			crit.addDescendingOrderByColumn(CudWfTodoPeer.WF_TODO_ID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			DTOWfTodo resultDTO = null;

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				resultDTO = new DTOWfTodo();
				resultDTO.setWfTodoID(rVal(r, CudWfTodoPeer.WF_TODO_ID).asIntegerObj());
				resultDTO.setIDCiselnik(rVal(r, CudWfTodoPeer.ID_CISELNIK).asIntegerObj());
				resultDTO.setIDZmena(rVal(r, CudWfTodoPeer.ID_ZMENA).asIntegerObj());
				resultDTO.setIDWfDef(rVal(r, CudWfTodoPeer.ID_WF_DEF).asIntegerObj());
				resultDTO.setPoznamka(rVal(r, CudWfTodoPeer.POZNAMKA).asString());
				resultDTO.setIDUcet(rVal(r, CudWfTodoPeer.ID_UCET).asIntegerObj());
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "readLast.error", auth);
			return null;
		}
	}

}
