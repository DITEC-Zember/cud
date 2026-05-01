package sk.ditec.cud.procvys;

import static sk.ditec.common.db.DBUtils.handleException;
import static sk.ditec.cud.procvys.ExportPripravaSuborovPreExport.generujNazovSuboru;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.crd._CudCrdDelegate;
import sk.ditec.crd.dto.DTOSend;
import sk.ditec.crd.dto.DTOSendSubor;
import sk.ditec.crd.dto.DTOTCountry;
import sk.ditec.crd.dto.DTOTPrimaryLocation;
import sk.ditec.crd.dto.DTOTSubsidiaryLocation;
import sk.ditec.crd.generated.tsi3_5.CountryCodeISO;
import sk.ditec.crd.generated.tsi3_5.LocationFileDatasetMessage;
import sk.ditec.crd.generated.tsi3_5.LocationSubsidiaryCode;
import sk.ditec.crd.generated.tsi3_5.LocationSubsidiaryInformation;
import sk.ditec.crd.generated.tsi3_5.MessageHeader;
import sk.ditec.crd.generated.tsi3_5.MessageReference;
import sk.ditec.crd.generated.tsi3_5.Recipient;
import sk.ditec.crd.generated.tsi3_5.Sender;
import sk.ditec.crd.generated.tsi3_5.ValidityPeriod;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.procvys.dto.DTOExportnySubor;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.notif.NotifUtils;

public class ExportVytvorenieSuboruCRD3_5 {

	private final _CudCrdDelegate dlgCrd = new _CudCrdDelegate();

	private final DTOOdberatelObjekt odberatelObjekt;
	private final Date datumACasNacitaniaDat;
	private final String companyCode = "0056";
	private final String messageType = "6002";
	private final String version = "3.2.0.1";

	public ExportVytvorenieSuboruCRD3_5(DTOOdberatelObjekt odberatelObjekt, Date datumACasNacitaniaDat) {
		this.odberatelObjekt = odberatelObjekt;
		this.datumACasNacitaniaDat = datumACasNacitaniaDat;
	}

