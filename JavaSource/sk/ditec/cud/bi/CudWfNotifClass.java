package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfDefCiselnikStlpec;
import sk.ditec.cud.dto.DTOWfNotif;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudKontrolaUtils;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.notif.DTOAttachment;
import sk.ditec.notif.NotifUtils;

public class CudWfNotifClass extends _CudBaseClass {

	private Logger log = LoggerFactory.getLogger(CudWfNotifClass.class);

	private String[] lookupEmailList(DTOWfDef dtoDef, Map<String, String> rowMap) throws AppException {

		try {
			Set<String> set = new HashSet<String>();

			if (StringUtils.isValid(dtoDef.getEmailList()) && _CudKontrolaUtils.isValidEmailList(dtoDef.getEmailList())) {
				set.addAll(new ArrayList<String>(Arrays.asList(_CudLookupUtils.lookupEmailList(dtoDef.getEmailList()))));
			}

			if (StringUtils.isValid(dtoDef.getWfDefCiselnikStlpecList())) {
				for (DTOWfDefCiselnikStlpec dtoCsDef : dtoDef.getWfDefCiselnikStlpecList()) {
					String emailsRow = rowMap.get(dtoCsDef.getCiselnikStlpecNazov());
					if (StringUtils.isValid(emailsRow) && _CudKontrolaUtils.isValidEmailList(emailsRow)) {
						// set.addAll(new ArrayList<String>(Arrays.asList(_CudLookupUtils.lookupEmailList(emailsRow))));
					}
				}
			}

			return set.isEmpty() ? null : set.toArray(new String[set.size()]);

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupEmailList.error");
			return null;
		}
	}

	private String lookupEmailSubject(DTOWfNotif dtoNotif, DTOWfDef dtoDef, DTOWfTodo dtoTodo) throws AppException {

		try {
			String title = dtoDef.getEmailSubject();
			if (!StringUtils.isValid(title)) {
				return null;
			}

			title = StringUtils.replaceAll(title, "{ciselnik}", dtoNotif.getCiselnikNazov());

			if (_CudConsts.WF_DEF_TYP_SC.equals(dtoDef.getTyp())) {
				String stav = "";
				if ("T".equals(dtoTodo.getPotvrdeny())) {
					stav = _CudConsts.TEXT_WF_DEF_TYP_SC;
				} else if ("F".equals(dtoTodo.getPotvrdeny())) {
					stav = _CudConsts.TEXT_WF_DEF_TYP_ZAM;
				}
				title = StringUtils.replaceAll(title, "{stavSch}", stav);
			}

			return title;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupEmailSubject.error");
			return null;
		}
	}

	private boolean jeObnovaZaznamu(String operacia, List<DTOCiselnikStlpecGui> guiList, List<DTOZmenaStlpec> zsList) throws AppException {

		try {
			if (!_CudConsts.ZMENA_OPERACIA_U.equals(operacia)) {
				return false;
			}

			DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiJedinecny(guiList);
			if (!StringUtils.isValid(dtoCS)) {
				return false;
			}

			DTOZmenaStlpec dtoZS = _CudLookupUtils.lookupDTOZmenaStlpecByFk(zsList, dtoCS.getCiselnikStlpecNazov());
			if (!StringUtils.isValid(dtoZS)) {
				return false;
			}

			if (StringUtils.isValid(dtoZS.getOldValue()) && StringUtils.isValid(dtoZS.getNewValue())) {
				if (dtoZS.getOldValue().equals(dtoZS.getNewValue())) {
					return true;
				}
			}

			return false;

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeObnovaZaznamu.error");
			return false;
		}
	}

	private List<DTOCiselnikStlpecGui> guiListSort(List<DTOCiselnikStlpecGui> list) throws AppException {

		try {
			List<DTOCiselnikStlpecGui> resultList = new ArrayList<DTOCiselnikStlpecGui>();

			for (DTOCiselnikStlpecGui dto : list) {
				if ("T".equals(dto.getFormZobrazenie())) {
					resultList.add(dto);
				}
			}

			Collections.sort(resultList, new Comparator<DTOCiselnikStlpecGui>() {
				public int compare(DTOCiselnikStlpecGui e1, DTOCiselnikStlpecGui e2) {
					return e1.getPoradie().compareTo(e2.getPoradie());
				}
			});

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getCiselnikStlpecListSort.error");
			return null;
		}
	}

