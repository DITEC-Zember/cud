package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.cud.dto.DTOPrekladJazyk;
import sk.ditec.dao.meta.CudPrekladJazykPeer;

import com.workingdogs.village.Record;

public class CudPrekladJazykReadClass extends _CudBaseClass {

	public DTOPrekladJazyk[] listLight(AuthInfo auth) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudPrekladJazykPeer.PREKLAD_JAZYK_ID, new DTOPrekladJazyk());

			crit.addSelectColumn(CudPrekladJazykPeer.PREKLAD_JAZYK_ID);
			crit.addSelectColumn(CudPrekladJazykPeer.KOD);
			crit.addSelectColumn(CudPrekladJazykPeer.NAZOV_SK);
			crit.addSelectColumn(CudPrekladJazykPeer.NAZOV_EN);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOPrekladJazyk> listDTO = new ArrayList<DTOPrekladJazyk>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPrekladJazyk dto = new DTOPrekladJazyk();
				dto.setPrekladJazykID(rVal(r, CudPrekladJazykPeer.PREKLAD_JAZYK_ID).asIntegerObj());
				dto.setKod(rVal(r, CudPrekladJazykPeer.KOD).asString());
				dto.setNazovSk(rVal(r, CudPrekladJazykPeer.NAZOV_SK).asString());
				dto.setNazovEn(rVal(r, CudPrekladJazykPeer.NAZOV_EN).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPrekladJazyk[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

}
