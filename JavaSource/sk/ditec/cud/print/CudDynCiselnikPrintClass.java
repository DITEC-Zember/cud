package sk.ditec.cud.print;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynCiselnik;
import sk.ditec.cud.dto.DTODynCiselnikExport;
import sk.ditec.cud.dto.DTODynValue;
import sk.ditec.cud.dto.DTOReport;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudResultUtils;

public class CudDynCiselnikPrintClass extends _CudPrintBaseClass implements _ICudPrint {

	private Logger log = LoggerFactory.getLogger(CudDynCiselnikPrintClass.class);

	protected Integer dynCiselnikCount(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnikStlpecGui[] metaList) throws AppException {

		try {
			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setCiselnikID(dtoDyn.getCiselnikID());
			dtoF.setTabulka(dtoDyn.getTabulka());
			dtoF.setPlatnostOd(dtoDyn.getPlatnostOd());
			if ("T".equals(dtoExp.getFilter())) {
				Map<Integer, DTODynValue> mapa = new HashMap<Integer, DTODynValue>();
				for (DTODynValue dtoDynValue : dtoDyn.getValues()) {
					if (StringUtils.isValid(dtoDynValue) && StringUtils.isValid(dtoDynValue.getValueStr())) {
						mapa.put(dtoDynValue.getValueID(), dtoDynValue);
					}
				}
				List<DTODynValue> list = new ArrayList<DTODynValue>();
				for (DTOCiselnikStlpecGui dtoCS : metaList) {
					list.add(mapa.get(dtoCS.getCiselnikStlpecGuiID()));
				}
				dtoF.setValues(list.toArray(new DTODynValue[list.size()]));
			}
			dtoF.setListZobrazenie("T");

			return getDelegate().getDynCiselnikRead().count(auth, dtoF, metaList);

		} catch (Throwable t) {
			handleException(t, "dynCiselnikCount.error", auth);
			return null;
		}
	}

	protected DTODynCiselnik[] dynCiselnikList(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnikStlpecGui[] metaList) throws AppException {

		try {
			DTODynCiselnik dtoF = new DTODynCiselnik();
			dtoF.setCiselnikID(dtoDyn.getCiselnikID());
			dtoF.setTabulka(dtoDyn.getTabulka());
			dtoF.setPlatnostOd(dtoDyn.getPlatnostOd());
			dtoF.setDynFilterTyp(dtoDyn.getDynFilterTyp());
			dtoF.setObjektStlpecList(getDelegate().getGuiRead().opravnenieList(auth, dtoDyn.getCiselnikID(), null));
			if ("T".equals(dtoExp.getFilter())) {
				Map<Integer, DTODynValue> mapa = new HashMap<Integer, DTODynValue>();
				for (DTODynValue dtoDynValue : dtoDyn.getValues()) {
					if (StringUtils.isValid(dtoDynValue) && StringUtils.isValid(dtoDynValue.getValueStr())) {
						mapa.put(dtoDynValue.getValueID(), dtoDynValue);
					}
				}
				List<DTODynValue> list = new ArrayList<DTODynValue>();
				for (DTOCiselnikStlpecGui dtoCS : metaList) {
					list.add(mapa.get(dtoCS.getCiselnikStlpecGuiID()));
				}
				dtoF.setValues(list.toArray(new DTODynValue[list.size()]));
			}
			dtoF.setListZobrazenie("T");

			DTODynCiselnik[] resultList = getDelegate().getDynCiselnikRead().list(auth, new Page(dtoExp.getPage(), dtoExp.getPageSize(), "1_ASC"), dtoF, metaList);

			return resultList;

		} catch (Throwable t) {
			handleException(t, "dynCiselnikList.error", auth);
			return null;
		}
	}