	private String createRecordTable(List<DTOCiselnikStlpecGui> guiList, Map<String, String> rowMap) throws AppException {

		try {
			if (rowMap.keySet().isEmpty()) {
				return "";
			}

			StringBuilder tableContent = new StringBuilder("<tr class=\"headerTR\">");
			tableContent.append("<td> Položka </td>");
			tableContent.append("<td> Hodnota </td>");
			tableContent.append("</tr>");

			for (DTOCiselnikStlpecGui dtoCS : guiListSort(guiList)) {
				tableContent.append("<tr class=\"contentTR\">");
				tableContent.append("<td class=\"contentTD\">").append(dtoCS.getNadpis()).append("</td>");

				String value = rowMap.get(dtoCS.getCiselnikStlpecNazov());
				if (!StringUtils.isValid(value)) {
					value = "&nbsp;";
				} else if ("T".equals(value)) {
					value = "Áno";
				} else if ("F".equals(value)) {
					value = "Nie";
				}

				tableContent.append("<td class=\"contentTD\">").append(value).append("</td>");
				tableContent.append("</tr>");
			}

			return tableContent.toString();

		} catch (Throwable t) {
			DBUtils.handleException(t, "createRecordTable.error");
			return null;
		}
	}

	private String createZmenaTable(List<DTOCiselnikStlpecGui> guiList, List<DTOZmenaStlpec> zsList) throws AppException {

		try {
			StringBuffer tableContent = new StringBuffer("<tr class=\"headerTR\">");
			tableContent.append("<td>").append(_CudConsts.TEXT_POLOZKA).append("</td>");
			tableContent.append("<td>").append(_CudConsts.TEXT_STARA_HODNOTA).append("</td>");
			tableContent.append("<td>").append(_CudConsts.TEXT_NOVA_HODNOTA).append("</td>");
			tableContent.append("</tr>");

			for (DTOZmenaStlpec dtoZS : zsList) {

				tableContent.append("<tr class=\"contentTD\">");

				DTOCiselnikStlpecGui dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(guiList, dtoZS.getIDCiselnikStlpec());
				tableContent.append("<td>").append(dtoCS.getNadpis()).append("</td>");

				String oldValue = dtoZS.getOldValue();
				if (!StringUtils.isValid(oldValue)) {
					oldValue = "&nbsp;";
				} else if ("T".equals(oldValue)) {
					oldValue = "Áno";
				} else if ("F".equals(oldValue)) {
					oldValue = "Nie";
				}
				tableContent.append("<td>").append(oldValue).append("</td>");

				String newValue = dtoZS.getNewValue();
				if (!StringUtils.isValid(newValue)) {
					newValue = "&nbsp;";
				} else if ("T".equals(newValue)) {
					newValue = "Áno";
				} else if ("F".equals(newValue)) {
					newValue = "Nie";
				}
				tableContent.append("<td>").append(newValue).append("</td>");

				tableContent.append("</tr>");
			}

			return tableContent.toString();

		} catch (Throwable t) {
			DBUtils.handleException(t, "getZmenaTable.error");
			return null;
		}
	}

	private String lookupEmailText(AuthInfo auth, DTOWfNotif dtoNotif, DTOWfDef dtoDef, DTOWfTodo dtoTodo, List<DTOCiselnikStlpecGui> guiList, List<DTOZmenaStlpec> zsList, Map<String, String> rowOldMap) throws AppException {

		try {
			if (!StringUtils.isValid(dtoDef.getEmailText())) {
				return null;
			}

			String emailText = StringUtils.replaceAll(dtoDef.getEmailText(), "{ciselnik}", dtoNotif.getCiselnikNazov());

			if (_CudConsts.WF_DEF_TYP_SC.equals(dtoDef.getTyp())) {
				String stavUloha = "";
				if ("T".equals(dtoTodo.getPotvrdeny())) {
					stavUloha = _CudConsts.TEXT_WF_DEF_TYP_SC;
				} else if ("F".equals(dtoTodo.getPotvrdeny())) {
					stavUloha = _CudConsts.TEXT_WF_DEF_TYP_ZAM;
				}
				emailText = StringUtils.replaceAll(emailText, "{stavUloha}", stavUloha);
			}

			String operaciaNazov = _CudLookupUtils.lookupZmenaOperaciaNazov(dtoNotif.getZmenaOperacia());
			if (_CudConsts.ZMENA_OPERACIA_U.equals(dtoNotif.getZmenaOperacia()) && jeObnovaZaznamu(dtoNotif.getZmenaOperacia(), guiList, zsList)) {
				operaciaNazov = _CudConsts.TEXT_ZMENA_OPERACIA_O;
			}
			emailText = StringUtils.replaceAll(emailText, "{operacia}", operaciaNazov);

			String hodnotyTableContent = createRecordTable(guiList, rowOldMap);
			emailText = StringUtils.replaceAll(emailText, "{hodnotyTableContent}", hodnotyTableContent);

			String zmenaTableContent = createZmenaTable(guiList, zsList);
			emailText = StringUtils.replaceAll(emailText, "{zmenaTableContent}", zmenaTableContent);

			String poznamka = "";
			if (StringUtils.isValid(dtoNotif.getPoznamka())) {
				poznamka = "Poznámka:<br/>" + dtoNotif.getPoznamka() + "<br/>";
			}
			emailText = StringUtils.replaceAll(emailText, "{poznamka}", poznamka);

			if (_CudConsts.WF_DEF_TYP_SC.equals(dtoDef.getTyp())) {
				String stavUser = "";
				if ("T".equals(dtoTodo.getPotvrdeny())) {
					stavUser = _CudConsts.TEXT_USER_SC;
				} else if ("F".equals(dtoTodo.getPotvrdeny())) {
					stavUser = _CudConsts.TEXT_USER_ZAM;
				}
				emailText = StringUtils.replaceAll(emailText, "{stavUser}", stavUser);
			}

			emailText = StringUtils.replaceAll(emailText, "{user}", auth.getAccountName());

			emailText = StringUtils.replaceAll(emailText, "{platnostOd}", _CudConsts.DATE_FORMAT.format(dtoNotif.getPlatnostOd()));

			return emailText;

		} catch (Throwable t) {
			handleException(t, "lookupEmailText.error", auth);
			return null;
		}

	}

