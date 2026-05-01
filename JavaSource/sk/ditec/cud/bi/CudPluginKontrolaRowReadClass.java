package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudPluginClassNamePeer;
import sk.ditec.dao.meta.CudPluginKontrolaRowPeer;
import sk.ditec.dao.meta.CudPluginPeer;

import com.workingdogs.village.Record;

public class CudPluginKontrolaRowReadClass extends _CudBaseClass {

	public DTOPluginKontrolaRow[] list(AuthInfo auth, Page page, DTOPluginKontrolaRow dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOPluginKontrolaRow();
			}

			MyCriteria2 crit = new MyCriteria2(CudPluginKontrolaRowPeer.PLUGIN_KONTROLA_ROW_ID, dtoF);

			crit.addSelectColumn(CudPluginKontrolaRowPeer.PLUGIN_KONTROLA_ROW_ID);
			crit.addSelectColumn(CudPluginKontrolaRowPeer.ID_PLUGIN_KONTROLA);
			crit.addSelectColumn(CudPluginKontrolaRowPeer.ID_PLUGIN);
			crit.addSelectColumn(CudPluginKontrolaRowPeer.ROW_ID);
			crit.addSelectColumn(CudPluginKontrolaRowPeer.STAV);
			crit.addSelectColumn(CudPluginKontrolaRowPeer.POPIS);

			crit.addSelectColumn(CudPluginClassNamePeer.CLASS_NAME);
			crit.addJoin(CudPluginKontrolaRowPeer.ID_PLUGIN, CudPluginPeer.PLUGIN_ID, MyCriteria2.LEFT_JOIN);
			crit.addJoin(CudPluginPeer.ID_PLUGIN_CLASS_NAME, CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID, MyCriteria2.LEFT_JOIN);

			String s = " WHEN " + CudPluginKontrolaRowPeer.STAV + " = \'" + _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR + "\' THEN \'F\' ";
			s += " WHEN " + CudPluginKontrolaRowPeer.STAV + " = \'" + _CudConsts.PLUGIN_KONTROLA_ROW_STAV_WARNING + "\' THEN \'F\' ";
			s += " WHEN " + CudPluginKontrolaRowPeer.STAV + " = \'" + _CudConsts.PLUGIN_KONTROLA_ROW_STAV_SUCCEDD + "\' THEN \'T\' ";
			crit.addAsColumn("kontrola_upspesna", "CASE " + s + " ELSE NULL END");

			crit.addConditional(CudPluginKontrolaRowPeer.ID_PLUGIN_KONTROLA, dtoF.getIDPluginKontrola());

			crit.addConditional(CudPluginKontrolaRowPeer.ROW_ID, dtoF.getRowID());
			crit.addConditional(CudPluginClassNamePeer.CLASS_NAME, dtoF.getPluginClassNameClassName(), true);
			crit.addConditional(CudPluginKontrolaRowPeer.STAV, dtoF.getStav(), false);
			crit.addConditional(CudPluginKontrolaRowPeer.POPIS, dtoF.getPopis(), true);

			if ("T".equals(dtoF.getKontrolaUspesna())) {
				crit.addConditional(CudPluginKontrolaRowPeer.STAV, _CudConsts.PLUGIN_KONTROLA_ROW_STAV_SUCCEDD, false);
			} else if ("F".equals(dtoF.getKontrolaUspesna())) {
				crit.addIn(CudPluginKontrolaRowPeer.STAV, new String[] { _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, _CudConsts.PLUGIN_KONTROLA_ROW_STAV_WARNING });
			}

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudPluginKontrolaRowPeer.PLUGIN_KONTROLA_ROW_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOPluginKontrolaRow> listDTO = new ArrayList<DTOPluginKontrolaRow>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPluginKontrolaRow dto = new DTOPluginKontrolaRow();
				dto.setPluginKontrolaRowID(rVal(r, CudPluginKontrolaRowPeer.PLUGIN_KONTROLA_ROW_ID).asIntegerObj());
				dto.setIDPluginKontrola(rVal(r, CudPluginKontrolaRowPeer.ID_PLUGIN_KONTROLA).asIntegerObj());
				dto.setIDPlugin(rVal(r, CudPluginKontrolaRowPeer.ID_PLUGIN).asIntegerObj());
				dto.setRowID(rVal(r, CudPluginKontrolaRowPeer.ROW_ID).asIntegerObj());
				dto.setStav(rVal(r, CudPluginKontrolaRowPeer.STAV).asString());
				dto.setPopis(rVal(r, CudPluginKontrolaRowPeer.POPIS).asString());

				dto.setPluginClassNameClassName(rVal(r, CudPluginClassNamePeer.CLASS_NAME).asString());

				dto.setKontrolaUspesna(rVal(r, "kontrola_upspesna").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPluginKontrolaRow[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Integer countNeuspesnych(AuthInfo auth, Integer pluginKontrolaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudPluginKontrolaRowPeer.PLUGIN_KONTROLA_ROW_ID, new DTOPluginKontrolaRow());

			crit.addAsColumn("pocet", "count(*)");

			crit.addIn(CudPluginKontrolaRowPeer.STAV, new String[] { _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, _CudConsts.PLUGIN_KONTROLA_ROW_STAV_WARNING });

			crit.addConditional(CudPluginKontrolaRowPeer.ID_PLUGIN_KONTROLA, pluginKontrolaID);

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
			handleException(t, "countNeuspesnych.error", auth);
			return null;
		}
	}

	public Set<Integer> pluginIDs(AuthInfo auth, Integer pluginKontrolaID, Integer rowID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(pluginKontrolaID) || !StringUtils.isValid(rowID)) {
			return new HashSet<Integer>();
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudPluginKontrolaRowPeer.PLUGIN_KONTROLA_ROW_ID, new DTOPluginKontrolaRow());

			crit.addSelectColumn(CudPluginKontrolaRowPeer.ID_PLUGIN);

			crit.addConditional(CudPluginKontrolaRowPeer.ID_PLUGIN_KONTROLA, pluginKontrolaID);
			crit.addConditional(CudPluginKontrolaRowPeer.ROW_ID, rowID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudPluginKontrolaRowPeer.ID_PLUGIN).asIntegerObj());
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "pluginIDs.error", auth);
			return null;
		}
	}

}
