package sk.ditec.cud.print;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynCiselnik;
import sk.ditec.cud.dto.DTODynCiselnikExport;
import sk.ditec.cud.dto.DTOReport;
import sk.ditec.cud.utils._CudConsts;

public class CudTVlakovyUsekPrintPdfClass extends _CudPrintBaseClass implements _ICudPrint {

	@Override
	public DTODynCiselnikExport exportPrint(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnik dtoCis) throws AppException {

		try {
			DTODynCiselnikExport resultDTO = new DTODynCiselnikExport();
			resultDTO.setFileName(lookupFileName(dtoCis.getNazov(), dtoExp.getFormat()));

			dtoDyn.setTabulka(dtoCis.getTabulka());

			Map<Integer, Integer> hpMap = hranicnyPriechodMap(auth, dtoExp, dtoDyn);

			Map<Integer, String> krajinaMap = krajinaMap(auth, dtoExp, dtoDyn);

			Map<String, Set<String>> nadusekMap = nadusekMap(auth, dtoExp, dtoDyn);

			DTODynCiselnik[] vuList = vlakovyUsekList(auth, dtoExp, dtoDyn);

			resultDTO.setPriloha(getReport(vuList, nadusekMap, hpMap, krajinaMap, createPageHeaderText(dtoCis.getPrintZahlavie(), dtoDyn.getPlatnostOd())));

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "exportPrint.error", auth);
			return null;
		}
	}

	@Override
	public DTODynCiselnikExport exportPrintKontrola(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnik dtoCis) throws AppException {

		try {
			dtoExp.setPage(1);
			dtoExp.setPageSize(_CudConsts.PRINT_MAX_POCET);
			return dtoExp;

		} catch (Throwable t) {
			handleException(t, "exportPrintKontrola.error", auth);
			return null;
		}
	}

	private DTOCiselnikStlpecGui[] createMetaListForVlakovyUsek() throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			resultList.add(createMetaAtribut(_CudConsts.NAZOV_VLAKOVY_USEK_ID, _CudConsts.CISELNIK_STLPEC_TYP_PK, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null,
					null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_CISLO_PARNY_SMER, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_STRING, 4, null, null, null, null, null, null,
					null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_CISLO_NEPARNY_SMER, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_STRING, 4, null, null, null, null, null,
					null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_OD, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 100, null,
					_CudConsts.TABULKA_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_DOPRAVNY_NAZOV_ID, _CudConsts.NAZOV_NAZOV, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_DO, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 100, null,
					_CudConsts.TABULKA_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_DOPRAVNY_NAZOV_ID, _CudConsts.NAZOV_NAZOV, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_OD, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 100, null,
					_CudConsts.TABULKA_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_DOPRAVNY_NAZOV_ID, _CudConsts.NAZOV_SKRATKA_ENEE, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_DO, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 100, null,
					_CudConsts.TABULKA_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_DOPRAVNY_NAZOV_ID, _CudConsts.NAZOV_SKRATKA_ENEE, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_KM, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null, null));
			resultList
					.add(createMetaAtribut(_CudConsts.NAZOV_KOLAJNOST, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_INTEGER, 5, null, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_TYP_ELEKTRICKEJ_TRAKCIE, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 1, null,
					_CudConsts.TABULKA_T_TYP_ELEKTRICKEJ_TRAKCIE, _CudConsts.NAZOV_TYP_ELEKTRICKEJ_TRAKCIE_ID, _CudConsts.NAZOV_OZNACENIE, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_ROZCHOD_KOLAJI, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 2, null,
					_CudConsts.TABULKA_T_ROZCHOD_KOLAJI, _CudConsts.NAZOV_ROZCHOD_KOLAJI_ID, _CudConsts.NAZOV_SKRATKA, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_NADUSEK, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_STRING, 1, null, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_OD, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null,
					null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_DO, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null,
					null, null));

			return resultList.toArray(new DTOCiselnikStlpecGui[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "createMetaListForVlakovyUsek.error");
			return null;
		}
	}

	private DTODynCiselnik[] vlakovyUsekList(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setCiselnikID(dtoDyn.getCiselnikID());
			dtoF.setTabulka(dtoDyn.getTabulka());
			dtoF.setPlatnostOd(dtoDyn.getPlatnostOd());
			dtoF.setListZobrazenie("T");

			return getDelegate().getDynCiselnikRead().list(auth, new Page(1, _CudConsts.PRINT_MAX_POCET, "4_ASC"), dtoF, createMetaListForVlakovyUsek(),
					new HashMap<Integer, List<String>>());

		} catch (Throwable t) {
			handleException(t, "vlakovyUsekList.error", auth);
			return null;
		}
	}

	private DTOCiselnikStlpecGui[] createMetaListForNadUsek() throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			resultList.add(createMetaAtribut(_CudConsts.NAZOV_NADUSEK_ID, _CudConsts.CISELNIK_STLPEC_TYP_PK, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null, null,
					null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_VLAKOVY_USEK_N, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null,
					null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_VLAKOVY_USEK, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 4, null,
					_CudConsts.TABULKA_T_VLAKOVY_USEK, _CudConsts.NAZOV_VLAKOVY_USEK_ID, _CudConsts.NAZOV_CISLO_PARNY_SMER, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_VLAKOVY_USEK, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 4, null,
					_CudConsts.TABULKA_T_VLAKOVY_USEK, _CudConsts.NAZOV_VLAKOVY_USEK_ID, _CudConsts.NAZOV_CISLO_NEPARNY_SMER, null, null, null));

			return resultList.toArray(new DTOCiselnikStlpecGui[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "createMetaListForTratovyUsek.error");
			return null;
		}

	}

	private Map<String, Set<String>> nadusekMap(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTOCiselnik dtoCis = getDelegate().getCiselnikRead().readLight(auth, _CudConsts.TABULKA_T_NADUSEK);

			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setCiselnikID(dtoCis.getCiselnikID());
			dtoF.setTabulka(_CudConsts.TABULKA_T_NADUSEK);
			dtoF.setPlatnostOd(dtoDyn.getPlatnostOd());
			dtoF.setListZobrazenie("T");

			DTODynCiselnik[] dynCiselnikList = getDelegate().getDynCiselnikRead().list(auth, new Page(1, _CudConsts.PRINT_MAX_POCET, "1_ASC"), dtoF, createMetaListForNadUsek(),
					new HashMap<Integer, List<String>>());

			Map<String, Set<String>> resultMap = new HashMap<String, Set<String>>();
			for (DTODynCiselnik dto : dynCiselnikList) {
				if (!StringUtils.isValid(resultMap.get(dto.getValues()[1].getValueStr() + "_P"))) {
					resultMap.put(dto.getValues()[1].getValueStr() + "_P", new HashSet<String>());
				}
				resultMap.get(dto.getValues()[1].getValueStr() + "_P").add(dto.getValues()[2].getValueStr());

				if (!StringUtils.isValid(resultMap.get(dto.getValues()[1].getValueStr() + "_N"))) {
					resultMap.put(dto.getValues()[1].getValueStr() + "_N", new HashSet<String>());
				}
				resultMap.get(dto.getValues()[1].getValueStr() + "_N").add(dto.getValues()[3].getValueStr());
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "nadusekMap.error", auth);
			return null;
		}
	}

	private DTOCiselnikStlpecGui[] createMetaListForHranicnyPriechod() throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			resultList.add(createMetaAtribut(_CudConsts.NAZOV_HRANICNY_PRIECHOD_ID, _CudConsts.CISELNIK_STLPEC_TYP_PK, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null,
					null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null,
					null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_KRAJINA, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null, null,
					null));

			return resultList.toArray(new DTOCiselnikStlpecGui[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "createMetaListForHranicnyPriechod.error");
			return null;
		}

	}

	private Map<Integer, Integer> hranicnyPriechodMap(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTOCiselnik dtoCis = getDelegate().getCiselnikRead().readLight(auth, _CudConsts.TABULKA_T_HRANICNY_PRIECHOD);

			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setCiselnikID(dtoCis.getCiselnikID());
			dtoF.setTabulka(_CudConsts.TABULKA_T_HRANICNY_PRIECHOD);
			dtoF.setPlatnostOd(dtoDyn.getPlatnostOd());
			dtoF.setListZobrazenie("T");

			DTODynCiselnik[] dynCiselnikList = getDelegate().getDynCiselnikRead().list(auth, new Page(1, _CudConsts.PRINT_MAX_POCET, "1_ASC"), dtoF,
					createMetaListForHranicnyPriechod(), new HashMap<Integer, List<String>>());

			Map<Integer, Integer> resultMap = new HashMap<Integer, Integer>();
			for (DTODynCiselnik dto : dynCiselnikList) {
				resultMap.put(Integer.parseInt(dto.getValues()[1].getValueStr()), Integer.parseInt(dto.getValues()[2].getValueStr()));
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "nadusekMap.error", auth);
			return null;
		}
	}

	private DTOCiselnikStlpecGui[] createMetaListForKrajina() throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			resultList.add(createMetaAtribut(_CudConsts.NAZOV_KRAJINA_ID, _CudConsts.CISELNIK_STLPEC_TYP_PK, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null, null,
					null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_SKRATKA_2, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_STRING, 2, null, null, null, null, null, null, null));

			return resultList.toArray(new DTOCiselnikStlpecGui[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "createMetaListForKrajina.error");
			return null;
		}

	}

	private Map<Integer, String> krajinaMap(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTOCiselnik dtoCis = getDelegate().getCiselnikRead().readLight(auth, _CudConsts.TABULKA_T_KRAJINA);

			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setCiselnikID(dtoCis.getCiselnikID());
			dtoF.setTabulka(_CudConsts.TABULKA_T_KRAJINA);
			dtoF.setPlatnostOd(dtoDyn.getPlatnostOd());
			dtoF.setListZobrazenie("T");

			DTODynCiselnik[] dynCiselnikList = getDelegate().getDynCiselnikRead().list(auth, new Page(1, _CudConsts.PRINT_MAX_POCET, "1_ASC"), dtoF, createMetaListForKrajina(),
					new HashMap<Integer, List<String>>());

			Map<Integer, String> resultMap = new HashMap<Integer, String>();
			for (DTODynCiselnik dto : dynCiselnikList) {
				resultMap.put(Integer.parseInt(dto.getValues()[0].getValueStr()), dto.getValues()[1].getValueStr());
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "krajinaMap.error", auth);
			return null;
		}
	}

	private byte[] getReport(DTODynCiselnik[] vuList, Map<String, Set<String>> nadusekMap, Map<Integer, Integer> hpMap, Map<Integer, String> krajinaMap, String reportPageHeader)
			throws AppException {

		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("report.page.header", reportPageHeader);

			Map<String, List<DTOReport>> datasourceMap = new HashMap<String, List<DTOReport>>();
			datasourceMap.put("CudDataSource", copyListDTO(vuList, nadusekMap, hpMap, krajinaMap));

			return generujReport(_CudConsts.PRINT_REPORT_NAME_PDF_VLAKOVY_USEK, null, paramMap, datasourceMap, null, _CudConsts.PRINT_FORMAT_PDF);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getReport.error");
			return null;
		}
	}

	private String formatValue(String value1, String value2, String value3) throws AppException {

		try {
			if (StringUtils.isValid(value1)) {

				if (StringUtils.isValid(value2)) {
					return (value1 + " " + (StringUtils.isValid(value2) ? "(" + value2 + ")" : "")).trim();

				} else if (StringUtils.isValid(value3)) {
					return value1 + " " + value3;
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "formatValue.error");
			return null;
		}
	}

	private String formatValueAsc(Set<String> set) throws AppException {

		try {
			String s = "";
			if (!StringUtils.isValid(set)) {
				return s;
			}
			for (String cislo : new TreeSet<String>(set)) {
				if (StringUtils.isValid(s)) {
					s += "+";
				}
				s += cislo;
			}
			return "(" + s + ")";

		} catch (Throwable t) {
			DBUtils.handleException(t, "formatValueAsc.error");
			return null;
		}
	}

	private String formatValueDesc(Set<String> set) throws AppException {

		try {
			String s = "";
			if (!StringUtils.isValid(set)) {
				return s;
			}
			for (String cislo : new TreeSet<String>(set)) {
				if (StringUtils.isValid(s)) {
					s = "+" + s;
				}
				s = cislo + s;
			}
			return "(" + s + ")";

		} catch (Throwable t) {
			DBUtils.handleException(t, "formatValueDesc.error");
			return null;
		}
	}

	private String lookupValue(String dopravnyNazovID, Map<Integer, Integer> hpMap, Map<Integer, String> krajinaMap) throws AppException {

		try {
			if (!StringUtils.isValid(dopravnyNazovID)) {
				return null;
			}

			Integer krajinaID = hpMap.get(Integer.parseInt(dopravnyNazovID));
			if (!StringUtils.isValid(krajinaID)) {
				return null;
			}

			return krajinaMap.get(krajinaID);

		} catch (Throwable t) {
			handleException(t, "lookupValue.error");
			return null;
		}
	}

	private List<DTOReport> copyListDTO(DTODynCiselnik[] vuList, Map<String, Set<String>> nadusekMap, Map<Integer, Integer> hpMap, Map<Integer, String> krajinaMap)
			throws AppException {

		try {
			List<String> headerList = new ArrayList<String>();

			List<DTOReport> resultList = new ArrayList<DTOReport>();

			List<String> fieldList = new ArrayList<String>();
			for (DTODynCiselnik dto : vuList) {

				fieldList.clear();

				String odKrajinaSkratka = lookupValue(dto.getValues()[12].getValueStr(), hpMap, krajinaMap);
				String doKrajinaSkratka = lookupValue(dto.getValues()[13].getValueStr(), hpMap, krajinaMap);

				fieldList.add(dto.getValues()[1].getValueStr());
				fieldList.add(dto.getValues()[2].getValueStr());
				String s1 = formatValue(dto.getValues()[3].getValueStr(), dto.getValues()[5].getValueStr(), odKrajinaSkratka);
				String s2 = formatValue(dto.getValues()[4].getValueStr(), dto.getValues()[6].getValueStr(), doKrajinaSkratka);
				String s3 = "";
				if ("T".equals(dto.getValues()[11].getValueStr())) {
					String s31 = formatValueAsc(nadusekMap.get(dto.getValues()[0].getValueStr() + "_P"));
					String s32 = formatValueDesc(nadusekMap.get(dto.getValues()[0].getValueStr() + "_N"));
					if (StringUtils.isValid(s31) && StringUtils.isValid(s32)) {
						s3 = " " + s31 + "-" + s32;
					} else {
						s3 = " " + s31 + s32;
					}
				}
				fieldList.add(s1 + " - " + s2 + s3);
				fieldList.add(dto.getValues()[7].getValueStr());
				fieldList.add(dto.getValues()[8].getValueStr());
				fieldList.add(dto.getValues()[10].getValueStr());
				fieldList.add(dto.getValues()[9].getValueStr());
				fieldList.add(dto.getValues()[11].getValueStr());

				resultList.add(createReportDTO(fieldList, headerList));
			}

			return resultList;

		} catch (Throwable t) {
			handleException(t, "copyListDTO.error");
			return null;
		}
	}

}
