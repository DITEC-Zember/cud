package sk.ditec.cud.print;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import sk.ditec.cud.dto.DTODynValue;
import sk.ditec.cud.dto.DTOReport;
import sk.ditec.cud.utils._CudConsts;

public class CudTDefinicnyUsekPrintPdfClass extends _CudPrintBaseClass implements _ICudPrint {

	@Override
	public DTODynCiselnikExport exportPrint(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnik dtoCis) throws AppException {

		try {
			DTODynCiselnikExport resultDTO = new DTODynCiselnikExport();
			resultDTO.setFileName(lookupFileName(dtoCis.getNazov(), dtoExp.getFormat()));

			dtoDyn.setTabulka(dtoCis.getTabulka());

			Map<String, DTODynValue[]> tuMap = tratovyUsekMap(auth, dtoExp, dtoDyn);

			Map<String, List<DTODynValue[]>> duMap = definicnyUsekMap(auth, dtoExp, dtoDyn);

			resultDTO.setPriloha(getReport(duMap, tuMap, createPageHeaderText(dtoCis.getPrintZahlavie(), dtoDyn.getPlatnostOd())));

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

	private DTOCiselnikStlpecGui[] createMetaListForDefinicnyUsek() throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			resultList.add(createMetaAtribut(_CudConsts.NAZOV_DEFINICNY_USEK_ID, _CudConsts.CISELNIK_STLPEC_TYP_PK, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null,
					null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_TRATOVY_USEK, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 4, null,
					_CudConsts.TABULKA_T_TRATOVY_USEK, _CudConsts.NAZOV_TRATOVY_USEK_ID, _CudConsts.NAZOV_CISLO, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_CISLO, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_STRING, 2, null, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_OD, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 100, null,
					_CudConsts.TABULKA_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_DOPRAVNY_NAZOV_ID, _CudConsts.NAZOV_NAZOV, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_DO, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 100, null,
					_CudConsts.TABULKA_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_DOPRAVNY_NAZOV_ID, _CudConsts.NAZOV_NAZOV, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_KM_OD, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_KM_DO, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null, null));
			resultList
					.add(createMetaAtribut(_CudConsts.NAZOV_KOLAJNOST, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_INTEGER, 5, null, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_ELEKTRICKA_TRAKCIA, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 1, null,
					_CudConsts.TABULKA_T_ELEKTRICKA_TRAKCIA, _CudConsts.NAZOV_ELEKTRICKA_TRAKCIA_ID, _CudConsts.NAZOV_ID_TYP_ELEKTRICKEJ_TRAKCIE,
					_CudConsts.TABULKA_T_TYP_ELEKTRICKEJ_TRAKCIE, _CudConsts.NAZOV_TYP_ELEKTRICKEJ_TRAKCIE_ID, _CudConsts.NAZOV_OZNACENIE));

			resultList
					.add(createMetaAtribut(_CudConsts.NAZOV_STAVEBNA_DLZKA, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null, null));

			return resultList.toArray(new DTOCiselnikStlpecGui[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "createMetaListForDefinicnyUsek.error");
			return null;
		}
	}

	private Map<String, List<DTODynValue[]>> definicnyUsekMap(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setCiselnikID(dtoDyn.getCiselnikID());
			dtoF.setTabulka(dtoDyn.getTabulka());
			dtoF.setPlatnostOd(dtoDyn.getPlatnostOd());
			if ("T".equals(dtoExp.getFilter())) {
				dtoF.setValues(dtoDyn.getValues());
			}
			dtoF.setListZobrazenie("T");

			DTODynCiselnik[] dynCiselnikList = getDelegate().getDynCiselnikRead().list(auth, new Page(1, _CudConsts.PRINT_MAX_POCET, "4_ASC"), dtoF,
					createMetaListForDefinicnyUsek(), new HashMap<Integer, List<String>>());

			Map<String, List<DTODynValue[]>> resultMap = new HashMap<String, List<DTODynValue[]>>();

			for (DTODynCiselnik dto : dynCiselnikList) {
				if (!StringUtils.isValid(resultMap.get(dto.getValues()[1].getValueStr()))) {
					resultMap.put(dto.getValues()[1].getValueStr(), new ArrayList<DTODynValue[]>());
				}
				resultMap.get(dto.getValues()[1].getValueStr()).add(dto.getValues());
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "definicnyUsekMap.error", auth);
			return null;
		}
	}

