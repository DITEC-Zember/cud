package sk.ditec.cud.proc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.WorkbookParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportPriloha;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOSkupina;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOValidate;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfNotif;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOWorkflow;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.dao.meta.CudCiselnikGuiPeer;
import sk.ditec.dao.meta.CudCiselnikPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecGuiPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudPrekladPeer;
import sk.ditec.dao.meta.CudWfDefPeer;
import sk.ditec.process.BaseProcess;


public class CudImportProcess extends BaseProcess {

	private Logger log = LoggerFactory.getLogger(CudImportProcess.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	@Override
	protected void process() throws Throwable {

		log.info("Start - Som proces CudImportProcess a bezim");

		try {
			AuthInfo auth = AuthInfo.system();

			DTOImport[] importList = dlg.getImportRead().listForProcess(auth);
			log.info("Pocet zaznamov na spracovanie {}", importList.length);

			Date startTime = new Date();

			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudImportProcess konci.");
				return;
			}

			DTOSkupina[] skupinaList = null;

			for (DTOImport dtoImport : importList) {

				Object[] objArr = new Object[] { dtoImport.getImportID(), dtoImport.getStav(), dtoImport.getCasKontrolaKon(), dtoImport.getCasImportKon() };
				log.info("Spracovanie zaznamu importID={}, stav={}, casKontrolaKon={}, casImportKon={}", objArr);

				startTransaction(auth, "CUDdataModify");

				try {
					if (_CudConsts.IMPORT_STAV_KONTROLA.equals(dtoImport.getStav())) {
						dlg.getImportModify().updateBeforeKontrola(auth, dtoImport.getImportID());

					} else if (_CudConsts.IMPORT_STAV_IMPORT.equals(dtoImport.getStav())) {
						dlg.getImportModify().updateBeforeImport(auth, dtoImport.getImportID());
					}

					dtoImport = dlg.getImportRead().readLight(auth, dtoImport.getImportID());

					Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();

					Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> metaMapForSend = new HashMap<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>();
					Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> fkMetaMap = new HashMap<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>();

					List<DTOCiselnikStlpec> csList = dlg.getCiselnikStlpecRead().listLight(auth, dtoImport.getIDCiselnik());

					if (!StringUtils.isValid(skupinaList)) {
						skupinaList = dlg.getIam().skupinaList(auth, null);
					}

					Map<String, DTOPlugin[]> pluginMap = new HashMap<String, DTOPlugin[]>();
					Map<String, Map<String, Map<String, String>>> pluginLookupMap = new HashMap<String, Map<String, Map<String, String>>>();

					Date d = new Date();

					if (_CudConsts.IMPORT_STAV_KONTROLA.equals(dtoImport.getStav())) {

						DTOImportPriloha dtoPriloha = dlg.getImportPrilohaRead().readLight(auth, dtoImport.getImportID());

						InputStream is = new ByteArrayInputStream(dtoPriloha.getPriloha());

						WorkbookSettings ws = new WorkbookSettings();
						ws.setEncoding("Cp1250");
						Workbook workBook = WorkbookParser.getWorkbook(is, ws);

						Sheet[] sheets = workBook.getSheets();
						if (sheets == null || sheets.length == 0) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3033, dtoImport.getCiselnikTabulka());
							log.error(err);
							dlg.getImportModify().updateError(auth, dtoImport.getImportID(), err);
							continue;
						}

						Sheet sheet = null;
						for (Sheet sheetItem : sheets) {
							if (dtoImport.getCiselnikTabulka().equals(sheetItem.getName())) {
								sheet = sheetItem;
								break;
							}
						}
						if (!StringUtils.isValid(sheet)) {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3033, dtoImport.getCiselnikTabulka());
							log.error(err);
							dlg.getImportModify().updateError(auth, dtoImport.getImportID(), err);
							continue;
						}

						Map<String, Integer> columnsMap = createColumnsMapFromXls(sheet);

						Map<String, String> lookupMap = new HashMap<String, String>();
						
						Set<Integer> skipSet = new HashSet<Integer>();
						// skipSet.add(243);
						


						boolean dataVlozenie = false;
						boolean errors = false;
						for (int row = 1; row < sheet.getRows(); row++) {

							if ((row % 500 == 0) && (new Date().getTime() - startTime.getTime()) > _CudConsts.PROC_NOTIFY_DELAY) {
								if (!statusOKnotify()) {
									log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudImportProcess konci.");
									return;
								}
								startTime = new Date();
							}

							Map<String, String> rowMap = rowMapFromSheet(sheet.getRow(row), columnsMap);
							if (!StringUtils.isValid(rowMap.get(_CudConsts.NAZOV_XLS_OPERACIA))) {
								continue;
							}
							rowMap.put(_CudConsts.NAZOV_XLS_ROW_ID, Integer.toString(row + 1));

							boolean b = dlg.getImportZmenaRead().existXlsRowID(auth, dtoImport.getImportID(), row + 1);
							if (b) {
								log.info("Riadok {} je uz spracovany, preskakujem", row + 1);
								continue;
							}
							
							if (skipSet.contains(row + 1)) {
								continue;
							}

							log.info("Spracovanie riadku {}", row + 1);

							dataVlozenie = true;

							applyLookupValues(auth, rowMap, lookupMap, skupinaList);

							String key = rowMap.get(_CudConsts.NAZOV_XLS_PLATNOST_OD);
							if (StringUtils.isValid(key)) {

								Date platnostOd = null;
								try {
									platnostOd = _CudConsts.DATE_FORMAT.parse(key);
								} catch (ParseException pe) {
								}
								if (!pluginMap.keySet().contains(key) && StringUtils.isValid(platnostOd)) {
									DTOPlugin[] plList = dlg.getGuiRead().pluginList(auth, dtoImport.getIDCiselnik(), _CudConsts.PLUGIN_TYP_VALIDACNY, platnostOd);
									pluginMap.put(key, plList);
								}

								if (!StringUtils.isValid(pluginLookupMap.get(key))) {
									pluginLookupMap.put(key, new HashMap<String, Map<String, String>>());
								}
							}

							DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, d, pluginMap.get(key), pluginLookupMap.get(key));
							if (StringUtils.isValid(dtoImport.getIDCiselnik())) {
								dlg.getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);
							} else {
								dlg.getValidation().validateMeta(auth, dtoVal, metaMap, rowMap, skupinaList);
							}

							if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
								errors = true;
							}

