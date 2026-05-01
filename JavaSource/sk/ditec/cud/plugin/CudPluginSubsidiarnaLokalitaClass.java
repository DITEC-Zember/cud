package sk.ditec.cud.plugin;

import java.util.ArrayList;
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

public class CudPluginSubsidiarnaLokalitaClass extends _CudBasePluginClass implements IPlugin {

	private String ALIAS_CISLO = "CISLO";
	private String ALIAS_NADRADENA_PRIMARNA = "NADRADENA_PRIMARNA";
	private String ALIAS_CRD_ZAC = "CRD_ZAC";
	private String ALIAS_CRD_KON = "CRD_KON";

	@Override
	public String updateKontrola(AuthInfo auth, DTOPluginStlpec[] pluginStlpecList, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			String s = kontrolaAlias(pluginStlpecList, ALIAS_CISLO);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_NADRADENA_PRIMARNA);
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

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_CISLO, csList, _CudConsts.DB_TYP_STRING);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaDbTyp(pluginStlpecList, ALIAS_NADRADENA_PRIMARNA, csList, _CudConsts.DB_TYP_INTEGER);
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

			String valNadradenaPrimarna = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_NADRADENA_PRIMARNA);
			String valCrdZac = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_CRD_ZAC);
			String valCrdKon = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_CRD_KON);

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

				String newValue = lookupValue(dtoPlg.getPluginStlpecList(), rowMap, ALIAS_CISLO);
				String oldValue = lookupValue(dtoPlg.getPluginStlpecList(), oldRowMap, ALIAS_CISLO);
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

			// kontrola subsidiarnej lokality
			if (StringUtils.isValid(valNadradenaPrimarna)) {

				List<DTOCiselnikStlpec> metaList = lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_DOPRAVNY_BOD);

				Map<Integer, List<Map<Integer, Set<String>>>> filterMap = new HashMap<Integer, List<Map<Integer, Set<String>>>>();

				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecPk(metaList);
				filterMap.put(0, new ArrayList<Map<Integer, Set<String>>>());
				filterMap.get(0).add(new HashMap<Integer, Set<String>>());
				filterMap.get(0).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
				filterMap.get(0).get(0).get(dtoCS.getCiselnikStlpecID()).add(valNadradenaPrimarna);

				dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(metaList, _CudConsts.NAZOV_ZMAZ);
				filterMap.put(1, new ArrayList<Map<Integer, Set<String>>>());
				filterMap.get(1).add(new HashMap<Integer, Set<String>>());
				filterMap.get(1).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
				filterMap.get(1).get(0).get(dtoCS.getCiselnikStlpecID()).add("F");

				List<String> isNotNullList = new ArrayList<String>();
				isNotNullList.add(_CudConsts.NAZOV_ID_SUBSIDIARY_TYPE);

				if (getDelegate().getDynCiselnikRead().futCount(auth, _CudConsts.TABULKA_T_DOPRAVNY_BOD, metaList, filterMap, null, isNotNullList, planPlatnostOd, planPlatnostDo).intValue() != 0) {
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3078);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
				}
			}

			// kontrola CRD subsidiarnej lokality
			if (StringUtils.isValid(valNadradenaPrimarna) && StringUtils.isValid(valCrdZac)) {

				List<DTOCiselnikStlpec> metaList = lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_DOPRAVNY_BOD);

				Map<Integer, List<Map<Integer, Set<String>>>> filterMap = new HashMap<Integer, List<Map<Integer, Set<String>>>>();

				DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecPk(metaList);
				filterMap.put(0, new ArrayList<Map<Integer, Set<String>>>());
				filterMap.get(0).add(new HashMap<Integer, Set<String>>());
				filterMap.get(0).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
				filterMap.get(0).get(0).get(dtoCS.getCiselnikStlpecID()).add(valNadradenaPrimarna);

				dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(metaList, _CudConsts.NAZOV_ZMAZ);
				filterMap.put(1, new ArrayList<Map<Integer, Set<String>>>());
				filterMap.get(1).add(new HashMap<Integer, Set<String>>());
				filterMap.get(1).get(0).put(dtoCS.getCiselnikStlpecID(), new HashSet<String>());
				filterMap.get(1).get(0).get(dtoCS.getCiselnikStlpecID()).add("F");

				List<Map<String, String>> npFutRowList = getDelegate().getDynCiselnikRead().futListLight(auth, _CudConsts.TABULKA_T_DOPRAVNY_BOD, metaList, filterMap, planPlatnostOd, planPlatnostDo, new Page(true));

				// kontrola CRD platnosti subsidiarnej lokality
				for (Map<String, String> futRowMap : npFutRowList) {

					String futValCrdZac = lookupValue(dtoPlg.getPluginStlpecList(), futRowMap, ALIAS_CRD_ZAC);
					if (!StringUtils.isValid(futValCrdZac)) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3089);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
					}
					if (_CudConsts.DATE_FORMAT.parse(futValCrdZac).after(_CudConsts.DATE_FORMAT.parse(valCrdZac)) && !_CudConsts.DATE_FORMAT.parse(futValCrdZac).equals(_CudConsts.DATE_FORMAT.parse(valCrdZac))) {
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3090);
						return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_ZAC).getCiselnikStlpecID()) };
					}

					String futValCrdKon = lookupValue(dtoPlg.getPluginStlpecList(), futRowMap, ALIAS_CRD_KON);
					if (StringUtils.isValid(futValCrdKon)) {

						if (!StringUtils.isValid(valCrdKon)) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3091);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_KON).getCiselnikStlpecID()) };
						}
						if (_CudConsts.DATE_FORMAT.parse(futValCrdKon).before(_CudConsts.DATE_FORMAT.parse(valCrdZac)) && !_CudConsts.DATE_FORMAT.parse(futValCrdKon).equals(_CudConsts.DATE_FORMAT.parse(valCrdZac))) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3092);
							return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_ZAC).getCiselnikStlpecID()) };
						}
						if (StringUtils.isValid(valCrdKon)) {
							if (_CudConsts.DATE_FORMAT.parse(futValCrdKon).before(_CudConsts.DATE_FORMAT.parse(valCrdKon)) && !_CudConsts.DATE_FORMAT.parse(futValCrdKon).equals(_CudConsts.DATE_FORMAT.parse(valCrdKon))) {
								String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3093);
								return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_CRD_KON).getCiselnikStlpecID()) };
							}
						}
					}
				}

				// kontrola ci existuje poziadavka na zmenu subsidiarnej lokality
				if (getDelegate().getWfTodoRead().count(auth, lookupDTOCiselnik(auth, lookupMap, _CudConsts.TABULKA_T_DOPRAVNY_BOD).getCiselnikID(), Integer.valueOf(valNadradenaPrimarna), null).intValue() != 0) {
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3094);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
				}

				// kontrola T_COMPANY
				String key = _CudConsts.TABULKA_T_COMPANY + "_ROW_DATA";
				Map<String, String> companyRowMap = lookupMap.getMap(key, String.class);
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

				// kontrola T_SUBSIDIARY_TYPE
				key = _CudConsts.TABULKA_T_SUBSIDIARY_TYPE + "_ROW_DATA";
				Map<String, String> subsidiaryTypeRowMap = lookupMap.getMap(key, String.class);
				if (!StringUtils.isValid(subsidiaryTypeRowMap)) {
					List<DTOCiselnikStlpec> pomMetaList = lookupDTOCiselnikStlpecList(auth, lookupMap, _CudConsts.TABULKA_T_SUBSIDIARY_TYPE);
					DTOCiselnikStlpec dtoPomCS = _CudLookupUtils.lookupDTOCiselnikStlpec(pomMetaList, _CudConsts.NAZOV_SUBSIDIARY_TYPE_CODE);
					subsidiaryTypeRowMap = getDelegate().getDynCiselnikRead().readLight(auth, _CudConsts.TABULKA_T_SUBSIDIARY_TYPE, pomMetaList, dtoPomCS.getNazov(), _CudConsts.SUBSIDIARY_TYPE_CODE_01, dtoPomCS.getDbTyp(), planPlatnostOd, "F");
					lookupMap.addRecord(key, subsidiaryTypeRowMap);

				}
				if (subsidiaryTypeRowMap.isEmpty()) {
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3112);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
				}

				boolean b = true;
				if ("T".equals(companyRowMap.get(_CudConsts.NAZOV_CE_ENTITY_FLAG)) && "T".equals(subsidiaryTypeRowMap.get(_CudConsts.NAZOV_CENTRAL_ENTITY_FLAG))) {
					b = false;
				} else if ("T".equals(companyRowMap.get(_CudConsts.NAZOV_NE_ENTITY_FLAG)) && "T".equals(subsidiaryTypeRowMap.get(_CudConsts.NAZOV_NATIONAL_ENTITY_FLAG)) && companyRowMap.get(_CudConsts.NAZOV_ID_COUNTRY).equals(countryRowMap.get(_CudConsts.NAZOV_COUNTRY_ID))) {
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
					String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3113);
					return new DTOPluginKontrolaRow[] { createDTOPluginKontrolaRow(dtoPlg.getPluginID(), _CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR, err, lookupDTOCiselnikStlpec(dtoPlg.getPluginStlpecList(), csList, ALIAS_NADRADENA_PRIMARNA).getCiselnikStlpecID()) };
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
