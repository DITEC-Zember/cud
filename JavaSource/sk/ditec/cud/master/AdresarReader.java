package sk.ditec.cud.master;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;

public class AdresarReader {

	public static void main(String[] args) {

		try {
			DBUtils.init();

			// Cesta k adresáru, ktorý chceš prehľadávať
			File adresar = new File("D:\\zal\\CRD\\subsidiary_location");

			AuthInfo auth = AuthInfo.system();

			if (adresar.exists() && adresar.isDirectory()) {
				File[] subory = adresar.listFiles();

				if (subory != null) {
					for (File subor : subory) {
						// Spracujeme len súbory (nie podadresáre)
						if (subor.isFile()) {
							System.out.println("--- Čítam súbor: " + subor.getName() + " ---");
							citajSubor(auth, subor);
						}
					}
				}
			} else {
				System.out.println("Zadaná cesta neexistuje alebo nie je adresár.");
			}

			System.out.println("Koniec");

		} catch (Throwable t) {
			System.out.println("chyba");
		}
	}

	public static void update(AuthInfo auth, String sql) throws AppException {

		Statement stmt = null;
		ResultSet rs = null;

		try {
			if (!StringUtils.isValid(sql)) {
				return;
			}

			if (sql.contains("CUD_ZMENA_STLPEC")) {
				sql = StringUtils.replaceAll(sql, "CUD_ZMENA_STLPEC", "ZAL_ZMENA_STLPEC");
			}

			if (sql.contains("T_SUBSIDIARY_LOCATION")) {
				sql = StringUtils.replaceAll(sql, "T_SUBSIDIARY_LOCATION", "ZAL_T_SUBSIDIARY_LOCATION");
			}

			DBUtils.getConnection(auth);
			stmt = auth.T.createStatement();
			rs = stmt.executeQuery(sql);

			DBUtils.cleanUp(stmt, rs);
			DBUtils.returnConnection(auth);

		} catch (Throwable t) {
			DBUtils.cleanUp(stmt, rs);
			DBUtils.handleException(t, "update.error, sql: " + sql, auth);
		}
	}

	private static void citajSubor(AuthInfo auth, File subor) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(subor), "UTF-8"));
			String riadok;

			while ((riadok = br.readLine()) != null) {
				update(auth, riadok.substring(0, riadok.length() - 1));
			}

		} catch (Exception e) {
			System.err.println("Chyba pri čítaní súboru " + subor.getName() + ": " + e.getMessage());

		} finally {
			// V Jave 6 musíme zatvárať streamy ručne v bloku finally
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}