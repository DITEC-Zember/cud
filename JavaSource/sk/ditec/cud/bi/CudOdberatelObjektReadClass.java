package sk.ditec.cud.bi;

import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_DENNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_MESACNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_STVRTROCNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_TYZDENNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT;

import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;
import org.apache.torque.util.SqlEnum;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.security.Rola;
import sk.ditec.common.utils.Base64;
import sk.ditec.common.utils.DateUtils;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOObjekt;
import sk.ditec.cud.dto.DTOOdberatel;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.dto.DTOOdberatelObjektLD;
import sk.ditec.cud.procvys.CudDzOdoslanieExportuProcess;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudObjektPeer;
import sk.ditec.dao.meta.CudOdberatelObjektPeer;
import sk.ditec.dao.meta.CudOdberatelPeer;

import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

public class CudOdberatelObjektReadClass extends _CudBaseClass {

	public DTOOdberatelObjekt[] list(AuthInfo auth, Page page, DTOOdberatelObjekt dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOOdberatelObjekt();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, dtoF);

			crit.addSelectColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_ODBERATEL);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_OBJEKT);
			crit.addSelectColumn(CudOdberatelObjektPeer.PLATNOST_OD);
			crit.addSelectColumn(CudOdberatelObjektPeer.PLATNOST_DO);
			crit.addSelectColumn(CudOdberatelObjektPeer.TYP_PRISTUPU);
			crit.addSelectColumn(CudOdberatelObjektPeer.OPAKOVANIE);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_DOVOD);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_FORMAT);
			// crit.addSelectColumn(CudOdberatelObjektPeer.AKTIVNY);
			crit.addSelectColumn(CudOdberatelObjektPeer.VSETKY_CISELNIKY);

			crit.addAlias("t1", CudOdberatelPeer.TABLE_NAME);
			crit.addAsColumn("odb_nazov", "t1.NAZOV");
			crit.addAsColumn("odb_aktivny", "t1.AKTIVNY");
			crit.addJoin(CudOdberatelObjektPeer.ID_ODBERATEL, "t1.ODBERATEL_ID", MyCriteria2.LEFT_JOIN);
			crit.addConditional("t1.NAZOV", dtoF.getOdberatelNazov(), true);
			crit.addConditional("t1.AKTIVNY", dtoF.getOdberatelAktivny(), false);

			crit.addAlias("t2", CudObjektPeer.TABLE_NAME);
			crit.addAsColumn("obj_nazov", "t2.NAZOV");
			crit.addAsColumn("obj_platny", "t2.PLATNY");
			crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, "t2.OBJEKT_ID", MyCriteria2.LEFT_JOIN);
			crit.addConditional("t2.NAZOV", dtoF.getObjektNazov(), true);
			crit.addConditional("t2.PLATNY", dtoF.getObjektPlatny(), false);

			String s = "CASE WHEN " + CudOdberatelObjektPeer.TYP_PRISTUPU + " = \'1\' THEN \'W\'";
			s += " WHEN " + CudOdberatelObjektPeer.TYP_PRISTUPU + " = \'2\' THEN \'E\'";
			s += " WHEN " + CudOdberatelObjektPeer.TYP_PRISTUPU + " = \'3\' THEN \'Z\'";
			s += " ELSE null END";
			crit.addAsColumn("typ_pristupu_lookup", s);

			s = " CASE WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'1\' THEN \'DE\'";
			s += " WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'2\' THEN \'TY\'";
			s += " WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'3\' THEN \'ME\'";
			s += " WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'4\' THEN \'ST\'";
			s += " WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'5\' THEN \'PR\'";
			s += " ELSE null END";
			crit.addAsColumn("opakovanie_lookup", s);

			crit.addConditional(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, dtoF.getOdberatelObjektID());
			crit.addConditional(CudOdberatelObjektPeer.ID_ODBERATEL, dtoF.getIDOdberatel());
			crit.addConditional(CudOdberatelObjektPeer.ID_OBJEKT, dtoF.getIDObjekt());
			crit.addConditional(CudOdberatelObjektPeer.PLATNOST_OD, dtoF.getPlatnostOdOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudOdberatelObjektPeer.PLATNOST_OD, dtoF.getPlatnostOdDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudOdberatelObjektPeer.PLATNOST_DO, dtoF.getPlatnostDoOd(), MyCriteria2.GREATER_EQUAL);
			crit.addConditionalSecond(CudOdberatelObjektPeer.PLATNOST_DO, dtoF.getPlatnostDoDo(), MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudOdberatelObjektPeer.TYP_PRISTUPU, dtoF.getTypPristupu(), false);
			crit.addConditional(CudOdberatelObjektPeer.OPAKOVANIE, dtoF.getOpakovanie(), false);
			crit.addConditional(CudOdberatelObjektPeer.EXPORT_DOVOD, dtoF.getExportDovod(), false);
			crit.addConditional(CudOdberatelObjektPeer.EXPORT_FORMAT, dtoF.getExportFormat(), false);

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOOdberatelObjekt> listDTO = new ArrayList<DTOOdberatelObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOOdberatelObjekt dto = new DTOOdberatelObjekt();
				dto.setOdberatelObjektID(rVal(r, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID).asIntegerObj());
				dto.setIDOdberatel(rVal(r, CudOdberatelObjektPeer.ID_ODBERATEL).asIntegerObj());
				dto.setIDObjekt(rVal(r, CudOdberatelObjektPeer.ID_OBJEKT).asIntegerObj());
				dto.setPlatnostOd(rVal(r, CudOdberatelObjektPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudOdberatelObjektPeer.PLATNOST_DO).asUtilDate());
				dto.setTypPristupu(rVal(r, CudOdberatelObjektPeer.TYP_PRISTUPU).asString());
				dto.setOpakovanie(rVal(r, CudOdberatelObjektPeer.OPAKOVANIE).asString());
				dto.setExportDovod(rVal(r, CudOdberatelObjektPeer.EXPORT_DOVOD).asString());
				dto.setExportFormat(rVal(r, CudOdberatelObjektPeer.EXPORT_FORMAT).asString());
				// dto.setAktivny(rVal(r, CudOdberatelObjektPeer.AKTIVNY).asString());
				dto.setVsetkyCiselniky(rVal(r, CudOdberatelObjektPeer.VSETKY_CISELNIKY).asString());

				dto.setOdberatelNazov(rVal(r, "odb_nazov").asString());
				dto.setOdberatelAktivny(rVal(r, "odb_aktivny").asString());

				dto.setObjektNazov(rVal(r, "obj_nazov").asString());
				dto.setObjektPlatny(rVal(r, "obj_platny").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOOdberatelObjekt[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTOOdberatelObjekt[] listForOdbebratel(AuthInfo auth, Page page, DTOOdberatelObjekt dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOOdberatelObjekt();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, dtoF);

			crit.addSelectColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_ODBERATEL);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_OBJEKT);
			crit.addSelectColumn(CudOdberatelObjektPeer.PLATNOST_OD);
			crit.addSelectColumn(CudOdberatelObjektPeer.PLATNOST_DO);
			crit.addSelectColumn(CudOdberatelObjektPeer.TYP_PRISTUPU);
			crit.addSelectColumn(CudOdberatelObjektPeer.OPAKOVANIE);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_DOVOD);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_FORMAT);

			crit.addAlias("t2", CudObjektPeer.TABLE_NAME);
			crit.addAsColumn("obj_nazov", "t2.NAZOV");
			crit.addAsColumn("obj_platny", "t2.PLATNY");
			crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, "t2.OBJEKT_ID", MyCriteria2.LEFT_JOIN);
			crit.addConditional("t2.NAZOV", dtoF.getObjektNazov(), true);
			crit.addConditional("t2.PLATNY", dtoF.getObjektPlatny(), false);

			String s = "CASE WHEN " + CudOdberatelObjektPeer.TYP_PRISTUPU + " = \'1\' THEN \'W\'";
			s += " WHEN " + CudOdberatelObjektPeer.TYP_PRISTUPU + " = \'2\' THEN \'E\'";
			s += " WHEN " + CudOdberatelObjektPeer.TYP_PRISTUPU + " = \'3\' THEN \'Z\'";
			s += " ELSE null END";
			crit.addAsColumn("typ_pristupu_lookup", s);

			s = " CASE WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'1\' THEN \'DE\'";
			s += " WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'2\' THEN \'TY\'";
			s += " WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'3\' THEN \'ME\'";
			s += " WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'4\' THEN \'ST\'";
			s += " WHEN " + CudOdberatelObjektPeer.OPAKOVANIE + " = \'5\' THEN \'PR\'";
			s += " ELSE null END";
			crit.addAsColumn("opakovanie_lookup", s);

			crit.addConditional(CudOdberatelObjektPeer.ID_ODBERATEL, dtoF.getIDOdberatel());

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOOdberatelObjekt> listDTO = new ArrayList<DTOOdberatelObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOOdberatelObjekt dto = new DTOOdberatelObjekt();
				dto.setOdberatelObjektID(rVal(r, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID).asIntegerObj());
				dto.setIDObjekt(rVal(r, CudOdberatelObjektPeer.ID_OBJEKT).asIntegerObj());
				dto.setPlatnostOd(rVal(r, CudOdberatelObjektPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudOdberatelObjektPeer.PLATNOST_DO).asUtilDate());
				dto.setTypPristupu(rVal(r, CudOdberatelObjektPeer.TYP_PRISTUPU).asString());
				dto.setOpakovanie(rVal(r, CudOdberatelObjektPeer.OPAKOVANIE).asString());
				dto.setExportDovod(rVal(r, CudOdberatelObjektPeer.EXPORT_DOVOD).asString());
				dto.setExportFormat(rVal(r, CudOdberatelObjektPeer.EXPORT_FORMAT).asString());

				dto.setObjektNazov(rVal(r, "obj_nazov").asString());
				dto.setObjektPlatny(rVal(r, "obj_platny").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOOdberatelObjekt[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForOdbebratel.error", auth);
			return null;
		}
	}

	private String decrypt(String passwd) throws AppException {

		try {
			if (!StringUtils.isValid(passwd)) {
				return null;
			}

			KeySpec keySpec = new DESedeKeySpec(_CudConsts.PASSWD_ENCRYPTION_KEY.getBytes("UTF8"));
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(_CudConsts.PASSWD_ENCRYPTION_SCHEMA);
			Cipher cipher = Cipher.getInstance(_CudConsts.PASSWD_ENCRYPTION_SCHEMA);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);

			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] encryptedByte = Base64.decode(passwd);
			byte[] plainText = cipher.doFinal(encryptedByte);
			return new String(plainText);

		} catch (Throwable t) {
			DBUtils.handleException(t, "decrypt.error");
			return null;
		}
	}

	private Map<Integer, DTOOdberatelObjekt> mapLight(AuthInfo auth, Integer[] ids) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ids)) {
				return new HashMap<Integer, DTOOdberatelObjekt>();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, new DTOOdberatelObjekt());

			crit.addSelectColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_ODBERATEL);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_OBJEKT);
			crit.addSelectColumn(CudOdberatelObjektPeer.PLATNOST_OD);
			crit.addSelectColumn(CudOdberatelObjektPeer.PLATNOST_DO);
			crit.addSelectColumn(CudOdberatelObjektPeer.TYP_PRISTUPU);
			crit.addSelectColumn(CudOdberatelObjektPeer.OPAKOVANIE);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_DOVOD);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_ROZSAH);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_FORMAT);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_TYP_PODLA_ODBERATELA);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_TYP);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_CESTA);
			crit.addSelectColumn(CudOdberatelObjektPeer.VSETKY_CISELNIKY);
			crit.addSelectColumn(CudOdberatelObjektPeer.CAS_POSL_EXPORTU_ZMENA);
			crit.addSelectColumn(CudOdberatelObjektPeer.CAS_POSL_EXPORTU);
			crit.addSelectColumn(CudOdberatelObjektPeer.CAS_POSL_EXPORTU_PLAN);
			crit.addSelectColumn(CudOdberatelObjektPeer.CAS_ZMENY);
			crit.addSelectColumn(CudOdberatelObjektPeer.LOGIN);
			crit.addSelectColumn(CudOdberatelObjektPeer.HESLO);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_UCET);
			crit.addSelectColumn(CudOdberatelObjektPeer.AKTIVNY);

			if (ids.length == 1) {
				crit.addConditional(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, ids[0]);
			} else {
				crit.addIn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, ids);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOOdberatelObjekt> mapDTO = new HashMap<Integer, DTOOdberatelObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOOdberatelObjekt dto = new DTOOdberatelObjekt();
				dto.setOdberatelObjektID(rVal(r, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID).asIntegerObj());
				dto.setIDOdberatel(rVal(r, CudOdberatelObjektPeer.ID_ODBERATEL).asIntegerObj());
				dto.setIDObjekt(rVal(r, CudOdberatelObjektPeer.ID_OBJEKT).asIntegerObj());
				dto.setPlatnostOd(rVal(r, CudOdberatelObjektPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudOdberatelObjektPeer.PLATNOST_DO).asUtilDate());
				dto.setTypPristupu(rVal(r, CudOdberatelObjektPeer.TYP_PRISTUPU).asString());
				dto.setOpakovanie(rVal(r, CudOdberatelObjektPeer.OPAKOVANIE).asString());
				dto.setExportDovod(rVal(r, CudOdberatelObjektPeer.EXPORT_DOVOD).asString());
				dto.setExportRozsah(rVal(r, CudOdberatelObjektPeer.EXPORT_ROZSAH).asString());
				dto.setExportFormat(rVal(r, CudOdberatelObjektPeer.EXPORT_FORMAT).asString());
				dto.setExportTypPodlaOdberatela(rVal(r, CudOdberatelObjektPeer.EXPORT_TYP_PODLA_ODBERATELA).asString());
				dto.setExportTyp(rVal(r, CudOdberatelObjektPeer.EXPORT_TYP).asString());
				dto.setExportCesta(rVal(r, CudOdberatelObjektPeer.EXPORT_CESTA).asString());
				dto.setVsetkyCiselniky(rVal(r, CudOdberatelObjektPeer.VSETKY_CISELNIKY).asString());
				dto.setCasPoslExportuZmena(rVal(r, CudOdberatelObjektPeer.CAS_POSL_EXPORTU_ZMENA).asUtilDate());
				dto.setCasPoslExportu(rVal(r, CudOdberatelObjektPeer.CAS_POSL_EXPORTU).asUtilDate());
				dto.setCasPoslExportuPlan(rVal(r, CudOdberatelObjektPeer.CAS_POSL_EXPORTU_PLAN).asUtilDate());
				dto.setCasZmeny(rVal(r, CudOdberatelObjektPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudOdberatelObjektPeer.ID_UCET).asIntegerObj());
				dto.setLogin(rVal(r, CudOdberatelObjektPeer.LOGIN).asString());
				dto.setHeslo(decrypt(rVal(r, CudOdberatelObjektPeer.HESLO).asString()));
				dto.setAktivny(rVal(r, CudOdberatelObjektPeer.AKTIVNY).asString());

				dto.setPriznakZmeny("F");

				mapDTO.put(dto.getOdberatelObjektID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	private DTOOdberatelObjekt readLight(AuthInfo auth, Integer odberatelObjektID) throws AppException {

		try {
			if (!StringUtils.isValid(odberatelObjektID)) {
				return null;
			}

			return mapLight(auth, new Integer[] { odberatelObjektID }).get(odberatelObjektID);

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	public DTOOdberatelObjektLD loadData(AuthInfo auth, DTOOdberatelObjektLD dtoF) throws AppException {

		try {
			Map<Integer, DTOOdberatelObjekt> odberatelObjektMap = mapLight(auth, new Integer[] { dtoF.getOdberatelObjektID() });

			Map<Integer, DTOOdberatel> odberatelMap = getDelegate().getOdberatelRead().mapLight(auth, new Integer[] { dtoF.getIDOdberatel() });

			Map<Integer, DTOObjekt> oobjektMap = getDelegate().getObjektRead().mapLight(auth, new Integer[] { dtoF.getIDObjekt() });

			DTOOdberatelObjektLD resultDTO = new DTOOdberatelObjektLD();
			resultDTO.setOdberatelObjektDTO(odberatelObjektMap.get(dtoF.getOdberatelObjektID()));
			resultDTO.setOdberatelDTO(odberatelMap.get(dtoF.getIDOdberatel()));
			resultDTO.setObjektDTO(oobjektMap.get(dtoF.getIDObjekt()));

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public Integer odberatelCount(AuthInfo auth, Integer odberatelID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(odberatelID)) {
				return 0;
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, new DTOOdberatelObjekt());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudOdberatelObjektPeer.ID_ODBERATEL, odberatelID);

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, "pocet").asIntegerObj();
			}

			return 0;

		} catch (Throwable t) {
			handleException(t, "odberatelCount.error", auth);
			return null;
		}
	}

	public Integer objektCount(AuthInfo auth, Integer objektID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(objektID)) {
				return 0;
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, new DTOOdberatelObjekt());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudOdberatelObjektPeer.ID_OBJEKT, objektID);

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, "pocet").asIntegerObj();
			}

			return 0;

		} catch (Throwable t) {
			handleException(t, "objektCount.error", auth);
			return null;
		}
	}

	public List<DTOOdberatelObjekt> listByObjekt(AuthInfo auth, Integer objektID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(objektID)) {
				return new ArrayList<DTOOdberatelObjekt>();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, new DTOOdberatelObjekt());

			crit.addSelectColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_ODBERATEL);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_OBJEKT);
			crit.addSelectColumn(CudOdberatelObjektPeer.TYP_PRISTUPU);
			crit.addSelectColumn(CudOdberatelObjektPeer.PLATNOST_OD);
			crit.addSelectColumn(CudOdberatelObjektPeer.PLATNOST_DO);
			crit.addSelectColumn(CudOdberatelObjektPeer.OPAKOVANIE);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_DOVOD);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_ROZSAH);

			crit.addAsColumn("odberatel_nazov", CudOdberatelPeer.NAZOV);
			crit.addJoin(CudOdberatelObjektPeer.ID_ODBERATEL, CudOdberatelPeer.ODBERATEL_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudOdberatelPeer.AKTIVNY, "T", false);

			crit.addAsColumn("objekt_nazov", CudObjektPeer.NAZOV);
			crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, CudObjektPeer.OBJEKT_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudObjektPeer.PLATNY, "T", false);

			crit.addConditional(CudOdberatelObjektPeer.ID_OBJEKT, objektID);

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOOdberatelObjekt> listDTO = new ArrayList<DTOOdberatelObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOOdberatelObjekt dto = new DTOOdberatelObjekt();
				dto.setOdberatelObjektID(rVal(r, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID).asIntegerObj());
				dto.setIDOdberatel(rVal(r, CudOdberatelObjektPeer.ID_ODBERATEL).asIntegerObj());
				dto.setIDObjekt(rVal(r, CudOdberatelObjektPeer.ID_OBJEKT).asIntegerObj());
				dto.setPlatnostOd(rVal(r, CudOdberatelObjektPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudOdberatelObjektPeer.PLATNOST_DO).asUtilDate());
				dto.setTypPristupu(rVal(r, CudOdberatelObjektPeer.TYP_PRISTUPU).asString());
				dto.setOpakovanie(rVal(r, CudOdberatelObjektPeer.OPAKOVANIE).asString());
				dto.setExportDovod(rVal(r, CudOdberatelObjektPeer.EXPORT_DOVOD).asString());
				dto.setExportRozsah(rVal(r, CudOdberatelObjektPeer.EXPORT_ROZSAH).asString());

				dto.setOdberatelNazov(rVal(r, "odberatel_nazov").asString());

				dto.setOdberatelNazov(rVal(r, "objekt_nazov").asString());

				dto.setTypPristupuNazov(_CudLookupUtils.lookupOdberatelTypPristupuNazov(dto.getTypPristupu()));

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listByObjekt.error", auth);
			return null;
		}
	}

	private boolean existujeZaznam(AuthInfo auth, DTOOdberatelObjekt dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOOdberatelObjekt();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, dtoF);

			crit.addSelectColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);

			crit.addConditional(CudOdberatelObjektPeer.ID_ODBERATEL, dtoF.getIDOdberatel());
			crit.addConditional(CudOdberatelObjektPeer.ID_OBJEKT, dtoF.getIDObjekt());
			crit.addConditional(CudOdberatelObjektPeer.TYP_PRISTUPU, dtoF.getTypPristupu(), false);
			crit.addConditional(CudOdberatelObjektPeer.VSETKY_CISELNIKY, dtoF.getVsetkyCiselniky(), false);

			Criterion c11 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, dtoF.getPlatnostOd(), MyCriteria2.LESS_EQUAL);
			Criterion c12 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, dtoF.getPlatnostOd(), MyCriteria2.GREATER_EQUAL);
			Criterion c13 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			Criterion c1 = c11.and(c12.or(c13));

			Criterion c2 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, dtoF.getPlatnostOd(), MyCriteria2.GREATER_EQUAL);
			if (StringUtils.isValid(dtoF.getPlatnostDo())) {
				Criterion c22 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, dtoF.getPlatnostDo(), MyCriteria2.LESS_EQUAL);
				c2 = c2.and(c22);
			}
			crit.add(c1.or(c2));

			if (StringUtils.isValid(dtoF.getOdberatelObjektID())) {
				crit.add(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, dtoF.getOdberatelObjektID(), MyCriteria2.NOT_EQUAL);
			}

			if ("T".equals(dtoF.getVsetkyCiselniky())) {
				crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, CudObjektPeer.OBJEKT_ID, MyCriteria2.LEFT_JOIN);
				crit.addConditional(CudObjektPeer.OBJEKT_ID, dtoF.getIDObjekt());
				if (StringUtils.isValid(dtoF.getObjektSystemovyKanal())) {
					crit.addConditional(CudObjektPeer.SYSTEMOVY_KANAL, dtoF.getObjektSystemovyKanal(), false);
				} else {
					crit.add(CudObjektPeer.SYSTEMOVY_KANAL, (Object) null, MyCriteria2.ISNULL);
				}
			}

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			return lp.iterator().hasNext();

		} catch (Throwable t) {
			handleException(t, "existujeZaznam.error", auth);
			return false;
		}
	}

	public boolean existujeOpravnenieNaVsetkyCiselnikyOdberatela(AuthInfo auth, DTOOdberatelObjekt dtoF, Date kDatumu) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOOdberatelObjekt();
			}

			List<Rola> rolaList = FrameworkUtils.getAuthMod().rolaListByAccount(auth.getAccountName());
			Set<String> kodRolySet = new HashSet<String>();
			for (Rola dto : rolaList) {
				if (_CudConsts.ROLA_MODUL_KODs.contains(dto.getKodRoly())) {
					kodRolySet.add(dto.getKodRoly());
				}
			}
			String[] poleRola = kodRolySet.toArray(new String[kodRolySet.size()]);

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, dtoF);

			crit.addSelectColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);

			crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, CudObjektPeer.OBJEKT_ID, SqlEnum.LEFT_JOIN);
			crit.addJoin(CudOdberatelObjektPeer.ID_ODBERATEL, CudOdberatelPeer.ODBERATEL_ID, SqlEnum.LEFT_JOIN);

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);
			crit.add(CudOdberatelObjektPeer.VSETKY_CISELNIKY, "T");

			Criterion cA = crit.getNewCriterion(CudOdberatelObjektPeer.ID_OBJEKT, null, MyCriteria2.ISNULL);
			Criterion cB = crit.getNewCriterion(CudObjektPeer.SYSTEMOVY_KANAL, null, MyCriteria2.ISNULL);
			Criterion cAB = cA.or(cB);
			crit.add(cAB);

			Criterion cC = crit.getNewCriterion(CudOdberatelPeer.OBM_UCET_NAZOV, auth.getAccountName(), MyCriteria2.EQUAL);
			Criterion cD = null;
			if (StringUtils.isValid(poleRola)) {
				if (poleRola.length == 1) {
					cD = crit.getNewCriterion(CudOdberatelPeer.ROLA_KOD, poleRola[0], MyCriteria2.EQUAL);
				} else {
					cD = crit.getNewCriterion(CudOdberatelPeer.ROLA_KOD, poleRola, MyCriteria2.IN);
				}
				Criterion cCD = cC.or(cD);
				crit.add(cCD);
			} else {
				crit.add(cC);
			}

			crit.add(CudOdberatelPeer.AKTIVNY, "T");
			crit.add(CudOdberatelPeer.ID_TRANSAKCIA_ZRUSENE, null);

			if (kDatumu == null) {
				kDatumu = DateUtils.removeTime(new Date());
			}

			Criterion cE = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, kDatumu, MyCriteria2.LESS_EQUAL);
			Criterion cF = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, kDatumu, MyCriteria2.GREATER_EQUAL);
			Criterion cG = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			Criterion cEFG = cE.and(cF.or(cG));
			crit.add(cEFG);

			crit.addConditional(CudOdberatelObjektPeer.TYP_PRISTUPU, dtoF.getTypPristupu(), false);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			return lp.iterator().hasNext();
		} catch (Throwable t) {
			handleException(t, "existujeOpravnenieNaVsetkyCiselniky.error", auth);
			return false;
		}
	}

	private Set<Integer> objektIDs(AuthInfo auth, DTOOdberatelObjekt dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOOdberatelObjekt();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, dtoF);

			crit.addSelectColumn(CudOdberatelObjektPeer.ID_OBJEKT);

			crit.addConditional(CudOdberatelObjektPeer.ID_ODBERATEL, dtoF.getIDOdberatel());
			crit.addConditional(CudOdberatelObjektPeer.TYP_PRISTUPU, dtoF.getTypPristupu(), false);

			crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, CudObjektPeer.OBJEKT_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudObjektPeer.PLATNY, "T", false);

			Criterion c11 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, dtoF.getPlatnostOd(), MyCriteria2.LESS_EQUAL);
			Criterion c12 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, dtoF.getPlatnostOd(), MyCriteria2.GREATER_EQUAL);
			Criterion c13 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			Criterion c1 = c11.and(c12.or(c13));

			Criterion c2 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, dtoF.getPlatnostOd(), MyCriteria2.GREATER_EQUAL);
			if (StringUtils.isValid(dtoF.getPlatnostDo())) {
				Criterion c22 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, dtoF.getPlatnostDo(), MyCriteria2.LESS_EQUAL);
				c2 = c2.and(c22);
			}
			crit.add(c1.or(c2));

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudOdberatelObjektPeer.ID_OBJEKT).asIntegerObj());
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "objektIDs.error", auth);
			return null;
		}
	}

	private boolean jeCasovaZlozka(Date d) throws AppException {

		try {
			if (!StringUtils.isValid(d)) {
				return false;
			}

			Calendar cal = Calendar.getInstance();
			cal.setTime(d);

			return (cal.get(Calendar.HOUR_OF_DAY) > 0) || (cal.get(Calendar.MINUTE) > 0) || (cal.get(Calendar.SECOND) > 0);

		} catch (Throwable t) {
			DBUtils.handleException(t, "objektIDs.error");
			return false;
		}
	}

	public String updateKontrola(AuthInfo auth, DTOOdberatelObjekt dto) throws AppException {

		try {
			if (!StringUtils.isValid(dto.getIDOdberatel())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Odberateľ");
			}
			if (!StringUtils.isValid(dto.getTypPristupu())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Typ prístupu");
			}
			if (!StringUtils.isValid(dto.getPlatnostOd())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Platnosť od");
			}
			if (!StringUtils.isValid(dto.getVsetkyCiselniky())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Všetky číselníky");
			}

			if (!_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT.equals(dto.getTypPristupu())) {

				if (StringUtils.isValid(dto.getExportTyp())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Typ cesty pre export");
				}
				if (StringUtils.isValid(dto.getExportCesta())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Cesta pre export");
				}
				if (StringUtils.isValid(dto.getExportFormat())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Fomát exportu");
				}
				if (StringUtils.isValid(dto.getExportDovod())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Dôvod exportu");
				}
				if (StringUtils.isValid(dto.getExportRozsah())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Rozsah exportu");
				}
				if (StringUtils.isValid(dto.getOpakovanie())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Opakovanie");
				}
				if (StringUtils.isValid(dto.getAktivny())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Aktívny");
				}
				if (jeCasovaZlozka(dto.getPlatnostOd())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_122, "Platnosť od");
				}
				if (jeCasovaZlozka(dto.getPlatnostDo())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_122, "Platnosť do");
				}
			}

			if (StringUtils.isValid(dto.getLogin()) || StringUtils.isValid(dto.getHeslo())) {
				boolean b = false;
				if (!_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT.equals(dto.getTypPristupu())) {
					b = true;
				}
				if (!_CudConsts.ODBERATEL_OBJEKT_EXPORT_TYP_URI.equals(dto.getExportTyp())) {
					b = true;
				}
				if (b && StringUtils.isValid(dto.getLogin())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Login");
				}
				if (b && StringUtils.isValid(dto.getHeslo())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Heslo");
				}
			}

			DTOOdberatel dtoOdb = getDelegate().getOdberatelRead().readLight(auth, dto.getIDOdberatel());
			if (!StringUtils.isValid(dtoOdb)) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_120, "Odberateľ");
			}

			DTOObjekt dtoObj = null;
			if (StringUtils.isValid(dto.getIDObjekt())) {
				if (!StringUtils.isValid(dtoObj)) {
					dtoObj = getDelegate().getObjektRead().read(auth, dto.getIDObjekt());
				}
				if (!StringUtils.isValid(dtoObj)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_120, "Objekt");
				}
			}

			if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT.equals(dto.getTypPristupu())) {

				if (!StringUtils.isValid(dto.getExportTyp())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Typ cesty pre export");
				}
				if (!StringUtils.isValid(dto.getExportCesta())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Cesta pre export");
				}
				if (!StringUtils.isValid(dto.getExportFormat())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Fomát exportu");
				}
				if (!StringUtils.isValid(dto.getExportDovod())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Dôvod exportu");
				}
				if (!StringUtils.isValid(dto.getOpakovanie())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Opakovanie");
				}
				if (!StringUtils.isValid(dto.getAktivny())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Aktívny");
				}

				if (StringUtils.isValid(dto.getIDObjekt())) {

					if ("T".equals(dtoObj.getSystemovy()) && StringUtils.isValid(dtoObj.getSystemovyExportFormat())) {
						if (!dtoObj.getSystemovyExportFormat().equals(dto.getExportFormat())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3069, "Formát exportu", dtoObj.getSystemovyExportFormat());
						}
					}
					if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_TYP_URI.equals(dto.getExportTyp())) {
						boolean b = true;
						if ("T".equals(dtoObj.getSystemovy()) && _CudConsts.OBJEKT_SYSTEMOVY_EXPORT_FORMAT_XML.equals(dtoObj.getSystemovyExportFormat())) {
							if (_CudConsts.OBJEKT_SYSTEMOVY_KANAL_CRD.equals(dtoObj.getSystemovyKanal())) {
								b = false;
							}
							if (_CudConsts.OBJEKT_SYSTEMOVY_KANAL_OBM.equals(dtoObj.getSystemovyKanal())) {
								b = false;
							}
						}
						if (b) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_120, "Typ cesty pre export");
						}
					}

					boolean b1 = false;
					if (_CudConsts.OBJEKT_SYSTEMOVY_KANAL_CRD.equals(dtoObj.getSystemovyKanal())) {
						b1 = true;
					}
					if (_CudConsts.OBJEKT_SYSTEMOVY_KANAL_OBM.equals(dtoObj.getSystemovyKanal())) {
						b1 = true;
					}
					if (_CudConsts.OBJEKT_NAZOV_RINF.equals(dtoObj.getNazov())) {
						b1 = true;
					}
					if ("T".equals(dtoObj.getSystemovy()) && b1 && StringUtils.isValid(dto.getExportRozsah())) {
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_119, "Rozsah exportu");
					}
					if ((!"T".equals(dtoObj.getSystemovy()) || !b1) && !StringUtils.isValid(dto.getExportRozsah())) {
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Rozsah exportu");
					}

					if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_TYP_URI.equals(dto.getExportTyp())) {
						if (!StringUtils.isValid(dto.getExportCesta())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Cesta pre export");
						}
					}

				} else {
					if (!StringUtils.isValid(dto.getExportRozsah())) {
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "Rozsah exportu");
					}
				}

				if (StringUtils.isValid(dtoOdb.getExportTyp()) && "T".equals(dto.getExportTypPodlaOdberatela()) && !dtoOdb.getExportTyp().equals(dto.getExportTyp())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3069, "Typ cesty pre export", dtoOdb.getExportTyp());
				}
				if (StringUtils.isValid(dtoOdb.getExportCesta()) && "T".equals(dto.getExportTypPodlaOdberatela()) && !dtoOdb.getExportCesta().equals(dto.getExportCesta())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3069, "Cesta pre export", dtoOdb.getExportCesta());
				}
			}

			DTOOdberatelObjekt dtoOld = readLight(auth, dto.getOdberatelObjektID());
			if (StringUtils.isValid(dtoOld)) {
				if (!_CudKontrolaUtils.equals(dtoOld.getIDObjekt(), dto.getIDObjekt())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Objekt");
				}
				if (!_CudKontrolaUtils.equals(dtoOld.getIDOdberatel(), dto.getIDOdberatel())) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Odberateľ");
				}
				Date now = DateUtils.removeTime(new Date());
				if (dtoOld.getPlatnostOd().before(now)) {
					if (dtoOld.getPlatnostOd().compareTo(dto.getPlatnostOd()) != 0) {
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Platnosť od");
					}
				}
				if (StringUtils.isValid(dtoOld.getPlatnostDo()) && dtoOld.getPlatnostDo().before(now)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_123);
				}

				if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT.equals(dtoOld.getTypPristupu())) {

					if (dtoOld.getPlatnostOd().before(now)) {

						if (!_CudKontrolaUtils.equals(dtoOld.getOpakovanie(), dto.getOpakovanie())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Opakovanie");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getTypPristupu(), dto.getTypPristupu())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Typ prístupu");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getExportFormat(), dto.getExportFormat())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Formát exportu");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getExportDovod(), dto.getExportDovod())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Dôvod exportu");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getExportRozsah(), dto.getExportRozsah())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Rozsah exportu");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getExportTyp(), dto.getExportTyp())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Typ cesty pre export");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getExportCesta(), dto.getExportCesta())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Cesta pre export");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getVsetkyCiselniky(), dto.getVsetkyCiselniky())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Všetky číselníky");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getPriznakZmeny(), dto.getPriznakZmeny())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Príznak zmeny");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getLogin(), dto.getLogin())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Login");
						}
						if (!_CudKontrolaUtils.equals(dtoOld.getHeslo(), dto.getHeslo())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, "Heslo");
						}

						if (StringUtils.isValid(dto.getPlatnostDo()) && now.before(dto.getPlatnostDo())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_113);
						}
					}
				}
			}

			if (StringUtils.isValid(dtoObj)) {
				if ("T".equals(dtoObj.getSystemovy())) {

					if (StringUtils.isValid(dtoObj.getSystemovyExportFormat()) && !dtoObj.getSystemovyExportFormat().equals(dto.getExportFormat())) {
						if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT.equals(dto.getTypPristupu())) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3069, "Formát exportu", dtoObj.getSystemovyExportFormat());
						}
					}
					if (StringUtils.isValid(dtoObj.getSystemovyVsetkyCiselniky()) && !dtoObj.getSystemovyVsetkyCiselniky().equals(dto.getVsetkyCiselniky())) {
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3069, "Všetky číselníky", dtoObj.getSystemovyVsetkyCiselniky());
					}

				} else {
					if ("T".equals(dto.getVsetkyCiselniky())) {
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_124);
					}
				}
			}

			if (StringUtils.isValid(dto.getTypPristupu())) {
				boolean b = true;
				if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_WS.equals(dto.getTypPristupu())) {
					b = false;
				}
				if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT.equals(dto.getTypPristupu())) {
					b = false;
				}
				if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_ZMENA.equals(dto.getTypPristupu())) {
					b = false;
				}
				if (b) {
					String err = _CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_WS + ", " + _CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT + ", "
							+ _CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_ZMENA;
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3068, "Typ prístupu", err);
				}
			}

			if (StringUtils.isValid(dto.getOpakovanie())) {
				boolean b = true;
				if (_CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_DENNE.equals(dto.getOpakovanie())) {
					b = false;
				}
				if (_CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_TYZDENNE.equals(dto.getOpakovanie())) {
					b = false;
				}
				if (_CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_MESACNE.equals(dto.getOpakovanie())) {
					b = false;
				}
				if (_CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_STVRTROCNE.equals(dto.getOpakovanie())) {
					b = false;
				}
				if (_CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE.equals(dto.getOpakovanie())) {
					b = false;
				}
				if (b) {
					String err = _CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_DENNE + ", " + _CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_TYZDENNE + ", "
							+ _CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_MESACNE + ", " + _CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_STVRTROCNE + ", "
							+ _CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE;
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3068, "Opakovanie", err);
				}
			}

			if (StringUtils.isValid(dto.getExportDovod())) {
				boolean b = true;
				if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_DOVOD_ZMENA.equals(dto.getExportDovod())) {
					b = false;
				}
				if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_DOVOD_OBDOBIE.equals(dto.getExportDovod())) {
					b = false;
				}
				if (b) {
					String err = _CudConsts.ODBERATEL_OBJEKT_EXPORT_DOVOD_ZMENA + ", " + _CudConsts.ODBERATEL_OBJEKT_EXPORT_DOVOD_OBDOBIE;
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3068, "Dôvod exportu", err);
				}
			}

			if (StringUtils.isValid(dto.getExportRozsah())) {
				boolean b = true;
				if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_ROZSAH_VSETKY.equals(dto.getExportRozsah())) {
					b = false;
				}
				if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_ROZSAH_ZMENENE.equals(dto.getExportRozsah())) {
					b = false;
				}
				if (b) {
					String err = _CudConsts.ODBERATEL_OBJEKT_EXPORT_ROZSAH_VSETKY + ", " + _CudConsts.ODBERATEL_OBJEKT_EXPORT_ROZSAH_ZMENENE;
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3068, "Rozsah exportu", err);
				}
			}

			if (StringUtils.isValid(dto.getExportFormat())) {
				boolean b = true;
				if (_CudConsts.OBJEKT_SYSTEMOVY_EXPORT_FORMAT_XML.equals(dto.getExportFormat())) {
					b = false;
				}
				if (_CudConsts.OBJEKT_SYSTEMOVY_EXPORT_FORMAT_XLS.equals(dto.getExportFormat())) {
					b = false;
				}
				if (b) {
					String err = _CudConsts.OBJEKT_SYSTEMOVY_EXPORT_FORMAT_XML + ", " + _CudConsts.OBJEKT_SYSTEMOVY_EXPORT_FORMAT_XLS;
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3068, "Formát exportu", err);
				}
			}

			if (StringUtils.isValid(dto.getExportTyp())) {
				boolean b = true;
				if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_TYP_DIR.equals(dto.getExportTyp())) {
					b = false;
				}
				if (_CudConsts.ODBERATEL_OBJEKT_EXPORT_TYP_URI.equals(dto.getExportTyp())) {
					b = false;
				}
				if (b) {
					String err = _CudConsts.ODBERATEL_OBJEKT_EXPORT_TYP_DIR + ", " + _CudConsts.ODBERATEL_OBJEKT_EXPORT_TYP_URI;
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3068, "Typ cesty pre export", err);
				}
			}

			if ("T".equals(dto.getVsetkyCiselniky())) {

				DTOOdberatelObjekt dtoF = new DTOOdberatelObjekt();
				dtoF.setOdberatelObjektID(dto.getOdberatelObjektID());
				dtoF.setIDOdberatel(dto.getIDOdberatel());
				dtoF.setTypPristupu(dto.getTypPristupu());
				dtoF.setPlatnostOd(dto.getPlatnostOd());
				dtoF.setPlatnostDo(dto.getPlatnostDo());

				if (existujeZaznam(auth, dtoF)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_110);
				}
			}

			if ("F".equals(dto.getVsetkyCiselniky())) {

				if (StringUtils.isValid(dto.getIDObjekt())) {
					Map<Integer, DTOObjekt> objektMap = getDelegate().getObjektRead().mapLight(auth, new Integer[] { dto.getIDObjekt() });
					dto.setObjektSystemovyKanal(objektMap.get(dto.getIDObjekt()).getSystemovyKanal());
				}

				DTOOdberatelObjekt dtoF = new DTOOdberatelObjekt();
				dtoF.setOdberatelObjektID(dto.getOdberatelObjektID());
				dtoF.setIDOdberatel(dto.getIDOdberatel());
				dtoF.setTypPristupu(dto.getTypPristupu());
				dtoF.setPlatnostOd(dto.getPlatnostOd());
				dtoF.setPlatnostDo(dto.getPlatnostDo());
				dtoF.setObjektSystemovyKanal(dto.getObjektSystemovyKanal());
				dtoF.setVsetkyCiselniky("T");

				if (existujeZaznam(auth, dtoF)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_111);
				}
			}

			if (StringUtils.isValid(dto.getIDObjekt())) {

				DTOOdberatelObjekt dtoF = new DTOOdberatelObjekt();
				dtoF.setOdberatelObjektID(dto.getOdberatelObjektID());
				dtoF.setIDOdberatel(dto.getIDOdberatel());
				dtoF.setIDObjekt(dto.getIDObjekt());
				dtoF.setTypPristupu(dto.getTypPristupu());
				dtoF.setPlatnostOd(dto.getPlatnostOd());
				dtoF.setPlatnostDo(dto.getPlatnostDo());

				if (existujeZaznam(auth, dtoF)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_114);
				}
			}

			Set<Integer> objektIDs = objektIDs(auth, dto);
			if (StringUtils.isValid(dto.getIDObjekt())) {
				objektIDs.add(dto.getIDObjekt());
			}
			Map<Integer, Integer> pocetnostMap = getDelegate().getObjektCiselnikRead().pocetnostCiselnikyMap(auth, objektIDs.toArray(new Integer[objektIDs.size()]));
			for (Integer ciselnikID : pocetnostMap.keySet()) {
				Integer pocet = pocetnostMap.get(ciselnikID);
				if (pocet.intValue() > 1) {
					String nazov = getDelegate().getCiselnikRead().read(auth, ciselnikID).getNazov();
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_115, nazov);
				}
			}

			if (StringUtils.isValid(dtoObj) && _CudConsts.NAZOV_OBJEKT_EXPORT_LOKACII_CRD.equals(dtoObj.getNazov())) {
				if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT.equals(dto.getTypPristupu())) {
					boolean b = false;
					if (!_CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE.equals(dto.getOpakovanie())) {
						b = true;
					} else if (!_CudConsts.ODBERATEL_OBJEKT_EXPORT_DOVOD_ZMENA.equals(dto.getExportDovod())) {
						b = true;
					} else if (!_CudConsts.ODBERATEL_OBJEKT_EXPORT_ROZSAH_ZMENENE.equals(dto.getExportRozsah())) {
						b = true;
					}
					if (b) {
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_109, _CudConsts.NAZOV_OBJEKT_EXPORT_LOKACII_CRD, _CudConsts.TEXT_ODBERATEL_TYP_PRISTUPU_EXPORT);
					}
				}
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	public List<DTOOdberatelObjekt> list(AuthInfo auth, Date d, String typPristupu, String[] rolaKods) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudOdberatelPeer.ODBERATEL_ID, new DTOOdberatel());

				crit.addSelectColumn(CudOdberatelPeer.ODBERATEL_ID);

				String s = "lower(" + CudOdberatelPeer.OBM_UCET_NAZOV + ") = \'" + auth.getAccountName().toLowerCase() + "\'";
				Criterion c1 = crit.getNewCriterion(CudOdberatelPeer.OBM_UCET_NAZOV, s, MyCriteria2.CUSTOM);

				if (StringUtils.isValid(rolaKods)) {
					Criterion c2 = null;
					if (rolaKods.length == 1) {
						c2 = crit.getNewCriterion(CudOdberatelPeer.ROLA_KOD, rolaKods[0], MyCriteria2.EQUAL);
					} else {
						c2 = crit.getNewCriterion(CudOdberatelPeer.ROLA_KOD, rolaKods, MyCriteria2.IN);
					}
					c1.or(c2);
				}

				if (StringUtils.isValid(auth.getDopravcaKod())) {
					String sql = "SELECT " + _CudConsts.TABULKA_T_DOPRAVCA + "." + _CudConsts.NAZOV_HIST_ID + " FROM " + _CudConsts.TABULKA_T_DOPRAVCA + " WHERE ";
					String datum = sk.ditec.zsr.common.server.utils.DateUtils.formatDateDDMMYYYY(d);
					String nazovAtributu = _CudConsts.TABULKA_T_DOPRAVCA + "." + _CudConsts.NAZOV_PLATNOST_OD;
					String con1 = nazovAtributu + " <= to_timestamp(\'" + datum + "\', \'DD.MM.YYYY\')";
					nazovAtributu = _CudConsts.TABULKA_T_DOPRAVCA + "." + _CudConsts.NAZOV_PLATNOST_DO;
					String con2 = "((" + nazovAtributu + " >= to_timestamp(\'" + datum + "\', \'DD.MM.YYYY\')) OR (" + nazovAtributu + " IS NULL))";
					String con3 = _CudConsts.TABULKA_T_DOPRAVCA + "." + _CudConsts.NAZOV_ZMAZ + "= \'F\'";
					String where = con1 + " AND " + con2 + " AND " + con3 + " AND " + _CudConsts.NAZOV_CISLO_PIS + " = \'" + auth.getDopravcaKod() + "\'";
					sql += where;

					Criterion c3 = crit.getNewCriterion(CudOdberatelPeer.ID_HIST_DOPRAVCA, CudOdberatelPeer.ID_HIST_DOPRAVCA + " IN (" + sql + ")", MyCriteria2.CUSTOM);
					c1.or(c3);
				}

				crit.add(c1);

				crit.addConditional(CudOdberatelPeer.AKTIVNY, "T", false);

				crit.add(CudOdberatelPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, new DTOOdberatelObjekt());

			crit.addSelectColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_ODBERATEL);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_OBJEKT);
			crit.addSelectColumn(CudOdberatelObjektPeer.VSETKY_CISELNIKY);

			crit.addConditional(CudOdberatelObjektPeer.PLATNOST_OD, d, MyCriteria2.LESS_EQUAL);
			crit.addConditional(CudOdberatelObjektPeer.TYP_PRISTUPU, typPristupu, false);

			Criterion c1 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, d, MyCriteria2.GREATER_EQUAL);
			Criterion c2 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			crit.add(c1.or(c2));

			crit.addCustomSql(CudOdberatelObjektPeer.ID_ODBERATEL, CudOdberatelObjektPeer.ID_ODBERATEL + " IN (" + subSql + ")");

			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOOdberatelObjekt> listDTO = new ArrayList<DTOOdberatelObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOOdberatelObjekt dto = new DTOOdberatelObjekt();
				dto.setOdberatelObjektID(rVal(r, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID).asIntegerObj());
				dto.setIDOdberatel(rVal(r, CudOdberatelObjektPeer.ID_ODBERATEL).asIntegerObj());
				dto.setIDObjekt(rVal(r, CudOdberatelObjektPeer.ID_OBJEKT).asIntegerObj());
				dto.setVsetkyCiselniky(rVal(r, CudOdberatelObjektPeer.VSETKY_CISELNIKY).asString());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	/**
	 * SQD CUD CAS DZ Odoslanie exportu - ZLTA POZNAMKA: Pre každého cudOdberatelObjekt beží zvlášť proces cize pobezi n instancii-process sa zobúdza každých 5 min vyberaju sa len
	 * tie záznamy cudOdberatelObjekt pre ktoré (pri každom behu sa kontroluje) PLATNOST_OD<=aktuálny dátum a èas a (PLATNOST_DO is null or aktuálny datum a èas<Platnost_DO) a
	 * AKTIVNY nie je False a TYP_PRISTUPU je Export a ID_TRANSAKCIA_ZRUSENE is null a ak existuje cudOdberatelObjekt.CUD_OBJEKT tak cudOdberatelObjekt.SYSTEMOVY_KANAL nie je OBM a
	 * cudOdberatelObjekt.CUD_ODBERATEL.AKTIVNY=TRUE a udOdberatelObjekt.CUD_ODBERATEL.Id_TRANSAKCIA_ZRUSENE IS NULL
	 * 
	 * @param auth
	 * @return
	 * @throws AppException
	 */
	public List<Integer> najdiOdberatelObjektIdsPreOdoslanieExportu(AuthInfo auth) throws AppException {
		try {
			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, new DTOOdberatelObjekt());
			crit.addSelectColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);

			crit.add(CudOdberatelObjektPeer.AKTIVNY, (Object) "F", MyCriteria2.NOT_LIKE);
			crit.add(CudOdberatelObjektPeer.TYP_PRISTUPU, ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT);
			crit.add(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addJoin(CudOdberatelObjektPeer.ID_ODBERATEL, CudOdberatelPeer.ODBERATEL_ID, MyCriteria2.LEFT_JOIN);
			crit.add(CudOdberatelPeer.AKTIVNY, "T");
			crit.add(CudOdberatelPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, CudObjektPeer.OBJEKT_ID, MyCriteria2.LEFT_JOIN);
			crit.add(CudObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			Criteria.Criterion clObjectExists = crit.getNewCriterion(CudOdberatelObjektPeer.ID_OBJEKT, (Object) null, MyCriteria2.ISNOTNULL);
			Criteria.Criterion clSystemKanalNotObm = crit.getNewCriterion(CudObjektPeer.SYSTEMOVY_KANAL, "OBM", MyCriteria2.NOT_EQUAL);

			Criteria.Criterion clObjectNotExists = crit.getNewCriterion(CudOdberatelObjektPeer.ID_OBJEKT, null, MyCriteria2.EQUAL);
			crit.add(clObjectNotExists.or(clObjectExists.and(clSystemKanalNotObm)));

			Date now = new Date();
			Criteria.Criterion cl1 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, null, MyCriteria2.EQUAL);
			Criteria.Criterion cl2 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_OD, now, MyCriteria2.LESS_EQUAL);
			Criteria.Criterion cl3 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, null, MyCriteria2.EQUAL);
			Criteria.Criterion cl4 = crit.getNewCriterion(CudOdberatelObjektPeer.PLATNOST_DO, now, MyCriteria2.GREATER_EQUAL);
			crit.add((cl1.or(cl2)).and(cl3.or(cl4)));

			crit.addAscendingOrderByColumn(CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID);

			String sql = crit.getSQL();

			getConnection(auth);
			List lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			@SuppressWarnings("rawtypes")
			Iterator iter = lp.iterator();

			List<Integer> listIDs = new ArrayList<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				listIDs.add(rVal(r, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID).asIntegerObj());
			}
			return listIDs;
		} catch (Throwable t) {
			handleException(t, "najdiOdberatelObjektIdsPreOdoslanieExportu.error", auth);
			return null;
		}
	}

	public List<DTOOdberatelObjekt> vratOdberatelObjektSoZmenamiPreExport(AuthInfo auth, DTOOdberatelObjekt cudOdberatelObjekt, Date datumACasNacitaniaDat) throws AppException {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS000");
			long now = System.currentTimeMillis();
			Date denne = new Date(now - CudDzOdoslanieExportuProcess.INTERVAL_DEN);
			Date tyzdenne = new Date(now - CudDzOdoslanieExportuProcess.INTERVAL_TYZDEN);
			Date mesacne = new Date(now - CudDzOdoslanieExportuProcess.INTERVAL_MESIAC);
			Date trojMesacne = new Date(now - CudDzOdoslanieExportuProcess.INTERVAL_3_MESIACE);

			String formattedDatumACasNacitaniaDat = sdf.format(datumACasNacitaniaDat);
			StringBuilder queryBuilder = new StringBuilder();
			queryBuilder.append("SELECT * FROM ");
			queryBuilder.append(CudOdberatelObjektPeer.TABLE_NAME);
			queryBuilder.append(" vcoo LEFT JOIN ");
			queryBuilder.append(CudObjektPeer.TABLE_NAME);
			queryBuilder.append(" obj ON vcoo.ID_OBJEKT = obj.OBJEKT_ID ");
			queryBuilder.append("WHERE vcoo.ID_TRANSAKCIA_ZRUSENE IS NULL AND vcoo.TYP_PRISTUPU = '");
			queryBuilder.append(ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT);
			queryBuilder.append("'");
			if (cudOdberatelObjekt != null) {
				queryBuilder.append(" AND vcoo.ODBERATEL_OBJEKT_ID = ");
				queryBuilder.append(cudOdberatelObjekt.getOdberatelObjektID());
			}
			queryBuilder.append(" AND (vcoo.VSETKY_CISELNIKY = '").append("T").append("' OR obj.PLATNY = '").append("T").append("')");
			queryBuilder.append(" AND vcoo.PLATNOST_OD <= TO_TIMESTAMP('").append(formattedDatumACasNacitaniaDat).append("', 'DD-MM-YYYY HH24:MI:SS.FF6')");
			queryBuilder.append(" AND (vcoo.PLATNOST_DO >= TO_TIMESTAMP('").append(formattedDatumACasNacitaniaDat).append("', 'DD-MM-YYYY HH24:MI:SS.FF6')")
					.append(" OR vcoo.PLATNOST_DO IS NULL)");

			queryBuilder.append(" AND (vcoo.CAS_POSL_EXPORTU_PLAN IS NULL OR").append("(vcoo.OPAKOVANIE = '").append(ODBERATEL_OBJEKT_OPAKOVANIE_DENNE)
					.append("' AND vcoo.CAS_POSL_EXPORTU_PLAN <= TO_TIMESTAMP('").append(sdf.format(denne))
					.append("', 'DD-MM-YYYY HH24:MI:SS.FF6') AND vcoo.CAS_POSL_EXPORTU_PLAN < vcoo.CAS_POSL_EXPORTU_ZMENA)")

					.append(" OR (vcoo.OPAKOVANIE = '").append(ODBERATEL_OBJEKT_OPAKOVANIE_TYZDENNE).append("' AND vcoo.CAS_POSL_EXPORTU_PLAN <= TO_TIMESTAMP('")
					.append(sdf.format(tyzdenne)).append("', 'DD-MM-YYYY HH24:MI:SS.FF6') AND vcoo.CAS_POSL_EXPORTU_PLAN < vcoo.CAS_POSL_EXPORTU_ZMENA)")

					.append(" OR (vcoo.OPAKOVANIE = '").append(ODBERATEL_OBJEKT_OPAKOVANIE_MESACNE).append("' AND vcoo.CAS_POSL_EXPORTU_PLAN <= TO_TIMESTAMP('")
					.append(sdf.format(mesacne)).append("', 'DD-MM-YYYY HH24:MI:SS.FF6') AND vcoo.CAS_POSL_EXPORTU_PLAN < vcoo.CAS_POSL_EXPORTU_ZMENA)")

					.append(" OR (vcoo.OPAKOVANIE = '").append(ODBERATEL_OBJEKT_OPAKOVANIE_STVRTROCNE).append("' AND vcoo.CAS_POSL_EXPORTU_PLAN <= TO_TIMESTAMP('")
					.append(sdf.format(trojMesacne)).append("', 'DD-MM-YYYY HH24:MI:SS.FF6') AND vcoo.CAS_POSL_EXPORTU_PLAN < vcoo.CAS_POSL_EXPORTU_ZMENA)")

					.append(" OR (vcoo.OPAKOVANIE = '").append(ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE).append("' AND vcoo.CAS_POSL_EXPORTU_PLAN < vcoo.CAS_POSL_EXPORTU_ZMENA))");

			getConnection(auth);
			// return CudOdberatelObjektPeer.populateObjects(CudOdberatelObjektPeer.executeQuery(queryBuilder.toString(), false, auth.T));
			List lp = BasePeer.executeQuery(queryBuilder.toString(), false, auth.T);
			returnConnection(auth);

			@SuppressWarnings("rawtypes")
			Iterator iter = lp.iterator();

			List<DTOOdberatelObjekt> odberatelObjektList = new ArrayList<DTOOdberatelObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				odberatelObjektList.add(vytvor(r));
			}

			return odberatelObjektList;
		} catch (Throwable t) {
			handleException(t, "vratOdberatelObjektSoZmenamiPreExport.error", auth);
			return null;
		}
	}

	private DTOOdberatelObjekt vytvor(Record r) throws DataSetException, AppException {
		DTOOdberatelObjekt dto = new DTOOdberatelObjekt();
		dto.setOdberatelObjektID(rVal(r, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID).asIntegerObj());
		dto.setIDOdberatel(rVal(r, CudOdberatelObjektPeer.ID_ODBERATEL).asIntegerObj());
		dto.setIDObjekt(rVal(r, CudOdberatelObjektPeer.ID_OBJEKT).asIntegerObj());
		dto.setPlatnostOd(rVal(r, CudOdberatelObjektPeer.PLATNOST_OD).asUtilDate());
		dto.setPlatnostDo(rVal(r, CudOdberatelObjektPeer.PLATNOST_DO).asUtilDate());
		dto.setTypPristupu(rVal(r, CudOdberatelObjektPeer.TYP_PRISTUPU).asString());
		dto.setOpakovanie(rVal(r, CudOdberatelObjektPeer.OPAKOVANIE).asString());
		dto.setExportDovod(rVal(r, CudOdberatelObjektPeer.EXPORT_DOVOD).asString());
		dto.setExportRozsah(rVal(r, CudOdberatelObjektPeer.EXPORT_ROZSAH).asString());
		dto.setExportFormat(rVal(r, CudOdberatelObjektPeer.EXPORT_FORMAT).asString());
		dto.setExportTyp(rVal(r, CudOdberatelObjektPeer.EXPORT_TYP).asString());
		dto.setExportCesta(rVal(r, CudOdberatelObjektPeer.EXPORT_CESTA).asString());
		dto.setVsetkyCiselniky(rVal(r, CudOdberatelObjektPeer.VSETKY_CISELNIKY).asString());
		dto.setCasPoslExportuZmena(rVal(r, CudOdberatelObjektPeer.CAS_POSL_EXPORTU_ZMENA).asUtilDate());
		dto.setCasPoslExportu(rVal(r, CudOdberatelObjektPeer.CAS_POSL_EXPORTU).asUtilDate());
		dto.setCasPoslExportuPlan(rVal(r, CudOdberatelObjektPeer.CAS_POSL_EXPORTU_PLAN).asUtilDate());
		dto.setCasZmeny(rVal(r, CudOdberatelObjektPeer.CAS_ZMENY).asUtilDate());
		dto.setIDUcet(rVal(r, CudOdberatelObjektPeer.ID_UCET).asIntegerObj());
		dto.setAktivny(rVal(r, CudOdberatelObjektPeer.AKTIVNY).asString());

		dto.setTypPristupuNazov(_CudLookupUtils.lookupOdberatelTypPristupuNazov(dto.getTypPristupu()));
		return dto;
	}

}
