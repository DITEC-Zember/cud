package sk.ditec.cud.bi;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOPluginStlpec;
import sk.ditec.dao.meta.CudPlugin;
import sk.ditec.dao.meta.CudPluginPeer;

public class CudPluginModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOPlugin dto, Date d) throws AppException {

		try {
			CudPlugin dao = null;

			if (StringUtils.isValid(dto.getPluginID())) {
				dao = CudPluginPeer.retrieveByPK(dto.getPluginID(), auth.T);
			} else {
				dao = new CudPlugin();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setIdPluginClassName(dto.getIDPluginClassName());
			dao.setTyp(dto.getTyp());
			dao.setPlatnostOd(dto.getPlatnostOd());
			dao.setPlatnostDo(dto.getPlatnostDo());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setPluginID(dao.getPluginId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOPlugin dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Set<Integer> idsNew = new HashSet<Integer>();
			if (StringUtils.isValid(dto.getPluginStlpecList())) {
				for (DTOPluginStlpec dtoItem : dto.getPluginStlpecList()) {
					if (StringUtils.isValid(dtoItem.getPluginStlpecID())) {
						idsNew.add(dtoItem.getPluginStlpecID());
					}
				}
			}

			Set<Integer> idsDelete = new HashSet<Integer>();
			for (Integer pluginStlpecID : getDelegate().getPluginStlpecRead().ids(auth, dto.getPluginID())) {
				if (!idsNew.contains(pluginStlpecID)) {
					idsDelete.add(pluginStlpecID);
				}
			}

			Date d = new Date();

			getConnection(auth);

			updateSoft(auth, dto, d);
			getDelegate().getPluginStlpecModify().deleteSoft(auth, idsDelete.toArray(new Integer[idsDelete.size()]), d);
			getDelegate().getPluginStlpecModify().update(auth, dto.getPluginStlpecList(), dto.getPluginID(), d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	private String deleteSoft(AuthInfo auth, Integer pluginID, Date d) throws AppException {

		try {
			// delete soft
			CudPlugin dao = CudPluginPeer.retrieveByPK(pluginID);
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

	public String delete(AuthInfo auth, Integer pluginID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			getDelegate().getPluginStlpecModify().deleteSoftByFk(auth, pluginID, d);
			deleteSoft(auth, pluginID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

}
