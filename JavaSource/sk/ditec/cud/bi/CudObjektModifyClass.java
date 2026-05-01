package sk.ditec.cud.bi;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOObjekt;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.dao.meta.CudObjekt;
import sk.ditec.dao.meta.CudObjektPeer;

public class CudObjektModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOObjekt dto, Date d) throws AppException {

		try {
			CudObjekt dao = null;

			if (StringUtils.isValid(dto.getObjektID())) {
				dao = CudObjektPeer.retrieveByPK(dto.getObjektID(), auth.T);
			} else {
				dao = new CudObjekt();
			}

			dao.setObjektId(dto.getObjektID());
			dao.setNazov(dto.getNazov());
			dao.setPlatny(dto.getPlatny());
			dao.setSystemovy(dto.getSystemovy());
			dao.setSystemovyKanal(dto.getSystemovyKanal());
			dao.setSystemovyVsetkyCiselniky(dto.getSystemovyVsetkyCiselniky());
			dao.setSystemovyExportFormat(dto.getSystemovyExportFormat());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setObjektID(dao.getObjektId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOObjekt dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Set<Integer> idsNew = new HashSet<Integer>();
			if (StringUtils.isValid(dto.getObjektCiselnikList())) {
				for (DTOObjektCiselnik dtoItem : dto.getObjektCiselnikList()) {
					if (StringUtils.isValid(dtoItem.getObjektCiselnikID())) {
						idsNew.add(dtoItem.getObjektCiselnikID());
					}
				}
			}

			Set<Integer> objektCiselnikIDsForDelete = new HashSet<Integer>();
			Set<Integer> idsOld = getDelegate().getObjektCiselnikRead().ids(auth, dto.getObjektID());
			for (Integer objektCiselnikID : idsOld) {
				if (!idsNew.contains(objektCiselnikID)) {
					objektCiselnikIDsForDelete.add(objektCiselnikID);
				}
			}

			Map<Integer, Set<Integer>> objektStlpecIDsForDelete = new HashMap<Integer, Set<Integer>>();
			if (StringUtils.isValid(dto.getObjektCiselnikList())) {
				for (DTOObjektCiselnik dtoOC : dto.getObjektCiselnikList()) {
					if ("T".equals(dtoOC.getBolZmenenyObjektStlpecList())) {
						idsNew.clear();
						if (StringUtils.isValid(dtoOC.getObjektStlpecList())) {
							for (DTOObjektStlpec dtoOS : dtoOC.getObjektStlpecList()) {
								if (StringUtils.isValid(dtoOS.getObjektStlpecID())) {
									idsNew.add(dtoOS.getObjektStlpecID());
								}
							}
						}
						objektStlpecIDsForDelete.put(dtoOC.getIDCiselnik(), new HashSet<Integer>());
						idsOld = getDelegate().getObjektStlpecRead().ids(auth, dtoOC.getObjektCiselnikID());
						for (Integer objektStlpecID : idsOld) {
							if (!idsNew.contains(objektStlpecID)) {
								objektStlpecIDsForDelete.get(dtoOC.getIDCiselnik()).add(objektStlpecID);
							}
						}
					}
				}
			}

			Date d = new Date();

			getConnection(auth);

			updateSoft(auth, dto, d);
			getDelegate().getObjektStlpecModify().deleteSoftByObjektCiselnik(auth, objektCiselnikIDsForDelete.toArray(new Integer[objektCiselnikIDsForDelete.size()]), d);
			getDelegate().getObjektCiselnikModify().deleteSoft(auth, objektCiselnikIDsForDelete.toArray(new Integer[objektCiselnikIDsForDelete.size()]), d);
			getDelegate().getObjektCiselnikModify().update(auth, dto.getObjektCiselnikList(), d, dto.getObjektID());
			if (StringUtils.isValid(dto.getObjektCiselnikList())) {
				for (DTOObjektCiselnik dtoOC : dto.getObjektCiselnikList()) {
					Set<Integer> set = objektStlpecIDsForDelete.get(dtoOC.getIDCiselnik());
					Integer[] poleID = StringUtils.isValid(set) ? set.toArray(new Integer[set.size()]) : null;
					getDelegate().getObjektStlpecModify().deleteSoftByIDs(auth, poleID, d);
					getDelegate().getObjektStlpecModify().update(auth, dtoOC.getObjektStlpecList(), d, dtoOC.getObjektCiselnikID());
				}
			}

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public String deleteSoft(AuthInfo auth, Integer objektID, Date d) throws AppException {

		try {
			// delete soft
			CudObjekt dao = CudObjektPeer.retrieveByPK(objektID);
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

	public String delete(AuthInfo auth, Integer objektID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Set<Integer> ids = getDelegate().getObjektCiselnikRead().ids(auth, objektID);

			Date d = new Date();

			getConnection(auth);

			deleteSoft(auth, objektID, d);
			getDelegate().getObjektCiselnikModify().deleteSoftByObjekt(auth, objektID, d);
			getDelegate().getObjektStlpecModify().deleteSoftByObjektCiselnik(auth, ids.toArray(new Integer[ids.size()]), d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

}
