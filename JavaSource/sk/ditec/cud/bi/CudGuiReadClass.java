package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.security.Rola;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynCiselnik;
import sk.ditec.cud.dto.DTODynCiselnikExport;
import sk.ditec.cud.dto.DTODynCiselnikMeta;
import sk.ditec.cud.dto.DTOFutPocetnostDynCiselnik;
import sk.ditec.cud.dto.DTOMainHead;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOPluginStlpec;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.print._ICudPrint;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikGuiPeer;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecGuiPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudPrekladPeer;
import sk.ditec.dao.meta.CudWfDefPeer;

import com.workingdogs.village.Record;

public class CudGuiReadClass extends _CudBaseClass {

	private Logger log = LoggerFactory.getLogger(CudGuiReadClass.class);

	public Set<String> userTabsColsList(AuthInfo auth, String tabulkaNazov) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(tabulkaNazov)) {
				return null;
			}

			String sql = "SELECT column_name FROM user_tab_cols WHERE table_name = \'" + tabulkaNazov + "\'";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<String> set = new HashSet<String>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				String nazov = rVal(r, "column_name").asString();
				if (StringUtils.isValid(nazov)) {
					set.add(nazov);
				}
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "columnsNamesList.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpec[] userTabColsListLight(AuthInfo auth, DTOCiselnikStlpec dtoF, boolean useIlike) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(dtoF) || !StringUtils.isValid(dtoF.getCiselnikTabulka())) {
				return null;
			}

			String sql = "SELECT table_name, column_name, data_type, data_precision, data_scale, nullable, column_id, char_length FROM user_tab_cols WHERE table_name = \'" + dtoF.getCiselnikTabulka() + "\'";

			if (StringUtils.isValid(dtoF.getNazov())) {
				sql += " AND column_name LIKE \'" + dtoF.getNazov().toUpperCase() + (useIlike ? "%\'" : "\'");
			}

			sql += " ORDER BY column_id ASC";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnikStlpec> listDTO = new ArrayList<DTOCiselnikStlpec>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
				dto.setCiselnikStlpecID(rVal(r, "column_id").asIntegerObj() * (-1));
				dto.setPoradie(dto.getCiselnikStlpecID());
				dto.setNazov(rVal(r, "column_name").asString());
				dto.setPovinny("Y".equals(rVal(r, "nullable").asString()) ? "F" : "T");
				dto.setCiselnikTabulka(rVal(r, "table_name").asString());

				String dataType = rVal(r, "data_type").asString();
				Integer dataPrecision = rVal(r, "data_precision").asIntegerObj();
				Integer dataScale = rVal(r, "data_scale").asIntegerObj();
				Integer charLenght = rVal(r, "char_length").asIntegerObj();

				if ("TIMESTAMP(6)".equals(dataType)) {
					dto.setDbTyp(_CudConsts.DB_TYP_DATE);
					dto.setDlzka(1);
				} else if ("NVARCHAR2".equals(dataType) && charLenght.intValue() != 1) {
					dto.setDbTyp(_CudConsts.DB_TYP_STRING);
					dto.setDlzka(charLenght);
				} else if ("NVARCHAR2".equals(dataType) && charLenght.intValue() == 1) {
					dto.setDbTyp(_CudConsts.DB_TYP_BOOLEAN);
					dto.setDlzka(1);
				} else if ("NUMBER".equals(dataType) && dataScale.intValue() != 0) {
					dto.setDbTyp(_CudConsts.DB_TYP_DOUBLE);
					dto.setDlzka(dataPrecision);
					dto.setDecimals(dataScale);
				} else if ("NUMBER".equals(dataType) && dataScale.intValue() == 0) {
					dto.setDbTyp(_CudConsts.DB_TYP_INTEGER);
					dto.setDlzka(dataPrecision);
				} else if ("BLOB".equals(dataType) || "CLOB".equals(dataType)) {
					dto.setDbTyp(_CudConsts.DB_TYP_STRING);
					dto.setDlzka(_CudConsts.MAX_LENGTH_STRING + 10);
				}

				if (_CudConsts.NAZOV_HIST_ID.equals(dto.getNazov())) {
					dto.setTyp(_CudConsts.CISELNIK_STLPEC_TYP_HK);
				} else if ((dtoF.getCiselnikTabulka().substring(2) + "_ID").equals(dto.getNazov())) {
					dto.setTyp(_CudConsts.CISELNIK_STLPEC_TYP_PK);
				} else if ((dtoF.getCiselnikTabulka().substring(4) + "_ID").equals(dto.getNazov())) {
					dto.setTyp(_CudConsts.CISELNIK_STLPEC_TYP_PK);
				} else {
					dto.setTyp(_CudConsts.CISELNIK_STLPEC_TYP_AT);
				}

				dto.setListSize(lp.size());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnikStlpec[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "userTabColsListLight.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpec userTabColsReadPK(AuthInfo auth, String tabulka) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(tabulka)) {
				return null;
			}

			String sql = "SELECT table_name, column_name, data_type, data_precision, data_scale, nullable, column_id, char_length FROM user_tab_cols WHERE column_id = 8 AND table_name = \'" + tabulka + "\'";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {

				Record r = (Record) iter.next();

				String povinny = "Y".equals(rVal(r, "nullable").asString()) ? "F" : "T";
				if (!"T".equals(povinny)) {
					return null;
				}

				String dataType = rVal(r, "data_type").asString();
				Integer dataScale = rVal(r, "data_scale").asIntegerObj();

				if ("NUMBER".equals(dataType) && dataScale.intValue() == 0) {
					DTOCiselnikStlpec dto = new DTOCiselnikStlpec();
					dto.setNazov(rVal(r, "column_name").asString());
					dto.setDlzka(rVal(r, "data_precision").asIntegerObj());
					return dto;
				}
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "userTabColsReadPK.error", auth);
			return null;
		}
	}

	public Integer ciselnikStlpecGuiPocet(AuthInfo auth, Integer ciselnikStlpecID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, new DTOCiselnikStlpecGui());

			crit.addAsColumn("pocet", "count(*)");

			crit.addConditional(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, ciselnikStlpecID);
			crit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

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
			handleException(t, "ciselnikStlpecGuiPocet.error", auth);
			return null;
		}
	}

	public DTOWfTodo[] wfTodoList(AuthInfo auth, DTOWfTodo dtoF) throws AppException {

		try {
			DTOWfTodo[] listDTO = getDelegate().getWfTodoRead().list(auth, dtoF.getIDCiselnik(), dtoF.getIDZmena());
			Map<String, Date> zmenaStavHistMap = getDelegate().getZmenaStavHistRead().map(auth, dtoF.getIDCiselnik(), dtoF.getIDZmena());

			for (DTOWfTodo dto : listDTO) {
				if (_CudConsts.WF_DEF_TYP_IN.equals(dto.getWfDefTyp())) {
					dto.setZmenaStavHistCasVytvorenia(zmenaStavHistMap.get(_CudConsts.ZMENA_STAV_VPO));
				} else if (_CudConsts.WF_DEF_TYP_SC.equals(dto.getWfDefTyp())) {
					dto.setZmenaStavHistCasVytvorenia(zmenaStavHistMap.get(_CudConsts.ZMENA_STAV_SCH));
				} else if (_CudConsts.WF_DEF_TYP_OV.equals(dto.getWfDefTyp())) {
					dto.setZmenaStavHistCasVytvorenia(zmenaStavHistMap.get(_CudConsts.ZMENA_STAV_PAU));
				}
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "wfTodoList.error", auth);
			return null;
		}
	}

	public DTOFutPocetnostDynCiselnik[] futPocetnostList(AuthInfo auth, DTOFutPocetnostDynCiselnik dtoF) throws AppException {

		try {
			Map<String, List<DTOCiselnikStlpec>> csMap = getDelegate().getCiselnikStlpecRead().mapForPocetnost(auth, dtoF.getIDCiselnik());

			List<DTOFutPocetnostDynCiselnik> listDTO = new ArrayList<DTOFutPocetnostDynCiselnik>();

			for (String tabulka : csMap.keySet()) {
				List<DTOCiselnikStlpec> csList = csMap.get(tabulka);

				Map<Integer, List<Map<Integer, Set<String>>>> filterMap = new HashMap<Integer, List<Map<Integer, Set<String>>>>();

				int index = 0;
				for (DTOCiselnikStlpec dtoCS : csList) {

					if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getTyp())) {
						filterMap.put(index, new ArrayList<Map<Integer, Set<String>>>());
						filterMap.get(index).add(new HashMap<Integer, Set<String>>());
						filterMap.get(index).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
						filterMap.get(index).get(0).get(dtoCS.getCiselnikStlpecID()).add(dtoF.getRowID().toString());
						index++;
					}
				}

				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(csList, _CudConsts.NAZOV_ZMAZ);
				filterMap.put(index, new ArrayList<Map<Integer, Set<String>>>());
				filterMap.get(index).add(new HashMap<Integer, Set<String>>());
				filterMap.get(index).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
				filterMap.get(index).get(0).get(dtoCS.getCiselnikStlpecID()).add("F");

				Integer pocet = getDelegate().getDynCiselnikRead().futCount(auth, tabulka, csList, filterMap, null, null, dtoF.getPlatnostOd(), dtoF.getPlatnostDo());
				if (StringUtils.isValid(pocet) && pocet.intValue() != 0) {

					DTOFutPocetnostDynCiselnik dtoNew = new DTOFutPocetnostDynCiselnik();
					dtoNew.setZmenaID(listDTO.size() + 1);
					dtoNew.setIDCiselnik(csList.get(0).getIDCiselnik());
					dtoNew.setCiselnikNazov(csList.get(0).getCiselnikNazov());
					dtoNew.setCiselnikTabulka(csList.get(0).getCiselnikTabulka());
					dtoNew.setPocet(pocet);
					dtoNew.setCiselnikStlpecList(csList.toArray(new DTOCiselnikStlpec[csList.size()]));

					listDTO.add(dtoNew);
				}
			}

			return listDTO.toArray(new DTOFutPocetnostDynCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "futPocetnostList.error", auth);
			return null;
		}
	}

	public List<DTOCiselnikStlpecGui> metaList(AuthInfo auth, String tabulka, String operacia) throws AppException {

		try {
			DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
			dtoF.setCiselnikTabulka(tabulka);
			DTOCiselnikStlpec[] poleList = userTabColsListLight(auth, dtoF, false);

			String[] columnsArr = null;
			if (CudCiselnikPeer.TABLE_NAME.equals(tabulka)) {
				columnsArr = new String[] { CudCiselnikPeer.CISELNIK_ID, CudCiselnikPeer.TABULKA, CudCiselnikPeer.NAZOV, CudCiselnikPeer.POPIS, CudCiselnikPeer.PRINT_CLASS, CudCiselnikPeer.PRINT_ZAHLAVIE, CudCiselnikPeer.AKTIVNY, CudCiselnikPeer.PREDPIS, CudCiselnikPeer.PRILOHA_KAPITOLA,
						CudCiselnikPeer.HLAVNY, CudCiselnikPeer.TYP, CudCiselnikPeer.KATEGORIA };

			} else if (CudCiselnikGuiPeer.TABLE_NAME.equals(tabulka)) {
				columnsArr = new String[] { CudCiselnikGuiPeer.CISELNIK_GUI_ID, CudCiselnikGuiPeer.ID_CISELNIK, CudCiselnikGuiPeer.STAV, CudCiselnikGuiPeer.POPIS };

			} else if (CudCiselnikStlpecPeer.TABLE_NAME.equals(tabulka)) {
				columnsArr = new String[] { CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikStlpecPeer.NAZOV, CudCiselnikStlpecPeer.NADPIS, CudCiselnikStlpecPeer.TYP, CudCiselnikStlpecPeer.PORADIE, CudCiselnikStlpecPeer.DLZKA, CudCiselnikStlpecPeer.DECIMALS,
						CudCiselnikStlpecPeer.DB_TYP, CudCiselnikStlpecPeer.POVINNY, CudCiselnikStlpecPeer.JEDINECNY, CudCiselnikStlpecPeer.AKTIVNY, CudCiselnikStlpecPeer.FK1_ID_CISELNIK, CudCiselnikStlpecPeer.FK1_PK_NAZOV, CudCiselnikStlpecPeer.FK1_FK_NAZOV, CudCiselnikStlpecPeer.POPIS };

			} else if (CudCiselnikStlpecGuiPeer.TABLE_NAME.equals(tabulka)) {
				columnsArr = new String[] { CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecGuiPeer.NADPIS, CudCiselnikStlpecGuiPeer.PORADIE, CudCiselnikStlpecGuiPeer.DLZKA,
						CudCiselnikStlpecGuiPeer.DECIMALS, CudCiselnikStlpecGuiPeer.ZMENA, CudCiselnikStlpecGuiPeer.POVINNY, CudCiselnikStlpecGuiPeer.ZAROVNANIE, CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV, CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV,
						CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV, CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE, CudCiselnikStlpecGuiPeer.LIST_SIRKA, CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE, CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, CudCiselnikStlpecGuiPeer.FORM_SIRKA,
						CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE, CudCiselnikStlpecGuiPeer.POPUP_SIRKA, CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE, CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE, CudCiselnikStlpecGuiPeer.EDIT_CONTROL, CudCiselnikStlpecGuiPeer.REG_EXP, CudCiselnikStlpecGuiPeer.POPIS };

			} else if (CudWfDefPeer.TABLE_NAME.equals(tabulka)) {
				columnsArr = new String[] { CudWfDefPeer.WF_DEF_ID, CudWfDefPeer.ID_CISELNIK, CudWfDefPeer.ID_WF_DEF_NASL, CudWfDefPeer.NAZOV, CudWfDefPeer.TYP, CudWfDefPeer.ZODPOVEDNOST, CudWfDefPeer.EMAIL_LIST, CudWfDefPeer.EMAIL_TEXT, CudWfDefPeer.EMAIL_SUBJECT, CudWfDefPeer.EMAIL_SEND,
						CudWfDefPeer.HODINY, CudWfDefPeer.ID_SKUPINA, CudWfDefPeer.SKUPINA_NAZOV };

			} else if (CudPrekladPeer.TABLE_NAME.equals(tabulka)) {
				columnsArr = new String[] { CudPrekladPeer.PREKLAD_ID, CudPrekladPeer.ID_PREKLAD_JAZYK, CudPrekladPeer.ID_PREKLAD_STLPEC, CudPrekladPeer.ZAZNAM_ID, CudPrekladPeer.PREKLAD };

			}

			Set<String> set = new HashSet<String>();
			for (String colName : columnsArr) {
				set.add(trimColumnName(colName));
			}

			List<DTOCiselnikStlpecGui> listDTO = new ArrayList<DTOCiselnikStlpecGui>();
			for (DTOCiselnikStlpec dto : poleList) {

				if (!set.contains(dto.getNazov())) {
					continue;
				}

				DTOCiselnikStlpecGui dtoNew = new DTOCiselnikStlpecGui();
				dtoNew.setCiselnikStlpecGuiID(dto.getCiselnikStlpecID());
				dtoNew.setIDCiselnikStlpec(dto.getCiselnikStlpecID());
				dtoNew.setCiselnikStlpecNazov(dto.getNazov());
				dtoNew.setDlzka(dto.getDlzka());
				dtoNew.setDecimals(dto.getDecimals());
				dtoNew.setCiselnikStlpecTyp(dto.getTyp());
				if (CudCiselnikStlpecGuiPeer.TABLE_NAME.equals(tabulka) && "ZAROVNANIE".equals(dto.getNazov())) {
					dtoNew.setCiselnikStlpecDbTyp(_CudConsts.DB_TYP_STRING);
				} else if (CudWfDefPeer.TABLE_NAME.equals(tabulka) && "ZODPOVEDNOST".equals(dto.getNazov())) {
					dtoNew.setCiselnikStlpecDbTyp(_CudConsts.DB_TYP_STRING);
				} else {
					dtoNew.setCiselnikStlpecDbTyp(dto.getDbTyp());
				}
				dtoNew.setZmena("T");
				dtoNew.setFormZobrazenie("T");
				if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dtoNew.getCiselnikStlpecTyp())) {
					dtoNew.setPovinny(dto.getPovinny());
				} else {
					dtoNew.setPovinny(_CudConsts.ZMENA_OPERACIA_N.equals(operacia) ? dto.getPovinny() : "F");
				}
				dtoNew.setCiselnikTabulka(tabulka);
				listDTO.add(dtoNew);
			}

			return listDTO;

		} catch (Throwable t) {
			DBUtils.handleException(t, "metaList.error");
			return null;
		}
	}

	public DTODynCiselnikExport exportPrint(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTOCiselnik dtoCis = getDelegate().getCiselnikRead().readLight(auth, dtoDyn.getCiselnikID());

			_ICudPrint cudPrint = _CudConsts.PRINT_TYP_VSETKY.equals(dtoExp.getTyp()) ? getDelegate().getDynCiselnikPrint() : getDelegate().getZmenaStlpecPrint();

			if (_CudConsts.PRINT_TYP_VSETKY.equals(dtoExp.getTyp())) {

				if (_CudConsts.PRINT_FORMAT_PDF.equals(dtoExp.getFormat())) {

					if (_CudConsts.CISELNIK_PRINT_CLASS_T_DEFINICNY_USEK.equals(dtoCis.getPrintClass())) {
						cudPrint = getDelegate().getTDefinicnyUsekPrintPdf();
					} else if (_CudConsts.CISELNIK_PRINT_CLASS_T_TRATOVY_USEK.equals(dtoCis.getPrintClass())) {
						cudPrint = getDelegate().getTTratovyUsekPrintPdf();
					} else if (_CudConsts.CISELNIK_PRINT_CLASS_T_USEK_DOPRAVNEJ_CESTY.equals(dtoCis.getPrintClass())) {
						// cudPrint = getDelegate().getDynCiselnikPrint();
					} else if (_CudConsts.CISELNIK_PRINT_CLASS_T_VLAKOVY_USEK.equals(dtoCis.getPrintClass())) {
						cudPrint = getDelegate().getTVlakovyUsekPrintPdf();
					}
				}

				if (_CudConsts.TABULKA_T_HRANICNY_PRIECHOD.equals(dtoCis.getTabulka())) {
					cudPrint = getDelegate().getTHranicnyPriechodPrint();
				}
			}

			return cudPrint.exportPrint(auth, dtoExp, dtoDyn, dtoCis);

		} catch (Throwable t) {
			handleException(t, "exportPrint.error", auth);
			return null;
		}
	}

	public DTODynCiselnikExport exportPrintKontrola(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTOCiselnik dtoCis = getDelegate().getCiselnikRead().readLight(auth, dtoDyn.getCiselnikID());

			_ICudPrint cudPrint = _CudConsts.PRINT_TYP_VSETKY.equals(dtoExp.getTyp()) ? getDelegate().getDynCiselnikPrint() : getDelegate().getZmenaStlpecPrint();

			if (_CudConsts.PRINT_TYP_VSETKY.equals(dtoExp.getTyp())) {

				if (_CudConsts.PRINT_FORMAT_PDF.equals(dtoExp.getFormat())) {

					if (_CudConsts.CISELNIK_PRINT_CLASS_T_DEFINICNY_USEK.equals(dtoCis.getPrintClass())) {
						cudPrint = getDelegate().getTDefinicnyUsekPrintPdf();
					} else if (_CudConsts.CISELNIK_PRINT_CLASS_T_TRATOVY_USEK.equals(dtoCis.getPrintClass())) {
						cudPrint = getDelegate().getTTratovyUsekPrintPdf();
					} else if (_CudConsts.CISELNIK_PRINT_CLASS_T_USEK_DOPRAVNEJ_CESTY.equals(dtoCis.getPrintClass())) {
						// cudPrint = getDelegate().getDynCiselnikPrint();
					} else if (_CudConsts.CISELNIK_PRINT_CLASS_T_VLAKOVY_USEK.equals(dtoCis.getPrintClass())) {
						cudPrint = getDelegate().getTVlakovyUsekPrintPdf();
					}
				}

				if (_CudConsts.TABULKA_T_HRANICNY_PRIECHOD.equals(dtoCis.getTabulka())) {
					cudPrint = getDelegate().getTHranicnyPriechodPrint();
				}
			}

			return cudPrint.exportPrintKontrola(auth, dtoExp, dtoDyn, dtoCis);

		} catch (Throwable t) {
			handleException(t, "exportPrintKontrola.error", auth);
			return null;
		}
	}

	public DTOObjektStlpec[] opravnenieList(AuthInfo auth, Integer ciselnikID, String readOnly) throws AppException {

		try {
			List<DTOObjektStlpec> resultList = new ArrayList<DTOObjektStlpec>();

			if (StringUtils.isValid(auth.getDopravcaKod()) && _CudConsts.ID_T_DOPRAVCA.intValue() == ciselnikID.intValue()) {

				resultList.addAll(getDelegate().getObjektStlpecRead().listByCiselnik(auth, ciselnikID));
				_CudLookupUtils.lookupDTOObjektStlpec(resultList.toArray(new DTOObjektStlpec[resultList.size()]), _CudConsts.NAZOV_CISLO_PIS).setHodnota(auth.getDopravcaKod());

			} else {

				List<Rola> rolaList = FrameworkUtils.getAuthMod().rolaListByAccount(auth.getAccountName());
				Set<String> kodRolySet = new HashSet<String>();
				for (Rola dto : rolaList) {
					if (_CudConsts.ROLA_MODUL_KODs.contains(dto.getKodRoly())) {
						kodRolySet.add(dto.getKodRoly());
					}
				}
				String[] poleRola = kodRolySet.toArray(new String[kodRolySet.size()]);

				boolean b = true;
				Set<Integer> objektIDs = new HashSet<Integer>();
				List<DTOObjektStlpec> allList = new ArrayList<DTOObjektStlpec>();

				List<DTOOdberatelObjekt> ooList = getDelegate().getOdberatelObjektRead().list(auth, new Date(), _CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_ZMENA, poleRola);
				for (DTOOdberatelObjekt dto : ooList) {
					if ("T".equals(dto.getVsetkyCiselniky())) {
						allList.addAll(getDelegate().getObjektStlpecRead().listByCiselnik(auth, ciselnikID));
						b = false;
					} else if (StringUtils.isValid(dto.getIDObjekt())) {
						objektIDs.add(dto.getIDObjekt());
					}
				}

				List<DTOObjektCiselnik> ocList = getDelegate().getObjektCiselnikRead().list(auth, ciselnikID, objektIDs.toArray(new Integer[objektIDs.size()]));

				Set<Integer> objektCiselnikIDs = new HashSet<Integer>();
				for (DTOObjektCiselnik dto : ocList) {
					if (b && "T".equals(dto.getVsetky())) {
						allList.addAll(getDelegate().getObjektStlpecRead().listByCiselnik(auth, ciselnikID));
						b = false;
					} else {
						objektCiselnikIDs.add(dto.getObjektCiselnikID());
					}
				}
				if (!objektCiselnikIDs.isEmpty()) {
					List<DTOObjektStlpec> osList = getDelegate().getObjektStlpecRead().list(auth, objektCiselnikIDs.toArray(new Integer[objektCiselnikIDs.size()]));
					if (!osList.isEmpty()) {
						allList.addAll(osList);
					}
				}

				if ("T".equals(readOnly)) {
					for (DTOObjektStlpec dtoOS : allList) {
						dtoOS.setZmena("F");
					}
					resultList.addAll(allList);

				} else {
					Set<Integer> set = new HashSet<Integer>();
					for (DTOObjektStlpec dtoOS : allList) {
						if ("F".equals(dtoOS.getZmena())) {
							set.add(dtoOS.getIDCiselnikStlpec());
						}
					}
					for (DTOObjektStlpec dtoOS : allList) {
						if ("F".equals(dtoOS.getZmena()) && set.contains(dtoOS.getIDCiselnikStlpec())) {
							continue;
						}
						resultList.add(dtoOS);
					}
				}
			}

			Set<String> ciselnikStlpecIDs = new HashSet<String>();
			for (DTOObjektStlpec dto : resultList) {
				ciselnikStlpecIDs.add(dto.getIDCiselnikStlpec().toString());
			}

			log.info("V ciselniku ciselnikID={} ma pouzivatel accountName={} pristup k tymto stlpcom={}", ciselnikID, auth.getAccountName(), Arrays.toString(ciselnikStlpecIDs.toArray(new String[ciselnikStlpecIDs.size()])));

			return resultList.toArray(new DTOObjektStlpec[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "opravnenieList.error", auth);
			return null;
		}
	}

	public DTOPlugin[] pluginList(AuthInfo auth, Integer ciselnikID, String typ, Date platnostOd) throws AppException {

		try {
			DTOPlugin[] pluginList = getDelegate().getPluginRead().list(auth, ciselnikID, typ, platnostOd);

			Set<Integer> set = new HashSet<Integer>();
			for (DTOPlugin dto : pluginList) {
				set.add(dto.getPluginID());
			}

			Map<Integer, List<DTOPluginStlpec>> mapa = null;
			if (StringUtils.isValid(pluginList)) {
				mapa = getDelegate().getPluginStlpecRead().map(auth, set.toArray(new Integer[set.size()]));
			}

			for (DTOPlugin dto : pluginList) {
				List<DTOPluginStlpec> psList = mapa.get(dto.getPluginID());
				if (StringUtils.isValid(psList) && !psList.isEmpty()) {
					dto.setPluginStlpecList(psList.toArray(new DTOPluginStlpec[psList.size()]));
				}
			}

			return pluginList;

		} catch (Throwable t) {
			handleException(t, "pluginList.error", auth);
			return null;
		}
	}

	public DTOMainHead mainHeadRead(AuthInfo auth, DTOMainHead dtoF) throws AppException {

		try {
			DTOMainHead resultDTO = new DTOMainHead();

			Set<String> set = new HashSet<String>();
			if (StringUtils.isValid(dtoF.getObjektStlpecList())) {
				for (DTOObjektStlpec dto : dtoF.getObjektStlpecList()) {
					if (!StringUtils.isValid(dto.getIDCiselnikStlpec())) {
						set.add(dto.getCiselnikStlpecNazov());
					}
				}
			}
			Map<String, Integer> csMap = getDelegate().getCiselnikStlpecRead().map(auth, dtoF.getCiselnikID(), set.toArray(new String[set.size()]));
			if (!csMap.keySet().isEmpty() && "T".equals(dtoF.getReadObjektStlpecList())) {
				for (DTOObjektStlpec dto : dtoF.getObjektStlpecList()) {
					Integer ciselnikStlpecID = csMap.get(dto.getCiselnikStlpecNazov());
					if (StringUtils.isValid(ciselnikStlpecID)) {
						dto.setIDCiselnikStlpec(ciselnikStlpecID);
					}
				}
				resultDTO.setObjektStlpecList(dtoF.getObjektStlpecList());
			}

			DTODynCiselnik dtoDynF = new DTODynCiselnik();
			dtoDynF.setCiselnikID(dtoF.getCiselnikID());
			dtoDynF.setTabulka(dtoF.getCiselnikTabulka());
			dtoDynF.setPlatnostOd(dtoF.getPlatnostOd());
			dtoDynF.setObjektStlpecList(StringUtils.isValid(resultDTO.getObjektStlpecList()) ? resultDTO.getObjektStlpecList() : dtoF.getObjektStlpecList());
			dtoDynF.setListZobrazenie("T");
			DTOCiselnikStlpecGui[] metaList = getDelegate().getDynCiselnikRead().metaListForData(auth, dtoDynF);
			resultDTO.setPocet(getDelegate().getDynCiselnikRead().count(auth, dtoDynF, metaList));

			if (resultDTO.getPocet().intValue() == 1) {

				if ("T".equals(dtoF.getReadObjektStlpecList())) {
					DTOObjektStlpec[] osList = opravnenieList(auth, dtoF.getCiselnikID(), "T");
					List<DTOObjektStlpec> listNew = new ArrayList<DTOObjektStlpec>(Arrays.asList(osList));
					listNew.addAll(new ArrayList<DTOObjektStlpec>(Arrays.asList(resultDTO.getObjektStlpecList())));
					resultDTO.setObjektStlpecList(listNew.toArray(new DTOObjektStlpec[listNew.size()]));
				}

				DTODynCiselnik[] dynList = getDelegate().getDynCiselnikRead().list(auth, new Page(true), dtoDynF, metaList);
				resultDTO.setHistID(dynList[0].getHistID());
			}

			resultDTO.setPlatnostOd(dtoF.getPlatnostOd());
			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "mainHeadRead.error", auth);
			return null;
		}
	}

	public DTODynCiselnikMeta metaRead(AuthInfo auth, DTODynCiselnikMeta dtoF) throws AppException {

		try {
			DTODynCiselnikMeta resultDTO = new DTODynCiselnikMeta();

			resultDTO.setCiselnikReadOnly(getDelegate().getWfDefRead().maOpravnenieModify(auth, dtoF.getCiselnikID()) ? "F" : "T");
			if ("F".equals(resultDTO.getCiselnikReadOnly())) {
				resultDTO.setCiselnikReadOnly(StringUtils.isValid(getDelegate().getUzamknutieRead().cisReadLight(auth, dtoF.getCiselnikID())) ? "T" : "F");
			}
			log.info("accountName={}, ciselnikID={}, ciselnikReadOnly={}", auth.getAccountName(), dtoF.getCiselnikID(), resultDTO.getCiselnikReadOnly());

			resultDTO.setObjektStlpecList(opravnenieList(auth, dtoF.getCiselnikID(), resultDTO.getCiselnikReadOnly()));
			if (!StringUtils.isValid(resultDTO.getObjektStlpecList())) {
				resultDTO.setErrMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_116));
				return resultDTO;
			}

			Integer pocet = getDelegate().getDynCiselnikRead().count(auth, dtoF.getCiselnikTabulka(), null, dtoF.getPlatnostOd(), null);
			if (!StringUtils.isValid(pocet) || pocet.intValue() == 0) {
				resultDTO.setErrMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_202));
				return resultDTO;
			}

			if ("T".equals(dtoF.getListZobrazenie())) {
				resultDTO.setMetaList(getDelegate().getCiselnikStlpecGuiRead().listForList(auth, dtoF.getCiselnikID(), dtoF.getPlatnostOd()));
			} else if ("T".equals(dtoF.getPopupZobrazenie())) {
				resultDTO.setMetaList(getDelegate().getCiselnikStlpecGuiRead().listForPop(auth, dtoF.getCiselnikID(), dtoF.getPlatnostOd()));
			}

			if ("T".equals(dtoF.getListZobrazenie())) {
				resultDTO.setPluginList(pluginList(auth, dtoF.getCiselnikID(), _CudConsts.PLUGIN_TYP_DOPLNENIE, dtoF.getPlatnostOd()));
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "metaRead.error", auth);
			return null;
		}
	}

}
