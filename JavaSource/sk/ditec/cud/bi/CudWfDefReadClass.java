package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.ListWraper;
import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfDefCiselnikStlpec;
import sk.ditec.cud.dto.DTOWfDefLD;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudWfDefPeer;

import com.workingdogs.village.Record;

public class CudWfDefReadClass extends _CudBaseClass {

	public DTOWfDef[] list(AuthInfo auth, Page page, DTOWfDef dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOWfDef();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, dtoF);

			crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);
			crit.addSelectColumn(CudWfDefPeer.ID_CISELNIK);
			crit.addSelectColumn(CudWfDefPeer.ID_WF_DEF_NASL);
			crit.addSelectColumn(CudWfDefPeer.NAZOV);
			crit.addSelectColumn(CudWfDefPeer.TYP);
			crit.addSelectColumn(CudWfDefPeer.ZODPOVEDNOST);
			crit.addSelectColumn(CudWfDefPeer.HODINY);
			crit.addSelectColumn(CudWfDefPeer.ID_SKUPINA);
			crit.addSelectColumn(CudWfDefPeer.SKUPINA_NAZOV);

			// join Cud_CISELNIK
			crit.addAsColumn("ciselnikNazov", CudCiselnikPeer.NAZOV);
			crit.addJoin(CudWfDefPeer.ID_CISELNIK, CudCiselnikPeer.CISELNIK_ID, MyCriteria2.LEFT_JOIN);
			crit.addConditional(CudCiselnikPeer.NAZOV, dtoF.getCiselnikNazov(), true);

			// where
			crit.addConditional(CudWfDefPeer.WF_DEF_ID, dtoF.getWfDefID());
			crit.addConditional(CudWfDefPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudWfDefPeer.ID_WF_DEF_NASL, dtoF.getIDWfDefNasl());
			crit.addConditional(CudWfDefPeer.NAZOV, dtoF.getNazov(), true);
			crit.addConditional(CudWfDefPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudWfDefPeer.ZODPOVEDNOST, dtoF.getZodpovednost(), false);
			crit.addConditional(CudWfDefPeer.SKUPINA_NAZOV, dtoF.getSkupinaNazov(), true);

			crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudWfDefPeer.WF_DEF_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOWfDef> listDTO = new ArrayList<DTOWfDef>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfDef dto = new DTOWfDef();
				dto.setWfDefID(rVal(r, CudWfDefPeer.WF_DEF_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudWfDefPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDWfDefNasl(rVal(r, CudWfDefPeer.ID_WF_DEF_NASL).asIntegerObj());
				dto.setNazov(rVal(r, CudWfDefPeer.NAZOV).asString());
				dto.setTyp(rVal(r, CudWfDefPeer.TYP).asString());
				dto.setZodpovednost(rVal(r, CudWfDefPeer.ZODPOVEDNOST).asString());
				dto.setHodiny(rVal(r, CudWfDefPeer.HODINY).asIntegerObj());
				dto.setIDSkupina(rVal(r, CudWfDefPeer.ID_SKUPINA).asIntegerObj());
				dto.setSkupinaNazov(rVal(r, CudWfDefPeer.SKUPINA_NAZOV).asString());

				dto.setCiselnikNazov(rVal(r, "ciselnikNazov").asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOWfDef[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	private Map<Integer, DTOWfDef> mapLight(AuthInfo auth, Integer[] wfDefIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(wfDefIDs)) {
				return new HashMap<Integer, DTOWfDef>();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDef());

			crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_LIST);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_TEXT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SUBJECT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SEND);
			crit.addSelectColumn(CudWfDefPeer.CAS_ZMENY);
			crit.addSelectColumn(CudWfDefPeer.ID_UCET);

			if (wfDefIDs.length == 1) {
				crit.addConditional(CudWfDefPeer.WF_DEF_ID, wfDefIDs[0]);
			} else {
				crit.addIn(CudWfDefPeer.WF_DEF_ID, wfDefIDs);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOWfDef> mapDTO = new HashMap<Integer, DTOWfDef>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfDef dto = new DTOWfDef();
				dto.setWfDefID(rVal(r, CudWfDefPeer.WF_DEF_ID).asIntegerObj());
				dto.setEmailList(rVal(r, CudWfDefPeer.EMAIL_LIST).asString());
				dto.setEmailText(rVal(r, CudWfDefPeer.EMAIL_TEXT).asString());
				dto.setEmailSubject(rVal(r, CudWfDefPeer.EMAIL_SUBJECT).asString());
				dto.setEmailSend(rVal(r, CudWfDefPeer.EMAIL_SEND).asString());
				dto.setCasZmeny(rVal(r, CudWfDefPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudWfDefPeer.ID_UCET).asIntegerObj());

				mapDTO.put(dto.getWfDefID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public DTOWfDefLD loadData(AuthInfo auth, DTOWfDefLD dtoF) throws AppException {

		try {
			DTOWfDefLD resultDTO = new DTOWfDefLD();

			Set<Integer> set = new HashSet<Integer>();
			set.add(dtoF.getWfDefID());
			if (StringUtils.isValid(dtoF.getIDWfDefNasl())) {
				set.add(dtoF.getIDWfDefNasl());
			}
			Map<Integer, DTOWfDef> wfDefMap = mapLight(auth, set.toArray(new Integer[set.size()]));
			resultDTO.setWfDefDTO(wfDefMap.get(dtoF.getWfDefID()));
			resultDTO.setWfDefNaslDTO(wfDefMap.get(dtoF.getIDWfDefNasl()));

			Map<Integer, DTOCiselnik> ciselnikMap = getDelegate().getCiselnikRead().mapLight(auth, new Integer[] { dtoF.getIDCiselnik() });
			resultDTO.setCiselnikDTO(ciselnikMap.get(dtoF.getIDCiselnik()));

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public List<DTOWfDef> listLight(AuthInfo auth, DTOWfDef dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOWfDef();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, dtoF);

			crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);
			crit.addSelectColumn(CudWfDefPeer.ID_CISELNIK);
			crit.addSelectColumn(CudWfDefPeer.ID_WF_DEF_NASL);
			crit.addSelectColumn(CudWfDefPeer.NAZOV);
			crit.addSelectColumn(CudWfDefPeer.TYP);
			crit.addSelectColumn(CudWfDefPeer.HODINY);
			crit.addSelectColumn(CudWfDefPeer.ZODPOVEDNOST);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_LIST);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_TEXT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SUBJECT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SEND);
			crit.addSelectColumn(CudWfDefPeer.ID_SKUPINA);
			crit.addSelectColumn(CudWfDefPeer.SKUPINA_NAZOV);

			crit.addConditional(CudWfDefPeer.WF_DEF_ID, dtoF.getWfDefID());
			crit.addConditional(CudWfDefPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudWfDefPeer.ID_WF_DEF_NASL, dtoF.getIDWfDefNasl());
			crit.addConditional(CudWfDefPeer.NAZOV, dtoF.getNazov(), false);
			crit.addConditional(CudWfDefPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudWfDefPeer.HODINY, dtoF.getHodiny());
			crit.addConditional(CudWfDefPeer.ZODPOVEDNOST, dtoF.getZodpovednost());
			crit.addConditional(CudWfDefPeer.EMAIL_LIST, dtoF.getEmailList(), false);
			crit.addConditional(CudWfDefPeer.EMAIL_TEXT, dtoF.getEmailText(), false);
			crit.addConditional(CudWfDefPeer.EMAIL_SUBJECT, dtoF.getEmailSubject(), false);
			crit.addConditional(CudWfDefPeer.EMAIL_SEND, dtoF.getEmailSend(), false);
			crit.addConditional(CudWfDefPeer.ID_SKUPINA, dtoF.getIDSkupina());
			crit.addConditional(CudWfDefPeer.SKUPINA_NAZOV, dtoF.getSkupinaNazov());

			crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudWfDefPeer.WF_DEF_ID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOWfDef> listDTO = new ArrayList<DTOWfDef>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfDef dto = new DTOWfDef();
				dto.setWfDefID(rVal(r, CudWfDefPeer.WF_DEF_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudWfDefPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDWfDefNasl(rVal(r, CudWfDefPeer.ID_WF_DEF_NASL).asIntegerObj());
				dto.setNazov(rVal(r, CudWfDefPeer.NAZOV).asString());
				dto.setTyp(rVal(r, CudWfDefPeer.TYP).asString());
				dto.setHodiny(rVal(r, CudWfDefPeer.HODINY).asIntegerObj());
				dto.setZodpovednost(rVal(r, CudWfDefPeer.ZODPOVEDNOST).asString());
				dto.setEmailList(rVal(r, CudWfDefPeer.EMAIL_LIST).asString());
				dto.setEmailText(rVal(r, CudWfDefPeer.EMAIL_TEXT).asString());
				dto.setEmailSubject(rVal(r, CudWfDefPeer.EMAIL_SUBJECT).asString());
				dto.setEmailSend(rVal(r, CudWfDefPeer.EMAIL_SEND).asString());
				dto.setIDSkupina(rVal(r, CudWfDefPeer.ID_SKUPINA).asIntegerObj());
				dto.setSkupinaNazov(rVal(r, CudWfDefPeer.SKUPINA_NAZOV).asString());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public List<DTOWfDef> listLight(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return new ArrayList<DTOWfDef>();
			}

			DTOWfDef dtoF = new DTOWfDef();
			dtoF.setIDCiselnik(ciselnikID);
			return listLight(auth, dtoF);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOWfDef readLight(AuthInfo auth, Integer wfDefID) throws AppException {

		try {
			if (!StringUtils.isValid(wfDefID)) {
				return null;
			}

			DTOWfDef dtoF = new DTOWfDef();
			dtoF.setWfDefID(wfDefID);
			List<DTOWfDef> listDTO = listLight(auth, dtoF);

			return listDTO.isEmpty() ? null : listDTO.get(0);

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	private List<DTOWfDef> list(AuthInfo auth, DTOWfDef dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOWfDef();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, dtoF);

			crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);
			crit.addSelectColumn(CudWfDefPeer.ID_CISELNIK);
			crit.addSelectColumn(CudWfDefPeer.ID_WF_DEF_NASL);
			crit.addSelectColumn(CudWfDefPeer.NAZOV);
			crit.addSelectColumn(CudWfDefPeer.TYP);
			crit.addSelectColumn(CudWfDefPeer.HODINY);
			crit.addSelectColumn(CudWfDefPeer.ZODPOVEDNOST);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_LIST);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_TEXT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SUBJECT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SEND);
			crit.addSelectColumn(CudWfDefPeer.ID_SKUPINA);
			crit.addSelectColumn(CudWfDefPeer.SKUPINA_NAZOV);

			crit.addConditional(CudWfDefPeer.WF_DEF_ID, dtoF.getWfDefID());
			crit.addConditional(CudWfDefPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudWfDefPeer.ID_WF_DEF_NASL, dtoF.getIDWfDefNasl());
			crit.addConditional(CudWfDefPeer.NAZOV, dtoF.getNazov(), false);
			crit.addConditional(CudWfDefPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudWfDefPeer.HODINY, dtoF.getHodiny());
			crit.addConditional(CudWfDefPeer.ZODPOVEDNOST, dtoF.getZodpovednost());
			crit.addConditional(CudWfDefPeer.EMAIL_LIST, dtoF.getEmailList(), false);
			crit.addConditional(CudWfDefPeer.EMAIL_TEXT, dtoF.getEmailText(), false);
			crit.addConditional(CudWfDefPeer.EMAIL_SUBJECT, dtoF.getEmailSubject(), false);
			crit.addConditional(CudWfDefPeer.EMAIL_SEND, dtoF.getEmailSend(), false);
			crit.addConditional(CudWfDefPeer.ID_SKUPINA, dtoF.getIDSkupina());
			crit.addConditional(CudWfDefPeer.SKUPINA_NAZOV, dtoF.getSkupinaNazov());

			crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudWfDefPeer.WF_DEF_ID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOWfDef> listDTO = new ArrayList<DTOWfDef>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfDef dto = new DTOWfDef();
				dto.setWfDefID(rVal(r, CudWfDefPeer.WF_DEF_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudWfDefPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDWfDefNasl(rVal(r, CudWfDefPeer.ID_WF_DEF_NASL).asIntegerObj());
				dto.setNazov(rVal(r, CudWfDefPeer.NAZOV).asString());
				dto.setTyp(rVal(r, CudWfDefPeer.TYP).asString());
				dto.setHodiny(rVal(r, CudWfDefPeer.HODINY).asIntegerObj());
				dto.setZodpovednost(rVal(r, CudWfDefPeer.ZODPOVEDNOST).asString());
				dto.setEmailList(rVal(r, CudWfDefPeer.EMAIL_LIST).asString());
				dto.setEmailText(rVal(r, CudWfDefPeer.EMAIL_TEXT).asString());
				dto.setEmailSubject(rVal(r, CudWfDefPeer.EMAIL_SUBJECT).asString());
				dto.setEmailSend(rVal(r, CudWfDefPeer.EMAIL_SEND).asString());
				dto.setIDSkupina(rVal(r, CudWfDefPeer.ID_SKUPINA).asIntegerObj());
				dto.setSkupinaNazov(rVal(r, CudWfDefPeer.SKUPINA_NAZOV).asString());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public List<DTOWfDef> list(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return new ArrayList<DTOWfDef>();
			}

			DTOWfDef dtoF = new DTOWfDef();
			dtoF.setIDCiselnik(ciselnikID);
			List<DTOWfDef> listDTO = list(auth, dtoF);

			Map<Integer, List<DTOWfDefCiselnikStlpec>> csMap = getDelegate().getWfDefCiselnikStlpecRead().mapByCiselnik(auth, ciselnikID);

			for (DTOWfDef dto : listDTO) {
				List<DTOWfDefCiselnikStlpec> csList = csMap.get(dto.getWfDefID());
				if (StringUtils.isValid(csList)) {
					dto.setWfDefCiselnikStlpecList(csList.toArray(new DTOWfDefCiselnikStlpec[csList.size()]));
				}
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "list.error", auth);
			return null;
		}
	}

	public DTOWfDef readLastLight(AuthInfo auth, Integer ciselnikID, String typ) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDef());

			crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);
			crit.addSelectColumn(CudWfDefPeer.ID_CISELNIK);
			crit.addSelectColumn(CudWfDefPeer.NAZOV);
			crit.addSelectColumn(CudWfDefPeer.TYP);
			crit.addSelectColumn(CudWfDefPeer.HODINY);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_LIST);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_TEXT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SUBJECT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SEND);
			crit.addSelectColumn(CudWfDefPeer.ID_SKUPINA);
			crit.addSelectColumn(CudWfDefPeer.SKUPINA_NAZOV);

			crit.addConditional(CudWfDefPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudWfDefPeer.TYP, typ, false);

			crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addDescendingOrderByColumn(CudWfDefPeer.HODINY);
			crit.addDescendingOrderByColumn(CudWfDefPeer.WF_DEF_ID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			DTOWfDef resultDTO = new DTOWfDef();
			resultDTO.setEmailSend("F");

			if (iter.hasNext()) {
				Record r = (Record) iter.next();

				resultDTO.setWfDefID(rVal(r, CudWfDefPeer.WF_DEF_ID).asIntegerObj());
				resultDTO.setIDCiselnik(rVal(r, CudWfDefPeer.ID_CISELNIK).asIntegerObj());
				resultDTO.setNazov(rVal(r, CudWfDefPeer.NAZOV).asString());
				resultDTO.setTyp(rVal(r, CudWfDefPeer.TYP).asString());
				resultDTO.setHodiny(rVal(r, CudWfDefPeer.HODINY).asIntegerObj());
				resultDTO.setEmailList(rVal(r, CudWfDefPeer.EMAIL_LIST).asString());
				resultDTO.setEmailText(rVal(r, CudWfDefPeer.EMAIL_TEXT).asString());
				resultDTO.setEmailSubject(rVal(r, CudWfDefPeer.EMAIL_SUBJECT).asString());
				resultDTO.setEmailSend(rVal(r, CudWfDefPeer.EMAIL_SEND).asString());
				resultDTO.setIDSkupina(rVal(r, CudWfDefPeer.ID_SKUPINA).asIntegerObj());
				resultDTO.setSkupinaNazov(rVal(r, CudWfDefPeer.SKUPINA_NAZOV).asString());
			}

			if (!StringUtils.isValid(resultDTO.getHodiny())) {
				resultDTO.setHodiny(0);
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "readLastLight.error", auth);
			return null;
		}
	}

	public Map<Integer, List<DTOWfDef>> mapLight(AuthInfo auth, DTOWfDef dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOWfDef();
			}

			MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, dtoF);

			crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);
			crit.addSelectColumn(CudWfDefPeer.ID_CISELNIK);
			crit.addSelectColumn(CudWfDefPeer.ID_WF_DEF_NASL);
			crit.addSelectColumn(CudWfDefPeer.NAZOV);
			crit.addSelectColumn(CudWfDefPeer.TYP);
			crit.addSelectColumn(CudWfDefPeer.ZODPOVEDNOST);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_LIST);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_TEXT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SUBJECT);
			crit.addSelectColumn(CudWfDefPeer.EMAIL_SEND);
			crit.addSelectColumn(CudWfDefPeer.HODINY);
			crit.addSelectColumn(CudWfDefPeer.ID_SKUPINA);
			crit.addSelectColumn(CudWfDefPeer.SKUPINA_NAZOV);

			crit.addConditional(CudWfDefPeer.WF_DEF_ID, dtoF.getWfDefID());
			crit.addConditional(CudWfDefPeer.ID_CISELNIK, dtoF.getIDCiselnik());
			crit.addConditional(CudWfDefPeer.ID_WF_DEF_NASL, dtoF.getIDWfDefNasl());
			crit.addConditional(CudWfDefPeer.NAZOV, dtoF.getNazov(), false);
			crit.addConditional(CudWfDefPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudWfDefPeer.ZODPOVEDNOST, dtoF.getZodpovednost());
			crit.addConditional(CudWfDefPeer.EMAIL_LIST, dtoF.getEmailList(), false);
			crit.addConditional(CudWfDefPeer.EMAIL_TEXT, dtoF.getEmailText(), false);
			crit.addConditional(CudWfDefPeer.EMAIL_SUBJECT, dtoF.getEmailSubject(), false);
			crit.addConditional(CudWfDefPeer.EMAIL_SEND, dtoF.getEmailSend(), false);
			crit.addConditional(CudWfDefPeer.HODINY, dtoF.getHodiny());
			crit.addConditional(CudWfDefPeer.ID_SKUPINA, dtoF.getIDSkupina());
			crit.addConditional(CudWfDefPeer.SKUPINA_NAZOV, dtoF.getSkupinaNazov());

			crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, List<DTOWfDef>> resultMap = new HashMap<Integer, List<DTOWfDef>>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOWfDef dto = new DTOWfDef();
				dto.setWfDefID(rVal(r, CudWfDefPeer.WF_DEF_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudWfDefPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDWfDefNasl(rVal(r, CudWfDefPeer.ID_WF_DEF_NASL).asIntegerObj());
				dto.setNazov(rVal(r, CudWfDefPeer.NAZOV).asString());
				dto.setTyp(rVal(r, CudWfDefPeer.TYP).asString());
				dto.setZodpovednost(rVal(r, CudWfDefPeer.ZODPOVEDNOST).asString());
				dto.setEmailList(rVal(r, CudWfDefPeer.EMAIL_LIST).asString());
				dto.setEmailText(rVal(r, CudWfDefPeer.EMAIL_TEXT).asString());
				dto.setEmailSubject(rVal(r, CudWfDefPeer.EMAIL_SUBJECT).asString());
				dto.setEmailSend(rVal(r, CudWfDefPeer.EMAIL_SEND).asString());
				dto.setHodiny(rVal(r, CudWfDefPeer.HODINY).asIntegerObj());
				dto.setIDSkupina(rVal(r, CudWfDefPeer.ID_SKUPINA).asIntegerObj());
				dto.setSkupinaNazov(rVal(r, CudWfDefPeer.SKUPINA_NAZOV).asString());

				if (!StringUtils.isValid(resultMap.get(dto.getIDCiselnik()))) {
					resultMap.put(dto.getIDCiselnik(), new ArrayList<DTOWfDef>());
				}
				resultMap.get(dto.getIDCiselnik()).add(dto);
			}

			return resultMap;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	/**
	 * Funckia zisti, ci pouzivatel auth ma opravnenie modifikovat ciselnik
	 * 
	 * @param auth
	 *            pouzivatel
	 * @param ciselnikID
	 *            dany ciselnik
	 * @return
	 * @throws AppException
	 */
	public boolean maOpravnenieModify(AuthInfo auth, Integer ciselnikID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			// ziska sa ID skupiny ktora moze modifikovat dany ciselnik
			MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDef());

			crit.addSelectColumn(CudWfDefPeer.ID_SKUPINA);

			crit.addConditional(CudWfDefPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudWfDefPeer.TYP, _CudConsts.WF_DEF_TYP_IN, false);

			crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			Integer skupinaID = null;
			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				skupinaID = rVal(r, CudWfDefPeer.ID_SKUPINA).asIntegerObj();
			}

			if (!StringUtils.isValid(skupinaID)) {
				return false;
			}

			// // kontrola v IAM-e ci dany pouzivatel sa nachadza v skupine, ktora moze modifikovat dany ciselnik
			ListWraper<AuthInfo> listWS = FrameworkUtils.getAuthMod().groupAccountList(auth, new Page(true), skupinaID);

			for (AuthInfo dtoWS : listWS.getList()) {
				if (auth.getAccountId() == dtoWS.getAccountId()) {
					return true;
				}
			}

			return false;

		} catch (Throwable t) {
			handleException(t, "maOpravnenieModify.error", auth);
			return false;
		}
	}

	public String updateKontrola(AuthInfo auth, DTOWfDef dto) throws AppException {

		try {
			if (!_CudKontrolaUtils.isValidEmailList(dto.getEmailList())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_621);
			}

			if (StringUtils.isValid(dto.getWfDefCiselnikStlpecList())) {
				Set<Integer> ids = getDelegate().getCiselnikStlpecRead().ids(auth, dto.getIDCiselnik());
				for (DTOWfDefCiselnikStlpec dtoItem : dto.getWfDefCiselnikStlpecList()) {
					if (!ids.contains(dtoItem.getIDCiselnikStlpec())) {
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3115);
					}
				}
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

}
