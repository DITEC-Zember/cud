package sk.ditec.cud.bi;

import java.util.Date;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.dao.meta.CudZmena;
import sk.ditec.dao.meta.CudZmenaPeer;

public class CudZmenaModifyClass extends _CudBaseClass {

	public ActionResult updateSoft(AuthInfo auth, DTOZmena dto) throws AppException {

		try {
			CudZmena dao = null;

			if (StringUtils.isValid(dto.getZmenaID())) {
				dao = CudZmenaPeer.retrieveByPK(dto.getZmenaID(), auth.T);
			} else {
				dao = new CudZmena();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setRowId(dto.getRowID());
			dao.setOperacia(dto.getOperacia());
			dao.setStav(dto.getStav());
			dao.setPlatnostOd(dto.getPlatnostOd());
			dao.setPlatnostDo(dto.getPlatnostDo());
			dao.setCasSchvaleniaGr(dto.getCasSchvaleniaGr());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setZmenaID(dao.getZmenaId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public ActionResult updatePlatnostDo(AuthInfo auth, Integer zmenaID, Date platnostDo) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.addConditional(CudZmenaPeer.ZMENA_ID, zmenaID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudZmenaPeer.PLATNOST_DO, platnostDo);
			valuesCrit.add(CudZmenaPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudZmenaPeer.doUpdate(whereCrit, valuesCrit, auth.T);

			return new ActionResult(null);

		} catch (Throwable t) {
			handleException(t, "updatePlatnostDo.error", auth);
			return null;
		}
	}
}
