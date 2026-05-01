package sk.ditec.crd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.dto.DTOCrdSpracTabuliek;
import sk.ditec.dao.meta.CrdNenajdeneZaznamyPeer;
import sk.ditec.dao.meta.CrdSpracTabuliek;
import sk.ditec.dao.meta.CrdSpracTabuliekPeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.Record;



public class CrdSpracTabuliekClass extends _NovyPISBaseClass {

	public ArrayList<DTOCrdSpracTabuliek> getList(AuthInfo auth, DTOCrdSpracTabuliek dtoF)
			throws AppException {
		try {
			if (dtoF == null) {
				dtoF = new DTOCrdSpracTabuliek();
			}			
			
			MyCriteria2 crit = new MyCriteria2(CrdSpracTabuliekPeer.CRD_SPRAC_TABULIEK_ID, dtoF);
			CrdNenajdeneZaznamyPeer.addSelectColumns(crit);
			crit.addConditional(CrdSpracTabuliekPeer.CRD_SPRAC_TABULIEK_ID, dtoF.getCrdSpracTabuliekId());
			crit.addConditional(CrdSpracTabuliekPeer.ID_CRD_SPRACOVANIE, dtoF.getIdCrdSpracovanie());
			crit.addConditional(CrdSpracTabuliekPeer.ID_CISELNIK, dtoF.getIdCiselnik());
			crit.addConditional(CrdSpracTabuliekPeer.DATE_FILTER_FOR_DELETED, dtoF.getDateFilterForDeleted(),
					Criteria.EQUAL);
			crit.addConditional(CrdSpracTabuliekPeer.REPLICATE_FROM_DATE, dtoF.getReplicateFromDate(), Criteria.EQUAL);
			crit.addConditional(CrdSpracTabuliekPeer.REPLICATE_ALL, dtoF.getReplicateAll());
			crit.addConditional(CrdSpracTabuliekPeer.NAVRATOVY_KOD, dtoF.getNavratovyKod());
			crit.addConditional(CrdSpracTabuliekPeer.POPIS_SPRACOVANIA, dtoF.getPopisSpracovania());
			crit.addConditional(CrdSpracTabuliekPeer.VSTUPNE_XML, dtoF.getVstupneXml());
			crit.addConditional(CrdSpracTabuliekPeer.VYSTUPNE_XML, dtoF.getVystupneXml());
			crit.addConditional(CrdSpracTabuliekPeer.ZMENOVE_XML_VSTUP, dtoF.getZmenoveXmlVstup());
			crit.addConditional(CrdSpracTabuliekPeer.ZMENOVE_XML_VYSTUP, dtoF.getZmenoveXmlVystup());
	String sql = crit.getSQL();

		getConnection(auth);
	List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
	returnConnection(auth);
	Iterator<?> iter = lp.iterator();
			ArrayList<DTOCrdSpracTabuliek> listDto = new ArrayList<DTOCrdSpracTabuliek>();
	while (iter.hasNext()) {
				DTOCrdSpracTabuliek dto = new DTOCrdSpracTabuliek();
		Record r = (Record) iter.next();
				dto.setCrdSpracTabuliekId(rVal(r, CrdSpracTabuliekPeer.CRD_SPRAC_TABULIEK_ID).asIntegerObj());
				dto.setIdCrdSpracovanie(rVal(r, CrdSpracTabuliekPeer.ID_CRD_SPRACOVANIE).asIntegerObj());
				dto.setIdCiselnik(rVal(r, CrdSpracTabuliekPeer.ID_CISELNIK).asIntegerObj());
				dto.setDateFilterForDeleted(rVal(r, CrdSpracTabuliekPeer.DATE_FILTER_FOR_DELETED).asUtilDate());
				dto.setReplicateFromDate(rVal(r, CrdSpracTabuliekPeer.REPLICATE_FROM_DATE).asUtilDate());
				dto.setReplicateAll(rVal(r, CrdSpracTabuliekPeer.REPLICATE_ALL).asString());
				dto.setNavratovyKod(rVal(r, CrdSpracTabuliekPeer.NAVRATOVY_KOD).asIntegerObj());
				dto.setPopisSpracovania(rVal(r, CrdSpracTabuliekPeer.POPIS_SPRACOVANIA).asString());
				dto.setVstupneXml(rVal(r, CrdSpracTabuliekPeer.VSTUPNE_XML).asString());
				dto.setVystupneXml(rVal(r, CrdSpracTabuliekPeer.VYSTUPNE_XML).asString());
				dto.setZmenoveXmlVstup(rVal(r, CrdSpracTabuliekPeer.ZMENOVE_XML_VSTUP).asString());
				dto.setZmenoveXmlVystup(rVal(r, CrdSpracTabuliekPeer.ZMENOVE_XML_VYSTUP).asString());
		listDto.add(dto);
	}
  return listDto;

} catch (Throwable t) {
			handleException(t, "CrdSpracTabuliekClass.getlist.error", auth);
	return null;
}

}

