package sk.ditec.cud.print;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTODynCiselnik;
import sk.ditec.cud.dto.DTODynCiselnikExport;
import sk.ditec.cud.dto.DTOReport;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;

public class CudZmenaStlpecPrintClass extends _CudPrintBaseClass implements _ICudPrint {

	private List<DTOZmenaStlpec> zmenaStlpecList(AuthInfo auth, Page page, Integer ciselnikID, String wfTodoPotvrdeny, Date zmenaPlatnostOd, String wfDefTyp) throws AppException {

		try {
			Map<Integer, DTOCiselnikStlpec> csMap = getDelegate().getCiselnikStlpecRead().mapForPrint(auth, ciselnikID);

			List<DTOCiselnikStlpec> fkList = new ArrayList<DTOCiselnikStlpec>();
			for (DTOCiselnikStlpec dto : csMap.values()) {
				if (StringUtils.isValid(dto.getFk1IDCiselnik())) {
					fkList.add(dto);
				}
			}

			List<DTOZmenaStlpec> zsList = getDelegate().getZmenaStlpecRead().listForPrint(auth, page, ciselnikID, wfTodoPotvrdeny, zmenaPlatnostOd, wfDefTyp, fkList);

			for (DTOZmenaStlpec dto : zsList) {
				DTOCiselnikStlpec dtoCS = csMap.get(dto.getIDCiselnikStlpec());
				if (_CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getDbTyp())) {
					if (StringUtils.isValid(dto.getOldValue())) {
						dto.setOldValue(getDelegate().getDynCiselnikRead().doubleValueFormat(dto.getOldValue(), dtoCS.getDecimals()));
					}
					if (StringUtils.isValid(dto.getNewValue())) {
						dto.setNewValue(getDelegate().getDynCiselnikRead().doubleValueFormat(dto.getNewValue(), dtoCS.getDecimals()));
					}
				} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getDbTyp())) {
					if (StringUtils.isValid(dto.getOldValue())) {
						dto.setOldValue("T".equals(dto.getOldValue()) ? "Áno" : "Nie");
					}
					if (StringUtils.isValid(dto.getNewValue())) {
						dto.setNewValue("T".equals(dto.getNewValue()) ? "Áno" : "Nie");
					}
				}
			}

			return zsList;

		} catch (Throwable t) {
			handleException(t, "zmenaStlpecList.error", auth);
			return null;
		}
	}

	@Override
	public DTODynCiselnikExport exportPrint(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnik dtoCis) throws AppException {

		try {
			DTODynCiselnikExport resultDTO = new DTODynCiselnikExport();
			resultDTO.setFileName(lookupFileName(dtoCis.getNazov(), dtoExp.getFormat()));

			Page page = new Page(dtoExp.getPage(), dtoExp.getPageSize(), "1_ASC");

			List<DTOZmenaStlpec> zsList = null;
			if (_CudConsts.PRINT_TYP_ZMENENE.equals(dtoExp.getTyp())) {
				zsList = zmenaStlpecList(auth, page, dtoCis.getCiselnikID(), "T", dtoDyn.getPlatnostOd(), _CudConsts.WF_DEF_TYP_OV);
			} else if (_CudConsts.PRINT_TYP_SCH.equals(dtoExp.getTyp())) {
				zsList = zmenaStlpecList(auth, page, dtoCis.getCiselnikID(), null, dtoDyn.getPlatnostOd(), _CudConsts.WF_DEF_TYP_SC);
			}

			if (_CudConsts.PRINT_FORMAT_XLS.equals(dtoExp.getFormat())) {
				resultDTO.setPriloha(getXlsReport(zsList, dtoExp.getFormat()));
			} else if (_CudConsts.PRINT_FORMAT_PDF.equals(dtoExp.getFormat()) || _CudConsts.PRINT_FORMAT_RTF.equals(dtoExp.getFormat())) {

				String reportPageHeader = createPageHeaderText(dtoCis.getPrintZahlavie(), dtoDyn.getPlatnostOd());
				resultDTO.setPriloha(getPdfReport(zsList, dtoExp.getFormat(), reportPageHeader));
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "exportPrint.error", auth);
			return null;
		}
	}

	@Override
	public DTODynCiselnikExport exportPrintKontrola(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnik dtoCis) throws AppException {

		try {
			if (StringUtils.isValid(dtoExp.getPage()) && StringUtils.isValid(dtoExp.getPageSize())) {
				return null;
			}

			int maxPocet = _CudConsts.PRINT_FORMAT_RTF.equals(dtoExp.getFormat()) ? _CudConsts.PRINT_MAX_POCET_RTF : _CudConsts.PRINT_MAX_POCET;

			Integer pocet = null;
			if (_CudConsts.PRINT_TYP_ZMENENE.equals(dtoExp.getTyp())) {
				pocet = getDelegate().getZmenaStlpecRead().countForPrint(auth, dtoCis.getCiselnikID(), "T", dtoExp.getPlatnostOd(), _CudConsts.WF_DEF_TYP_OV);
			} else if (_CudConsts.PRINT_TYP_SCH.equals(dtoExp.getTyp())) {
				pocet = getDelegate().getZmenaStlpecRead().countForPrint(auth, dtoCis.getCiselnikID(), null, dtoExp.getPlatnostOd(), _CudConsts.WF_DEF_TYP_SC);
			}

			if (pocet.intValue() > maxPocet) {

				int n = pocet / maxPocet;
				List<Integer> list = new ArrayList<Integer>();
				for (int i = 1; i <= n + 1; i++) {
					list.add(i);
				}
				dtoExp.setPageList(list.toArray(new Integer[list.size()]));
				dtoExp.setPageSize(maxPocet);
				dtoExp.setErrorMsg(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_2001, Integer.toString(maxPocet)));
				return dtoExp;
			}

			dtoExp.setPage(1);
			dtoExp.setPageSize(maxPocet);

			return dtoExp;

		} catch (Throwable t) {
			handleException(t, "exportPrintKontrola.error", auth);
			return null;
		}
	}

	private List<DTOReport> copyListDTOForPdf(List<DTOZmenaStlpec> zmenaStlpecList) throws AppException {

		try {
			List<String> headerList = new ArrayList<String>();

			List<DTOReport> resultList = new ArrayList<DTOReport>();

			List<String> fieldList = new ArrayList<String>();
			for (DTOZmenaStlpec dto : zmenaStlpecList) {

				fieldList.clear();

				fieldList.add(dto.getCiselnikStlpecNazov());
				fieldList.add(dto.getOldValue());
				fieldList.add(dto.getNewValue());

				String s = _CudConsts.TEXT_PLATNOST_OD + ": " + _CudConsts.DATE_FORMAT.format(dto.getZmenaPlatnostOd());
				s += ",  " + _CudConsts.TEXT_ID + ": " + dto.getZmenaRowID();
				s += ",  " + _CudConsts.TEXT_OPERACIA + ": " + _CudLookupUtils.lookupZmenaOperaciaNazov(dto.getZmenaOperacia());
				fieldList.add(s);

				resultList.add(createReportDTO(fieldList, headerList));
			}

			return resultList;

		} catch (Throwable t) {
			handleException(t, "copyListDTOForPdf.error");
			return null;
		}
	}

	private List<DTOReport> copyListDTOForXls(List<DTOZmenaStlpec> zmenaStlpecList) throws AppException {

		try {
			List<String> headerList = new ArrayList<String>();
			headerList.add(_CudConsts.TEXT_PLATNOST_OD);
			headerList.add(_CudConsts.TEXT_ID);
			headerList.add(_CudConsts.TEXT_OPERACIA);
			headerList.add(_CudConsts.TEXT_POLOZKA);
			headerList.add(_CudConsts.TEXT_STARA_HODNOTA);
			headerList.add(_CudConsts.TEXT_NOVA_HODNOTA);

			List<DTOReport> resultList = new ArrayList<DTOReport>();

			List<String> fieldList = new ArrayList<String>();
			for (DTOZmenaStlpec dto : zmenaStlpecList) {

				fieldList.clear();

				fieldList.add(_CudConsts.DATE_FORMAT.format(dto.getZmenaPlatnostOd()));
				fieldList.add(StringUtils.isValid(dto.getZmenaRowID()) ? dto.getZmenaRowID().toString() : null);
				fieldList.add(_CudLookupUtils.lookupZmenaOperaciaNazov(dto.getZmenaOperacia()));
				fieldList.add(dto.getCiselnikStlpecNazov());
				fieldList.add(dto.getOldValue());
				fieldList.add(dto.getNewValue());

				resultList.add(createReportDTO(fieldList, headerList));
			}

			return resultList;

		} catch (Throwable t) {
			handleException(t, "copyListDTOForXls.error");
			return null;
		}
	}

	private byte[] getXlsReport(List<DTOZmenaStlpec> zmenaStlpecList, String format) throws AppException {

		try {
			Map<String, String> paramMap = new HashMap<String, String>();

			Map<String, List<DTOReport>> datasourceMap = new HashMap<String, List<DTOReport>>();
			datasourceMap.put("UniTableDataSource", copyListDTOForXls(zmenaStlpecList));

			return generujReport(_CudConsts.PRINT_REPORT_NAME_XLS_ZMENA_STLPEC, null, paramMap, datasourceMap, null, format);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getXlsReport.error");
			return null;
		}
	}

	private byte[] getPdfReport(List<DTOZmenaStlpec> zmenaStlpecList, String format, String reportPageHeader) throws AppException {

		try {
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("report.col.header1", _CudConsts.TEXT_POLOZKA);
			paramMap.put("report.col.header2", _CudConsts.TEXT_STARA_HODNOTA);
			paramMap.put("report.col.header3", _CudConsts.TEXT_NOVA_HODNOTA);
			paramMap.put("report.page.header", reportPageHeader);

			Map<String, List<DTOReport>> datasourceMap = new HashMap<String, List<DTOReport>>();

			List<DTOReport> dataList = copyListDTOForPdf(zmenaStlpecList);

			return generujReport(_CudConsts.PRINT_REPORT_NAME_PDF_ZMENA_STLPEC, null, paramMap, datasourceMap, dataList, format);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getXlsReport.error");
			return null;
		}
	}

}
