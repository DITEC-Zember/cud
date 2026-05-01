package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOPluginClassName;
import sk.ditec.cud.dto.DTOPluginLD;
import sk.ditec.cud.dto.DTOPluginStlpec;
import sk.ditec.cud.plugin.IPlugin;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudPluginClassNamePeer;
import sk.ditec.dao.meta.CudPluginPeer;
import sk.ditec.dao.meta.CudPluginStlpecPeer;

import com.workingdogs.village.Record;

public class CudPluginReadClass extends _CudBaseClass {

	public DTOPlugin[] list(AuthInfo auth, Page page, DTOPlugin dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOPlugin();
			}

			String subSql = null;
			if (StringUtils.isValid(dtoF.getPluginStlpecCiselnikStlpecNadpis())) {

				MyCriteria2 crit = new MyCriteria2(CudPluginStlpecPeer.PLUGIN_STLPEC_ID, new DTOPluginStlpec());

				crit.addSelectColumn(CudPluginStlpecPeer.ID_PLUGIN);

				if (StringUtils.isValid(dtoF.getPluginStlpecCiselnikStlpecNadpis())) {

					crit.addJoin(CudPluginStlpecPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);
					crit.addConditional(CudCiselnikStlpecPeer.NADPIS, dtoF.getPluginStlpecCiselnikStlpecNadpis(), true);
				}

				crit.add(CudPluginStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudPluginPeer.PLUGIN_ID, dtoF);

			crit.addSelectColumn(CudPluginPeer.PLUGIN_ID);
			crit.addSelectColumn(CudPluginPeer.ID_CISELNIK);
			crit.addSelectColumn(CudPluginPeer.ID_PLUGIN_CLASS_NAME);
			crit.addSelectColumn(CudPluginPeer.TYP);
			crit.addSelectColumn(CudPluginPeer.PLATNOST_OD);
			crit.addSelectColumn(CudPluginPeer.PLATNOST_DO);

			crit.addAlias("t1", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("t1_nazov", "t1.NAZOV");
			crit.addJoin(CudPluginPeer.ID_CISELNIK, "t1.CISELNIK_ID", MyCriteria2.LEFT_JOIN);
			crit.addConditional("t1.NAZOV", dtoF.getCiselnikNazov(), true);

			crit.addSelectColumn(CudPluginClassNamePeer.CLASS_NAME);
			crit.addJoin(CudPluginPeer.ID_PLUGIN_CLASS_NAME, CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudPluginClassNamePeer.CLASS_NAME, dtoF.getPluginClassNameClassName(), true);

			crit.addConditional(CudPluginPeer.TYP, dtoF.getTyp(), false);

			if (StringUtils.isValid(subSql)) {
				crit.addCustomSql(CudPluginPeer.PLUGIN_ID, CudPluginPeer.PLUGIN_ID + " IN (" + subSql + ")");
			}

			crit.add(CudPluginPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudPluginPeer.PLUGIN_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOPlugin> listDTO = new ArrayList<DTOPlugin>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPlugin dto = new DTOPlugin();
				dto.setPluginID(rVal(r, CudPluginPeer.PLUGIN_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudPluginPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDPluginClassName(rVal(r, CudPluginPeer.ID_PLUGIN_CLASS_NAME).asIntegerObj());
				dto.setTyp(rVal(r, CudPluginPeer.TYP).asString());
				dto.setPlatnostOd(rVal(r, CudPluginPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudPluginPeer.PLATNOST_DO).asUtilDate());

				dto.setCiselnikNazov(rVal(r, "t1_nazov").asString());

				dto.setPluginClassNameClassName(rVal(r, CudPluginClassNamePeer.CLASS_NAME).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPlugin[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOPlugin> mapLight(AuthInfo auth, Integer[] pluginIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(pluginIDs)) {
				return new HashMap<Integer, DTOPlugin>();
			}

			MyCriteria2 crit = new MyCriteria2(CudPluginPeer.PLUGIN_ID, new DTOPlugin());

			crit.addSelectColumn(CudPluginPeer.PLUGIN_ID);
			crit.addSelectColumn(CudPluginPeer.CAS_ZMENY);
			crit.addSelectColumn(CudPluginPeer.ID_UCET);

			if (pluginIDs.length == 1) {
				crit.addConditional(CudPluginPeer.PLUGIN_ID, pluginIDs[0]);
			} else {
				crit.addIn(CudPluginPeer.PLUGIN_ID, pluginIDs);
			}

			crit.add(CudPluginPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOPlugin> resultMap = new HashMap<Integer, DTOPlugin>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPlugin dto = new DTOPlugin();
				dto.setPluginID(rVal(r, CudPluginPeer.PLUGIN_ID).asIntegerObj());
				dto.setCasZmeny(rVal(r, CudPluginPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudPluginPeer.ID_UCET).asIntegerObj());

				resultMap.put(dto.getPluginID(), dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public DTOPluginLD loadData(AuthInfo auth, DTOPluginLD dtoF) throws AppException {

		try {
			DTOPluginLD resultDTO = new DTOPluginLD();

			if (StringUtils.isValid(dtoF.getPluginID())) {
				Map<Integer, DTOPlugin> mapa = mapLight(auth, new Integer[] { dtoF.getPluginID() });
				resultDTO.setPluginDTO(mapa.get(dtoF.getPluginID()));
			}

			if (StringUtils.isValid(dtoF.getIDCiselnik())) {
				Map<Integer, DTOCiselnik> mapa = getDelegate().getCiselnikRead().mapLight(auth, new Integer[] { dtoF.getIDCiselnik() });
				resultDTO.setCiselnikDTO(mapa.get(dtoF.getIDCiselnik()));
			}

			if (StringUtils.isValid(dtoF.getIDPluginClassName())) {
				Map<Integer, DTOPluginClassName> mapa = getDelegate().getPluginClassNameRead().mapLight(auth, new Integer[] { dtoF.getIDPluginClassName() });
				resultDTO.setPluginClassNameDTO(mapa.get(dtoF.getIDPluginClassName()));
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public String updateKontrola(AuthInfo auth, DTOPlugin dto) throws AppException {

		try {
			if (!StringUtils.isValid(dto.getPluginStlpecList())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_619);
			}

			IPlugin plg = null;
			if (_CudConsts.PLUGIN_TYP_DOPLNENIE.equals(dto.getTyp())) {
				plg = getDelegate().getPluginDoplnenie();
			}

			if (_CudConsts.PLUGIN_TYP_VALIDACNY.equals(dto.getTyp())) {
				plg = (IPlugin) Class.forName(_CudConsts.PLUGIN_PACKAGE + dto.getPluginClassNameClassName()).newInstance();
			}

			List<DTOCiselnikStlpec> csList = getDelegate().getCiselnikStlpecRead().list(auth, dto.getIDCiselnik());

			return plg.updateKontrola(auth, dto.getPluginStlpecList(), csList);

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	public DTOPlugin[] list(AuthInfo auth, Integer ciselnikID, String typ, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudPluginPeer.PLUGIN_ID, new DTOPlugin());

			crit.addSelectColumn(CudPluginPeer.PLUGIN_ID);
			crit.addSelectColumn(CudPluginPeer.TYP);

			crit.addSelectColumn(CudPluginClassNamePeer.CLASS_NAME);
			crit.addJoin(CudPluginPeer.ID_PLUGIN_CLASS_NAME, CudPluginClassNamePeer.PLUGIN_CLASS_NAME_ID, MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudPluginPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudPluginPeer.TYP, typ, false);

			crit.addConditional(CudPluginPeer.PLATNOST_OD, platnostOd, MyCriteria2.LESS_EQUAL);

			Criterion c1 = crit.getNewCriterion(CudPluginPeer.PLATNOST_DO, platnostOd, MyCriteria2.GREATER_EQUAL);
			Criterion c2 = crit.getNewCriterion(CudPluginPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			crit.add(c1.or(c2));

			crit.add(CudPluginPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudPluginPeer.PLUGIN_ID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOPlugin> listDTO = new ArrayList<DTOPlugin>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPlugin dto = new DTOPlugin();
				dto.setPluginID(rVal(r, CudPluginPeer.PLUGIN_ID).asIntegerObj());
				dto.setTyp(rVal(r, CudPluginPeer.TYP).asString());

				dto.setPluginClassNameClassName(rVal(r, CudPluginClassNamePeer.CLASS_NAME).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPlugin[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

}
