package sk.ditec.cskmd.data.ws;

import java.util.Date;

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
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cskmd.data.ws.dto.DTOCiselnikDataWS;
import sk.ditec.cskmd.data.ws.dto.DTOCiselnikStlpecWS;
import sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS;
import sk.ditec.cskmd.data.ws.dto.DTOPageWS;
import sk.ditec.cskmd.data.ws.dto.DTOZmenaWrapperWS;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.zsr.common.server.auth.ZSRAuthInfo;

@WebService(serviceName = "CskmdService", targetNamespace = "sk.ditec.cskmd")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@HandlerChain(file = "LogMessage_handler.xml")
public class CskmdService {

	private Logger log = LoggerFactory.getLogger(CskmdService.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WS_KMD);

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

	@WebMethod(operationName = "getCiselnikMetaList")
	public DTOCiselnikWS[] getCiselnikMetaList() throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikMetaList(), accountName: " + auth.getAccountName());
			return dlg.getCskmdDataWS().ciselnikMetaList(auth);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikMetaList.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikMeta")
	public DTOCiselnikWS getCiselnikMeta(@WebParam(name = "ciselnikId") int ciselnikId) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikMeta(), ciselnikId={}, accountName={}", ciselnikId, auth.getAccountName());
			return dlg.getCskmdDataWS().ciselnikMetaRead(auth, ciselnikId);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikMeta.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikStlpecMetaList")
	public DTOCiselnikStlpecWS[] getCiselnikStlpecMetaList(@WebParam(name = "ciselnikId") int ciselnikId) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikStlpecMetaList(), ciselnikId={}, accountName={}", ciselnikId, auth.getAccountName());
			return dlg.getCskmdDataWS().ciselnikStlpecMetaList(auth, ciselnikId);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikStlpecMetaList.error");
			return null;
		}
	}

	private String toString(DTOPageWS pageWS) throws AppException {

		try {
			if (!StringUtils.isValid(pageWS)) {
				return "page=NULL";
			}
			String page = StringUtils.isValid(pageWS.getPage()) ? "page.page=" + pageWS.getPage() : "page.page=NULL";
			String pageSize = StringUtils.isValid(pageWS.getPageSize()) ? "page.pageSize=" + pageWS.getPageSize() : "page.pageSize=NULL";
			return page + ", " + pageSize;

		} catch (Exception e) {
			DBUtils.handleException(e, "toString.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikData")
	public DTOCiselnikDataWS getCiselnikData(@WebParam(name = "ciselnikId") int ciselnikId, @WebParam(name = "page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikData(), ciselnikId={}, {}, accountName={}", new Object[] { ciselnikId, toString(pageWS), auth.getAccountName() });
			return dlg.getCskmdDataWS().getCiselnikDataToDate(auth, ciselnikId, new Date(), pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikData.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikDataToDate")
	public DTOCiselnikDataWS getCiselnikDataToDate(@WebParam(name = "ciselnikId") int ciselnikId, @WebParam(name = "datum") Date date, @WebParam(name = "page") DTOPageWS pageWS)
			throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikDataToDate(), ciselnikId={}, datum={}, {}, accountName={}",
					new Object[] { ciselnikId, date, toString(pageWS), auth.getAccountName() });
			return dlg.getCskmdDataWS().getCiselnikDataToDate(auth, ciselnikId, date, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataToDate.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikDataDatumOdDo")
	public DTOCiselnikDataWS getCiselnikDataDatumOdDo(@WebParam(name = "ciselnikId") int ciselnikId, @WebParam(name = "page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikDataDatumOdDo(), ciselnikId={}, {}, accountName={}", new Object[] { ciselnikId, toString(pageWS), auth.getAccountName() });
			return dlg.getCskmdDataWS().getCiselnikDataDatumOdDo(auth, ciselnikId, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataDatumOdDo.error");
			return null;
		}
	}

	private String toString(Integer[] ids) throws AppException {

		try {
			if (!StringUtils.isValid(ids)) {
				return "NULL";
			}
			String str = "";
			for (Integer value : ids) {
				str += StringUtils.isValid(value) ? str += ", " + value : value;
			}
			return str;

		} catch (Exception e) {
			DBUtils.handleException(e, "toString.error");
			return null;
		}
	}

	@WebMethod(operationName = "getZmenaListDatumOd")
	public DTOZmenaWrapperWS getZmenaListDatumOd(@WebParam(name = "ids") Integer[] ids, @WebParam(name = "datumOd") Date casVytvorenieOd, @WebParam(name = "page") DTOPageWS pageWS)
			throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenaListDatumOd(), ids={}, casVytvorenieOd={}, {}, accountName={}",
					new Object[] { toString(ids), casVytvorenieOd, toString(pageWS), auth.getAccountName() });
			return dlg.getCskmdDataWS().getZmenyListDatumOd(auth, ids, casVytvorenieOd, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenaListToDate.error");
			return null;
		}
	}

	@WebMethod(operationName = "getZmenaListDatumOdDo")
	public DTOZmenaWrapperWS getZmenaListDatumOdDo(@WebParam(name = "ids") Integer[] ids, @WebParam(name = "datumOd") Date casVytvorenieOd,
			@WebParam(name = "datumDo") Date casVytvorenieDo, @WebParam(name = "page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenaListDatumOdDo(), ids={}, casVytvorenieOd={}, casVytvorenieDo={}, {}, accountName={}", new Object[] { toString(ids), casVytvorenieOd,
					casVytvorenieDo, toString(pageWS), auth.getAccountName() });
			return dlg.getCskmdDataWS().getZmenyListDatumOdDo(auth, ids, casVytvorenieOd, casVytvorenieDo, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenaListDatumOdDo.error");
			return null;
		}
	}

	@WebMethod(operationName = "getZmenaListSchvaleneDatumOd")
	public DTOZmenaWrapperWS getZmenaListSchvaleneDatumOd(@WebParam(name = "ids") Integer[] ids, @WebParam(name = "datumOd") Date casVytvorenieOd,
			@WebParam(name = "page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenaListSchvaleneDatumOd(), ids={}, casVytvorenieOd={}, {}, accountName={}", new Object[] { toString(ids), casVytvorenieOd,
					toString(pageWS), auth.getAccountName() });
			return dlg.getCskmdDataWS().getZmenyListSchvaleneDatumOd(auth, ids, casVytvorenieOd, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenaListSchvaleneToDate.error");
			return null;
		}
	}

	@WebMethod(operationName = "getZmenaListSchvaleneDatumOdDo")
	public DTOZmenaWrapperWS getZmenaListSchvaleneDatumOdDo(@WebParam(name = "ids") Integer[] ids, @WebParam(name = "datumOd") Date casVytvorenieOd,
			@WebParam(name = "datumDo") Date casVytvorenieDo, @WebParam(name = "page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenaListSchvaleneDatumOdDo(), ids={}, casVytvorenieOd={}, casVytvorenieDo={}, {}, accountName={}", new Object[] { toString(ids),
					casVytvorenieOd, casVytvorenieDo, toString(pageWS), auth.getAccountName() });
			return dlg.getCskmdDataWS().getZmenyListSchvaleneDatumOdDo(auth, ids, casVytvorenieOd, casVytvorenieDo, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenaListSchvaleneDatumOdDo.error");
			return null;
		}
	}

}