package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.DTO;
import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.cud.dto.DTOSendSubor;
import sk.ditec.cud.dto.DTOSubor;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudObjektPeer;
import sk.ditec.dao.meta.CudOdberatelObjektPeer;
import sk.ditec.dao.meta.CudOdberatelPeer;
import sk.ditec.dao.meta.CudSendPeer;
import sk.ditec.dao.meta.CudSendSuborPeer;

import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

public class CudSendSuborReadClass extends _CudBaseClass {

	public DTOSendSubor[] listForList(AuthInfo auth, Page page, DTOSendSubor dtoF)
			throws AppException {
		try {
			if (dtoF == null) {
				dtoF = new DTOSendSubor();
			}
			MyCriteria2 crit = new MyCriteria2(CudSendSuborPeer.SEND_SUBOR_ID, dtoF);
//			CudSendSuborPeer.addSelectColumns(crit);

			// sort
			crit.addSelectColumn(CudSendSuborPeer.PORADOVE_CISLO); //1
			crit.addSelectColumn(CudSendSuborPeer.CAS_ODOSLANIA); //2
//			crit.addSelectColumn(CudSendSuborPeer.CAS_VYTVORENIA);
			crit.addSelectColumn(CudSendPeer.CAS_VYTVORENIA);
			crit.addSelectColumn(CudSendSuborPeer.NAVRAT_KOD);
			crit.addSelectColumn(CudSendSuborPeer.NAVRAT_TEXT); //5
			crit.addSelectColumn(CudSendSuborPeer.ERROR_SPRAVA);
			crit.addSelectColumn(CudOdberatelObjektPeer.EXPORT_FORMAT);
			crit.addSelectColumn(CudSendPeer.SPRAVA_UUID); // 8


			// ostatne potrebne
			crit.addSelectColumn(CudSendSuborPeer.SEND_SUBOR_ID); //9
			crit.addSelectColumn(CudSendSuborPeer.ID_SEND); //10
			crit.addSelectColumn(CudSendSuborPeer.ID_CISELNIK);
			crit.addSelectColumn(CudSendSuborPeer.ROW_ID_EXT);
			crit.addSelectColumn(CudSendSuborPeer.NAZOV_SUBORU);

			crit.addSelectColumn(CudSendSuborPeer.POCET_POKUSOV);
			crit.addSelectColumn(CudSendSuborPeer.ERROR_CAS); // 15
			crit.addSelectColumn(CudSendSuborPeer.ODPOVED_UUID);
			crit.addSelectColumn(CudSendSuborPeer.ODPOVED_TYP);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_ODBERATEL);
			crit.addSelectColumn(CudOdberatelObjektPeer.ID_OBJEKT);

			crit.addSelectColumn(CudSendPeer.ID_ODBERATEL_OBJEKT); // 20

			// tieto dava vzdy dozadu, lebo su 'customizovane'
			crit.addAsColumn("odberatel_nazov", CudOdberatelPeer.NAZOV); // 21
			crit.addAsColumn("objekt_nazov", CudObjektPeer.NAZOV);
			crit.addAsColumn("ciselnik_nazov", CudCiselnikPeer.NAZOV);

			// joins
			crit.addJoin(CudSendSuborPeer.ID_SEND, CudSendPeer.SEND_ID, MyCriteria2.LEFT_JOIN);
			crit.addJoin(CudSendPeer.ID_ODBERATEL_OBJEKT, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID, MyCriteria2.LEFT_JOIN);
			crit.addJoin(CudOdberatelObjektPeer.ID_OBJEKT, CudObjektPeer.OBJEKT_ID, Criteria.LEFT_JOIN);
			crit.addJoin(CudOdberatelObjektPeer.ID_ODBERATEL, CudOdberatelPeer.ODBERATEL_ID, Criteria.LEFT_JOIN);
			crit.addJoin(CudSendSuborPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, Criteria.LEFT_JOIN);


			// filter: ID odberatel
			if (dtoF.getIDOdberatel() != null)
				crit.addConditional(CudOdberatelObjektPeer.ID_ODBERATEL, dtoF.getIDOdberatel());

			// filter: ID objektu
			if (dtoF.getIDObjekt() != null)
				crit.add(CudOdberatelObjektPeer.ID_OBJEKT, dtoF.getIDObjekt());

			// filter: ID ciselnik
			if (dtoF.getIDCiselnik() != null)
				crit.addConditional(CudSendSuborPeer.ID_CISELNIK, dtoF.getIDCiselnik()); // filter ID ciselnik

			// filter: cas odoslania exportu
			if (dtoF.getCasOdoslaniaOd() != null)
				crit.addConditional(CudSendSuborPeer.CAS_ODOSLANIA, dtoF.getCasOdoslaniaOd(), MyCriteria2.GREATER_EQUAL);
			if (dtoF.getCasOdoslaniaDo() != null)
				crit.addConditional(CudSendSuborPeer.CAS_ODOSLANIA, dtoF.getCasOdoslaniaDo(), MyCriteria2.LESS_EQUAL);

			// filter: cas vytvorenia exportu
			if (dtoF.getCasVytvoreniaOd() != null)
				crit.addConditional(CudSendPeer.CAS_VYTVORENIA, dtoF.getCasVytvoreniaOd(), MyCriteria2.GREATER_EQUAL);
			if (dtoF.getCasVytvoreniaDo() != null)
				crit.addConditional(CudSendPeer.CAS_VYTVORENIA, dtoF.getCasVytvoreniaDo(), MyCriteria2.LESS_EQUAL);

			// filter: odoslane s chybou ANO / NIE
			if (DTO.TRUE.equals(dtoF.getLenChybne()))
				crit.add(CudSendSuborPeer.ERROR_SPRAVA, (Object) null, MyCriteria2.ISNOTNULL);
			if (DTO.FALSE.equals(dtoF.getLenChybne()))
				crit.add(CudSendSuborPeer.ERROR_SPRAVA, null);

			// filter: len odoslane ANO / NIE
			if (DTO.TRUE.equals(dtoF.getLenOdoslane()))
				crit.add(CudSendSuborPeer.CAS_ODOSLANIA, null);
			if (DTO.FALSE.equals(dtoF.getLenOdoslane()))
				crit.add(CudSendSuborPeer.CAS_ODOSLANIA, (Object) null, MyCriteria2.ISNOTNULL);

			// filter: identifikator spravy
			if (dtoF.getSendSpravaUuid() != null && !dtoF.getSendSpravaUuid().isEmpty())
				crit.addConditional(CudSendPeer.SPRAVA_UUID, "%" + dtoF.getSendSpravaUuid(), true, true);

			// filter: navratovy kod
			if (dtoF.getNavratKod() != null && !dtoF.getNavratKod().isEmpty())
				crit.addConditional(CudSendSuborPeer.NAVRAT_KOD, dtoF.getNavratKod());

			// len nezrusene
			crit.add(CudSendSuborPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudSendSuborPeer.SEND_SUBOR_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			ArrayList<DTOSendSubor> listDto = new ArrayList<DTOSendSubor>();
			while (iter.hasNext()) {
				DTOSendSubor dto = new DTOSendSubor();
				Record r = (Record) iter.next();
				dto.setSendSuborID(rVal(r, CudSendSuborPeer.SEND_SUBOR_ID).asIntegerObj());
				dto.setIDSend(rVal(r, CudSendSuborPeer.ID_SEND).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudSendSuborPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowIdExt(rVal(r, CudSendSuborPeer.ROW_ID_EXT).asIntegerObj());
				dto.setNazovSuboru(rVal(r, CudSendSuborPeer.NAZOV_SUBORU).asString());
				dto.setPoradoveCislo(rVal(r, CudSendSuborPeer.PORADOVE_CISLO).asIntegerObj());
				dto.setPocetPokusov(rVal(r, CudSendSuborPeer.POCET_POKUSOV).asIntegerObj());
//				dto.setCasVytvorenia(rVal(r, CudSendSuborPeer.CAS_VYTVORENIA).asUtilDate()); // tento zaznam sa nepouziva / nezobrazuje

				dto.setCasOdoslania(rVal(r, CudSendSuborPeer.CAS_ODOSLANIA).asUtilDate());
				dto.setNavratKod(rVal(r, CudSendSuborPeer.NAVRAT_KOD).asString());
				dto.setNavratText(rVal(r, CudSendSuborPeer.NAVRAT_TEXT).asString());
				dto.setErrorSprava(rVal(r, CudSendSuborPeer.ERROR_SPRAVA).asString());
				dto.setErrorCas(rVal(r, CudSendSuborPeer.ERROR_CAS).asUtilDate());
				dto.setOdpovedUuid(rVal(r, CudSendSuborPeer.ODPOVED_UUID).asString());
				dto.setOdpovedTyp(rVal(r, CudSendSuborPeer.ODPOVED_TYP).asString());
//				dto.setOdpovedSubor(rVal(r, CudSendSuborPeer.ODPOVED_SUBOR).asString());

				dto.setCasVytvorenia(rVal(r, CudSendPeer.CAS_VYTVORENIA).asUtilDate()); // podla analyzy za berie cas vytvorenia z CUD_SEND tabulky

				dto.setIDOdberatel(rVal(r, CudOdberatelObjektPeer.ID_ODBERATEL).asIntegerObj());
				dto.setOdberatelNazov(rVal(r, "odberatel_nazov").asString());
				dto.setIDObjekt(rVal(r, CudOdberatelObjektPeer.ID_OBJEKT).asIntegerObj());
				dto.setObjektNazov(rVal(r, "objekt_nazov").asString());
				dto.setCiselnikNazov(rVal(r, "ciselnik_nazov").asString());
				dto.setOdberatelObjektExportFormatKod(rVal(r, CudOdberatelObjektPeer.EXPORT_FORMAT).asString());
				dto.setSendSpravaUuid(rVal(r, CudSendPeer.SPRAVA_UUID).asString());
				dto.setIDOdberatelObjekt(rVal(r, CudSendPeer.ID_ODBERATEL_OBJEKT).asIntegerObj());

				// velkost
				dto.setListSize(lp.total_count);

				listDto.add(dto);
			}
			return listDto.toArray(new DTOSendSubor[listDto.size()]);

		} catch (Throwable t) {
			handleException(t, "CudSendSuborReadClass.listForList.error", auth);
			return null;
		}
	}

	public boolean existujeNeodoslanySubor(AuthInfo auth, Integer sendId) throws AppException {
		try {
			MyCriteria2 crit = new MyCriteria2();
			crit.addSelectColumn(CudSendPeer.SEND_ID);
			crit.addSelectColumn(CudSendSuborPeer.SEND_SUBOR_ID);

			crit.add(CudSendPeer.SEND_ID, sendId);
			crit.addJoin(CudSendPeer.SEND_ID, CudSendSuborPeer.ID_SEND);
			crit.add(CudSendSuborPeer.CAS_ODOSLANIA, (Object) null, MyCriteria2.ISNULL);

			String sql = crit.getSQL();

			getConnection(auth);
			List lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator iter = lp.iterator();
			if (iter.hasNext()) {
				return true;
			}
		} catch (Throwable t) {
			handleException(t, "existujeNeodoslanySubor.error", auth);
		}
		return false;
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

	public DTOSubor sendSuborDownload(AuthInfo auth, String typ, Integer suborID) throws AppException {
		try {
			DTOSubor dto = new DTOSubor();

			if (suborID == null) {
				return dto;
			}


			String rowSuborBlob = typ.equals("suborOdpoved") ? CudSendSuborPeer.ODPOVED_SUBOR : CudSendSuborPeer.SUBOR;

			MyCriteria2 crit = new MyCriteria2();

			crit.addSelectColumn(rowSuborBlob);
			crit.addSelectColumn(CudSendSuborPeer.NAZOV_SUBORU);
			crit.add(CudSendSuborPeer.SEND_SUBOR_ID, suborID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if(iter.hasNext()) {
				Record r = (Record) iter.next();

				String nazovSuboru = rVal(r, CudSendSuborPeer.NAZOV_SUBORU).asString();

				if (nazovSuboru == null || nazovSuboru.isEmpty())
					nazovSuboru = "subor";

				if (typ.equals("suborOdpoved"))
					nazovSuboru = "odpoved-" + nazovSuboru;

				dto.setSuborID(suborID);
				dto.setNazovSuboru(nazovSuboru);
				dto.setSubor(rVal(r, rowSuborBlob).asBytes());
			}

			return dto;

		} catch (Throwable t) {
			handleException(t, "sendSuborDownload.error", auth);
			return null;
		}
	}
}
