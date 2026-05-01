package sk.ditec.cud.print;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class CudTTratovyUsekPrintPdfClass extends _CudPrintBaseClass implements _ICudPrint {

	@Override
	public DTODynCiselnikExport exportPrint(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnik dtoCis) throws AppException {

		try {
			DTODynCiselnikExport resultDTO = new DTODynCiselnikExport();
			resultDTO.setFileName(lookupFileName(dtoCis.getNazov(), dtoExp.getFormat()));

			dtoDyn.setTabulka(dtoCis.getTabulka());

			DTODynCiselnik[] dynCiselnikList = dynCiselnikList(auth, dtoExp, dtoDyn);
			resultDTO.setPriloha(getReport(dynCiselnikList, createPageHeaderText(dtoCis.getPrintZahlavie(), dtoDyn.getPlatnostOd())));

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

	private DTOCiselnikStlpecGui[] createMetaList() throws AppException {

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
			handleException(t, "createMetaList.error");
			return null;
		}

	}

	private DTODynCiselnik[] dynCiselnikList(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn) throws AppException {

		try {
			DTOCiselnikStlpecGui[] metaList = createMetaList();

			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setCiselnikID(dtoDyn.getCiselnikID());
			dtoF.setTabulka(dtoDyn.getTabulka());
			dtoF.setPlatnostOd(dtoDyn.getPlatnostOd());
			if ("T".equals(dtoExp.getFilter())) {
				List<DTODynValue> filterValueList = new ArrayList<DTODynValue>();
				for (DTODynValue dto : dtoDyn.getValues()) {
					filterValueList.add(dto);
				}
				while (filterValueList.size() < metaList.length) {
					filterValueList.add(new DTODynValue());
				}
				dtoF.setValues(filterValueList.toArray(new DTODynValue[filterValueList.size()]));
			}
			dtoF.setListZobrazenie("T");

			return getDelegate().getDynCiselnikRead().list(auth, new Page(1, _CudConsts.PRINT_MAX_POCET, "1_ASC"), dtoF, metaList, new HashMap<Integer, List<String>>());

		} catch (Throwable t) {
			handleException(t, "dynCiselnikList.error", auth);
			return null;
		}
	}

	private byte[] getReport(DTODynCiselnik[] dynCiselnikList, String reportPageHeader) throws AppException {

		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("report.page.header", reportPageHeader);

			Map<String, List<DTOReport>> datasourceMap = new HashMap<String, List<DTOReport>>();
			datasourceMap.put("CudDataSource", copyListDTO(dynCiselnikList));

			return generujReport(_CudConsts.PRINT_REPORT_NAME_PDF_TRATOVY_USEK, null, paramMap, datasourceMap, null, _CudConsts.PRINT_FORMAT_PDF);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getReport.error");
			return null;
		}
	}

	private List<DTOReport> copyListDTO(DTODynCiselnik[] dynCiselnikList) throws AppException {

		try {
			List<String> headerList = new ArrayList<String>();

			List<DTOReport> resultList = new ArrayList<DTOReport>();

			List<String> fieldList = new ArrayList<String>();
			for (DTODynCiselnik dto : dynCiselnikList) {

				fieldList.clear();

				fieldList.add("Traťový úsek: " + dto.getValues()[1].getValueStr());
				fieldList.add(dto.getValues()[2].getValueStr());
				fieldList.add(dto.getValues()[3].getValueStr());

				fieldList.add(dto.getValues()[4].getValueStr());
				fieldList.add(StringUtils.isValid(dto.getValues()[6].getValueStr()) ? "km" : null);
				fieldList.add(StringUtils.isValid(dto.getValues()[6].getValueStr()) ? dto.getValues()[6].getValueStr() : null);
				fieldList.add(StringUtils.isValid(dto.getValues()[8].getValueStr()) ? "=" : null);
				fieldList.add(dto.getValues()[8].getValueStr());
				fieldList.add(StringUtils.isValid(dto.getValues()[12].getValueStr()) ? "z TÚ" : null);
				fieldList.add(dto.getValues()[12].getValueStr());
				fieldList.add(StringUtils.isValid(dto.getValues()[10].getValueStr()) ? "; DÚ" : null);
				fieldList.add(dto.getValues()[10].getValueStr());

				fieldList.add(dto.getValues()[5].getValueStr());
				fieldList.add(StringUtils.isValid(dto.getValues()[7].getValueStr()) ? "km" : null);
				fieldList.add(dto.getValues()[7].getValueStr());
				fieldList.add(StringUtils.isValid(dto.getValues()[9].getValueStr()) ? "=" : null);
				fieldList.add(dto.getValues()[9].getValueStr());
				fieldList.add(StringUtils.isValid(dto.getValues()[13].getValueStr()) ? "z TÚ" : null);
				fieldList.add(dto.getValues()[13].getValueStr());
				fieldList.add(StringUtils.isValid(dto.getValues()[11].getValueStr()) ? "; DÚ" : null);
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