	public void sendNotif(AuthInfo auth, DTOWfNotif dtoNotif, DTOWfDef dtoDef, DTOWfTodo dtoTodo, List<DTOCiselnikStlpecGui> guiList, List<DTOZmenaStlpec> zsList, Map<String, String> rowOldMap) throws AppException {

		try {
			if ("F".equals(dtoDef.getEmailSend())) {
				log.error("EmailSend==\'F\' pre ciselnik ciselnikID={} a typ={}, email sa neposiela!", dtoNotif.getCiselnikID(), dtoDef.getTyp());
				return;
			}

			String[] emailList = lookupEmailList(dtoDef, rowOldMap);
			if (!StringUtils.isValid(emailList)) {
				log.error("EmailList pre ciselnik ciselnikID={} a typ={} je prazdny, email sa neposiela!", dtoNotif.getCiselnikID(), dtoDef.getTyp());
				return;
			}

			String emailSubject = lookupEmailSubject(dtoNotif, dtoDef, dtoTodo);
			if (!StringUtils.isValid(emailSubject)) {
				log.error("EmailSubject pre ciselnik ciselnikID={} a typ={} nie je vyplneny, email sa neposiela!", dtoNotif.getCiselnikID(), dtoDef.getTyp());
				return;
			}

			String emailText = lookupEmailText(auth, dtoNotif, dtoDef, dtoTodo, guiList, zsList, rowOldMap);
			if (!StringUtils.isValid(emailText)) {
				log.error("EmailText pre ciselnik ciselnikID={} a typ={} nie je vyplneny, email sa neposiela!", dtoNotif.getCiselnikID(), dtoDef.getTyp());
				return;
			}

			NotifUtils.sendNotif("", emailList, emailSubject, emailText);

		} catch (Throwable t) {
			handleException(t, "sendNotif.error", auth);
		}
	}

	public void sendNotifError(String emailSubject, String errMsg) throws AppException {

		try {
			String[] emailList = _CudLookupUtils.lookupEmailList(FrameworkUtils.getConfigProperty("cud", "error.mail.list"));
			if (!StringUtils.isValid(emailList)) {
				log.error("Nie je vyplneny konfiguracny parameter: error.mail.list, email sa neposiela!");
				return;
			}

			String emailText = FrameworkUtils.getConfigProperty("cud", "error.mail.body");
			if (!StringUtils.isValid(emailText)) {
				log.error("Nie je vyplneny konfiguracny parameter: error.mail.body, email sa neposiela!");
				return;
			}
			emailText = StringUtils.replaceAll(emailText, "{CUD_MSG_ERROR}", errMsg);

			NotifUtils.sendNotif("", emailList, emailSubject, emailText);

		} catch (Throwable t) {
			DBUtils.handleException(t, "sendNotifError.error");
		}
	}

	public void sendNotif(String emailSubject, String emailText, DTOWfDef dtoDef, DTOAttachment[] att) throws AppException {

		try {
			String[] emailList = lookupEmailList(dtoDef, new HashMap<String, String>());
			if (!StringUtils.isValid(emailList)) {
				return;
			}

			String htmlBody = FrameworkUtils.getConfigProperty("cud", "error.mail.body");
			if (!StringUtils.isValid(htmlBody)) {
				log.error("Nie je vyplneny konfiguracny parameter: error.mail.body, email sa neposiela!");
				return;
			}
			htmlBody = StringUtils.replaceAll(htmlBody, "{CUD_MSG_ERROR}", emailText);

			if (StringUtils.isValid(att)) {
				NotifUtils.sendNotif("", emailList, emailSubject, htmlBody, att);
			} else {
				NotifUtils.sendNotif("", emailList, emailSubject, htmlBody);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "sendNotif.error");
		}
	}

}
