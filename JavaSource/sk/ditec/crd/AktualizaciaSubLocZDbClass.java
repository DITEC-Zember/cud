package sk.ditec.crd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.DTO;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.crd.dto.DTOTCompany;
import sk.ditec.crd.dto.DTOTCountry;
import sk.ditec.crd.dto.DTOTDopravnyBod;
import sk.ditec.crd.dto.DTOTPrimaryLocation;
import sk.ditec.crd.dto.DTOTStanicnaKolaj;
import sk.ditec.crd.dto.DTOTSubsidiaryLocation;
import sk.ditec.crd.dto.DTOTSubsidiaryType;
import sk.ditec.crd.dto.DTOZmenaStlpecCrd;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.proc.CudPauClass;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.zsr.common.server._NovyPISBaseClass;
import sk.ditec.zsr.common.server.utils.DateUtils;

public class AktualizaciaSubLocZDbClass extends _NovyPISBaseClass {

	private Logger log = LoggerFactory.getLogger(AktualizaciaSubLocZDbClass.class);

	private _CudCrdDelegate dlg = new _CudCrdDelegate();
	private _CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private CudPauClass cudPau = new CudPauClass();
	private static String StringdateFormat = "dd.MM.yyyy HH:mm:ss";
	private static DateFormat dateFormat = new SimpleDateFormat(StringdateFormat, new Locale("sk"));

	private class DtoRow extends DTO {
		boolean isKolaj;
		boolean isDb;
		Date crdZac;
		Date crdKon;
		Date platnostOd;
		Date platnostDo;
		String zmaz;

		public String getZmaz() {
			return zmaz;
		}

		public void setZmaz(String zmaz) {
			this.zmaz = zmaz;
		}

		public boolean isKolaj() {
			return isKolaj;
		}

		public void setKolaj(boolean isKolaj) {
			this.isKolaj = isKolaj;
		}

		public boolean isDb() {
			return isDb;
		}

		public void setDb(boolean isDb) {
			this.isDb = isDb;
		}

		public Date getCrdZac() {
			return crdZac;
		}

		public void setCrdZac(Date crdZac) {
			this.crdZac = crdZac;
		}

		public Date getCrdKon() {
			return crdKon;
		}

		public void setCrdKon(Date crdKon) {
			this.crdKon = crdKon;
		}

		public Date getPlatnostOd() {
			return platnostOd;
		}

		public void setPlatnostOd(Date platnostOd) {
			this.platnostOd = platnostOd;
		}

		public Date getPlatnostDo() {
			return platnostDo;
		}

		public void setPlatnostDo(Date platnostDo) {
			this.platnostDo = platnostDo;
		}

	}

	private DtoRow getDtoRow(DTOTDopravnyBod dtoDataRowDb, DTOTStanicnaKolaj dtoDataRowKolaj) {
		DtoRow dto = new DtoRow();
		if (dtoDataRowDb != null) {
			dto.setCrdZac(dtoDataRowDb.getCrdZac());
			dto.setCrdKon(dtoDataRowDb.getCrdKon());
			dto.setPlatnostOd(dtoDataRowDb.getPlatnostOd());
			dto.setPlatnostDo(dtoDataRowDb.getPlatnostDo());
			dto.setZmaz(dtoDataRowDb.getZmaz());
			return dto;
		} else if (dtoDataRowKolaj != null) {
			dto.setCrdZac(dtoDataRowKolaj.getCrdZac());
			dto.setCrdKon(dtoDataRowKolaj.getCrdKon());
			dto.setPlatnostOd(dtoDataRowKolaj.getPlatnostOd());
			dto.setPlatnostDo(dtoDataRowKolaj.getPlatnostDo());
			dto.setZmaz(dtoDataRowKolaj.getZmaz());
			return dto;
		}
		return null;
	}

