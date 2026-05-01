package sk.ditec.cud.proc;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.ListWraper;
import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.security.AuthSkupina;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.process.BaseProcess;

public class CudUdrzbaDbProcess extends BaseProcess {

	private Logger log = LoggerFactory.getLogger(CudUdrzbaDbProcess.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	@Override
	protected void process() throws Throwable {

		log.info("Start - Som proces CudUdrzbaDbProcess a bezim");

		try {
			AuthInfo auth = AuthInfo.system();

			log.info("Mazanie nepotrebnych zaznamov hromadneho importu.");

			Integer[] importIDs = dlg.getImportRead().idsForDelete(auth);
			log.info("Pocet zaznamov pre hard delete importu {}", importIDs.length);

			if (!statusOKnotify()) {
				log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudUdrzbaDbProcess konci.");
				return;
			}

			Date startTime = new Date();

			for (Integer importID : importIDs) {

				if ((new Date().getTime() - startTime.getTime()) > _CudConsts.PROC_NOTIFY_DELAY) {
					if (!statusOKnotify()) {
						log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudUdrzbaDbProcess konci.");
						return;
					}
					startTime = new Date();
				}

				log.info("Mazanie zaznamov pre importID={}", importID);
				dlg.getImportModify().deleteHardAll(auth, importID);
			}

			log.info("mazanie nepotrebnych zaznamov pri PAU");

			Set<Integer> zmenaEskalaciaIDs = dlg.getZmenaEskalaciaRead().zmenaEskalaciaIDs(auth);
			while (!zmenaEskalaciaIDs.isEmpty()) {

				if ((new Date().getTime() - startTime.getTime()) > _CudConsts.PROC_NOTIFY_DELAY) {
					if (!statusOKnotify()) {
						log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudUdrzbaDbProcess konci.");
						return;
					}
					startTime = new Date();
				}

				dlg.getZmenaEskalaciaMofify().deleteHard(auth, zmenaEskalaciaIDs.toArray(new Integer[zmenaEskalaciaIDs.size()]));
				zmenaEskalaciaIDs = dlg.getZmenaEskalaciaRead().zmenaEskalaciaIDs(auth);
			}

			Integer[] pluginKontrolaIDs = dlg.getPluginKontrolaRead().idsForDelete(auth);
			log.info("Pocet zaznamov pre hard delete kontroly pluginov {}", pluginKontrolaIDs.length);

			for (Integer pluginKontrolaID : pluginKontrolaIDs) {

				if ((new Date().getTime() - startTime.getTime()) > _CudConsts.PROC_NOTIFY_DELAY) {
					if (!statusOKnotify()) {
						log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudUdrzbaDbProcess konci.");
						return;
					}
					startTime = new Date();
				}

				log.info("Mazanie zaznamov pre pluginKontrolaID={}", pluginKontrolaID);
				dlg.getPluginKontrolaModify().deleteHardAll(auth, pluginKontrolaID);
			}

			// kontrola atributov CUD_WF_DEF.ID_SKUPINA a CUD_WF_DEF.SKUPINA_NAZOV s IAM
			if ((new Date().getTime() - startTime.getTime()) > _CudConsts.PROC_NOTIFY_DELAY) {
				if (!statusOKnotify()) {
					log.info("Bola prijata poziadavka na ukoncenie procesu, vykonavanie CudUdrzbaDbProcess konci.");
					return;
				}
				startTime = new Date();
			}

			log.info("Kontrola tabulky CUD_WF_DEF.");

			Map<Integer, String> iamSkupinaMap = iamSkupinaMap(auth);
			Map<Integer, DTOCiselnik> ciselnikMap = ciselnikMap(auth);

			Set<String> zlyNazovSet = new HashSet<String>();
			Set<String> zlyKodSet = new HashSet<String>();

			List<DTOWfDef> wfDefList = dlg.getWfDefRead().listLight(auth, (DTOWfDef) null);
			for (DTOWfDef dto : wfDefList) {

				DTOCiselnik dtoCis = ciselnikMap.get(dto.getIDCiselnik());
				if (!StringUtils.isValid(dtoCis)) {
					continue;
				}

				String skupinaNazov = iamSkupinaMap.get(dto.getIDSkupina());
				if (!StringUtils.isValid(skupinaNazov)) {
					zlyKodSet.add(dto.getIDSkupina() + " - " + dto.getSkupinaNazov());
					continue;
				}
				if (!skupinaNazov.equals(dto.getSkupinaNazov())) {
					zlyNazovSet.add(dto.getIDSkupina() + " - " + dto.getSkupinaNazov());
				}
			}

			if (!zlyKodSet.isEmpty() || !zlyNazovSet.isEmpty()) {
				log.info("Chyba v tabulke CUD_WF_DEF, posiela sa email.");
				sendNotif(zlyKodSet, zlyNazovSet);
			} else {
				log.info("Tabulka CUD_WF_DEF je ok.");
			}

		} catch (Exception e) {
			DBUtils.handleException(e, "process.error");
		}

		log.info("End - Som proces CudUdrzbaDbProcess a koncim");
	}

	private Map<Integer, DTOCiselnik> ciselnikMap(AuthInfo auth) throws AppException {

		try {
			DTOCiselnik dtoF = new DTOCiselnik();
			dtoF.setAktivny("T");
			DTOCiselnik[] listDTO = dlg.getCiselnikRead().listLight(auth, dtoF);

			Map<Integer, DTOCiselnik> resultMap = new HashMap<Integer, DTOCiselnik>();
			for (DTOCiselnik dto : listDTO) {
				resultMap.put(dto.getCiselnikID(), dto);
			}

			return resultMap;

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikMap.error");
			return null;
		}
	}

	private Map<Integer, String> iamSkupinaMap(AuthInfo auth) throws AppException {

		try {
			ListWraper<AuthSkupina> listWS = FrameworkUtils.getAuthMod().groupList(AuthInfo.system(), new Page(true), _CudConsts.ROLA_MODUL_CUD, "CUD ");
			Map<Integer, String> resultMap = new HashMap<Integer, String>();
			for (AuthSkupina dto : listWS.getList()) {
				resultMap.put(dto.getSkupinaID(), dto.getNazov());
			}

			return resultMap;

		} catch (Exception e) {
			DBUtils.handleException(e, "iamSkupinaMap.error");
			return null;
		}
	}

	private void sendNotif(Set<String> zlyNazovSet, Set<String> zlyKodSet) throws AppException {

		try {
			String lineSeparator = System.getProperty("line.separator");

			String errMsg1 = lineSeparator;
			for (String s : zlyKodSet) {
				errMsg1 += s + lineSeparator;
			}
			if (StringUtils.isValid(errMsg1)) {
				errMsg1 = "Nezname skupiny: " + System.getProperty("line.separator") + errMsg1;
			}

			String errMsg2 = lineSeparator;
			for (String s : zlyNazovSet) {
				errMsg2 += s + lineSeparator;
			}
			if (StringUtils.isValid(errMsg2)) {
				errMsg2 = "Nesedia nazvy skupin: " + lineSeparator + errMsg2;
			}

			String errorMsg = (errMsg1 + lineSeparator + errMsg2).trim();
			errorMsg = StringUtils.replaceAll(errorMsg, lineSeparator, "<br />");

			dlg.getWfNotif().sendNotifError("Chyba v tabulke CUD_WF_DEF", errorMsg);

		} catch (Throwable t) {
			DBUtils.handleException(t, "sendNotif.error");
		}
	}
	
	@Override
	protected String getLogName() {
		return "udrzba.cud";
	}
}