	private void calculatedPdfSirka(DTOCiselnikStlpecGui[] metaList) throws AppException {

		try {
			int sum = 0;
			for (DTOCiselnikStlpecGui dto : metaList) {
				if (!StringUtils.isValid(dto.getListSirka())) {
					log.error("Chyba v metadatach, prepocet sirok konci!");
					return;
				}
				if (dto.getListSirka().intValue() < 40) {
					dto.setListSirka(40);
				}
				sum += dto.getListSirka();
			}

			int sumNovy = 0;
			for (DTOCiselnikStlpecGui dto : metaList) {
				int percento = (int) (100 * dto.getListSirka() / sum);
				dto.setListSirka((int) (_CudConsts.PRINT_PDF_WIDTH * percento) / 100);
				sumNovy += dto.getListSirka();
			}

			int minSirka = 10000;
			for (DTOCiselnikStlpecGui dto : metaList) {
				if (dto.getListSirka().intValue() <= minSirka) {
					minSirka = dto.getListSirka().intValue();
				}
			}
			List<DTOCiselnikStlpecGui> minList = new ArrayList<DTOCiselnikStlpecGui>();
			for (DTOCiselnikStlpecGui dto : metaList) {
				if (dto.getListSirka().intValue() == minSirka || dto.getListSirka().intValue() < 40) {
					minList.add(dto);
				}
			}

			while (sumNovy < _CudConsts.PRINT_PDF_WIDTH) {
				for (DTOCiselnikStlpecGui dto : minList) {
					if (sumNovy < _CudConsts.PRINT_PDF_WIDTH) {
						dto.setListSirka(dto.getListSirka() + 1);
						sumNovy++;
					}
				}
			}

			sum = 0;
			for (DTOCiselnikStlpecGui dto : metaList) {
				if (StringUtils.isValid(dto.getListSirka())) {
					sum += dto.getListSirka();
				}
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "calculatedListSirka.error");
		}
	}

	private Integer getMinSirka(Integer sirka1, Integer sirka2, Integer sirka3) throws AppException {

		try {
			if (!StringUtils.isValid(sirka1)) {
				sirka1 = Integer.MAX_VALUE;
			}
			if (!StringUtils.isValid(sirka2)) {
				sirka2 = Integer.MAX_VALUE;
			}
			if (!StringUtils.isValid(sirka3)) {
				sirka3 = Integer.MAX_VALUE;
			}

			return Math.min(Math.min(sirka1, sirka2), sirka3);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getMinSirka.error");
			return null;
		}
	}

	private void calculatedXlsSirka(DTOCiselnikStlpecGui[] metaList, DTODynCiselnik[] dynCiselnikList) throws AppException {

		try {
			int index = 0;
			for (DTOCiselnikStlpecGui dto : metaList) {

				dto.setListSirka(getMinSirka(dto.getListSirka(), dto.getFormSirka(), dto.getPopupSirka()));

				int sirka = (((dto.getNadpis().length() + 5) / 2) + 1) * 10;
				if (sirka > dto.getListSirka().intValue()) {
					dto.setListSirka(sirka);
				}

				for (DTODynCiselnik dtoDyn : dynCiselnikList) {
					DTODynValue dynValue = dtoDyn.getValues()[index];
					if (StringUtils.isValid(dynValue.getValueStr())) {
						sirka = (((dynValue.getValueStr().length() + 2) / 2) + 1) * 10;
						if (sirka > dto.getListSirka().intValue()) {
							dto.setListSirka(sirka);
						}
					}
				}

				index++;
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "calculatedXlsSirka.error");
		}
	}

