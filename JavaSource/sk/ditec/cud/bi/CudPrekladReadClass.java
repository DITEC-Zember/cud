package sk.ditec.cud.bi;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOPreklad;
import sk.ditec.cud.dto.DTOPrekladJazyk;
import sk.ditec.cud.dto.DTOPrekladLD;
import sk.ditec.cud.dto.DTOPrekladStlpec;
import sk.ditec.cud.dto.DTOPrekladTabulka;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudPrekladJazykPeer;
import sk.ditec.dao.meta.CudPrekladPeer;
import sk.ditec.dao.meta.CudPrekladStlpecPeer;
import sk.ditec.dao.meta.CudPrekladTabulkaPeer;

import com.workingdogs.village.Record;

public class CudPrekladReadClass extends _CudBaseClass {

	public DTOPreklad[] list(AuthInfo auth, Page page, DTOPreklad dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOPreklad();
			}

			MyCriteria2 crit = new MyCriteria2(CudPrekladPeer.PREKLAD_ID, dtoF);

			crit.addSelectColumn(CudPrekladPeer.PREKLAD_ID);
			crit.addSelectColumn(CudPrekladPeer.ID_PREKLAD_JAZYK);
			crit.addSelectColumn(CudPrekladPeer.ID_PREKLAD_STLPEC);
			crit.addSelectColumn(CudPrekladPeer.ZAZNAM_ID);
			crit.addSelectColumn(CudPrekladPeer.PREKLAD);

			crit.addConditional(CudPrekladPeer.PREKLAD_ID, dtoF.getPrekladID());
			crit.addConditional(CudPrekladPeer.ZAZNAM_ID, dtoF.getZaznamID());

			// join CUD_PREKLAD_JAZYK
			crit.addSelectColumn(CudPrekladJazykPeer.KOD);
			crit.addJoin(CudPrekladPeer.ID_PREKLAD_JAZYK, CudPrekladJazykPeer.PREKLAD_JAZYK_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudPrekladJazykPeer.KOD, dtoF.getPrekladJazykKod(), false);

			// join CUD_PREKLAD_STLPEC
			crit.addAlias("t1", CudPrekladStlpecPeer.TABLE_NAME);
			crit.addAsColumn("stlpec_nazov_db", "t1.NAZOV_DB");
			crit.addAsColumn("stlpec_nadpis", "t1.NADPIS");
			crit.addJoin(CudPrekladPeer.ID_PREKLAD_STLPEC, "t1.PREKLAD_STLPEC_ID", MyCriteria2.LEFT_JOIN);
			crit.addConditional("t1.NAZOV_DB", dtoF.getPrekladStlpecNazovDB(), true);
			crit.addConditional("t1.NADPIS", dtoF.getPrekladStlpecNadpis(), true);
			crit.addConditional("t1.ID_PREKLAD_TABULKA", dtoF.getPrekladStlpecIDPrekladTabulka());

			// join CUD_PREKLAD_TABULKA
			crit.addAlias("t2", CudPrekladTabulkaPeer.TABLE_NAME);
			crit.addAsColumn("tabulka_id", "t2.preklad_tabulka_id");
			crit.addAsColumn("tabulka_nazov_db", "t2.NAZOV_DB");
			crit.addJoin("t1.ID_PREKLAD_TABULKA", "t2.PREKLAD_TABULKA_ID", MyCriteria2.LEFT_JOIN);

