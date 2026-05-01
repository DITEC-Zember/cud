package sk.ditec.cud.bi;

import java.util.Set;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.dao.meta.CudImportZmena;
import sk.ditec.dao.meta.CudImportZmenaPeer;

public class CudImportZmenaModifyClass extends _CudBaseClass {

	public void deleteHard(AuthInfo auth, Integer importID) throws AppException {

		try {
			Set<Integer> set = getDelegate().getImportZmenaRead().ids(auth, new Page(1, 100, "1_ASC"), importID);

			while (!set.isEmpty()) {

				getConnection(auth);

				for (Integer importZmenaID : set) {
					// hard delete
					String sql = "DELETE FROM " + CudImportZmenaPeer.TABLE_NAME + " WHERE " + CudImportZmenaPeer.IMPORT_ZMENA_ID + " = " + importZmenaID;
					BasePeer.executeStatement(sql, auth.T);
				}

				returnConnection(auth);

				set = getDelegate().getImportZmenaRead().ids(auth, new Page(1, 100, "1_ASC"), importID);
			}

		} catch (Throwable t) {
			handleException(t, "deleteHard.error", auth);
		}
	}

	public void updateSoft(AuthInfo auth, DTOImportZmena dto, Integer importID) throws AppException {

		try {
			CudImportZmena dao = null;

			if (StringUtils.isValid(dto.getImportZmenaID())) {
				dao = CudImportZmenaPeer.retrieveByPK(dto.getImportZmenaID(), auth.T);
			} else {
				dao = new CudImportZmena();
			}

			dao.setIdImport(importID);
			dao.setRowId(dto.getRowID());
			dao.setXlsRowId(dto.getXlsRowID());
			dao.setOperacia(dto.getOperacia());
			dao.setPlatnostOd(dto.getPlatnostOd());
			dao.setCasSchvaleniaGr(dto.getCasSchvaleniaGr());
			dao.setPoznamka(dto.getPoznamka());
			dao.setSpracovany(dto.getSpracovany());
			dao.setObnova(dto.getObnova());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setImportZmenaID(dao.getImportZmenaId());

		} catch (Throwable t) {
			handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOImportZmena dto, Integer importID, String importCiselnikTabulka) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			getConnection(auth);

			updateSoft(auth, dto, importID);
			getDelegate().getImportZmenaStlpecModify().update(auth, dto.getImportZmenaStlpecList(), importID, dto.getImportZmenaID(), importCiselnikTabulka);
			getDelegate().getImportMsgModify().update(auth, dto.getImportMsgList(), importID, dto.getImportZmenaID());

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public void updateSpracovany(AuthInfo auth, Integer importID, Integer importZmenaID) throws AppException {

		try {
			MyCriteria2 whereCrit = new MyCriteria2();
			whereCrit.add(CudImportZmenaPeer.ID_IMPORT, importID);
			whereCrit.add(CudImportZmenaPeer.IMPORT_ZMENA_ID, importZmenaID);

			MyCriteria2 valuesCrit = new MyCriteria2();
			valuesCrit.add(CudImportZmenaPeer.SPRACOVANY, "T");
			valuesCrit.add(CudImportZmenaPeer.ID_TRANSAKCIA_ZAPISANE, auth.getTransakciaID());

			CudImportZmenaPeer.doUpdate(whereCrit, valuesCrit, auth.T);

		} catch (Throwable t) {
			handleException(t, "updateSpracovany.error", auth);
		}
	}

}
