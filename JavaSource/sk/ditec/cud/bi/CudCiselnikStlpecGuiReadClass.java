package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOCiselnikStlpecGuiLD;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikGuiPeer;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecGuiPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;

import com.workingdogs.village.Record;

public class CudCiselnikStlpecGuiReadClass extends _CudBaseClass {

	public DTOCiselnikStlpecGui readLight(AuthInfo auth, Integer ciselnikStlpecGuiID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikStlpecGuiID)) {
				return null;
			}

			DTOCiselnikStlpecGui dtoF = new DTOCiselnikStlpecGui();
			dtoF.setCiselnikStlpecGuiID(ciselnikStlpecGuiID);
			return mapLight(auth, dtoF, null).values().iterator().next().get(0);

		} catch (Throwable t) {
			DBUtils.handleException(t, "readLight.error");
			return null;
		}
	}

	public Map<Integer, List<DTOCiselnikStlpecGui>> mapLight(AuthInfo auth, Integer... ciselnikGuiIDs) throws AppException {

		try {
			Set<Integer> set = new HashSet<Integer>();
			for (Integer ciselnikGuiID : ciselnikGuiIDs) {
				if (StringUtils.isValid(ciselnikGuiID)) {
					set.add(ciselnikGuiID);
				}
			}

			if (set.isEmpty()) {
				return null;
			}

			Integer[] poleIDs = set.toArray(new Integer[set.size()]);

			return mapLight(auth, null, poleIDs);

		} catch (Throwable t) {
			DBUtils.handleException(t, "mapLight.error");
			return null;
		}
	}

	public List<DTOCiselnikStlpecGui> listLight(AuthInfo auth, Integer ciselnikGuiID) throws AppException {

		try {
			DTOCiselnikStlpecGui dtoF = new DTOCiselnikStlpecGui();
			dtoF.setIDCiselnikGui(ciselnikGuiID);

			Map<Integer, List<DTOCiselnikStlpecGui>> resultMap = mapLight(auth, dtoF, null);

			return resultMap.get(ciselnikGuiID);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	private Map<Integer, List<DTOCiselnikStlpecGui>> mapLight(AuthInfo auth, DTOCiselnikStlpecGui dtoF, Integer[] ciselnikGuiIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnikStlpecGui();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, dtoF);

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.PORADIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZMENA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZAROVNANIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.EDIT_CONTROL);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.REG_EXP);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPIS);

			crit.addConditional(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, dtoF.getCiselnikStlpecGuiID());
			crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, dtoF.getIDCiselnikGui());
			crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, dtoF.getIDCiselnikStlpec());
			crit.addConditional(CudCiselnikStlpecGuiPeer.NADPIS, dtoF.getNadpis(), true);
			crit.addConditional(CudCiselnikStlpecGuiPeer.ZMENA, dtoF.getZmena(), false);
			crit.addConditional(CudCiselnikStlpecGuiPeer.POVINNY, dtoF.getPovinny(), false);

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			if (StringUtils.isValid(ciselnikGuiIDs)) {
				if (ciselnikGuiIDs.length == 1) {
					crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, ciselnikGuiIDs[0]);
				} else {
					crit.addIn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, ciselnikGuiIDs);
				}
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOCiselnikStlpecGui>> mapDTO = new HashMap<Integer, List<DTOCiselnikStlpecGui>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikGui(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setNadpis(rVal(r, CudCiselnikStlpecGuiPeer.NADPIS).asString());
				dto.setPoradie(rVal(r, CudCiselnikStlpecGuiPeer.PORADIE).asIntegerObj());
				dto.setDlzka(rVal(r, CudCiselnikStlpecGuiPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecGuiPeer.DECIMALS).asIntegerObj());
				dto.setZmena(rVal(r, CudCiselnikStlpecGuiPeer.ZMENA).asString());
				dto.setPovinny(rVal(r, CudCiselnikStlpecGuiPeer.POVINNY).asString());
				dto.setZarovnanie(rVal(r, CudCiselnikStlpecGuiPeer.ZAROVNANIE).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV).asString());
				dto.setFk2IDCiselnik(rVal(r, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK).asIntegerObj());
				dto.setFk2PkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV).asString());
				dto.setFk2FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV).asString());
				dto.setListSirka(rVal(r, CudCiselnikStlpecGuiPeer.LIST_SIRKA).asIntegerObj());
				dto.setListSirkaChange(rVal(r, CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE).asString());
				dto.setListZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE).asString());
				dto.setFormSirka(rVal(r, CudCiselnikStlpecGuiPeer.FORM_SIRKA).asIntegerObj());
				dto.setFormZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE).asString());
				dto.setPopupSirka(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_SIRKA).asIntegerObj());
				dto.setPopupSirkaChange(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE).asString());
				dto.setPopupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE).asString());
				dto.setLookupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE).asString());
				dto.setEditControl(rVal(r, CudCiselnikStlpecGuiPeer.EDIT_CONTROL).asString());
				dto.setRegExp(rVal(r, CudCiselnikStlpecGuiPeer.REG_EXP).asString());
				dto.setPopis(rVal(r, CudCiselnikStlpecGuiPeer.POPIS).asString());

				if (!StringUtils.isValid(mapDTO.get(dto.getIDCiselnikGui()))) {
					mapDTO.put(dto.getIDCiselnikGui(), new ArrayList<DTOCiselnikStlpecGui>());
				}
				mapDTO.get(dto.getIDCiselnikGui()).add(dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	private String ciselnikGuiSql(Integer ciselnikID, Date platnostOd) throws AppException {

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

			crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);

			crit.addConditional(CudCiselnikGuiPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudCiselnikGuiPeer.STAV, _CudConsts.CISELNIK_GUI_STAV_PUB);

			crit.addConditional(CudCiselnikGuiPeer.PLATNOST_OD, platnostOd, MyCriteria2.LESS_EQUAL);

			Criterion c1 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, platnostOd, MyCriteria2.GREATER_EQUAL);
			Criterion c2 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			crit.add(c1.or(c2));

			crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			return crit.getSQL();

		} catch (Throwable t) {
			DBUtils.handleException(t, "ciselnikGuiSql.error");
			return null;
		}
	}

	private String ciselnikStlpecSql(String columnName, String fkIdCiselnikNazov, String fkFkNazovNazov, String alias) throws AppException {

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());

			crit.addSelectColumn(columnName);

			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, 11111);
			crit.addConditional(CudCiselnikStlpecPeer.NAZOV, "22222", false);

			String sql = crit.getSQL();
			sql = StringUtils.replaceAll(sql, CudCiselnikStlpecPeer.TABLE_NAME, alias);
			sql = StringUtils.replaceAll(sql, alias + " ", CudCiselnikStlpecPeer.TABLE_NAME + " " + alias + " ");

			sql = StringUtils.replaceAll(sql, "11111", fkIdCiselnikNazov);
			sql = StringUtils.replaceAll(sql, "\'22222\'", fkFkNazovNazov);

			return sql;

		} catch (Throwable t) {
			DBUtils.handleException(t, "ciselnikStlpecSql.error");
			return null;
		}
	}

	private String ciselnikStlpecGuiSql(String columnName, Date platnostOd, String fkIdCiselnikNazov, String fkFkNazovNazov) throws AppException {

		try {
			String subSql1 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

				crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);

				crit.addConditional(CudCiselnikGuiPeer.STAV, _CudConsts.CISELNIK_GUI_STAV_PUB, false);

				crit.addCustomSql(CudCiselnikGuiPeer.ID_CISELNIK, CudCiselnikGuiPeer.ID_CISELNIK + " = " + fkIdCiselnikNazov);

				crit.addConditional(CudCiselnikGuiPeer.PLATNOST_OD, platnostOd, MyCriteria2.LESS_EQUAL);

				Criterion c1 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, platnostOd, MyCriteria2.GREATER_EQUAL);
				Criterion c2 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
				crit.add(c1.or(c2));

				crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql1 = crit.getSQL();
				subSql1 = StringUtils.replaceAll(subSql1, CudCiselnikGuiPeer.TABLE_NAME, "a2");
				subSql1 = StringUtils.replaceAll(subSql1, "a2 ", CudCiselnikGuiPeer.TABLE_NAME + " a2 ");
			}

			String subSql2 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikGui());

				crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);

				crit.addCustomSql(CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikStlpecPeer.ID_CISELNIK + " = A1a2B3b4");
				crit.addCustomSql(CudCiselnikStlpecPeer.NAZOV, CudCiselnikStlpecPeer.NAZOV + " = X1x2Y3y4");
				crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql2 = crit.getSQL();
				subSql2 = StringUtils.replaceAll(subSql2, CudCiselnikStlpecPeer.TABLE_NAME, "a3");
				subSql2 = StringUtils.replaceAll(subSql2, "a3 ", CudCiselnikStlpecPeer.TABLE_NAME + " a3 ");
				subSql2 = StringUtils.replaceAll(subSql2, "A1a2B3b4", fkIdCiselnikNazov);
				subSql2 = StringUtils.replaceAll(subSql2, "X1x2Y3y4", fkFkNazovNazov);
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikGui());

			crit.addSelectColumn(columnName);

			crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, 11111);
			crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, 22222);

			String sql = crit.getSQL();
			sql = StringUtils.replaceAll(sql, CudCiselnikStlpecGuiPeer.TABLE_NAME, "a1");
			sql = StringUtils.replaceAll(sql, "a1 ", CudCiselnikStlpecGuiPeer.TABLE_NAME + " a1 ");

			sql = StringUtils.replaceAll(sql, "11111", "(" + subSql1 + ")");
			sql = StringUtils.replaceAll(sql, "22222", "(" + subSql2 + ")");

			return sql;

		} catch (Throwable t) {
			DBUtils.handleException(t, "ciselnikStlpecGuiSql.error");
			return null;
		}
	}

	public DTOCiselnikStlpecGui[] listForData(AuthInfo auth, DTOCiselnikStlpecGui dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE);

			String s1 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DLZKA, dtoF.getPlatnostOd(), CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			s1 = "WHEN " + CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK + " IS NOT NULL THEN (" + s1 + ")";
			String s2 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DLZKA, dtoF.getPlatnostOd(), "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			s2 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s2 + ")";
			String s3 = "ELSE " + CudCiselnikStlpecGuiPeer.DLZKA;
			crit.addAsColumn("dlzka", "CASE " + s1 + " " + s2 + " " + s3 + " END");

			s1 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DECIMALS, dtoF.getPlatnostOd(), CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			s1 = "WHEN " + CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK + " IS NOT NULL THEN (" + s1 + ")";
			s2 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DECIMALS, dtoF.getPlatnostOd(), "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			s2 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s2 + ")";
			s3 = "ELSE " + CudCiselnikStlpecGuiPeer.DECIMALS;
			crit.addAsColumn("decimals", "CASE " + s1 + " " + s2 + " " + s3 + " END");

			s1 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV, "c");
			s1 = "WHEN " + CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK + " IS NOT NULL THEN (" + s1 + ")";
			s2 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV, "d");
			s2 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s2 + ")";
			s3 = "ELSE " + "t1.DB_TYP";
			crit.addAsColumn("db_typ", "CASE " + s1 + " " + s2 + " " + s3 + " END");

			s1 = ciselnikStlpecSql(CudCiselnikStlpecPeer.JE_DB_STRING, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV, "e");
			s1 = "WHEN " + CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK + " IS NOT NULL THEN (" + s1 + ")";
			s2 = ciselnikStlpecSql(CudCiselnikStlpecPeer.JE_DB_STRING, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV, "f");
			s2 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s2 + ")";
			s3 = "ELSE " + "t1.JE_DB_STRING";
			crit.addAsColumn("je_db_string", "CASE " + s1 + " " + s2 + " " + s3 + " END");

			// join CUD_CISELNIK_STLPEC
			crit.addAlias("t1", CudCiselnikStlpecPeer.TABLE_NAME);
			crit.addAsColumn("nazov", "t1.NAZOV");
			crit.addAsColumn("typ", "t1.TYP");
			crit.addAsColumn("fk1_id_ciselnik", "t1.FK1_ID_CISELNIK");
			crit.addAsColumn("fk1_pk_nazov", "t1.FK1_PK_NAZOV");
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, "t1.CISELNIK_STLPEC_ID", MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addAlias("t2", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fk1_ciselnik_tabulka", "t2.tabulka");
			crit.addJoin("t1.FK1_ID_CISELNIK", "t2.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addAlias("t3", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fk2_ciselnik_tabulka", "t3.tabulka");
			crit.addJoin(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, "t3.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			String subSql = ciselnikGuiSql(dtoF.getCiselnikStlpecIDCiselnik(), dtoF.getPlatnostOd());
			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " = (" + subSql + ")");

			if ("T".equals(dtoF.getPopupZobrazenie())) {
				Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
				Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
				Criterion c3 = crit.getNewCriterion("t1.nazov", dtoF.getLookupColumnName(), MyCriteria2.EQUAL);
				crit.add(c1.or(c2).or(c3));
			}

			crit.addConditional(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE, dtoF.getListZobrazenie(), false);
			crit.addConditional(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, dtoF.getFormZobrazenie(), false);

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecGuiPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpecGui> listDTO = new ArrayList<DTOCiselnikStlpecGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV).asString());
				dto.setFk2IDCiselnik(rVal(r, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK).asIntegerObj());
				dto.setFk2PkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV).asString());
				dto.setFk2FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV).asString());
				dto.setListZobrazenie("T".equals(dtoF.getListZobrazenie()) ? rVal(r, CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE).asString() : null);
				dto.setPopupZobrazenie("T".equals(dtoF.getPopupZobrazenie()) ? rVal(r, CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE).asString() : null);
				dto.setLookupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE).asString());

				dto.setDlzka(rVal(r, "dlzka").asIntegerObj());
				dto.setDecimals(rVal(r, "decimals").asIntegerObj());

				dto.setCiselnikStlpecDbTyp(rVal(r, "db_typ").asString());
				dto.setCiselnikStlpecJeDbString(rVal(r, "je_db_string").asString());
				dto.setCiselnikStlpecNazov(rVal(r, "nazov").asString());
				dto.setCiselnikStlpecTyp(rVal(r, "typ").asString());
				dto.setCiselnikStlpecFk1IDCiselnik(rVal(r, "fk1_id_ciselnik").asIntegerObj());
				dto.setCiselnikStlpecFk1CiselnikTabulka(rVal(r, "fk1_ciselnik_tabulka").asString());
				dto.setCiselnikStlpecFk1PkNazov(rVal(r, "fk1_pk_nazov").asString());

				dto.setFk2CiselnikTabulka(rVal(r, "fk2_ciselnik_tabulka").asString());

				dto.setListSize(lp.size());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikStlpecGui[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForData.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpecGui[] listForList(AuthInfo auth, Integer ciselnikID, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZAROVNANIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.EDIT_CONTROL);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.REG_EXP);

			String s1 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV, "a");
			s1 = "WHEN " + CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK + " IS NOT NULL THEN (" + s1 + ")";
			String s2 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV, "b");
			s2 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s2 + ")";
			String s3 = "ELSE " + "t1.DB_TYP";
			crit.addAsColumn("db_typ", "CASE " + s1 + " " + s2 + " " + s3 + " END");

			// join CUD_CISELNIK_STLPEC
			crit.addAlias("t1", CudCiselnikStlpecPeer.TABLE_NAME);
			crit.addAsColumn("typ", "t1.TYP");
			crit.addAsColumn("jedinecny", "t1.JEDINECNY");
			crit.addAsColumn("fk1_id_ciselnik", "t1.FK1_ID_CISELNIK");
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, "t1.CISELNIK_STLPEC_ID", MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addAlias("t2", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fk1_ciselnik_nazov", "t2.NAZOV");
			crit.addAsColumn("fk1_ciselnik_tabulka", "t2.TABULKA");
			crit.addJoin("t1.FK1_ID_CISELNIK", "t2.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addAlias("t3", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fk2_ciselnik_nazov", "t3.NAZOV");
			crit.addAsColumn("fk2_ciselnik_tabulka", "t3.TABULKA");
			crit.addJoin(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, "t3.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			String subSql = ciselnikGuiSql(ciselnikID, platnostOd);
			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " = (" + subSql + ")");

			crit.addConditional(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE, "T", false);

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecGuiPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpecGui> listDTO = new ArrayList<DTOCiselnikStlpecGui>();
			int poradie = 1;

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setNadpis(rVal(r, CudCiselnikStlpecGuiPeer.NADPIS).asString());
				dto.setZarovnanie(rVal(r, CudCiselnikStlpecGuiPeer.ZAROVNANIE).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV).asString());
				dto.setFk2IDCiselnik(rVal(r, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK).asIntegerObj());
				dto.setFk2FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV).asString());
				dto.setListZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE).asString());
				dto.setListSirka(rVal(r, CudCiselnikStlpecGuiPeer.LIST_SIRKA).asIntegerObj());
				dto.setListSirkaChange(rVal(r, CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE).asString());
				dto.setEditControl(rVal(r, CudCiselnikStlpecGuiPeer.EDIT_CONTROL).asString());
				dto.setRegExp(rVal(r, CudCiselnikStlpecGuiPeer.REG_EXP).asString());

				dto.setCiselnikStlpecDbTyp(rVal(r, "db_typ").asString());
				dto.setCiselnikStlpecTyp(rVal(r, "typ").asString());
				dto.setCiselnikStlpecJedinecny(rVal(r, "jedinecny").asString());
				dto.setCiselnikStlpecFk1IDCiselnik(rVal(r, "fk1_id_ciselnik").asIntegerObj());

				dto.setCiselnikStlpecFk1CiselnikNazov(rVal(r, "fk1_ciselnik_nazov").asString());
				dto.setCiselnikStlpecFk1CiselnikTabulka(rVal(r, "fk1_ciselnik_tabulka").asString());

				dto.setFk2CiselnikNazov(rVal(r, "fk2_ciselnik_nazov").asString());
				dto.setFk2CiselnikTabulka(rVal(r, "fk2_ciselnik_tabulka").asString());

				dto.setPoradie(poradie++);

				dto.setListSize(lp.size());

				_CudLookupUtils.lookupDTOCiselnikStlpecGui(dto);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikStlpecGui[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForList.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpecGui[] listForPop(AuthInfo auth, Integer ciselnikID, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZAROVNANIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE);

			String s1 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV, "a");
			s1 = "WHEN " + CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK + " IS NOT NULL THEN (" + s1 + ")";
			String s2 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, CudCiselnikStlpecPeer.FK1_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV, "b");
			s2 = "WHEN " + CudCiselnikStlpecPeer.FK1_ID_CISELNIK + " IS NOT NULL THEN (" + s2 + ")";
			String s3 = "ELSE " + CudCiselnikStlpecPeer.DB_TYP;
			crit.addAsColumn("db_typ", "CASE " + s1 + " " + s2 + " " + s3 + " END");

			// join Cud_CISELNIK_STLPEC
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);

			String subSql = ciselnikGuiSql(ciselnikID, platnostOd);
			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " = (" + subSql + ")");

			Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
			Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
			crit.add(c1.or(c2));

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecGuiPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpecGui> listDTO = new ArrayList<DTOCiselnikStlpecGui>();
			int poradie = 1;

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setNadpis(rVal(r, CudCiselnikStlpecGuiPeer.NADPIS).asString());
				dto.setZarovnanie(rVal(r, CudCiselnikStlpecGuiPeer.ZAROVNANIE).asString());
				dto.setPopupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE).asString());
				dto.setPopupSirka(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_SIRKA).asIntegerObj());
				dto.setPopupSirkaChange(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE).asString());
				dto.setLookupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE).asString());

				dto.setCiselnikStlpecDbTyp(rVal(r, "db_typ").asString());

				dto.setPoradie(poradie++);

				dto.setListSize(lp.size());

				listDTO.add(dto);

			}

			return listDTO.toArray(new DTOCiselnikStlpecGui[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForPop.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpecGui[] listForForm(AuthInfo auth, Integer ciselnikID, Date planostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZAROVNANIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZMENA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.PORADIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.EDIT_CONTROL);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.REG_EXP);

			String s1 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DLZKA, planostOd, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			s1 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s1 + ")";
			String s2 = "ELSE " + CudCiselnikStlpecGuiPeer.DLZKA;
			crit.addAsColumn("dlzka", "CASE " + s1 + " " + s2 + " END");

			s1 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DECIMALS, planostOd, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			s1 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s1 + ")";
			s2 = "ELSE " + CudCiselnikStlpecGuiPeer.DECIMALS;
			crit.addAsColumn("decimals", "CASE " + s1 + " " + s2 + " END");

			s1 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV, "a");
			s1 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s1 + ")";
			s2 = "ELSE " + "t1.DB_TYP";
			crit.addAsColumn("db_typ", "CASE " + s1 + " " + s2 + " END");

			// join CUD_CISELNIK_STLPEC
			crit.addAlias("t1", CudCiselnikStlpecPeer.TABLE_NAME);
			crit.addAsColumn("typ", "t1.TYP");
			crit.addAsColumn("fk1_id_ciselnik", "t1.FK1_ID_CISELNIK");
			crit.addAsColumn("fk1_pk_nazov", "t1.FK1_PK_NAZOV");
			crit.addAsColumn("ciselnikStlpecNazov", "t1.NAZOV");
			crit.addAsColumn("ciselnikStlpecJedinecny", "t1.JEDINECNY");
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, "t1.CISELNIK_STLPEC_ID", MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addAlias("t2", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fk1_ciselnik_nazov", "t2.NAZOV");
			crit.addAsColumn("fk1_ciselnik_tabulka", "t2.TABULKA");
			crit.addJoin("t1.FK1_ID_CISELNIK", "t2.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			String subSql = ciselnikGuiSql(ciselnikID, planostOd);
			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " = (" + subSql + ")");

			crit.addConditional(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, "T", false);

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecGuiPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpecGui> listDTO = new ArrayList<DTOCiselnikStlpecGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setNadpis(rVal(r, CudCiselnikStlpecGuiPeer.NADPIS).asString());
				dto.setZarovnanie(rVal(r, CudCiselnikStlpecGuiPeer.ZAROVNANIE).asString());
				dto.setFormZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE).asString());
				dto.setFormSirka(rVal(r, CudCiselnikStlpecGuiPeer.FORM_SIRKA).asIntegerObj());
				dto.setPovinny(rVal(r, CudCiselnikStlpecGuiPeer.POVINNY).asString());
				dto.setZmena(rVal(r, CudCiselnikStlpecGuiPeer.ZMENA).asString());
				dto.setPoradie(rVal(r, CudCiselnikStlpecGuiPeer.PORADIE).asIntegerObj());
				dto.setEditControl(rVal(r, CudCiselnikStlpecGuiPeer.EDIT_CONTROL).asString());
				dto.setRegExp(rVal(r, CudCiselnikStlpecGuiPeer.REG_EXP).asString());

				dto.setDlzka(rVal(r, "dlzka").asIntegerObj());
				dto.setDecimals(rVal(r, "decimals").asIntegerObj());

				dto.setCiselnikStlpecDbTyp(rVal(r, "db_typ").asString());
				dto.setCiselnikStlpecTyp(rVal(r, "typ").asString());
				dto.setCiselnikStlpecFk1IDCiselnik(rVal(r, "fk1_id_ciselnik").asIntegerObj());
				dto.setCiselnikStlpecFk1PkNazov(rVal(r, "fk1_pk_nazov").asString());
				dto.setCiselnikStlpecNazov(rVal(r, "ciselnikStlpecNazov").asString());
				dto.setCiselnikStlpecJedinecny(rVal(r, "ciselnikStlpecJedinecny").asString());

				dto.setCiselnikStlpecFk1CiselnikNazov(rVal(r, "fk1_ciselnik_nazov").asString());
				dto.setCiselnikStlpecFk1CiselnikTabulka(rVal(r, "fk1_ciselnik_tabulka").asString());

				dto.setListSize(lp.size());

				_CudLookupUtils.lookupDTOCiselnikStlpecGui(dto);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikStlpecGui[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForForm.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpecGui[] listForZmena(AuthInfo auth, Integer ciselnikID, Date platnostOd, Set<Integer> set) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (set.isEmpty()) {
				return new DTOCiselnikStlpecGui[0];
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.EDIT_CONTROL);

			String s1 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DLZKA, platnostOd, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			s1 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s1 + ")";
			String s2 = "ELSE " + CudCiselnikStlpecGuiPeer.DLZKA;
			crit.addAsColumn("dlzka", "CASE " + s1 + " " + s2 + " END");

			s1 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DECIMALS, platnostOd, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			s1 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s1 + ")";
			s2 = "ELSE " + CudCiselnikStlpecGuiPeer.DECIMALS;
			crit.addAsColumn("decimals", "CASE " + s1 + " " + s2 + " END");

			s1 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV, "a");
			s1 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s1 + ")";
			s2 = "ELSE " + "t1.DB_TYP";
			crit.addAsColumn("db_typ", "CASE " + s1 + " " + s2 + " END");

			// join CUD_CISELNIK_STLPEC
			crit.addAlias("t1", CudCiselnikStlpecPeer.TABLE_NAME);
			crit.addAsColumn("nazov", "t1.NAZOV");
			crit.addAsColumn("typ", "t1.TYP");
			crit.addAsColumn("fk1_id_ciselnik", "t1.FK1_ID_CISELNIK");
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, "t1.CISELNIK_STLPEC_ID", MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addAlias("t2", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fk1_ciselnik_nazov", "t2.NAZOV");
			crit.addAsColumn("fk1_ciselnik_tabulka", "t2.TABULKA");
			crit.addJoin("t1.FK1_ID_CISELNIK", "t2.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			String subSql = ciselnikGuiSql(ciselnikID, platnostOd);
			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " = (" + subSql + ")");

			if (set.size() == 1) {
				crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, set.iterator().next());
			} else {
				crit.addIn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, set.toArray(new Integer[set.size()]));
			}

			crit.add(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, "T");

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecGuiPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpecGui> listDTO = new ArrayList<DTOCiselnikStlpecGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setNadpis(rVal(r, CudCiselnikStlpecGuiPeer.NADPIS).asString());
				dto.setEditControl(rVal(r, CudCiselnikStlpecGuiPeer.EDIT_CONTROL).asString());

				dto.setDlzka(rVal(r, "dlzka").asIntegerObj());
				dto.setDecimals(rVal(r, "decimals").asIntegerObj());
				dto.setCiselnikStlpecDbTyp(rVal(r, "db_typ").asString());

				dto.setCiselnikStlpecNazov(rVal(r, "nazov").asString());
				dto.setCiselnikStlpecTyp(rVal(r, "typ").asString());
				dto.setCiselnikStlpecFk1IDCiselnik(rVal(r, "fk1_id_ciselnik").asIntegerObj());

				dto.setCiselnikStlpecFk1CiselnikNazov(rVal(r, "fk1_ciselnik_nazov").asString());
				dto.setCiselnikStlpecFk1CiselnikTabulka(rVal(r, "fk1_ciselnik_tabulka").asString());

				dto.setListSize(lp.size());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikStlpecGui[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForZmena.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpecGui[] listForPrint(AuthInfo auth, Integer ciselnikID, Date platnostOd, String listZobrazenie, String formZobrazenie) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_SIRKA);

			String s1 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DLZKA, platnostOd, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			s1 = "WHEN " + CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK + " IS NOT NULL THEN (" + s1 + ")";
			String s2 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DLZKA, platnostOd, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			s2 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s2 + ")";
			String s3 = "ELSE " + CudCiselnikStlpecGuiPeer.DLZKA;
			crit.addAsColumn("dlzka", "CASE " + s1 + " " + s2 + " " + s3 + " END");

			s1 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DECIMALS, platnostOd, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			s1 = "WHEN " + CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK + " IS NOT NULL THEN (" + s1 + ")";
			s2 = ciselnikStlpecGuiSql(CudCiselnikStlpecGuiPeer.DECIMALS, platnostOd, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			s2 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s2 + ")";
			s3 = "ELSE " + CudCiselnikStlpecGuiPeer.DECIMALS;
			crit.addAsColumn("decimals", "CASE " + s1 + " " + s2 + " " + s3 + " END");

			s1 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV, "a");
			s1 = "WHEN " + CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK + " IS NOT NULL THEN (" + s1 + ")";
			s2 = ciselnikStlpecSql(CudCiselnikStlpecPeer.DB_TYP, "t1.fk1_id_ciselnik", CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV, "b");
			s2 = "WHEN " + "t1.fk1_id_ciselnik" + " IS NOT NULL THEN (" + s2 + ")";
			s3 = "ELSE " + "t1.DB_TYP";
			crit.addAsColumn("db_typ", "CASE " + s1 + " " + s2 + " " + s3 + " END");

			// join CUD_CISELNIK_STLPEC
			crit.addAlias("t1", CudCiselnikStlpecPeer.TABLE_NAME);
			crit.addAsColumn("nazov", "t1.NAZOV");
			crit.addAsColumn("typ", "t1.TYP");
			crit.addAsColumn("fk1_id_ciselnik", "t1.FK1_ID_CISELNIK");
			crit.addAsColumn("fk1_pk_nazov", "t1.FK1_PK_NAZOV");
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, "t1.CISELNIK_STLPEC_ID", MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addAlias("t2", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fk1_ciselnik_tabulka", "t2.tabulka");
			crit.addJoin("t1.FK1_ID_CISELNIK", "t2.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addAlias("t3", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fk2_ciselnik_tabulka", "t3.tabulka");
			crit.addJoin(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, "t3.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			String subSql = ciselnikGuiSql(ciselnikID, platnostOd);
			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " = (" + subSql + ")");

			if ("T".equals(listZobrazenie) && "T".equals(formZobrazenie)) {
				Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
				Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
				crit.add(c1.or(c2));

			} else if ("T".equals(listZobrazenie)) {
				crit.addConditional(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE, "T", false);

			} else if ("T".equals(formZobrazenie)) {
				crit.addConditional(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, "T", false);
			}

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecGuiPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpecGui> listDTO = new ArrayList<DTOCiselnikStlpecGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setNadpis(rVal(r, CudCiselnikStlpecGuiPeer.NADPIS).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV).asString());
				dto.setFk2IDCiselnik(rVal(r, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK).asIntegerObj());
				dto.setFk2PkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV).asString());
				dto.setFk2FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV).asString());
				dto.setListSirka(rVal(r, CudCiselnikStlpecGuiPeer.LIST_SIRKA).asIntegerObj());
				dto.setPopupSirka(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_SIRKA).asIntegerObj());
				dto.setFormSirka(rVal(r, CudCiselnikStlpecGuiPeer.FORM_SIRKA).asIntegerObj());

				dto.setDlzka(rVal(r, "dlzka").asIntegerObj());
				dto.setDecimals(rVal(r, "decimals").asIntegerObj());
				dto.setCiselnikStlpecDbTyp(rVal(r, "db_typ").asString());

				dto.setCiselnikStlpecNazov(rVal(r, "nazov").asString());
				dto.setCiselnikStlpecTyp(rVal(r, "typ").asString());
				dto.setCiselnikStlpecFk1IDCiselnik(rVal(r, "fk1_id_ciselnik").asIntegerObj());
				dto.setCiselnikStlpecFk1CiselnikTabulka(rVal(r, "fk1_ciselnik_tabulka").asString());
				dto.setCiselnikStlpecFk1PkNazov(rVal(r, "fk1_pk_nazov").asString());

				dto.setFk2CiselnikTabulka(rVal(r, "fk2_ciselnik_tabulka").asString());

				dto.setPopupZobrazenie("T");

				dto.setListSize(lp.size());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikStlpecGui[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForPrint.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOCiselnikStlpecGui> mapForWS(AuthInfo auth, Integer ciselnikID, Date planostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DECIMALS);

			String subSql = ciselnikGuiSql(ciselnikID, planostOd);
			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " = (" + subSql + ")");

			crit.addConditional(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, "T", false);

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecGuiPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOCiselnikStlpecGui> resultMap = new HashMap<Integer, DTOCiselnikStlpecGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setNadpis(rVal(r, CudCiselnikStlpecGuiPeer.NADPIS).asString());
				dto.setPovinny(rVal(r, CudCiselnikStlpecGuiPeer.POVINNY).asString());
				dto.setDlzka(rVal(r, CudCiselnikStlpecGuiPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecGuiPeer.DECIMALS).asIntegerObj());

				dto.setListSize(lp.size());

				resultMap.put(dto.getIDCiselnikStlpec(), dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "mapForWS.error", auth);
			return null;
		}
	}

	private Map<Integer, List<DTOCiselnikStlpecGui>> mapForLookupLight(AuthInfo auth, Set<Integer> set, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (set.isEmpty()) {
				return new HashMap<Integer, List<DTOCiselnikStlpecGui>>();
			}

			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

				crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);

				if (set.size() == 1) {
					crit.addConditional(CudCiselnikGuiPeer.ID_CISELNIK, set.iterator().next());
				} else {
					crit.addIn(CudCiselnikGuiPeer.ID_CISELNIK, set.toArray(new Integer[set.size()]));
				}

				crit.addConditional(CudCiselnikGuiPeer.PLATNOST_OD, platnostOd, MyCriteria2.LESS_EQUAL);

				Criterion c1 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, platnostOd, MyCriteria2.GREATER_EQUAL);
				Criterion c2 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
				crit.add(c1.or(c2));

				crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE);

			// join CUD_CISELNIK_STLPEC
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);

			// join CUD_CISELNIK
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addJoin(CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);

			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " IN (" + subSql + ")");

			Criterion c1 = crit.getNewCriterion(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE, "T", MyCriteria2.EQUAL);
			Criterion c2 = crit.getNewCriterion(CudCiselnikStlpecPeer.TYP, _CudConsts.CISELNIK_STLPEC_TYP_PK, MyCriteria2.EQUAL);
			crit.add(c1.or(c2));

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);
			crit.add(CudCiselnikStlpecGuiPeer.EDIT_CONTROL, null);

			crit.addAscendingOrderByColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addAscendingOrderByColumn(CudCiselnikStlpecGuiPeer.PORADIE);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOCiselnikStlpecGui>> mapa = new HashMap<Integer, List<DTOCiselnikStlpecGui>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setDlzka(rVal(r, CudCiselnikStlpecGuiPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecGuiPeer.DECIMALS).asIntegerObj());
				dto.setLookupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE).asString());

				dto.setCiselnikStlpecNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setCiselnikStlpecTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setCiselnikStlpecIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setCiselnikStlpecFk1IDCiselnik(rVal(r, CudCiselnikStlpecPeer.FK1_ID_CISELNIK).asIntegerObj());
				dto.setCiselnikStlpecDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());

				dto.setCiselnikTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());

				if (!StringUtils.isValid(mapa.get(dto.getCiselnikStlpecIDCiselnik()))) {
					mapa.put(dto.getCiselnikStlpecIDCiselnik(), new ArrayList<DTOCiselnikStlpecGui>());
				}
				mapa.get(dto.getCiselnikStlpecIDCiselnik()).add(dto);
			}

			return mapa;

		} catch (Throwable t) {
			handleException(t, "mapForLookupLight.error", auth);
			return null;
		}
	}

	public Map<Integer, List<DTOCiselnikStlpecGui>> mapForLookup(AuthInfo auth, Set<Integer> set, Date platnostOd) throws AppException {

		try {
			Map<Integer, List<DTOCiselnikStlpecGui>> resultMap = mapForLookupLight(auth, set, platnostOd);

			Set<Integer> ciselnikIDs = new HashSet<Integer>();
			for (Integer ciselnikID : resultMap.keySet()) {
				for (DTOCiselnikStlpecGui dtoMeta : resultMap.get(ciselnikID)) {
					if (StringUtils.isValid(dtoMeta.getCiselnikStlpecFk1IDCiselnik())) {
						ciselnikIDs.add(dtoMeta.getCiselnikStlpecFk1IDCiselnik());
					}
				}
			}

			Map<Integer, List<DTOCiselnikStlpecGui>> metaMap = mapForLookupLight(auth, ciselnikIDs, platnostOd);
			for (Integer ciselnikID : metaMap.keySet()) {
				resultMap.put(ciselnikID, metaMap.get(ciselnikID));
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "mapForLookup.error", auth);
			return null;
		}
	}

	private DTOCiselnikStlpecGui readLightForLoad(AuthInfo auth, Integer ciselnikStlpecGuiID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CAS_ZMENY);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_UCET);

			crit.addConditional(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, ciselnikStlpecGuiID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			DTOCiselnikStlpecGui resultDTO = null;

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				resultDTO = new DTOCiselnikStlpecGui();
				resultDTO.setCiselnikStlpecIDCiselnik(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				resultDTO.setCasZmeny(rVal(r, CudCiselnikStlpecGuiPeer.CAS_ZMENY).asUtilDate());
				resultDTO.setIDUcet(rVal(r, CudCiselnikStlpecGuiPeer.ID_UCET).asIntegerObj());
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "readLightForLoad.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpecGui[] list(AuthInfo auth, Page page, DTOCiselnikStlpecGui dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnikStlpecGui();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, dtoF);

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.PORADIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZMENA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZAROVNANIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.EDIT_CONTROL);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.REG_EXP);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPIS);

			// join Cud_CISELNIK_STLPEC
			crit.addAlias("t1", CudCiselnikStlpecPeer.TABLE_NAME);
			crit.addAsColumn("ciselnikStlpecNazov", "t1.NAZOV");
			crit.addAsColumn("ciselnikStlpecTyp", "t1.TYP");
			crit.addAsColumn("ciselnikStlpecJedinecny", "t1.JEDINECNY");
			crit.addAsColumn("ciselnikStlpecPovinny", "t1.POVINNY");
			crit.addAsColumn("ciselnikStlpecFk1IDCiselnik", "t1.FK1_ID_CISELNIK");
			crit.addAsColumn("ciselnikStlpecFk1PkNazov", "t1.FK1_PK_NAZOV");
			crit.addAsColumn("jeDbString", "t1.JE_DB_STRING");
			crit.addAsColumn("db_typ", "t1.DB_TYP");
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, "t1.CISELNIK_STLPEC_ID", MyCriteria2.LEFT_JOIN);
			crit.addConditional("t1.TYP", dtoF.getCiselnikStlpecTyp());

			// join Cud_CISELNIK
			crit.addAlias("t2", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("ciselnikStlpecFk1CiselnikNazov", "t2.NAZOV");
			crit.addJoin("t1.FK1_ID_CISELNIK", "t2.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			// join Cud_CISELNIK
			crit.addAlias("t3", CudCiselnikPeer.TABLE_NAME);
			crit.addAsColumn("fk2CiselnikNazov", "t3.NAZOV");
			crit.addJoin(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, "t3.CISELNIK_ID", MyCriteria2.LEFT_JOIN);

			crit.addConditional(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, dtoF.getCiselnikStlpecGuiID());
			crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, dtoF.getIDCiselnikGui());
			crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, dtoF.getIDCiselnikStlpec());
			crit.addConditional(CudCiselnikStlpecGuiPeer.NADPIS, dtoF.getNadpis(), true);
			crit.addConditional(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE, dtoF.getLookupZobrazenie(), false);
			crit.addConditional(CudCiselnikStlpecGuiPeer.ZMENA, dtoF.getZmena(), false);
			crit.addConditional(CudCiselnikStlpecGuiPeer.POVINNY, dtoF.getPovinny(), false);
			crit.addConditional(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE, dtoF.getListZobrazenie(), false);
			crit.addConditional(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, dtoF.getFormZobrazenie(), false);
			crit.addConditional(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE, dtoF.getPopupZobrazenie(), false);

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOCiselnikStlpecGui> listDTO = new ArrayList<DTOCiselnikStlpecGui>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikGui(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setNadpis(rVal(r, CudCiselnikStlpecGuiPeer.NADPIS).asString());
				dto.setPoradie(rVal(r, CudCiselnikStlpecGuiPeer.PORADIE).asIntegerObj());
				dto.setDlzka(rVal(r, CudCiselnikStlpecGuiPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecGuiPeer.DECIMALS).asIntegerObj());
				dto.setZmena(rVal(r, CudCiselnikStlpecGuiPeer.ZMENA).asString());
				dto.setPovinny(rVal(r, CudCiselnikStlpecGuiPeer.POVINNY).asString());
				dto.setZarovnanie(rVal(r, CudCiselnikStlpecGuiPeer.ZAROVNANIE).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV).asString());
				dto.setFk2IDCiselnik(rVal(r, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK).asIntegerObj());
				dto.setFk2PkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV).asString());
				dto.setFk2FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV).asString());
				dto.setListSirka(rVal(r, CudCiselnikStlpecGuiPeer.LIST_SIRKA).asIntegerObj());
				dto.setListSirkaChange(rVal(r, CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE).asString());
				dto.setListZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE).asString());
				dto.setFormSirka(rVal(r, CudCiselnikStlpecGuiPeer.FORM_SIRKA).asIntegerObj());
				dto.setFormZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE).asString());
				dto.setPopupSirka(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_SIRKA).asIntegerObj());
				dto.setPopupSirkaChange(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE).asString());
				dto.setPopupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE).asString());
				dto.setLookupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE).asString());
				dto.setEditControl(rVal(r, CudCiselnikStlpecGuiPeer.EDIT_CONTROL).asString());
				dto.setRegExp(rVal(r, CudCiselnikStlpecGuiPeer.REG_EXP).asString());
				dto.setPopis(rVal(r, CudCiselnikStlpecGuiPeer.POPIS).asString());

				dto.setCiselnikStlpecNazov(rVal(r, "ciselnikStlpecNazov").asString());
				dto.setCiselnikStlpecTyp(rVal(r, "ciselnikStlpecTyp").asString());
				dto.setCiselnikStlpecJedinecny(rVal(r, "ciselnikStlpecJedinecny").asString());
				dto.setCiselnikStlpecPovinny(rVal(r, "ciselnikStlpecPovinny").asString());
				dto.setCiselnikStlpecFk1IDCiselnik(rVal(r, "ciselnikStlpecFk1IDCiselnik").asIntegerObj());
				dto.setCiselnikStlpecFk1CiselnikNazov(rVal(r, "ciselnikStlpecFk1CiselnikNazov").asString());
				dto.setCiselnikStlpecFk1PkNazov(rVal(r, "ciselnikStlpecFk1PkNazov").asString());
				dto.setCiselnikStlpecDbTyp(rVal(r, "db_typ").asString());
				dto.setCiselnikStlpecJeDbString(rVal(r, "jeDbString").asString());

				dto.setFk2CiselnikNazov(rVal(r, "fk2CiselnikNazov").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikStlpecGui[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpecGuiLD loadData(AuthInfo auth, DTOCiselnikStlpecGuiLD dtoF) throws AppException {

		try {
			Set<Integer> set = new HashSet<Integer>();
			if (StringUtils.isValid(dtoF.getFk1IDCiselnik())) {
				set.add(dtoF.getFk1IDCiselnik());
			}
			if (StringUtils.isValid(dtoF.getFk2IDCiselnik())) {
				set.add(dtoF.getFk2IDCiselnik());
			}

			Integer[] ids = set.toArray(new Integer[set.size()]);

			DTOCiselnikStlpecGuiLD resultDTO = new DTOCiselnikStlpecGuiLD();

			resultDTO.setCiselnikStlpecGuiDTO(readLightForLoad(auth, dtoF.getCiselnikStlpecGuiID()));

			Map<Integer, DTOCiselnik> ciselnikMap = getDelegate().getCiselnikRead().mapLight(auth, ids);
			resultDTO.setFk1CiselnikDTO(ciselnikMap.get(dtoF.getFk1IDCiselnik()));
			resultDTO.setFk2CiselnikDTO(ciselnikMap.get(dtoF.getFk2IDCiselnik()));

			Map<String, DTOCiselnikStlpec> csMap = getDelegate().getCiselnikStlpecRead().mapLight(auth, dtoF.getCiselnikStlpecID(), dtoF.getFk1IDCiselnik(), dtoF.getFk1FkNazov(), dtoF.getFk2IDCiselnik(), dtoF.getFk2FkNazov());
			resultDTO.setCiselnikStlpecDTO(csMap.get(dtoF.getCiselnikStlpecID().toString()));
			resultDTO.setFk1FkCiselnikStlpecDTO(csMap.get(dtoF.getFk1IDCiselnik() + "*" + dtoF.getFk1FkNazov()));
			resultDTO.setFk2FkCiselnikStlpecDTO(csMap.get(dtoF.getFk2IDCiselnik() + "*" + dtoF.getFk2FkNazov()));

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public Map<Integer, Integer> poradieMap(AuthInfo auth, Integer ciselnikGuiID, Integer ciselnikStlpecGuiID, Integer poradie) throws AppException {

		try {
			String subSql = null;

			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

				crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);

				crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, ciselnikGuiID);
				crit.addConditional(CudCiselnikStlpecGuiPeer.PORADIE, poradie);
				crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

				if (StringUtils.isValid(ciselnikStlpecGuiID)) {
					crit.addConditional(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, ciselnikStlpecGuiID, MyCriteria2.NOT_EQUAL);
				}

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.PORADIE);

			crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, ciselnikGuiID);
			crit.addConditional(CudCiselnikStlpecGuiPeer.PORADIE, poradie, MyCriteria2.GREATER_EQUAL);

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			if (StringUtils.isValid(ciselnikStlpecGuiID)) {
				crit.addConditional(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, ciselnikStlpecGuiID, MyCriteria2.NOT_EQUAL);
			}

			crit.addCustomSql(CudCiselnikStlpecGuiPeer.DECIMALS, "EXISTS (" + subSql + ")");

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, Integer> mapDTO = new HashMap<Integer, Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Integer value1 = rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj();
				Integer value2 = rVal(r, CudCiselnikStlpecGuiPeer.PORADIE).asIntegerObj();

				mapDTO.put(value1, value2);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "poradieMap.error", auth);
			return null;
		}
	}

	public String updateKontrola(AuthInfo auth, DTOCiselnikStlpecGui dto) throws AppException {

		try {
			DTOCiselnikGui dtoGui = getDelegate().getCiselnikGuiRead().read(auth, dto.getIDCiselnikGui());
			if (!_CudConsts.CISELNIK_GUI_STAV_DRAFT.equals(dtoGui.getStav())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_606);
			}

			if (_CudConsts.CISELNIK_STLPEC_GUI_EDIT_CONTROL_COMBO.equals(dto.getEditControl()) && !StringUtils.isValid(dto.getRegExp())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_622);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	public String deleteKontrola(AuthInfo auth, DTOCiselnikStlpecGui dto) throws AppException {

		try {
			DTOCiselnikGui dtoGui = getDelegate().getCiselnikGuiRead().read(auth, dto.getIDCiselnikGui());
			if (!_CudConsts.CISELNIK_GUI_STAV_DRAFT.equals(dtoGui.getStav())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_606);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "deleteKontrola.error", auth);
			return null;
		}
	}

	public Map<Integer, List<DTOCiselnikStlpecGui>> mapByFk(AuthInfo auth, Integer fk1IdCiselnik, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

				crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);

				crit.addIn(CudCiselnikGuiPeer.STAV, new String[] { _CudConsts.CISELNIK_GUI_STAV_DRAFT, _CudConsts.CISELNIK_GUI_STAV_PUB });

				crit.addConditional(CudCiselnikGuiPeer.PLATNOST_OD, platnostOd, MyCriteria2.LESS_EQUAL);

				Criterion c1 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, platnostOd, MyCriteria2.GREATER_EQUAL);
				Criterion c2 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
				crit.add(c1.or(c2));

				crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE);

			// join CUD_CISELNIK_STLPEC
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecPeer.TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecPeer.FK1_ID_CISELNIK);
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, fk1IdCiselnik);

			crit.addConditional(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, "T", false);

			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " IN (" + subSql + ")");

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);
			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOCiselnikStlpecGui>> resultMap = new HashMap<Integer, List<DTOCiselnikStlpecGui>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setFormZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE).asString());

				dto.setCiselnikStlpecNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());
				dto.setCiselnikStlpecTyp(rVal(r, CudCiselnikStlpecPeer.TYP).asString());
				dto.setCiselnikStlpecIDCiselnik(rVal(r, CudCiselnikStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setCiselnikStlpecFk1IDCiselnik(rVal(r, CudCiselnikStlpecPeer.FK1_ID_CISELNIK).asIntegerObj());

				if (!StringUtils.isValid(resultMap.get(dto.getCiselnikStlpecIDCiselnik()))) {
					resultMap.put(dto.getCiselnikStlpecIDCiselnik(), new ArrayList<DTOCiselnikStlpecGui>());
				}
				resultMap.get(dto.getCiselnikStlpecIDCiselnik()).add(dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "mapByFk.error", auth);
			return null;
		}
	}

	public Map<Integer, List<DTOCiselnikStlpecGui>> map(AuthInfo auth, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudCiselnikGuiPeer.CISELNIK_GUI_ID, new DTOCiselnikGui());

				crit.addSelectColumn(CudCiselnikGuiPeer.CISELNIK_GUI_ID);

				crit.addConditional(CudCiselnikGuiPeer.PLATNOST_OD, platnostOd, MyCriteria2.LESS_EQUAL);

				Criterion c1 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, platnostOd, MyCriteria2.GREATER_EQUAL);
				Criterion c2 = crit.getNewCriterion(CudCiselnikGuiPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
				crit.add(c1.or(c2));

				crit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.NADPIS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.PORADIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DLZKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.DECIMALS);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZMENA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POVINNY);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ZAROVNANIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_SIRKA);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.EDIT_CONTROL);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.REG_EXP);
			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.POPIS);

			crit.addSelectColumn(CudCiselnikStlpecPeer.DB_TYP);
			crit.addSelectColumn(CudCiselnikStlpecPeer.JE_DB_STRING);
			crit.addJoin(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, MyCriteria2.LEFT_JOIN);

			crit.addCustomSql(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI + " IN (" + subSql + ")");

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOCiselnikStlpecGui>> mapDTO = new HashMap<Integer, List<DTOCiselnikStlpecGui>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
				dto.setCiselnikStlpecGuiID(rVal(r, CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID).asIntegerObj());
				dto.setIDCiselnikGui(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setNadpis(rVal(r, CudCiselnikStlpecGuiPeer.NADPIS).asString());
				dto.setPoradie(rVal(r, CudCiselnikStlpecGuiPeer.PORADIE).asIntegerObj());
				dto.setDlzka(rVal(r, CudCiselnikStlpecGuiPeer.DLZKA).asIntegerObj());
				dto.setDecimals(rVal(r, CudCiselnikStlpecGuiPeer.DECIMALS).asIntegerObj());
				dto.setZmena(rVal(r, CudCiselnikStlpecGuiPeer.ZMENA).asString());
				dto.setPovinny(rVal(r, CudCiselnikStlpecGuiPeer.POVINNY).asString());
				dto.setZarovnanie(rVal(r, CudCiselnikStlpecGuiPeer.ZAROVNANIE).asString());
				dto.setFk1FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV).asString());
				dto.setFk2IDCiselnik(rVal(r, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK).asIntegerObj());
				dto.setFk2PkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV).asString());
				dto.setFk2FkNazov(rVal(r, CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV).asString());
				dto.setListSirka(rVal(r, CudCiselnikStlpecGuiPeer.LIST_SIRKA).asIntegerObj());
				dto.setListSirkaChange(rVal(r, CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE).asString());
				dto.setListZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE).asString());
				dto.setFormSirka(rVal(r, CudCiselnikStlpecGuiPeer.FORM_SIRKA).asIntegerObj());
				dto.setFormZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE).asString());
				dto.setPopupSirka(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_SIRKA).asIntegerObj());
				dto.setPopupSirkaChange(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE).asString());
				dto.setPopupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE).asString());
				dto.setLookupZobrazenie(rVal(r, CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE).asString());
				dto.setEditControl(rVal(r, CudCiselnikStlpecGuiPeer.EDIT_CONTROL).asString());
				dto.setRegExp(rVal(r, CudCiselnikStlpecGuiPeer.REG_EXP).asString());
				dto.setPopis(rVal(r, CudCiselnikStlpecGuiPeer.POPIS).asString());

				dto.setCiselnikStlpecDbTyp(rVal(r, CudCiselnikStlpecPeer.DB_TYP).asString());
				dto.setCiselnikStlpecJeDbString(rVal(r, CudCiselnikStlpecPeer.JE_DB_STRING).asString());
				if (_CudConsts.DB_TYP_INTEGER.equals(dto.getCiselnikStlpecDbTyp()) && "T".equals(dto.getCiselnikStlpecJeDbString())) {
					dto.setCiselnikStlpecDbTyp(_CudConsts.DB_TYP_STRING);
					dto.setCiselnikStlpecJeDbString(null);
				}

				if (!StringUtils.isValid(mapDTO.get(dto.getIDCiselnikGui()))) {
					mapDTO.put(dto.getIDCiselnikGui(), new ArrayList<DTOCiselnikStlpecGui>());
				}
				mapDTO.get(dto.getIDCiselnikGui()).add(dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "map.error", auth);
			return null;
		}
	}

	public Set<Integer> ciselnikStlpecIDs(AuthInfo auth, Set<Integer> ciselnikGuiIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ciselnikGuiIDs) || ciselnikGuiIDs.isEmpty()) {
				return new HashSet<Integer>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.setDistinct();

			crit.addSelectColumn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC);

			if (ciselnikGuiIDs.size() == 1) {
				crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, ciselnikGuiIDs.iterator().next());
			} else {
				crit.addIn(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, ciselnikGuiIDs.toArray(new Integer[ciselnikGuiIDs.size()]));
			}

			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC).asIntegerObj());
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "ciselnikStlpecIDs.error", auth);
			return null;
		}
	}

}
