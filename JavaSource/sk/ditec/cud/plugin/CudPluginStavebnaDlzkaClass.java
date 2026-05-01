package sk.ditec.cud.plugin;

import java.math.BigDecimal;
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
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudResultUtils;

public class CudPluginStavebnaDlzkaClass extends _CudBasePluginClass implements IPlugin {

	private String ALIAS_KM_OD = "KM_OD";
	private String ALIAS_KM_DO = "KM_DO";
	private String ALIAS_STAVEBNA_DLZKA = "STAVEBNA_DLZKA";
	private String ALIAS_MHK = "MHK";

	@Override
	public String updateKontrola(AuthInfo auth, DTOPluginStlpec[] pluginStlpecList, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			String s = kontrolaAlias(pluginStlpecList, ALIAS_KM_OD);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_KM_DO);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_STAVEBNA_DLZKA);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_MHK);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_KM_OD, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_KM_DO, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_STAVEBNA_DLZKA, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_MHK, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrola.error");
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

			DTOCiselnikStlpec dtoCSKmOd = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_KM_OD);
			DTOCiselnikStlpec dtoCSKmDo = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_KM_DO);
			DTOCiselnikStlpec dtoCSStavebnaDlzka = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_STAVEBNA_DLZKA);
			DTOCiselnikStlpec dtoCSMhk = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_MHK);

			String valKmOd = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_KM_OD);
			if (!StringUtils.isValid(valKmOd)) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCSKmOd.getNadpis() : dtoCSKmOd.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3073, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSKmOd.getCiselnikStlpecID()) };
			}

			String valKmDo = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_KM_DO);
			if (!StringUtils.isValid(valKmDo)) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCSKmDo.getNadpis() : dtoCSKmDo.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3073, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSKmDo.getCiselnikStlpecID()) };
			}

			String valStavebnaDlzka = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_STAVEBNA_DLZKA);
			if (!StringUtils.isValid(valStavebnaDlzka)) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCSStavebnaDlzka.getNadpis() : dtoCSStavebnaDlzka.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3073, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSStavebnaDlzka.getCiselnikStlpecID()) };
			}

			String valMhk = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_MHK);
			if (!StringUtils.isValid(valMhk)) {
				return null;
			}

			Integer bulharKmOd = new Double(Math.pow(10, dtoCSKmOd.getDecimals())).intValue();
			Integer bulharKmDo = new Double(Math.pow(10, dtoCSKmDo.getDecimals())).intValue();
			Integer bulharStavebnaDlzka = new Double(Math.pow(10, dtoCSStavebnaDlzka.getDecimals())).intValue();
			Integer bulharMhk = new Double(Math.pow(10, dtoCSMhk.getDecimals())).intValue();
			Integer bulhar = _CudKontrolaUtils.biggest(bulharKmOd, bulharKmDo, bulharStavebnaDlzka, bulharMhk);

			Integer maxDecimal = _CudKontrolaUtils.biggest(dtoCSKmOd.getDecimals(), dtoCSKmDo.getDecimals(), dtoCSStavebnaDlzka.getDecimals(), dtoCSMhk.getDecimals());

			Integer kmOd = new BigDecimal(valKmOd).movePointRight(maxDecimal).intValue();
			Integer kmDo = new BigDecimal(valKmDo).movePointRight(maxDecimal).intValue();
			Integer stavebnaDlzka = new BigDecimal(valStavebnaDlzka).movePointRight(maxDecimal).intValue();
			Integer mhk = new BigDecimal(valMhk).movePointRight(maxDecimal).intValue();

			Integer testStavebnaDlzka = ((kmDo - kmOd) * 1000) + mhk;
			if (testStavebnaDlzka.intValue() != stavebnaDlzka.intValue()) {
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCSStavebnaDlzka.getNadpis() : dtoCSStavebnaDlzka.getNazov();
				String value = new Double((double) testStavebnaDlzka / bulhar).toString();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3069, col, value);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCSStavebnaDlzka.getCiselnikStlpecID()) };
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
