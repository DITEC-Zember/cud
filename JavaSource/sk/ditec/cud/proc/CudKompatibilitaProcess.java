package sk.ditec.cud.proc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOKompatibilita;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;
import sk.ditec.notif.NotifUtils;
import sk.ditec.process.BaseProcess;

public class CudKompatibilitaProcess extends BaseProcess {

	private Logger log = LoggerFactory.getLogger(CudKompatibilitaProcess.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	@Override
	protected void process() throws Throwable {

		log.info("Start - Som proces {} a bezim", getClass().getSimpleName());

		try {
			AuthInfo auth = AuthInfo.system();

			Map<Integer, DTOCiselnik> cisMap = new HashMap<Integer, DTOCiselnik>();
			for (DTOCiselnik dto : dlg.getCiselnikRead().listLight(auth, null)) {
				cisMap.put(dto.getCiselnikID(), dto);
			}

			log.info("Notifikujem proces {}", getClass().getSimpleName());
			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudImportProcess konci.");
				return;
			}

			Date startTime = new Date();

			List<DTOKompatibilita> resultList = new ArrayList<DTOKompatibilita>();

			for (Integer ciselnikID : new TreeSet<Integer>(cisMap.keySet())) {

				DTOCiselnik dtoCis = cisMap.get(ciselnikID);

				if (!"T".equals(dtoCis.getAktivny()) || !_CudConsts.CISELNIK_TYP_TECHNICKY.equals(dtoCis.getTyp())) {
					continue;
				}

				if ((new Date().getTime() - startTime.getTime()) > _CudConsts.PROC_NOTIFY_DELAY) {
					log.info("Notifikujem proces {}", getClass().getSimpleName());
					if (!statusOKnotify()) {
						log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudImportProcess konci.");
						return;
					}
					startTime = new Date();
				}

				log.info("Kontrola ciselnika: {}", dtoCis.getTabulka());

				DTOKompatibilita dtoResult = new DTOKompatibilita();
				dtoResult.setNadpis(dtoCis.getCiselnikID() + ". " + dtoCis.getNazov() + " (" + dtoCis.getTabulka() + ")");

				Set<String> errorSet = new HashSet<String>();

				Map<Integer, DTOCiselnikStlpec> csMap = ciselnikStlpecMap(auth, dtoCis.getCiselnikID());
				List<DTOCiselnikStlpec> userTabColsList = userTabColsListLight(auth, dtoCis.getTabulka());

				DTOKompatibilita dtoF = new DTOKompatibilita();
				dtoF.setCiselnikID(dtoCis.getCiselnikID());
				dtoF.setZdroj(_CudConsts.ZDROJ_XLS);

				DTOKompatibilita komDTO = dlg.getKompatibilitaRead().kontrolaCiselnikStlpec(auth, dtoF, csMap, userTabColsList);
				if ("F".equals(komDTO.getKompatibilita())) {
					errorSet.addAll(errorList(komDTO.getErrorMsgList(), "Kontrola atribútov v zozname stĺpcov"));
				}

				komDTO = dlg.getKompatibilitaRead().kontrolaWfDef(auth, dtoF);
				if ("F".equals(komDTO.getKompatibilita())) {
					errorSet.addAll(errorList(komDTO.getErrorMsgList(), "Kontrola definície procesov"));
				}

				List<DTOCiselnikGui> ciselnikGuiList = ciselnikGuiList(auth, dtoCis.getCiselnikID());
				log.info("Pocet zaznamov v tabulke CUD_CISELNIK_GUI= {}", ciselnikGuiList.size());

				for (DTOCiselnikGui dtoCisGui : ciselnikGuiList) {

					dtoF.setCiselnikGuiID(dtoCisGui.getCiselnikGuiID());

					Map<Integer, List<DTOCiselnikStlpecGui>> guiMap = dlg.getCiselnikStlpecGuiRead().mapLight(auth, dtoF.getCiselnikGuiID());

					List<DTOCiselnikStlpecGui> guiList = guiMap.get(dtoF.getCiselnikGuiID());
					if (!StringUtils.isValid(guiList)) {
						guiList = new ArrayList<DTOCiselnikStlpecGui>();
					}

					komDTO = dlg.getKompatibilitaRead().kontrolaCiselnika(auth, dtoF, null, _CudConsts.CISELNIK_GUI_STAV_PUB);
					if ("F".equals(komDTO.getKompatibilita())) {
						errorSet.addAll(errorList(komDTO.getErrorMsgList(), "Kontrola číselníka"));
					}

					komDTO = dlg.getKompatibilitaRead().kontrolaReferencia(auth, guiList, csMap, cisMap, dtoF.getZdroj());
					if ("F".equals(komDTO.getKompatibilita())) {
						errorSet.addAll(errorList(komDTO.getErrorMsgList(), "Kontrola referencií"));
					}

					komDTO = dlg.getKompatibilitaRead().kontrolaCiselnikStlpecGui(auth, dtoF, guiList, csMap);
					if ("F".equals(komDTO.getKompatibilita())) {
						errorSet.addAll(errorList(komDTO.getErrorMsgList(), "Kontrola atribútov v definícií obrazovky"));
					}
				}

				komDTO = kontrolaPlatnosti(ciselnikGuiList);
				if ("F".equals(komDTO.getKompatibilita())) {
					errorSet.addAll(errorList(komDTO.getErrorMsgList(), "Kontrola platnosti obrazoviek"));
				}

				if (!errorSet.isEmpty()) {
					dtoResult.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				}

				resultList.add(dtoResult);
			}

			sendNotif(resultList);

		} catch (Exception e) {
			DBUtils.handleException(e, "process.error");

		} finally {
			log.info("End - Som proces {} a koncim", getClass().getSimpleName());
		}

	}

