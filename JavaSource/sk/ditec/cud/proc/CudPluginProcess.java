package sk.ditec.cud.proc;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.read.biff.WorkbookParser;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

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
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOPluginKontrola;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.plugin.IPlugin;
import sk.ditec.cud.utils.CudCacheMap;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.notif.DTOAttachment;
import sk.ditec.process.BaseProcess;

public class CudPluginProcess extends BaseProcess {

	private Logger log = LoggerFactory.getLogger(CudPluginProcess.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	@Override
	protected void process() throws Throwable {

		log.info("Start - som proces {} a bezim", getClass().getSimpleName());

		try {
			AuthInfo auth = AuthInfo.system();

			Map<Integer, DTOCiselnik> ciselnikMap = new HashMap<Integer, DTOCiselnik>();

			Map<Integer, List<DTOCiselnikStlpec>> csMap = new HashMap<Integer, List<DTOCiselnikStlpec>>();

			Map<Integer, DTOWfDef> wfDefMap = new HashMap<Integer, DTOWfDef>();

			DTOPluginKontrola dto = dlg.getPluginKontrolaRead().readFirst(auth);

			Date startTime = new Date();

			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie {} konci.", getClass().getSimpleName());
				return;
			}

			while (StringUtils.isValid(dto)) {

				try {
					log.info("Spracovanie kontroly pluginKontrolaID=={}", dto.getPluginKontrolaID());

					startTransaction(auth, _CudConsts.PERM_DATA_MODIFY);

					dlg.getPluginKontrolaModify().update(auth, dto.getPluginKontrolaID(), true, false, _CudConsts.PLUGIN_KONTROLA_STAV_CONTROL, null);

					if (!ciselnikMap.keySet().contains(dto.getIDCiselnik())) {
						ciselnikMap.put(dto.getIDCiselnik(), dlg.getCiselnikRead().read(auth, dto.getIDCiselnik()));
					}
					DTOCiselnik dtoCis = ciselnikMap.get(dto.getIDCiselnik());
					if (!StringUtils.isValid(dtoCis)) {
						log.info("Ciselnik sa nepodarilo nacitat, ukoncujem kontrolu pluginKontrolaID=={}", dto.getPluginKontrolaID());
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_607);
						dlg.getPluginKontrolaModify().update(auth, dto.getPluginKontrolaID(), false, true, _CudConsts.PLUGIN_KONTROLA_STAV_ERROR, err);
						endTransaction(auth, true);
						dto = dlg.getPluginKontrolaRead().readFirst(auth);
						continue;
					}

					DTOPlugin[] pluginList = dlg.getGuiRead().pluginList(auth, dto.getIDCiselnik(), _CudConsts.PLUGIN_TYP_VALIDACNY, dto.getPlatnostOd());
					if (!StringUtils.isValid(pluginList)) {
						log.info("Zoznam pluginov je prazdny, ukoncujem kontrolu pluginKontrolaID=={}", dto.getPluginKontrolaID());
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3104, dtoCis.getNazov());
						dlg.getPluginKontrolaModify().update(auth, dto.getPluginKontrolaID(), false, true, _CudConsts.PLUGIN_KONTROLA_STAV_ERROR, err);
						endTransaction(auth, true);
						dto = dlg.getPluginKontrolaRead().readFirst(auth);
						continue;
					}

					if (!csMap.keySet().contains(dto.getIDCiselnik())) {
						csMap.put(dto.getIDCiselnik(), dlg.getCiselnikStlpecRead().listLight(auth, dto.getIDCiselnik()));
					}
					List<DTOCiselnikStlpec> csList = csMap.get(dto.getIDCiselnik());
					if (!StringUtils.isValid(csList) || csList.isEmpty()) {
						log.info("Zoznam stlpcov sa nepodarilo nacitat, ukoncujem kontrolu pluginKontrolaID=={}", dto.getPluginKontrolaID());
						String err = _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_619);
						dlg.getPluginKontrolaModify().update(auth, dto.getPluginKontrolaID(), false, true, _CudConsts.PLUGIN_KONTROLA_STAV_ERROR, err);
						endTransaction(auth, true);
						dto = dlg.getPluginKontrolaRead().readFirst(auth);
						continue;
					}
					DTOCiselnikStlpec dtoCSPK = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList);

					Map<String, String> rowMap = dlg.getDynCiselnikRead().readGreaterThanLight(auth, dtoCis.getTabulka(), csList, dtoCSPK.getNazov(), 0, dto.getPlatnostOd());

					CudCacheMap cacheMap = new CudCacheMap();
					cacheMap.addRecord(_CudConsts.NAZOV_PLG_PLATNOST_OD, dto.getPlatnostOd());
					cacheMap.addRecord(_CudConsts.NAZOV_PLG_PLATNOST_DO, StringUtils.isValid(rowMap.get(_CudConsts.NAZOV_PLATNOST_DO)) ? _CudConsts.DATE_FORMAT.parse(rowMap.get(_CudConsts.NAZOV_PLATNOST_DO)) : null);

