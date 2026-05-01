package sk.ditec.crd;

import static sk.ditec.zsr.common.server.utils.DateUtils.formatDateDDMMYYYYHHmm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.torque.util.BasePeer;
import org.apache.torque.util.Criteria;
import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.crd.dto.DTOTCompany;
import sk.ditec.crd.dto.DTOTCountry;
import sk.ditec.crd.dto.DTOTDopravnyBod;
import sk.ditec.crd.dto.DTOTPrimaryLocation;
import sk.ditec.crd.dto.DTOTStanicnaKolaj;
import sk.ditec.crd.dto.DTOTSubsidiaryLocation;
import sk.ditec.crd.dto.DTOTSubsidiaryType;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOOdberatelObjekt;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.master.TCompanyPeer;
import sk.ditec.dao.master.TCountryPeer;
import sk.ditec.dao.master.TDopravnyBodPeer;
import sk.ditec.dao.master.TPrimaryLocationPeer;
import sk.ditec.dao.master.TStanicnaKolajPeer;
import sk.ditec.dao.master.TSubsidiaryLocationPeer;
import sk.ditec.dao.master.TSubsidiaryTypePeer;
import sk.ditec.dao.master.TVyssiUzemnyCelokPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudOdberatelObjektPeer;
import sk.ditec.dao.meta.CudZmenaStlpecPeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.DataSetException;
import com.workingdogs.village.Record;

public class TCudCiselnikyClass extends _NovyPISBaseClass {

