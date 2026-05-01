package sk.ditec.cud.bi;

import static sk.ditec.zsr.common.server.utils.DateUtils.formatDateDDMMYYYYHHmm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOUzamknutie;
import sk.ditec.cud.procvys.dto.DTOStlpecInValue;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudPluginPeer;
import sk.ditec.dao.meta.CudUzamknutiePeer;

import com.workingdogs.village.Record;

public class CudCiselnikReadClass extends _CudBaseClass {

	public DTOCiselnik[] listLight(AuthInfo auth, Page page, DTOCiselnik dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnik();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, dtoF);

			crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikPeer.POPIS);
			crit.addSelectColumn(CudCiselnikPeer.PRINT_CLASS);
			crit.addSelectColumn(CudCiselnikPeer.PRINT_ZAHLAVIE);
			crit.addSelectColumn(CudCiselnikPeer.AKTIVNY);
			crit.addSelectColumn(CudCiselnikPeer.PREDPIS);
			crit.addSelectColumn(CudCiselnikPeer.PRILOHA_KAPITOLA);
			crit.addSelectColumn(CudCiselnikPeer.HLAVNY);
			crit.addSelectColumn(CudCiselnikPeer.TYP);
			crit.addSelectColumn(CudCiselnikPeer.KATEGORIA);

			crit.addConditional(CudCiselnikPeer.CISELNIK_ID, dtoF.getCiselnikID());
			crit.addConditional(CudCiselnikPeer.TABULKA, dtoF.getTabulka(), true);
			crit.addConditional(CudCiselnikPeer.NAZOV, dtoF.getNazov(), true);
			crit.addConditional(CudCiselnikPeer.POPIS, dtoF.getPopis(), true);
			crit.addConditional(CudCiselnikPeer.PRINT_CLASS, dtoF.getPrintClass(), true);
			crit.addConditional(CudCiselnikPeer.PRINT_ZAHLAVIE, dtoF.getPrintZahlavie(), false);
			crit.addConditional(CudCiselnikPeer.AKTIVNY, dtoF.getAktivny(), false);
			crit.addConditional(CudCiselnikPeer.PREDPIS, dtoF.getPredpis(), true);
			crit.addConditional(CudCiselnikPeer.PRILOHA_KAPITOLA, dtoF.getPrilohaKapitola(), true);
			crit.addConditional(CudCiselnikPeer.HLAVNY, dtoF.getHlavny(), false);
			crit.addConditional(CudCiselnikPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudCiselnikPeer.KATEGORIA, dtoF.getKategoria(), false);

			crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudCiselnikPeer.CISELNIK_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOCiselnik> listDTO = new ArrayList<DTOCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnik dto = new DTOCiselnik();
				dto.setCiselnikID(rVal(r, CudCiselnikPeer.CISELNIK_ID).asIntegerObj());
				dto.setTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());
				dto.setNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());
				dto.setPopis(rVal(r, CudCiselnikPeer.POPIS).asString());
				dto.setPrintClass(rVal(r, CudCiselnikPeer.PRINT_CLASS).asString());
				dto.setPrintZahlavie(rVal(r, CudCiselnikPeer.PRINT_ZAHLAVIE).asString());
				dto.setAktivny(rVal(r, CudCiselnikPeer.AKTIVNY).asString());
				dto.setPredpis(rVal(r, CudCiselnikPeer.PREDPIS).asString());
				dto.setPrilohaKapitola(rVal(r, CudCiselnikPeer.PRILOHA_KAPITOLA).asString());
				dto.setHlavny(rVal(r, CudCiselnikPeer.HLAVNY).asString());
				dto.setTyp(rVal(r, CudCiselnikPeer.TYP).asString());
				dto.setKategoria(rVal(r, CudCiselnikPeer.KATEGORIA).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOCiselnik[] list(AuthInfo auth, Page page, DTOCiselnik dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnik();
			}

			String plgSubSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudPluginPeer.PLUGIN_ID, new DTOPlugin());

				crit.addAsColumn("plg_pocet", "count(*)");

				crit.addConditional(CudPluginPeer.ID_CISELNIK, 112233);

				crit.addConditional(CudPluginPeer.TYP, _CudConsts.PLUGIN_TYP_VALIDACNY);

				crit.addCustomSql(CudPluginPeer.PLATNOST_OD, "(" + CudPluginPeer.PLATNOST_OD + " <= " + CudPluginPeer.PLATNOST_DO + " OR " + CudPluginPeer.PLATNOST_DO + " IS NULL)");

				crit.add(CudPluginPeer.ID_TRANSAKCIA_ZRUSENE, null);

				plgSubSql = StringUtils.replaceAll(crit.getSQL(), "112233", CudCiselnikPeer.CISELNIK_ID);
			}

			String uzSubSql = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudUzamknutiePeer.UZAMKNUTIE_ID, new DTOUzamknutie());

				crit.addAsColumn("uz_pocet", "count(*)");

				crit.addConditional(CudUzamknutiePeer.ID_CISELNIK, 112233);

				crit.add(CudUzamknutiePeer.ROW_ID, (Object) null, MyCriteria2.ISNULL);

				crit.add(CudUzamknutiePeer.ID_TRANSAKCIA_ZRUSENE, null);

				uzSubSql = StringUtils.replaceAll(crit.getSQL(), "112233", CudCiselnikPeer.CISELNIK_ID);
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, dtoF);

			crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikPeer.POPIS);
			crit.addSelectColumn(CudCiselnikPeer.PRINT_CLASS);
			crit.addSelectColumn(CudCiselnikPeer.PRINT_ZAHLAVIE);
			crit.addSelectColumn(CudCiselnikPeer.AKTIVNY);
			crit.addSelectColumn(CudCiselnikPeer.PREDPIS);
			crit.addSelectColumn(CudCiselnikPeer.PRILOHA_KAPITOLA);
			crit.addSelectColumn(CudCiselnikPeer.HLAVNY);
			crit.addSelectColumn(CudCiselnikPeer.TYP);
			crit.addSelectColumn(CudCiselnikPeer.KATEGORIA);

			crit.addAsColumn("cis_uz_pocet", "(" + uzSubSql + ")");
			crit.addAsColumn("cis_plg_pocet", "(" + plgSubSql + ")");

			crit.addConditional(CudCiselnikPeer.CISELNIK_ID, dtoF.getCiselnikID());
			crit.addConditional(CudCiselnikPeer.TABULKA, dtoF.getTabulka(), true);
			crit.addConditional(CudCiselnikPeer.NAZOV, dtoF.getNazov(), true);
			crit.addConditional(CudCiselnikPeer.POPIS, dtoF.getPopis(), true);
			crit.addConditional(CudCiselnikPeer.PRINT_CLASS, dtoF.getPrintClass(), true);
			crit.addConditional(CudCiselnikPeer.PRINT_ZAHLAVIE, dtoF.getPrintZahlavie(), false);
			crit.addConditional(CudCiselnikPeer.AKTIVNY, dtoF.getAktivny(), false);
			crit.addConditional(CudCiselnikPeer.PREDPIS, dtoF.getPredpis(), true);
			crit.addConditional(CudCiselnikPeer.PRILOHA_KAPITOLA, dtoF.getPrilohaKapitola(), true);
			crit.addConditional(CudCiselnikPeer.HLAVNY, dtoF.getHlavny(), false);
			crit.addConditional(CudCiselnikPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudCiselnikPeer.KATEGORIA, dtoF.getKategoria(), false);

			crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			predVolanimDotazu(auth);
			ListPaging lp = new ListPaging(sql, page, CudCiselnikPeer.CISELNIK_ID, auth.T);
			poVolaniDotazu(auth);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOCiselnik> listDTO = new ArrayList<DTOCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnik dto = new DTOCiselnik();
				dto.setCiselnikID(rVal(r, CudCiselnikPeer.CISELNIK_ID).asIntegerObj());
				dto.setTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());
				dto.setNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());
				dto.setPopis(rVal(r, CudCiselnikPeer.POPIS).asString());
				dto.setPrintClass(rVal(r, CudCiselnikPeer.PRINT_CLASS).asString());
				dto.setPrintZahlavie(rVal(r, CudCiselnikPeer.PRINT_ZAHLAVIE).asString());
				dto.setAktivny(rVal(r, CudCiselnikPeer.AKTIVNY).asString());
				dto.setPredpis(rVal(r, CudCiselnikPeer.PREDPIS).asString());
				dto.setPrilohaKapitola(rVal(r, CudCiselnikPeer.PRILOHA_KAPITOLA).asString());
				dto.setHlavny(rVal(r, CudCiselnikPeer.HLAVNY).asString());
				dto.setTyp(rVal(r, CudCiselnikPeer.TYP).asString());
				dto.setKategoria(rVal(r, CudCiselnikPeer.KATEGORIA).asString());

				dto.setJeUzamknuty(rVal(r, "cis_uz_pocet").asIntegerObj().intValue() != 0 ? "T" : "F");
				dto.setPluginValidacia(rVal(r, "cis_plg_pocet").asIntegerObj().intValue() != 0 ? "T" : "F");

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOCiselnik loadData(AuthInfo auth, DTOCiselnik dtoF) throws AppException {

		try {
			Map<Integer, DTOCiselnik> mapa = mapLight(auth, new Integer[] { dtoF.getCiselnikID() });
			return mapa.get(dtoF.getCiselnikID());

		} catch (Throwable t) {
			handleException(t, "loadData.error", auth);
			return null;
		}
	}

	public Map<Integer, DTOCiselnik> mapLight(AuthInfo auth, Integer[] ids) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(ids)) {
				return new HashMap<Integer, DTOCiselnik>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, new DTOCiselnik());

			crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikPeer.AKTIVNY);
			crit.addSelectColumn(CudCiselnikPeer.TYP);
			crit.addSelectColumn(CudCiselnikPeer.CAS_ZMENY);
			crit.addSelectColumn(CudCiselnikPeer.ID_UCET);

			if (ids.length == 1) {
				crit.addConditional(CudCiselnikPeer.CISELNIK_ID, ids[0]);
			} else {
				crit.addIn(CudCiselnikPeer.CISELNIK_ID, ids);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, DTOCiselnik> mapDTO = new HashMap<Integer, DTOCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnik dto = new DTOCiselnik();
				dto.setCiselnikID(rVal(r, CudCiselnikPeer.CISELNIK_ID).asIntegerObj());
				dto.setTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());
				dto.setNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());
				dto.setAktivny(rVal(r, CudCiselnikPeer.AKTIVNY).asString());
				dto.setTyp(rVal(r, CudCiselnikPeer.TYP).asString());
				dto.setCasZmeny(rVal(r, CudCiselnikPeer.CAS_ZMENY).asUtilDate());
				dto.setIDUcet(rVal(r, CudCiselnikPeer.ID_UCET).asIntegerObj());

				dto.setListSize(lp.size());

				mapDTO.put(dto.getCiselnikID(), dto);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "mapLight.error", auth);
			return null;
		}
	}

	public DTOCiselnik read(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			Map<Integer, DTOCiselnik> mapa = mapLight(auth, new Integer[] { ciselnikID });
			return mapa.get(ciselnikID);

		} catch (Throwable t) {
			handleException(t, "read.error", auth);
			return null;
		}
	}

	public String updateKontrola(AuthInfo auth, DTOCiselnik dto) throws AppException {

		try {
			if (StringUtils.isValid(dto.getCiselnikID())) {
				return null;
			}

			if (_CudConsts.CISELNIK_TYP_TECHNICKY.equals(dto.getTyp())) {

				Set<String> set = getDelegate().getGuiRead().userTabsColsList(auth, dto.getTabulka());

				// kontrola ci existuje tabulka v DB
				if (set.isEmpty()) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_403);
				}

				// kontrola ci existuju vsetky technicke atributy
				if (!set.contains(_CudConsts.NAZOV_HIST_ID)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_401);
				}
				if (!set.contains(_CudConsts.NAZOV_PLATNOST_OD)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_401);
				}
				if (!set.contains(_CudConsts.NAZOV_PLATNOST_DO)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_401);
				}
				if (!set.contains(_CudConsts.NAZOV_CAS_VYTVORENIA)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_401);
				}
				if (!set.contains(_CudConsts.NAZOV_CAS_ZMENY)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_401);
				}
				if (!set.contains(_CudConsts.NAZOV_ID_ZMENA)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_401);
				}
				if (!set.contains(_CudConsts.NAZOV_ZMAZ)) {
					return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_401);
				}
			}

			DTOCiselnik dtoCis = readLight(auth, dto.getTabulka());
			if (StringUtils.isValid(dtoCis) && !dtoCis.getCiselnikID().equals(dto.getCiselnikID())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3029);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	public String deleteKontrola(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return null;
			}

			Integer pocet = getDelegate().getCiselnikGuiRead().count(auth, null, ciselnikID, null);
			if (pocet.intValue() > 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_402);
			}

			pocet = getDelegate().getZmenaRead().count(auth, ciselnikID);
			if (pocet.intValue() > 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_402);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "updateKontrola.error", auth);
			return null;
		}
	}

	public Map<Integer, String> map(AuthInfo auth, Set<Integer> set) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(set) || set.isEmpty()) {
				return new HashMap<Integer, String>();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, new DTOCiselnik());

			crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);

			if (set.size() == 1) {
				crit.addConditional(CudCiselnikPeer.CISELNIK_ID, set.iterator().next());
			} else {
				crit.addIn(CudCiselnikPeer.CISELNIK_ID, set.toArray(new Integer[set.size()]));
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<Integer, String> mapDTO = new HashMap<Integer, String>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Integer ciselnikID = rVal(r, CudCiselnikPeer.CISELNIK_ID).asIntegerObj();
				String tabulka = rVal(r, CudCiselnikPeer.TABULKA).asString();

				mapDTO.put(ciselnikID, tabulka);
			}

			return mapDTO;

		} catch (Throwable t) {
			handleException(t, "map.error", auth);
			return null;
		}
	}

	public DTOCiselnik readLight(AuthInfo auth, String tabulka) throws AppException {

		try {
			if (!StringUtils.isValid(tabulka)) {
				return null;
			}

			DTOCiselnik dtoF = new DTOCiselnik();
			dtoF.setTabulka(tabulka);

			DTOCiselnik[] listDTO = listLight(auth, dtoF);

			if (StringUtils.isValid(listDTO)) {
				return listDTO[0];
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	public DTOCiselnik readLight(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return null;
			}

			DTOCiselnik dtoF = new DTOCiselnik();
			dtoF.setCiselnikID(ciselnikID);

			DTOCiselnik[] dto = listLight(auth, dtoF);

			if (dto.length < 1)
				return new DTOCiselnik();

			return listLight(auth, dtoF)[0];

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	public DTOCiselnik[] listLight(AuthInfo auth, DTOCiselnik dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnik();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, dtoF);

			crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikPeer.POPIS);
			crit.addSelectColumn(CudCiselnikPeer.PRINT_CLASS);
			crit.addSelectColumn(CudCiselnikPeer.PRINT_ZAHLAVIE);
			crit.addSelectColumn(CudCiselnikPeer.AKTIVNY);
			crit.addSelectColumn(CudCiselnikPeer.PREDPIS);
			crit.addSelectColumn(CudCiselnikPeer.PRILOHA_KAPITOLA);
			crit.addSelectColumn(CudCiselnikPeer.HLAVNY);
			crit.addSelectColumn(CudCiselnikPeer.TYP);
			crit.addSelectColumn(CudCiselnikPeer.KATEGORIA);

			crit.addConditional(CudCiselnikPeer.CISELNIK_ID, dtoF.getCiselnikID());
			crit.addConditional(CudCiselnikPeer.TABULKA, dtoF.getTabulka(), false);
			crit.addConditional(CudCiselnikPeer.NAZOV, dtoF.getNazov(), false);
			crit.addConditional(CudCiselnikPeer.POPIS, dtoF.getPopis(), false);
			crit.addConditional(CudCiselnikPeer.PRINT_CLASS, dtoF.getPrintClass(), false);
			crit.addConditional(CudCiselnikPeer.PRINT_ZAHLAVIE, dtoF.getPrintZahlavie(), false);
			crit.addConditional(CudCiselnikPeer.AKTIVNY, dtoF.getAktivny(), false);
			crit.addConditional(CudCiselnikPeer.TYP, dtoF.getTyp(), false);
			crit.addConditional(CudCiselnikPeer.PREDPIS, dtoF.getPredpis(), false);
			crit.addConditional(CudCiselnikPeer.PRILOHA_KAPITOLA, dtoF.getPrilohaKapitola(), true);
			crit.addConditional(CudCiselnikPeer.HLAVNY, dtoF.getHlavny(), false);

			crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnik> listDTO = new ArrayList<DTOCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnik dto = new DTOCiselnik();
				dto.setCiselnikID(rVal(r, CudCiselnikPeer.CISELNIK_ID).asIntegerObj());
				dto.setTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());
				dto.setNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());
				dto.setPopis(rVal(r, CudCiselnikPeer.POPIS).asString());
				dto.setPrintClass(rVal(r, CudCiselnikPeer.PRINT_CLASS).asString());
				dto.setPrintZahlavie(rVal(r, CudCiselnikPeer.PRINT_ZAHLAVIE).asString());
				dto.setAktivny(rVal(r, CudCiselnikPeer.AKTIVNY).asString());
				dto.setPredpis(rVal(r, CudCiselnikPeer.PREDPIS).asString());
				dto.setPrilohaKapitola(rVal(r, CudCiselnikPeer.PRILOHA_KAPITOLA).asString());
				dto.setHlavny(rVal(r, CudCiselnikPeer.HLAVNY).asString());
				dto.setTyp(rVal(r, CudCiselnikPeer.TYP).asString());
				dto.setKategoria(rVal(r, CudCiselnikPeer.KATEGORIA).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOCiselnik[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public Set<Integer> ids(AuthInfo auth, DTOCiselnik dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnik();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, dtoF);

			crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);

			crit.addConditional(CudCiselnikPeer.AKTIVNY, dtoF.getAktivny(), false);
			crit.addConditional(CudCiselnikPeer.HLAVNY, dtoF.getHlavny(), false);
			crit.addConditional(CudCiselnikPeer.TYP, dtoF.getTyp(), false);

			crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> resultSet = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				resultSet.add(rVal(r, CudCiselnikPeer.CISELNIK_ID).asIntegerObj());
			}

			return resultSet;

		} catch (Throwable t) {
			handleException(t, "ids.error", auth);
			return null;
		}
	}

	public List<DTOCiselnik> ciselnikListLight(AuthInfo auth, DTOCiselnik dtoF, Integer[] ciselnikIDs) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOCiselnik();
			}

			MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, dtoF);

			crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikPeer.POPIS);
			crit.addSelectColumn(CudCiselnikPeer.AKTIVNY);

			crit.addConditional(CudCiselnikPeer.CISELNIK_ID, dtoF.getCiselnikID());
			crit.addConditional(CudCiselnikPeer.AKTIVNY, dtoF.getAktivny(), false);
			crit.addConditional(CudCiselnikPeer.TYP, dtoF.getTyp(), false);

			if (StringUtils.isValid(ciselnikIDs)) {
				if (ciselnikIDs.length == 1) {
					crit.addConditional(CudCiselnikPeer.CISELNIK_ID, ciselnikIDs[0]);
				} else {
					crit.addIn(CudCiselnikPeer.CISELNIK_ID, ciselnikIDs);
				}
			}

			crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikPeer.CISELNIK_ID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnik> listDTO = new ArrayList<DTOCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnik dto = new DTOCiselnik();
				dto.setCiselnikID(rVal(r, CudCiselnikPeer.CISELNIK_ID).asIntegerObj());
				dto.setTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());
				dto.setNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());
				dto.setPopis(rVal(r, CudCiselnikPeer.POPIS).asString());
				dto.setAktivny(rVal(r, CudCiselnikPeer.AKTIVNY).asString());

				dto.setListSize(lp.size());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "ciselnikListLight.error", auth);
			return null;
		}
	}

	public String[] vratTypyCiselnikovSHistoriouVZakladnejScheme() {
		return new String[] { _CudConsts.CISELNIK_TYP_TECHNICKY };
	}

	public List<DTOCiselnik> vratAktivneCiselniky(AuthInfo auth, String[] typCiselnikaList, Integer[] idList) throws AppException {
		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudCiselnikPeer.CISELNIK_ID, new DTOCiselnik());

			crit.addSelectColumn(CudCiselnikPeer.CISELNIK_ID);
			crit.addSelectColumn(CudCiselnikPeer.TABULKA);
			crit.addSelectColumn(CudCiselnikPeer.NAZOV);
			crit.addSelectColumn(CudCiselnikPeer.POPIS);
			crit.addSelectColumn(CudCiselnikPeer.AKTIVNY);

			crit.add(CudCiselnikPeer.AKTIVNY, "T");
			if (typCiselnikaList != null && typCiselnikaList.length > 0) {
				if (typCiselnikaList.length == 1) {
					crit.add(CudCiselnikPeer.TYP, typCiselnikaList[0]);
				} else {
					crit.addIn(CudCiselnikPeer.TYP, typCiselnikaList);
				}
			}

			if (idList != null && idList.length > 0) {
				if (idList.length == 1) {
					crit.add(CudCiselnikPeer.CISELNIK_ID, idList[0]);
				} else {
					crit.addIn(CudCiselnikPeer.CISELNIK_ID, idList);
				}
			}

			crit.add(CudCiselnikPeer.ID_TRANSAKCIA_ZRUSENE, null);

			crit.addAscendingOrderByColumn(CudCiselnikPeer.CISELNIK_ID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOCiselnik> listDTO = new ArrayList<DTOCiselnik>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOCiselnik dto = new DTOCiselnik();
				dto.setCiselnikID(rVal(r, CudCiselnikPeer.CISELNIK_ID).asIntegerObj());
				dto.setTabulka(rVal(r, CudCiselnikPeer.TABULKA).asString());
				dto.setNazov(rVal(r, CudCiselnikPeer.NAZOV).asString());
				dto.setPopis(rVal(r, CudCiselnikPeer.POPIS).asString());
				dto.setAktivny(rVal(r, CudCiselnikPeer.AKTIVNY).asString());

				dto.setListSize(lp.size());

				listDTO.add(dto);
			}

			return listDTO;
		} catch (Throwable t) {
			handleException(t, "vratAktivneCiselniky.error", auth);
			return null;
		}
	}

	public LinkedHashMap<String, List<String>> vratDataZmeneneOdDatumu(AuthInfo auth, Integer idCiselnik, Date datumZmenyOd, Date datumACasNacitaniaDat, List<DTOCiselnikStlpec> stlpecList, List<DTOStlpecInValue> stlpecInValueList, Boolean zmazane) throws AppException {

		try {
			DTOCiselnikStlpec stlpecPK = _CudLookupUtils.lookupDTOCiselnikStlpecPk(stlpecList);

			String tabulka = stlpecPK.getCiselnikTabulka();
			StringBuilder columns = new StringBuilder();
			Iterator<DTOCiselnikStlpec> iteratorStlpecList = stlpecList.iterator();
			while (iteratorStlpecList.hasNext()) {
				columns.append(iteratorStlpecList.next().getNazov());
				if (iteratorStlpecList.hasNext()) {
					columns.append(", ");
				}
			}

			StringBuilder conditionals = new StringBuilder();

			if (datumACasNacitaniaDat == null && datumZmenyOd != null) {
				conditionals.append("('").append(formatDateDDMMYYYYHHmm(datumZmenyOd)).append("' < ").append(_CudConsts.NAZOV_CAS_VYTVORENIA).append(" OR ");
				conditionals.append("'").append(formatDateDDMMYYYYHHmm(datumZmenyOd)).append("' < ").append(_CudConsts.NAZOV_CAS_ZMENY).append(")");
			} else if (datumACasNacitaniaDat != null && datumZmenyOd == null) {
				conditionals.append("(").append(_CudConsts.NAZOV_CAS_VYTVORENIA).append(" < '").append(formatDateDDMMYYYYHHmm(datumACasNacitaniaDat)).append("' OR ");
				conditionals.append(_CudConsts.NAZOV_CAS_ZMENY).append(" < '").append(formatDateDDMMYYYYHHmm(datumACasNacitaniaDat)).append("')");
			} else if (datumACasNacitaniaDat != null && datumZmenyOd != null) {
				conditionals.append("(('").append(formatDateDDMMYYYYHHmm(datumZmenyOd)).append("' < ").append(_CudConsts.NAZOV_CAS_VYTVORENIA).append(" AND ");
				conditionals.append(_CudConsts.NAZOV_CAS_VYTVORENIA).append(" <= '").append(formatDateDDMMYYYYHHmm(datumACasNacitaniaDat)).append("')");
				conditionals.append(" OR ");
				conditionals.append("('").append(formatDateDDMMYYYYHHmm(datumZmenyOd)).append("' < ").append(_CudConsts.NAZOV_CAS_ZMENY).append(" AND ");
				conditionals.append(_CudConsts.NAZOV_CAS_ZMENY).append(" <= '").append(formatDateDDMMYYYYHHmm(datumACasNacitaniaDat)).append("'))");
			}

			if (zmazane == null || zmazane.equals(Boolean.FALSE)) {
				conditionals.append(" AND ").append(_CudConsts.NAZOV_ZMAZ).append(" = 'F'");
			}

			if (stlpecInValueList != null && !stlpecInValueList.isEmpty()) {
				for (DTOStlpecInValue stlpecInValue : stlpecInValueList) {
					conditionals.append(" AND ").append(stlpecInValue.getNazov()).append(" IN (");
					Iterator<String> iteratorStlpecInValue = stlpecInValue.getHodnoty().iterator();
					while (iteratorStlpecInValue.hasNext()) {
						conditionals.append(konvertujPodlaDbTyp(stlpecInValue.getDbTyp(), iteratorStlpecInValue.next()));
						if (iteratorStlpecInValue.hasNext()) {
							conditionals.append(", ");
						} else {
							conditionals.append(")");
						}
					}
				}
			}

			String tabulkaNazov = stlpecPK.getCiselnikTabulka();
			String sort = tabulkaNazov + "." + _CudConsts.NAZOV_PLATNOST_OD + ", " + tabulkaNazov + "." + _CudConsts.NAZOV_ID_ZMENA;

			String sql = "SELECT " + columns + " FROM " + tabulka + " WHERE " + conditionals + " ORDER BY " + sort;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			LinkedHashMap<String, List<String>> result = new LinkedHashMap<String, List<String>>();
			for (Object o : lp) {
				Record r = (Record) o;

				for (DTOCiselnikStlpec column : stlpecList) {
					String value = rVal(r, tabulka + "." + column.getNazov()).asString();

					List<String> dtoDynValues = result.get(column.getNazov());
					if (dtoDynValues == null) {
						dtoDynValues = new ArrayList<String>();
						result.put(column.getNazov(), dtoDynValues);
					}
					dtoDynValues.add(value);
				}
			}

			return result;
		} catch (Throwable t) {
			handleException(t, "zmenaStlpecMap.error", auth);
			return null;
		}
	}

	public LinkedHashMap<String, List<String>> vratDataCiselnika(AuthInfo auth, Integer objektCiselnikId, List<DTOCiselnikStlpec> stlpecList, List<DTOStlpecInValue> stlpecInValueList, Boolean zmazane) throws AppException {
		try {
			DTOCiselnikStlpec stlpecPK = _CudLookupUtils.lookupDTOCiselnikStlpecPk(stlpecList);

			String tabulka = stlpecPK.getCiselnikTabulka();
			StringBuilder columns = new StringBuilder();
			Iterator<DTOCiselnikStlpec> iteratorStlpecList = stlpecList.iterator();
			while (iteratorStlpecList.hasNext()) {
				columns.append(iteratorStlpecList.next().getNazov());
				if (iteratorStlpecList.hasNext()) {
					columns.append(", ");
				}
			}

			StringBuilder conditionals = new StringBuilder();

			if (stlpecInValueList != null && !stlpecInValueList.isEmpty()) {
				for (DTOStlpecInValue stlpecInValue : stlpecInValueList) {
					conditionals.append(" AND ");
					conditionals.append(stlpecInValue.getNazov()).append(" IN (");
					Iterator<String> iteratorStlpecInValue = stlpecInValue.getHodnoty().iterator();
					while (iteratorStlpecInValue.hasNext()) {
						conditionals.append(konvertujPodlaDbTyp(stlpecInValue.getDbTyp(), iteratorStlpecInValue.next()));
						if (iteratorStlpecInValue.hasNext()) {
							conditionals.append(", ");
						} else {
							conditionals.append(")");
						}
					}
				}
			}

			if (zmazane == null || zmazane.equals(Boolean.FALSE)) {
				if (!conditionals.toString().isEmpty()) {
					conditionals.append(" AND ");
				}
				conditionals.append(_CudConsts.NAZOV_ZMAZ).append(" = 'F'");
			}

			String sort = _CudConsts.NAZOV_PLATNOST_OD + ", " + _CudConsts.NAZOV_ID_ZMENA;
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ").append(columns);
			sql.append(" FROM ").append(tabulka);
			if (!conditionals.toString().isEmpty()) {
				sql.append(" WHERE ").append(conditionals);
			}
			sql.append(" ORDER BY ").append(sort);

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql.toString(), false, auth.T);
			returnConnection(auth);

			LinkedHashMap<String, List<String>> result = new LinkedHashMap<String, List<String>>();
			for (Object o : lp) {
				Record r = (Record) o;

				for (DTOCiselnikStlpec column : stlpecList) {
					String value = rVal(r, tabulka + "." + column.getNazov()).asString();

					List<String> dtoDynValues = result.get(column.getNazov());
					if (dtoDynValues == null) {
						dtoDynValues = new ArrayList<String>();
						result.put(column.getNazov(), dtoDynValues);
					}
					dtoDynValues.add(value);
				}
			}

			return result;
		} catch (Throwable t) {
			handleException(t, "vratDataCiselnika.error");
			return null;
		}
	}

	private String konvertujPodlaDbTyp(String dbTyp, String value) {
		// booleans are represented as Strings
		if (dbTyp == null || dbTyp.equals(_CudConsts.DB_TYP_STRING) || dbTyp.equals(_CudConsts.DB_TYP_BOOLEAN))
			return value;

		if (dbTyp.equals(_CudConsts.DB_TYP_INTEGER) || dbTyp.equals(_CudConsts.DB_TYP_DOUBLE)) {
			return "to_number(" + value + ")";
		} else if (dbTyp.equals(_CudConsts.DB_TYP_DATE)) {
			return "to_date(" + value + ")";
		}

		return value;
	}

	private String formatDateToTimestamp(Date d) throws AppException {

		try {
			if (!StringUtils.isValid(d)) {
				return null;
			}
			return " to_timestamp(\'" + sk.ditec.zsr.common.server.utils.DateUtils.formatDateDDMMYYYY(d) + "\', \'DD.MM.YYYY\')";

		} catch (Throwable t) {
			DBUtils.handleException(t, "formatDateToTimestamp.error");
			return null;
		}
	}

}
