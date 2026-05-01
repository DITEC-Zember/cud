package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria.Criterion;
import org.apache.torque.util.MyCriteria2;
import org.apache.torque.util.SqlEnum;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOZmenaStavHist;
import sk.ditec.dao.meta.CudZmenaPeer;
import sk.ditec.dao.meta.CudZmenaStavHistPeer;

import com.workingdogs.village.Record;

public class CudZmenaStavHistReadClass extends _CudBaseClass {

	public Map<String, Date> map(AuthInfo auth, Integer ciselnikID, Integer zmenaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaStavHistPeer.ZMENA_STAV_HIST_ID, new DTOZmenaStavHist());

			crit.addSelectColumn(CudZmenaStavHistPeer.STAV);
			crit.addAsColumn("max_cas_vytvorenia", "max(" + CudZmenaStavHistPeer.CAS_VYTVORENIA + ")");

			crit.addConditional(CudZmenaStavHistPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudZmenaStavHistPeer.ID_ZMENA, zmenaID);

			crit.addGroupByColumn(CudZmenaStavHistPeer.STAV);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Map<String, Date> mapa = new HashMap<String, Date>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				String stav = rVal(r, CudZmenaStavHistPeer.STAV).asString();
				Date casVytvorenia = rVal(r, "max_cas_vytvorenia").asUtilDate();

				mapa.put(stav, casVytvorenia);
			}

			return mapa;

		} catch (Throwable t) {
			handleException(t, "map.error", auth);
			return null;
		}
	}

	public Integer[] ciselnikIDs(AuthInfo auth, Date platnostOd, Date platnostDo, String stav) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaStavHistPeer.ZMENA_STAV_HIST_ID, new DTOZmenaStavHist());

			crit.setDistinct();

			crit.addSelectColumn(CudZmenaStavHistPeer.ID_CISELNIK);

			if (StringUtils.isValid(platnostDo)) {
				Criterion c1 = crit.getNewCriterion(CudZmenaStavHistPeer.CAS_VYTVORENIA, platnostOd, MyCriteria2.GREATER_EQUAL);
				Criterion c2 = crit.getNewCriterion(CudZmenaStavHistPeer.CAS_VYTVORENIA, platnostDo, MyCriteria2.LESS_EQUAL);
				crit.add(c1.and(c2));

			} else {
				crit.addConditional(CudZmenaStavHistPeer.CAS_VYTVORENIA, platnostOd, MyCriteria2.GREATER_EQUAL);
			}

			crit.addConditional(CudZmenaStavHistPeer.STAV, stav, false);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudZmenaStavHistPeer.ID_CISELNIK).asIntegerObj());
			}

			return set.toArray(new Integer[set.size()]);

		} catch (Throwable t) {
			handleException(t, "ciselnikIDs.error", auth);
			return null;
		}
	}

	public List<Integer> vratZmenyKDatumuIdList(
			AuthInfo auth,
			Integer ciselnikID,
			Date casPoslExportu,
			Date datumACasNacitaniaDat,
			String stav) throws AppException {
		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaStavHistPeer.ZMENA_STAV_HIST_ID, new DTOZmenaStavHist());
			crit.addSelectColumn(CudZmenaStavHistPeer.ZMENA_STAV_HIST_ID);

			crit.add(CudZmenaStavHistPeer.STAV, stav);
			if (casPoslExportu != null) {
				crit.add(CudZmenaStavHistPeer.CAS_VYTVORENIA, casPoslExportu, MyCriteria2.GREATER_THAN);
			}

			crit.add(CudZmenaStavHistPeer.CAS_VYTVORENIA, datumACasNacitaniaDat, MyCriteria2.LESS_EQUAL);

			if (ciselnikID != null) {
				crit.addJoin(CudZmenaStavHistPeer.ID_ZMENA, CudZmenaPeer.ZMENA_ID, SqlEnum.LEFT_JOIN);
				crit.add(CudZmenaPeer.ID_CISELNIK, ciselnikID);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<Integer> result = new ArrayList<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				Integer id = rVal(r, CudZmenaStavHistPeer.ZMENA_STAV_HIST_ID).asIntegerObj();
				result.add(id);
			}

			return result;
		} catch (Throwable t) {
			handleException(t, "vratZmenyKDatumu.error", auth);
			return null;
		}
	}
}
