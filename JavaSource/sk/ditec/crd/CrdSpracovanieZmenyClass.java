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
import sk.ditec.crd.dto.DTOCrdSpracovanieZmeny;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.dao.meta.CrdNenajdeneZaznamyPeer;
import sk.ditec.dao.meta.CrdSpracovanieZmeny;
import sk.ditec.dao.meta.CrdSpracovanieZmenyPeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.Record;



public class CrdSpracovanieZmenyClass extends _NovyPISBaseClass {

	public ArrayList<DTOCrdSpracovanieZmeny> getList(AuthInfo auth, DTOCrdSpracovanieZmeny dtoF) throws AppException {
		try {
			if (dtoF == null) {
				dtoF = new DTOCrdSpracovanieZmeny();
			}			
			
			MyCriteria2 crit = new MyCriteria2(CrdSpracovanieZmenyPeer.CRD_SPRACOVANIE_ZMENY_ID, dtoF);
			CrdNenajdeneZaznamyPeer.addSelectColumns(crit);
			crit.addConditional(CrdSpracovanieZmenyPeer.CRD_SPRACOVANIE_ZMENY_ID, dtoF.getCrdSpracovanieZmenyID());
			crit.addConditional(CrdSpracovanieZmenyPeer.ID_ZMENA, dtoF.getIDZmena());
			crit.addConditional(CrdSpracovanieZmenyPeer.DATUM_VOLANIA, dtoF.getDatumVolania(), Criteria.EQUAL);
			crit.addConditional(CrdSpracovanieZmenyPeer.CHYBOVA_SPRAVA, dtoF.getChybovaSprava());
			crit.addConditional(CrdSpracovanieZmenyPeer.CHYBA, dtoF.getChyba());
	String sql = crit.getSQL();

		getConnection(auth);
	List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
	returnConnection(auth);
	Iterator<?> iter = lp.iterator();
			ArrayList<DTOCrdSpracovanieZmeny> listDto = new ArrayList<DTOCrdSpracovanieZmeny>();
	while (iter.hasNext()) {
				DTOCrdSpracovanieZmeny dto = new DTOCrdSpracovanieZmeny();
		Record r = (Record) iter.next();

				dto.setCrdSpracovanieZmenyID(rVal(r, CrdSpracovanieZmenyPeer.CRD_SPRACOVANIE_ZMENY_ID).asIntegerObj());
				dto.setIDZmena(rVal(r, CrdSpracovanieZmenyPeer.ID_ZMENA).asIntegerObj());
				dto.setDatumVolania(rVal(r, CrdSpracovanieZmenyPeer.DATUM_VOLANIA).asUtilDate());
				dto.setChybovaSprava(rVal(r, CrdSpracovanieZmenyPeer.CHYBOVA_SPRAVA).asString());
				dto.setChyba(rVal(r, CrdSpracovanieZmenyPeer.CHYBA).asString());
		listDto.add(dto);
	}
  return listDto;

} catch (Throwable t) {
			handleException(t, "CrdSpracovanieZmenyClass.getlist.error", auth);
	return null;
}
	}



	public DTOCrdSpracovanieZmeny getSpracovaneDto(AuthInfo auth, Integer id) throws AppException {
		try {

			MyCriteria2 crit = new MyCriteria2(CrdSpracovanieZmenyPeer.CRD_SPRACOVANIE_ZMENY_ID,
					CrdSpracovanieZmenyPeer.TABLE_NAME);
			CrdSpracovanieZmenyPeer.addSelectColumns(crit);
			crit.addConditional(CrdSpracovanieZmenyPeer.ID_ZMENA, id);
			// crit.addCustomSql(CrdSpracovanieZmenyPeer.CHYBA, CrdSpracovanieZmenyPeer.CHYBA + " is null ");
			// crit.addIn(CrdSpracovanieZmenyPeer.DATUM_VOLANIA, ISNOTNULL);

			// Ak neexistuje zmenaCRD alebo zmenaCRD.ERROR is null
			// ked je v chybe null, tak je to spracovane uz nespracujem.


			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				DTOCrdSpracovanieZmeny dto = new DTOCrdSpracovanieZmeny();
				Record r = (Record) iter.next();
				dto.setChyba(rVal(r, CrdSpracovanieZmenyPeer.CHYBA).asString());
				if ("T".equals(dto.getChyba())) {
					return null;
				}

				dto.setCrdSpracovanieZmenyID(rVal(r, CrdSpracovanieZmenyPeer.CRD_SPRACOVANIE_ZMENY_ID).asIntegerObj());
				dto.setIDZmena(rVal(r, CrdSpracovanieZmenyPeer.ID_ZMENA).asIntegerObj());
				dto.setDatumVolania(rVal(r, CrdSpracovanieZmenyPeer.DATUM_VOLANIA).asUtilDate());
				dto.setChybovaSprava(rVal(r, CrdSpracovanieZmenyPeer.CHYBOVA_SPRAVA).asString());

				// if (dto.getChyba()==null){
				return dto;
				// }
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "CrdSpracovanieZmenyClass.getSpracovaneDto.error", auth);
			return null;
		}
	}

	public ActionResult update(AuthInfo auth, DTOCrdSpracovanieZmeny dto) throws AppException {

		try {
			getConnection(auth);

			CrdSpracovanieZmeny dao = new CrdSpracovanieZmeny();
			if (StringUtils.isValid(dto.getCrdSpracovanieZmenyID())) {
				dao = CrdSpracovanieZmenyPeer.retrieveByPK(dto.getCrdSpracovanieZmenyID(), auth.T);
			}

			dao.setCrdSpracovanieZmenyId(dto.getCrdSpracovanieZmenyID());
			dao.setIdZmena(dto.getIDZmena());
			dao.setDatumVolania(dto.getDatumVolania());
			// length oracle a jave nie je rovnake, bud diakritika alebo formatovacie znaky
			// su tam znaky \n, co je pre Orle dlzka 2 namisto jedneho znaku tak to radsej zmensim na 490
			// problemom su aj diakriticke znaky, preto ich odstranim
			if (dto.getChybovaSprava() != null && dto.getChybovaSprava().length() > 950) {
				dto.setChybovaSprava(dto.getChybovaSprava().substring(0, 950));
			}
			// dao.setChybovaSprava(dto.getChybovaSprava());
			dao.setChybovaSprava(CudVysielanieUtils.getStringBezDia(dto.getChybovaSprava()));
			dao.setChyba(dto.getChyba());
			dao.save(auth.T);
			returnConnection(auth);
			dto.setCrdSpracovanieZmenyID(dao.getCrdSpracovanieZmenyId());
			return new ActionResult(dto);

		} catch (Throwable e) {
			handleException(e, "CrdSpracovanieZmenyClass.update.error", auth);
			return null;
		}
	}

}
