package sk.ditec.cud.master;

import java.util.HashSet;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;

public class CopyAllClass {

	public static void main(String[] args) throws Exception {

		DBUtils.init();

		Set<String> set = new HashSet<String>();
		set.add("T_DYNAMICKE_INFORMACIE_PRM");
		set.add("T_INE_DP_PRM");
		set.add("T_INFO_PULTY_PRM");
		set.add("T_NASTUPISTE_PRM");
		set.add("T_OZNACENIA_PRM");
		set.add("T_PARKOVISKO_PRM");
		set.add("T_PCL_PRM");
		set.add("T_PCL_ZSKK");
		set.add("T_TOALETY_PRM");
		set.add("T_ZAKLADNE_INFORMACIE_O_PRM");

		AuthInfo auth = FrameworkUtils.getAuthMod().accountRead("zember");

		_CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

		DTOCiselnik[] ciselnikList = dlg.getCiselnikRead().listLight(auth, null);

		for (String tabulka : set) {
			DTOCiselnik dtoCis = _CudLookupUtils.lookupDTOCiselnik(ciselnikList, tabulka);

			if (!set.contains(dtoCis.getTabulka())) {
				System.err.println("Tabulka neexistuje: " + tabulka);
				return;
			}

			DTOCiselnikGui dtoNew = new DTOCiselnikGui();
			dtoNew.setStav(_CudConsts.CISELNIK_GUI_STAV_DRAFT);
			dtoNew.setIDCiselnik(dtoCis.getCiselnikID());
			dlg.getCiselnikGuiModify().updateAndCopy(auth, dtoNew);

			System.out.println("Tabulka " + tabulka + " ok");
		}

		System.out.println("end");
	}

}
