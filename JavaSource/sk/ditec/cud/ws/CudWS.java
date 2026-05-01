package sk.ditec.cud.ws;

import javax.jws.HandlerChain;
import javax.jws.WebService;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.common.ws.AuthInfoWS;
import sk.ditec.common.ws.WSUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOCiselnikGuiLD;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOCiselnikStlpecGuiLD;
import sk.ditec.cud.dto.DTOCiselnikStlpecLD;
import sk.ditec.cud.dto.DTODynCiselnik;
import sk.ditec.cud.dto.DTODynCiselnikExport;
import sk.ditec.cud.dto.DTODynCiselnikLD;
import sk.ditec.cud.dto.DTODynCiselnikMeta;
import sk.ditec.cud.dto.DTODynValue;
import sk.ditec.cud.dto.DTOFutDynCiselnik;
import sk.ditec.cud.dto.DTOFutPocetnostDynCiselnik;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOImportPriloha;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOKompatibilita;
import sk.ditec.cud.dto.DTOMainHead;
import sk.ditec.cud.dto.DTOObjekt;
import sk.ditec.cud.dto.DTOObjektCiselnik;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.dto.DTOOdberatel;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.dto.DTOOdberatelObjektLD;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOPluginAlias;
import sk.ditec.cud.dto.DTOPluginClassName;
import sk.ditec.cud.dto.DTOPluginKontrola;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.cud.dto.DTOPluginLD;
import sk.ditec.cud.dto.DTOPluginStlpec;
import sk.ditec.cud.dto.DTOPluginStlpecLD;
import sk.ditec.cud.dto.DTOPreklad;
import sk.ditec.cud.dto.DTOPrekladJazyk;
import sk.ditec.cud.dto.DTOPrekladLD;
import sk.ditec.cud.dto.DTOPrekladStlpec;
import sk.ditec.cud.dto.DTOPrekladTabulka;
import sk.ditec.cud.dto.DTORola;
import sk.ditec.cud.dto.DTOSendSubor;
import sk.ditec.cud.dto.DTOSkupina;
import sk.ditec.cud.dto.DTOSubor;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOUzamknutie;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfDefCiselnikStlpec;
import sk.ditec.cud.dto.DTOWfDefLD;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOWfTodoLD;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.dto.DTOZmenaLD;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;

@WebService(name = "CudWS", endpointInterface = "sk.ditec.cud.ws.CudWSRemote", portName = "CudWSPort", serviceName = "CudWSService", targetNamespace = "urn:ws.server.cud.zsr.ditec.sk")
@HandlerChain(file = "LogMessage_handler.xml")
public class CudWS extends HttpServlet implements CudWSRemote {

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	private Logger log = LoggerFactory.getLogger(CudWS.class);

	public DTOCiselnik[] ciselnikList(AuthInfoWS authWS, Page page, DTOCiselnik dtoF) throws AppException {

		try {
			log.info("Volanie metody: ciselnikList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikRead().listLight(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikList.error");
			return null;
		}
	}

	public DTOCiselnik[] popCiselnikList(AuthInfoWS authWS, Page page, DTOCiselnik dtoF) throws AppException {

		try {
			log.info("Volanie metody: popCiselnikList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikRead().listLight(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popCiselnikList.error");
			return null;
		}
	}

	public DTOCiselnik[] ciselnikListByKategoria(AuthInfoWS authWS, Page page, DTOCiselnik dtoF) throws AppException {

		try {
			log.info("Volanie metody: ciselnikListByKategoria(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikListByKategoria.error");
			return null;
		}
	}

	public DTOCiselnik ciselnikRead(AuthInfoWS authWS, String tabulka) throws AppException {

		try {
			log.info("Volanie metody: ciselnikRead(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikRead().readLight(auth, tabulka);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikRead.error");
			return null;
		}
	}

	public DTOCiselnik ciselnikLoadData(AuthInfoWS authWS, DTOCiselnik dtoF) throws AppException {

		try {
			log.info("Volanie metody: ciselnikLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikLoadData.error");
			return null;
		}
	}

	public String ciselnikUpdateKontrola(AuthInfoWS authWS, DTOCiselnik dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikRead().updateKontrola(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikUpdateKontrola.error");
			return null;
		}
	}

