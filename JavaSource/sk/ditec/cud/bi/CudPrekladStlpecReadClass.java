package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.cud.dto.DTOPrekladStlpec;
import sk.ditec.dao.meta.CudPrekladStlpecPeer;
import sk.ditec.dao.meta.CudPrekladTabulkaPeer;

import com.workingdogs.village.Record;

public class CudPrekladStlpecReadClass extends _CudBaseClass {

	public DTOPrekladStlpec[] listLight(AuthInfo auth, DTOPrekladStlpec dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOPrekladStlpec();
			}

			MyCriteria2 crit = new MyCriteria2(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, dtoF);

			crit.addSelectColumn(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID);
			crit.addSelectColumn(CudPrekladStlpecPeer.ID_PREKLAD_TABULKA);
			crit.addSelectColumn(CudPrekladStlpecPeer.NAZOV_DB);
			crit.addSelectColumn(CudPrekladStlpecPeer.NADPIS);

			crit.addConditional(CudPrekladStlpecPeer.ID_PREKLAD_TABULKA, dtoF.getIDPrekladTabulka());
			crit.addConditional(CudPrekladStlpecPeer.NAZOV_DB, dtoF.getNazovDb());
			crit.addConditional(CudPrekladStlpecPeer.NADPIS, dtoF.getNadpis());

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			List<?> lp = BasePeer.executeQuery(sql, true, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOPrekladStlpec> listDTO = new ArrayList<DTOPrekladStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPrekladStlpec dto = new DTOPrekladStlpec();
				dto.setPrekladStlpecID(rVal(r, CudPrekladStlpecPeer.PREKLAD_STLPEC_ID).asIntegerObj());
				dto.setIDPrekladTabulka(rVal(r, CudPrekladStlpecPeer.ID_PREKLAD_TABULKA).asIntegerObj());
				dto.setNazovDb(rVal(r, CudPrekladStlpecPeer.NAZOV_DB).asString());
				dto.setNadpis(rVal(r, CudPrekladStlpecPeer.NADPIS).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPrekladStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOPrekladStlpec[] listLight(AuthInfo auth, Page page, DTOPrekladStlpec dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOPrekladStlpec();
			}

			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudPrekladTabulkaPeer.PREKLAD_TABULKA_ID, dtoF);

				crit.addSelectColumn(CudPrekladTabulkaPeer.PREKLAD_TABULKA_ID);

				crit.addConditional(CudPrekladTabulkaPeer.NAZOV_DB, dtoF.getPrekladTabulkaNazovDB(), false);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, dtoF);

			crit.addSelectColumn(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID);
			crit.addSelectColumn(CudPrekladStlpecPeer.NAZOV_DB);
			crit.addSelectColumn(CudPrekladStlpecPeer.NADPIS);

			crit.addCustomSql(CudPrekladStlpecPeer.ID_PREKLAD_TABULKA, CudPrekladStlpecPeer.ID_PREKLAD_TABULKA + " = (" + subSql + ")");

			crit.addConditional(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, dtoF.getPrekladStlpecID());
			crit.addConditional(CudPrekladStlpecPeer.NAZOV_DB, dtoF.getNazovDb(), true);
			crit.addConditional(CudPrekladStlpecPeer.NADPIS, dtoF.getNadpis(), true);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOPrekladStlpec> listDTO = new ArrayList<DTOPrekladStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPrekladStlpec dto = new DTOPrekladStlpec();
				dto.setPrekladStlpecID(rVal(r, CudPrekladStlpecPeer.PREKLAD_STLPEC_ID).asIntegerObj());
				dto.setNazovDb(rVal(r, CudPrekladStlpecPeer.NAZOV_DB).asString());
				dto.setNadpis(rVal(r, CudPrekladStlpecPeer.NADPIS).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPrekladStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOPrekladStlpec> mapLight(AuthInfo auth, Integer[] ids) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, new DTOPrekladStlpec());

			crit.addSelectColumn(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID);
			crit.addSelectColumn(CudPrekladStlpecPeer.ID_PREKLAD_TABULKA);
			crit.addSelectColumn(CudPrekladStlpecPeer.NAZOV_DB);
			crit.addSelectColumn(CudPrekladStlpecPeer.NADPIS);

			if (ids.length == 1) {
				crit.addConditional(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, ids[0]);
			} else {
				crit.addIn(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, ids);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOPrekladStlpec> mapDTO = new HashMap<Integer, DTOPrekladStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPrekladStlpec dto = new DTOPrekladStlpec();
				dto.setPrekladStlpecID(rVal(r, CudPrekladStlpecPeer.PREKLAD_STLPEC_ID).asIntegerObj());
				dto.setNazovDb(rVal(r, CudPrekladStlpecPeer.NAZOV_DB).asString());
				dto.setNadpis(rVal(r, CudPrekladStlpecPeer.NADPIS).asString());

				mapDTO.put(dto.getPrekladStlpecID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public Integer[] ids(AuthInfo auth, String prekladTabulkaNazovDb) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, new DTOPrekladStlpec());

			crit.addSelectColumn(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID);

			// join CUD_PREKLAD_TABULKA
			crit.addJoin(CudPrekladStlpecPeer.ID_PREKLAD_TABULKA, CudPrekladTabulkaPeer.PREKLAD_TABULKA_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudPrekladTabulkaPeer.NAZOV_DB, prekladTabulkaNazovDb, false);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudPrekladStlpecPeer.PREKLAD_STLPEC_ID).asIntegerObj());
			}

			return set.toArray(new Integer[set.size()]);

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}

}
