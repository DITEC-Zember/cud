package sk.ditec.cud.bi;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOPreklad;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.dao.meta.CudPreklad;
import sk.ditec.dao.meta.CudPrekladPeer;

public class CudPrekladModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOPreklad dto, Date d) throws AppException {

		try {
			CudPreklad dao = null;

			if (StringUtils.isValid(dto.getPrekladID())) {
				dao = CudPrekladPeer.retrieveByPK(dto.getPrekladID(), auth.T);
			} else {
				dao = new CudPreklad();
			}

			dao.setIdPrekladJazyk(dto.getIDPrekladJazyk());
			dao.setIdPrekladStlpec(dto.getIDPrekladStlpec());
			dao.setZaznamId(dto.getZaznamID());
			dao.setPreklad(dto.getPreklad().getBytes(Charset.forName("UTF-8")));
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setPrekladID(dao.getPrekladId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOPreklad dto) throws AppException {

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

	public String deleteSoft(AuthInfo auth, Integer[] prekladStlpecIDs, Integer zaznamID, Date d) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudPrekladPeer.ZAZNAM_ID, zaznamID);
			whereCrit.add(CudPrekladPeer.ID_TRANSAKCIA_ZRUSENE, null);

			if (prekladStlpecIDs.length == 1) {
				whereCrit.add(CudPrekladPeer.ID_PREKLAD_STLPEC, prekladStlpecIDs[0]);
			} else {
				whereCrit.addIn(CudPrekladPeer.ID_PREKLAD_STLPEC, prekladStlpecIDs);
			}

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudPrekladPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());
			valuesCrit.add(CudPrekladPeer.CAS_ZMENY, d);
			valuesCrit.add(CudPrekladPeer.ID_UCET, auth.getAccountId());

			CudPrekladPeer.doUpdate(whereCrit, valuesCrit, auth.T);

			return null;

		} catch (Throwable t) {
			handleException(t, "deleteSoft.error", auth);
			return null;
		}
	}

	private String deleteSoft(AuthInfo auth, Integer prekladID, Date d) throws AppException {

		try {
			// delete soft
			CudPreklad dao = CudPrekladPeer.retrieveByPK(prekladID);
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

	public String delete(AuthInfo auth, Integer prekladID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			deleteSoft(auth, prekladID, d);

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
			CudPreklad dao = new CudPreklad();

			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudPrekladPeer.ID_PREKLAD_JAZYK));
			if (StringUtils.isValid(dtoZS)) {
				dao.setIdPrekladJazyk(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudPrekladPeer.ID_PREKLAD_STLPEC));
			if (StringUtils.isValid(dtoZS)) {
				dao.setIdPrekladStlpec(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudPrekladPeer.ZAZNAM_ID));
			if (StringUtils.isValid(dtoZS)) {
				dao.setZaznamId(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudPrekladPeer.PREKLAD));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPreklad(dtoZS.getNewValue().getBytes(Charset.forName("UTF-8")));
			}

			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setRowID(dao.getPrekladId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "insertSoft.error", auth);
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudPrekladPeer.PREKLAD_ID, dto.getRowID());

			MyCriteria2 valuesCrit = new MyCriteria2();

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudPrekladPeer.ID_PREKLAD_JAZYK));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudPrekladPeer.ID_PREKLAD_JAZYK, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudPrekladPeer.ID_PREKLAD_STLPEC));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudPrekladPeer.ID_PREKLAD_STLPEC, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudPrekladPeer.ZAZNAM_ID));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudPrekladPeer.ZAZNAM_ID, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudPrekladPeer.PREKLAD));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudPrekladPeer.PREKLAD, dtoZS.getNewValue());
			}

			valuesCrit.add(CudPrekladPeer.CAS_ZMENY, d);
			valuesCrit.add(CudPrekladPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudPrekladPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudPrekladPeer.doUpdate(whereCrit, valuesCrit, auth.T);

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

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

}
