package sk.ditec.cud.print;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudBaseClass;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOReport;
import sk.ditec.cud.utils._CudConsts;

abstract class _CudPrintBaseClass extends _CudBaseClass {

	protected DTOCiselnikStlpecGui createMetaAtribut(String nazov, String typ, String dbTyp, Integer dlzka, Integer decimals, String fk1CiselnikTabulka, String fk1PkNazov,
			String fk1FkNazov, String fk2CiselnikTabulka, String fk2PkNazov, String fk2FkNazov) throws AppException {

		try {
			DTOCiselnikStlpecGui dtoNew = new DTOCiselnikStlpecGui();
			dtoNew.setCiselnikStlpecNazov(nazov);
			dtoNew.setCiselnikStlpecTyp(typ);
			dtoNew.setCiselnikStlpecDbTyp(dbTyp);
			dtoNew.setDlzka(dlzka);
			dtoNew.setDecimals(decimals);
			dtoNew.setCiselnikStlpecFk1CiselnikTabulka(fk1CiselnikTabulka);
			dtoNew.setCiselnikStlpecFk1PkNazov(fk1PkNazov);
			dtoNew.setFk1FkNazov(fk1FkNazov);
			dtoNew.setFk2CiselnikTabulka(fk2CiselnikTabulka);
			dtoNew.setFk2PkNazov(fk2PkNazov);
			dtoNew.setFk2FkNazov(fk2FkNazov);
			dtoNew.setListZobrazenie("T");

			return dtoNew;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createMetaAtribut.error");
			return null;
		}
	}

	protected byte[] generujReport(String reportName, byte[] arr, Map<String, String> paramMap, Map<String, List<DTOReport>> datasourceMap, List<DTOReport> dataList, String format)
			throws AppException {

		try {
			Map<String, Object> params = new HashMap<String, Object>();

			for (String key : paramMap.keySet()) {
				params.put(key, paramMap.get(key));
			}
			for (String key : datasourceMap.keySet()) {
				List<DTOReport> datasourceList = datasourceMap.get(key);
				params.put(key, createDataSource(datasourceList));
			}

			byte[] byteArray = null;

			if (_CudConsts.PRINT_FORMAT_PDF.equals(format)) {
				byteArray = generujPdf(reportName, arr, params, dataList);

			} else if (_CudConsts.PRINT_FORMAT_RTF.equals(format)) {
				byteArray = generujRtf(reportName, arr, params, dataList);

			} else if (_CudConsts.PRINT_FORMAT_XLS.equals(format)) {
				byteArray = generujXls(reportName, arr, params, dataList);
			}

			return byteArray;

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujReport.error");
			return null;
		}
	}

