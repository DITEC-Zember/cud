package sk.ditec.cud.plugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;

public class CudPluginStavebnaDlzkaVociDUClass extends _CudBasePluginClass implements IPlugin {

	private String ALIAS_TRATOVY_USEK = "TRATOVY_USEK";
	private String ALIAS_STAVEBNA_DLZKA = "STAVEBNA_DLZKA";

	@Override
	public String updateKontrola(AuthInfo auth, DTOPluginStlpec[] pluginStlpecList, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			String s = kontrolaAlias(pluginStlpecList, ALIAS_TRATOVY_USEK);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_STAVEBNA_DLZKA);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_TRATOVY_USEK, csList, _CudConsts.DB_TYP_INTEGER);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_STAVEBNA_DLZKA, csList, _CudConsts.DB_TYP_DOUBLE);
			if (StringUtils.isValid(s)) {
				return s;
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrola.error");
			return null;
		}
	}

	private List<Map<String, String>> futListFromDU(AuthInfo auth, CudCacheMap lookupMap, String tratovyIsekID) throws AppException {

		try {
			List<DTOCiselnikStlpec> metaList = lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_DEFINICNY_USEK);

			Map<Integer, List<Map<Integer, Set<String>>>> filterMap = new HashMap<Integer, List<Map<Integer, Set<String>>>>();

			DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(metaList, _CudConsts.NAZOV_ID_TRATOVY_USEK);
			filterMap.put(0, new ArrayList<Map<Integer, Set<String>>>());
			filterMap.get(0).add(new HashMap<Integer, Set<String>>());
			filterMap.get(0).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
			filterMap.get(0).get(0).get(dtoCS.getCiselnikStlpecID()).add(tratovyIsekID);

			dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(metaList, _CudConsts.NAZOV_ZMAZ);
			filterMap.put(1, new ArrayList<Map<Integer, Set<String>>>());
			filterMap.get(1).add(new HashMap<Integer, Set<String>>());
			filterMap.get(1).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
			filterMap.get(1).get(0).get(dtoCS.getCiselnikStlpecID()).add("F");

			Date planPlatnostOd = lookupMap.getRecord(_CudConsts.NAZOV_PLG_PLATNOST_OD, Date.class);
			Date planPlatnostDo = lookupMap.getRecord(_CudConsts.NAZOV_PLG_PLATNOST_DO, Date.class);

			List<Map<String, String>> resultList = getDelegate().getDynCiselnikRead().futListLight(auth, _CudConsts.TABULKA_T_DEFINICNY_USEK, metaList, filterMap, planPlatnostOd, planPlatnostDo, new Page(true));

			Integer bulhar = new Double(Math.pow(10, _CudLookupUtils.lookupDTOCiselnikStlpec(metaList, _CudConsts.NAZOV_STAVEBNA_DLZKA).getDecimals())).intValue();
			for (Map<String, String> histRow : resultList) {
				String histStavebnaDlzkaStr = histRow.get(_CudConsts.NAZOV_STAVEBNA_DLZKA);
				if (StringUtils.isValid(histStavebnaDlzkaStr)) {
					Integer stavebnaDlzka = new Double(new Double(histStavebnaDlzkaStr) * bulhar).intValue();
					histRow.put(_CudConsts.NAZOV_STAVEBNA_DLZKA, stavebnaDlzka.toString());
				}
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "futListFromDU.error");
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

			String valTratovyUsekID = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_TRATOVY_USEK);
			if (!StringUtils.isValid(valTratovyUsekID)) {
				return null;
			}

			Integer bulhar = new Double(Math.pow(10, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_STAVEBNA_DLZKA).getDecimals())).intValue();

			String valStavebnaDlzka = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_STAVEBNA_DLZKA);
			if (!StringUtils.isValid(valStavebnaDlzka)) {
				return null;
			}
			Integer stavebnaDlzka = new Double(new Double(valStavebnaDlzka) * bulhar * 1000).intValue();

			Date planPlatnostOd = lookupMap.getRecord(_CudConsts.NAZOV_PLG_PLATNOST_OD, Date.class);

			List<Map<String, String>> duFutRowList = futListFromDU(auth, lookupMap, valTratovyUsekID);

			Set<Date> platnostSet = new HashSet<Date>();
			for (Map<String, String> duFutRowMap : duFutRowList) {

				String histPlatnostOdStr = duFutRowMap.get(_CudConsts.NAZOV_PLATNOST_OD);
				Date histPlatnostOd = StringUtils.isValid(histPlatnostOdStr) ? _CudConsts.DATE_FORMAT.parse(histPlatnostOdStr) : null;

				String histPlatnostDoStr = duFutRowMap.get(_CudConsts.NAZOV_PLATNOST_DO);
				Date histPlatnostDo = StringUtils.isValid(histPlatnostDoStr) ? _CudConsts.DATE_FORMAT.parse(histPlatnostDoStr) : null;
				if (StringUtils.isValid(histPlatnostDo)) {
					Calendar cal = Calendar.getInstance(new Locale("sk_SK"));
					cal.setTime(histPlatnostDo);
					cal.add(Calendar.DAY_OF_YEAR, 1);
					histPlatnostDo = cal.getTime();
				}

				if (planPlatnostOd.equals(histPlatnostOd) || planPlatnostOd.before(histPlatnostOd)) {
					platnostSet.add(histPlatnostOd);
				}

				if (StringUtils.isValid(histPlatnostDo)) {
					platnostSet.add(histPlatnostDo);
				}
			}

			platnostSet.add(planPlatnostOd);

			List<Date> platnostList = new ArrayList<Date>(platnostSet);
			Collections.sort(platnostList);

			Map<Date, Integer> stavebnaDlzkaMap = new HashMap<Date, Integer>();
			for (Date datum : platnostList) {
				for (Map<String, String> duFutRowMap : duFutRowList) {

					String histPlatnostOdStr = duFutRowMap.get(_CudConsts.NAZOV_PLATNOST_OD);
					Date histPlatnostOd = StringUtils.isValid(histPlatnostOdStr) ? _CudConsts.DATE_FORMAT.parse(histPlatnostOdStr) : null;

					String histPlatnostDoStr = duFutRowMap.get(_CudConsts.NAZOV_PLATNOST_DO);
					Date histPlatnostDo = StringUtils.isValid(histPlatnostDoStr) ? _CudConsts.DATE_FORMAT.parse(histPlatnostDoStr) : null;

					if (histPlatnostOd.equals(datum) || histPlatnostOd.before(datum)) {
						if (!StringUtils.isValid(histPlatnostDo) || datum.equals(histPlatnostDo) || datum.before(histPlatnostDo)) {

							if (!StringUtils.isValid(stavebnaDlzkaMap.get(datum))) {
								stavebnaDlzkaMap.put(datum, 0);
							}
							String histStavebnaDlzkaStr = duFutRowMap.get(_CudConsts.NAZOV_STAVEBNA_DLZKA);
							if (StringUtils.isValid(histStavebnaDlzkaStr)) {
								stavebnaDlzkaMap.put(datum, stavebnaDlzkaMap.get(datum) + new Integer(histStavebnaDlzkaStr).intValue());
							}
						}
					}
				}
			}

			List<DTOPluginKontrolaRow> resultList = new ArrayList<DTOPluginKontrolaRow>();

			for (Date datum : platnostList) {

				if (stavebnaDlzkaMap.get(datum).intValue() != stavebnaDlzka.intValue()) {
					String d = _CudConsts.DATE_FORMAT.format(datum);
					int div = stavebnaDlzkaMap.get(datum).intValue() / (bulhar * 1000);
					int mod = stavebnaDlzkaMap.get(datum).intValue() % (bulhar * 1000);
					String value = new Double(div + "." + mod).toString();
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3103, d, valStavebnaDlzka, value);
					resultList.add(createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_WARNING, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_STAVEBNA_DLZKA).getCiselnikStlpecID()));
				}
			}

			return resultList.isEmpty() ? null : resultList.toArray(new DTOPluginKontrolaRow[resultList.size()]);

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
