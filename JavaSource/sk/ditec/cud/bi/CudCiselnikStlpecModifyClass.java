package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.dao.meta.CudCiselnikStlpec;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;

public class CudCiselnikStlpecModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOCiselnikStlpec dto, Date d) throws AppException {

		try {
			CudCiselnikStlpec dao = null;

			if (StringUtils.isValid(dto.getCiselnikStlpecID())) {
				dao = CudCiselnikStlpecPeer.retrieveByPK(dto.getCiselnikStlpecID(), auth.T);
			} else {
				dao = new CudCiselnikStlpec();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setNazov(dto.getNazov());
			dao.setNadpis(dto.getNadpis());
			dao.setTyp(dto.getTyp());
			dao.setPoradie(dto.getPoradie());
			dao.setDlzka(dto.getDlzka());
			dao.setDecimals(dto.getDecimals());
			dao.setDbTyp(dto.getDbTyp());
			dao.setPovinny(dto.getPovinny());
			dao.setJedinecny(dto.getJedinecny());
			dao.setAktivny(dto.getAktivny());
			dao.setJeDbString(dto.getJeDbString());
			dao.setFk1IdCiselnik(dto.getFk1IDCiselnik());
			dao.setFk1PkNazov(dto.getFk1PkNazov());
			dao.setFk1FkNazov(dto.getFk1FkNazov());
			dao.setPopis(dto.getPopis());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setCiselnikStlpecID(dao.getCiselnikStlpecId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, List<DTOCiselnikStlpec> list, Integer ciselnikID, Date d) throws AppException {

		try {
			if (!StringUtils.isValid(list)) {
				return null;
			}

			for (DTOCiselnikStlpec dto : list) {
				dto.setIDCiselnik(ciselnikID);
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

	public String update(AuthInfo auth, DTOCiselnikStlpec dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			ActionResult actionResult = updateSoft(auth, dto, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return messageLookup(actionResult);

		} catch (Exception t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public String delete(AuthInfo auth, Integer ciselnikStlpecID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			Integer[] prekladStlpecIDs = getDelegate().getPrekladStlpecRead().ids(auth, CudCiselnikStlpecPeer.TABLE_NAME);

			getConnection(auth);

			getDelegate().getPrekladModify().deleteSoft(auth, prekladStlpecIDs, ciselnikStlpecID, d);
			ActionResult actionResult = deleteSoft(auth, ciselnikStlpecID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return messageLookup(actionResult);

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

	private ActionResult deleteSoft(AuthInfo auth, Integer ciselnikStlpecID, Date d) throws AppException {

		try {
			// delete soft
			CudCiselnikStlpec dao = CudCiselnikStlpecPeer.retrieveByPK(ciselnikStlpecID, auth.T);
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZrusene(auth.getTransakciaID());

			dao.save(auth.T);

			return new ActionResult(ciselnikStlpecID);

		} catch (Throwable t) {
			return handleException(t, "deleteSoft.error", auth);
		}
	}

	public void deleteSoftByFk(AuthInfo auth, Integer ciselnikID, Date d) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudCiselnikStlpecPeer.ID_CISELNIK, ciselnikID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());
			valuesCrit.add(CudCiselnikStlpecPeer.CAS_ZMENY, d);
			valuesCrit.add(CudCiselnikStlpecPeer.ID_UCET, auth.getAccountId());

			CudCiselnikStlpecPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteSoftByFk.error", auth);
		}
	}

	private ActionResult insertSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			CudCiselnikStlpec dao = new CudCiselnikStlpec();

			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				dao.setIdCiselnik(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setNazov(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.NADPIS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setNadpis(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.TYP));
			if (StringUtils.isValid(dtoZS)) {
				dao.setTyp(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.PORADIE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPoradie(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.DLZKA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setDlzka(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.DECIMALS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setDecimals(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.DB_TYP));
			if (StringUtils.isValid(dtoZS)) {
				dao.setDbTyp(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.POVINNY));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPovinny(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.JEDINECNY));
			if (StringUtils.isValid(dtoZS)) {
				dao.setJedinecny(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.AKTIVNY));
			if (StringUtils.isValid(dtoZS)) {
				dao.setAktivny(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.FK1_ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				dao.setFk1IdCiselnik(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.FK1_PK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setFk1PkNazov(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.FK1_FK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setFk1FkNazov(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.POPIS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPopis(dtoZS.getNewValue());
			}

			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setRowID(dao.getCiselnikStlpecId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "insertSoft.error", auth);
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, dto.getRowID());

			MyCriteria2 valuesCrit = new MyCriteria2();

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.ID_CISELNIK, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.NAZOV, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.NADPIS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.NADPIS, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.TYP));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.TYP, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.PORADIE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.PORADIE, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.DLZKA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.DLZKA, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.DECIMALS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.DECIMALS, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.DB_TYP));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.DB_TYP, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.POVINNY));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.POVINNY, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.JEDINECNY));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.JEDINECNY, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.AKTIVNY));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.AKTIVNY, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.FK1_ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.FK1_ID_CISELNIK, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.FK1_PK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.FK1_PK_NAZOV, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.FK1_FK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.FK1_FK_NAZOV, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecPeer.POPIS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecPeer.POPIS, dtoZS.getNewValue());
			}

			valuesCrit.add(CudCiselnikStlpecPeer.CAS_ZMENY, d);
			valuesCrit.add(CudCiselnikStlpecPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudCiselnikStlpecPeer.doUpdate(whereCrit, valuesCrit, auth.T);

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
