package sk.ditec.cud.bi;

import java.util.Date;
import java.util.List;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOZmenaStavHist;
import sk.ditec.dao.meta.CudZmenaStavHist;
import sk.ditec.dao.meta.CudZmenaStavHistPeer;

public class CudZmenaStavHistModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOZmenaStavHist dto) throws AppException {

		try {
			CudZmenaStavHist dao = null;

			if (StringUtils.isValid(dto.getZmenaStavHistID())) {
				dao = CudZmenaStavHistPeer.retrieveByPK(dto.getZmenaStavHistID(), auth.T);
			} else {
				dao = new CudZmenaStavHist();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setIdZmena(dto.getIDZmena());
			dao.setStav(dto.getStav());
			dao.setCasVytvorenia(dto.getCasVytvorenia());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setZmenaStavHistID(dao.getZmenaStavHistId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public ActionResult update(AuthInfo auth, List<DTOZmenaStavHist> list, Integer zmenaID, Date d) throws AppException {

		try {
			if (!StringUtils.isValid(list) || list.isEmpty()) {
				return new ActionResult();
			}

			for (DTOZmenaStavHist dto : list) {
				dto.setIDZmena(zmenaID);
				dto.setCasVytvorenia(d);
				updateSoft(auth, dto);
			}

			return new ActionResult();

		} catch (Throwable t) {
			return handleException(t, "update.error", auth);
		}
	}

}
