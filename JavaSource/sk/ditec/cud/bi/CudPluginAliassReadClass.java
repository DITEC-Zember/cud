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
import sk.ditec.cud.dto.DTOPluginAlias;
import sk.ditec.dao.meta.CudPluginAliasPeer;

import com.workingdogs.village.Record;

public class CudPluginAliassReadClass extends _CudBaseClass {

	public DTOPluginAlias[] list(AuthInfo auth, DTOPluginAlias dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOPluginAlias();
			}

			MyCriteria2 crit = new MyCriteria2(CudPluginAliasPeer.PLUGIN_ALIAS_ID, dtoF);

			crit.addSelectColumn(CudPluginAliasPeer.PLUGIN_ALIAS_ID);
			crit.addSelectColumn(CudPluginAliasPeer.NAZOV_ALIASU);
			crit.addSelectColumn(CudPluginAliasPeer.POPIS);
			crit.addSelectColumn(CudPluginAliasPeer.KONSTANTA);

			crit.addConditional(CudPluginAliasPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudPluginAliasPeer.ID_PLUGIN_CLASS_NAME, dtoF.getIDPluginClassName());
			crit.addConditional(CudPluginAliasPeer.KONSTANTA, dtoF.getKonstanta(), false);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOPluginAlias> listDTO = new ArrayList<DTOPluginAlias>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPluginAlias dto = new DTOPluginAlias();
				dto.setPluginAliasID(rVal(r, CudPluginAliasPeer.PLUGIN_ALIAS_ID).asIntegerObj());
				dto.setNazovAliasu(rVal(r, CudPluginAliasPeer.NAZOV_ALIASU).asString());
				dto.setPopis(rVal(r, CudPluginAliasPeer.POPIS).asString());
				dto.setKonstanta(rVal(r, CudPluginAliasPeer.KONSTANTA).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPluginAlias[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOPluginAlias> mapLight(AuthInfo auth, Integer[] pluginAliasIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(pluginAliasIDs)) {
			return new HashMap<Integer, DTOPluginAlias>();
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudPluginAliasPeer.PLUGIN_ALIAS_ID, new DTOPluginAlias());

			crit.addSelectColumn(CudPluginAliasPeer.PLUGIN_ALIAS_ID);
			crit.addSelectColumn(CudPluginAliasPeer.NAZOV_ALIASU);
			crit.addSelectColumn(CudPluginAliasPeer.POPIS);
			crit.addSelectColumn(CudPluginAliasPeer.KONSTANTA);

			if (pluginAliasIDs.length == 1) {
				crit.addConditional(CudPluginAliasPeer.PLUGIN_ALIAS_ID, pluginAliasIDs[0]);
			} else {
				crit.addIn(CudPluginAliasPeer.PLUGIN_ALIAS_ID, pluginAliasIDs);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOPluginAlias> resultMap = new HashMap<Integer, DTOPluginAlias>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPluginAlias dto = new DTOPluginAlias();
				dto.setPluginAliasID(rVal(r, CudPluginAliasPeer.PLUGIN_ALIAS_ID).asIntegerObj());
				dto.setNazovAliasu(rVal(r, CudPluginAliasPeer.NAZOV_ALIASU).asString());
				dto.setPopis(rVal(r, CudPluginAliasPeer.POPIS).asString());
				dto.setKonstanta(rVal(r, CudPluginAliasPeer.KONSTANTA).asString());

				resultMap.put(dto.getPluginAliasID(), dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

}
