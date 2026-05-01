package sk.ditec.crd;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.dao.meta.CudParametre;
import sk.ditec.dao.meta.CudParametrePeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.Record;



public class CudParametreClass extends _NovyPISBaseClass {

	public String getValue(AuthInfo auth, String name)
			throws AppException {
		try {
			
			MyCriteria2 crit = new MyCriteria2(CudParametrePeer.PARAMETRE_NAME);
			crit.addConditional(CudParametrePeer.PARAMETRE_NAME, name);
			crit.addSelectColumn(CudParametrePeer.PARAMETRE_VALUE);

	String sql = crit.getSQL();

		getConnection(auth);
	List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
	returnConnection(auth);
	Iterator<?> iter = lp.iterator();

	while (iter.hasNext()) {

		Record r = (Record) iter.next();
				String value = rVal(r, CudParametrePeer.PARAMETRE_VALUE).asString();
				return value;
	}
			return null;

} catch (Throwable t) {
			handleException(t, "CudParametreClass.getValue.error", auth);
	return null;
}

}

	public ActionResult update(AuthInfo auth, String name, String value) throws AppException {

		try {
			if (name == null) {
				return null;
			}


			MyCriteria2 crit = new MyCriteria2(CudParametrePeer.PARAMETRE_ID);
			CudParametrePeer.addSelectColumns(crit);

			crit.addConditional(CudParametrePeer.PARAMETRE_NAME, name, Criteria.EQUAL);
			// crit.addConditional(CudParametrePeer.PARAMETRE_ID, id, Criteria.EQUAL);

			String sql = crit.getSQL();
			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);

			Iterator<?> iter = lp.iterator();

			CudParametre dao = new CudParametre();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				// dao.setNew(false);
				int id = rVal(r, CudParametrePeer.PARAMETRE_ID).asIntegerObj();
				dao = CudParametrePeer.retrieveByPK(id, auth.T);
				// dao.setParametreName(rVal(r, CudParametrePeer.PARAMETRE_ID).asString());
			}


			// sql = "select max(" + CudParametrePeer.PARAMETRE_ID + " ) as ID from " + CudParametrePeer.TABLE_NAME;
			// lp = BasePeer.executeQuery(sql, true, auth.T);
			// // Record result = (Record) CudParametrePeer.doSelectVillageRecords(crit).get(0);
			// Record r = (Record) lp.iterator().next();
			// Integer id = rVal(r, "ID").asIntegerObj();
			// if (id != null) {
			// dao = CudParametrePeer.retrieveByPK(id, auth.T);
			// } else {
			// dao.setParametreId(1);
			// }

			dao.setParametreName(name);
			dao.setParametreValue(value);
			dao.save(auth.T);

			returnConnection(auth);
			return null;
		} catch (Throwable e) {
			handleException(e, "CudParametreClass.update.error", auth);
			return null;
		}
	}

}
