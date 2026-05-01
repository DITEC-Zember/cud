package sk.ditec.cud.master;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.WorkbookParser;
import sk.ditec.common.utils.StringUtils;

public class DeleteClass {

	public static void main(String[] args) throws Throwable {

		InputStream is = new FileInputStream("d:\\zsr\\cud_import\\TSI\\3\\kmd_ciselnik_meta v2.0-TSI.xls");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1250");
		Workbook xls = WorkbookParser.getWorkbook(is, ws);
		Sheet[] sheets = xls.getSheets();

		Sheet sheet = null;
		for (Sheet sheetItem : sheets) {
			if ("KMD_CISELNIK_STLPEC_NEW".equals(sheetItem.getName())) {
				sheet = sheetItem;
				break;
			}
		}

		Set<String> set = new HashSet<String>();
		for (int i = 1; i < sheet.getRows(); i++) {

			Cell[] cells = sheet.getRow(i);
			if (StringUtils.isValid(cells) && "N".equals(cells[0].getContents())) {
				String tableName = cells[1].getContents();
				if (StringUtils.isValid(tableName)) {
					set.add(tableName);
				}
			}
		}

		for (String table : set) {
			System.out.println("DROP SEQUENCE " + table + "_SEQ;");
		}

		System.out.println("\n");

		for (String table : set) {
			System.out.println("DROP TABLE " + table + ";");
		}

		System.out.println("\n");

		for (String table : set) {
			System.out.println("PURGE TABLE " + table + ";");
		}

	}

}