			crit.add(CudPrekladPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudPrekladPeer.PREKLAD_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOPreklad> listDTO = new ArrayList<DTOPreklad>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPreklad dto = new DTOPreklad();
				dto.setPrekladID(rVal(r, CudPrekladPeer.PREKLAD_ID).asIntegerObj());
				dto.setIDPrekladJazyk(rVal(r, CudPrekladPeer.ID_PREKLAD_JAZYK).asIntegerObj());
				dto.setIDPrekladStlpec(rVal(r, CudPrekladPeer.ID_PREKLAD_STLPEC).asIntegerObj());
				dto.setZaznamID(rVal(r, CudPrekladPeer.ZAZNAM_ID).asIntegerObj());
				dto.setPreklad(new String(rVal(r, CudPrekladPeer.PREKLAD).asBytes(), Charset.forName("UTF-8")));

				dto.setPrekladJazykKod(rVal(r, CudPrekladJazykPeer.KOD).asString());

				dto.setPrekladStlpecIDPrekladTabulka(rVal(r, "tabulka_id").asIntegerObj());
				dto.setPrekladStlpecPrekladTabulkaNazovDB(rVal(r, "tabulka_nazov_db").asString());

				dto.setPrekladStlpecNazovDB(rVal(r, "stlpec_nazov_db").asString());
				dto.setPrekladStlpecNadpis(rVal(r, "stlpec_nadpis").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPreklad[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	private Integer count(AuthInfo auth, Integer prekladID, Integer prekladJazykID, Integer prekladStlpecID, Integer rowID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudPrekladPeer.PREKLAD_ID, new DTOPreklad());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudPrekladPeer.ID_PREKLAD_JAZYK, prekladJazykID);
			crit.addConditional(CudPrekladPeer.ID_PREKLAD_STLPEC, prekladStlpecID);
			crit.addConditional(CudPrekladPeer.ZAZNAM_ID, rowID);

			if (StringUtils.isValid(prekladID)) {
				crit.add(CudPrekladPeer.PREKLAD_ID, prekladID, MyCriteria2.NOT_EQUAL);
			}

			crit.add(CudPrekladPeer.ID_TRANSAKCIA_ZRUSENE, null);

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

	public String updateKontrola(AuthInfo auth, DTOPreklad dto) throws AppException {

		try {
			Integer pocet = count(auth, dto.getPrekladID(), dto.getIDPrekladJazyk(), dto.getIDPrekladStlpec(), dto.getZaznamID());
			if (pocet.intValue() != 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_404);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	private Map<Integer, DTOPreklad> mapLight(AuthInfo auth, Integer[] ids) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ids)) {
				return new HashMap<Integer, DTOPreklad>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, new DTOPreklad());

			crit.addSelectColumn(CudPrekladPeer.PREKLAD_ID);
			crit.addSelectColumn(CudPrekladPeer.CAS_ZMENY);
			crit.addSelectColumn(CudPrekladPeer.ID_UCET);

			if (ids.length == 1) {
				crit.addConditional(CudPrekladPeer.PREKLAD_ID, ids[0]);
			} else {
				crit.addIn(CudPrekladPeer.PREKLAD_ID, ids);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOPreklad> mapDTO = new HashMap<Integer, DTOPreklad>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPreklad dto = new DTOPreklad();
				dto.setPrekladID(rVal(r, CudPrekladPeer.PREKLAD_ID).asIntegerObj());
				dto.setCasZmeny(rVal(r, CudPrekladPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudPrekladPeer.ID_UCET).asIntegerObj());

				dto.setListSize(lp.size());

				mapDTO.put(dto.getPrekladID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public DTOPrekladLD loadData(AuthInfo auth, DTOPrekladLD dtoF) throws AppException {

		try {
			DTOPrekladLD resultDTO = new DTOPrekladLD();

			Map<Integer, DTOPreklad> mapa1 = mapLight(auth, new Integer[] { dtoF.getPrekladID() });
			resultDTO.setPrekladDTO(mapa1.get(dtoF.getPrekladID()));

			Map<Integer, DTOPrekladStlpec> mapa2 = getDelegate().getPrekladStlpecRead().mapLight(auth, new Integer[] { dtoF.getIDPrekladStlpec() });
			resultDTO.setPrekladStlpecDTO(mapa2.get(dtoF.getIDPrekladStlpec()));

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public Map<Integer, Map<String, String>> map(AuthInfo auth, String jazykKod, String tabulkaNazov, Integer[] zaznamIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String jazykSubSql = null;
			{
				if (!StringUtils.isValid(jazykKod)) {
					return new HashMap<Integer, Map<String, String>>();
				}

				MyCriteria2 crit = new MyCriteria2(CudPrekladJazykPeer.PREKLAD_JAZYK_ID, new DTOPrekladJazyk());

				crit.addSelectColumn(CudPrekladJazykPeer.PREKLAD_JAZYK_ID);

				crit.addConditional(CudPrekladJazykPeer.KOD, jazykKod.toUpperCase());

				jazykSubSql = crit.getSQL();
			}

			String tabulkaSubSql = null;
			{
				if (!StringUtils.isValid(tabulkaNazov)) {
					return new HashMap<Integer, Map<String, String>>();
				}

				MyCriteria2 crit = new MyCriteria2(CudPrekladTabulkaPeer.PREKLAD_TABULKA_ID, new DTOPrekladTabulka());

				crit.addSelectColumn(CudPrekladTabulkaPeer.PREKLAD_TABULKA_ID);

				crit.addConditional(CudPrekladTabulkaPeer.NAZOV_DB, tabulkaNazov);

				tabulkaSubSql = crit.getSQL();
			}

			String stlpecSubSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, new DTOPrekladStlpec());

				crit.addSelectColumn(CudPrekladStlpecPeer.PREKLAD_STLPEC_ID);

				crit.addCustomSql(CudPrekladStlpecPeer.ID_PREKLAD_TABULKA, CudPrekladStlpecPeer.ID_PREKLAD_TABULKA + " = (" + tabulkaSubSql + ")");

				stlpecSubSql = crit.getSQL();
			}

			if (!StringUtils.isValid(zaznamIDs)) {
				return new HashMap<Integer, Map<String, String>>();
			}

			MyCriteria2 crit = new MyCriteria2(CudPrekladPeer.PREKLAD_ID, new DTOPreklad());

			crit.addSelectColumn(CudPrekladPeer.ZAZNAM_ID);
			crit.addSelectColumn(CudPrekladPeer.PREKLAD);

			// join CUD_PREKLAD_STLPEC
			crit.addSelectColumn(CudPrekladStlpecPeer.NAZOV_DB);
			crit.addJoin(CudPrekladPeer.ID_PREKLAD_STLPEC, CudPrekladStlpecPeer.PREKLAD_STLPEC_ID, MyCriteria2.LEFT_JOIN);

			if (zaznamIDs.length == 0) {
				crit.addConditional(CudPrekladPeer.ZAZNAM_ID, zaznamIDs[0]);
			} else {
				crit.addIn(CudPrekladPeer.ZAZNAM_ID, zaznamIDs);
			}

			crit.addCustomSql(CudPrekladPeer.ID_PREKLAD_JAZYK, CudPrekladPeer.ID_PREKLAD_JAZYK + " = (" + jazykSubSql + ")");
			crit.addCustomSql(CudPrekladPeer.ID_PREKLAD_STLPEC, CudPrekladPeer.ID_PREKLAD_STLPEC + " IN (" + stlpecSubSql + ")");

			crit.add(CudPrekladPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, Map<String, String>> mapDTO = new HashMap<Integer, Map<String, String>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Integer rowID = rVal(r, CudPrekladPeer.ZAZNAM_ID).asIntegerObj();
				String preklad = new String(rVal(r, CudPrekladPeer.PREKLAD).asBytes(), Charset.forName("UTF-8"));
				String stpecNazov = rVal(r, CudPrekladStlpecPeer.NAZOV_DB).asString();

				if (!StringUtils.isValid(mapDTO.get(rowID))) {
					mapDTO.put(rowID, new HashMap<String, String>());
				}
				mapDTO.get(rowID).put(stpecNazov, preklad);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "map.error", auth);
			return null;
		}
	}

}
