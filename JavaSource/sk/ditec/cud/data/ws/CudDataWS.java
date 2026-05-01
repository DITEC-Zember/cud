package sk.ditec.cud.data.ws;

import java.util.Date;

import javax.annotation.Resource;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.DateUtils;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.data.ws.dto.DTOCiselnikDataWS;
import sk.ditec.cud.data.ws.dto.DTOCiselnikMetaWS;
import sk.ditec.cud.data.ws.dto.DTOCiselnikStlpecMetaWS;
import sk.ditec.cud.data.ws.dto.DTOPageWS;
import sk.ditec.cud.data.ws.dto.DTOUpdZmenaResponseWS;
import sk.ditec.cud.data.ws.dto.DTOUpdZmenaWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaCiselnikArrayWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaWrapperWS;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.zsr.common.server.auth.ZSRAuthInfo;

@WebService(name = "CudDataWS", endpointInterface = "sk.ditec.cud.data.ws.CudDataWSRemote", portName = "CudDataWSPort", serviceName = "CudDataWSService", targetNamespace = "urn:ws.server.cud.zsr.ditec.sk")
@HandlerChain(file = "LogMessage_handler.xml")
public class CudDataWS extends HttpServlet implements CudDataWSRemote {

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WS);

	private Logger log = LoggerFactory.getLogger(CudDataWS.class);

	@Resource
	WebServiceContext wsContext;

	private ZSRAuthInfo getAuthInfo() throws AppException {

		try {
			MessageContext msgContext = wsContext.getMessageContext();
			HttpServletRequest request = (HttpServletRequest) msgContext.get(MessageContext.SERVLET_REQUEST);

			ZSRAuthInfo auth = (ZSRAuthInfo) request.getSession().getAttribute("authInfo");
			auth.setPerms(FrameworkUtils.getAuthMod().getPermissionList(auth.getAccountId()));

			return auth;

		} catch (Exception e) {
			DBUtils.handleException(e, "getAuthInfo.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikMetaRead")
	public DTOCiselnikMetaWS getCiselnikMetaRead(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "jazyk") String jazyk, @WebParam(name = "datum") Date d) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikMetaRead(), ciselnikID={}, jazyk={}, datum={}, accountName={}", new Object[] { ciselnikID, jazyk, d, auth.getAccountName() });
			return dlg.getDataWS().ciselnikMetaRead(auth, ciselnikID, jazyk, d, true);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikMetaRead.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikMetaList")
	public DTOCiselnikMetaWS[] getCiselnikMetaList(@WebParam(name = "jazyk") String jazyk) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikMetaList(), jazyk={}, accountName={}", jazyk, auth.getAccountName());
			return dlg.getDataWS().ciselnikMetaList(auth, jazyk);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikMetaList.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikStlpecMetaList")
	public DTOCiselnikStlpecMetaWS[] getCiselnikStlpecMetaList(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "jazyk") String jazyk, @WebParam(name = "datum") Date d) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikStlpecMetaList(), ciselnikID={}, jazyk={}, accountName={}", new Object[] { ciselnikID, jazyk, auth.getAccountName() });
			return dlg.getDataWS().ciselnikStlpecMetaList(auth, ciselnikID, jazyk, d);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikStlpecMetaList.error");
			return null;
		}
	}

	private String toString(DTOPageWS pageWS) throws AppException {

		try {
			if (!StringUtils.isValid(pageWS)) {
				return "NULL";
			}
			String page = StringUtils.isValid(pageWS.getPage()) ? pageWS.getPage().toString() : "NULL";
			String pageSize = StringUtils.isValid(pageWS.getPageSize()) ? pageWS.getPageSize().toString() : "NULL";
			return "(" + page + ", " + pageSize + ")";

		} catch (Exception e) {
			DBUtils.handleException(e, "toString.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikData")
	public DTOCiselnikDataWS getCiselnikData(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikData(), ciselnikId={}, page={}, accountName={}", new Object[] { ciselnikID, toString(pageWS), auth.getAccountName() });
			Date d = DateUtils.removeTime(new Date());
			return dlg.getDataWS().ciselnikDataListOdDo(auth, ciselnikID, jazyk, pageWS, d, d, d);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikData.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikDataDatumOdDo")
	public DTOCiselnikDataWS getCiselnikDataDatumOdDo(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikDataDatumOdDo(), ciselnikId={}, jazyk={}, page={}, accountName={}", new Object[] { ciselnikID, jazyk, toString(pageWS), auth.getAccountName() });
			return dlg.getDataWS().ciselnikDataListOdDo(auth, ciselnikID, jazyk, pageWS, null, null, null);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataDatumOdDo.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikDataToDate")
	public DTOCiselnikDataWS getCiselnikDataToDate(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "datum") Date d, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikDataToDate(), ciselnikId={}, datum={}, jazyk={}, page={}, accountName={}", new Object[] { ciselnikID, d, jazyk, toString(pageWS), auth.getAccountName() });
			return dlg.getDataWS().getCiselnikDataToDate(auth, ciselnikID, d, jazyk, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataToDate.error");
			return null;
		}
	}

	@WebMethod(operationName = "getCiselnikDataZmena")
	public DTOCiselnikDataWS getCiselnikDataZmena(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "zmenaOd") Date d, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikDataZmena(), ciselnikId={}, zmenaOd={}, jazyk={}, page={}, accountName={}", new Object[] { ciselnikID, d, jazyk, toString(pageWS), auth.getAccountName() });
			return dlg.getDataWS().getCiselnikDataZmena(auth, ciselnikID, jazyk, pageWS, d);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataZmena.error");
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

	@WebMethod(operationName = "getCiselnikZmena")
	public DTOZmenaCiselnikArrayWS getCiselnikZmena(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "zmenaOd") Date d) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikDataZmena(), ciselnikIds={}, zmenaOd={}, accountName={}", new Object[] { toString(ciselnikIDs), d, auth.getAccountName() });
			return dlg.getDataWS().getCiselnikZmena(auth, ciselnikIDs, d);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataZmena.error");
			return null;
		}
	}

	@WebMethod(operationName = "getZmenyListDatumOd")
	public DTOZmenaWrapperWS getZmenyListDatumOd(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "datumOd") Date d, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenyListDatumOd(), ciselnikIds={}, datumOd={}, jazyk={}, page={}, accountName={}", new Object[] { toString(ciselnikIDs), d, jazyk, toString(pageWS), auth.getAccountName() });
			return dlg.getDataWS().getZmenyListDatumOd(auth, ciselnikIDs, d, jazyk, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListDatumOd.error");
			return null;
		}
	}

	@WebMethod(operationName = "getZmenyListDatumOdDo")
	public DTOZmenaWrapperWS getZmenyListDatumOdDo(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "datumOd") Date datumOd, @WebParam(name = "datumDo") Date datumDo, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenyListDatumOdDo(), ciselnikIds={}, datumOd={}, datumDo={}, jazyk={}, page={}, accountName={}", new Object[] { toString(ciselnikIDs), datumOd, datumDo, jazyk, toString(pageWS), auth.getAccountName() });
			return dlg.getDataWS().getZmenyListDatumOdDo(auth, ciselnikIDs, datumOd, datumDo, jazyk, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListDatumOdDo.error");
			return null;
		}
	}

	@WebMethod(operationName = "getZmenyListSchvaleneDatumOd")
	public DTOZmenaWrapperWS getZmenyListSchvaleneDatumOd(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "datumOd") Date d, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenyListSchvaleneDatumOd(), ciselnikIds={}, d={}, jazyk={}, page={}, accountName={}", new Object[] { toString(ciselnikIDs), d, jazyk, toString(pageWS), auth.getAccountName() });
			return dlg.getDataWS().getZmenyListSchvaleneDatumOd(auth, ciselnikIDs, d, jazyk, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListSchvaleneDatumOd.error");
			return null;
		}
	}

	@WebMethod(operationName = "getZmenyListSchvaleneDatumOdDo")
	public DTOZmenaWrapperWS getZmenyListSchvaleneDatumOdDo(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "datumOd") Date datumOd, @WebParam(name = "datumDo") Date datumDo, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenyListSchvaleneDatumOdDo(), ciselnikIds={}, datumOd={}, datumDo={}, jazyk={}, page={}, accountName={}", new Object[] { toString(ciselnikIDs), datumOd, datumDo, jazyk, toString(pageWS), auth.getAccountName() });
			return dlg.getDataWS().getZmenyListSchvaleneDatumOdDo(auth, ciselnikIDs, datumOd, datumDo, jazyk, pageWS);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListSchvaleneDatumOdDo.error");
			return null;
		}
	}

	@WebMethod(operationName = "updZmenaHodnotCiselnikaUpdate")
	public DTOUpdZmenaResponseWS updZmenaHodnotCiselnikaUpdate(@WebParam(name = "zmena") DTOUpdZmenaWS dto) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			return dlg.getDataWS().updZmenaHodnotCiselnikaUpdate(auth, dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "updZmenaHodnotCiselnikaUpdate.error");
			return null;
		}
	}

}
