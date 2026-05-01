package sk.ditec.crd;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.MyCriteria2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.dto.DTOCrdSpracovanie;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOValidate;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWorkflow;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.proc.CudCrdProcess;
import sk.ditec.cud.proc.CudPauClass;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CrdNenajdeneZaznamyPeer;
import sk.ditec.dao.meta.CrdSpracovanie;
import sk.ditec.dao.meta.CrdSpracovaniePeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.Record;



public class CrdSpracovanieClass extends _NovyPISBaseClass {
	private _CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private CudPauClass cudPau = new CudPauClass();
	private _CudCrdDelegate dlg = new _CudCrdDelegate();
	private Logger log = LoggerFactory.getLogger(CudCrdProcess.class);

	public ArrayList<DTOCrdSpracovanie> getList(AuthInfo auth, DTOCrdSpracovanie dtoF) throws AppException {
		try {
			if (dtoF == null) {
				dtoF = new DTOCrdSpracovanie();
			}			
			
			MyCriteria2 crit = new MyCriteria2(CrdNenajdeneZaznamyPeer.CRD_NENAJDENE_ZAZNAMY_ID, dtoF);
			CrdNenajdeneZaznamyPeer.addSelectColumns(crit);
			crit.addConditional(CrdSpracovaniePeer.CRD_SPRACOVANIE_ID, dtoF.getCrdSpracovanieId());
			crit.addConditional(CrdSpracovaniePeer.DATUM_VOLANIA, dtoF.getDatumVolania(), Criteria.EQUAL);
			crit.addConditional(CrdSpracovaniePeer.KOD_SPRACOVANIA, dtoF.getKodSpracovania());
			crit.addConditional(CrdSpracovaniePeer.POSLEDNE_USPESNE_SPRACOVANIE, dtoF.getPosledneUspesneSpracovanie(),
					Criteria.EQUAL);
	String sql = crit.getSQL();

		getConnection(auth);
	List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
	returnConnection(auth);
	Iterator<?> iter = lp.iterator();
			ArrayList<DTOCrdSpracovanie> listDto = new ArrayList<DTOCrdSpracovanie>();
	while (iter.hasNext()) {
				DTOCrdSpracovanie dto = new DTOCrdSpracovanie();
		Record r = (Record) iter.next();
				dto.setCrdSpracovanieId(rVal(r, CrdSpracovaniePeer.CRD_SPRACOVANIE_ID).asIntegerObj());
				dto.setDatumVolania(rVal(r, CrdSpracovaniePeer.DATUM_VOLANIA).asUtilDate());
				dto.setKodSpracovania(rVal(r, CrdSpracovaniePeer.KOD_SPRACOVANIA).asString());
				dto.setPosledneUspesneSpracovanie(rVal(r, CrdSpracovaniePeer.POSLEDNE_USPESNE_SPRACOVANIE).asUtilDate());
		listDto.add(dto);
	}
  return listDto;

} catch (Throwable t) {
			handleException(t, "CrdSpracovanieClass.getlist.error", auth);
	return null;
}

}

	public ActionResult update(AuthInfo auth, DTOCrdSpracovanie dto) throws AppException {

		try {
			getConnection(auth);

			CrdSpracovanie dao = new CrdSpracovanie();
			if (StringUtils.isValid(dto.getCrdSpracovanieId())) {
				dao = CrdSpracovaniePeer.retrieveByPK(dto.getCrdSpracovanieId(), auth.T);
			}

			dao.setCrdSpracovanieId(dto.getCrdSpracovanieId());
			dao.setDatumVolania(dto.getDatumVolania());
			dao.setKodSpracovania(dto.getKodSpracovania());
			dao.setPosledneUspesneSpracovanie(dto.getPosledneUspesneSpracovanie());
			dao.save(auth.T);
			dto.setCrdSpracovanieId(dao.getCrdSpracovanieId());
			returnConnection(auth);

			return new ActionResult(dto);

		} catch (Throwable e) {
			handleException(e, "CrdSpracovanieClass.update.error", auth);
			return null;
		}
	}

	public Date getDatumPoslSpracovania(AuthInfo auth) throws AppException {
		try{
			MyCriteria2 crit = new MyCriteria2(CrdSpracovaniePeer.TABLE_NAME);
			crit.addAsColumn("DATUM", "max(" + CrdSpracovaniePeer.POSLEDNE_USPESNE_SPRACOVANIE + ")");
       
			String sql = crit.getSQL() + " " + CrdSpracovaniePeer.TABLE_NAME;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				Date datum = rVal(r, "DATUM").asTimestamp();
				return datum;
			}
		} catch (Throwable e) {
			handleException(e, "getDatumPoslSpracovania.update.error", auth);
			return null;
		}
		return null;
	}

	public String zapisLokCezZmenoveProcesy(AuthInfo auth, Map<String, String> rowMap, DTOImport dtoImport,
			int navratovyKod, String popisSpracovania) throws Throwable {

		DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, new Date(), null, null);
		DTOZmena dtoZmenaZap = null;

		String stringRowMap = rowMap.toString();
		Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();

		List<DTOCiselnikStlpec> csList = dlgcud.getCiselnikStlpecRead().listLight(auth, dtoImport.getIDCiselnik());
		dlgcud.getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);
		String chyba = "";
		if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
			navratovyKod = 4;
			for (DTOImportMsg dto : dtoVal.getImportZmenaDTO().getImportMsgList()) {
				chyba += dto.getMsg();
				popisSpracovania += dto.getMsg();
			}
			chyba += rowMap.toString();
			log.info(chyba + "  " + stringRowMap);
		}

		Map<Integer, DTOUcet[]> ucetMap = new HashMap<Integer, DTOUcet[]>();

		List<DTOWfDef> wfDefList = dlgcud.getWfDefRead().list(auth, dtoImport.getIDCiselnik());
		if (StringUtils.isValid(dtoImport.getIDCiselnik())) {
			DTOWorkflow dtoWf = dlgcud.getWorkflow().generujWorkflowAll(auth, dtoImport.getIDCiselnik(),
					dtoVal.getImportZmenaDTO(), wfDefList, ucetMap);
			if (StringUtils.isValid(dtoWf)) {
				ActionResult res = cudPau.workflowUpdateCrd(auth, dtoWf, dtoVal.getImportZmenaDTO(), new Date());
				dtoZmenaZap = (DTOZmena) res.getResult();
				if (dtoZmenaZap != null) {
					dlg.getCrdAktualizujCiselnikClass().aktualizujCiselnik(auth, dtoZmenaZap);
				}
			} else { // dtoWf not valid
				popisSpracovania = "NeÚspešné spracovanie OLDPRIMLOC DTOWorkflow is null ";

				log.debug(popisSpracovania);
			}
		}
		return "OK";
	}
}
