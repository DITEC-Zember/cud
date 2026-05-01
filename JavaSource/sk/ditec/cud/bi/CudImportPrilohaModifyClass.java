package sk.ditec.cud.bi;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImportPriloha;
import sk.ditec.dao.meta.CudImportPriloha;
import sk.ditec.dao.meta.CudImportPrilohaPeer;

public class CudImportPrilohaModifyClass extends _CudBaseClass {

	public void deleteHard(AuthInfo auth, Integer importID) throws AppException {

		try {
			getConnection(auth);

			// hard delete
			MyCriteria2 crit = new MyCriteria2();
			crit.add(CudImportPrilohaPeer.ID_IMPORT, importID);

			CudImportPrilohaPeer.doDelete(crit, auth.T);

			returnConnection(auth);

		} catch (Throwable t) {
			handleException(t, "deleteHard.error", auth);
		}
	}

	public ActionResult updateSoft(AuthInfo auth, DTOImportPriloha dto, Integer importID) throws AppException {

		try {
			CudImportPriloha dao = null;

			if (StringUtils.isValid(dto.getImportPrilohaID())) {
				dao = CudImportPrilohaPeer.retrieveByPK(dto.getImportPrilohaID(), auth.T);
			} else {
				dao = new CudImportPriloha();
			}

			dao.setIdImport(importID);
			dao.setFileName(dto.getFileName());
			dao.setPriloha(dto.getPriloha());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setImportPrilohaID(dao.getImportPrilohaId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

}
