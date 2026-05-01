package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.dao.meta.CudCiselnikStlpecGui;
import sk.ditec.dao.meta.CudCiselnikStlpecGuiPeer;

public class CudCiselnikStlpecGuiModifyClass extends _CudBaseClass {

	private ActionResult updateSoft(AuthInfo auth, DTOCiselnikStlpecGui dto, Date d) throws AppException {

		try {
			CudCiselnikStlpecGui dao = null;

			if (StringUtils.isValid(dto.getCiselnikStlpecGuiID())) {
				dao = CudCiselnikStlpecGuiPeer.retrieveByPK(dto.getCiselnikStlpecGuiID(), auth.T);
			} else {
				dao = new CudCiselnikStlpecGui();
			}

			dao.setIdCiselnikGui(dto.getIDCiselnikGui());
			dao.setIdCiselnikStlpec(dto.getIDCiselnikStlpec());
			dao.setNadpis(dto.getNadpis());
			dao.setPoradie(dto.getPoradie());
			dao.setDlzka(dto.getDlzka());
			dao.setDecimals(dto.getDecimals());
			dao.setZmena(dto.getZmena());
			dao.setPovinny(dto.getPovinny());
			dao.setZarovnanie(dto.getZarovnanie());
			dao.setFk1FkNazov(dto.getFk1FkNazov());
			dao.setFk2IdCiselnik(dto.getFk2IDCiselnik());
			dao.setFk2PkNazov(dto.getFk2PkNazov());
			dao.setFk2FkNazov(dto.getFk2FkNazov());
			dao.setListZobrazenie(dto.getListZobrazenie());
			dao.setListSirka(dto.getListSirka());
			dao.setListSirkaChange(dto.getListSirkaChange());
			dao.setFormZobrazenie(dto.getFormZobrazenie());
			dao.setFormSirka(dto.getFormSirka());
			dao.setPopupZobrazenie(dto.getPopupZobrazenie());
			dao.setPopupSirka(dto.getPopupSirka());
			dao.setPopupSirkaChange(dto.getPopupSirkaChange());
			dao.setLookupZobrazenie(dto.getLookupZobrazenie());
			dao.setEditControl(dto.getEditControl());
			dao.setRegExp(dto.getRegExp());
			dao.setPopis(dto.getPopis());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setCiselnikStlpecGuiID(dao.getCiselnikStlpecGuiId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOCiselnikStlpecGui dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Map<Integer, Integer> poradieMap = getDelegate().getCiselnikStlpecGuiRead().poradieMap(auth, dto.getIDCiselnikGui(), dto.getCiselnikStlpecGuiID(), dto.getPoradie());

			Date d = new Date();

			getConnection(auth);

			updatePoradie(auth, poradieMap);

			ActionResult actionResult = updateSoft(auth, dto, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return messageLookup(actionResult);

		} catch (Exception t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public void update(AuthInfo auth, List<DTOCiselnikStlpecGui> list, Integer ciselnikGuiID, Date d) throws AppException {

		try {
			if (StringUtils.isValid(list)) {
				for (DTOCiselnikStlpecGui dto : list) {
					dto.setCiselnikStlpecGuiID(null);
					dto.setIDCiselnikGui(ciselnikGuiID);
					updateSoft(auth, dto, d);
				}
			}

		} catch (Exception t) {
			handleException(t, "update.error", auth);
		}
	}

	public String delete(AuthInfo auth, Integer ciselnikStlpecGuiID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			ActionResult actionResult = deleteSoft(auth, ciselnikStlpecGuiID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return messageLookup(actionResult);

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

	private ActionResult deleteSoft(AuthInfo auth, Integer ciselnikStlpecGuiID, Date d) throws AppException {

		try {
			// delete soft
			CudCiselnikStlpecGui dao = CudCiselnikStlpecGuiPeer.retrieveByPK(ciselnikStlpecGuiID, auth.T);
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZrusene(auth.getTransakciaID());

			dao.save(auth.T);

			return new ActionResult(ciselnikStlpecGuiID);

		} catch (Throwable t) {
			return handleException(t, "deleteSoft.error", auth);
		}
	}

	public void deleteByFk(AuthInfo auth, Integer ciselnikGuiID, Date d) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, ciselnikGuiID);
			whereCrit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, null);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudCiselnikStlpecGuiPeer.CAS_ZMENY, d);
			valuesCrit.add(CudCiselnikStlpecGuiPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZRUSENE, auth.getTransakciaID());

			CudCiselnikStlpecGuiPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "deleteByFk.error", auth);
		}
	}

