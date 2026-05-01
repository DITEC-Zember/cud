package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.DateUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.dao.meta.CudCiselnikGui;
import sk.ditec.dao.meta.CudCiselnikGuiPeer;

public class CudCiselnikGuiModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOCiselnikGui dto, Date d) throws AppException {

		try {
			CudCiselnikGui dao = null;

			if (StringUtils.isValid(dto.getCiselnikGuiID())) {
				dao = CudCiselnikGuiPeer.retrieveByPK(dto.getCiselnikGuiID(), auth.T);
			} else {
				dao = new CudCiselnikGui();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setStav(dto.getStav());
			dao.setPlatnostOd(dto.getPlatnostOd());
			dao.setPlatnostDo(dto.getPlatnostDo());
			dao.setCasPublikovania(dto.getCasPublikovania());
			dao.setPopis(dto.getPopis());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setCiselnikGuiID(dao.getCiselnikGuiId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	private void updateSoft(AuthInfo auth, Integer ciselnikGuiID, Date platnostDo, Date d, String stav) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudCiselnikGuiPeer.CISELNIK_GUI_ID, ciselnikGuiID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			if (StringUtils.isValid(platnostDo)) {
				valuesCrit.add(CudCiselnikGuiPeer.PLATNOST_DO, platnostDo);
			}
			if (StringUtils.isValid(stav)) {
				valuesCrit.add(CudCiselnikGuiPeer.STAV, stav);
			}
			valuesCrit.add(CudCiselnikGuiPeer.CAS_ZMENY, d);
			valuesCrit.add(CudCiselnikGuiPeer.ID_UCET, auth.getAccountId());

			CudCiselnikGuiPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "updateSoft.error", auth);
		}
	}

	private Date previousDay(Date d) throws AppException {

		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) - 1);
			return cal.getTime();

		} catch (Throwable t) {
			DBUtils.handleException(t, "previousDay.error");
			return null;
		}
	}

	public String publishNew(AuthInfo auth, DTOCiselnikGui dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();
			Date platnostOd = DateUtils.removeTime(d);

			dto.setCasPublikovania(d);
			dto.setStav(_CudConsts.CISELNIK_GUI_STAV_PUB);
			dto.setPlatnostOd(platnostOd);

			DTOCiselnikGui dtoLast = getDelegate().getCiselnikGuiRead().readLast(auth, dto.getIDCiselnik(), _CudConsts.CISELNIK_GUI_STAV_PUB);

			getConnection(auth);

			if (StringUtils.isValid(dtoLast)) {
				updateSoft(auth, dtoLast.getCiselnikGuiID(), previousDay(platnostOd), d, null);
			}
			updateSoft(auth, dto, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "publishNew.error", auth);
			return null;
		}
	}

	public String publishActual(AuthInfo auth, DTOCiselnikGui dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();
			Date platnostOd = DateUtils.removeTime(d);

			dto.setCasPublikovania(d);
			dto.setStav(_CudConsts.CISELNIK_GUI_STAV_PUB);

			DTOCiselnikGui dtoLast = getDelegate().getCiselnikGuiRead().readLast(auth, dto.getIDCiselnik(), _CudConsts.CISELNIK_GUI_STAV_PUB);
			if (StringUtils.isValid(dtoLast)) {
				platnostOd = dtoLast.getPlatnostOd();
			}

			dto.setPlatnostOd(platnostOd);

			getConnection(auth);

			if (StringUtils.isValid(dtoLast)) {
				updateSoft(auth, dtoLast.getCiselnikGuiID(), null, d, _CudConsts.CISELNIK_GUI_STAV_ZMAZ);
			}
			updateSoft(auth, dto, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "publishActual.error", auth);
			return null;
		}
	}

	public String update(AuthInfo auth, DTOCiselnikGui dto) throws AppException {

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

	public String updateAndCopy(AuthInfo auth, DTOCiselnikGui dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			DTOCiselnikGui dtoLast = getDelegate().getCiselnikGuiRead().readLast(auth, dto.getIDCiselnik(), _CudConsts.CISELNIK_GUI_STAV_PUB);
			List<DTOCiselnikStlpecGui> list = getDelegate().getCiselnikStlpecGuiRead().listLight(auth, dtoLast.getCiselnikGuiID());

			Date d = new Date();

			getConnection(auth);

			updateSoft(auth, dto, d);
			getDelegate().getCiselnikStlpecGuiModify().update(auth, list, dto.getCiselnikGuiID(), d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "updateAndCopy.error", auth);
			return null;
		}
	}

	public String delete(AuthInfo auth, Integer ciselnikGuiID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			getDelegate().getCiselnikStlpecGuiModify().deleteByFk(auth, ciselnikGuiID, d);

			ActionResult actionResult = deleteSoft(auth, ciselnikGuiID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return messageLookup(actionResult);

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

	private ActionResult deleteSoft(AuthInfo auth, Integer ciselnikGuiID, Date d) throws AppException {

		try {
			// delete soft
			CudCiselnikGui dao = CudCiselnikGuiPeer.retrieveByPK(ciselnikGuiID, auth.T);
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZrusene(auth.getTransakciaID());

			dao.save(auth.T);

			return new ActionResult(ciselnikGuiID);

		} catch (Throwable t) {
			return handleException(t, "deleteSoft.error", auth);
		}
	}

	private ActionResult insertSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			CudCiselnikGui dao = new CudCiselnikGui();

			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikGuiPeer.ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				dao.setIdCiselnik(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikGuiPeer.STAV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setStav(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikGuiPeer.POPIS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPopis(dtoZS.getNewValue());
			}

			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setRowID(dao.getCiselnikGuiId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "insertSoft.error", auth);
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudCiselnikGuiPeer.CISELNIK_GUI_ID, dto.getRowID());

			MyCriteria2 valuesCrit = new MyCriteria2();

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikGuiPeer.ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikGuiPeer.ID_CISELNIK, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikGuiPeer.STAV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikGuiPeer.STAV, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikGuiPeer.POPIS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikGuiPeer.POPIS, dtoZS.getNewValue());
			}

			valuesCrit.add(CudCiselnikGuiPeer.CAS_ZMENY, d);
			valuesCrit.add(CudCiselnikGuiPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudCiselnikGuiPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudCiselnikGuiPeer.doUpdate(whereCrit, valuesCrit, auth.T);

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
				getDelegate().getCiselnikStlpecGuiModify().deleteByFk(auth, dto.getRowID(), d);
				deleteSoft(auth, dto.getRowID(), d);
			}

			getDelegate().getImportZmenaModify().updateSpracovany(auth, dto.getIDImport(), dto.getImportZmenaID());

			returnConnection(auth);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

}
