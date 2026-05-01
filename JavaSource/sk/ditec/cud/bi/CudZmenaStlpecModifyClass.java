package sk.ditec.cud.bi;

import java.util.List;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudZmenaStlpec;
import sk.ditec.dao.meta.CudZmenaStlpecPeer;

public class CudZmenaStlpecModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOZmenaStlpec dto) throws AppException {

		try {
			CudZmenaStlpec dao = null;

			if (StringUtils.isValid(dto.getZmenaStlpecID())) {
				dao = CudZmenaStlpecPeer.retrieveByPK(dto.getZmenaStlpecID(), auth.T);
			} else {
				dao = new CudZmenaStlpec();
			}

			if (StringUtils.isValid(dto.getOldValue()) && dto.getOldValue().length() > _CudConsts.MAX_LENGTH_STRING) {
				dao.setOldValue(dto.getOldValue().substring(0, _CudConsts.MAX_LENGTH_STRING));
				dao.setOldValueExt(dto.getOldValue());
			} else {
				dao.setOldValue(dto.getOldValue());
			}

			if (StringUtils.isValid(dto.getNewValue()) && dto.getNewValue().length() > _CudConsts.MAX_LENGTH_STRING) {
				dao.setNewValue(dto.getNewValue().substring(0, _CudConsts.MAX_LENGTH_STRING));
				dao.setNewValueExt(dto.getNewValue());
			} else {
				dao.setNewValue(dto.getNewValue());

			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setIdZmena(dto.getIDZmena());
			dao.setIdCiselnikStlpec(dto.getIDCiselnikStlpec());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setZmenaStlpecID(dao.getZmenaStlpecId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public ActionResult update(AuthInfo auth, List<DTOZmenaStlpec> list, Integer zmenaID) throws AppException {

		try {
			if (!StringUtils.isValid(list) || list.isEmpty()) {
				return new ActionResult();
			}

			for (DTOZmenaStlpec dto : list) {
				dto.setIDZmena(zmenaID);
				updateSoft(auth, dto);
			}

			return new ActionResult();

		} catch (Throwable t) {
			return handleException(t, "update.error", auth);
		}
	}

}
