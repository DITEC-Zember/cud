package sk.ditec.cud.hlp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.procvys.dto.DTOStlpecInValue;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;

public class HlpOpravneniaNaAtributy {

    private final _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

    public List<DTOStlpecInValue> preCudCiselnikStlpec(
            Integer ciselnikId,
            DTOObjektCiselnik objektCiselnik,
            boolean vytvorInValueList,
            List<DTOCiselnikStlpec> stlpecMetaList) throws AppException {
        AuthInfo auth = AuthInfo.system();

        if (!"T".equals(objektCiselnik.getVsetky())) {
            // Ak sa nemajú poskytovat všetky atribúty, tak ak existuje atribút pre ktorý sa má kontrolovat hodnota:
            // Poskytnú sa iba tie záznamy ktorých hodnota = kontrolovanej hodnote
            // alebo pre ktorý v Zmene bola OldValue = kontrolovanej hodnote

            List<DTOObjektStlpec> ciselnikAtributList = dlg.getObjektStlpecRead().list(auth, new Integer[] {objektCiselnik.getObjektCiselnikID()});
            List<DTOCiselnikStlpec> stlpecMetaListCopy = new ArrayList<DTOCiselnikStlpec>(stlpecMetaList);
            for (DTOCiselnikStlpec dtoCiselnikStlpec : stlpecMetaListCopy) {
                if (!existujeZaznam(dtoCiselnikStlpec, ciselnikAtributList) && !_CudKontrolaUtils.jeAtributTechnicky(dtoCiselnikStlpec)) {
                    stlpecMetaList.remove(dtoCiselnikStlpec);
                }
            }

            if (vytvorInValueList) {
                HashMap<Integer, List<String>> kontrolovaneHodnotyList = new HashMap<Integer, List<String>>();
                List<String> hodnoty;
                for (DTOObjektStlpec dtoObjektStlpec : ciselnikAtributList) {
                    if (!kontrolovaneHodnotyList.containsKey(dtoObjektStlpec.getIDCiselnikStlpec())) {
                        hodnoty = new ArrayList<String>();
                        kontrolovaneHodnotyList.put(dtoObjektStlpec.getIDCiselnikStlpec(), hodnoty);
                    } else {
                        hodnoty = kontrolovaneHodnotyList.get(dtoObjektStlpec.getIDCiselnikStlpec());
                    }
                    hodnoty.add(dtoObjektStlpec.getHodnota());
                }

                List<DTOStlpecInValue> stlpecValueList = new ArrayList<DTOStlpecInValue>();
                for (Integer key : kontrolovaneHodnotyList.keySet()) {
                    if (!kontrolovaneHodnotyList.get(key).contains(null)) {
                        stlpecValueList.add(vytvorZaznamStlpecInValue(ciselnikId, key, stlpecMetaList, kontrolovaneHodnotyList.get(key)));
                    }
                }
                return stlpecValueList;
            }
        }

        return null;
    }

    private static boolean existujeZaznam(DTOCiselnikStlpec ciselnikStlpec, List<DTOObjektStlpec> ciselnikAtributList) {
        for (DTOObjektStlpec dtoObjektStlpec : ciselnikAtributList) {
            if (dtoObjektStlpec.getIDCiselnikStlpec().equals(ciselnikStlpec.getCiselnikStlpecID())) {
                return true;
            }
        }
        return false;
    }

    private static DTOStlpecInValue vytvorZaznamStlpecInValue(
            Integer ciselnikId,
            Integer ciselnikStlpecId,
            List<DTOCiselnikStlpec> ciselnikStlpecList,
            List<String> hodnoty) {

        for (DTOCiselnikStlpec dtoCiselnikStlpec : ciselnikStlpecList) {
            if (dtoCiselnikStlpec.getCiselnikStlpecID().equals(ciselnikStlpecId)) {
                return new DTOStlpecInValue(ciselnikId, ciselnikStlpecId, dtoCiselnikStlpec.getNazov(), dtoCiselnikStlpec.getDbTyp(), hodnoty);
            }
        }
        return null;
    }
}
