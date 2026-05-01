package sk.ditec.cud.bi;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOWfDefCiselnikStlpec;
import sk.ditec.dao.meta.CudPluginStlpecPeer;
import sk.ditec.dao.meta.CudWfDefCiselnikStlpec;
import sk.ditec.dao.meta.CudWfDefCiselnikStlpecPeer;

public class CudWfDefCiselnikStlpecModifyClass extends _CudBaseClass {

	public void deleteSoft(AuthInfo auth, Integer[] wfDefCiselnikStlpecIDs) throws AppException {

		try {
			if (!StringUtils.isValid(wfDefCiselnikStlpecIDs)) {
				return;
			}

			MyCriteria2 whereCrit = new MyCriteria2();
			if (wfDefCiselnikStlpecIDs.length == 1) {
				whereCrit.add(CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID, wfDefCiselnikStlpecIDs[0]);
			} else {
				whereCrit.addIn(CudWfDefCiselnikStlpecPeer.WF_DEF_CISELNIK_STLPEC_ID, wfDefCiselnikStlpecIDs);
			}

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudWfDefCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudWfDefCiselnikStlpecPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteSoft.error", auth);
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOWfDefCiselnikStlpec dto) throws AppException {

		try {
			CudWfDefCiselnikStlpec dao = null;

			if (StringUtils.isValid(dto.getWfDefCiselnikStlpecID())) {
				dao = CudWfDefCiselnikStlpecPeer.retrieveByPK(dto.getWfDefCiselnikStlpecID(), auth.T);
			} else {
				dao = new CudWfDefCiselnikStlpec();
			}

			dao.setIdWfDef(dto.getIDWfDef());
			dao.setIdCiselnikStlpec(dto.getIDCiselnikStlpec());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setWfDefCiselnikStlpecID(dao.getWfDefCiselnikStlpecId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOWfDefCiselnikStlpec[] pole, Integer wfDefID) throws AppException {

		try {
			if (!StringUtils.isValid(pole)) {
				return null;
			}

			for (DTOWfDefCiselnikStlpec dto : pole) {
				if (dto.getWfDefCiselnikStlpecID().intValue() < 0) {
					dto.setWfDefCiselnikStlpecID(null);
					dto.setIDWfDef(wfDefID);
					ActionResult actionResult = updateSoft(auth, dto);
					String result = messageLookup(actionResult);
					if (StringUtils.isValid(result)) {
						return result;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public void deleteSoftByFk(AuthInfo auth, Integer wfDefID) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudWfDefCiselnikStlpecPeer.ID_WF_DEF, wfDefID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudWfDefCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudPluginStlpecPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteSoftByFk.error", auth);
		}
	}

}
