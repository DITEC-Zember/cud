package sk.ditec.cud.master;
import java.io.FileInputStream;
import java.io.InputStream;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.WorkbookParser;
import sk.ditec.common.utils.StringUtils;

public class TriggerClass {

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

		for (int i = 1; i < sheet.getRows(); i++) {

			Cell[] cells = sheet.getRow(i);
			if (StringUtils.isValid(cells) && "N".equals(cells[0].getContents())) {
				String tableName = cells[1].getContents();
				String columnsName = cells[2].getContents();
				String typ = cells[3].getContents();
				if ("PK".equals(typ)) {
					String s = "CREATE OR REPLACE TRIGGER insert_pk_t_" + columnsName + " BEFORE ";
					s += " INSERT ON " + tableName;
					s += " FOR EACH ROW";
					s += " BEGIN";
					s += " IF inserting THEN";
					s += " IF :new.hist_id IS NULL THEN";
					s += " SELECT " + tableName + "_SEQ.NEXTVAL";
					s += " INTO :new.hist_id";
					s += " FROM dual;";
					s += " END IF;";
					s += " END IF;";
					s += " END;";
					s += " /";

					System.out.println(s);
				}
			}
		}

	}

}