	public ActionResult aktualizujSubLoc(AuthInfo auth, DTOZmena zmena, String vsupOperacia, Date vstupPlatnostOd, DTOTDopravnyBod dataRowDb, DTOTStanicnaKolaj dataRowKolaj, DTOTCountry tCountry, DTOTCompany tCompany, Integer idPrimLoc, Integer idSubLoc) throws AppException {

		String chyba = "";
		ActionResult res = new ActionResult();
		Integer $SubsidiaryType = null;
		String $LocationCode = null;
		Integer $CompanyId = null;
		Integer $IdDbNadradena = null;
		String $Nazov = null;
		Double $Latitude = null;
		Double $Longitude = null;
		Date $CrdZac = null;
		Date $CrdKon = null;
		String $Poznamka = null;
		Date $DatumOd = null;
		String $NadradenyDB = null;
		Integer $OldSubsidiaryType = null;
		String $OldLocationCode = null;
		Integer $OldIdDBNadradena = null;
		String $OldNadradenyDB = null;
		Integer $OldCompany = null;
		String $OldUICCode = null;
		Integer $OldIdNadradena = null;
		// 7.3. SpracujOldDB=false
		boolean spracujOldDB = false;
		// SpracujNewDB=false
		boolean spracujNewDB = false;
		// $ZmenaIdentifikacie=false
		boolean $ZmenaIdentifikacie = false;
		String $Operacia = vsupOperacia;

		try {
			// 2. UICZSR='0056'
			// String uiczsr = "0056";
			// $datumNow=aktuálny dátum
			Date $datumNow = new Date();

			// 3. /*CompanyZsr*/ - posielam ako parameter v tcompany
			// Systém nájde záznam pre UIC ZSR platný k dátumu $dataRow.PLATNOST_OD

			// 4. Systém nastaví Operácia=$Vst_Operacia pride ako parameter vsupOperacia

			// 5. Systém nastaví
			// PLATNOST_OD=$Vst_PLATNOST_OD
			Date platnostOd = vstupPlatnostOd;
			// 5.1. Ak $Zmena nie je null
			// Systém nastaví
			// Operácia=$Zmena.OPERACIA
			// PLATNOST_OD=$Zmena.PLATNOST_OD
			if (zmena != null) {
				$Operacia = zmena.getOperacia();
				platnostOd = zmena.getPlatnostOd();
			}

			DtoRow dataRow = getDtoRow(dataRowDb, dataRowKolaj);
			// 6.IDENTIFIKACIA CRD
			// NewJeCR=false
			boolean newCRD = false;
			// /*$CRD_ZACStlpec:CUD_ZMENA_STLPEC*/
			// Systém nájde $Ståpec v $Zmena, kde $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV
			// = CRD_ZAC
			DTOZmenaStlpecCrd dtoZmenaStlpcaCrdZac = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(), "CRD_ZAC");
			// 6.2. Ak $dataRow.CRD_ZAC nie je null
			// NewJeCR=true
			if (dataRow.getCrdZac() != null) {
				newCRD = true;
			}
			// 5.3.4. OldCRD=NewJeCRD
			// 6.3. Ak $Zmena nie je null
			boolean oldCRD = newCRD;
			if (zmena != null) {

				// /*$CRD_KONStlpec:CUD_ZMENA_STLPEC*/
				// Systém nájde $Ståpec v $Zmena, kde
				// $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV = CRD_KON
				DTOZmenaStlpecCrd dtoZmenaStlpcaCrdKon = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(), "CRD_KON");
				// 6.3.3. Ak dataRow.CRD_KON is not null and dataRow.CRD_KON<= dataRow.PLATNOST_OD and $CRD_KONStlpec je
				// null
				// NAVRAT
				if (dataRow.getCrdKon() != null && dataRow.getCrdKon().getTime() < dataRow.getPlatnostOd().getTime() && dtoZmenaStlpcaCrdKon == null) {
					// navrat
					return res;
				}
				// 6.3.5. Ak $CRD_ZACStlpec nie je null and $CRDStlpec.OLD_VALUE je null
				if (dtoZmenaStlpcaCrdZac != null && dtoZmenaStlpcaCrdZac.getOldValue() == null) {
					// OldCRD=false
					oldCRD = false;
				} // INAK $CRD_ZACStlpec nie je null and $CRDStlpec.OLD_VALUE nie je null
				else if (dtoZmenaStlpcaCrdZac != null && dtoZmenaStlpcaCrdZac.getOldValue() != null) {
					// OldCRD=true
					oldCRD = true;
				}
			} else { // end if (zmena!=null){
				// OldCRD=false
				oldCRD = false;
			}

			// 6.4. Ak NewJeCR =false and OldJeCRD =false
			if (!newCRD && !oldCRD) {
				// NESPRACOVÁVAM záznam
				return res;
			}
			// 7. NACITANIE HODNOT
			// 7.1. Ak $Èíselník = T_DOPRAVNY_BOD
			String $uicCode = null;
			if (dataRowDb != null) { // ked je zmena do DB a nie do kolaji
				// Systém nastaví
				// $SubsidiaryType=$dataRow.ID_SUBSIDIARY_TYPE
				// $LocationCode=$dataRow.CISLO
				// $Company=$dataRow.Id_COMPANY
				$SubsidiaryType = dataRowDb.getIDSubsidiaryType();
				$LocationCode = dataRowDb.getCislo().substring(0, 5);
				$CompanyId = dataRowDb.getIDCompany();
				// $IdDBNadradena=$dataRow.ID_NADRADENA_PRIMARNA
				$IdDbNadradena = dataRowDb.getIDNadradenaPrimarna();
				// $Nazov=$dataRow.NAZOV
				$Nazov = dataRowDb.getNazov();
				// $Latitude=$dataRow.GPS_SIRKA
				$Latitude = dataRowDb.getGpsSirka();
				// $Longitude=$dataRow.GPS_DLZKA
				$Longitude = dataRowDb.getGpsDlzka();
				// $crd_zac=$dataRow.CRD_ZAC
				$CrdZac = dataRowDb.getCrdZac();
				// $crd_kon=$dataRow.CRD_KON
				$CrdKon = dataRowDb.getCrdKon();
				// $Poznamka=$dataRow.POZNAMKA
				$Poznamka = dataRowDb.getPoznamka();
				// $datumOd=PLATNOST_OD
				$DatumOd = platnostOd;
				// 7.1.1. Ak $Company<>null
				/* UICCode */
				if ($CompanyId != null) {
					// Systém nájde COMPANY_UIC_CODE pre poslednú verziu záznamu T_COMPANY.COMPANY_ID=$Company
					$uicCode = dlg.getTCudCiselnikyClass().getCompanyUicKodById(auth, $CompanyId);
					if ($uicCode == null) {
						chyba = " CHYBASUBLOC7: neexistuje platne uicCode  pre id=" + $CompanyId;
						res.setError(true);
						res.setErrorMsg(chyba);
						return res;
					}
				}

				// 7.1.2. /*$NadradenyDB*/
				// Systém vyh¾adá dopravnýBod kde DOPRAVNY_BOD_ID=$IdDBNadradena
				$NadradenyDB = dlg.getTCudCiselnikyClass().getDopravnyBodCisloById(auth, dataRowDb.getIDNadradenaPrimarna(), dataRowDb.getPlatnostOd());

				// AK $NadradenyDB je null
				// CHYBA "Dopravny bod id=$IdDBNadradena k dátumu %datumOd% neexistuje"
				if ($NadradenyDB == null) {
					chyba = " CHYBASUBLOC7: Dopravny bod id=" + dataRowDb.getIDNadradenaPrimarna() + " k dátumu " + dataRowDb.getPlatnostOd() + " neexistuje ";
					res.setError(true);
					res.setErrorMsg(chyba);
					return res;
				}
				$NadradenyDB = $NadradenyDB.substring(0, 5);
				// 7.1.3. Systém nastaví
				// $OldSubsidiaryType=$SubsidiaryType
				$OldSubsidiaryType = $SubsidiaryType;
				// $OldLocationCode=$LocationCode
				$OldLocationCode = $LocationCode;
				// $OldCompany=$Company
				$OldCompany = $CompanyId;
				// $OldUICCode=$UICCode
				$OldUICCode = $uicCode;
				// $OldIdDBNadradena=$IdDBNadradena
				$OldIdDBNadradena = $IdDbNadradena;
				// $OldNadradenyDB=$NadradenyDB
				$OldNadradenyDB = $NadradenyDB;

				// 7.1.3.1. Ak $Zmena nie je null
				if (zmena != null) {
					// /*$SubsidiaryTypeStlpec:CUD_ZMENA_STLPEC*/
					// Systém nájde $Ståpec v $Zmena, kde
					// $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV =ID_SUBSIDIARY_TYPE
					DTOZmenaStlpecCrd dtoCudZmenaStlpec = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(), "ID_SUBSIDIARY_TYPE");
					// AK $SubsidiaryTypeStlpec nie je null
					if (dtoCudZmenaStlpec != null && dtoCudZmenaStlpec.getOldValue() != null) {
						// $OldSubsidiaryType=$OldSubsidiaryTypeStlpec.OLD_VALUE
						$OldSubsidiaryType = Integer.valueOf(dtoCudZmenaStlpec.getOldValue());
					}
					// Systém nájde $Ståpec v $Zmena, kde
					// $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV =CISLO
					dtoCudZmenaStlpec = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(), "CISLO");
					// AK $LocationCodeStlpec nie je null
					if (dtoCudZmenaStlpec != null) {
						// $OldLocationCode=$LocationCodeStlpec.OLD_VALUE
						$OldLocationCode = dtoCudZmenaStlpec.getOldValue();
					}
					// /*$CompanyStlpec:CUD_ZMENA_STLPEC*/
					// Systém nájde $Ståpec v $Zmena, kde
					// $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV =ID_COMPANY

