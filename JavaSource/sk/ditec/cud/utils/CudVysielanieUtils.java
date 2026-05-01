package sk.ditec.cud.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.torque.util.MyCriteria2;
import org.w3c.dom.Element;

import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.zsr.common.server.utils.DateUtils;
public class CudVysielanieUtils {

	private static final ValidationHandler validationHandler = new ValidationHandler();

	private static String StringdateFormat = "dd.MM.yyyy HH:mm:ss";
	private static DateFormat dateFormat = new SimpleDateFormat(StringdateFormat, new Locale("sk"));
	static String crdimportPath = FrameworkUtils.getConfigProperty("cud", "crdimport.path");

	static public <T> String marshal(T dto) throws AppException {
	

		StringWriter sw = new StringWriter();
		try {
		    // 1. Marshall do pamäte
		    JAXBContext ctx = JAXBContext.newInstance(dto.getClass());
		    Marshaller marshaller = ctx.createMarshaller();
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    marshaller.marshal(dto, baos);

		    byte[] xmlBytes = baos.toByteArray();
		    long sizeBytes = xmlBytes.length;
		    long sizeMb = sizeBytes / (1024 * 1024);

			// System.out.println("XML veľkosť = " + sizeMb + " MB");

		    // 2. Porovnanie s limitom
			long limitMb = 0;
			if (sizeMb <= limitMb) {
				// uloženie do Oracle
		    	return  new String(xmlBytes, Charset.forName("UTF-8"));
		        //saveToOracle(new String(xmlBytes, StandardCharsets.UTF_8));
		       // System.out.println("XML uložený do databázy.");
		    } else {
				// uloženie na disk
				DateFormat FILE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String name = "lok_import_" + FILE_FORMAT.format(new Date());
				saveXML(xmlBytes, name);
				// String xml = new String(xmlBytes, Charset.forName("UTF-8"));
				return name + "  velkost xml v MB:  " + sizeMb;
			}

//			JAXBContext jaxbContext = JAXBContext.newInstance(dto.getClass());
//			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//			// marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
//			jaxbMarshaller.marshal(dto, sw);
		} catch (JAXBException e) {
			throw new AppException("Chyba pri serializacii dto do xml", e); //$NON-NLS-1$
		}

		
	}


