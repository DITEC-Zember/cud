package sk.ditec.cud.plugin;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class CudPluginCisloTsiClass extends _CudBasePluginClass implements IPlugin {

	private String ALIAS_TSI_CISLO = "TSI_CISLO";
	private String ALIAS_TT = "TT";

	@Override
	public String updateKontrola(AuthInfo auth, DTOPluginStlpec[] pluginStlpecList, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			String s = kontrolaAlias(pluginStlpecList, ALIAS_TSI_CISLO);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaParam(pluginStlpecList, ALIAS_TT);
			if (StringUtils.isValid(s)) {
				return s;
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrola.error");
			return null;
		}
	}

	private boolean existujeZaznamTCountry(AuthInfo auth, String platnostOd, String countryUicCode) throws AppException {

		try {
			String con1 = _CudConsts.NAZOV_PLATNOST_OD + " <= to_timestamp(\'" + platnostOd + "\', \'DD.MM.YYYY\')";
			String con2 = "((" + _CudConsts.NAZOV_PLATNOST_DO + " >= to_timestamp(\'" + platnostOd + "\', \'DD.MM.YYYY\')) OR (" + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL))";
			String con3 = _CudConsts.NAZOV_ZMAZ + "= \'F\'";
			String con4 = _CudConsts.NAZOV_COUNTRY_UIC_CODE + " = \'" + countryUicCode + "\'";
			String where = con1 + " AND " + con2 + " AND " + con3 + " AND " + con4;

			String sql = "SELECT count(*) FROM " + _CudConsts.TABULKA_T_COUNTRY + " WHERE " + where;

			Integer[] values = getDelegate().getDynCiselnikRead().readValuesAsInteger(auth, sql);

			if (!StringUtils.isValid(values)) {
				return false;
			}

			if (values[0] == 0) {
				return false;
			}

			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "existujeZaznamTCountry.error");
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

			String value = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_TSI_CISLO);
			if (!StringUtils.isValid(value)) {
				return null;
			}

			Set<String> ttSet = lookupParams(dtoPlg.getPluginStlpecList(), ALIAS_TT);

			Date planPlatnostOd = lookupMap.getRecord(_CudConsts.NAZOV_PLG_PLATNOST_OD, Date.class);

			DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_TSI_CISLO);

			DTOCiselnikStlpec dtoCSPK = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList);
			Integer pkValue = StringUtils.isValid(rowMap.get(dtoCSPK.getNazov())) ? Integer.valueOf(rowMap.get(dtoCSPK.getNazov())) : null;

			if (StringUtils.isValid(pkValue)) {
				Map<String, String> oldMap = getDelegate().getDynCiselnikRead().readLight(auth, dtoPlg.getCiselnikTabulka(), csList, dtoCSPK.getNazov(), Integer.toString(pkValue), dtoCSPK.getDbTyp(), planPlatnostOd, "F");
				String oldValue = oldMap.get(dtoCS.getNazov());
				if (!_CudKontrolaUtils.equals(oldValue, value) && StringUtils.isValid(oldValue)) {
					String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3100, col);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
				}
			}

			if (!StringUtils.isValid(value) || value.length() != 8) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3101, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			for (int i = 0; i < value.length(); i++) {
				if (!Character.isDigit(value.charAt(i))) {
					String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3101, col);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
				}
			}

			if (!ttSet.contains(value.substring(0, 2))) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3101, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			if (!existujeZaznamTCountry(auth, _CudConsts.DATE_FORMAT.format(planPlatnostOd), value.substring(2, 4))) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3101, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			String con1 = _CudConsts.NAZOV_PLATNOST_OD + " <= to_timestamp('" + _CudConsts.DATE_FORMAT.format(planPlatnostOd) + "', 'DD.MM.YYYY')";
			con1 += " AND (" + _CudConsts.NAZOV_PLATNOST_DO + " >= to_timestamp('" + _CudConsts.DATE_FORMAT.format(planPlatnostOd) + "', 'DD.MM.YYYY')";
			con1 += " OR " + _CudConsts.NAZOV_PLATNOST_DO + " IS NULL)";

			String con2 = _CudConsts.NAZOV_ZMAZ + " = 'F'";

			String con3 = null;
			if (_CudConsts.DB_TYP_STRING.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getDbTyp())) {
				con3 = dtoCS.getNazov() + " = \'" + value + "\'";

			} else if (_CudConsts.DB_TYP_INTEGER.equals(dtoCS.getDbTyp()) || _CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getDbTyp())) {
				con3 = dtoCS.getNazov() + " = " + value;

			} else if (_CudConsts.DB_TYP_DATE.equals(dtoCS.getDbTyp())) {
				con3 = "to_char(" + dtoCS.getNazov() + ", 'DD.MM.YYYY')" + " = " + value;
			}

			String where = con3 + " AND " + con2 + " AND " + con1;

			String sql = "SELECT " + dtoCSPK.getNazov() + " FROM " + dtoPlg.getCiselnikTabulka() + " WHERE " + where;

			Integer[] rowIDs = getDelegate().getDynCiselnikRead().readValuesAsInteger(auth, sql);
			if (StringUtils.isValid(rowIDs)) {

				for (Integer rowID : rowIDs) {
					if (!_CudKontrolaUtils.equals(rowID, pkValue)) {
						String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3070, col, rowID.toString());
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
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
	public void setDelegat(_CudDelegateBi dlgBi) {
		this.setDelegate(dlgBi);
	}

	@Override
	public DTODynCiselnik[] lookupValues(AuthInfo auth, DTOPlugin[] pluginList, DTODynCiselnik popValueDTO, DTOCiselnikStlpecGui dtoCS, DTOCiselnikStlpecGui[] metaArray) throws AppException {
		throw new AppException("Pouzitie neimplementovanej metody");
	}

}
