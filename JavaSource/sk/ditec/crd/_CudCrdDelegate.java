package sk.ditec.crd;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.ws.CRDDataReplicationPort;
import sk.ditec.crd.ws.CRDDataReplicationPortService;
import sk.ditec.crdexp.ws.OutboundConnectorService_Service;
import sk.ditec.exp.rinfws.RinfDataWSService;
import sk.ditec.zsr.common.server.utils.Monitoring;




public class _CudCrdDelegate {
	
	private static ThreadLocal<CRDDataReplicationPort> wsCrd = new ThreadLocal<CRDDataReplicationPort>();
	private static ThreadLocal<OutboundConnectorService_Service> wsCrdExport = new ThreadLocal<OutboundConnectorService_Service>();
	private static ThreadLocal<RinfDataWSService> wsRinfExport = new ThreadLocal<RinfDataWSService>();
	
	private Logger log = LoggerFactory.getLogger(_CudCrdDelegate.class);

	//@WebServiceClient(name = "CRDDataReplicationPortService", targetNamespace = "http://schema.refdata.li.cc.uic.org/types/v1", wsdlLocation = "http://a01service-http-zsr-ipaas.apps.ocp-test.intra.zsr.sk/service/a01service?wsdl")
	// public class CRDDataReplicationPortService
	public CRDDataReplicationPort getCrdWS() throws AppException, MalformedURLException {

		CRDDataReplicationPortService stub = null;
		String configUrl = FrameworkUtils.getConfigProperty("cud", "url.crd.wsdl");

		try{
			if (wsCrd.get() == null) {
				// urlString = "https://a01service-zsr-ipaas.apps.ocptest.intra.zsr.sk/service/a01service?wsdl";
	
				if (!StringUtils.isValid(configUrl)) {
					log.error("URL na auth nenacitana! Je potrebne nastavit v SYS_CONFIG: " + " cud " + "url.crd.wsdl");
					
					Monitoring.logSysConfig("cud " + "url.crd.wsdl");
					throw new AppException(Monitoring.logSysConfigMesssage("cud " + "url.crd.wsdl"));
				}
				
				URL url = new URL(configUrl);

				QName qname = new QName("http://schema.refdata.li.cc.uic.org/types/v1", "CRDDataReplicationPortService");

				 stub = new CRDDataReplicationPortService(url, qname);
				wsCrd.set(stub.getCRDDataReplicationPortSoap11());
			}
	
			return wsCrd.get();
		
		} catch(Throwable t){
			Monitoring.log(t, configUrl.toString());
			throw new AppException(Monitoring.logMesssage(t, configUrl.toString()));
		}
	}

	public OutboundConnectorService_Service getCrdExportWSold() throws AppException, MalformedURLException {

		OutboundConnectorService_Service stub = null;
		String configUrl = FrameworkUtils.getConfigProperty("cud", "url.crdexport.wsdl");
	
		try{
			if (wsCrdExport.get() == null) {
	
				// urlString =
				// "https://location01service-zsr-ipaas.apps.ocptest.intra.zsr.sk/service/location01service?wsdl";
	
				if (!StringUtils.isValid(configUrl)) {
					log.error("URL na auth nenacitana! Je potrebne nastavit v SYS_CONFIG: " + " cud " + "url.crdexport.wsdl");

					Monitoring.logSysConfig("cud " + "url.crdexport.wsdl");
					throw new AppException(Monitoring.logSysConfigMesssage("cud " + "url.crdexport.wsdl"));
				}
				URL url = new URL(configUrl);
				QName qname = new QName("http://uic.cc.org/li/messageprocessing/outbound/", "OutboundConnectorService");

				stub = new OutboundConnectorService_Service(url, qname);
				wsCrdExport.set(stub);
	
			}
			return wsCrdExport.get();

		} catch(Throwable t){
			Monitoring.log(t, configUrl.toString());
			throw new AppException(Monitoring.logMesssage(t, configUrl.toString()));
		}
	}

