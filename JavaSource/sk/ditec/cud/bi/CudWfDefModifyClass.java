package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfDefCiselnikStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.dao.meta.CudWfDef;
import sk.ditec.dao.meta.CudWfDefPeer;

public class CudWfDefModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOWfDef dto, Date d) throws AppException {

		try {
			CudWfDef dao = null;

			if (StringUtils.isValid(dto.getWfDefID())) {
				dao = CudWfDefPeer.retrieveByPK(dto.getWfDefID(), auth.T);
			} else {
				dao = new CudWfDef();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setIdWfDefNasl(dto.getIDWfDefNasl());
			dao.setNazov(dto.getNazov());
			dao.setTyp(dto.getTyp());
			dao.setZodpovednost(dto.getZodpovednost());
			dao.setEmailList(dto.getEmailList());
			dao.setEmailText(dto.getEmailText());
			dao.setEmailSubject(dto.getEmailSubject());
			dao.setEmailSend(dto.getEmailSend());
			dao.setHodiny(dto.getHodiny());
			dao.setIdSkupina(dto.getIDSkupina());
			dao.setSkupinaNazov(dto.getSkupinaNazov());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setWfDefID(dao.getWfDefId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOWfDef dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Set<Integer> idsNew = new HashSet<Integer>();
			if (StringUtils.isValid(dto.getWfDefCiselnikStlpecList())) {
				for (DTOWfDefCiselnikStlpec dtoItem : dto.getWfDefCiselnikStlpecList()) {
					if (StringUtils.isValid(dtoItem.getWfDefCiselnikStlpecID())) {
						idsNew.add(dtoItem.getWfDefCiselnikStlpecID());
					}
				}
			}

			Set<Integer> idsDelete = new HashSet<Integer>();
			for (Integer wfDefCiselnikStlpecID : getDelegate().getWfDefCiselnikStlpecRead().ids(auth, dto.getWfDefID())) {
				if (!idsNew.contains(wfDefCiselnikStlpecID)) {
					idsDelete.add(wfDefCiselnikStlpecID);
				}
			}

			Date d = new Date();

			getConnection(auth);

			updateSoft(auth, dto, d);
			getDelegate().getWfDefCiselnikStlpecModify().deleteSoft(auth, idsDelete.toArray(new Integer[idsDelete.size()]));
			getDelegate().getWfDefCiselnikStlpecModify().update(auth, dto.getWfDefCiselnikStlpecList(), dto.getWfDefID());

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	private String deleteSoft(AuthInfo auth, Integer wfDefID, Date d) throws AppException {

		try {
			// delete soft
			CudWfDef dao = CudWfDefPeer.retrieveByPK(wfDefID);
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

	public String delete(AuthInfo auth, Integer wfDefID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			deleteSoft(auth, wfDefID, d);
			getDelegate().getWfDefCiselnikStlpecModify().deleteSoftByFk(auth, wfDefID);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

	private ActionResult insertSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			CudWfDef dao = new CudWfDef();

			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				dao.setIdCiselnik(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.ID_WF_DEF_NASL));
			if (StringUtils.isValid(dtoZS)) {
				dao.setIdWfDefNasl(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setNazov(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.TYP));
			if (StringUtils.isValid(dtoZS)) {
				dao.setTyp(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.ZODPOVEDNOST));
			if (StringUtils.isValid(dtoZS)) {
				dao.setZodpovednost(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.EMAIL_LIST));
			if (StringUtils.isValid(dtoZS)) {
				dao.setEmailList(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.EMAIL_TEXT));
			if (StringUtils.isValid(dtoZS)) {
				dao.setEmailText(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.EMAIL_SUBJECT));
			if (StringUtils.isValid(dtoZS)) {
				dao.setEmailSubject(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.EMAIL_SEND));
			if (StringUtils.isValid(dtoZS)) {
				dao.setEmailSend(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.HODINY));
			if (StringUtils.isValid(dtoZS)) {
				dao.setHodiny(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.ID_SKUPINA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setIdSkupina(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.SKUPINA_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setSkupinaNazov(dtoZS.getNewValue());
			}

			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setRowID(dao.getWfDefId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "insertSoft.error", auth);
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudWfDefPeer.WF_DEF_ID, dto.getRowID());

			MyCriteria2 valuesCrit = new MyCriteria2();

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.ID_CISELNIK, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.ID_WF_DEF_NASL));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.ID_WF_DEF_NASL, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.NAZOV, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.TYP));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.TYP, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.ZODPOVEDNOST));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.ZODPOVEDNOST, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.EMAIL_LIST));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.EMAIL_LIST, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.EMAIL_TEXT));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.EMAIL_TEXT, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.EMAIL_SUBJECT));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.EMAIL_SUBJECT, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.EMAIL_SEND));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.EMAIL_SEND, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.HODINY));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.HODINY, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.ID_SKUPINA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.ID_SKUPINA, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudWfDefPeer.SKUPINA_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudWfDefPeer.SKUPINA_NAZOV, dtoZS.getNewValue());
			}

			valuesCrit.add(CudWfDefPeer.CAS_ZMENY, d);
			valuesCrit.add(CudWfDefPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudWfDefPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudWfDefPeer.doUpdate(whereCrit, valuesCrit, auth.T);

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			getConnection(auth);

			if (_CudConsts.ZMENA_OPERACIA_N.equals(dto.getOperacia()) || _CudConsts.ZMENA_OPERACIA_U.equals(dto.getOperacia())) {

				if (StringUtils.isValid(dto.getRowID())) {
					updateSoft(auth, dto, d);

				} else {
					insertSoft(auth, dto, d);
				}
			}

			if (_CudConsts.ZMENA_OPERACIA_Z.equals(dto.getOperacia())) {
				deleteSoft(auth, dto.getRowID(), d);
			}

			getDelegate().getImportZmenaModify().updateSpracovany(auth, dto.getIDImport(), dto.getImportZmenaID());

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

}