	public DTOTCountry getCountryByIso(AuthInfo auth, String countryKodeIso) throws AppException {

		try {
			// Systém vyhľadá záznam v T_COUNTRY, kde
			// .COUNTRY_CODE_ISO = vst. CountryCodeISO a zároveň
			// .ZMAZ = False a zároveň
			// .PLATNOST_DO is nul

			MyCriteria2 crit = new MyCriteria2(TCountryPeer.HIST_ID);
			TCountryPeer.addSelectColumns(crit);
			crit.addConditional(TCountryPeer.COUNTRY_CODE_ISO, countryKodeIso);
			crit.addConditional(TCountryPeer.ZMAZ, "F");
			// crit.getNewCriterion(TCountryPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			crit.addCustomSql(TCountryPeer.PLATNOST_DO, TCountryPeer.PLATNOST_DO + " is null ");
			String sql = crit.getSQL() + " ORDER BY " + TCountryPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				DTOTCountry dto = new DTOTCountry();
				Record r = (Record) iter.next();
				dto.setHistID(rVal(r, TCountryPeer.HIST_ID).asIntegerObj());
				dto.setPlatnostOd(rVal(r, TCountryPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, TCountryPeer.PLATNOST_DO).asUtilDate());
				dto.setCasVytvorenia(rVal(r, TCountryPeer.CAS_VYTVORENIA).asUtilDate());
				dto.setCasZmeny(rVal(r, TCountryPeer.CAS_ZMENY).asUtilDate());
				dto.setIDZmena(rVal(r, TCountryPeer.ID_ZMENA).asIntegerObj());
				dto.setZmaz(rVal(r, TCountryPeer.ZMAZ).asString());
				dto.setCountryID(rVal(r, TCountryPeer.COUNTRY_ID).asIntegerObj());
				dto.setCountryCodeIso(rVal(r, TCountryPeer.COUNTRY_CODE_ISO).asString());
				dto.setCountryUicCode(rVal(r, TCountryPeer.COUNTRY_UIC_CODE).asString());
				dto.setCountryNameEn(rVal(r, TCountryPeer.COUNTRY_NAME_EN).asString());
				dto.setCountryNameFr(rVal(r, TCountryPeer.COUNTRY_NAME_FR).asString());
				dto.setCountryNameDe(rVal(r, TCountryPeer.COUNTRY_NAME_DE).asString());
				dto.setSubLocCodeFlag(rVal(r, TCountryPeer.SUB_LOC_CODE_FLAG).asString());
				return dto;
			}

			return null;
		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getCountry.error", auth);
			return null;
		}
	}

	public ArrayList<DTOTCountry> getCountryList(AuthInfo auth, Date datum) throws AppException {
		try {
			// Systém vyhľadá záznam v T_COUNTRY, kde
			// .COUNTRY_CODE_ISO = vst. CountryCodeISO a zároveň
			// .ZMAZ = False a zároveň
			MyCriteria2 crit = new MyCriteria2(TCountryPeer.HIST_ID);
			TCountryPeer.addSelectColumns(crit);
			crit.addConditional(TCountryPeer.ZMAZ, "F");
			if (datum != null) {
				// where trunc(platnost_od) <= trunc(systimestamp) and (platnost_do is null or trunc(platnost_do) >=
				// trunc(systimestamp) ) ;
				String s = CudVysielanieUtils.getCritPlatneDatumOdDo(datum, TCountryPeer.PLATNOST_OD,
						TCountryPeer.PLATNOST_DO);
				crit.addCustomSql(TCountryPeer.PLATNOST_OD, s);
			} else {
				crit.addCustomSql(TCountryPeer.PLATNOST_DO, TCountryPeer.PLATNOST_DO + " is null ");
			}

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOTCountry> list = new ArrayList<DTOTCountry>();
			while (iter.hasNext()) {
				DTOTCountry dto = new DTOTCountry();
				Record r = (Record) iter.next();
				dto.setHistID(rVal(r, TCountryPeer.HIST_ID).asIntegerObj());
				dto.setPlatnostOd(rVal(r, TCountryPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, TCountryPeer.PLATNOST_DO).asUtilDate());
				dto.setCasVytvorenia(rVal(r, TCountryPeer.CAS_VYTVORENIA).asUtilDate());
				dto.setCasZmeny(rVal(r, TCountryPeer.CAS_ZMENY).asUtilDate());
				dto.setIDZmena(rVal(r, TCountryPeer.ID_ZMENA).asIntegerObj());
				dto.setZmaz(rVal(r, TCountryPeer.ZMAZ).asString());
				dto.setCountryID(rVal(r, TCountryPeer.COUNTRY_ID).asIntegerObj());
				dto.setCountryCodeIso(rVal(r, TCountryPeer.COUNTRY_CODE_ISO).asString());
				dto.setCountryUicCode(rVal(r, TCountryPeer.COUNTRY_UIC_CODE).asString());
				dto.setCountryNameEn(rVal(r, TCountryPeer.COUNTRY_NAME_EN).asString());
				dto.setCountryNameFr(rVal(r, TCountryPeer.COUNTRY_NAME_FR).asString());
				dto.setCountryNameDe(rVal(r, TCountryPeer.COUNTRY_NAME_DE).asString());
				dto.setSubLocCodeFlag(rVal(r, TCountryPeer.SUB_LOC_CODE_FLAG).asString());
				list.add(dto);
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getCountryList.error", auth);
			return null;
		}
	}

	public DTOTStanicnaKolaj getStanicnaKolajList(AuthInfo auth, Integer kolajId, Integer zmenaId) throws AppException {
		try {
			/* $dataRow - dáta z tabu¾ky */
			// Systém naèíta z T_STANICNA_KOLAJ záznam kde STANICNA_KOLAJ_ID=Zmena.ROW_ID a zároveò
			// ID_ZMENA=Zmena.ZMENA_ID

			MyCriteria2 crit = new MyCriteria2(TStanicnaKolajPeer.HIST_ID);
			TStanicnaKolajPeer.addSelectColumns(crit);
			// crit.addConditional(TStanicnaKolajPeer.ZMAZ, "F");
			crit.addConditional(TStanicnaKolajPeer.ID_ZMENA, zmenaId);
			crit.addConditional(TStanicnaKolajPeer.STANICNA_KOLAJ_ID, kolajId);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				DTOTStanicnaKolaj dto = new DTOTStanicnaKolaj();
				Record r = (Record) iter.next();

				dto.setHistID(rVal(r, TStanicnaKolajPeer.HIST_ID).asIntegerObj());
				dto.setPlatnostOd(rVal(r, TStanicnaKolajPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, TStanicnaKolajPeer.PLATNOST_DO).asUtilDate());
				dto.setCasVytvorenia(rVal(r, TStanicnaKolajPeer.CAS_VYTVORENIA).asUtilDate());
				dto.setCasZmeny(rVal(r, TStanicnaKolajPeer.CAS_ZMENY).asUtilDate());
				dto.setIDZmena(rVal(r, TStanicnaKolajPeer.ID_ZMENA).asIntegerObj());
				dto.setZmaz(rVal(r, TStanicnaKolajPeer.ZMAZ).asString());
				dto.setStanicnaKolajID(rVal(r, TStanicnaKolajPeer.STANICNA_KOLAJ_ID).asIntegerObj());
				dto.setIDDopravnyBod(rVal(r, TStanicnaKolajPeer.ID_DOPRAVNY_BOD).asIntegerObj());
				dto.setCislo(rVal(r, TStanicnaKolajPeer.CISLO).asString());
				dto.setIDUrcenieKolaje(rVal(r, TStanicnaKolajPeer.ID_URCENIE_KOLAJE).asIntegerObj());
				dto.setIDDruhKolaje(rVal(r, TStanicnaKolajPeer.ID_DRUH_KOLAJE).asIntegerObj());
				dto.setUzitocnaDlzka(rVal(r, TStanicnaKolajPeer.UZITOCNA_DLZKA).asIntegerObj());
				dto.setStavebnaDlzka(rVal(r, TStanicnaKolajPeer.STAVEBNA_DLZKA).asIntegerObj());
				dto.setIDElektrickaTrakcia(rVal(r, TStanicnaKolajPeer.ID_ELEKTRICKA_TRAKCIA).asIntegerObj());
				dto.setDlzkaNastupista(rVal(r, TStanicnaKolajPeer.DLZKA_NASTUPISTA).asIntegerObj());
				dto.setPoznamka(rVal(r, TStanicnaKolajPeer.POZNAMKA).asString());
				dto.setNeprevadzkovana(rVal(r, TStanicnaKolajPeer.NEPREVADZKOVANA).asString());
				dto.setVlecka(rVal(r, TStanicnaKolajPeer.VLECKA).asString());
				dto.setCisloNastupista(rVal(r, TStanicnaKolajPeer.CISLO_NASTUPISTA).asString());
				dto.setPoradieOdDk(rVal(r, TStanicnaKolajPeer.PORADIE_OD_DK).asString());
				dto.setSok(rVal(r, TStanicnaKolajPeer.SOK).asString());
				dto.setCrdZac(rVal(r, TStanicnaKolajPeer.CRD_ZAC).asUtilDate());
				dto.setCrdKon(rVal(r, TStanicnaKolajPeer.CRD_KON).asUtilDate());
				dto.setNazov(rVal(r, TStanicnaKolajPeer.NAZOV).asString());
				return dto;
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getStanicnaKolaj.error", auth);
			return null;
		}
	}

	public DTOTCompany getCompany(AuthInfo auth, String kod) throws AppException {
		return getCompany(auth, kod, null);
	}

	public DTOTCompany getCompany(AuthInfo auth, String kod, Date startValidity) throws AppException {
		try {
//			Systém vyhľadá záznam v T_COMPANY, kde
//			.COMPANY_UIC_CODE  = vst. ComapnyUICCode  a zároveň
//			.START_VALIDITY= vst. StartValidity
//			.ZMAZ = False   a zároveň 
//			.PLATNOST_DO is null


			MyCriteria2 crit = new MyCriteria2(TCompanyPeer.TABLE_NAME);

			TCompanyPeer.addSelectColumns(crit);
			crit.addConditional(TCompanyPeer.COMPANY_UIC_CODE, kod);
			crit.addConditional(TCompanyPeer.ZMAZ, "F");
			// crit.getNewCriterion(TCompanyPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			crit.addCustomSql(TCompanyPeer.PLATNOST_DO, TCompanyPeer.PLATNOST_DO + " is null ");
			crit.getNewCriterion(TCompanyPeer.START_VALIDITY, startValidity, MyCriteria2.EQUAL);
			// crit.addDescendingOrderByColumn(TCompanyPeer.HIST_ID);
			// ma existovat len jediny zaznam, order je zbytocny
			String sql = crit.getSQL() + " ORDER BY " + TCompanyPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				return getDTOTCompany(r);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getCompany.error", auth);
			return null;
		}

	}

	public ArrayList<DTOTCompany> getCompanyList(AuthInfo auth, String kod) throws AppException {
		try {
			// Systém vyhľadá záznam v T_COMPANY, kde
			// .COMPANY_UIC_CODE = vst. ComapnyUICCode a zároveň
			// .START_VALIDITY= vst. StartValidity
			// .ZMAZ = False a zároveň
			// .PLATNOST_DO is null

			MyCriteria2 crit = new MyCriteria2(TCompanyPeer.TABLE_NAME);

			TCompanyPeer.addSelectColumns(crit);
			crit.addConditional(TCompanyPeer.COMPANY_UIC_CODE, kod);
			crit.addConditional(TCompanyPeer.ZMAZ, "F");
			crit.addCustomSql(TCompanyPeer.PLATNOST_DO, TCompanyPeer.PLATNOST_DO + " is null ");
			// crit.getNewCriterion(TCompanyPeer.START_VALIDITY, startValidity, MyCriteria2.EQUAL);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOTCompany> list = new ArrayList<DTOTCompany>();
			while (iter.hasNext()) {

				Record r = (Record) iter.next();

				list.add(getDTOTCompany(r));
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getCompanyList.error", auth);
			return null;
		}

	}

	private DTOTCompany getDTOTCompany(Record r) throws DataSetException {
		DTOTCompany dto = new DTOTCompany();
		dto.setHistID(rVal(r, TCompanyPeer.HIST_ID).asIntegerObj());
		dto.setPlatnostOd(rVal(r, TCompanyPeer.PLATNOST_OD).asUtilDate());
		dto.setPlatnostDo(rVal(r, TCompanyPeer.PLATNOST_DO).asUtilDate());
		dto.setCasVytvorenia(rVal(r, TCompanyPeer.CAS_VYTVORENIA).asUtilDate());
		dto.setCasZmeny(rVal(r, TCompanyPeer.CAS_ZMENY).asUtilDate());
		dto.setIDZmena(rVal(r, TCompanyPeer.ID_ZMENA).asIntegerObj());
		dto.setZmaz(rVal(r, TCompanyPeer.ZMAZ).asString());
		dto.setCompanyID(rVal(r, TCompanyPeer.COMPANY_ID).asIntegerObj());
		dto.setIDCountry(rVal(r, TCompanyPeer.ID_COUNTRY).asIntegerObj());
		dto.setCompanyName(rVal(r, TCompanyPeer.COMPANY_NAME).asString());
		dto.setCompanyNameAscii(rVal(r, TCompanyPeer.COMPANY_NAME_ASCII).asString());
		dto.setCompanyUicCode(rVal(r, TCompanyPeer.COMPANY_UIC_CODE).asString());
		dto.setCompanyUrl(rVal(r, TCompanyPeer.COMPANY_URL).asString());
		dto.setStartValidity(rVal(r, TCompanyPeer.START_VALIDITY).asUtilDate());
		dto.setEndValidity(rVal(r, TCompanyPeer.END_VALIDITY).asUtilDate());
		dto.setCompanyShortName(rVal(r, TCompanyPeer.COMPANY_SHORT_NAME).asString());
		dto.setFreeText(rVal(r, TCompanyPeer.FREE_TEXT).asString());
		dto.setFreightFlag(rVal(r, TCompanyPeer.FREIGHT_FLAG).asString());
		dto.setPassengerFlag(rVal(r, TCompanyPeer.PASSENGER_FLAG).asString());
		dto.setInfrastructureFlag(rVal(r, TCompanyPeer.INFRASTRUCTURE_FLAG).asString());
		dto.setOtherCompanyFlag(rVal(r, TCompanyPeer.OTHER_COMPANY_FLAG).asString());
		dto.setNeEntityFlag(rVal(r, TCompanyPeer.NE_ENTITY_FLAG).asString());
		dto.setCeEntityFlag(rVal(r, TCompanyPeer.CE_ENTITY_FLAG).asString());
		dto.setContactPerson(rVal(r, TCompanyPeer.CONTACT_PERSON).asString());
		dto.setEmail(rVal(r, TCompanyPeer.EMAIL).asString());
		dto.setPhoneNumber(rVal(r, TCompanyPeer.PHONE_NUMBER).asString());
		dto.setFaxNumber(rVal(r, TCompanyPeer.FAX_NUMBER).asString());
		dto.setAddress(rVal(r, TCompanyPeer.ADDRESS).asString());
		dto.setCity(rVal(r, TCompanyPeer.CITY).asString());
		dto.setMobileNumber(rVal(r, TCompanyPeer.MOBILE_NUMBER).asString());
		dto.setPostalCode(rVal(r, TCompanyPeer.POSTAL_CODE).asString());
		dto.setActiveFlag(rVal(r, TPrimaryLocationPeer.ACTIVE_FLAG).asString());
		return dto;
	}

	public String getCompanyUicKodById(AuthInfo auth, Integer companyId) throws AppException {
		try {
			// Systém nájde COMPANY_UIC_CODE pre poslednú verziu záznamu T_COMPANY.COMPANY_ID=$Company
			// .ZMAZ = False a zároveň
			// .HIS_ID je najväčie (posledný záznam)
			MyCriteria2 crit = new MyCriteria2(TCompanyPeer.TABLE_NAME);

			crit.addSelectColumn(TCompanyPeer.COMPANY_UIC_CODE);
			crit.addConditional(TCompanyPeer.COMPANY_ID, companyId);
			crit.addConditional(TCompanyPeer.ZMAZ, "F");

			String sql = crit.getSQL() + " ORDER BY " + TCompanyPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, TCompanyPeer.COMPANY_UIC_CODE).asString();

			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getCompanyUicKodId.error", auth);
			return null;
		}

	}

	public List<DTOTPrimaryLocation> vratAktualnePlatneZaznamyPreExportCRDPrimary(AuthInfo auth, Date casPoslExportu,
			Date datumACasNacitaniaDat, DTOTCountry countrySK) throws AppException {
		try {
			StringBuilder selectFrom = new StringBuilder("SELECT ");
			selectFrom.append("t1.HIST_ID,\n");
			selectFrom.append("t1.PLATNOST_OD,\n");
			selectFrom.append("t1.PLATNOST_DO,\n");
			selectFrom.append("t1.CAS_VYTVORENIA,\n");
			selectFrom.append("t1.CAS_ZMENY,\n");
			selectFrom.append("t1.ID_ZMENA,\n");
			selectFrom.append("t1.ZMAZ,\n");
			selectFrom.append("t1.PRIMARY_LOCATION_ID,\n");
			selectFrom.append("t1.ID_COUNTRY,\n");
			selectFrom.append("t1.LOCATION_CODE,\n");
			selectFrom.append("t1.START_VALIDITY,\n");
			selectFrom.append("t1.END_VALIDITY,\n");
			selectFrom.append("t1.ID_COMPANY,\n");
			selectFrom.append("t1.LOCATION_NAME,\n");
			selectFrom.append("t1.LOCATION_NAME_ASCII,\n");
			selectFrom.append("t1.NUTS_CODE,\n");
			selectFrom.append("t1.CONTAINER_HANDLING_FLAG,\n");
			selectFrom.append("t1.HANDOVER_POINT_FLAG,\n");
			selectFrom.append("t1.FREIGHT_POSSIBLE_FLAG,\n");
			selectFrom.append("t1.FREIGHT_START_VALIDITY,\n");
			selectFrom.append("t1.FREIGHT_END_VALIDITY,\n");
			selectFrom.append("t1.PASSENGER_POSSIBLE_FLAG,\n");
			selectFrom.append("t1.PASSENGER_START_VALIDITY,\n");
			selectFrom.append("t1.PASSENGER_END_VALIDITY,\n");
			selectFrom.append("t1.LONGITUDE,\n");
			selectFrom.append("t1.LATITUDE,\n");
			selectFrom.append("t1.FREE_TEXT\n");
			selectFrom.append("FROM ").append(TPrimaryLocationPeer.TABLE_NAME).append(" t1 \n");

			// crit.add(TPrimaryLocationPeer.PLATNOST_OD, (Object) formatDateDDMMYYYYHHmm(datumACasNacitaniaDat),
			// MyCriteria2.LESS_EQUAL);
			//
			// Criteria.Criterion c1 = crit.getNewCriterion(TPrimaryLocationPeer.PLATNOST_DO, (Object) null,
			// MyCriteria2.ISNULL);
			// Criteria.Criterion c2 = crit.getNewCriterion(TPrimaryLocationPeer.PLATNOST_DO,
			// formatDateDDMMYYYYHHmm(datumACasNacitaniaDat), MyCriteria2.GREATER_EQUAL);
			// crit.add(c1.or(c2));
			//
			// if (casPoslExportu == null) {
			// Criteria.Criterion c4 = crit.getNewCriterion(TPrimaryLocationPeer.CAS_ZMENY,
			// formatDateDDMMYYYYHHmm(datumACasNacitaniaDat), MyCriteria2.LESS_EQUAL);
			// Criteria.Criterion c6 = crit.getNewCriterion(TPrimaryLocationPeer.CAS_VYTVORENIA,
			// formatDateDDMMYYYYHHmm(datumACasNacitaniaDat), MyCriteria2.LESS_EQUAL);
			// crit.add(c4.or(c6));
			// } else {
			// Criteria.Criterion c3 = crit.getNewCriterion(TPrimaryLocationPeer.CAS_ZMENY,
			// formatDateDDMMYYYYHHmm(casPoslExportu), MyCriteria2.GREATER_THAN);
			// Criteria.Criterion c4 = crit.getNewCriterion(TPrimaryLocationPeer.CAS_ZMENY,
			// formatDateDDMMYYYYHHmm(datumACasNacitaniaDat), MyCriteria2.LESS_EQUAL);
			// Criteria.Criterion c34 = c3.and(c4);
			//
			// Criteria.Criterion c5 = crit.getNewCriterion(TPrimaryLocationPeer.CAS_VYTVORENIA,
			// formatDateDDMMYYYYHHmm(casPoslExportu), MyCriteria2.GREATER_THAN);
			// Criteria.Criterion c6 = crit.getNewCriterion(TPrimaryLocationPeer.CAS_VYTVORENIA,
			// formatDateDDMMYYYYHHmm(datumACasNacitaniaDat), MyCriteria2.LESS_EQUAL);
			// Criteria.Criterion c56 = c5.and(c6);
			//
			// crit.add(c34.or(c56));
			// }
			//
			// crit.add(TPrimaryLocationPeer.ID_COUNTRY, countrySK.getCountryID());
			//
			// Systém vyhľadá T_PRIMARY_LOCATION, kde:
			// PLATNOST_OD <= datumACasNacitaniaDat
			// A zároveň
			// (
			// PLATNOST_DO JE NULL
			// ALebo
			// PLATNOST_DO >= dátum(datumACasNacitaniaDat)
			// )
			// A zároveň
			// (
			// CAS_ZMENY > vstup:PoslednyExport a zároveň CAS_ZMENY<=datumACasNacitaniaDat
			// alebo
			// CAS_VYTVORENIA> vstup:PoslednyExport a zároveň CAS_VYTVORENIA <=dátumAčasNačítaniaDát
			// )
			// A zároveň
			// ID_COUNTRY= vstup:CountryID
			// Systém vráti pre každý nájdeý LOCATION_CODE, iba jeden záznam, kde NVL(CAS_ZMENY,CAS_VYTVORENIA) je
			// maximálny.

			String subSelect = "JOIN ( SELECT DISTINCT t2.LOCATION_CODE, FIRST_VALUE(t2.HIST_ID) OVER ( \n"
					+ "PARTITION BY t2.LOCATION_CODE \n"
					+ "ORDER BY NVL(t2.CAS_ZMENY, t2.CAS_VYTVORENIA) DESC, t2.HIST_ID DESC \n) " + "AS HIST_ID FROM "
					+ TPrimaryLocationPeer.TABLE_NAME + " t2 \n";

			String casZmenyAVytvorenia;
			if (casPoslExportu == null) {
				casZmenyAVytvorenia = "(t2.CAS_ZMENY <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "' OR "
						+ "t2.CAS_VYTVORENIA <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "')\n";
			} else {
				casZmenyAVytvorenia = "((t2.CAS_ZMENY > '" + formatDateDDMMYYYYHHmm(casPoslExportu) + "' AND "
						+ "t2.CAS_ZMENY <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "') OR "
						+ "(t2.CAS_VYTVORENIA > '" + formatDateDDMMYYYYHHmm(casPoslExportu) + "' AND "
						+ "t2.CAS_VYTVORENIA <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "'))\n";
			}

			String subSelectWhere = "WHERE " + casZmenyAVytvorenia + "AND t2.ID_COUNTRY = " + countrySK.getCountryID()
					+ " \n" + "AND (t2.PLATNOST_DO IS NULL OR t2.PLATNOST_DO >= '"
					+ formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "')\n" + "AND t2.PLATNOST_OD <= '"
					+ formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "') lr ON t1.HIST_ID = lr.HIST_ID\n";

			String orderBy = " ORDER BY t1.LOCATION_CODE";
			String query = selectFrom + subSelect + subSelectWhere + orderBy;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(query, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			List<DTOTPrimaryLocation> result = new ArrayList<DTOTPrimaryLocation>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOTPrimaryLocation dto = getDtoPrimaryLoc(r);
				result.add(dto);
			}

			return result;
		} catch (Throwable t) {
			t.printStackTrace();
			handleException(t, "TCudCiselnikyClass.vratAktualnePlatneZaznamyPreExportCRD.error", auth);
			return null;
		}
	}

	public List<DTOTSubsidiaryLocation> vratAktualnePlatneZaznamyPreExportCRDSubsidiary(AuthInfo auth,
			Date casPoslExportu, Date datumACasNacitaniaDat, Integer countrySkId) throws AppException {
		try {
			MyCriteria2 crit = new MyCriteria2(TSubsidiaryLocationPeer.TABLE_NAME, new DTOTSubsidiaryLocation());
			TSubsidiaryLocationPeer.addSelectColumns(crit);

			String s = CudVysielanieUtils.getCritPlatneDatumOdDo(datumACasNacitaniaDat,
					TSubsidiaryLocationPeer.PLATNOST_OD, TSubsidiaryLocationPeer.PLATNOST_DO);
			crit.addCustomSql(TSubsidiaryLocationPeer.PLATNOST_OD, s);
			if (casPoslExportu == null) {
				Criteria.Criterion c4 = crit.getNewCriterion(TSubsidiaryLocationPeer.CAS_ZMENY, datumACasNacitaniaDat,
						MyCriteria2.LESS_EQUAL);
				Criteria.Criterion c6 = crit.getNewCriterion(TSubsidiaryLocationPeer.CAS_VYTVORENIA,
						datumACasNacitaniaDat, MyCriteria2.LESS_EQUAL);
				crit.add(c4.or(c6));
			} else {
				Criteria.Criterion c3 = crit.getNewCriterion(TSubsidiaryLocationPeer.CAS_ZMENY, casPoslExportu,
						MyCriteria2.GREATER_THAN);
				Criteria.Criterion c4 = crit.getNewCriterion(TSubsidiaryLocationPeer.CAS_ZMENY, datumACasNacitaniaDat,
						MyCriteria2.LESS_EQUAL);
				Criteria.Criterion c34 = c3.and(c4);

				Criteria.Criterion c5 = crit.getNewCriterion(TSubsidiaryLocationPeer.CAS_VYTVORENIA, casPoslExportu,
						MyCriteria2.GREATER_THAN);
				Criteria.Criterion c6 = crit.getNewCriterion(TSubsidiaryLocationPeer.CAS_VYTVORENIA,
						datumACasNacitaniaDat, MyCriteria2.LESS_EQUAL);
				Criteria.Criterion c56 = c5.and(c6);

				crit.add(c34.or(c56));
			}

			crit.add(TSubsidiaryLocationPeer.ID_COUNTRY, countrySkId);

			String sql = crit.getSQL();
			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			List<DTOTSubsidiaryLocation> result = new ArrayList<DTOTSubsidiaryLocation>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				DTOTSubsidiaryLocation dto = getDtoSubsidiaryLocation(r);
				result.add(dto);
			}

			return result;
		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.vratAktualnePlatneZaznamyPreExportCRD.error", auth);
			return null;
		}
	}

	public List<DTOTPrimaryLocation> vratVBuducnostiPlatneZaznamyPreExportCRDPrimary(AuthInfo auth,
			Date casPoslExportu, Date datumACasNacitaniaDat, DTOTCountry idCountrySK) throws AppException {
		try {
			Date datumACasNacitaniaDatDateOnly = DateUtils.truncate(datumACasNacitaniaDat, Calendar.DAY_OF_MONTH);

			StringBuilder selectFrom = new StringBuilder("SELECT ");
			selectFrom.append("t1.HIST_ID,\n");
			selectFrom.append("t1.PLATNOST_OD,\n");
			selectFrom.append("t1.PLATNOST_DO,\n");
			selectFrom.append("t1.CAS_VYTVORENIA,\n");
			selectFrom.append("t1.CAS_ZMENY,\n");
			selectFrom.append("t1.ID_ZMENA,\n");
			selectFrom.append("t1.ZMAZ,\n");
			selectFrom.append("t1.PRIMARY_LOCATION_ID,\n");
			selectFrom.append("t1.ID_COUNTRY,\n");
			selectFrom.append("t1.LOCATION_CODE,\n");
			selectFrom.append("t1.START_VALIDITY,\n");
			selectFrom.append("t1.END_VALIDITY,\n");
			selectFrom.append("t1.ID_COMPANY,\n");
			selectFrom.append("t1.LOCATION_NAME,\n");
			selectFrom.append("t1.LOCATION_NAME_ASCII,\n");
			selectFrom.append("t1.NUTS_CODE,\n");
			selectFrom.append("t1.CONTAINER_HANDLING_FLAG,\n");
			selectFrom.append("t1.HANDOVER_POINT_FLAG,\n");
			selectFrom.append("t1.FREIGHT_POSSIBLE_FLAG,\n");
			selectFrom.append("t1.FREIGHT_START_VALIDITY,\n");
			selectFrom.append("t1.FREIGHT_END_VALIDITY,\n");
			selectFrom.append("t1.PASSENGER_POSSIBLE_FLAG,\n");
			selectFrom.append("t1.PASSENGER_START_VALIDITY,\n");
			selectFrom.append("t1.PASSENGER_END_VALIDITY,\n");
			selectFrom.append("t1.LONGITUDE,\n");
			selectFrom.append("t1.LATITUDE,\n");
			selectFrom.append("t1.FREE_TEXT\n");
			selectFrom.append("FROM ").append(TPrimaryLocationPeer.TABLE_NAME).append(" t1 \n");

			String casZmenyAVytvorenia;
			if (casPoslExportu == null) {
				casZmenyAVytvorenia = "(t1.CAS_ZMENY <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "' OR "
						+ "t1.CAS_VYTVORENIA <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "')\n";
			} else {
				casZmenyAVytvorenia = "((t1.CAS_ZMENY > '" + formatDateDDMMYYYYHHmm(casPoslExportu) + "' AND "
						+ "t1.CAS_ZMENY <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "') OR "
						+ "(t1.CAS_VYTVORENIA > '" + formatDateDDMMYYYYHHmm(casPoslExportu) + "' AND "
						+ "t1.CAS_VYTVORENIA <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "'))\n";
			}

			String where = "WHERE " + casZmenyAVytvorenia + "AND t1.ID_COUNTRY = " + idCountrySK.getCountryID() + "\n"
					+ "AND t1.PLATNOST_OD <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "'\n";

			String subQuery = " AND NOT EXISTS ( SELECT t2.PRIMARY_LOCATION_ID\n" + "FROM "
					+ TPrimaryLocationPeer.TABLE_NAME + " t2\n" + "WHERE t2.ZMAZ = 'F'\n" + "AND t2.ID_COUNTRY = "
					+ idCountrySK.getCountryID() + "\n" + "AND (t2.PLATNOST_DO >= '"
					+ formatDateDDMMYYYYHHmm(datumACasNacitaniaDatDateOnly) + "' OR" + " t2.PLATNOST_DO IS NULL)\n"
					+ "AND t2.PLATNOST_OD <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDatDateOnly) + "'\n"
					+ "AND t2.LOCATION_CODE = t1.LOCATION_CODE)";

			String query = selectFrom + where + subQuery;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(query, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			List<DTOTPrimaryLocation> result = new ArrayList<DTOTPrimaryLocation>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOTPrimaryLocation dto = getDtoPrimaryLoc(r);
				result.add(dto);
			}

			return result;
		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.vratVBuducnostiPlatneZaznamyPreExportCRD.error", auth);
			return null;
		}
	}

	public List<DTOTSubsidiaryLocation> vratVBuducnostiPlatneZaznamyPreExportCRDSubsidiary(AuthInfo auth,
			Date casPoslExportu, Date datumACasNacitaniaDat, Integer idCountrySkId) throws AppException {
		try {
			Date datumACasNacitaniaDatDateOnly = DateUtils.truncate(datumACasNacitaniaDat, Calendar.DAY_OF_MONTH);

			StringBuilder selectFrom = new StringBuilder("SELECT ");
			selectFrom.append("t1.HIST_ID, \n");
			selectFrom.append("t1.PLATNOST_OD, \n");
			selectFrom.append("t1.PLATNOST_DO, \n");
			selectFrom.append("t1.CAS_VYTVORENIA, \n");
			selectFrom.append("t1.CAS_ZMENY, \n");
			selectFrom.append("t1.ID_ZMENA, \n");
			selectFrom.append("t1.ZMAZ, \n");
			selectFrom.append("t1.SUBSIDIARY_LOCATION_ID, \n");
			selectFrom.append("t1.ID_SUBSIDIARY_TYPE, \n");
			selectFrom.append("t1.ID_COMPANY, \n");
			selectFrom.append("t1.ID_COUNTRY, \n");
			selectFrom.append("t1.RESPONSIBLE_IM_CODE, \n");
			selectFrom.append("t1.SUBSIDIARY_LOCATION_CODE, \n");
			selectFrom.append("t1.SUBSIDIARY_LOCATION_NAME, \n");
			selectFrom.append("t1.ID_PRIMARY_LOCATION, \n");
			selectFrom.append("t1.START_VALIDITY, \n");
			selectFrom.append("t1.END_VALIDITY, \n");
			selectFrom.append("t1.LONGITUDE, \n");
			selectFrom.append("t1.LATITUDE, \n");
			selectFrom.append("t1.FREE_TEXT \n");
			selectFrom.append("FROM ").append(TSubsidiaryLocationPeer.TABLE_NAME).append(" t1 \n");

			String casZmenyAVytvorenia;
			if (casPoslExportu == null) {
				casZmenyAVytvorenia = "(t1.CAS_ZMENY <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "' OR "
						+ "t1.CAS_VYTVORENIA <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "')";
			} else {
				casZmenyAVytvorenia = "((t1.CAS_ZMENY > '" + formatDateDDMMYYYYHHmm(casPoslExportu) + "' AND "
						+ "t1.CAS_ZMENY <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "') OR "
						+ "(t1.CAS_VYTVORENIA > '" + formatDateDDMMYYYYHHmm(casPoslExportu) + "' AND "
						+ "t1.CAS_VYTVORENIA <= '" + formatDateDDMMYYYYHHmm(datumACasNacitaniaDat) + "'))";
			}

			StringBuilder where = new StringBuilder("WHERE ");
			where.append("t1.PLATNOST_OD <= '").append(formatDateDDMMYYYYHHmm(datumACasNacitaniaDatDateOnly))
					.append("' \nAND ");
			where.append(casZmenyAVytvorenia).append(" \nAND ");
			where.append("t1.ID_COUNTRY = ").append(idCountrySkId).append(" \nAND ");

			StringBuilder subQuery = new StringBuilder("NOT EXISTS( SELECT t2.SUBSIDIARY_LOCATION_ID \n");
			subQuery.append("FROM ").append(TSubsidiaryLocationPeer.TABLE_NAME).append(" t2 \n");
			subQuery.append("WHERE t1.SUBSIDIARY_LOCATION_CODE = t2.SUBSIDIARY_LOCATION_CODE \nAND ");
			subQuery.append("t2.ID_COUNTRY = ").append(idCountrySkId).append(" \nAND ");
			subQuery.append("t2.PLATNOST_OD <= '").append(formatDateDDMMYYYYHHmm(datumACasNacitaniaDatDateOnly))
					.append("' \nAND ");
			subQuery.append("t2.ZMAZ = 'F' \nAND ");
			subQuery.append("(t2.PLATNOST_DO >= '").append(formatDateDDMMYYYYHHmm(datumACasNacitaniaDatDateOnly))
					.append("' OR ");
			subQuery.append("t2.PLATNOST_DO IS NULL))");

			String query = selectFrom.toString() + where.toString() + subQuery.toString();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(query, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			List<DTOTSubsidiaryLocation> result = new ArrayList<DTOTSubsidiaryLocation>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOTSubsidiaryLocation dto = getDtoSubsidiaryLocation(r);
				result.add(dto);
			}

			return result;
		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.vratVBuducnostiPlatneZaznamyPreExportCRD.error", auth);
			return null;
		}
	}

	public DTOTPrimaryLocation vratPoslednyStarsiZaznamPreDanuLokalituPrimary(AuthInfo auth, String locationCode,
			DTOTCountry idCountrySK, Date casPoslednehoExportu) throws AppException {
		try {
			String tableName = TPrimaryLocationPeer.TABLE_NAME;
			MyCriteria2 selectFrom = new MyCriteria2(tableName, new DTOTPrimaryLocation());
			TPrimaryLocationPeer.addSelectColumns(selectFrom);

			StringBuilder where = new StringBuilder();
			where.append(" WHERE (").append(tableName).append(".PLATNOST_DO IS NULL OR ").append(tableName)
					.append(".PLATNOST_OD <= ").append(tableName).append(".PLATNOST_DO) \nAND ");
			if (casPoslednehoExportu != null) {
				where.append(tableName).append(".PLATNOST_OD < ").append(casPoslednehoExportu).append(" \nAND ");
			}

			where.append(tableName).append(".LOCATION_CODE = ").append(locationCode).append(" \nAND ");
			where.append(tableName).append(".ID_COUNTRY = ").append(idCountrySK.getCountryID()).append("\n");
			where.append("ORDER BY ").append(tableName).append(".PLATNOST_OD DESC");

			String query = selectFrom.getSQL() + where;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(query, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return getDtoPrimaryLoc(r);
			}

			return null;
		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.vratPoslednyStarsiZaznamPreDanuLokalitu.error", auth);
			return null;
		}
	}

	public DTOTSubsidiaryLocation vratPoslednyStarsiZaznamPreDanuLokalituSubsidiary(AuthInfo auth, String locationCode,
			DTOTCountry idCountrySK, Date casPoslednehoExportu) throws AppException {
		try {
			String tableName = TSubsidiaryLocationPeer.TABLE_NAME;
			MyCriteria2 selectFrom = new MyCriteria2(tableName, new DTOTSubsidiaryLocation());
			TSubsidiaryLocationPeer.addSelectColumns(selectFrom);

			StringBuilder where = new StringBuilder();
			where.append(" WHERE (").append(tableName).append(".PLATNOST_DO IS NULL OR ").append(tableName)
					.append(".PLATNOST_OD <= ").append(tableName).append(".PLATNOST_DO) \nAND ");
			if (casPoslednehoExportu != null) {
				where.append(tableName).append(".PLATNOST_OD < ").append(casPoslednehoExportu).append(" \nAND ");
			}

			where.append(tableName).append(".SUBSIDIARY_LOCATION_CODE = ").append(locationCode).append(" \nAND ");
			where.append(tableName).append(".ID_COUNTRY = ").append(idCountrySK.getCountryID()).append("\n");
			where.append("ORDER BY ").append(tableName).append(".PLATNOST_OD DESC");

			String query = selectFrom.getSQL() + where;

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(query, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return getDtoSubsidiaryLocation(r);
			}

			return null;
		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.vratPoslednyStarsiZaznamPreDanuLokalitu.error", auth);
			return null;
		}
	}

	public DTOTPrimaryLocation vratNadradenuLokalituVDatume(AuthInfo auth, Integer idPrimaryLocation,
			Integer countryID, Date kDatumu) throws AppException {
		try {
			MyCriteria2 crit = new MyCriteria2(TPrimaryLocationPeer.TABLE_NAME, new DTOTPrimaryLocation());
			TPrimaryLocationPeer.addSelectColumns(crit);

			crit.add(TPrimaryLocationPeer.PRIMARY_LOCATION_ID, idPrimaryLocation);
			crit.add(TPrimaryLocationPeer.PLATNOST_OD, kDatumu, MyCriteria2.LESS_EQUAL);
			Criteria.Criterion c1 = crit.getNewCriterion(TPrimaryLocationPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			Criteria.Criterion c2 = crit.getNewCriterion(TPrimaryLocationPeer.PLATNOST_DO, kDatumu,
					MyCriteria2.GREATER_EQUAL);
			crit.add(c1.or(c2));
			crit.add(TPrimaryLocationPeer.ID_COUNTRY, countryID);

			String sql = crit.getSQL();
			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return getDtoPrimaryLoc(r);
			}

			return null;
		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.vratNadradenuLokalituVDatume.error", auth);
			return null;
		}
	}

	// public DTOTSubsidiaryLocation getSubsidiaryLocation(AuthInfo auth, String kodSubLoc, Integer idPrimLoc,
	// Integer idSubType, Integer idCompany, Integer idCountry) throws AppException {
	// return getSubsidiaryLocation(auth, kodSubLoc, idCompany, idSubType, idCompany, idCountry, null);
	//
	// }

	public DTOTSubsidiaryLocation getSubsidiaryLocationBySubCode(AuthInfo auth, String kodSubLoc, Integer idPrimLoc,
			Integer idSubType, Integer idCompany, Integer idCountry, Date startValidity)

	throws AppException {
		try {

			// Systém vyhľadá záznam v T_SUBSIDIARY_LOCATION, kde
			// .SUBSIDIARY_LOCATION_CODE = vst. SubsidiaryLocationCode a zároveň
			// .ID_SUBSIDIARY_TYPE = vst. SubsidiaryTypeID a zároveň
			// .START_VALIDITY= vst. StartValidity
			// .ID_COMPANY.COMPANY_UIC_CODE = vst. companyUICCode a zároveň
			// .ID_COMPANY.PLATNOST_DO is null
			// .ID_COUNTRY.COUNTRY_CODE_ISO = vst. CountryCodeISO a zároveň
			// .ID_COUNTRY.PLATNOST_DO is null
			// .ID_PRIMARY_LOCATION.LOCATION_CODE = vst. PrimaryLocation.LOCATION_CODE a zároveň
			// .ID_PRIMARY_LOCATION.ID_COUNTRY = vst. PrimaryLocation.ID_COUNTRY
			// .ID_PRIMARY_LOCATION.PLATNOST_DO is null
			// .ZMAZ = False a zároveň
			// .PLATNOST_DO is null


			// crit.addConditional(TSubsidiaryLocationPeer.HIST_ID, dtoF.getHistID());
			MyCriteria2 crit = new MyCriteria2(TSubsidiaryLocationPeer.TABLE_NAME, new DTOTSubsidiaryLocation());
			TSubsidiaryLocationPeer.addSelectColumns(crit);
			crit.addConditional(TSubsidiaryLocationPeer.SUBSIDIARY_LOCATION_CODE, kodSubLoc);

			crit.addConditional(TSubsidiaryLocationPeer.ID_COMPANY, idCompany);
			crit.addConditional(TSubsidiaryLocationPeer.ID_COUNTRY, idCountry);
			crit.addConditional(TSubsidiaryLocationPeer.ID_PRIMARY_LOCATION, idPrimLoc);
			crit.addConditional(TSubsidiaryLocationPeer.ID_SUBSIDIARY_TYPE, idSubType);
			crit.addConditional(TSubsidiaryLocationPeer.ZMAZ, "F");
			crit.addConditional(TSubsidiaryLocationPeer.START_VALIDITY, startValidity, Criteria.EQUAL);
			// crit.addConditional(TSubsidiaryLocationPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			crit.addCustomSql(TSubsidiaryLocationPeer.PLATNOST_DO, TSubsidiaryLocationPeer.PLATNOST_DO + " is null ");
			// String sql = crit.getSQL() + " AND ( " + CudVysielanieUtils.timeCritFormat(datum) + "<="
			// + TSubsidiaryLocationPeer.PLATNOST_DO + " OR " + TSubsidiaryLocationPeer.PLATNOST_DO + " IS NULL)";
			String sql = crit.getSQL();
			sql += " ORDER BY " + TSubsidiaryLocationPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				return getDtoSubsidiaryLocation(r);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubsidiaryLocation(.error", auth);
			return null;
		}
	}

	public ArrayList<DTOTSubsidiaryLocation> getSubsidiaryLocationList(AuthInfo auth, String subLocCode,
			String primLocCode, Integer idSubType, Integer idCountry, String companyUicCode, Date vlozDatum)
			throws AppException {
		try {
			// /*$dataRowSubsidiaryList*/
			// Systém z aktuálnych záznamov vyfiltruje zaznamy
			// kde
			// ZMAZ='F'
			// SUBSIDIARY_LOCATION_CODE=$OldLocationCode
			// ID_SUBSIDIARY_TYPE=$OldSubsidiaryType
			//
			// ID_COMPANY.COMPANY_UIC_CODE = UICZSR
			// ID_COMPANY.PLATNOST_DO is null
			//
			// ID_COUNTRY=$IDCountrySK
			//
			// (START_VALIDITY<= $vlozDatum or START_VALIDITY>$vlozDatum ) and (END_VALIDTY is null or END_VALIDITY>=$vlozDatum)
			//
			// ID_PRIMARY_LOCATION.LOCATION_CODE=prvých 5 èislic z $OldNadradenyDb.CISLO
			// ID_PRIMARY_LOCATION.PLATNOST_DO is null
			//
			// ID_PRIMARY_LOCATION.ID_COMPANY.COMPANY_UIC_CODE =UICZSR
			// ID_PRIMARY_LOCATION.ID_COMPANY.PLATNOST_DO is null
			//
			// ID_PRIMARY_LOCATION.ID_COUNTRY=$IDCountrySK
			//
			// ID_PRIMARY_LOCATION.PLATNOST_DO is null
			// usporiadaj podla START_VALIDITY desc

			


			String sql = " select sl.* from T_SUBSIDIARY_LOCATION sl "
					+ " JOIN T_PRIMARY_LOCATION pl ON (pl.primary_location_id = sl.id_primary_location and pl.platnost_do"
					+ " is null and pl.location_code='"
					+ primLocCode
					+ "' )"
					+ "  JOIN t_country country ON (country.country_id = sl.id_country and "
					+ "country.country_id = "
					+ idCountry
					+ ")"
					+ "  JOIN t_company company ON (company.company_id = sl.id_company and company.platnost_do is null "
					+ " and company_uic_code='"
					+ companyUicCode
					+ "')  "
					+ " where sl.ZMAZ='F' "
					+ "  and sl.SUBSIDIARY_LOCATION_CODE= '"
					+ subLocCode
					+ "'"
					+ " and sl.id_subsidiary_type="
					+ idSubType
					// (START_VALIDITY<= $vlozDatum or START_VALIDITY>$vlozDatum ) and (END_VALIDTY is null or
					// END_VALIDITY>=$vlozDatum)
					+ " and ( sl.start_validity<="
					+ CudVysielanieUtils.dateCritFormat(vlozDatum)
					+ " or sl.START_VALIDITY >"
					+ CudVysielanieUtils.dateCritFormat(vlozDatum)
					+ " ) and ( sl.end_validity is null or sl.end_validity>="
					+ CudVysielanieUtils.dateCritFormat(vlozDatum) + ") order by sl.start_validity desc";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOTSubsidiaryLocation> list = new ArrayList<DTOTSubsidiaryLocation>();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				DTOTSubsidiaryLocation dto = getDtoSubsidiaryLocation(r);
				list.add(dto);
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubsidiaryLocationList.error", auth);
			return null;
		}
	}



	public DTOTSubsidiaryType getSubsidiaryType(AuthInfo auth, String kod) throws AppException {
		try {
			// Systém vyhľadá záznam v T_COUNTRY, kde
			// .COUNTRY_CODE_ISO = vst. CountryCodeISO a zároveň
			// .ZMAZ = False a zároveň
			// .HIS_ID je najväčie (posledný záznam)
			MyCriteria2 crit = new MyCriteria2(TSubsidiaryTypePeer.TABLE_NAME);

			TSubsidiaryTypePeer.addSelectColumns(crit);
			crit.addConditional(TSubsidiaryTypePeer.SUBSIDIARY_TYPE_CODE, kod);
			crit.addConditional(TSubsidiaryTypePeer.ZMAZ, "F");
			crit.getNewCriterion(TSubsidiaryTypePeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			// crit.addDescendingOrderByColumn(TSubsidiaryTypePeer.HIST_ID);

			String sql = crit.getSQL() + " ORDER BY " + TSubsidiaryTypePeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				DTOTSubsidiaryType dto = new DTOTSubsidiaryType();
				Record r = (Record) iter.next();

				dto.setHistID(rVal(r, TSubsidiaryTypePeer.HIST_ID).asIntegerObj());
				dto.setPlatnostOd(rVal(r, TSubsidiaryTypePeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, TSubsidiaryTypePeer.PLATNOST_DO).asUtilDate());
				dto.setCasVytvorenia(rVal(r, TSubsidiaryTypePeer.CAS_VYTVORENIA).asUtilDate());
				dto.setCasZmeny(rVal(r, TSubsidiaryTypePeer.CAS_ZMENY).asUtilDate());
				dto.setIDZmena(rVal(r, TSubsidiaryTypePeer.ID_ZMENA).asIntegerObj());
				dto.setZmaz(rVal(r, TSubsidiaryTypePeer.ZMAZ).asString());
				dto.setSubsidiaryTypeID(rVal(r, TSubsidiaryTypePeer.SUBSIDIARY_TYPE_ID).asIntegerObj());
				dto.setSubsidiaryTypeCode(rVal(r, TSubsidiaryTypePeer.SUBSIDIARY_TYPE_CODE).asString());
				dto.setSubsidiaryTypeName(rVal(r, TSubsidiaryTypePeer.SUBSIDIARY_TYPE_NAME).asString());
				dto.setImFlag(rVal(r, TSubsidiaryTypePeer.IM_FLAG).asString());
				dto.setFreightRuFlag(rVal(r, TSubsidiaryTypePeer.FREIGHT_RU_FLAG).asString());
				dto.setPassengerRuFlag(rVal(r, TSubsidiaryTypePeer.PASSENGER_RU_FLAG).asString());
				dto.setCentralEntityFlag(rVal(r, TSubsidiaryTypePeer.CENTRAL_ENTITY_FLAG).asString());
				dto.setNationalEntityFlag(rVal(r, TSubsidiaryTypePeer.NATIONAL_ENTITY_FLAG).asString());
				dto.setOthersFlag(rVal(r, TSubsidiaryTypePeer.OTHERS_FLAG).asString());
				dto.setFreeText(rVal(r, TSubsidiaryTypePeer.FREE_TEXT).asString());

				return dto;
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubsidiaryType.error", auth);
		}
		return null;
	}

	public DTOTPrimaryLocation getPrimaryLocationByLocCodeAndCountry(AuthInfo auth,
			String locationCode, Integer idCountry, Integer idCompany, Date primaryLocationStartValidaty)
			throws AppException {
		// Systém vyhľadá záznam v T_PRIMARY_LOCATION, kde
		// .LOCATION_CODE = locationCode a zároveň
		// .ID_COUNTRY= vst. CountryID a zároveň
		// .START_VALIDITY = vst. StartValidity a zároveň // posledny by mal byt aktualny
		// .ZMAZ = False a zároveň
		// .PLATNOST_DO is null

		try {
			MyCriteria2 crit = new MyCriteria2(TPrimaryLocationPeer.PRIMARY_LOCATION_ID, new DTOTPrimaryLocation());
			TPrimaryLocationPeer.addSelectColumns(crit);
			crit.addConditional(TPrimaryLocationPeer.LOCATION_CODE, locationCode);
			crit.addConditional(TPrimaryLocationPeer.ID_COUNTRY, idCountry);
			crit.addConditional(TPrimaryLocationPeer.ID_COMPANY, idCompany);
			crit.getNewCriterion(TPrimaryLocationPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			crit.getNewCriterion(TPrimaryLocationPeer.START_VALIDITY, primaryLocationStartValidaty,
 MyCriteria2.EQUAL);
			crit.addConditional(TPrimaryLocationPeer.ZMAZ, "F");

			String sql = crit.getSQL();// --+ " ORDER BY " + TPrimaryLocationPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				// DTOTPrimaryLocation dto = new DTOTPrimaryLocation();
				Record r = (Record) iter.next();

				return getDtoPrimaryLoc(r);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getPrimaryLocationByLocCode.error", auth);
			return null;
		}
	}



	


	public DTOTPrimaryLocation getPrimaryLocationByLocCodeAndCountry(AuthInfo auth, String locationCode,
			Integer idCountry,
			Date primaryLocationStartValidaty) throws AppException {
		try {
			// Systém vyhľadá záznam v T_PRIMARY_LOCATION, kde
			//
			// .LOCATION_CODE = locationCode a zároveň
			// .ID_COUNTRY= vst. CountryID a zároveň
			// .START_VALIDITY = vst. StartValidity a zároveň
			// .ZMAZ = False a zároveň
			// .PLATNOST_DO is null

			MyCriteria2 crit = new MyCriteria2(TPrimaryLocationPeer.PRIMARY_LOCATION_ID, new DTOTPrimaryLocation());
			TPrimaryLocationPeer.addSelectColumns(crit);
			crit.addConditional(TPrimaryLocationPeer.LOCATION_CODE, locationCode);
			// crit.addConditional(TPrimaryLocationPeer.PRIMARY_LOCATION_ID, idPrimLoc);
			crit.addConditional(TPrimaryLocationPeer.ID_COUNTRY, idCountry);
			crit.addConditional(TPrimaryLocationPeer.START_VALIDITY, primaryLocationStartValidaty, Criteria.EQUAL);
			crit.addConditional(TPrimaryLocationPeer.ZMAZ, "F");
			// crit.addConditional(TPrimaryLocationPeer.PLATNOST_DO, null, MyCriteria2.ISNULL); neprida
			crit.addCustomSql(TPrimaryLocationPeer.PLATNOST_DO, TPrimaryLocationPeer.PLATNOST_DO + " is null ");
			// crit.addDescendingOrderByColumn(TPrimaryLocationPeer.HIST_ID);

			String sql = crit.getSQL() + " ORDER BY " + TPrimaryLocationPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				// DTOTPrimaryLocation dto = new DTOTPrimaryLocation();
				Record r = (Record) iter.next();

				return getDtoPrimaryLoc(r);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getCountry.error", auth);
			return null;
		}
	}

	public DTOTPrimaryLocation getPrimaryLocationByIdPrimLoc(AuthInfo auth, Integer idPrimLoc, Date platnostOd)
			throws AppException {

		try {
			// Systém vyhľadá T_PRIMARY_LOCATION, kde PRIMARY_LOCATION_ID = vstup:PrimaryLocationID
			// .PLANTOST_Od<=kdatumu a zaroveň
			// (
			// PLATNOST_DO je null
			// alebo
			// PLATNOST_DO>=kdatumu
			// )

			// je potrebne pridat dalsie kriteria : primarylocationCode, CountryCode, primaryLocationStartValidaty
			MyCriteria2 crit = new MyCriteria2(TPrimaryLocationPeer.PRIMARY_LOCATION_ID, new DTOTPrimaryLocation());
			TPrimaryLocationPeer.addSelectColumns(crit);
			crit.addConditional(TPrimaryLocationPeer.PRIMARY_LOCATION_ID, idPrimLoc);
			crit.addConditional(TPrimaryLocationPeer.ZMAZ, "F");
			String s = CudVysielanieUtils.getCritPlatneDatumOdDo(new Date(), TPrimaryLocationPeer.PLATNOST_OD,
					TPrimaryLocationPeer.PLATNOST_DO);
			crit.addCustomSql(TPrimaryLocationPeer.PLATNOST_OD, s);
			String sql = crit.getSQL() + " ORDER BY " + TPrimaryLocationPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				// DTOTPrimaryLocation dto = new DTOTPrimaryLocation();
				Record r = (Record) iter.next();

				return getDtoPrimaryLoc(r);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getPrimaryLocationByIdPrimLoc.error", auth);
			return null;
		}
	}

	public DTOTPrimaryLocation getPrimaryLocationForValidity(AuthInfo auth, String locationCode, Integer idCountry,
			Integer idCompany, Date primaryLocationStartValidaty, Date $datumNow) throws AppException {
		try {

			// /*$dataRowPrimaryLocation*/
			// Systém vyh¾adá pre bod primárnu Lokalitu
			// ZMAZ=F
			// LOCATION_CODE=prvých 5 èislic z $NadradenyDb.CISLO
			// START_VALIDITY<= $vlozDatum a (END_VALIDTY is null or END_VALIDITY>=$vlozDatum)
			// ID_COMPANY.COMPANY_UIC_CODE = UICZSR
			// ID_COMPANY.PLATNOST_DO is null
			// ID_COUNTRY=$IDCountrySK
			// usporiadaj podla START_VALIDITY desc

			// je potrebne pridat dalsie kriteria : primarylocationCode, CountryCode, primaryLocationStartValidaty
			MyCriteria2 crit = new MyCriteria2(TPrimaryLocationPeer.PRIMARY_LOCATION_ID, new DTOTPrimaryLocation());
			TPrimaryLocationPeer.addSelectColumns(crit);
			crit.addConditional(TPrimaryLocationPeer.LOCATION_CODE, locationCode);
			crit.addConditional(TPrimaryLocationPeer.ID_COUNTRY, idCountry);
			crit.addConditional(TPrimaryLocationPeer.ID_COMPANY, idCompany);
			crit.add(TPrimaryLocationPeer.START_VALIDITY, primaryLocationStartValidaty, MyCriteria2.LESS_EQUAL);
			Criteria.Criterion c1 = crit.getNewCriterion(TPrimaryLocationPeer.END_VALIDITY, null, MyCriteria2.ISNULL);

			Criteria.Criterion c2 = crit.getNewCriterion(TPrimaryLocationPeer.END_VALIDITY,
					primaryLocationStartValidaty, MyCriteria2.GREATER_EQUAL);
			crit.add(c1.or(c2));
			crit.add(TPrimaryLocationPeer.PLATNOST_OD, $datumNow, MyCriteria2.LESS_EQUAL);
			Criteria.Criterion c3 = crit.getNewCriterion(TPrimaryLocationPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);

			Criteria.Criterion c4 = crit.getNewCriterion(TPrimaryLocationPeer.PLATNOST_DO, $datumNow,
					MyCriteria2.GREATER_EQUAL);
			crit.add(c3.or(c4));

			crit.addConditional(TPrimaryLocationPeer.ZMAZ, "F");

			String sql = crit.getSQL() + " ORDER BY " + TPrimaryLocationPeer.START_VALIDITY + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				return getDtoPrimaryLoc(r);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getCountry.error", auth);
			return null;
		}
	}

	public ArrayList<DTOTPrimaryLocation> getPrimaryLocationList(AuthInfo auth, String locationCode, Integer idCountry,
			String companyUicCode, Date vlozDatum) throws AppException {
		try {
			// /*$dataRowPrimaryList*/
			// Systém z aktuálnych záznamov vyfiltruje zaznamy
			// ZMAZ='F'
			// LOCATION_CODE=prvých 5 èislic z $dataRow.CISLO
			// OLD:START_VALIDITY<= $vlozDatum and (END_VALIDTY is null or END_VALIDITY>=$vlozDatum)
			// (START_VALIDITY<= $vlozDatum or START_VALIDITY>$vlozDatum ) and (END_VALIDTY is null or
			// END_VALIDITY>=$vlozDatum)

			// ID_COMPANY.COMPANY_UIC_CODE = UICZSR
			// ID_COMPANY.PLATNOST_DO is null
			//
			// ID_COUNTRY=$IDCountrySK
			// usporiadaj podla START_VALIDITY desc

			String sql = " select pl.* from T_PRIMARY_LOCATION pl "
					+ " JOIN t_country  country ON (country.country_id = "
					+ idCountry
					+ " and country.platnost_do is null) "
					+ " JOIN t_company  company ON (company.company_id = pl.id_company  and company.company_uic_code='"
					+ companyUicCode
					+ "' and company.platnost_do is null)"
					+ " where pl.ZMAZ='F'"
					// + "  AND  pl.PLATNOST_OD  <=  " + CudVysielanieUtils.dateCritFormat(datumNow)
					// OR pl.PLATNOST_DO <= CudVysielanieUtils.dateCritFormat(datumNow) + ")"
					+ " and pl.location_code = '"
					+ locationCode
					+ "'"
					// (START_VALIDITY<= $vlozDatum or START_VALIDITY>$vlozDatum ) and (END_VALIDTY is null or
					// END_VALIDITY>=$vlozDatum)
					+ " and ( pl.start_validity<=" + CudVysielanieUtils.dateCritFormat(vlozDatum)
					+ " or pl.start_validity >" + CudVysielanieUtils.dateCritFormat(vlozDatum)
					+ " ) and ( pl.end_validity is null or pl.end_validity>="
					+ CudVysielanieUtils.dateCritFormat(vlozDatum) + ") order by pl.start_validity desc";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			ArrayList<DTOTPrimaryLocation> dtoList = new ArrayList<DTOTPrimaryLocation>();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				DTOTPrimaryLocation dto = getDtoPrimaryLoc(r);
				dtoList.add(dto);
			}
			return dtoList;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getPrimaryLocationList.error", auth);
			return null;
		}
	}

	public ArrayList<DTOTPrimaryLocation> getPrimaryLocationListForExport(AuthInfo auth, Date datumAcasNacitania,
			Date datumAcasPoslExportu, Integer idCountry, Integer idCompany) throws AppException {
		try {
			// Systém vyhľadá T_PRIMARY_LOCATION, kde:
			// PLATNOST_OD <= datumACasNacitaniaDat
			// A zároveň
			// (
			// PLATNOST_DO JE NULL
			// ALebo
			// PLATNOST_DO >= dátum(datumACasNacitaniaDat)
			// )
			// A zároveň
			// (
			// CAS_ZMENY > vstup:PoslednyExport a zároveň CAS_ZMENY<=datumACasNacitaniaDat
			// alebo
			// CAS_VYTVORENIA> vstup:PoslednyExport a zároveň CAS_VYTVORENIA <=dátumAčasNačítaniaDát
			// )
			// A zároveň
			// ID_COUNTRY= vstup:CountryID
			// A zároveň
			// ID%COMPANY.T_COMPANY.COMPANY_UIC_CODE=0056 kde T_COMPANY.PLATOST_DO is null

//				Systém vráti pre každý nájdeý PRIMARY_LOCATION_ID, zaznam kde NVL(CAS_ZMENY,CAS_VYTVORENIA) je maximálny. 
//
//				zaznamy su usporiadane podľa NVL(CAS_ZMENY,CAS_VYTVORENIA)  asc,PRIMARY_LOCATION_ID asc ,HIS_ID desc
//
//				ak idu dva zaznamy za sebou PRIMARY_LOCATION_ID vezme len ten prvy t.j. vrá?ti mladsi záznam pre danú lokalitu



			String sql = " SELECT  * FROM ( SELECT tpl.*, ROW_NUMBER() OVER ( "
 + " PARTITION BY PRIMARY_LOCATION_ID "
					+ " order by NVL(CAS_ZMENY,CAS_VYTVORENIA) desc,  hist_id desc " +
					
					"	 ) AS rn" + "	FROM T_PRIMARY_LOCATION tpl"
					+ "  WHERE PLATNOST_OD <= " + CudVysielanieUtils.dateTimeCritFormat(datumAcasNacitania)
					+ "	  AND (PLATNOST_DO IS NULL OR PLATNOST_DO >="
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasNacitania) + ") AND (" + "	(CAS_ZMENY >"
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasPoslExportu) + " AND CAS_ZMENY <= "
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasNacitania) + ") " + "	OR (CAS_VYTVORENIA >"
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasPoslExportu) + " AND CAS_VYTVORENIA <="
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasNacitania) + ")  )" + " AND ID_COUNTRY ="
					+ idCountry + " AND ID_COMPANY =" + idCompany + ") " + "	WHERE rn = 1 ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOTPrimaryLocation> list = new ArrayList<DTOTPrimaryLocation>();
			while (iter.hasNext()) {

				Record r = (Record) iter.next();

				list.add(getDtoPrimaryLoc(r));
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getPrimaryLocationListForExport.error", auth);
			return null;
		}
	}

	private DTOTPrimaryLocation getDtoPrimaryLoc(Record r) throws DataSetException {
		DTOTPrimaryLocation dto = new DTOTPrimaryLocation();
		dto.setHistID(rVal(r, TPrimaryLocationPeer.HIST_ID).asIntegerObj());
		dto.setPlatnostOd(rVal(r, TPrimaryLocationPeer.PLATNOST_OD).asUtilDate());
		dto.setPlatnostDo(rVal(r, TPrimaryLocationPeer.PLATNOST_DO).asUtilDate());
		dto.setCasVytvorenia(rVal(r, TPrimaryLocationPeer.CAS_VYTVORENIA).asUtilDate());
		dto.setCasZmeny(rVal(r, TPrimaryLocationPeer.CAS_ZMENY).asUtilDate());
		dto.setIDZmena(rVal(r, TPrimaryLocationPeer.ID_ZMENA).asIntegerObj());
		dto.setZmaz(rVal(r, TPrimaryLocationPeer.ZMAZ).asString());
		dto.setPrimaryLocationID(rVal(r, TPrimaryLocationPeer.PRIMARY_LOCATION_ID).asIntegerObj());
		dto.setIDCountry(rVal(r, TPrimaryLocationPeer.ID_COUNTRY).asIntegerObj());
		dto.setLocationCode(rVal(r, TPrimaryLocationPeer.LOCATION_CODE).asString());
		dto.setStartValidity(rVal(r, TPrimaryLocationPeer.START_VALIDITY).asUtilDate());
		dto.setEndValidity(rVal(r, TPrimaryLocationPeer.END_VALIDITY).asUtilDate());
		dto.setIDCompany(rVal(r, TPrimaryLocationPeer.ID_COMPANY).asIntegerObj());
		dto.setLocationName(rVal(r, TPrimaryLocationPeer.LOCATION_NAME).asString());
		dto.setLocationNameAscii(rVal(r, TPrimaryLocationPeer.LOCATION_NAME_ASCII).asString());
		dto.setNutsCode(rVal(r, TPrimaryLocationPeer.NUTS_CODE).asString());
		dto.setContainerHandlingFlag(rVal(r, TPrimaryLocationPeer.CONTAINER_HANDLING_FLAG).asString());
		dto.setHandoverPointFlag(rVal(r, TPrimaryLocationPeer.HANDOVER_POINT_FLAG).asString());
		dto.setFreightPossibleFlag(rVal(r, TPrimaryLocationPeer.FREIGHT_POSSIBLE_FLAG).asString());
		dto.setFreightStartValidity(rVal(r, TPrimaryLocationPeer.FREIGHT_START_VALIDITY).asUtilDate());
		dto.setFreightEndValidity(rVal(r, TPrimaryLocationPeer.FREIGHT_END_VALIDITY).asUtilDate());
		dto.setPassengerPossibleFlag(rVal(r, TPrimaryLocationPeer.PASSENGER_POSSIBLE_FLAG).asString());
		dto.setPassengerStartValidity(rVal(r, TPrimaryLocationPeer.PASSENGER_START_VALIDITY).asUtilDate());
		dto.setPassengerEndValidity(rVal(r, TPrimaryLocationPeer.PASSENGER_END_VALIDITY).asUtilDate());
		dto.setLongitude(rVal(r, TPrimaryLocationPeer.LONGITUDE).asDoubleObj());
		dto.setLatitude(rVal(r, TPrimaryLocationPeer.LATITUDE).asDoubleObj());
		dto.setFreeText(rVal(r, TPrimaryLocationPeer.FREE_TEXT).asString());
		dto.setActiveFlag(rVal(r, TPrimaryLocationPeer.ACTIVE_FLAG).asString());
		return dto;
	}

	public ArrayList<DTOTPrimaryLocation> getPrimaryLocationListBuduceForExport(AuthInfo auth, Date datumAcasNacitania,
			Date datumAcasPoslExportu, Integer countryID) throws AppException {
		try {
			// Systém vyhľadá T_PRIMARY_LOCATION $Loccation1, kde:
			// (
			// CAS_ZMENY > vstup:PoslednyExport A zároveň CAS_ZMENY <= dátumAčasNačítaniaDát
			// alebo
			// CAS_VYTVORENIA> vstup:PoslednyExport a zároveň CAS_VYTVORENIA <=dátumAčasNačítaniaDát
			// )
			// A zároveň
			// PLATNOST_OD > datum(dátumAčasNačítaniaDát)
			// A zároveň
			// ID_COUNTRY= vstup:CountryID

			String sql = " select * from t_primary_location p "
					+ " join t_subsidiary_location s on p.primary_location_id=s.id_primary_location "
					+ " where p.id_country=" + countryID

					+ " and (p.cas_zmeny > " + CudVysielanieUtils.dateCritFormat(datumAcasPoslExportu)
					+ "  and p.cas_zmeny <= " + CudVysielanieUtils.dateCritFormat(datumAcasNacitania) + " ) "
					+ " or (p.cas_vytvorenia> " + CudVysielanieUtils.dateCritFormat(datumAcasPoslExportu)
					+ " and p.cas_vytvorenia<=" + CudVysielanieUtils.dateCritFormat(datumAcasNacitania) + " )"
					+ " and ( p.platnost_od >" + CudVysielanieUtils.dateCritFormat(datumAcasNacitania) + ")";

			// A zároveň
			// //neexistuje žiaden záznam platný aktuálne (platne v minulosti ma uz nezaujimaju)
			// Neexistuje T_PRIMARY_LOCADTION, kde LOCATION_CODE = $Loccation1.LOCATION_CODE
			// A zároveň
			// ID_COUNTRY= vstup:CountryID
			// A zároveň
			// PLATNOST_OD =< datum(dátumAčasNačítaniaDát)
			// A zároveň
			// ZRUSENY='F'
			// (PLATNOST_DO >=datum(dátumAčasNačítaniaDáta) Alebo PLATNOST_DO JE NULL)
			sql += " and  p.primary_location_id not in ( select l.primary_location_id from   t_primary_location l 	"
					+ " where l.location_code =p.location_code and l.id_country = " + countryID
					+ " and l.platnost_od <=" + CudVysielanieUtils.dateCritFormat(datumAcasNacitania)
					+ " and l.zmaz ='F' " + " and ( l.platnost_do >="
					+ CudVysielanieUtils.dateCritFormat(datumAcasNacitania) + " or l.platnost_do is null) )	";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOTPrimaryLocation> list = new ArrayList<DTOTPrimaryLocation>();
			while (iter.hasNext()) {

				Record r = (Record) iter.next();

				list.add(getDtoPrimaryLoc(r));
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getPrimaryLocationListBuduceForExport.error", auth);
			return null;
		}
	}

	public DTOTPrimaryLocation getPrimaryLocationListPredch(AuthInfo auth, Integer locationCodeId, Integer idCountry,
			Date datumAcasPosl, Date datumAcasNacitania) throws AppException {
		try {
			// .PRIMARY_LOCATION_CODE = vstup:LocationCode,
			// A zároveň
			// .ID_COUNTRY= vstup:CountryID
			// A zároveň
			// PLATNOST_OD < vstup:Datum
			// A zároveň
			// (
			// CAS_ZMENY is not null zároveň CAS_ZMENY<= vstup:Datum
			// alebo
			// CAS_VYTVORENIA is not null a zaroveň CAS_VYTVORENIA <= vstup:Datum
			// )
			// vyber záznam kde NVL(CAS_ZMENY ,CAS_VYTVORENIA) je maximalne

			MyCriteria2 crit = new MyCriteria2(TPrimaryLocationPeer.PRIMARY_LOCATION_ID, new DTOTPrimaryLocation());
			TPrimaryLocationPeer.addSelectColumns(crit);
			// crit.addConditional(TPrimaryLocationPeer.LOCATION_CODE, locationCode);
			crit.addConditional(TPrimaryLocationPeer.PRIMARY_LOCATION_ID, locationCodeId);
			crit.addConditional(TPrimaryLocationPeer.ID_COUNTRY, idCountry);
			// crit.addConditional(TPrimaryLocationPeer.PLATNOST_OD, datumAcasNacitania, Criteria.LESS_THAN);
			String sql = crit.getSQL();
			// MyCriteria2 crit2 = new MyCriteria2();
			// crit.addConditional(TPrimaryLocationPeer.PLATNOST_DO, datum, MyCriteria2.GREATER_EQUAL);
			// sql = crit.getCriterion(atribut).toString();
			sql += "  AND "
					+ TPrimaryLocationPeer.PLATNOST_OD
					+ "<"
					+ CudVysielanieUtils.timeCritFormat(datumAcasPosl)
					// + " AND (" + TPrimaryLocationPeer.PLATNOST_DO + " IS NULL OR " + TPrimaryLocationPeer.PLATNOST_OD
					// + "<=" + TPrimaryLocationPeer.PLATNOST_DO + " )"

					+ " AND (( " + TPrimaryLocationPeer.CAS_ZMENY + " IS NOT NULL  AND "
					+ TPrimaryLocationPeer.CAS_ZMENY + " <=" + CudVysielanieUtils.timeCritFormat(datumAcasPosl)
					+ ")  OR ( " + TPrimaryLocationPeer.CAS_VYTVORENIA + " IS NOT NULL  AND "
					+ TPrimaryLocationPeer.CAS_VYTVORENIA + " <=" + CudVysielanieUtils.timeCritFormat(datumAcasPosl)
					+ ") ) ";

			sql += " ORDER BY  NVL(CAS_ZMENY ,CAS_VYTVORENIA) DESC, "
					+ TPrimaryLocationPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {

				Record r = (Record) iter.next();
				return getDtoPrimaryLoc(r);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getPrimaryLocationListPredch.error", auth);
			return null;
		}
	}

	public DTOTSubsidiaryLocation getSubLocationListPredch(AuthInfo auth, Integer locationCodeId, Integer idCountry,
			Date datumAcasPosl, Date datumAcasNacitania) throws AppException {
		try {

			MyCriteria2 crit = new MyCriteria2(TSubsidiaryLocationPeer.SUBSIDIARY_LOCATION_ID,
					new DTOTSubsidiaryLocation());
			TSubsidiaryLocationPeer.addSelectColumns(crit);
			crit.addConditional(TSubsidiaryLocationPeer.SUBSIDIARY_LOCATION_ID, locationCodeId);
			crit.addConditional(TSubsidiaryLocationPeer.ID_COUNTRY, idCountry);
			// crit.addConditional(TSubsidiaryLocationPeer.PLATNOST_OD, datumAcasPosl, Criteria.LESS_THAN);
			String sql = crit.getSQL();

			// .PRIMARY_LOCATION_CODE = vstup:LocationCode,
			// A zároveň
			// .ID_COUNTRY= vstup:CountryID
			// A zároveň
			// PLATNOST_OD < vstup:Datum
			// A zároveň
			// (
			// CAS_ZMENY is not null zároveň CAS_ZMENY<= vstup:Datum
			// alebo
			// CAS_VYTVORENIA is not null a zaroveň CAS_VYTVORENIA <= vstup:Datum
			// )
			// vyber záznam kde NVL(CAS_ZMENY ,CAS_VYTVORENIA) je maximalne

			sql += " AND "
					+ TSubsidiaryLocationPeer.PLATNOST_OD
					+ "<"
					+ CudVysielanieUtils.timeCritFormat(datumAcasPosl)

					// + " AND (" + TSubsidiaryLocationPeer.PLATNOST_DO + " IS NULL OR " +
					// TSubsidiaryLocationPeer.PLATNOST_OD + "<=" + TSubsidiaryLocationPeer.PLATNOST_DO + " )"

					+ " AND (( " + TSubsidiaryLocationPeer.CAS_ZMENY + " IS NOT NULL  AND "
					+ TSubsidiaryLocationPeer.CAS_ZMENY + " <=" + CudVysielanieUtils.timeCritFormat(datumAcasPosl)
					+ ")  OR ( " + TSubsidiaryLocationPeer.CAS_VYTVORENIA + " IS NOT NULL  AND "
					+ TSubsidiaryLocationPeer.CAS_VYTVORENIA + " <=" + CudVysielanieUtils.timeCritFormat(datumAcasPosl)
					+ ") ) ";

			sql += " ORDER BY  NVL(CAS_ZMENY , CAS_VYTVORENIA) DESC, " + TSubsidiaryLocationPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			while (iter.hasNext()) {

				Record r = (Record) iter.next();
				return getDtoSubsidiaryLocation(r);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubLocationListPredch.error", auth);
			return null;
		}
	}

	public List<DTOOdberatelObjekt> getOOlist(AuthInfo auth, Date caPreVyber, Integer idObjekt) throws AppException {

		try {

			MyCriteria2 crit = new MyCriteria2(CudOdberatelObjektPeer.TABLE_NAME, new DTOOdberatelObjekt());
			CudOdberatelObjektPeer.addSelectColumns(crit);

			crit.addConditional(CudOdberatelObjektPeer.ID_OBJEKT, idObjekt);
			crit.addConditional(CudOdberatelObjektPeer.TYP_PRISTUPU, "2");
			crit.addCustomSql(CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE,
					CudOdberatelObjektPeer.ID_TRANSAKCIA_ZRUSENE + " is null ");
			// and typ_pristupu=2
			// and id_transakcia_zrusene is null

			crit.addConditional(CudOdberatelObjektPeer.PLATNOST_OD, caPreVyber, Criteria.LESS_THAN);
			String sql = crit.getSQL();

			sql += " AND ( " + CudVysielanieUtils.timeCritFormat(caPreVyber) + "<="
					+ CudOdberatelObjektPeer.PLATNOST_DO + " OR " + CudOdberatelObjektPeer.PLATNOST_DO + " IS NULL)";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			List<DTOOdberatelObjekt> listDTO = new ArrayList<DTOOdberatelObjekt>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOOdberatelObjekt dto = new DTOOdberatelObjekt();
				dto.setOdberatelObjektID(rVal(r, CudOdberatelObjektPeer.ODBERATEL_OBJEKT_ID).asIntegerObj());
				dto.setIDOdberatel(rVal(r, CudOdberatelObjektPeer.ID_ODBERATEL).asIntegerObj());
				dto.setIDObjekt(rVal(r, CudOdberatelObjektPeer.ID_OBJEKT).asIntegerObj());
				dto.setPlatnostOd(rVal(r, CudOdberatelObjektPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudOdberatelObjektPeer.PLATNOST_DO).asUtilDate());
				dto.setTypPristupu(rVal(r, CudOdberatelObjektPeer.TYP_PRISTUPU).asString());
				dto.setVsetkyCiselniky(rVal(r, CudOdberatelObjektPeer.VSETKY_CISELNIKY).asString());
				dto.setOpakovanie(rVal(r, CudOdberatelObjektPeer.OPAKOVANIE).asString());
				dto.setExportRozsah(rVal(r, CudOdberatelObjektPeer.EXPORT_ROZSAH).asString());
				dto.setExportDovod(rVal(r, CudOdberatelObjektPeer.EXPORT_DOVOD).asString());
				dto.setExportFormat(rVal(r, CudOdberatelObjektPeer.EXPORT_FORMAT).asString());
				dto.setCasPoslExportuZmena(rVal(r, CudOdberatelObjektPeer.CAS_POSL_EXPORTU_ZMENA).asUtilDate());
				dto.setCasPoslExportu(rVal(r, CudOdberatelObjektPeer.CAS_POSL_EXPORTU).asUtilDate());
				dto.setExportCesta(rVal(r, CudOdberatelObjektPeer.EXPORT_CESTA).asString());

				listDTO.add(dto);
			}

			return listDTO;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getOOlist.error", auth);
			return null;
		}
	}

	public ArrayList<DTOTSubsidiaryLocation> getSubsidiaryLocationListForExport(AuthInfo auth, Date datumAcasNacitania,
			Date datumAcasPoslExportu, Integer idCountry, Integer idCompany) throws AppException {
		try {
			// Systém vyhľadá T_SUBSIDIARY_LOCATION, kde:
			// PLATNOST_OD <= datum(dátumAčasNačítaniaDát)
			// A zároveň
			// (PLATNOST_DO JE NULL
			// ALebo
			// PLATNOST_DO >= datum (dátumAčasNačítaniaDát)
			// )
			// A zároveň
			// ID_COUNTRY= vstup:CountryID
			// A zároveň
			// (
			// CAS_ZMENY > vstup:PoslednyExport a zároveň CAS_ZMENY <=dátumAčasNačítaniaDát
			// alebo
			// CAS_VYTVORENIA> vstup:PoslednyExport a zároveň CAS_VYTVORENIA <=dátumAčasNačítaniaDát
			// //)

			// MyCriteria2 crit = new MyCriteria2(TSubsidiaryLocationPeer.TABLE_NAME, new DTOTSubsidiaryLocation());
			// TSubsidiaryLocationPeer.addSelectColumns(crit);

			// crit.addConditional(TSubsidiaryLocationPeer.ID_COUNTRY, idCountry);
			// crit.addConditional(TSubsidiaryLocationPeer.PLATNOST_OD, datumAcasNacitania, Criteria.LESS_EQUAL);
			String sql = " SELECT * FROM (  SELECT tpl.*, 	ROW_NUMBER() OVER ( "
					+ "	PARTITION BY SUBSIDIARY_LOCATION_ID "
					+ " order by NVL(CAS_ZMENY,CAS_VYTVORENIA)desc ,  HIST_ID desc " + ") AS rn"
					+ " FROM T_SUBSIDIARY_LOCATION tpl" + "	WHERE PLATNOST_OD <= "
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasNacitania)
					+ "	AND (PLATNOST_DO IS NULL OR PLATNOST_DO >="
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasNacitania) + ")  AND ( (CAS_ZMENY >"
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasPoslExportu) + " AND CAS_ZMENY <= "
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasNacitania) + ")" + "	 OR (CAS_VYTVORENIA >"
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasPoslExportu) + " AND CAS_VYTVORENIA <="
					+ CudVysielanieUtils.dateTimeCritFormat(datumAcasNacitania) + ") )" + " AND ID_COUNTRY ="
					+ idCountry + " AND ID_COMPANY =" + idCompany + ") 	WHERE rn = 1 ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOTSubsidiaryLocation> list = new ArrayList<DTOTSubsidiaryLocation>();
			while (iter.hasNext()) {

				Record r = (Record) iter.next();

				list.add(getDtoSubsidiaryLocation(r));
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubsidiaryLocationListForExport.error", auth);
			return null;
		}
	}

	private DTOTSubsidiaryLocation getDtoSubsidiaryLocation(Record r) throws DataSetException {
		DTOTSubsidiaryLocation dto = new DTOTSubsidiaryLocation();
		dto.setHistID(rVal(r, TSubsidiaryLocationPeer.HIST_ID).asIntegerObj());
		dto.setPlatnostOd(rVal(r, TSubsidiaryLocationPeer.PLATNOST_OD).asUtilDate());
		dto.setPlatnostDo(rVal(r, TSubsidiaryLocationPeer.PLATNOST_DO).asUtilDate());
		dto.setCasVytvorenia(rVal(r, TSubsidiaryLocationPeer.CAS_VYTVORENIA).asUtilDate());
		dto.setCasZmeny(rVal(r, TSubsidiaryLocationPeer.CAS_ZMENY).asUtilDate());
		dto.setIDZmena(rVal(r, TSubsidiaryLocationPeer.ID_ZMENA).asIntegerObj());
		dto.setZmaz(rVal(r, TSubsidiaryLocationPeer.ZMAZ).asString());
		dto.setSubsidiaryLocationID(rVal(r, TSubsidiaryLocationPeer.SUBSIDIARY_LOCATION_ID).asIntegerObj());
		dto.setIDSubsidiaryType(rVal(r, TSubsidiaryLocationPeer.ID_SUBSIDIARY_TYPE).asIntegerObj());
		dto.setIDCompany(rVal(r, TSubsidiaryLocationPeer.ID_COMPANY).asIntegerObj());
		dto.setIDCountry(rVal(r, TSubsidiaryLocationPeer.ID_COUNTRY).asIntegerObj());
		dto.setResponsibleImCode(rVal(r, TSubsidiaryLocationPeer.RESPONSIBLE_IM_CODE).asIntegerObj());
		dto.setSubsidiaryLocationCode(rVal(r, TSubsidiaryLocationPeer.SUBSIDIARY_LOCATION_CODE).asString());
		dto.setSubsidiaryLocationName(rVal(r, TSubsidiaryLocationPeer.SUBSIDIARY_LOCATION_NAME).asString());
		dto.setIDPrimaryLocation(rVal(r, TSubsidiaryLocationPeer.ID_PRIMARY_LOCATION).asIntegerObj());
		dto.setStartValidity(rVal(r, TSubsidiaryLocationPeer.START_VALIDITY).asUtilDate());
		dto.setEndValidity(rVal(r, TSubsidiaryLocationPeer.END_VALIDITY).asUtilDate());
		dto.setLongitude(rVal(r, TSubsidiaryLocationPeer.LONGITUDE).asDoubleObj());
		dto.setLatitude(rVal(r, TSubsidiaryLocationPeer.LATITUDE).asDoubleObj());
		dto.setFreeText(rVal(r, TSubsidiaryLocationPeer.FREE_TEXT).asString());
		dto.setActiveFlag(rVal(r, TSubsidiaryLocationPeer.ACTIVE_FLAG).asString());
		return dto;
	}

	public ArrayList<DTOTSubsidiaryLocation> getSubsidiaryLocationBuduceForExport(AuthInfo auth,
			Date datumAcasNacitania, Date datumAcasPoslExportu, Integer countryID) throws AppException {
		// Systém vyhľadá T_SUBSIDIARY_LOCATION $Loccation1, kde:
		// // ID_COUNTRY= vstup:CountryID (
		// CAS_ZMENY > vstup:PoslednyExport A zároveň CAS_ZMENY <= dátumAčasNačítaniaDát
		// alebo
		// CAS_VYVORENIA > vstup:PoslednyExport A zároveň CAS_VYVORENIA <= dátumAčasNačítaniaDát
		// )
		try {
			String sql = " select * from T_SUBSIDIARY_LOCATION p " + " where p.id_country=" + countryID
					+ " and  p.platnost_od >" + CudVysielanieUtils.dateCritFormat(datumAcasNacitania)
					+ " and p.cas_zmeny > " + CudVysielanieUtils.timeCritFormat(datumAcasPoslExportu)
					+ "  and ( p.cas_zmeny <= " + CudVysielanieUtils.timeCritFormat(datumAcasNacitania)
					+ " or (p.cas_vytvorenia> " + CudVysielanieUtils.timeCritFormat(datumAcasPoslExportu)
					+ " and p.cas_vytvorenia<=" + CudVysielanieUtils.timeCritFormat(datumAcasNacitania) + " ) ) ";

			sql += " and  p.subsidiary_location_id  not in ( select l.subsidiary_location_id from   T_SUBSIDIARY_LOCATION l 	"
					+ " where l.SUBSIDIARY_LOCATION_CODE =p.SUBSIDIARY_LOCATION_CODE and l.ID_COUNTRY = "
					+ countryID
					+ " and l.platnost_od <="
					+ CudVysielanieUtils.timeCritFormat(datumAcasNacitania)
					+ " and l.zmaz ='F' "
					+ " and ( l.platnost_do >="
					+ CudVysielanieUtils.timeCritFormat(datumAcasNacitania) + " or l.platnost_do is null) )	";

			// A zároveň
			// PLATNOST_OD > dátum(dátumAčasNa čítaniaDát)

			// A zároveň
			// //neexistuje žiaden záznam platný aktuálne (platne v minulosti ma uz nezaujimaju)
			// Neexistuje T_SUBSIDIARY_LOCADTION, kde SUBSIDIARY_LOCATION_CODE = $Loccation1.SUBSIDIARY_LOCATION_CODE
			// A zároveň
			// ID_COUNTRY= vstup:CountryID
			// A zároveň
			// PLATNOST_OD =< dátum(dátumAčasNačítaniaDát)
			// A zároveň
			// ZRUSENY='F'
			// (PLATNOST_DO >= dátum(dátumAčasNačítaniaDáta) čas Alebo PLATNOST_DO JE NULL)
			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOTSubsidiaryLocation> list = new ArrayList<DTOTSubsidiaryLocation>();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				list.add(getDtoSubsidiaryLocation(r));
			}
			return list;
		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubsidiaryLocationBuduceForExport.error", auth);
			return null;
		}

	}

	public DTOTDopravnyBod getDopravnyBodByZmenaId(AuthInfo auth, Integer dbId, Integer idZmena) throws AppException {
		try {
			// Systém naèíta z T_DOPRAVNY_BOD záznam kde DOPRAVNY_BOD_ID=Zmena.ROW_ID a zároveò
			// ID_ZMENA=Zmena.ZMENA_ID

			MyCriteria2 crit = new MyCriteria2(TDopravnyBodPeer.HIST_ID);
			TDopravnyBodPeer.addSelectColumns(crit);
			crit.addConditional(TDopravnyBodPeer.ID_ZMENA, idZmena);
			crit.addConditional(TDopravnyBodPeer.DOPRAVNY_BOD_ID, dbId);
			// crit.addConditional(TCountryPeer.ZMAZ, "F");

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			// ArrayList<DTOTDopravnyBod> list = new ArrayList<DTOTDopravnyBod>();
			while (iter.hasNext()) {
				DTOTDopravnyBod dto = new DTOTDopravnyBod();
				Record r = (Record) iter.next();
				dto.setHistID(rVal(r, TDopravnyBodPeer.HIST_ID).asIntegerObj());
				dto.setPlatnostOd(rVal(r, TDopravnyBodPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, TDopravnyBodPeer.PLATNOST_DO).asUtilDate());
				dto.setCasVytvorenia(rVal(r, TDopravnyBodPeer.CAS_VYTVORENIA).asUtilDate());
				// dto.setCasZmeny(rVal(r, TDopravnyBodPeer.CAS_ZMENY).asUtilDate());
				dto.setIDZmena(rVal(r, TDopravnyBodPeer.ID_ZMENA).asIntegerObj());
				dto.setZmaz(rVal(r, TDopravnyBodPeer.ZMAZ).asString());
				dto.setDopravnyBodID(rVal(r, TDopravnyBodPeer.DOPRAVNY_BOD_ID).asIntegerObj());
				dto.setIDTypDopravne(rVal(r, TDopravnyBodPeer.ID_TYP_DOPRAVNE).asIntegerObj());
				dto.setIDDopravnyNazov(rVal(r, TDopravnyBodPeer.ID_DOPRAVNY_NAZOV).asIntegerObj());
				dto.setIDDopravnyBod(rVal(r, TDopravnyBodPeer.ID_DOPRAVNY_BOD).asIntegerObj());
				dto.setIDDefinicnyUsek(rVal(r, TDopravnyBodPeer.ID_DEFINICNY_USEK).asIntegerObj());
				dto.setIDOblastneRiaditelstvo(rVal(r, TDopravnyBodPeer.ID_OBLASTNE_RIADITELSTVO).asIntegerObj());
				dto.setIDDopravnyNazovZpps(rVal(r, TDopravnyBodPeer.ID_DOPRAVNY_NAZOV_ZPPS).asIntegerObj());
				dto.setCislo(rVal(r, TDopravnyBodPeer.CISLO).asString());
				dto.setNazov(rVal(r, TDopravnyBodPeer.NAZOV).asString());
				dto.setObsadenie(rVal(r, TDopravnyBodPeer.OBSADENIE).asString());
				dto.setKmPoloha1(rVal(r, TDopravnyBodPeer.KM_POLOHA_1).asDoubleObj());
				dto.setPps(rVal(r, TDopravnyBodPeer.PPS).asString());
				dto.setCestovnePoriadky(rVal(r, TDopravnyBodPeer.CESTOVNE_PORIADKY).asString());
				dto.setLenPreIs(rVal(r, TDopravnyBodPeer.LEN_PRE_IS).asString());
				dto.setKmPoloha2(rVal(r, TDopravnyBodPeer.KM_POLOHA_2).asDoubleObj());
				dto.setSkratka2(rVal(r, TDopravnyBodPeer.SKRATKA_2).asString());
				dto.setSkratka4(rVal(r, TDopravnyBodPeer.SKRATKA_4).asString());
				dto.setIDTrat1(rVal(r, TDopravnyBodPeer.ID_TRAT_1).asIntegerObj());
				dto.setIDTrat2(rVal(r, TDopravnyBodPeer.ID_TRAT_2).asIntegerObj());
				dto.setIDTrat3(rVal(r, TDopravnyBodPeer.ID_TRAT_3).asIntegerObj());
				dto.setIDTrat4(rVal(r, TDopravnyBodPeer.ID_TRAT_4).asIntegerObj());
				dto.setIDTrat5(rVal(r, TDopravnyBodPeer.ID_TRAT_5).asIntegerObj());
				dto.setKmPoloha3(rVal(r, TDopravnyBodPeer.KM_POLOHA_3).asDoubleObj());
				dto.setKmPoloha4(rVal(r, TDopravnyBodPeer.KM_POLOHA_4).asDoubleObj());
				dto.setKmPoloha5(rVal(r, TDopravnyBodPeer.KM_POLOHA_5).asDoubleObj());
				dto.setIDVyssiUzemnyCelok(rVal(r, TDopravnyBodPeer.ID_VYSSI_UZEMNY_CELOK).asIntegerObj());
				dto.setIDKategoriaDbOd(rVal(r, TDopravnyBodPeer.ID_KATEGORIA_DB_OD).asIntegerObj());
				dto.setIDKategoriaDbNd(rVal(r, TDopravnyBodPeer.ID_KATEGORIA_DB_ND).asIntegerObj());
				dto.setGpsSirka(rVal(r, TDopravnyBodPeer.GPS_SIRKA).asDoubleObj());
				dto.setGpsDlzka(rVal(r, TDopravnyBodPeer.GPS_DLZKA).asDoubleObj());
				dto.setMapaPolohaX(rVal(r, TDopravnyBodPeer.MAPA_POLOHA_X).asIntegerObj());
				dto.setMapaPolohaY(rVal(r, TDopravnyBodPeer.MAPA_POLOHA_Y).asIntegerObj());
				dto.setTtp(rVal(r, TDopravnyBodPeer.TTP).asString());
				dto.setEmail(rVal(r, TDopravnyBodPeer.EMAIL).asString());
				dto.setIDStavDopravy(rVal(r, TDopravnyBodPeer.ID_STAV_DOPRAVY).asIntegerObj());
				dto.setIDObec(rVal(r, TDopravnyBodPeer.ID_OBEC).asIntegerObj());
				dto.setMimoObec(rVal(r, TDopravnyBodPeer.MIMO_OBEC).asString());
				dto.setIDPristupKObjektu(rVal(r, TDopravnyBodPeer.ID_PRISTUP_K_OBJEKTU).asIntegerObj());
				dto.setIDStavObjektuBudova(rVal(r, TDopravnyBodPeer.ID_STAV_OBJEKTU_BUDOVA).asIntegerObj());
				dto.setIDStavObjektuCakaren(rVal(r, TDopravnyBodPeer.ID_STAV_OBJEKTU_CAKAREN).asIntegerObj());
				dto.setPristresok(rVal(r, TDopravnyBodPeer.PRISTRESOK).asString());
				dto.setPristupPrm(rVal(r, TDopravnyBodPeer.PRISTUP_PRM).asString());
				dto.setObmedzeniePrm(rVal(r, TDopravnyBodPeer.OBMEDZENIE_PRM).asString());
				dto.setPomocPrm(rVal(r, TDopravnyBodPeer.POMOC_PRM).asString());
				dto.setPomocPrmMin(rVal(r, TDopravnyBodPeer.POMOC_PRM_MIN).asIntegerObj());
				dto.setIDDopravnyBodPrm1(rVal(r, TDopravnyBodPeer.ID_DOPRAVNY_BOD_PRM_1).asIntegerObj());
				dto.setIDDopravnyBodPrm2(rVal(r, TDopravnyBodPeer.ID_DOPRAVNY_BOD_PRM_2).asIntegerObj());
				dto.setStanicaPrmPoznamka(rVal(r, TDopravnyBodPeer.STANICA_PRM_POZNAMKA).asString());
				dto.setKontaktPrm(rVal(r, TDopravnyBodPeer.KONTAKT_PRM).asString());
				dto.setInaDraha(rVal(r, TDopravnyBodPeer.INA_DRAHA).asString());
				dto.setTelefonZts(rVal(r, TDopravnyBodPeer.TELEFON_ZTS).asString());
				dto.setTelefonVts(rVal(r, TDopravnyBodPeer.TELEFON_VTS).asString());
				dto.setIDTratovyUsek1(rVal(r, TDopravnyBodPeer.ID_TRATOVY_USEK_1).asIntegerObj());
				dto.setIDTratovyUsek2(rVal(r, TDopravnyBodPeer.ID_TRATOVY_USEK_2).asIntegerObj());
				dto.setIDTratovyUsek3(rVal(r, TDopravnyBodPeer.ID_TRATOVY_USEK_3).asIntegerObj());
				dto.setIDTratovyUsek4(rVal(r, TDopravnyBodPeer.ID_TRATOVY_USEK_4).asIntegerObj());
				dto.setIDTratovyUsek5(rVal(r, TDopravnyBodPeer.ID_TRATOVY_USEK_5).asIntegerObj());
				dto.setZeleznicaStanica(rVal(r, TDopravnyBodPeer.ZELEZNICA_STANICA).asString());
				dto.setNastupiste(rVal(r, TDopravnyBodPeer.NASTUPISTE).asString());

				dto.setIDNadradenaPrimarna(rVal(r, TDopravnyBodPeer.ID_NADRADENA_PRIMARNA).asIntegerObj());
				dto.setCrd(rVal(r, TDopravnyBodPeer.CRD).asString());
				dto.setCrdZac(rVal(r, TDopravnyBodPeer.CRD_ZAC).asUtilDate());
				dto.setCrdKon(rVal(r, TDopravnyBodPeer.CRD_KON).asUtilDate());
				dto.setIDSubsidiaryType(rVal(r, TDopravnyBodPeer.ID_SUBSIDIARY_TYPE).asIntegerObj());
				dto.setIDCompany(rVal(r, TDopravnyBodPeer.ID_COMPANY).asIntegerObj());
				dto.setManipulaciaSKontajnermi(rVal(r, TDopravnyBodPeer.MANIPULACIA_S_KONTAJNERMI).asString());
				dto.setOtvorenyPreOd(rVal(r, TDopravnyBodPeer.OTVORENY_PRE_OD).asString());
				dto.setOtvorenyPreOdZac(rVal(r, TDopravnyBodPeer.OTVORENY_PRE_OD_ZAC).asUtilDate());
				dto.setOtvorenyPreOdKon(rVal(r, TDopravnyBodPeer.OTVORENY_PRE_OD_KON).asUtilDate());

				dto.setOtvorenyPreNd(rVal(r, TDopravnyBodPeer.OTVORENY_PRE_ND).asString());
				dto.setOtvorenyPreNdZac(rVal(r, TDopravnyBodPeer.OTVORENY_PRE_ND_ZAC).asUtilDate());
				dto.setOtvorenyPreNdKon(rVal(r, TDopravnyBodPeer.OTVORENY_PRE_ND_KON).asUtilDate());
				dto.setPoznamka(rVal(r, TDopravnyBodPeer.POZNAMKA).asString());
				dto.setUlica(rVal(r, TDopravnyBodPeer.ULICA).asString());
				dto.setOrientacneCislo(rVal(r, TDopravnyBodPeer.ORIENTACNE_CISLO).asString());
				dto.setMestoPsc(rVal(r, TDopravnyBodPeer.MESTO_PSC).asString());
				dto.setPs(rVal(r, TDopravnyBodPeer.PS).asString());
				dto.setStykDrah(rVal(r, TDopravnyBodPeer.STYK_DRAH).asString());
				return dto;
				// list.add(dto);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getDopravnyBodByZmenaId.error", auth);
			return null;
		}

	}

	public String getDopravnyBodCisloById(AuthInfo auth, Integer idDB, Date platnostOd) throws AppException {
		try {

			MyCriteria2 crit = new MyCriteria2(TDopravnyBodPeer.HIST_ID, new DTOTDopravnyBod());
			crit.addSelectColumn(TDopravnyBodPeer.CISLO);
			crit.addConditional(TDopravnyBodPeer.DOPRAVNY_BOD_ID, idDB);
			crit.addConditional(TDopravnyBodPeer.ZMAZ, "F");
			String s = TDopravnyBodPeer.PLATNOST_OD + " <=  " + CudVysielanieUtils.dateCritFormat(platnostOd)
					+ " AND ( " + TDopravnyBodPeer.PLATNOST_DO + " is null OR " + TDopravnyBodPeer.PLATNOST_DO + " <= "
					+ CudVysielanieUtils.dateCritFormat(platnostOd) + ")";
			crit.addCustomSql(TDopravnyBodPeer.PLATNOST_OD, s);
			String sql = crit.getSQL() + " ORDER BY " + TDopravnyBodPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				String cislo = rVal(r, TDopravnyBodPeer.CISLO).asString();
				// dto.setNazov(rVal(r, TDopravnyBodPeer.NAZOV).asString());
				return cislo;
				// list.add(dto);
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getDopravnyBodCisloById.error", auth);
			return null;
		}

	}

	public ArrayList<DTOTSubsidiaryLocation> getSubLocByZmenaId(AuthInfo auth, Integer zmenaID) throws AppException {
		try {
			// Systém vráti záznam z CUD_CISELNIK.TABULKA pre CUD_CISELNIK.CISELNIK_ID = vst. CiselnikID kde
			// ID_ZMENA = vst. ZmenaID

			MyCriteria2 crit = new MyCriteria2(TSubsidiaryLocationPeer.HIST_ID);
			TSubsidiaryLocationPeer.addSelectColumns(crit);
			crit.addConditional(TSubsidiaryLocationPeer.ID_ZMENA, zmenaID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			ArrayList<DTOTSubsidiaryLocation> list = new ArrayList<DTOTSubsidiaryLocation>();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				list.add(getDtoSubsidiaryLocation(r));
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubLocByZmenaId.error", auth);
			return null;
		}
	}

	public ArrayList<DTOTPrimaryLocation> getPrimLocByZmenaId(AuthInfo auth, Integer zmenaID) throws AppException {
		try {
			// Systém vráti záznam z CUD_CISELNIK.TABULKA pre CUD_CISELNIK.CISELNIK_ID = vst. CiselnikID kde
			// ID_ZMENA = vst. ZmenaID

			MyCriteria2 crit = new MyCriteria2(TPrimaryLocationPeer.HIST_ID);
			TPrimaryLocationPeer.addSelectColumns(crit);
			crit.addConditional(TPrimaryLocationPeer.ID_ZMENA, zmenaID);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			ArrayList<DTOTPrimaryLocation> list = new ArrayList<DTOTPrimaryLocation>();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				list.add(getDtoPrimaryLoc(r));
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getPrimLocByZmenaId.error", auth);
			return null;
		}
	}

	public DTOZmenaStlpec getZmenaStlpecByZmenaId(AuthInfo auth, Integer zmenaID, String nazov) throws AppException {
		ArrayList<DTOZmenaStlpec> list = getZmenaStlpecByZmenaId(auth, null, zmenaID, nazov);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	public ArrayList<DTOZmenaStlpec> getZmenaStlpecByZmenaId(AuthInfo auth, Integer idCiselnik, Integer zmenaID,
			String nazov) throws AppException {
		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID);
			CudZmenaStlpecPeer.addSelectColumns(crit);
			crit.addSelectColumn(CudCiselnikStlpecPeer.NAZOV);
			crit.addConditional(CudZmenaStlpecPeer.ID_ZMENA, zmenaID);
			crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK, idCiselnik);
			crit.addConditional(CudCiselnikStlpecPeer.NAZOV, nazov);
			crit.addJoin(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			ArrayList<DTOZmenaStlpec> list = new ArrayList<DTOZmenaStlpec>();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmenaStlpec dto = new DTOZmenaStlpec();
				dto.setZmenaStlpecID(rVal(r, CudZmenaStlpecPeer.ZMENA_STLPEC_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaStlpecPeer.ID_CISELNIK).asIntegerObj());
				dto.setIDZmena(rVal(r, CudZmenaStlpecPeer.ID_ZMENA).asIntegerObj());
				dto.setIDCiselnikStlpec(rVal(r, CudZmenaStlpecPeer.ID_CISELNIK_STLPEC).asIntegerObj());
				dto.setOldValue(rVal(r, CudZmenaStlpecPeer.OLD_VALUE).asString());
				dto.setNewValue(rVal(r, CudZmenaStlpecPeer.NEW_VALUE).asString());
				dto.setCiselnikStlpecNazov(rVal(r, CudCiselnikStlpecPeer.NAZOV).asString());

				list.add(dto);
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getZmenaStlpecByZmenaId.error", auth);
			return null;
		}
	}

	public DTOTSubsidiaryLocation getSubLocByPlatnostOd(AuthInfo auth, String locationCode, Integer idCountry,
			Date platnostOd) throws AppException {
		try {
			// Popis: Systém vráti všetky záznamy T_SUBSIDIARY_LOCATION platné k rozsahu dátumov.
			// Algoritmus:
			// Systém vráti záznam z T_SUBSIDIARY_LOCATION kde:
			// LOCATION_CODE = vstup:locationCode a zároveň
			// ID_COUNTRY= vstup:CountryID a zároveň
			// (PLATNOST_OD < PLATNOST_DO
			// A zároveň
			// (PLATNOST_DO je NULL Alebo PLATNOST_DO >= vstup:DátumOd)
			// A zároveň
			// ZMAZ='F'

			// crit.addConditional(TSubsidiaryLocationPeer.HIST_ID, dtoF.getHistID());
			MyCriteria2 crit = new MyCriteria2(TSubsidiaryLocationPeer.TABLE_NAME, new DTOTSubsidiaryLocation());
			TSubsidiaryLocationPeer.addSelectColumns(crit);
			crit.addConditional(TSubsidiaryLocationPeer.SUBSIDIARY_LOCATION_CODE, locationCode);
			// crit.addConditional(TSubsidiaryLocationPeer.ID_COMPANY, idCompany);
			crit.addConditional(TSubsidiaryLocationPeer.ID_COUNTRY, idCountry);
			// crit.addConditional(TSubsidiaryLocationPeer.ID_PRIMARY_LOCATION, idPrimLoc);
			// crit.addConditional(TSubsidiaryLocationPeer.ID_SUBSIDIARY_TYPE, idSubType);
			crit.addConditional(TSubsidiaryLocationPeer.ZMAZ, "F");
			String s = TSubsidiaryLocationPeer.PLATNOST_OD + " <  " + TSubsidiaryLocationPeer.PLATNOST_DO + " AND ( "
					+ TSubsidiaryLocationPeer.PLATNOST_DO + " is null OR " + TSubsidiaryLocationPeer.PLATNOST_DO
					+ " <=  " + CudVysielanieUtils.dateCritFormat(platnostOd) + ")";
			crit.addCustomSql(TSubsidiaryLocationPeer.PLATNOST_OD, s);
			String sql = crit.getSQL() + " ORDER BY " + TSubsidiaryLocationPeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				DTOTSubsidiaryLocation dto = new DTOTSubsidiaryLocation();
				Record r = (Record) iter.next();

				getDtoSubsidiaryLocation(r);

				return dto;
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubsidiaryLocation(.error", auth);
			return null;
		}
	}

	public String lookupVyssiUzemnyCelokOznacenie(AuthInfo auth, Integer id) throws AppException {
		try {
			// Systém vráti záznam z CUD_CISELNIK.TABULKA pre CUD_CISELNIK.CISELNIK_ID = vst. CiselnikID kde
			// ID_ZMENA = vst. ZmenaID
			// AK T_DOPRAVNY_BOD.(ID_VYSSI_UZEMNY_CELOK).T_VYSSI_UZEMNY_CELOK.OZNACENIE_SUSR = "BL-1-SK010"
			// (Bratislavský samosprávny kraj) TAK "SK010"
			// AK T_DOPRAVNY_BOD.(ID_VYSSI_UZEMNY_CELOK).T_VYSSI_UZEMNY_CELOK.OZNACENIE_SUSR = "BC-6-SK032"
			// (Banskobystrický samosprávny kraj) TAK "SK032"
			// AK T_DOPRAVNY_BOD.(ID_VYSSI_UZEMNY_CELOK).T_VYSSI_UZEMNY_CELOK.OZNACENIE_SUSR = "KI-8-SK042" (Košický
			// samosprávny kraj) TAK "SK042"
			// AK T_DOPRAVNY_BOD.(ID_VYSSI_UZEMNY_CELOK).T_VYSSI_UZEMNY_CELOK.OZNACENIE_SUSR = "PV-7-SK041" (Prešovský
			// samosprávny kraj) TAK "SK023"
			// AK T_DOPRAVNY_BOD.(ID_VYSSI_UZEMNY_CELOK).T_VYSSI_UZEMNY_CELOK.OZNACENIE_SUSR = "NI-4-SK023" (Nitriansky
			// samosprávny kraj) TAK "SK041"
			// AK T_DOPRAVNY_BOD.(ID_VYSSI_UZEMNY_CELOK).T_VYSSI_UZEMNY_CELOK.OZNACENIE_SUSR = "TC-3-SK022" (Trenèiansky
			// samosprávny kraj) TAK "SK022"
			// AK T_DOPRAVNY_BOD.(ID_VYSSI_UZEMNY_CELOK).T_VYSSI_UZEMNY_CELOK.OZNACENIE_SUSR = "TA-2-SK021" (Trnavský
			// samosprávny kraj) TAK "SK021"
			// AK T_DOPRAVNY_BOD.(ID_VYSSI_UZEMNY_CELOK).T_VYSSI_UZEMNY_CELOK.OZNACENIE_SUSR = "ZI-5-SK031" (Žilinský
			// samosprávny kraj) TAK "SK031"

			MyCriteria2 crit = new MyCriteria2(TVyssiUzemnyCelokPeer.HIST_ID);
			TVyssiUzemnyCelokPeer.addSelectColumns(crit);
			crit.addConditional(TVyssiUzemnyCelokPeer.VYSSI_UZEMNY_CELOK_ID, id);
			crit.addConditional(TVyssiUzemnyCelokPeer.ZMAZ, "F");
			// crit.addDescendingOrderByColumn(TVyssiUzemnyCelokPeer.HIST_ID);

			String sql = crit.getSQL() + " order by " + TVyssiUzemnyCelokPeer.HIST_ID + " DESC";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				String nazov = rVal(r, TVyssiUzemnyCelokPeer.OZNACENIE_SUSR).asString();
				int pozicia = nazov.indexOf("SK");
				if (pozicia > 0) {
					return nazov.substring(pozicia);
				}
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.lookupVyssiUzemnyCelokOznacenie.error", auth);
			return null;
		}
	}

	public Integer getIdCiselnik(AuthInfo auth, String tabulka) throws AppException {

		try {
			if (!StringUtils.isValid(tabulka)) {
				return null;
			}

			DTOCiselnik dtoF = new DTOCiselnik();
			dtoF.setTabulka(tabulka);
			_CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
			DTOCiselnik[] listDTO = dlgcud.getCiselnikRead().listLight(auth, dtoF);

			if (StringUtils.isValid(listDTO)) {
				return listDTO[0].getCiselnikID();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "getIdCiselnik.error", auth);
			return null;
		}
	}

	public String getSubsidiaryType(AuthInfo auth, Integer idSubsidiaryType) throws AppException {
		try {

			MyCriteria2 crit = new MyCriteria2(TSubsidiaryTypePeer.HIST_ID);
			crit.addSelectColumn(TSubsidiaryTypePeer.SUBSIDIARY_TYPE_CODE);
			crit.addConditional(TSubsidiaryTypePeer.SUBSIDIARY_TYPE_ID, idSubsidiaryType);
			crit.addConditional(TSubsidiaryTypePeer.ZMAZ, "F");
			// crit.getNewCriterion(TCountryPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);/
			crit.addCustomSql(TSubsidiaryTypePeer.PLATNOST_DO, TSubsidiaryTypePeer.PLATNOST_DO + " is null ");
			String sql = crit.getSQL() + " ORDER BY " + TSubsidiaryTypePeer.HIST_ID + " DESC ";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				String type = rVal(r, TSubsidiaryTypePeer.SUBSIDIARY_TYPE_CODE).asString();
				return type;
			}
			return null;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubsidiaryType.error", auth);
			return null;
		}
	}

	public ArrayList<DTOTPrimaryLocation> getPrimaryLocationList(AuthInfo auth, String locationCode, String countryIso)
			throws AppException {

		// Systém vyhľadá záznam v T_PRIMARY_LOCATION, kde
		//
		// .LOCATION_CODE = locationCode a zároveň
		// .ID_COUNTRY= vst. CountryID a zároveň

		// .ZMAZ = False a zároveň
		// .PLATNOST_DO is null

		try {
			String sql = " SELECT   pl.* FROM    t_primary_location pl "
					// + " left join T_COMPANY lc on lc.company_id=pl.id_company "
					+ " left join T_COUNTRY py on pl.id_country=py.country_id "

					+ " WHERE  pl.PLATNOST_DO is null " + " and pl.zmaz='F' "
					// + " and lc.PLATNOST_DO is null " + " and lc.zmaz='F' "
					+ " and py.PLATNOST_DO is null " + " and py.zmaz='F'" + " and pl.location_code = '" + locationCode
					+ "'"

					// + " and lc.Company_uic_code='"+ companyUicCode + "'"
					+ " and py.country_code_iso ='" + countryIso + "'";

			// if (validityEquals) {
			// sql += " AND pl.START_VALIDITY =" + CudVysielanieUtils.dateCritFormat(startValidity);
			// // } else {
			// // // -- Dátumová platnosť
			// // sql += " AND pl.START_VALIDITY < " + CudVysielanieUtils.dateCritFormat(startValidity) + " AND ("
			// // + " pl.END_VALIDITY IS NULL " + "  OR pl.END_VALIDITY >= "
			// // + CudVysielanieUtils.dateCritFormat(startValidity) + " )";
			// // }
			// }

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOTPrimaryLocation> list = new ArrayList<DTOTPrimaryLocation>();
			while (iter.hasNext()) {
				// DTOTPrimaryLocation dto = new DTOTPrimaryLocation();
				Record r = (Record) iter.next();

				list.add(getDtoPrimaryLoc(r));
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getPrimaryLocationList.error", auth);
			return null;
		}
	}

	public ArrayList<DTOTSubsidiaryLocation> getSubsidiaryLocationList(AuthInfo auth, String subLocCode,
			String primLocCode,
			String subTypeCode, String countryIso, String companyUicCode) throws AppException {
		try {
			// Systém vyhľadá záznam v T_SUBSIDIARY_LOCATION, kde
			// .SUBSIDIARY_LOCATION_CODE = vst. SubsidiaryLocationCode a zároveň
			// .ID_SUBSIDIARY_TYPE = vst. SubsidiaryTypeID a zároveň
			// .START_VALIDITY< vst. StartValidity a zároveň
			//
			// (END_VALIDITY is null alebo END_VALIDITY >=vst. StartValidity) a zároveň
			//
			//
			// .ID_COMPANY.COMPANY_UIC_CODE = vst. companyUICCode a zároveň
			// .ID_COMPANY.PLATNOST_DO is null
			//
			// .ID_COUNTRY.COUNTRY_CODE_ISO = vst. CountryCodeISO a zároveň
			// .ID_COUNTRY.PLATNOST_DO is null
			//
			// .ID_PRIMARY_LOCATION.LOCATION_CODE = vst. PrimaryLocation.LOCATION_CODE a zároveň
			// .ID_PRIMARY_LOCATION.ID_COUNTRY = vst. PrimaryLocation.ID_COUNTRY
			// .ID_PRIMARY_LOCATION.PLATNOST_DO is null
			//
			//
			// .ZMAZ = False a zároveň
			// .PLATNOST_DO is null

			String sql = " select l.* 	from T_SUBSIDIARY_LOCATION l "
					+ " left join T_COUNTRY ly on ly.country_id=l.id_country "
					+ " left join T_SUBSIDIARY_TYPE lt on lt.SUBSIDIARY_TYPE_ID=l.id_Subsidiary_type "
					+ " left join T_COMPANY lc on lc.company_id=l.id_company "
					+ " left join T_PRIMARY_LOCATION p on p.primary_location_id=l.id_primary_location "
					+ " left join T_COUNTRY py on p.id_country=py.country_id " + " where  " + " l.PLATNOST_DO is null "
					+ " and l.zmaz='F' " + " and ly.PLATNOST_DO is null " + " and ly.zmaz='F' "
					+ " and lt.PLATNOST_DO is null " + " and lt.zmaz='F' " + " and lc.PLATNOST_DO is null "
					+ " and lc.zmaz='F' " + " and p.PLATNOST_DO is null " + " and p.zmaz='F' "
					+ " and py.PLATNOST_DO is null " + " and py.zmaz='F' ";

			// if (validityEquals) {
			// sql += " and (l.End_validity is null or l.End_validity= "
			// + CudVysielanieUtils.dateCritFormat(startValidity) + ")";
			// } else {
			// // -- Dátumová platnosť
			// sql += " and (l.start_validity is null or l.start_validity<="
			// + CudVysielanieUtils.dateCritFormat(startValidity) + ")"
			// + "and (l.End_validity is null or l.End_validity>="
			// + CudVysielanieUtils.dateCritFormat(startValidity) + ")";
			// }

			sql += " and l.SUBSIDIARY_LOCATION_CODE='" + subLocCode + "'" + " and ly.country_code_iso='" + countryIso
					+ "'" + " and lt.subsidiary_type_code='" + subTypeCode + "'"
					// + " and lc.Company_uic_code='" + companyUicCode + "'"
					+ " and p.LOCATION_CODE='" + primLocCode + "'"
					+ " and py.country_code_iso ='" + countryIso + "'";

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);
			Iterator<?> iter = lp.iterator();
			ArrayList<DTOTSubsidiaryLocation> list = new ArrayList<DTOTSubsidiaryLocation>();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				DTOTSubsidiaryLocation dto = getDtoSubsidiaryLocation(r);
				list.add(dto);
			}
			return list;

		} catch (Throwable t) {
			handleException(t, "TCudCiselnikyClass.getSubsidiaryLocationList.error", auth);
			return null;
		}
	}
}
