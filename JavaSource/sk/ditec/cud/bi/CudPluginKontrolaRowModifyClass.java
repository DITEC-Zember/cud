package sk.ditec.cud.bi;

import org.apache.torque.util.BasePeer;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.dao.meta.CudPluginKontrolaRow;
import sk.ditec.dao.meta.CudPluginKontrolaRowPeer;

public class CudPluginKontrolaRowModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOPluginKontrolaRow dto) throws AppException {

		try {
			CudPluginKontrolaRow dao = null;

			if (StringUtils.isValid(dto.getPluginKontrolaRowID())) {
				dao = CudPluginKontrolaRowPeer.retrieveByPK(dto.getPluginKontrolaRowID(), auth.T);
			} else {
				dao = new CudPluginKontrolaRow();
			}

			dao.setIdPluginKontrola(dto.getIDPluginKontrola());
			dao.setIdPlugin(dto.getIDPlugin());
			dao.setRowId(dto.getRowID());
			dao.setStav(dto.getStav());
			dao.setPopis(dto.getPopis());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setPluginKontrolaRowID(dao.getPluginKontrolaRowId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOPluginKontrolaRow[] list) throws AppException {

		try {
			getConnection(auth);

			for (DTOPluginKontrolaRow dto : list) {
				updateSoft(auth, dto);
			}

			returnConnection(auth);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public void deleteHard(AuthInfo auth, Integer pluginKontrolaID) throws AppException {

		try {
			// hard delete
			String sql = "DELETE FROM " + CudPluginKontrolaRowPeer.TABLE_NAME + " WHERE " + CudPluginKontrolaRowPeer.ID_PLUGIN_KONTROLA + " = " + pluginKontrolaID;

			BasePeer.executeStatement(sql, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteHard.error", auth);
		}
	}

}
