package sk.ditec.crd;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.dto.DTOSend;
import sk.ditec.dao.meta.CudSend;
import sk.ditec.dao.meta.CudSendPeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.Record;

public class CudSendClass extends _NovyPISBaseClass {

	public ArrayList<DTOSend> getList(AuthInfo auth, DTOSend dtoF)
			throws AppException {
		try {
			if (dtoF == null) {
				dtoF = new DTOSend();
			}

			MyCriteria2 crit = new MyCriteria2(CudSendPeer.SEND_ID, dtoF);
			CudSendPeer.addSelectColumns(crit);
			crit.addConditional(CudSendPeer.SEND_ID, dtoF.getSendID());
			crit.addConditional(CudSendPeer.ID_ODBERATEL_OBJEKT, dtoF.getIDOdberatelObjekt());
			crit.addConditional(CudSendPeer.CAS_VYTVORENIA, dtoF.getCasVytvorenia(), Criteria.EQUAL);
			crit.addConditional(CudSendPeer.SPRAVA_UUID, dtoF.getSpravaUuid());
			crit.addConditional(CudSendPeer.SPRAVA_TYP, dtoF.getSpravaTyp());
			crit.addConditional(CudSendPeer.ID_TRANSAKCIA_ZAPISANE, dtoF.getIdTransakciaZapisane(), Criteria.EQUAL);
			crit.addConditional(CudSendPeer.ID_TRANSAKCIA_ZRUSENE, dtoF.getIdTransakciaZrusene(), Criteria.EQUAL);
			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOSend> listDto = new ArrayList<DTOSend>();
			while (iter.hasNext()) {
				DTOSend dto = new DTOSend();
				Record r = (Record) iter.next();
				dto.setSendID(rVal(r, CudSendPeer.SEND_ID).asIntegerObj());
				dto.setIDOdberatelObjekt(rVal(r, CudSendPeer.ID_ODBERATEL_OBJEKT).asIntegerObj());
				dto.setCasVytvorenia(rVal(r, CudSendPeer.CAS_VYTVORENIA).asUtilDate());
				dto.setSpravaUuid(rVal(r, CudSendPeer.SPRAVA_UUID).asString());
				dto.setSpravaTyp(rVal(r, CudSendPeer.SPRAVA_TYP).asString());
				dto.setIdTransakciaZapisane(rVal(r, CudSendPeer.ID_TRANSAKCIA_ZAPISANE).asLong());
				dto.setIdTransakciaZrusene(rVal(r, CudSendPeer.ID_TRANSAKCIA_ZRUSENE).asLong());

				listDto.add(dto);
			}
			return listDto;

		} catch (Throwable t) {
			handleException(t, "CudSendClass.getlist.error", auth);
			return null;
		}
	}

	public ActionResult update(AuthInfo auth, DTOSend dto) throws AppException {

		try {
			getConnection(auth);

			CudSend dao = new CudSend();
			if (StringUtils.isValid(dto.getSendID())) {
				dao = CudSendPeer.retrieveByPK(dto.getSendID(), auth.T);
			}

			dao.setSendId(dto.getSendID());
			dao.setIdOdberatelObjekt(dto.getIDOdberatelObjekt());
			dao.setCasVytvorenia(dto.getCasVytvorenia());
			dao.setSpravaUuid(dto.getSpravaUuid());
			dao.setSpravaTyp(dto.getSpravaTyp());
			dao.setIdTransakciaZapisane(dto.getIdTransakciaZapisane());
			dao.setIdTransakciaZrusene(dto.getIdTransakciaZrusene());

			dao.save(auth.T);
			dto.setSendID(dao.getSendId());
			returnConnection(auth);

			return new ActionResult(dto);

		} catch (Throwable e) {
			handleException(e, "CudSendClass.update.error", auth);
			return null;
		}
	}

	public DTOSend vytvorZaznam(AuthInfo auth, Integer odberatelObjektId, String identifikatorSpravy, String typSpravy) throws AppException {
		Date datumACasVytvorenia = new Date();

		// Systém vytvorí záznam v údajoch o odoslaní súboru
		DTOSend dtoSend = new DTOSend();
		dtoSend.setIDOdberatelObjekt(odberatelObjektId);
		dtoSend.setCasVytvorenia(datumACasVytvorenia);
		dtoSend.setSpravaTyp(typSpravy);
		dtoSend.setSpravaUuid(identifikatorSpravy);
		dtoSend.setIdTransakciaZapisane(auth.getTransakciaID());
		ActionResult actionResult = update(auth, dtoSend);
		DTOSend dtoResult = (DTOSend) actionResult.getResult();
		return dtoResult;
	}

}
