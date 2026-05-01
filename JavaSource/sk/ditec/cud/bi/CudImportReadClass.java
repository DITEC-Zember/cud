package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudImportMsgPeer;
import sk.ditec.dao.meta.CudImportPeer;
import sk.ditec.dao.meta.CudImportPrilohaPeer;
import sk.ditec.dao.meta.CudImportZmenaPeer;

import com.workingdogs.village.Record;

public class CudImportReadClass extends _CudBaseClass {

	public DTOImport[] list(AuthInfo auth, Page page, DTOImport dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOImport();
			}

			String subSqlErr = "";
			{
				MyCriteria2 crit = new MyCriteria2(CudImportMsgPeer.IMPORT_MSG_ID, new DTOImportMsg());

				crit.addAsColumn("pocet", "count(*)");

				crit.addConditional(CudImportMsgPeer.ID_IMPORT, 1000);
				crit.addConditional(CudImportMsgPeer.TYP, _CudConsts.IMPORT_MSG_TYP_ERROR, false);

				crit.add(CudImportMsgPeer.ID_IMPORT_ZMENA, (Object) null, MyCriteria2.ISNULL);

				subSqlErr = crit.getSQL();
				subSqlErr = StringUtils.replaceAll(subSqlErr, "1000", CudImportPeer.IMPORT_ID);
			}

			String subSqlZmeny = "";
			{
				MyCriteria2 crit = new MyCriteria2(CudImportZmenaPeer.IMPORT_ZMENA_ID, new DTOImportZmena());

				crit.addAsColumn("pocet", "count(*)");

				crit.addConditional(CudImportZmenaPeer.ID_IMPORT, 1000);

				subSqlZmeny = crit.getSQL();
				subSqlZmeny = StringUtils.replaceAll(subSqlZmeny, "1000", CudImportPeer.IMPORT_ID);
			}

			MyCriteria2 crit = new MyCriteria2(CudImportPeer.IMPORT_ID, dtoF);

			crit.addSelectColumn(CudImportPeer.IMPORT_ID);
			crit.addSelectColumn(CudImportPeer.ID_CISELNIK);
			crit.addSelectColumn(CudImportPeer.CISELNIK_TABULKA);
			crit.addSelectColumn(CudImportPeer.STAV);
			crit.addSelectColumn(CudImportPeer.CAS_VYTVORENIA);
			crit.addSelectColumn(CudImportPeer.CAS_KONTROLA_ZAC);
			crit.addSelectColumn(CudImportPeer.CAS_KONTROLA_KON);
			crit.addSelectColumn(CudImportPeer.CAS_IMPORT_ZAC);
			crit.addSelectColumn(CudImportPeer.CAS_IMPORT_KON);

			crit.addSelectColumn(CudImportPrilohaPeer.FILE_NAME);
			crit.addJoin(CudImportPeer.IMPORT_ID, CudImportPrilohaPeer.ID_IMPORT, MyCriteria2.LEFT_JOIN);

			crit.addAsColumn("err_pocet", "(" + subSqlErr + ")");
			crit.addAsColumn("zmena_pocet", "(" + subSqlZmeny + ")");

			crit.addConditional(CudImportPeer.STAV, dtoF.getStav(), false);