					dtoCudZmenaStlpec = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(), "ID_COMPANY");
					// AK $CompanyStlpec nie je null
					if (dtoCudZmenaStlpec != null && dtoCudZmenaStlpec.getOldValue() != null) {
						// $OldCompany=$CompanyStlpec.OLD_VALUE
						$OldCompany = Integer.valueOf(dtoCudZmenaStlpec.getOldValue());
						// AK $OldCompany nie je null
						// /*OldUICCode*/
						// Systém zistí COMPANY_UIC_CODE pre poslednú verziu záznamu T_COMPANY.COMPANY_ID=$OldCompany
						if ($OldCompany != null) {
							$OldUICCode = dlg.getTCudCiselnikyClass().getCompanyUicKodById(auth, $OldCompany);
							if ($OldUICCode == null) {
								chyba = " CHYBASUBLOC7: neexistuje platne $OldUICCode  pre id=" + $CompanyId;
								res.setError(true);
								res.setErrorMsg(chyba);
								return res;
							}
						}

					}
					// /*$IdNadradenaStlpec:CUD_ZMENA_STLPEC*/
					// Systém nájde $Ståpec v $Zmena, kde
					// $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV =ID_NADRADENA_PRIMARNA
					dtoCudZmenaStlpec = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(), "ID_NADRADENA_PRIMARNA");
					// AK $IdNadradenaStlpec nie je null
					if (dtoCudZmenaStlpec != null && dtoCudZmenaStlpec.getOldValue() != null) {
						// $OldIdNadradena=$IdNadradenaStlpec.OLD_VALUE
						$OldIdDBNadradena = Integer.valueOf(dtoCudZmenaStlpec.getOldValue());
						// /*$OldNadradenyDB*/
						// Systém vyh¾adá dopravnýBod kde DOPRAVNY_BOD_ID=$OldIdDBNadradena
						String OldNadradenyDB = dlg.getTCudCiselnikyClass().getDopravnyBodCisloById(auth, $OldIdDBNadradena, dataRowDb.getPlatnostOd());
						// AK $OldNadradenyDB je null
						// CHYBA "Dopravny bod ID=$OldIdDBNadradena k dátumu %datumOd% neexistuje"
						if (OldNadradenyDB == null) {
							chyba = "CHYBASUBLOC8: Dopravny bod id=" + $OldIdDBNadradena + " k dátumu " + dataRowDb.getPlatnostOd() + " neexistuje ";
							res.setError(true);
							res.setErrorMsg(chyba);
							return res;
						}
					}
				} // if (zmena!=null) {

			} // end if (dataRowDb!=null)
			else { // 7.1.4. ///KOLAJE//////
					// systém nastaví
					// $SubsidiaryType=ID_SUBSIDIARY_TYPE kde subsidiaryTypeCode=01 a záznam je platný
				DTOTSubsidiaryType tSubType = dlg.getTCudCiselnikyClass().getSubsidiaryType(auth, "01");
				$SubsidiaryType = tSubType.getSubsidiaryTypeID();
				// $LocationCode=$dataRow.CISLO
				$LocationCode = dataRowKolaj.getCislo();
				// $Nazov= ak trim($dataRow.NAZOV) null alebo prazdne tak $dataRow.CISLO INAK $dataRow.NAZOV '
				$Nazov = dataRowKolaj.getNazov();
				if (dataRowKolaj.getNazov() == null || "".equals(dataRowKolaj.getNazov())) {
					$Nazov = dataRowKolaj.getCislo();
				}
				$uicCode = _CudConsts.COMPANY_UIC_CODE_ZSR;
				$OldUICCode = _CudConsts.COMPANY_UIC_CODE_ZSR;
				// $IdDBNadradena=$dataRow.ID_DOPRAVNY_BOD
				$IdDbNadradena = dataRowKolaj.getIDDopravnyBod();
				// AK $NadradenyDB je null
				// CHYBA "Dopravny bod ID=$IdDBNadradena k dátumu %datumOd% neexistuje"
				$NadradenyDB = dlg.getTCudCiselnikyClass().getDopravnyBodCisloById(auth, dataRowKolaj.getIDDopravnyBod(), dataRowKolaj.getPlatnostOd());
				// AK $NadradenyDB je null
				// CHYBA "Dopravny bod id=$IdDBNadradena k dátumu %datumOd% neexistuje"
				if ($NadradenyDB == null) {
					chyba += "CHYBASUBLOC9: Dopravny bod id=" + dataRowKolaj.getIDDopravnyBod() + " k dátumu " + dataRowKolaj.getPlatnostOd() + " neexistuje ";
					;
					res.setError(true);
					res.setErrorMsg(chyba);
					return res;
				}
				$NadradenyDB = $NadradenyDB.substring(0, 5);
				// $Latitude=NULL
				$Latitude = null;
				// $Longitude=NULL
				$Longitude = null;
				// $crd_zac=$dataRow.CRD_ZAC
				$CrdZac = dataRowKolaj.getCrdZac();
				// $crd_kon=$dataRow.CRD_KON
				$CrdKon = dataRowKolaj.getCrdKon();
				// $Poznamka=$dataRow.POZNAMKA oreza na 255
				$Poznamka = dataRowKolaj.getPoznamka();
				if ($Poznamka != null && $Poznamka.length() > 255) {
					$Poznamka = $Poznamka.substring(0, 254);
				}
				// $datumOd=Platnost_OD
				$DatumOd = dataRowKolaj.getPlatnostOd();

				$OldSubsidiaryType = $SubsidiaryType;
				// $OldLocationCode=$LocationCode
				$OldLocationCode = $LocationCode;
				// $OldIdDBNadradena=$IdDBNadradena
				$OldIdDBNadradena = $IdDbNadradena;
				// $OldNadradenyDB=$NadradenyDB
				$OldNadradenyDB = $NadradenyDB;

				// 7.1.6.1 Ak $Zmena nie je null
				if (zmena != null) {
					// /*$LocationCodeStlpec:CUD_ZMENA_STLPEC*/
					// Systém nájde $Ståpec v $Zmena, kde
					// $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV =CISLO
					DTOZmenaStlpecCrd dtoCudZmenaStlpec = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(), "CISLO");
					// AK $LocationCodeStlpec nie je null
					if (dtoCudZmenaStlpec != null) {
						// $OldLocationCode=$LocationCodeStlpec.OLD_VALUE
						$OldLocationCode = dtoCudZmenaStlpec.getOldValue();
					}

					// /*$IdNadradenaStlpec:CUD_ZMENA_STLPEC*/
					// Systém nájde $Ståpec v $Zmena, kde
					// $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV =ID_DOPRAVNY_BOD
					dtoCudZmenaStlpec = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(), "ID_DOPRAVNY_BOD");
					// AK $IdNadradenaStlpec nie je null

					if (dtoCudZmenaStlpec != null && dtoCudZmenaStlpec.getOldValue() != null) {
						// // $OldIdNadradena=$IdNadradenaStlpec.OLD_VALUE
						$OldIdNadradena = Integer.valueOf(dtoCudZmenaStlpec.getOldValue());
						// /*$OldNadradenyDB*/
						// Systém vyh¾adá dopravnýBod kde DOPRAVNY_BOD_ID=$OldIdDBNadradena

						$OldNadradenyDB = dlg.getTCudCiselnikyClass().getDopravnyBodCisloById(auth, $OldIdNadradena, dataRowKolaj.getPlatnostOd());
						// AK $OldNadradenyDB je null
						// CHYBA "Dopravny bod ID=$OldIdDBNadradena k dátumu %datumOd% neexistuje"
						if ($OldNadradenyDB == null) {
							chyba += "CHYBASUBLOC10: Dopravny bod id=" + $OldIdNadradena + " k dátumu " + dataRowKolaj.getPlatnostOd() + " neexistuje ";
							res.setError(true);
							res.setErrorMsg(chyba);
							return res;
						}
						return res;
					}

				} // end if (zmena!=null) {
			}// end else if (dataRowDb!=null) -> tj Kolaj

			// ////////////////////////////////////////////////////////////////////////////////
			// 8. IDENTIFIKACIA PRIMARNEJ
			// 8.1. NewJePrimárna=NULL
			Boolean newJePrimarna = null;
			// AK
			// $SubsidiaryType = NULL a zároveò
			// $UICCode=UICZSR
			// NewJePrimárna=TRUE
			// INAK AK $SubsidiaryType <> NULL a zároveò
			// $UICCode= UICZSR
			// NewJePrimárna=false

			if ($SubsidiaryType == null && CudVysielanieUtils.isEqual($uicCode, _CudConsts.COMPANY_UIC_CODE_ZSR)) {
				newJePrimarna = true;
			}
			// INAK AK $SubsidiaryType <> NULL a zároveò
			// $Company = NewZSR.COMPANY_ID
			// NewJePrimárna=false
			else if ($SubsidiaryType != null && CudVysielanieUtils.isEqual($uicCode, _CudConsts.COMPANY_UIC_CODE_ZSR)) {
				newJePrimarna = false;
			}

			// 8.2. OldJePrimárna=NULL - null sposobi probemy pri zapise kolaji
			Boolean oldJePrimarna = false;
			// Ak
			// $OldSubsidiaryType <> null
			// $OldCompany= NewZsr.COMPANY_ID
			// OldJePrimárna=false
			if ($OldSubsidiaryType != null && CudVysielanieUtils.isEqual($OldCompany, tCompany.getCompanyID())) {
				oldJePrimarna = false;
			}
			// INAK AK
			// $OldSubsidiaryType= null
			// $OldCompany= NewZsr.COMPANY_ID
			// OldJePrimárna=true
			if ($OldSubsidiaryType == null && CudVysielanieUtils.isEqual($OldCompany, tCompany.getCompanyID())) {
				oldJePrimarna = true;
			}
			// 8.3 definicia vyssie
			// boolean spracujOldDB=false;
			// boolean spracujNewDB=false;
			// boolean sZmenaIdentifikacie=false;

			// 8.3.1. $OldLocationCode<>$LocationCode alebo
			// $OldIdDBNadradena<>$IdDBNadradena
			// $ZmenaIdentifikacie=true
			if (!CudVysielanieUtils.isEqual($OldLocationCode, $LocationCode) || !CudVysielanieUtils.isEqual($OldIdDBNadradena, $IdDbNadradena)) {
				$ZmenaIdentifikacie = true;
			}
			// 8.4. IDENTIFIKACIA AKCIE
			// Ak OldJeCRD= false
			if (!oldCRD) {
				// AK NewCRD= true
				if (newCRD) {
					// Ak NewJePrimarna je null
					if (newJePrimarna == null) {
						// CHYBA KONIEC
						// "nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: Číslo DB %$dataRow.CISLO%"
						chyba += "CHYBASUBLOC11: nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: Číslo DB! ";
						res.setError(true);
						res.setErrorMsg(chyba);
						return res;
					}
					// INAK Ak NewJePrimarna=TRUE
					else if (newJePrimarna) {
						// SpracujNewDB=false
						spracujNewDB = false;
					}
					// INAK Ak NewJePrimarna=FALSE
					else if (!newJePrimarna) {
						// SpracujNewDB=true
						spracujNewDB = true;
					}
				}// end if (newJeCr) {

			} // end if OldJeCRD= false

			else if (oldCRD) { // Ak OldJeCRD= true
				// 8.4.1.2. AK NewJeCRD=true
				// AK OldJePrimarna<>NewJePrimarna
				// CHYBA KONIEC
				// "Nemožno zmenit z primárnej na subsidiárnu a opaène ak je záznam v CRD Èíslo DB %$dataRw.CISLO%"
				if (newCRD) {
					// AK OldJePrimarna<>NewJePrimarna newJePrimarna!=null - je napr. pri kolajach
					if (newJePrimarna != null && !CudVysielanieUtils.isEqual(oldJePrimarna, newJePrimarna)) {
						// CHYBASUBLOC KONIEC
						// "Nemožno zmenit z primárnej na subsidiárnu a opaène ak je záznam v CRD Èíslo DB %$dataRw.CISLO%"
						chyba += "CHYBASUBLOC12: Nemožno zmenit z primárnej na subsidiárnu a opačne ak je záznam v CRD ";
						res.setError(true);
						res.setErrorMsg(chyba);
						return res;
					}
					// AK OldJePrimarna je null
					// CHYBA KONIEC
					// "Nesprávne definovaná lokalita v èíselníku T_DOPRAVNY_BOD s priznakom CRD: Èíslo DB %$dataRow.CISLO%"
					else if (oldJePrimarna.equals(null)) {
						chyba += "CHYBASUBLOC13: Nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: ";
						res.setError(true);
						res.setErrorMsg(chyba);
						return res;
					}
					// INAK AK OldJePrimarna =true
					else if (oldJePrimarna.equals(true)) {
						// SpracujOldDB=false
						spracujOldDB = false;
					}
					// INAK AK OldJePrimarna =false
					// SpracujOldDB=true
					else if (oldJePrimarna.equals(false)) {
						spracujOldDB = true;
					}
					// 8.4.1.2.3. Ak NewJePrimarna je null
					// CHYBA KONIEC
					// "Nesprávne definovaná lokalita v èíselníku T_DOPRAVNY_BOD s priznakom CRD: Èíslo DB %$dataRow.CISLO%"
					if (newJePrimarna.equals(null)) {
						// CHYBA KONIEC
						// "Nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: Číslo DB %$dataRow.CISLO%"
						chyba += "CHYBASUBLOC14: Nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: ";
						res.setError(true);
						res.setErrorMsg(chyba);
						return res;
					}
					// INAK AK NewJePrimarna=TRUE
					// SpracujNewDB=false
					else if (newJePrimarna.equals(true)) {
						spracujNewDB = false;
					}
					// INAK AK NewJePrimarna=FALSE
					// SpracujNewDB=true
					else if (newJePrimarna.equals(false)) {
						spracujNewDB = true;
					}
					// 8.4.1.2.4. Ak $ZmenaIdentifikacie=false
					if (!$ZmenaIdentifikacie) {
						// Ak SpracujNewDB=true and SpracujOldDB=true
						// SpracujOldDB=false
						if (spracujNewDB && spracujOldDB) {
							spracujOldDB = false;
						}
					}
				} // end if (newCRD) {
					// INAK AK NewJeCRD=false
				else if (!newCRD) {
					// 8.4.1.2.5. AK OldJePrimarna je null
					if (CudVysielanieUtils.isEqual(oldJePrimarna, null)) {
						// CHYBA KONIEC
						// "Nesprávne definovaná lokalita v èíselníku T_DOPRAVNY_BOD s priznakom CRD: Èíslo DB %$dataRow.CISLO%"
						chyba += "CHYBASUBLOC15: Nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: ";
						res.setError(true);
						res.setErrorMsg(chyba);
						return res;
					}
					// INAK AK OldJePrimarna=true
					// SpracujOldDB=false
					else if (CudVysielanieUtils.isEqual(oldJePrimarna, true)) {
						spracujOldDB = false;
					}
					// INAK AK OldJePrimarna=false
					// SpracujOldDB=true
					else if (CudVysielanieUtils.isEqual(oldJePrimarna, false)) {
						spracujOldDB = true;
					}
				}

			}// end Ak OldJeCRD= true

			// 9. IDENTIFIKACIA_OPERACIE
			// 9.1. /*$IDCountrySK*/
			// Systém vyh¾adá ID country platný záznam kde CountryCodeISO = "SK"
			// to uz mam v tcountry
			Integer IDCountrySK = tCountry.getCountryID();

			// 9.5. Ak in (Z)
			if ("Z".equals($Operacia)) {
				// SpracujNewDB=false
				// SpracujOldDB=true
				spracujNewDB = false;
				spracujOldDB = true;
			}

			// INAK AK $dataRow.ZMAZ=T
			else if ("T".equals(dataRow.getZmaz())) {
				// $OPERACIA=Z
				// SpracujNewDB=false
				// SpracujOldDB=true
				$Operacia = "Z";
				spracujNewDB = false;
				spracujOldDB = true;
			}

			// 10 . SPRACOVANIE
			// 10.1. Ak SpracujOldDB=true

			if (spracujOldDB) {
				/* $dataRowSubsidiaryList */
				// Systém z aktuálnych záznamov vyfiltruje zaznamy
				// kde
				// ZMAZ='F'
				// SUBSIDIARY_LOCATION_CODE=$OldLocationCode
				Date $vlozDatum = $DatumOd;
				ArrayList<DTOTSubsidiaryLocation> dataRowSubsidiaryList = dlg.getTCudCiselnikyClass().getSubsidiaryLocationList(auth, $OldLocationCode, $OldNadradenyDB, $OldSubsidiaryType, tCountry.getCountryID(), tCompany.getCompanyUicCode(), $vlozDatum);

				// 10.1.1.1.1 AK $dataRowSubsidiaryList neprazdny
				// $OldOPERACIA="U"
				// AKTUALIZUJ ZAZNAMY
				// PRE kazdy zaznam $dataRowEnd v $dataRowSubsidiaryList
				for (DTOTSubsidiaryLocation dataRowEnd : dataRowSubsidiaryList) {
					String $oldOperacia = "U";
					// Systém vytvorí updZmenaHodnotCiselnika a nastaví
					// ÈiselníkNázov=´T_SUBSIDIARY_LOCATION
					boolean ZapísaZmeny = true;
					Map<String, String> rowMap = new HashMap<String, String>();

					DTOImport dtoImport = new DTOImport();
					dtoImport.setIDCiselnik(idSubLoc);
					dtoImport.setCiselnikTabulka("T_SUBSIDIARY_LOCATION");
					dtoImport.setStav(_CudConsts.IMPORT_STAV_IMPORT);
					/* updZaznam */
					// Systém vytvorí updZmena a nastaví
					// ID=$dataRowEnd.SUBSIDIARY_LOCATION_ID
					rowMap.put("SUBSIDIARY_LOCATION_ID", dataRowEnd.getSubsidiaryLocationID().toString());

					// OPERACIA=$OldOPERACIA
					// platnostOd=datumNow
					// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR
					rowMap.put("XLS_OPERACIA", $oldOperacia);
					// platnostOd=datumNow
					rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum($datumNow));
					// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR
					rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(zmena.getCasSchvaleniaGrOd()));

					if (dataRowEnd.getStartValidity().getTime() >= $vlozDatum.getTime()) {
						// OPERACIA=$OldOperacia
						rowMap.put("XLS_OPERACIA", "Z");
					} else {
						rowMap.put("END_VALIDITY",
 CudVysielanieUtils.getStringDatum(DateUtils.plusDay($vlozDatum, -1)));
					}

					// if (dataRowEnd.getStartValidity() != null
					// && dataRowEnd.getStartValidity().getTime() >= $vlozDatum.getTime()) {
					// // EndValidity= $dataRowEnd.START_VALIDITY-1den
					// rowMap.put("END_VALIDITY",
					// CudVysielanieUtils.getStringDatum(DateUtils.plusDay(dataRowEnd.getStartValidity(), -1)));
					// } else {
					// // INAK EndValidity= $vlozDatum-1 den
					// rowMap.put("END_VALIDITY", CudVysielanieUtils.getStringDatum(DateUtils.plusDay($vlozDatum, -1)));
					// }

					res = dlg.getAktualizaciaLocZDbClass().cudServiseUpdate(auth, dtoImport, rowMap, $datumNow);
					if (res.isError()) {
						return res;
					}
				}// end for

			} // if (spracujOldDB) {

			// 10.2 Ak SpracujNewDB=true
			if (spracujNewDB) {
				// $vlozDatum=max(dataRow.CDR_ZAC,$datumOd)
				Date $vlozDatum = CudVysielanieUtils.getMax(dataRow.getCrdZac(), $DatumOd);
				// 10.2.1 /*$dataRowPrimaryLocation*/
				// Systém vyh¾adá pre bod primárnu Lokalitu
				// ZMAZ=F
				// LOCATION_CODE=prvých 5 èislic z $NadradenyDb.CISLO
				// START_VALIDITY<= $vlozDatum a (END_VALIDTY is null or END_VALIDITY>=$vlozDatum)
				// ID_COMPANY.COMPANY_UIC_CODE = UICZSR
				// ID_COMPANY.PLATNOST_DO is null
				// ID_COUNTRY=$IDCountrySK
				// usporiadaj podla START_VALIDITY desc

				DTOTPrimaryLocation dataRowPrimary = dlg.getTCudCiselnikyClass().getPrimaryLocationForValidity(auth, $NadradenyDB, tCountry.getCountryID(), tCompany.getCompanyID(), $vlozDatum, $datumNow);

				if (dataRowPrimary == null) {
					// CHYBA "Primarna lokalita %$OldNadradenyDb.CISLO[1..5]% k dátumu %datumNow% neexistuje"
					chyba += "CHYBASUBLOC16: Nadradena Primarna lokalita " + $OldNadradenyDB + " k dátumu " + $vlozDatum + " neexistuje. ";
					res.setError(true);
					// res.setErrorSubj("F");○
					res.setErrorMsg(chyba);
					return res;
				}
				// 10.2.1.2./*$dataRowSubsidiaryList*/
				ArrayList<DTOTSubsidiaryLocation> dataRowSubsidiaryListNew = dlg.getTCudCiselnikyClass().getSubsidiaryLocationList(auth, $LocationCode, $OldNadradenyDB, $OldSubsidiaryType, tCountry.getCountryID(), tCompany.getCompanyUicCode(), $vlozDatum);

				// 10.2.1.3 PRE kazdy zaznam $dataRowEnd v $dataRowSubsidiaryList

				DTOTSubsidiaryLocation $dataRowSubsidiary = null;
				// pri kolajach moze byt viacero zaznamov, tak vsetky ukoncim a vytvorim ho znova
				for (DTOTSubsidiaryLocation dataRowEnd : dataRowSubsidiaryListNew) {
					// Ak $dataRowEnd.START_VALIDITY<=$vlozDatum a zaroveò($dataRowEnd.END_VALIDITY is null or
					// $dataRowEnd.END_VALIDITY>=$vlozDatum
					// NewOperácia=X
					// $dataRowSubsidiary=$dataRowEnd
					// INAK
					// NewOperácia='U'

					String newOperacia = null;

					if (dataRowEnd.getStartValidity().getTime() <= $vlozDatum.getTime()
							&& (dataRowEnd.getEndValidity() == null || dataRowEnd.getStartValidity().getTime() >= $vlozDatum
									.getTime())) {
						newOperacia = "X";
						$dataRowSubsidiary = dataRowEnd;

					} else {
						newOperacia = "U";

					}

					// //10.2.1.3.2. AK NewOperácia='U'
					if ("U".equals(newOperacia)) {
						// Systém vytvorí updZmenaHodnotCiselnika a nastaví
						// ÈiselníkNázov=´T_SUBSIDIARY_LOCATION
						// ZapísaZmeny=true
						// /*updZaznam*/
						// Systém vytvorí updZmena a nastaví
						// ID=$dataRowEnd.SUBSIDIARY_LOCATION_ID

						boolean ZapísaZmeny = true;
						Map<String, String> rowMap = new HashMap<String, String>();

						DTOImport dtoImport = new DTOImport();
						dtoImport.setIDCiselnik(idSubLoc);
						dtoImport.setCiselnikTabulka("T_SUBSIDIARY_LOCATION");
						dtoImport.setStav(_CudConsts.IMPORT_STAV_IMPORT);
						/* updZaznam */
						// Systém vytvorí updZmena a nastaví
						// ID=$dataRowEnd.SUBSIDIARY_LOCATION_ID
						rowMap.put("SUBSIDIARY_LOCATION_ID", dataRowEnd.getSubsidiaryLocationID().toString());

						rowMap.put("XLS_OPERACIA", newOperacia);
						// platnostOd=datumNow
						rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum($datumNow));
						// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR
						rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(zmena.getCasSchvaleniaGrOd()));

						// AK $dataRowEnd.START_VALIDITY>=$vlozDatum
						// EndValidity= $dataRowEnd.START_VALIDITY-1den
						// INAK
						// EndValidity= $vlozDatum-1 den

						// rowMap.put("START_VALIDITY", CudVysielanieUtils.getStringDatum($vlozDatum));

						// if (dataRowEnd.getStartValidity() != null
						// && dataRowEnd.getStartValidity().getTime() >= $vlozDatum.getTime()) {
						// rowMap.put("END_VALIDITY", CudVysielanieUtils.getStringDatum(DateUtils.plusDay(
						// dataRowEnd.getStartValidity(), -1)));
						// } else {
						// rowMap.put("END_VALIDITY",
						// CudVysielanieUtils.getStringDatum(DateUtils.plusDay($vlozDatum, -1)));
						// }
						// AK $dataRowEnd.START_VALIDITY>=$vlozDatum
						// updZaznam.OPERACIA=Z
						// INAK
						// EndValidity= $vlozDatum-1 den
						// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
						// nazovStlpca = END_VALIDITY
						// novaHodnota =EndValidity
						// staraHonota =$dataRowEnd.END_VALIDITY

						if (dataRowEnd.getStartValidity().getTime() >= $vlozDatum.getTime()) {
							// OPERACIA=$OldOperacia
							rowMap.put("XLS_OPERACIA", "Z");
						} else {
							rowMap.put("END_VALIDITY",
									CudVysielanieUtils.getStringDatum(DateUtils.plusDay($vlozDatum, -1)));
						}

						res = dlg.getAktualizaciaLocZDbClass().cudServiseUpdate(auth, dtoImport, rowMap, $datumNow);
						if (res.isError()) {
							return res;
						}

					}// end if
				} // end for
					// 10.2.2. entityStart= $vlozDatum
				Date entitaStart = $vlozDatum;
				// Ak$dataRowSubsidiary existuje
				// $NewOperácia='U'
				// entitastart= $dataRowSubsidiary.START_VALIDITY
				// INAK
				// $NewOperácia='N'

				String newOperacia = "N";
				if (null != $dataRowSubsidiary) { // ked uz bola predtym zmena
					newOperacia = "U";
					entitaStart = $dataRowSubsidiary.getStartValidity();
				}
				// 10.2.3. Systém vytvorí updZmenaHodnotCiselnika a nastaví
				// ÈiselníkNázov=´T_SUBSIDIARY_LOCATION
				// ZapísaZmeny=true
				Map<String, String> rowMap = new HashMap<String, String>();

				DTOImport dtoImport = new DTOImport();
				dtoImport.setIDCiselnik(idSubLoc);
				dtoImport.setCiselnikTabulka("T_SUBSIDIARY_LOCATION");
				dtoImport.setStav(_CudConsts.IMPORT_STAV_IMPORT);

				// 10.2.3.1. /*updZaznam*/
				// Systém vytvorí updZmena a nastaví
				// ID=ak existuje $dataRowsubsidiary tak $dataRowSubsidiary.SUBSIDIARY_LOCATION_ID
				if ($dataRowSubsidiary != null) {
					rowMap.put("SUBSIDIARY_LOCATION_ID", $dataRowSubsidiary.getSubsidiaryLocationID().toString());
				}

				// OPERACIA=$NewOperácia
				rowMap.put("XLS_OPERACIA", newOperacia);
				// platnostOd=datumNow
				rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum($datumNow));
				// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR
				rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(zmena.getCasSchvaleniaGrOd()));

				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = ID_COUNTRY
				// novaHodnota = $IDCountrySK
				rowMap.put("ID_COUNTRY", AktualizaciaLocZDbClass.getString(tCountry.getCountryID()));
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = ID_COMPANY
				// novaHodnota =$IDCompanyZSR
				rowMap.put("ID_COMPANY", AktualizaciaLocZDbClass.getString(tCompany.getCompanyID()));
				// ID_PRIMARY_LOCATION= $dataRowPrimaryLocation.ID_PRIMARY_LOCATION
				if (dataRowPrimary != null) {
					rowMap.put("ID_PRIMARY_LOCATION", AktualizaciaLocZDbClass.getString(dataRowPrimary.getPrimaryLocationID()));
				}
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = SUBSIDIARY_LOCATION_CODE
				// novaHodnota = $Locationcode
				rowMap.put("SUBSIDIARY_LOCATION_CODE", $LocationCode);
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = SUBSIDIARY_LOCATION_NAME
				// novaHodnota = $Nazov
				rowMap.put("SUBSIDIARY_LOCATION_NAME", $Nazov);

				rowMap.put("START_VALIDITY", CudVysielanieUtils.getStringDatum(entitaStart));
				rowMap.put("END_VALIDITY", CudVysielanieUtils.getStringDatum(dataRow.getCrdKon()));

				// nazovStlpca = ID_SUBSIDIARY_TYPE
				// novaHodnota = $subsidiaryType
				rowMap.put("ID_SUBSIDIARY_TYPE", AktualizaciaLocZDbClass.getString($SubsidiaryType));
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = LONGITUDE
				// novaHodnota = $Longitude
				rowMap.put("LONGITUDE", CudVysielanieUtils.getStringZaokruhleneNa6($Longitude));
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = LATITUDE
				// novaHodnota = $Latitude
				rowMap.put("LATITUDE", CudVysielanieUtils.getStringZaokruhleneNa6($Latitude));

				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = FREE_TEXT
				// novaHodnota = $Poznamka
				rowMap.put("FREE_TEXT", $Poznamka);

				rowMap.put("ACTIVE_FLAG", "T");

				// Systém zavolá CUD WS CSCudService.updZmenaHodnotCiselnika
				res = dlg.getAktualizaciaLocZDbClass().cudServiseUpdate(auth, dtoImport, rowMap, $datumNow);
				if (res.isError()) {
					return res;
				}

			} // end if ( spracujNewDB) {

		} catch (Throwable e) {
			res = new ActionResult();
			res.setError(true);
			// actRes.setErrorMsg(e.getMessage() + " " + dataRowDb.toString() + " " + rowMap);
			chyba += e.getMessage();
			log.info("CudAktLocZDbProcess rowMap PrimaryLocation: " + chyba);
			res.setErrorMsg(chyba);
			handleException(e, "CudAktPrimLocZDbProcess rowMap PrimaryLocation.error", auth);
			return res;
		}
		return new ActionResult();
	}

			private boolean isZmena(AuthInfo auth, DTOZmena zmena) throws AppException {

		ArrayList<DTOZmenaStlpecCrd> zmenaList = dlg.getZmenaStlpecClass().getZmenaStlpecHodnotList(auth,
				zmena.getZmenaID());
		// dto.setNewValue(rVal(r, CudZmenaStlpecPeer.NEW_VALUE).asString());
		// dto.setOldValue(rVal(r, CudZmenaStlpecPeer.OLD_VALUE).asString());
		// dto.setNazov(nazov);

		for (DTOZmenaStlpecCrd dto : zmenaList) {
			// Ak neexistuje
			// $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV in

			if ("CRD_ZAC".equals(dto.getNazov()) && !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}
			if ("CRD_KON".equals(dto.getNazov()) && !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("CISLO".equals(dto.getNazov()) && !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("NAZOV".equals(dto.getNazov()) && !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}


			if ("ID_SUBSIDIARY_TYPE".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}
			if ("ID_COMPANY".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}
			if ("ID_NADRADENA_PRIMARNA".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("POZNAMKA".equals(dto.getNazov()) && !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("GPS_SIRKA".equals(dto.getNazov()) && !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("GPS_DLZKA".equals(dto.getNazov()) && !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

		}
		return false;
	}

}
