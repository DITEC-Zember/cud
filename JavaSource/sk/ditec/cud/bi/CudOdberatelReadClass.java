package sk.ditec.cud.bi;

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
import sk.ditec.cud.dto.DTOOdberatel;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.master.TDopravcaPeer;
import sk.ditec.dao.meta.CudOdberatelPeer;

import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

public class CudOdberatelReadClass extends _CudBaseClass {

	public DTOOdberatel[] listForList(AuthInfo auth, Page page, DTOOdberatel dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOOdberatel();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelPeer.ODBERATEL_ID, dtoF);

			crit.addSelectColumn(CudOdberatelPeer.ODBERATEL_ID);
			crit.addSelectColumn(CudOdberatelPeer.NAZOV);
			crit.addSelectColumn(CudOdberatelPeer.OBM_UCET_NAZOV);
			crit.addSelectColumn(CudOdberatelPeer.ROLA_NAZOV);
			crit.addSelectColumn(CudOdberatelPeer.AKTIVNY);

			// left join T_DOPRAVCA
			crit.addAsColumn("dopr_id", _CudConsts.TABULKA_T_DOPRAVCA + "." + _CudConsts.NAZOV_DOPRAVCA_ID);
			crit.addAsColumn("dopr_nazov", _CudConsts.TABULKA_T_DOPRAVCA + "." + _CudConsts.NAZOV_NAZOV);
			crit.addJoin(CudOdberatelPeer.ID_HIST_DOPRAVCA, _CudConsts.TABULKA_T_DOPRAVCA + "." + _CudConsts.NAZOV_HIST_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(_CudConsts.TABULKA_T_DOPRAVCA + "." + _CudConsts.NAZOV_NAZOV, dtoF.getHistDopravcaNazov(), true);
			crit.addConditional(_CudConsts.TABULKA_T_DOPRAVCA + "." + _CudConsts.NAZOV_DOPRAVCA_ID, StringUtils.isValid(dtoF.getHistDopravcaID()) ? dtoF.getHistDopravcaID()
					.toString() : null, true);

			if (StringUtils.isValid(dtoF.getObmUcetNazov()) && dtoF.getObmUcetNazov().indexOf("\\") != -1) {
				dtoF.setObmUcetNazov(StringUtils.replaceAll(dtoF.getObmUcetNazov(), "\\", "\\\\"));
			}

			crit.addConditional(CudOdberatelPeer.ODBERATEL_ID, StringUtils.isValid(dtoF.getOdberatelID()) ? dtoF.getOdberatelID().toString() : null, true);
			crit.addConditional(CudOdberatelPeer.NAZOV, dtoF.getNazov(), true);
			crit.addConditional(CudOdberatelPeer.OBM_UCET_NAZOV, dtoF.getObmUcetNazov(), true);
			crit.addConditional(CudOdberatelPeer.ROLA_NAZOV, dtoF.getRolaNazov(), true);
			crit.addConditional(CudOdberatelPeer.AKTIVNY, dtoF.getAktivny(), false);

			crit.add(CudOdberatelPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudOdberatelPeer.ODBERATEL_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOOdberatel> listDTO = new ArrayList<DTOOdberatel>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOOdberatel dto = new DTOOdberatel();
				dto.setOdberatelID(rVal(r, CudOdberatelPeer.ODBERATEL_ID).asIntegerObj());
				dto.setNazov(rVal(r, CudOdberatelPeer.NAZOV).asString());
				dto.setObmUcetNazov(rVal(r, CudOdberatelPeer.OBM_UCET_NAZOV).asString());
				dto.setRolaNazov(rVal(r, CudOdberatelPeer.ROLA_NAZOV).asString());
				dto.setAktivny(rVal(r, CudOdberatelPeer.AKTIVNY).asString());

				dto.setHistDopravcaID(rVal(r, "dopr_id").asIntegerObj());
				dto.setHistDopravcaNazov(rVal(r, "dopr_nazov").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOOdberatel[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForList.error", auth);
			return null;
		}
	}

	public DTOOdberatel[] listForPop(AuthInfo auth, Page page, DTOOdberatel dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOOdberatel();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelPeer.ODBERATEL_ID, dtoF);

			crit.addSelectColumn(CudOdberatelPeer.ODBERATEL_ID);
			crit.addSelectColumn(CudOdberatelPeer.NAZOV);
			crit.addSelectColumn(CudOdberatelPeer.AKTIVNY);
			crit.addSelectColumn(CudOdberatelPeer.EXPORT_TYP);
			crit.addSelectColumn(CudOdberatelPeer.EXPORT_CESTA);

			crit.addConditional(CudOdberatelPeer.ODBERATEL_ID, StringUtils.isValid(dtoF.getOdberatelID()) ? dtoF.getOdberatelID().toString() : null, true);
			crit.addConditional(CudOdberatelPeer.NAZOV, dtoF.getNazov(), true);
			crit.addConditional(CudOdberatelPeer.AKTIVNY, dtoF.getAktivny(), false);

			crit.add(CudOdberatelPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudOdberatelPeer.ODBERATEL_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOOdberatel> listDTO = new ArrayList<DTOOdberatel>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOOdberatel dto = new DTOOdberatel();
				dto.setOdberatelID(rVal(r, CudOdberatelPeer.ODBERATEL_ID).asIntegerObj());
				dto.setNazov(rVal(r, CudOdberatelPeer.NAZOV).asString());
				dto.setAktivny(rVal(r, CudOdberatelPeer.AKTIVNY).asString());
				dto.setExportTyp(rVal(r, CudOdberatelPeer.EXPORT_TYP).asString());
				dto.setExportCesta(rVal(r, CudOdberatelPeer.EXPORT_CESTA).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOOdberatel[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForPop.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOOdberatel> mapLight(AuthInfo auth, Integer[] ids) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ids)) {
				return new HashMap<Integer, DTOOdberatel>();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelPeer.ODBERATEL_ID, new DTOOdberatel());

			crit.addSelectColumn(CudOdberatelPeer.ODBERATEL_ID);
			crit.addSelectColumn(CudOdberatelPeer.ID_HIST_DOPRAVCA);
			crit.addSelectColumn(CudOdberatelPeer.NAZOV);
			crit.addSelectColumn(CudOdberatelPeer.OBM_UCET_NAZOV);
			crit.addSelectColumn(CudOdberatelPeer.ROLA_KOD);
			crit.addSelectColumn(CudOdberatelPeer.ROLA_NAZOV);
			crit.addSelectColumn(CudOdberatelPeer.EXPORT_TYP);
			crit.addSelectColumn(CudOdberatelPeer.EXPORT_CESTA);
			crit.addSelectColumn(CudOdberatelPeer.AKTIVNY);
			crit.addSelectColumn(CudOdberatelPeer.INTERNY);
			crit.addSelectColumn(CudOdberatelPeer.CAS_ZMENY);
			crit.addSelectColumn(CudOdberatelPeer.ID_UCET);

			if (ids.length == 1) {
				crit.addConditional(CudOdberatelPeer.ODBERATEL_ID, ids[0]);
			} else {
				crit.addIn(CudOdberatelPeer.ODBERATEL_ID, ids);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOOdberatel> mapDTO = new HashMap<Integer, DTOOdberatel>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOOdberatel dto = new DTOOdberatel();
				dto.setOdberatelID(rVal(r, CudOdberatelPeer.ODBERATEL_ID).asIntegerObj());

				dto.setIDHistDopravca(rVal(r, CudOdberatelPeer.ID_HIST_DOPRAVCA).asIntegerObj());
				dto.setNazov(rVal(r, CudOdberatelPeer.NAZOV).asString());
				dto.setObmUcetNazov(rVal(r, CudOdberatelPeer.OBM_UCET_NAZOV).asString());
				dto.setRolaKod(rVal(r, CudOdberatelPeer.ROLA_KOD).asString());
				dto.setRolaNazov(rVal(r, CudOdberatelPeer.ROLA_NAZOV).asString());
				dto.setExportTyp(rVal(r, CudOdberatelPeer.EXPORT_TYP).asString());
				dto.setExportCesta(rVal(r, CudOdberatelPeer.EXPORT_CESTA).asString());
				dto.setAktivny(rVal(r, CudOdberatelPeer.AKTIVNY).asString());
				dto.setInterny(rVal(r, CudOdberatelPeer.INTERNY).asString());
				dto.setCasZmeny(rVal(r, CudOdberatelPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudOdberatelPeer.ID_UCET).asIntegerObj());

				mapDTO.put(dto.getOdberatelID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public DTOOdberatel readLight(AuthInfo auth, Integer odberatelID) throws AppException {

		try {
			if (!StringUtils.isValid(odberatelID)) {
				return null;
			}

			return mapLight(auth, new Integer[] { odberatelID }).get(odberatelID);

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	private Integer obmUcetNazovCount(AuthInfo auth, Integer odberatelID, String obmUcetNazov) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(obmUcetNazov)) {
				return 0;
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelPeer.ODBERATEL_ID, new DTOOdberatel());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudOdberatelPeer.OBM_UCET_NAZOV, obmUcetNazov, false);
			crit.addConditional(CudOdberatelPeer.AKTIVNY, "T", false);

			if (StringUtils.isValid(odberatelID)) {
				crit.add(CudOdberatelPeer.ODBERATEL_ID, odberatelID, MyCriteria2.NOT_EQUAL);
			}

			crit.add(CudOdberatelPeer.ID_TRANSAKCIA_ZRUSENE, null);

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
			handleException(t, "obmUcetNazovCount.error", auth);
			return null;
		}
	}

	private Integer rolaCount(AuthInfo auth, Integer odberatelID, String rolaKod, String rolaNazov) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(rolaKod) && !StringUtils.isValid(rolaNazov)) {
				return 0;
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelPeer.ODBERATEL_ID, new DTOOdberatel());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudOdberatelPeer.ROLA_KOD, rolaKod, false);
			crit.addConditional(CudOdberatelPeer.ROLA_NAZOV, rolaNazov, false);
			crit.addConditional(CudOdberatelPeer.AKTIVNY, "T", false);

			if (StringUtils.isValid(odberatelID)) {
				crit.add(CudOdberatelPeer.ODBERATEL_ID, odberatelID, MyCriteria2.NOT_EQUAL);
			}

			crit.add(CudOdberatelPeer.ID_TRANSAKCIA_ZRUSENE, null);

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
			handleException(t, "rolaCount.error", auth);
			return null;
		}
	}

	public DTOOdberatel loadData(AuthInfo auth, DTOOdberatel dtoF) throws AppException {

		try {
			Map<Integer, DTOOdberatel> mapa = mapLight(auth, new Integer[] { dtoF.getOdberatelID() });
			DTOOdberatel dto = mapa.get(dtoF.getOdberatelID());

			if (dto == null)
				return dto;

			// T_DOPRAVCA
			if (dto.getIDHistDopravca() != null) {
				MyCriteria2 crit = new MyCriteria2();
				crit.addSelectColumn(TDopravcaPeer.NAZOV);
				crit.addSelectColumn(TDopravcaPeer.WEBSTRANKA);
				crit.add(TDopravcaPeer.HIST_ID, dto.getIDHistDopravca());

				String sql = crit.getSQL();
				getConnection(auth);
				List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
				returnConnection(auth);

				Iterator<?> iter = lp.iterator();

				if (iter.hasNext()) {
					Record r = (Record) iter.next();
					dto.setHistDopravcaNazov(rVal(r, TDopravcaPeer.NAZOV).asString());
					dto.setWebstranka(rVal(r, TDopravcaPeer.WEBSTRANKA).asString());
				}
			}

			return dto;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public String updateKontrola(AuthInfo auth, DTOOdberatel dto) throws AppException {

		try {
			Integer pocet = obmUcetNazovCount(auth, dto.getOdberatelID(), dto.getObmUcetNazov());
			if (pocet.intValue() != 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_103);
			}

			pocet = rolaCount(auth, dto.getOdberatelID(), dto.getRolaKod(), dto.getRolaNazov());
			if (pocet.intValue() != 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_118);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	public String deleteKontrola(AuthInfo auth, Integer odberatelID) throws AppException {

		try {
			Integer pocet = getDelegate().getOdberatelObjektRead().odberatelCount(auth, odberatelID);
			if (pocet.intValue() != 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_102);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "deleteKontrola.error", auth);
			return null;
		}
	}

	public DTOOdberatel getByOdberatelId(AuthInfo auth, Integer odberatelId) throws AppException {
		try {
			MyCriteria2 crit = new MyCriteria2();
			CudOdberatelPeer.addSelectColumns(crit);
			crit.add(CudOdberatelPeer.ODBERATEL_ID, odberatelId);
			crit.add(CudOdberatelPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator iter = lp.iterator();
			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return vytvor(r);
			}
			return null;
		} catch (Throwable t) {
			handleException(t, "getByOdberatelId.error", auth);
			return null;
		}
	}

	protected DTOOdberatel vytvor(Record r) throws DataSetException {
		DTOOdberatel dto = new DTOOdberatel();
		dto.setOdberatelID(rVal(r, CudOdberatelPeer.ODBERATEL_ID).asIntegerObj());
		dto.setIDHistDopravca(rVal(r, CudOdberatelPeer.ID_HIST_DOPRAVCA).asIntegerObj());
		dto.setNazov(rVal(r, CudOdberatelPeer.NAZOV).asString());
		dto.setObmUcetNazov(rVal(r, CudOdberatelPeer.OBM_UCET_NAZOV).asString());
		dto.setRolaKod(rVal(r, CudOdberatelPeer.ROLA_KOD).asString());
		dto.setRolaNazov(rVal(r, CudOdberatelPeer.ROLA_NAZOV).asString());
		dto.setExportTyp(rVal(r, CudOdberatelPeer.EXPORT_TYP).asString());
		dto.setExportCesta(rVal(r, CudOdberatelPeer.EXPORT_CESTA).asString());
		dto.setAktivny(rVal(r, CudOdberatelPeer.AKTIVNY).asString());
		dto.setInterny(rVal(r, CudOdberatelPeer.INTERNY).asString());
		dto.setCasZmeny(rVal(r, CudOdberatelPeer.CAS_ZMENY).asUtilDate());
		dto.setIDUcet(rVal(r, CudOdberatelPeer.ID_UCET).asIntegerObj());
		return dto;
	}

}
