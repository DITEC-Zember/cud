package sk.ditec.cud.plugin;

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

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynCiselnik;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.cud.dto.DTOPluginStlpec;
import sk.ditec.cud.utils.CudCacheMap;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;

public class CudPluginLokalitaDbClass extends _CudBasePluginClass implements IPlugin {

	private String ALIAS_SUBSIDIARY_TYPE = "SUBSIDIARY_TYPE";
	private String ALIAS_CISLO = "CISLO";
	private String ALIAS_NADRADENA_PRIMARNA = "NADRADENA_PRIMARNA";
	private String ALIAS_COMPANY = "COMPANY";
	private String ALIAS_CRD_ZAC = "CRD_ZAC";
	private String ALIAS_CRD_KON = "CRD_KON";
	private String ALIAS_OTVORENY_PRE_OD = "OTVORENY_PRE_OD";
	private String ALIAS_OTVORENY_PRE_ND = "OTVORENY_PRE_ND";
	private String ALIAS_OTVORENY_PRE_OD_ZAC = "OTVORENY_PRE_OD_ZAC";
	private String ALIAS_OTVORENY_PRE_OD_KON = "OTVORENY_PRE_OD_KON";
	private String ALIAS_OTVORENY_PRE_ND_ZAC = "OTVORENY_PRE_ND_ZAC";
	private String ALIAS_OTVORENY_PRE_ND_KON = "OTVORENY_PRE_ND_KON";

