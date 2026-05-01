package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOPluginAlias;
import sk.ditec.cud.dto.DTOPluginStlpec;
import sk.ditec.cud.dto.DTOPluginStlpecLD;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudPluginAliasPeer;
import sk.ditec.dao.meta.CudPluginStlpecPeer;

import com.workingdogs.village.Record;

public class CudPluginStlpecReadClass extends _CudBaseClass {

	public DTOPluginStlpec[] list(AuthInfo auth, DTOPluginStlpec dtoF, DTOPluginStlpec dto, DTOPluginStlpec[] data) throws AppException {

		try {
			if (!StringUtils.isValid(dtoF)) {
				dtoF = new DTOPluginStlpec();
			}

			if ("I".equals(dtoF.getOperacia())) {
				return insertList(auth, dto, data);

			} else if ("U".equals(dtoF.getOperacia())) {
				return updateList(auth, dto, data);

			} else if ("D".equals(dtoF.getOperacia())) {
				return deleteList(auth, dtoF.getPluginStlpecID(), data);

			} else if ("list".equals(dtoF.getOperacia())) {
				return list(auth, dtoF.getIDPlugin());

			} else if ("load".equals(dtoF.getOperacia())) {
				return data;
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	private DTOPluginStlpec[] list(AuthInfo auth, Integer pluginID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(pluginID)) {
				return new DTOPluginStlpec[0];
			}

			MyCriteria2 crit = new MyCriteria2(CudPluginStlpecPeer.PLUGIN_STLPEC_ID, new DTOPluginStlpec());

			crit.addSelectColumn(CudPluginStlpecPeer.PLUGIN_STLPEC_ID);
			crit.addSelectColumn(CudPluginStlpecPeer.ID_PLUGIN);
			crit.addSelectColumn(CudPluginStlpecPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudPluginStlpecPeer.ID_PLUGIN_ALIAS);
			crit.addSelectColumn(CudPluginStlpecPeer.HODNOTA);

			crit.addAlias("t2", CudCiselnikStlpecPeer.TABLE_NAME);
			crit.addAsColumn("cs_nadpis", "t2.NADPIS");
			crit.addJoin(CudPluginStlpecPeer.ID_CISELNIK_STLPEC, "t2.CISELNIK_STLPEC_ID", MyCriteria2.LEFT_JOIN);

			crit.addAlias("t1", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("cis_nazov", "t1.NAZOV");
			crit.addJoin("t2.id_ciselnik", "t1.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			crit.addAlias("t3", CudPluginAliasPeer.TABLE_NAME);
			crit.addAsColumn("alias_nazov", "t3.NAZOV_ALIASU");
			crit.addJoin(CudPluginStlpecPeer.ID_PLUGIN_ALIAS, "t3.PLUGIN_ALIAS_ID", MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudPluginStlpecPeer.ID_PLUGIN, pluginID);

			crit.add(CudPluginStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOPluginStlpec> listDTO = new ArrayList<DTOPluginStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPluginStlpec dto = new DTOPluginStlpec();
				dto.setPluginStlpecID(rVal(r, CudPluginStlpecPeer.PLUGIN_STLPEC_ID).asIntegerObj());
				dto.setIDPlugin(rVal(r, CudPluginStlpecPeer.ID_PLUGIN).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudPluginStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setIDPluginAlias(rVal(r, CudPluginStlpecPeer.ID_PLUGIN_ALIAS).asIntegerObj());
				dto.setHodnota(rVal(r, CudPluginStlpecPeer.HODNOTA).asString());

				dto.setCiselnikNazov(rVal(r, "cis_nazov").asString());

				dto.setCiselnikStlpecNadpis(rVal(r, "cs_nadpis").asString());

				dto.setPluginAliasNazovAliasu(rVal(r, "alias_nazov").asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPluginStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	private DTOPluginStlpec[] insertList(AuthInfo auth, DTOPluginStlpec dto, DTOPluginStlpec[] dataList) throws AppException {

		try {
			int minValue = -10;
			if (StringUtils.isValid(dataList)) {
				for (DTOPluginStlpec dtoItem : dataList) {
					if (dtoItem.getPluginStlpecID().intValue() < minValue) {
						minValue = dtoItem.getPluginStlpecID();
					}
				}
			}
			dto.setPluginStlpecID(--minValue);

			List<DTOPluginStlpec> listDTO = new ArrayList<DTOPluginStlpec>(Arrays.asList(dataList));
			listDTO.add(dto);

			return listDTO.toArray(new DTOPluginStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "insertList.error", auth);
			return null;
		}
	}

	private DTOPluginStlpec[] updateList(AuthInfo auth, DTOPluginStlpec dto, DTOPluginStlpec[] dataList) throws AppException {

		try {
			List<DTOPluginStlpec> listDTO = new ArrayList<DTOPluginStlpec>();

			for (DTOPluginStlpec dtoItem : dataList) {
				if (dtoItem.getPluginStlpecID().intValue() == dto.getPluginStlpecID().intValue()) {
					listDTO.add(dto);
				} else {
					listDTO.add(dtoItem);
				}
			}

			return listDTO.toArray(new DTOPluginStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "updateList.error", auth);
			return null;
		}
	}

	private DTOPluginStlpec[] deleteList(AuthInfo auth, Integer pluginStlpecID, DTOPluginStlpec[] dataList) throws AppException {

		try {
			List<DTOPluginStlpec> listDTO = new ArrayList<DTOPluginStlpec>();

			for (DTOPluginStlpec dtoItem : dataList) {
				if (dtoItem.getPluginStlpecID().intValue() != pluginStlpecID.intValue()) {
					listDTO.add(dtoItem);
				}
			}

			return listDTO.toArray(new DTOPluginStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "deleteList.error", auth);
			return null;
		}
	}

	public DTOPluginStlpecLD loadData(AuthInfo auth, DTOPluginStlpecLD dtoF) throws AppException {

		try {
			DTOPluginStlpecLD resultDTO = new DTOPluginStlpecLD();

			if (StringUtils.isValid(dtoF.getIDPluginAlias())) {
				Map<Integer, DTOPluginAlias> mapa = getDelegate().getPluginAliassRead().mapLight(auth, new Integer[] { dtoF.getIDPluginAlias() });
				resultDTO.setPluginAliasDTO(mapa.get(dtoF.getIDPluginAlias()));
			}

			if (StringUtils.isValid(dtoF.getIDCiselnikStlpec())) {
				resultDTO.setCiselnikStlpecDTO(getDelegate().getCiselnikStlpecRead().readLight(auth, dtoF.getIDCiselnikStlpec()));
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public Set<Integer> ids(AuthInfo auth, Integer pluginID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(pluginID)) {
				return new HashSet<Integer>();
			}

			MyCriteria2 crit = new MyCriteria2(CudPluginStlpecPeer.PLUGIN_STLPEC_ID, new DTOPluginStlpec());

			crit.addSelectColumn(CudPluginStlpecPeer.PLUGIN_STLPEC_ID);

			crit.addConditional(CudPluginStlpecPeer.ID_PLUGIN, pluginID);

			crit.add(CudPluginStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudPluginStlpecPeer.PLUGIN_STLPEC_ID).asIntegerObj());
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}

	public Map<Integer, List<DTOPluginStlpec>> map(AuthInfo auth, Integer[] pluginIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(pluginIDs)) {
			return new HashMap<Integer, List<DTOPluginStlpec>>();
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudPluginStlpecPeer.PLUGIN_STLPEC_ID, new DTOPluginStlpec());

			crit.addSelectColumn(CudPluginStlpecPeer.PLUGIN_STLPEC_ID);
			crit.addSelectColumn(CudPluginStlpecPeer.ID_PLUGIN);
			crit.addSelectColumn(CudPluginStlpecPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudPluginStlpecPeer.ID_PLUGIN_ALIAS);
			crit.addSelectColumn(CudPluginStlpecPeer.HODNOTA);

			crit.addSelectColumn(CudPluginAliasPeer.NAZOV_ALIASU);
			crit.addJoin(CudPluginStlpecPeer.ID_PLUGIN_ALIAS, CudPluginAliasPeer.PLUGIN_ALIAS_ID, MyCriteria2.LEFT_JOIN);

			crit.addAlias("t1", CudCiselnikStlpecPeer.TABLE_NAME);
			crit.addAsColumn("cs_nazov", "t1.nazov");
			crit.addAsColumn("cs_nadpis", "t1.nadpis");
			crit.addJoin(CudPluginStlpecPeer.ID_CISELNIK_STLPEC, "t1.CISELNIK_STLPEC_ID", MyCriteria2.LEFT_JOIN);

			crit.addAlias("t2", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("cis_id", "t2.ciselnik_id");
			crit.addAsColumn("cis_tabulka", "t2.tabulka");
			crit.addAsColumn("cis_nazov", "t2.nazov");
			crit.addJoin("t1.id_ciselnik", "t2.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			if (pluginIDs.length == 1) {
				crit.addConditional(CudPluginStlpecPeer.ID_PLUGIN, pluginIDs[0]);
			} else {
				crit.addIn(CudPluginStlpecPeer.ID_PLUGIN, pluginIDs);
			}

			crit.add(CudPluginStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOPluginStlpec>> mapa = new HashMap<Integer, List<DTOPluginStlpec>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPluginStlpec dto = new DTOPluginStlpec();
				dto.setPluginStlpecID(rVal(r, CudPluginStlpecPeer.PLUGIN_STLPEC_ID).asIntegerObj());
				dto.setIDPlugin(rVal(r, CudPluginStlpecPeer.ID_PLUGIN).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudPluginStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setIDPluginAlias(rVal(r, CudPluginStlpecPeer.ID_PLUGIN_ALIAS).asIntegerObj());
				dto.setHodnota(rVal(r, CudPluginStlpecPeer.HODNOTA).asString());

				dto.setPluginAliasNazovAliasu(rVal(r, CudPluginAliasPeer.NAZOV_ALIASU).asString());

				dto.setCiselnikStlpecNadpis(rVal(r, "cs_nadpis").asString());
				dto.setCiselnikStlpecNazov(rVal(r, "cs_nazov").asString());

				dto.setIDCiselnik(rVal(r, "cis_id").asIntegerObj());
				dto.setCiselnikNazov(rVal(r, "cis_nazov").asString());
				dto.setCiselnikTabulka(rVal(r, "cis_tabulka").asString());

				if (!StringUtils.isValid(mapa.get(dto.getIDPlugin()))) {
					mapa.put(dto.getIDPlugin(), new ArrayList<DTOPluginStlpec>());
				}
				mapa.get(dto.getIDPlugin()).add(dto);
			}

			return mapa;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

}
