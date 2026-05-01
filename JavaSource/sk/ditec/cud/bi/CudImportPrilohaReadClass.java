package sk.ditec.cud.bi;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.read.biff.WorkbookParser;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportPriloha;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.dao.meta.CudImportPrilohaPeer;

import com.workingdogs.village.Record;

public class CudImportPrilohaReadClass extends _CudBaseClass {

	private DTOImportPriloha[] listLight(AuthInfo auth, DTOImportPriloha dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOImportPriloha();
			}

			MyCriteria2 crit = new MyCriteria2(CudImportPrilohaPeer.IMPORT_PRILOHA_ID, dtoF);

			crit.addSelectColumn(CudImportPrilohaPeer.IMPORT_PRILOHA_ID);
			crit.addSelectColumn(CudImportPrilohaPeer.ID_IMPORT);
			crit.addSelectColumn(CudImportPrilohaPeer.FILE_NAME);
			crit.addSelectColumn(CudImportPrilohaPeer.PRILOHA);

			crit.addConditional(CudImportPrilohaPeer.IMPORT_PRILOHA_ID, dtoF.getImportPrilohaID());
			crit.addConditional(CudImportPrilohaPeer.ID_IMPORT, dtoF.getIDImport());
			crit.addConditional(CudImportPrilohaPeer.FILE_NAME, dtoF.getFileName(), false);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			List<DTOImportPriloha> listDTO = new ArrayList<DTOImportPriloha>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOImportPriloha dto = new DTOImportPriloha();
				dto.setImportPrilohaID(rVal(r, CudImportPrilohaPeer.IMPORT_PRILOHA_ID).asIntegerObj());
				dto.setIDImport(rVal(r, CudImportPrilohaPeer.ID_IMPORT).asIntegerObj());
				dto.setFileName(rVal(r, CudImportPrilohaPeer.FILE_NAME).asString());
				dto.setPriloha(rVal(r, CudImportPrilohaPeer.PRILOHA).asBytes());

				listDTO.add(dto);
			}

			return listDTO.toArray(new DTOImportPriloha[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "listLight.error", auth);
			return null;
		}
	}

	public DTOImportPriloha readLight(AuthInfo auth, Integer importID) throws AppException {

		try {
			DTOImportPriloha dtoF = new DTOImportPriloha();
			dtoF.setIDImport(importID);

			DTOImportPriloha[] listDTO = listLight(auth, dtoF);

			return StringUtils.isValid(listDTO) ? listDTO[0] : null;

		} catch (Throwable t) {
			handleException(t, "readLight.error", auth);
			return null;
		}
	}

	private List<DTOCiselnikStlpecGui> listForForm(AuthInfo auth, Integer ciselnikID, Date planostOd, String operacia) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			DTOCiselnikStlpecGui[] listDTO = getDelegate().getCiselnikStlpecGuiRead().listForForm(auth, ciselnikID, planostOd);

			for (DTOCiselnikStlpecGui dto : listDTO) {
				if (_CudConsts.ZMENA_OPERACIA_U.equals(operacia) || _CudConsts.ZMENA_OPERACIA_Z.equals(operacia)) {
					dto.setPovinny(_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getCiselnikStlpecTyp()) ? dto.getPovinny() : "F");
				}
				resultList.add(dto);
			}

			return resultList;

		} catch (Throwable t) {
			handleException(t, "listForForm.error", auth);
			return null;
		}
	}

	public DTOImportPriloha templateRead(AuthInfo auth, DTOImport dtoImport, DTOImportZmena dtoZmena) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> metaList = null;
			if (StringUtils.isValid(dtoImport.getIDCiselnik())) {
				metaList = listForForm(auth, dtoImport.getIDCiselnik(), dtoZmena.getPlatnostOd(), dtoZmena.getOperacia());
			} else {
				metaList = getDelegate().getGuiRead().metaList(auth, dtoImport.getCiselnikTabulka(), dtoZmena.getOperacia());
			}

			String fileName = dtoImport.getCiselnikTabulka() + "_";
			if (_CudConsts.ZMENA_OPERACIA_N.equals(dtoZmena.getOperacia())) {
				fileName += "INSERT";
			} else if (_CudConsts.ZMENA_OPERACIA_U.equals(dtoZmena.getOperacia())) {
				fileName += "UPDATE";
			} else if (_CudConsts.ZMENA_OPERACIA_Z.equals(dtoZmena.getOperacia())) {
				fileName += "DELETE";
			}

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1250");
			WritableWorkbook xlsWrite = WorkbookParser.createWorkbook(buffer, ws);
			WritableSheet sheetWrite = xlsWrite.createSheet(dtoImport.getCiselnikTabulka(), 0);

			int index = 0;

			WritableCellFormat titleformatBold = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 10, WritableFont.BOLD, false));
			titleformatBold.setAlignment(Alignment.CENTRE);

			WritableCellFormat titleformat = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 10, WritableFont.NO_BOLD, false));
			titleformat.setAlignment(Alignment.CENTRE);

			sheetWrite.addCell(new Label(index++, 0, _CudConsts.NAZOV_XLS_OPERACIA, titleformatBold));
			if (StringUtils.isValid(dtoImport.getIDCiselnik())) {
				sheetWrite.addCell(new Label(index++, 0, _CudConsts.NAZOV_XLS_PLATNOST_OD, titleformatBold));
				sheetWrite.addCell(new Label(index++, 0, _CudConsts.NAZOV_XLS_CAS_SCHVALENIA_GR, titleformat));
				sheetWrite.addCell(new Label(index++, 0, _CudConsts.NAZOV_XLS_POZNAMKA, titleformat));
			}

			if (_CudConsts.ZMENA_OPERACIA_N.equals(dtoZmena.getOperacia()) || _CudConsts.ZMENA_OPERACIA_U.equals(dtoZmena.getOperacia())) {
				for (DTOCiselnikStlpecGui dtoCS : metaList) {
					if ("T".equals(dtoCS.getPovinny())) {
						sheetWrite.addCell(new Label(index++, 0, dtoCS.getCiselnikStlpecNazov(), titleformatBold));
					} else {
						sheetWrite.addCell(new Label(index++, 0, dtoCS.getCiselnikStlpecNazov(), titleformat));
					}
				}
			}
			if (_CudConsts.ZMENA_OPERACIA_Z.equals(dtoZmena.getOperacia())) {
				DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiPk(metaList);
				if ("T".equals(dtoCS.getPovinny())) {
					sheetWrite.addCell(new Label(index++, 0, dtoCS.getCiselnikStlpecNazov(), titleformatBold));
				}
			}

			for (int i = 0; i < index; i++) {
				sheetWrite.setColumnView(i, 30);
			}

			xlsWrite.write();
			xlsWrite.close();

			DTOImportPriloha resultDTO = new DTOImportPriloha();
			resultDTO.setFileName(fileName + ".xls");
			resultDTO.setPriloha(buffer.toByteArray());

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "templateRead.error", auth);
			return null;
		}
	}

}
