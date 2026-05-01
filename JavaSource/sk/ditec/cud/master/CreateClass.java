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

public class CreateClass {

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

		boolean b = true;
		String s = "";
		String tableName = "";

		Set<String> set = new HashSet();
		for (int i = 1; i < sheet.getRows(); i++) {

			Cell[] cells = sheet.getRow(i);
			if (StringUtils.isValid(cells) && "N".equals(cells[0].getContents())) {
				tableName = cells[1].getContents();
				if (b) {
					s = "CREATE TABLE " + tableName + " (\n";
					s += "hist_id NUMBER(10) NOT NULL,\n";
					s += "platnost_od TIMESTAMP(6) NOT NULL,\n";
					s += "platnost_do TIMESTAMP(6),\n";
					s += "cas_vytvorenia TIMESTAMP(6) NOT NULL,\n";
					s += "cas_zmeny TIMESTAMP(6),\n";
					s += "id_zmena NUMBER(10) NOT NULL,\n";
					s += "zmaz NVARCHAR2(1) NOT NULL,\n";
					b = false;
				}
				String columnsName = cells[2].getContents();
				String typ = cells[3].getContents();
				typ = "PK".equals(typ) ? " NOT NULL" : "";
				String dbTyp = cells[7].getContents();
				if ("String".equals(dbTyp)) {
					dbTyp = "NVARCHAR2";
				} else if ("Integer".equals(dbTyp)) {
					dbTyp = "NUMBER";
				}
				String dlzka = cells[8].getContents();
				s += columnsName + " " + dbTyp + "(" + dlzka + ")" + typ + ",\n";
			} else {
				if (StringUtils.isValid(s)) {
					s += "CONSTRAINT " + tableName + "_PK PRIMARY KEY ( hist_id )\n";
					set.add(tableName);
					System.out.println(s + ");\n");
					s = "";
					b = true;
				}
			}
		}

		System.err.println("\n");

		for (String table : set) {
			System.out.println("ALTER TABLE " + table + " ADD CONSTRAINT " + table + "_FK1 FOREIGN KEY (ID_ZMENA) REFERENCES CUD_ZMENA (ZMENA_ID);");
		}

		System.err.println("\n");

		for (String table : set) {
			System.out.println("CREATE SEQUENCE " + table + "_SEQ INCREMENT BY 1 START WITH 1 maxvalue 1.0e28 minvalue 1 nocycle nocache noorder;");
		}

	}

}
