package sk.ditec.crd;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.torque.util.BasePeer;
import org.apache.torque.util.MyCriteria2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.DTO;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImport;
import sk.ditec.cud.dto.DTOImportMsg;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOValidate;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWorkflow;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.proc.CudPauClass;
import sk.ditec.cud.utils.CudVysielanieUtils;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.dao.master.TDopravnyBodPeer;
import sk.ditec.dao.meta.CudCiselnikStlpecPeer;
import sk.ditec.dao.meta.CudZmenaPeer;
import sk.ditec.dao.meta.CudZmenaStavHistPeer;
import sk.ditec.dao.meta.CudZmenaStlpecPeer;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

import com.workingdogs.village.Record;

public class AktualizaciaLocZDbClass extends _NovyPISBaseClass {
	
	private Logger log = LoggerFactory.getLogger(AktualizaciaLocZDbClass.class);
	
	private _CudCrdDelegate dlg = new _CudCrdDelegate();
	private _CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	private CudPauClass cudPau = new CudPauClass();





	private String getindex(String operacia, String subOperacia) {
		if ("D".equals(subOperacia) || "Z".equals(subOperacia)) {
			return "1";
		}
		// $Operacia = "D"
		if ("D".equals(operacia) || "Z".equals(operacia)) {
			return "2";
		}
		// $Operacia = "U"
		if ("U".equals(operacia)) {
			return "3";
		}
		// $Operacia = "N"
		if ("N".equals(operacia)) {
			return "4";
		}
		// $SubOperacia = "U"
		if ("U".equals(subOperacia)) {
			return "5";
		}
		// $SubOperacia = "N"
		if ("N".equals(subOperacia)) {
			return "6";
		}

		return null;
	}

	public Date readMaxDatumZmeny(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			// CUD_ZMENA.CUD_CISELNIK = vstup:Číselník
			// A zároveň
			// CUD_ZMENA.PLATNOST_OD je MAX
			// Systém vráti MAX CUD_ZMENA.PLATNOST_OD

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());

			crit.addAsColumn("max", "max(" + CudZmenaPeer.PLATNOST_OD + ")");

			crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);
			// crit.addConditional(CudZmenaPeer.ROW_ID, rowID);

			crit.add(crit.getNewCriterion(CudZmenaPeer.STAV, _CudConsts.ZMENA_STAV_ZAM, MyCriteria2.NOT_EQUAL));

			// String s = CudZmenaPeer.PLATNOST_OD + " <= " + CudZmenaPeer.PLATNOST_DO;
			// Criterion c1 = crit.getNewCriterion(CudZmenaPeer.PLATNOST_OD, s, MyCriteria2.CUSTOM);
			// Criterion c2 = crit.getNewCriterion(CudZmenaPeer.PLATNOST_DO, null, MyCriteria2.ISNULL);
			// crit.add(c1.or(c2));

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			if (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, "max").asUtilDate();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "AktualizaciaLocZDbClass.readMaxPlatnostOd.error", auth);
			return null;
		}
	}

	public ArrayList<DTOZmena> getZmenyListVsetkyZmeny(AuthInfo auth, Date datumCasZmeny, Integer ciselnikID,
			String stav) throws AppException {
		try {
			// Popis: Systém vrái zmeny z registra ziem pre číselník a stav údajov s dátumu platnosti od väčším alebo
			// rovným ako dátum na vstupe.
			// Algoritmus:
			//
			// Systém vyberie zaznamy CUD_ZMENA také, že
			// CUD_ZMENA.CISELNIK_ID je v vs. ČíselníkIDList a zároveň
			// CUD_ZMENA.STAV je v vs. STAVList a zároveň
			// ak na vstupe vs. PlatnosťOd
			// CUD_ZMENA.PlatnosťOd >=vs. PlatnosťOd
			// záznamy vráť v usporiadaní
			// Ak PlatnosťOd na vstupe tak PLATNOST_OD vzostupne, ZMENA_ID vzostupne
			// Ak PlatnosťOd nie je na vstupe tak PLATNOST_OD zostupne, ZMENA_ID zostupne
			// (
			// T_DOPRAVNY_BOD.CAS_VYTVORENIA>=TO_DATE('11-06-2023 11:00:00', 'DD-MM-YYYY HH24:MI:SS')
			// OR
			// (T_DOPRAVNY_BOD.CAS_ZMENY IS not NULL AND T_DOPRAVNY_BOD.CAS_ZMENY>=TO_DATE('11-06-2023 11:00:00',
			// 'DD-MM-YYYY HH24:MI:SS') )
			// )

			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());
			CudZmenaPeer.addSelectColumns(crit);
			// crit.addAsColumn("max", "max(" + CudZmenaPeer.PLATNOST_OD + ")");

			crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudZmenaPeer.STAV, stav, MyCriteria2.EQUAL);
			crit.addJoin(TDopravnyBodPeer.ID_ZMENA, CudZmenaPeer.ZMENA_ID);

			MyCriteria2 crit2 = new MyCriteria2();
			crit2.addConditional(TDopravnyBodPeer.CAS_VYTVORENIA, datumCasZmeny, MyCriteria2.GREATER_EQUAL);
			// sql = crit.getCriterion(atribut).toString();
			// crit.addConditional(TDopravnyBodPeer.CAS_VYTVORENIA, datumCasZmeny, MyCriteria2.GREATER_EQUAL);

			String subsql = crit2.getCriterion(TDopravnyBodPeer.CAS_VYTVORENIA).toString() + " OR "
					+ CudVysielanieUtils.getCritGreaterEqualsActDateNotNull(TDopravnyBodPeer.CAS_ZMENY, datumCasZmeny);

			// sql += " OR "
			// + CudVysielanieUtils.getCritGreaterEqualsActDateNotNull(TDopravnyBodPeer.CAS_ZMENY, datumCasZmeny);

			// crit.addCustomSql(ZpWsPrijemPeer.DATUM_CAS_SPRAVY,
			// MyCriteriaUtils.getCritEqualsDateMs(ZpWsPrijemPeer.DATUM_CAS_SPRAVY, datumCasSpravy));
			// String s = TDopravnyBodPeer.CAS_VYTVORENIA + " > '2022-08-11 19:30:29.927000'
			// and
			// ( cas_zmeny is null or cas_zmeny >= '2023-03-15 13:59:51.436000')
			// crit.addConditional(CudZmenaPeer.PLATNOST_OD, platnostOd, MyCriteria2.GREATER_EQUAL);

			String sql = crit.getSQL() + " AND (" + subsql + ") order by " + CudZmenaPeer.ZMENA_ID;
			// order by zmena_id aby som zapisala poslednu vykonanu zmenu ako poslednu

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			ArrayList<DTOZmena> list = new ArrayList<DTOZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmena dto = new DTOZmena();
				dto.setZmenaID(rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setStav(rVal(r, CudZmenaPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudZmenaPeer.PLATNOST_DO).asUtilDate());
				dto.setCasSchvaleniaGr(rVal(r, CudZmenaPeer.CAS_SCHVALENIA_GR).asUtilDate());

				// dto.setCiselnikNazov(rVal(r, CudZmenaPeer.NAZOV).asString());
				// dto.setCiselnikTabulka(rVal(r, CudZmenaPeer.TABULKA).asString());

				list.add(dto);
			}

			return list;

		} catch (Throwable t) {
			handleException(t, "AktualizaciaLocZDbClass.getZmenyList", auth);
			return null;
		}
	}

	public ArrayList<DTOZmena> getZmenyList(AuthInfo auth, Date datumCasZmeny, Integer[] idCiselnikovList, String stav)
			throws AppException {
		try {
			// Systém vyberie zaznamy CUD_ZMENA také, že
			// CUD_ZMENA.CISELNIK_ID je v vs. ČíselníkIDList a zároveň
			// CUD_ZMENA.STAV=PAU a zároveň
			// ak na vstupe vs. DátumDo tak potom musí existovať - datumDo nie je definovany
			// CUD_ZMENA.CUD_ZMENA_STAV_HIST kde STAV=PAU a CAS_VYTVORENIA <vs. dátumDo
			// a zároveň
			// ak na vstupe vs. DátumOd tak potom musí existovať
			// CUD_ZMENA.CUD_ZMENA_STAV_HIST kde STAV=PAU a CAS_VYTVORENIA >=vs. dátumOd
			// záznamy vráť v usporiadaní
			// CAS_VYTVORENIA zo CUD_ZMENA_STAV_HIST pre stav PAU vzostupne, ZMENA_ID vzostupne

			// SELECT *
			// FROM
			// CUD_ZMENA --on d.ID_ZMENA=z.ZMENA_ID AND z.STAV='PAU'
			// left join CUD_ZMENA_STAV_HIST h on h.id_zmena=cud_zmena.zmena_id and cud_zmena.STAV=h.stav
			// --left join CUD_ZMENA_STAV_HIST h on h.ID_ZMENA=cud_zmena.zmena_id
			// -- and h.STAV=cud_zmena.stav
			// WHERE
			// cud_zmena.id_ciselnik in (6,9)
			// and CUD_ZMENA.stav = 'PAU'
			// AND CUD_ZMENA.platnost_od>=TO_DATE('01-01-2024 00:00:00', 'DD-MM-YYYY HH24:MI:SS')
			MyCriteria2 crit = new MyCriteria2(CudZmenaPeer.ZMENA_ID, new DTOZmena());
			CudZmenaPeer.addSelectColumns(crit);
			crit.addInConditional(CudZmenaPeer.ID_CISELNIK, idCiselnikovList);
			// crit.addConditional(CudZmenaPeer.ID_CISELNIK, ciselnikID);
			crit.addConditional(CudZmenaPeer.STAV, stav, MyCriteria2.EQUAL);
			crit.addJoin(CudZmenaStavHistPeer.ID_ZMENA, CudZmenaPeer.ZMENA_ID);
			// CUD_ZMENA.CUD_ZMENA_STAV_HIST kde STAV=PAU a CAS_VYTVORENIA >=vs. dátumOd
			// crit.addConditional(CudZmenaPeer.PLATNOST_OD, datumCasZmeny, MyCriteria2.GREATER_EQUAL);
			crit.addConditional(CudZmenaStavHistPeer.CAS_VYTVORENIA, datumCasZmeny, MyCriteria2.GREATER_EQUAL);
			crit.addConditional(CudZmenaStavHistPeer.STAV, stav);
			// TODO: pre ucely ladenia
			// crit.addCustomSql(CudZmenaPeer.ZMENA_ID, "ZMENA_ID in (703407) ");
			crit.addAscendingOrderByColumn(CudZmenaStavHistPeer.ID_ZMENA);


			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			ArrayList<DTOZmena> list = new ArrayList<DTOZmena>();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();

				DTOZmena dto = new DTOZmena();
				dto.setZmenaID(rVal(r, CudZmenaPeer.ZMENA_ID).asIntegerObj());
				dto.setIDCiselnik(rVal(r, CudZmenaPeer.ID_CISELNIK).asIntegerObj());
				dto.setRowID(rVal(r, CudZmenaPeer.ROW_ID).asIntegerObj());
				dto.setOperacia(rVal(r, CudZmenaPeer.OPERACIA).asString());
				dto.setStav(rVal(r, CudZmenaPeer.STAV).asString());
				dto.setPlatnostOd(rVal(r, CudZmenaPeer.PLATNOST_OD).asUtilDate());
				dto.setPlatnostDo(rVal(r, CudZmenaPeer.PLATNOST_DO).asUtilDate());
				dto.setCasSchvaleniaGr(rVal(r, CudZmenaPeer.CAS_SCHVALENIA_GR).asUtilDate());

				// dto.setCiselnikNazov(rVal(r, CudZmenaPeer.NAZOV).asString());
				// dto.setCiselnikTabulka(rVal(r, CudZmenaPeer.TABULKA).asString());

				list.add(dto);
			}

			return list;

		} catch (Throwable t) {
			handleException(t, "AktualizaciaLocZDbClass.getZmenyList", auth);
			return null;
		}
	}

	public static Integer getIdStlpec(AuthInfo auth, Integer idCiselnik, String nazovStlpca) throws AppException {
		// checkPermission(auth, getDelegate().getReadPermission());
		try {

			MyCriteria2 crit = new MyCriteria2(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID, new DTOCiselnikStlpec());
			crit.addSelectColumn(CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID);
			crit.addConditional(CudCiselnikStlpecPeer.ID_CISELNIK, idCiselnik);
			crit.addConditional(CudCiselnikStlpecPeer.NAZOV, nazovStlpca);

			crit.add(CudCiselnikStlpecPeer.ID_TRANSAKCIA_ZRUSENE, null);

			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();
			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				return rVal(r, CudCiselnikStlpecPeer.CISELNIK_STLPEC_ID).asIntegerObj();
			}

			return null;

		} catch (Throwable t) {
			handleException(t, "getIdStlpec.error", auth);
			return null;
		}
	}



	public static String getZmenaCrd(AuthInfo auth, Integer idCiselnikStlpec, boolean zaciatok) throws AppException {

		try {
			MyCriteria2 crit = new MyCriteria2(CudZmenaStlpecPeer.ZMENA_STLPEC_ID, CudZmenaStlpecPeer.TABLE_NAME);

			crit.addSelectColumn(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC);
			crit.addSelectColumn(CudZmenaStlpecPeer.OLD_VALUE);
			crit.addSelectColumn(CudZmenaStlpecPeer.NEW_VALUE);

			crit.addConditional(CudZmenaStlpecPeer.ID_CISELNIK_STLPEC, idCiselnikStlpec);
			// if (zaciatok) {
			// crit.addConditional(CudZmenaStlpecPeer.NEW_VALUE,"T");
			// } else {
			// crit.addConditional(CudZmenaStlpecPeer.OLD_VALUE,"T");
			// }
			String sql = crit.getSQL();

			getConnection(auth);
			List<?> lp = BasePeer.executeQuery(sql, false, auth.T);
			returnConnection(auth);

			Iterator<?> iter = lp.iterator();

			while (iter.hasNext()) {
				Record r = (Record) iter.next();
				if (zaciatok) {
					return rVal(r, CudZmenaStlpecPeer.NEW_VALUE).asString();
				} else {
					return rVal(r, CudZmenaStlpecPeer.OLD_VALUE).asString();
				}
			}
		} catch (Throwable t) {
			handleException(t, "getZmenaCrd.error", auth);
			return null;
		}
		return null;
	}

	public static Comparator<DTOZmena> comparebyindex = new Comparator<DTOZmena>() {
		@Override
		public int compare(DTOZmena o1, DTOZmena o2) {
			int i = o1.getIndex().compareTo(o2.getIndex());
			return i;
		};
	};

	public static String getString(Integer value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	};

	public static String getString(Double value) {
		if (value == null) {
			return null;
		}
		return value.toString();
	};




	public class dtoZmena extends DTO {
		String oldValue;
		String newValue;
		String nazov;
		Integer idZmenaStlpca;

		public String getOldValue() {
			return oldValue;
		}

		public void setOldValue(String oldValue) {
			this.oldValue = oldValue;
		}

		public String getNewValue() {
			return newValue;
		}

		public void setNewValue(String newValue) {
			this.newValue = newValue;
		}

		public String getNazov() {
			return nazov;
		}

		public void setNazov(String nazov) {
			this.nazov = nazov;
		}

		public Integer getIdZmenaStlpca() {
			return idZmenaStlpca;
		}

		public void setIdZmenaStlpca(Integer idZmenaStlpca) {
			this.idZmenaStlpca = idZmenaStlpca;
		}

	}

	public ActionResult cudServiseUpdate(AuthInfo auth, DTOImport dtoImport, Map<String, String> rowMap,
			Date datumCasAkt) throws AppException {

		getConnection(auth);
		ActionResult res = new ActionResult();
		try {
			DTOValidate dtoVal = DTOValidate.createDTO(dtoImport, _CudConsts.ZDROJ_XLS, new Date(), null, null);

			// DTOZmena dtoZmenaZap = null;
			Map<String, List<DTOCiselnikStlpecGui>> metaMap = new HashMap<String, List<DTOCiselnikStlpecGui>>();
			List<DTOCiselnikStlpec> csList = dlgcud.getCiselnikStlpecRead().listLight(auth, dtoImport.getIDCiselnik());
			dlgcud.getValidation().validateMaster(auth, dtoVal, metaMap, rowMap, csList);
			String subChyba = "";
			if ("T".equals(dtoVal.getImportZmenaDTO().getErrors())) {
				// navratovyKod = 4;
				for (DTOImportMsg dto : dtoVal.getImportZmenaDTO().getImportMsgList()) {
					subChyba += dto.getMsg();
					// popisSpracovania += dto.getMsg();
				}
				if (!subChyba.startsWith("neobsahuje žiadnu zmenu")) {
					res.setError(true);
					res.setErrorMsg(subChyba);
					rollbackConnection(auth);
					// /break;
					return res;
				}
			}

			Map<Integer, DTOUcet[]> ucetMap = new HashMap<Integer, DTOUcet[]>();

			List<DTOWfDef> wfDefList = dlgcud.getWfDefRead().list(auth, dtoImport.getIDCiselnik());

			// Integer totalCount = dlgcud.getImportZmenaRead().pocet(auth, dtoImport.getImportID());

			if (StringUtils.isValid(dtoImport.getIDCiselnik())) {
				DTOWorkflow dtoWf = dlgcud.getWorkflow().generujWorkflowAll(auth, dtoImport.getIDCiselnik(),
						dtoVal.getImportZmenaDTO(), wfDefList, ucetMap);
				if (StringUtils.isValid(dtoWf)) {

					res = cudPau.workflowUpdateCrd(auth, dtoWf, dtoVal.getImportZmenaDTO(), datumCasAkt);
					// sendNotif(auth, dtoCis, dtoVal, dtoWf, wfDefList, metaMapForSend, fkMetaMap);
					DTOZmena dtoZmenaZap = (DTOZmena) res.getResult();
					// if (dtoZmenaZap != null) {
					// spracovane += "UICCode=" + ws.getLocationCode() + " ; ";
					// dlg.getCrdAktualizujCiselnikClass().aktualizujCiselnik(auth, dtoZmenaZap);
					// popisSpracovania = "Úspešné spracovanie";
					// }
					if (dtoZmenaZap != null) {
						String aktualizacia = dlg.getCrdAktualizujCiselnikClass().aktualizujCiselnik(auth, dtoZmenaZap);

						if (!"OK".equals(aktualizacia)) {
							String chyba = "Chyba pri aktualizacii " + aktualizacia + rowMap;
							rollbackConnection(auth);
							res.setError(true);
							res.setErrorMsg(chyba);
							return res;
					}
				}
			}
			}
		} catch (Throwable e) { // ked nejaky zapis Andreja
			String chyba = e.getMessage() + rowMap;
			rollbackConnection(auth);
			res.setError(true);
			res.setErrorMsg(chyba);
			return res;
		}
		returnConnection(auth);
		return res;
	}

}
