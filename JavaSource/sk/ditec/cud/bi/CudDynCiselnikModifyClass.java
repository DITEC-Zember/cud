package sk.ditec.cud.bi;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynValue;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOSubor;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfNotif;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOWorkflow;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;

public class CudDynCiselnikModifyClass extends _CudBaseClass {

	public String update(AuthInfo auth, DTOImport dto, DTODynValue[] values, DTOCiselnikStlpecGui[] guiList) throws AppException {

		try {
			List<DTOWfDef> wfDefList = getDelegate().getWfDefRead().list(auth, dto.getIDCiselnik());

			DTOCiselnikStlpecGui dtoCSF = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByEditControl(Arrays.asList(guiList), _CudConsts.CISELNIK_STLPEC_GUI_EDIT_CONTROL_FILE);
			if (StringUtils.isValid(dtoCSF)) {
				for (DTOImportZmenaStlpec dtoIZS : dto.getImportZmenaList()[0].getImportZmenaStlpecList()) {
					if (dtoIZS.getIDCiselnikStlpec().intValue() == dtoCSF.getIDCiselnikStlpec().intValue()) {
						dtoIZS.setSubor("T");
					}
				}
			}

			DTOWorkflow dtoWf = getDelegate().getWorkflow().createDTOWorkflowIn(auth, dto.getIDCiselnik(), dto.getImportZmenaList()[0], wfDefList,
					new HashMap<Integer, DTOUcet[]>());

			ActionResult actionResult = getDelegate().getWorkflow().workflowUpdate(auth, dtoWf);

			sendNotif(auth, dto, dtoWf.getDefActDTO(), dtoWf.getWfTodoUpdateList().get(0), values, guiList);

			return messageLookup(actionResult);

		} catch (Exception t) {
			handleException(t, "update.error", auth);
			return null;
		}
	}

	public void update(AuthInfo auth, String sql) throws AppException {

		Statement stmt = null;
		ResultSet rs = null;

		try {
			getConnection(auth);
			stmt = auth.T.createStatement();
			rs = stmt.executeQuery(sql);

			cleanUp(stmt, rs);
			returnConnection(auth);

		} catch (Throwable t) {
			cleanUp(stmt, rs);
			handleException(t, "update.error", auth);
		}
	}

