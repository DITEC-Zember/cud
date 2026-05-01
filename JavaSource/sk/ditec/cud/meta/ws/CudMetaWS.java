package sk.ditec.cud.meta.ws;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.meta.ws.dto.DTOCiselnikWS;
import sk.ditec.cud.meta.ws.dto.DTOOpravneniaListResponse;
import sk.ditec.cud.meta.ws.dto.DTOUpdCiselnikMetaResponse;
import sk.ditec.cud.meta.ws.dto.DTOUpdCiselnikMetaWS;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.zsr.common.server.auth.ZSRAuthInfo;

@WebService(name = "CudMetaWS", endpointInterface = "sk.ditec.cud.meta.ws.CudMetaWS", portName = "CudMetaWSPort", serviceName = "CudMetaWSService", targetNamespace = "urn:ws.meta.cud.ditec.sk")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@HandlerChain(file = "LogMessage_handler.xml")
public class CudMetaWS {

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	private Logger log = LoggerFactory.getLogger(CudMetaWS.class);

	@Resource
	WebServiceContext wsContext;

	private ZSRAuthInfo getAuthInfo() {

		try {
			MessageContext msgContext = wsContext.getMessageContext();
			HttpServletRequest request = (HttpServletRequest) msgContext.get(MessageContext.SERVLET_REQUEST);

			ZSRAuthInfo auth = (ZSRAuthInfo) request.getSession().getAttribute("authInfo");
			auth.setPerms(FrameworkUtils.getAuthMod().getPermissionList(auth.getAccountId()));

			return auth;

		} catch (Exception e) {
			log.error("getAuthInfo.error", e);
			return null;
		}
	}

	@WebMethod(operationName = "getMetaList")
	public DTOCiselnikWS[] getMetaList() throws AppException {
		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getMetaList(), accountName: " + auth.getAccountName());
			DTOCiselnikWS[] response = dlg.getMetaWS().getMetaList(auth);
			return response;
		} catch (Exception e) {
			log.error("getMetaList", e);
			DBUtils.handleException(e, "getMetaList.error");
			return null;
		}
	}

	@WebMethod(operationName = "getOpravneniaList")
	public DTOOpravneniaListResponse getOpravneniaList(@WebParam(name = "tabulka") String[] tabulkaList, @WebParam(name = "typPristupu") String typPristupu) throws AppException {
		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getOpravneniaList(), accountName: " + auth.getAccountName());
			DTOOpravneniaListResponse response = dlg.getMetaWS().getOpravneniaList(auth, tabulkaList, typPristupu);
			return response;
		} catch (Exception e) {
			log.error("getOpravneniaList", e);
			DBUtils.handleException(e, "getOpravneniaList.error");
			return null;
		}
	}

	@WebMethod(operationName = "getMeta")
	public DTOCiselnikWS getMeta(@WebParam(name = "tabulka") Integer ciselnikId) throws AppException {
		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getMeta(), accountName: " + auth.getAccountName());
			DTOCiselnikWS response = dlg.getMetaWS().getMeta(auth, ciselnikId);
			return response;
		}catch (Exception e) {
			log.error("getMeta", e);
			DBUtils.handleException(e, "getMeta.error");
			return null;
		}
	}

	@WebMethod(operationName = "updCiselnikMeta")
	public DTOUpdCiselnikMetaResponse updCiselnikMeta(@WebParam(name = "updCiselnikMeta") DTOUpdCiselnikMetaWS dtoUpdCiselnikMetaWS) throws AppException {
		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: updCiselnikMeta(), accountName: " + auth.getAccountName());
			DTOUpdCiselnikMetaResponse response = dlg.getMetaWS().updCiselnikMeta(auth, dtoUpdCiselnikMetaWS);
			return response;
		} catch (Exception e) {
			log.error("updCiselnikMeta", e);
			DBUtils.handleException(e, "updCiselnikMeta.error");
			return null;
		}
	}

}
