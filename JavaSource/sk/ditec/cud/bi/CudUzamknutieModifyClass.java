package sk.ditec.cud.bi;

import java.util.Date;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOUzamknutie;
import sk.ditec.dao.meta.CudUzamknutie;
import sk.ditec.dao.meta.CudUzamknutiePeer;

public class CudUzamknutieModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOUzamknutie dto, Date d) throws AppException {

		try {
			CudUzamknutie dao = null;

			if (StringUtils.isValid(dto.getUzamknutieID())) {
				dao = CudUzamknutiePeer.retrieveByPK(dto.getUzamknutieID(), auth.T);
			} else {
				dao = new CudUzamknutie();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setRowId(dto.getRowID());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setUzamknutieID(dao.getUzamknutieId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOUzamknutie dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			updateSoft(auth, dto, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	private String deleteSoft(AuthInfo auth, Integer uzamknutieID, Date d) throws AppException {

		try {
			// delete soft
			CudUzamknutie dao = CudUzamknutiePeer.retrieveByPK(uzamknutieID);
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZrusene(auth.getTransakciaID());

			dao.save(auth.T);

			return null;

		} catch (Throwable t) {
			handleException(t, "deleteSoft.error", auth);
			return null;
		}
	}

	private String delete(AuthInfo auth, Integer uzamknutieID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			deleteSoft(auth, uzamknutieID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

	public String deleteRow(AuthInfo auth, DTOUzamknutie dto) throws AppException {

		try {
			DTOUzamknutie dtoU = getDelegate().getUzamknutieRead().rowReadLight(auth, dto.getIDCiselnik(), dto.getRowID());
			if (!StringUtils.isValid(dtoU)) {
				return null;
			}

			return delete(auth, dtoU.getUzamknutieID());

		} catch (Throwable t) {
			handleException(t, "deleteRow.error", auth);
			return null;
		}
	}

	public String deleteCis(AuthInfo auth, DTOUzamknutie dto) throws AppException {

		try {
			DTOUzamknutie dtoU = getDelegate().getUzamknutieRead().cisReadLight(auth, dto.getIDCiselnik());
			if (!StringUtils.isValid(dtoU)) {
				return null;
			}

			return delete(auth, dtoU.getUzamknutieID());

		} catch (Throwable t) {
			handleException(t, "deleteCis.error", auth);
			return null;
		}
	}

}