					while (StringUtils.isValid(rowMap)) {

						Integer pkValue = Integer.parseInt(rowMap.get(dtoCSPK.getNazov()));

						Set<Integer> pluginIds = dlg.getPluginKontrolaRowRead().pluginIDs(auth, dto.getPluginKontrolaID(), pkValue);

						log.info("Validacia riadku rowID={}", pkValue);

						for (DTOPlugin dtoPlg : pluginList) {

							if (pluginIds.contains(dtoPlg.getPluginID())) {
								log.info("Validacia pluginID={} je uz spracovana, preskakujem.", dtoPlg.getPluginID());
								continue;
							}

							dtoPlg.setCiselnikTabulka(dtoCis.getTabulka());
							dtoPlg.setZdroj(_CudConsts.ZDROJ_FORM);

							IPlugin iplg = (IPlugin) Class.forName(_CudConsts.PLUGIN_PACKAGE + dtoPlg.getPluginClassNameClassName()).newInstance();
							iplg.setDelegat(dlg);
							DTOPluginKontrolaRow[] resList = iplg.validate(auth, dtoPlg, rowMap, csList, cacheMap);
							if (!StringUtils.isValid(resList)) {
								DTOPluginKontrolaRow dtoNew = new DTOPluginKontrolaRow();
								dtoNew.setIDPlugin(dtoPlg.getPluginID());
								dtoNew.setStav(_CudConsts.PLUGIN_KONTROLA_ROW_STAV_SUCCEDD);
								resList = new DTOPluginKontrolaRow[] { dtoNew };
							}
							lookupValues(dto.getPluginKontrolaID(), pkValue, resList);
							dlg.getPluginKontrolaRowModify().update(auth, resList);
						}

						rowMap = dlg.getDynCiselnikRead().readGreaterThanLight(auth, dtoCis.getTabulka(), csList, dtoCSPK.getNazov(), pkValue, dto.getPlatnostOd());

						if (StringUtils.isValid(rowMap)) {
							cacheMap.addRecord(_CudConsts.NAZOV_PLG_PLATNOST_OD, dto.getPlatnostOd());
							cacheMap.addRecord(_CudConsts.NAZOV_PLG_PLATNOST_DO, StringUtils.isValid(rowMap.get(_CudConsts.NAZOV_PLATNOST_DO)) ? _CudConsts.DATE_FORMAT.parse(rowMap.get(_CudConsts.NAZOV_PLATNOST_DO)) : null);
						}
					}

					Integer pocet = dlg.getPluginKontrolaRowRead().countNeuspesnych(auth, dto.getPluginKontrolaID());
					if (pocet.intValue() == 0) {
						dlg.getPluginKontrolaModify().update(auth, dto.getPluginKontrolaID(), false, true, _CudConsts.PLUGIN_KONTROLA_STAV_SUCCESS, null);
					} else {
						dlg.getPluginKontrolaModify().update(auth, dto.getPluginKontrolaID(), false, true, _CudConsts.PLUGIN_KONTROLA_STAV_ERROR, null);
					}

					endTransaction(auth, true);

					sendNotif(auth, dto, dtoCis, wfDefMap, pocet);

