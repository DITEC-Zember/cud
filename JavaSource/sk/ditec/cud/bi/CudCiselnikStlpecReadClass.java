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
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;
import org.apache.torque.util.SqlEnum;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecLD;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;

import com.workingdogs.village.Record;

public class CudCiselnikStlpecReadClass extends _CudBaseClass {

	private DTOCiselnikStlpec createTechnickyAtribut(String nazov, String nadpis, String typ, Integer dlzka, String dbTyp, String povinny, Integer poradie) throws AppException {

		try {
			DTOCiselnikStlpec dtoNew = new DTOCiselnikStlpec();
			dtoNew.setNazov(nazov);
			dtoNew.setNadpis(nadpis);
			dtoNew.setTyp(typ);
			dtoNew.setPoradie(poradie);
			dtoNew.setDlzka(dlzka);
			dtoNew.setDbTyp(dbTyp);
			dtoNew.setPovinny(povinny);
			dtoNew.setJedinecny("F");
			dtoNew.setAktivny("T");

			return dtoNew;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createTechnickyAtribut.error");
			return null;
		}
	}

	public DTOCiselnikStlpec createTechnickyAtribut(String nazov) throws AppException {

		try {
			if (_CudConsts.NAZOV_HIST_ID.equals(nazov)) {
				return createTechnickyAtribut(_CudConsts.NAZOV_HIST_ID, "HistID", _CudConsts.CISELNIK_STLPEC_TYP_HK, 10, _CudConsts.DB_TYP_INTEGER, "T", 1);
			} else if (_CudConsts.NAZOV_PLATNOST_OD.equals(nazov)) {
				return createTechnickyAtribut(_CudConsts.NAZOV_PLATNOST_OD, "Platnosť od", _CudConsts.CISELNIK_STLPEC_TYP_AT, 1, _CudConsts.DB_TYP_DATE, "T", 2);
			} else if (_CudConsts.NAZOV_PLATNOST_DO.equals(nazov)) {
				return createTechnickyAtribut(_CudConsts.NAZOV_PLATNOST_DO, "Platnosť do", _CudConsts.CISELNIK_STLPEC_TYP_AT, 1, _CudConsts.DB_TYP_DATE, "F", 3);
			} else if (_CudConsts.NAZOV_CAS_VYTVORENIA.equals(nazov)) {
				return createTechnickyAtribut(_CudConsts.NAZOV_CAS_VYTVORENIA, "Čas vytvorenia", _CudConsts.CISELNIK_STLPEC_TYP_AT, 1, _CudConsts.DB_TYP_DATE, "T", 4);
			} else if (_CudConsts.NAZOV_CAS_ZMENY.equals(nazov)) {
				return createTechnickyAtribut(_CudConsts.NAZOV_CAS_ZMENY, "Čas zmeny", _CudConsts.CISELNIK_STLPEC_TYP_AT, 1, _CudConsts.DB_TYP_DATE, "F", 5);
			} else if (_CudConsts.NAZOV_ID_ZMENA.equals(nazov)) {
				return createTechnickyAtribut(_CudConsts.NAZOV_ID_ZMENA, "Zmena", _CudConsts.CISELNIK_STLPEC_TYP_AT, 10, _CudConsts.DB_TYP_INTEGER, "T", 6);
			} else if (_CudConsts.NAZOV_ZMAZ.equals(nazov)) {
				return createTechnickyAtribut(_CudConsts.NAZOV_ZMAZ, "Zmaž", _CudConsts.CISELNIK_STLPEC_TYP_AT, 1, _CudConsts.DB_TYP_BOOLEAN, "T", 7);
			} else if (_CudConsts.NAZOV_PK_KEY.equals(nazov)) {
				return createTechnickyAtribut(null, "ID", _CudConsts.CISELNIK_STLPEC_TYP_PK, 10, _CudConsts.DB_TYP_INTEGER, "T", 8);
			}
			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createTechnickyAtribut.error");
			return null;
		}
	}

