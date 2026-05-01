package sk.ditec.cud.master;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportPriloha;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;

public class ImportAll {

	public static void main(String[] args) throws Exception {

		DBUtils.init();

		AuthInfo auth = FrameworkUtils.getAuthMod().accountRead("zember");

		_CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

		DTOCiselnik[] ciselnikList = dlg.getCiselnikRead().listLight(auth, null);

		File[] files = new File("d:\\zsr\\workspace\\cud_dao_master\\xls\\2022_12_12\\01").listFiles();
		for (File file : files) {
			String tabulka = file.getName().substring(0, file.getName().length() - 4);
			
			if (!StringUtils.isValid(tabulka)) {
				continue;
			}
			
			InputStream is = new FileInputStream(new File(file.getAbsolutePath()));
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[1024];
			while ((nRead = is.read(data, 0, data.length)) != -1) {
				buffer.write(data, 0, nRead);
			}
			byte[] pole = buffer.toByteArray();

			DTOCiselnik dtoCis = _CudLookupUtils.lookupDTOCiselnik(ciselnikList, tabulka);
			if (!StringUtils.isValid(dtoCis)) {
				System.out.println(tabulka + " sa nenachaza v zozname!");
				continue;
			}

			DTOImport dtoImport = new DTOImport();
			dtoImport.setIDCiselnik(dtoCis.getCiselnikID());
			dtoImport.setCiselnikTabulka(tabulka);
			dtoImport.setStav(_CudConsts.IMPORT_STAV_KONTROLA);

			DTOImportPriloha dtoPriloha = new DTOImportPriloha();
			dtoPriloha.setFileName(file.getName());
			dtoPriloha.setPriloha(pole);

			dlg.getImportModify().update(auth, dtoImport, dtoPriloha);
		}

		System.out.println("end");

		// na konci treba spustit sql prikaz sirka.sql
	}

}