	private static String saveXML(byte[] xmlBytes, String nazov) {

		File adresar = new File(crdimportPath);
		if (!adresar.exists()) {
			adresar.mkdirs();
		}

		File file = new File(adresar, nazov + ".xml");
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			fos.write(xmlBytes);
			fos.flush();
			return "OK";
		} catch (IOException e) {
			// vrátime chybovú hlášku
			return "ERROR: " + e.getMessage();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// prípadná chyba pri zatváraní
					return "ERROR pri zatváraní súboru: " + e.getMessage();
				}
			}
		}
	}


	


	private static class ValidationHandler implements ValidationEventHandler {

		@Override
		public boolean handleEvent(ValidationEvent event) {
			return false;
		}
	}

	public static boolean isEqual(String str1, String str2) {
		if (str1 == null || str2 == null) {
			if (str1 == null && str2 == null) {
				return true;
			} else if (str1 != null && str1.length() == 0) {
				return true;
			} else if (str2 != null && str2.length() == 0) {
				return true;
			}
			return false;
		}
		return str1.equals(str2);
	}

	public static boolean isEqual(Boolean str1, Boolean str2) {
		if (str1 == null && str2 == null) {
			return true;
		} else if (str1 == null && str2 != null) {
			return false;
		} else if (str2 == null && str1 != null) {
			return false;
		}

		return str1.equals(str2);
	}

	public static boolean isEqual(Integer int1, Integer int2) {
		if (int1 == null || int2 == null) {
			// return false; if you assume null not equal to null
			return int1 == int2;
		}
		return int1.equals(int2);
	}

	public static boolean isEqual(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			// return false; if you assume null not equal to null
			if (date1 == null && date2 == null) {
				return true;
			}
			return false;
		}
		return date1.getTime() == date2.getTime();
	}

	public static boolean isEqual(BigDecimal dec, Double doub) {
		if (dec == null || doub == null) {
			// return false; if you assume null not equal to null
			if (dec == null && doub == null) {
				return true;
			}
			return false;
		}
		return doub.equals(Double.valueOf(dec.doubleValue()));

		// dec.toString().equals(doub.toString());
	}

	public static boolean isEqual(String stringInt, Integer i) {
		if (stringInt == null && i != null) {
			return false;
		}
		return Integer.valueOf(stringInt).equals(i);
	}

	public static boolean isEqual(boolean imFlag, String imFlag2) {
		if (imFlag2 == null) {
			return false;
		} else if ("T".equals(imFlag2) && imFlag) {
			return true;
		} else if ("F".equals(imFlag2) && !imFlag) {
			return true;
		}
		return false;
	}

	public static String dateTimeCritFormat(Date datum) {

		return " TO_TIMESTAMP('"
 + DateUtils.formatDate(datum, "dd.MM.yyyy HH:mm") + "','DD.MM.YYYY HH24:MI') ";
	}

	public static String timeCritFormat(Date datum) {

		return " TO_TIMESTAMP('" + DateUtils.formatDate(datum, "dd.MM.yyyy HH:mm:ss") + "','DD.MM.YYYY HH24:MI:SS') ";
	}

	public static String dateCritFormat(Date datum) {

		return " TO_TIMESTAMP('" + DateUtils.formatDate(datum, "dd.MM.yyyy") + "','DD.MM.YYYY') ";
	}

	// xmlDateFormat                   <n1:StartDate>1957-08-13</n1:StartDate>
	public static String dateToStringXmlDate(Date datum) {
		if (datum == null) {
			return null;
		}
		return DateUtils.formatDate(datum, "yyyy-MM-dd");
	}

	public static XMLGregorianCalendar dateToXmlDateTime(Date datum) {
		if (datum == null) {
			return null;
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(datum);
		XMLGregorianCalendar xmlDatum = null;
		try {
			xmlDatum = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}
		return xmlDatum;
	}

	public static XMLGregorianCalendar dateToXmlDate(Date datum) {
		if (datum == null) {
			return null;
		}


		Calendar cal = Calendar.getInstance();
		cal.setTime(datum);
			int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;// lebo vrati o mesiac menej
			int day = cal.get(Calendar.DAY_OF_MONTH);


			// DatatypeConstants.FIELD_UNDEFINED // časová zóna
		XMLGregorianCalendar xmlDatum = null;
		try {
			xmlDatum = DatatypeFactory.newInstance().newXMLGregorianCalendarDate(year, month, day, 0);
			xmlDatum.setTimezone(DatatypeConstants.FIELD_UNDEFINED); // odsekne timezone
		} catch (DatatypeConfigurationException e) {
			e.printStackTrace();
		}

		return xmlDatum;
	}

	public static XMLGregorianCalendar convertToXMLGregorian(Date date) throws AppException {
		try {
			XMLGregorianCalendar retValue = null;
			if (null != date) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(date);
				XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
				retValue = xmlDate;
			}
			return retValue;
		} catch (Exception e) {
			return null;
		}
	}


	static public Date getCasNaslImp(DTOOdberatelObjekt dtooo, Date cas) {
		// Systém zráta nasledujúci čas exportu
		// AK cudOdberatelObjekt.POSLEDNY_EXPORT nie je null
		// AK cudOdberatelObjekt.POSLEDNY_EXPORT<cudOdberatelObjekt.POSLEDNY_PLANOVANY_EXPORT
		// Systém vrátu cudOdberatelObjekt.POSLEDNY_EXPORT
		// END AK
		// END AK
		if (dtooo.getCasPoslExportu() != null && dtooo.getCasPoslExportuPlan() != null
				&& dtooo.getCasPoslExportu().getTime() < dtooo.getCasPoslExportuPlan().getTime()) {
			return dtooo.getCasPoslExportu();
		}
		// values = { 1 - denne, 2 - tyzdenne, 3 - mesacne, 4 - 3mesacne, 5 - pri zmene }
		// AK cudOdberatelObjekt.OPAKOVANIE = "Denne" tak INTERVAL=1 den END AK
		Long kontrolnyInterval = (long) 0;
		if (dtooo.getOpakovanie() != null) {
			// AK cudOdberatelObjekt.OPAKOVANIE = "Týždenne" tak INTERVAL=7 dní END AK
			if ("1".equals(dtooo.getOpakovanie())) {
				// cas = ( new Date(cas.getTime() + (24*60 * 60000 )) );
				kontrolnyInterval = (long) (24 * 60 * 60000);
			} else if ("2".equals(dtooo.getOpakovanie())) {
				kontrolnyInterval = (long) (7 * 24 * 60 * 60000);
			}
			// TODO doplnit intervaly
			// AK cudOdberatelObjekt.OPAKOVANIE = "Mesačne" tak INTERVAL=1 mesiac END AK
			// AK cudOdberatelObjekt.OPAKOVANIE = "3 mesiace" tak INTERVAL=3 mesiace END AK
			// AK cudOdberatelObjekt.OPAKOVANIE = "Pri zmene" tak INTERVAL=0,

		}
		// AK cudOdberatelObjekt.POSLEDNY_EXPORT je null
		if (dtooo.getCasPoslExportu() == null) {
			// NASLEDUJÚCI_EXPORT= cudOdberatelObjekt.PLATNOST_OD+KONTROLNY_INTERVAL\
			return new Date(dtooo.getPlatnostOd().getTime() + kontrolnyInterval);
		}
		if (dtooo.getCasPoslExportu() == null) {
			return new Date(cas.getTime() + kontrolnyInterval);
		}
		// INAK
		// NASLEDUJÚCI_EXPORT = aktuálny dátum a čas - KONTROLNY_INTERVAL
		// END
		// END AK
		// AK cudOdberatelObjekt.POSLEDNY_PLANOVANY_EXPORT+INTERVAL <= cudOdberatelObjekt.POSLEDNY_EXPORT
		// NASLEDUJÚCI_EXPORT=cudOdberatelObjekt.POSLEDNY_PLANOVANY_EXPORT+INTERVAL
		// KONIEC CYKLU
		// Systém vráti hodnotu NASLEDUJÚCI_EXPORT
		return cas;
	}

	
	static public String refactorApostrof(String text) {
		if (text == null) {
			return null;
		}
		return text.replaceAll("'", "''");
	}

	static public String getStringBezDia(String text) {
		if (text == null) {
			return null;
		}
		try {

		return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		} catch (Exception e) {
			return text;
		}
	}

	public static String getCritEqualsDateMs(String atribut, Date datum) {
		String crit = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		// JAva do verzie 8 neberie 4 SSSS microsekundy, potom na zaciatok doplni 0
		String strDate = sdf.format(datum);
		if (null != atribut && null != datum) {
			crit = atribut + " = TO_TIMESTAMP('" + strDate + "','YYYY-MM-DD HH24:MI:SS.FF3') ";
		}
		return crit;

	}

	public static String getCritGreaterEqualsActDateOrNull(String atribut, Date datum) {
		String sql = "";
		// Date datum = DateUtils.withoutHHMMSS(new Date());
		if (null != atribut) {
			MyCriteria2 crit = new MyCriteria2();
			crit.addConditional(atribut, datum, MyCriteria2.GREATER_EQUAL);
			sql = crit.getCriterion(atribut).toString();

			sql = "( " + sql + " OR " + atribut + " IS NULL)";
		}
		return sql;
	}

	public static String getCritGreaterEqualsActDateNotNull(String atribut, Date datum) {
		String sql = "";
		// Date datum = DateUtils.withoutHHMMSS(new Date());
		if (null != atribut) {
			MyCriteria2 crit = new MyCriteria2();
			crit.addConditional(atribut, datum, MyCriteria2.GREATER_EQUAL);
			sql = crit.getCriterion(atribut).toString();

			sql = "( " + sql + " AND " + atribut + " IS NOT NULL)";
		}
		return sql;
	}

	public static String getCritPlatneDatumOdDo(Date datum, String atributDatumOd, String atributDatumDo) {
		// PLATNOST_OD <= datumACasNacitaniaDat
		// A zároveň
		// (
		// PLATNOST_DO JE NULL
		// ALebo
		// PLATNOST_DO >= dátum(datumACasNacitaniaDat)
		// )

		String sql = "";
		if (datum == null) {
			return "";
		}
		if (null != atributDatumOd && null != atributDatumDo) {
			sql = atributDatumOd + " <=  " + CudVysielanieUtils.dateCritFormat(datum) + " AND ( " + atributDatumDo
					+ " is null OR " + atributDatumDo + " >= " + CudVysielanieUtils.dateCritFormat(datum) + ")";
		}
		return sql;
	}

	
	
	
	public static String getEmailText( String text, String aplikacia) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		String stringDatum = formatter.format(new Date());
		String doplText = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 4.01 Transitional//EN\">"
				+ "<html>"
				// + "<head><meta http-equiv=Content-Type content=\"text/html; charset=utf-8\">"
				// zadanie typu bolt neberie do uvahy "<b></b>"
				+ "<b>Aplikácia:</b>"
				+ aplikacia
				+ "<br/><b>Čas:</b>"
				+ stringDatum
				+ "<br><b>Popis chyby:</b>"
				+ text
				+ "</html>"
				+ "<br/><hr/> Tento mail bol automaticky vygenerovaný pre chybu počas behu procesu prípravy/odoslaní súboru do CRD.</body></html>";
		return doplText;