	private void sendNotif(AuthInfo auth, DTOImport dtoImport, DTOWfDef dtoDef, DTOWfTodo dtoTodo, DTODynValue[] values, DTOCiselnikStlpecGui[] metaPole) throws AppException {

		try {
			if (!"T".equals(dtoDef.getEmailSend())) {
				return;
			}

			DTOImportZmena dtoIZ = dtoImport.getImportZmenaList()[0];

			DTOWfNotif dtoNotif = new DTOWfNotif();
			dtoNotif.setCiselnikID(dtoImport.getIDCiselnik());
			dtoNotif.setCiselnikNazov(dtoImport.getCiselnikNazov());
			dtoNotif.setZmenaOperacia(dtoIZ.getOperacia());
			dtoNotif.setPoznamka(dtoIZ.getPoznamka());
			dtoNotif.setPlatnostOd(dtoIZ.getPlatnostOd());

			Map<String, String> rowMap = new HashMap<String, String>();
			for (int i = 0; i < metaPole.length; i++) {
				rowMap.put(metaPole[i].getCiselnikStlpecNazov(), values[i].getValueStr());
			}

			Set<Integer> ciselnikIDs = new HashSet<Integer>();
			for (DTOCiselnikStlpecGui dto : metaPole) {
				if (StringUtils.isValid(dto.getCiselnikStlpecFk1IDCiselnik())) {
					ciselnikIDs.add(dto.getCiselnikStlpecFk1IDCiselnik());
				}
			}

			Map<Integer, List<DTOCiselnikStlpecGui>> lookupMetaMap = getDelegate().getCiselnikStlpecGuiRead().mapForLookup(auth, ciselnikIDs, dtoIZ.getPlatnostOd());

			List<DTOCiselnikStlpecGui> metaList = new ArrayList<DTOCiselnikStlpecGui>(Arrays.asList(metaPole));

			List<DTOZmenaStlpec> zsList = new ArrayList<DTOZmenaStlpec>();
			for (DTOImportZmenaStlpec dtoIZS : dtoIZ.getImportZmenaStlpecList()) {

				DTOZmenaStlpec dtoNew = new DTOZmenaStlpec();
				dtoNew.setIDCiselnikStlpec(dtoIZS.getIDCiselnikStlpec());
				dtoNew.setCiselnikStlpecNazov(dtoIZS.getCiselnikStlpecNazov());
				dtoNew.setOldValue(dtoIZS.getOldValue());
				dtoNew.setNewValue(dtoIZS.getNewValue());

				DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, dtoIZS.getIDCiselnikStlpec());
				if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
					if (StringUtils.isValid(dtoNew.getOldValue())) {
						dtoNew.setOldValue(getDelegate().getDynCiselnikRead().lookupValueFormat(auth, lookupMetaMap, dtoCS.getCiselnikStlpecFk1IDCiselnik(), dtoNew.getOldValue(),
								dtoIZ.getPlatnostOd()));
					}
					if (StringUtils.isValid(dtoNew.getNewValue())) {
						dtoNew.setNewValue(getDelegate().getDynCiselnikRead().lookupValueFormat(auth, lookupMetaMap, dtoCS.getCiselnikStlpecFk1IDCiselnik(), dtoNew.getNewValue(),
								dtoIZ.getPlatnostOd()));
					}
				} else if (_CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getCiselnikStlpecDbTyp())) {
					if (StringUtils.isValid(dtoNew.getOldValue())) {
						dtoNew.setOldValue(getDelegate().getDynCiselnikRead().doubleValueFormat(dtoNew.getOldValue(), dtoCS.getDecimals()));
					}
					if (StringUtils.isValid(dtoNew.getNewValue())) {
						dtoNew.setNewValue(getDelegate().getDynCiselnikRead().doubleValueFormat(dtoNew.getNewValue(), dtoCS.getDecimals()));
					}
				} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getCiselnikStlpecDbTyp())) {
					if (StringUtils.isValid(dtoNew.getOldValue())) {
						dtoNew.setOldValue("T".equals(dtoNew.getOldValue()) ? "Áno" : "Nie");
					}
					if (StringUtils.isValid(dtoNew.getNewValue())) {
						dtoNew.setNewValue("T".equals(dtoNew.getNewValue()) ? "Áno" : "Nie");
					}
				}

				zsList.add(dtoNew);
			}

			getDelegate().getWfNotif().sendNotif(auth, dtoNotif, dtoDef, dtoTodo, metaList, zsList, rowMap);

		} catch (Exception t) {
			handleException(t, "sendNotif.error", auth);
		}
	}

	public Integer suborUpdate(AuthInfo auth, DTOSubor dto) throws AppException {

		Statement stmt = null;
		ResultSet rs = null;

		try {
			getConnection(auth);

			String sql = "SELECT " + dto.getTabulka() + "_seq.NEXTVAL FROM dual";
			stmt = auth.T.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			int suborID = rs.getInt(1);
			cleanUp(stmt, rs);

			sql = "INSERT INTO " + dto.getTabulka() + " VALUES (?,?,?,?,?)";
			PreparedStatement pstmt = auth.T.prepareStatement(sql);
			pstmt.setInt(1, suborID);
			pstmt.setString(2, dto.getNazovSuboru());
			pstmt.setBytes(3, dto.getSubor());
			pstmt.setString(4, "F");
			pstmt.setDate(5, new java.sql.Date(new Date().getTime()));
			int result = pstmt.executeUpdate();
			pstmt.close();
			if (result != 1) {
				throw new AppException("Vynimka pri zapise do tabulky " + dto.getTabulka());
			}

			returnConnection(auth);

			return suborID;

		} catch (Throwable t) {
			cleanUp(stmt, rs);
			handleException(t, "suborUpdate.error", auth);
		}
		return 0;
	}

	public void suborUpdateSpracovany(AuthInfo auth, String tabulka, Integer suborID) throws AppException {

		PreparedStatement pstm = null;

		try {
			if (!StringUtils.isValid(tabulka) || !StringUtils.isValid(suborID)) {
				return;
			}

			String sql = "update " + tabulka + " set " + _CudConsts.NAZOV_SPRACOVANY + " = \'T\' where " + tabulka.substring(2) + "_ID = ?";
			pstm = auth.T.prepareStatement(sql);
			pstm.setInt(1, suborID);
			pstm.executeUpdate();

			cleanUp(pstm, null);

		} catch (Exception e) {
			cleanUp(pstm, null);
			handleException(e, "suborUpdateSpracovany.error", auth);
		}

	}
}
