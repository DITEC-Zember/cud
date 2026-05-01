package sk.ditec.crd;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.crd.dto.DTOZmenaStlpecCrd;
import sk.ditec.dao.meta.CudZmenaStlpecPeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.Record;



public class ZmenaStlpecClass extends _NovyPISBaseClass {

	public String getZmenaCrd(AuthInfo auth, Integer idCiselnikStlpec, boolean zaciatok) throws AppException {

		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, CudZmenaStlpecPeer.TABLE_NAME);

			crit.addSelectColumn(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudZmenaStlpecPeer.OLD_VALUE);
			crit.addSelectColumn(CudZmenaStlpecPeer.NEW_VALUE);

			crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, idCiselnikStlpec);
			// if (zaciatok) {
			// crit.addConditional(CudZmenaStlpecPeer.NEW_VALUE,"T");
			// } else {
			// crit.addConditional(CudZmenaStlpecPeer.OLD_VALUE,"T");
			// }
			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				if (zaciatok) {
					return rVal(r, CudZmenaStlpecPeer.NEW_VALUE).asString();
				} else {
					return rVal(r, CudZmenaStlpecPeer.OLD_VALUE).asString();
				}
			}
		} catch (Throwable t) {
			handleException(t, "getZmenaCrd.error", auth);
			return null;
		}
		return null;
	}

	public DTOZmenaStlpecCrd getZmenaStlpecHodnot(AuthInfo auth, Integer idZmena, String nazov) throws AppException {

		try {
			// MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, CudZmenaStlpecPeer.TABLE_NAME);

			String sql = " select cud_zmena_stlpec.* from cud_zmena_stlpec "
					+ " join cud_ciselnik_stlpec on ( cud_ciselnik_stlpec.ciselnik_stlpec_id = cud_zmena_stlpec.id_ciselnik_stlpec "
					+ " and cud_ciselnik_stlpec.id_ciselnik = cud_zmena_stlpec.id_ciselnik ) "
					+ " where cud_ciselnik_stlpec.nazov = '" + nazov + "' " + " and  cud_zmena_stlpec.id_zmena = "
					+ idZmena;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			DTOZmenaStlpecCrd dto = new DTOZmenaStlpecCrd();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				dto.setNewValue(rVal(r, CudZmenaStlpecPeer.NEW_VALUE).asString());
				dto.setOldValue(rVal(r, CudZmenaStlpecPeer.OLD_VALUE).asString());
				dto.setNazov(nazov);
				dto.setIdZmenaStlpca(rVal(r, CudZmenaStlpecPeer.ZMENA_STLPEC_ID).asIntegerObj());
				return dto;
			}
		} catch (Throwable t) {
			handleException(t, "getZmenaStlpecHodnot.error", auth);
			return null;
		}
		return null;
	}

	public ArrayList<DTOZmenaStlpecCrd> getZmenaStlpecHodnotList(AuthInfo auth, Integer idZmena)
			throws AppException {

		try {
			// MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, CudZmenaStlpecPeer.TABLE_NAME);

			String sql = " select cud_zmena_stlpec.*, cud_ciselnik_stlpec.nazov from cud_zmena_stlpec "
					+ " join cud_ciselnik_stlpec on ( cud_ciselnik_stlpec.ciselnik_stlpec_id = cud_zmena_stlpec.id_ciselnik_stlpec "
					+ " and cud_ciselnik_stlpec.id_ciselnik = cud_zmena_stlpec.id_ciselnik ) "
					+ " where cud_zmena_stlpec.id_zmena = "
					+ idZmena;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			DTOZmenaStlpecCrd dto = new DTOZmenaStlpecCrd();
			ArrayList<DTOZmenaStlpecCrd> list = new ArrayList<DTOZmenaStlpecCrd>();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				dto.setNewValue(rVal(r, CudZmenaStlpecPeer.NEW_VALUE).asString());
				dto.setOldValue(rVal(r, CudZmenaStlpecPeer.OLD_VALUE).asString());
				dto.setNazov(rVal(r, "cud_ciselnik_stlpec.nazov").asString());
				dto.setIdZmenaStlpca(rVal(r, CudZmenaStlpecPeer.ZMENA_STLPEC_ID).asIntegerObj());
				list.add(dto);
			}
		} catch (Throwable t) {
			handleException(t, "getZmenaStlpecHodnot.error", auth);
			return null;
		}
		return null;
	}
}