	protected String lookupFileName(String ciselnikNazov, String format) throws AppException {

		try {
			String s = Normalizer.normalize(ciselnikNazov, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			s = StringUtils.replaceAll(s, " ", "_");

			String datum = new SimpleDateFormat("ssmmHH").format(new Date());

			return s + "_" + datum + lookupFileType(format);

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupFileName.error");
			return null;
		}
	}

	private String lookupFileType(String format) throws AppException {

		try {
			if (_CudConsts.PRINT_FORMAT_PDF.equals(format)) {
				return ".pdf";
			} else if (_CudConsts.PRINT_FORMAT_RTF.equals(format)) {
				return ".rtf";
			} else if (_CudConsts.PRINT_FORMAT_XLS.equals(format)) {
				return ".xls";
			} else if (_CudConsts.PRINT_FORMAT_XML.equals(format)) {
				return ".xml";
			} else {
			}
			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupFileType.error");
			return null;
		}
	}

	private void writeDTOValue(Object object, String methodName, Object value) throws AppException {

		try {
			Method method = null;

			try {
				method = object.getClass().getDeclaredMethod(methodName, String.class);
			} catch (java.lang.NoSuchMethodException ee) {
				// skontrolovat predka
				method = object.getClass().getSuperclass().getDeclaredMethod(methodName, String.class);
			}

			if (value == null) {
				method.invoke(object, (Object[]) null);
			} else {
				method.invoke(object, new Object[] { value });
			}

		} catch (Exception t) {
			DBUtils.handleException(t, "writeDTOValue.error");
		}
	}

	private void lookupValues(DTOReport dto, List<String> valueList, String methodName) throws AppException {

		try {
			int i = 1;
			for (String value : valueList) {

				if (!StringUtils.isValid(value)) {
					value = " ";
				}

				writeDTOValue(dto, methodName + i++, value);
			}

		} catch (Throwable t) {
			handleException(t, "lookupValues.error");
		}
	}

	protected JRBeanCollectionDataSource createDataSource(List<DTOReport> dataList) throws AppException {

		try {
			if (!StringUtils.isValid(dataList)) {
				return null;
			}

			if (dataList.size() > _CudConsts.PRINT_MAX_POCET) {
				dataList = dataList.subList(0, _CudConsts.PRINT_MAX_POCET - 1);
			}

			return new JRBeanCollectionDataSource(dataList);

		} catch (Throwable t) {
			handleException(t, "createDataSource.error");
			return null;
		}
	}

	protected DTOReport createReportDTO(List<String> fieldList, List<String> headerList) throws AppException {

		try {
			DTOReport dtoNew = new DTOReport();

			if (StringUtils.isValid(fieldList)) {
				lookupValues(dtoNew, fieldList, "setField");
			}

			if (StringUtils.isValid(headerList)) {
				lookupValues(dtoNew, headerList, "setHeader");
			}

			return dtoNew;

		} catch (Throwable t) {
			handleException(t, "createReportDTO.error");
			return null;
		}
	}

	protected String createPageHeaderText(String zahlavie, Date platnostOd) throws AppException {

		try {
			Date d = new Date();
			return zahlavie + ", tlačené: " + _CudConsts.DATE_FORMAT.format(d) + " o " + _CudConsts.TIME_FORMAT.format(d) + ", stav ku dňu: "
					+ _CudConsts.DATE_FORMAT.format(platnostOd);

		} catch (Throwable t) {
			handleException(t, "createPageHeaderText.error");
			return null;
		}
	}

	protected JRBeanCollectionDataSource createNoDataDataSource(List<String> headerList) throws AppException {

		try {
			DTOReport dto = new DTOReport();
			dto.setNoDataMessage("Žiadne dáta na zobrazenie");

			lookupValues(dto, headerList, "setHeader");

			List<DTOReport> dataList = new ArrayList<DTOReport>();
			dataList.add(dto);

			return createDataSource(dataList);

		} catch (Throwable t) {
			handleException(t, "createNoDataDataSource.error");
			return null;
		}
	}

	private JasperPrint generujJasperPrint(String reportName, byte[] arr, Map<String, Object> params, List<DTOReport> dataList) throws AppException {

		try {
			InputStream is = null;

			if (StringUtils.isValid(arr)) {
				is = new ByteArrayInputStream(arr);

			} else {
				URL url = getClass().getResource(_CudPrintBaseClass.class.getSimpleName() + ".class");
				String baseDir = url.getPath().replace(_CudPrintBaseClass.class.getCanonicalName().replace(".", "/") + ".class", "");
				baseDir = baseDir.replace("%20", " ");
				String reportHomeDir = "resources/reports/";
				String jrxml = "/" + reportHomeDir + reportName + ".jrxml";
				params.put("SUBREPORT_DIR", baseDir + reportHomeDir);
				is = _CudPrintBaseClass.class.getResourceAsStream(jrxml);
			}

			JasperReport jasperReport = JasperCompileManager.compileReport(is);

			JasperPrint jasperPrintResult = null;

			if (dataList == null) {
				jasperPrintResult = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource(1));
			} else {
				jasperPrintResult = JasperFillManager.fillReport(jasperReport, params, createDataSource(dataList));
			}

			return jasperPrintResult;

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujJasperPrint.error");
			return null;
		}
	}

	private byte[] generujXls(String reportName, byte[] arr, Map<String, Object> params, List<DTOReport> dataList) throws AppException {

		try {
			JasperPrint jasperPrint = generujJasperPrint(reportName, arr, params, dataList);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
			exporter.exportReport();

			byte[] byteResult = null;
			if (bos != null) {
				byteResult = bos.toByteArray();
				bos.close();
			}

			return byteResult;

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujXls.error");
			return null;
		}
	}

	private byte[] generujRtf(String reportName, byte[] arr, Map<String, Object> params, List<DTOReport> dataList) throws AppException {

		try {
			JasperPrint jasperPrint = generujJasperPrint(reportName, arr, params, dataList);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			JRRtfExporter exporter = new JRRtfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
			exporter.exportReport();

			byte[] byteResult = null;
			if (bos != null) {
				byteResult = bos.toByteArray();
				bos.close();
			}

			return byteResult;

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujRtf.error");
			return null;
		}
	}

	private byte[] generujPdf(String reportName, byte[] arr, Map<String, Object> params, List<DTOReport> dataList) throws AppException {

		try {
			JasperPrint jasperPrint = generujJasperPrint(reportName, arr, params, dataList);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, bos);
			exporter.exportReport();

			byte[] byteResult = null;
			if (bos != null) {
				byteResult = bos.toByteArray();
				bos.close();
			}

			return byteResult;

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujPdf.error");
			return null;
		}

	}

}
