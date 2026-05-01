package sk.ditec.cud.bi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.dto.DTOSendSubor;
import sk.ditec.cud.procvys.OdoslanieExportuOdoslanieSuboru;
import sk.ditec.cud.procvys.dto.DTOExportnySubor;
import sk.ditec.cud.utils.CudStringUtils;
import sk.ditec.dao.meta.CudSendSubor;
import sk.ditec.dao.meta.CudSendSuborPeer;

public class CudSendSuborModifyClass extends _CudBaseClass {

    private static final Logger log = LoggerFactory.getLogger(CudSendSuborModifyClass.class);

    public ActionResult update(AuthInfo auth, DTOSendSubor dto) throws AppException {

        try {
            getConnection(auth);

            CudSendSubor dao = new CudSendSubor();
            if (StringUtils.isValid(dto.getSendSuborID())) {
                dao = CudSendSuborPeer.retrieveByPK(dto.getSendSuborID(), auth.T);
            }
            dao.setSendSuborId(dto.getSendSuborID());
            dao.setIdSend(dto.getIDSend());
            dao.setIdCiselnik(dto.getIDCiselnik());
            dao.setRowIdExt(dto.getRowIdExt());
            dao.setNazovSuboru(dto.getNazovSuboru());
            if (dto.getSubor() != null) {
                dao.setSubor(dto.getSubor().getBytes());
            }
            dao.setPoradoveCislo(dto.getPoradoveCislo());
            dao.setPocetPokusov(dto.getPocetPokusov());
            dao.setCasVytvorenia(dto.getCasVytvorenia());
            dao.setCasOdoslania(dto.getCasOdoslania());
            dao.setNavratKod(dto.getNavratKod());
            dao.setNavratText(dto.getNavratText());
            dao.setErrorSprava(CudStringUtils.trunkToSize(dto.getErrorSprava(), 500));
            dao.setErrorCas(dto.getErrorCas());
            dao.setOdpovedUuid(dto.getOdpovedUuid());
            dao.setOdpovedTyp(dto.getOdpovedTyp());
            if (dto.getOdpovedSubor() != null) {
                dao.setOdpovedSubor(dto.getOdpovedSubor().getBytes());
            }
            dao.setIdTransakciaZapisane(auth.getTransakciaID());

            dao.save(auth.T);
            dto.setSendSuborID(dao.getSendSuborId());
            returnConnection(auth);

            return new ActionResult(dto);

        } catch (Throwable e) {
            handleException(e, "CudSendSuborModifyClass.update.error", auth);
            return null;
        }
    }

    public ActionResult opatovneOdoslanieSuboru(AuthInfo auth, DTOSendSubor dtoSendSubor, DTOOdberatelObjekt dtoOdberatelObjekt) throws AppException {
        ActionResult result = null;
        try {
            if (getDelegate().getCudSendSuborRead().existujeNeodoslanySubor(auth, dtoSendSubor.getIDSend())) {
                return new ActionResult("CUD-723-DZ", "V systéme existuje neodoslaná kópia daného súboru, nie je možné opätovné odoslanie.");
            }

            startTransaction(auth, "CUDdataModify");

            DTOExportnySubor exportnySubor = new DTOExportnySubor();
            exportnySubor.setSubor(dtoSendSubor.getSubor());
            exportnySubor.setIdCiselnik(dtoSendSubor.getIDCiselnik());
            exportnySubor.setNazovSuboru(dtoSendSubor.getNazovSuboru());
            exportnySubor.setPoradoveCislo(dtoSendSubor.getPoradoveCislo());
            exportnySubor.setCasVytvorenia(dtoSendSubor.getCasVytvorenia());

            // 16.2.3 cudSuborKopia
            DTOSendSubor cudSuborKopia = vytvorZaznam(auth, dtoSendSubor.getIDSend(), exportnySubor);

            // 16.2.4 Systém zavolá odoslanie súboru s exportom
            OdoslanieExportuOdoslanieSuboru odoslanieExportu = new OdoslanieExportuOdoslanieSuboru();

            // 16.2.4.1
            result = new ActionResult();
            endTransaction(auth, true);
        } catch (Throwable t) {
            handleException(t, "update.error", auth);
            return null;
        }

        return result;
    }

    public DTOSendSubor vytvorZaznam(AuthInfo auth, Integer sendId, DTOExportnySubor exportnySubor) throws AppException {
        // Systém vytvorí záznam v CUD_SEND_SUBOR, kde
        DTOSendSubor dtoSendSubor = new DTOSendSubor();
        dtoSendSubor.setIDSend(sendId);

        dtoSendSubor.setIDCiselnik(exportnySubor.getIdCiselnik());
        dtoSendSubor.setRowIdExt(exportnySubor.getRowIdExt());

        dtoSendSubor.setSubor(exportnySubor.getSubor());
        dtoSendSubor.setNazovSuboru(exportnySubor.getNazovSuboru());
        dtoSendSubor.setCasVytvorenia(exportnySubor.getCasVytvorenia());

        dtoSendSubor.setPoradoveCislo(exportnySubor.getPoradoveCislo());
        dtoSendSubor.setPocetPokusov(1);
        ActionResult result = update(auth, dtoSendSubor);
        return (DTOSendSubor) result.getResult();
    }


}
