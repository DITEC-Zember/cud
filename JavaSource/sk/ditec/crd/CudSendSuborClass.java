package sk.ditec.crd;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.dto.DTOSendSubor;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.procvys.dto.DTOExportnySubor;
import sk.ditec.cud.utils.CudStringUtils;
import sk.ditec.dao.meta.CudSendPeer;
import sk.ditec.dao.meta.CudSendSubor;
import sk.ditec.dao.meta.CudSendSuborPeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

public class CudSendSuborClass extends _NovyPISBaseClass {

	public ArrayList<DTOSendSubor> getList(AuthInfo auth, DTOSendSubor dtoF)
			throws AppException {
		try {
			if (dtoF == null) {
				dtoF = new DTOSendSubor();
			}
			MyCriteria2 crit = new MyCriteria2(CudSendSuborPeer.SEND_SUBOR_ID, dtoF);
			CudSendSuborPeer.addSelectColumns(crit);
			crit.addConditional(CudSendSuborPeer.SEND_SUBOR_ID, dtoF.getSendSuborID());
			crit.addConditional(CudSendSuborPeer.ID_SEND, dtoF.getIDSend());
			crit.addConditional(CudSendSuborPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudSendSuborPeer.ROW_ID_EXT, dtoF.getRowIdExt());
			crit.addConditional(CudSendSuborPeer.NAZOV_SUBORU, dtoF.getNazovSuboru());
			crit.addConditional(CudSendSuborPeer.SUBOR, dtoF.getSubor());
			crit.addConditional(CudSendSuborPeer.PORADOVE_CISLO, dtoF.getPoradoveCislo());
			crit.addConditional(CudSendSuborPeer.POCET_POKUSOV, dtoF.getPocetPokusov());
			crit.addConditional(CudSendSuborPeer.CAS_VYTVORENIA, dtoF.getCasVytvorenia(), Criteria.EQUAL);
			crit.addConditional(CudSendSuborPeer.CAS_ODOSLANIA, dtoF.getCasOdoslania(), Criteria.EQUAL);
			crit.addConditional(CudSendSuborPeer.NAVRAT_KOD, dtoF.getNavratKod());
			crit.addConditional(CudSendSuborPeer.NAVRAT_TEXT, dtoF.getNavratText());
			crit.addConditional(CudSendSuborPeer.ERROR_SPRAVA, dtoF.getErrorSprava());
			crit.addConditional(CudSendSuborPeer.ERROR_CAS, dtoF.getErrorCas(), Criteria.EQUAL);
			crit.addConditional(CudSendSuborPeer.ODPOVED_UUID, dtoF.getOdpovedUuid());
			crit.addConditional(CudSendSuborPeer.ODPOVED_TYP, dtoF.getOdpovedTyp());
			crit.addConditional(CudSendSuborPeer.ODPOVED_SUBOR, dtoF.getOdpovedSubor());
			crit.addConditional(CudSendSuborPeer.ID_TRANSAKCIA_ZAPISANE, dtoF.getIDTransakciaZapisane(), Criteria.EQUAL);
			crit.addConditional(CudSendSuborPeer.ID_TRANSAKCIA_ZRUSENE, dtoF.getIDTransakciaZrusene(), Criteria.EQUAL);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOSendSubor> listDto = new ArrayList<DTOSendSubor>();
			while (iter.hasNext()) {
				DTOSendSubor dto = new DTOSendSubor();
				Record r = (Record) iter.next();
				dto.setSendSuborID(rVal(r, CudSendSuborPeer.SEND_SUBOR_ID).asIntegerObj());
				dto.setIDSend(rVal(r, CudSendSuborPeer.ID_SEND).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudSendSuborPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowIdExt(rVal(r, CudSendSuborPeer.ROW_ID_EXT).asIntegerObj());
				dto.setNazovSuboru(rVal(r, CudSendSuborPeer.NAZOV_SUBORU).asString());
				dto.setSubor(rVal(r, CudSendSuborPeer.SUBOR).asString());
				dto.setPoradoveCislo(rVal(r, CudSendSuborPeer.PORADOVE_CISLO).asIntegerObj());
				dto.setPocetPokusov(rVal(r, CudSendSuborPeer.POCET_POKUSOV).asIntegerObj());
				dto.setCasVytvorenia(rVal(r, CudSendSuborPeer.CAS_VYTVORENIA).asUtilDate());
				dto.setCasOdoslania(rVal(r, CudSendSuborPeer.CAS_ODOSLANIA).asUtilDate());
				dto.setNavratKod(rVal(r, CudSendSuborPeer.NAVRAT_KOD).asString());
				dto.setNavratText(rVal(r, CudSendSuborPeer.NAVRAT_TEXT).asString());
				dto.setErrorSprava(rVal(r, CudSendSuborPeer.ERROR_SPRAVA).asString());
				dto.setErrorCas(rVal(r, CudSendSuborPeer.ERROR_CAS).asUtilDate());
				dto.setOdpovedUuid(rVal(r, CudSendSuborPeer.ODPOVED_UUID).asString());
				dto.setOdpovedTyp(rVal(r, CudSendSuborPeer.ODPOVED_TYP).asString());
				dto.setOdpovedSubor(rVal(r, CudSendSuborPeer.ODPOVED_SUBOR).asString());
				dto.setIDTransakciaZapisane(rVal(r, CudSendSuborPeer.ID_TRANSAKCIA_ZAPISANE).asLongObj());
				dto.setIDTransakciaZrusene(rVal(r, CudSendSuborPeer.ID_TRANSAKCIA_ZRUSENE).asLongObj());

				listDto.add(dto);
			}
			return listDto;

		} catch (Throwable t) {
			handleException(t, "CudSendSuborClass.getlist.error", auth);
			return null;
		}
	}

	public ActionResult update(AuthInfo auth, DTOSendSubor dto) throws AppException {

		try {
			getConnection(auth);

			CudSendSubor dao = new CudSendSubor();
			if (StringUtils.isValid(dto.getSendSuborID())) {
				dao = CudSendSuborPeer.retrieveByPK(dto.getSendSuborID(), auth.T);
			}
			dao.setSendSuborId(dto.getSendSuborID());
			dao.setIdSend(dto.getIDSend());
			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setRowIdExt(dto.getRowIdExt());
			dao.setNazovSuboru(dto.getNazovSuboru());
			if (dto.getSubor() != null) {
				dao.setSubor(dto.getSubor().getBytes());
			}
			dao.setPoradoveCislo(dto.getPoradoveCislo());
			dao.setPocetPokusov(dto.getPocetPokusov());
			dao.setCasVytvorenia(dto.getCasVytvorenia());
			dao.setCasOdoslania(dto.getCasOdoslania());
			dao.setNavratKod(dto.getNavratKod());
			dao.setNavratText(dto.getNavratText());
			dao.setErrorSprava(CudStringUtils.trunkToSize(dto.getErrorSprava(), 500));
			dao.setErrorCas(dto.getErrorCas());
			dao.setOdpovedUuid(dto.getOdpovedUuid());
			dao.setOdpovedTyp(dto.getOdpovedTyp());
			if (dto.getOdpovedSubor() != null) {
				dao.setOdpovedSubor(dto.getOdpovedSubor().getBytes());
			}
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);
			dto.setSendSuborID(dao.getSendSuborId());
			returnConnection(auth);

			return new ActionResult(dto);

		} catch (Throwable e) {
			handleException(e, "CudSendSuborClass.update.error", auth);
			return null;
		}
	}

