package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOObjekt;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudObjektPeer;

import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

public class CudObjektReadClass extends _CudBaseClass {

	public DTOObjekt[] listForList(AuthInfo auth, Page page, DTOObjekt dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOObjekt();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektPeer.OBJEKT_ID, dtoF);

			crit.addSelectColumn(CudObjektPeer.OBJEKT_ID);
			crit.addSelectColumn(CudObjektPeer.NAZOV);
			crit.addSelectColumn(CudObjektPeer.PLATNY);
			crit.addSelectColumn(CudObjektPeer.SYSTEMOVY);

			crit.addConditional(CudObjektPeer.OBJEKT_ID, StringUtils.isValid(dtoF.getObjektID()) ? dtoF.getObjektID().toString() : null, true);
			crit.addConditional(CudObjektPeer.NAZOV, dtoF.getNazov(), true);
			crit.addConditional(CudObjektPeer.PLATNY, dtoF.getPlatny(), false);

			crit.add(CudObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudObjektPeer.OBJEKT_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOObjekt> listDTO = new ArrayList<DTOObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjekt dto = new DTOObjekt();
				dto.setObjektID(rVal(r, CudObjektPeer.OBJEKT_ID).asIntegerObj());
				dto.setNazov(rVal(r, CudObjektPeer.NAZOV).asString());
				dto.setPlatny(rVal(r, CudObjektPeer.PLATNY).asString());
				dto.setSystemovy(rVal(r, CudObjektPeer.SYSTEMOVY).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOObjekt[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForList.error", auth);
			return null;
		}
	}

	public DTOObjekt[] listForPop(AuthInfo auth, Page page, DTOObjekt dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOObjekt();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektPeer.OBJEKT_ID, dtoF);

			crit.addSelectColumn(CudObjektPeer.OBJEKT_ID);
			crit.addSelectColumn(CudObjektPeer.NAZOV);
			crit.addSelectColumn(CudObjektPeer.PLATNY);
			crit.addSelectColumn(CudObjektPeer.SYSTEMOVY);
			crit.addSelectColumn(CudObjektPeer.SYSTEMOVY_KANAL);
			crit.addSelectColumn(CudObjektPeer.SYSTEMOVY_VSETKY_CISELNIKY);
			crit.addSelectColumn(CudObjektPeer.SYSTEMOVY_EXPORT_FORMAT);

			crit.addConditional(CudObjektPeer.OBJEKT_ID, StringUtils.isValid(dtoF.getObjektID()) ? dtoF.getObjektID().toString() : null, true);
			crit.addConditional(CudObjektPeer.NAZOV, dtoF.getNazov(), true);
			crit.addConditional(CudObjektPeer.PLATNY, dtoF.getPlatny(), false);

			crit.add(CudObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudObjektPeer.OBJEKT_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOObjekt> listDTO = new ArrayList<DTOObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjekt dto = new DTOObjekt();
				dto.setObjektID(rVal(r, CudObjektPeer.OBJEKT_ID).asIntegerObj());
				dto.setNazov(rVal(r, CudObjektPeer.NAZOV).asString());
				dto.setPlatny(rVal(r, CudObjektPeer.PLATNY).asString());
				dto.setSystemovy(rVal(r, CudObjektPeer.SYSTEMOVY).asString());
				dto.setSystemovyKanal(rVal(r, CudObjektPeer.SYSTEMOVY_KANAL).asString());
				dto.setSystemovyVsetkyCiselniky(rVal(r, CudObjektPeer.SYSTEMOVY_VSETKY_CISELNIKY).asString());
				dto.setSystemovyExportFormat(rVal(r, CudObjektPeer.SYSTEMOVY_EXPORT_FORMAT).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOObjekt[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listForPop.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOObjekt> mapLight(AuthInfo auth, Integer[] ids) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ids)) {
				return new HashMap<Integer, DTOObjekt>();
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektPeer.OBJEKT_ID, new DTOObjekt());

			crit.addSelectColumn(CudObjektPeer.OBJEKT_ID);
			crit.addSelectColumn(CudObjektPeer.NAZOV);
			crit.addSelectColumn(CudObjektPeer.PLATNY);
			crit.addSelectColumn(CudObjektPeer.SYSTEMOVY);
			crit.addSelectColumn(CudObjektPeer.SYSTEMOVY_KANAL);
			crit.addSelectColumn(CudObjektPeer.SYSTEMOVY_VSETKY_CISELNIKY);
			crit.addSelectColumn(CudObjektPeer.SYSTEMOVY_EXPORT_FORMAT);
			crit.addSelectColumn(CudObjektPeer.CAS_ZMENY);
			crit.addSelectColumn(CudObjektPeer.ID_UCET);

			if (ids.length == 1) {
				crit.addConditional(CudObjektPeer.OBJEKT_ID, ids[0]);
			} else {
				crit.addIn(CudObjektPeer.OBJEKT_ID, ids);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOObjekt> mapDTO = new HashMap<Integer, DTOObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOObjekt dto = new DTOObjekt();
				dto.setObjektID(rVal(r, CudObjektPeer.OBJEKT_ID).asIntegerObj());
				dto.setNazov(rVal(r, CudObjektPeer.NAZOV).asString());
				dto.setPlatny(rVal(r, CudObjektPeer.PLATNY).asString());
				dto.setSystemovy(rVal(r, CudObjektPeer.SYSTEMOVY).asString());
				dto.setSystemovyKanal(rVal(r, CudObjektPeer.SYSTEMOVY_KANAL).asString());
				dto.setSystemovyVsetkyCiselniky(rVal(r, CudObjektPeer.SYSTEMOVY_VSETKY_CISELNIKY).asString());
				dto.setSystemovyExportFormat(rVal(r, CudObjektPeer.SYSTEMOVY_EXPORT_FORMAT).asString());
				dto.setCasZmeny(rVal(r, CudObjektPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudObjektPeer.ID_UCET).asIntegerObj());

				mapDTO.put(dto.getObjektID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public DTOObjekt read(AuthInfo auth, Integer objektID) throws AppException {

		try {
			if (!StringUtils.isValid(objektID)) {
				return null;
			}

			return mapLight(auth, new Integer[] { objektID }).get(objektID);

		} catch (Throwable t) {
			handleException(t, "read.error", auth);
			return null;
		}
	}

	public DTOObjekt loadData(AuthInfo auth, DTOObjekt dtoF) throws AppException {

		try {
			Map<Integer, DTOObjekt> mapa = mapLight(auth, new Integer[] { dtoF.getObjektID() });
			return mapa.get(dtoF.getObjektID());

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	private boolean kontrolaNaNazov(AuthInfo auth, Integer objektID, String nazov) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(nazov)) {
				return false;
			}

			MyCriteria2 crit = new MyCriteria2(CudObjektPeer.OBJEKT_ID, new DTOObjekt());

			crit.addSelectColumn(CudObjektPeer.OBJEKT_ID);

			crit.addConditional(CudObjektPeer.NAZOV, nazov, false);

			if (StringUtils.isValid(objektID)) {
				crit.add(CudObjektPeer.OBJEKT_ID, objektID, MyCriteria2.NOT_EQUAL);
			}

			crit.add(CudObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			return lp.iterator().hasNext();

		} catch (Throwable t) {
			handleException(t, "kontrolaNaNazov.error", auth);
			return false;
		}
	}

	public String updateKontrola(AuthInfo auth, DTOObjekt dto) throws AppException {

		try {
			// kontrola na nazov
			if (kontrolaNaNazov(auth, dto.getObjektID(), dto.getNazov())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_101);
			}

			// kontrola na duplicitu ciselnika
			List<DTOOdberatelObjekt> ooList = getDelegate().getOdberatelObjektRead().listByObjekt(auth, dto.getObjektID());
			for (DTOOdberatelObjekt dtoOo : ooList) {
				int pocet = 0;
				String s = "";
				Set<Integer> set = getDelegate().getObjektCiselnikRead().ciselnikIds(auth, dtoOo);
				for (DTOObjektCiselnik dtoItemCis : dto.getObjektCiselnikList()) {
					if (set.contains(dtoItemCis.getIDCiselnik())) {
						s = StringUtils.isValid(s) ? ", " + dtoItemCis.getCiselnikNazov() : dtoItemCis.getCiselnikNazov();
						pocet++;
					}
				}
				if (StringUtils.isValid(s)) {
					String err = (pocet == 1) ? _CudResultUtils.ERROR_CODE_105 : _CudResultUtils.ERROR_CODE_106;
					return _CudResultUtils.returnMsg(err, dtoOo.getOdberatelNazov(), dtoOo.getTypPristupuNazov(), s);
				}
			}

			// kontrola ak nazov objektu == ExportLokaciiCRD
			if (_CudConsts.NAZOV_OBJEKT_EXPORT_LOKACII_CRD.equals(dto.getNazov())) {

				if (!StringUtils.isValid(dto.getObjektCiselnikList())) {
					String s = toString(_CudConsts.TABULKA_T_DOPRAVNY_BOD, _CudConsts.TABULKA_T_SUBSIDIARY_LOCATION, _CudConsts.TABULKA_T_PRIMARY_LOCATION);
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_107, _CudConsts.NAZOV_OBJEKT_EXPORT_LOKACII_CRD, s);
				}

				for (DTOObjektCiselnik dtoOC : dto.getObjektCiselnikList()) {
					boolean b = true;
					if (_CudConsts.TABULKA_T_DOPRAVNY_BOD.equals(dtoOC.getCiselnikTabulka())) {
						b = false;
					}
					if (_CudConsts.TABULKA_T_SUBSIDIARY_LOCATION.equals(dtoOC.getCiselnikTabulka())) {
						b = false;
					}
					if (_CudConsts.TABULKA_T_PRIMARY_LOCATION.equals(dtoOC.getCiselnikTabulka())) {
						b = false;
					}
					if (b) {
						String s = toString(_CudConsts.TABULKA_T_DOPRAVNY_BOD, _CudConsts.TABULKA_T_SUBSIDIARY_LOCATION, _CudConsts.TABULKA_T_PRIMARY_LOCATION);
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_107, _CudConsts.NAZOV_OBJEKT_EXPORT_LOKACII_CRD, s);
					}
					if (!"T".equals(dtoOC.getVsetky())) {
						return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_108, _CudConsts.NAZOV_OBJEKT_EXPORT_LOKACII_CRD);
					}
				}

				for (DTOOdberatelObjekt dtoOo : ooList) {
					if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT.equals(dtoOo.getTypPristupu())) {
						boolean b = false;
						if (!_CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE.equals(dtoOo.getOpakovanie())) {
							b = true;
						} else if (!_CudConsts.ODBERATEL_OBJEKT_EXPORT_DOVOD_ZMENA.equals(dtoOo.getExportDovod())) {
							b = true;
						} else if (!_CudConsts.ODBERATEL_OBJEKT_EXPORT_ROZSAH_ZMENENE.equals(dtoOo.getExportRozsah())) {
							b = true;
						}
						if (b) {
							return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_109, _CudConsts.NAZOV_OBJEKT_EXPORT_LOKACII_CRD,
									_CudConsts.TEXT_ODBERATEL_TYP_PRISTUPU_EXPORT);
						}
					}
				}
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	public String deleteKontrola(AuthInfo auth, Integer objektID) throws AppException {

		try {
			// kontrola na odberatela
			Integer pocet = getDelegate().getOdberatelObjektRead().objektCount(auth, objektID);
			if (pocet.intValue() != 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_104);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "deleteKontrola.error", auth);
			return null;
		}
	}

