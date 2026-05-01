package sk.ditec.cud.ws;

import javax.jws.WebService;

import sk.ditec.common.bi.Page;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.ws.AuthInfoWS;
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

@WebService(name = "CudWS", endpointInterface = "sk.ditec.cud.ws.CudWSRemote", portName = "CudWSPort", serviceName = "CudWSService", targetNamespace = "urn:ws.cud.zsr.ditec.sk")
public interface CudWSRemote {

	public DTOCiselnik[] ciselnikList(AuthInfoWS authWS, Page page, DTOCiselnik dtoF) throws AppException;

	public DTOCiselnik[] popCiselnikList(AuthInfoWS authWS, Page page, DTOCiselnik dtoF) throws AppException;

	public DTOCiselnik[] ciselnikListByKategoria(AuthInfoWS authWS, Page page, DTOCiselnik dtoF) throws AppException;

	public DTOCiselnik ciselnikRead(AuthInfoWS authWS, String tabulka) throws AppException;

	public DTOMainHead mainHeadRead(AuthInfoWS authWS, DTOMainHead dtoF) throws AppException;

	public DTOCiselnik ciselnikLoadData(AuthInfoWS authWS, DTOCiselnik dtoF) throws AppException;

	public String ciselnikUpdateKontrola(AuthInfoWS authWS, DTOCiselnik dto) throws AppException;

	public String ciselnikUpdate(AuthInfoWS authWS, DTOCiselnik dto) throws AppException;

	public String ciselnikDelete(AuthInfoWS authWS, Integer ciselnikID) throws AppException;

	public DTOCiselnikStlpec[] ciselnikStlpecList(AuthInfoWS authWS, Page page, DTOCiselnikStlpec dtoF) throws AppException;

	public DTOCiselnikStlpec[] popCiselnikStlpecList(AuthInfoWS authWS, Page page, DTOCiselnikStlpec dtoF) throws AppException;

	public String ciselnikStlpecReadByPrimaryKey(AuthInfoWS authWS, Integer ciselnikID) throws AppException;

	public String ciselnikStlpecGuiReadByPrimaryKey(AuthInfoWS authWS, Integer ciselnikID) throws AppException;

	public DTOCiselnikStlpecLD ciselnikStlpecLoadData(AuthInfoWS authWS, DTOCiselnikStlpecLD dtoF) throws AppException;

	public String ciselnikStlpecUpdate(AuthInfoWS authWS, DTOCiselnikStlpec dto) throws AppException;

	public String ciselnikStlpecDelete(AuthInfoWS authWS, Integer ciselnikID, Integer ciselnikStlpecID) throws AppException;

	public DTOCiselnikStlpec[] popUserTabColsList(AuthInfoWS authWS, DTOCiselnikStlpec dtoF) throws AppException;

	public DTOCiselnikGui[] ciselnikGuiList(AuthInfoWS authWS, Page page, DTOCiselnikGui dtoF) throws AppException;

	public DTOCiselnikGuiLD ciselnikGuiLoadData(AuthInfoWS authWS, DTOCiselnikGuiLD dto) throws AppException;