	private ActionResult updatePoradie(AuthInfo auth, Map<Integer, Integer> poradieMap) throws AppException {

		try {
			for (Integer ciselnikStlpecGuiID : poradieMap.keySet()) {
				Integer poradie = poradieMap.get(ciselnikStlpecGuiID);
				updatePoradie(auth, ciselnikStlpecGuiID, poradie + 1);
			}

			return null;

		} catch (Throwable t) {
			return handleException(t, "updatePoradie.error", auth);
		}
	}

	private ActionResult updatePoradie(AuthInfo auth, Integer ciselnikStlpecGuiID, Integer poradie) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, ciselnikStlpecGuiID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudCiselnikStlpecGuiPeer.PORADIE, poradie);
			valuesCrit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudCiselnikStlpecGuiPeer.doUpdate(whereCrit, valuesCrit, auth.T);

			return null;

		} catch (Throwable t) {
			return handleException(t, "updatePoradie.error", auth);
		}
	}

	private ActionResult insertSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			CudCiselnikStlpecGui dao = new CudCiselnikStlpecGui();

			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI));
			if (StringUtils.isValid(dtoZS)) {
				dao.setIdCiselnikGui(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC));
			if (StringUtils.isValid(dtoZS)) {
				dao.setIdCiselnikStlpec(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.NADPIS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setNadpis(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.PORADIE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPoradie(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.DLZKA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setDlzka(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.DECIMALS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setDecimals(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.ZMENA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setZmena(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POVINNY));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPovinny(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.ZAROVNANIE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setZarovnanie(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setFk1FkNazov(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				dao.setFk2IdCiselnik(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setFk2PkNazov(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				dao.setFk2FkNazov(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setListZobrazenie(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.LIST_SIRKA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setListSirka(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setListSirkaChange(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setFormZobrazenie(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FORM_SIRKA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setFormSirka(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPopupZobrazenie(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POPUP_SIRKA));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPopupSirka(Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPopupSirkaChange(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE));
			if (StringUtils.isValid(dtoZS)) {
				dao.setLookupZobrazenie(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.EDIT_CONTROL));
			if (StringUtils.isValid(dtoZS)) {
				dao.setEditControl(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.REG_EXP));
			if (StringUtils.isValid(dtoZS)) {
				dao.setRegExp(dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POPIS));
			if (StringUtils.isValid(dtoZS)) {
				dao.setPopis(dtoZS.getNewValue());
			}

			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setRowID(dao.getCiselnikStlpecGuiId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "insertSoft.error", auth);
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOImportZmena dto, Date d) throws AppException {

		try {
			List<DTOImportZmenaStlpec> zsList = new ArrayList<DTOImportZmenaStlpec>(Arrays.asList(dto.getImportZmenaStlpecList()));

			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudCiselnikStlpecGuiPeer.CISELNIK_STLPEC_GUI_ID, dto.getRowID());

			MyCriteria2 valuesCrit = new MyCriteria2();

			DTOImportZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.ID_CISELNIK_GUI, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.ID_CISELNIK_STLPEC, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.NADPIS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.NADPIS, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.PORADIE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.PORADIE, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.DLZKA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.DLZKA, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.DECIMALS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.DECIMALS, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.ZMENA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.ZMENA, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POVINNY));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.POVINNY, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.ZAROVNANIE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.ZAROVNANIE, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.FK1_FK_NAZOV, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.FK2_ID_CISELNIK, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.FK2_PK_NAZOV, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.FK2_FK_NAZOV, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.LIST_ZOBRAZENIE, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.LIST_SIRKA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.LIST_SIRKA, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.LIST_SIRKA_CHANGE, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.FORM_ZOBRAZENIE, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.FORM_SIRKA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.FORM_SIRKA, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.POPUP_ZOBRAZENIE, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POPUP_SIRKA));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.POPUP_SIRKA, Integer.parseInt(dtoZS.getNewValue()));
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.POPUP_SIRKA_CHANGE, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.LOOKUP_ZOBRAZENIE, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.EDIT_CONTROL));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.EDIT_CONTROL, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.REG_EXP));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.REG_EXP, dtoZS.getNewValue());
			}
			dtoZS = _CudLookupUtils.lookupDTOImportZmenaStlpecByFk(zsList, trimColumnName(CudCiselnikStlpecGuiPeer.POPIS));
			if (StringUtils.isValid(dtoZS)) {
				valuesCrit.add(CudCiselnikStlpecGuiPeer.POPIS, dtoZS.getNewValue());
			}

			valuesCrit.add(CudCiselnikStlpecGuiPeer.CAS_ZMENY, d);
			valuesCrit.add(CudCiselnikStlpecGuiPeer.ID_UCET, auth.getAccountId());
			valuesCrit.add(CudCiselnikStlpecGuiPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudCiselnikStlpecGuiPeer.doUpdate(whereCrit, valuesCrit, auth.T);

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
