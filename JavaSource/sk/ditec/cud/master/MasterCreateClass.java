package sk.ditec.cud.master;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.WorkbookParser;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;

public class MasterCreateClass {

	public static void createCopy(AuthInfo auth, String tabulkaNazov) throws Exception {

		InputStream inputStream = new FileInputStream(new File("d:\\zsr\\cud_import\\TSI\\4\\data\\T_SUBSIDIARY_LOCATION.xls"));
		WorkbookSettings ws1 = new WorkbookSettings();
		ws1.setEncoding("Cp1250");
		Workbook xlsRead = WorkbookParser.getWorkbook(inputStream, ws1);
		Sheet[] sheetsRead = xlsRead.getSheets();

		int ciselnikIndex = 0;
		String sheetName = tabulkaNazov;
		for (Sheet sheet : sheetsRead) {
			if (sheetName.equals(sheet.getName())) {
				break;
			}
			ciselnikIndex++;
		}
		Sheet sheetRead = sheetsRead[ciselnikIndex];

		OutputStream outputStream = new FileOutputStream(new File("d:\\zsr\\cud_import\\TSI\\4\\data\\T_SUBSIDIARY_LOCATION_NEW.xls"));
		WorkbookSettings ws2 = new WorkbookSettings();
		ws2.setEncoding("Cp1250");
		WritableWorkbook xlsWrite = WorkbookParser.createWorkbook(outputStream, ws2);

		WritableSheet sheetWrite = xlsWrite.createSheet(tabulkaNazov, 0);

		sheetWrite.addCell(new Label(0, 0, "XLS_OPERACIA"));
		sheetWrite.addCell(new Label(1, 0, "XLS_PLATNOST_OD"));
		sheetWrite.addCell(new Label(2, 0, "XLS_CAS_SCHVALENIA"));
		sheetWrite.addCell(new Label(3, 0, "XLS_POZNAMKA"));
		sheetWrite.addCell(new Label(4, 0, "ID_COUNTRY"));
		sheetWrite.addCell(new Label(5, 0, "ID_COUNTRY.COUNTRY_CODE_ISO"));
		sheetWrite.addCell(new Label(6, 0, "ID_PRIMARY_LOCATION"));
		sheetWrite.addCell(new Label(7, 0, "ID_PRIMARY_LOCATION.ID_COUNTRY.COUNTRY_CODE_ISO"));
		sheetWrite.addCell(new Label(8, 0, "ID_PRIMARY_LOCATION.LOCATION_CODE"));
		sheetWrite.addCell(new Label(9, 0, "ID_SUBSIDIARY_TYPE"));
		sheetWrite.addCell(new Label(10, 0, "ID_SUBSIDIARY_TYPE.SUBSIDIARY_TYPE_CODE"));
		sheetWrite.addCell(new Label(11, 0, "SUBSIDIARY_LOCATION_CODE"));
		sheetWrite.addCell(new Label(12, 0, "SUBSIDIARY_LOCATION_NAME"));
		sheetWrite.addCell(new Label(13, 0, "START_VALIDITY"));
		sheetWrite.addCell(new Label(14, 0, "END_VALIDITY"));
		sheetWrite.addCell(new Label(15, 0, "ID_COMPANY"));
		sheetWrite.addCell(new Label(16, 0, "ID_COMPANY."));
		sheetWrite.addCell(new Label(17, 0, "LONGITUDE"));
		sheetWrite.addCell(new Label(18, 0, "LATITUDE"));
		sheetWrite.addCell(new Label(19, 0, "FREE_TEXT"));
		sheetWrite.addCell(new Label(20, 0, "XLS_LOOKUP_ID_COUNTRY"));
		sheetWrite.addCell(new Label(21, 0, "XLS_LOOKUP_ID_PRIMARY_LOCATION"));
		sheetWrite.addCell(new Label(22, 0, "XLS_LOOKUP_ID_SUBSIDIARY_TYPE"));
		sheetWrite.addCell(new Label(23, 0, "XLS_LOOKUP_ID_COMPANY"));

		for (int row = 1; row < sheetRead.getRows(); row++) {

			Cell[] cells = sheetRead.getRow(row);

			if (cells.length == 0) {
				continue;
			}

			String operacia = cells[0].getContents();
			if (!"N".equals(operacia)) {
				continue;
			}

			String lookupCountry = null;
			String lookupPrimaryLocationFkCountry = null;
			String lookupPrimaryLocation = null;
			String lookupSubsidiaryType = null;
			String lookupCompany = null;

			for (int col = 0; col <= 22; col++) {

				if (cells.length > col) {
					String s = cells[col].getContents();
					s = StringUtils.isValid(s) ? s.trim() : null;
					if (StringUtils.isValid(s)) {
						if (col == 5) {
							lookupCountry = s;
						}
						if (col == 7) {
							lookupPrimaryLocationFkCountry = s;
						}
						if (col == 8) {
							lookupPrimaryLocation = s;
						}
						if (col == 10) {
							lookupSubsidiaryType = s;
						}
						if (col == 16) {
							lookupCompany = s;
						}
						sheetWrite.addCell(new Label(col, row, s));
					}
				}
			}

			if (StringUtils.isValid(lookupCountry)) {
				String s = "select country_id from t_country where country_code_iso = \'" + lookupCountry;
				s += "' AND zmaz = 'F' AND platnost_od >= TO_DATE('01.03.2022', 'DD.MM.YYYY') AND ( platnost_do <= TO_DATE('01.03.2022', 'DD.MM.YYYY') OR platnost_do IS NULL )";
				sheetWrite.addCell(new Label(20, row, s));
			}
			if (StringUtils.isValid(lookupPrimaryLocationFkCountry) && StringUtils.isValid(lookupPrimaryLocation)) {
				String s = "SELECT t.primary_location_id FROM t_primary_location t LEFT JOIN t_country t1 ON t1.country_id = t.id_country ";
				s += "WHERE t.location_code = '" + lookupPrimaryLocation + "' ";
				s += "and t1.country_code_iso = '" + lookupPrimaryLocationFkCountry + "' ";
				s += "AND t.zmaz = 'F' AND t.platnost_od >= TO_DATE('01.03.2022', 'DD.MM.YYYY') AND ( t.platnost_do <= TO_DATE('01.03.2022', 'DD.MM.YYYY') OR t.platnost_do IS NULL ) AND t1.zmaz = 'F' AND t1.platnost_od >= TO_DATE('01.03.2022', 'DD.MM.YYYY') AND ( t1.platnost_do <= TO_DATE('01.03.2022', 'DD.MM.YYYY') OR t1.platnost_do IS NULL )";
				sheetWrite.addCell(new Label(21, row, s));
			}
			if (StringUtils.isValid(lookupSubsidiaryType)) {
				String s = "select subsidiary_type_id from t_subsidiary_type where subsidiary_type_code = '" + lookupSubsidiaryType;
				s += "' and zmaz = 'F' AND platnost_od >= TO_DATE('01.03.2022', 'DD.MM.YYYY') AND ( platnost_do <= TO_DATE('01.03.2022', 'DD.MM.YYYY') OR platnost_do IS NULL )";
				sheetWrite.addCell(new Label(22, row, s));
			}
			if (StringUtils.isValid(lookupCompany)) {
				String s = "select company_id from t_company where company_uic_code = '" + lookupCompany;
				s += "' and zmaz = 'F' AND platnost_od >= TO_DATE('01.03.2022', 'DD.MM.YYYY') AND ( platnost_do <= TO_DATE('01.03.2022', 'DD.MM.YYYY') OR platnost_do IS NULL )";
				sheetWrite.addCell(new Label(23, row, s));
			}
		}

		xlsWrite.write();
		xlsWrite.close();
		outputStream.close();

		inputStream.close();
		xlsRead.close();

	}

	public static void main(String[] args) throws Exception {

		DBUtils.init();

		AuthInfo auth = FrameworkUtils.getAuthMod().accountRead("zember");

		createCopy(auth, "T_SUBSIDIARY_LOCATION");

		System.out.println("end");
	}
}
