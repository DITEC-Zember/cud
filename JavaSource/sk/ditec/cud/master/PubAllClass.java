package sk.ditec.cud.master;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOKompatibilita;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;

public class PubAllClass {

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

		DTOCiselnikGui dtoF1 = new DTOCiselnikGui();
		dtoF1.setStav(_CudConsts.CISELNIK_GUI_STAV_DRAFT);
		List<DTOCiselnikGui> guiList = dlg.getCiselnikGuiRead().listLight(auth, dtoF1);

		boolean b = false;

		for (DTOCiselnikGui dtoGui : guiList) {
			DTOCiselnik dtoCis = _CudLookupUtils.lookupDTOCiselnik(ciselnikList, dtoGui.getIDCiselnik());

			if (!set.contains(dtoCis.getTabulka())) {
				continue;
			}

			DTOKompatibilita dtoF = new DTOKompatibilita();
			dtoF.setCiselnikGuiID(dtoGui.getCiselnikGuiID());
			dtoF.setCiselnikID(dtoGui.getIDCiselnik());
			dtoF.setCiselnikTabulka(dtoCis.getTabulka());
			dtoF.setZdroj(_CudConsts.ZDROJ_FORM);
			dtoF.setPublishActual("F");

			DTOKompatibilita[] result = dlg.getKompatibilitaRead().kontrola(auth, dtoF);
			String kontrola = "ok";
			for (DTOKompatibilita dtoResult : result) {
				if (!"T".equals(dtoResult.getKompatibilita())) {
					kontrola = "error";
					b = true;
				}
			}
			System.out.println(dtoCis.getTabulka() + " = " + kontrola);
		}

		if (b) {
			System.out.println("Chyba pri kontrola");
			return;
		}

		System.out.println("Kontrola ok");

		for (DTOCiselnikGui dtoGui : guiList) {
			DTOCiselnik dtoCis = _CudLookupUtils.lookupDTOCiselnik(ciselnikList, dtoGui.getIDCiselnik());

			if (!set.contains(dtoCis.getTabulka())) {
				continue;
			}
			dlg.getCiselnikGuiModify().publishActual(auth, dtoGui);
		}

		System.out.println("end");

		// na konci treba spustit sql prikaz sirka.sql
	}

}
