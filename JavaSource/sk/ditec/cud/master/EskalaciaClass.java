package sk.ditec.cud.master;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.cud.bi._CudBaseClass;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudWfDef;

public class EskalaciaClass extends _CudBaseClass {

	public static void main(String[] args) throws Exception {

		DBUtils.init();

		AuthInfo auth = FrameworkUtils.getAuthMod().accountRead("zember");

		_CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

		List<DTOWfDef> newListDTO = new ArrayList<DTOWfDef>();

		DTOWfDef dtoF = new DTOWfDef();
		dtoF.setTyp(_CudConsts.WF_DEF_TYP_IN);
		List<DTOWfDef> listDTO = dlg.getWfDefRead().listLight(auth, dtoF);
		Map<Integer, DTOWfDef> wfdefMap = new HashMap<Integer, DTOWfDef>();
		for (DTOWfDef dto : listDTO) {
			wfdefMap.put(dto.getIDCiselnik(), dto);
		}

		Set<Integer> ciselnikIDs = new HashSet<Integer>();
		dtoF = new DTOWfDef();
		dtoF.setTyp(_CudConsts.WF_DEF_TYP_ES);
		for (DTOWfDef dto : dlg.getWfDefRead().listLight(auth, dtoF)) {
			ciselnikIDs.add(dto.getIDCiselnik());
		}

		dtoF = new DTOWfDef();
		dtoF.setTyp(_CudConsts.WF_DEF_TYP_SC);

		for (DTOWfDef dto : dlg.getWfDefRead().listLight(auth, dtoF)) {

			if (ciselnikIDs.contains(dto.getIDCiselnik())) {
				continue;
			}

			DTOWfDef dtoNew = new DTOWfDef();
			dtoNew.setIDCiselnik(dto.getIDCiselnik());
			dtoNew.setNazov("Eskalácia");
			dtoNew.setTyp(_CudConsts.WF_DEF_TYP_ES);
			dtoNew.setEmailList(dto.getEmailList());
			dtoNew.setEmailText(wfdefMap.get(dto.getIDCiselnik()).getEmailText());
			dtoNew.setEmailSubject("Eskalácia - vypršanie časového limitu v číselníku {ciselnik}");
			dtoNew.setEmailSend("T");
			dtoNew.setHodiny(24);
			dtoNew.setIDSkupina(dto.getIDSkupina());
			dtoNew.setSkupinaNazov(dto.getSkupinaNazov());

			newListDTO.add(dtoNew);
		}

		startTransaction(auth, "CUDdataModify");

		Date d = new Date();

		getConnection(auth);

		for (DTOWfDef dto : newListDTO) {

			CudWfDef dao = new CudWfDef();
			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setIdWfDefNasl(dto.getIDWfDefNasl());
			dao.setNazov(dto.getNazov());
			dao.setTyp(dto.getTyp());
			dao.setEmailList(dto.getEmailList());
			dao.setEmailText(dto.getEmailText());
			dao.setEmailSubject(dto.getEmailSubject());
			dao.setEmailSend(dto.getEmailSend());
			dao.setHodiny(dto.getHodiny());
			dao.setIdSkupina(dto.getIDSkupina());
			dao.setSkupinaNazov(dto.getSkupinaNazov());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

		}

		returnConnection(auth);

		endTransaction(auth, true);

		System.out.println("end");

	}

}
