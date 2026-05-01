package sk.ditec.cud.bi;

import java.util.Date;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOImportPriloha;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudImport;
import sk.ditec.dao.meta.CudImportPeer;

public class CudImportModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOImport dto, Date d) throws AppException {

		try {
			CudImport dao = null;

			if (StringUtils.isValid(dto.getImportID())) {
				dao = CudImportPeer.retrieveByPK(dto.getImportID(), auth.T);
			} else {
				dao = new CudImport();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setCiselnikTabulka(dto.getCiselnikTabulka());
			dao.setStav(dto.getStav());
			dao.setCasVytvorenia(d);
			dao.setCasKontrolaZac(dto.getCasKontrolaZac());
			dao.setCasKontrolaKon(dto.getCasKontrolaKon());
			dao.setCasImportZac(dto.getCasImportZac());
			dao.setCasImportKon(dto.getCasImportKon());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setImportID(dao.getImportId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOImport dto, DTOImportPriloha dtoPriloha) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			updateSoft(auth, dto, d);
			getDelegate().getImportPrilohaModify().updateSoft(auth, dtoPriloha, dto.getImportID());

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	private void deleteHard(AuthInfo auth, Integer importID) throws AppException {

		try {
			getConnection(auth);

			// hard delete
			MyCriteria2 crit = new MyCriteria2();
			crit.add(CudImportPeer.IMPORT_ID, importID);

			CudImportPeer.doDelete(crit, auth.T);

			returnConnection(auth);

		} catch (Throwable t) {
			handleException(t, "deleteHard.error", auth);
		}
	}

	public void deleteHardAll(AuthInfo auth, Integer importID) throws AppException {

		try {
			getDelegate().getImportZmenaStlpecModify().deleteHard(auth, importID);
			getDelegate().getImportMsgModify().deleteHard(auth, importID);
			getDelegate().getImportPrilohaModify().deleteHard(auth, importID);
			getDelegate().getImportZmenaModify().deleteHard(auth, importID);
			deleteHard(auth, importID);

		} catch (Throwable t) {
			handleException(t, "deleteHardAll.error", auth);
		}
	}

	private ActionResult deleteSoft(AuthInfo auth, Integer importID, Date d) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudImportPeer.IMPORT_ID, importID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudImportPeer.CAS_ZMENY, d);
			valuesCrit.add(CudImportPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudImportPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudImportPeer.doUpdate(whereCrit, valuesCrit, auth.T);

			return new ActionResult(null);

		} catch (Throwable t) {
			return handleException(t, "deleteSoft.error", auth);
		}
	}

	public String delete(AuthInfo auth, Integer importID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			deleteSoft(auth, importID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

	private ActionResult updateStavSoft(AuthInfo auth, Integer importID, Date d) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudImportPeer.IMPORT_ID, importID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudImportPeer.STAV, _CudConsts.IMPORT_STAV_IMPORT);
			valuesCrit.add(CudImportPeer.CAS_ZMENY, d);
			valuesCrit.add(CudImportPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudImportPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudImportPeer.doUpdate(whereCrit, valuesCrit, auth.T);

			return new ActionResult(null);

		} catch (Throwable t) {
			return handleException(t, "updateStavSoft.error", auth);
		}
	}

	public String updateStav(AuthInfo auth, Integer importID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			updateStavSoft(auth, importID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "updateStav.error", auth);
			return null;
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOImport dto) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudImportPeer.IMPORT_ID, dto.getImportID());

			MyCriteria2 valuesCrit = new MyCriteria2();
			if (StringUtils.isValid(dto.getStav())) {
				valuesCrit.add(CudImportPeer.STAV, dto.getStav());
			}
			if (StringUtils.isValid(dto.getCasKontrolaZac())) {
				valuesCrit.add(CudImportPeer.CAS_KONTROLA_ZAC, dto.getCasKontrolaZac());
			}
			if (StringUtils.isValid(dto.getCasKontrolaKon())) {
				valuesCrit.add(CudImportPeer.CAS_KONTROLA_KON, dto.getCasKontrolaKon());
			}
			if (StringUtils.isValid(dto.getCasImportZac())) {
				valuesCrit.add(CudImportPeer.CAS_IMPORT_ZAC, dto.getCasImportZac());
			}
			if (StringUtils.isValid(dto.getCasImportKon())) {
				valuesCrit.add(CudImportPeer.CAS_IMPORT_KON, dto.getCasImportKon());
			}
			valuesCrit.add(CudImportPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudImportPeer.doUpdate(whereCrit, valuesCrit, auth.T);

			return new ActionResult(null);

		} catch (Throwable t) {
			return handleException(t, "updateCasSoft.error", auth);
		}
	}

	public String updateBeforeKontrola(AuthInfo auth, Integer importID) throws AppException {

		try {
			Date d = new Date();

			getConnection(auth);

			// aktualzujem stavovu tabulku
			DTOImport dto = new DTOImport();
			dto.setImportID(importID);
			dto.setCasKontrolaZac(d);
			updateSoft(auth, dto);

			returnConnection(auth);

			return null;

		} catch (Throwable t) {
			handleException(t, "updateBeforeKontrola.error", auth);
			return null;
		}
	}

	public String updateAfterKontrola(AuthInfo auth, Integer importID) throws AppException {

		try {
			Date d = new Date();

			getConnection(auth);

			// aktualzujem stavovu tabulku
			DTOImport dto = new DTOImport();
			dto.setImportID(importID);
			dto.setCasKontrolaKon(d);
			updateSoft(auth, dto);

			returnConnection(auth);

			return null;

		} catch (Throwable t) {
			handleException(t, "updateAfterKontrola.error", auth);
			return null;
		}
	}

	public String updateBeforeImport(AuthInfo auth, Integer importID) throws AppException {

		try {
			Date d = new Date();

			getConnection(auth);

			// aktualzujem stavovu tabulku
			DTOImport dto = new DTOImport();
			dto.setImportID(importID);
			dto.setCasImportZac(d);
			updateSoft(auth, dto);

			returnConnection(auth);

			return null;

		} catch (Throwable t) {
			handleException(t, "updateBeforeImport.error", auth);
			return null;
		}
	}

	public String updateAfterImport(AuthInfo auth, Integer importID) throws AppException {

		try {
			Date d = new Date();

			getConnection(auth);

			// aktualzujem stavovu tabulku
			DTOImport dto = new DTOImport();
			dto.setImportID(importID);
			dto.setCasImportKon(d);
			updateSoft(auth, dto);

			returnConnection(auth);

			return null;

		} catch (Throwable t) {
			handleException(t, "updateAfterImport.error", auth);
			return null;
		}
	}

	public String updateError(AuthInfo auth, Integer importID, String errorMsg) throws AppException {

		try {
			getConnection(auth);

			DTOImport dto = new DTOImport();
			dto.setImportID(importID);
			dto.setStav(_CudConsts.IMPORT_STAV_ERROR);
			updateSoft(auth, dto);

			if (StringUtils.isValid(errorMsg)) {
				DTOImportMsg dtoMsg = new DTOImportMsg();
				dtoMsg.setIDImport(importID);
				dtoMsg.setTyp(_CudConsts.IMPORT_MSG_TYP_ERROR);
				dtoMsg.setMsg(errorMsg);
				getDelegate().getImportMsgModify().updateSoft(auth, dtoMsg);
			}

			returnConnection(auth);

			return null;

		} catch (Throwable t) {
			handleException(t, "updateError.error", auth);
			return null;
		}
	}

}