	@Override
	public DTODynCiselnikExport exportPrint(AuthInfo auth, DTODynCiselnikExport dtoExp, DTODynCiselnik dtoDyn, DTOCiselnik dtoCis) throws AppException {

		try {
			DTODynCiselnikExport resultDTO = new DTODynCiselnikExport();
			resultDTO.setFileName(lookupFileName(dtoCis.getNazov(), dtoExp.getFormat()));

			dtoDyn.setTabulka(dtoCis.getTabulka());

			if (_CudConsts.PRINT_FORMAT_XLS.equals(dtoExp.getFormat())) {

				DTOCiselnikStlpecGui[] metaList = getDelegate().getCiselnikStlpecGuiRead().listForPrint(auth, dtoDyn.getCiselnikID(), dtoDyn.getPlatnostOd(), "T", "T");
				DTODynCiselnik[] dynCiselnikList = dynCiselnikList(auth, dtoExp, dtoDyn, metaList);
				calculatedXlsSirka(metaList, dynCiselnikList);
				resultDTO.setPriloha(getXlsReport(metaList, dynCiselnikList, dtoDyn.getTabulka(), dtoExp.getFormat()));

			} else if (_CudConsts.PRINT_FORMAT_PDF.equals(dtoExp.getFormat()) || _CudConsts.PRINT_FORMAT_RTF.equals(dtoExp.getFormat())) {

				DTOCiselnikStlpecGui[] metaList = getDelegate().getCiselnikStlpecGuiRead().listForPrint(auth, dtoDyn.getCiselnikID(), dtoDyn.getPlatnostOd(), "T", null);
				DTODynCiselnik[] dynCiselnikList = dynCiselnikList(auth, dtoExp, dtoDyn, metaList);
				calculatedPdfSirka(metaList);
				resultDTO.setPriloha(getPdfReport(metaList, dynCiselnikList, dtoCis, dtoExp.getFormat(), dtoDyn.getPlatnostOd()));

			} else if (_CudConsts.PRINT_FORMAT_XML.equals(dtoExp.getFormat())) {

				DTOCiselnikStlpecGui[] metaList = getDelegate().getCiselnikStlpecGuiRead().listForPrint(auth, dtoDyn.getCiselnikID(), dtoDyn.getPlatnostOd(), "T", "T");
				DTODynCiselnik[] dynCiselnikList = dynCiselnikList(auth, dtoExp, dtoDyn, metaList);
				resultDTO.setPriloha(getXmlReport(metaList, dynCiselnikList));
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

			dtoDyn.setTabulka(dtoCis.getTabulka());

			DTOCiselnikStlpecGui[] metaList = null;
			if (_CudConsts.PRINT_FORMAT_XLS.equals(dtoExp.getFormat()) || _CudConsts.PRINT_FORMAT_XML.equals(dtoExp.getFormat())) {
				metaList = getDelegate().getCiselnikStlpecGuiRead().listForPrint(auth, dtoDyn.getCiselnikID(), dtoDyn.getPlatnostOd(), "T", "T");
			} else if (_CudConsts.PRINT_FORMAT_PDF.equals(dtoExp.getFormat()) || _CudConsts.PRINT_FORMAT_RTF.equals(dtoExp.getFormat())) {
				metaList = getDelegate().getCiselnikStlpecGuiRead().listForPrint(auth, dtoDyn.getCiselnikID(), dtoDyn.getPlatnostOd(), "T", null);
			}

			int maxPocet = _CudConsts.PRINT_FORMAT_RTF.equals(dtoExp.getFormat()) ? _CudConsts.PRINT_MAX_POCET_RTF : _CudConsts.PRINT_MAX_POCET;

			Integer pocet = dynCiselnikCount(auth, dtoExp, dtoDyn, metaList);
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

	protected byte[] getXlsReport(DTOCiselnikStlpecGui[] metaList, DTODynCiselnik[] dataList, String tabulka, String format) throws AppException {

		try {
			String jrxml = _CudConsts.PRINT_XLS_BODY;
			jrxml = StringUtils.replaceAll(jrxml, "{ciselnikName}", tabulka);

			String fields = "";
			for (int i = 1; i < metaList.length + 1; i++) {
				String s = FrameworkUtils.getConfigProperty("cud", "print.field");
				fields += "\n" + StringUtils.replaceAll(s, "{cudIndex}", Integer.toString(i));
			}
			jrxml = StringUtils.replaceAll(jrxml, "{cudFields}", fields);

			String headers = "";
			for (int i = 1; i < metaList.length + 1; i++) {
				String s = _CudConsts.PRINT_HEADER;
				headers += "\n" + StringUtils.replaceAll(s, "{cudIndex}", Integer.toString(i));
			}
			jrxml = StringUtils.replaceAll(jrxml, "{cudHeaders}", headers);

			String variables = "";
			for (int i = 1; i < metaList.length + 1; i++) {
				String s = _CudConsts.PRINT_VARIABLE;
				variables += "\n" + StringUtils.replaceAll(s, "{cudIndex}", Integer.toString(i));
			}
			jrxml = StringUtils.replaceAll(jrxml, "{cudVariables}", variables);

			String columns = "";
			int i = 1;
			for (DTOCiselnikStlpecGui dto : metaList) {
				String s = getTypedXlsColumn(dto.getCiselnikStlpecDbTyp());
				s = StringUtils.replaceAll(s, "{cudIndex}", Integer.toString(i++));
				s = StringUtils.replaceAll(s, "{cudColumnWidth}", Integer.toString(dto.getListSirka()));
				columns += "\n" + StringUtils.replaceAll(s, "{cudTextAlignment}", "Left");
			}
			jrxml = StringUtils.replaceAll(jrxml, "{cudColumns}", columns);

			Map<String, List<DTOReport>> datasourceMap = new HashMap<String, List<DTOReport>>();
			datasourceMap.put("UniTableDataSource", copyListDTO(dataList, metaList));

			System.out.println(jrxml);

			return generujReport(null, jrxml.getBytes(), new HashMap<String, String>(), datasourceMap, null, format);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getXlsReport.error");
			return null;
		}
	}

	private String getTypedXlsColumn(String dbTyp) {
		if (dbTyp == null || dbTyp.equals(_CudConsts.DB_TYP_STRING) || dbTyp.equals(_CudConsts.DB_TYP_BOOLEAN)) {
			return _CudConsts.PRINT_XLS_COLUMN_STRING != null ? _CudConsts.PRINT_XLS_COLUMN_STRING : _CudConsts.PRINT_XLS_COLUMN;
		} else if (dbTyp.equals(_CudConsts.DB_TYP_INTEGER)) {
			return _CudConsts.PRINT_XLS_COLUMN_INTEGER != null ? _CudConsts.PRINT_XLS_COLUMN_INTEGER : _CudConsts.PRINT_XLS_COLUMN;
		} else if (dbTyp.equals(_CudConsts.DB_TYP_DOUBLE)) {
			return _CudConsts.PRINT_XLS_COLUMN_DOUBLE != null ? _CudConsts.PRINT_XLS_COLUMN_DOUBLE : _CudConsts.PRINT_XLS_COLUMN;
		} else if (dbTyp.equals(_CudConsts.DB_TYP_DATE)) {
			return _CudConsts.PRINT_XLS_COLUMN_DATE != null ? _CudConsts.PRINT_XLS_COLUMN_DATE : _CudConsts.PRINT_XLS_COLUMN;
		}

		return _CudConsts.PRINT_XLS_COLUMN;
	}

	protected byte[] getPdfReport(DTOCiselnikStlpecGui[] metaList, DTODynCiselnik[] dataList, DTOCiselnik dtoCis, String format, Date platnostOd) throws AppException {

		try {
			String jrxml = _CudConsts.PRINT_PDF_BODY;
			jrxml = StringUtils.replaceAll(jrxml, "{ciselnikName}", dtoCis.getTabulka());

			String fields = "";
			for (int i = 1; i < metaList.length + 1; i++) {
				String s = FrameworkUtils.getConfigProperty("cud", "print.field");
				fields += "\n" + StringUtils.replaceAll(s, "{cudIndex}", Integer.toString(i));
			}
			jrxml = StringUtils.replaceAll(jrxml, "{cudFields}", fields);

			String headers = "";
			for (int i = 1; i < metaList.length + 1; i++) {
				String s = _CudConsts.PRINT_HEADER;
				headers += "\n" + StringUtils.replaceAll(s, "{cudIndex}", Integer.toString(i));
			}
			jrxml = StringUtils.replaceAll(jrxml, "{cudHeaders}", headers);

			String variables = "";
			for (int i = 1; i < metaList.length + 1; i++) {
				String s = _CudConsts.PRINT_VARIABLE;
				variables += "\n" + StringUtils.replaceAll(s, "{cudIndex}", Integer.toString(i));
			}
			jrxml = StringUtils.replaceAll(jrxml, "{cudVariables}", variables);

			String columns = "";
			int i = 1;
			for (DTOCiselnikStlpecGui dto : metaList) {

				String s = _CudConsts.PRINT_PDF_COLUMN;
				s = StringUtils.replaceAll(s, "{cudIndex}", Integer.toString(i++));
				s = StringUtils.replaceAll(s, "{cudColumnWidth}", Integer.toString(dto.getListSirka()));
				columns += "\n" + StringUtils.replaceAll(s, "{cudTextAlignment}", "Left");

			}
			jrxml = StringUtils.replaceAll(jrxml, "{cudColumns}", columns);

			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("report.page.header", createPageHeaderText(dtoCis.getPrintZahlavie(), platnostOd));

			Map<String, List<DTOReport>> datasourceMap = new HashMap<String, List<DTOReport>>();
			datasourceMap.put("UniTableDataSource", copyListDTO(dataList, metaList));

			return generujReport(null, jrxml.getBytes(), paramMap, datasourceMap, null, format);

		} catch (Throwable t) {
			DBUtils.handleException(t, "getPdfReport.error");
			return null;
		}
	}

	private List<DTOReport> copyListDTO(DTODynCiselnik[] dataList, DTOCiselnikStlpecGui[] metaList) throws AppException {

		try {
			List<String> headerList = new ArrayList<String>();
			for (DTOCiselnikStlpecGui dtoCS : metaList) {
				headerList.add(dtoCS.getNadpis());
			}

			List<DTOReport> resultList = new ArrayList<DTOReport>();

			List<String> fieldList = new ArrayList<String>();
			for (DTODynCiselnik dto : dataList) {

				fieldList.clear();
				for (DTODynValue dtoDynValue : dto.getValues()) {
					fieldList.add(dtoDynValue.getValueStr());
				}

				resultList.add(createReportDTO(fieldList, headerList));
			}

			return resultList;

		} catch (Throwable t) {
			handleException(t, "copyListDTO.error");
			return null;
		}
	}

	protected byte[] getXmlReport(DTOCiselnikStlpecGui[] metaList, DTODynCiselnik[] dataList) throws AppException {

		try {
			for (DTOCiselnikStlpecGui dto : metaList) {
				String s = Normalizer.normalize(dto.getNadpis(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
				s = s.replaceAll("\\W", "");
				dto.setNadpis(s);
			}

			StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?><export>");
			for (DTODynCiselnik dtoDyn : dataList) {
				sb.append("<polozka>");
				for (int i = 0; i < metaList.length; i++) {
					DTODynValue dynValue = dtoDyn.getValues()[i];
					DTOCiselnikStlpecGui dtoCS = metaList[i];
					if (StringUtils.isValid(dynValue.getValueStr())) {
						sb.append("<" + dtoCS.getNadpis() + ">" + dynValue.getValueStr() + "</" + dtoCS.getNadpis() + ">");
					}

				}
				sb.append("</polozka>");
			}
			sb.append("</export>");

			return sb.toString().getBytes();

		} catch (Throwable t) {
			DBUtils.handleException(t, "getXmlReport.error");
			return null;
		}
	}

}
