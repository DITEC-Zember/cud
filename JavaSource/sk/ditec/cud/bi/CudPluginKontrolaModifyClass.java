package sk.ditec.cud.bi;

import java.util.Date;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOPluginKontrola;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudPluginKontrola;
import sk.ditec.dao.meta.CudPluginKontrolaPeer;
import sk.ditec.dao.meta.CudPluginPeer;

public class CudPluginKontrolaModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOPluginKontrola dto, Date d) throws AppException {

		try {
			CudPluginKontrola dao = null;

			if (StringUtils.isValid(dto.getPluginKontrolaID())) {
				dao = CudPluginKontrolaPeer.retrieveByPK(dto.getPluginKontrolaID(), auth.T);
			} else {
				dao = new CudPluginKontrola();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setPlatnostOd(dto.getPlatnostOd());
			dao.setCasKontrolaZac(dto.getCasKontrolaZac());
			dao.setCasKontrolaKon(dto.getCasKontrolaKon());
			dao.setStav(dto.getStav());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setPluginKontrolaID(dao.getPluginKontrolaId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOPluginKontrola dto) throws AppException {

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

	private ActionResult updateSoft(AuthInfo auth, DTOPluginKontrola dto) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID, dto.getPluginKontrolaID());

			MyCriteria2 valuesCrit = new MyCriteria2();
			if (StringUtils.isValid(dto.getStav())) {
				valuesCrit.add(CudPluginKontrolaPeer.STAV, dto.getStav());
			}
			if (StringUtils.isValid(dto.getCasKontrolaZac())) {
				valuesCrit.add(CudPluginKontrolaPeer.CAS_KONTROLA_ZAC, dto.getCasKontrolaZac());
			}
			if (StringUtils.isValid(dto.getCasKontrolaKon())) {
				valuesCrit.add(CudPluginKontrolaPeer.CAS_KONTROLA_KON, dto.getCasKontrolaKon());
			}
			valuesCrit.add(CudPluginKontrolaPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudPluginKontrolaPeer.doUpdate(whereCrit, valuesCrit, auth.T);

			return new ActionResult(null);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, Integer pluginKontrolaID, boolean casZac, boolean casKon, String stav, String errorMsg) throws AppException {

		try {
			Date d = new Date();

			DTOPluginKontrola dto = new DTOPluginKontrola();
			dto.setPluginKontrolaID(pluginKontrolaID);
			dto.setCasKontrolaZac(casZac ? d : null);
			dto.setCasKontrolaKon(casKon ? d : null);
			dto.setStav(stav);

			DTOPluginKontrolaRow dtoRow = null;
			if (StringUtils.isValid(errorMsg)) {
				dtoRow = new DTOPluginKontrolaRow();
				dtoRow.setIDPluginKontrola(pluginKontrolaID);
				dtoRow.setPopis(errorMsg);
				dtoRow.setStav(_CudConsts.PLUGIN_KONTROLA_ROW_STAV_ERROR);
			}

			getConnection(auth);

			updateSoft(auth, dto);
			if (StringUtils.isValid(dtoRow)) {
				getDelegate().getPluginKontrolaRowModify().update(auth, new DTOPluginKontrolaRow[] { dtoRow });
			}

			returnConnection(auth);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	private void deleteHard(AuthInfo auth, Integer pluginKontrolaID) throws AppException {

		try {
			// hard delete
			MyCriteria2 crit = new MyCriteria2();
			crit.add(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID, pluginKontrolaID);

			CudPluginPeer.doDelete(crit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteHard.error", auth);
		}
	}

	public void deleteHardAll(AuthInfo auth, Integer pluginKontrolaID) throws AppException {

		try {
			getConnection(auth);

			getDelegate().getPluginKontrolaRowModify().deleteHard(auth, pluginKontrolaID);
			deleteHard(auth, pluginKontrolaID);

			returnConnection(auth);

		} catch (Throwable t) {
			handleException(t, "deleteHardAll.error", auth);
		}
	}

	private ActionResult deleteSoft(AuthInfo auth, Integer pluginKontrolaID, Date d) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudPluginKontrolaPeer.PLUGIN_KONTROLA_ID, pluginKontrolaID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudPluginKontrolaPeer.CAS_ZMENY, d);
			valuesCrit.add(CudPluginKontrolaPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudPluginKontrolaPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudPluginKontrolaPeer.doUpdate(whereCrit, valuesCrit, auth.T);

			return new ActionResult(null);

		} catch (Throwable t) {
			return handleException(t, "deleteSoft.error", auth);
		}
	}

	public String delete(AuthInfo auth, Integer pluginKontrolaID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			deleteSoft(auth, pluginKontrolaID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

}
