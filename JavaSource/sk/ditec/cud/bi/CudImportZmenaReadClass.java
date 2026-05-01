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
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudImportMsgPeer;
import sk.ditec.dao.meta.CudImportZmenaPeer;
import sk.ditec.dao.meta.CudImportZmenaStlpecPeer;

import com.workingdogs.village.Record;

public class CudImportZmenaReadClass extends _CudBaseClass {

	public DTOImportZmena[] list(AuthInfo auth, Page page, DTOImportZmena dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOImportZmena();
			}

			String subSqlError = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudImportMsgPeer.IMPORT_MSG_ID, new DTOImportMsg());

				crit.addAsColumn("pocet_err_msg", "count(*)");

				crit.addConditional(CudImportMsgPeer.ID_IMPORT, dtoF.getIDImport());
				crit.addConditional(CudImportMsgPeer.ID_IMPORT_ZMENA, 1000);
				crit.addConditional(CudImportMsgPeer.TYP, _CudConsts.IMPORT_MSG_TYP_ERROR, false);

				subSqlError = crit.getSQL();
				subSqlError = StringUtils.replaceAll(subSqlError, "1000", CudImportZmenaPeer.IMPORT_ZMENA_ID);
			}

			String subSqlWarrning = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudImportMsgPeer.IMPORT_MSG_ID, new DTOImportMsg());

				crit.addAsColumn("pocet_warr_msg", "count(*)");

				crit.addConditional(CudImportMsgPeer.ID_IMPORT, dtoF.getIDImport());
				crit.addConditional(CudImportMsgPeer.ID_IMPORT_ZMENA, 1000);
				crit.addConditional(CudImportMsgPeer.TYP, _CudConsts.IMPORT_MSG_TYP_WARNING, false);

				subSqlWarrning = crit.getSQL();
				subSqlWarrning = StringUtils.replaceAll(subSqlWarrning, "1000", CudImportZmenaPeer.IMPORT_ZMENA_ID);
			}

			String subSqlColumns = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID, new DTOImportZmenaStlpec());

				crit.addAsColumn("pocet_zmena_stlpec", "count(*)");

				crit.addConditional(CudImportZmenaStlpecPeer.ID_IMPORT, dtoF.getIDImport());
				crit.addConditional(CudImportZmenaStlpecPeer.ID_IMPORT_ZMENA, 1000);

				subSqlColumns = crit.getSQL();
				subSqlColumns = StringUtils.replaceAll(subSqlColumns, "1000", CudImportZmenaPeer.IMPORT_ZMENA_ID);
			}

			MyCriteria2 crit = new MyCriteria2(CudImportZmenaPeer.IMPORT_ZMENA_ID, dtoF);

			crit.addSelectColumn(CudImportZmenaPeer.IMPORT_ZMENA_ID);
			crit.addSelectColumn(CudImportZmenaPeer.ID_IMPORT);
			crit.addSelectColumn(CudImportZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudImportZmenaPeer.XLS_ROW_ID);
			crit.addSelectColumn(CudImportZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudImportZmenaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudImportZmenaPeer.CAS_SCHVALENIA_GR);
			crit.addSelectColumn(CudImportZmenaPeer.POZNAMKA);
			crit.addSelectColumn(CudImportZmenaPeer.SPRACOVANY);

			crit.addAsColumn("err_pocet", "(" + subSqlError + ")");
			crit.addAsColumn("warr_pocet", "(" + subSqlWarrning + ")");
			crit.addAsColumn("cols_pocet", "(" + subSqlColumns + ")");

			crit.add(CudImportZmenaPeer.ID_IMPORT, dtoF.getIDImport());

			String sql = "SELECT t.*, decode(err_pocet, 0, \'F\', \'T\') AS errors, decode(warr_pocet, 0, \'F\', \'T\') AS warnings, decode(cols_pocet, 0, \'F\', \'T\') AS cols FROM ( "
					+ crit.getSQL() + " ) t";

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudImportZmenaPeer.IMPORT_ZMENA_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOImportZmena> listDTO = new ArrayList<DTOImportZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOImportZmena dto = new DTOImportZmena();
				dto.setImportZmenaID(rVal(r, CudImportZmenaPeer.IMPORT_ZMENA_ID).asIntegerObj());
				dto.setIDImport(rVal(r, CudImportZmenaPeer.ID_IMPORT).asIntegerObj());
				dto.setRowID(rVal(r, CudImportZmenaPeer.ROW_ID).asIntegerObj());
				dto.setXlsRowID(rVal(r, CudImportZmenaPeer.XLS_ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudImportZmenaPeer.OPERACIA).asString());
				dto.setPlatnostOd(rVal(r, CudImportZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setCasSchvaleniaGr(rVal(r, CudImportZmenaPeer.CAS_SCHVALENIA_GR).asUtilDate());
				dto.setPoznamka(rVal(r, CudImportZmenaPeer.POZNAMKA).asString());
				dto.setSpracovany(rVal(r, CudImportZmenaPeer.SPRACOVANY).asString());

				dto.setErrors(rVal(r, "errors").asString());
				dto.setWarnings(rVal(r, "warnings").asString());
				dto.setColumns(rVal(r, "cols").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOImportZmena[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTOImportZmena[] listLight(AuthInfo auth, Page page, Integer importID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(importID)) {
			return new DTOImportZmena[0];
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudImportZmenaPeer.IMPORT_ZMENA_ID, new DTOImportZmena());

			crit.addSelectColumn(CudImportZmenaPeer.IMPORT_ZMENA_ID);
			crit.addSelectColumn(CudImportZmenaPeer.ID_IMPORT);
			crit.addSelectColumn(CudImportZmenaPeer.ROW_ID);
			crit.addSelectColumn(CudImportZmenaPeer.XLS_ROW_ID);
			crit.addSelectColumn(CudImportZmenaPeer.OPERACIA);
			crit.addSelectColumn(CudImportZmenaPeer.PLATNOST_OD);
			crit.addSelectColumn(CudImportZmenaPeer.CAS_SCHVALENIA_GR);
			crit.addSelectColumn(CudImportZmenaPeer.POZNAMKA);
			crit.addSelectColumn(CudImportZmenaPeer.SPRACOVANY);
			crit.addSelectColumn(CudImportZmenaPeer.OBNOVA);

			crit.addConditional(CudImportZmenaPeer.ID_IMPORT, importID);
			crit.addConditional(CudImportZmenaPeer.SPRACOVANY, "F", false);

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudImportZmenaPeer.IMPORT_ZMENA_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOImportZmena> listDTO = new ArrayList<DTOImportZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOImportZmena dto = new DTOImportZmena();
				dto.setImportZmenaID(rVal(r, CudImportZmenaPeer.IMPORT_ZMENA_ID).asIntegerObj());
				dto.setIDImport(rVal(r, CudImportZmenaPeer.ID_IMPORT).asIntegerObj());
				dto.setRowID(rVal(r, CudImportZmenaPeer.ROW_ID).asIntegerObj());
				dto.setXlsRowID(rVal(r, CudImportZmenaPeer.XLS_ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudImportZmenaPeer.OPERACIA).asString());
				dto.setPlatnostOd(rVal(r, CudImportZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setCasSchvaleniaGr(rVal(r, CudImportZmenaPeer.CAS_SCHVALENIA_GR).asUtilDate());
				dto.setPoznamka(rVal(r, CudImportZmenaPeer.POZNAMKA).asString());
				dto.setSpracovany(rVal(r, CudImportZmenaPeer.SPRACOVANY).asString());
				dto.setObnova(rVal(r, CudImportZmenaPeer.OBNOVA).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOImportZmena[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public Integer pocet(AuthInfo auth, Integer importID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(importID)) {
			return 0;
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudImportZmenaPeer.IMPORT_ZMENA_ID, new DTOImportZmena());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudImportZmenaPeer.ID_IMPORT, importID);
			crit.addConditional(CudImportZmenaPeer.SPRACOVANY, "F", false);

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
			handleException(t, "pocet.error", auth);
			return null;
		}
	}

	public boolean existXlsRowID(AuthInfo auth, Integer importID, Integer xlsRowID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(importID) || !StringUtils.isValid(xlsRowID)) {
			return false;
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudImportZmenaPeer.IMPORT_ZMENA_ID, new DTOImportZmena());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudImportZmenaPeer.ID_IMPORT, importID);
			crit.addConditional(CudImportZmenaPeer.XLS_ROW_ID, xlsRowID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Integer pocet = 0;

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				pocet = rVal(r, "pocet").asIntegerObj();
			}

			return pocet.intValue() != 0;

		} catch (Throwable t) {
			handleException(t, "xlsRowIds.error", auth);
			return false;
		}
	}

	public Set<Integer> ids(AuthInfo auth, Page page, Integer importID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		if (!StringUtils.isValid(importID)) {
			return new HashSet<Integer>();
		}

		try {
			MyCriteria2 crit = new MyCriteria2(CudImportZmenaPeer.IMPORT_ZMENA_ID, new DTOImportZmena());

			crit.addSelectColumn(CudImportZmenaPeer.IMPORT_ZMENA_ID);

			crit.addConditional(CudImportZmenaPeer.ID_IMPORT, importID);

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudImportZmenaPeer.IMPORT_ZMENA_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			Set<Integer> resultSet = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				resultSet.add(rVal(r, CudImportZmenaPeer.IMPORT_ZMENA_ID).asIntegerObj());
			}

			return resultSet;

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}

}