	public String readByPrimaryKey(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setTyp(_CudConsts.CISELNIK_STLPEC_TYP_PK);

			Map<Integer, DTOCiselnikStlpec> mapDTO = mapLight(auth, dtoF, null, null);

			if (mapDTO.keySet().isEmpty()) {
				return null;
			}

			return mapDTO.get(mapDTO.keySet().iterator().next()).getNazov();

		} catch (Throwable t) {
			handleException(t, "readByPrimaryKey.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpec readLight(AuthInfo auth, Integer ciselnikStlpecID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikStlpecID)) {
				return null;
			}

			DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
			dtoF.setCiselnikStlpecID(ciselnikStlpecID);

			List<DTOCiselnikStlpec> listDTO = listLight(auth, dtoF);

			if (!listDTO.isEmpty()) {
				return listDTO.get(0);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpec readLight(AuthInfo auth, Integer ciselnikID, String nazov) throws AppException {

		try {
			DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setNazov(nazov);

			Map<Integer, DTOCiselnikStlpec> mapDTO = mapLight(auth, dtoF, null, null);

			if (mapDTO.keySet().isEmpty()) {
				return null;
			}

			return mapDTO.get(mapDTO.keySet().iterator().next());

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpecLD loadData(AuthInfo auth, DTOCiselnikStlpecLD dtoF) throws AppException {

		try {
			Set<Integer> set = new HashSet<Integer>();
			if (StringUtils.isValid(dtoF.getIDCiselnik())) {
				set.add(dtoF.getIDCiselnik());
			}
			if (StringUtils.isValid(dtoF.getFk1IDCiselnik())) {
				set.add(dtoF.getFk1IDCiselnik());
			}

			Integer[] ids = set.toArray(new Integer[set.size()]);

			DTOCiselnikStlpecLD resultDTO = new DTOCiselnikStlpecLD();

			Map<Integer, DTOCiselnik> ciselnikMap = getDelegate().getCiselnikRead().mapLight(auth, ids);
			resultDTO.setCiselnikDTO(ciselnikMap.get(dtoF.getIDCiselnik()));
			resultDTO.setFk1CiselnikDTO(ciselnikMap.get(dtoF.getFk1IDCiselnik()));

			Map<String, DTOCiselnikStlpec> csMap = mapLight(auth, dtoF.getCiselnikStlpecID(), dtoF.getFk1IDCiselnik(), dtoF.getFk1FkNazov(), null, null);
			resultDTO.setCiselnikStlpecDTO(csMap.get(dtoF.getCiselnikStlpecID().toString()));
			resultDTO.setFk1FkDTO(csMap.get(dtoF.getFk1IDCiselnik() + "*" + dtoF.getFk1FkNazov()));

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public Map<String, DTOCiselnikStlpec> mapLight(AuthInfo auth, Integer ciselnikStlpecID, Integer fk1IDCiselnik, String fk1FkNazov, Integer fk2IDCiselnik, String fk2FkNazov) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.CAS_ZMENY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_UCET);

			Criterion c = crit.getNewCriterion(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, ciselnikStlpecID, MyCriteria2.EQUAL);

			if (StringUtils.isValid(fk1IDCiselnik) && StringUtils.isValid(fk1FkNazov)) {
				Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecPeer.ID_CISELNIK, fk1IDCiselnik, MyCriteria2.EQUAL);
				Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecPeer.NAZOV, fk1FkNazov, MyCriteria2.EQUAL);
				c = c.or(c1.and(c2));
			}

			if (StringUtils.isValid(fk2IDCiselnik) && StringUtils.isValid(fk2FkNazov)) {
				Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecPeer.ID_CISELNIK, fk2IDCiselnik, MyCriteria2.EQUAL);
				Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecPeer.NAZOV, fk2FkNazov, MyCriteria2.EQUAL);
				c = c.or(c1.and(c2));
			}

			crit.add(c);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<String, DTOCiselnikStlpec> mapa = new HashMap<String, DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());
				dto.setCasZmeny(rVal(r, CudCiselnikStlpecPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudCiselnikStlpecPeer.ID_UCET).asIntegerObj());

