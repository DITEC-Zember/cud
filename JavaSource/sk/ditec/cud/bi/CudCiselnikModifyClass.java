package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.dao.meta.CudCiselnik;
import sk.ditec.dao.meta.CudCiselnikPeer;

public class CudCiselnikModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOCiselnik dto, Date d) throws AppException {

		try {
			CudCiselnik dao = null;

			if (StringUtils.isValid(dto.getCiselnikID())) {
				dao = CudCiselnikPeer.retrieveByPK(dto.getCiselnikID(), auth.T);
			} else {
				dao = new CudCiselnik();
			}

			dao.setTabulka(dto.getTabulka());
			dao.setNazov(dto.getNazov());
			dao.setPopis(dto.getPopis());
			dao.setPrintClass(dto.getPrintClass());
			dao.setPrintZahlavie(dto.getPrintZahlavie());
			dao.setAktivny(dto.getAktivny());
			dao.setPredpis(dto.getPredpis());
			dao.setPrilohaKapitola(dto.getPrilohaKapitola());
			dao.setHlavny(dto.getHlavny());
			dao.setTyp(dto.getTyp());
			dao.setKategoria(dto.getKategoria());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setCiselnikID(dao.getCiselnikId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	private List<DTOCiselnikStlpec> generujTechnickeAtributy(AuthInfo auth, String tabulka) throws AppException {

		try {
			DTOCiselnikStlpec dtoCSPK = getDelegate().getGuiRead().userTabColsReadPK(auth, tabulka);

			List<DTOCiselnikStlpec> listDTO = new ArrayList<DTOCiselnikStlpec>();

			listDTO.add(getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(_CudConsts.NAZOV_HIST_ID));
			listDTO.add(getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(_CudConsts.NAZOV_PLATNOST_OD));
			listDTO.add(getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(_CudConsts.NAZOV_PLATNOST_DO));
			listDTO.add(getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(_CudConsts.NAZOV_CAS_VYTVORENIA));
			listDTO.add(getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(_CudConsts.NAZOV_CAS_ZMENY));
			listDTO.add(getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(_CudConsts.NAZOV_ID_ZMENA));
			listDTO.add(getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(_CudConsts.NAZOV_ZMAZ));

			if (StringUtils.isValid(dtoCSPK)) {
				DTOCiselnikStlpec dtoNew = getDelegate().getCiselnikStlpecRead().createTechnickyAtribut(_CudConsts.NAZOV_PK_KEY);
				dtoNew.setNazov(dtoCSPK.getNazov());
				dtoNew.setDlzka(dtoCSPK.getDlzka());
				listDTO.add(dtoNew);
			}

			return listDTO;

		} catch (Throwable t) {
			DBUtils.handleException(t, "generujTechnickeAtributy.error");
			return null;
		}
	}

	public String update(AuthInfo auth, DTOCiselnik dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			List<DTOCiselnikStlpec> csList = null;
			if (!StringUtils.isValid(dto.getCiselnikID()) && _CudConsts.CISELNIK_TYP_TECHNICKY.equals(dto.getTyp())) {
				csList = generujTechnickeAtributy(auth, dto.getTabulka());
			}

			Date d = new Date();

			getConnection(auth);

			updateSoft(auth, dto, d);
			getDelegate().getCiselnikStlpecModify().update(auth, csList, dto.getCiselnikID(), d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public String deleteSoft(AuthInfo auth, Integer ciselnikID, Date d) throws AppException {

		try {
			// delete soft
			CudCiselnik dao = CudCiselnikPeer.retrieveByPK(ciselnikID);
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

	public String delete(AuthInfo auth, Integer ciselnikID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			Integer[] prekladStlpecIDs = getDelegate().getPrekladStlpecRead().ids(auth, CudCiselnikPeer.TABLE_NAME);

			getConnection(auth);

			getDelegate().getPrekladModify().deleteSoft(auth, prekladStlpecIDs, ciselnikID, d);
			getDelegate().getCiselnikStlpecModify().deleteSoftByFk(auth, ciselnikID, d);
			deleteSoft(auth, ciselnikID, d);

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
			CudCiselnik dao = new CudCiselnik();

			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.TABULKA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setTabulka(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setNazov(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.POPIS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPopis(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.PRINT_CLASS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPrintClass(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.PRINT_ZAHLAVIE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPrintZahlavie(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.AKTIVNY));
			if (StringUtils.isValid(dtoZS)) {
				dao.setAktivny(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.PREDPIS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPredpis(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.PRILOHA_KAPITOLA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPrilohaKapitola(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.HLAVNY));
			if (StringUtils.isValid(dtoZS)) {
				dao.setHlavny(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.TYP));
			if (StringUtils.isValid(dtoZS)) {
				dao.setTyp(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.KATEGORIA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setKategoria(dtoZS.getNewValue());
			}

			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setRowID(dao.getCiselnikId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "insertSoft.error", auth);
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudCiselnikPeer.CISELNIK_ID, dto.getRowID());

			MyCriteria2 valuesCrit = new MyCriteria2();

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.TABULKA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.TABULKA, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.NAZOV, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.POPIS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.POPIS, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.PRINT_CLASS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.PRINT_CLASS, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.PRINT_ZAHLAVIE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.PRINT_ZAHLAVIE, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.AKTIVNY));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.AKTIVNY, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.PREDPIS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.PREDPIS, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.PRILOHA_KAPITOLA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.PRILOHA_KAPITOLA, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.HLAVNY));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.HLAVNY, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.TYP));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.TYP, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.KATEGORIA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikPeer.KATEGORIA, dtoZS.getNewValue());
			}

			valuesCrit.add(CudCiselnikPeer.CAS_ZMENY, d);
			valuesCrit.add(CudCiselnikPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudCiselnikPeer.doUpdate(whereCrit, valuesCrit, auth.T);

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
					List<DTOCiselnikStlpec> csList = null;
					List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));
					DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikPeer.TABULKA));
					if (StringUtils.isValid(dtoZS)) {
						csList = generujTechnickeAtributy(auth, dtoZS.getNewValue());
					}

					insertSoft(auth, dto, d);
					getDelegate().getCiselnikStlpecModify().update(auth, csList, dto.getRowID(), d);
				}
			}

			if (_CudConsts.ZMENA_OPERACIA_Z.equals(dto.getOperacia())) {
				getDelegate().getCiselnikStlpecModify().deleteSoftByFk(auth, dto.getRowID(), d);
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
