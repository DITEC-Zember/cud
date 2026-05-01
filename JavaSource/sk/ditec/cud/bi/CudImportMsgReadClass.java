package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.bi.Page;
import sk.ditec.common.paging.ListPaging;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.dao.meta.CudImportMsgPeer;

import com.workingdogs.village.Record;

public class CudImportMsgReadClass extends _CudBaseClass {

	public DTOImportMsg[] list(AuthInfo auth, Page page, DTOImportMsg dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOImportMsg();
			}

			MyCriteria2 crit = new MyCriteria2(CudImportMsgPeer.IMPORT_MSG_ID, dtoF);

			crit.addSelectColumn(CudImportMsgPeer.IMPORT_MSG_ID);
			crit.addSelectColumn(CudImportMsgPeer.ID_IMPORT);
			crit.addSelectColumn(CudImportMsgPeer.ID_IMPORT_ZMENA);
			crit.addSelectColumn(CudImportMsgPeer.TYP);
			crit.addSelectColumn(CudImportMsgPeer.MSG);

			crit.addConditional(CudImportMsgPeer.IMPORT_MSG_ID, dtoF.getImportMsgID());
			crit.addConditional(CudImportMsgPeer.ID_IMPORT, dtoF.getIDImport());
			crit.addConditional(CudImportMsgPeer.ID_IMPORT_ZMENA, dtoF.getIDImportZmena());
			crit.addConditional(CudImportMsgPeer.TYP, dtoF.getTyp(), false);

			if (!StringUtils.isValid(dtoF.getIDImportZmena())) {
				crit.add(CudImportMsgPeer.ID_IMPORT_ZMENA, (Object) null, MyCriteria2.ISNULL);
			}

			String sql = crit.getSQL();

			getConnection(auth);
			ListPaging lp = new ListPaging(sql, page, CudImportMsgPeer.IMPORT_MSG_ID, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.result.iterator();
			List<DTOImportMsg> listDTO = new ArrayList<DTOImportMsg>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOImportMsg dto = new DTOImportMsg();
				dto.setImportMsgID(rVal(r, CudImportMsgPeer.IMPORT_MSG_ID).asIntegerObj());
				dto.setIDImport(rVal(r, CudImportMsgPeer.ID_IMPORT).asIntegerObj());
				dto.setIDImportZmena(rVal(r, CudImportMsgPeer.ID_IMPORT_ZMENA).asIntegerObj());
				dto.setTyp(rVal(r, CudImportMsgPeer.TYP).asString());
				dto.setMsg(rVal(r, CudImportMsgPeer.MSG).asString());

				dto.setListSize(lp.total_count);

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOImportMsg[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "importMsgList.error", auth);
			return null;
		}
	}

}