	@Override
	public String updateKontrola(AuthInfo auth, DTOPluginStlpec[] pluginStlpecList, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			String s = kontrolaAlias(pluginStlpecList, ALIAS_SUBSIDIARY_TYPE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_CISLO);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_NADRADENA_PRIMARNA);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_COMPANY);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_CRD_ZAC);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_CRD_KON);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_OTVORENY_PRE_OD);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_OTVORENY_PRE_ND);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_OTVORENY_PRE_OD_ZAC);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_OTVORENY_PRE_OD_KON);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_OTVORENY_PRE_ND_ZAC);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_OTVORENY_PRE_ND_KON);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_SUBSIDIARY_TYPE, csList, _CudConsts.DB_TYP_INTEGER);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_CISLO, csList, _CudConsts.DB_TYP_STRING);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_NADRADENA_PRIMARNA, csList, _CudConsts.DB_TYP_INTEGER);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_COMPANY, csList, _CudConsts.DB_TYP_INTEGER);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_CRD_ZAC, csList, _CudConsts.DB_TYP_DATE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_CRD_KON, csList, _CudConsts.DB_TYP_DATE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_OTVORENY_PRE_OD, csList, _CudConsts.DB_TYP_BOOLEAN);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_OTVORENY_PRE_ND, csList, _CudConsts.DB_TYP_BOOLEAN);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_OTVORENY_PRE_OD_ZAC, csList, _CudConsts.DB_TYP_DATE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_OTVORENY_PRE_OD_KON, csList, _CudConsts.DB_TYP_DATE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_OTVORENY_PRE_ND_ZAC, csList, _CudConsts.DB_TYP_DATE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_OTVORENY_PRE_ND_KON, csList, _CudConsts.DB_TYP_DATE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrola.error");
			return null;
		}
	}

	private boolean existujeZaznamNaZmenuDopravnyBod(AuthInfo auth, Integer IDNadradenaPrimarna, Date platnostOd) throws AppException {

		try {
			String str = _CudConsts.DATE_FORMAT.format(platnostOd);
			String con1 = "( " + _CudConsts.NAZOV_PLATNOST_OD + " <= " + _CudConsts.NAZOV_PLATNOST_DO + " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL )";
			String con2 = "((" + _CudConsts.NAZOV_PLATNOST_DO + " >= to_timestamp(\'" + str + "\', \'DD.MM.YYYY\')) OR (" + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL))";
			String con3 = _CudConsts.NAZOV_ZMAZ + "= \'F\'";
			String con4 = _CudConsts.NAZOV_ID_NADRADENA_PRIMARNA + " = " + IDNadradenaPrimarna;
			String where = con1 + " AND " + con2 + " AND " + con3 + " AND " + con4;

			String sql = "SELECT " + _CudConsts.NAZOV_DOPRAVNY_BOD_ID + " FROM " + _CudConsts.TABULKA_T_DOPRAVNY_BOD + " WHERE " + where;

			return getDelegate().getZmenaRead().existujeZaznam(auth, _CudConsts.TABULKA_T_DOPRAVNY_BOD, sql, new String[] { _CudConsts.ZMENA_STAV_VPO, _CudConsts.ZMENA_STAV_SCH });

		} catch (Throwable t) {
			DBUtils.handleException(t, "existujeZaznamNaZmenuDopravnyBod.error");
			return false;
		}
	}

	private boolean existujeZaznamNaZmenuStanicnaKolaj(AuthInfo auth, Integer IDDopravnyBod, Date platnostOd) throws AppException {

		try {
			String str = _CudConsts.DATE_FORMAT.format(platnostOd);
			String con1 = "( " + _CudConsts.NAZOV_PLATNOST_OD + " <= " + _CudConsts.NAZOV_PLATNOST_DO + " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL )";
			String con2 = "((" + _CudConsts.NAZOV_PLATNOST_DO + " >= to_timestamp(\'" + str + "\', \'DD.MM.YYYY\')) OR (" + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL))";
			String con3 = _CudConsts.NAZOV_ZMAZ + "= \'F\'";
			String con4 = _CudConsts.NAZOV_ID_DOPRAVNY_BOD + " = " + IDDopravnyBod;
			String where = con1 + " AND " + con2 + " AND " + con3 + " AND " + con4;

			String sql = "SELECT " + _CudConsts.NAZOV_STANICNA_KOLAJ_ID + " FROM " + _CudConsts.TABULKA_T_STANICNA_KOLAJ + " WHERE " + where;

			return getDelegate().getZmenaRead().existujeZaznam(auth, _CudConsts.TABULKA_T_STANICNA_KOLAJ, sql, new String[] { _CudConsts.ZMENA_STAV_VPO, _CudConsts.ZMENA_STAV_SCH });

		} catch (Throwable t) {
			DBUtils.handleException(t, "existujeZaznamNaZmenuStanicnaKolaj.error");
			return false;
		}
	}

	@Override
	public DTOPluginKontrolaRow[] validate(AuthInfo auth, DTOPlugin dtoPlg, Map<String, String> rowMap, List<DTOCiselnikStlpec> csList, CudCacheMap lookupMap) throws AppException {

		try {
			String s = updateKontrola(auth, dtoPlg.getPluginStlpecList(), csList);
			if (StringUtils.isValid(s)) {
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, s, null) };
			}

			String valSubsidiaryType = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_SUBSIDIARY_TYPE);
			String valNadradenaPrimarna = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_NADRADENA_PRIMARNA);
			String valCompany = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_COMPANY);
			String valCrdZac = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_CRD_ZAC);
			String valCrdKon = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_CRD_KON);
			String valOtvorenyPreOd = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_OTVORENY_PRE_OD);
			String valOtvorenyPreNd = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_OTVORENY_PRE_ND);
			String valOtvorenyPreOdZac = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_OTVORENY_PRE_OD_ZAC);
			String valOtvorenyPreOdKon = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_OTVORENY_PRE_OD_KON);
			String valOtvorenyPreNdZac = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_OTVORENY_PRE_ND_ZAC);
			String valOtvorenyPreNdKon = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_OTVORENY_PRE_ND_KON);

			Date planPlatnostOd = lookupMap.getRecord(_CudConsts.NAZOV_PLG_PLATNOST_OD, Date.class);
			Date planPlatnostDo = lookupMap.getRecord(_CudConsts.NAZOV_PLG_PLATNOST_DO, Date.class);

			DTOCiselnikStlpec dtoCSPK = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList);
			Integer pkValue = StringUtils.isValid(rowMap.get(dtoCSPK.getNazov())) ? Integer.valueOf(rowMap.get(dtoCSPK.getNazov())) : null;

			Map<String, String> oldRowMap = new HashMap<String, String>();
			if (StringUtils.isValid(pkValue)) {
				oldRowMap = getDelegate().getDynCiselnikRead().readLight(auth, dtoPlg.getCiselnikTabulka(), csList, dtoCSPK.getNazov(), Integer.toString(pkValue), _CudConsts.DB_TYP_INTEGER, planPlatnostOd, "F");
			}

			// kontrola identifikacie
			if (!oldRowMap.keySet().isEmpty() && StringUtils.isValid(lookupValue(dtoPlg.getPluginStlpecList(), oldRowMap, ALIAS_CRD_ZAC))) {

				String newValue = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_SUBSIDIARY_TYPE);
				String oldValue = lookupValue(dtoPlg.getPluginStlpecList(), oldRowMap, ALIAS_SUBSIDIARY_TYPE);
				if (!_CudKontrolaUtils.equals(newValue, oldValue)) {
					DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_SUBSIDIARY_TYPE);
					String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3084, col);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
				}

				newValue = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_CISLO);
				oldValue = lookupValue(dtoPlg.getPluginStlpecList(), oldRowMap, ALIAS_CISLO);
				if (!_CudKontrolaUtils.equals(newValue, oldValue)) {
					DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CISLO);
					String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3084, col);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
				}

				newValue = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_NADRADENA_PRIMARNA);
				oldValue = lookupValue(dtoPlg.getPluginStlpecList(), oldRowMap, ALIAS_NADRADENA_PRIMARNA);
				if (!_CudKontrolaUtils.equals(newValue, oldValue)) {
					DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA);
					String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3084, col);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
				}

				newValue = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_COMPANY);
				oldValue = lookupValue(dtoPlg.getPluginStlpecList(), oldRowMap, ALIAS_COMPANY);
				if (!_CudKontrolaUtils.equals(newValue, oldValue)) {
					DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_COMPANY);
					String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3084, col);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
				}
			}

			// kontrola OTVORENY_PRE_OD
			if (StringUtils.isValid(valOtvorenyPreOdZac)) {
				if (!"T".equals(valOtvorenyPreOd)) {
					DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_OD_ZAC);
					String col1 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_OD);
					String col2 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3085, col1, col2);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
				}
				if (StringUtils.isValid(valOtvorenyPreOdKon)) {
					if (_CudConsts.DATE_FORMAT.parse(valOtvorenyPreOdZac).after(_CudConsts.DATE_FORMAT.parse(valOtvorenyPreOdKon)) || _CudConsts.DATE_FORMAT.parse(valOtvorenyPreOdZac).equals(_CudConsts.DATE_FORMAT.parse(valOtvorenyPreOdKon))) {
						DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_OD_ZAC);
						String col1 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
						dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_OD_KON);
						String col2 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3086, col2, col1);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
					}
				}

			} else if (StringUtils.isValid(valOtvorenyPreOdKon)) {
				DTOCiselnikStlpec dtoCS1 = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_OD_ZAC);
				String col1 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS1.getNadpis() : dtoCS1.getNazov();
				DTOCiselnikStlpec dtoCS2 = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_OD_KON);
				String col2 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS2.getNadpis() : dtoCS2.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3087, col2, col1);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS1.getCiselnikStlpecID()) };
			}

			// kontrola OTVORENY_PRE_ND
			if (StringUtils.isValid(valOtvorenyPreNdZac)) {
				if (!"T".equals(valOtvorenyPreNd)) {
					DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_ND_ZAC);
					String col1 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_ND);
					String col2 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3085, col1, col2);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
				}
				if (StringUtils.isValid(valOtvorenyPreNdKon)) {
					if (_CudConsts.DATE_FORMAT.parse(valOtvorenyPreNdZac).after(_CudConsts.DATE_FORMAT.parse(valOtvorenyPreNdKon)) || _CudConsts.DATE_FORMAT.parse(valOtvorenyPreNdZac).equals(_CudConsts.DATE_FORMAT.parse(valOtvorenyPreNdKon))) {
						DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_ND_ZAC);
						String col1 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
						dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_ND_KON);
						String col2 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3086, col2, col1);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
					}
				}

			} else if (StringUtils.isValid(valOtvorenyPreNdKon)) {
				DTOCiselnikStlpec dtoCS1 = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_ND_ZAC);
				String col1 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS1.getNadpis() : dtoCS1.getNazov();
				DTOCiselnikStlpec dtoCS2 = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_OTVORENY_PRE_ND_KON);
				String col2 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS2.getNadpis() : dtoCS2.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3087, col2, col1);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS1.getCiselnikStlpecID()) };
			}

			// kontrola CRD
			if (StringUtils.isValid(valCrdZac)) {
				if (StringUtils.isValid(valCrdKon)) {
					if (_CudConsts.DATE_FORMAT.parse(valCrdZac).after(_CudConsts.DATE_FORMAT.parse(valCrdKon)) || _CudConsts.DATE_FORMAT.parse(valCrdZac).equals(_CudConsts.DATE_FORMAT.parse(valCrdKon))) {
						DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_ZAC);
						String col1 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
						dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_KON);
						String col2 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3086, col2, col1);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
					}
				}

			} else if (StringUtils.isValid(valCrdKon)) {
				DTOCiselnikStlpec dtoCS1 = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_ZAC);
				String col1 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS1.getNadpis() : dtoCS1.getNazov();
				DTOCiselnikStlpec dtoCS2 = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_KON);
				String col2 = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS2.getNadpis() : dtoCS2.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3087, col2, col1);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS1.getCiselnikStlpecID()) };
			}

			// kontrola CRD datumov
			if (StringUtils.isValid(valCrdZac)) {

				String valOldCrdZac = lookupValue(dtoPlg.getPluginStlpecList(), oldRowMap, ALIAS_CRD_ZAC);
				if (StringUtils.isValid(valOldCrdZac)) {

					if (!_CudConsts.DATE_FORMAT.parse(valOldCrdZac).equals(_CudConsts.DATE_FORMAT.parse(valCrdZac))) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3106);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_ZAC).getCiselnikStlpecID()) };
					}

				} else {

					if (!planPlatnostOd.equals(_CudConsts.DATE_FORMAT.parse(valCrdZac))) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3106);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_ZAC).getCiselnikStlpecID()) };
					}
				}

				String valOldCrdKon = lookupValue(dtoPlg.getPluginStlpecList(), oldRowMap, ALIAS_CRD_KON);
				if (StringUtils.isValid(valOldCrdKon)) {

					if (StringUtils.isValid(valCrdKon) && !_CudConsts.DATE_FORMAT.parse(valOldCrdKon).equals(_CudConsts.DATE_FORMAT.parse(valCrdKon))) {
						if (_CudConsts.DATE_FORMAT.parse(valCrdKon).before(planPlatnostOd) || _CudConsts.DATE_FORMAT.parse(valOldCrdKon).before(planPlatnostOd)) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3107);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_KON).getCiselnikStlpecID()) };
						}
					}

				} else {
					if (StringUtils.isValid(valCrdKon) && _CudConsts.DATE_FORMAT.parse(valCrdKon).before(planPlatnostOd)) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3107);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_KON).getCiselnikStlpecID()) };
					}
				}

			} else {

				String valOldCrdZac = lookupValue(dtoPlg.getPluginStlpecList(), oldRowMap, ALIAS_CRD_ZAC);
				if (StringUtils.isValid(valOldCrdZac)) {
					Calendar cal = Calendar.getInstance(new Locale("sk_SK"));
					cal.setTime(planPlatnostOd);
					cal.add(Calendar.DAY_OF_YEAR, -1);
					if (cal.getTime().equals(_CudConsts.DATE_FORMAT.parse(valOldCrdZac))) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3117);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_ZAC).getCiselnikStlpecID()) };
					}
				}
			}

			// kontrola na spolocnost
			if (!StringUtils.isValid(valCompany)) {
				DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_COMPANY);
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			Map<String, String> companyRowMap = dynCiselnikRead(auth, lookupMap, _CudConsts.TABULKA_T_COMPANY, rowMap.get(_CudConsts.NAZOV_ID_COMPANY));
			if (StringUtils.isValid(valCrdZac) && !_CudConsts.COMPANY_UIC_CODE_ZSR.equals(companyRowMap.get(_CudConsts.NAZOV_COMPANY_UIC_CODE))) {
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3088);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_COMPANY).getCiselnikStlpecID()) };
			}

			if (StringUtils.isValid(valCrdZac)) {

				Date startCrd = _CudKontrolaUtils.biggest(planPlatnostOd, _CudConsts.DATE_FORMAT.parse(valCrdZac));

				String valStartValidity = companyRowMap.get(_CudConsts.NAZOV_START_VALIDITY);
				if (StringUtils.isValid(valStartValidity) && startCrd.before(_CudConsts.DATE_FORMAT.parse(valStartValidity)) && !startCrd.equals(_CudConsts.DATE_FORMAT.parse(valStartValidity))) {
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3105);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_COMPANY).getCiselnikStlpecID()) };
				}

				String valEndValidity = companyRowMap.get(_CudConsts.NAZOV_END_VALIDITY);
				if (StringUtils.isValid(valEndValidity) && startCrd.after(_CudConsts.DATE_FORMAT.parse(valEndValidity)) && !startCrd.equals(_CudConsts.DATE_FORMAT.parse(valEndValidity))) {
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3105);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_COMPANY).getCiselnikStlpecID()) };
				}
			}

			// kontrola na typ lokality
			if (!StringUtils.isValid(valNadradenaPrimarna) && StringUtils.isValid(valSubsidiaryType)) {
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3075);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_SUBSIDIARY_TYPE).getCiselnikStlpecID()) };
			}
			if (StringUtils.isValid(valNadradenaPrimarna) && !StringUtils.isValid(valSubsidiaryType)) {
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3076);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_SUBSIDIARY_TYPE).getCiselnikStlpecID()) };
			}

			// kontrola subsidiarnej lokality
			if (StringUtils.isValid(valNadradenaPrimarna)) {

				Map<Integer, List<Map<Integer, Set<String>>>> filterMap = new HashMap<Integer, List<Map<Integer, Set<String>>>>();

				filterMap.put(0, new ArrayList<Map<Integer, Set<String>>>());
				filterMap.get(0).add(new HashMap<Integer, Set<String>>());
				filterMap.get(0).get(0).put(dtoCSPK.getCiselnikStlpecID(), new HashSet<String>());
				filterMap.get(0).get(0).get(dtoCSPK.getCiselnikStlpecID()).add(valNadradenaPrimarna);

				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(csList, _CudConsts.NAZOV_ZMAZ);
				filterMap.put(1, new ArrayList<Map<Integer, Set<String>>>());
				filterMap.get(1).add(new HashMap<Integer, Set<String>>());
				filterMap.get(1).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
				filterMap.get(1).get(0).get(dtoCS.getCiselnikStlpecID()).add("F");

				List<Map<String, String>> futRowList = getDelegate().getDynCiselnikRead().futListLight(auth, dtoPlg.getCiselnikTabulka(), csList, filterMap, planPlatnostOd, planPlatnostDo, new Page(true));

				for (Map<String, String> futRowMap : futRowList) {

					if (StringUtils.isValid(futRowMap.get(lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_SUBSIDIARY_TYPE).getNazov()))) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3078);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
					}

					if (StringUtils.isValid(valCrdZac)) {

						String valFutCrdZac = lookupValue(dtoPlg.getPluginStlpecList(), futRowMap, ALIAS_CRD_ZAC);
						if (!StringUtils.isValid(valFutCrdZac)) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3089);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
						}
						if (_CudConsts.DATE_FORMAT.parse(valFutCrdZac).after(_CudConsts.DATE_FORMAT.parse(valCrdZac)) && !_CudConsts.DATE_FORMAT.parse(valFutCrdZac).equals(_CudConsts.DATE_FORMAT.parse(valCrdZac))) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3090);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
						}
						String valFutCrdKon = lookupValue(dtoPlg.getPluginStlpecList(), futRowMap, ALIAS_CRD_KON);
						if (StringUtils.isValid(valFutCrdKon) && !StringUtils.isValid(valCrdKon)) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3091);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
						}
						if (StringUtils.isValid(valFutCrdKon)) {
							if (_CudConsts.DATE_FORMAT.parse(valFutCrdKon).before(_CudConsts.DATE_FORMAT.parse(valCrdZac)) && !_CudConsts.DATE_FORMAT.parse(valFutCrdKon).equals(_CudConsts.DATE_FORMAT.parse(valCrdZac))) {
								String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3092);
								return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
							}
							if (StringUtils.isValid(valCrdKon)) {
								if (_CudConsts.DATE_FORMAT.parse(valFutCrdKon).before(_CudConsts.DATE_FORMAT.parse(valCrdKon)) && !_CudConsts.DATE_FORMAT.parse(valFutCrdKon).equals(_CudConsts.DATE_FORMAT.parse(valCrdKon))) {
									String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3093);
									return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
								}
							}
						}
					}
				}

				if (StringUtils.isValid(valCrdZac)) {

					if (getDelegate().getWfTodoRead().count(auth, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getFk1IDCiselnik(), Integer.valueOf(valNadradenaPrimarna), null).intValue() != 0) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3094);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
					}

					// kontrola T_COMPANY
					String key = _CudConsts.TABULKA_T_COMPANY + "_ROW_DATA";
					companyRowMap = lookupMap.getMap(key, String.class);
					if (!StringUtils.isValid(companyRowMap)) {
						List<DTOCiselnikStlpec> pomMetaList = lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_COMPANY);
						DTOCiselnikStlpec dtoPomCS = _CudLookupUtils.lookupDTOCiselnikStlpec(pomMetaList, _CudConsts.NAZOV_COMPANY_UIC_CODE);
						companyRowMap = getDelegate().getDynCiselnikRead().readLight(auth, _CudConsts.TABULKA_T_COMPANY, pomMetaList, dtoPomCS.getNazov(), _CudConsts.COMPANY_UIC_CODE_ZSR, dtoPomCS.getDbTyp(), planPlatnostOd, "F");
						lookupMap.addRecord(key, companyRowMap);
					}
					if (companyRowMap.isEmpty()) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3108, _CudConsts.COMPANY_UIC_CODE_ZSR);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
					}

					Date startCrd = _CudKontrolaUtils.biggest(planPlatnostOd, _CudConsts.DATE_FORMAT.parse(valCrdZac));

					String valStartValidity = companyRowMap.get(_CudConsts.NAZOV_START_VALIDITY);
					if (!StringUtils.isValid(valStartValidity) || (startCrd.before(_CudConsts.DATE_FORMAT.parse(valStartValidity)) && !startCrd.equals(_CudConsts.DATE_FORMAT.parse(valStartValidity)))) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3108, _CudConsts.COMPANY_UIC_CODE_ZSR);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
					}

					String valEndValidity = companyRowMap.get(_CudConsts.NAZOV_END_VALIDITY);
					if (StringUtils.isValid(valEndValidity) && _CudConsts.DATE_FORMAT.parse(valEndValidity).before(startCrd) && !_CudConsts.DATE_FORMAT.parse(valEndValidity).equals(startCrd)) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3108, _CudConsts.COMPANY_UIC_CODE_ZSR);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
					}

					// kontrola T_COUNTRY
					key = _CudConsts.TABULKA_T_COUNTRY + "_ROW_DATA";
					Map<String, String> countryRowMap = lookupMap.getMap(key, String.class);
					if (!StringUtils.isValid(countryRowMap)) {
						List<DTOCiselnikStlpec> pomMetaList = lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_COUNTRY);
						DTOCiselnikStlpec dtoPomCS = _CudLookupUtils.lookupDTOCiselnikStlpec(pomMetaList, _CudConsts.NAZOV_COUNTRY_CODE_ISO);
						countryRowMap = getDelegate().getDynCiselnikRead().readLight(auth, _CudConsts.TABULKA_T_COUNTRY, pomMetaList, dtoPomCS.getNazov(), _CudConsts.COUNTRY_CODE_ISO_SK, dtoPomCS.getDbTyp(), planPlatnostOd, "F");
						lookupMap.addRecord(key, countryRowMap);
					}
					if (countryRowMap.isEmpty()) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3109, _CudConsts.COUNTRY_CODE_ISO_SK);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
					}

					// kontrola na T_SUBSIDIARY_TYPE
					Map<String, String> subsidiaryTypeRowMap = dynCiselnikRead(auth, lookupMap, _CudConsts.TABULKA_T_SUBSIDIARY_TYPE, rowMap.get(_CudConsts.NAZOV_ID_SUBSIDIARY_TYPE));
					if (!new HashSet<String>(Arrays.asList(_CudConsts.VALUES_SUBSIDIARY_TYPE_CODE)).contains(subsidiaryTypeRowMap.get(_CudConsts.NAZOV_SUBSIDIARY_TYPE_CODE))) {
						dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_SUBSIDIARY_TYPE);
						String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3113, col);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
					}

					boolean b = true;

					if ("T".equals(companyRowMap.get(_CudConsts.NAZOV_CE_ENTITY_FLAG)) && "T".equals(subsidiaryTypeRowMap.get(_CudConsts.NAZOV_CENTRAL_ENTITY_FLAG))) {
						b = false;
					} else if ("T".equals(companyRowMap.get(_CudConsts.NAZOV_NE_ENTITY_FLAG)) && "T".equals(subsidiaryTypeRowMap.get(_CudConsts.NAZOV_NATIONAL_ENTITY_FLAG)) && countryRowMap.get(_CudConsts.NAZOV_COUNTRY_ID).equals(companyRowMap.get(_CudConsts.NAZOV_ID_COUNTRY))) {
						b = false;
					} else if ("T".equals(companyRowMap.get(_CudConsts.NAZOV_INFRASTRUCTURE_FLAG)) && "T".equals(subsidiaryTypeRowMap.get(_CudConsts.NAZOV_IM_FLAG)) && "T".equals(countryRowMap.get(_CudConsts.NAZOV_SUB_LOC_CODE_FLAG))) {
						b = false;
					} else if ("T".equals(companyRowMap.get(_CudConsts.NAZOV_FREIGHT_FLAG)) && "T".equals(subsidiaryTypeRowMap.get(_CudConsts.NAZOV_FREIGHT_RU_FLAG)) && "T".equals(countryRowMap.get(_CudConsts.NAZOV_SUB_LOC_CODE_FLAG))) {
						b = false;
					} else if ("T".equals(companyRowMap.get(_CudConsts.NAZOV_PASSENGER_FLAG)) && "T".equals(subsidiaryTypeRowMap.get(_CudConsts.NAZOV_PASSENGER_RU_FLAG)) && "T".equals(countryRowMap.get(_CudConsts.NAZOV_SUB_LOC_CODE_FLAG))) {
						b = false;
					} else if ("T".equals(companyRowMap.get(_CudConsts.NAZOV_OTHER_COMPANY_FLAG)) && "T".equals(subsidiaryTypeRowMap.get(_CudConsts.NAZOV_OTHERS_FLAG)) && "T".equals(countryRowMap.get(_CudConsts.NAZOV_SUB_LOC_CODE_FLAG))) {
						b = false;
					}

					if (b) {
						dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_SUBSIDIARY_TYPE);
						String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3113, col);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
					}
				}

				if (StringUtils.isValid(pkValue)) {

					filterMap = new HashMap<Integer, List<Map<Integer, Set<String>>>>();

					dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA);
					filterMap.put(0, new ArrayList<Map<Integer, Set<String>>>());
					filterMap.get(0).add(new HashMap<Integer, Set<String>>());
					filterMap.get(0).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
					filterMap.get(0).get(0).get(dtoCS.getCiselnikStlpecID()).add(pkValue.toString());

					dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(csList, _CudConsts.NAZOV_ZMAZ);
					filterMap.put(1, new ArrayList<Map<Integer, Set<String>>>());
					filterMap.get(1).add(new HashMap<Integer, Set<String>>());
					filterMap.get(1).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
					filterMap.get(1).get(0).get(dtoCS.getCiselnikStlpecID()).add("F");

					List<Map<String, String>> podRowList = getDelegate().getDynCiselnikRead().futListLight(auth, dtoPlg.getCiselnikTabulka(), csList, filterMap, planPlatnostOd, planPlatnostDo, new Page(true));
					if (!podRowList.isEmpty()) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3079);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
					}
				}
			}

			// kontrola primarnej lokality
			if (!StringUtils.isValid(valNadradenaPrimarna)) {

				List<Map<String, String>> futRowList = new ArrayList<Map<String, String>>();

				if (StringUtils.isValid(pkValue)) {

					List<DTOCiselnikStlpec> metaList = lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_DOPRAVNY_BOD);

					Map<Integer, List<Map<Integer, Set<String>>>> filterMap = new HashMap<Integer, List<Map<Integer, Set<String>>>>();

					DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(metaList, _CudConsts.NAZOV_ID_NADRADENA_PRIMARNA);
					filterMap.put(0, new ArrayList<Map<Integer, Set<String>>>());
					filterMap.get(0).add(new HashMap<Integer, Set<String>>());
					filterMap.get(0).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
					filterMap.get(0).get(0).get(dtoCS.getCiselnikStlpecID()).add(pkValue.toString());

					dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(metaList, _CudConsts.NAZOV_ZMAZ);
					filterMap.put(1, new ArrayList<Map<Integer, Set<String>>>());
					filterMap.get(1).add(new HashMap<Integer, Set<String>>());
					filterMap.get(1).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
					filterMap.get(1).get(0).get(dtoCS.getCiselnikStlpecID()).add("F");

					List<Map<String, String>> pomRowList = getDelegate().getDynCiselnikRead().futListLight(auth, _CudConsts.TABULKA_T_DOPRAVNY_BOD, metaList, filterMap, planPlatnostOd, planPlatnostDo, new Page(true));
					if (!pomRowList.isEmpty()) {
						futRowList.addAll(pomRowList);
					}

					metaList = lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_STANICNA_KOLAJ);

					filterMap = new HashMap<Integer, List<Map<Integer, Set<String>>>>();

					dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(metaList, _CudConsts.NAZOV_ID_DOPRAVNY_BOD);
					filterMap.put(0, new ArrayList<Map<Integer, Set<String>>>());
					filterMap.get(0).add(new HashMap<Integer, Set<String>>());
					filterMap.get(0).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
					filterMap.get(0).get(0).get(dtoCS.getCiselnikStlpecID()).add(pkValue.toString());

					dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(metaList, _CudConsts.NAZOV_ZMAZ);
					filterMap.put(1, new ArrayList<Map<Integer, Set<String>>>());
					filterMap.get(1).add(new HashMap<Integer, Set<String>>());
					filterMap.get(1).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
					filterMap.get(1).get(0).get(dtoCS.getCiselnikStlpecID()).add("F");

					pomRowList = getDelegate().getDynCiselnikRead().futListLight(auth, _CudConsts.TABULKA_T_STANICNA_KOLAJ, metaList, filterMap, planPlatnostOd, planPlatnostDo, new Page(true));
					if (!pomRowList.isEmpty()) {
						futRowList.addAll(pomRowList);
					}
				}

				if (StringUtils.isValid(valCrdZac)) {

					for (Map<String, String> futRowMap : futRowList) {

						String valFutCrdZac = futRowMap.get(_CudConsts.NAZOV_CRD_ZAC);
						if (!StringUtils.isValid(valFutCrdZac)) {
							continue;
						}

						if (_CudConsts.DATE_FORMAT.parse(valFutCrdZac).before(_CudConsts.DATE_FORMAT.parse(valCrdZac)) && !_CudConsts.DATE_FORMAT.parse(valFutCrdZac).equals(_CudConsts.DATE_FORMAT.parse(valCrdZac))) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3095);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
						}

						String valFutCrdKon = futRowMap.get(_CudConsts.NAZOV_CRD_KON);
						if (!StringUtils.isValid(valFutCrdKon) && StringUtils.isValid(valCrdKon)) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3096);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
						}

						if (StringUtils.isValid(valFutCrdKon)) {
							if (_CudConsts.DATE_FORMAT.parse(valFutCrdKon).before(_CudConsts.DATE_FORMAT.parse(valCrdZac)) && !_CudConsts.DATE_FORMAT.parse(valFutCrdKon).equals(_CudConsts.DATE_FORMAT.parse(valCrdZac))) {
								String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3097);
								return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
							}
							if (StringUtils.isValid(valCrdKon)) {
								if (_CudConsts.DATE_FORMAT.parse(valFutCrdKon).after(_CudConsts.DATE_FORMAT.parse(valCrdKon)) && !_CudConsts.DATE_FORMAT.parse(valFutCrdKon).equals(_CudConsts.DATE_FORMAT.parse(valCrdKon))) {
									String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3098);
									return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
								}
							}
						}
					}

					if (StringUtils.isValid(pkValue)) {

						if (existujeZaznamNaZmenuDopravnyBod(auth, pkValue, planPlatnostOd)) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3071);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
						}
						if (existujeZaznamNaZmenuStanicnaKolaj(auth, pkValue, planPlatnostOd)) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3071);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
						}

						Integer ciselnikStlpecID = _CudLookupUtils.lookupDTOCiselnikStlpec(lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_DOPRAVNY_BOD), _CudConsts.NAZOV_ID_NADRADENA_PRIMARNA).getCiselnikStlpecID();
						Integer pocet = getDelegate().getZmenaStlpecRead().getPocetNepublikovanychZaznamov(auth, lookupDTOCiselnik(auth, lookupMap, _CudConsts.TABULKA_T_DOPRAVNY_BOD).getCiselnikID(), ciselnikStlpecID, pkValue.toString());
						if (pocet.intValue() != 0) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3071);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
						}

						ciselnikStlpecID = _CudLookupUtils.lookupDTOCiselnikStlpec(lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_STANICNA_KOLAJ), _CudConsts.NAZOV_ID_DOPRAVNY_BOD).getCiselnikStlpecID();
						pocet = getDelegate().getZmenaStlpecRead().getPocetNepublikovanychZaznamov(auth, lookupDTOCiselnik(auth, lookupMap, _CudConsts.TABULKA_T_STANICNA_KOLAJ).getCiselnikID(), ciselnikStlpecID, pkValue.toString());
						if (pocet.intValue() != 0) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3071);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
						}
					}

				} else {

					for (Map<String, String> podHistRowMap : futRowList) {
						if (StringUtils.isValid(podHistRowMap.get(_CudConsts.NAZOV_CRD_ZAC))) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3099);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSPK.getCiselnikStlpecID()) };
						}
					}
				}

			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "validate.error");
			return null;
		}
	}

	@Override
	public DTODynCiselnik[] lookupValues(AuthInfo auth, DTOPlugin[] pluginList, DTODynCiselnik popValueDTO, DTOCiselnikStlpecGui dtoCS, DTOCiselnikStlpecGui[] metaArray) throws AppException {
		throw new AppException("Pouzitie neimplementovanej metody");
	}

	@Override
	public void setDelegat(_CudDelegateBi dlgBi) {
		this.setDelegate(dlgBi);
	}

}