	public void vytvorenieSuboruCrdLocationPrimary(AuthInfo auth, DTOCiselnik cudCiselnik) {
		// try {
		// DTOTCountry idCountrySK = dlgCrd.getTCudCiselnikyClass().getCountry(auth, "SK");
		//
		// List<DTOTPrimaryLocation> primaryListAktualne = dlgCrd.getTCudCiselnikyClass()
		// .vratAktualnePlatneZaznamyPreExportCRDPrimary(auth, odberatelObjekt.getCasPoslExportu(),
		// datumACasNacitaniaDat, idCountrySK);
		//
		// List<DTOTPrimaryLocation> primaryListBuduce = dlgCrd.getTCudCiselnikyClass()
		// .vratVBuducnostiPlatneZaznamyPreExportCRDPrimary(auth, odberatelObjekt.getCasPoslExportu(),
		// datumACasNacitaniaDat, idCountrySK);
		//
		// List<DTOTPrimaryLocation> primaryList = new ArrayList<DTOTPrimaryLocation>(primaryListAktualne);
		// primaryList.addAll(primaryListBuduce);
		//
		// for (DTOTPrimaryLocation primLok : primaryList) {
		// DTOTPrimaryLocation predchPlatnaLokalita = dlgCrd.getTCudCiselnikyClass()
		// .vratPoslednyStarsiZaznamPreDanuLokalituPrimary(auth, primLok.getLocationCode(), idCountrySK,
		// odberatelObjekt.getCasPoslExportu());
		//
		// String messageStatus = null;
		// if (predchPlatnaLokalita == null || "T".equals(predchPlatnaLokalita.getZmaz())) {
		// messageStatus = _CudConsts.EXPORT_CRD_MESSAGE_STATUS_NEW;
		// } else {
		// if ("F".equals(predchPlatnaLokalita.getZmaz())) {
		// messageStatus = _CudConsts.EXPORT_CRD_MESSAGE_STATUS_UPDATE;
		// } else {
		// messageStatus = _CudConsts.EXPORT_CRD_MESSAGE_STATUS_DELETE;
		// }
		// }
		//
		// LocationFileDatasetMessage mess = new LocationFileDatasetMessage();
		// if (primLok.getLocationCode().length() > 5) { // iba prvych 5 cislic
		// mess.setLocationPrimaryCode(Integer.parseInt(primLok.getLocationCode().substring(0, 5)));
		// } else {
		// mess.setLocationPrimaryCode(Integer.parseInt(primLok.getLocationCode()));
		// }
		// mess.setMessageStatus(messageStatus);
		//
		// UUID guid = UUID.randomUUID();
		// MessageHeader messageHeader = vytvorMessageHeader(guid);
		// mess.setMessageHeader(messageHeader);
		//
		// CountryCodeISO countryCodeISO = new CountryCodeISO();
		// countryCodeISO.setValue("SK");
		// mess.setCountryCodeISO(countryCodeISO);
		//
		// LocationPrimaryInformation locationPrimaryInformation = new LocationPrimaryInformation();
		// locationPrimaryInformation.setResponsibleIM(companyCode);
		// locationPrimaryInformation.setPrimaryLocationNameASCII(primLok.getLocationNameAscii());
		//
		// locationPrimaryInformation.setFreightFlag("1".equals(primLok.getFreightPossibleFlag()));
		// if (primLok.getFreightStartValidity() != null || primLok.getFreightEndValidity() != null) {
		// ValidityPeriod freightFlagValidityPeriod = new ValidityPeriod();
		// freightFlagValidityPeriod.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(primLok
		// .getFreightStartValidity()));
		// freightFlagValidityPeriod.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(primLok
		// .getFreightEndValidity()));
		// locationPrimaryInformation.setFreightValidityPeriod(freightFlagValidityPeriod);
		// }
		//
		// GeographicCoordinates coordinates = new GeographicCoordinates();
		// if (primLok.getLatitude() != null) {
		// coordinates.setLatitude(primLok.getLatitude().floatValue());
		// }
		// if (primLok.getLongitude() != null) {
		// coordinates.setLongitude(primLok.getLongitude().floatValue());
		// }
		// locationPrimaryInformation.setGeographicCoordinates(coordinates);
		//
		// locationPrimaryInformation.setPassengerFlag("1".equals(primLok.getPassengerPossibleFlag()));
		//
		// if (primLok.getPassengerStartValidity() != null || primLok.getPassengerEndValidity() != null) {
		// ValidityPeriod passengerValidityPeriod = new ValidityPeriod();
		// passengerValidityPeriod.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(primLok
		// .getPassengerStartValidity()));
		// passengerValidityPeriod.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(primLok
		// .getPassengerEndValidity()));
		// locationPrimaryInformation.setPassengerValidityPeriod(passengerValidityPeriod);
		// }
		//
		// ValidityPeriod locationValidityPeriod = new ValidityPeriod();
		// if (messageStatus.equals(_CudConsts.EXPORT_CRD_MESSAGE_STATUS_DELETE)) {
		// locationValidityPeriod.setStartDateTime(CudVysielanieUtils
		// .convertToXMLGregorian(predchPlatnaLokalita
		// .getStartValidity()));
		// locationValidityPeriod.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(predchPlatnaLokalita
		// .getPlatnostDo()));
		// } else {
		// locationValidityPeriod.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(primLok
		// .getPlatnostOd()));
		// locationValidityPeriod.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(primLok
		// .getPlatnostDo()));
		// }
		// locationPrimaryInformation.setLocationValidityPeriod(locationValidityPeriod);
		// mess.setLocationPrimaryInformation(locationPrimaryInformation);
		//
		// String xmlReceiptConfirmationMessage = vratSpravuXML(auth, mess);
		//
		// DTOExportnySubor exportnySubor = new DTOExportnySubor();
		// exportnySubor.setSubor(xmlReceiptConfirmationMessage);
		// exportnySubor.setIdCiselnik(cudCiselnik.getCiselnikID());
		// exportnySubor.setNazovSuboru(generujNazovSuboru(cudCiselnik.getNazov(), datumACasNacitaniaDat));
		// exportnySubor.setPoradoveCislo(1);
		// exportnySubor.setSpravaUuid(guid);
		// exportnySubor.setCasVytvorenia(datumACasNacitaniaDat);
		//
		// vytvorZaznamOOdoslaniSuboru(auth, exportnySubor, guid);
		// }
		//
		// } catch (Throwable t) {
		// odosliChybovuSpravu("CHYBA pri EXPORT CudCrdProcess LOCATION_PRIMARY");
		// }
	}

