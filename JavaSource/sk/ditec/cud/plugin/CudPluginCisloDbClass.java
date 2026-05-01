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

public class CudPluginCisloDbClass extends _CudBasePluginClass implements IPlugin {

	private String ALIAS_CISLO = "CISLO";

	@Override
	public String updateKontrola(AuthInfo auth, DTOPluginStlpec[] pluginStlpecList, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			String s = kontrolaAlias(pluginStlpecList, ALIAS_CISLO);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_CISLO, csList, _CudConsts.DB_TYP_STRING);
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

			String cislo = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_CISLO);
			if (cislo.length() != 6) {
				DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CISLO);
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3081, col);
				return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
			}

			for (int i = 0; i < cislo.length(); i++) {
				if (!Character.isDigit(cislo.charAt(i))) {
					DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CISLO);
					String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3082, col);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, dtoCS.getCiselnikStlpecID()) };
				}
			}

			int sucet = 0;
			for (int i = 0; i < 5; i++) {
				String value = String.valueOf(cislo.charAt(i));
				if ((i + 1) % 2 == 0) {
					sucet += Integer.valueOf(value);
				} else {
					String newValue = Integer.toString(Integer.parseInt(value) * 2);
					for (int j = 0; j < newValue.length(); j++) {
						sucet += Integer.parseInt(String.valueOf(newValue.charAt(j)));
					}
				}
			}

			int nasobok = 10;
			while (nasobok < sucet) {
				nasobok += 10;
			}

			int validNumber = nasobok - sucet;

			if (Integer.parseInt(String.valueOf(cislo.charAt(5))) != validNumber) {
				DTOCiselnikStlpec dtoCS = lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CISLO);
				String col = _CudConsts.ZDROJ_FORM.equals(dtoPlg.getZdroj()) ? dtoCS.getNadpis() : dtoCS.getNazov();
				String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3083, col, cislo.substring(0, 5) + validNumber);
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
