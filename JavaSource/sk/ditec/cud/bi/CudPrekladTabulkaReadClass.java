package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.cud.dto.DTOPrekladTabulka;
import sk.ditec.dao.meta.CudPrekladTabulkaPeer;

import com.workingdogs.village.Record;

public class CudPrekladTabulkaReadClass extends _CudBaseClass {

	public DTOPrekladTabulka[] listLight(AuthInfo auth) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudPrekladTabulkaPeer.PREKLAD_TABULKA_ID, new DTOPrekladTabulka());

			crit.addSelectColumn(CudPrekladTabulkaPeer.PREKLAD_TABULKA_ID);
			crit.addSelectColumn(CudPrekladTabulkaPeer.NAZOV_DB);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOPrekladTabulka> listDTO = new ArrayList<DTOPrekladTabulka>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOPrekladTabulka dto = new DTOPrekladTabulka();
				dto.setPrekladTabulkaID(rVal(r, CudPrekladTabulkaPeer.PREKLAD_TABULKA_ID).asIntegerObj());
				dto.setNazovDb(rVal(r, CudPrekladTabulkaPeer.NAZOV_DB).asString());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOPrekladTabulka[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

}