	public void vytvorZaznam(AuthInfo auth, Integer sendId, DTOExportnySubor[] exportnySuborList) throws AppException {
		for(DTOExportnySubor cudSubor : exportnySuborList) {
			// Systém vytvorí záznam v CUD_SEND_SUBOR, kde
			DTOSendSubor dtoSendSubor = new DTOSendSubor();
			dtoSendSubor.setIDSend(sendId);

			dtoSendSubor.setIDCiselnik(cudSubor.getIdCiselnik());
			dtoSendSubor.setRowIdExt(cudSubor.getRowIdExt());

			dtoSendSubor.setSubor(cudSubor.getSubor());
			dtoSendSubor.setNazovSuboru(cudSubor.getNazovSuboru());
			dtoSendSubor.setCasVytvorenia(cudSubor.getCasVytvorenia());

			dtoSendSubor.setPoradoveCislo(cudSubor.getPoradoveCislo());
			dtoSendSubor.setPocetPokusov(1);
			update(auth, dtoSendSubor);
		}
	}

	/**
	 * Metoda aktualizuje len zadane vstupne nenulove hodnoty. Nesluzi na odmazanie hodnot resp. prepisanie ostatnych existujucich hodnot na NULL.
	 * @param auth
	 * @param cudSendSubor
	 * @param navratKod
	 * @param navratText
	 * @param chybovaSprava
	 * @param xml
	 * @param identifikatorOdpoved
	 * @param typOdpovede
	 * @throws AppException
	 */
	public void aktualizujZaznam(AuthInfo auth, DTOSendSubor cudSendSubor, String navratKod, String navratText, String chybovaSprava, String xml, String identifikatorOdpoved, String typOdpovede) throws AppException {
		if (navratKod != null)
			cudSendSubor.setNavratKod(navratKod);
		if (navratText != null)
			cudSendSubor.setNavratText(navratText);

		if (chybovaSprava != null && !chybovaSprava.isEmpty()) {
			cudSendSubor.setErrorSprava(CudStringUtils.trunkToSize(chybovaSprava, 500));
			cudSendSubor.setErrorCas(new Date());

			if (cudSendSubor.getPocetPokusov() == null)
				cudSendSubor.setPocetPokusov(1);
			else
				cudSendSubor.setPocetPokusov(cudSendSubor.getPocetPokusov() + 1);
		} else {
			cudSendSubor.setErrorSprava(null);
			cudSendSubor.setErrorCas(null);
		}

		if (xml != null) {
			cudSendSubor.setSubor(xml);
		}

		if (identifikatorOdpoved != null)
			cudSendSubor.setOdpovedUuid(identifikatorOdpoved);
		if (typOdpovede != null)
			cudSendSubor.setOdpovedTyp(typOdpovede);

		update(auth, cudSendSubor);
	}