	public String ciselnikUpdate(AuthInfoWS authWS, DTOCiselnik dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikUpdate.error");
			return null;
		}
	}

	public String ciselnikDeleteKontrola(AuthInfoWS authWS, Integer ciselnikID) throws AppException {

		try {
			log.info("Volanie metody: ciselnikDeleteKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikRead().deleteKontrola(auth, ciselnikID);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikDeleteKontrola.error");
			return null;
		}
	}

	public String ciselnikDelete(AuthInfoWS authWS, Integer ciselnikID) throws AppException {

		try {
			log.info("Volanie metody: ciselnikDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikModify().delete(auth, ciselnikID);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikDelete.error");
			return null;
		}
	}

	public DTOCiselnikStlpec[] ciselnikStlpecList(AuthInfoWS authWS, Page page, DTOCiselnikStlpec dtoF) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecList.error");
			return null;
		}
	}

	public DTOCiselnikStlpec ciselnikStlpecRead(AuthInfoWS authWS, Integer ciselnikStlpecID) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecRead(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecRead().read(auth, ciselnikStlpecID);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecRead.error");
			return null;
		}
	}

	public DTOCiselnikStlpec[] popCiselnikStlpecList(AuthInfoWS authWS, Page page, DTOCiselnikStlpec dtoF) throws AppException {

		try {
			log.info("Volanie metody: popCiselnikStlpecList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecRead().listForPopup(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popCiselnikStlpecList.error");
			return null;
		}
	}

	public String ciselnikStlpecReadByPrimaryKey(AuthInfoWS authWS, Integer ciselnikID) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecReadByPrimaryKey(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecRead().readByPrimaryKey(auth, ciselnikID);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecReadByPrimaryKey.error");
			return null;
		}
	}

	public DTOCiselnikStlpecLD ciselnikStlpecLoadData(AuthInfoWS authWS, DTOCiselnikStlpecLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecLoadData.error");
			return null;
		}
	}

	public String ciselnikStlpecUpdate(AuthInfoWS authWS, DTOCiselnikStlpec dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecUpdate.error");
			return null;
		}
	}

	public String ciselnikStlpecDelete(AuthInfoWS authWS, Integer ciselnikID, Integer ciselnikStlpecID) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			String s = dlg.getCiselnikStlpecRead().deleteKontrola(auth, ciselnikID, ciselnikStlpecID);
			if (StringUtils.isValid(s)) {
				return s;
			}
			return dlg.getCiselnikStlpecModify().delete(auth, ciselnikStlpecID);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecDelete.error");
			return null;
		}
	}

	public DTOCiselnikStlpec[] popUserTabColsList(AuthInfoWS authWS, DTOCiselnikStlpec dtoF) throws AppException {

		try {
			log.info("Volanie metody: popUserTabColsList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getGuiRead().userTabColsListLight(auth, dtoF, true);

		} catch (Exception e) {
			DBUtils.handleException(e, "popUserTabColsList.error");
			return null;
		}
	}

	public DTOCiselnikGui[] ciselnikGuiList(AuthInfoWS authWS, Page page, DTOCiselnikGui dtoF) throws AppException {

		try {
			log.info("Volanie metody: ciselnikGuiList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikGuiRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikGuiList.error");
			return null;
		}
	}

	public DTOCiselnikGuiLD ciselnikGuiLoadData(AuthInfoWS authWS, DTOCiselnikGuiLD dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikGuiLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikGuiRead().loadData(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikGuiLoadData.error");
			return null;
		}
	}

	public String ciselnikStlpecGuiReadByPrimaryKey(AuthInfoWS authWS, Integer ciselnikID) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecGuiReadByPrimaryKey(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecRead().readByPrimaryKey(auth, ciselnikID);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecGuiReadByPrimaryKey.error");
			return null;
		}
	}

	public String ciselnikGuiUpdateKontrola(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikGuiUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikGuiRead().updateKontrola(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikGuiUpdateKontrola.error");
			return null;
		}
	}

	public String ciselnikGuiUpdate(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikGuiUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikGuiModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikGuiUpdate.error");
			return null;
		}
	}

	public String ciselnikGuiUpdateAndCopy(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikGuiUpdateAndCopy(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikGuiModify().updateAndCopy(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikGuiUpdateAndCopy.error");
			return null;
		}
	}

	public String ciselnikGuiPublishNew(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikGuiPublishNew(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikGuiModify().publishNew(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikGuiPublishNew.error");
			return null;
		}
	}

	public String ciselnikGuiPublishActual(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikGuiPublishActual(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikGuiModify().publishActual(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikGuiPublishActual.error");
			return null;
		}
	}

	public DTOKompatibilita[] ciselnikGuiKompatibilitaList(AuthInfoWS authWS, DTOKompatibilita dtoF) throws AppException {

		try {
			log.info("Volanie metody: ciselnikGuiKompatibilitaList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getKompatibilitaRead().kontrola(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikGuiKompatibilitaList.error");
			return null;
		}
	}

	public String ciselnikGuiDelete(AuthInfoWS authWS, Integer ciselnikGuiID) throws AppException {

		try {
			log.info("Volanie metody: ciselnikGuiDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikGuiModify().delete(auth, ciselnikGuiID);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikGuiDelete.error");
			return null;
		}
	}

	public DTOCiselnikStlpecGui[] ciselnikStlpecGuiList(AuthInfoWS authWS, Page page, DTOCiselnikStlpecGui dtoF) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecGuiList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecGuiRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecGuiList.error");
			return null;
		}
	}

	public DTODynCiselnikMeta dynCiselnikMetaRead(AuthInfoWS authWS, DTODynCiselnikMeta dtoF) throws AppException {

		try {
			log.info("Volanie metody: dynCiselnikMetaRead(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getGuiRead().metaRead(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "dynCiselnikMetaRead.error");
			return null;
		}
	}

	public DTOMainHead mainHeadRead(AuthInfoWS authWS, DTOMainHead dtoF) throws AppException {

		try {
			log.info("Volanie metody: mainHeadRead(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getGuiRead().mainHeadRead(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "mainHeadRead.error");
			return null;
		}
	}

	public DTODynCiselnik[] dynCiselnikDataList(AuthInfoWS authWS, Page page, DTODynCiselnik dtoF) throws AppException {

		try {
			log.info("Volanie metody: dynCiselnikDataList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getDynCiselnikRead().list(auth, page, dtoF, null);

		} catch (Exception e) {
			DBUtils.handleException(e, "dynCiselnikDataList.error");
			return null;
		}
	}

	public DTODynCiselnik[] popDynCiselnikDataList(AuthInfoWS authWS, Page page, DTODynCiselnik dtoF) throws AppException {

		try {
			log.info("Volanie metody: popDynCiselnikDataList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getDynCiselnikRead().list(auth, page, dtoF, null);

		} catch (Exception e) {
			DBUtils.handleException(e, "popDynCiselnikDataList.error");
			return null;
		}
	}

	public DTODynCiselnikExport dynCiselnikExportPrintKontrola(AuthInfoWS authWS, DTODynCiselnikExport dtoF, DTODynCiselnik dtoDyn) throws AppException {

		try {
			log.info("Volanie metody: dynCiselnikExportPrintKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getGuiRead().exportPrintKontrola(auth, dtoF, dtoDyn);

		} catch (Exception e) {
			DBUtils.handleException(e, "dynCiselnikExportPrintKontrola.error");
			return null;
		}
	}

	public DTODynCiselnikExport dynCiselnikExportPrint(AuthInfoWS authWS, DTODynCiselnikExport dtoF, DTODynCiselnik dtoDyn) throws AppException {

		try {
			log.info("Volanie metody: dynCiselnikExportPrint(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getGuiRead().exportPrint(auth, dtoF, dtoDyn);

		} catch (Exception e) {
			DBUtils.handleException(e, "dynCiselnikExportPrint.error");
			return null;
		}
	}

	public DTODynCiselnikLD dynCiselnikLoadData(AuthInfoWS authWS, DTODynCiselnikLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: dynCiselnikLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getDynCiselnikRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "dynCiselnikLoadData.error");
			return null;
		}
	}

	public DTOImport dynCiselnikUpdateKontrola(AuthInfoWS authWS, DTOCiselnikStlpecGui[] metaPole, DTOImport dto) throws AppException {

		try {
			log.info("Volanie metody: dynCiselnikUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getDynCiselnikRead().updateKontrola(auth, metaPole, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "dynCiselnikUpdateKontrola.error");
			return null;
		}
	}

	public String dynCiselnikUpdate(AuthInfoWS authWS, DTOImport dto, DTODynValue[] values, DTOCiselnikStlpecGui[] guiList) throws AppException {

		try {
			log.info("Volanie metody: dynCiselnikUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getDynCiselnikModify().update(auth, dto, values, guiList);

		} catch (Exception e) {
			DBUtils.handleException(e, "dynCiselnikUpdate.error");
			return null;
		}
	}

	public DTODynCiselnik[] pluginDoplnenieLookupValues(AuthInfoWS authWS, DTOPlugin[] pluginList, DTODynCiselnik dtoF, DTOCiselnikStlpecGui dtoCS, DTOCiselnikStlpecGui[] metaList) throws AppException {

		try {
			log.info("Volanie metody: pluginDoplnenieLookupValues(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginDoplnenie().lookupValues(auth, pluginList, dtoF, dtoCS, metaList);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginDoplnenieLookupValues.error");
			return null;
		}
	}

	public DTOCiselnikStlpecGuiLD ciselnikStlpecGuiLoadData(AuthInfoWS authWS, DTOCiselnikStlpecGuiLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecGuiLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecGuiRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecGuiLoadData.error");
			return null;
		}
	}

	public String ciselnikStlpecGuiUpdateKontrola(AuthInfoWS authWS, DTOCiselnikStlpecGui dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecGuiUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecGuiRead().updateKontrola(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecGuiUpdateKontrola.error");
			return null;
		}
	}

	public String ciselnikStlpecGuiUpdate(AuthInfoWS authWS, DTOCiselnikStlpecGui dto) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecGuiUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecGuiModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecGuiUpdate.error");
			return null;
		}
	}

	public String ciselnikStlpecGuiDelete(AuthInfoWS authWS, Integer ciselnikStlpecGuiID) throws AppException {

		try {
			log.info("Volanie metody: ciselnikStlpecGuiDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCiselnikStlpecGuiModify().delete(auth, ciselnikStlpecGuiID);

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecGuiDelete.error");
			return null;
		}
	}

	public DTOWfDef[] wfDefList(AuthInfoWS authWS, Page page, DTOWfDef dtoF) throws AppException {

		try {
			log.info("Volanie metody: wfDefList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfDefRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfDefList.error");
			return null;
		}
	}

	public DTOWfDef[] popWfDefList(AuthInfoWS authWS, Page page, DTOWfDef dtoF) throws AppException {

		try {
			log.info("Volanie metody: popWfDefList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfDefRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popWfDefList.error");
			return null;
		}
	}

	public DTOWfDefLD wfDefLoadData(AuthInfoWS authWS, DTOWfDefLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: wfDefLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfDefRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfDefLoadData.error");
			return null;
		}
	}

	public String wfDefUpdateKontrola(AuthInfoWS authWS, DTOWfDef dto) throws AppException {

		try {
			log.info("Volanie metody: wfDefUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfDefRead().updateKontrola(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfDefUpdate.error");
			return null;
		}
	}

	public String wfDefUpdate(AuthInfoWS authWS, DTOWfDef dto) throws AppException {

		try {
			log.info("Volanie metody: wfDefUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfDefModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfDefUpdate.error");
			return null;
		}
	}

	public String wfDefDelete(AuthInfoWS authWS, Integer wfDefID) throws AppException {

		try {
			log.info("Volanie metody: wfDefDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfDefModify().delete(auth, wfDefID);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfDefDelete.error");
			return null;
		}
	}

	public DTOWfDefCiselnikStlpec[] wfDefCiselnikStlpecList(AuthInfoWS authWS, DTOWfDefCiselnikStlpec dtoF, DTOWfDefCiselnikStlpec dto, DTOWfDefCiselnikStlpec[] data) throws AppException {

		try {
			log.info("Volanie metody: wfDefCiselnikStlpecList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfDefCiselnikStlpecRead().list(auth, dtoF, dto, data);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfDefCiselnikStlpecList.error");
			return null;
		}
	}

	public DTOSkupina[] popSkupinaList(AuthInfoWS authWS, DTOSkupina dtoF) throws AppException {

		try {
			log.info("Volanie metody: popSkupinaList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getIam().skupinaList(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popSkupinaList.error");
			return null;
		}
	}

	public DTOWfTodoLD wfUlohaLoadData(AuthInfoWS authWS, DTOWfTodoLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: wfUlohaLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfTodoRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfUlohaLoadData.error");
			return null;
		}
	}

	public DTOWfTodo[] wfUlohaList(AuthInfoWS authWS, Page page, DTOWfTodo dtoF) throws AppException {

		try {
			log.info("Volanie metody: wfUlohaList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfTodoRead().ulohaList(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfUlohaList.error");
			return null;
		}
	}

	public String wfUlohaUpdate(AuthInfoWS authWS, DTOWfTodo dtoF, DTOZmenaStlpec[] zsPole, Integer histID) throws AppException {

		try {
			log.info("Volanie metody: wfUlohaUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfTodoModify().ulohaUpdate(auth, dtoF, zsPole, histID);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfUlohaUpdate.error");
			return null;
		}
	}

	public DTOUcet[] popUcetList(AuthInfoWS authWS, Page page, DTOUcet dtoF) throws AppException {

		try {
			log.info("Volanie metody: popUcetList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getIam().ucetList(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popUcetList.error");
			return null;
		}
	}

	public DTOWfTodo[] popWfTodoList(AuthInfoWS authWS, DTOWfTodo dtoF) throws AppException {

		try {
			log.info("Volanie metody: popWfTodoList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getGuiRead().wfTodoList(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popWfTodoList.error");
			return null;
		}
	}

	public DTOWfTodo[] wfTodoList(AuthInfoWS authWS, Page page, DTOWfTodo dtoF) throws AppException {

		try {
			log.info("Volanie metody: wfTodoList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getWfTodoRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "wfTodoList.error");
			return null;
		}
	}

	public DTOZmena[] zmenaListForDynCiselnikDetail(AuthInfoWS authWS, Page page, DTOZmena dtoF) throws AppException {

		try {
			log.info("Volanie metody: zmenaListForDynCiselnikDetail(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getZmenaRead().listForDynCiselnikDetail(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "zmenaListForDynCiselnik.error");
			return null;
		}
	}

	public DTOFutPocetnostDynCiselnik[] futPocetnostListForDynCiselnikDetail(AuthInfoWS authWS, DTOFutPocetnostDynCiselnik dtoF) throws AppException {

		try {
			log.info("Volanie metody: futPocetnostListForDynCiselnikDetail(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getGuiRead().futPocetnostList(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "futPocetnostListForDynCiselnikDetail.error");
			return null;
		}
	}

	public DTOFutDynCiselnik[] popFutListForDynCiselnik(AuthInfoWS authWS, DTOFutDynCiselnik dtoF, Page page) throws AppException {

		try {
			log.info("Volanie metody: popFutListForDynCiselnik(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getDynCiselnikRead().futListForDynCiselnik(auth, dtoF, page);

		} catch (Exception e) {
			DBUtils.handleException(e, "popFutListForDynCiselnik.error");
			return null;
		}
	}

	public DTOImport[] importList(AuthInfoWS authWS, Page page, DTOImport dtoF) throws AppException {

		try {
			log.info("Volanie metody: importList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getImportRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "importList.error");
			return null;
		}
	}

	public String importUpdate(AuthInfoWS authWS, DTOImport dto, DTOImportPriloha dtoPriloha) throws AppException {

		try {
			log.info("Volanie metody: importUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getImportModify().update(auth, dto, dtoPriloha);

		} catch (Exception e) {
			DBUtils.handleException(e, "importUpdate.error");
			return null;
		}
	}

	public String importDelete(AuthInfoWS authWS, Integer importID) throws AppException {

		try {
			log.info("Volanie metody: importDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getImportModify().delete(auth, importID);

		} catch (Exception e) {
			DBUtils.handleException(e, "importDelete.error");
			return null;
		}
	}

	public String importUpdateStav(AuthInfoWS authWS, Integer importID) throws AppException {

		try {
			log.info("Volanie metody: importUpdateStav(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			String s = dlg.getImportRead().updateStavToKontrola(auth, importID);
			if (StringUtils.isValid(s)) {
				return s;
			}
			return dlg.getImportModify().updateStav(auth, importID);

		} catch (Exception e) {
			DBUtils.handleException(e, "importUpdateStav.error");
			return null;
		}
	}

	public DTOImportPriloha importPrilohaRead(AuthInfoWS authWS, Integer importID) throws AppException {

		try {
			log.info("Volanie metody: importPrilohaRead(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getImportPrilohaRead().readLight(auth, importID);

		} catch (Exception e) {
			DBUtils.handleException(e, "importPrilohaRead.error");
			return null;
		}
	}

	public DTOImportMsg[] popImportMsgList(AuthInfoWS authWS, Page page, DTOImportMsg dtoF) throws AppException {

		try {
			log.info("Volanie metody: popImportMsgList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getImportMsgRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popImportMsgList.error");
			return null;
		}
	}

	public DTOImportZmena[] popImportZmenaList(AuthInfoWS authWS, Page page, DTOImportZmena dtoF) throws AppException {

		try {
			log.info("Volanie metody: popImportZmenaList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getImportZmenaRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popImportZmenaList.error");
			return null;
		}
	}

	public DTOImportZmenaStlpec[] popImportZmenaStlpecList(AuthInfoWS authWS, Page page, DTOImportZmenaStlpec dtoF) throws AppException {

		try {
			log.info("Volanie metody: popImportZmenaStlpecList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getImportZmenaStlpecRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popImportZmenaStlpecList.error");
			return null;
		}
	}

	public DTOImportPriloha importTemplateRead(AuthInfoWS authWS, DTOImport dtoImport, DTOImportZmena dtoZmena) throws AppException {

		try {
			log.info("Volanie metody: importTemplateRead(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getImportPrilohaRead().templateRead(auth, dtoImport, dtoZmena);

		} catch (Exception e) {
			DBUtils.handleException(e, "importTemplateRead.error");
			return null;
		}
	}

	public DTOZmena[] popZmenaList(AuthInfoWS authWS, Page page, DTOZmena dtoF) throws AppException {

		try {
			log.info("Volanie metody: popZmenaList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getZmenaRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popZmenaList.error");
			return null;
		}
	}

	public DTOZmena[] zmenaList(AuthInfoWS authWS, Page page, DTOZmena dtoF) throws AppException {

		try {
			log.info("Volanie metody: zmenaList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getZmenaRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "zmenaList.error");
			return null;
		}
	}

	public DTOZmenaLD zmenaLoadData(AuthInfoWS authWS, DTOZmenaLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: zmenaLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getZmenaRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "zmenaLoadData.error");
			return null;
		}
	}

	public String getConfigProperty(String group, String item) throws AppException {

		try {
			log.info("Volanie metody: getConfigProperty()");
			return FrameworkUtils.getConfigProperty(group, item);

		} catch (Exception e) {
			DBUtils.handleException(e, "getConfigProperty.error");
			return null;
		}
	}

	public DTOOdberatel[] odberatelList(AuthInfoWS authWS, Page page, DTOOdberatel dtoF) throws AppException {

		try {
			log.info("Volanie metody: odberatelList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelRead().listForList(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelList.error");
			return null;
		}
	}

	public DTOOdberatel[] popOdberatelList(AuthInfoWS authWS, Page page, DTOOdberatel dtoF) throws AppException {

		try {
			log.info("Volanie metody: popOdberatelList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelRead().listForPop(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popOdberatelList.error");
			return null;
		}
	}

	public DTOOdberatel odberatelLoadData(AuthInfoWS authWS, DTOOdberatel dtoF) throws AppException {

		try {
			log.info("Volanie metody: odberatelLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelLoadData.error");
			return null;
		}
	}

	public DTORola[] popRolaList(AuthInfoWS authWS, DTORola dtoF) throws AppException {

		try {
			log.info("Volanie metody: popRolaList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getIam().rolaList(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popRolaList.error");
			return null;
		}
	}

	public String odberatelUpdate(AuthInfoWS authWS, DTOOdberatel dto) throws AppException {

		try {
			log.info("Volanie metody: odberatelUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelUpdate.error");
			return null;
		}
	}

	public String odberatelUpdateKontrola(AuthInfoWS authWS, DTOOdberatel dto) throws AppException {

		try {
			log.info("Volanie metody: odberatelUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelRead().updateKontrola(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelUpdateKontrola.error");
			return null;
		}
	}

	public String odberatelDelete(AuthInfoWS authWS, Integer odberatelID) throws AppException {

		try {
			log.info("Volanie metody: odberatelDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelModify().delete(auth, odberatelID);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelDelete.error");
			return null;
		}
	}

	public String odberatelDeleteKontrola(AuthInfoWS authWS, Integer odberatelID) throws AppException {

		try {
			log.info("Volanie metody: odberatelDeleteKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelRead().deleteKontrola(auth, odberatelID);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelDeleteKontrola.error");
			return null;
		}
	}

	public DTOObjekt[] objektList(AuthInfoWS authWS, Page page, DTOObjekt dtoF) throws AppException {

		try {
			log.info("Volanie metody: objektList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektRead().listForList(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektList.error");
			return null;
		}
	}

	public DTOObjekt[] popObjektList(AuthInfoWS authWS, Page page, DTOObjekt dtoF) throws AppException {

		try {
			log.info("Volanie metody: popObjektList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektRead().listForPop(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popObjektList.error");
			return null;
		}
	}

	public DTOObjekt objektLoadData(AuthInfoWS authWS, DTOObjekt dtoF) throws AppException {

		try {
			log.info("Volanie metody: objektLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektLoadData.error");
			return null;
		}
	}

	public String objektUpdate(AuthInfoWS authWS, DTOObjekt dto) throws AppException {

		try {
			log.info("Volanie metody: objektUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektUpdate.error");
			return null;
		}
	}

	public String objektUpdateKontrola(AuthInfoWS authWS, DTOObjekt dto) throws AppException {

		try {
			log.info("Volanie metody: objektUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektRead().updateKontrola(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektUpdateKontrola.error");
			return null;
		}
	}

	public String objektDelete(AuthInfoWS authWS, Integer objektID) throws AppException {

		try {
			log.info("Volanie metody: objektDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektModify().delete(auth, objektID);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektDelete.error");
			return null;
		}
	}

	public String objektDeleteKontrola(AuthInfoWS authWS, Integer objektID) throws AppException {

		try {
			log.info("Volanie metody: objektDeleteKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektRead().deleteKontrola(auth, objektID);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektDeleteKontrola.error");
			return null;
		}
	}

	public DTOOdberatelObjekt[] odberatelObjektList(AuthInfoWS authWS, Page page, DTOOdberatelObjekt dtoF) throws AppException {

		try {
			log.info("Volanie metody: odberatelObjektList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelObjektRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelObjektList.error");
			return null;
		}
	}

	public String odberatelObjektJeAdmin(AuthInfoWS authWS) throws AppException {

		try {
			log.info("Volanie metody: odberatelObjektJeAdmin(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getIam().jeAdmin(auth);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelObjektJeAdmin.error");
			return null;
		}
	}

	public DTOOdberatelObjekt[] odberatelObjektListPreOdberatel(AuthInfoWS authWS, Page page, DTOOdberatelObjekt dtoF) throws AppException {

		try {
			log.info("Volanie metody: odberatelObjektListPreOdberatel(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelObjektRead().listForOdbebratel(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelObjektListPreOdberatel.error");
			return null;
		}
	}

	public DTOOdberatelObjektLD odberatelObjektLoadData(AuthInfoWS authWS, DTOOdberatelObjektLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: odberatelObjektLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelObjektRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelObjektLoadData.error");
			return null;
		}
	}

	public String odberatelObjektUpdate(AuthInfoWS authWS, DTOOdberatelObjekt dto) throws AppException {

		try {
			log.info("Volanie metody: odberatelObjektUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelObjektModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelObjektUpdate.error");
			return null;
		}
	}

	public String odberatelObjektUpdateKontrola(AuthInfoWS authWS, DTOOdberatelObjekt dto) throws AppException {

		try {
			log.info("Volanie metody: odberatelObjektUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelObjektRead().updateKontrola(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelObjektUpdateKontrola.error");
			return null;
		}
	}

	public String odberatelObjektDelete(AuthInfoWS authWS, Integer odberatelObjektID) throws AppException {

		try {
			log.info("Volanie metody: odberatelObjektDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getOdberatelObjektModify().delete(auth, odberatelObjektID);

		} catch (Exception e) {
			DBUtils.handleException(e, "odberatelObjektDelete.error");
			return null;
		}
	}

	public DTOObjektCiselnik[] objektCiselnikListPreObjekt(AuthInfoWS authWS, DTOObjektCiselnik dtoF, DTOObjektCiselnik dto, DTOObjektCiselnik[] data) throws AppException {

		try {
			log.info("Volanie metody: objektCiselnikListPreObjekt(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektCiselnikRead().list(auth, dtoF, dto, data);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektCiselnikListPreObjekt.error");
			return null;
		}
	}

	public DTOObjektCiselnik[] objektCiselnikList(AuthInfoWS authWS, Page page, DTOObjektCiselnik dtoF) throws AppException {

		try {
			log.info("Volanie metody: objektCiselnikList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektCiselnikRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektCiselnikList.error");
			return null;
		}
	}

	public String objektCiselnikDelete(AuthInfoWS authWS, Integer objektCiselnikID) throws AppException {

		try {
			log.info("Volanie metody: objektCiselnikDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektCiselnikModify().delete(auth, objektCiselnikID);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektCiselnikDelete.error");
			return null;
		}
	}

	public DTOObjektCiselnik objektCiselnikLoadData(AuthInfoWS authWS, DTOObjektCiselnik dtoF) throws AppException {

		try {
			log.info("Volanie metody: objektCiselnikLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektCiselnikRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektCiselnikLoadData.error");
			return null;
		}
	}

	public DTOObjektStlpec[] objektStlpecList(AuthInfoWS authWS, DTOObjektStlpec dtoF, DTOObjektStlpec dto, DTOObjektStlpec[] dataList) throws AppException {

		try {
			log.info("Volanie metody: objektStlpecList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getObjektStlpecRead().list(auth, dtoF, dto, dataList);

		} catch (Exception e) {
			DBUtils.handleException(e, "objektStlpecList.error");
			return null;
		}
	}

	public DTOPlugin[] pluginList(AuthInfoWS authWS, Page page, DTOPlugin dtoF) throws AppException {

		try {
			log.info("Volanie metody: pluginList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginList.error");
			return null;
		}
	}

	public DTOPluginLD pluginLoadData(AuthInfoWS authWS, DTOPluginLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: pluginLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginLoadData.error");
			return null;
		}
	}

	public String pluginUpdateKontrola(AuthInfoWS authWS, DTOPlugin dto) throws AppException {

		try {
			log.info("Volanie metody: pluginUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginRead().updateKontrola(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginUpdateKontrola.error");
			return null;
		}
	}

	public String pluginUpdate(AuthInfoWS authWS, DTOPlugin dto) throws AppException {

		try {
			log.info("Volanie metody: pluginUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginUpdate.error");
			return null;
		}
	}

	public String pluginDelete(AuthInfoWS authWS, Integer pluginID) throws AppException {

		try {
			log.info("Volanie metody: pluginDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginModify().delete(auth, pluginID);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginDelete.error");
			return null;
		}
	}

	public DTOPluginStlpec[] pluginStlpecList(AuthInfoWS authWS, DTOPluginStlpec dtoF, DTOPluginStlpec dto, DTOPluginStlpec[] dataList) throws AppException {

		try {
			log.info("Volanie metody: pluginStlpecList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginStlpecRead().list(auth, dtoF, dto, dataList);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginStlpecList.error");
			return null;
		}
	}

	public DTOPluginStlpecLD pluginStlpecLoadData(AuthInfoWS authWS, DTOPluginStlpecLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: pluginStlpecLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginStlpecRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginStlpecLoadData.error");
			return null;
		}
	}

	public DTOPluginClassName[] popPluginClassNameList(AuthInfoWS authWS, DTOPluginClassName dtoF) throws AppException {

		try {
			log.info("Volanie metody: popPluginClassNameList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginClassNameRead().list(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popPluginClassNameList.error");
			return null;
		}
	}

	public DTOPluginAlias[] popPluginAliassList(AuthInfoWS authWS, DTOPluginAlias dtoF) throws AppException {

		try {
			log.info("Volanie metody: popPluginAliassList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginAliassRead().list(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popPluginAliassList.error");
			return null;
		}
	}

	public String pluginKontrolaListKontrola(AuthInfoWS authWS, DTOPluginKontrola dtoF) throws AppException {

		try {
			log.info("Volanie metody: pluginKontrolaListKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginKontrolaRead().kontrola(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginKontrolaListKontrola.error");
			return null;
		}
	}

	public DTOPluginKontrola[] pluginKontrolaList(AuthInfoWS authWS, Page page, DTOPluginKontrola dtoF) throws AppException {

		try {
			log.info("Volanie metody: pluginKontrolaList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginKontrolaRead().listLight(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginKontrolaList.error");
			return null;
		}
	}

	public String pluginKontrolaUpdate(AuthInfoWS authWS, DTOPluginKontrola dto) throws AppException {

		try {
			log.info("Volanie metody: pluginKontrolaUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginKontrolaModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginKontrolaUpdate.error");
			return null;
		}
	}

	public String pluginKontrolaDelete(AuthInfoWS authWS, Integer pluginKontrolaID) throws AppException {

		try {
			log.info("Volanie metody: pluginKontrolaDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginKontrolaModify().delete(auth, pluginKontrolaID);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginKontrolaDelete.error");
			return null;
		}
	}

	public DTOPluginKontrolaRow[] pluginKontrolaRowList(AuthInfoWS authWS, Page page, DTOPluginKontrolaRow dtoF) throws AppException {

		try {
			log.info("Volanie metody: pluginKontrolaRowList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPluginKontrolaRowRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "pluginKontrolaRowList.error");
			return null;
		}
	}

	public DTOPreklad[] prekladList(AuthInfoWS authWS, Page page, DTOPreklad dtoF) throws AppException {

		try {
			log.info("Volanie metody: prekladList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPrekladRead().list(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "prekladList.error");
			return null;
		}
	}

	public DTOPrekladJazyk[] prekladJazykListLight(AuthInfoWS authWS) throws AppException {

		try {
			log.info("Volanie metody: prekladJazykListLight(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPrekladJazykRead().listLight(auth);

		} catch (Exception e) {
			DBUtils.handleException(e, "prekladJazykListLight.error");
			return null;
		}
	}

	public DTOPrekladTabulka[] prekladTabulkakListLight(AuthInfoWS authWS) throws AppException {

		try {
			log.info("Volanie metody: prekladTabulkakListLight(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPrekladTabulkaRead().listLight(auth);

		} catch (Exception e) {
			DBUtils.handleException(e, "prekladTabulkakListLight.error");
			return null;
		}
	}

	public DTOPrekladStlpec[] popPrekladStlpecListLight(AuthInfoWS authWS, Page page, DTOPrekladStlpec dtoF) throws AppException {

		try {
			log.info("Volanie metody: popPrekladStlpecListLight(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPrekladStlpecRead().listLight(auth, page, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "popPrekladStlpecListLight.error");
			return null;
		}
	}

	public String prekladUpdateKontrola(AuthInfoWS authWS, DTOPreklad dto) throws AppException {

		try {
			log.info("Volanie metody: prekladUpdateKontrola(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPrekladRead().updateKontrola(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "prekladUpdateKontrola.error");
			return null;
		}
	}

	public String prekladUpdate(AuthInfoWS authWS, DTOPreklad dto) throws AppException {

		try {
			log.info("Volanie metody: prekladUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPrekladModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "prekladUpdate.error");
			return null;
		}
	}

	public String prekladDelete(AuthInfoWS authWS, Integer prekladID) throws AppException {

		try {
			log.info("Volanie metody: prekladDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPrekladModify().delete(auth, prekladID);

		} catch (Exception e) {
			DBUtils.handleException(e, "prekladDelete.error");
			return null;
		}
	}

	public DTOPrekladLD prekladLoadData(AuthInfoWS authWS, DTOPrekladLD dtoF) throws AppException {

		try {
			log.info("Volanie metody: prekladLoadData(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getPrekladRead().loadData(auth, dtoF);

		} catch (Exception e) {
			DBUtils.handleException(e, "prekladLoadData.error");
			return null;
		}
	}

	public DTOSubor suborRead(AuthInfoWS authWS, String tabulka, Integer suborID) throws AppException {

		try {
			log.info("Volanie metody: suborRead(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getDynCiselnikRead().suborReadLight(auth, tabulka, suborID);

		} catch (Exception e) {
			DBUtils.handleException(e, "suborRead.error");
			return null;
		}
	}

	public Integer suborUpdate(AuthInfoWS authWS, DTOSubor dto) throws AppException {

		try {
			log.info("Volanie metody: suborUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getDynCiselnikModify().suborUpdate(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "suborUpdate.error");
			return null;
		}
	}

	public String uzamknutieRowUpdate(AuthInfoWS authWS, DTOUzamknutie dto) throws AppException {

		try {
			log.info("Volanie metody: uzamknutieRowUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getUzamknutieModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "uzamknutieRowUpdate.error");
			return null;
		}
	}

	public String uzamknutieRowDelete(AuthInfoWS authWS, DTOUzamknutie dto) throws AppException {

		try {
			log.info("Volanie metody: uzamknutieRowDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getUzamknutieModify().deleteRow(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "uzamknutieRowDelete.error");
			return null;
		}
	}

	public String uzamknutieCisUpdate(AuthInfoWS authWS, DTOUzamknutie dto) throws AppException {

		try {
			log.info("Volanie metody: uzamknutieCisUpdate(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			String s = dlg.getUzamknutieRead().cisUpdateKontrola(auth, dto);
			if (StringUtils.isValid(s)) {
				return s;
			}
			return dlg.getUzamknutieModify().update(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "uzamknutieCisUpdate.error");
			return null;
		}
	}

	public String uzamknutieCisDelete(AuthInfoWS authWS, DTOUzamknutie dto) throws AppException {

		try {
			log.info("Volanie metody: uzamknutieCisDelete(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			String s = dlg.getUzamknutieRead().cisDeleteKontrola(auth, dto);
			if (StringUtils.isValid(s)) {
				return s;
			}
			return dlg.getUzamknutieModify().deleteCis(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "uzamknutieCisDelete.error");
			return null;
		}
	}

	@Override
	public DTOSendSubor[] sendSuborList(AuthInfoWS authWS, Page page, DTOSendSubor dto) throws AppException {
		try {
			log.info("Volanie metody: sendSuborList(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCudSendSuborRead().listForList(auth, page, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "sendSuborList.error");
			return null;
		}
	}

	@Override
	public DTOSubor sendSuborDownload(AuthInfoWS authWS, String typ, Integer suborID) throws AppException {

		try {
			log.info("Volanie metody: sendSuborDownload(" + typ + "), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCudSendSuborRead().sendSuborDownload(auth, typ, suborID);

		} catch (Exception e) {
			DBUtils.handleException(e, "suborRead.error");
			return null;
		}
	}

	@Override
	public Boolean existujeNeodoslanySubor(AuthInfoWS authWS, Integer idSend) throws AppException {
		try {
			log.info("Volanie metody: existujeNeodoslanySubor(), accountName: " + authWS.getAccountName());

			AuthInfo auth = WSUtils.toAuth(authWS);
			return dlg.getCudSendSuborRead().existujeNeodoslanySubor(auth, idSend);

		} catch (Exception e) {
			DBUtils.handleException(e, "existujeNeodoslanySubor.error");
			return null;
		}
	}

	@Override
	public ActionResult opatovneOdoslanieSuboru(AuthInfoWS authWS, Integer dtoSendSuborId, Integer dtoOdberatelObjektId) throws AppException {
		try {
			log.info("Volanie metody: opatovneOdoslanieSuboru(), accountName: " + authWS.getAccountName());
			AuthInfo auth = WSUtils.toAuth(authWS);

			DTOSendSubor dtoSendSubor = dlg.getCudSendSuborRead().vratSuborPreExport(auth, dtoSendSuborId);
			DTOOdberatelObjekt dtoF = new DTOOdberatelObjekt();
			dtoF.setOdberatelObjektID(dtoOdberatelObjektId);
			DTOOdberatelObjekt dtoOdberatelObjekt = dlg.getOdberatelObjektRead().list(auth, new Page(), dtoF)[0];
			ActionResult result = dlg.getCudSendSuborModify().opatovneOdoslanieSuboru(auth, dtoSendSubor, dtoOdberatelObjekt);
			return result;
		} catch (Exception e) {
			DBUtils.handleException(e, "opatovneOdoslanieSuboru.error");
			return null;
		}

	}

}
