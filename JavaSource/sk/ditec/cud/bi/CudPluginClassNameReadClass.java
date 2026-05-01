package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOPluginClassName;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.dao.meta.CudPluginClassNamePeer;
import sk.ditec.dao.meta.CudPluginKontrolaRowPeer;
import sk.ditec.dao.meta.CudPluginPeer;

import com.workingdogs.village.Record;

public class CudPluginClassNameReadClass extends _CudBaseClass {

	public DTOPluginClassName[] list(AuthInfo auth, DTOPluginClassName dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOPluginClassName();
			}

			String subSql = null;
			if (StringUtils.isValid(dtoF.getIDPluginKontrola())) {

				MyCriteria2 crit = new MyCriteria2(CudPluginKontrolaRowPeer.PLUGIN_KONTROLA_ROW_ID, new DTOPluginKontrolaRow());

				crit.setDistinct();

				crit.addSelectColumn(CudPluginKontrolaRowPeer.ID_PLUGIN);

				crit.addConditional(CudPluginKontrolaRowPeer.ID_PLUGIN_KONTROLA, dtoF.getIDPluginKontrola());

				subSql = crit.getSQL();

				crit = new MyCriteria2(CudPluginPeer.PLUGIN_ID, new DTOPlugin());

				crit.addSelectColumn(CudPluginPeer.ID_PLUGIN_CLASS_NAME);

				crit.addCustomSql(CudPluginPeer.PLUGIN_ID, CudPluginPeer.PLUGIN_ID + " IN (" + subSql + ")");

				crit.add(CudPluginPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID, dtoF);

			crit.addSelectColumn(CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID);
			crit.addSelectColumn(CudPluginClassNamePeer.CLASS_NAME);
			crit.addSelectColumn(CudPluginClassNamePeer.POPIS);

			if (StringUtils.isValid(subSql)) {
				crit.addCustomSql(CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID, CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID + " IN (" + subSql + ")");
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOPluginClassName> listDTO = new ArrayList<DTOPluginClassName>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPluginClassName dto = new DTOPluginClassName();
				dto.setPluginClassNameID(rVal(r, CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID).asIntegerObj());
				dto.setClassName(rVal(r, CudPluginClassNamePeer.CLASS_NAME).asString());
				dto.setPopis(rVal(r, CudPluginClassNamePeer.POPIS).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPluginClassName[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOPluginClassName> mapLight(AuthInfo auth, Integer[] pluginClassNameIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(pluginClassNameIDs)) {
			return new HashMap<Integer, DTOPluginClassName>();
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID, new DTOPluginClassName());

			crit.addSelectColumn(CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID);
			crit.addSelectColumn(CudPluginClassNamePeer.CLASS_NAME);
			crit.addSelectColumn(CudPluginClassNamePeer.POPIS);

			if (pluginClassNameIDs.length == 1) {
				crit.addConditional(CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID, pluginClassNameIDs[0]);
			} else {
				crit.addIn(CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID, pluginClassNameIDs);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOPluginClassName> resultMap = new HashMap<Integer, DTOPluginClassName>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPluginClassName dto = new DTOPluginClassName();
				dto.setPluginClassNameID(rVal(r, CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID).asIntegerObj());
				dto.setClassName(rVal(r, CudPluginClassNamePeer.CLASS_NAME).asString());
				dto.setPopis(rVal(r, CudPluginClassNamePeer.POPIS).asString());

				resultMap.put(dto.getPluginClassNameID(), dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

}
