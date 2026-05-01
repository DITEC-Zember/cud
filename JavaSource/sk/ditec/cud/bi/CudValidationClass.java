package sk.ditec.cud.bi;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.cud.dto.DTOPreklad;
import sk.ditec.cud.dto.DTOSkupina;
import sk.ditec.cud.dto.DTOValidate;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.plugin.IPlugin;
import sk.ditec.cud.utils.CudCacheMap;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikGuiPeer;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecGuiPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudPrekladPeer;
import sk.ditec.dao.meta.CudWfDefPeer;
import sk.ditec.zsr.common.server.utils.DateUtils;

public class CudValidationClass extends _CudBaseClass {

	private boolean hasInvalidChars(String value) throws AppException {

		try {
			if (!StringUtils.isValid(value)) {
				return false;
			}

			// if (value.indexOf("\'") > -1) {
			// return true;
			// } else if (value.indexOf(";") > -1) {
			// return true;
			// }
			// if (value.indexOf("„") > -1) {
			// return true;
			// }

			return false;

		} catch (Throwable t) {
			DBUtils.handleException(t, "hasInvalidChars.error");
			return true;
		}
	}

	private String validateIntegerValue(DTOCiselnikStlpecGui dto, String value, Set<String> errorsSet, String zdroj) throws AppException {

		try {
			value = value.replaceAll(" ", "");
			Integer val = null;
			try {
				val = new Integer(value);
			} catch (NumberFormatException e) {
				String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
				errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3017, colName));
				return null;
			}
			Integer dlzka = StringUtils.isValid(dto.getDlzka()) ? dto.getDlzka() : 0;
			int maxValue = (int) Math.pow(10, dlzka) - 1;
			if (val.intValue() > maxValue) {
				String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
				errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3018, colName, Integer.toString(-maxValue), Integer.toString(maxValue)));
				return null;
			}

			return val.toString();

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateIntegerValue.error");
			return null;
		}
	}

	private String validateDoubleValue(DTOCiselnikStlpecGui dto, String value, Set<String> errorsSet, String zdroj) throws AppException {

		try {
			value = value.replaceAll(" ", "");
			value = value.replaceAll(",", ".");

			Double val = null;
			try {
				val = new Double(value);
			} catch (NumberFormatException e) {
				String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
				errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3019, colName));
				return null;
			}
			Integer dlzka = StringUtils.isValid(dto.getDlzka()) ? dto.getDlzka() : 0;
			Integer presnost = StringUtils.isValid(dto.getDecimals()) ? dto.getDecimals() : 0;
			dlzka = dlzka - presnost;

			String[] arr = value.split("\\.");
			if (arr.length > 1 && arr[1].length() > presnost.intValue()) {
				String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
				errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3020, colName, Integer.toString(presnost)));
				return null;
			}

			BigDecimal bc = new BigDecimal(val);
			String celaCast = bc.toBigInteger().toString();
			Integer pocetCislic = celaCast.length() - ((celaCast.charAt(0) == '-') ? 1 : 0);
			if (pocetCislic.intValue() > dlzka.intValue()) {
				double maxValue = Math.pow(10, dlzka) - 1;
				double maxValueDecimal = Math.pow(10, -presnost);
				String min = Double.toString(-maxValue - 1 + maxValueDecimal);
				String max = Double.toString(maxValue + 1 - maxValueDecimal);
				String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
				errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3018, colName, min, max));
				return null;
			}

			return val.toString();

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateDoubleValue.error");
			return null;
		}
	}

	private String validateDateValue(DTOCiselnikStlpecGui dto, String value, Set<String> errorsSet, String zdroj) throws AppException {

		try {
			value = value.replaceAll(" ", "");
			Date val = null;
			try {
				val = _CudConsts.DATE_FORMAT.parse(value);
				String[] arr = value.split("\\.");
				Calendar cal = Calendar.getInstance();
				cal.setTime(val);

				if (Integer.parseInt(arr[0]) != cal.get(Calendar.DAY_OF_MONTH)) {
					throw new ParseException("", 0);
				}

				if (Integer.parseInt(arr[1]) != (cal.get(Calendar.MONTH) + 1)) {
					throw new ParseException("", 0);
				}

				if (Integer.parseInt(arr[2]) != cal.get(Calendar.YEAR)) {
					throw new ParseException("", 0);
				}

			} catch (Exception e) {
				String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
				errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3021, colName));
				return null;
			}

			return _CudConsts.DATE_FORMAT.format(val);

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateDateValue.error");
			return null;
		}
	}

	private String validateStringValue(DTOCiselnikStlpecGui dto, String value, Set<String> errorsSet, String zdroj) throws AppException {

		try {
			Integer dlzka = StringUtils.isValid(dto.getDlzka()) ? dto.getDlzka() : 0;
			if (value.length() > dlzka.intValue()) {
				String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
				errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3023, colName, dlzka.toString()));
				return null;
			}

			if (_CudConsts.ZDROJ_FORM.equals(zdroj) && hasInvalidChars(value)) {
				errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3024, dto.getNadpis()));
				return null;
			}

			return value;

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateStringValue.error");
			return null;
		}
	}

	private String validateBooleanValue(DTOCiselnikStlpecGui dto, String value, Set<String> errorsSet, String zdroj) throws AppException {

		try {
			if (!"T".equals(value) && !"F".equals(value) && !"Áno".equals(value) && !"Nie".equals(value)) {
				String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
				errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3022, colName));
				return null;
			}

			if ("Áno".equals(value)) {
				value = "T";
			}

			if ("Nie".equals(value)) {
				value = "F";
			}

			return value;

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateBooleanValue.error");
			return null;
		}
	}

	private String validateValue(DTOCiselnikStlpecGui dto, String valueStr, Set<String> errorsSet, String zdroj) throws AppException {

		try {
			if (!StringUtils.isValid(valueStr)) {

				if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getCiselnikStlpecTyp())) {
					return null;
				}

				if ("T".equals(dto.getPovinny())) {
					String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
					errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, colName));
				}

				return null;
			}

			if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dto.getCiselnikStlpecTyp())) {
				return valueStr;
			}

			String value = valueStr.trim();

			if (_CudConsts.DB_TYP_INTEGER.equals(dto.getCiselnikStlpecDbTyp())) {
				return validateIntegerValue(dto, value, errorsSet, zdroj);
			} else if (_CudConsts.DB_TYP_DOUBLE.equals(dto.getCiselnikStlpecDbTyp())) {
				return validateDoubleValue(dto, value, errorsSet, zdroj);
			} else if (_CudConsts.DB_TYP_DATE.equals(dto.getCiselnikStlpecDbTyp())) {
				return validateDateValue(dto, value, errorsSet, zdroj);
			} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dto.getCiselnikStlpecDbTyp())) {
				return validateBooleanValue(dto, value, errorsSet, zdroj);
			} else if (_CudConsts.DB_TYP_STRING.equals(dto.getCiselnikStlpecDbTyp())) {
				return validateStringValue(dto, value, errorsSet, zdroj);
			}

			String colName = _CudConsts.ZDROJ_FORM.equals(zdroj) ? dto.getNadpis() : dto.getCiselnikStlpecNazov();
			errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3025, colName));

			return valueStr;

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateValue.error");
			return null;
		}
	}

	private void jeAtributPovinny(DTOCiselnikStlpecGui dto, String valueStr, Set<String> errorsSet, String zdroj, String operacia, boolean changed) throws AppException {

		try {
			if ("T".equals(dto.getPovinny()) && !StringUtils.isValid(valueStr)) {

				if (_CudConsts.ZDROJ_FORM.equals(zdroj)) {
					errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, dto.getNadpis()));

				} else {
					if (_CudConsts.ZMENA_OPERACIA_N.equals(operacia)) {
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, dto.getCiselnikStlpecNazov()));
					} else if (changed) {
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, dto.getCiselnikStlpecNazov()));
					}
				}

			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeAtributPovinny.error");
		}
	}

	private void addMsg(Map<Integer, Set<String>> errMap, Integer ciselnikStlpecGuiID, Set<String> errorSet, String zdroj, boolean zobrazitNaGui) throws AppException {

		try {
			if (!zobrazitNaGui && _CudConsts.ZDROJ_FORM.equals(zdroj)) {
				return;
			}
			if (!StringUtils.isValid(errMap.get(ciselnikStlpecGuiID))) {
				errMap.put(ciselnikStlpecGuiID, new HashSet<String>());
			}
			errMap.get(ciselnikStlpecGuiID).addAll(errorSet);

		} catch (Throwable t) {
			DBUtils.handleException(t, "addMsg.error");
		}
	}

	private void addMsg(Map<Integer, Set<String>> errMap, Integer ciselnikStlpecGuiID, String errorMsg, String zdroj, boolean zobrazitNaGui) throws AppException {

		try {
			if (!zobrazitNaGui && _CudConsts.ZDROJ_FORM.equals(zdroj)) {
				return;
			}
			if (!StringUtils.isValid(ciselnikStlpecGuiID)) {
				ciselnikStlpecGuiID = 0;
			}
			if (!StringUtils.isValid(errMap.get(ciselnikStlpecGuiID))) {
				errMap.put(ciselnikStlpecGuiID, new HashSet<String>());
			}
			errMap.get(ciselnikStlpecGuiID).add(errorMsg);

		} catch (Throwable t) {
			DBUtils.handleException(t, "addMsg.error");
		}
	}

	private boolean jeZmenaAtributu(String oldValue, String newValue) throws AppException {

		try {
			if (!StringUtils.isValid(oldValue) && !StringUtils.isValid(newValue)) {
				return false;
			}
			if (StringUtils.isValid(oldValue) && oldValue.equals(newValue)) {
				return false;
			}
			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeZmenaAtributu.error");
			return false;
		}
	}

	private boolean suAtributyRovnake(String oldValue, String newValue) throws AppException {

		try {
			if (StringUtils.isValid(oldValue)) {
				return oldValue.equals(newValue);
			}
			if (StringUtils.isValid(newValue)) {
				return newValue.equals(oldValue);
			}
			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "suAtributyRovnake.error");
			return false;
		}
	}

	public Integer getPocetVaziebNaZaznamVCiselnikoch(AuthInfo auth, Integer ciselnikID, Integer rowID, Integer jumpCiselnikID, String jumpPkNazov, Integer jumpRowID, Map<Integer, List<DTOCiselnikStlpecGui>> metaMap, Date platnostOd) throws AppException {

		try {
			String date = DateUtils.formatDateDDMMYYYY(platnostOd);
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_OD + " <= " + _CudConsts.NAZOV_PLATNOST_DO + " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			Map<String, String> mapa = new HashMap<String, String>();

			Map<Integer, String> ciselnikMap = getDelegate().getCiselnikRead().map(auth, metaMap.keySet());

			for (Integer ciselnikIDkey : metaMap.keySet()) {

				if (!StringUtils.isValid(ciselnikMap.get(ciselnikIDkey))) {
					continue;
				}

				String conditional = "";

				int pocet = 0;
				for (DTOCiselnikStlpecGui dtoCS : metaMap.get(ciselnikIDkey)) {
					if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
						if ("T".equals(dtoCS.getFormZobrazenie())) {
							if (ciselnikID.intValue() == dtoCS.getCiselnikStlpecFk1IDCiselnik().intValue()) {
								String s = dtoCS.getCiselnikStlpecNazov() + " = " + rowID;
								conditional += StringUtils.isValid(conditional) ? " OR " + s : s;
								pocet++;
							}
						}
					}
				}

				if (pocet == 0) {
					continue;
				}

				if (pocet == 1) {
					conditional += " AND " + _CudConsts.NAZOV_ZMAZ + " = \'F\' AND " + dateConditions;
				} else {
					conditional = "(" + conditional + ") AND " + _CudConsts.NAZOV_ZMAZ + " = \'F\' AND " + dateConditions;
				}

				if (ciselnikIDkey.intValue() == jumpCiselnikID.intValue()) {
					conditional += " AND " + jumpPkNazov + " <> " + jumpRowID;

				}

				mapa.put(ciselnikMap.get(ciselnikIDkey), conditional);
			}

			return getDelegate().getDynCiselnikRead().getPocetVaziebNaZaznam(auth, mapa);

		} catch (Throwable t) {
			handleException(t, "getPocetVaziebNaZaznamVCiselnikoch.error", auth);
			return null;
		}
	}

	private Integer getPocetVaziebNaZaznamVCiselnikoch(AuthInfo auth, Integer ciselnikID, Integer rowID, String pkNazov, Map<Integer, List<DTOCiselnikStlpecGui>> metaMap, Date platnostOd) throws AppException {

		try {
			Map<String, String> mapa = new HashMap<String, String>();

			Map<Integer, String> ciselnikMap = getDelegate().getCiselnikRead().map(auth, metaMap.keySet());

			for (Integer ciselnikIDkey : metaMap.keySet()) {

				if (!StringUtils.isValid(ciselnikMap.get(ciselnikIDkey))) {
					continue;
				}

				String conditional = "";

				int pocet = 0;
				for (DTOCiselnikStlpecGui dtoCS : metaMap.get(ciselnikIDkey)) {
					if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
						if ("T".equals(dtoCS.getFormZobrazenie())) {
							if (ciselnikID.intValue() == dtoCS.getCiselnikStlpecFk1IDCiselnik().intValue()) {
								String s = dtoCS.getCiselnikStlpecNazov() + " = " + rowID;
								conditional += StringUtils.isValid(conditional) ? " OR " + s : s;
								pocet++;
							}
						}
					}
				}

				if (pocet == 0) {
					continue;
				}

				if (pocet > 1) {
					conditional = "(" + conditional + ") ";
				}

				if (ciselnikIDkey.intValue() == ciselnikID.intValue()) {
					conditional += " AND " + pkNazov + " <> " + rowID;

				}

				mapa.put(ciselnikMap.get(ciselnikIDkey), conditional);
			}

			return getDelegate().getDynCiselnikRead().pocetVazieb(auth, mapa, platnostOd, "F");

		} catch (Throwable t) {
			handleException(t, "getPocetVaziebNaZaznamVCiselnikoch.error", auth);
			return null;
		}
	}

	public Integer getPocetVaziebNaZaznamVRegistriZmien(AuthInfo auth, Map<Integer, List<DTOCiselnikStlpecGui>> metaMap, Integer ciselnikID, Integer rowID) throws AppException {

		try {
			Set<Integer> ciselnikStlpecIDs = new HashSet<Integer>();

			for (Integer ciselnikIDkey : metaMap.keySet()) {
				for (DTOCiselnikStlpecGui dto : metaMap.get(ciselnikIDkey)) {
					if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dto.getCiselnikStlpecTyp())) {
						if ("T".equals(dto.getFormZobrazenie())) {
							if (ciselnikID.intValue() == dto.getCiselnikStlpecFk1IDCiselnik().intValue()) {
								ciselnikStlpecIDs.add(dto.getIDCiselnikStlpec());
							}
						}
					}
				}
			}

			Set<String> operaciaSet = new HashSet<String>();
			operaciaSet.add(_CudConsts.ZMENA_OPERACIA_N);
			operaciaSet.add(_CudConsts.ZMENA_OPERACIA_U);
			operaciaSet.add(_CudConsts.ZMENA_OPERACIA_D);

			return getDelegate().getZmenaStlpecRead().getPocetVaziebNaZaznam(auth, metaMap.keySet(), ciselnikStlpecIDs, rowID.toString(), operaciaSet);

		} catch (Throwable t) {
			handleException(t, "getPocetVaziebNaZaznamVRegistryZmien.error", auth);
			return null;
		}
	}

	private Integer getPocetVaziebNaZaznamSpecial(AuthInfo auth, Integer rowID, Date platnostOd) throws AppException {

		try {
			Set<String> set = new HashSet<String>();
			set.add(_CudConsts.TABULKA_T_DOPRAVNY_BOD);
			set.add(_CudConsts.TABULKA_T_HRANICNY_PRIECHOD);

			String date = DateUtils.formatDateDDMMYYYY(platnostOd);
			String dateConditions = _CudConsts.NAZOV_PLATNOST_OD + " <= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= TO_DATE('" + date + " 00:00:00', 'DD.MM.YYYY HH24:MI:SS')";
			dateConditions += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			Map<String, String> mapa = new HashMap<String, String>();

			for (String tabulkaNazov : set) {
				String s = _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV + " = " + rowID + " AND " + _CudConsts.NAZOV_ZMAZ + " = \'F\' AND " + dateConditions;
				mapa.put(tabulkaNazov, s);
			}

			return getDelegate().getDynCiselnikRead().getPocetVaziebNaZaznam(auth, mapa);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getPocetVaziebNaZaznamSpecial.error");
			return null;
		}
	}

	private boolean jePlatnostOdValidna(Date platnostOd) throws AppException {

		try {
			Date d = sk.ditec.common.utils.DateUtils.removeTime(new Date());

			if ("T".equals(_CudConsts.LEN_DOPREDNE) && platnostOd.before(d)) {
				return false;
			}

			Calendar cal = Calendar.getInstance(new Locale("sk_SK"));
			cal.setTime(d);
			cal.add(Calendar.DAY_OF_YEAR, _CudConsts.MAX_DNI_DOPREDU);

			if (platnostOd.after(cal.getTime())) {
				return false;
			}

			if ("F".equals(_CudConsts.LEN_DOPREDNE)) {
				cal = Calendar.getInstance(new Locale("sk_SK"));
				cal.setTime(d);
				int day = cal.get(Calendar.DAY_OF_MONTH);

				cal.add(Calendar.MONTH, -1);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				if (day < _CudConsts.DEN_UZAVIERKY && platnostOd.before(cal.getTime())) {
					return false;
				}

				cal = Calendar.getInstance(new Locale("sk_SK"));
				cal.setTime(d);
				day = cal.get(Calendar.DAY_OF_MONTH);

				cal.set(Calendar.DAY_OF_MONTH, 1);
				if (day > _CudConsts.DEN_UZAVIERKY && platnostOd.before(cal.getTime())) {
					return false;
				}
			}

			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "jePlatnostOdValidna.error");
			return false;
		}
	}

	private boolean jeAtributEditovatelny(List<DTOCiselnikStlpecGui> metaList, String ciselnikStlpecNazov) throws AppException {

		try {
			for (DTOCiselnikStlpecGui dto : metaList) {
				if (ciselnikStlpecNazov.equals(dto.getCiselnikStlpecNazov()) && "T".equals(dto.getZmena())) {
					return true;
				}
			}
			return false;

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeAtributEditovatelny.error");
			return false;
		}
	}

	private void validateMasterByIntegrity(AuthInfo auth, Map<String, List<DTOCiselnikStlpecGui>> metaMap, DTOValidate dtoVal, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> metaList = metaMap.get(dtoVal.getPlatnostOd());

			DTOCiselnikStlpecGui dtoCSPK = _CudLookupUtils.lookupDTOCiselnikStlpecGuiPk(metaList);
			DTOCiselnikStlpecGui dtoCSJed = _CudLookupUtils.lookupDTOCiselnikStlpecGuiJedinecny(metaList);

			if (!jePlatnostOdValidna(dtoVal.getImportZmenaDTO().getPlatnostOd())) {
				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(csList, _CudConsts.NAZOV_PLATNOST_OD);
				String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				addMsg(dtoVal.getErrorsMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3001, colName), dtoVal.getZdroj(), true);
			}

			if (_CudConsts.ZMENA_OPERACIA_N.equals(dtoVal.getImportZmenaDTO().getOperacia())) {

				// kontrola na ROW_ID
				if (StringUtils.isValid(dtoVal.getImportZmenaDTO().getRowID()) || StringUtils.isValid(dtoVal.getNewValueMap().get(dtoCSPK.getCiselnikStlpecNazov()))) {
					addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3002), dtoVal.getZdroj(), true);
					return;
				}

				if (StringUtils.isValid(dtoCSJed)) {

					String newValue = dtoVal.getNewValueMap().get(dtoCSJed.getCiselnikStlpecNazov());
					if (StringUtils.isValid(newValue)) {

						Map<String, String> rowMap = getDelegate().getDynCiselnikRead().readLight(auth, dtoVal.getCiselnikTabulka(), csList, dtoCSJed.getCiselnikStlpecNazov(), newValue, dtoCSJed.getCiselnikStlpecDbTyp(), dtoVal.getImportZmenaDTO().getPlatnostOd(), null);

						// kontrola ci ide o obnovu zaznamu
						if (!rowMap.keySet().isEmpty()) {

							String pkValue = rowMap.get(dtoCSPK.getCiselnikStlpecNazov());

							// Nasla sa zhoda jedinecneho atributu s uz existujucim zaznamom
							addMsg(dtoVal.getWarningMap(), dtoCSJed.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_301, pkValue), dtoVal.getZdroj(), false);

							if ("T".equals(rowMap.get(_CudConsts.NAZOV_ZMAZ))) {

								dtoVal.getImportZmenaDTO().setRowID(new Integer(pkValue));
								dtoVal.getImportZmenaDTO().setOperacia(_CudConsts.ZMENA_OPERACIA_U);
								dtoVal.getImportZmenaDTO().setObnova("T");

								validateMasterByIntegrity(auth, metaMap, dtoVal, csList);
								return;

							} else if ("F".equals(rowMap.get(_CudConsts.NAZOV_ZMAZ))) {
								addMsg(dtoVal.getErrorsMap(), dtoCSJed.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3003, pkValue), dtoVal.getZdroj(), true);
							}
						}

						// kontrola na nespracovane WF_TODO
						Integer pocet = getDelegate().getZmenaStlpecRead().getPocetNepublikovanychZaznamov(auth, dtoVal.getCiselnikID(), dtoCSJed.getIDCiselnikStlpec(), newValue);
						if (pocet.intValue() != 0) {
							addMsg(dtoVal.getErrorsMap(), dtoCSJed.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3004), dtoVal.getZdroj(), true);
						}
					}
				}

				for (DTOCiselnikStlpecGui dtoCS : metaList) {

					if (_CudKontrolaUtils.jeAtributTechnicky(dtoCS)) {
						continue;
					}
					if (!"T".equals(dtoCS.getFormZobrazenie())) {
						continue;
					}

					String newValue = dtoVal.getNewValueMap().get(dtoCS.getCiselnikStlpecNazov());
					if (!StringUtils.isValid(newValue)) {
						continue;
					}

					if ("T".equals(dtoCS.getZmena())) {

						if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {

							// kontrola ci atribut nereferencuje zmazanu hodnotu
							Integer rowID = new Integer(newValue);
							if (!getDelegate().getDynCiselnikRead().jeZaznamNezmazany(auth, dtoCS.getCiselnikStlpecFk1CiselnikTabulka(), dtoCS.getCiselnikStlpecFk1PkNazov(), rowID, dtoVal.getImportZmenaDTO().getPlatnostOd())) {
								String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
								addMsg(dtoVal.getErrorsMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3005, colName), dtoVal.getZdroj(), true);
							}

							// kontrola ci existuje poziadavka v registri zmien na zmazanie novo referencovanej hodnoty
							Integer pocet = getDelegate().getZmenaRead().getPocetNepublikovanychZaznamov(auth, dtoCS.getCiselnikStlpecFk1IDCiselnik(), rowID, _CudConsts.ZMENA_OPERACIA_Z);
							if (pocet.intValue() > 0) {
								String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
								addMsg(dtoVal.getErrorsMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3006, colName), dtoVal.getZdroj(), true);
							}
						}

						DTOImportZmenaStlpec dtoNew = new DTOImportZmenaStlpec();
						dtoNew.setIDCiselnikStlpec(dtoCS.getIDCiselnikStlpec());
						dtoNew.setCiselnikStlpecNazov(dtoCS.getCiselnikStlpecNazov());
						dtoNew.setNewValue(newValue);
						dtoVal.getImportZmenaStlpecList().add(dtoNew);

					} else if ("F".equals(dtoCS.getZmena()) && !jeAtributEditovatelny(metaList, dtoCS.getCiselnikStlpecNazov())) {
						String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
						addMsg(dtoVal.getWarningMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_302, colName), dtoVal.getZdroj(), false);
					}
				}

			} else if (_CudConsts.ZMENA_OPERACIA_U.equals(dtoVal.getImportZmenaDTO().getOperacia())) {

				// kontrola na ROW_ID
				if (!StringUtils.isValid(dtoVal.getImportZmenaDTO().getRowID())) {
					addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3007), dtoVal.getZdroj(), true);
					return;
				}

				// kontrola ci existuje nejake nespracovane CUD_WF_TODO
				Integer pocetTodo = getDelegate().getWfTodoRead().count(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getRowID(), null);
				if (pocetTodo.intValue() > 0) {
					addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3008), dtoVal.getZdroj(), true);
				}

				// kontrola atributu CUD_ZMENA.PLATNOST_OD
				Date maxPlatnostOd = getDelegate().getZmenaRead().readMaxPlatnostOd(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getRowID());
				if (StringUtils.isValid(maxPlatnostOd) && StringUtils.isValid(dtoVal.getImportZmenaDTO().getPlatnostOd())) {
					if (dtoVal.getImportZmenaDTO().getPlatnostOd().before(maxPlatnostOd)) {
						addMsg(dtoVal.getErrorsMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3009), dtoVal.getZdroj(), true);
					}
				}

				// nacita sa povodny zaznam, podla ROW_ID
				dtoVal.setOldValueMap(getDelegate().getDynCiselnikRead().readLight(auth, dtoVal.getCiselnikTabulka(), csList, dtoCSPK.getCiselnikStlpecNazov(), dtoVal.getImportZmenaDTO().getRowID().toString(), dtoCSPK.getCiselnikStlpecDbTyp(), dtoVal.getImportZmenaDTO().getPlatnostOd(), null));

				// kontrola ci zaznam existuje alebo ci nie je zneplatneny alebo zruseny
				if (!StringUtils.isValid(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_PK_KEY)) || "T".equals(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_ZMAZ)) || StringUtils.isValid(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_PLATNOST_DO))) {
					if (!"T".equals(dtoVal.getImportZmenaDTO().getObnova())) {
						addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3010), dtoVal.getZdroj(), true);
					}
				}

				// kontrola ci ciselnik ma jedinecny atribut
				if (StringUtils.isValid(dtoCSJed)) {

					// kontrola ci nastala zmena v JEDINECNOM atribute
					String oldValue = dtoVal.getOldValueMap().get(dtoCSJed.getCiselnikStlpecNazov());
					String newValue = dtoVal.getNewValueMap().get(dtoCSJed.getCiselnikStlpecNazov());

					if (StringUtils.isValid(newValue)) {

						if (!newValue.equals(oldValue)) {
							Map<String, String> rowMap = getDelegate().getDynCiselnikRead().readLight(auth, dtoVal.getCiselnikTabulka(), csList, dtoCSJed.getCiselnikStlpecNazov(), newValue, dtoCSJed.getCiselnikStlpecDbTyp(), dtoVal.getImportZmenaDTO().getPlatnostOd(), "F");
							String rowIDs = rowMap.get(dtoCSPK.getCiselnikStlpecNazov());

							// kontrola ci sa meni hodnota jedinecneho atributu na nezmazanu hodnotu
							if (StringUtils.isValid(rowIDs) && !rowIDs.equals(dtoVal.getImportZmenaDTO().getRowID().toString())) {
								String pkValue = rowMap.get(dtoCSPK.getCiselnikStlpecNazov());
								addMsg(dtoVal.getErrorsMap(), dtoCSJed.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3003, pkValue), dtoVal.getZdroj(), true);
							}
						}

						Integer pocet = getDelegate().getZmenaStlpecRead().getPocetNepublikovanychZaznamov(auth, dtoVal.getCiselnikID(), dtoCSJed.getIDCiselnikStlpec(), newValue);
						if (pocet.intValue() != 0) {
							addMsg(dtoVal.getErrorsMap(), dtoCSJed.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3004), dtoVal.getZdroj(), true);
						}
					}
				}

				for (DTOCiselnikStlpecGui dtoCS : metaList) {

					if (_CudKontrolaUtils.jeAtributTechnicky(dtoCS)) {
						continue;
					}
					if (!"T".equals(dtoCS.getFormZobrazenie())) {
						continue;
					}

					if (!dtoVal.getNewValueMap().keySet().contains(dtoCS.getCiselnikStlpecNazov())) {
						continue;
					}

					String newValue = dtoVal.getNewValueMap().get(dtoCS.getCiselnikStlpecNazov());
					String oldValue = dtoVal.getOldValueMap().get(dtoCS.getCiselnikStlpecNazov());

					if ("T".equals(dtoCS.getZmena())) {

						if (jeZmenaAtributu(oldValue, newValue) || ("T".equals(dtoCS.getCiselnikStlpecJedinecny()) && "T".equals(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_ZMAZ)))) {

							if (StringUtils.isValid(oldValue) && oldValue.equals(newValue)) {
								// ak sa oldValue == newValue a sucasne atribut nie je jedninecny => ignoruj zmenu
								if ("F".equals(dtoCS.getCiselnikStlpecJedinecny())) {
									continue;
								}
								// ak sa oldValue == newValue a sucasne atribut je jedninecny a sucasne povodny zaznam nie je zmazany => ignoruj zmenu
								if ("T".equals(dtoCS.getCiselnikStlpecJedinecny()) && "F".equals(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_ZMAZ))) {
									continue;
								}
							}

							if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {

								if (StringUtils.isValid(newValue)) {

									// kontrola ci je referencovana hodnota platna
									Integer rowID = new Integer(newValue);
									if (!getDelegate().getDynCiselnikRead().jeZaznamNezmazany(auth, dtoCS.getCiselnikStlpecFk1CiselnikTabulka(), dtoCS.getCiselnikStlpecFk1PkNazov(), rowID, dtoVal.getImportZmenaDTO().getPlatnostOd())) {
										String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
										addMsg(dtoVal.getErrorsMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3005, colName), dtoVal.getZdroj(), true);
									}

									// kontrola ci existuje poziadavka v registri zmien na zmazanie novo referencovanej hodnoty
									Integer pocet = getDelegate().getZmenaRead().getPocetNepublikovanychZaznamov(auth, dtoCS.getCiselnikStlpecFk1IDCiselnik(), rowID, _CudConsts.ZMENA_OPERACIA_Z);
									if (pocet.intValue() > 0) {
										String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
										addMsg(dtoVal.getErrorsMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3006, colName), dtoVal.getZdroj(), true);
									}
								}
							}

							// ak ide o atribut T_DOPRAVNY_BOD.ID_DOPRAVNY_BOD, nebola ziadna zmena a ide o obnovenie zaznamu, tak preskoc tuto polozku zmeny
							String ciselnikTabulka = dtoVal.getCiselnikTabulka();
							if (_CudConsts.TABULKA_T_DOPRAVNY_BOD.equals(ciselnikTabulka)) {
								if (_CudConsts.NAZOV_ID_DOPRAVNY_BOD.equals(dtoCS.getCiselnikStlpecNazov())) {
									if ("T".equals(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_ZMAZ))) {
										if (StringUtils.isValid(oldValue) && !StringUtils.isValid(newValue)) {
											continue;
										}
									}
								}
							}

							DTOImportZmenaStlpec dtoNew = new DTOImportZmenaStlpec();
							dtoNew.setIDCiselnikStlpec(dtoCS.getIDCiselnikStlpec());
							dtoNew.setCiselnikStlpecNazov(dtoCS.getCiselnikStlpecNazov());
							dtoNew.setOldValue(oldValue);
							dtoNew.setNewValue(newValue);
							dtoVal.getImportZmenaStlpecList().add(dtoNew);

						} else {

							// kontrola na referencovanu hodnotu, ktora sa ale nemeni, prebera sa z povodneho zmazaneho zaznamu
							if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp()) && "T".equals(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_ZMAZ))) {
								if (StringUtils.isValid(newValue)) {
									Integer rowID = new Integer(newValue);
									if (!getDelegate().getDynCiselnikRead().jeZaznamNezmazany(auth, dtoCS.getCiselnikStlpecFk1CiselnikTabulka(), dtoCS.getCiselnikStlpecFk1PkNazov(), rowID, dtoVal.getImportZmenaDTO().getPlatnostOd())) {
										String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
										addMsg(dtoVal.getErrorsMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3005, colName), dtoVal.getZdroj(), true);
									}
								}
							}

							if (suAtributyRovnake(oldValue, newValue)) {
								String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
								addMsg(dtoVal.getWarningMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_303, colName), dtoVal.getZdroj(), false);
							}
						}

					} else if ("F".equals(dtoCS.getZmena()) && !jeAtributEditovatelny(metaList, dtoCS.getCiselnikStlpecNazov())) {
						if (StringUtils.isValid(newValue)) {
							String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
							addMsg(dtoVal.getWarningMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_302, colName), dtoVal.getZdroj(), false);
						}
					}
				}

				// kontrola na ci ide o zmenu ciselnika T_DOPRAVNY_NAZOV
				if (_CudConsts.TABULKA_T_DOPRAVNY_NAZOV.equals(dtoVal.getCiselnikTabulka())) {
					Integer pocetVazieb = getPocetVaziebNaZaznamSpecial(auth, dtoVal.getImportZmenaDTO().getRowID(), dtoVal.getImportZmenaDTO().getPlatnostOd());
					if (pocetVazieb.intValue() > 0) {
						addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3011), dtoVal.getZdroj(), true);
					}
				}

			} else if (_CudConsts.ZMENA_OPERACIA_D.equals(dtoVal.getImportZmenaDTO().getOperacia())) {

				// kontrola na ROW_ID
				if (!StringUtils.isValid(dtoVal.getImportZmenaDTO().getRowID())) {
					addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3007), dtoVal.getZdroj(), true);
					return;
				}

				// kontrola ci existuje nejake nespracovane CUD_WF_TODO
				Integer pocetTodo = getDelegate().getWfTodoRead().count(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getRowID(), null);
				if (pocetTodo.intValue() > 0) {
					addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3008), dtoVal.getZdroj(), true);
				}

				// nacita sa povodny zaznam, podla ROW_ID
				dtoVal.setOldValueMap(getDelegate().getDynCiselnikRead().readLight(auth, dtoVal.getCiselnikTabulka(), csList, dtoCSPK.getCiselnikStlpecNazov(), dtoVal.getImportZmenaDTO().getRowID().toString(), dtoCSPK.getCiselnikStlpecDbTyp(), dtoVal.getImportZmenaDTO().getPlatnostOd(), null));

				// kontrola ci zaznam existuje alebo ci nie je zneplatneny alebo zruseny
				if (!StringUtils.isValid(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_PK_KEY)) || "T".equals(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_ZMAZ))) {
					addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3010), dtoVal.getZdroj(), true);
				}

				// kontrola ci ID_DOPRAVNY_NAZOV nie je lookupovany do inych tabuliek (tyka sa to len zaznamov, ktore sa vymazu z historie zaznamu)
				if (_CudKontrolaUtils.jeCiselnikSpecialny(dtoVal.getCiselnikTabulka())) {
					List<Map<String, String>> rowList = getDelegate().getDynCiselnikRead().listLight(auth, dtoVal.getCiselnikTabulka(), csList, dtoVal.getImportZmenaDTO().getRowID().toString(), dtoVal.getImportZmenaDTO().getPlatnostOd());
					DTOCiselnikStlpec dtoPK = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList);
					for (Map<String, String> rowMap : rowList) {

						if ("T".equals(rowMap.get(_CudConsts.NAZOV_ZMAZ))) {
							continue;
						}

						if (!dtoVal.getOldValueMap().get(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV).equals(rowMap.get(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV))) {

							Date platnostOd = _CudConsts.DATE_FORMAT.parse(rowMap.get(_CudConsts.NAZOV_PLATNOST_OD));
							Map<Integer, List<DTOCiselnikStlpecGui>> fkMetaMap = getDelegate().getCiselnikStlpecGuiRead().mapByFk(auth, _CudConsts.ID_T_DOPRAVNY_NAZOV, platnostOd);
							if (!fkMetaMap.keySet().isEmpty()) {

								Integer dopravnyNazovID = Integer.parseInt(rowMap.get(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV));

								// kontrola na pocet vazieb naviazanych v ciselnikoch
								Integer pocetVazieb = getPocetVaziebNaZaznamVCiselnikoch(auth, _CudConsts.ID_T_DOPRAVNY_NAZOV, dopravnyNazovID, dtoVal.getCiselnikID(), dtoPK.getNazov(), dtoVal.getImportZmenaDTO().getRowID(), fkMetaMap, platnostOd);
								if (pocetVazieb.intValue() > 0) {
									DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV);
									String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
									String datum = rowMap.get(_CudConsts.NAZOV_PLATNOST_OD);
									addMsg(dtoVal.getErrorsMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3062, colName, datum), dtoVal.getZdroj(), true);
								}

								pocetVazieb = getPocetVaziebNaZaznamVRegistriZmien(auth, fkMetaMap, _CudConsts.ID_T_DOPRAVNY_NAZOV, dopravnyNazovID);
								if (pocetVazieb.intValue() > 0) {
									DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV);
									String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
									String datum = rowMap.get(_CudConsts.NAZOV_PLATNOST_OD);
									addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3063, colName, datum), dtoVal.getZdroj(), true);
								}
							}
						}
					}
				}

				for (DTOCiselnikStlpecGui dtoCS : metaList) {

					if (_CudKontrolaUtils.jeAtributTechnicky(dtoCS)) {
						continue;
					}
					if (!"T".equals(dtoCS.getFormZobrazenie())) {
						continue;
					}

					String oldValue = dtoVal.getOldValueMap().get(dtoCS.getCiselnikStlpecNazov());
					if (!StringUtils.isValid(oldValue)) {
						continue;
					}

					boolean b = _CudKontrolaUtils.jeCiselnikSpecialny(dtoVal.getCiselnikTabulka());
					if ("T".equals(dtoCS.getZmena()) || (b && _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV.equals(dtoCS.getCiselnikStlpecNazov()))) {
						DTOImportZmenaStlpec dtoNew = new DTOImportZmenaStlpec();
						dtoNew.setIDCiselnikStlpec(dtoCS.getIDCiselnikStlpec());
						dtoNew.setCiselnikStlpecNazov(dtoCS.getCiselnikStlpecNazov());
						dtoNew.setOldValue(oldValue);
						dtoNew.setNewValue(oldValue);
						dtoVal.getImportZmenaStlpecList().add(dtoNew);
					}
				}

				Map<Integer, List<DTOCiselnikStlpecGui>> fkMetaMap = getDelegate().getCiselnikStlpecGuiRead().mapByFk(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getPlatnostOd());
				if (!fkMetaMap.keySet().isEmpty()) {

					// kontrola na pocet vazieb naviazanych v ciselnikoch
					Integer pocetVazieb = getPocetVaziebNaZaznamVCiselnikoch(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getRowID(), dtoCSPK.getCiselnikStlpecNazov(), fkMetaMap, dtoVal.getImportZmenaDTO().getPlatnostOd());
					if (pocetVazieb.intValue() > 0) {
						addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3014), dtoVal.getZdroj(), true);
					}

					pocetVazieb = getPocetVaziebNaZaznamVRegistriZmien(auth, fkMetaMap, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getRowID());
					if (pocetVazieb.intValue() > 0) {
						addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3015), dtoVal.getZdroj(), true);
					}
				}

				// kontrola ci ide o zmenu ciselnika T_DOPRAVNY_NAZOV
				if (_CudConsts.TABULKA_T_DOPRAVNY_NAZOV.equals(dtoVal.getCiselnikTabulka())) {
					Integer pocetVazieb = getPocetVaziebNaZaznamSpecial(auth, dtoVal.getImportZmenaDTO().getRowID(), dtoVal.getImportZmenaDTO().getPlatnostOd());
					if (pocetVazieb.intValue() > 0) {
						addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3011), dtoVal.getZdroj(), true);
					}
				}

			} else if (_CudConsts.ZMENA_OPERACIA_Z.equals(dtoVal.getImportZmenaDTO().getOperacia())) {

				// kontrola na ROW_ID
				if (!StringUtils.isValid(dtoVal.getImportZmenaDTO().getRowID())) {
					addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3013), dtoVal.getZdroj(), true);
					return;
				}

				// kontrola ci existuje nejake nespracovane CUD_WF_TODO
				Integer pocetTodo = getDelegate().getWfTodoRead().count(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getRowID(), null);
				if (pocetTodo.intValue() > 0) {
					addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3008), dtoVal.getZdroj(), true);
				}

				// kontrola atributu CUD_ZMENA.PLATNOST_OD
				Date maxPlatnostOd = getDelegate().getZmenaRead().readMaxPlatnostOd(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getRowID());
				if (dtoVal.getImportZmenaDTO().getPlatnostOd().before(maxPlatnostOd)) {
					DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(csList, _CudConsts.NAZOV_PLATNOST_OD);
					String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					addMsg(dtoVal.getErrorsMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3001, colName), dtoVal.getZdroj(), true);
				}

				// nacita sa povodny zaznam, podla ROW_ID
				dtoVal.setOldValueMap(getDelegate().getDynCiselnikRead().readLight(auth, dtoVal.getCiselnikTabulka(), csList, dtoCSPK.getCiselnikStlpecNazov(), dtoVal.getImportZmenaDTO().getRowID().toString(), dtoCSPK.getCiselnikStlpecDbTyp(), dtoVal.getImportZmenaDTO().getPlatnostOd(), null));

				// kontrola ci zaznam existuje alebo ci nie je zneplatneny alebo zruseny
				if (!StringUtils.isValid(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_PK_KEY)) || "T".equals(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_ZMAZ)) || StringUtils.isValid(dtoVal.getOldValueMap().get(_CudConsts.NAZOV_PLATNOST_DO))) {
					addMsg(dtoVal.getErrorsMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3010), dtoVal.getZdroj(), true);
				}

				for (DTOCiselnikStlpecGui dtoCS : metaList) {

					if (_CudKontrolaUtils.jeAtributTechnicky(dtoCS)) {
						continue;
					}
					if (!"T".equals(dtoCS.getFormZobrazenie())) {
						continue;
					}

					String oldValue = dtoVal.getOldValueMap().get(dtoCS.getCiselnikStlpecNazov());
					if (!StringUtils.isValid(oldValue)) {
						continue;
					}

					boolean b = _CudKontrolaUtils.jeCiselnikSpecialny(dtoVal.getCiselnikTabulka());
					if ("T".equals(dtoCS.getZmena()) || (b && _CudConsts.NAZOV_ID_DOPRAVNY_NAZOV.equals(dtoCS.getCiselnikStlpecNazov()))) {
						DTOImportZmenaStlpec dtoNew = new DTOImportZmenaStlpec();
						dtoNew.setIDCiselnikStlpec(dtoCS.getIDCiselnikStlpec());
						dtoNew.setCiselnikStlpecNazov(dtoCS.getCiselnikStlpecNazov());
						dtoNew.setOldValue(oldValue);
						dtoVal.getImportZmenaStlpecList().add(dtoNew);
					}
				}

				Map<Integer, List<DTOCiselnikStlpecGui>> fkMetaMap = getDelegate().getCiselnikStlpecGuiRead().mapByFk(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getPlatnostOd());
				if (!fkMetaMap.keySet().isEmpty()) {

					// kontrola na pocet vazieb naviazanych v ciselnikoch
					Integer pocetVazieb = getPocetVaziebNaZaznamVCiselnikoch(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getRowID(), dtoCSPK.getCiselnikStlpecNazov(), fkMetaMap, dtoVal.getImportZmenaDTO().getPlatnostOd());
					if (pocetVazieb.intValue() > 0) {
						addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3014), dtoVal.getZdroj(), true);
					}

					pocetVazieb = getPocetVaziebNaZaznamVRegistriZmien(auth, fkMetaMap, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getRowID());
					if (pocetVazieb.intValue() > 0) {
						addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3015), dtoVal.getZdroj(), true);
					}
				}
			}

			if (dtoVal.getImportZmenaStlpecList().isEmpty()) {
				if (_CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj())) {
					addMsg(dtoVal.getErrorsMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3012), dtoVal.getZdroj(), true);
				} else {
					addMsg(dtoVal.getWarningMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_304), dtoVal.getZdroj(), false);
				}
			}

		} catch (Throwable t) {
			handleException(t, "validateMasterDataByIntegrity.error", auth);
		}
	}

	private List<DTOImportMsg> createDTOImportMsgList(DTOCiselnikStlpecGui dtoCS, Set<String> set, String typ) throws AppException {

		try {
			List<DTOImportMsg> resultList = new ArrayList<DTOImportMsg>();
			for (String msg : set) {
				DTOImportMsg dtoNew = new DTOImportMsg();
				if (StringUtils.isValid(dtoCS)) {
					dtoNew.setIDCiselnikStlpecGui(dtoCS.getCiselnikStlpecGuiID());
					dtoNew.setCiselnikStlpecNazov(dtoCS.getCiselnikStlpecNazov());
				}
				dtoNew.setTyp(typ);
				dtoNew.setMsg(msg);
				resultList.add(dtoNew);

			}
			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOImportMsgList.error");
			return null;
		}
	}

	private void lookupValues(DTOValidate dtoVal, Map<String, List<DTOCiselnikStlpecGui>> metaMap) throws AppException {

		try {
			dtoVal.getImportZmenaDTO().setErrors("F");
			dtoVal.getImportZmenaDTO().setWarnings("F");

			if (!dtoVal.getImportZmenaStlpecList().isEmpty()) {
				dtoVal.getImportZmenaDTO().setImportZmenaStlpecList(dtoVal.getImportZmenaStlpecList().toArray(new DTOImportZmenaStlpec[dtoVal.getImportZmenaStlpecList().size()]));
			}

			List<DTOCiselnikStlpecGui> metaList = metaMap.get(dtoVal.getPlatnostOd());

			List<DTOImportMsg> importMsgList = new ArrayList<DTOImportMsg>();
			for (Integer ciselnikStlpecGuiID : dtoVal.getErrorsMap().keySet()) {
				DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGui(metaList, ciselnikStlpecGuiID);
				importMsgList.addAll(createDTOImportMsgList(dtoCS, dtoVal.getErrorsMap().get(ciselnikStlpecGuiID), _CudConsts.IMPORT_MSG_TYP_ERROR));
				dtoVal.getImportZmenaDTO().setErrors("T");
			}
			for (Integer ciselnikStlpecGuiID : dtoVal.getWarningMap().keySet()) {
				DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGui(metaList, ciselnikStlpecGuiID);
				importMsgList.addAll(createDTOImportMsgList(dtoCS, dtoVal.getWarningMap().get(ciselnikStlpecGuiID), _CudConsts.IMPORT_MSG_TYP_WARNING));
				dtoVal.getImportZmenaDTO().setWarnings("T");
			}
			dtoVal.getImportZmenaDTO().setImportMsgList(importMsgList.toArray(new DTOImportMsg[importMsgList.size()]));

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupVaues.error");
		}
	}

	private DTOCiselnikStlpecGui createDTOCiselnikStlpecGui(String stlpecNazov) throws AppException {

		try {
			DTOCiselnikStlpec dto = getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(stlpecNazov);

			DTOCiselnikStlpecGui dtoNew = new DTOCiselnikStlpecGui();
			dtoNew.setCiselnikStlpecNazov(dto.getNazov());
			dtoNew.setCiselnikStlpecTyp(dto.getTyp());
			dtoNew.setDlzka(dto.getDlzka());
			dtoNew.setCiselnikStlpecDbTyp(dto.getDbTyp());
			dtoNew.setPovinny(dto.getPovinny());
			return dtoNew;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOCiselnikStlpecGui.error");
			return null;
		}
	}

	private boolean jePlatnostOdValidnaMeta(Date platnostOd) throws AppException {

		try {
			if (StringUtils.isValid(platnostOd)) {
				Date d = sk.ditec.common.utils.DateUtils.removeTime(new Date());
				return platnostOd.after(d) || platnostOd.equals(d);
			}

			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "jePlatnostOdValidnaMeta.error");
			return false;
		}
	}

	private void validateSpecialMetaValuea(DTOCiselnikStlpecGui dto, String valueStr, Set<String> errorsSet, String tabulka) throws AppException {

		try {
			if (!StringUtils.isValid(valueStr)) {
				return;
			}

			if (CudCiselnikPeer.TABLE_NAME.equals(tabulka)) {

				if (trimColumnName(CudCiselnikPeer.TYP).equals(dto.getCiselnikStlpecNazov())) {
					if (!_CudConsts.CISELNIK_TYP_TECHNICKY.equals(valueStr) && !_CudConsts.CISELNIK_TYP_INY.equals(valueStr)) {
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3039, trimColumnName(CudCiselnikPeer.TYP), _CudConsts.CISELNIK_TYP_TECHNICKY, _CudConsts.CISELNIK_TYP_INY));
					}
				}
				if (trimColumnName(CudCiselnikPeer.KATEGORIA).equals(dto.getCiselnikStlpecNazov())) {
					if (!_CudConsts.CISELNIK_KATEGORIA_ZAKLADNY.equals(valueStr) && !_CudConsts.CISELNIK_KATEGORIA_VOZIDLO.equals(valueStr) && !_CudConsts.CISELNIK_KATEGORIA_TAFTSI.equals(valueStr) && !_CudConsts.CISELNIK_KATEGORIA_INFRA.equals(valueStr)
							&& !_CudConsts.CISELNIK_KATEGORIA_LOKALITA.equals(valueStr) && !_CudConsts.CISELNIK_KATEGORIA_PRM.equals(valueStr) && !_CudConsts.CISELNIK_KATEGORIA_SPOLOCNOST.equals(valueStr)) {
						String paramValue = _CudConsts.CISELNIK_KATEGORIA_ZAKLADNY + ", " + _CudConsts.CISELNIK_KATEGORIA_VOZIDLO + ", " + _CudConsts.CISELNIK_KATEGORIA_TAFTSI + ", " + _CudConsts.CISELNIK_KATEGORIA_INFRA + ", " + _CudConsts.CISELNIK_KATEGORIA_LOKALITA + ", "
								+ _CudConsts.CISELNIK_KATEGORIA_PRM + ", " + _CudConsts.CISELNIK_KATEGORIA_SPOLOCNOST;
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3039, trimColumnName(CudCiselnikPeer.KATEGORIA), paramValue));
					}
				}

			} else if (CudCiselnikGuiPeer.TABLE_NAME.equals(tabulka)) {

				if (trimColumnName(CudCiselnikGuiPeer.STAV).equals(dto.getCiselnikStlpecNazov())) {
					if (!_CudConsts.CISELNIK_GUI_STAV_DRAFT.equals(valueStr)) {
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3038, trimColumnName(CudCiselnikGuiPeer.STAV), _CudConsts.CISELNIK_GUI_STAV_DRAFT));
					}
				}

			} else if (CudCiselnikStlpecPeer.TABLE_NAME.equals(tabulka)) {

				if (trimColumnName(CudCiselnikStlpecPeer.TYP).equals(dto.getCiselnikStlpecNazov())) {

					if (!_CudConsts.CISELNIK_STLPEC_TYP_HK.equals(valueStr) && !_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(valueStr) && !_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(valueStr) && !_CudConsts.CISELNIK_STLPEC_TYP_AT.equals(valueStr)) {
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3039, trimColumnName(CudCiselnikStlpecPeer.TYP), _CudConsts.CISELNIK_STLPEC_TYP_HK, _CudConsts.CISELNIK_STLPEC_TYP_PK, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.CISELNIK_STLPEC_TYP_AT));
					}
				}
				if (trimColumnName(CudCiselnikStlpecPeer.DB_TYP).equals(dto.getCiselnikStlpecNazov())) {

					if (!_CudConsts.DB_TYP_STRING.equals(valueStr) && !_CudConsts.DB_TYP_INTEGER.equals(valueStr) && !_CudConsts.DB_TYP_DOUBLE.equals(valueStr) && !_CudConsts.DB_TYP_DATE.equals(valueStr) && !_CudConsts.DB_TYP_BOOLEAN.equals(valueStr)) {
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3039, trimColumnName(CudCiselnikStlpecPeer.DB_TYP), _CudConsts.DB_TYP_STRING, _CudConsts.DB_TYP_INTEGER, _CudConsts.DB_TYP_DOUBLE, _CudConsts.DB_TYP_DATE, _CudConsts.DB_TYP_BOOLEAN));
					}
				}

			} else if (CudCiselnikStlpecGuiPeer.TABLE_NAME.equals(tabulka)) {

				if (trimColumnName(CudCiselnikStlpecGuiPeer.ZAROVNANIE).equals(dto.getCiselnikStlpecNazov())) {
					if (!"R".equals(valueStr) && !"L".equals(valueStr) && !"C".equals(valueStr)) {
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3039, trimColumnName(CudCiselnikStlpecGuiPeer.ZAROVNANIE), "R, L, C"));
					}
				}

			} else if (CudWfDefPeer.TABLE_NAME.equals(tabulka)) {

				if (trimColumnName(CudWfDefPeer.TYP).equals(dto.getCiselnikStlpecNazov())) {

					if (!_CudConsts.WF_DEF_TYP_IN.equals(valueStr) && !_CudConsts.WF_DEF_TYP_SC.equals(valueStr) && !_CudConsts.WF_DEF_TYP_OV.equals(valueStr) && !_CudConsts.WF_DEF_TYP_ES.equals(valueStr)) {
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3039, trimColumnName(CudWfDefPeer.TYP), _CudConsts.WF_DEF_TYP_IN, _CudConsts.WF_DEF_TYP_SC, _CudConsts.WF_DEF_TYP_OV, _CudConsts.WF_DEF_TYP_ES));
					}
				}
				if (trimColumnName(CudWfDefPeer.ZODPOVEDNOST).equals(dto.getCiselnikStlpecNazov())) {
					if (!_CudConsts.WF_DEF_ZODPOVEDNOST_J.equals(valueStr) && !_CudConsts.WF_DEF_ZODPOVEDNOST_V.equals(valueStr)) {
						errorsSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3039, trimColumnName(CudWfDefPeer.ZODPOVEDNOST), _CudConsts.WF_DEF_ZODPOVEDNOST_J, _CudConsts.WF_DEF_ZODPOVEDNOST_V));
					}
				}
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateSpecialMetaValuea.error");
		}
	}

	private void loadOldValues(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			if (CudCiselnikPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
				DTOCiselnik dto = getDelegate().getCiselnikRead().readLight(auth, dtoVal.getImportZmenaDTO().getRowID());
				lookupOldValues(dto, dtoVal);

			} else if (CudCiselnikStlpecPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
				DTOCiselnikStlpec dto = getDelegate().getCiselnikStlpecRead().readLight(auth, dtoVal.getImportZmenaDTO().getRowID());
				lookupOldValues(dto, dtoVal);

			} else if (CudCiselnikGuiPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
				DTOCiselnikGui dto = getDelegate().getCiselnikGuiRead().read(auth, dtoVal.getImportZmenaDTO().getRowID());
				lookupOldValues(dto, dtoVal);

			} else if (CudCiselnikStlpecGuiPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
				DTOCiselnikStlpecGui dto = getDelegate().getCiselnikStlpecGuiRead().readLight(auth, dtoVal.getImportZmenaDTO().getRowID());
				lookupOldValues(dto, dtoVal);

			} else if (CudWfDefPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
				DTOWfDef dto = getDelegate().getWfDefRead().readLight(auth, dtoVal.getImportZmenaDTO().getRowID());
				lookupOldValues(dto, dtoVal);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "loadOldValues.error");
		}
	}

	private void lookupOldValues(DTOCiselnik dto, DTOValidate dtoVal) throws AppException {

		try {
			if (!StringUtils.isValid(dto)) {
				return;
			}

			if (StringUtils.isValid(dto.getTabulka())) {
				dtoVal.getOldValueMap().put("TABULKA", dto.getTabulka());
			}
			if (StringUtils.isValid(dto.getNazov())) {
				dtoVal.getOldValueMap().put("NAZOV", dto.getNazov());
			}
			if (StringUtils.isValid(dto.getPopis())) {
				dtoVal.getOldValueMap().put("POPIS", dto.getPopis());
			}
			if (StringUtils.isValid(dto.getPrintClass())) {
				dtoVal.getOldValueMap().put("PRINT_CLASS", dto.getPrintClass());
			}
			if (StringUtils.isValid(dto.getPrintZahlavie())) {
				dtoVal.getOldValueMap().put("PRINT_ZAHLAVIE", dto.getPrintZahlavie());
			}
			if (StringUtils.isValid(dto.getAktivny())) {
				dtoVal.getOldValueMap().put("AKTIVNY", dto.getAktivny());
			}
			if (StringUtils.isValid(dto.getPredpis())) {
				dtoVal.getOldValueMap().put("PREDPIS", dto.getPredpis());
			}
			if (StringUtils.isValid(dto.getPrilohaKapitola())) {
				dtoVal.getOldValueMap().put("PRILOHA_KAPITOLA", dto.getPrilohaKapitola());
			}
			if (StringUtils.isValid(dto.getHlavny())) {
				dtoVal.getOldValueMap().put("HLAVNY", dto.getHlavny());
			}
			if (StringUtils.isValid(dto.getTyp())) {
				dtoVal.getOldValueMap().put("TYP", dto.getTyp());
			}
			if (StringUtils.isValid(dto.getKategoria())) {
				dtoVal.getOldValueMap().put("KATEGORIA", dto.getKategoria());
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupOldValues.error");
		}
	}

	private void lookupOldValues(DTOCiselnikStlpec dto, DTOValidate dtoVal) throws AppException {

		try {
			if (!StringUtils.isValid(dto)) {
				return;
			}

			if (StringUtils.isValid(dto.getIDCiselnik())) {
				dtoVal.getOldValueMap().put("ID_CISELNIK", dto.getIDCiselnik().toString());
			}
			if (StringUtils.isValid(dto.getNazov())) {
				dtoVal.getOldValueMap().put("NAZOV", dto.getNazov());
			}
			if (StringUtils.isValid(dto.getNadpis())) {
				dtoVal.getOldValueMap().put("NADPIS", dto.getNadpis());
			}
			if (StringUtils.isValid(dto.getTyp())) {
				dtoVal.getOldValueMap().put("TYP", dto.getTyp());
			}
			if (StringUtils.isValid(dto.getPoradie())) {
				dtoVal.getOldValueMap().put("PORADIE", dto.getPoradie().toString());
			}
			if (StringUtils.isValid(dto.getDlzka())) {
				dtoVal.getOldValueMap().put("DLZKA", dto.getDlzka().toString());
			}
			if (StringUtils.isValid(dto.getDecimals())) {
				dtoVal.getOldValueMap().put("DECIMALS", dto.getDecimals().toString());
			}
			if (StringUtils.isValid(dto.getDbTyp())) {
				dtoVal.getOldValueMap().put("DB_TYP", dto.getDbTyp());
			}
			if (StringUtils.isValid(dto.getPovinny())) {
				dtoVal.getOldValueMap().put("POVINNY", dto.getPovinny());
			}
			if (StringUtils.isValid(dto.getJedinecny())) {
				dtoVal.getOldValueMap().put("JEDINECNY", dto.getJedinecny());
			}
			if (StringUtils.isValid(dto.getFk1IDCiselnik())) {
				dtoVal.getOldValueMap().put("FK1_ID_CISELNIK", dto.getFk1IDCiselnik().toString());
			}
			if (StringUtils.isValid(dto.getFk1PkNazov())) {
				dtoVal.getOldValueMap().put("FK1_PK_NAZOV", dto.getFk1PkNazov());
			}
			if (StringUtils.isValid(dto.getFk1FkNazov())) {
				dtoVal.getOldValueMap().put("FK1_FK_NAZOV", dto.getFk1FkNazov());
			}
			if (StringUtils.isValid(dto.getPopis())) {
				dtoVal.getOldValueMap().put("POPIS", dto.getPopis());
			}
			if (StringUtils.isValid(dto.getAktivny())) {
				dtoVal.getOldValueMap().put("AKTIVNY", dto.getAktivny());
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupOldValues.error");
		}
	}

	private void lookupOldValues(DTOCiselnikGui dto, DTOValidate dtoVal) throws AppException {

		try {
			if (!StringUtils.isValid(dto)) {
				return;
			}

			if (StringUtils.isValid(dto.getIDCiselnik())) {
				dtoVal.getOldValueMap().put("ID_CISELNIK", dto.getIDCiselnik().toString());
			}
			if (StringUtils.isValid(dto.getStav())) {
				dtoVal.getOldValueMap().put("STAV", dto.getStav());
			}
			if (StringUtils.isValid(dto.getPlatnostOd())) {
				dtoVal.getOldValueMap().put("PLATNOST_OD", _CudConsts.DATE_FORMAT.format(dto.getPlatnostOd()));
			}
			if (StringUtils.isValid(dto.getPlatnostDo())) {
				dtoVal.getOldValueMap().put("PLATNOST_DO", _CudConsts.DATE_FORMAT.format(dto.getPlatnostDo()));
			}
			if (StringUtils.isValid(dto.getCasPublikovania())) {
				dtoVal.getOldValueMap().put("CAS_PUBLIKOVANIA", _CudConsts.DATE_TIME_FORMAT.format(dto.getCasPublikovania()));
			}
			if (StringUtils.isValid(dto.getPopis())) {
				dtoVal.getOldValueMap().put("POPIS", dto.getPopis());
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupOldValues.error");
		}
	}

	private void lookupOldValues(DTOCiselnikStlpecGui dto, DTOValidate dtoVal) throws AppException {

		try {
			if (!StringUtils.isValid(dto)) {
				return;
			}

			if (StringUtils.isValid(dto.getIDCiselnikGui())) {
				dtoVal.getOldValueMap().put("ID_CISELNIK_GUI", dto.getIDCiselnikGui().toString());
			}
			if (StringUtils.isValid(dto.getIDCiselnikStlpec())) {
				dtoVal.getOldValueMap().put("ID_CISELNIK_STLPEC", dto.getIDCiselnikStlpec().toString());
			}
			if (StringUtils.isValid(dto.getNadpis())) {
				dtoVal.getOldValueMap().put("NADPIS", dto.getNadpis());
			}
			if (StringUtils.isValid(dto.getPoradie())) {
				dtoVal.getOldValueMap().put("PORADIE", dto.getPoradie().toString());
			}
			if (StringUtils.isValid(dto.getDlzka())) {
				dtoVal.getOldValueMap().put("DLZKA", dto.getDlzka().toString());
			}
			if (StringUtils.isValid(dto.getDecimals())) {
				dtoVal.getOldValueMap().put("DECIMALS", dto.getDecimals().toString());
			}
			if (StringUtils.isValid(dto.getCiselnikStlpecDbTyp())) {
				dtoVal.getOldValueMap().put("DB_TYP", dto.getCiselnikStlpecDbTyp());
			}
			if (StringUtils.isValid(dto.getZmena())) {
				dtoVal.getOldValueMap().put("ZMENA", dto.getZmena());
			}
			if (StringUtils.isValid(dto.getPovinny())) {
				dtoVal.getOldValueMap().put("POVINNY", dto.getPovinny());
			}
			if (StringUtils.isValid(dto.getZarovnanie())) {
				dtoVal.getOldValueMap().put("ZAROVNANIE", dto.getZarovnanie());
			}
			if (StringUtils.isValid(dto.getFk1FkNazov())) {
				dtoVal.getOldValueMap().put("FK1_FK_NAZOV", dto.getFk1FkNazov());
			}
			if (StringUtils.isValid(dto.getFk2IDCiselnik())) {
				dtoVal.getOldValueMap().put("FK2_ID_CISELNIK", dto.getFk2IDCiselnik().toString());
			}
			if (StringUtils.isValid(dto.getFk2PkNazov())) {
				dtoVal.getOldValueMap().put("FK2_PK_NAZOV", dto.getFk2PkNazov());
			}
			if (StringUtils.isValid(dto.getFk2FkNazov())) {
				dtoVal.getOldValueMap().put("FK2_FK_NAZOV", dto.getFk2FkNazov());
			}
			if (StringUtils.isValid(dto.getListZobrazenie())) {
				dtoVal.getOldValueMap().put("LIST_ZOBRAZENIE", dto.getListZobrazenie());
			}
			if (StringUtils.isValid(dto.getListSirka())) {
				dtoVal.getOldValueMap().put("LIST_SIRKA", dto.getListSirka().toString());
			}
			if (StringUtils.isValid(dto.getListSirkaChange())) {
				dtoVal.getOldValueMap().put("LIST_SIRKA_CHANGE", dto.getListSirkaChange());
			}
			if (StringUtils.isValid(dto.getFormZobrazenie())) {
				dtoVal.getOldValueMap().put("FORM_ZOBRAZENIE", dto.getFormZobrazenie());
			}
			if (StringUtils.isValid(dto.getFormSirka())) {
				dtoVal.getOldValueMap().put("FORM_SIRKA", dto.getFormSirka().toString());
			}
			if (StringUtils.isValid(dto.getPopupZobrazenie())) {
				dtoVal.getOldValueMap().put("POPUP_ZOBRAZENIE", dto.getPopupZobrazenie().toString());
			}
			if (StringUtils.isValid(dto.getPopupSirka())) {
				dtoVal.getOldValueMap().put("POPUP_SIRKA", dto.getPopupSirka().toString());
			}
			if (StringUtils.isValid(dto.getPopupSirkaChange())) {
				dtoVal.getOldValueMap().put("POPUP_SIRKA_CHANGE", dto.getPopupSirkaChange());
			}
			if (StringUtils.isValid(dto.getLookupZobrazenie())) {
				dtoVal.getOldValueMap().put("LOOKUP_ZOBRAZENIE", dto.getLookupZobrazenie());
			}
			if (StringUtils.isValid(dto.getEditControl())) {
				dtoVal.getOldValueMap().put("EDIT_CONTROL", dto.getEditControl());
			}
			if (StringUtils.isValid(dto.getRegExp())) {
				dtoVal.getOldValueMap().put("REG_EXP", dto.getRegExp());
			}
			if (StringUtils.isValid(dto.getPopis())) {
				dtoVal.getOldValueMap().put("POPIS", dto.getPopis());
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupOldValues.error");
		}
	}

	private void lookupOldValues(DTOWfDef dto, DTOValidate dtoVal) throws AppException {

		try {
			if (!StringUtils.isValid(dto)) {
				return;
			}

			if (StringUtils.isValid(dto.getIDCiselnik())) {
				dtoVal.getOldValueMap().put("ID_CISELNIK", dto.getIDCiselnik().toString());
			}
			if (StringUtils.isValid(dto.getIDWfDefNasl())) {
				dtoVal.getOldValueMap().put("ID_WF_DEF_NASL", dto.getIDWfDefNasl().toString());
			}
			if (StringUtils.isValid(dto.getNazov())) {
				dtoVal.getOldValueMap().put("NAZOV", dto.getNazov());
			}
			if (StringUtils.isValid(dto.getTyp())) {
				dtoVal.getOldValueMap().put("TYP", dto.getTyp());
			}
			if (StringUtils.isValid(dto.getZodpovednost())) {
				dtoVal.getOldValueMap().put("ZODPOVEDNOST", dto.getZodpovednost());
			}
			if (StringUtils.isValid(dto.getEmailList())) {
				dtoVal.getOldValueMap().put("EMAIL_LIST", dto.getEmailList());
			}
			if (StringUtils.isValid(dto.getEmailText())) {
				dtoVal.getOldValueMap().put("EMAIL_TEXT", dto.getEmailText());
			}
			if (StringUtils.isValid(dto.getEmailSubject())) {
				dtoVal.getOldValueMap().put("EMAIL_SUBJECT", dto.getEmailSubject());
			}
			if (StringUtils.isValid(dto.getEmailSend())) {
				dtoVal.getOldValueMap().put("EMAIL_SEND", dto.getEmailSend());
			}
			if (StringUtils.isValid(dto.getHodiny())) {
				dtoVal.getOldValueMap().put("HODINY", dto.getHodiny().toString());
			}
			if (StringUtils.isValid(dto.getIDSkupina())) {
				dtoVal.getOldValueMap().put("ID_SKUPINA", dto.getIDSkupina().toString());
			}
			if (StringUtils.isValid(dto.getSkupinaNazov())) {
				dtoVal.getOldValueMap().put("SKUPINA_NAZOV", dto.getSkupinaNazov());
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupOldValues.error");
		}
	}

	private void validateFkPreCiselnikStlpec(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			String typ = dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikStlpecPeer.TYP));
			if (!StringUtils.isValid(typ)) {
				typ = dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecPeer.TYP));
			}
			if (!_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(typ)) {
				return;
			}

			boolean b1 = false;
			String fkIDCiselnik = null;
			if (dtoVal.getNewValueMap().keySet().contains(trimColumnName(CudCiselnikStlpecPeer.FK1_ID_CISELNIK))) {
				fkIDCiselnik = dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikStlpecPeer.FK1_ID_CISELNIK));
				b1 = true;
			} else {
				fkIDCiselnik = dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecPeer.FK1_ID_CISELNIK));
			}
			if (!StringUtils.isValid(fkIDCiselnik)) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudCiselnikStlpecPeer.FK1_ID_CISELNIK)), dtoVal.getZdroj(), true);
			}

			boolean b2 = false;
			String fkPkNazov = null;
			if (dtoVal.getNewValueMap().keySet().contains(trimColumnName(CudCiselnikStlpecPeer.FK1_PK_NAZOV))) {
				fkPkNazov = dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikStlpecPeer.FK1_PK_NAZOV));
				b2 = true;
			} else {
				fkPkNazov = dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecPeer.FK1_PK_NAZOV));
			}
			if (!StringUtils.isValid(fkPkNazov)) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudCiselnikStlpecPeer.FK1_PK_NAZOV)), dtoVal.getZdroj(), true);
			}

			if ((b1 || b2) && StringUtils.isValid(fkIDCiselnik) && StringUtils.isValid(fkPkNazov)) {
				if (!StringUtils.isValid(getDelegate().getCiselnikStlpecRead().readLight(auth, Integer.parseInt(fkIDCiselnik), fkPkNazov))) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3040, trimColumnName(CudCiselnikStlpecPeer.FK1_PK_NAZOV)), dtoVal.getZdroj(), true);
				}
			}

			String fkFkNazov = dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikStlpecPeer.FK1_FK_NAZOV));
			if (StringUtils.isValid(fkIDCiselnik) && StringUtils.isValid(fkFkNazov)) {
				if (!StringUtils.isValid(getDelegate().getCiselnikStlpecRead().readLight(auth, Integer.parseInt(fkIDCiselnik), fkFkNazov))) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3040, trimColumnName(CudCiselnikStlpecPeer.FK1_FK_NAZOV)), dtoVal.getZdroj(), true);
				}
			}

		} catch (Throwable t) {
			handleException(t, "validateFkPreCiselnikStlpec.error", auth);
		}
	}

	private void validateFkPreCiselnikStlpecGui(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			Integer ciselnikStlpecID = null;
			if (dtoVal.getNewValueMap().keySet().contains(trimColumnName(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC))) {
				ciselnikStlpecID = Integer.parseInt(dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC)));
			} else {
				ciselnikStlpecID = Integer.parseInt(dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC)));
			}
			DTOCiselnikStlpec dtoCS = getDelegate().getCiselnikStlpecRead().readLight(auth, ciselnikStlpecID);
			if (!_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getTyp())) {
				return;
			}

			boolean b1 = false;
			String fkFkNazov = null;
			if (dtoVal.getNewValueMap().keySet().contains(trimColumnName(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV))) {
				fkFkNazov = dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV));
				b1 = true;
			} else {
				fkFkNazov = dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV));
			}
			if (!StringUtils.isValid(fkFkNazov)) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV)), dtoVal.getZdroj(), true);
			}
			if (b1 && StringUtils.isValid(fkFkNazov)) {
				if (!StringUtils.isValid(getDelegate().getCiselnikStlpecRead().readLight(auth, dtoCS.getFk1IDCiselnik(), fkFkNazov))) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3040, trimColumnName(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV)), dtoVal.getZdroj(), true);
				}
			}

			b1 = false;
			String fkIDCiselnik = null;
			if (dtoVal.getNewValueMap().keySet().contains(trimColumnName(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK))) {
				fkIDCiselnik = dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK));
				b1 = true;
			} else {
				fkIDCiselnik = dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK));
			}

			boolean b2 = false;
			String fkPkNazov = null;
			if (dtoVal.getNewValueMap().keySet().contains(trimColumnName(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV))) {
				fkPkNazov = dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV));
				b2 = true;
			} else {
				fkPkNazov = dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV));
			}

			boolean b3 = false;
			fkFkNazov = null;
			if (dtoVal.getNewValueMap().keySet().contains(trimColumnName(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV))) {
				fkFkNazov = dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV));
				b3 = true;
			} else {
				fkFkNazov = dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV));
			}

			if (StringUtils.isValid(fkIDCiselnik) || StringUtils.isValid(fkPkNazov) || StringUtils.isValid(fkFkNazov)) {

				if (!StringUtils.isValid(fkIDCiselnik)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK)), dtoVal.getZdroj(), true);
				}
				if (!StringUtils.isValid(fkPkNazov)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV)), dtoVal.getZdroj(), true);
				}
				if (!StringUtils.isValid(fkFkNazov)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV)), dtoVal.getZdroj(), true);
				}
			}

			if (StringUtils.isValid(fkIDCiselnik) & StringUtils.isValid(fkPkNazov) && StringUtils.isValid(fkFkNazov) && (b1 || b2 || b3)) {
				if (!StringUtils.isValid(getDelegate().getCiselnikStlpecRead().readLight(auth, Integer.parseInt(fkIDCiselnik), fkPkNazov))) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3040, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV)), dtoVal.getZdroj(), true);
				}
				if (!StringUtils.isValid(getDelegate().getCiselnikStlpecRead().readLight(auth, Integer.parseInt(fkIDCiselnik), fkFkNazov))) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3040, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV)), dtoVal.getZdroj(), true);
				}
			}

		} catch (Throwable t) {
			handleException(t, "validateFkPreCiselnikStlpecGui.error", auth);
		}
	}

	private boolean validateZobrazenie(DTOValidate dtoVal, String zobrazenieName, String sirkaName) throws AppException {

		try {
			String zobrazenie = null;
			if (dtoVal.getNewValueMap().keySet().contains(zobrazenieName)) {
				zobrazenie = dtoVal.getNewValueMap().get(zobrazenieName);
			} else {
				zobrazenie = dtoVal.getOldValueMap().get(zobrazenieName);
			}

			String sirka = null;
			if (dtoVal.getNewValueMap().keySet().contains(sirkaName)) {
				sirka = dtoVal.getNewValueMap().get(sirkaName);
			} else {
				sirka = dtoVal.getOldValueMap().get(sirkaName);
			}

			if ("T".equals(zobrazenie)) {
				return StringUtils.isValid(sirka);
			}

			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateZobrazenie.error");
			return false;
		}
	}

	private boolean validateSkupina(DTOValidate dtoVal, DTOSkupina[] skupinaList) throws AppException {

		try {
			String skupinaID = dtoVal.getNewValueMap().get(trimColumnName(CudWfDefPeer.ID_SKUPINA));
			String skupinaNazov = dtoVal.getNewValueMap().get(trimColumnName(CudWfDefPeer.SKUPINA_NAZOV));

			if (StringUtils.isValid(skupinaID) || StringUtils.isValid(skupinaNazov)) {
				boolean b = false;

				for (DTOSkupina dto : skupinaList) {

					boolean b1 = dto.getSkupinaID().toString().equals(skupinaID);
					boolean b2 = dto.getNazov().equals(skupinaNazov);

					if (b1 && b2) {
						b = true;
					} else if (!StringUtils.isValid(skupinaID) && b2) {
						b = true;
					} else if (b1 && !StringUtils.isValid(skupinaNazov)) {
						b = true;
					}
				}

				return b;
			}

			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateSkupina.error");
			return false;
		}
	}

	private void insertKontrolaCiselnik(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (!dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3029), dtoVal.getZdroj(), true);
			}

			// kontrola povinny atributov
			for (String colName : new String[] { CudCiselnikPeer.TABULKA, CudCiselnikPeer.NAZOV, CudCiselnikPeer.PRINT_ZAHLAVIE, CudCiselnikPeer.AKTIVNY, CudCiselnikPeer.TYP, CudCiselnikPeer.KATEGORIA }) {
				String newValue = dtoVal.getNewValueMap().get(trimColumnName(colName));
				if (!StringUtils.isValid(newValue)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(colName)), dtoVal.getZdroj(), true);
				}
			}

			// ta ista kontrola ako pri inserte z GUI
			DTOCiselnik dto = new DTOCiselnik();
			dto.setTabulka(dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikPeer.TABULKA)));
			dto.setTyp(dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikPeer.TYP)));
			String err = getDelegate().getCiselnikRead().updateKontrola(auth, dto);
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			handleException(t, "insertKontrolaCiselnik.error", auth);
		}
	}

	private void insertKontrolaCiselnikStlpec(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (!dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3037), dtoVal.getZdroj(), true);
			}

			// kontrola povinny atributov
			for (String colName : new String[] { CudCiselnikStlpecPeer.ID_CISELNIK, CudCiselnikStlpecPeer.NAZOV, CudCiselnikStlpecPeer.NADPIS, CudCiselnikStlpecPeer.TYP, CudCiselnikStlpecPeer.PORADIE, CudCiselnikStlpecPeer.DLZKA, CudCiselnikStlpecPeer.DB_TYP, CudCiselnikStlpecPeer.POVINNY,
					CudCiselnikStlpecPeer.JEDINECNY, CudCiselnikStlpecPeer.AKTIVNY }) {
				String newValue = dtoVal.getNewValueMap().get(trimColumnName(colName));
				if (!StringUtils.isValid(newValue)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(colName)), dtoVal.getZdroj(), true);
				}
			}

			// kontrola FK
			validateFkPreCiselnikStlpec(auth, dtoVal);

		} catch (Throwable t) {
			handleException(t, "insertKontrolaCiselnikStlpec.error", auth);
		}
	}

	private void insertKontrolaCiselnikGui(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (!dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3037), dtoVal.getZdroj(), true);
			}

			// kontrola povinny atributov
			for (String colName : new String[] { CudCiselnikGuiPeer.ID_CISELNIK, CudCiselnikGuiPeer.STAV }) {
				if (!dtoVal.getNewValueMap().keySet().contains(trimColumnName(colName))) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(colName)), dtoVal.getZdroj(), true);
					continue;
				}
				String newValue = dtoVal.getNewValueMap().get(trimColumnName(colName));
				if (!StringUtils.isValid(newValue)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(colName)), dtoVal.getZdroj(), true);
				}
			}

			// ta ista kontrola ako pri inserte z GUI
			DTOCiselnikGui dtoNew = new DTOCiselnikGui();
			dtoNew.setIDCiselnik(Integer.parseInt(dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikGuiPeer.ID_CISELNIK))));
			dtoNew.setStav(dtoVal.getNewValueMap().get(trimColumnName(CudCiselnikGuiPeer.STAV)));
			String err = getDelegate().getCiselnikGuiRead().updateKontrola(auth, dtoNew);
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "insertKontrolaCiselnikGui.error");
		}
	}

	private void insertKontrolaCiselnikStlpecGui(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (!dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3037), dtoVal.getZdroj(), true);
				return;
			}

			// kontrola povinny atributov
			for (String colName : new String[] { CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecGuiPeer.NADPIS, CudCiselnikStlpecGuiPeer.PORADIE, CudCiselnikStlpecGuiPeer.DLZKA, CudCiselnikStlpecGuiPeer.ZMENA,
					CudCiselnikStlpecGuiPeer.POVINNY, CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE, CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE, CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE, CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE,
					CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE }) {
				String newValue = dtoVal.getNewValueMap().get(trimColumnName(colName));
				if (!StringUtils.isValid(newValue)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(colName)), dtoVal.getZdroj(), true);
				}
			}
			if (!validateZobrazenie(dtoVal, trimColumnName(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE), trimColumnName(CudCiselnikStlpecGuiPeer.LIST_SIRKA))) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudCiselnikStlpecGuiPeer.LIST_SIRKA)), dtoVal.getZdroj(), true);
			}
			if (!validateZobrazenie(dtoVal, trimColumnName(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE), trimColumnName(CudCiselnikStlpecGuiPeer.FORM_SIRKA))) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudCiselnikStlpecGuiPeer.FORM_SIRKA)), dtoVal.getZdroj(), true);
			}
			if (!validateZobrazenie(dtoVal, trimColumnName(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE), trimColumnName(CudCiselnikStlpecGuiPeer.POPUP_SIRKA))) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudCiselnikStlpecGuiPeer.POPUP_SIRKA)), dtoVal.getZdroj(), true);
			}

			// kontrola FK
			validateFkPreCiselnikStlpecGui(auth, dtoVal);

		} catch (Throwable t) {
			handleException(t, "insertKontrolaCiselnikStlpec.error", auth);
		}
	}

	private void insertKontrolaWfDef(AuthInfo auth, DTOValidate dtoVal, DTOSkupina[] skupinaList) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (!dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3037), dtoVal.getZdroj(), true);
			}

			// kontrola povinny atributov
			for (String colName : new String[] { CudWfDefPeer.ID_CISELNIK, CudWfDefPeer.NAZOV, CudWfDefPeer.TYP, CudWfDefPeer.EMAIL_SEND, CudWfDefPeer.ID_SKUPINA, CudWfDefPeer.SKUPINA_NAZOV }) {
				String newValue = dtoVal.getNewValueMap().get(trimColumnName(colName));
				if (!StringUtils.isValid(newValue)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(colName)), dtoVal.getZdroj(), true);
				}
			}

			String newValue = dtoVal.getNewValueMap().get(trimColumnName(CudWfDefPeer.TYP));
			if (_CudConsts.WF_DEF_TYP_IN.equals(newValue) || _CudConsts.WF_DEF_TYP_SC.equals(newValue) || _CudConsts.WF_DEF_TYP_OV.equals(newValue)) {
				newValue = dtoVal.getNewValueMap().get(trimColumnName(CudWfDefPeer.ZODPOVEDNOST));
				if (!StringUtils.isValid(newValue)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(CudWfDefPeer.ZODPOVEDNOST)), dtoVal.getZdroj(), true);
				}
			}

			// kontrola skupin z IAM
			if (!validateSkupina(dtoVal, skupinaList)) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3061), dtoVal.getZdroj(), true);
			}

			// rovnaka konrola ako na GUI
			DTOWfDef dto = new DTOWfDef();
			dto.setEmailList(dtoVal.getNewValueMap().get(trimColumnName(CudWfDefPeer.EMAIL_LIST)));
			String err = getDelegate().getWfDefRead().updateKontrola(auth, dto);
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "insertKontrolaWfDef.error");
		}
	}

	private void insertKontrolaPreklad(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola povinny atributov
			for (String colName : new String[] { CudPrekladPeer.ID_PREKLAD_JAZYK, CudPrekladPeer.ID_PREKLAD_STLPEC, CudPrekladPeer.ZAZNAM_ID, CudPrekladPeer.PREKLAD }) {
				String newValue = dtoVal.getNewValueMap().get(trimColumnName(colName));
				if (!StringUtils.isValid(newValue)) {
					addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, trimColumnName(colName)), dtoVal.getZdroj(), true);
				}
			}

			// ta ista kontrola ako pri inserte z GUI
			DTOPreklad dto = new DTOPreklad();
			String value = dtoVal.getNewValueMap().get(trimColumnName(CudPrekladPeer.PREKLAD_ID));
			dto.setPrekladID(StringUtils.isValid(value) ? Integer.parseInt(value) : null);
			dto.setIDPrekladJazyk(Integer.parseInt(dtoVal.getNewValueMap().get(trimColumnName(CudPrekladPeer.ID_PREKLAD_JAZYK))));
			dto.setIDPrekladStlpec(Integer.parseInt(dtoVal.getNewValueMap().get(trimColumnName(CudPrekladPeer.ID_PREKLAD_STLPEC))));
			dto.setZaznamID(Integer.parseInt(dtoVal.getNewValueMap().get(trimColumnName(CudPrekladPeer.ZAZNAM_ID))));
			String err = getDelegate().getPrekladRead().updateKontrola(auth, dto);
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			handleException(t, "insertKontrolaPreklad.error", auth);
		}
	}

	private void updateKontrolaCiselnik(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

			// ta ista kontrola ako pri update z GUI
			DTOCiselnik dto = new DTOCiselnik();
			dto.setCiselnikID(dtoVal.getImportZmenaDTO().getRowID());
			String colName = trimColumnName(CudCiselnikPeer.TABULKA);
			dto.setTabulka(dtoVal.getNewValueMap().keySet().contains(colName) ? dtoVal.getNewValueMap().get(colName) : dtoVal.getOldValueMap().get(colName));
			colName = trimColumnName(CudCiselnikPeer.TYP);
			dto.setTyp(dtoVal.getNewValueMap().keySet().contains(colName) ? dtoVal.getNewValueMap().get(colName) : dtoVal.getOldValueMap().get(colName));
			String err = getDelegate().getCiselnikRead().updateKontrola(auth, dto);
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), null, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrolaCiselnik.error");
		}
	}

	private void updateKontrolaCiselnikStlpec(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

			// kontrola FK
			validateFkPreCiselnikStlpec(auth, dtoVal);

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrolaCiselnikStlpec.error");
		}
	}

	private void updateKontrolaCiselnikGui(DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

			// kontrola ci je ciselnikGui v stave DRAFT
			if (!_CudConsts.CISELNIK_GUI_STAV_DRAFT.equals(dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikGuiPeer.STAV)))) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_606), dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrolaCiselnikStlpec.error");
		}
	}

	private String lookupValue(DTOValidate dtoVal, String colName) throws AppException {

		try {
			String oldValue = dtoVal.getOldValueMap().get(trimColumnName(colName));
			String newValue = dtoVal.getNewValueMap().get(trimColumnName(colName));
			return (StringUtils.isValid(newValue)) ? newValue : oldValue;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupValue.error");
			return null;
		}
	}

	private void updateKontrolaCiselnikStlpecGui(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

			// kontrola FK
			validateFkPreCiselnikStlpecGui(auth, dtoVal);

			// ta ista kontrola ako pri update z GUI
			DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
			dto.setIDCiselnikGui(Integer.parseInt(dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI))));
			dto.setEditControl(lookupValue(dtoVal, CudCiselnikStlpecGuiPeer.EDIT_CONTROL));
			dto.setRegExp(lookupValue(dtoVal, CudCiselnikStlpecGuiPeer.EDIT_CONTROL));
			String err = getDelegate().getCiselnikStlpecGuiRead().updateKontrola(auth, dto);
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrolaCiselnikStlpecGui.error");
		}
	}

	private void updateKontrolaWfDef(AuthInfo auth, DTOValidate dtoVal, DTOSkupina[] skupinaList) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

			// kontrola skupin z IAM
			if (!validateSkupina(dtoVal, skupinaList)) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3061), dtoVal.getZdroj(), true);
			}

			// rovnaka kontrola ako na GUI
			DTOWfDef dto = new DTOWfDef();
			dto.setEmailList(dtoVal.getNewValueMap().get(trimColumnName(CudWfDefPeer.EMAIL_LIST)));
			String err = getDelegate().getWfDefRead().updateKontrola(auth, dto);
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrolaWfDef.error");
		}
	}

	private void updateKontrolaPreklad(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

			// ta ista kontrola ako pri inserte z GUI
			DTOPreklad dto = new DTOPreklad();
			String colName = trimColumnName(CudPrekladPeer.PREKLAD_ID);
			String value = dtoVal.getNewValueMap().keySet().contains(colName) ? dtoVal.getNewValueMap().get(colName) : dtoVal.getOldValueMap().get(colName);
			dto.setPrekladID(Integer.parseInt(value));
			colName = trimColumnName(CudPrekladPeer.ID_PREKLAD_JAZYK);
			value = dtoVal.getNewValueMap().keySet().contains(colName) ? dtoVal.getNewValueMap().get(colName) : dtoVal.getOldValueMap().get(colName);
			dto.setIDPrekladJazyk(Integer.parseInt(value));
			colName = trimColumnName(CudPrekladPeer.ID_PREKLAD_STLPEC);
			value = dtoVal.getNewValueMap().keySet().contains(colName) ? dtoVal.getNewValueMap().get(colName) : dtoVal.getOldValueMap().get(colName);
			dto.setIDPrekladStlpec(Integer.parseInt(value));
			colName = trimColumnName(CudPrekladPeer.ZAZNAM_ID);
			value = dtoVal.getNewValueMap().keySet().contains(colName) ? dtoVal.getNewValueMap().get(colName) : dtoVal.getOldValueMap().get(colName);
			dto.setZaznamID(Integer.parseInt(value));
			String err = getDelegate().getPrekladRead().updateKontrola(auth, dto);
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrolaCiselnik.error");
		}
	}

	private void deleteKontrolaCiselnik(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

			// ta ista kontrola ako pri delete z GUI
			String err = getDelegate().getCiselnikRead().deleteKontrola(auth, dtoVal.getImportZmenaDTO().getRowID());
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "deleteKontrolaCiselnik.error");
		}
	}

	private void deleteKontrolaCiselnikStlpec(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

			// ta ista kontrola ako pri delete z GUI
			Integer ciselnikID = Integer.parseInt(dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecPeer.ID_CISELNIK)));
			String err = getDelegate().getCiselnikStlpecRead().deleteKontrola(auth, ciselnikID, dtoVal.getImportZmenaDTO().getRowID());
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "deleteKontrolaCiselnikStlpec.error");
		}
	}

	private void deleteKontrolaCiselnikGui(DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "deleteKontrolaCiselnikStlpec.error");
		}
	}

	private void deleteKontrolaCiselnikStlpecGui(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

			// ta ista kontrola ako pri delete z GUI
			String ciselnikGuiID = dtoVal.getOldValueMap().get(trimColumnName(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI));
			DTOCiselnikStlpecGui dto = new DTOCiselnikStlpecGui();
			dto.setIDCiselnikGui(Integer.parseInt(ciselnikGuiID));
			String err = getDelegate().getCiselnikStlpecGuiRead().deleteKontrola(auth, dto);
			if (StringUtils.isValid(err)) {
				addMsg(dtoVal.getErrorsMap(), 0, err, dtoVal.getZdroj(), true);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "deleteKontrolaCiselnikStlpecGui.error");
		}
	}

	private void deleteWfDef(DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "deleteWfDef.error");
		}
	}

	private void deleteKontrolaPreklad(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			// kontrola ci zaznam existuje v DB
			if (dtoVal.getOldValueMap().keySet().isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), 0, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3030), dtoVal.getZdroj(), true);
				return;
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "deleteKontrolaCiselnikStlpecGui.error");
		}
	}

	private List<DTOCiselnikStlpecGui> temporaryMetaList(DTOValidate dtoVal) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			DTOCiselnikStlpecGui dtoNew = createDTOCiselnikStlpecGui(_CudConsts.NAZOV_PK_KEY);
			dtoNew.setCiselnikStlpecNazov(_CudConsts.NAZOV_XLS_ROW_ID);
			dtoNew.setNadpis(_CudConsts.NAZOV_XLS_ROW_ID);
			dtoNew.setPovinny(_CudConsts.ZDROJ_XLS.equals(dtoVal.getZdroj()) ? "T" : "F");
			resultList.add(dtoNew);

			dtoNew = createDTOCiselnikStlpecGui(_CudConsts.NAZOV_PLATNOST_OD);
			dtoNew.setCiselnikStlpecNazov(_CudConsts.NAZOV_XLS_PLATNOST_OD);
			dtoNew.setNadpis(_CudConsts.NAZOV_XLS_PLATNOST_OD);
			dtoNew.setPovinny(StringUtils.isValid(dtoVal.getCiselnikID()) ? "T" : "F");
			resultList.add(dtoNew);

			dtoNew = createDTOCiselnikStlpecGui(_CudConsts.NAZOV_ZMAZ);
			dtoNew.setCiselnikStlpecNazov(_CudConsts.NAZOV_XLS_OPERACIA);
			dtoNew.setNadpis(_CudConsts.NAZOV_XLS_OPERACIA);
			dtoNew.setCiselnikStlpecDbTyp(_CudConsts.DB_TYP_STRING);
			resultList.add(dtoNew);

			dtoNew = createDTOCiselnikStlpecGui(_CudConsts.NAZOV_ZMAZ);
			dtoNew.setCiselnikStlpecNazov(_CudConsts.NAZOV_XLS_POZNAMKA);
			dtoNew.setNadpis(_CudConsts.NAZOV_XLS_POZNAMKA);
			dtoNew.setCiselnikStlpecDbTyp(_CudConsts.DB_TYP_STRING);
			dtoNew.setPovinny("F");
			dtoNew.setDlzka(_CudConsts.MAX_LENGTH_STRING);
			resultList.add(dtoNew);

			dtoNew = createDTOCiselnikStlpecGui(_CudConsts.NAZOV_PLATNOST_DO);
			dtoNew.setCiselnikStlpecNazov(_CudConsts.NAZOV_XLS_CAS_SCHVALENIA_GR);
			dtoNew.setNadpis(_CudConsts.NAZOV_XLS_CAS_SCHVALENIA_GR);
			dtoNew.setPovinny("F");
			resultList.add(dtoNew);

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "temporaryMetaList.error");
			return null;
		}
	}

	private List<DTOCiselnikStlpecGui> metaList(AuthInfo auth, DTOValidate dtoVal) throws AppException {

		try {
			if (StringUtils.isValid(dtoVal.getCiselnikID())) {

				DTOCiselnikStlpecGui[] metaPole = getDelegate().getCiselnikStlpecGuiRead().listForForm(auth, dtoVal.getCiselnikID(), dtoVal.getImportZmenaDTO().getPlatnostOd());
				return new ArrayList<DTOCiselnikStlpecGui>(Arrays.asList(metaPole));

			} else {
				return getDelegate().getGuiRead().metaList(auth, dtoVal.getCiselnikTabulka(), dtoVal.getImportZmenaDTO().getOperacia());
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "temporaryMetaList.error");
			return null;
		}
	}

	private boolean parseData(AuthInfo auth, DTOValidate dtoVal, Map<String, List<DTOCiselnikStlpecGui>> metaMap, Map<String, String> rowMap) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> tempMetaList = metaMap.get("tempMetaList");
			if (!StringUtils.isValid(tempMetaList)) {
				tempMetaList = temporaryMetaList(dtoVal);
				metaMap.put("tempMetaList", tempMetaList);
			}

			Set<String> errorSet = new HashSet<String>();

			// XLS_ROW_ID
			errorSet.clear();
			String s = rowMap.get(_CudConsts.NAZOV_XLS_ROW_ID);
			DTOCiselnikStlpecGui dtoMeta = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(tempMetaList, _CudConsts.NAZOV_XLS_ROW_ID);
			String value = validateValue(dtoMeta, s, errorSet, dtoVal.getZdroj());
			if (!errorSet.isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), null, errorSet, dtoVal.getZdroj(), true);
				return true;
			} else if (StringUtils.isValid(value)) {
				dtoVal.getImportZmenaDTO().setXlsRowID(Integer.parseInt(value));
			}

			// XLS_PLATNOST_OD
			errorSet.clear();
			s = rowMap.get(_CudConsts.NAZOV_XLS_PLATNOST_OD);
			dtoMeta = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(tempMetaList, _CudConsts.NAZOV_XLS_PLATNOST_OD);
			value = validateValue(dtoMeta, s, errorSet, dtoVal.getZdroj());
			if (!errorSet.isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), null, errorSet, dtoVal.getZdroj(), true);
				return true;
			} else if (StringUtils.isValid(value)) {
				dtoVal.getImportZmenaDTO().setPlatnostOd(_CudConsts.DATE_FORMAT.parse(value));
				dtoVal.setPlatnostOd(value);
			}

			// XLS_OPERACIA
			errorSet.clear();
			s = rowMap.get(_CudConsts.NAZOV_XLS_OPERACIA);
			dtoMeta = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(tempMetaList, _CudConsts.NAZOV_XLS_OPERACIA);
			value = validateValue(dtoMeta, s, errorSet, dtoVal.getZdroj());
			if (!errorSet.isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), null, errorSet, dtoVal.getZdroj(), true);
				return true;
			} else if (!_CudConsts.ZMENA_OPERACIA_N.equals(s) && !_CudConsts.ZMENA_OPERACIA_U.equals(s) && !_CudConsts.ZMENA_OPERACIA_Z.equals(s) && !_CudConsts.ZMENA_OPERACIA_D.equals(s)) {
				addMsg(dtoVal.getErrorsMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3031), dtoVal.getZdroj(), true);
				return true;
			} else if (StringUtils.isValid(value)) {
				dtoVal.getImportZmenaDTO().setOperacia(value);
				dtoVal.setPlatnostOd(dtoVal.getPlatnostOd() + "_" + value);
			}

			// nastavia sa metadata ciselnika pre danu PLATNOST_OD a OPERACIU
			List<DTOCiselnikStlpecGui> metaList = metaMap.get(dtoVal.getPlatnostOd());
			if (!StringUtils.isValid(metaList)) {
				metaList = metaList(auth, dtoVal);
				if (!StringUtils.isValid(metaList) || metaList.isEmpty()) {
					addMsg(dtoVal.getErrorsMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_608), dtoVal.getZdroj(), true);
					return true;
				}
				metaMap.put(dtoVal.getPlatnostOd(), metaList);
			}

			DTOCiselnikStlpecGui dtoCSPK = _CudLookupUtils.lookupDTOCiselnikStlpecGuiPk(metaList);

			// ROW_ID
			errorSet.clear();
			s = rowMap.get(dtoCSPK.getCiselnikStlpecNazov());
			if (!StringUtils.isValid(s)) {
				s = rowMap.get(_CudConsts.NAZOV_ROW_ID);
			}
			value = validateValue(dtoCSPK, s, errorSet, dtoVal.getZdroj());
			if (!errorSet.isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), dtoCSPK.getCiselnikStlpecGuiID(), errorSet, dtoVal.getZdroj(), true);
				return true;
			} else if (StringUtils.isValid(value)) {
				dtoVal.getImportZmenaDTO().setRowID(new Integer(value));
				dtoVal.getNewValueMap().put(dtoCSPK.getCiselnikStlpecNazov(), value);
				rowMap.put(dtoCSPK.getCiselnikStlpecNazov(), value);
			}

			// CAS_SCHVALENIA_GR
			errorSet.clear();
			s = rowMap.get(_CudConsts.NAZOV_XLS_CAS_SCHVALENIA_GR);
			dtoMeta = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(tempMetaList, _CudConsts.NAZOV_XLS_CAS_SCHVALENIA_GR);
			value = validateValue(dtoMeta, s, errorSet, dtoVal.getZdroj());
			if (!errorSet.isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), null, errorSet, dtoVal.getZdroj(), true);
			} else if (StringUtils.isValid(value)) {
				dtoVal.getImportZmenaDTO().setCasSchvaleniaGr(_CudConsts.DATE_FORMAT.parse(value));
			}

			// POZNAMKA
			errorSet.clear();
			s = rowMap.get(_CudConsts.NAZOV_XLS_POZNAMKA);
			dtoMeta = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(tempMetaList, _CudConsts.NAZOV_XLS_POZNAMKA);
			value = validateValue(dtoMeta, s, errorSet, dtoVal.getZdroj());
			if (!errorSet.isEmpty()) {
				addMsg(dtoVal.getErrorsMap(), null, errorSet, dtoVal.getZdroj(), true);
			} else if (StringUtils.isValid(value)) {
				dtoVal.getImportZmenaDTO().setPoznamka(value);
			}

			// kontrola definicie
			s = rowMap.get(_CudConsts.IMPORT_KONTROLA_DEF);
			if ("F".equals(s)) {
				addMsg(dtoVal.getErrorsMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3032), dtoVal.getZdroj(), true);
			}

			for (DTOCiselnikStlpecGui dtoCS : metaList) {

				if (!"T".equals(dtoCS.getFormZobrazenie())) {
					continue;
				}

				if (dtoCSPK.getCiselnikStlpecNazov().equals(dtoCS.getCiselnikStlpecNazov())) {
					continue;
				}

				errorSet.clear();

				if (!rowMap.keySet().contains(dtoCS.getCiselnikStlpecNazov())) {

					jeAtributPovinny(dtoCS, null, errorSet, dtoVal.getZdroj(), dtoVal.getImportZmenaDTO().getOperacia(), false);
					if (!errorSet.isEmpty()) {
						addMsg(dtoVal.getErrorsMap(), dtoCS.getCiselnikStlpecGuiID(), errorSet, dtoVal.getZdroj(), true);
					}

					continue;
				}

				s = rowMap.get(dtoCS.getCiselnikStlpecNazov());
				value = validateValue(dtoCS, s, errorSet, dtoVal.getZdroj());
				validateSpecialMetaValuea(dtoCS, value, errorSet, dtoVal.getCiselnikTabulka());

				if ("F".equals(dtoCS.getZmena()) && !jeAtributEditovatelny(metaList, dtoCS.getCiselnikStlpecNazov())) {
					if (StringUtils.isValid(s)) {
						String colName = _CudConsts.ZDROJ_FORM.equals(dtoVal.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getCiselnikStlpecNazov();
						addMsg(dtoVal.getWarningMap(), dtoCS.getCiselnikStlpecGuiID(), _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_302, colName), dtoVal.getZdroj(), false);
					}
					continue;
				}

				jeAtributPovinny(dtoCS, value, errorSet, dtoVal.getZdroj(), dtoVal.getImportZmenaDTO().getOperacia(), true);

				if (!errorSet.isEmpty()) {
					addMsg(dtoVal.getErrorsMap(), dtoCS.getCiselnikStlpecGuiID(), errorSet, dtoVal.getZdroj(), true);
					continue;
				}

				if (StringUtils.isValid(value)) {
					dtoVal.getNewValueMap().put(dtoCS.getCiselnikStlpecNazov(), value);
				} else if (rowMap.keySet().contains(dtoCS.getCiselnikStlpecNazov())) {
					dtoVal.getNewValueMap().put(dtoCS.getCiselnikStlpecNazov(), null);
				}
			}

			return !dtoVal.getErrorsMap().keySet().isEmpty();

		} catch (Throwable t) {
			DBUtils.handleException(t, "parseData.error");
			return false;
		}
	}

	private boolean jeAtributTechnickyAtribut(Map<String, String> valueMap) throws AppException {

		try {
			String s = valueMap.get(_CudConsts.NAZOV_TYP);
			if (_CudConsts.CISELNIK_STLPEC_TYP_HK.equals(s) || _CudConsts.CISELNIK_STLPEC_TYP_PK.equals(s)) {
				return true;
			}

			s = valueMap.get(_CudConsts.NAZOV_NAZOV);
			if (_CudKontrolaUtils.jeAtributTechnicky(s)) {
				return true;
			}

			return false;

		} catch (Throwable t) {
			DBUtils.handleException(t, "parseData.error");
			return false;
		}
	}

	private void validateMetaByIntegrity(AuthInfo auth, DTOValidate dtoVal, Map<String, List<DTOCiselnikStlpecGui>> metaMap, DTOSkupina[] skupinaList) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> metaList = metaMap.get(dtoVal.getPlatnostOd());

			// kontrola na platnost od
			if (!jePlatnostOdValidnaMeta(dtoVal.getImportZmenaDTO().getPlatnostOd())) {
				addMsg(dtoVal.getErrorsMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3001, _CudConsts.NAZOV_PLATNOST_OD), dtoVal.getZdroj(), true);
			}

			loadOldValues(auth, dtoVal);

			if (_CudConsts.ZMENA_OPERACIA_N.equals(dtoVal.getImportZmenaDTO().getOperacia())) {

				if (CudCiselnikPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					insertKontrolaCiselnik(auth, dtoVal);

				} else if (CudCiselnikStlpecPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					insertKontrolaCiselnikStlpec(auth, dtoVal);

				} else if (CudCiselnikGuiPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					insertKontrolaCiselnikGui(auth, dtoVal);

				} else if (CudCiselnikStlpecGuiPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					insertKontrolaCiselnikStlpecGui(auth, dtoVal);

				} else if (CudWfDefPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					insertKontrolaWfDef(auth, dtoVal, skupinaList);

				} else if (CudPrekladPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					insertKontrolaPreklad(auth, dtoVal);
				}

				if (CudCiselnikStlpecPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					if (jeAtributTechnickyAtribut(dtoVal.getNewValueMap())) {
						String colName = dtoVal.getNewValueMap().get(_CudConsts.NAZOV_NAZOV);
						addMsg(dtoVal.getWarningMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_305, colName), dtoVal.getZdroj(), false);
						return;
					}
				}

				for (DTOCiselnikStlpecGui dtoCS : metaList) {

					if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dtoCS.getCiselnikStlpecTyp())) {
						continue;
					}
					String newValue = dtoVal.getNewValueMap().get(dtoCS.getCiselnikStlpecNazov());
					if (!StringUtils.isValid(newValue)) {
						continue;
					}

					DTOImportZmenaStlpec dtoNew = new DTOImportZmenaStlpec();
					dtoNew.setCiselnikStlpecNazov(dtoCS.getCiselnikStlpecNazov());
					dtoNew.setNewValue(newValue);
					dtoVal.getImportZmenaStlpecList().add(dtoNew);
				}

			} else if (_CudConsts.ZMENA_OPERACIA_U.equals(dtoVal.getImportZmenaDTO().getOperacia())) {

				if (CudCiselnikPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					updateKontrolaCiselnik(auth, dtoVal);

				} else if (CudCiselnikStlpecPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					updateKontrolaCiselnikStlpec(auth, dtoVal);

				} else if (CudCiselnikGuiPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					updateKontrolaCiselnikGui(dtoVal);

				} else if (CudCiselnikStlpecGuiPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					updateKontrolaCiselnikStlpecGui(auth, dtoVal);

				} else if (CudWfDefPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					updateKontrolaWfDef(auth, dtoVal, skupinaList);

				} else if (CudPrekladPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					updateKontrolaPreklad(auth, dtoVal);
				}

				if (CudCiselnikStlpecPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					if (jeAtributTechnickyAtribut(dtoVal.getNewValueMap()) || jeAtributTechnickyAtribut(dtoVal.getOldValueMap())) {
						String colName = dtoVal.getOldValueMap().get(_CudConsts.NAZOV_NAZOV);
						addMsg(dtoVal.getWarningMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_305, colName), dtoVal.getZdroj(), false);
						return;
					}
				}

				for (DTOCiselnikStlpecGui dtoCS : metaList) {

					if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dtoCS.getCiselnikStlpecTyp())) {
						continue;
					}
					if (!dtoVal.getNewValueMap().keySet().contains(dtoCS.getCiselnikStlpecNazov())) {
						continue;
					}

					String newValue = dtoVal.getNewValueMap().get(dtoCS.getCiselnikStlpecNazov());
					String oldValue = dtoVal.getOldValueMap().get(dtoCS.getCiselnikStlpecNazov());

					if (jeZmenaAtributu(oldValue, newValue)) {

						DTOImportZmenaStlpec dtoNew = new DTOImportZmenaStlpec();
						dtoNew.setCiselnikStlpecNazov(dtoCS.getCiselnikStlpecNazov());
						dtoNew.setOldValue(oldValue);
						dtoNew.setNewValue(newValue);
						dtoVal.getImportZmenaStlpecList().add(dtoNew);
					}
					if (suAtributyRovnake(oldValue, newValue)) {
						addMsg(dtoVal.getWarningMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_303, dtoCS.getCiselnikStlpecNazov()), dtoVal.getZdroj(), false);
					}
				}

			} else if (_CudConsts.ZMENA_OPERACIA_Z.equals(dtoVal.getImportZmenaDTO().getOperacia())) {

				if (CudCiselnikPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					deleteKontrolaCiselnik(auth, dtoVal);

				} else if (CudCiselnikStlpecPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					deleteKontrolaCiselnikStlpec(auth, dtoVal);

				} else if (CudCiselnikGuiPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					deleteKontrolaCiselnikGui(dtoVal);

				} else if (CudCiselnikStlpecGuiPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					deleteKontrolaCiselnikStlpecGui(auth, dtoVal);

				} else if (CudWfDefPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					deleteWfDef(dtoVal);

				} else if (CudPrekladPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					deleteKontrolaPreklad(auth, dtoVal);
				}

				if (CudCiselnikStlpecPeer.TABLE_NAME.equals(dtoVal.getCiselnikTabulka())) {
					if (jeAtributTechnickyAtribut(dtoVal.getOldValueMap())) {
						String colName = dtoVal.getOldValueMap().get(_CudConsts.NAZOV_NAZOV);
						addMsg(dtoVal.getWarningMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_305, colName), dtoVal.getZdroj(), false);
						return;
					}
				}

				for (DTOCiselnikStlpecGui dtoCS : metaList) {

					if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dtoCS.getCiselnikStlpecTyp())) {
						continue;
					}
					String oldValue = dtoVal.getOldValueMap().get(dtoCS.getCiselnikStlpecNazov());
					if (!StringUtils.isValid(oldValue)) {
						continue;
					}

					DTOImportZmenaStlpec dtoNew = new DTOImportZmenaStlpec();
					dtoNew.setCiselnikStlpecNazov(dtoCS.getCiselnikStlpecNazov());
					dtoNew.setOldValue(oldValue);
					dtoVal.getImportZmenaStlpecList().add(dtoNew);
				}
			}

			if (dtoVal.getImportZmenaStlpecList().isEmpty()) {
				addMsg(dtoVal.getWarningMap(), null, _CudResultUtils.returnMsg(_CudResultUtils.WARN_CODE_304), dtoVal.getZdroj(), false);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateMetaByIntegrity.error");
		}
	}

	public void validateMeta(AuthInfo auth, DTOValidate dtoVal, Map<String, List<DTOCiselnikStlpecGui>> metaMap, Map<String, String> rowMap, DTOSkupina[] skupinaList) throws AppException {

		try {
			if (parseData(auth, dtoVal, metaMap, rowMap)) {
				lookupValues(dtoVal, metaMap);
				return;
			}
			if (!dtoVal.getErrorsMap().keySet().isEmpty()) {
				lookupValues(dtoVal, metaMap);
				return;
			}

			validateMetaByIntegrity(auth, dtoVal, metaMap, skupinaList);
			lookupValues(dtoVal, metaMap);

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateMeta.error");
		}
	}

	private Map<String, String> createRowMap(DTOValidate dtoVal) throws AppException {

		try {
			Map<String, String> resultMap = new HashMap<String, String>();

			if (StringUtils.isValid(dtoVal.getOldValueMap())) {
				for (String colName : dtoVal.getOldValueMap().keySet()) {
					resultMap.put(colName, dtoVal.getOldValueMap().get(colName));
				}
			}

			if (StringUtils.isValid(dtoVal.getNewValueMap())) {
				for (String colName : dtoVal.getNewValueMap().keySet()) {
					resultMap.put(colName, dtoVal.getNewValueMap().get(colName));
				}
			}

			if (!resultMap.keySet().contains(_CudConsts.NAZOV_PLATNOST_OD) && !resultMap.keySet().contains(_CudConsts.NAZOV_XLS_PLATNOST_OD)) {
				resultMap.put(_CudConsts.NAZOV_XLS_PLATNOST_OD, _CudConsts.DATE_FORMAT.format(dtoVal.getImportZmenaDTO().getPlatnostOd()));
			}

			return resultMap;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createRowMap.error");
			return null;
		}
	}

	private void validateByPlugin(AuthInfo auth, DTOValidate dtoVal, List<DTOCiselnikStlpec> csList, List<DTOCiselnikStlpecGui> metaList) throws AppException {

		try {
			if (_CudConsts.ZMENA_OPERACIA_Z.equals(dtoVal.getImportZmenaDTO().getOperacia()) || _CudConsts.ZMENA_OPERACIA_D.equals(dtoVal.getImportZmenaDTO().getOperacia())) {
				return;
			}

			if (!StringUtils.isValid(dtoVal.getPluginList())) {
				return;
			}

			CudCacheMap cacheMap = new CudCacheMap();
			cacheMap.addRecord(_CudConsts.NAZOV_PLG_PLATNOST_OD, dtoVal.getImportZmenaDTO().getPlatnostOd());

			for (DTOPlugin dtoPlg : dtoVal.getPluginList()) {

				dtoPlg.setIDCiselnik(dtoVal.getCiselnikID());
				dtoPlg.setCiselnikTabulka(dtoVal.getCiselnikTabulka());
				dtoPlg.setZdroj(dtoVal.getZdroj());

				IPlugin iplg = (IPlugin) Class.forName(_CudConsts.PLUGIN_PACKAGE + dtoPlg.getPluginClassNameClassName()).newInstance();
				iplg.setDelegat(getDelegate());
				DTOPluginKontrolaRow[] errList = iplg.validate(auth, dtoPlg, createRowMap(dtoVal), csList, cacheMap);
				if (StringUtils.isValid(errList)) {
					for (DTOPluginKontrolaRow dtoErr : errList) {
						DTOCiselnikStlpecGui dtoMeta = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, dtoErr.getIDCiselnikStlpec());
						Integer ciselnikStlpecGuiID = StringUtils.isValid(dtoMeta) ? dtoMeta.getCiselnikStlpecGuiID() : null;
						if (_CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR.equals(dtoErr.getStav())) {
							addMsg(dtoVal.getErrorsMap(), ciselnikStlpecGuiID, dtoErr.getPopis(), dtoPlg.getZdroj(), true);
						} else if (_CudConsts.PLUGIN_KONTROLA_ROW_STAV_WARNING.equals(dtoErr.getStav())) {
							addMsg(dtoVal.getWarningMap(), ciselnikStlpecGuiID, dtoErr.getPopis(), dtoPlg.getZdroj(), true);
						}
					}
				}
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateByPlugin.error");
		}
	}

	public void validateMaster(AuthInfo auth, DTOValidate dtoVal, Map<String, List<DTOCiselnikStlpecGui>> metaMap, Map<String, String> rowMap, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			if (parseData(auth, dtoVal, metaMap, rowMap)) {
				lookupValues(dtoVal, metaMap);
				return;
			}

			validateMasterByIntegrity(auth, metaMap, dtoVal, csList);
			if (!dtoVal.getErrorsMap().keySet().isEmpty()) {
				lookupValues(dtoVal, metaMap);
				return;
			}

			validateByPlugin(auth, dtoVal, csList, metaMap.get(dtoVal.getPlatnostOd()));
			lookupValues(dtoVal, metaMap);

		} catch (Throwable t) {
			DBUtils.handleException(t, "validateMaster.error");
		}
	}
}
