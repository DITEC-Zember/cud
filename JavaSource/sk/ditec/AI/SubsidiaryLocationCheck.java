package sk.ditec.AI;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.util.*;
import java.util.regex.*;

public class SubsidiaryLocationCheck {
    private static final String CRD_XML = "M:/git/CUD/cud/crd/subsidiary location_vystup z CRD od dátumu 2025_06_01.xml";
    private static final String MAP_CSV = "M:/git/CUD/cud/crd/t_subsidiary_location.csv";

    public static void main(String[] args) throws Exception {
        File crdFile = new File(CRD_XML);
        File mapCsvFile = new File(MAP_CSV);
        if (!crdFile.exists() || !mapCsvFile.exists()) {
            System.err.println("Missing input files");
            System.exit(2);
        }

        // Read CRD XML content (UTF-8)
        String crdText = readFileToString(crdFile, "UTF-8");

        // Extract per-block Subsidiary_Location_Code and Start_Validity
        Pattern blockPat = Pattern.compile("<\\s*ns2:Subsidiary_Location\\s*>[\\s\\S]*?<\\s*/\\s*ns2:Subsidiary_Location\\s*>", Pattern.CASE_INSENSITIVE);
        Pattern codePat = Pattern.compile("<\\s*Subsidiary_Location_Code\\s*>\\s*([^<]+?)\\s*<\\s*/\\s*Subsidiary_Location_Code\\s*>", Pattern.CASE_INSENSITIVE);
        Pattern startPat = Pattern.compile("<\\s*Start_Validity\\s*>\\s*([^<]+?)\\s*<\\s*/\\s*Start_Validity\\s*>", Pattern.CASE_INSENSITIVE);
        Matcher bm = blockPat.matcher(crdText);
        Map<String, String> xmlStartByCode = new LinkedHashMap<String, String>();
        while (bm.find()) {
            String block = bm.group();
            Matcher cm = codePat.matcher(block);
            Matcher sm = startPat.matcher(block);
            String code = null;
            String start = null;
            if (cm.find()) code = cm.group(1).trim();
            if (sm.find()) start = sm.group(1).trim();
            if (code != null && code.length() > 0) {
                xmlStartByCode.put(code, start == null ? "" : start);
            }
        }
        System.out.println("XML codes: " + xmlStartByCode.size());

        // Parse CSV: map code -> START_VALIDITY
        Map<String, String> csvStartByCode = new HashMap<String, String>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mapCsvFile), "UTF-8"));
            try {
                String header = br.readLine();
                String delim = header.indexOf(';') >= 0 ? ";" : ",";
                String[] cols = header.split(Pattern.quote(delim), -1);
                int idxCode = -1, idxStart = -1;
                for (int i = 0; i < cols.length; i++) {
                    String col = cols[i].trim().replace("\"", "");
                    if (col.equalsIgnoreCase("SUBSIDIARY_LOCATION_CODE")) idxCode = i;
                    if (col.equalsIgnoreCase("START_VALIDITY")) idxStart = i;
                }
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(Pattern.quote(delim), -1);
                    if (idxCode >= 0 && idxCode < parts.length) {
                        String code = strip(parts[idxCode]);
                        String start = (idxStart >= 0 && idxStart < parts.length) ? strip(parts[idxStart]) : "";
                        if (code.length() > 0) csvStartByCode.put(code, start);
                    }
                }
            } finally {
                br.close();
            }
        } catch (MalformedInputException mie) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mapCsvFile), "windows-1250"));
            try {
                String header = br.readLine();
                String delim = header.indexOf(';') >= 0 ? ";" : ",";
                String[] cols = header.split(Pattern.quote(delim), -1);
                int idxCode = -1, idxStart = -1;
                for (int i = 0; i < cols.length; i++) {
                    String col = cols[i].trim().replace("\"", "");
                    if (col.equalsIgnoreCase("SUBSIDIARY_LOCATION_CODE")) idxCode = i;
                    if (col.equalsIgnoreCase("START_VALIDITY")) idxStart = i;
                }
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(Pattern.quote(delim), -1);
                    String code = (idxCode >= 0 && idxCode < parts.length) ? strip(parts[idxCode]) : "";
                    String start = (idxStart >= 0 && idxStart < parts.length) ? strip(parts[idxStart]) : "";
                    if (code.length() > 0) csvStartByCode.put(code, start);
                }
            } finally {
                br.close();
            }
        }
        System.out.println("CSV codes: " + csvStartByCode.size());

        // Compare normalized dates
        List<String[]> mismatches = new ArrayList<String[]>();
        for (Map.Entry<String, String> e : xmlStartByCode.entrySet()) {
            String code = e.getKey();
            String xmlStart = e.getValue();
            String csvStart = csvStartByCode.containsKey(code) ? csvStartByCode.get(code) : "";
            if (!equalsNullSafe(normalize(xmlStart), normalize(csvStart))) {
                mismatches.add(new String[]{code, xmlStart, csvStart});
            }
        }
        System.out.println("Start_Validity mismatches: " + mismatches.size());

        File outFile = new File("M:/git/CUD/cud/crd/subsidiary_location_check_report.csv");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "UTF-8"));
        try {
            bw.write("TYPE;CODE;STATUS;UNIQUE_ID;XML_START;CSV_START\n");
            for (int i = 0; i < mismatches.size(); i++) {
                String[] row = mismatches.get(i);
                bw.write("Start_Validity;");
                bw.write(row[0]);
                bw.write(";MISMATCH;");
                bw.write(row[0]);
                bw.write(';');
                bw.write(row[1] == null ? "" : row[1]);
                bw.write(';');
                bw.write(row[2] == null ? "" : row[2]);
                bw.write('\n');
            }
        } finally {
            bw.close();
        }
        System.out.println("Report written: " + outFile.getAbsolutePath());
    }

    private static String readFileToString(File file, String charset) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        try {
            char[] buf = new char[8192];
            int n;
            while ((n = br.read(buf)) != -1) {
                sb.append(buf, 0, n);
            }
        } finally {
            br.close();
        }
        return sb.toString();
    }

    private static String strip(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.length() >= 2 && s.startsWith("\"") && s.endsWith("\"")) {
            s = s.substring(1, s.length()-1);
        }
        return s;
    }

    private static boolean equalsNullSafe(String a, String b) {
        if (a == null) return b == null || b.length() == 0;
        if (b == null) return a.length() == 0;
        return a.equals(b);
    }

    private static String normalize(String val) {
        if (val == null) return "";
        val = val.trim();
        if (val.matches("\\d{4}-\\d{2}-\\d{2}.*")) {
            String yyyy = val.substring(0,4);
            String mm = val.substring(5,7);
            String dd = val.substring(8,10);
            return dd + "." + mm + "." + yyyy;
        }
        return val;
    }
}