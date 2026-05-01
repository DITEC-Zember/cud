package sk.ditec.crd;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.crd.dto.DTOTCompany;
import sk.ditec.crd.dto.DTOTCountry;
import sk.ditec.crd.dto.DTOTDopravnyBod;
import sk.ditec.crd.dto.DTOTPrimaryLocation;
import sk.ditec.crd.dto.DTOZmenaStlpecCrd;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.zsr.common.server._NovyPISBaseClass;
import sk.ditec.zsr.common.server.utils.DateUtils;

public class AktualizaciaPrimLocZDbClass extends _NovyPISBaseClass {

	private Logger log = LoggerFactory.getLogger(AktualizaciaPrimLocZDbClass.class);

	private _CudCrdDelegate dlg = new _CudCrdDelegate();

	// private ArrayList<DTOZmena> zmenaListAkt;
	// private DTOTCountry tCountry;
	// private DTOTCompany tCompany;
	// private String chyba = "";
	// private boolean errors = false;
	// private Integer idSubLoc ;
	// private Integer idPrimLoc;
	// private Date datumACasAktualizacie = new Date();
	// private Date datumCasPoslAkt ;
	// private Integer idCrdStlpec;

	public ActionResult aktualizujPrimLoc(AuthInfo auth, DTOZmena zmena, Date datumACasAktualizacie,
			DTOTDopravnyBod dataRow, DTOTCountry tCountry, DTOTCompany tCompany, Integer idPrimLoc, Integer idSubLoc)
			throws AppException {
		String chyba = "";

		ActionResult res = new ActionResult();
		try {
			// 2. UICZSR='0056'
			// String uiczsr = "0056";
			// $datumNow=aktuálny dátum
			Date datumNow = new Date();
			// 3. /*CompanyZsr*/ posielam ako parameter v tcompany - posielam
			// Systém nájde záznam pre UIC ZSR platný k dátumu $dataRow.PLATNOST_OD

			// ak neexistuje zmena definovanych atributov
			if (zmena != null) {
				// $Zmena.OPERACIA = U, N
				// if ("U".equals(zmena.getOperacia()) || "N".equals(zmena.getOperacia())) {
				// boolean isZmena = isZmena(auth, zmena);
				// if (!isZmena) {
				// return res;
				// }
				// }
			}

			// 4./*$CRD_ZACStlpec:CUD_ZMENA_STLPEC*/
			// Systém nájde $Ståpec v $Zmena, kde $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV
			// = CRD_ZAC
			DTOZmenaStlpecCrd $CRDZACStlpec = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(),
					"CRD_ZAC");
			// 5 /*$CRDKONStlpec:CUD_ZMENA_STLPEC*/
			// Systém nájde $Ståpec v $Zmena, kde $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV
			// = CRD_KON
			DTOZmenaStlpecCrd CRDKONStlpec = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth, zmena.getZmenaID(),
					"CRD_KON");

			// DTOTDopravnyBod dataRowDb = dlg.getTCudCiselnikyClass().getDopravnyBodByZmenaId(auth,
			// zmena.getZmenaID());

			// 5.1 Ak$dataRow.CRD_KON nie je null and $dataRow.CRD_KON< $dataRow.PLATNOST_OD and $CRDKONStlpec: je null
			// NAVRAT

			if (dataRow.getCrdKon() != null && dataRow.getCrdKon().getTime() < dataRow.getPlatnostOd().getTime()
					&& CRDKONStlpec == null) {
				// navrat
				return res;
			}

			// 6. IDENTIFIKACIA CRD
			// DTOZmenaStlpecCrd dtoZmenaStlpcaCrd = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth,
			// zmena.getZmenaID(), "CRD");
			// 6.1. NewJeCR=false
			boolean newJeCrd = false;

			if (dataRow.getCrdZac() != null) {
				newJeCrd = true;
			}
			// 6.2. OldCRD=NewJeCRD
			boolean oldJeCrd = newJeCrd;
			// Ak $CRD_ZACStlpec nie je null and $CRDStlpec.OLD_VALUE je null
			// OldCRD=false
			if ($CRDZACStlpec != null && $CRDZACStlpec.getOldValue() == null) {
				oldJeCrd = false;
				// INAK $CRD_ZACStlpec nie je null and $CRDStlpec.OLD_VALUE nie je null
				// OldCRD=true
			} else if ($CRDZACStlpec != null && $CRDZACStlpec.getOldValue() != null) {
				oldJeCrd = true;
			}
			// 6.2.2 Ak NewJeCR =false and OldJeCRD =false
			// NESPRACOVÁVAM záznam
			if (!newJeCrd && !oldJeCrd) {
				return res;
			}

			// 7. NACITANIE HODNOT
			// ci ide o primarnu Lokalitu identifikujme CRD=true a ID_SUBSIDIARY_TYPE is null a ID_COMPANY=0056)
			// (ci ide o subsidiarnu Lokalitu identifikujme CRD=true a ID_SUBSIDIARY_TYPE is not null a ID_COMPANY=0056)
			// pre primarnu lokalitu riesim CRD, ID_SUBSIDIARY_TYPE,ID_COMPANY,CISLO
			// pre primarnu lokalitu riesim CRD, ID_SUBSIDIARYYPE,ID_COMPANY,ID_NADRADENA_PRIMARNA,CISLO