	private DTOCiselnikStlpecGui[] createMetaListForTratovyUsek() throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			resultList.add(createMetaAtribut(_CudConsts.NAZOV_TRATOVY_USEK_ID, _CudConsts.CISELNIK_STLPEC_TYP_PK, _CudConsts.DB_TYP_INTEGER, 10, null, null, null, null, null,
					null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_CISLO, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_STRING, 4, null, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_OD, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 100, null,
					_CudConsts.TABULKA_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_DOPRAVNY_NAZOV_ID, _CudConsts.NAZOV_NAZOV, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DOPRAVNY_NAZOV_DO, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 100, null,
					_CudConsts.TABULKA_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_DOPRAVNY_NAZOV_ID, _CudConsts.NAZOV_NAZOV, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_KM_OD, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_KM_DO, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_KM_TOTOZNOSTI_OD, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null,
					null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_KM_TOTOZNOSTI_DO, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null,
					null));
			resultList
					.add(createMetaAtribut(_CudConsts.NAZOV_KM_TOTOZNY_OD, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null, null));
			resultList
					.add(createMetaAtribut(_CudConsts.NAZOV_KM_TOTOZNY_DO, _CudConsts.CISELNIK_STLPEC_TYP_AT, _CudConsts.DB_TYP_DOUBLE, 8, 3, null, null, null, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DEFINICNY_USEK_T_OD, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 2, null,
					_CudConsts.TABULKA_T_DEFINICNY_USEK, _CudConsts.NAZOV_DEFINICNY_USEK_ID, _CudConsts.NAZOV_CISLO, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DEFINICNY_USEK_T_DO, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 2, null,
					_CudConsts.TABULKA_T_DEFINICNY_USEK, _CudConsts.NAZOV_DEFINICNY_USEK_ID, _CudConsts.NAZOV_CISLO, null, null, null));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DEFINICNY_USEK_T_OD, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 4, null,
					_CudConsts.TABULKA_T_DEFINICNY_USEK, _CudConsts.NAZOV_DEFINICNY_USEK_ID, _CudConsts.NAZOV_ID_TRATOVY_USEK, _CudConsts.TABULKA_T_TRATOVY_USEK,
					_CudConsts.NAZOV_TRATOVY_USEK_ID, _CudConsts.NAZOV_CISLO));
			resultList.add(createMetaAtribut(_CudConsts.NAZOV_ID_DEFINICNY_USEK_T_DO, _CudConsts.CISELNIK_STLPEC_TYP_FK, _CudConsts.DB_TYP_STRING, 4, null,
					_CudConsts.TABULKA_T_DEFINICNY_USEK, _CudConsts.NAZOV_DEFINICNY_USEK_ID, _CudConsts.NAZOV_ID_TRATOVY_USEK, _CudConsts.TABULKA_T_TRATOVY_USEK,
					_CudConsts.NAZOV_TRATOVY_USEK_ID, _CudConsts.NAZOV_CISLO));

			return resultList.toArray(new DTOCiselnikStlpecGui[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "createMetaListForTratovyUsek.error");
			return null;
		}

	}

	private Map<String, DTODynValue[]> tratovyUsekMap(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTOCiselnik dtoCis = getDelegate().getCiselnikRead().readLight(auth, _CudConsts.TABULKA_T_TRATOVY_USEK);

			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setCiselnikID(dtoCis.getCiselnikID());
			dtoF.setTabulka(_CudConsts.TABULKA_T_TRATOVY_USEK);
			dtoF.setPlatnostOd(dtoDyn.getPlatnostOd());
			dtoF.setListZobrazenie("T");

			DTODynCiselnik[] dynCiselnikList = getDelegate().getDynCiselnikRead().list(auth, new Page(1, _CudConsts.PRINT_MAX_POCET, "1_ASC"), dtoF,
					createMetaListForTratovyUsek(), new HashMap<Integer, List<String>>());

			Map<String, DTODynValue[]> resultMap = new HashMap<String, DTODynValue[]>();
			for (DTODynCiselnik dto : dynCiselnikList) {
				resultMap.put(dto.getValues()[1].getValueStr(), dto.getValues());
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "tratovyUsekMap.error", auth);
			return null;
		}
	}

	private byte[] getReport(Map<String, List<DTODynValue[]>> duMap, Map<String, DTODynValue[]> tuMap, String reportPageHeader) throws AppException {

		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("report.page.header", reportPageHeader);

			Map<String, List<DTOReport>> datasourceMap = new HashMap<String, List<DTOReport>>();

			return generujReport(_CudConsts.PRINT_REPORT_NAME_PDF_DEFINICNY_USEK, null, paramMap, datasourceMap, copyListDTO(duMap, tuMap), _CudConsts.PRINT_FORMAT_PDF);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getReport.error");
			return null;
		}
	}

	private String formatValue(String value1, String value2) throws AppException {

		try {
			String s = StringUtils.isValid(value1) ? value1 : "";

			if (StringUtils.isValid(value2)) {
				if (StringUtils.isValid(s)) {
					s += System.getProperty("line.separator");
				}
				s += value2;
			}

			return s;

		} catch (Throwable t) {
			DBUtils.handleException(t, "formatValue.error");
			return null;
		}
	}

	private List<DTOReport> copyListDTO(Map<String, List<DTODynValue[]>> duMap, Map<String, DTODynValue[]> tuMap) throws AppException {

		try {
			List<String> headerList = new ArrayList<String>();

			List<DTOReport> resultList = new ArrayList<DTOReport>();

			List<String> fieldList = new ArrayList<String>();
			for (String tuCislo : new TreeSet<String>(tuMap.keySet())) {

				DTODynValue[] tuList = tuMap.get(tuCislo);

				List<DTODynValue[]> duList = duMap.get(tuCislo);
				if (!StringUtils.isValid(duList)) {
					continue;
				}

				for (DTODynValue[] values : duList) {

					fieldList.clear();

					fieldList.add("Traťový úsek: " + tuList[1].getValueStr());
					fieldList.add(tuList[2].getValueStr());
					fieldList.add(tuList[3].getValueStr());

					fieldList.add(tuList[4].getValueStr());
					fieldList.add(StringUtils.isValid(tuList[6].getValueStr()) ? "km" : null);
					fieldList.add(StringUtils.isValid(tuList[6].getValueStr()) ? tuList[6].getValueStr() : null);
					fieldList.add(StringUtils.isValid(tuList[8].getValueStr()) ? "=" : null);
					fieldList.add(tuList[8].getValueStr());
					fieldList.add(StringUtils.isValid(tuList[12].getValueStr()) ? "z TÚ" : null);
					fieldList.add(tuList[12].getValueStr());
					fieldList.add(StringUtils.isValid(tuList[10].getValueStr()) ? "; DÚ" : null);
					fieldList.add(tuList[10].getValueStr());

					fieldList.add(tuList[5].getValueStr());
					fieldList.add(StringUtils.isValid(tuList[7].getValueStr()) ? "km" : null);
					fieldList.add(tuList[7].getValueStr());
					fieldList.add(StringUtils.isValid(tuList[9].getValueStr()) ? "=" : null);
					fieldList.add(tuList[9].getValueStr());
					fieldList.add(StringUtils.isValid(tuList[13].getValueStr()) ? "z TÚ" : null);
					fieldList.add(tuList[13].getValueStr());
					fieldList.add(StringUtils.isValid(tuList[11].getValueStr()) ? "; DÚ" : null);
					fieldList.add(tuList[11].getValueStr());

					fieldList.add(values[2].getValueStr());
					fieldList.add(formatValue(values[3].getValueStr(), values[4].getValueStr()));
					fieldList.add(formatValue(values[5].getValueStr(), values[6].getValueStr()));
					fieldList.add(values[7].getValueStr());
					fieldList.add(values[8].getValueStr());
					fieldList.add(String.valueOf(Double.valueOf(values[9].getValueStr()).intValue()));

					resultList.add(createReportDTO(fieldList, headerList));
				}
			}

			return resultList;

		} catch (Throwable t) {
			handleException(t, "copyListDTO.error");
			return null;
		}
	}

}
