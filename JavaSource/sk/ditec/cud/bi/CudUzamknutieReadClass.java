package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOUzamknutie;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudUzamknutiePeer;

import com.workingdogs.village.Record;

public class CudUzamknutieReadClass extends _CudBaseClass {

	private List<DTOUzamknutie> listLight(AuthInfo auth, DTOUzamknutie dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			MyCriteria2 crit = new MyCriteria2(CudUzamknutiePeer.UZAMKNUTIE_ID, new DTOUzamknutie());

			crit.addSelectColumn(CudUzamknutiePeer.UZAMKNUTIE_ID);
			crit.addSelectColumn(CudUzamknutiePeer.ID_CISELNIK);
			crit.addSelectColumn(CudUzamknutiePeer.ROW_ID);

			crit.addConditional(CudUzamknutiePeer.UZAMKNUTIE_ID, dtoF.getUzamknutieID());
			crit.addConditional(CudUzamknutiePeer.ID_CISELNIK, dtoF.getIDCiselnik());

			if (StringUtils.isValid(dtoF.getRowID())) {
				crit.addConditional(CudUzamknutiePeer.ROW_ID, dtoF.getRowID());
			} else {
				crit.add(CudUzamknutiePeer.ROW_ID, (Object) null, MyCriteria2.ISNULL);
			}

			crit.add(CudUzamknutiePeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOUzamknutie> listDTO = new ArrayList<DTOUzamknutie>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOUzamknutie dto = new DTOUzamknutie();
				dto.setUzamknutieID(rVal(r, CudUzamknutiePeer.UZAMKNUTIE_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudUzamknutiePeer.ID_CISELNIK).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudUzamknutiePeer.ROW_ID).asIntegerObj());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOUzamknutie rowReadLight(AuthInfo auth, Integer ciselnikID, Integer rowID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikID) || !StringUtils.isValid(rowID)) {
				return null;
			}

			DTOUzamknutie dtoF = new DTOUzamknutie();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setRowID(rowID);
			List<DTOUzamknutie> listDTO = listLight(auth, dtoF);

			if (listDTO.isEmpty()) {
				return null;
			}

			return listDTO.get(0);

		} catch (Throwable t) {
			handleException(t, "rowReadLight.error", auth);
			return null;
		}
	}

	public DTOUzamknutie cisReadLight(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			if (!StringUtils.isValid(ciselnikID)) {
				return null;
			}

			DTOUzamknutie dtoF = new DTOUzamknutie();
			dtoF.setIDCiselnik(ciselnikID);
			List<DTOUzamknutie> listDTO = listLight(auth, dtoF);

			if (listDTO.isEmpty()) {
				return null;
			}

			return listDTO.get(0);

		} catch (Throwable t) {
			handleException(t, "cisReadLight.error", auth);
			return null;
		}
	}

	public String cisUpdateKontrola(AuthInfo auth, DTOUzamknutie dto) throws AppException {

		try {
			if (!StringUtils.isValid(dto) || !StringUtils.isValid(dto.getIDCiselnik())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "IDCiselnik");
			}

			if (!getDelegate().getIam().jeUcetZoSkupiny(auth, dto.getIDCiselnik(), _CudConsts.WF_DEF_TYP_SC)) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_125);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "cisUpdateKontrola.error", auth);
			return null;
		}
	}

	public String cisDeleteKontrola(AuthInfo auth, DTOUzamknutie dto) throws AppException {

		try {
			if (!StringUtils.isValid(dto) || !StringUtils.isValid(dto.getIDCiselnik())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3016, "IDCiselnik");
			}

			if (!getDelegate().getIam().jeUcetZoSkupiny(auth, dto.getIDCiselnik(), _CudConsts.WF_DEF_TYP_SC)) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_126);
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "cisDeleteKontrola.error", auth);
			return null;
		}
	}

}