	public ActionResult updateANuluj(AuthInfo auth, DTOCrdSpracTabuliek dto) throws AppException {
		dto = (DTOCrdSpracTabuliek) update(auth, dto).getResult();
		dto.setCrdSpracTabuliekId(null);
		dto.setPopisSpracovania(null);
		dto.setZmenoveXmlVstup(null);
		dto.setZmenoveXmlVystup(null);
		dto.setNavratovyKod(null);
		dto.setReplicateAll("N");
		dto.setVstupneXml(null);
		dto.setVystupneXml(null);
		return new ActionResult(dto);

	}

	public ActionResult updateANulujZmenoveXML(AuthInfo auth, DTOCrdSpracTabuliek dto) throws AppException {
		dto = (DTOCrdSpracTabuliek) update(auth, dto).getResult();
		dto.setCrdSpracTabuliekId(null);
		dto.setZmenoveXmlVystup(null);
		return new ActionResult(dto);

	}

	public ActionResult update(AuthInfo auth, DTOCrdSpracTabuliek dto) throws AppException {

		try {
			getConnection(auth);

			CrdSpracTabuliek dao = new CrdSpracTabuliek();
			if (StringUtils.isValid(dto.getCrdSpracTabuliekId())) {
				dao = CrdSpracTabuliekPeer.retrieveByPK(dto.getCrdSpracTabuliekId(), auth.T);
			}
			dao.setCrdSpracTabuliekId(dto.getCrdSpracTabuliekId());
			dao.setIdCrdSpracovanie(dto.getIdCrdSpracovanie());
			dao.setIdCiselnik(dto.getIdCiselnik());
			dao.setDateFilterForDeleted(dto.getDateFilterForDeleted());
			dao.setReplicateFromDate(dto.getReplicateFromDate());
			dao.setReplicateAll(dto.getReplicateAll());
			dao.setNavratovyKod(dto.getNavratovyKod());

			if (dto.getPopisSpracovania() != null && dto.getPopisSpracovania().length() > 450) {
				dto.setPopisSpracovania(dto.getPopisSpracovania().substring(0, 449));
			}
			dao.setPopisSpracovania(dto.getPopisSpracovania());
			dao.setVstupneXml(dto.getVstupneXml());
			dao.setVystupneXml(dto.getVystupneXml());
			dao.setZmenoveXmlVstup(dto.getZmenoveXmlVstup());
			dao.setZmenoveXmlVystup(dto.getZmenoveXmlVystup());
			dao.save(auth.T);
			dto.setCrdSpracTabuliekId(dao.getCrdSpracTabuliekId());
			returnConnection(auth);

			return new ActionResult(dto);

		} catch (Throwable e) {
			handleException(e, "CrdSpracTabuliekClass.update.error", auth);
			return null;
		}
	}

}