			// 7.1 Systém nastaví
			// $SubsidiaryType=$dataRow.ID_SUBSIDIARY_TYPE
			// $LocationCode=$dataRow.CISLO
			// $Company=$dataRow.Id_COMPANY
			// $IdDBNadradena=$dataRow.ID_NADRADENA_PRIMARNA
			// $datumOd=$dataRow.PLATNOST_OD
			Integer $SubsidiaryType = dataRow.getIDSubsidiaryType();
			String $LocationCode = dataRow.getCislo();
			if ($LocationCode != null) {
				$LocationCode = $LocationCode.substring(0, 5);
			}
			Integer $CompanyId = dataRow.getIDCompany();
			// 7.1.1. Ak $Company<>null
			/* UICCode */
			String $uicCode = null;
			// Systém nájde COMPANY_UIC_CODE pre poslednúverziu záznamu T_COMPANY.COMPANY_ID=$Company
			if ($CompanyId != null) {
				$uicCode = dlg.getTCudCiselnikyClass().getCompanyUicKodById(auth, $CompanyId);
				if ($uicCode == null) {
					chyba = " CHYBASUBLOC7: neexistuje platne uicCode  pre id=" + $CompanyId;
					res.setError(true);
					res.setErrorMsg(chyba);
					return res;
				}
			}
			Date $datumOd = dataRow.getPlatnostOd();

			// 7.2 Systém nastaví
			// Systém nastaví
			// $OldSubsidiaryType=$SubsidiaryType
			// $OldLocationCode=$LocationCode
			// $OldUICCode=$UICCode
			// $OldCompany=$Company
			// $OldIdDBNadradena=$IdDBNadradena

			Integer $OldSubsidiaryType = $SubsidiaryType;
			String $OldLocationCode = $LocationCode;
			Integer $OldCompany = $CompanyId;
			String $OldUICCode = $uicCode;

