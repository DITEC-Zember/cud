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
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfDefCiselnikStlpec;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudWfDefCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudWfDefPeer;

import com.workingdogs.village.Record;

public class CudWfDefCiselnikStlpecReadClass extends _CudBaseClass {

	public Map<Integer, List<DTOWfDefCiselnikStlpec>> mapByCiselnik(AuthInfo auth, Integer ciselnikID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return new HashMap<Integer, List<DTOWfDefCiselnikStlpec>>();
			}

			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDef());

				crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);

				crit.addConditional(CudWfDefPeer.ID_CISELNIK, ciselnikID);

				crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID, new DTOWfDefCiselnikStlpec());

			crit.addSelectColumn(CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudWfDefCiselnikStlpecPeer.ID_WF_DEF);
			crit.addSelectColumn(CudWfDefCiselnikStlpecPeer.ID_CISELNIK_STLPEC);

			// join CUC_CISELNIK_STLPEC
			crit.addAsColumn("cs_nazov", CudCiselnikStlpecPeer.NAZOV);
			crit.addJoin(CudWfDefCiselnikStlpecPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);

			crit.addCustomSql(CudWfDefCiselnikStlpecPeer.ID_WF_DEF, CudWfDefCiselnikStlpecPeer.ID_WF_DEF + " IN (" + subSql + ")");

			crit.add(CudWfDefCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOWfDefCiselnikStlpec>> resultMap = new HashMap<Integer, List<DTOWfDefCiselnikStlpec>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfDefCiselnikStlpec dto = new DTOWfDefCiselnikStlpec();
				dto.setWfDefCiselnikStlpecID(rVal(r, CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setIDWfDef(rVal(r, CudWfDefCiselnikStlpecPeer.ID_WF_DEF).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudWfDefCiselnikStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());

				dto.setCiselnikStlpecNazov(rVal(r, "cs_nazov").asString());

				if (!StringUtils.isValid(resultMap.get(dto.getIDWfDef()))) {
					resultMap.put(dto.getIDWfDef(), new ArrayList<DTOWfDefCiselnikStlpec>());
				}
				resultMap.get(dto.getIDWfDef()).add(dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "mapByCiselnik.error", auth);
			return null;
		}
	}

	public DTOWfDefCiselnikStlpec[] list(AuthInfo auth, Integer wfDefID) throws AppException {

		try {
			if (!StringUtils.isValid(wfDefID)) {
				return new DTOWfDefCiselnikStlpec[0];
			}

			DTOWfDefCiselnikStlpec dtoF = new DTOWfDefCiselnikStlpec();
			dtoF.setIDWfDef(wfDefID);
			return list(auth, dtoF);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	private DTOWfDefCiselnikStlpec[] list(AuthInfo auth, DTOWfDefCiselnikStlpec dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOWfDefCiselnikStlpec();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID, dtoF);

			crit.addSelectColumn(CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudWfDefCiselnikStlpecPeer.ID_WF_DEF);
			crit.addSelectColumn(CudWfDefCiselnikStlpecPeer.ID_CISELNIK_STLPEC);

			// join CUC_CISELNIK_STLPEC
			crit.addAsColumn("cs_nazov", CudCiselnikStlpecPeer.NAZOV);
			crit.addAsColumn("cs_nadpis", CudCiselnikStlpecPeer.NADPIS);
			crit.addJoin(CudWfDefCiselnikStlpecPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID, dtoF.getWfDefCiselnikStlpecID());
			crit.addConditional(CudWfDefCiselnikStlpecPeer.ID_WF_DEF, dtoF.getIDWfDef());
			crit.addConditional(CudWfDefCiselnikStlpecPeer.ID_CISELNIK_STLPEC, dtoF.getIDCiselnikStlpec());

			crit.add(CudWfDefCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOWfDefCiselnikStlpec> listDTO = new ArrayList<DTOWfDefCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfDefCiselnikStlpec dto = new DTOWfDefCiselnikStlpec();
				dto.setWfDefCiselnikStlpecID(rVal(r, CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setIDWfDef(rVal(r, CudWfDefCiselnikStlpecPeer.ID_WF_DEF).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudWfDefCiselnikStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());

				dto.setCiselnikStlpecNazov(rVal(r, "cs_nazov").asString());
				dto.setCiselnikStlpecNadpis(rVal(r, "cs_nadpis").asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOWfDefCiselnikStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	private DTOWfDefCiselnikStlpec[] insertList(AuthInfo auth, DTOWfDefCiselnikStlpec dto, DTOWfDefCiselnikStlpec[] dataList) throws AppException {

		try {
			int minValue = -100;
			if (StringUtils.isValid(dataList)) {
				for (DTOWfDefCiselnikStlpec dtoItem : dataList) {
					if (dtoItem.getWfDefCiselnikStlpecID().intValue() < minValue) {
						minValue = dtoItem.getWfDefCiselnikStlpecID();
					}
				}
			}
			dto.setWfDefCiselnikStlpecID(--minValue);

			List<DTOWfDefCiselnikStlpec> listDTO = new ArrayList<DTOWfDefCiselnikStlpec>(Arrays.asList(dataList));
			listDTO.add(dto);
			return listDTO.toArray(new DTOWfDefCiselnikStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "insertList.error", auth);
			return null;
		}
	}

	private DTOWfDefCiselnikStlpec[] deleteList(AuthInfo auth, Integer wfDefCiselnikStlpecID, DTOWfDefCiselnikStlpec[] dataList) throws AppException {

		try {
			List<DTOWfDefCiselnikStlpec> listDTO = new ArrayList<DTOWfDefCiselnikStlpec>();

			for (DTOWfDefCiselnikStlpec dtoItem : dataList) {
				if (dtoItem.getWfDefCiselnikStlpecID().intValue() != wfDefCiselnikStlpecID.intValue()) {
					listDTO.add(dtoItem);
				}
			}

			return listDTO.toArray(new DTOWfDefCiselnikStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "deleteList.error", auth);
			return null;
		}
	}

	public DTOWfDefCiselnikStlpec[] list(AuthInfo auth, DTOWfDefCiselnikStlpec dtoF, DTOWfDefCiselnikStlpec dto, DTOWfDefCiselnikStlpec[] data) throws AppException {

		try {
			if (!StringUtils.isValid(dtoF)) {
				dtoF = new DTOWfDefCiselnikStlpec();
			}

			if ("I".equals(dtoF.getOperacia())) {
				return insertList(auth, dto, data);

			} else if ("D".equals(dtoF.getOperacia())) {
				return deleteList(auth, dtoF.getWfDefCiselnikStlpecID(), data);

			} else if ("load".equals(dtoF.getOperacia())) {
				return data;

			} else if ("list".equals(dtoF.getOperacia())) {
				return list(auth, dtoF.getIDWfDef());
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Set<Integer> ids(AuthInfo auth, Integer wfDefID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(wfDefID)) {
				new HashSet<Integer>();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDefCiselnikStlpec());

			crit.addSelectColumn(CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID);

			crit.addConditional(CudWfDefCiselnikStlpecPeer.ID_WF_DEF, wfDefID);

			crit.add(CudWfDefCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> resultSet = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				resultSet.add(rVal(r, CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID).asIntegerObj());
			}

			return resultSet;

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}

}
