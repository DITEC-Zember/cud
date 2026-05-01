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
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudObjektStlpecPeer;

import com.workingdogs.village.Record;

public class CudObjektStlpecReadClass extends _CudBaseClass {

	public DTOObjektStlpec[] list(AuthInfo auth, DTOObjektStlpec dtoF, DTOObjektStlpec dto, DTOObjektStlpec[] data) throws AppException {

		try {
			if (!StringUtils.isValid(dtoF)) {
				dtoF = new DTOObjektStlpec();
			}

			if ("listByCiselnik".equals(dtoF.getOperacia())) {
				List<DTOObjektStlpec> listDTO = listByCiselnik(auth, dtoF.getIDCiselnik());
				for (DTOObjektStlpec dtoOS : listDTO) {
					dtoOS.setZmena("T");
					dtoOS.setBolZmeneny("T");
				}
				return listDTO.toArray(new DTOObjektStlpec[listDTO.size()]);

			} else if ("I".equals(dtoF.getOperacia())) {
				return insertList(auth, dto, data);

			} else if ("U".equals(dtoF.getOperacia())) {
				return updateList(auth, dto, data);

			} else if ("D".equals(dtoF.getOperacia())) {
				return deleteList(auth, dtoF.getObjektStlpecID(), data);

			} else if ("load".equals(dtoF.getOperacia())) {
				return data;

			} else if ("list".equals(dtoF.getOperacia())) {
				return list(auth, dtoF.getIDObjektCiselnik());
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public List<DTOObjektStlpec> listByCiselnik(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return new ArrayList<DTOObjektStlpec>();
			}

			List<DTOCiselnikStlpec> list = getDelegate().getCiselnikStlpecRead().listLight(auth, ciselnikID);

			List<DTOObjektStlpec> listDTO = new ArrayList<DTOObjektStlpec>();

			int pocet = -1;
			for (DTOCiselnikStlpec dto : list) {

				if (_CudKontrolaUtils.jeAtributTechnicky(dto.getNazov())) {
					continue;
				}

				DTOObjektStlpec dtoNew = new DTOObjektStlpec();
				dtoNew.setObjektStlpecID(pocet--);
				dtoNew.setIDCiselnikStlpec(dto.getCiselnikStlpecID());
				dtoNew.setCiselnikStlpecNadpis(dto.getNadpis());
				dtoNew.setCiselnikStlpecNazov(dto.getNazov());
				listDTO.add(dtoNew);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listByCiselnik.error", auth);
			return null;
		}
	}

	public Set<Integer> ciselnikStlpecIDsByCiselnik(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return new HashSet<Integer>();
			}

			List<DTOCiselnikStlpec> list = getDelegate().getCiselnikStlpecRead().listLight(auth, ciselnikID);

			Set<Integer> set = new HashSet<Integer>();

			for (DTOCiselnikStlpec dto : list) {

				if (!_CudKontrolaUtils.jeAtributTechnicky(dto.getNazov())) {
					set.add(dto.getCiselnikStlpecID());
				}
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "ciselnikStlpecIDsByCiselnik.error", auth);
			return null;
		}
	}

	private DTOObjektStlpec[] insertList(AuthInfo auth, DTOObjektStlpec dto, DTOObjektStlpec[] dataList) throws AppException {

		try {
			List<DTOObjektStlpec> listDTO = new ArrayList<DTOObjektStlpec>(Arrays.asList(dataList));
			listDTO.add(dto);
			return listDTO.toArray(new DTOObjektStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "insertList.error", auth);
			return null;
		}
	}

	private DTOObjektStlpec[] updateList(AuthInfo auth, DTOObjektStlpec dto, DTOObjektStlpec[] dataList) throws AppException {

		try {
			List<DTOObjektStlpec> listDTO = new ArrayList<DTOObjektStlpec>();

			for (DTOObjektStlpec dtoItem : dataList) {
				if (dtoItem.getObjektStlpecID().intValue() == dto.getObjektStlpecID().intValue()) {
					listDTO.add(dto);
				} else {
					listDTO.add(dtoItem);
				}
			}

			return listDTO.toArray(new DTOObjektStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "updateList.error", auth);
			return null;
		}
	}

	private DTOObjektStlpec[] deleteList(AuthInfo auth, Integer objektStlpecID, DTOObjektStlpec[] dataList) throws AppException {

		try {
			List<DTOObjektStlpec> listDTO = new ArrayList<DTOObjektStlpec>();

			for (DTOObjektStlpec dtoItem : dataList) {
				if (dtoItem.getObjektStlpecID().intValue() != objektStlpecID.intValue()) {
					listDTO.add(dtoItem);
				}
			}

			return listDTO.toArray(new DTOObjektStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "deleteList.error", auth);
			return null;
		}
	}

