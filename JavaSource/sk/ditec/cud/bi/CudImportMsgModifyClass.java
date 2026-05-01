package sk.ditec.cud.bi;

import org.apache.torque.util.BasePeer;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.dao.meta.CudImportMsg;
import sk.ditec.dao.meta.CudImportMsgPeer;

public class CudImportMsgModifyClass extends _CudBaseClass {

	public void deleteHard(AuthInfo auth, Integer importID) throws AppException {

		try {
			getConnection(auth);

			// hard delete
			String sql = "DELETE FROM " + CudImportMsgPeer.TABLE_NAME + " WHERE " + CudImportMsgPeer.ID_IMPORT + " = " + importID;

			BasePeer.executeStatement(sql, auth.T);

			returnConnection(auth);

		} catch (Throwable t) {
			handleException(t, "deleteHard.error", auth);
		}
	}

	public void updateSoft(AuthInfo auth, DTOImportMsg dto) throws AppException {

		try {
			CudImportMsg dao = null;

			if (StringUtils.isValid(dto.getImportMsgID())) {
				dao = CudImportMsgPeer.retrieveByPK(dto.getImportMsgID(), auth.T);
			} else {
				dao = new CudImportMsg();
			}

			dao.setIdImport(dto.getIDImport());
			dao.setIdImportZmena(dto.getIDImportZmena());
			dao.setTyp(dto.getTyp());
			dao.setMsg(dto.getMsg());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setImportMsgID(dao.getImportMsgId());

		} catch (Throwable t) {
			handleException(t, "updateSoft.error", auth);
		}
	}

	public void update(AuthInfo auth, DTOImportMsg[] pole, Integer importID, Integer importZmenaID) throws AppException {

		try {
			if (StringUtils.isValid(pole)) {
				for (DTOImportMsg dto : pole) {
					dto.setIDImport(importID);
					dto.setIDImportZmena(importZmenaID);
					updateSoft(auth, dto);
				}
			}

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
		}
	}

}
