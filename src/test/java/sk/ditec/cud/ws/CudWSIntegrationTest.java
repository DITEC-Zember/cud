package sk.ditec.cud.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import org.junit.Before;
import org.junit.Test;

import sk.ditec.common.bi.Page;
import sk.ditec.common.ws.AuthInfoWS;
import sk.ditec.cud.dto.DTOCiselnik;

/**
 * Integračný test pre CudWS webovú službu.
 * 
 * PREDPOKLAD: Webová služba musí bežať na http://localhost:8081/cud/CudWS
 * 
 * Spustenie: 1. Najprv spustite aplikáciu (napr. v Tomcat na porte 8081) 2. Potom spustite tento test
 */
public class CudWSIntegrationTest {

    // Použitie system properties pre flexibilitu - môže sa prepísať cez -Dwsdl.url=... a -DaccountId=...
    private static final String WSDL_URL = System.getProperty("wsdl.url", "http://localhost:8081/cud/CudWS?wsdl");
    private static final String ACCOUNT_ID = System.getProperty("accountId", "136");

	private CudWSRemote cudWS;

	@Before
	public void setUp() throws Exception {
		// Vytvorenie SOAP klienta z WSDL
		URL wsdlLocation = new URL(WSDL_URL);
		QName serviceName = new QName(NAMESPACE_URI, SERVICE_NAME);

		Service service = Service.create(wsdlLocation, serviceName);
		cudWS = service.getPort(CudWSRemote.class);

		System.out.println("SOAP klient úspešne vytvorený pre: " + WSDL_URL);
	}

	@Test
	public void testCiselnikList_BasicCall() throws Exception {
		// Príprava testovacích údajov
		AuthInfoWS authWS = createTestAuthInfo();
		Page page = createTestPage();
		DTOCiselnik dtoFilter = new DTOCiselnik();

		// Volanie webovej služby
		System.out.println("Volanie ciselnikList...");
		DTOCiselnik[] result = cudWS.ciselnikList(authWS, page, dtoFilter);

		// Overenia
		assertNotNull("Výsledok by nemal byť null", result);
		System.out.println("Test úspešný! Počet záznamov: " + (result != null ? result.length : 0));
	}

	@Test
	public void testCiselnikList_WithPagination() throws Exception {
		// Príprava testovacích údajov s pagináciou
		AuthInfoWS authWS = createTestAuthInfo();
		Page page = createTestPage();
		page.PAGE_SIZE = 10;
		page.PAGE = 1;

		DTOCiselnik dtoFilter = new DTOCiselnik();

		// Volanie webovej služby
		System.out.println("Volanie ciselnikList s pagináciou (strana: 1, veľkosť: 10)...");
		DTOCiselnik[] result = cudWS.ciselnikList(authWS, page, dtoFilter);

		// Overenia
		assertNotNull("Výsledok by nemal byť null", result);
		assertTrue("Počet záznamov by nemal prekročiť veľkosť strany", result.length <= 10);
		System.out.println("Test úspešný! Počet záznamov: " + result.length);
	}

	@Test
	public void testCiselnikList_WithFilter() throws Exception {
		// Príprava testovacích údajov s filtrom
		AuthInfoWS authWS = createTestAuthInfo();
		Page page = createTestPage();

		DTOCiselnik dtoFilter = new DTOCiselnik();
		// Nastavenie filtra - upravte podľa vašich potrieb
		// dtoFilter.setNazov("TEST");

		// Volanie webovej služby
		System.out.println("Volanie ciselnikList s filtrom...");
		DTOCiselnik[] result = cudWS.ciselnikList(authWS, page, dtoFilter);

		// Overenia
		assertNotNull("Výsledok by nemal byť null", result);
		System.out.println("Test úspešný! Počet filtrovaných záznamov: " + result.length);
	}

	/**
	 * Vytvorenie testovacieho AuthInfoWS objektu. POZOR: Upravte prihlasovacie údaje podľa vašich potrieb!
	 */
	private AuthInfoWS createTestAuthInfo() {
		AuthInfoWS authWS = new AuthInfoWS();

		// DÔLEŽITÉ: Nastavte platné prihlasovacie údaje pre váš systém
		// AuthInfoWS používa public fieldy, nie settery
		// Môže sa prepísať cez -DaccountId=...
		authWS.setAccountId(Integer.parseInt(ACCOUNT_ID));

		return authWS;
	}

	/**
	 * Vytvorenie testovacieho Page objektu.
	 */
	private Page createTestPage() {
		Page page = new Page();
		// Page používa public fieldy, nie settery
		page.PAGE_SIZE = 100;
		page.PAGE = 1;
		return page;
	}
}
