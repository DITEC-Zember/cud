package sk.ditec.cud.bi;

import java.nio.charset.Charset;
import java.util.Set;

import org.apache.torque.util.BasePeer;

import sk.ditec.common.bi.Page;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOImportZmenaStlpecPriloha;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudImportZmenaStlpec;
import sk.ditec.dao.meta.CudImportZmenaStlpecPeer;
import sk.ditec.dao.meta.CudImportZmenaStlpecPriloha;
import sk.ditec.dao.meta.CudImportZmenaStlpecPrilohaPeer;
import sk.ditec.dao.meta.CudPrekladPeer;

public class CudImportZmenaStlpecModifyClass extends _CudBaseClass {

	private void prilohaDeleteHard(AuthInfo auth, Integer importID) throws AppException {

		try {
			Set<Integer> set = getDelegate().getImportZmenaStlpecRead().prilohaIds(auth, new Page(1, 100, "1_ASC"), importID);

			while (!set.isEmpty()) {

				getConnection(auth);

				for (Integer importZmenaStlpecPrilohaID : set) {
					// hard delete
					String sql = "DELETE FROM " + CudImportZmenaStlpecPrilohaPeer.TABLE_NAME + " WHERE " + CudImportZmenaStlpecPrilohaPeer.IMPORT_ZMENA_STLPEC_PRILOHA_ID + " = "
							+ importZmenaStlpecPrilohaID;
					BasePeer.executeStatement(sql, auth.T);
				}

				returnConnection(auth);

				set = getDelegate().getImportZmenaStlpecRead().prilohaIds(auth, new Page(1, 100, "1_ASC"), importID);
			}

		} catch (Throwable t) {
			handleException(t, "prilohaDeleteHard.error", auth);
		}
	}

	private void masterDeleteHard(AuthInfo auth, Integer importID) throws AppException {

		try {
			Set<Integer> set = getDelegate().getImportZmenaStlpecRead().ids(auth, new Page(1, 100, "1_ASC"), importID);

			while (!set.isEmpty()) {

				getConnection(auth);

				for (Integer importZmenaStlpecID : set) {
					// hard delete
					String sql = "DELETE FROM " + CudImportZmenaStlpecPeer.TABLE_NAME + " WHERE " + CudImportZmenaStlpecPeer.IMPORT_ZMENA_STLPEC_ID + " = " + importZmenaStlpecID;
					BasePeer.executeStatement(sql, auth.T);
				}

				returnConnection(auth);

				set = getDelegate().getImportZmenaStlpecRead().ids(auth, new Page(1, 100, "1_ASC"), importID);
			}

		} catch (Throwable t) {
			handleException(t, "masterDeleteHard.error", auth);
		}
	}

	public void deleteHard(AuthInfo auth, Integer importID) throws AppException {

		try {
			prilohaDeleteHard(auth, importID);
			masterDeleteHard(auth, importID);

		} catch (Throwable t) {
			handleException(t, "deleteHard.error", auth);
		}
	}

	private void updateSoft(AuthInfo auth, DTOImportZmenaStlpecPriloha dto) throws AppException {

		try {
			CudImportZmenaStlpecPriloha dao = null;

			if (StringUtils.isValid(dto.getImportZmenaStlpecPrilohaID())) {
				dao = CudImportZmenaStlpecPrilohaPeer.retrieveByPK(dto.getImportZmenaStlpecPrilohaID(), auth.T);
			} else {
				dao = new CudImportZmenaStlpecPriloha();
			}

			dao.setIdImport(dto.getIDImport());
			dao.setIdImportZmena(dto.getIDImportZmena());
			dao.setOldValuePriloha(StringUtils.isValid(dto.getOldValuePriloha()) ? dto.getOldValuePriloha().getBytes(Charset.forName("UTF-8")) : null);
			dao.setNewValuePriloha(StringUtils.isValid(dto.getNewValuePriloha()) ? dto.getNewValuePriloha().getBytes(Charset.forName("UTF-8")) : null);
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setImportZmenaStlpecPrilohaID(dao.getImportZmenaStlpecPrilohaId());

		} catch (Throwable t) {
			handleException(t, "updateSoft.error", auth);
		}
	}

	private void updateSoft(AuthInfo auth, DTOImportZmenaStlpec dto) throws AppException {

		try {
			CudImportZmenaStlpec dao = null;

			if (StringUtils.isValid(dto.getImportZmenaStlpecID())) {
				dao = CudImportZmenaStlpecPeer.retrieveByPK(dto.getImportZmenaStlpecID(), auth.T);
			} else {
				dao = new CudImportZmenaStlpec();
			}

			dao.setIdImport(dto.getIDImport());
			dao.setIdImportZmena(dto.getIDImportZmena());
			dao.setIdCiselnikStlpec(dto.getIDCiselnikStlpec());
			dao.setIdImportZmenaStlpecPriloha(dto.getIDImportZmenaStlpecPriloha());
			dao.setCiselnikStlpecNazov(dto.getCiselnikStlpecNazov());
			dao.setOldValue(dto.getOldValue());
			dao.setNewValue(dto.getNewValue());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setImportZmenaStlpecID(dao.getImportZmenaStlpecId());

		} catch (Throwable t) {
			handleException(t, "updateSoft.error", auth);
		}
	}

	public void update(AuthInfo auth, DTOImportZmenaStlpec[] pole, Integer importID, Integer importZmenaID, String importCiselnikTabulka) throws AppException {

		try {
			if (StringUtils.isValid(pole)) {
				for (DTOImportZmenaStlpec dto : pole) {

					Integer IDImportZmenaStlpecPriloha = null;
					boolean b1 = CudPrekladPeer.TABLE_NAME.equals(importCiselnikTabulka) && trimColumnName(CudPrekladPeer.PREKLAD).equals(dto.getCiselnikStlpecNazov());
					boolean b2 = StringUtils.isValid(dto.getOldValue()) && (dto.getOldValue().length() > _CudConsts.MAX_LENGTH_STRING);
					boolean b3 = StringUtils.isValid(dto.getNewValue()) && (dto.getNewValue().length() > _CudConsts.MAX_LENGTH_STRING);
					if (b1 || b2 || b3) {
						DTOImportZmenaStlpecPriloha dtoPriloha = new DTOImportZmenaStlpecPriloha();
						dtoPriloha.setImportZmenaStlpecPrilohaID(dto.getIDImportZmenaStlpecPriloha());
						dtoPriloha.setIDImport(importID);
						dtoPriloha.setIDImportZmena(importZmenaID);
						dtoPriloha.setOldValuePriloha(dto.getOldValue());
						dtoPriloha.setNewValuePriloha(dto.getNewValue());
						updateSoft(auth, dtoPriloha);
						IDImportZmenaStlpecPriloha = dtoPriloha.getImportZmenaStlpecPrilohaID();
						dto.setOldValue(null);
						dto.setNewValue(null);
					}

					dto.setIDImport(importID);
					dto.setIDImportZmena(importZmenaID);
					dto.setIDImportZmenaStlpecPriloha(IDImportZmenaStlpecPriloha);
					updateSoft(auth, dto);
				}
			}

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
		}
	}

}