	public Map<Integer, List<DTOObjektStlpec>> map(AuthInfo auth, Integer[] objektCiselnikIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(objektCiselnikIDs)) {
				return new HashMap<Integer, List<DTOObjektStlpec>>();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektStlpecPeer.OBJEKT_STLPEC_ID, new DTOObjektStlpec());

			crit.addSelectColumn(CudObjektStlpecPeer.OBJEKT_STLPEC_ID);
			crit.addSelectColumn(CudObjektStlpecPeer.ID_OBJEKT_CISELNIK);
			crit.addSelectColumn(CudObjektStlpecPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudObjektStlpecPeer.ZMENA);
			crit.addSelectColumn(CudObjektStlpecPeer.HODNOTA);

			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addJoin(CudObjektStlpecPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);

			if (objektCiselnikIDs.length == 1) {
				crit.addConditional(CudObjektStlpecPeer.ID_OBJEKT_CISELNIK, objektCiselnikIDs[0]);
			} else {
				crit.addIn(CudObjektStlpecPeer.ID_OBJEKT_CISELNIK, objektCiselnikIDs);
			}

			crit.add(CudObjektStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOObjektStlpec>> resultMap = new HashMap<Integer, List<DTOObjektStlpec>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjektStlpec dto = new DTOObjektStlpec();
				dto.setObjektStlpecID(rVal(r, CudObjektStlpecPeer.OBJEKT_STLPEC_ID).asIntegerObj());
				dto.setIDObjektCiselnik(rVal(r, CudObjektStlpecPeer.ID_OBJEKT_CISELNIK).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudObjektStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setZmena(rVal(r, CudObjektStlpecPeer.ZMENA).asString());
				dto.setHodnota(rVal(r, CudObjektStlpecPeer.HODNOTA).asString());

				dto.setCiselnikStlpecNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());
				dto.setCiselnikStlpecNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());

				if (!StringUtils.isValid(resultMap.get(dto.getIDObjektCiselnik()))) {
					resultMap.put(dto.getIDObjektCiselnik(), new ArrayList<DTOObjektStlpec>());
				}
				resultMap.get(dto.getIDObjektCiselnik()).add(dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "map.error", auth);
			return null;
		}
	}

	public DTOObjektStlpec[] list(AuthInfo auth, Integer objektCiselnikID) throws AppException {

		try {
			Map<Integer, List<DTOObjektStlpec>> mapDTO = map(auth, new Integer[] { objektCiselnikID });
			List<DTOObjektStlpec> listDTO = StringUtils.isValid(objektCiselnikID) ? mapDTO.get(objektCiselnikID) : null;
			if (!StringUtils.isValid(listDTO)) {
				listDTO = new ArrayList<DTOObjektStlpec>();
			}
			return listDTO.toArray(new DTOObjektStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public List<DTOObjektStlpec> list(AuthInfo auth, Integer[] objektCiselnikIDs) throws AppException {

		try {
			List<DTOObjektStlpec> resultList = new ArrayList<DTOObjektStlpec>();

			Map<Integer, List<DTOObjektStlpec>> mapDTO = map(auth, objektCiselnikIDs);
			for (Integer objektCiselnikID : objektCiselnikIDs) {
				List<DTOObjektStlpec> list = mapDTO.get(objektCiselnikID);
				if (StringUtils.isValid(list) && !list.isEmpty()) {
					resultList.addAll(list);
				}
			}

			return resultList;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Set<Integer> ids(AuthInfo auth, Integer objektCiselnikID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(objektCiselnikID)) {
				return new HashSet<Integer>();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektStlpecPeer.OBJEKT_STLPEC_ID, new DTOObjektCiselnik());

			crit.addSelectColumn(CudObjektStlpecPeer.OBJEKT_STLPEC_ID);

			crit.addConditional(CudObjektStlpecPeer.ID_OBJEKT_CISELNIK, objektCiselnikID);

			crit.add(CudObjektStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudObjektStlpecPeer.OBJEKT_STLPEC_ID).asIntegerObj());
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}

	public Set<Integer> ciselnikStlpecIDsSet(AuthInfo auth, Integer[] objektCiselnikIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(objektCiselnikIDs)) {
				return new HashSet<Integer>();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektStlpecPeer.OBJEKT_STLPEC_ID, new DTOObjektStlpec());

			crit.setDistinct();

			crit.addSelectColumn(CudObjektStlpecPeer.ID_CISELNIK_STLPEC);

			if (objektCiselnikIDs.length == 1) {
				crit.addConditional(CudObjektStlpecPeer.ID_OBJEKT_CISELNIK, objektCiselnikIDs[0]);
			} else {
				crit.addIn(CudObjektStlpecPeer.ID_OBJEKT_CISELNIK, objektCiselnikIDs);
			}

			crit.add(CudObjektStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudObjektStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "ciselnikStlpecIDsSet.error", auth);
			return null;
		}
	}

}
