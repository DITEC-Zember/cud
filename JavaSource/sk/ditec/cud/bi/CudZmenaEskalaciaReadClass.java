package sk.ditec.cud.bi;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudWfDefPeer;
import sk.ditec.dao.meta.CudWfTodoPeer;
import sk.ditec.dao.meta.CudZmenaEskalaciaPeer;

import com.workingdogs.village.Record;

public class CudZmenaEskalaciaReadClass extends _CudBaseClass {

	public Set<Integer> zmenaEskalaciaIDs(AuthInfo auth) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			String subSql1 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfDefPeer.WF_DEF_ID, new DTOWfDef());

				crit.addSelectColumn(CudWfDefPeer.WF_DEF_ID);

				crit.addConditional(CudWfDefPeer.TYP, _CudConsts.WF_DEF_TYP_OV, false);

				crit.add(CudWfDefPeer.ID_TRANSAKCIA_ZRUSENE, null);

				subSql1 = crit.getSQL();
			}

			String subSql2 = null;
			{
				MyCriteria2 crit = new MyCriteria2(CudWfTodoPeer.WF_TODO_ID, new DTOWfTodo());

				crit.addSelectColumn(CudWfTodoPeer.ID_ZMENA);

				crit.add(CudWfTodoPeer.POTVRDENY, null);

				crit.addCustomSql(CudWfTodoPeer.ID_WF_DEF, CudWfTodoPeer.ID_WF_DEF + " IN (" + subSql1 + ")");

				subSql2 = crit.getSQL();
			}

			MyCriteria2 crit = new MyCriteria2(CudZmenaEskalaciaPeer.ZMENA_ESKALACIA_ID, new DTOZmena());

			crit.addSelectColumn(CudZmenaEskalaciaPeer.ZMENA_ESKALACIA_ID);

			crit.addCustomSql(CudZmenaEskalaciaPeer.ID_ZMENA, CudZmenaEskalaciaPeer.ID_ZMENA + " IN (" + subSql2 + ")");

			String sql = "SELECT * FROM (" + crit.getSQL() + " ) WHERE rownum <= 100";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			Set<Integer> set = new HashSet<Integer>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				set.add(rVal(r, CudZmenaEskalaciaPeer.ZMENA_ESKALACIA_ID).asIntegerObj());
			}

			return set;

		} catch (Throwable t) {
			handleException(t, "zmenaEskalaciaIDs.error", auth);
			return null;
		}
	}

}
