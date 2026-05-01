package sk.ditec.AI;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class XmlElementCounter {
    private static final String XML_FILE = "M:/git/CUD/cud/crd/subsidiary location_vystup z CRD od dátumu 2025_06_01.xml";

    public static void main(String[] args) throws Exception {
        File file = new File(XML_FILE);
        if (!file.exists()) {
            System.err.println("XML file not found: " + file.getAbsolutePath());
            System.exit(2);
        }
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        SAXParser parser = factory.newSAXParser();
        final int[] count = {0};
        DefaultHandler handler = new DefaultHandler() {
            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                // Count only ns2:Subsidiary_Location elements
                if ("Subsidiary_Location".equals(localName) || (qName != null && qName.endsWith(":Subsidiary_Location"))) {
                    count[0]++;
                }
            }
        };
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            parser.parse(is, handler);
        } finally {
            if (is != null) {
                try { is.close(); } catch (Exception ignore) {}
            }
        }
        System.out.println("ns2:Subsidiary_Location elements: " + count[0]);
    }
}