			crit.add(CudImportPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String colSort = "CASE";
			colSort += " WHEN stav = \'V\' THEN \'vlozeny\'";
			colSort += " WHEN stav = \'K\' AND cas_kontrola_zac IS NOT NULL AND cas_kontrola_kon IS NULL THEN \'KP\'";
			colSort += " WHEN stav = \'K\' AND cas_kontrola_kon IS NOT NULL THEN \'KUU\'";
			colSort += " WHEN stav = \'I\' AND cas_import_zac IS NULL AND cas_import_kon IS NULL THEN \'I\'";
			colSort += " WHEN stav = \'I\' AND cas_import_zac IS NOT NULL AND cas_import_kon IS NULL THEN \'IP\'";
			colSort += " WHEN stav = \'I\' AND cas_import_kon IS NOT NULL THEN \'IPU\'";
			colSort += " WHEN stav = \'E\' AND cas_import_kon IS NOT NULL THEN \'IPS\'";
			colSort += " WHEN stav = \'E\' AND cas_kontrola_kon IS NOT NULL THEN \'KUS\'";
			colSort += " ELSE null";
			colSort += " END AS stav_sort";

			String sql = "SELECT t.*, decode(err_pocet, 0, \'F\', \'T\') AS errors, decode(zmena_pocet, 0, \'F\', \'T\') AS cols, " + colSort + " FROM ( " + crit.getSQL() + ") t";

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudImportPeer.IMPORT_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOImport> listDTO = new ArrayList<DTOImport>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOImport dto = new DTOImport();
				dto.setImportID(rVal(r, CudImportPeer.IMPORT_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudImportPeer.ID_CISELNIK).asIntegerObj());
				dto.setCiselnikTabulka(rVal(r, CudImportPeer.CISELNIK_TABULKA).asString());
				dto.setStav(rVal(r, CudImportPeer.STAV).asString());
				dto.setCasVytvorenia(rVal(r, CudImportPeer.CAS_VYTVORENIA).asUtilDate());
				dto.setCasKontrolaZac(rVal(r, CudImportPeer.CAS_KONTROLA_ZAC).asUtilDate());
				dto.setCasKontrolaKon(rVal(r, CudImportPeer.CAS_KONTROLA_KON).asUtilDate());
				dto.setCasImportZac(rVal(r, CudImportPeer.CAS_IMPORT_ZAC).asUtilDate());
				dto.setCasImportKon(rVal(r, CudImportPeer.CAS_IMPORT_KON).asUtilDate());

				dto.setImportPrilohaFileName(rVal(r, CudImportPrilohaPeer.FILE_NAME).asString());

				dto.setErrors(rVal(r, "errors").asString());
				dto.setColumns(rVal(r, "cols").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOImport[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	private DTOImport[] listLight(AuthInfo auth, DTOImport dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOImport();
			}

			MyCriteria2 crit = new MyCriteria2(CudImportPeer.IMPORT_ID, dtoF);

			crit.addSelectColumn(CudImportPeer.IMPORT_ID);
			crit.addSelectColumn(CudImportPeer.ID_CISELNIK);
			crit.addSelectColumn(CudImportPeer.CISELNIK_TABULKA);
			crit.addSelectColumn(CudImportPeer.STAV);
			crit.addSelectColumn(CudImportPeer.CAS_VYTVORENIA);
			crit.addSelectColumn(CudImportPeer.CAS_KONTROLA_ZAC);
			crit.addSelectColumn(CudImportPeer.CAS_KONTROLA_KON);
			crit.addSelectColumn(CudImportPeer.CAS_IMPORT_ZAC);
			crit.addSelectColumn(CudImportPeer.CAS_IMPORT_KON);

			crit.addConditional(CudImportPeer.IMPORT_ID, dtoF.getImportID());
			crit.addConditional(CudImportPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudImportPeer.CISELNIK_TABULKA, dtoF.getCiselnikTabulka(), false);
			crit.addConditional(CudImportPeer.STAV, dtoF.getStav(), false);

			crit.add(CudImportPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOImport> listDTO = new ArrayList<DTOImport>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOImport dto = new DTOImport();
				dto.setImportID(rVal(r, CudImportPeer.IMPORT_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudImportPeer.ID_CISELNIK).asIntegerObj());
				dto.setCiselnikTabulka(rVal(r, CudImportPeer.CISELNIK_TABULKA).asString());
				dto.setStav(rVal(r, CudImportPeer.STAV).asString());
				dto.setCasVytvorenia(rVal(r, CudImportPeer.CAS_VYTVORENIA).asUtilDate());
				dto.setCasKontrolaZac(rVal(r, CudImportPeer.CAS_KONTROLA_ZAC).asUtilDate());
				dto.setCasKontrolaKon(rVal(r, CudImportPeer.CAS_KONTROLA_KON).asUtilDate());
				dto.setCasImportZac(rVal(r, CudImportPeer.CAS_IMPORT_ZAC).asUtilDate());
				dto.setCasImportKon(rVal(r, CudImportPeer.CAS_IMPORT_KON).asUtilDate());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOImport[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOImport readLight(AuthInfo auth, Integer importID) throws AppException {

		try {
			DTOImport dtoF = new DTOImport();
			dtoF.setImportID(importID);

			DTOImport[] listDTO = listLight(auth, dtoF);

			return StringUtils.isValid(listDTO) ? listDTO[0] : null;

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	public DTOImport[] listForProcess(AuthInfo auth) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudImportPeer.IMPORT_ID, new DTOImport());

			crit.addSelectColumn(CudImportPeer.IMPORT_ID);
			crit.addSelectColumn(CudImportPeer.ID_CISELNIK);
			crit.addSelectColumn(CudImportPeer.CISELNIK_TABULKA);
			crit.addSelectColumn(CudImportPeer.STAV);
			crit.addSelectColumn(CudImportPeer.CAS_VYTVORENIA);
			crit.addSelectColumn(CudImportPeer.CAS_KONTROLA_ZAC);
			crit.addSelectColumn(CudImportPeer.CAS_KONTROLA_KON);
			crit.addSelectColumn(CudImportPeer.CAS_IMPORT_ZAC);
			crit.addSelectColumn(CudImportPeer.CAS_IMPORT_KON);

			Criterion c11 = crit.getNewCriterion(CudImportPeer.STAV, _CudConsts.IMPORT_STAV_KONTROLA, MyCriteria2.EQUAL);
			Criterion c12 = crit.getNewCriterion(CudImportPeer.CAS_KONTROLA_KON, null, MyCriteria2.ISNULL);
			Criterion c1 = c11.and(c12);

			Criterion c21 = crit.getNewCriterion(CudImportPeer.STAV, _CudConsts.IMPORT_STAV_IMPORT, MyCriteria2.EQUAL);
			Criterion c22 = crit.getNewCriterion(CudImportPeer.CAS_IMPORT_KON, null, MyCriteria2.ISNULL);
			Criterion c2 = c21.and(c22);

			crit.add(c1.or(c2));

			crit.add(CudImportPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudImportPeer.CAS_VYTVORENIA);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOImport> listDTO = new ArrayList<DTOImport>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOImport dto = new DTOImport();
				dto.setImportID(rVal(r, CudImportPeer.IMPORT_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudImportPeer.ID_CISELNIK).asIntegerObj());
				dto.setCiselnikTabulka(rVal(r, CudImportPeer.CISELNIK_TABULKA).asString());
				dto.setStav(rVal(r, CudImportPeer.STAV).asString());
				dto.setCasVytvorenia(rVal(r, CudImportPeer.CAS_VYTVORENIA).asUtilDate());
				dto.setCasKontrolaZac(rVal(r, CudImportPeer.CAS_KONTROLA_ZAC).asUtilDate());
				dto.setCasKontrolaKon(rVal(r, CudImportPeer.CAS_KONTROLA_KON).asUtilDate());
				dto.setCasImportZac(rVal(r, CudImportPeer.CAS_IMPORT_ZAC).asUtilDate());
				dto.setCasImportKon(rVal(r, CudImportPeer.CAS_IMPORT_KON).asUtilDate());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOImport[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForProcess.error", auth);
			return null;
		}
	}

	public String updateStavToKontrola(AuthInfo auth, Integer importID) throws AppException {

		try {
			DTOImport dto = readLight(auth, importID);

			if (!_CudConsts.IMPORT_STAV_KONTROLA.equals(dto.getStav())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3034);
			}
			if (!StringUtils.isValid(dto.getCasKontrolaKon())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3035);
			}
			if (StringUtils.isValid(dto.getCasImportZac())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3036);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateStavToImportKontrola.error", auth);
			return null;
		}
	}

	public Integer[] idsForDelete(AuthInfo auth) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudImportPeer.IMPORT_ID, new DTOImport());

			crit.addSelectColumn(CudImportPeer.IMPORT_ID);

			crit.add(CudImportPeer.ID_TRANSAKCIA_ZRUSENE, (Object) null, MyCriteria2.ISNOTNULL);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudImportPeer.IMPORT_ID).asIntegerObj());
			}

			return set.toArray(new Integer[set.size()]);

		} catch (Throwable t) {
			handleException(t, "idsForDelete.error", auth);
			return null;
		}
	}

}
