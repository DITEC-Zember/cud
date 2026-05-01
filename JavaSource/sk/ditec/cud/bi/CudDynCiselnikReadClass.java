package sk.ditec.cud.bi;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.Normalizer;
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

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynCiselnik;
import sk.ditec.cud.dto.DTODynCiselnikLD;
import sk.ditec.cud.dto.DTODynValue;
import sk.ditec.cud.dto.DTOFutDynCiselnik;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOSubor;
import sk.ditec.cud.dto.DTOValidate;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.zsr.common.server.utils.DateUtils;

import com.workingdogs.village.Record;
import com.workingdogs.village.Value;

public class CudDynCiselnikReadClass extends _CudBaseClass {

	private String getValueAsString(String dbTyp, Value value, boolean convertBoolean) throws Exception {

		try {
			Object obj = null;

			if (value.isNull()) {
				return null;
			}

			if (_CudConsts.DB_TYP_DATE.equalsIgnoreCase(dbTyp)) {
				Date d = value.asUtilDate();
				obj = StringUtils.isValid(d) ? DateUtils.formatDateDDMMYYYY(d) : null;

			} else if (_CudConsts.DB_TYP_BOOLEAN.equalsIgnoreCase(dbTyp)) {
				obj = value.asString();
				if (convertBoolean) {
					String s = value.asString();
					if (StringUtils.isValid(s)) {
						obj = "T".equals(s) ? "Áno" : "Nie";
					}
				}

			} else if (_CudConsts.DB_TYP_DOUBLE.equalsIgnoreCase(dbTyp)) {
				obj = value.asDoubleObj();

			} else if (_CudConsts.DB_TYP_INTEGER.equalsIgnoreCase(dbTyp)) {
				obj = value.asIntegerObj();

			} else {
				obj = value.asString();
			}

			return StringUtils.isValid(obj) ? obj.toString().trim() : null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getValueAsString.error");
			return null;
		}
	}

	private String getDoubleValueAsSql(String columnName, Integer dlzka, Integer decimals) throws Exception {

		try {
			String patern1 = String.format("%" + decimals + "s", "0").replace(" ", "0");
			String patern21 = String.format("%" + (dlzka - decimals) + "s", "9").replace(" ", "9");
			String patern22 = String.format("%" + decimals + "s", "9").replace(" ", "9");
			String patern2 = patern21 + "." + patern22;

			return "CASE WHEN " + columnName + " < 1 AND " + columnName + " > -1 THEN trim(to_char(" + columnName + ", \'0." + patern1 + "\')) ELSE trim(to_char(" + columnName + ", \'" + patern2 + "\')) END";

		} catch (Throwable t) {
			DBUtils.handleException(t, "getDoubleValueAsSql.error");
			return null;
		}
	}

	private String fkSubSql(String fkTabulka, String pkNazov, String fkNazov, Date d) throws AppException {

		try {
			String columns = pkNazov;

			columns += ", " + fkNazov;

			String datum = DateUtils.formatDateDDMMYYYY(d);

			String con1 = _CudConsts.NAZOV_PLATNOST_OD + " <= to_timestamp(\'" + datum + "\', \'DD.MM.YYYY\')";
			String con2 = "((" + _CudConsts.NAZOV_PLATNOST_DO + " >= to_timestamp(\'" + datum + "\', \'DD.MM.YYYY\')) OR (" + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL))";
			String con3 = _CudConsts.NAZOV_ZMAZ + "= \'F\'";
			String where = con1 + " AND " + con2 + " AND " + con3;

