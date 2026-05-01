package sk.ditec.cud.bi;

import java.util.Date;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.dao.meta.CudObjektCiselnik;
import sk.ditec.dao.meta.CudObjektCiselnikPeer;

public class CudObjektCiselnikModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOObjektCiselnik dto, Date d) throws AppException {

		try {
			CudObjektCiselnik dao = null;

			if (StringUtils.isValid(dto.getObjektCiselnikID())) {
				dao = CudObjektCiselnikPeer.retrieveByPK(dto.getObjektCiselnikID(), auth.T);
			} else {
				dao = new CudObjektCiselnik();
			}

			dao.setIdObjekt(dto.getIDObjekt());
			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setVsetky(dto.getVsetky());
			dao.setPlatny(dto.getPlatny());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setObjektCiselnikID(dao.getObjektCiselnikId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public void update(AuthInfo auth, DTOObjektCiselnik[] pole, Date d, Integer objektID) throws AppException {

		try {
			if (!StringUtils.isValid(pole)) {
				return;
			}
			for (DTOObjektCiselnik dto : pole) {
				if ("T".equals(dto.getBolZmeneny())) {
					dto.setIDObjekt(objektID);
					updateSoft(auth, dto, d);
				}
			}

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
		}
	}

	public void deleteSoftByObjekt(AuthInfo auth, Integer objektID, Date d) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudObjektCiselnikPeer.ID_OBJEKT, objektID);
			whereCrit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudObjektCiselnikPeer.CAS_ZMENY, d);
			valuesCrit.add(CudObjektCiselnikPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudObjektCiselnikPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteSoftByObjekt.error", auth);
		}
	}

	public void deleteSoft(AuthInfo auth, Integer[] objektCiselnikIDs, Date d) throws AppException {

		try {
			if (!StringUtils.isValid(objektCiselnikIDs)) {
				return;
			}

			MyCriteria2 whereCrit = new MyCriteria2();
			if (objektCiselnikIDs.length == 1) {
				whereCrit.add(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, objektCiselnikIDs[0]);
			} else {
				whereCrit.addIn(CudObjektCiselnikPeer.OBJEKT_CISELNIK_ID, objektCiselnikIDs);
			}
			whereCrit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudObjektCiselnikPeer.CAS_ZMENY, d);
			valuesCrit.add(CudObjektCiselnikPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudObjektCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudObjektCiselnikPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteSoft.error", auth);
		}
	}

	public String deleteSoft(AuthInfo auth, Integer objektCiselnikID, Date d) throws AppException {

		try {
			// delete soft
			CudObjektCiselnik dao = CudObjektCiselnikPeer.retrieveByPK(objektCiselnikID);
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

	public String delete(AuthInfo auth, Integer objektCiselnikID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			deleteSoft(auth, objektCiselnikID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

}