//		.setEmailText("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /><style  type=\"text/css\"> body { font-family: Arial, Helvetica, sans-serif; font-size: 8pt; color: black; text-decoration: none; margin: 8px; background-color: #EFEFF7; } a { text-decoration: none; color: black; } a:visited { text-decoration: none; } a:hover { text-decoration: underline; } input,textarea { background-color: white; } textarea { overflow: hide; } select { background-color: #B5CFE7; margin: 0px; border: 1px solid #83838F; } img { border: 0px solid black; } h2 { font-family: verdana, arial, sans-serif; color: #999; font-size: 12px; font-weight: bold; background: #FFF; line-height: 14px; padding: 2px; margin-top: 10px; border-bottom: 1px dotted #666; text-transform: uppercase; letter-spacing: .2em; } .headerTR { align: center; font-weight: bold; font-size: 11px; } .contentTR { align: center;font-size: 11px; } .contentTD { align: center; font-size: 11px; }</style></head><body> V aplikácii Centrálna správa kmeňových údajov bola iniciovaná zmena číselníka <b>{ciselnik}</b>. <br/> Typ operácie : {operacia} <br/> {idZaznamu} <br/><br/><table border=\"1\"  cellpadding=\"2\" cellspacing=\"0\"> {tableContent}</table> <br/> Zmena bola iniciovaná používateľom <b>{user}</b> a nadobudne účinnosť dňa <b>{ucinnost}</b> <br/> Poznámka ku zmene : {poznamka} <br/> <br/> <br/> <br/><hr/> Prosím, neodpovedajte na túto správu. Odosielateľ je nemonitorovaný poštový priečinok.</body></html>");
//daoKmdWfDefPrvy.setEmailSubject("Iniciovaná zmena v číselníku {ciselnik}");

	}

	public static Date getMin(Date datum1, Date datum2) {
		if (datum2 == null) {
			return datum1;
		}
		if (datum1 == null) {
			return datum2;
		}

		if (datum1.getTime() > datum2.getTime()) {
			return datum2;
		}
		return datum1;
	}

	public static Date getMax(Date datum1, Date datum2) {
		if (datum2 == null) {
			return datum1;
		}
		if (datum1 == null) {
			return datum2;
		}

		if (datum1.getTime() < datum2.getTime()) {
			return datum2;
		}
		return datum1;
	}

	public static String getStringDatum(Date datum) {
		if (datum == null) {
			return null;
		}
		String StringdateFormat = "dd.MM.yyyy";
		DateFormat dateFormat = new SimpleDateFormat(StringdateFormat, new Locale("sk"));

		return dateFormat.format(datum);
	}

	public static String getStringDatumCas(Date datum) {
		if (datum == null) {
			return null;
		}
		return dateFormat.format(datum);
	}

	public static String extractCDataContent(String text) {
		if (text == null || text.isEmpty())
			return text;

		int cdataStart = text.indexOf(_CudConsts.CDATA_START);
		int cdataEnd = text.lastIndexOf(_CudConsts.CDATA_END);

		if (cdataStart != -1 && cdataEnd != -1)
			return text.substring(cdataStart + _CudConsts.CDATA_START.length(), cdataEnd);

		// Ak text neobsahuje CDATA sekvenciu
		return text;
	}

	public static String extractContentFromObject(Object o) throws AppException {
		if (o instanceof Element) {
			o = ((Element) o).getTextContent();
		}

		if (!(o instanceof String)) {
			throw new AppException("Neocakavany typ objektu. Ocakavany je String.");
		}

		String message = ((String) o).trim();
		if (message.startsWith(_CudConsts.CDATA_START) && message.endsWith(_CudConsts.CDATA_END)) {
			message = CudVysielanieUtils.extractCDataContent(message);
		}

		return message;
	}

	public static String getStringZaokruhleneNa6(Double number) {
		if (number == null) {
			return null;
		}
		String formatted = String.format("%.6f", number);
		return formatted;
	};

	public static Float getStringZaokruhleneNa5(Double number) {
		if (number == null) {
			return null;
		}
		// Zaokrúhlenie na 5 desatinných miest pomocou BigDecimal
		BigDecimal bd = BigDecimal.valueOf(number);
		bd = bd.setScale(5, RoundingMode.HALF_UP);

		// Prevod na float
		float result = bd.floatValue();
		return result;
	};
}
