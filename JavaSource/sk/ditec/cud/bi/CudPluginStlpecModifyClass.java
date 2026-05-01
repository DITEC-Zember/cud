package sk.ditec.cud.bi;

import java.util.Date;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOPluginStlpec;
import sk.ditec.dao.meta.CudPluginStlpec;
import sk.ditec.dao.meta.CudPluginStlpecPeer;

public class CudPluginStlpecModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOPluginStlpec dto, Date d) throws AppException {

		try {
			CudPluginStlpec dao = null;

			if (StringUtils.isValid(dto.getPluginStlpecID())) {
				dao = CudPluginStlpecPeer.retrieveByPK(dto.getPluginStlpecID(), auth.T);
			} else {
				dao = new CudPluginStlpec();
			}

			dao.setIdPlugin(dto.getIDPlugin());
			dao.setIdCiselnikStlpec(dto.getIDCiselnikStlpec());
			dao.setIdPluginAlias(dto.getIDPluginAlias());
			dao.setHodnota(dto.getHodnota());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setPluginStlpecID(dao.getPluginStlpecId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOPluginStlpec[] pole, Integer pluginID, Date d) throws AppException {

		try {
			if (!StringUtils.isValid(pole)) {
				return null;
			}

			for (DTOPluginStlpec dto : pole) {
				dto.setIDPlugin(pluginID);
				dto.setPluginStlpecID((dto.getPluginStlpecID().intValue() < 0 ? null : dto.getPluginStlpecID()));
				ActionResult actionResult = updateSoft(auth, dto, d);
				String result = messageLookup(actionResult);
				if (StringUtils.isValid(result)) {
					return result;
				}
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public void deleteSoftByFk(AuthInfo auth, Integer pluginID, Date d) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudPluginStlpecPeer.ID_PLUGIN, pluginID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudPluginStlpecPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudPluginStlpecPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteSoftByFk.error", auth);
		}
	}

	public void deleteSoft(AuthInfo auth, Integer[] pluginStlpecIDs, Date d) throws AppException {

		try {
			if (!StringUtils.isValid(pluginStlpecIDs)) {
				return;
			}

			MyCriteria2 whereCrit = new MyCriteria2();
			if (pluginStlpecIDs.length == 1) {
				whereCrit.add(CudPluginStlpecPeer.PLUGIN_STLPEC_ID, pluginStlpecIDs[0]);
			} else {
				whereCrit.addIn(CudPluginStlpecPeer.PLUGIN_STLPEC_ID, pluginStlpecIDs);
			}

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudPluginStlpecPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudPluginStlpecPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteSoft.error", auth);
		}
	}

}
