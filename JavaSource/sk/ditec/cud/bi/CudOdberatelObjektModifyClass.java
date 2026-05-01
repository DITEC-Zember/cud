package sk.ditec.cud.bi;

import static sk.ditec.cud.procvys.CudDzOdoslanieExportuProcess.INTERVAL_3_MESIACE;
import static sk.ditec.cud.procvys.CudDzOdoslanieExportuProcess.INTERVAL_DEN;
import static sk.ditec.cud.procvys.CudDzOdoslanieExportuProcess.INTERVAL_MESIAC;
import static sk.ditec.cud.procvys.CudDzOdoslanieExportuProcess.INTERVAL_TYZDEN;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_DENNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_MESACNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_STVRTROCNE;
import static sk.ditec.cud.utils._CudConsts.ODBERATEL_OBJEKT_OPAKOVANIE_TYZDENNE;

import java.security.spec.KeySpec;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.Base64;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.meta.CudOdberatelObjekt;
import sk.ditec.dao.meta.CudOdberatelObjektPeer;

public class CudOdberatelObjektModifyClass extends _CudBaseClass {

	private String encrypt(String passwd) throws AppException {

		try {
			if (!StringUtils.isValid(passwd)) {
				return null;
			}

			KeySpec ks = new DESedeKeySpec(_CudConsts.PASSWD_ENCRYPTION_KEY.getBytes("UTF8"));
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(_CudConsts.PASSWD_ENCRYPTION_SCHEMA);
			Cipher cipher = Cipher.getInstance(_CudConsts.PASSWD_ENCRYPTION_SCHEMA);
			SecretKey secretKey = secretKeyFactory.generateSecret(ks);

			byte[] plainTextByte = passwd.getBytes("UTF8");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encryptedByte = cipher.doFinal(plainTextByte);
			return Base64.encodeToString(encryptedByte, false);

		} catch (Throwable t) {
			DBUtils.handleException(t, "encrypt.error");
			return null;
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOOdberatelObjekt dto, Date d) throws AppException {

		try {
			CudOdberatelObjekt dao = null;

			if (StringUtils.isValid(dto.getOdberatelObjektID())) {
				dao = CudOdberatelObjektPeer.retrieveByPK(dto.getOdberatelObjektID(), auth.T);
			} else {
				dao = new CudOdberatelObjekt();
			}

			// dao.setOdberatelObjektId(dto.getOdberatelObjektID()); //OdberatelObjektID
			dao.setIdOdberatel(dto.getIDOdberatel());
			dao.setIdObjekt(dto.getIDObjekt());
			dao.setPlatnostOd(dto.getPlatnostOd());
			dao.setPlatnostDo(dto.getPlatnostDo());
			dao.setTypPristupu(dto.getTypPristupu());
			dao.setOpakovanie(dto.getOpakovanie());
			dao.setExportDovod(dto.getExportDovod());
			dao.setExportRozsah(dto.getExportRozsah());
			dao.setExportFormat(dto.getExportFormat());
			dao.setExportTypPodlaOdberatela(dto.getExportTypPodlaOdberatela());
			dao.setExportTyp(dto.getExportTyp());
			dao.setExportCesta(dto.getExportCesta());
			dao.setVsetkyCiselniky(dto.getVsetkyCiselniky());
			dao.setCasPoslExportuZmena(dto.getCasPoslExportuZmena());
			dao.setCasPoslExportu(dto.getCasPoslExportu());
			dao.setCasPoslExportuPlan(dto.getCasPoslExportuPlan());
			dao.setAktivny(dto.getAktivny());
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			if ("T".equals(dto.getJeAdmin())) {
				dao.setLogin(dto.getLogin());
				dao.setHeslo(encrypt(dto.getHeslo()));
			}

			dao.save(auth.T);

			dto.setOdberatelObjektID(dao.getOdberatelObjektId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public String update(AuthInfo auth, DTOOdberatelObjekt dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			updateSoft(auth, dto, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public String deleteSoft(AuthInfo auth, Integer odberatelObjektID, Date d) throws AppException {

		try {
			// delete soft
			CudOdberatelObjekt dao = CudOdberatelObjektPeer.retrieveByPK(odberatelObjektID);
			dao.setCasZmeny(d);
			dao.setIdUcet(auth.getAccountId());
			dao.setIdTransakciaZrusene(auth.getTransakciaID());

			dao.save(auth.T);

			return null;

		} catch (Throwable t) {
			handleException(t, "deleteSoft.error", auth);
			return null;
		}
	}

	public String delete(AuthInfo auth, Integer odberatelObjektID) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			deleteSoft(auth, odberatelObjektID, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return null;

		} catch (Throwable t) {
			handleException(t, "delete.error", auth);
			return null;
		}
	}

	public void nastavPoslednyPlanovanyExport(AuthInfo auth, DTOOdberatelObjekt cudOdberatelObjekt) throws AppException {
		if (cudOdberatelObjekt.getCasPoslExportuPlan() == null) {
			cudOdberatelObjekt.setCasPoslExportuPlan(cudOdberatelObjekt.getPlatnostOd());
		}

		long interval = 0;

		if (ODBERATEL_OBJEKT_OPAKOVANIE_DENNE.equals(cudOdberatelObjekt.getOpakovanie())) {
			interval = INTERVAL_DEN;
		} else if (ODBERATEL_OBJEKT_OPAKOVANIE_TYZDENNE.equals(cudOdberatelObjekt.getOpakovanie())) {
			interval = INTERVAL_TYZDEN;
		} else if (ODBERATEL_OBJEKT_OPAKOVANIE_MESACNE.equals(cudOdberatelObjekt.getOpakovanie())) {
			interval = INTERVAL_MESIAC;
		} else if (ODBERATEL_OBJEKT_OPAKOVANIE_STVRTROCNE.equals(cudOdberatelObjekt.getOpakovanie())) {
			interval = INTERVAL_3_MESIACE;
		} else if (ODBERATEL_OBJEKT_OPAKOVANIE_PRI_ZMENE.equals(cudOdberatelObjekt.getOpakovanie())) {
			cudOdberatelObjekt.setCasPoslExportuPlan(new Date(cudOdberatelObjekt.getCasPoslExportu().getTime()));
		}

		if (interval != 0) {
			while ((cudOdberatelObjekt.getCasPoslExportuPlan().getTime() + interval) <= cudOdberatelObjekt.getCasPoslExportu().getTime()) {
				cudOdberatelObjekt.setCasPoslExportuPlan(new Date(cudOdberatelObjekt.getCasPoslExportuPlan().getTime() + interval));
			}
		}
		update(auth, cudOdberatelObjekt);
	}
}