	public DTOObjekt getObjektById(AuthInfo auth, Integer cudObjektId) throws AppException {

		try {
			MyCriteria2 crit = new MyCriteria2();
			CudObjektPeer.addSelectColumns(crit);
			crit.add(CudObjektPeer.OBJEKT_ID, cudObjektId);
			crit.add(CudObjektPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();
			getConnection(auth);
			@SuppressWarnings("rawtypes")
			List lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			DTOObjekt dto = null;
			if (!lp.isEmpty()) {
				Record r = (Record) lp.get(0);
				dto = vytvor(r);
			}

			return dto;

		} catch (Throwable t) {
			handleException(t, "getObjektById.error", auth);
			return null;
		}
	}

	private DTOObjekt vytvor(Record r) throws DataSetException {
		DTOObjekt dto = new DTOObjekt();
		dto.setObjektID(rVal(r, CudObjektPeer.OBJEKT_ID).asIntegerObj());
		dto.setNazov(rVal(r, CudObjektPeer.NAZOV).asString());
		dto.setPlatny(rVal(r, CudObjektPeer.PLATNY).asString());
		dto.setSystemovy(rVal(r, CudObjektPeer.SYSTEMOVY).asString());
		dto.setSystemovyKanal(rVal(r, CudObjektPeer.SYSTEMOVY_KANAL).asString());
		dto.setSystemovyVsetkyCiselniky(rVal(r, CudObjektPeer.SYSTEMOVY_VSETKY_CISELNIKY).asString());
		dto.setSystemovyExportFormat(rVal(r, CudObjektPeer.SYSTEMOVY_EXPORT_FORMAT).asString());
		dto.setCasZmeny(rVal(r, CudObjektPeer.CAS_ZMENY).asUtilDate());
		dto.setIDUcet(rVal(r, CudObjektPeer.ID_UCET).asIntegerObj());
		return dto;
	}

}