			// 7.3. /*$SubsidiaryTypeStlpec:CUD_ZMENA_STLPEC*/
			// Systém nájde $Ståpec v $Zmena, kde $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV
			// =ID_SUBSIDIARY_TYPE
			DTOZmenaStlpecCrd dtoZmenaStlpcaIdSubType = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth,
					zmena.getZmenaID(), "ID_SUBSIDIARY_TYPE");
			// AK $SubsidiaryTypeStlpec nie je null
			// $OldSubsidiaryType=$OldSubsidiaryTypeStlpec.OLD_VALUE
			if (dtoZmenaStlpcaIdSubType != null && dtoZmenaStlpcaIdSubType.getOldValue() != null) {
				$OldSubsidiaryType = Integer.valueOf(dtoZmenaStlpcaIdSubType.getOldValue());
			}

			// 7.4. null;
			/* $LocationCodeStlpec:CUD_ZMENA_STLPEC */
			// Systém nájde $Ståpec v $Zmena, kde $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV
			// =CISLO
			// AK $LocationCodeStlpec nie je null
			// $OldLocationCode=$LocationCodeStlpec.OLD_VALUE
			DTOZmenaStlpecCrd dtoZmenaStlpcaCislo = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth,
					zmena.getZmenaID(), "CISLO");

			if (dtoZmenaStlpcaCislo != null) {
				$OldLocationCode = dtoZmenaStlpcaCislo.getOldValue();

				if ($OldLocationCode != null) {
					$OldLocationCode = $OldLocationCode.substring(0, 5);
				}
			}
			// 7.5. /*$CompanyStlpec:CUD_ZMENA_STLPEC*/
			// Systém nájde $Ståpec v $Zmena, kde $Zmena.CUD_ZMENA_STLPEC.(ID_CISELNIK_STLPEC).CUD_CISELNIK_STLPEC.NAZOV
			// =ID_COMPANY
			// AK $CompanyStlpec nie je null
			// $OldCompany=$CompanyStlpec.OLD_VALUE
			DTOZmenaStlpecCrd dtoZmenaStlpcaIdCompany = dlg.getZmenaStlpecClass().getZmenaStlpecHodnot(auth,
					zmena.getZmenaID(), "ID_COMPANY");

			if (dtoZmenaStlpcaIdCompany != null && dtoZmenaStlpcaIdCompany.getOldValue() != null) {
				$OldCompany = Integer.valueOf(dtoZmenaStlpcaIdCompany.getOldValue());
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

			// //8. IDENTIFIKACIA PRIMARNEJ
			// 8.1.NewJePrimárna=NULL
			Boolean newJePrimarna = null;
			// AK
			// $SubsidiaryType = NULL a zároveò
			// $UICCode=UICZSR
			if ($SubsidiaryType == null && CudVysielanieUtils.isEqual($uicCode, _CudConsts.COMPANY_UIC_CODE_ZSR)) {
				newJePrimarna = true;
			}
			// INAK AK $SubsidiaryType <> NULL a zároveò
			// $UICCode=UICZSR
			else if ($SubsidiaryType != null && CudVysielanieUtils.isEqual($uicCode, _CudConsts.COMPANY_UIC_CODE_ZSR)) {
				newJePrimarna = false;
			}
			// 8.2. OldJePrimárna=NULL
			Boolean oldJePrimarna = null;
			// Ak
			// $OldSubsidiaryType <> null
			// $OldUICCode= UICZSR

			if ($OldSubsidiaryType != null && CudVysielanieUtils.isEqual($OldUICCode, _CudConsts.COMPANY_UIC_CODE_ZSR)) {
				oldJePrimarna = false;
			}
			// INAK AK
			// $OldSubsidiaryType= null
			// $OldUICCode= UICZSR
			// OldJePrimárna=true
			else if ($OldSubsidiaryType == null
					&& CudVysielanieUtils.isEqual($OldUICCode, _CudConsts.COMPANY_UIC_CODE_ZSR)) {
				oldJePrimarna = true;
			}

			// 9. SpracujOldDB=false
			// SpracujNewDB=false
			// $ZmenaIdentifikacie=false
			boolean spracujOldDB = false;
			boolean spracujNewDB = false;
			boolean $zmenaIdentifikacie = false;
			// 8.1 Ak
			// $OldLocationCode<>$LocationCode
			// $ZmenaIdentifikacie=true
			if (!CudVysielanieUtils.isEqual($OldLocationCode, $LocationCode)) {
				$zmenaIdentifikacie = true;
			}

			// 10. IDENTIFIKACIA AKCIE
			// 10.1. Ak OldJeCRD= false
			if (!oldJeCrd) {
				// 10.1.1. AK NewCRD= true
				if (newJeCrd) {
					// Ak NewJePrimarna je null
					if (newJePrimarna == null) {
						// CHYBA KONIEC
						// "nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: Číslo DB %$dataRow.CISLO%"
						chyba += "CHYBAPRIMLOC1: nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: Číslo DB! ";
						res.setError(true);
						res.setErrorMsg(chyba);
						return res;
					}
					// INAK Ak NewJePrimarna=TRUE
					else if (newJePrimarna) {
						// SpracujNewDB=true
						spracujNewDB = true;
					}
					// INAK Ak NewJePrimarna=FALSE
					else if (!newJePrimarna) {
						res = dlg.getAktualizaciaSubLocZDbClass().aktualizujSubLoc(auth, zmena, null,
								datumACasAktualizacie, dataRow, null, tCountry, tCompany, idPrimLoc, idSubLoc);
						if (res.isError()) {
							return res;
						}

						spracujNewDB = false;
					}
				}// end if (newJeCr) {

			} // end if OldJeCRD= false
				// 10.1.2. INAK Ak OldJeCRD= true
			else if (oldJeCrd) {
				// 10.1.2. AK NewJeCRD=true
				if (newJeCrd) {
					// AK OldJePrimarna<>NewJePrimarna
					if (!CudVysielanieUtils.isEqual(oldJePrimarna, newJePrimarna)) {
						// CHYBA KONIEC
						// "Nemožno zmenit z primárnej na subsidiárnu a opaène ak je záznam v CRD Èíslo DB %$dataRw.CISLO%"
						chyba += "CHYBAPRIMLOC2: Nemožno zmenit z primárnej na subsidiárnu a opačne ak je záznam v CRD";
						res.setError(true);
						res.setErrorMsg(chyba);
						return res;
					}
					// 10.1.2.2..AK OldJePrimarna je null
					if (oldJePrimarna == null) {
						// CHYBA KONIEC
						// "Nesprávne definovaná lokalita v èíselníku T_DOPRAVNY_BOD s priznakom CRD: Èíslo DB %$dataRow.CISLO%"
						chyba += "CHYBAPRIMLOC3: Nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: ";
						res.setError(true);
						res.setErrorMsg(chyba);
						res.setKeyErrorSubj("F");
						return res;
					}
					// 10.1.2.3. INAK AK OldJePrimarna =true
					else if (CudVysielanieUtils.isEqual(oldJePrimarna, true)) {
						// SpracujOldDB=true
						spracujOldDB = true;
					}
					// INAK AK OldJePrimarna =false
					else if (CudVysielanieUtils.isEqual(oldJePrimarna, false)) {
						// SpracujOldDB=false
						spracujOldDB = false;
						// Spracuj subsidiarnu lokalitu Anika odtiel prvykrat
						res = dlg.getAktualizaciaSubLocZDbClass().aktualizujSubLoc(auth, zmena, null,
								datumACasAktualizacie, dataRow, null, tCountry, tCompany, idPrimLoc, idSubLoc);
						return res;
					}

					// 10.1,2.3 Ak NewJePrimarna je null
					// CHYBA KONIEC
					// "Nesprávne definovaná lokalita v èíselníku T_DOPRAVNY_BOD s priznakom CRD: Èíslo DB %$dataRow.CISLO%"
					// INAK AK NewJePrimarna=TRUE
					// SpracujNewDB=true
					// INAK AK NewJePrimarna=FALSE
					// SpracujNewDB=false
					// Spracuj subsidiarnu lokalitu
					// NAVRAT
					if (newJePrimarna == null) {
						chyba += "CHYBAPRIMLOC3: Nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: cisloDb= "
								+ dataRow.getCislo();
						res.setError(true);
						res.setErrorMsg(chyba);
						res.setKeyErrorSubj("F");
						return res;
					} else if (newJePrimarna) {
						spracujNewDB = true;
					} else if (!newJePrimarna) {
						spracujNewDB = false;
						res = dlg.getAktualizaciaSubLocZDbClass().aktualizujSubLoc(auth, zmena, null,
								datumACasAktualizacie, dataRow, null, tCountry, tCompany, idPrimLoc, idSubLoc);
						return res;
					}

					// 10.1.2.4. Ak $ZmenaIdentifikacie=false
					if (!$zmenaIdentifikacie) {
						// Ak SpracujNewDB=true and SpracujOldDB=true
						if (spracujNewDB && spracujOldDB)
							// SpracujOldDB=false
							spracujOldDB = false;
					}
				}
			}
			// // 10.1.2.5. INAK AK NewJeCRD=false
			else if (!newJeCrd) {
				// Ak NewJePrimarna je null
				if (oldJePrimarna.equals(null)) {
					// CHYBA KONIEC
					// "Nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: Číslo DB %$dataRow.CISLO%"
					chyba += "CHYBAPRIMLOC4: Nesprávne definovaná lokalita v číselníku T_DOPRAVNY_BOD s priznakom CRD: ";
					res.setError(true);
					res.setErrorMsg(chyba);
					return res;
				}
				// INAK AK NewJePrimarna=TRUE
				else if (newJePrimarna.equals(true)) {
					// SpracujNewDB=true
					spracujNewDB = true;
				}
				// INAK AK NewJePrimarna=FALSE
				else if (newJePrimarna.equals(false)) {
					// SpracujNewDB=false
					spracujNewDB = false;
					// Spracuj subsidiarnu lokalitu
					res = dlg.getAktualizaciaSubLocZDbClass().aktualizujSubLoc(auth, zmena, null,
							datumACasAktualizacie, dataRow, null, tCountry, tCompany, idPrimLoc, idSubLoc);
					return res;
					// if (res.isError()) {
					// return res;
					// }
				}

			}// end if newJeCr

			// 11. IDENTIFIKACIA_OPERACIE
			// /*$datumNow*/
			// $datumNow=aktuálny dátum
			// /* $IDCompanyZSR*/
			// Systém vyh¾adá platný záznam kde companyUICCode = "0056"
			// /*$IDCountrySK*/
			// Systém vyh¾adá ID country platný záznam kde CountryCodeISO = "SK"

			// 11.2. Ak $dataRow.ZMAZ=T
			if ("T".equals(dataRow.getZmaz())) {
				// SpracujNewDB=false
				// SpracujOldDB=true
				spracujNewDB = false;
				spracujOldDB = true;
			}

			// 12. SPRACOVANIE
			// 12.1. Ak SpracujOldDB=true
			if (spracujOldDB) {
				Date $vlozDatum = $datumOd;
				$LocationCode = $LocationCode.substring(0, 5);
				ArrayList<DTOTPrimaryLocation> dataRowPrimaryList = dlg.getTCudCiselnikyClass().getPrimaryLocationList(
						auth, $LocationCode, tCountry.getCountryID(), _CudConsts.COMPANY_UIC_CODE_ZSR, $vlozDatum);

				// 12.1.1.2 Ak $dataRowPrimaryList neprazdny
				if (dataRowPrimaryList != null && dataRowPrimaryList.size() > 0) {
					// OldOperacia=U

					// 12.1.1.2.2.
					for (DTOTPrimaryLocation dataRowEndPrimary : dataRowPrimaryList) {
						// Systém vytvorí updZmenaHodnotCiselnika a nastaví
						// ÈiselníkNázov=´T_PRIMARY_LOCATION
						// ZapísaZmeny=true
						boolean zapisatZmeny = true;
						Map<String, String> rowMap = new HashMap<String, String>();

						DTOImport dtoImport = new DTOImport();
						dtoImport.setIDCiselnik(idPrimLoc);
						dtoImport.setCiselnikTabulka("T_PRIMARY_LOCATION");
						dtoImport.setStav(_CudConsts.IMPORT_STAV_IMPORT);
						/* updZaznam */
						// OPERACIA=$OldOperacia
						rowMap.put("XLS_OPERACIA", "U");
						// AK $dataRowEnd.START_VALIDITY>=$vlozDatum
						// updZaznam.OPERACIA=Z
						// INAK
						// EndValidity= $vlozDatum-1 den
						// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
						// nazovStlpca = END_VALIDITY
						// novaHodnota = EndValidity
						// staraHodnota= $dataRowEnd.END_VALIDITY
						if (dataRowEndPrimary.getStartValidity().getTime() >= $vlozDatum.getTime()) {
							// OPERACIA=$OldOperacia
							rowMap.put("XLS_OPERACIA", "Z");
						} else {
							// (dataRowEndPrimary.getStartValidity() != null &&
							// dataRowEndPrimary.getStartValidity().getTime() >= $vlozDatum.getTime()) {
							// // EndValidity= $dataRowEnd.START_VALIDITY-1den
							// rowMap.put("END_VALIDITY",
							// CudVysielanieUtils.getStringDatum(DateUtils.plusDay(dataRowEndPrimary.getStartValidity(),
							// -1)));
							// } else {
							// INAK EndValidity= $vlozDatum-1 den
							rowMap.put("END_VALIDITY",
									CudVysielanieUtils.getStringDatum(DateUtils.plusDay($vlozDatum, -1)));
							// }
						}

						rowMap.put("PRIMARY_LOCATION_ID", dataRowEndPrimary.getPrimaryLocationID().toString());

						// platnostOd=datumNow
						rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(datumNow));
						// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR
						rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(datumNow));

						// 1AK $dataRowEnd.START_VALIDITY>=$vlozDatum

						// rowMap.put("END_VALIDITY",
						// CudVysielanieUtils.getStringDatum(dataRowEndPrimary.getEndValidity()));
						// 11.1.2.3. Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
						// nazovStlpca = END_VALIDITY
						// novaHodnota = EndValidity
						// staraHodnota=$dataRowPrimary.END_VALIDITY
						// 11.1.3. Systém zavolá CUD WS CSCudService.updZmenaHodnotCiselnika
						res = dlg.getAktualizaciaLocZDbClass().cudServiseUpdate(auth, dtoImport, rowMap, datumNow);
						// if (res.isError()) {
						// return res;
						// }
					}// end for
				}
			} // end if (spracujOldDB) {

			// 12.2. Ak SpracujNewDB=true
			if (spracujNewDB) {
				// $vlozDatum=max(dataRow.CDR_ZAC,$datumOd)
				Date $vlozDatum = CudVysielanieUtils.getMax(dataRow.getCrdZac(), $datumOd);

				// $OldLocationCode = $OldLocationCode.substring(0, 5);
				ArrayList<DTOTPrimaryLocation> dataRowPrimaryListNew = dlg.getTCudCiselnikyClass()
						.getPrimaryLocationList(auth, $LocationCode, tCountry.getCountryID(),
								_CudConsts.COMPANY_UIC_CODE_ZSR, $vlozDatum);

				// 12.2.1.2. PRE kazdy $dataRowEnd v $dataRowPrimaryList
				// DTOTPrimaryLocation $dataRowPrimary = null;
				DTOTPrimaryLocation $dataRowPrimary = null;
				String newOperacia = "";
				for (DTOTPrimaryLocation dataRowEndPrimary : dataRowPrimaryListNew) {

					// Ak $dataRowEnd.START_VALIDITY<=$vlozDatum a zaroveò
					// ($dataRowEnd.END_VALIDITY is null or $dataRowEnd.END_VALIDITY>=$vlozDatum

					if (dataRowEndPrimary.getStartValidity().getTime() <= $vlozDatum.getTime()
							&& (dataRowEndPrimary.getEndValidity() == null || dataRowEndPrimary.getStartValidity()
									.getTime() >= $vlozDatum.getTime())) {
						newOperacia = "X";
						$dataRowPrimary = dataRowEndPrimary;

					} else {
						newOperacia = "U";

					}

					// 12.2.1.2.2 AK NewOperácia='U'
					if ("U".equals(newOperacia)) {
						// Systém vytvorí updZmenaHodnotCiselnika a nastaví
						// ÈiselníkNázov=´T_PRIMARY_LOCATION
						// ZapísaZmeny=true

						// ÈiselníkNázov=´T_PRIMARY_LOCATION
						// ZapísaZmeny=true
						Map<String, String> rowMap = new HashMap<String, String>();

						DTOImport dtoImport = new DTOImport();
						dtoImport.setIDCiselnik(idPrimLoc);
						dtoImport.setCiselnikTabulka("T_PRIMARY_LOCATION");
						dtoImport.setStav(_CudConsts.IMPORT_STAV_IMPORT);
						boolean zapisatZmeny = true;
						/* updZaznam */
						/* updZaznam */
						// Systém vytvorí updZmena a nastaví
						// ID= $dataRowEnd.PRIMARY_LOCATION_ID
						// OPERACIA=$OldOperacia
						// platnostOd=datumNow
						// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR
						// AK $dataRowEnd.START_VALIDITY>=$vlozDatum
						// updZaznam.OPERACIA=Z
						// INAK
						// EndValidity= $vlozDatum-1 den
						// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
						// nazovStlpca = END_VALIDITY
						// novaHodnota = EndValidity
						// staraHodnota=$dataRowEnd.END_VALIDITY

						if (dataRowEndPrimary.getStartValidity().getTime() >= $vlozDatum.getTime()) {
							// OPERACIA=$OldOperacia
							rowMap.put("XLS_OPERACIA", "Z");
						} else {
							// (dataRowEndPrimary.getStartValidity() != null &&
							// dataRowEndPrimary.getStartValidity().getTime() >= $vlozDatum.getTime()) {
							// // EndValidity= $dataRowEnd.START_VALIDITY-1den
							// rowMap.put("END_VALIDITY",
							// CudVysielanieUtils.getStringDatum(DateUtils.plusDay(dataRowEndPrimary.getStartValidity(),
							// -1)));
							// } else {
							// INAK EndValidity= $vlozDatum-1 den
							rowMap.put("END_VALIDITY",
									CudVysielanieUtils.getStringDatum(DateUtils.plusDay($vlozDatum, -1)));
							// }
						}

						rowMap.put("PRIMARY_LOCATION_ID", dataRowEndPrimary.getPrimaryLocationID().toString());
						rowMap.put("XLS_OPERACIA", newOperacia);
						// platnostOd=datumNow
						rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(datumNow));
						// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR
						rowMap.put("XLS_CAS_SCHVALENIA_GR",
								CudVysielanieUtils.getStringDatum(zmena.getCasSchvaleniaGr()));
						// EndValidity= min($datumOd,$dataRowPrimary.END_VALIDITY)
						// rowMap.put(
						// "END_VALIDITY",
						// CudVysielanieUtils.getStringDatum(
						// CudVysielanieUtils.getMin($datumOd, dataRowEndPrimary.getEndValidity())));
						// AK $dataRowEnd.START_VALIDITY>=$vlozDatum
						// EndValidity= $dataRowEnd.START_VALIDITY-1den
						// INAK
						// EndValidity= $vlozDatum-1 den

						// if (dataRowEndPrimary.getStartValidity() != null &&
						// dataRowEndPrimary.getStartValidity().getTime() >= $vlozDatum.getTime()) {
						// rowMap.put("END_VALIDITY",
						// CudVysielanieUtils.getStringDatum(DateUtils.plusDay(dataRowEndPrimary.getStartValidity(),
						// -1)));
						// } else {
						// rowMap.put("END_VALIDITY", CudVysielanieUtils.getStringDatum(DateUtils.plusDay($vlozDatum,
						// -1)));
						// }
						// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
						// nazovStlpca = END_VALIDITY
						// novaHodnota = EndValidity
						// staraHodnota=$dataRowEnd.END_VALIDITY
						// Systém zavolá CUD WS CSCudService.updZmenaHodnotCiselnika

						// 12.2.1.2.2.2. Systém zavolá CUD WS CSCudService.updZmenaHodnotCiselnika
						res = dlg.getAktualizaciaLocZDbClass().cudServiseUpdate(auth, dtoImport, rowMap, datumNow);
						// ignorujem chyby if (res.isError()) {
						// return res;
						// }
					}// end AK NewOperácia='U'
				}// end for

				// 12.2.2. AKTUALIZACIA/INSERT PRIMARY
				// INIT OD/ND START END
				// otvorenypreNDZac= dataRow.OTVORENY_PRE_ND_ZAC
				// otvorenypreNDKon= dataRow.OTVORENY_PRE_ND_KON
				// otvorenypreNDFlag =dataRow.OTVORENY_PRE_ND
				// otvorenypreODZac= dataRow.OTVORENY_PRE_OD_ZAC
				// otvorenypreODKon= dataRow.OTVORENY_PRE_OD_KON
				// otvorenypreODFlag =dataRow.OTVORENY_PRE_OD
				// entitastart= CRD_ZAC
				// entitaend=dataRow,CRD_KON
				Date otvorenypreNDZac = dataRow.getOtvorenyPreNdZac();
				Date otvorenypreNDKon = dataRow.getOtvorenyPreNdKon();
				String otvorenypreNDFlag = dataRow.getOtvorenyPreNd();
				Date otvorenypreODZac = dataRow.getOtvorenyPreOdZac();
				Date otvorenypreODKon = dataRow.getOtvorenyPreOdKon();
				String otvorenypreODFlag = dataRow.getOtvorenyPreOd();

				Date entitaStart = dataRow.getCrdZac();
				Date entitaEnd = dataRow.getCrdKon();

				// ak datarow,crd_kon !=null and crdKon<vlozdatum => navrat, podla Roba uz to riesi plugin,
				// tu nam robi zle.
				// if (dataRow.getCrdKon() != null && dataRow.getCrdKon().getTime() >= $vlozDatum.getTime()) {
				// return res;
				// }

				// Ak $dataRowPrimary existuje
				// NewOperácia='U'
				// /entitastart= $dataRowPrimary.START_VALIDITY takyto parameter nie je, dam vlozdatum
				// INAK
				// NewOperácia='N'
				if ($dataRowPrimary != null) {
					newOperacia = "U";
					if ($dataRowPrimary.getStartValidity() != null) {
						entitaStart = $dataRowPrimary.getStartValidity();
					}
				} else {
					newOperacia = "N";
				}

				// 12.2.2.3. NASTAV OD/ND START END
				// //12.2.2.3.1 Ak otvorenypreNDKon nie je null a entitaEnd nie je null
				// otvorenypreNDKon = min (otvorenypreNDKon,entitaEnd)
				if (otvorenypreNDKon != null && entitaEnd != null) {
					otvorenypreNDKon = CudVysielanieUtils.getMin(otvorenypreNDKon, entitaEnd);
				}
				// //12.2.2.3.2 Ak otvorenypreODKon nie je null a entitaEnd nie je null
				// otvorenypreODKon = min (otvorenypreODKon,entitaEnd)
				if (otvorenypreODKon != null && entitaEnd != null) {
					otvorenypreODKon = CudVysielanieUtils.getMin(otvorenypreODKon, entitaEnd);
				}
				// 12.2.2.3.3 AK otvorenypreNDKon nie je null a otvorenypreNDKon<entitastart
				// otvorenypreNDKon =null
				// otvorenypreNDZac=null
				// otvorenypreNDFlag=FALSE
				if (otvorenypreNDKon != null && otvorenypreNDKon.getTime() < entitaStart.getTime()) {
					otvorenypreNDKon = null;
					otvorenypreNDZac = null;
					otvorenypreNDFlag = "F";

				}
				// INAK AK otvorenypreNDZac nie je null
				else if (otvorenypreNDZac != null) {
					// 12.2.2.3.3.3. otvorenypreNDZac= max( otvorenypreNDZac,entitastart)

					otvorenypreNDZac = CudVysielanieUtils.getMax(otvorenypreNDZac, entitaStart);
					// 12.2.2.3.3.3.4.Ak otvorenyNDKon <>null a otvorenyNDKon<=otvorenyNDZac
					if (otvorenypreNDKon != null && otvorenypreNDKon.getTime() < entitaStart.getTime()) {
						otvorenypreNDKon = null;
						otvorenypreNDZac = null;
						otvorenypreNDFlag = "F";

					}
				} else {

					// 12.2.2.3.3.5. otvorenypreNDKon =null
					otvorenypreNDKon = null;
					// AK dataRow.OTVORENY_PRE_OD_ZAC nie je null
					if (dataRow.getOtvorenyPreNdZac() != null) {
						// otvorenypreNDFlag=FALSE
						otvorenypreNDFlag = "F";
					}
				}

				// 12.2.2.3.4. AK otvorenypreODKon nie je null a otvorenypreODKon<entitastart
				// otvorenypreODKon =null
				// otvorenypreODZac=null
				// otvorenypreODFlag=FALSE
				if (otvorenypreODKon != null && otvorenypreODKon.getTime() < entitaStart.getTime()) {
					otvorenypreODKon = null;
					otvorenypreODZac = null;
					otvorenypreODFlag = "F";
				}

				else if (otvorenypreODZac != null) {
					// 12.2.2.3.4.3. otvorenypreODZac= max( otvorenypreODZac,entitastart)
					otvorenypreODZac = CudVysielanieUtils.getMax(otvorenypreODZac, entitaStart);
					// 12.2.2.3.4.3. Ak otvorenyODKon <>null a otvorenyODKon<=otvorenyODZac

					if (otvorenypreODKon != null && otvorenypreODKon.getTime() <= otvorenypreODZac.getTime()) {
						// otvorenypreODKon =null
						// otvorenypreODZac=null
						// otvorenypreODFlag=FALSE
						otvorenypreODKon = null;
						otvorenypreODZac = null;
						otvorenypreODFlag = "F";
					}
				} else {
					// 12.2.2.3.4.5. otvorenypreODKon =null
					otvorenypreODKon = null;
					// AK dataRow.OTVORENY_PRE_ND_ZAC nie je null
					// otvorenypreODFlag=FALSE
					if (dataRow.getOtvorenyPreNdZac() != null) {
						otvorenypreODFlag = "F";
					}

				}


				// /////////////////////////////////////////////////////////
				// Ak $dataRowPrimary existuje
				// NewOperácia='U'
				// INAK
				// NewOperácia='N'
				Map<String, String> rowMap = new HashMap<String, String>();
				if ($dataRowPrimary != null) {
					newOperacia = "U";
					rowMap.put("PRIMARY_LOCATION_ID", $dataRowPrimary.getPrimaryLocationID().toString());

				} else {
					newOperacia = "N";
				}

				// 12.2.3 Systém vytvorí updZmenaHodnotCiselnika a nastaví
				// ÈiselníkNázov=´T_PRIMARY_LOCATION
				// ZapísaZmeny=true
				// boolean zapisatZmeny = true;

				DTOImport dtoImport = new DTOImport();
				dtoImport.setIDCiselnik(idPrimLoc);
				dtoImport.setCiselnikTabulka("T_PRIMARY_LOCATION");
				dtoImport.setStav(_CudConsts.IMPORT_STAV_IMPORT);

				/* updZaznam */
				/* updZaznam */
				// Systém vytvorí updZmena a nastaví
				// ID=$dataRowPrimary.PRIMARY_LOCATION_ID
				// OPERACIA=$NewOperácia
				// platnostOd=datumNow
				// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR

				// platnostOd=datumNow
				// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR

				// OPERACIA=$OldOperacia
				rowMap.put("XLS_OPERACIA", newOperacia);
				// platnostOd=datumNow
				rowMap.put("XLS_PLATNOST_OD", CudVysielanieUtils.getStringDatum(datumNow));
				// datumSchvalenia=Zmena.DATUM_SCHVALENIA_GR
				rowMap.put("XLS_CAS_SCHVALENIA_GR", CudVysielanieUtils.getStringDatum(zmena.getCasSchvaleniaGrOd()));

				// 12.2.3.2. ak ide o operaciu='U' tak sa vyplna v updStlpece
				// aj pole staraHodnota zo stlpca nazovStåpca a záznamu $dataRowPrimary
				// ak je novaHodnota a staraHodnota rovnaka tak sa zaznam updStlpece nevytvara

				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = ID_COUNTRY
				// novaHodnota = $IDCountrySK
				rowMap.put("ID_COUNTRY", AktualizaciaLocZDbClass.getString(tCountry.getCountryID()));
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = ID_COMPANY
				// novaHodnota =$IDCompanyZSR
				rowMap.put("ID_COMPANY", AktualizaciaLocZDbClass.getString(tCompany.getCompanyID()));
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = LOCATION_CODE
				// novaHodnota = $dataRow.CISLO bez kontrolnej èíslice na konci
				rowMap.put("LOCATION_CODE", dataRow.getCislo().substring(0, 5));
				// nazovStlpca = LOCATION_NAME
				// novaHodnota = $dataRow.NAZOV
				rowMap.put("LOCATION_NAME", dataRow.getNazov());
				// nazovStlpca = LOCATION_NAME_ASCII
				// novaHodnota = = $dataRow.NAZOV konvertovany na ASCII (bez diakritiky)
				rowMap.put("LOCATION_NAME_ASCII", CudVysielanieUtils.getStringBezDia(dataRow.getNazov()));
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = CONTAINER_HANDLING_FLAG
				// novaHodnota = $dataRow.MANIPULACIA_S_KONTAJNERMI
				rowMap.put("CONTAINER_HANDLING_FLAG", dataRow.getManipulaciaSKontajnermi());
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = HANDOVER_POINT_FLAG
				// novaHodnota = AK $dataRow.STYK_DRAH = TRUE TAK TRUE INAK FALSE
				rowMap.put("HANDOVER_POINT_FLAG", dataRow.getStykDrah());
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = NUTS_CODE
				// novaHodnota = pod¾a mapovania z $Ståpec.NEW_VALUE
				try { // kvoli chybnym datam sa obcas nenajde vyssi uzemny celok a potom pada
					if (dataRow.getIDVyssiUzemnyCelok() != null && dataRow.getIDVyssiUzemnyCelok() != null) {
						String oznacenie = dlg.getTCudCiselnikyClass().lookupVyssiUzemnyCelokOznacenie(auth,
								Integer.valueOf(dataRow.getIDVyssiUzemnyCelok()));
						rowMap.put("NUTS_CODE", oznacenie);
					}

				} catch (Throwable t) {
					log.error(" nenasiel sa vyssi uzemny celok pre IDVyssiUzemnyCelok= "
							+ dataRow.getIDVyssiUzemnyCelok());
					res = new ActionResult();
					res.setError(true);
					return res;
					// handleException(t, "CudAktPrimLocZDbProcess rowMap PrimaryLocation.error", auth);
				}

				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = START_VALIDITY
				// novaHodnota = $vlozDatum
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = END_VALIDITY
				// novaHodnota = dataRow,CRD_KON
				rowMap.put("START_VALIDITY", CudVysielanieUtils.getStringDatum(entitaStart));
				rowMap.put("END_VALIDITY", CudVysielanieUtils.getStringDatum(entitaEnd));

				// nazovStlpca = FREIGHT_POSSIBLE_FLAG
				// novaHodnota = AK $Ståpec.NEW_VALUE// NIE JE NULL TAK TRUE INAK FALSE
				// if (dataRow.getOtvorenyPreNd() != null) {
				// rowMap.put("FREIGHT_POSSIBLE_FLAG", ("T".equals(dataRow.getOtvorenyPreNd()) ? "T" : "F"));
				// }
				rowMap.put("FREIGHT_POSSIBLE_FLAG", (otvorenypreNDFlag));
				// /azovStlpca = FREIGHT_START_VALIDITY
				// novaHodnota = dataRow.OTVORENY_PRE_ND_ZAC
				rowMap.put("FREIGHT_START_VALIDITY", CudVysielanieUtils.getStringDatum(otvorenypreNDZac));
				// nazovStlpca = FREIGHT_END_VALIDITY
				// novaHodnota = dataRow.OTVORENY_PRE_ND_KON
				rowMap.put("FREIGHT_END_VALIDITY", CudVysielanieUtils.getStringDatum(otvorenypreNDKon));

				// nazovStlpca = PASSENGER_POSSIBLE_FLAG
				// novaHodnota = dataRow.OTVORENY_PRE_OD

				// if (dataRow.getOtvorenyPreOd() != null) {
				rowMap.put("PASSENGER_POSSIBLE_FLAG", (otvorenypreODFlag));
				// }
				// nazovStlpca = PASSENGER_START_VALIDITY
				// novaHodnota = dataRow.OTVORENY_PRE_OD_ZAC
				rowMap.put("PASSENGER_START_VALIDITY", CudVysielanieUtils.getStringDatum(otvorenypreODZac));
				// nazovStlpca = PASSENGER_END_VALIDITY
				// novaHodnota = dataRow.OTVORENY_PRE_OD_KON
				rowMap.put("PASSENGER_END_VALIDITY", CudVysielanieUtils.getStringDatum(otvorenypreODKon));
				// nazovStlpca = LONGITUDE
				// novaHodnota = dataRow.GPS_DLZKA
				rowMap.put("LONGITUDE", CudVysielanieUtils.getStringZaokruhleneNa6(dataRow.getGpsDlzka()));
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = LATITUDE
				// novaHodnota = dataRow.GPS_SIRKA
				rowMap.put("LATITUDE", CudVysielanieUtils.getStringZaokruhleneNa6(dataRow.getGpsSirka()));
				// Systém vytvorí updStlpce an naviaze ma updZaznam a naplní
				// nazovStlpca = FREE_TEXT
				// novaHodnota = dataRow.POZNAMKA
				rowMap.put("FREE_TEXT", dataRow.getPoznamka());
				rowMap.put("ACTIVE_FLAG", "T");
				// 11.2.2.3 Systém zavolá CUD WS CSCudService.updZmenaHodnotCiselnika
				res = dlg.getAktualizaciaLocZDbClass().cudServiseUpdate(auth, dtoImport, rowMap, datumNow);
				if (res.isError()) {
					return res;
				}

			} // end if ( spracujNewDB) {
		} catch (Throwable e) {
			res = new ActionResult();
			res.setError(true);
			chyba += e.getMessage();
			log.info("CudAktPrimLocZDbProcess rowMap PrimaryLocation: " + chyba);
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

			if ("ID_COMPANY".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("ID_NADRADENA_PRIMARNA".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("ID_SUBSIDIARY_TYPE".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("ID_VYSSI_UZEMNY_CELOK".equals(dto.getNazov())
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
			if ("STYK_DRAH".equals(dto.getNazov()) && !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("MANIPULACIA_S_KONTAJNERMI".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("OTVORENY_PRE_OD".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("OTVORENY_PRE_ND".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("OTVORENY_PRE_OD_ZAC".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}
			if ("OTVORENY_PRE_OD_KON".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("OTVORENY_PRE_ND_ZAC".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}

			if ("OTVORENY_PRE_ND_KON".equals(dto.getNazov())
					&& !CudVysielanieUtils.isEqual(dto.getNewValue(), dto.getOldValue())) {
				return true;
			}
		}
		return false;
	}

}