							dlg.getImportZmenaModify().update(auth, dtoVal.getImportZmenaDTO(), dtoImport.getImportID(), dtoImport.getCiselnikTabulka());
						}

						is.close();
						workBook.close();

						if (dataVlozenie) {
							if (errors) {
								dlg.getImportModify().updateError(auth, dtoImport.getImportID(), null);
							} else {
								dlg.getImportModify().updateAfterKontrola(auth, dtoImport.getImportID());
							}
						} else {
							String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3059, dtoImport.getCiselnikTabulka());
							dlg.getImportModify().updateError(auth, dtoImport.getImportID(), err);
						}

					} else if (_CudConsts.IMPORT_STAV_IMPORT.equals(dtoImport.getStav())) {

						DTOCiselnik dtoCis = dlg.getCiselnikRead().read(auth, dtoImport.getIDCiselnik());

						Map<Integer, DTOUcet[]> ucetMap = new HashMap<Integer, DTOUcet[]>();

						List<DTOWfDef> wfDefList = dlg.getWfDefRead().list(auth, dtoImport.getIDCiselnik());

						Integer totalCount = dlg.getImportZmenaRead().pocet(auth, dtoImport.getImportID());
						Integer pocet = 0;

						int page = 0;
						while (pocet < totalCount) {

							if ((new Date().getTime() - startTime.getTime()) > _CudConsts.PROC_NOTIFY_DELAY) {
								if (!statusOKnotify()) {
									log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudImportProcess konci.");
									return;
								}
								startTime = new Date();
							}

							DTOImportZmena[] zmenaList = dlg.getImportZmenaRead().listLight(auth, new Page(++page, 500), dtoImport.getImportID());
							pocet += zmenaList.length;

							Set<Integer> set = new HashSet<Integer>();
							for (DTOImportZmena dtoZmena : zmenaList) {
								set.add(dtoZmena.getImportZmenaID());
							}
							Map<Integer, List<DTOImportZmenaStlpec>> zmenaStlpecMap = dlg.getImportZmenaStlpecRead().map(auth, dtoImport.getImportID(), set.toArray(new Integer[set.size()]));

							for (DTOImportZmena dtoZmena : zmenaList) {

								log.info("Spracovanie zmeny importZmenaID={}", dtoZmena.getImportZmenaID());

								List<DTOImportZmenaStlpec> zmenaStlpecList = zmenaStlpecMap.get(dtoZmena.getImportZmenaID());
								if (!StringUtils.isValid(zmenaStlpecList) || zmenaStlpecList.isEmpty()) {
									log.info("Zoznam zmien v tabulke CUD_ZMENA_STLPEC je prazdny, preskakujem zmenu.");
									continue;
								}

								Map<String, String> rowMap = parseDataFromDB(dtoZmena, zmenaStlpecList);

								String key = null;

								if (StringUtils.isValid(dtoZmena.getPlatnostOd())) {
									key = _CudConsts.DATE_FORMAT.format(dtoZmena.getPlatnostOd());
									if (!pluginMap.keySet().contains(key)) {
										DTOPlugin[] plList = dlg.getGuiRead().pluginList(auth, dtoImport.getIDCiselnik(), _CudConsts.PLUGIN_TYP_VALIDACNY, dtoZmena.getPlatnostOd());
										pluginMap.put(key, plList);
									}

									if (!StringUtils.isValid(pluginLookupMap.get(key))) {
										pluginLookupMap.put(key, new HashMap<String, Map<String, String>>());
									}
								}

								DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, d, pluginMap.get(key), pluginLookupMap.get(key));
								dtoVal.getImportZmenaDTO().setObnova(dtoZmena.getObnova());
								if (StringUtils.isValid(dtoImport.getIDCiselnik())) {
									dlg.getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);
								} else {
									dlg.getValidation().validateMeta(auth, dtoVal, metaMap, rowMap, skupinaList);
								}

								dtoVal.getImportZmenaDTO().setImportZmenaID(dtoZmena.getImportZmenaID());
								dtoVal.getImportZmenaDTO().setIDImport(dtoImport.getImportID());

								if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
									dlg.getImportMsgModify().update(auth, dtoVal.getImportZmenaDTO().getImportMsgList(), dtoImport.getImportID(), dtoVal.getImportZmenaDTO().getImportZmenaID());
									dlg.getImportModify().updateError(auth, dtoImport.getImportID(), null);
									endTransaction(auth, true);
									continue;

								} else if (StringUtils.isValid(dtoImport.getIDCiselnik())) {

									DTOCiselnikStlpecGui dtoCSFile = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByEditControl(metaMap.get(dtoVal.getPlatnostOd()), _CudConsts.CISELNIK_STLPEC_GUI_EDIT_CONTROL_FILE);
									if (StringUtils.isValid(dtoCSFile)) {
										for (DTOImportZmenaStlpec dtoIZS : dtoVal.getImportZmenaDTO().getImportZmenaStlpecList()) {
											if (dtoIZS.getIDCiselnikStlpec().intValue() == dtoCSFile.getIDCiselnikStlpec().intValue()) {
												dtoIZS.setSubor("T");
											}
										}
									}

									DTOWorkflow dtoWf = dlg.getWorkflow().generujWorkflowAll(auth, dtoImport.getIDCiselnik(), dtoVal.getImportZmenaDTO(), wfDefList, ucetMap);
									if (StringUtils.isValid(dtoWf)) {
										workflowUpdate(auth, dtoWf, dtoVal.getImportZmenaDTO(), d);
										sendNotif(auth, dtoCis, dtoVal, dtoWf, wfDefList, metaMapForSend, fkMetaMap);
									}

								} else {

									if (CudCiselnikPeer.TABLE_NAME.equals(dtoImport.getCiselnikTabulka())) {
										dlg.getCiselnikModify().update(auth, dtoVal.getImportZmenaDTO(), d);

									} else if (CudCiselnikStlpecPeer.TABLE_NAME.equals(dtoImport.getCiselnikTabulka())) {
										dlg.getCiselnikStlpecModify().update(auth, dtoVal.getImportZmenaDTO(), d);

									} else if (CudCiselnikGuiPeer.TABLE_NAME.equals(dtoImport.getCiselnikTabulka())) {
										dlg.getCiselnikGuiModify().update(auth, dtoVal.getImportZmenaDTO(), d);

									} else if (CudCiselnikStlpecGuiPeer.TABLE_NAME.equals(dtoImport.getCiselnikTabulka())) {
										dlg.getCiselnikStlpecGuiModify().update(auth, dtoVal.getImportZmenaDTO(), d);

									} else if (CudWfDefPeer.TABLE_NAME.equals(dtoImport.getCiselnikTabulka())) {
										dlg.getWfDefModify().update(auth, dtoVal.getImportZmenaDTO(), d);

									} else if (CudPrekladPeer.TABLE_NAME.equals(dtoImport.getCiselnikTabulka())) {
										dlg.getPrekladModify().update(auth, dtoVal.getImportZmenaDTO(), d);
									}
								}
							}
						}

						dlg.getImportModify().updateAfterImport(auth, dtoImport.getImportID());
					}

					endTransaction(auth, true);

				} catch (Exception e) {
					dlg.getImportModify().updateError(auth, dtoImport.getImportID(), e.getMessage());
					handleException(e, "process.error");
				}

			} // for

		} catch (Exception e) {
			// sendNotifikaciaError(getStackTraceToString(e));
			DBUtils.handleException(e, "process.error");
		}

		log.info("End - Som proces CudImportProcess a koncim");
	}

	private Map<String, Integer> createColumnsMapFromXls(Sheet sheet) throws AppException {

		try {
			Map<String, Integer> resultMap = new HashMap<String, Integer>();

			if (sheet == null || sheet.getRows() == 0) {
				return resultMap;
			}

			Cell[] cells = sheet.getRow(0);
			for (int i = 0; i < cells.length; i++) {
				String stlpecNazov = cells[i].getContents();
				if (StringUtils.isValid(stlpecNazov)) {
					stlpecNazov = stlpecNazov.trim();
					resultMap.put(stlpecNazov, i);
				}
			}

			return resultMap;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createColumnsMapFromXls.error");
			return null;
		}
	}

	private Map<String, String> rowMapFromSheet(Cell[] cells, Map<String, Integer> columnsMap) throws AppException {

		try {
			Map<String, String> resultMap = new HashMap<String, String>();
			Map<String, Integer> columnsCountMap = new HashMap<String, Integer>();

			for (String columnsName : columnsMap.keySet()) {

				if (cells.length > columnsMap.get(columnsName)) {

					String s = cells[columnsMap.get(columnsName)].getContents();
					s = StringUtils.isValid(s) ? s.trim() : null;
					resultMap.put(columnsName, s);

					if (!StringUtils.isValid(columnsCountMap.get(columnsName))) {
						columnsCountMap.put(columnsName, 0);
					}
					columnsCountMap.put(columnsName, columnsCountMap.get(columnsName) + 1);

				} else {
					resultMap.put(columnsName, null);
				}
			}

			if (StringUtils.isValid(resultMap.get(_CudConsts.NAZOV_XLS_OPERACIA))) {
				boolean b = true;
				for (Integer pocet : columnsCountMap.values()) {
					if (pocet.intValue() > 1) {
						b = false;
					}
				}
				resultMap.put(_CudConsts.IMPORT_KONTROLA_DEF, b ? "T" : "F");
			}

			return resultMap;

		} catch (Throwable t) {
			DBUtils.handleException(t, "rowMapFromSheet.error");
			return null;
		}
	}

	private Map<String, String> parseDataFromDB(DTOImportZmena dtoZmena, List<DTOImportZmenaStlpec> zmenaStlpecList) throws AppException {

		try {
			Map<String, String> resultMapMap = new HashMap<String, String>();

			if (!StringUtils.isValid(zmenaStlpecList) || zmenaStlpecList.isEmpty()) {
				return resultMapMap;
			}

			Map<String, Integer> columnsCountMap = new HashMap<String, Integer>();

			if (StringUtils.isValid(dtoZmena.getRowID())) {
				resultMapMap.put(_CudConsts.NAZOV_ROW_ID, Integer.toString(dtoZmena.getRowID()));
			}
			if (StringUtils.isValid(dtoZmena.getXlsRowID())) {
				resultMapMap.put(_CudConsts.NAZOV_XLS_ROW_ID, Integer.toString(dtoZmena.getXlsRowID()));
			}
			if (StringUtils.isValid(dtoZmena.getOperacia())) {
				resultMapMap.put(_CudConsts.NAZOV_XLS_OPERACIA, dtoZmena.getOperacia());
			}
			if (StringUtils.isValid(dtoZmena.getPlatnostOd())) {
				resultMapMap.put(_CudConsts.NAZOV_XLS_PLATNOST_OD, _CudConsts.DATE_FORMAT.format(dtoZmena.getPlatnostOd()));
			}
			if (StringUtils.isValid(dtoZmena.getCasSchvaleniaGr())) {
				resultMapMap.put(_CudConsts.NAZOV_XLS_CAS_SCHVALENIA_GR, _CudConsts.DATE_FORMAT.format(dtoZmena.getCasSchvaleniaGr()));
			}
			if (StringUtils.isValid(dtoZmena.getPoznamka())) {
				resultMapMap.put(_CudConsts.NAZOV_XLS_POZNAMKA, dtoZmena.getPoznamka());
			}

			if (!_CudConsts.ZMENA_OPERACIA_Z.equals(resultMapMap.get(_CudConsts.NAZOV_XLS_OPERACIA))) {

				for (DTOImportZmenaStlpec dtoZS : zmenaStlpecList) {

					String s = dtoZS.getNewValue();
					s = StringUtils.isValid(s) ? s.trim() : null;
					resultMapMap.put(dtoZS.getCiselnikStlpecNazov(), s);

					if (!StringUtils.isValid(columnsCountMap.get(dtoZS.getCiselnikStlpecNazov()))) {
						columnsCountMap.put(dtoZS.getCiselnikStlpecNazov(), 0);
					}
					columnsCountMap.put(dtoZS.getCiselnikStlpecNazov(), columnsCountMap.get(dtoZS.getCiselnikStlpecNazov()) + 1);
				}
			}

			boolean b = true;
			for (Integer pocet : columnsCountMap.values()) {
				if (pocet.intValue() > 1) {
					b = false;
				}
			}
			resultMapMap.put(_CudConsts.IMPORT_KONTROLA_DEF, b ? "T" : "F");

			return resultMapMap;

		} catch (Throwable t) {
			DBUtils.handleException(t, "parseDataFromDB.error");
			return null;
		}
	}

	private void applyLookupValues(AuthInfo auth, Map<String, String> rowMap, Map<String, String> lookupMap, DTOSkupina[] skupinaList) throws AppException {

		try {
			for (String columnName : rowMap.keySet()) {
				if (rowMap.keySet().contains("XLS_LOOKUP_" + columnName)) {
					String sql = rowMap.get("XLS_LOOKUP_" + columnName);
					if (StringUtils.isValid(sql)) {
						sql = sql.trim();
						String lookupValue = null;
						if (lookupMap.keySet().contains(sql)) {
							lookupValue = lookupMap.get(sql);
						} else {
							lookupValue = dlg.getDynCiselnikRead().readPkValue(auth, sql);
							lookupMap.put(sql, lookupValue);
						}
						rowMap.put(columnName, lookupValue);
					}
				}
				if (rowMap.keySet().contains("XLS_LOOKUP_IAM_ID_SKUPINA")) {
					String skupinaNazov = rowMap.get("XLS_LOOKUP_IAM_ID_SKUPINA");
					Integer skupinaID = null;
					if (StringUtils.isValid(skupinaNazov)) {
						for (DTOSkupina dto : skupinaList) {
							if (skupinaNazov.equals(dto.getNazov())) {
								skupinaID = dto.getSkupinaID();
							}
						}
					}
					if (StringUtils.isValid(skupinaID)) {
						rowMap.put("ID_SKUPINA", skupinaID.toString());
					}
				}
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "applyLookupValues.error");
		}
	}

	private void workflowUpdate(AuthInfo auth, DTOWorkflow dtoWf, DTOImportZmena dtoZmena, Date d) throws AppException {

		try {
			getConnection(auth);

			dlg.getWorkflow().workflowUpdateSoft(auth, dtoWf, dtoZmena, d);

			returnConnection(auth);

			endTransaction(auth, true);

		} catch (Throwable t) {
			handleException(t, "workflowUpdate.error", auth);
		}
	}

	private void sendNotif(AuthInfo auth, DTOCiselnik dtoCis, DTOValidate dtoVal, DTOWorkflow dtoWf, List<DTOWfDef> wfDefList, Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> metaMap, Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> metaMapForLookup) throws AppException {

		try {
			Map<String, String> rowMap = null;
			List<DTOCiselnikStlpecGui> metaList = null;

			if (!StringUtils.isValid(metaMap.get(dtoWf.getZmenaDTO().getPlatnostOd()))) {
				metaMap.put(dtoWf.getZmenaDTO().getPlatnostOd(), new HashMap<Integer, List<DTOCiselnikStlpecGui>>());
			}
			if (!StringUtils.isValid(metaMapForLookup.get(dtoWf.getZmenaDTO().getPlatnostOd()))) {
				metaMapForLookup.put(dtoWf.getZmenaDTO().getPlatnostOd(), new HashMap<Integer, List<DTOCiselnikStlpecGui>>());
			}

			DTOWfDef dtoDef = _CudLookupUtils.lookupDTOWfDef(wfDefList, _CudConsts.WF_DEF_TYP_IN);
			if (StringUtils.isValid(dtoDef) && "T".equals(dtoDef.getEmailSend())) {
				DTOWfTodo dtoTodo = _CudLookupUtils.lookupDTOWfTodo(dtoWf.getWfTodoUpdateList(), dtoDef.getWfDefID());
				if (StringUtils.isValid(dtoTodo)) {
					rowMap = dlg.getDynCiselnikRead().readLookupValues(auth, dtoVal.getCiselnikID(), dtoWf.getZmenaDTO().getPlatnostOd(), metaMap, metaMapForLookup, dtoVal.getOldValueMap());

					metaList = metaMap.get(dtoWf.getZmenaDTO().getPlatnostOd()).get(dtoWf.getZmenaDTO().getIDCiselnik());

					Set<Integer> ciselnikIDs = new HashSet<Integer>();
					for (DTOCiselnikStlpecGui dto : metaList) {
						if (StringUtils.isValid(dto.getCiselnikStlpecFk1IDCiselnik())) {
							if (!metaMapForLookup.get(dtoWf.getZmenaDTO().getPlatnostOd()).keySet().contains(dto.getCiselnikStlpecFk1IDCiselnik())) {
								ciselnikIDs.add(dto.getCiselnikStlpecFk1IDCiselnik());
							}
						}
					}

					Map<Integer, List<DTOCiselnikStlpecGui>> lookupMetaMap = dlg.getCiselnikStlpecGuiRead().mapForLookup(auth, ciselnikIDs, dtoWf.getZmenaDTO().getPlatnostOd());
					for (Integer ciselnikID : lookupMetaMap.keySet()) {
						metaMapForLookup.get(dtoWf.getZmenaDTO().getPlatnostOd()).put(ciselnikID, lookupMetaMap.get(ciselnikID));
					}

					for (DTOZmenaStlpec dto : dtoWf.getZmenaStlpecList()) {
						DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, dto.getIDCiselnikStlpec());
						if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
							if (StringUtils.isValid(dto.getOldValue())) {
								dto.setOldValue(dlg.getDynCiselnikRead().lookupValueFormat(auth, metaMapForLookup.get(dtoWf.getZmenaDTO().getPlatnostOd()), dtoCS.getCiselnikStlpecFk1IDCiselnik(), dto.getOldValue(), dtoWf.getZmenaDTO().getPlatnostOd()));
							}
							if (StringUtils.isValid(dto.getNewValue())) {
								dto.setNewValue(dlg.getDynCiselnikRead().lookupValueFormat(auth, metaMapForLookup.get(dtoWf.getZmenaDTO().getPlatnostOd()), dtoCS.getCiselnikStlpecFk1IDCiselnik(), dto.getNewValue(), dtoWf.getZmenaDTO().getPlatnostOd()));
							}
						} else if (_CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getCiselnikStlpecDbTyp())) {
							if (StringUtils.isValid(dto.getOldValue())) {
								dto.setOldValue(dlg.getDynCiselnikRead().doubleValueFormat(dto.getOldValue(), dtoCS.getDecimals()));
							}
							if (StringUtils.isValid(dto.getNewValue())) {
								dto.setNewValue(dlg.getDynCiselnikRead().doubleValueFormat(dto.getNewValue(), dtoCS.getDecimals()));
							}
						} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getCiselnikStlpecDbTyp())) {
							if (StringUtils.isValid(dto.getOldValue())) {
								dto.setOldValue("T".equals(dto.getOldValue()) ? "Áno" : "Nie");
							}
							if (StringUtils.isValid(dto.getNewValue())) {
								dto.setNewValue("T".equals(dto.getNewValue()) ? "Áno" : "Nie");
							}
						}
					}

					DTOWfNotif dtoNotif = new DTOWfNotif();
					dtoNotif.setCiselnikID(dtoCis.getCiselnikID());
					dtoNotif.setCiselnikNazov(dtoCis.getNazov());
					dtoNotif.setZmenaOperacia(dtoWf.getZmenaDTO().getOperacia());
					dtoNotif.setPoznamka(dtoTodo.getPoznamka());
					dtoNotif.setPlatnostOd(dtoWf.getZmenaDTO().getPlatnostOd());

					dlg.getWfNotif().sendNotif(auth, dtoNotif, dtoDef, dtoTodo, metaList, dtoWf.getZmenaStlpecList(), rowMap);
				}
			}

			dtoDef = _CudLookupUtils.lookupDTOWfDef(wfDefList, _CudConsts.WF_DEF_TYP_SC);
			if (StringUtils.isValid(dtoDef) && "T".equals(dtoDef.getEmailSend())) {
				DTOWfTodo dtoTodo = _CudLookupUtils.lookupDTOWfTodo(dtoWf.getWfTodoUpdateList(), dtoDef.getWfDefID());
				if (StringUtils.isValid(dtoTodo)) {

					if (!StringUtils.isValid(rowMap)) {
						rowMap = dlg.getDynCiselnikRead().readLookupValues(auth, dtoVal.getCiselnikID(), dtoWf.getZmenaDTO().getPlatnostOd(), metaMap, metaMapForLookup, dtoVal.getOldValueMap());

						metaList = metaMap.get(dtoWf.getZmenaDTO().getPlatnostOd()).get(dtoWf.getZmenaDTO().getIDCiselnik());

						Set<Integer> ciselnikIDs = new HashSet<Integer>();
						for (DTOCiselnikStlpecGui dto : metaList) {
							if (StringUtils.isValid(dto.getCiselnikStlpecFk1IDCiselnik())) {
								if (!metaMapForLookup.get(dtoWf.getZmenaDTO().getPlatnostOd()).keySet().contains(dto.getCiselnikStlpecFk1IDCiselnik())) {
									ciselnikIDs.add(dto.getCiselnikStlpecFk1IDCiselnik());
								}
							}
						}

						Map<Integer, List<DTOCiselnikStlpecGui>> lookupMetaMap = dlg.getCiselnikStlpecGuiRead().mapForLookup(auth, ciselnikIDs, dtoWf.getZmenaDTO().getPlatnostOd());
						for (Integer ciselnikID : lookupMetaMap.keySet()) {
							metaMapForLookup.get(dtoWf.getZmenaDTO().getPlatnostOd()).put(ciselnikID, lookupMetaMap.get(ciselnikID));
						}

						for (DTOZmenaStlpec dto : dtoWf.getZmenaStlpecList()) {
							DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, dto.getIDCiselnikStlpec());
							if (_CudConsts.CISELNIK_STLPEC_TYP_FK.equals(dtoCS.getCiselnikStlpecTyp())) {
								if (StringUtils.isValid(dto.getOldValue())) {
									dto.setOldValue(dlg.getDynCiselnikRead().lookupValueFormat(auth, metaMapForLookup.get(dtoWf.getZmenaDTO().getPlatnostOd()), dtoCS.getCiselnikStlpecFk1IDCiselnik(), dto.getOldValue(), dtoWf.getZmenaDTO().getPlatnostOd()));
								}
								if (StringUtils.isValid(dto.getNewValue())) {
									dto.setNewValue(dlg.getDynCiselnikRead().lookupValueFormat(auth, metaMapForLookup.get(dtoWf.getZmenaDTO().getPlatnostOd()), dtoCS.getCiselnikStlpecFk1IDCiselnik(), dto.getNewValue(), dtoWf.getZmenaDTO().getPlatnostOd()));
								}
							} else if (_CudConsts.DB_TYP_DOUBLE.equals(dtoCS.getCiselnikStlpecDbTyp())) {
								if (StringUtils.isValid(dto.getOldValue())) {
									dto.setOldValue(dlg.getDynCiselnikRead().doubleValueFormat(dto.getOldValue(), dtoCS.getDecimals()));
								}
								if (StringUtils.isValid(dto.getNewValue())) {
									dto.setNewValue(dlg.getDynCiselnikRead().doubleValueFormat(dto.getNewValue(), dtoCS.getDecimals()));
								}
							} else if (_CudConsts.DB_TYP_BOOLEAN.equals(dtoCS.getCiselnikStlpecDbTyp())) {
								if (StringUtils.isValid(dto.getOldValue())) {
									dto.setOldValue("T".equals(dto.getOldValue()) ? "Áno" : "Nie");
								}
								if (StringUtils.isValid(dto.getNewValue())) {
									dto.setNewValue("T".equals(dto.getNewValue()) ? "Áno" : "Nie");
								}
							}
						}
					}

					DTOWfNotif dtoNotif = new DTOWfNotif();
					dtoNotif.setCiselnikID(dtoCis.getCiselnikID());
					dtoNotif.setCiselnikNazov(dtoCis.getNazov());
					dtoNotif.setZmenaOperacia(dtoWf.getZmenaDTO().getOperacia());
					dtoNotif.setPoznamka(dtoTodo.getPoznamka());
					dtoNotif.setPlatnostOd(dtoWf.getZmenaDTO().getPlatnostOd());

					dlg.getWfNotif().sendNotif(auth, dtoNotif, dtoDef, dtoTodo, metaList, dtoWf.getZmenaStlpecList(), rowMap);
				}
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "sendNotif.error");
		}
	}

	@Override
	protected String getLogName() {
		return "import";
	}
}
