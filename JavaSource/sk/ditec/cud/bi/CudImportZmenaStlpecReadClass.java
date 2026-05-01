package sk.ditec.cud.bi;

import java.nio.charset.Charset;
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
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOImportZmenaStlpecPriloha;
import sk.ditec.dao.meta.CudImportPeer;
import sk.ditec.dao.meta.CudImportZmenaStlpecPeer;
import sk.ditec.dao.meta.CudImportZmenaStlpecPrilohaPeer;

import com.workingdogs.village.Record;

public class CudImportZmenaStlpecReadClass extends _CudBaseClass {

	public DTOImportZmenaStlpec[] list(AuthInfo auth, Page page, DTOImportZmenaStlpec dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOImportZmenaStlpec();
			}

			MyCriteria2 crit = new MyCriteria2(CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID, dtoF);

			crit.addSelectColumn(CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.ID_IMPORT);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.CISELNIK_STLPEC_NAZOV);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.OLD_VALUE);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.NEW_VALUE);

			crit.addSelectColumn(CudImportPeer.CISELNIK_TABULKA);
			crit.addJoin(CudImportZmenaStlpecPeer.ID_IMPORT, CudImportPeer.IMPORT_ID, MyCriteria2.LEFT_JOIN);

			crit.addSelectColumn(CudImportZmenaStlpecPrilohaPeer.OLD_VALUE_PRILOHA);
			crit.addSelectColumn(CudImportZmenaStlpecPrilohaPeer.NEW_VALUE_PRILOHA);
			crit.addJoin(CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA_STLPEC_PRILOHA, CudImportZmenaStlpecPrilohaPeer.IMPORT_ZMENA_STLPEC_PRILOHA_ID, MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID, dtoF.getImportZmenaStlpecID());
			crit.addConditional(CudImportZmenaStlpecPeer.ID_IMPORT, dtoF.getIDImport());
			crit.addConditional(CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA, dtoF.getIDImportZmena());
			crit.addConditional(CudImportZmenaStlpecPeer.ID_CISELNIK_STLPEC, dtoF.getIDCiselnikStlpec());
			crit.addConditional(CudImportZmenaStlpecPeer.CISELNIK_STLPEC_NAZOV, dtoF.getCiselnikStlpecNazov(), false);
			crit.addConditional(CudImportZmenaStlpecPeer.OLD_VALUE, dtoF.getOldValue(), false);
			crit.addConditional(CudImportZmenaStlpecPeer.NEW_VALUE, dtoF.getNewValue(), false);

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOImportZmenaStlpec> listDTO = new ArrayList<DTOImportZmenaStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOImportZmenaStlpec dto = new DTOImportZmenaStlpec();
				dto.setImportZmenaStlpecID(rVal(r, CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID).asIntegerObj());
				dto.setIDImport(rVal(r, CudImportZmenaStlpecPeer.ID_IMPORT).asIntegerObj());
				dto.setIDImportZmena(rVal(r, CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudImportZmenaStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setCiselnikStlpecNazov(rVal(r, CudImportZmenaStlpecPeer.CISELNIK_STLPEC_NAZOV).asString());
				dto.setOldValue(rVal(r, CudImportZmenaStlpecPeer.OLD_VALUE).asString());
				dto.setNewValue(rVal(r, CudImportZmenaStlpecPeer.NEW_VALUE).asString());

				byte[] priloha = rVal(r, CudImportZmenaStlpecPrilohaPeer.OLD_VALUE_PRILOHA).asBytes();
				if (StringUtils.isValid(priloha) && priloha.length != 0) {
					dto.setOldValue(new String(priloha, Charset.forName("UTF-8")));
				}

				priloha = rVal(r, CudImportZmenaStlpecPrilohaPeer.NEW_VALUE_PRILOHA).asBytes();
				if (StringUtils.isValid(priloha) && priloha.length != 0) {
					dto.setNewValue(new String(priloha, Charset.forName("UTF-8")));
				}

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOImportZmenaStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Map<Integer, List<DTOImportZmenaStlpec>> map(AuthInfo auth, Integer importID, Integer[] importZmenaIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(importID) || !StringUtils.isValid(importZmenaIDs)) {
			return new HashMap<Integer, List<DTOImportZmenaStlpec>>();
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID, new DTOImportZmenaStlpec());

			crit.addSelectColumn(CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.ID_IMPORT);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.CISELNIK_STLPEC_NAZOV);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.OLD_VALUE);
			crit.addSelectColumn(CudImportZmenaStlpecPeer.NEW_VALUE);

			crit.addSelectColumn(CudImportZmenaStlpecPrilohaPeer.OLD_VALUE_PRILOHA);
			crit.addSelectColumn(CudImportZmenaStlpecPrilohaPeer.NEW_VALUE_PRILOHA);
			crit.addJoin(CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA_STLPEC_PRILOHA, CudImportZmenaStlpecPrilohaPeer.IMPORT_ZMENA_STLPEC_PRILOHA_ID, MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudImportZmenaStlpecPeer.ID_IMPORT, importID);

			if (importZmenaIDs.length == 1) {
				crit.addConditional(CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA, importZmenaIDs[0]);
			} else {
				crit.addIn(CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA, importZmenaIDs);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOImportZmenaStlpec>> resultMap = new HashMap<Integer, List<DTOImportZmenaStlpec>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOImportZmenaStlpec dto = new DTOImportZmenaStlpec();
				dto.setImportZmenaStlpecID(rVal(r, CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID).asIntegerObj());
				dto.setIDImport(rVal(r, CudImportZmenaStlpecPeer.ID_IMPORT).asIntegerObj());
				dto.setIDImportZmena(rVal(r, CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudImportZmenaStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setCiselnikStlpecNazov(rVal(r, CudImportZmenaStlpecPeer.CISELNIK_STLPEC_NAZOV).asString());
				dto.setOldValue(rVal(r, CudImportZmenaStlpecPeer.OLD_VALUE).asString());
				dto.setNewValue(rVal(r, CudImportZmenaStlpecPeer.NEW_VALUE).asString());

				byte[] priloha = rVal(r, CudImportZmenaStlpecPrilohaPeer.OLD_VALUE_PRILOHA).asBytes();
				if (StringUtils.isValid(priloha) && priloha.length != 0) {
					dto.setOldValue(new String(priloha, Charset.forName("UTF-8")));
				}

				priloha = rVal(r, CudImportZmenaStlpecPrilohaPeer.NEW_VALUE_PRILOHA).asBytes();
				if (StringUtils.isValid(priloha) && priloha.length != 0) {
					dto.setNewValue(new String(priloha, Charset.forName("UTF-8")));
				}

				if (!StringUtils.isValid(resultMap.get(dto.getIDImportZmena()))) {
					resultMap.put(dto.getIDImportZmena(), new ArrayList<DTOImportZmenaStlpec>());
				}
				resultMap.get(dto.getIDImportZmena()).add(dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "map.error", auth);
			return null;
		}
	}

	public Set<Integer> ids(AuthInfo auth, Page page, Integer importID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(importID)) {
			return new HashSet<Integer>();
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID, new DTOImportZmenaStlpec());

			crit.addSelectColumn(CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID);

			crit.addConditional(CudImportZmenaStlpecPeer.ID_IMPORT, importID);

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			Set<Integer> resultSet = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				resultSet.add(rVal(r, CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID).asIntegerObj());
			}

			return resultSet;

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}

	public Set<Integer> prilohaIds(AuthInfo auth, Page page, Integer importID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(importID)) {
			return new HashSet<Integer>();
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudImportZmenaStlpecPrilohaPeer.IMPORT_ZMENA_STLPEC_PRILOHA_ID, new DTOImportZmenaStlpecPriloha());

			crit.addSelectColumn(CudImportZmenaStlpecPrilohaPeer.IMPORT_ZMENA_STLPEC_PRILOHA_ID);

			crit.addConditional(CudImportZmenaStlpecPrilohaPeer.ID_IMPORT, importID);

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudImportZmenaStlpecPrilohaPeer.IMPORT_ZMENA_STLPEC_PRILOHA_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			Set<Integer> resultSet = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				resultSet.add(rVal(r, CudImportZmenaStlpecPrilohaPeer.IMPORT_ZMENA_STLPEC_PRILOHA_ID).asIntegerObj());
			}

			return resultSet;

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}
}
