package sk.ditec.crd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.dto.DTOCrdNenajdeneZaznamy;
import sk.ditec.dao.meta.CrdNenajdeneZaznamy;
import sk.ditec.dao.meta.CrdNenajdeneZaznamyPeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.Record;



public class CrdNenajdeneZaznamyClass extends _NovyPISBaseClass  {

	public ArrayList<DTOCrdNenajdeneZaznamy> getList(AuthInfo auth, DTOCrdNenajdeneZaznamy dtoF) throws AppException {
		try {
			if (dtoF == null) {
				dtoF = new DTOCrdNenajdeneZaznamy();
			}			
			
			MyCriteria2 crit = new MyCriteria2(CrdNenajdeneZaznamyPeer.CRD_NENAJDENE_ZAZNAMY_ID, dtoF);
			CrdNenajdeneZaznamyPeer.addSelectColumns(crit);
			crit.addConditional(CrdNenajdeneZaznamyPeer.CRD_NENAJDENE_ZAZNAMY_ID, dtoF.getCrdNenajdeneZaznamyID());
			crit.addConditional(CrdNenajdeneZaznamyPeer.ID_CRD_SPRAC_TABULIEK, dtoF.getIdCrdSpracTabuliek());
			crit.addConditional(CrdNenajdeneZaznamyPeer.POPIS, dtoF.getPopis());
			crit.addConditional(CrdNenajdeneZaznamyPeer.CHYBOVA_SPRAVA, dtoF.getChybovaSprava());
			crit.addConditional(CrdNenajdeneZaznamyPeer.CHYBA, dtoF.getChyba());
			crit.addConditional(CrdNenajdeneZaznamyPeer.VAROVANIE, dtoF.getVarovanie());
	String sql = crit.getSQL();

		getConnection(auth);
	List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
	returnConnection(auth);
	Iterator<?> iter = lp.iterator();
	ArrayList<DTOCrdNenajdeneZaznamy> listDto = new ArrayList<DTOCrdNenajdeneZaznamy>();
	while (iter.hasNext()) {
		DTOCrdNenajdeneZaznamy dto = new DTOCrdNenajdeneZaznamy();
		Record r = (Record) iter.next();
		dto.setCrdNenajdeneZaznamyID(rVal(r, CrdNenajdeneZaznamyPeer.CRD_NENAJDENE_ZAZNAMY_ID).asIntegerObj());
		dto.setIdCrdSpracTabuliek(rVal(r, CrdNenajdeneZaznamyPeer.ID_CRD_SPRAC_TABULIEK).asIntegerObj());
		dto.setPopis(rVal(r, CrdNenajdeneZaznamyPeer.POPIS).asString());
		dto.setChybovaSprava(rVal(r, CrdNenajdeneZaznamyPeer.CHYBOVA_SPRAVA).asString());
		dto.setChyba(rVal(r, CrdNenajdeneZaznamyPeer.CHYBA).asString());
		dto.setVarovanie(rVal(r, CrdNenajdeneZaznamyPeer.VAROVANIE).asString());
		listDto.add(dto);
	}
  return listDto;

} catch (Throwable t) {
			handleException(t, " CrdNenajdeneZaznamyClass.getlist.error", auth);
	return null;
}

}

	public ActionResult update(AuthInfo auth, DTOCrdNenajdeneZaznamy dto) throws AppException {

		try {
			getConnection(auth);

			CrdNenajdeneZaznamy dao = new CrdNenajdeneZaznamy();
			if (StringUtils.isValid(dto.getIdCrdSpracTabuliek())) {
				dao = CrdNenajdeneZaznamyPeer.retrieveByPK(dto.getCrdNenajdeneZaznamyID(), auth.T);
			}

			dao.setCrdNenajdeneZaznamyId(dto.getCrdNenajdeneZaznamyID());
			dao.setIdCrdSpracTabuliek(dto.getIdCrdSpracTabuliek());
			if (dto.getPopis() != null && dto.getPopis().length() > 500) {
				dto.setPopis(dto.getPopis().substring(0, 499));
			}
			dao.setPopis(dto.getPopis());
			if (dto.getChybovaSprava() != null && dto.getChybovaSprava().length() > 500) {
				dto.setChybovaSprava(dto.getChybovaSprava().substring(0, 499));
			}
			dao.setChybovaSprava(dto.getChybovaSprava());
			dao.setChyba(dto.getChyba());
			dao.setVarovanie(dto.getVarovanie());
			dao.save(auth.T);
			dto.setCrdNenajdeneZaznamyID(dao.getCrdNenajdeneZaznamyId());
			returnConnection(auth);

			return new ActionResult(dto);

		} catch (Throwable e) {
			handleException(e, " CrdNenajdeneZaznamyClass.update.error", auth);
			return null;
		}
	}

}
