package sk.ditec.cud.bi;

import java.util.Date;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOOdberatel;
import sk.ditec.dao.meta.CudOdberatel;
import sk.ditec.dao.meta.CudOdberatelPeer;

public class CudOdberatelModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOOdberatel dto, Date d) throws AppException {

		try {
			CudOdberatel dao = null;

			if (StringUtils.isValid(dto.getOdberatelID())) {
				dao = CudOdberatelPeer.retrieveByPK(dto.getOdberatelID(), auth.T);
			} else {
				dao = new CudOdberatel();
			}

			dao.setOdberatelId(dto.getOdberatelID());
			dao.setIdHistDopravca(dto.getIDHistDopravca());
			dao.setNazov(dto.getNazov());
			dao.setObmUcetNazov(dto.getObmUcetNazov());
			dao.setRolaKod(dto.getRolaKod());
			dao.setRolaNazov(dto.getRolaNazov());
			dao.setExportTyp(dto.getExportTyp());
			dao.setExportCesta(dto.getExportCesta());
			dao.setAktivny(dto.getAktivny());
			dao.setInterny(dto.getInterny());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setOdberatelID(dao.getOdberatelId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOOdberatel dto) throws AppException {

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

	public String deleteSoft(AuthInfo auth, Integer odberatelID, Date d) throws AppException {

		try {
			// delete soft
			CudOdberatel dao = CudOdberatelPeer.retrieveByPK(odberatelID);
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

	public String delete(AuthInfo auth, Integer odberatelID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			deleteSoft(auth, odberatelID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

}
