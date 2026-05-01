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

public class CudPluginCelociselneZaokruhlenieClass extends _CudBasePluginClass implements IPlugin {

	private String ALIAS_CISLO_DESATINNE = "CISLO_DESATINNE";
	private String ALIAS_CISLO_CELE = "CISLO_CELE";

	@Override
	public String updateKontrola(AuthInfo auth, DTOPluginStlpec[] pluginStlpecList, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			String s = kontrolaAlias(pluginStlpecList, ALIAS_CISLO_DESATINNE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_CISLO_CELE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_CISLO_DESATINNE, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_CISLO_CELE, csList, _CudConsts.DB_TYP_INTEGER);
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

			String cisloDesatinne = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_CISLO_DESATINNE);
			String cisloCele = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_CISLO_CELE);

			if (!StringUtils.isValid(cisloDesatinne) && !StringUtils.isValid(cisloCele)) {
				return null;
			}

			if (StringUtils.isValid(cisloDesatinne) && !StringUtils.isValid(cisloCele)) {
				DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CISLO_CELE);
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3072, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}
			if (!StringUtils.isValid(cisloDesatinne) && StringUtils.isValid(cisloCele)) {
				DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CISLO_DESATINNE);
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3073, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			int round = Math.round(Float.valueOf(cisloDesatinne));
			if (Integer.valueOf(cisloCele).intValue() != round) {
				DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CISLO_DESATINNE);
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3074, col, cisloCele);
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