	public OutboundConnectorService_Service getCrdExportWSFromOdberatelExportCesta(String exportCesta)
			throws AppException, MalformedURLException {

		OutboundConnectorService_Service stub = null;
		// // urlString =
		// "https://location01service-zsr-ipaas.apps.ocptest.intra.zsr.sk/service/location01service?wsdl";
		try {
		if (!StringUtils.isValid(exportCesta)) {
				log.error("URL Export CRD nie je zadefinovane! ");
			return null;
		}
			URL url = new URL(exportCesta + "?wsdl");
		QName qname = new QName("http://uic.cc.org/li/messageprocessing/outbound/", "OutboundConnectorService");
			// https nefunguje
			// QName qname = new QName("https://uic.cc.org/li/messageprocessing/outbound/", "OutboundConnectorService");
		
		stub = new OutboundConnectorService_Service(url, qname);
			// ------------------------------------------------


		wsCrdExport.set(stub);
		} catch (Throwable t) {
			Monitoring.log(t, exportCesta);
			throw new AppException(Monitoring.logMesssage(t, exportCesta));
		}
		return wsCrdExport.get();
		

	}

	public RinfDataWSService getRinfExportWS() throws AppException, MalformedURLException {

		RinfDataWSService stub = null;
		String configUrl = FrameworkUtils.getConfigProperty("cud", "url.rinfexport.wsdl");

		try{
			if (wsCrdExport.get() == null) {
	
				if (!StringUtils.isValid(configUrl)) {
					log.error("URL na auth nenacitana! Je potrebne nastavit v SYS_CONFIG: " + " cud " + "url.rinfexport.wsdl");

					Monitoring.logSysConfig("cud " + "url.rinfexport.wsdl");
					throw new AppException(Monitoring.logSysConfigMesssage("cud " + "url.rinfexport.wsdl"));
				}
				URL url = new URL(configUrl);
				QName qname = new QName("urn:rinf.pis.ws.ditec.sk", "RinfDataWSService");
				stub = new RinfDataWSService(url, qname);
				wsRinfExport.set(stub);
	
			}
	
			return wsRinfExport.get();

		} catch(Throwable t){
			Monitoring.log(t, configUrl.toString());
			throw new AppException(Monitoring.logMesssage(t, configUrl.toString()));
		}
	}


	public CrdNenajdeneZaznamyClass getCrdNenajdeneZaznamyClass() {
		return new CrdNenajdeneZaznamyClass();
	}
	
	public CrdSpracovanieClass getCrdSpracovanieClass() {
		return new CrdSpracovanieClass();
	}

	public CrdSpracTabuliekClass getCrdSpracTabuliekClass() {
		return new CrdSpracTabuliekClass();
	}

	public SpracujCrdCountryClass getSpracujCrdCountryClass() {
		return new SpracujCrdCountryClass();
	}

	public TCudCiselnikyClass getTCudCiselnikyClass() {
		return new TCudCiselnikyClass();
	}

	public CrdAktualizujCiselnikClass getCrdAktualizujCiselnikClass() {
		return new CrdAktualizujCiselnikClass();
	}

	public SpracujCrdCompanyClass getSpracujCrdCompanyClass() {
		return new SpracujCrdCompanyClass();
	}

	public SpracujCrdPrimaryLocationClass getSpracujCrdPrimaryLocationClass() {
		return new SpracujCrdPrimaryLocationClass();
	}

	public SpracujCrdSubsidiaryTypeClass getSpracujCrdSubsidiaryTypeClass() {
		return new SpracujCrdSubsidiaryTypeClass();
	}

	public SpracujCrdSubsidiaryLocationClass getSpracujCrdSubsidiaryLocationClass() {
		return new SpracujCrdSubsidiaryLocationClass();
	}

	public CudParametreClass getCudParametreClass() {
		return new CudParametreClass();
	}

	public CudSendClass getCudSendClass() {
		return new CudSendClass();
	}

	public CudSendSuborClass getCudSendSuborClass() {
		return new CudSendSuborClass();
	}

	public CrdSpracovanieZmenyClass getCrdSpracovanieZmenyClass() {
		return new CrdSpracovanieZmenyClass();
	}

	public AktualizaciaPrimLocZDbClass getAktualizaciaPrimLocZDbClass() {
		return new AktualizaciaPrimLocZDbClass();
	}

	public ZmenaStlpecClass getZmenaStlpecClass() {
		return new ZmenaStlpecClass();
	}

	public AktualizaciaSubLocZDbClass getAktualizaciaSubLocZDbClass() {
		return new AktualizaciaSubLocZDbClass();
	}

	public AktualizaciaLocZDbClass getAktualizaciaLocZDbClass() {
		return new AktualizaciaLocZDbClass();
	}


}