	public String ciselnikGuiUpdate(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException;

	public String ciselnikGuiPublishNew(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException;

	public String ciselnikGuiPublishActual(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException;

	public String ciselnikGuiDelete(AuthInfoWS authWS, Integer ciselnikGuiID) throws AppException;

	public DTOCiselnikStlpecGui[] ciselnikStlpecGuiList(AuthInfoWS authWS, Page page, DTOCiselnikStlpecGui dtoF) throws AppException;

	public DTODynCiselnikMeta dynCiselnikMetaRead(AuthInfoWS authWS, DTODynCiselnikMeta dtoF) throws AppException;

	public DTODynCiselnik[] dynCiselnikDataList(AuthInfoWS authWS, Page page, DTODynCiselnik dtoF) throws AppException;

	public DTODynCiselnik[] popDynCiselnikDataList(AuthInfoWS authWS, Page page, DTODynCiselnik dtoF) throws AppException;

	public String ciselnikStlpecGuiUpdate(AuthInfoWS authWS, DTOCiselnikStlpecGui dto) throws AppException;

	public String ciselnikStlpecGuiDelete(AuthInfoWS authWS, Integer ciselnikStlpecGuiID) throws AppException;

	public DTOWfDef[] wfDefList(AuthInfoWS authWS, Page page, DTOWfDef dtoF) throws AppException;

	public DTOWfTodo[] wfTodoList(AuthInfoWS authWS, Page page, DTOWfTodo dtoF) throws AppException;

	public DTOZmena[] zmenaListForDynCiselnikDetail(AuthInfoWS authWS, Page page, DTOZmena dtoF) throws AppException;

	public DTOFutPocetnostDynCiselnik[] futPocetnostListForDynCiselnikDetail(AuthInfoWS authWS, DTOFutPocetnostDynCiselnik dtoF) throws AppException;

	public DTOWfTodo[] popWfTodoList(AuthInfoWS authWS, DTOWfTodo dtoF) throws AppException;

	public String ciselnikGuiUpdateKontrola(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException;

	public String ciselnikGuiUpdateAndCopy(AuthInfoWS authWS, DTOCiselnikGui dto) throws AppException;

	public DTOCiselnikStlpecGuiLD ciselnikStlpecGuiLoadData(AuthInfoWS authWS, DTOCiselnikStlpecGuiLD dtoF) throws AppException;

	public DTOCiselnikStlpec ciselnikStlpecRead(AuthInfoWS authWS, Integer ciselnikStlpecID) throws AppException;

	public String ciselnikStlpecGuiUpdateKontrola(AuthInfoWS authWS, DTOCiselnikStlpecGui dto) throws AppException;

	public DTOKompatibilita[] ciselnikGuiKompatibilitaList(AuthInfoWS authWS, DTOKompatibilita dtoF) throws AppException;

	public DTOWfDef[] popWfDefList(AuthInfoWS authWS, Page page, DTOWfDef dtoF) throws AppException;

	public DTOSkupina[] popSkupinaList(AuthInfoWS authWS, DTOSkupina dtoF) throws AppException;

	public DTOWfDefLD wfDefLoadData(AuthInfoWS authWS, DTOWfDefLD dtoF) throws AppException;

	public String wfDefDelete(AuthInfoWS authWS, Integer wfDefID) throws AppException;

	public String wfDefUpdate(AuthInfoWS authWS, DTOWfDef dto) throws AppException;

	public String wfDefUpdateKontrola(AuthInfoWS authWS, DTOWfDef dto) throws AppException;

	public DTOWfTodo[] wfUlohaList(AuthInfoWS authWS, Page page, DTOWfTodo dtoF) throws AppException;

	public DTOFutDynCiselnik[] popFutListForDynCiselnik(AuthInfoWS authWS, DTOFutDynCiselnik dtoF, Page page) throws AppException;

	public DTOWfTodoLD wfUlohaLoadData(AuthInfoWS authWS, DTOWfTodoLD dtoF) throws AppException;

	public DTODynCiselnikLD dynCiselnikLoadData(AuthInfoWS authWS, DTODynCiselnikLD dtoF) throws AppException;

	public DTOImport dynCiselnikUpdateKontrola(AuthInfoWS authWS, DTOCiselnikStlpecGui[] metaPole, DTOImport dto) throws AppException;

	public String dynCiselnikUpdate(AuthInfoWS authWS, DTOImport dto, DTODynValue[] values, DTOCiselnikStlpecGui[] guiList) throws AppException;

	public String wfUlohaUpdate(AuthInfoWS authWS, DTOWfTodo dtoF, DTOZmenaStlpec[] zsPole, Integer histID) throws AppException;

	public String ciselnikDeleteKontrola(AuthInfoWS authWS, Integer ciselnikID) throws AppException;

	public DTOImport[] importList(AuthInfoWS authWS, Page page, DTOImport dtoF) throws AppException;

	public String importUpdate(AuthInfoWS authWS, DTOImport dto, DTOImportPriloha dtoPriloha) throws AppException;

	public String importDelete(AuthInfoWS authWS, Integer importID) throws AppException;

	public String importUpdateStav(AuthInfoWS authWS, Integer importID) throws AppException;

	public DTOImportPriloha importPrilohaRead(AuthInfoWS authWS, Integer importID) throws AppException;

	public DTOImportMsg[] popImportMsgList(AuthInfoWS authWS, Page page, DTOImportMsg dtoF) throws AppException;

	public DTOImportZmena[] popImportZmenaList(AuthInfoWS authWS, Page page, DTOImportZmena dtoF) throws AppException;

	public DTOImportZmenaStlpec[] popImportZmenaStlpecList(AuthInfoWS authWS, Page page, DTOImportZmenaStlpec dtoF) throws AppException;

	public DTOImportPriloha importTemplateRead(AuthInfoWS authWS, DTOImport dtoImport, DTOImportZmena dtoZmena) throws AppException;

	public DTOZmena[] zmenaList(AuthInfoWS authWS, Page page, DTOZmena dtoF) throws AppException;

	public DTOZmenaLD zmenaLoadData(AuthInfoWS authWS, DTOZmenaLD dtoF) throws AppException;

	public DTOUcet[] popUcetList(AuthInfoWS authWS, Page page, DTOUcet dtoF) throws AppException;

	public DTOZmena[] popZmenaList(AuthInfoWS authWS, Page page, DTOZmena dtoF) throws AppException;

	public String getConfigProperty(String group, String item) throws AppException;

	public DTODynCiselnikExport dynCiselnikExportPrintKontrola(AuthInfoWS authWS, DTODynCiselnikExport dtoF, DTODynCiselnik dtoDyn) throws AppException;

	public DTODynCiselnikExport dynCiselnikExportPrint(AuthInfoWS authWS, DTODynCiselnikExport dtoF, DTODynCiselnik dtoDyn) throws AppException;

	public DTOOdberatel[] odberatelList(AuthInfoWS authWS, Page page, DTOOdberatel dtoF) throws AppException;

	public DTOOdberatel odberatelLoadData(AuthInfoWS authWS, DTOOdberatel dtoF) throws AppException;

	public DTORola[] popRolaList(AuthInfoWS authWS, DTORola dtoF) throws AppException;

	public String odberatelUpdate(AuthInfoWS authWS, DTOOdberatel dto) throws AppException;

	public String odberatelDelete(AuthInfoWS authWS, Integer odberatelID) throws AppException;

	public DTOOdberatel[] popOdberatelList(AuthInfoWS authWS, Page page, DTOOdberatel dtoF) throws AppException;

	public DTOObjekt[] objektList(AuthInfoWS authWS, Page page, DTOObjekt dtoF) throws AppException;

	public DTOObjekt[] popObjektList(AuthInfoWS authWS, Page page, DTOObjekt dtoF) throws AppException;

	public DTOObjekt objektLoadData(AuthInfoWS authWS, DTOObjekt dtoF) throws AppException;

	public String objektUpdate(AuthInfoWS authWS, DTOObjekt dto) throws AppException;

	public String objektDelete(AuthInfoWS authWS, Integer objektID) throws AppException;

	public DTOOdberatelObjekt[] odberatelObjektList(AuthInfoWS authWS, Page page, DTOOdberatelObjekt dtoF) throws AppException;

	public String odberatelObjektJeAdmin(AuthInfoWS authWS) throws AppException;

	public DTOOdberatelObjekt[] odberatelObjektListPreOdberatel(AuthInfoWS authWS, Page page, DTOOdberatelObjekt dtoF) throws AppException;

	public DTOOdberatelObjektLD odberatelObjektLoadData(AuthInfoWS authWS, DTOOdberatelObjektLD dtoF) throws AppException;

	public String odberatelObjektUpdate(AuthInfoWS authWS, DTOOdberatelObjekt dto) throws AppException;

	public String odberatelObjektDelete(AuthInfoWS authWS, Integer odberatelObjektID) throws AppException;

	public DTOObjektCiselnik[] objektCiselnikListPreObjekt(AuthInfoWS authWS, DTOObjektCiselnik dtoF, DTOObjektCiselnik dto, DTOObjektCiselnik[] data) throws AppException;

	public DTOObjektCiselnik[] objektCiselnikList(AuthInfoWS authWS, Page page, DTOObjektCiselnik dtoF) throws AppException;

	public DTOObjektCiselnik objektCiselnikLoadData(AuthInfoWS authWS, DTOObjektCiselnik dtoF) throws AppException;

	public String objektCiselnikDelete(AuthInfoWS authWS, Integer objektCiselnikID) throws AppException;

	public DTOObjektStlpec[] objektStlpecList(AuthInfoWS authWS, DTOObjektStlpec dtoF, DTOObjektStlpec dto, DTOObjektStlpec[] dataList) throws AppException;

	public String odberatelDeleteKontrola(AuthInfoWS authWS, Integer odberatelID) throws AppException;

	public String odberatelUpdateKontrola(AuthInfoWS authWS, DTOOdberatel dto) throws AppException;

	public String objektDeleteKontrola(AuthInfoWS authWS, Integer objektID) throws AppException;

	public String objektUpdateKontrola(AuthInfoWS authWS, DTOObjekt dto) throws AppException;

	public String odberatelObjektUpdateKontrola(AuthInfoWS authWS, DTOOdberatelObjekt dto) throws AppException;

	public DTOPlugin[] pluginList(AuthInfoWS authWS, Page page, DTOPlugin dtoF) throws AppException;

	public DTOPluginStlpec[] pluginStlpecList(AuthInfoWS authWS, DTOPluginStlpec dtoF, DTOPluginStlpec dto, DTOPluginStlpec[] dataList) throws AppException;

	public DTOPluginStlpecLD pluginStlpecLoadData(AuthInfoWS authWS, DTOPluginStlpecLD dtoF) throws AppException;

	public String pluginUpdate(AuthInfoWS authWS, DTOPlugin dto) throws AppException;

	public String pluginDelete(AuthInfoWS authWS, Integer pluginID) throws AppException;

	public DTOPluginLD pluginLoadData(AuthInfoWS authWS, DTOPluginLD dtoF) throws AppException;

	public DTOPluginClassName[] popPluginClassNameList(AuthInfoWS authWS, DTOPluginClassName dtoF) throws AppException;

	public DTOPluginAlias[] popPluginAliassList(AuthInfoWS authWS, DTOPluginAlias dtoF) throws AppException;

	public String pluginUpdateKontrola(AuthInfoWS authWS, DTOPlugin dto) throws AppException;

	public DTODynCiselnik[] pluginDoplnenieLookupValues(AuthInfoWS authWS, DTOPlugin[] pluginList, DTODynCiselnik dtoF, DTOCiselnikStlpecGui dtoCS, DTOCiselnikStlpecGui[] metaList) throws AppException;

	public String pluginKontrolaListKontrola(AuthInfoWS authWS, DTOPluginKontrola dtoF) throws AppException;

	public DTOPluginKontrola[] pluginKontrolaList(AuthInfoWS authWS, Page page, DTOPluginKontrola dtoF) throws AppException;

	public DTOPluginKontrolaRow[] pluginKontrolaRowList(AuthInfoWS authWS, Page page, DTOPluginKontrolaRow dtoF) throws AppException;

	public String pluginKontrolaUpdate(AuthInfoWS authWS, DTOPluginKontrola dto) throws AppException;

	public String pluginKontrolaDelete(AuthInfoWS authWS, Integer pluginKontrolaID) throws AppException;

	public DTOPreklad[] prekladList(AuthInfoWS authWS, Page page, DTOPreklad dtoF) throws AppException;

	public DTOPrekladJazyk[] prekladJazykListLight(AuthInfoWS authWS) throws AppException;

	public DTOPrekladTabulka[] prekladTabulkakListLight(AuthInfoWS authWS) throws AppException;

	public DTOPrekladStlpec[] popPrekladStlpecListLight(AuthInfoWS authWS, Page page, DTOPrekladStlpec dtoF) throws AppException;

	public String prekladUpdateKontrola(AuthInfoWS authWS, DTOPreklad dto) throws AppException;

	public String prekladUpdate(AuthInfoWS authWS, DTOPreklad dto) throws AppException;

	public String prekladDelete(AuthInfoWS authWS, Integer prekladID) throws AppException;

	public DTOPrekladLD prekladLoadData(AuthInfoWS authWS, DTOPrekladLD dtoF) throws AppException;

	public DTOSubor suborRead(AuthInfoWS authWS, String tabulka, Integer suborID) throws AppException;

	public Integer suborUpdate(AuthInfoWS authWS, DTOSubor dto) throws AppException;

	public DTOSendSubor[] sendSuborList(AuthInfoWS authWS, Page page, DTOSendSubor dto) throws AppException;

	public String uzamknutieRowUpdate(AuthInfoWS authWS, DTOUzamknutie dto) throws AppException;

	public String uzamknutieRowDelete(AuthInfoWS authWS, DTOUzamknutie dto) throws AppException;

	public String uzamknutieCisUpdate(AuthInfoWS authWS, DTOUzamknutie dto) throws AppException;

	public String uzamknutieCisDelete(AuthInfoWS authWS, DTOUzamknutie dto) throws AppException;

	DTOSubor sendSuborDownload(AuthInfoWS authWS, String typ, Integer suborID) throws AppException;

	public Boolean existujeNeodoslanySubor(AuthInfoWS authWS, Integer idSend) throws AppException;

	public ActionResult opatovneOdoslanieSuboru(AuthInfoWS authWs, Integer dtoSendSubor, Integer dtoOdberatelObjekt) throws AppException;

	public DTOWfDefCiselnikStlpec[] wfDefCiselnikStlpecList(AuthInfoWS authWS, DTOWfDefCiselnikStlpec dtoF, DTOWfDefCiselnikStlpec dto, DTOWfDefCiselnikStlpec[] data) throws AppException;

}
