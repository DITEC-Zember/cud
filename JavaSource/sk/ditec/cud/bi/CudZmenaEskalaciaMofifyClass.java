package sk.ditec.cud.bi;

import java.util.Date;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.dao.meta.CudImportPrilohaPeer;
import sk.ditec.dao.meta.CudZmenaEskalacia;
import sk.ditec.dao.meta.CudZmenaEskalaciaPeer;

public class CudZmenaEskalaciaMofifyClass extends _CudBaseClass {

	public void update(AuthInfo auth, Integer ciselnikID, Integer zmenaID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			getConnection(auth);

			CudZmenaEskalacia dao = new CudZmenaEskalacia();
			dao.setIdCiselnik(ciselnikID);
			dao.setIdZmena(zmenaID);
			dao.setCasVytvorenia(new Date());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			returnConnection(auth);

			endTransaction(auth, true);

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
		}
	}

	public void deleteHard(AuthInfo auth, Integer[] zmenaEskalaciaIDs) throws AppException {

		try {
			if (!StringUtils.isValid(zmenaEskalaciaIDs)) {
				return;
			}

			getConnection(auth);

			// hard delete
			MyCriteria2 crit = new MyCriteria2();
			if (zmenaEskalaciaIDs.length == 1) {
				crit.add(CudZmenaEskalaciaPeer.ZMENA_ESKALACIA_ID, zmenaEskalaciaIDs[0]);
			} else {
				crit.addIn(CudZmenaEskalaciaPeer.ZMENA_ESKALACIA_ID, zmenaEskalaciaIDs);
			}

			CudImportPrilohaPeer.doDelete(crit, auth.T);

			returnConnection(auth);

		} catch (Throwable t) {
			handleException(t, "deleteHard.error", auth);
		}
	}

}
