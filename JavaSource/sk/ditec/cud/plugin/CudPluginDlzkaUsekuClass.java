package sk.ditec.cud.plugin;

import java.util.List;
import java.util.Map;

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
import sk.ditec.cud.utils._CudResultUtils;

public class CudPluginDlzkaUsekuClass extends _CudBasePluginClass implements IPlugin {

	private String ALIAS_DLZKA = "DLZKA";
	private String ALIAS_DLZKA_CASTI_TU = "DLZKA_CASTI_TU";
	private String ALIAS_DLZKA_CASTI_DU_Z = "DLZKA_CASTI_DU_Z";
	private String ALIAS_DLZKA_CASTI_DU_DO = "DLZKA_CASTI_DU_DO";

	@Override
	public String updateKontrola(AuthInfo auth, DTOPluginStlpec[] pluginStlpecList, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			String s = kontrolaAlias(pluginStlpecList, ALIAS_DLZKA);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_DLZKA_CASTI_DU_Z);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_DLZKA_CASTI_TU);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_DLZKA_CASTI_DU_DO);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_DLZKA, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_DLZKA_CASTI_DU_Z, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_DLZKA_CASTI_TU, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_DLZKA_CASTI_DU_DO, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrola.error");
			return null;
		}
	}

	private Integer parseValue(String value, Integer bulhar) throws AppException {

		try {
			String[] arr = Double.toString(Math.round(Double.parseDouble(value) * bulhar)).split("\\.");
			if (StringUtils.isValid(arr)) {
				return Integer.valueOf(arr[0]);
			}

			return Integer.valueOf(value);

		} catch (Throwable t) {
			DBUtils.handleException(t, "parseValue.error");
			return null;
		}
	}

	@Override
	public DTOPluginKontrolaRow[] validate(AuthInfo auth, DTOPlugin dtoPlg, Map<String, String> rowMap, List<DTOCiselnikStlpec> csList, CudCacheMap lookupMap) throws AppException {

		try {
			String s = updateKontrola(auth, dtoPlg.getPluginStlpecList(), csList);
			if (StringUtils.isValid(s)) {
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, s, null) };
			}

			int decimals = 0;
			int dlzkaDecimals = 0;

			String dlzkaStrValue = null;
			DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_DLZKA);
			if (StringUtils.isValid(dtoCS.getDecimals()) && dtoCS.getDecimals().intValue() > decimals) {
				decimals = dtoCS.getDecimals();
				dlzkaDecimals = dtoCS.getDecimals();
			}
			dlzkaStrValue = rowMap.get(dtoCS.getNazov());
			String ciselnikStlpecStr = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
			if (!StringUtils.isValid(dlzkaStrValue)) {
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, ciselnikStlpecStr);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			String dlzkaCastiTuValue = null;
			dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_DLZKA_CASTI_TU);
			if (StringUtils.isValid(dtoCS.getDecimals()) && dtoCS.getDecimals().intValue() > decimals) {
				decimals = dtoCS.getDecimals();
			}
			dlzkaCastiTuValue = rowMap.get(dtoCS.getNazov());
			if (!StringUtils.isValid(dlzkaCastiTuValue)) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			String dlzkaCastiDUZValue = null;
			dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_DLZKA_CASTI_DU_Z);
			if (StringUtils.isValid(dtoCS.getDecimals()) && dtoCS.getDecimals().intValue() > decimals) {
				decimals = dtoCS.getDecimals();
			}
			dlzkaCastiDUZValue = rowMap.get(dtoCS.getNazov());
			if (!StringUtils.isValid(dlzkaCastiDUZValue)) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			String dlzkaCastiDUDoValue = null;
			dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_DLZKA_CASTI_DU_DO);
			if (StringUtils.isValid(dtoCS.getDecimals()) && dtoCS.getDecimals().intValue() > decimals) {
				decimals = dtoCS.getDecimals();
			}
			dlzkaCastiDUDoValue = rowMap.get(dtoCS.getNazov());
			if (!StringUtils.isValid(dlzkaCastiDUDoValue)) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			Integer bulhar = (int) Math.pow(10, decimals);

			int sum = parseValue(dlzkaCastiTuValue, bulhar) + parseValue(dlzkaCastiDUZValue, bulhar) + parseValue(dlzkaCastiDUDoValue, bulhar);
			int dlzkaValue = parseValue(dlzkaStrValue, bulhar);

			if (dlzkaValue != sum) {
				String value = String.format("%.0" + dlzkaDecimals + "f", (float) sum / bulhar);
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3069, ciselnikStlpecStr, value);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
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
