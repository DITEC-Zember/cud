package sk.ditec.kmd.data.ws;

import java.util.ArrayList;
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

import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.kmd.data.ws.dto.DTOWSCiselnik;
import sk.ditec.kmd.data.ws.dto.DTOWSCiselnikMeta;
import sk.ditec.kmd.data.ws.dto.DTOWSStlpec;
import sk.ditec.kmd.data.ws.dto.DTOWSZmena;
import sk.ditec.zsr.common.server.auth.ZSRAuthInfo;

@WebService(serviceName = "KMDDataService", targetNamespace = "sk.ditec.kmd.data")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
@HandlerChain(file = "LogMessage_handler.xml")
public class KMDDataService {

	private Logger log = LoggerFactory.getLogger(KMDDataService.class);

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

	@WebMethod
	public ArrayList<DTOWSCiselnikMeta> getCiselnikyMeta() {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikyMeta(), accountName: " + auth.getAccountName());
			return dlg.getKmdDataWS().getCiselnikyMeta(auth);

		} catch (Exception e) {
			log.error("getCiselnikyMeta", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public DTOWSCiselnikMeta getCiselnikMeta(@WebParam(name = "ciselnikId") int ciselnikId) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikMeta(), ciselnikId={}, accountName={}", ciselnikId, auth.getAccountName());
			return dlg.getKmdDataWS().getCiselnikMeta(auth, ciselnikId);

		} catch (Exception e) {
			log.error("getCiselnikyMeta", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public ArrayList<DTOWSStlpec> getStlpce(@WebParam(name = "ciselnikId") int ciselnikId) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getStlpce(), ciselnikId={}, accountName={}", ciselnikId, auth.getAccountName());
			return dlg.getKmdDataWS().getStlpce(auth, ciselnikId);

		} catch (Exception e) {
			log.error("getStlpce", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public DTOWSCiselnik getCiselnikData(@WebParam(name = "ciselnikId") int ciselnikId) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikData(), ciselnikId={}, {}, accountName={}", ciselnikId, auth.getAccountName());
			return dlg.getKmdDataWS().getCiselnikData(auth, ciselnikId);

		} catch (Exception e) {
			log.error("getCiselnikData", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public DTOWSCiselnik getCiselnikToDate(@WebParam(name = "ciselnikId") int ciselnikId, @WebParam(name = "datum") Date date) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikToDate(), ciselnikId={}, date={}, accountName={}", new Object[] { ciselnikId, date, auth.getAccountName() });
			return dlg.getKmdDataWS().getCiselnikToDate(auth, ciselnikId, date);

		} catch (Exception e) {
			log.error("getCiselnikToDate", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public DTOWSCiselnik getCiselnikOdDo(@WebParam(name = "ciselnikId") int ciselnikId) throws AppException {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getCiselnikOdDo(), ciselnikId={}, accountName={}", new Object[] { ciselnikId, auth.getAccountName() });
			return dlg.getKmdDataWS().getCiselnikOdDo(auth, ciselnikId, null);

		} catch (Exception e) {
			log.error("getCiselnikOdDo", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public ArrayList<DTOWSZmena> getZmenyPreCiselnik(@WebParam(name = "ciselnikId") int ciselnikID, @WebParam(name = "datumOd") Date dateFrom) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenyPreCiselnik(), ciselnikID={}, dateFrom={}, accountName={}", new Object[] { ciselnikID, dateFrom, auth.getAccountName() });
			return dlg.getKmdDataWS().getZmenyListDatumOd(auth, ciselnikID, dateFrom);

		} catch (Exception e) {
			log.error("getZmenyPreCiselnik", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public ArrayList<DTOWSZmena> getZmenyPreCiselnikDatumDo(@WebParam(name = "ciselnikId") int ciselnikID, @WebParam(name = "datumOd") Date dateFrom,
			@WebParam(name = "datumDo") Date dateTo) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenyPreCiselnikDatumDo(), ciselnikID={}, dateFrom={}, dateTo={}, accountName={}",
					new Object[] { ciselnikID, dateFrom, dateTo, auth.getAccountName() });
			return dlg.getKmdDataWS().getZmenyListDatumOdDo(auth, ciselnikID, dateFrom, dateTo);

		} catch (Exception e) {
			log.error("getZmenyPreCiselnikDatumDo", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public ArrayList<DTOWSZmena> getZmeny(@WebParam(name = "datumOd") Date dateFrom) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmeny(), dateFrom={}, accountName={}", dateFrom, auth.getAccountName());
			return dlg.getKmdDataWS().getZmeny(auth, dateFrom);

		} catch (Exception e) {
			log.error("getZmeny", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public ArrayList<DTOWSZmena> getZmenyDatumDo(@WebParam(name = "datumOd") Date dateFrom, @WebParam(name = "datumDo") Date dateTo) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenyDatumDo(), dateFrom={}, dateTo={}, accountName={}", new Object[] { dateFrom, dateTo, auth.getAccountName() });
			return dlg.getKmdDataWS().getZmenyDatumDo(auth, dateFrom, dateTo);

		} catch (Exception e) {
			log.error("getZmenyDatumDo", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public ArrayList<DTOWSZmena> getZmenyPreCiselnikSchvalene(@WebParam(name = "ciselnikId") int ciselnikID, @WebParam(name = "datumOd") Date dateFrom) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenyPreCiselnikSchvalene(), ciselnikID={}, dateFrom={}, accountName={}", new Object[] { ciselnikID, dateFrom, auth.getAccountName() });
			return dlg.getKmdDataWS().getZmenyListSchvaleneDatumOd(auth, ciselnikID, dateFrom);

		} catch (Exception e) {
			log.error("getZmenyPreCiselnikSchvalene", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public ArrayList<DTOWSZmena> getZmenyPreCiselnikSchvaleneDatumDo(@WebParam(name = "ciselnikId") int ciselnikID, @WebParam(name = "datumOd") Date dateFrom,
			@WebParam(name = "datumDo") Date dateTo) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenyPreCiselnikSchvaleneDatumDo(), ciselnikID={}, dateFrom={}, dateTo={}, accountName={}", new Object[] { ciselnikID, dateFrom, dateTo,
					auth.getAccountName() });
			return dlg.getKmdDataWS().getZmenyListSchvaleneDatumOdDo(auth, ciselnikID, dateFrom, dateTo);

		} catch (Exception e) {
			log.error("getZmenyPreCiselnikSchvaleneDatumDo", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public ArrayList<DTOWSZmena> getZmenySchvalene(@WebParam(name = "datumOd") Date dateFrom) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenySchvalene(), dateFrom={}, accountName={}", dateFrom, auth.getAccountName());
			return dlg.getKmdDataWS().getZmenySchvalene(auth, dateFrom);

		} catch (Exception e) {
			log.error("getZmenySchvalene", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	@WebMethod
	public ArrayList<DTOWSZmena> getZmenySchvaleneDatumDo(@WebParam(name = "datumOd") Date dateFrom, @WebParam(name = "datumDo") Date dateTo) {

		try {
			ZSRAuthInfo auth = getAuthInfo();
			log.info("Volanie metody: getZmenySchvaleneDatumDo(), dateFrom={}, dateTo={}, accountName={}", new Object[] { dateFrom, dateTo, auth.getAccountName() });
			return dlg.getKmdDataWS().getZmenySchvaleneDatumDo(auth, dateFrom, dateTo);

		} catch (Exception e) {
			log.error("getZmenySchvaleneDatumDo", e);
			throw new RuntimeException(e.getMessage());
		}
	}
}
