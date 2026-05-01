
package sk.ditec.exp.rinfws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import sk.ditec.common.ws.AuthInfoWS;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the sk.ditec.exp.rinfws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetRinfDataServiceSol_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceSol");
    private final static QName _GetRinfDataServiceSolResponse_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceSolResponse");
    private final static QName _GetRinfDataServiceTuKompletResponse_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceTuKompletResponse");
    private final static QName _AppException_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "AppException");
    private final static QName _GetRinfDataServiceKomplet_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceKomplet");
    private final static QName _GetRinfDataServiceOp_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceOp");
    private final static QName _GetRinfDataServiceTu_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceTu");
    private final static QName _GetRinfDataServiceOpResponse_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceOpResponse");
    private final static QName _GetRinfDataServiceTuKomplet_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceTuKomplet");
    private final static QName _GetRinfDataServiceTuResponse_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceTuResponse");
    private final static QName _GetRinfDataServiceKompletResponse_QNAME = new QName("urn:rinf.pis.ws.ditec.sk", "getRinfDataServiceKompletResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: sk.ditec.exp.rinfws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link AuthInfoWS }
     * 
     */
    public AuthInfoWS createAuthInfoWS() {
        return new AuthInfoWS();
    }


    /**
     * Create an instance of {@link GetRinfDataServiceOpResponse }
     * 
     */
    public GetRinfDataServiceOpResponse createGetRinfDataServiceOpResponse() {
        return new GetRinfDataServiceOpResponse();
    }

    /**
     * Create an instance of {@link GetRinfDataServiceTuKomplet }
     * 
     */
    public GetRinfDataServiceTuKomplet createGetRinfDataServiceTuKomplet() {
        return new GetRinfDataServiceTuKomplet();
    }

    /**
     * Create an instance of {@link AppExceptionFaultBean }
     * 
     */
    public AppExceptionFaultBean createAppExceptionFaultBean() {
        return new AppExceptionFaultBean();
    }

    /**
     * Create an instance of {@link GetRinfDataServiceKomplet }
     * 
     */
    public GetRinfDataServiceKomplet createGetRinfDataServiceKomplet() {
        return new GetRinfDataServiceKomplet();
    }

    /**
     * Create an instance of {@link GetRinfDataServiceOp }
     * 
     */
    public GetRinfDataServiceOp createGetRinfDataServiceOp() {
        return new GetRinfDataServiceOp();
    }

    /**
     * Create an instance of {@link GetRinfDataServiceTu }
     * 
     */
    public GetRinfDataServiceTu createGetRinfDataServiceTu() {
        return new GetRinfDataServiceTu();
    }

    /**
     * Create an instance of {@link GetRinfDataServiceTuResponse }
     * 
     */
    public GetRinfDataServiceTuResponse createGetRinfDataServiceTuResponse() {
        return new GetRinfDataServiceTuResponse();
    }

    /**
     * Create an instance of {@link GetRinfDataServiceKompletResponse }
     * 
     */
    public GetRinfDataServiceKompletResponse createGetRinfDataServiceKompletResponse() {
        return new GetRinfDataServiceKompletResponse();
    }

    /**
     * Create an instance of {@link GetRinfDataServiceSol }
     * 
     */
    public GetRinfDataServiceSol createGetRinfDataServiceSol() {
        return new GetRinfDataServiceSol();
    }

    /**
     * Create an instance of {@link GetRinfDataServiceSolResponse }
     * 
     */
    public GetRinfDataServiceSolResponse createGetRinfDataServiceSolResponse() {
        return new GetRinfDataServiceSolResponse();
    }

    /**
     * Create an instance of {@link GetRinfDataServiceTuKompletResponse }
     * 
     */
    public GetRinfDataServiceTuKompletResponse createGetRinfDataServiceTuKompletResponse() {
        return new GetRinfDataServiceTuKompletResponse();
    }



    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceSol }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceSol")
    public JAXBElement<GetRinfDataServiceSol> createGetRinfDataServiceSol(GetRinfDataServiceSol value) {
        return new JAXBElement<GetRinfDataServiceSol>(_GetRinfDataServiceSol_QNAME, GetRinfDataServiceSol.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceSolResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceSolResponse")
    public JAXBElement<GetRinfDataServiceSolResponse> createGetRinfDataServiceSolResponse(GetRinfDataServiceSolResponse value) {
        return new JAXBElement<GetRinfDataServiceSolResponse>(_GetRinfDataServiceSolResponse_QNAME, GetRinfDataServiceSolResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceTuKompletResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceTuKompletResponse")
    public JAXBElement<GetRinfDataServiceTuKompletResponse> createGetRinfDataServiceTuKompletResponse(GetRinfDataServiceTuKompletResponse value) {
        return new JAXBElement<GetRinfDataServiceTuKompletResponse>(_GetRinfDataServiceTuKompletResponse_QNAME, GetRinfDataServiceTuKompletResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AppExceptionFaultBean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "AppException")
    public JAXBElement<AppExceptionFaultBean> createAppException(AppExceptionFaultBean value) {
        return new JAXBElement<AppExceptionFaultBean>(_AppException_QNAME, AppExceptionFaultBean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceKomplet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceKomplet")
    public JAXBElement<GetRinfDataServiceKomplet> createGetRinfDataServiceKomplet(GetRinfDataServiceKomplet value) {
        return new JAXBElement<GetRinfDataServiceKomplet>(_GetRinfDataServiceKomplet_QNAME, GetRinfDataServiceKomplet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceOp }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceOp")
    public JAXBElement<GetRinfDataServiceOp> createGetRinfDataServiceOp(GetRinfDataServiceOp value) {
        return new JAXBElement<GetRinfDataServiceOp>(_GetRinfDataServiceOp_QNAME, GetRinfDataServiceOp.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceTu }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceTu")
    public JAXBElement<GetRinfDataServiceTu> createGetRinfDataServiceTu(GetRinfDataServiceTu value) {
        return new JAXBElement<GetRinfDataServiceTu>(_GetRinfDataServiceTu_QNAME, GetRinfDataServiceTu.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceOpResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceOpResponse")
    public JAXBElement<GetRinfDataServiceOpResponse> createGetRinfDataServiceOpResponse(GetRinfDataServiceOpResponse value) {
        return new JAXBElement<GetRinfDataServiceOpResponse>(_GetRinfDataServiceOpResponse_QNAME, GetRinfDataServiceOpResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceTuKomplet }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceTuKomplet")
    public JAXBElement<GetRinfDataServiceTuKomplet> createGetRinfDataServiceTuKomplet(GetRinfDataServiceTuKomplet value) {
        return new JAXBElement<GetRinfDataServiceTuKomplet>(_GetRinfDataServiceTuKomplet_QNAME, GetRinfDataServiceTuKomplet.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceTuResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceTuResponse")
    public JAXBElement<GetRinfDataServiceTuResponse> createGetRinfDataServiceTuResponse(GetRinfDataServiceTuResponse value) {
        return new JAXBElement<GetRinfDataServiceTuResponse>(_GetRinfDataServiceTuResponse_QNAME, GetRinfDataServiceTuResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRinfDataServiceKompletResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:rinf.pis.ws.ditec.sk", name = "getRinfDataServiceKompletResponse")
    public JAXBElement<GetRinfDataServiceKompletResponse> createGetRinfDataServiceKompletResponse(GetRinfDataServiceKompletResponse value) {
        return new JAXBElement<GetRinfDataServiceKompletResponse>(_GetRinfDataServiceKompletResponse_QNAME, GetRinfDataServiceKompletResponse.class, null, value);
    }

}