	public void vytvorenieSuboruCrdLocationSubsidiary(AuthInfo auth, DTOCiselnik cudCiselnik) {
		try {
			DTOTCountry idCountrySK = dlgCrd.getTCudCiselnikyClass().getCountryByIso(auth, "SK");

			List<DTOTSubsidiaryLocation> subsidiaryListAktualne = dlgCrd.getTCudCiselnikyClass()
					.vratAktualnePlatneZaznamyPreExportCRDSubsidiary(auth, odberatelObjekt.getCasPoslExportu(),
							datumACasNacitaniaDat, idCountrySK.getCountryID());

			List<DTOTSubsidiaryLocation> subsidiaryListBuduce = dlgCrd.getTCudCiselnikyClass()
					.vratVBuducnostiPlatneZaznamyPreExportCRDSubsidiary(auth, odberatelObjekt.getCasPoslExportu(),
							datumACasNacitaniaDat, idCountrySK.getCountryID());

			List<DTOTSubsidiaryLocation> subsidiaryList = new ArrayList<DTOTSubsidiaryLocation>(subsidiaryListAktualne);
			subsidiaryList.addAll(subsidiaryListBuduce);

			for (DTOTSubsidiaryLocation subsLok : subsidiaryList) {
				DTOTPrimaryLocation nadradenaLokalita = dlgCrd.getTCudCiselnikyClass().vratNadradenuLokalituVDatume(
						auth, subsLok.getIDPrimaryLocation(), idCountrySK.getCountryID(), subsLok.getPlatnostOd());

				DTOTSubsidiaryLocation predchPlatnaLokalita = dlgCrd.getTCudCiselnikyClass()
						.vratPoslednyStarsiZaznamPreDanuLokalituSubsidiary(auth, subsLok.getSubsidiaryLocationCode(),
								idCountrySK, odberatelObjekt.getCasPoslExportu());

				String messageStatus = null;
				if (predchPlatnaLokalita == null || "T".equals(predchPlatnaLokalita.getZmaz())) {
					messageStatus = _CudConsts.EXPORT_CRD_MESSAGE_STATUS_NEW;
				} else {
					if ("F".equals(predchPlatnaLokalita.getZmaz())) {
						messageStatus = _CudConsts.EXPORT_CRD_MESSAGE_STATUS_UPDATE;
					} else {
						messageStatus = _CudConsts.EXPORT_CRD_MESSAGE_STATUS_DELETE;
					}
				}

				LocationFileDatasetMessage mess = new LocationFileDatasetMessage();
				if (nadradenaLokalita.getLocationCode().length() > 5) { // iba prvych 5 cislic
					mess.setLocationPrimaryCode(Integer.parseInt(nadradenaLokalita.getLocationCode().substring(0, 5)));
				} else {
					mess.setLocationPrimaryCode(Integer.parseInt(nadradenaLokalita.getLocationCode()));
				}
				mess.setMessageStatus(messageStatus);

				UUID guid = UUID.randomUUID();
				MessageHeader messageHeader = vytvorMessageHeader(guid);
				mess.setMessageHeader(messageHeader);

				CountryCodeISO countryCodeISO = new CountryCodeISO();
				countryCodeISO.setValue("SK");
				mess.setCountryCodeISO(countryCodeISO);

				LocationSubsidiaryInformation locationSubsidiaryInformation = new LocationSubsidiaryInformation();
				locationSubsidiaryInformation.setLocationSubsidiaryName(subsLok.getSubsidiaryLocationName());
				LocationSubsidiaryCode subsidiaryCode = new LocationSubsidiaryCode();
				String subType = dlgCrd.getTCudCiselnikyClass().getSubsidiaryType(auth,
						subsLok.getIDSubsidiaryType());
				if (subType != null) {
					subsidiaryCode.setLocationSubsidiaryTypeCode("" + Integer.valueOf(subType));
				}
				subsidiaryCode.setValue(subsLok.getSubsidiaryLocationCode());
				locationSubsidiaryInformation.setLocationSubsidiaryCode(subsidiaryCode);

				ValidityPeriod locationValidityPeriod = new ValidityPeriod();
				if (messageStatus.equals(_CudConsts.EXPORT_CRD_MESSAGE_STATUS_DELETE)) {
					locationValidityPeriod.setStartDateTime(CudVysielanieUtils
							.convertToXMLGregorian(predchPlatnaLokalita
							.getStartValidity()));
					locationValidityPeriod.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(predchPlatnaLokalita
							.getPlatnostDo()));
				} else {
					locationValidityPeriod.setStartDateTime(CudVysielanieUtils.convertToXMLGregorian(subsLok
							.getPlatnostOd()));
					locationValidityPeriod.setEndDateTime(CudVysielanieUtils.convertToXMLGregorian(subsLok
							.getPlatnostDo()));
				}
				locationSubsidiaryInformation.setValidityPeriod(locationValidityPeriod);
				mess.setLocationSubsidiaryInformation(locationSubsidiaryInformation);

				String xmlReceiptConfirmationMessage = vratSpravuXML(auth, mess);

				DTOExportnySubor exportnySubor = new DTOExportnySubor();
				exportnySubor.setSubor(xmlReceiptConfirmationMessage);
				exportnySubor.setIdCiselnik(cudCiselnik.getCiselnikID());
				exportnySubor.setNazovSuboru(generujNazovSuboru(cudCiselnik.getNazov(), datumACasNacitaniaDat));
				exportnySubor.setPoradoveCislo(1);
				exportnySubor.setSpravaUuid(guid);
				exportnySubor.setCasVytvorenia(datumACasNacitaniaDat);

				vytvorZaznamOOdoslaniSuboru(auth, exportnySubor, guid);
			}
		} catch (Throwable t) {
			odosliChybovuSpravu("CHYBA pri EXPORT CudCrdProcess LOCATION_SUBSIDIARY");
		}
	}

	private void odosliChybovuSpravu(String error) {
		String mailingList = FrameworkUtils.getConfigProperty("cud", "cud.hlp.crd.mail");
		NotifUtils.sendNotif("", mailingList, "Pri príprave súborov pre export CRD došlo k chybe: ", error);
	}

	public String vratSpravuXML(AuthInfo auth, LocationFileDatasetMessage mess) throws AppException {

		String sprava = null;

		try {
			JAXBContext context = JAXBContext.newInstance(LocationFileDatasetMessage.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// poziadavka na pridanie utf
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

			StringWriter sw = new StringWriter();
			m.marshal(mess, sw);
			// sprava = "<![CDATA[" + sw.toString() + "]]>";
			sprava = sw.toString();
		} catch (Throwable t) {
			handleException(t, "vratSpravuXML.error", auth);
		}

		return sprava;
	}

	private void vytvorZaznamOOdoslaniSuboru(AuthInfo auth, DTOExportnySubor exportnySubor, UUID guid)
			throws AppException {
		Date datumACasVytvorenia = new Date();

		// Systém vytvorí záznam v údajoch o odoslaní súboru
		DTOSend dtoSend = new DTOSend();
		dtoSend.setIDOdberatelObjekt(odberatelObjekt.getOdberatelObjektID());
		dtoSend.setCasVytvorenia(datumACasVytvorenia);
		dtoSend.setSpravaUuid(guid.toString());
		dtoSend.setIdTransakciaZapisane(auth.getTransakciaID());
		ActionResult actionResult = dlgCrd.getCudSendClass().update(auth, dtoSend);
		DTOSend dtoResult = (DTOSend) actionResult.getResult();
		Integer sendID = dtoResult.getSendID();

		// Systém vytvorí záznam s pripraveným súborom
		DTOSendSubor dtoSendSubor = new DTOSendSubor();
		dtoSendSubor.setIDSend(sendID);
		dtoSendSubor.setIDCiselnik(exportnySubor.getIdCiselnik());
		dtoSendSubor.setNazovSuboru(exportnySubor.getNazovSuboru());
		dtoSendSubor.setSubor(exportnySubor.getSubor());
		dtoSendSubor.setPoradoveCislo(exportnySubor.getPoradoveCislo());
		dtoSendSubor.setPocetPokusov(1);
		dtoSendSubor.setCasVytvorenia(exportnySubor.getCasVytvorenia());
		dlgCrd.getCudSendSuborClass().update(auth, dtoSendSubor);
	}

	private MessageHeader vytvorMessageHeader(UUID guid) throws AppException {
		MessageHeader messageHeader = new MessageHeader();
		Sender sender = new Sender();
		sender.setCIInstanceNumber(1);
		sender.setValue(companyCode);
		messageHeader.setSender(sender);

		Recipient recipient = new Recipient();
		recipient.setCIInstanceNumber(1);
		recipient.setValue("3178");
		messageHeader.setRecipient(recipient);

		MessageReference messageReference = new MessageReference();
		messageReference.setMessageDateTime(CudVysielanieUtils.convertToXMLGregorian(new Date()));
		messageReference.setMessageType(messageType);
		messageReference.setMessageTypeVersion(version);
		messageReference.setMessageIdentifier(guid.toString());
		messageHeader.setMessageReference(messageReference);

		return messageHeader;
	}

}