	public void aktualizujDatumOdoslania(AuthInfo auth, DTOSendSubor cudSendSubor, Date datum) throws AppException {
		cudSendSubor.setCasOdoslania(datum);
		update(auth, cudSendSubor);
	}

	/**
	 * Systém vráti objekty CUD_SEND_SUBOR, ktoré ešte neboli odoslané. Metoda nenacitava BLOB stlpce (SUBOR a ODPOVED_SUBOR), ktore
	 * treba docitat pocas spracovania pre kazdy subor zvlast.
	 * @param auth
	 * @param cudOdberatelObjekt
	 * @param loadBlobColumns
	 * @return Systém vyhľadá CUD_SEND_SUBOR  kde
	 * CUD_SEND_SUBOR.CAS_ODOSLANIA = NULL
	 * a zároveň  ak na vstupe CudOdberatelObjekt tak musí platiť CUD_SEND_SUBOR.CUD_SEND.ID_ODBERATEL_OBJEKT=CudOdberatelObjekt
	 * usporiada záznamy podľa CUD_SEND_SUBOR.CUD_SEND.CAS_VYTVORENIA,CUD_SEND_SUBOR.PORADOVE_CISLO
	 * @throws AppException
	 */
	public List<DTOSendSubor> vratZaznamyPreExport(AuthInfo auth, DTOOdberatelObjekt cudOdberatelObjekt, boolean loadBlobColumns)
			throws AppException {
		List<DTOSendSubor> dtoSendSuborList = new ArrayList<DTOSendSubor>();

		try {
			MyCriteria2 crit = new MyCriteria2();
			crit.addSelectColumn(CudSendSuborPeer.SEND_SUBOR_ID);
			crit.addSelectColumn(CudSendSuborPeer.ID_SEND);
			crit.addSelectColumn(CudSendSuborPeer.ID_CISELNIK);
			crit.addSelectColumn(CudSendSuborPeer.ROW_ID_EXT);
			crit.addSelectColumn(CudSendSuborPeer.NAZOV_SUBORU);
			crit.addSelectColumn(CudSendSuborPeer.PORADOVE_CISLO);
			crit.addSelectColumn(CudSendSuborPeer.POCET_POKUSOV);
			crit.addSelectColumn(CudSendSuborPeer.CAS_VYTVORENIA);
			crit.addSelectColumn(CudSendSuborPeer.CAS_ODOSLANIA);
			crit.addSelectColumn(CudSendSuborPeer.NAVRAT_KOD);
			crit.addSelectColumn(CudSendSuborPeer.NAVRAT_TEXT);
			crit.addSelectColumn(CudSendSuborPeer.ERROR_SPRAVA);
			crit.addSelectColumn(CudSendSuborPeer.ERROR_CAS);
			crit.addSelectColumn(CudSendSuborPeer.ODPOVED_UUID);
			crit.addSelectColumn(CudSendSuborPeer.ODPOVED_TYP);
			crit.addSelectColumn(CudSendSuborPeer.ID_TRANSAKCIA_ZAPISANE);
			crit.addSelectColumn(CudSendSuborPeer.ID_TRANSAKCIA_ZRUSENE);

			if (loadBlobColumns) {
				crit.addSelectColumn(CudSendSuborPeer.SUBOR);
				crit.addSelectColumn(CudSendSuborPeer.ODPOVED_SUBOR);
			}

			crit.add(CudSendSuborPeer.CAS_ODOSLANIA, null);
			crit.addJoin(CudSendSuborPeer.ID_SEND, CudSendPeer.SEND_ID, MyCriteria2.INNER_JOIN);

			if(cudOdberatelObjekt != null) {
				crit.and(CudSendPeer.ID_ODBERATEL_OBJEKT, cudOdberatelObjekt.getOdberatelObjektID());
			}

			crit.addAscendingOrderByColumn(CudSendPeer.CAS_VYTVORENIA);
			crit.addAscendingOrderByColumn(CudSendSuborPeer.PORADOVE_CISLO);

			String sql = crit.getSQL();

			getConnection(auth);
			List lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);


			Iterator iter = lp.iterator();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				dtoSendSuborList.add(vytvor(r, loadBlobColumns));
			}
			return dtoSendSuborList;
		} catch (Throwable t) {
			handleException(t, "getByOdberatelId.error", auth);
			return null;
		}
	}

	public DTOSendSubor vratSuborPreExport(AuthInfo auth, int cudSendSuborId)
			throws AppException {

		try {
			MyCriteria2 crit = new MyCriteria2();
			CudSendSuborPeer.addSelectColumns(crit);

			crit.add(CudSendSuborPeer.SEND_SUBOR_ID, cudSendSuborId);

			String sql = crit.getSQL();

			getConnection(auth);
			List lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			DTOSendSubor dtoSendSubor = null;
			Iterator iter = lp.iterator();
			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				dtoSendSubor = vytvor(r, true);
			}
			return dtoSendSubor;
		} catch (Throwable t) {
			handleException(t, "vratSuborPreExport.error", auth);
			return null;
		}
	}


	/**
	 * Nacita vsetky stlpce entity, ktore nie su typu BLOB.
	 * Aby pri nacitani suborov do zoznamu nezaberali vela pamate.
	 * @param r
	 * @return
	 * @throws DataSetException
	 */
	private DTOSendSubor vytvor(Record r, boolean loadBlobColumns) throws DataSetException {
	    DTOSendSubor dtoSendSubor = new DTOSendSubor();
	    dtoSendSubor.setSendSuborID(rVal(r, CudSendSuborPeer.SEND_SUBOR_ID).asIntegerObj());
	    dtoSendSubor.setIDSend(rVal(r, CudSendSuborPeer.ID_SEND).asIntegerObj());
	    dtoSendSubor.setIDCiselnik(rVal(r, CudSendSuborPeer.ID_CISELNIK).asIntegerObj());
	    dtoSendSubor.setRowIdExt(rVal(r, CudSendSuborPeer.ROW_ID_EXT).asIntegerObj());
	    dtoSendSubor.setNazovSuboru(rVal(r, CudSendSuborPeer.NAZOV_SUBORU).asString());
	    //dtoSendSubor.setSubor(rVal(r, CudSendSuborPeer.SUBOR).asString());
	    dtoSendSubor.setPoradoveCislo(rVal(r, CudSendSuborPeer.PORADOVE_CISLO).asIntegerObj());
	    dtoSendSubor.setPocetPokusov(rVal(r, CudSendSuborPeer.POCET_POKUSOV).asIntegerObj());
	    dtoSendSubor.setCasVytvorenia(rVal(r, CudSendSuborPeer.CAS_VYTVORENIA).asUtilDate());
	    dtoSendSubor.setCasOdoslania(rVal(r, CudSendSuborPeer.CAS_ODOSLANIA).asUtilDate());
	    dtoSendSubor.setNavratKod(rVal(r, CudSendSuborPeer.NAVRAT_KOD).asString());
	    dtoSendSubor.setNavratText(rVal(r, CudSendSuborPeer.NAVRAT_TEXT).asString());
	    dtoSendSubor.setErrorSprava(rVal(r, CudSendSuborPeer.ERROR_SPRAVA).asString());
	    dtoSendSubor.setErrorCas(rVal(r, CudSendSuborPeer.ERROR_CAS).asUtilDate());
	    dtoSendSubor.setOdpovedUuid(rVal(r, CudSendSuborPeer.ODPOVED_UUID).asString());
	    dtoSendSubor.setOdpovedTyp(rVal(r, CudSendSuborPeer.ODPOVED_TYP).asString());
	    //dtoSendSubor.setOdpovedSubor(rVal(r, CudSendSuborPeer.ODPOVED_SUBOR).asString());
	    //dtoSendSubor.setIDTransakciaZapisane(rVal(r, CudSendSuborPeer.ID_TRANSAKCIA_ZAPISANE).asLong());
	    //dtoSendSubor.setIDTransakciaZrusene(rVal(r, CudSendSuborPeer.ID_TRANSAKCIA_ZRUSENE).asLong());
		if (loadBlobColumns)
			return nacitajBlobStlpce(r, dtoSendSubor);

	    return dtoSendSubor;
	}

	private DTOSendSubor nacitajBlobStlpce(Record r, DTOSendSubor dtoSendSubor) throws DataSetException {
		dtoSendSubor.setSubor(rVal(r, CudSendSuborPeer.SUBOR).asString());
		dtoSendSubor.setOdpovedSubor(rVal(r, CudSendSuborPeer.ODPOVED_SUBOR).asString());
		return dtoSendSubor;
	}

}
