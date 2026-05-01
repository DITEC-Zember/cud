package sk.ditec.cud.data.ws;

import java.util.Date;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import sk.ditec.common.security.AppException;
import sk.ditec.cud.data.ws.dto.DTOCiselnikDataWS;
import sk.ditec.cud.data.ws.dto.DTOCiselnikMetaWS;
import sk.ditec.cud.data.ws.dto.DTOCiselnikStlpecMetaWS;
import sk.ditec.cud.data.ws.dto.DTOPageWS;
import sk.ditec.cud.data.ws.dto.DTOUpdZmenaResponseWS;
import sk.ditec.cud.data.ws.dto.DTOUpdZmenaWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaCiselnikArrayWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaWrapperWS;

@WebService(name = "CudDataWS", endpointInterface = "sk.ditec.cud.data.ws.CudDataWSRemote", portName = "CudDataWSPort", serviceName = "CudDataWSService", targetNamespace = "urn:ws.cud.zsr.ditec.sk")
public interface CudDataWSRemote {

	@WebMethod(operationName = "getCiselnikMetaRead")
	public DTOCiselnikMetaWS getCiselnikMetaRead(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "jazyk") String jazyk, @WebParam(name = "datum") Date d) throws AppException;

	@WebMethod(operationName = "getCiselnikMetaList")
	public DTOCiselnikMetaWS[] getCiselnikMetaList(@WebParam(name = "jazyk") String jazyk) throws AppException;

	@WebMethod(operationName = "getCiselnikStlpecMetaList")
	public DTOCiselnikStlpecMetaWS[] getCiselnikStlpecMetaList(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "jazyk") String jazyk, @WebParam(name = "datum") Date d) throws AppException;

	@WebMethod(operationName = "getCiselnikData")
	public DTOCiselnikDataWS getCiselnikData(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException;

	@WebMethod(operationName = "getCiselnikDataDatumOdDo")
	public DTOCiselnikDataWS getCiselnikDataDatumOdDo(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException;

	@WebMethod(operationName = "getCiselnikDataToDate")
	public DTOCiselnikDataWS getCiselnikDataToDate(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "datum") Date d, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException;

	@WebMethod(operationName = "getCiselnikDataZmena")
	public DTOCiselnikDataWS getCiselnikDataZmena(@WebParam(name = "ciselnikID") Integer ciselnikID, @WebParam(name = "zmenaOd") Date d, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException;

	@WebMethod(operationName = "getCiselnikZmena")
	public DTOZmenaCiselnikArrayWS getCiselnikZmena(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "zmenaOd") Date d) throws AppException;

	@WebMethod(operationName = "getZmenyListDatumOd")
	public DTOZmenaWrapperWS getZmenyListDatumOd(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "datumOd") Date d, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException;

	@WebMethod(operationName = "getZmenyListDatumOdDo")
	public DTOZmenaWrapperWS getZmenyListDatumOdDo(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "datumOd") Date datumOd, @WebParam(name = "datumDo") Date datumDo, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException;

	@WebMethod(operationName = "getZmenyListSchvaleneDatumOd")
	public DTOZmenaWrapperWS getZmenyListSchvaleneDatumOd(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "datumOd") Date d, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException;

	@WebMethod(operationName = "getZmenyListSchvaleneDatumOdDo")
	public DTOZmenaWrapperWS getZmenyListSchvaleneDatumOdDo(@WebParam(name = "ids") Integer[] ciselnikIDs, @WebParam(name = "datumOd") Date datumOd, @WebParam(name = "datumDo") Date datumDo, @WebParam(name = "Jazyk") String jazyk, @WebParam(name = "Page") DTOPageWS pageWS) throws AppException;

	@WebMethod(operationName = "updZmenaHodnotCiselnikaUpdate")
	public DTOUpdZmenaResponseWS updZmenaHodnotCiselnikaUpdate(@WebParam(name = "zmena") DTOUpdZmenaWS dto) throws AppException;

}