				mapa.put(dto.getCiselnikStlpecID().toString(), dto);
				mapa.put(dto.getIDCiselnik() + "*" + dto.getNazov(), dto);
			}

			return mapa;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOCiselnikStlpec> mapLight(AuthInfo auth, DTOCiselnikStlpec dtoF, Integer[] ciselnikStlpecIDs, String[] nazovList) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnikStlpec();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, dtoF);

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.PORADIE);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JEDINECNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.POPIS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JE_DB_STRING);

			crit.addConditional(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, dtoF.getCiselnikStlpecID());
			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudCiselnikStlpecPeer.NAZOV, dtoF.getNazov(), false);
			crit.addConditional(CudCiselnikStlpecPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudCiselnikStlpecPeer.PORADIE, dtoF.getPoradie());
			crit.addConditional(CudCiselnikStlpecPeer.DLZKA, dtoF.getDlzka());
			crit.addConditional(CudCiselnikStlpecPeer.DECIMALS, dtoF.getDecimals());
			crit.addConditional(CudCiselnikStlpecPeer.DB_TYP, dtoF.getDbTyp(), false);
			crit.addConditional(CudCiselnikStlpecPeer.POVINNY, dtoF.getPovinny(), false);
			crit.addConditional(CudCiselnikStlpecPeer.JEDINECNY, dtoF.getJedinecny(), false);
			crit.addConditional(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, dtoF.getFk1IDCiselnik());
			crit.addConditional(CudCiselnikStlpecPeer.FK1_PK_NAZOV, dtoF.getFk1PkNazov(), false);
			crit.addConditional(CudCiselnikStlpecPeer.FK1_FK_NAZOV, dtoF.getFk1FkNazov(), false);
			crit.addConditional(CudCiselnikStlpecPeer.POPIS, dtoF.getPopis(), false);
			crit.addConditional(CudCiselnikStlpecPeer.JE_DB_STRING, dtoF.getJeDbString(), false);

			if (StringUtils.isValid(ciselnikStlpecIDs)) {
				if (ciselnikStlpecIDs.length == 1) {
					crit.addConditional(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, ciselnikStlpecIDs[0]);
				} else {
					crit.addIn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, ciselnikStlpecIDs);
				}
			}

			if (StringUtils.isValid(nazovList)) {
				if (nazovList.length == 1) {
					crit.addConditional(CudCiselnikStlpecPeer.NAZOV, nazovList[0]);
				} else {
					crit.addIn(CudCiselnikStlpecPeer.NAZOV, nazovList);
				}
			}

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOCiselnikStlpec> mapDTO = new HashMap<Integer, DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setPoradie(rVal(r, CudCiselnikStlpecPeer.PORADIE).asIntegerObj());
				dto.setDlzka(rVal(r, CudCiselnikStlpecPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecPeer.DECIMALS).asIntegerObj());
				dto.setDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setPovinny(rVal(r, CudCiselnikStlpecPeer.POVINNY).asString());
				dto.setJedinecny(rVal(r, CudCiselnikStlpecPeer.JEDINECNY).asString());
				dto.setFk1IDCiselnik(rVal(r, CudCiselnikStlpecPeer.FK1_ID_CISELNIK).asIntegerObj());
				dto.setFk1PkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_PK_NAZOV).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_FK_NAZOV).asString());
				dto.setPopis(rVal(r, CudCiselnikStlpecPeer.POPIS).asString());
				dto.setJeDbString(rVal(r, CudCiselnikStlpecPeer.JE_DB_STRING).asString());

				dto.setListSize(lp.size());

				mapDTO.put(dto.getCiselnikStlpecID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpec read(AuthInfo auth, Integer ciselnikStlpecID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikStlpecID)) {
				return null;
			}

			DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
			dtoF.setCiselnikStlpecID(ciselnikStlpecID);
			return list(auth, new Page(true), dtoF)[0];

		} catch (Throwable t) {
			handleException(t, "read.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpec[] list(AuthInfo auth, Page page, DTOCiselnikStlpec dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnikStlpec();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, dtoF);

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.PORADIE);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JEDINECNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.AKTIVNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JE_DB_STRING);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.POPIS);

			// join Cud_CISELNIK
			crit.addAlias("t1", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("ciselnikNazov", "t1.NAZOV");
			crit.addAsColumn("ciselnikTyp", "t1.TYP");
			crit.addJoin(CudCiselnikStlpecPeer.ID_CISELNIK, "t1.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			// join Cud_CISELNIK
			crit.addAlias("t2", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fkCiselnikNazov", "t2.NAZOV");
			crit.addJoin(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, "t2.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			// where
			crit.addConditional(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, dtoF.getCiselnikStlpecID());
			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudCiselnikStlpecPeer.NAZOV, dtoF.getNazov(), true);
			crit.addConditional(CudCiselnikStlpecPeer.NADPIS, dtoF.getNadpis(), true);
			crit.addConditional(CudCiselnikStlpecPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudCiselnikStlpecPeer.DB_TYP, dtoF.getDbTyp(), false);
			crit.addConditional(CudCiselnikStlpecPeer.AKTIVNY, dtoF.getAktivny(), false);
			crit.addConditional(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, dtoF.getFk1IDCiselnik());
			crit.addConditional(CudCiselnikStlpecPeer.FK1_FK_NAZOV, dtoF.getFk1FkNazov(), false);
			crit.addConditional("t1.AKTIVNY", "T", false);

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOCiselnikStlpec> listDTO = new ArrayList<DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());
				dto.setTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setPoradie(rVal(r, CudCiselnikStlpecPeer.PORADIE).asIntegerObj());
				dto.setDlzka(rVal(r, CudCiselnikStlpecPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecPeer.DECIMALS).asIntegerObj());
				dto.setDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setPovinny(rVal(r, CudCiselnikStlpecPeer.POVINNY).asString());
				dto.setJedinecny(rVal(r, CudCiselnikStlpecPeer.JEDINECNY).asString());
				dto.setAktivny(rVal(r, CudCiselnikStlpecPeer.AKTIVNY).asString());
				dto.setJeDbString(rVal(r, CudCiselnikStlpecPeer.JE_DB_STRING).asString());
				dto.setFk1IDCiselnik(rVal(r, CudCiselnikStlpecPeer.FK1_ID_CISELNIK).asIntegerObj());
				dto.setFk1PkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_PK_NAZOV).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_FK_NAZOV).asString());
				dto.setPopis(rVal(r, CudCiselnikStlpecPeer.POPIS).asString());

				dto.setCiselnikNazov(rVal(r, "ciselnikNazov").asString());
				dto.setCiselnikTyp(rVal(r, "ciselnikTyp").asString());

				dto.setFk1CiselnikNazov(rVal(r, "fkCiselnikNazov").asString());

				dto.setTechnicky(_CudKontrolaUtils.jeAtributTechnicky(dto) ? "T" : "F");

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpec[] listForPopup(AuthInfo auth, Page page, DTOCiselnikStlpec dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnikStlpec();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, dtoF);

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);

			// join Cud_CISELNIK
			crit.addAsColumn("ciselnikNazov", CudCiselnikPeer.NAZOV);
			crit.addJoin(CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudCiselnikPeer.AKTIVNY, dtoF.getCiselnikAktivny(), false);

			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			// crit.addConditional(CudCiselnikStlpecPeer.NAZOV, dtoF.getNazov(), true);
			crit.addConditional(CudCiselnikStlpecPeer.NADPIS, dtoF.getNadpis(), true);
			crit.addConditional(CudCiselnikStlpecPeer.AKTIVNY, dtoF.getAktivny(), false);

			Set<String> set = new HashSet<String>();
			if ("F".equals(dtoF.getTechnicky())) {
				set.addAll(new ArrayList<String>(Arrays.asList(new String[] { _CudConsts.NAZOV_HIST_ID, _CudConsts.NAZOV_PLATNOST_OD, _CudConsts.NAZOV_PLATNOST_DO, _CudConsts.NAZOV_CAS_VYTVORENIA, _CudConsts.NAZOV_CAS_ZMENY, _CudConsts.NAZOV_ID_ZMENA, _CudConsts.NAZOV_ZMAZ })));
			}
			if (StringUtils.isValid(dtoF.getNotInNazovList())) {
				set.addAll(new ArrayList<String>(Arrays.asList(dtoF.getNotInNazovList())));
			}
			if (!set.isEmpty()) {
				Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecPeer.NAZOV, set.toArray(new String[set.size()]), MyCriteria2.NOT_IN);
				if (StringUtils.isValid(dtoF.getNazov())) {
					Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecPeer.NAZOV, CudCiselnikStlpecPeer.NAZOV + " LIKE \'" + dtoF.getNazov() + "%\'", MyCriteria2.CUSTOM);
					c1 = c1.and(c2);
				}
				crit.add(c1);
			} else {
				crit.addConditional(CudCiselnikStlpecPeer.NAZOV, dtoF.getNazov(), true);
			}

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOCiselnikStlpec> listDTO = new ArrayList<DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());

				dto.setCiselnikNazov(rVal(r, "ciselnikNazov").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForPopup.error", auth);
			return null;
		}
	}

	public String deleteKontrola(AuthInfo auth, Integer ciselnikID, Integer ciselnikStlpecID) throws AppException {

		try {
			Integer pocet = getDelegate().getGuiRead().ciselnikStlpecGuiPocet(auth, ciselnikStlpecID);
			if (StringUtils.isValid(pocet) && pocet.intValue() != 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_501);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "deleteKontrola.error", auth);
			return null;
		}
	}

	public Map<String, List<DTOCiselnikStlpec>> mapForPocetnost(AuthInfo auth, Integer fk1IdCiselnik) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String sql1 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

				crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
				crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
				crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
				crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
				crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
				crit.addSelectColumn(CudCiselnikStlpecPeer.PORADIE);

				crit.addAsColumn("ciselnik_tabulka", CudCiselnikPeer.TABULKA);
				crit.addAsColumn("ciselnik_nazov", CudCiselnikPeer.NAZOV);
				crit.addJoin(CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);
				crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

				crit.addConditional(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, fk1IdCiselnik);
				crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

				sql1 = crit.getSQL();
			}

			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

				crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);

				crit.addConditional(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, fk1IdCiselnik);
				crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
				subSql = StringUtils.replaceAll(subSql, CudCiselnikStlpecPeer.TABLE_NAME, "t1");
				subSql = StringUtils.replaceAll(subSql, "t1" + " ", CudCiselnikStlpecPeer.TABLE_NAME + " " + "t1" + " ");

			}

			String sql2 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

				crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
				crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
				crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
				crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
				crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
				crit.addSelectColumn(CudCiselnikStlpecPeer.PORADIE);

				crit.addAsColumn("ciselnik_tabulka", CudCiselnikPeer.TABULKA);
				crit.addAsColumn("ciselnik_nazov", CudCiselnikPeer.NAZOV);
				crit.addJoin(CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);
				crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

				crit.addCustomSql(CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikStlpecPeer.ID_CISELNIK + " IN (" + subSql + ")");

				Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecPeer.NAZOV, new String[] { _CudConsts.NAZOV_HIST_ID, _CudConsts.NAZOV_ZMAZ, _CudConsts.NAZOV_PLATNOST_OD, _CudConsts.NAZOV_PLATNOST_DO }, MyCriteria2.IN);
				Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecPeer.TYP, _CudConsts.CISELNIK_STLPEC_TYP_PK, MyCriteria2.EQUAL);
				crit.add(c1.or(c2));

				crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

				sql2 = crit.getSQL();
			}

			String sql = sql1 + " UNION " + sql2 + " ORDER BY " + trimColumnName(CudCiselnikStlpecPeer.ID_CISELNIK) + " ASC, " + trimColumnName(CudCiselnikStlpecPeer.PORADIE) + " ASC";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<String, List<DTOCiselnikStlpec>> resultMap = new HashMap<String, List<DTOCiselnikStlpec>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());

				dto.setCiselnikNazov(rVal(r, "ciselnik_nazov").asString());
				dto.setCiselnikTabulka(rVal(r, "ciselnik_tabulka").asString());

				if (!StringUtils.isValid(resultMap.get(dto.getCiselnikTabulka()))) {
					resultMap.put(dto.getCiselnikTabulka(), new ArrayList<DTOCiselnikStlpec>());
				}
				resultMap.get(dto.getCiselnikTabulka()).add(dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "mapForPocetnost.error", auth);
			return null;
		}
	}

	public List<DTOCiselnikStlpec> listLight(AuthInfo auth, Integer ciselnikID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return new ArrayList<DTOCiselnikStlpec>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);

			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, ciselnikID);

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpec> listDTO = new ArrayList<DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());
				dto.setTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setDecimals(rVal(r, CudCiselnikStlpecPeer.DECIMALS).asIntegerObj());
				dto.setFk1IDCiselnik(rVal(r, CudCiselnikStlpecPeer.FK1_ID_CISELNIK).asIntegerObj());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public List<DTOCiselnikStlpec> listLight(AuthInfo auth, DTOCiselnikStlpec dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnikStlpec();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, dtoF);

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.PORADIE);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JEDINECNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.AKTIVNY);

			crit.addConditional(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, dtoF.getCiselnikStlpecID());
			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudCiselnikStlpecPeer.NAZOV, dtoF.getNazov(), false);
			crit.addConditional(CudCiselnikStlpecPeer.NADPIS, dtoF.getNadpis(), false);
			crit.addConditional(CudCiselnikStlpecPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudCiselnikStlpecPeer.PORADIE, dtoF.getPoradie());
			crit.addConditional(CudCiselnikStlpecPeer.DLZKA, dtoF.getDlzka());
			crit.addConditional(CudCiselnikStlpecPeer.DECIMALS, dtoF.getDecimals());
			crit.addConditional(CudCiselnikStlpecPeer.DB_TYP, dtoF.getTyp(), false);
			crit.addConditional(CudCiselnikStlpecPeer.POVINNY, dtoF.getPovinny(), false);
			crit.addConditional(CudCiselnikStlpecPeer.JEDINECNY, dtoF.getJedinecny());
			crit.addConditional(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, dtoF.getFk1IDCiselnik());
			crit.addConditional(CudCiselnikStlpecPeer.FK1_PK_NAZOV, dtoF.getFk1PkNazov(), false);
			crit.addConditional(CudCiselnikStlpecPeer.FK1_FK_NAZOV, dtoF.getFk1FkNazov(), false);

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpec> listDTO = new ArrayList<DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());
				dto.setTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setPoradie(rVal(r, CudCiselnikStlpecPeer.PORADIE).asIntegerObj());
				dto.setDlzka(rVal(r, CudCiselnikStlpecPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecPeer.DECIMALS).asIntegerObj());
				dto.setDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setPovinny(rVal(r, CudCiselnikStlpecPeer.POVINNY).asString());
				dto.setJedinecny(rVal(r, CudCiselnikStlpecPeer.JEDINECNY).asString());
				dto.setFk1IDCiselnik(rVal(r, CudCiselnikStlpecPeer.FK1_ID_CISELNIK).asIntegerObj());
				dto.setFk1PkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_PK_NAZOV).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_FK_NAZOV).asString());
				dto.setAktivny(rVal(r, CudCiselnikStlpecPeer.AKTIVNY).asString());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public List<DTOCiselnikStlpec> vratStlpcePreCiselnik(AuthInfo auth, Integer ciselnikID) throws AppException {
		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return new ArrayList<DTOCiselnikStlpec>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());
			CudCiselnikStlpecPeer.addSelectColumns(crit);

			crit.addJoin(CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, SqlEnum.LEFT_JOIN);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);

			crit.add(CudCiselnikStlpecPeer.ID_CISELNIK, ciselnikID);

			Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecPeer.TYP, new String[] { "PK", "HK" }, MyCriteria2.IN);

			Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecPeer.NAZOV, new String[] { _CudConsts.NAZOV_PLATNOST_OD, _CudConsts.NAZOV_PLATNOST_DO, _CudConsts.NAZOV_ZMAZ }, MyCriteria2.IN);

			Criterion c3 = crit.getNewCriterion(CudCiselnikStlpecPeer.NAZOV, _CudConsts.NAZOV_TECHNICKY_STLPEC_LIST, MyCriteria2.NOT_IN);

			crit.add(c1.or(c2.or(c3)));

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpec> listDTO = new ArrayList<DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());
				dto.setTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setDecimals(rVal(r, CudCiselnikStlpecPeer.DECIMALS).asIntegerObj());

				dto.setCiselnikTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "vratStlpcePreCiselnik.error", auth);
			return null;
		}
	}

	public Map<Integer, List<DTOCiselnikStlpec>> mapLightForPau(AuthInfo auth) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, new DTOCiselnikStlpec());

				crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);

				crit.addConditional(CudCiselnikPeer.AKTIVNY, "T", false);
				crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.PORADIE);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JEDINECNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JE_DB_STRING);

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addCustomSql(CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikStlpecPeer.ID_CISELNIK + " IN (" + subSql + ")");

			crit.addAscendingOrderByColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addAscendingOrderByColumn(CudCiselnikStlpecPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOCiselnikStlpec>> resultMap = new HashMap<Integer, List<DTOCiselnikStlpec>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());
				dto.setTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setPoradie(rVal(r, CudCiselnikStlpecPeer.PORADIE).asIntegerObj());
				dto.setDlzka(rVal(r, CudCiselnikStlpecPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecPeer.DECIMALS).asIntegerObj());
				dto.setDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setPovinny(rVal(r, CudCiselnikStlpecPeer.POVINNY).asString());
				dto.setJedinecny(rVal(r, CudCiselnikStlpecPeer.JEDINECNY).asString());
				dto.setFk1IDCiselnik(rVal(r, CudCiselnikStlpecPeer.FK1_ID_CISELNIK).asIntegerObj());
				dto.setFk1PkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_PK_NAZOV).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_FK_NAZOV).asString());
				dto.setJeDbString(rVal(r, CudCiselnikStlpecPeer.JE_DB_STRING).asString());

				if ("T".equals(dto.getJeDbString())) {
					dto.setDbTyp(_CudConsts.DB_TYP_STRING);
					dto.setJeDbString(null);
				}

				if (!StringUtils.isValid(resultMap.get(dto.getIDCiselnik()))) {
					resultMap.put(dto.getIDCiselnik(), new ArrayList<DTOCiselnikStlpec>());
				}
				resultMap.get(dto.getIDCiselnik()).add(dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "mapLightForPau.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOCiselnikStlpec> mapForPrint(AuthInfo auth, Integer ciselnikID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return new HashMap<Integer, DTOCiselnikStlpec>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_FK_NAZOV);

			// join CUD_CISELNIK
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addJoin(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, ciselnikID);

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOCiselnikStlpec> resultDTO = new HashMap<Integer, DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setDecimals(rVal(r, CudCiselnikStlpecPeer.DECIMALS).asIntegerObj());
				dto.setFk1IDCiselnik(rVal(r, CudCiselnikStlpecPeer.FK1_ID_CISELNIK).asIntegerObj());
				dto.setFk1PkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_PK_NAZOV).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_FK_NAZOV).asString());

				dto.setFk1CiselnikTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());

				resultDTO.put(dto.getCiselnikStlpecID(), dto);
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "mapForPrint.error", auth);
			return null;
		}
	}

	public List<DTOCiselnikStlpec> list(AuthInfo auth, Integer ciselnikID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JE_DB_STRING);
			crit.addSelectColumn(CudCiselnikStlpecPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JEDINECNY);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.POPIS);

			// join Cud_CISELNIK
			crit.addAlias("t1", CudCiselnikPeer.TABLE_NAME);
			crit.addJoin(CudCiselnikStlpecPeer.ID_CISELNIK, "t1.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			// join Cud_CISELNIK
			crit.addAlias("t2", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fkCiselnikTabulka", "t2.TABULKA");
			crit.addJoin(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, "t2.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, ciselnikID);

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpec> listDTO = new ArrayList<DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setNadpis(rVal(r, CudCiselnikStlpecPeer.NADPIS).asString());
				dto.setTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setDlzka(rVal(r, CudCiselnikStlpecPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecPeer.DECIMALS).asIntegerObj());
				dto.setDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setJeDbString(rVal(r, CudCiselnikStlpecPeer.JE_DB_STRING).asString());
				dto.setPovinny(rVal(r, CudCiselnikStlpecPeer.POVINNY).asString());
				dto.setJedinecny(rVal(r, CudCiselnikStlpecPeer.JEDINECNY).asString());
				dto.setFk1IDCiselnik(rVal(r, CudCiselnikStlpecPeer.FK1_ID_CISELNIK).asIntegerObj());
				dto.setFk1PkNazov(rVal(r, CudCiselnikStlpecPeer.FK1_PK_NAZOV).asString());
				dto.setPopis(rVal(r, CudCiselnikStlpecPeer.POPIS).asString());

				dto.setFk1CiselnikTabulka(rVal(r, "fkCiselnikTabulka").asString());

				dto.setListSize(lp.size());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public boolean jeCiselnikStlpecAktivny(AuthInfo auth, Integer ciselnikStlpecID) throws AppException {
		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());
			crit.addSelectColumn(CudCiselnikStlpecPeer.AKTIVNY);

			crit.add(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, ciselnikStlpecID, MyCriteria2.EQUAL);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, true, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Record r = (Record) iter.next();
			String aktivny = rVal(r, CudCiselnikStlpecPeer.AKTIVNY).asString();
			return "T".equals(aktivny);
		} catch (Throwable t) {
			handleException(t, "jeCiselnikStlpecAktivny.error", auth);
			return false;
		}
	}

	public Map<String, Integer> map(AuthInfo auth, Integer ciselnikID, String[] nazovList) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikID) || !StringUtils.isValid(nazovList)) {
				return new HashMap<String, Integer>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);

			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, ciselnikID);

			if (nazovList.length == 1) {
				crit.addConditional(CudCiselnikStlpecPeer.NAZOV, nazovList[0]);
			} else {
				crit.addIn(CudCiselnikStlpecPeer.NAZOV, nazovList);
			}

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<String, Integer> mapDTO = new HashMap<String, Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Integer ciselnikStrlpecID = rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj();
				String nazov = rVal(r, CudCiselnikStlpecPeer.NAZOV).asString();

				mapDTO.put(nazov, ciselnikStrlpecID);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public Set<Integer> ids(AuthInfo auth, Integer ciselnikID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return new HashSet<Integer>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);

			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, ciselnikID);

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> resultSet = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				resultSet.add(rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj());
			}

			return resultSet;

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}
}
