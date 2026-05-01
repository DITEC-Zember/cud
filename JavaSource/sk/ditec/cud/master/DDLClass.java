package sk.ditec.cud.master;

import java.util.HashSet;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;

public class DDLClass {

	public static void main(String[] args) throws Exception {

		DBUtils.init();

		AuthInfo auth = FrameworkUtils.getAuthMod().accountRead("zember");

		_CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

		DTOCiselnik[] ciselnikList = dlg.getCiselnikRead().listLight(auth, null);

		Set<String> todoSet = new HashSet<String>();
		Set<String> stavHistSet = new HashSet<String>();
		Set<String> eskalaciaSet = new HashSet<String>();
		Set<String> stlpecSet = new HashSet<String>();
		Set<String> masterSet = new HashSet<String>();
		Set<String> zmenaSet = new HashSet<String>();
		
		


		for (String tabulka : new String[] { "T_ZAKLADNE_INFORMACIE_O_PRM", "T_NASTUPISTE_PRM", "T_OZNACENIA_PRM", "T_DYNAMICKE_INFORMACIE_PRM", "T_INFO_PULTY_PRM", "T_PCL_PRM", "T_TOALETY_PRM", "T_PARKOVISKO_PRM", "T_INE_DP_PRM" } ) {



			DTOCiselnik dtoCis = _CudLookupUtils.lookupDTOCiselnik(ciselnikList, tabulka);
			if (!StringUtils.isValid(dtoCis)) {
				System.out.println(tabulka + " sa nenachaza v zozname!");
				continue;
			}

			String s = "DELETE FROM cud_wf_todo WHERE id_ciselnik IN ( SELECT ciselnik_id FROM cud_ciselnik WHERE tabulka = \'" + tabulka
					+ "\' AND id_transakcia_zrusene IS NULL );";
			todoSet.add(s);

			s = "DELETE FROM cud_zmena_stav_hist WHERE id_ciselnik IN ( SELECT ciselnik_id FROM cud_ciselnik WHERE tabulka = \'" + tabulka
					+ "\' AND id_transakcia_zrusene IS NULL );";
			stavHistSet.add(s);

			s = "DELETE FROM cud_zmena_eskalacia WHERE id_ciselnik IN ( SELECT ciselnik_id FROM cud_ciselnik WHERE tabulka = \'" + tabulka
					+ "\' AND id_transakcia_zrusene IS NULL );";
			eskalaciaSet.add(s);

			s = "DELETE FROM cud_zmena_stlpec WHERE id_ciselnik IN ( SELECT ciselnik_id FROM cud_ciselnik WHERE tabulka = \'" + tabulka + "\' AND id_transakcia_zrusene IS NULL );";
			stlpecSet.add(s);

			s = "DELETE FROM " + tabulka + ";";
			masterSet.add(s);

			s = "DELETE FROM cud_zmena WHERE id_ciselnik IN ( SELECT ciselnik_id FROM cud_ciselnik WHERE tabulka = \'" + tabulka + "\' AND id_transakcia_zrusene IS NULL );";
			zmenaSet.add(s);

		}

		System.out.println();
		for (String s : todoSet) {
			System.out.println(s);
		}
		System.out.println("commit;");

		System.out.println();
		for (String s : stavHistSet) {
			System.out.println(s);
		}
		System.out.println("commit;");

		System.out.println();
		for (String s : eskalaciaSet) {
			System.out.println(s);
		}
		System.out.println("commit;");

		System.out.println();
		for (String s : stlpecSet) {
			System.out.println(s);
		}
		System.out.println("commit;");

		System.out.println();
		for (String s : masterSet) {
			System.out.println(s);
		}
		System.out.println("commit;");

		System.out.println();
		for (String s : zmenaSet) {
			System.out.println(s);
		}
		System.out.println("commit;");

		System.out.println("end");

		// na konci treba spustit sql prikaz sirka.sql
	}

}