			return "SELECT " + columns + " FROM " + fkTabulka + " WHERE " + where;

		} catch (Throwable t) {
			DBUtils.handleException(t, "fkSubSql.error");
			return null;
		}
	}

	public DTODynCiselnik[] list(AuthInfo auth, Page page, DTODynCiselnik dtoF, DTOCiselnikStlpecGui[] metaList) throws AppException {

		try {
			Map<Integer, List<String>> obmMap = new HashMap<Integer, List<String>>();
			if (StringUtils.isValid(dtoF.getObjektStlpecList())) {
				for (DTOObjektStlpec dto : dtoF.getObjektStlpecList()) {
					if (StringUtils.isValid(dto.getHodnota())) {
						if (!StringUtils.isValid(obmMap.get(dto.getIDCiselnikStlpec()))) {
							obmMap.put(dto.getIDCiselnikStlpec(), new ArrayList<String>());
						}
						obmMap.get(dto.getIDCiselnikStlpec()).add(dto.getHodnota());
					}
				}
			}

			if (!StringUtils.isValid(metaList)) {
				metaList = metaListForData(auth, dtoF);
			}

			return list(auth, page, dtoF, metaList, obmMap);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTODynCiselnik[] list(AuthInfo auth, Page page, DTODynCiselnik dtoF, DTOCiselnikStlpecGui[] metaList, Map<Integer, List<String>> obmMap) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String columns = dtoF.getTabulka() + "." + _CudConsts.NAZOV_HIST_ID;
			String joinTables = "";
			int fkIndex = 0;
			String conditionals = "";
			String obmConditionals = "";
			String sortColumns = "";
			int sortColumnsIndex = 0;
			int colIndex = 0;
			int lookupColIndex = 0;

			DTOCiselnikStlpecGui dtoLookupCSG = null;

			for (DTOCiselnikStlpecGui dto : metaList) {

				String column = null;
				if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getCiselnikStlpecTyp()) || _CudConsts.CISELNIK_STLPEC_TYP_AT.equals(dto.getCiselnikStlpecTyp())) {

					column = dtoF.getTabulka() + "." + dto.getCiselnikStlpecNazov();

					if (dto.getCiselnikStlpecNazov().equals(dtoF.getLookupColumnName())) {
						dtoLookupCSG = dto;
					}

				} else if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dto.getCiselnikStlpecTyp())) {

					String fkNazov = null;
					if (StringUtils.isValid(dto.getFk1FkNazov())) {
						String subSql = fkSubSql(dto.getCiselnikStlpecFk1CiselnikTabulka(), dto.getCiselnikStlpecFk1PkNazov(), dto.getFk1FkNazov(), dtoF.getPlatnostOd());
						String alias = "(" + subSql + ") t" + ++fkIndex;
						String condition = dtoF.getTabulka() + "." + dto.getCiselnikStlpecNazov() + " = t" + fkIndex + "." + dto.getCiselnikStlpecFk1PkNazov();
						joinTables += " LEFT JOIN " + alias + " ON " + condition;
						fkNazov = dto.getFk1FkNazov();
					}

					if (StringUtils.isValid(dto.getFk2FkNazov())) {
						String subSql = fkSubSql(dto.getFk2CiselnikTabulka(), dto.getFk2PkNazov(), dto.getFk2FkNazov(), dtoF.getPlatnostOd());
						String alias = "(" + subSql + ") t" + ++fkIndex;
						String condition = "t" + (fkIndex - 1) + "." + dto.getFk1FkNazov() + " = t" + fkIndex + "." + dto.getFk2PkNazov();
						joinTables += " LEFT JOIN " + alias + " ON " + condition;
						fkNazov = dto.getFk2FkNazov();
					}

					column = "t" + fkIndex + "." + fkNazov;

				} else {
					throw new AppException("Neocakavany typ atributu");
				}

				if (_CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
					column = getDoubleValueAsSql(column, dto.getDlzka(), dto.getDecimals());
				}

				if ("T".equals(dto.getListZobrazenie()) || "T".equals(dto.getPopupZobrazenie())) {
					dto.setAlias("c" + ++colIndex);
				} else {
					dto.setAlias("lookup" + ++lookupColIndex);
				}

				if (_CudConsts.DB_TYP_INTEGER.equals(dto.getCiselnikStlpecDbTyp()) && "T".equals(dto.getCiselnikStlpecJeDbString())) {
					column = "TO_NUMBER(" + column + ")";
				}

				columns += ", " + column + " AS " + dto.getAlias();

				if (_CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
					String sortColumn = "CAST (replace(" + dto.getAlias() + ", \'.\', \',\') AS NUMBER(" + dto.getDlzka() + "," + dto.getDecimals() + "))";
					sortColumns += ", " + sortColumn + " AS d" + ++sortColumnsIndex;
				}

				String filterValue = null;
				if ("T".equals(dto.getListZobrazenie()) || "T".equals(dto.getPopupZobrazenie())) {
					if (StringUtils.isValid(dtoF.getValues()) && StringUtils.isValid(dtoF.getValues()[colIndex - 1])) {
						filterValue = dtoF.getValues()[colIndex - 1].getValueStr();
					}
				}
				if (StringUtils.isValid(filterValue)) {

					String conditional = "";

					if (_CudConsts.DB_TYP_STRING.equals(dto.getCiselnikStlpecDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
						conditional = dto.getAlias() + " LIKE \'" + (filterValue.contains("'") ? StringUtils.replaceAll(filterValue, "'", "''") : filterValue) + "%\'";

					} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dto.getCiselnikStlpecDbTyp())) {
						String s = Normalizer.normalize(filterValue, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
						if ("ano".equalsIgnoreCase(s)) {
							filterValue = "T";
						} else if ("nie".equalsIgnoreCase(s)) {
							filterValue = "F";
						}
						conditional = dto.getAlias() + " = \'" + filterValue + "\'";

					} else if (_CudConsts.DB_TYP_INTEGER.equals(dto.getCiselnikStlpecDbTyp())) {
						conditional = "to_char(" + dto.getAlias() + ") LIKE \'" + filterValue + "%\'";

					} else if (_CudConsts.DB_TYP_DATE.equals(dto.getCiselnikStlpecDbTyp())) {
						conditional = "to_char(" + dto.getAlias() + ", 'DD.MM.YYYY')" + " = " + filterValue;
					}

					conditionals += StringUtils.isValid(conditionals) ? " " + dtoF.getDynFilterTyp() + " " + conditional : conditional;
				}

				List<String> obmList = obmMap.get(dto.getIDCiselnikStlpec());
				if (StringUtils.isValid(obmList) && !obmList.isEmpty()) {

					// String conditional = "";

					for (String obmValue : obmList) {

						String s = null;
						if (_CudConsts.DB_TYP_STRING.equals(dto.getCiselnikStlpecDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
							s = dto.getAlias() + " LIKE \'" + obmValue + "%\'";

						} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dto.getCiselnikStlpecDbTyp())) {
							s = dto.getAlias() + " = \'" + obmValue + "\'";

						} else if (_CudConsts.DB_TYP_INTEGER.equals(dto.getCiselnikStlpecDbTyp())) {
							s = "to_char(" + dto.getAlias() + ") LIKE \'" + obmValue + "%\'";

						} else if (_CudConsts.DB_TYP_DATE.equals(dto.getCiselnikStlpecDbTyp())) {
							s = "to_char(" + dto.getAlias() + ", 'DD.MM.YYYY')" + " = " + obmValue;
						}

						obmConditionals += StringUtils.isValid(obmConditionals) ? " OR " + s : s;
					}

					// conditionals += StringUtils.isValid(conditionals) ? " AND (" + conditional + " ) " : "(" + conditional + " )";
				}
			}

			if (StringUtils.isValid(obmConditionals)) {
				conditionals = StringUtils.isValid(conditionals) ? " (" + conditionals + " ) " : conditionals;
				conditionals += StringUtils.isValid(conditionals) ? " AND (" + obmConditionals + " ) " : obmConditionals;
			}

			String datum = DateUtils.formatDateDDMMYYYY(dtoF.getPlatnostOd());
			String nazovAtributu = dtoF.getTabulka() + "." + _CudConsts.NAZOV_PLATNOST_OD;
			String con1 = nazovAtributu + " <= to_timestamp(\'" + datum + "\', \'DD.MM.YYYY\')";
			nazovAtributu = dtoF.getTabulka() + "." + _CudConsts.NAZOV_PLATNOST_DO;
			String con2 = "((" + nazovAtributu + " >= to_timestamp(\'" + datum + "\', \'DD.MM.YYYY\')) OR (" + nazovAtributu + " IS NULL))";
			nazovAtributu = dtoF.getTabulka() + "." + _CudConsts.NAZOV_ZMAZ;
			String con3 = nazovAtributu + "= \'F\'";
			String where = con1 + " AND " + con2 + " AND " + con3;

			String sql = "SELECT t.* " + sortColumns + " FROM (SELECT " + columns + " FROM " + dtoF.getTabulka() + " " + joinTables + " WHERE " + where + ") t";
			if (StringUtils.isValid(conditionals)) {
				sql += " WHERE " + conditionals;
			}

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, _CudConsts.NAZOV_HIST_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTODynCiselnik> listDTO = new ArrayList<DTODynCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTODynCiselnik dto = new DTODynCiselnik();
				dto.setHistID(rVal(r, _CudConsts.NAZOV_HIST_ID).asIntegerObj());
				dto.setRowID(rVal(r, "c1").asIntegerObj());
				dto.setValues(new DTODynValue[colIndex]);
				dto.setTabulka(dtoF.getTabulka());
				dto.setCiselnikID(dtoF.getCiselnikID());
				int i = 0;
				int lookupIndex = 0;
				for (DTOCiselnikStlpecGui dtoCS : metaList) {
					Value value = rVal(r, dtoCS.getAlias());
					String valueStr = getValueAsString(dtoCS.getCiselnikStlpecDbTyp(), value, true);

					if ("T".equals(dtoCS.getListZobrazenie()) || "T".equals(dtoCS.getPopupZobrazenie())) {
						DTODynValue dtoNew = new DTODynValue();
						dtoNew.setValueStr(valueStr);
						dto.getValues()[i++] = dtoNew;
					}

					if (StringUtils.isValid(dtoLookupCSG) && dtoLookupCSG.getCiselnikStlpecGuiID().intValue() == dtoCS.getCiselnikStlpecGuiID().intValue()) {
						dto.setLookupValueShort(valueStr);
					}

					if ("T".equals(dtoCS.getLookupZobrazenie())) {
						if (lookupIndex == 0) {
							dto.setLookupValueLong(valueStr);
						} else if (lookupIndex == 1) {
							dto.setLookupValueLong(dto.getLookupValueLong() + " (" + valueStr);
						} else {
							dto.setLookupValueLong(dto.getLookupValueLong() + ", " + valueStr);
						}
						lookupIndex++;
					}
				}

				if (lookupIndex > 1) {
					dto.setLookupValueLong(dto.getLookupValueLong() + ")");
				}

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTODynCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public Integer count(AuthInfo auth, DTODynCiselnik dtoF, DTOCiselnikStlpecGui[] metaList) throws AppException {

		try {
			Map<Integer, List<String>> obmMap = new HashMap<Integer, List<String>>();
			if (StringUtils.isValid(dtoF.getObjektStlpecList())) {
				for (DTOObjektStlpec dto : dtoF.getObjektStlpecList()) {
					if (StringUtils.isValid(dto.getHodnota())) {
						if (!StringUtils.isValid(obmMap.get(dto.getIDCiselnikStlpec()))) {
							obmMap.put(dto.getIDCiselnikStlpec(), new ArrayList<String>());
						}
						obmMap.get(dto.getIDCiselnikStlpec()).add(dto.getHodnota());
					}
				}
			}

			if (!StringUtils.isValid(metaList)) {
				metaList = metaListForData(auth, dtoF);
			}

			return count(auth, dtoF, metaList, obmMap);

		} catch (Throwable t) {
			handleException(t, "count.error", auth);
			return null;
		}
	}

	private Integer count(AuthInfo auth, DTODynCiselnik dtoF, DTOCiselnikStlpecGui[] metaList, Map<Integer, List<String>> obmMap) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String columns = dtoF.getTabulka() + "." + _CudConsts.NAZOV_HIST_ID;
			String joinTables = "";
			int fkIndex = 0;
			String conditionals = "";
			String sortColumns = "";
			int sortColumnsIndex = 0;
			int colIndex = 0;
			int lookupColIndex = 0;

			for (DTOCiselnikStlpecGui dto : metaList) {

				String column = null;
				if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getCiselnikStlpecTyp()) || _CudConsts.CISELNIK_STLPEC_TYP_AT.equals(dto.getCiselnikStlpecTyp())) {

					column = dtoF.getTabulka() + "." + dto.getCiselnikStlpecNazov();

				} else if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dto.getCiselnikStlpecTyp())) {

					String fkNazov = null;
					if (StringUtils.isValid(dto.getFk1FkNazov())) {
						String subSql = fkSubSql(dto.getCiselnikStlpecFk1CiselnikTabulka(), dto.getCiselnikStlpecFk1PkNazov(), dto.getFk1FkNazov(), dtoF.getPlatnostOd());
						String alias = "(" + subSql + ") t" + ++fkIndex;
						String condition = dtoF.getTabulka() + "." + dto.getCiselnikStlpecNazov() + " = t" + fkIndex + "." + dto.getCiselnikStlpecFk1PkNazov();
						joinTables += " LEFT JOIN " + alias + " ON " + condition;
						fkNazov = dto.getFk1FkNazov();
					}

					if (StringUtils.isValid(dto.getFk2FkNazov())) {
						String subSql = fkSubSql(dto.getFk2CiselnikTabulka(), dto.getFk2PkNazov(), dto.getFk2FkNazov(), dtoF.getPlatnostOd());
						String alias = "(" + subSql + ") t" + ++fkIndex;
						String condition = "t" + (fkIndex - 1) + "." + dto.getFk1FkNazov() + " = t" + fkIndex + "." + dto.getFk2PkNazov();
						joinTables += " LEFT JOIN " + alias + " ON " + condition;
						fkNazov = dto.getFk2FkNazov();
					}

					column = "t" + fkIndex + "." + fkNazov;

				} else {
					throw new AppException("Neocakavany typ atributu");
				}

				if (_CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
					column = getDoubleValueAsSql(column, dto.getDlzka(), dto.getDecimals());
				}

				if ("T".equals(dto.getListZobrazenie()) || "T".equals(dto.getPopupZobrazenie())) {
					dto.setAlias("c" + ++colIndex);
				} else {
					dto.setAlias("lookup" + ++lookupColIndex);
				}

				if (_CudConsts.DB_TYP_INTEGER.equals(dto.getCiselnikStlpecDbTyp()) && "T".equals(dto.getCiselnikStlpecJeDbString())) {
					column = "TO_NUMBER(" + column + ")";
				}

				columns += ", " + column + " AS " + dto.getAlias();

				if (_CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
					String sortColumn = "CAST (replace(" + dto.getAlias() + ", \'.\', \',\') AS NUMBER(" + dto.getDlzka() + "," + dto.getDecimals() + "))";
					sortColumns += ", " + sortColumn + " AS d" + ++sortColumnsIndex;
				}

				String filterValue = null;
				if ("T".equals(dto.getListZobrazenie()) || "T".equals(dto.getPopupZobrazenie())) {
					if (StringUtils.isValid(dtoF.getValues()) && StringUtils.isValid(dtoF.getValues()[colIndex - 1])) {
						filterValue = dtoF.getValues()[colIndex - 1].getValueStr();
					}
				}
				if (StringUtils.isValid(filterValue)) {

					String conditional = "";

					if (_CudConsts.DB_TYP_STRING.equals(dto.getCiselnikStlpecDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
						conditional = dto.getAlias() + " LIKE \'" + filterValue + "%\'";

					} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dto.getCiselnikStlpecDbTyp())) {
						String s = Normalizer.normalize(filterValue, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
						if ("ano".equalsIgnoreCase(s)) {
							filterValue = "T";
						} else if ("nie".equalsIgnoreCase(s)) {
							filterValue = "F";
						}
						conditional = dto.getAlias() + " = \'" + filterValue + "\'";

					} else if (_CudConsts.DB_TYP_INTEGER.equals(dto.getCiselnikStlpecDbTyp())) {
						conditional = "to_char(" + dto.getAlias() + ") LIKE \'" + filterValue + "%\'";

					} else if (_CudConsts.DB_TYP_DATE.equals(dto.getCiselnikStlpecDbTyp())) {
						conditional = "to_char(" + dto.getAlias() + ", 'DD.MM.YYYY')" + " = " + filterValue;
					}

					conditionals += StringUtils.isValid(conditionals) ? " AND " + conditional : conditional;
				}

				List<String> obmList = obmMap.get(dto.getIDCiselnikStlpec());
				if (StringUtils.isValid(obmList) && !obmList.isEmpty()) {

					String conditional = "";

					for (String obmValue : obmList) {

						String s = null;
						if (_CudConsts.DB_TYP_STRING.equals(dto.getCiselnikStlpecDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
							s = dto.getAlias() + " LIKE \'" + obmValue + "%\'";

						} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dto.getCiselnikStlpecDbTyp())) {
							s = dto.getAlias() + " = \'" + obmValue + "\'";

						} else if (_CudConsts.DB_TYP_INTEGER.equals(dto.getCiselnikStlpecDbTyp())) {
							s = "to_char(" + dto.getAlias() + ") LIKE \'" + obmValue + "%\'";

						} else if (_CudConsts.DB_TYP_DATE.equals(dto.getCiselnikStlpecDbTyp())) {
							s = "to_char(" + dto.getAlias() + ", 'DD.MM.YYYY')" + " = " + obmValue;
						}

						conditional += StringUtils.isValid(conditional) ? " OR " + s : s;
					}

					conditionals += StringUtils.isValid(conditionals) ? " AND (" + conditional + " ) " : "(" + conditional + " )";
				}
			}

			String datum = DateUtils.formatDateDDMMYYYY(dtoF.getPlatnostOd());
			String nazovAtributu = dtoF.getTabulka() + "." + _CudConsts.NAZOV_PLATNOST_OD;
			String con1 = nazovAtributu + " <= to_timestamp(\'" + datum + "\', \'DD.MM.YYYY\')";
			nazovAtributu = dtoF.getTabulka() + "." + _CudConsts.NAZOV_PLATNOST_DO;
			String con2 = "((" + nazovAtributu + " >= to_timestamp(\'" + datum + "\', \'DD.MM.YYYY\')) OR (" + nazovAtributu + " IS NULL))";
			nazovAtributu = dtoF.getTabulka() + "." + _CudConsts.NAZOV_ZMAZ;
			String con3 = nazovAtributu + "= \'F\'";
			String where = con1 + " AND " + con2 + " AND " + con3;

			String sql = "SELECT count(*) as pocet FROM (SELECT " + columns + " FROM " + dtoF.getTabulka() + " " + joinTables + " WHERE " + where + ") t";
			if (StringUtils.isValid(conditionals)) {
				sql += " WHERE " + conditionals;
			}

			getConnection(auth);
			predVolanimDotazu(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, "pocet").asIntegerObj();
			}

			return 0;

		} catch (Throwable t) {
			handleException(t, "count.error", auth);
			return null;
		}
	}

	public List<Map<String, String>> listLight(AuthInfo auth, String tabulka, List<DTOCiselnikStlpec> csList, String pkValueID, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String date = DateUtils.formatDateDDMMYYYY(platnostOd);
			String conditions = _CudConsts.NAZOV_PLATNOST_OD + " > TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			conditions += " AND (" + _CudConsts.NAZOV_PLATNOST_OD + " <= " + _CudConsts.NAZOV_PLATNOST_DO + " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";
			// conditions += " AND " + _CudConsts.NAZOV_ZMAZ + " =\'F\'";
			conditions += " AND " + _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList).getNazov() + " = " + pkValueID;

			String columns = "";
			for (DTOCiselnikStlpec dto : csList) {
				columns += StringUtils.isValid(columns) ? ", " + dto.getNazov() : dto.getNazov();
			}

			String sql = "SELECT " + columns.toString() + " FROM " + tabulka + " WHERE " + conditions + " ORDER BY " + _CudConsts.NAZOV_HIST_ID + " ASC";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Map<String, String> rowMap = new HashMap<String, String>();
				for (DTOCiselnikStlpec dtoCS : csList) {
					Value val = rVal(r, dtoCS.getNazov());
					String valStr = getValueAsString(dtoCS.getDbTyp(), val, false);
					if (StringUtils.isValid(valStr)) {
						rowMap.put(dtoCS.getNazov(), valStr);
					}
					if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dtoCS.getTyp())) {
						rowMap.put(_CudConsts.NAZOV_PK_KEY, valStr);
					}
				}

				resultList.add(rowMap);
			}

			return resultList;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	private DTODynCiselnik readLight(AuthInfo auth, DTODynCiselnikLD dtoF, DTOCiselnikStlpecGui[] metaList, Set<Integer> ciselnikIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(dtoF) || !StringUtils.isValid(dtoF.getHistID())) {
				return new DTODynCiselnik();
			}

			String columns = _CudConsts.NAZOV_HIST_ID + ", " + _CudConsts.NAZOV_ID_ZMENA + ", " + _CudConsts.NAZOV_PLATNOST_OD + ", " + _CudConsts.NAZOV_PLATNOST_DO + ", " + _CudConsts.NAZOV_CAS_VYTVORENIA + ", " + _CudConsts.NAZOV_CAS_ZMENY + ", " + _CudConsts.NAZOV_ZMAZ;
			int colIndex = 0;

			for (DTOCiselnikStlpecGui dto : metaList) {

				String column = dtoF.getCiselnikTabulka() + "." + dto.getCiselnikStlpecNazov();

				if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dto.getCiselnikStlpecTyp())) {
					ciselnikIDs.add(dto.getCiselnikStlpecFk1IDCiselnik());
				}

				if (_CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
					column = getDoubleValueAsSql(column, dto.getDlzka(), dto.getDecimals());
				}

				dto.setNadpis("c" + ++colIndex);
				column += " AS " + dto.getNadpis();
				columns += ", " + column;
			}

			String sql = "SELECT " + columns + " FROM " + dtoF.getCiselnikTabulka() + " WHERE " + _CudConsts.NAZOV_HIST_ID + " = " + dtoF.getHistID();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			DTODynCiselnik resultDTO = new DTODynCiselnik();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				resultDTO.setHistID(rVal(r, _CudConsts.NAZOV_HIST_ID).asIntegerObj());
				resultDTO.setRowID(rVal(r, "c1").asIntegerObj());
				resultDTO.setIDZmena(rVal(r, _CudConsts.NAZOV_ID_ZMENA).asIntegerObj());
				resultDTO.setPlatnostOd(rVal(r, _CudConsts.NAZOV_PLATNOST_OD).asUtilDate());
				resultDTO.setPlatnostDo(rVal(r, _CudConsts.NAZOV_PLATNOST_DO).asUtilDate());
				resultDTO.setCasVytvorenia(rVal(r, _CudConsts.NAZOV_CAS_VYTVORENIA).asUtilDate());
				resultDTO.setCasZmeny(rVal(r, _CudConsts.NAZOV_CAS_ZMENY).asUtilDate());
				resultDTO.setZmaz(rVal(r, _CudConsts.NAZOV_ZMAZ).asString());
				resultDTO.setValues(new DTODynValue[metaList.length]);
				int i = 0;
				for (DTOCiselnikStlpecGui dtoCS : metaList) {
					Value value = rVal(r, dtoCS.getNadpis());
					DTODynValue dtoNew = new DTODynValue();
					if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
						dtoNew.setValueID(value.asIntegerObj());
					} else {
						dtoNew.setValueStr(getValueAsString(dtoCS.getCiselnikStlpecDbTyp(), value, true));
					}
					resultDTO.getValues()[i++] = dtoNew;
				}
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	public Map<String, String> readLookupValuesMap(AuthInfo auth, DTODynCiselnik dtoF, List<DTOCiselnikStlpecGui> metaList) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(dtoF.getRowID())) {
				return null;
			}

			String columns = null;
			int colIndex = 0;

			for (DTOCiselnikStlpecGui dto : metaList) {

				if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getCiselnikStlpecTyp())) {
					continue;
				}

				String column = dtoF.getTabulka() + "." + dto.getCiselnikStlpecNazov();

				if (_CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
					column = getDoubleValueAsSql(column, dto.getDlzka(), dto.getDecimals());
				}

				dto.setNadpis("c" + ++colIndex);
				column += " AS " + dto.getNadpis();
				if (StringUtils.isValid(columns)) {
					columns += ", " + column;
				} else {
					columns = column;
				}
			}

			String conditions = dtoF.getPkName() + " = " + dtoF.getRowID();

			String date = DateUtils.formatDateDDMMYYYY(dtoF.getPlatnostOd());
			conditions += " AND " + dtoF.getTabulka() + "." + _CudConsts.NAZOV_PLATNOST_OD + " <= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			conditions += " AND (" + dtoF.getTabulka() + "." + _CudConsts.NAZOV_PLATNOST_DO + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			conditions += " OR " + dtoF.getTabulka() + "." + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			String sql = "SELECT " + columns + " FROM " + dtoF.getTabulka() + " WHERE " + conditions;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<String, String> resultMap = new HashMap<String, String>();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				for (DTOCiselnikStlpecGui dto : metaList) {

					if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getCiselnikStlpecTyp())) {
						continue;
					}

					Value value = rVal(r, dto.getNadpis());
					resultMap.put(dto.getCiselnikStlpecNazov(), getValueAsString(dto.getCiselnikStlpecDbTyp(), value, true));
				}
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "readLookupValue.error", auth);
			return null;
		}
	}

	public Map<String, String> readLookupValues(AuthInfo auth, Integer ciselnikID, Date platnostOd, Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> metaMap, Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> lookupMetaMap, Map<String, String> rowMap) throws AppException {

		try {
			if (!StringUtils.isValid(metaMap.get(platnostOd).get(ciselnikID))) {
				DTOCiselnikStlpecGui[] metaPole = getDelegate().getCiselnikStlpecGuiRead().listForForm(auth, ciselnikID, platnostOd);
				List<DTOCiselnikStlpecGui> metaList = new ArrayList<DTOCiselnikStlpecGui>(Arrays.asList(metaPole));
				metaMap.get(platnostOd).put(ciselnikID, metaList);
			}
			List<DTOCiselnikStlpecGui> metaListForData = metaMap.get(platnostOd).get(ciselnikID);

			Set<Integer> ciselnikIDs = new HashSet<Integer>();
			for (DTOCiselnikStlpecGui dto : metaListForData) {
				if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dto.getCiselnikStlpecTyp())) {
					if (!StringUtils.isValid(lookupMetaMap.get(platnostOd).get(dto.getCiselnikStlpecFk1IDCiselnik()))) {
						ciselnikIDs.add(dto.getCiselnikStlpecFk1IDCiselnik());
					}
				}
			}

			Map<Integer, List<DTOCiselnikStlpecGui>> csMap = getDelegate().getCiselnikStlpecGuiRead().mapForLookup(auth, ciselnikIDs, platnostOd);
			if (StringUtils.isValid(csMap)) {
				for (Integer cisID : csMap.keySet()) {
					lookupMetaMap.get(platnostOd).put(cisID, csMap.get(cisID));
				}
			}

			Map<String, String> resultMap = new HashMap<String, String>();

			for (DTOCiselnikStlpecGui dtoCS : metaListForData) {
				if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
					String valueID = rowMap.get(dtoCS.getCiselnikStlpecNazov());
					if (StringUtils.isValid(valueID)) {
						String valueStr = lookupValueFormat(auth, lookupMetaMap.get(platnostOd), dtoCS.getCiselnikStlpecFk1IDCiselnik(), valueID, platnostOd);
						resultMap.put(dtoCS.getCiselnikStlpecNazov(), valueStr);
					}
				} else {
					resultMap.put(dtoCS.getCiselnikStlpecNazov(), rowMap.get(dtoCS.getCiselnikStlpecNazov()));
				}
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "readLookupValues.error", auth);
			return null;
		}
	}

	public DTOCiselnikStlpecGui[] metaListForData(AuthInfo auth, DTODynCiselnik dto) throws AppException {

		try {
			DTOCiselnikStlpecGui dtoF = new DTOCiselnikStlpecGui();
			dtoF.setCiselnikStlpecIDCiselnik(dto.getCiselnikID());
			dtoF.setPlatnostOd(dto.getPlatnostOd());
			dtoF.setLookupColumnName(dto.getLookupColumnName());
			dtoF.setListZobrazenie(dto.getListZobrazenie());
			dtoF.setPopupZobrazenie(dto.getPopupZobrazenie());

			return getDelegate().getCiselnikStlpecGuiRead().listForData(auth, dtoF);

		} catch (Throwable t) {
			handleException(t, "metaListForData.error", auth);
			return null;
		}
	}

	private DTOCiselnikStlpecGui[] metaListForData(AuthInfo auth, DTODynCiselnikLD dto) throws AppException {

		try {
			DTOCiselnikStlpecGui dtoF = new DTOCiselnikStlpecGui();
			dtoF.setCiselnikStlpecIDCiselnik(dto.getCiselnikID());
			dtoF.setPlatnostOd(dto.getPlatnostOd());
			dtoF.setLookupColumnName(dto.getLookupColumnName());
			dtoF.setFormZobrazenie("T");

			return getDelegate().getCiselnikStlpecGuiRead().listForData(auth, dtoF);

		} catch (Throwable t) {
			handleException(t, "metaListForData.error", auth);
			return null;
		}
	}

	public DTODynCiselnikLD loadData(AuthInfo auth, DTODynCiselnikLD dtoF) throws AppException {

		try {
			DTODynCiselnikLD resultDTO = new DTODynCiselnikLD();
			resultDTO.setMetaList(getDelegate().getCiselnikStlpecGuiRead().listForForm(auth, dtoF.getCiselnikID(), dtoF.getPlatnostOd()));

			if (!StringUtils.isValid(dtoF.getHistID())) {

				List<DTODynValue> valuesList = new ArrayList<DTODynValue>();
				while (valuesList.size() < resultDTO.getMetaList().length) {
					valuesList.add(new DTODynValue());
				}
				DTODynCiselnik dynCiselnikDTO = new DTODynCiselnik();
				dynCiselnikDTO.setValues(valuesList.toArray(new DTODynValue[valuesList.size()]));
				resultDTO.setValueDTO(dynCiselnikDTO);

				return resultDTO;
			}

			Set<Integer> ciselnikIDs = new HashSet<Integer>();
			resultDTO.setValueDTO(readLight(auth, dtoF, metaListForData(auth, dtoF), ciselnikIDs));

			if (getDelegate().getIam().jeUcetZoSkupiny(auth, dtoF.getCiselnikID(), _CudConsts.WF_DEF_TYP_SC)) {
				if (StringUtils.isValid(getDelegate().getUzamknutieRead().rowReadLight(auth, dtoF.getCiselnikID(), resultDTO.getValueDTO().getRowID()))) {
					resultDTO.getValueDTO().setZobrazitOdomknutie("T");
				} else {
					resultDTO.getValueDTO().setZobrazitUzamknutie("T");
				}
			}

			Map<Integer, List<DTOCiselnikStlpecGui>> csMap = getDelegate().getCiselnikStlpecGuiRead().mapForLookup(auth, ciselnikIDs, dtoF.getPlatnostOd());

			int index = 0;
			for (DTOCiselnikStlpecGui dtoCS : resultDTO.getMetaList()) {

				if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
					DTODynValue dynValueDTO = resultDTO.getValueDTO().getValues()[index];
					if (StringUtils.isValid(dynValueDTO) && StringUtils.isValid(dynValueDTO.getValueID())) {
						String valueStr = lookupValueFormat(auth, csMap, dtoCS.getCiselnikStlpecFk1IDCiselnik(), dynValueDTO.getValueID().toString(), dtoF.getPlatnostOd());
						resultDTO.getValueDTO().getValues()[index].setValueStr(valueStr);
					}
				}

				if (_CudConsts.CISELNIK_STLPEC_GUI_EDIT_CONTROL_FILE.equals(dtoCS.getEditControl())) {
					DTODynValue dynValueDTO = resultDTO.getValueDTO().getValues()[index];
					if (StringUtils.isValid(dynValueDTO) && StringUtils.isValid(dynValueDTO.getValueStr())) {
						String valueStr = suborReadLookupValue(auth, "T_" + dtoCS.getCiselnikStlpecNazov().substring(3), Integer.parseInt(dynValueDTO.getValueStr()));
						resultDTO.getValueDTO().getValues()[index].setValueID(Integer.parseInt(dynValueDTO.getValueStr()));
						resultDTO.getValueDTO().getValues()[index].setValueStr(valueStr);
					}
				}

				index++;
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public Integer count(AuthInfo auth, String tabulka, String stlpecNazov, boolean isNull, boolean isNotNull) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String sql = "SELECT COUNT(*) AS pocet FROM " + tabulka + " WHERE platnost_do IS NULL AND zmaz = \'F\' AND " + stlpecNazov;

			if (isNull) {
				sql += " IS NULL";
			}
			if (isNotNull) {
				sql += " IS NOT NULL";
			}

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
			handleException(t, "count.error", auth);
			return null;
		}
	}

	public Integer count(AuthInfo auth, String tabulka, Integer rowID, Date platnostOd, List<DTOCiselnikStlpec> csList) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String date = DateUtils.formatDateDDMMYYYY(platnostOd);
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " <= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			String columnsConditions = "";
			if (StringUtils.isValid(csList)) {
				for (DTOCiselnikStlpec dto : csList) {
					if (StringUtils.isValid(columnsConditions)) {
						columnsConditions += " OR ";
					}
					columnsConditions += dto.getNazov() + " = " + rowID;
				}
				columnsConditions = " AND (" + columnsConditions + ")";
			}

			String sql = "SELECT COUNT(*) AS pocet FROM " + tabulka + " WHERE zmaz = \'F\' AND " + dateConditions + columnsConditions;

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
			handleException(t, "count.error", auth);
			return null;
		}
	}

	public Map<String, String> readLight(AuthInfo auth, String tabulka, List<DTOCiselnikStlpec> csList, String stlpecNazov, String value, String dbTyp, Date platnostOd, String zmaz) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			// vytvorenie podmienky
			String date = DateUtils.formatDateDDMMYYYY(platnostOd);
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " <= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			String conditional = null;
			if (_CudConsts.DB_TYP_STRING.equals(dbTyp) || _CudConsts.DB_TYP_BOOLEAN.equals(dbTyp)) {
				conditional = stlpecNazov + " = \'" + value + "\'";

			} else if (_CudConsts.DB_TYP_INTEGER.equals(dbTyp) || _CudConsts.DB_TYP_DOUBLE.equals(dbTyp)) {
				conditional = stlpecNazov + " = " + value;

			} else if (_CudConsts.DB_TYP_DATE.equals(dbTyp)) {
				conditional = "to_char(" + stlpecNazov + ", 'DD.MM.YYYY')" + " = " + value;
			}
			conditional += " AND " + dateConditions;

			if (StringUtils.isValid(zmaz)) {
				conditional += " AND " + _CudConsts.NAZOV_ZMAZ + " = \'" + zmaz + "\'";
			}

			// vytvorenie nazvov stlpcov
			int columnIndex = 0;
			StringBuffer columns = new StringBuffer("");
			for (DTOCiselnikStlpec dto : csList) {
				if (columnIndex > 0) {
					columns.append(", ");
				}

				columns.append(dto.getNazov() + " AS c" + columnIndex++);
			}

			// vyskladany komplet cely select
			String sql = "SELECT " + columns.toString() + " FROM " + tabulka + " WHERE " + conditional;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<String, String> resultMap = new HashMap<String, String>();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				columnIndex = 0;
				for (DTOCiselnikStlpec dto : csList) {
					String colNazov = "c" + columnIndex++;
					Value val = rVal(r, colNazov);
					String valStr = getValueAsString(dto.getDbTyp(), val, false);
					if (StringUtils.isValid(valStr)) {
						resultMap.put(dto.getNazov(), valStr);
					}
					if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getTyp())) {
						resultMap.put(_CudConsts.NAZOV_PK_KEY, valStr);
					}
				}
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	public Integer futCount(AuthInfo auth, String tabulka, List<DTOCiselnikStlpec> csList, Map<Integer, List<Map<Integer, Set<String>>>> filterMap, List<String> isNullList, List<String> isNotNullList, Date platnostOd, Date platnostDo) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String dateCon = "(" + _CudConsts.NAZOV_PLATNOST_OD + " <= " + _CudConsts.NAZOV_PLATNOST_DO + " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			String dateOd = "to_timestamp(\'" + _CudConsts.DATE_FORMAT.format(platnostOd) + "\', \'DD.MM.YYYY\')";
			dateCon += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL OR " + _CudConsts.NAZOV_PLATNOST_DO + " >= " + dateOd + ")";

			if (StringUtils.isValid(platnostDo)) {
				String dateDo = "to_timestamp(\'" + _CudConsts.DATE_FORMAT.format(platnostDo) + "\', \'DD.MM.YYYY\')";
				dateCon += " AND " + _CudConsts.NAZOV_PLATNOST_OD + " <= " + dateDo;
			}

			String isNull = "";
			if (StringUtils.isValid(isNullList)) {
				for (String column : isNullList) {
					if (StringUtils.isValid(isNull)) {
						isNull += ", ";
					}
					isNull += column + " IS NULL ";
				}
				isNull = " AND " + isNull;
			}

			String isNotNull = "";
			if (StringUtils.isValid(isNotNullList)) {
				for (String column : isNotNullList) {
					if (StringUtils.isValid(isNotNull)) {
						isNotNull += ", ";
					}
					isNotNull += column + " IS NOT NULL ";
				}
				isNotNull = " AND " + isNotNull;
			}

			String where = dateCon + " AND " + lookupConditions(csList, filterMap) + isNull + isNotNull;

			String sql = "SELECT count(*) as pocet FROM " + tabulka + " WHERE " + where;

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
			handleException(t, "futCount.error", auth);
			return null;
		}
	}

	private String lookupConditions(List<DTOCiselnikStlpec> csList, Map<Integer, List<Map<Integer, Set<String>>>> filterMap) throws AppException {

		try {
			Map<Integer, Integer> pocetMap = new HashMap<Integer, Integer>();
			Map<Integer, String> conditionMap = new HashMap<Integer, String>();

			for (Integer key : filterMap.keySet()) {

				for (Map<Integer, Set<String>> valueFilterMap : filterMap.get(key)) {

					Set<String> set2 = new HashSet<String>();
					for (DTOCiselnikStlpec dtoCS : csList) {

						Set<String> filterSet = valueFilterMap.get(dtoCS.getCiselnikStlpecID());
						if (StringUtils.isValid(filterSet) && !filterSet.isEmpty()) {

							Set<String> set1 = new HashSet<String>();

							for (String filterValue : filterSet) {

								String s = null;
								if (_CudConsts.DB_TYP_STRING.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getDbTyp())) {
									s = dtoCS.getNazov() + " = \'" + filterValue + "\'";

								} else if (_CudConsts.DB_TYP_INTEGER.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getDbTyp())) {
									s = dtoCS.getNazov() + " = " + filterValue;

								} else if (_CudConsts.DB_TYP_DATE.equals(dtoCS.getDbTyp())) {
									s = "to_char(" + dtoCS.getNazov() + ", 'DD.MM.YYYY')" + " = " + filterValue;
								}

								set1.add(s);
							}

							if (!set1.isEmpty()) {
								String conditional = "";
								if (set1.size() == 1) {
									conditional = set1.iterator().next();
								} else {
									for (String s : set1) {
										conditional += StringUtils.isValid(conditional) ? " OR (" + s + " ) " : "(" + s + " )";
									}
								}
								set2.add(conditional);
							}
						}

						if (!set2.isEmpty()) {
							String conditional = "";
							if (set2.size() == 1) {
								conditional = set2.iterator().next();
							} else {
								for (String s : set2) {
									conditional += StringUtils.isValid(conditional) ? " OR (" + s + " ) " : "(" + s + " )";
								}
							}
							pocetMap.put(key, set2.size());
							conditionMap.put(key, conditional);
						}

					}
				}

			}

			String filterStr = "";
			for (Integer key : conditionMap.keySet()) {
				if (StringUtils.isValid(filterStr)) {
					filterStr += " AND ";
				}
				filterStr += (pocetMap.get(key).intValue() == 1) ? conditionMap.get(key) : "(" + conditionMap.get(key) + ")";
			}

			return filterStr;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupConditions.error");
			return null;
		}
	}

	public DTOFutDynCiselnik[] futListForDynCiselnik(AuthInfo auth, DTOFutDynCiselnik dtoF, Page page) throws AppException {

		try {
			Map<Integer, List<Map<Integer, Set<String>>>> filterMap = new HashMap<Integer, List<Map<Integer, Set<String>>>>();

			int index = 0;
			for (DTOCiselnikStlpec dtoCS : dtoF.getCiselnikStlpecList()) {

				if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getTyp())) {
					filterMap.put(0, new ArrayList<Map<Integer, Set<String>>>());
					filterMap.get(0).add(new HashMap<Integer, Set<String>>());
					filterMap.get(0).get(index).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
					filterMap.get(0).get(index).get(dtoCS.getCiselnikStlpecID()).add(dtoF.getRowID().toString());
					index++;
				}
			}

			List<DTOCiselnikStlpec> csList = new ArrayList<DTOCiselnikStlpec>(Arrays.asList(dtoF.getCiselnikStlpecList()));

			DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(csList, _CudConsts.NAZOV_ZMAZ);
			filterMap.put(1, new ArrayList<Map<Integer, Set<String>>>());
			filterMap.get(1).add(new HashMap<Integer, Set<String>>());
			filterMap.get(1).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
			filterMap.get(1).get(0).get(dtoCS.getCiselnikStlpecID()).add("F");

			DTOCiselnikStlpec dtoCSPK = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList);

			List<DTOFutDynCiselnik> listDTO = new ArrayList<DTOFutDynCiselnik>();
			for (Map<String, String> rowMap : futListLight(auth, dtoF.getCiselnikTabulka(), csList, filterMap, dtoF.getPlatnostOd(), dtoF.getPlatnostDo(), page)) {

				DTOFutDynCiselnik dtoNew = new DTOFutDynCiselnik();
				dtoNew.setHistID(Integer.parseInt(rowMap.get(_CudConsts.NAZOV_HIST_ID)));
				dtoNew.setRowID(Integer.parseInt(rowMap.get(dtoCSPK.getNazov())));
				dtoNew.setPlatnostOd(_CudConsts.DATE_FORMAT.parse(rowMap.get(_CudConsts.NAZOV_PLATNOST_OD)));
				dtoNew.setPlatnostDo(StringUtils.isValid(rowMap.get(_CudConsts.NAZOV_PLATNOST_DO)) ? _CudConsts.DATE_FORMAT.parse(rowMap.get(_CudConsts.NAZOV_PLATNOST_DO)) : null);

				dtoNew.setListSize(Integer.parseInt(rowMap.get(_CudConsts.NAZOV_LIST_SIZE)));

				listDTO.add(dtoNew);
			}

			return listDTO.toArray(new DTOFutDynCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "futListLight.error", auth);
			return null;
		}
	}

	public List<Map<String, String>> futListLight(AuthInfo auth, String tabulka, List<DTOCiselnikStlpec> csList, Map<Integer, List<Map<Integer, Set<String>>>> filterMap, Date platnostOd, Date platnostDo, Page page) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String dateCon = "(" + _CudConsts.NAZOV_PLATNOST_OD + " <= " + _CudConsts.NAZOV_PLATNOST_DO + " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			String dateOd = "to_timestamp(\'" + _CudConsts.DATE_FORMAT.format(platnostOd) + "\', \'DD.MM.YYYY\')";
			dateCon += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL OR " + _CudConsts.NAZOV_PLATNOST_DO + " >= " + dateOd + ")";

			if (StringUtils.isValid(platnostDo)) {
				String dateDo = "to_timestamp(\'" + _CudConsts.DATE_FORMAT.format(platnostDo) + "\', \'DD.MM.YYYY\')";
				dateCon += " AND " + _CudConsts.NAZOV_PLATNOST_OD + " <= " + dateDo;
			}

			String where = dateCon + " AND " + lookupConditions(csList, filterMap);

			int columnIndex = 0;
			StringBuffer columns = new StringBuffer("");
			for (DTOCiselnikStlpec dto : csList) {
				if (columnIndex > 0) {
					columns.append(", ");
				}

				columns.append(dto.getNazov() + " AS c" + columnIndex++);
			}

			String sql = "SELECT " + columns.toString() + " FROM " + tabulka + " WHERE " + where;

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, "c0", auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				columnIndex = 0;
				Map<String, String> rowMap = new HashMap<String, String>();
				for (DTOCiselnikStlpec dto : csList) {
					String colNazov = "c" + columnIndex++;
					Value val = rVal(r, colNazov);
					String valStr = getValueAsString(dto.getDbTyp(), val, false);
					if (StringUtils.isValid(valStr)) {
						rowMap.put(dto.getNazov(), valStr);
					}
					if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getTyp())) {
						rowMap.put(_CudConsts.NAZOV_PK_KEY, valStr);
					}
					rowMap.put(_CudConsts.NAZOV_LIST_SIZE, Integer.toString(lp.total_count));

				}
				resultList.add(rowMap);
			}

			return resultList;

		} catch (Throwable t) {
			handleException(t, "futListLight.error", auth);
			return null;
		}
	}

	public Map<String, String> readGreaterThanLight(AuthInfo auth, String tabulka, List<DTOCiselnikStlpec> csList, String pkStlpecNazov, Integer pkValue, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			// vytvorenie podmienky
			String date = DateUtils.formatDateDDMMYYYY(platnostOd);
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " <= to_timestamp(\'" + date + "\', \'DD.MM.YYYY\')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= to_timestamp(\'" + date + "\', \'DD.MM.YYYY\')";
			dateConditions += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			String conditional = dateConditions + " AND " + pkStlpecNazov + " > " + pkValue.intValue() + " AND " + _CudConsts.NAZOV_ZMAZ + " = \'F\'";

			// vytvorenie nazvov stlpcov
			int columnIndex = 0;
			StringBuffer columns = new StringBuffer("");
			for (DTOCiselnikStlpec dto : csList) {
				if (columnIndex > 0) {
					columns.append(", ");
				}

				columns.append(dto.getNazov() + " AS c" + columnIndex++);
			}

			// vyskladany komplet cely select
			String sql = "SELECT " + columns.toString() + " FROM " + tabulka + " WHERE " + conditional + " ORDER BY " + pkStlpecNazov + " ASC";
			sql = "SELECT * FROM (" + sql + ") WHERE rownum = 1";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<String, String> resultMap = null;

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				resultMap = new HashMap<String, String>();
				columnIndex = 0;
				for (DTOCiselnikStlpec dto : csList) {
					String colNazov = "c" + columnIndex++;
					Value val = rVal(r, colNazov);
					String valStr = getValueAsString(dto.getDbTyp(), val, false);
					if (StringUtils.isValid(valStr)) {
						resultMap.put(dto.getNazov(), valStr);
					}
					if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getTyp())) {
						resultMap.put(_CudConsts.NAZOV_PK_KEY, valStr);
					}
				}
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "readGreaterThanLight.error", auth);
			return null;
		}
	}

	public boolean jeZaznamZmazany(AuthInfo auth, String tabulkaNazov, String pkNazov, Integer rowID, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String date = DateUtils.formatDateDDMMYYYY(platnostOd);
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " <= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			String sql = "SELECT " + _CudConsts.NAZOV_ZMAZ + " FROM " + tabulkaNazov + " WHERE " + pkNazov + " = " + rowID + " AND " + dateConditions;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				String zmaz = rVal(r, _CudConsts.NAZOV_ZMAZ).asString();
				return "T".equals(zmaz);
			}

			return false;

		} catch (Throwable t) {
			handleException(t, "jeZaznamZmazany.error", auth);
			return false;
		}
	}

	public boolean jeZaznamNezmazany(AuthInfo auth, String tabulka, String pkNazov, Integer rowID, Date platnostOd) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String date = DateUtils.formatDateDDMMYYYY(platnostOd);
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " <= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			String sql = "SELECT " + _CudConsts.NAZOV_ZMAZ + " FROM " + tabulka + " WHERE " + pkNazov + " = " + rowID + " AND " + dateConditions;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				String zmaz = rVal(r, "ZMAZ").asString();
				return "F".equals(zmaz);
			}

			return false;

		} catch (Throwable t) {
			handleException(t, "jeZaznamNezmazany.error", auth);
			return false;
		}
	}

	public Integer getPocetVaziebNaZaznam(AuthInfo auth, Map<String, String> mapa) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			List<String> list = new ArrayList<String>();
			for (String key : mapa.keySet()) {
				String s = "SELECT COUNT(*) FROM " + key + " WHERE " + mapa.get(key);
				list.add(s);
			}

			if (list.isEmpty()) {
				return 0;
			}

			String sql = "";
			for (String s : list) {
				if (StringUtils.isValid(sql)) {
					sql += "+ (" + s + ")";
				} else {
					sql += "(" + s + ")";
				}
			}

			sql = "SELECT " + sql + " AS POCET FROM DUAL";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, "POCET").asIntegerObj();
			}

			return 0;

		} catch (Throwable t) {
			handleException(t, "getPocetVaziebNaZaznam.error", auth);
			return null;
		}
	}

	public Integer pocetVazieb(AuthInfo auth, Map<String, String> mapa, Date platnostOd, String zmazany) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			List<String> sqlList = new ArrayList<String>();

			String dateCon = "(" + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL OR " + _CudConsts.NAZOV_PLATNOST_OD + " <= " + _CudConsts.NAZOV_PLATNOST_DO + ")";

			for (String key : mapa.keySet()) {
				String s = "SELECT " + _CudConsts.NAZOV_PLATNOST_DO + ", " + _CudConsts.NAZOV_ZMAZ + " FROM " + key + " WHERE " + mapa.get(key) + " AND " + dateCon;
				sqlList.add(s);
			}

			if (sqlList.isEmpty()) {
				return 0;
			}

			String sql = "";
			for (String s : sqlList) {
				if (StringUtils.isValid(sql)) {
					sql += " UNION " + s;
				} else {
					sql = s;
				}
			}

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			int pocet = 0;

			Iterator<?> iter = lp.iterator();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Date dateDo = rVal(r, _CudConsts.NAZOV_PLATNOST_DO).asUtilDate();
				String zmaz = rVal(r, _CudConsts.NAZOV_ZMAZ).asString();

				if (StringUtils.isValid(dateDo)) {
					if (dateDo.before(platnostOd)) {
						continue;
					}
				}

				if (!zmazany.equals(zmaz)) {
					continue;
				}

				pocet++;
			}

			return pocet;

		} catch (Throwable t) {
			handleException(t, "pocetVazieb.error", auth);
			return null;
		}
	}

	/**
	 * Funkcia kontroluje definiciu formulara, tzn ci sa ID_CISELNIK_STLPEC nachadza prave 1x.
	 * 
	 * @param pole
	 *            atributy zmenu
	 * @return TRUE ak sa ID_CISELNIK_STLPEC nachadza prave 1x, inac vrati FALSE
	 * @throws AppException
	 */
	private boolean kontrolaDefinicie(DTOImportZmenaStlpec[] pole) throws AppException {

		try {
			Map<Integer, Integer> mapa = new HashMap<Integer, Integer>();

			if (!StringUtils.isValid(pole)) {
				return true;
			}

			for (DTOImportZmenaStlpec dtoZS : pole) {
				if (!StringUtils.isValid(mapa.get(dtoZS.getIDCiselnikStlpec()))) {
					mapa.put(dtoZS.getIDCiselnikStlpec(), 0);
				}
				mapa.put(dtoZS.getIDCiselnikStlpec(), mapa.get(dtoZS.getIDCiselnikStlpec()) + 1);
			}

			for (Integer pocet : mapa.values()) {
				if (pocet.intValue() > 1) {
					return false;
				}
			}

			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaDefinicie.error");
			return false;
		}
	}

	private Map<String, String> parseForm(DTOImportZmena dtoZmena) throws AppException {

		try {
			Map<String, String> rowMap = new HashMap<String, String>();

			rowMap.put(_CudConsts.NAZOV_XLS_OPERACIA, dtoZmena.getOperacia());
			rowMap.put(_CudConsts.NAZOV_XLS_PLATNOST_OD, _CudConsts.DATE_FORMAT.format(dtoZmena.getPlatnostOd()));
			rowMap.put(_CudConsts.NAZOV_XLS_POZNAMKA, dtoZmena.getPoznamka());
			rowMap.put(_CudConsts.IMPORT_KONTROLA_DEF, kontrolaDefinicie(dtoZmena.getImportZmenaStlpecList()) ? "T" : "F");

			if (StringUtils.isValid(dtoZmena.getRowID())) {
				rowMap.put(_CudConsts.NAZOV_ROW_ID, dtoZmena.getRowID().toString());
			}

			for (DTOImportZmenaStlpec dtoZS : dtoZmena.getImportZmenaStlpecList()) {
				rowMap.put(dtoZS.getCiselnikStlpecNazov(), dtoZS.getNewValue());
			}

			return rowMap;

		} catch (Throwable t) {
			DBUtils.handleException(t, "parseForm.error");
			return null;
		}

	}

	private boolean equals(DTOImportMsg[] msgList1, DTOImportMsg[] msgList2) throws AppException {

		try {
			if (StringUtils.isValid(msgList1) && StringUtils.isValid(msgList2)) {

				for (DTOImportMsg dtoMsg1 : msgList1) {

					boolean b = true;
					for (DTOImportMsg dtoMsg2 : msgList2) {
						if (_CudKontrolaUtils.equals(dtoMsg1.getTyp(), dtoMsg2.getTyp()) && _CudKontrolaUtils.equals(dtoMsg1.getMsg(), dtoMsg2.getMsg()) && _CudKontrolaUtils.equals(dtoMsg1.getIDCiselnikStlpecGui(), dtoMsg2.getIDCiselnikStlpecGui())) {
							b = false;
							break;
						}
					}
					if (b) {
						return false;
					}
				}

				return true;

			} else if (StringUtils.isValid(msgList1) && !StringUtils.isValid(msgList2)) {
				return false;

			} else if (!StringUtils.isValid(msgList1) && StringUtils.isValid(msgList2)) {
				return false;

			}

			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "equals.error");
			return false;
		}
	}

	public DTOImport updateKontrola(AuthInfo auth, DTOCiselnikStlpecGui[] metaPole, DTOImport dtoImport) throws AppException {

		try {
			DTOImportZmena dtoZmena = dtoImport.getImportZmenaList()[0];
			DTOImportMsg[] oldMsgList = dtoZmena.getImportMsgList();

			String o = dtoImport.getImportZmenaList()[0].getOperacia();
			if (_CudConsts.ZMENA_OPERACIA_D.equals(o) || _CudConsts.ZMENA_OPERACIA_Z.equals(o)) {
				for (DTOCiselnikStlpecGui dtoCS : metaPole) {
					if (!_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dtoCS.getCiselnikStlpecTyp())) {
						dtoCS.setPovinny("F");
					}
				}
			}

			Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();
			String key = _CudConsts.DATE_FORMAT.format(dtoZmena.getPlatnostOd()) + "_" + dtoZmena.getOperacia();
			metaMap.put(key, new ArrayList<DTOCiselnikStlpecGui>(Arrays.asList(metaPole)));

			Map<String, String> rowMap = parseForm(dtoZmena);

			DTOPlugin[] pluginList = getDelegate().getGuiRead().pluginList(auth, dtoImport.getIDCiselnik(), _CudConsts.PLUGIN_TYP_VALIDACNY, dtoZmena.getPlatnostOd());

			DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_FORM, dtoZmena.getPlatnostOd(), pluginList, new HashMap<String, Map<String, String>>());

			List<DTOCiselnikStlpec> csList = getDelegate().getCiselnikStlpecRead().listLight(auth, dtoImport.getIDCiselnik());

			getDelegate().getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);

			if (equals(oldMsgList, dtoVal.getImportZmenaDTO().getImportMsgList())) {
				dtoVal.getImportZmenaDTO().setWarnings(null);
				dtoVal.getImportZmenaDTO().setErrors(null);
				dtoVal.getImportZmenaDTO().setImportMsgList(null);
			}

			dtoImport.setStav(("T".equals(dtoVal.getImportZmenaDTO().getErrors()) || "T".equals(dtoVal.getImportZmenaDTO().getWarnings())) ? _CudConsts.IMPORT_STAV_ERROR : _CudConsts.IMPORT_STAV_IMPORT);
			dtoImport.setImportZmenaList(new DTOImportZmena[] { dtoVal.getImportZmenaDTO() });

			return dtoImport;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	public String[] getZoznamZmazanychAtributov(AuthInfo auth, Map<String, String> mapa) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String sql = "";
			for (String key : mapa.keySet()) {
				if (StringUtils.isValid(sql)) {
					sql += ", ";
				}
				sql += mapa.get(key);
			}

			sql = "SELECT " + sql + " FROM DUAL";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			List<String> resultList = new ArrayList<String>();
			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				for (String nazov : mapa.keySet()) {
					Integer pocet = rVal(r, nazov).asIntegerObj();
					if (pocet.intValue() == 0) {
						resultList.add(nazov);
					}
				}
			}

			return (String[]) resultList.toArray(new String[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "getZoznamZmazanychAtributov.error", auth);
			return null;
		}
	}

	public Integer getNewID(AuthInfo auth, String tabulkaNazov, String pkName) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		Statement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT MAX(" + pkName + ") FROM " + tabulkaNazov;

			getConnection(auth);
			stmt = auth.T.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			Integer pkValue = rs.getInt(1);
			cleanUp(stmt, rs);
			returnConnection(auth);

			if (!StringUtils.isValid(pkValue)) {
				pkValue = 0;
			}

			return pkValue + 1;

		} catch (Throwable t) {
			cleanUp(stmt, rs);
			handleException(t, "getNewID.error", auth);
			return null;
		}
	}

	/**
	 * Specialna metoda, ktora vrati ROW_ID zaznamu (primarny kluc). Ak nenajde ani jeden alebo najde viac ako 1, cize 2 a viac zaznamov, metoda vrati NULL.
	 * 
	 * @param auth
	 * @param sql
	 * @return
	 * @throws AppException
	 */
	public String readPkValue(AuthInfo auth, String sql) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			String value = null;

			int pocet = 0;
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				value = r.getValue(1).asString();
				pocet++;
			}

			return pocet == 1 ? value : null;

		} catch (Throwable t) {
			handleException(t, "readPkValue.error", auth);
			return null;
		}
	}

	public Integer[] readValuesAsInteger(AuthInfo auth, String sql) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(r.getValue(1).asIntegerObj());
			}

			return set.toArray(new Integer[set.size()]);

		} catch (Throwable t) {
			handleException(t, "readValuesAsInteger.error", auth);
			return null;
		}
	}

	public String lookupValueFormat(AuthInfo auth, Map<Integer, List<DTOCiselnikStlpecGui>> lookupMetaMap, Integer fk1IDCiselnik, String rowID, Date platnostOd) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> lookupMetaList = lookupMetaMap.get(fk1IDCiselnik);

			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setTabulka(lookupMetaList.get(0).getCiselnikTabulka());
			dtoF.setRowID(Integer.parseInt(rowID));
			dtoF.setPkName(lookupMetaList.get(0).getCiselnikStlpecNazov());
			dtoF.setPlatnostOd(platnostOd);

			Map<String, String> valueMap = readLookupValuesMap(auth, dtoF, lookupMetaList);

			List<String> resultList = new ArrayList<String>();

			for (DTOCiselnikStlpecGui dtoCS : lookupMetaList) {

				if (!"T".equals(dtoCS.getLookupZobrazenie())) {
					continue;
				}

				String lookupValue = valueMap.get(dtoCS.getCiselnikStlpecNazov());
				if (!StringUtils.isValid(lookupValue)) {
					continue;
				}

				if (StringUtils.isValid(dtoCS.getCiselnikStlpecFk1IDCiselnik())) {

					List<DTOCiselnikStlpecGui> metaListItem = lookupMetaMap.get(dtoCS.getCiselnikStlpecFk1IDCiselnik());

					dtoF = new DTODynCiselnik();
					dtoF.setTabulka(metaListItem.get(0).getCiselnikTabulka());
					dtoF.setRowID(Integer.parseInt(lookupValue));
					dtoF.setPkName(metaListItem.get(0).getCiselnikStlpecNazov());
					dtoF.setPlatnostOd(platnostOd);

					Map<String, String> lookupValueMapItem = readLookupValuesMap(auth, dtoF, metaListItem);
					for (DTOCiselnikStlpecGui dtoCSItem2 : metaListItem) {
						if ("T".equals(dtoCSItem2.getLookupZobrazenie())) {
							String lookupValueItem2 = lookupValueMapItem.get(dtoCSItem2.getCiselnikStlpecNazov());
							if (StringUtils.isValid(lookupValueItem2)) {
								resultList.add(lookupValueItem2);
								break;
							}
						}
					}

				} else {

					if (StringUtils.isValid(lookupValue)) {
						resultList.add(lookupValue);
					}
				}
			}

			return _CudLookupUtils.formatLookupValues(resultList);

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupValueFormat.error");
			return null;
		}
	}

	public String doubleValueFormat(String value, Integer decimals) throws AppException {

		try {
			if (!StringUtils.isValid(value)) {
				return null;
			}

			String[] arr = value.split("\\.");
			if (arr.length > 1) {
				for (int i = 0; i < (decimals.intValue() - arr[1].length()); i++) {
					value += "0";
				}
			}

			return value;

		} catch (Throwable t) {
			DBUtils.handleException(t, "doubleValueFormat.error");
			return null;
		}
	}

	public Integer getHistID(AuthInfo auth, String tabulka, Integer zmenaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(zmenaID)) {
				return null;
			}

			String sql = "SELECT " + _CudConsts.NAZOV_HIST_ID + " FROM " + tabulka + " WHERE " + _CudConsts.NAZOV_ID_ZMENA + " = " + zmenaID;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return r.getValue(1).asIntegerObj();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "getHistID.error", auth);
			return null;
		}
	}

	private DTOSubor[] suborListLight(AuthInfo auth, DTOSubor dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOSubor();
			}

			String pkName = dtoF.getTabulka() + "." + dtoF.getTabulka().substring(2) + "_ID";

			MyCriteria2 crit = new MyCriteria2(pkName, dtoF);

			crit.addSelectColumn(pkName);
			crit.addSelectColumn(dtoF.getTabulka() + "." + _CudConsts.NAZOV_NAZOV_SUBORU);
			crit.addSelectColumn(dtoF.getTabulka() + "." + _CudConsts.NAZOV_SUBOR);

			crit.addConditional(pkName, dtoF.getSuborID());

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOSubor> listDTO = new ArrayList<DTOSubor>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOSubor dto = new DTOSubor();
				dto.setSuborID(rVal(r, pkName).asIntegerObj());
				dto.setNazovSuboru(rVal(r, dtoF.getTabulka() + "." + _CudConsts.NAZOV_NAZOV_SUBORU).asString());
				dto.setSubor(rVal(r, dtoF.getTabulka() + "." + _CudConsts.NAZOV_SUBOR).asBytes());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOSubor[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "suborListLight.error", auth);
			return null;
		}
	}

	public DTOSubor suborReadLight(AuthInfo auth, String tabulka, Integer suborID) throws AppException {

		try {
			DTOSubor dtoF = new DTOSubor();
			dtoF.setTabulka(tabulka);
			dtoF.setSuborID(suborID);

			DTOSubor[] listDTO = suborListLight(auth, dtoF);

			return StringUtils.isValid(listDTO) ? listDTO[0] : null;

		} catch (Throwable t) {
			handleException(t, "suborReadLight.error", auth);
			return null;
		}
	}

	public String suborReadLookupValue(AuthInfo auth, String tabulka, Integer suborID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String pkName = tabulka + "." + tabulka.substring(2) + "_ID";

			MyCriteria2 crit = new MyCriteria2(pkName, new DTOSubor());

			crit.addSelectColumn(tabulka + "." + _CudConsts.NAZOV_NAZOV_SUBORU);

			crit.addConditional(pkName, suborID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, tabulka + "." + _CudConsts.NAZOV_NAZOV_SUBORU).asString();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "suborReadLookupValue.error", auth);
			return null;
		}
	}

	public String suborReadSpracovany(AuthInfo auth, String tabulka, Integer suborID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String pkName = tabulka + "." + tabulka.substring(2) + "_ID";

			MyCriteria2 crit = new MyCriteria2(pkName, new DTOSubor());

			crit.addSelectColumn(tabulka + "." + _CudConsts.NAZOV_SPRACOVANY);

			crit.addConditional(pkName, suborID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, tabulka + "." + _CudConsts.NAZOV_SPRACOVANY).asString();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "suborReadSpracovany.error", auth);
			return null;
		}
	}

}