	private List<DTOCiselnikGui> ciselnikGuiList(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			DTOCiselnikGui dtoF = new DTOCiselnikGui();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setStav(_CudConsts.CISELNIK_GUI_STAV_PUB);

			List<DTOCiselnikGui> resultDTO = new ArrayList<DTOCiselnikGui>();

			// zaujmave su len tie zaznamy ktore su v stave PUB, platne a maju spravne nastavene atributy PLATNOST_OD, PLATNOST_DO
			for (DTOCiselnikGui dto : dlg.getCiselnikGuiRead().listLight(auth, dtoF)) {
				if (!StringUtils.isValid(dto.getPlatnostDo())) {
					resultDTO.add(dto);
				} else if (dto.getPlatnostOd().before(dto.getPlatnostDo())) {
					resultDTO.add(dto);
				}
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "ciselnikGuiList.error", auth);
			return null;
		}
	}

	private Map<Integer, DTOCiselnikStlpec> ciselnikStlpecMap(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
			dtoF.setIDCiselnik(ciselnikID);
			return dlg.getCiselnikStlpecRead().mapLight(auth, dtoF, null, null);

		} catch (Throwable t) {
			handleException(t, "ciselnikStlpecMap.error", auth);
			return null;
		}
	}

	private List<DTOCiselnikStlpec> userTabColsListLight(AuthInfo auth, String tabulka) throws AppException {

		try {
			DTOCiselnikStlpec dtoF = new DTOCiselnikStlpec();
			dtoF.setCiselnikTabulka(tabulka);

			DTOCiselnikStlpec[] listDTO = dlg.getGuiRead().userTabColsListLight(auth, dtoF, false);
			if (StringUtils.isValid(listDTO)) {
				return new ArrayList<DTOCiselnikStlpec>(Arrays.asList(listDTO));
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "userTabColsListLight.error", auth);
			return null;
		}
	}

	private DTOKompatibilita kontrolaPlatnosti(List<DTOCiselnikGui> ciselnikGuiList) throws AppException {

		try {
			DTOKompatibilita resultDTO = new DTOKompatibilita();
			resultDTO.setKompatibilita("T");

			Set<String> errorSet = new HashSet<String>();

			int pocet = 0;
			Set<Long> set = new HashSet<Long>();
			for (DTOCiselnikGui dto : ciselnikGuiList) {
				set.add(dto.getPlatnostOd().getTime());
				if (!StringUtils.isValid(dto.getPlatnostDo())) {
					pocet++;
				}
			}

			if (pocet > 1) {
				errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3116));
			}

			Long platnostDo = null;
			for (Long platnostOd : new TreeSet<Long>(set)) {
				List<DTOCiselnikGui> list = new ArrayList<DTOCiselnikGui>();
				for (DTOCiselnikGui dto : ciselnikGuiList) {
					if (platnostOd.longValue() == dto.getPlatnostOd().getTime()) {
						list.add(dto);
					}
				}
				if (list.size() != 1) {
					errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3116));
					break;
				}

				if (StringUtils.isValid(platnostDo)) {
					if (platnostDo.longValue() >= list.get(0).getPlatnostOd().getTime()) {
						errorSet.add(_CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3116));
					}

				}
				if (StringUtils.isValid(list.get(0).getPlatnostDo())) {
					platnostDo = list.get(0).getPlatnostDo().getTime();
				}

			}

			if (!errorSet.isEmpty()) {
				resultDTO.setErrorMsgList(errorSet.toArray(new String[errorSet.size()]));
				resultDTO.setKompatibilita("F");
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "kontrolaPlatnosti.error");
			return null;
		}
	}

	private Set<String> errorList(String[] errorMsgList, String title) throws AppException {

		try {
			Set<String> resultSet = new HashSet<String>();
			for (String err : errorMsgList) {
				resultSet.add(title + ": " + err);
			}

			return resultSet;

		} catch (Throwable t) {
			handleException(t, "errorList.error");
			return null;
		}
	}

	private void sendNotif(List<DTOKompatibilita> resultList) throws AppException {

		try {
			String emailStr = FrameworkUtils.getConfigProperty("cud", "kompatibilita.emailList");
			if (!StringUtils.isValid(emailStr)) {
				log.error("Nie je vyplneny konfiguracny parameter: kompatibilita.emailList, email sa neposiela!");
				return;
			}

			String[] emailList = null;
			if (StringUtils.isValid(emailStr) && _CudKontrolaUtils.isValidEmailList(emailStr)) {
				emailList = _CudLookupUtils.lookupEmailList(emailStr);
			} else {
				log.error("Konfiguracny parameter: kompatibilita.emailList je vyplneny nespravne, email sa neposiela!");
				return;
			}

			String htmlBody = FrameworkUtils.getConfigProperty("cud", "kompatibilita.htmlBody");
			if (!StringUtils.isValid(htmlBody)) {
				log.error("Nie je vyplneny konfiguracny parameter: kompatibilita.htmlBody, email sa neposiela!");
				return;
			}
			InputStream is = CudKompatibilitaProcess.class.getResourceAsStream(htmlBody);
			InputStreamReader isr = new InputStreamReader(is, Charset.forName("UTF-8"));
			BufferedReader br = new BufferedReader(isr);
			String line;
			htmlBody = "";
			while ((line = br.readLine()) != null) {
				htmlBody += line;
			}

			is.close();
			isr.close();
			br.close();

			String htmlRow = FrameworkUtils.getConfigProperty("cud", "kompatibilita.htmlRow");
			if (!StringUtils.isValid(htmlRow)) {
				log.error("Nie je vyplneny konfiguracny parameter: kompatibilita.htmlRow, email sa neposiela!");
				return;
			}
			String htmlError = FrameworkUtils.getConfigProperty("cud", "kompatibilita.htmlError");
			if (!StringUtils.isValid(htmlError)) {
				log.error("Nie je vyplneny konfiguracny parameter: kompatibilita.htmlError, email sa neposiela!");
				return;
			}

			String emailSubject = "CUD notifikacia o kompatibilite";

			{
				int pocet = 0;
				String cisRows = "";
				for (DTOKompatibilita dto : resultList) {
					if (StringUtils.isValid(dto.getErrorMsgList())) {
						cisRows += StringUtils.replaceAll(htmlRow, "{tabulka}", dto.getNadpis());
						pocet++;
						for (String err : dto.getErrorMsgList()) {
							cisRows += StringUtils.replaceAll(htmlError, "{errorMsg}", err);
						}
					}
				}
				htmlBody = StringUtils.replaceAll(htmlBody, "{cudErrorCount}", Integer.toString(pocet));
				htmlBody = StringUtils.replaceAll(htmlBody, "{cudTablesError}", cisRows);
			}
			{
				int pocet = 0;
				String cisRows = "";
				for (DTOKompatibilita dto : resultList) {
					if (!StringUtils.isValid(dto.getErrorMsgList())) {
						cisRows += StringUtils.replaceAll(htmlRow, "{tabulka}", dto.getNadpis());
						pocet++;
					}
				}
				htmlBody = StringUtils.replaceAll(htmlBody, "{cudRightCount}", Integer.toString(pocet));
				htmlBody = StringUtils.replaceAll(htmlBody, "{cudTablesRight}", cisRows);
			}

			NotifUtils.sendNotif("", emailList, emailSubject, htmlBody);

		} catch (Throwable t) {
			handleException(t, "sendNotif.error");
		}
	}

	@Override
	protected String getLogName() {
		return "kompatibilita";
	}

}
