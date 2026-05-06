package sk.ditec.cud.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Jednoduchý smoke test pre overenie dostupnosti CudWS webovej služby. Tento test len overuje, či je WSDL prístupný -
 * nevyžaduje prihlasovacie údaje.
 */
public class CudWSAvailabilityTest {

	// Použitie system property pre flexibilitu - defaultne localhost:8081, ale môže sa prepísať cez -Dwsdl.url=...
	private static final String WSDL_URL = System.getProperty("wsdl.url", "http://localhost:8081/cud/CudWS?wsdl");

	// @Test
	public static void main(String[] args) throws Exception {

		// public void testWsdlIsAccessible() throws Exception {
		System.out.println("Overujem dostupnosť WSDL: " + WSDL_URL);

		URL url = new URL(WSDL_URL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(5000); // 5 sekúnd timeout
		connection.setReadTimeout(5000);

		try {
			int responseCode = connection.getResponseCode();

			assertEquals("WSDL by mal byť dostupný (HTTP 200)", HttpURLConnection.HTTP_OK, responseCode);

			// Overenie, že odpoveď obsahuje XML
			String contentType = connection.getContentType();
			assertTrue("Content-Type by mal byť XML", contentType != null && (contentType.contains("xml") || contentType.contains("wsdl")));

			// Prečítanie časti obsahu pre overenie
			InputStream inputStream = connection.getInputStream();
			byte[] buffer = new byte[1024];
			int bytesRead;
			boolean b1 = false;
			boolean b2 = false;
			while ((bytesRead = inputStream.read(buffer)) != -1 && !b1 && !b2) {
				String content = new String(buffer, 0, bytesRead, "UTF-8");
				b1 = content.contains("wsdl");
				b2 = content.contains("CudWS");
			}

			// Overenie, že obsah obsahuje WSDL elementy
			assertTrue("Obsah by mal obsahovať WSDL definície", b1);
			assertTrue("Obsah by mal obsahovať CudWS service", b2);

			System.out.println("✓ WSDL je dostupný a validný");
			System.out.println("✓ HTTP Status: " + responseCode);
			System.out.println("✓ Content-Type: " + contentType);

		} finally {
			connection.disconnect();
		}
	}

}
