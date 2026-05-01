package sk.ditec.cud.bi;

import java.util.Date;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.dao.meta.CudObjektStlpec;
import sk.ditec.dao.meta.CudObjektStlpecPeer;

public class CudObjektStlpecModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOObjektStlpec dto, Date d) throws AppException {

		try {
			CudObjektStlpec dao = null;

			if (StringUtils.isValid(dto.getObjektStlpecID())) {
				dao = CudObjektStlpecPeer.retrieveByPK(dto.getObjektStlpecID(), auth.T);
			} else {
				dao = new CudObjektStlpec();
			}

			dao.setIdObjektCiselnik(dto.getIDObjektCiselnik());
			dao.setIdCiselnikStlpec(dto.getIDCiselnikStlpec());
			dao.setZmena(dto.getZmena());
			dao.setHodnota(dto.getHodnota());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setObjektStlpecID(dao.getObjektStlpecId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public void update(AuthInfo auth, DTOObjektStlpec[] pole, Date d, Integer objektCiselnikID) throws AppException {

		try {
			if (!StringUtils.isValid(pole)) {
				return;
			}
			for (DTOObjektStlpec dto : pole) {
				if ("T".equals(dto.getBolZmeneny())) {
					dto.setObjektStlpecID(dto.getObjektStlpecID().intValue() < 0 ? null : dto.getObjektStlpecID());
					dto.setIDObjektCiselnik(objektCiselnikID);
					updateSoft(auth, dto, d);
				}
			}

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
		}
	}

	public void deleteSoftByIDs(AuthInfo auth, Integer[] objektStlpecIDs, Date d) throws AppException {

		try {
			if (!StringUtils.isValid(objektStlpecIDs)) {
				return;
			}

			MyCriteria2 whereCrit = new MyCriteria2();
			if (objektStlpecIDs.length == 1) {
				whereCrit.add(CudObjektStlpecPeer.OBJEKT_STLPEC_ID, objektStlpecIDs[0]);
			} else {
				whereCrit.addIn(CudObjektStlpecPeer.OBJEKT_STLPEC_ID, objektStlpecIDs);
			}
			whereCrit.add(CudObjektStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudObjektStlpecPeer.CAS_ZMENY, d);
			valuesCrit.add(CudObjektStlpecPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudObjektStlpecPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudObjektStlpecPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteSoftByIDs.error", auth);
		}
	}

	public void deleteSoftByObjektCiselnik(AuthInfo auth, Integer[] objektCiselnikIDs, Date d) throws AppException {

		try {
			if (!StringUtils.isValid(objektCiselnikIDs)) {
				return;
			}

			MyCriteria2 whereCrit = new MyCriteria2();
			if (objektCiselnikIDs.length == 1) {
				whereCrit.addConditional(CudObjektStlpecPeer.ID_OBJEKT_CISELNIK, objektCiselnikIDs[0]);
			} else {
				whereCrit.addIn(CudObjektStlpecPeer.ID_OBJEKT_CISELNIK, objektCiselnikIDs);
			}
			whereCrit.add(CudObjektStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudObjektStlpecPeer.CAS_ZMENY, d);
			valuesCrit.add(CudObjektStlpecPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudObjektStlpecPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudObjektStlpecPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteSoftByObjektCiselnik.error", auth);
		}
	}

}