					if ((new Date().getTime() - startTime.getTime()) > _CudConsts.PROC_NOTIFY_DELAY) {
						if (!statusOKnotify()) {
							log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie {} konci", getClass().getSimpleName());
							return;
						}
						startTime = new Date();
					}

				} catch (Exception e) {
					String err = getStackTraceToString(e);
					dlg.getPluginKontrolaModify().update(auth, dto.getPluginKontrolaID(), false, true, _CudConsts.PLUGIN_KONTROLA_STAV_ERROR, err);
					dlg.getWfNotif().sendNotifError(_CudConsts.TEXT_NOTIF_SUBJ_PLUGIN, err);
					endTransaction(auth, true);
				}

				dto = dlg.getPluginKontrolaRead().readFirst(auth);
			}

			log.info("End - som proces {} a koncim", getClass().getSimpleName());

		} catch (Exception e) {
			dlg.getWfNotif().sendNotifError(_CudConsts.TEXT_NOTIF_SUBJ_PLUGIN, getStackTraceToString(e));
			DBUtils.handleException(e, "process.error");
		}
	}

	private String getStackTraceToString(Exception e) {

		StringBuilder result = new StringBuilder();
		result.append(e.toString());
		String NEW_LINE = System.getProperty("line.separator");
		result.append(NEW_LINE);

		for (StackTraceElement element : e.getStackTrace()) {
			result.append(element);
			result.append(NEW_LINE);
		}

		return "<pre>" + result.toString() + "</pre>";
	}

	private void lookupValues(Integer pluginKontrolaID, Integer rowID, DTOPluginKontrolaRow[] listDTO) throws AppException {

		try {
			if (!StringUtils.isValid(listDTO)) {
				return;
			}

			for (DTOPluginKontrolaRow dtoNew : listDTO) {
				dtoNew.setIDPluginKontrola(pluginKontrolaID);
				dtoNew.setRowID(rowID);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupValues.error");
		}
	}

	private DTOWfDef wfDefRead(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			DTOWfDef dtoF = new DTOWfDef();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setTyp(_CudConsts.WF_DEF_TYP_SC);

			return dlg.getWfDefRead().mapLight(auth, dtoF).get(ciselnikID).get(0);

		} catch (Throwable t) {
			handleException(t, "wfDefRead.error");
			return null;
		}
	}

	private byte[] creratDTOAttachment(AuthInfo auth, Integer pluginKontrolaID, String ciselnikTabulka) throws AppException {

		try {
			DTOPluginKontrolaRow dtoF = new DTOPluginKontrolaRow();
			dtoF.setIDPluginKontrola(pluginKontrolaID);
			// dtoF.setKontrolaUspesna("F");

			DTOPluginKontrolaRow[] listDTO = dlg.getPluginKontrolaRowRead().list(auth, new Page(1, 100, "1_ASC"), dtoF);

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1250");
			WritableWorkbook xlsWrite = WorkbookParser.createWorkbook(buffer, ws);
			WritableSheet sheetWrite = xlsWrite.createSheet(ciselnikTabulka, 0);

			WritableCellFormat titleformatBold = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 10, WritableFont.BOLD, false));
			titleformatBold.setAlignment(Alignment.CENTRE);

			sheetWrite.addCell(new Label(0, 0, _CudConsts.NAZOV_ROW_ID, titleformatBold));
			sheetWrite.addCell(new Label(1, 0, "Trieda", titleformatBold));
			sheetWrite.addCell(new Label(2, 0, "POPIS", titleformatBold));

			WritableCellFormat dataformat1 = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 10, WritableFont.NO_BOLD, false));
			dataformat1.setAlignment(Alignment.CENTRE);

			WritableCellFormat dataformat = new WritableCellFormat(new WritableFont(WritableFont.COURIER, 10, WritableFont.NO_BOLD, false));
			dataformat.setAlignment(Alignment.LEFT);

			int j = 1;
			for (DTOPluginKontrolaRow dtoRow : listDTO) {
				sheetWrite.addCell(new Label(0, j, dtoRow.getRowID().toString(), dataformat1));
				sheetWrite.addCell(new Label(1, j, dtoRow.getPluginClassNameClassName(), dataformat));
				sheetWrite.addCell(new Label(2, j++, dtoRow.getPopis(), dataformat));
			}

			sheetWrite.setColumnView(0, 15);
			sheetWrite.setColumnView(1, 55);
			sheetWrite.setColumnView(2, 125);

			xlsWrite.write();
			xlsWrite.close();

			return buffer.toByteArray();

		} catch (Throwable t) {
			handleException(t, "creratDTOAttachment.error");
			return null;
		}
	}

	public void sendNotif(AuthInfo auth, DTOPluginKontrola dto, DTOCiselnik dtoCis, Map<Integer, DTOWfDef> wfDefMap, Integer pocetNeuspesnych) throws AppException {

		try {
			DTOWfDef dtoDef = wfDefMap.get(dto.getIDCiselnik());
			if (!StringUtils.isValid(dtoDef)) {
				dtoDef = wfDefRead(auth, dto.getIDCiselnik());
				wfDefMap.put(dto.getIDCiselnik(), dtoDef);
			}

			String emailSubject = "CUD validácia číselníka: " + dtoCis.getTabulka() + " / " + dtoCis.getNazov();

			String emailText = "";
			if (pocetNeuspesnych.intValue() == 0) {
				emailText = "Validácia prebehla bez zaznamenania chýb.";
			} else {
				emailText = "Validáciou bol odhalený zoznam chýb, viď príloha.";
			}
			emailText += "</br></br>";

			DTOAttachment[] att = null;
			if (pocetNeuspesnych.intValue() != 0) {
				DTOAttachment dtoAtt = new DTOAttachment();
				dtoAtt.setName(dtoCis.getTabulka() + "_" + _CudConsts.DATE_FORMAT.format(dto.getPlatnostOd()) + ".xls");
				dtoAtt.setContentType("application/octet-stream");
				dtoAtt.setContent(creratDTOAttachment(auth, dto.getPluginKontrolaID(), dtoCis.getTabulka()));
				att = new DTOAttachment[] { dtoAtt };
			}

			dlg.getWfNotif().sendNotif(emailSubject, emailText, dtoDef, att);

		} catch (Throwable t) {
			handleException(t, "sendNotif.error");
		}
	}

	@Override
	protected String getLogName() {
		return "validacia.pluginom";
	}
}
