package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.DateUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.data.ws.dto.DTOCiselnikDataWS;
import sk.ditec.cud.data.ws.dto.DTOCiselnikMetaWS;
import sk.ditec.cud.data.ws.dto.DTOCiselnikStlpecMetaWS;
import sk.ditec.cud.data.ws.dto.DTOPageWS;
import sk.ditec.cud.data.ws.dto.DTORecordWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaStlpecWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaWS;
import sk.ditec.cud.data.ws.dto.DTOZmenaWrapperWS;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.kmd.data.ws.KMDDataService;
import sk.ditec.kmd.data.ws.dto.DTOCiselnikRecord;
import sk.ditec.kmd.data.ws.dto.DTOWSAtributZmeny;
import sk.ditec.kmd.data.ws.dto.DTOWSCiselnik;
import sk.ditec.kmd.data.ws.dto.DTOWSCiselnikMeta;
import sk.ditec.kmd.data.ws.dto.DTOWSStlpec;
import sk.ditec.kmd.data.ws.dto.DTOWSZmena;
import sk.ditec.zsr.common.server.auth.ZSRAuthInfo;

public class KmdDataWSClass extends _CudBaseClass {

	private Logger log = LoggerFactory.getLogger(KMDDataService.class);

	private DTOWSStlpec copy(DTOCiselnikStlpecMetaWS dto) throws AppException {

		try {
			DTOWSStlpec dtoWS = new DTOWSStlpec();
			dtoWS.setStlpecID(dto.getCiselnikStlpecID());
			dtoWS.setIDCiselnik(dto.getIDCiselnik());
			dtoWS.setNazov(dto.getNazov());
			dtoWS.setTyp(dto.getTyp());
			dtoWS.setNadpis(dto.getNadpis());
			dtoWS.setDlzka(dto.getDlzka());
			dtoWS.setDbTyp(dto.getDbTyp());
			dtoWS.setPovinne(dto.getPovinny());
			dtoWS.setJedinecne(dto.getJedinecny());
			dtoWS.setPopis(dto.getPopis());
			dtoWS.setFk1IDCiselnik(dto.getFk1IDCiselnik());
			dtoWS.setFk1Tabulka(dto.getFk1Tabulka());
			dtoWS.setFk1PkNazov(dto.getFk1PkNazov());
			dtoWS.setDecimals(dto.getDecimals());
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private DTOWSCiselnikMeta copy(DTOCiselnikMetaWS dto) throws AppException {

		try {
			DTOWSCiselnikMeta dtoWS = new DTOWSCiselnikMeta();
			dtoWS.setCiselnikID(dto.getCiselnikID());
			dtoWS.setTabulka(dto.getTabulka());
			dtoWS.setNazov(dto.getNazov());
			dtoWS.setPopis(dto.getPopis());
			dtoWS.setStlpce(copy(dto.getCiselnikStlpecList()));
			dtoWS.setAktivny("T");
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private ArrayList<DTOWSCiselnikMeta> copy(DTOCiselnikMetaWS[] listDTO) throws AppException {

		try {
			ArrayList<DTOWSCiselnikMeta> listWS = new ArrayList<DTOWSCiselnikMeta>();
			if (StringUtils.isValid(listDTO)) {
				for (DTOCiselnikMetaWS dto : listDTO) {
					listWS.add(copy(dto));
				}
			}

			return listWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private ArrayList<DTOWSStlpec> copy(DTOCiselnikStlpecMetaWS[] listDTO) throws AppException {

		try {
			ArrayList<DTOWSStlpec> listWS = null;
			if (StringUtils.isValid(listDTO)) {
				listWS = new ArrayList<DTOWSStlpec>();
				for (DTOCiselnikStlpecMetaWS dto : listDTO) {
					listWS.add(copy(dto));
				}
			}

			return listWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private DTOCiselnikRecord copy(DTORecordWS dto) throws AppException {

		try {
			DTOCiselnikRecord dtoWS = new DTOCiselnikRecord();
			if (StringUtils.isValid(dto.getValues())) {
				ArrayList<Object> listWS = new ArrayList<Object>();
				for (Object value : dto.getValues()) {
					listWS.add(value);
				}
				dtoWS.setValues(listWS);
			}
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private ArrayList<DTOCiselnikRecord> copy(DTORecordWS[] recordList) throws AppException {

		try {
			ArrayList<DTOCiselnikRecord> listWS = null;
			if (StringUtils.isValid(recordList)) {
				listWS = new ArrayList<DTOCiselnikRecord>();
				for (DTORecordWS dto : recordList) {
					listWS.add(copy(dto));
				}
			}
			return listWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private DTOWSCiselnik copy(DTOCiselnikDataWS dto) throws AppException {

		try {
			DTOWSCiselnik dtoWS = new DTOWSCiselnik();
			dtoWS.setCiselnikID(dto.getCiselnikID());
			dtoWS.setCiselnikName(dto.getCiselnikName());
			dtoWS.setCiselnikNazov(dto.getCiselnikNazov());
			dtoWS.setStlpce(copy(dto.getCiselnikStlpecList()));
			dtoWS.setData(copy(dto.getRecordList()));
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private DTOWSAtributZmeny copy(DTOZmenaStlpecWS dto) throws AppException {

		try {
			DTOWSAtributZmeny dtoWS = new DTOWSAtributZmeny();
			dtoWS.setOldValue(dto.getOldValue());
			dtoWS.setNewValue(dto.getNewValue());
			dtoWS.setNazov(dto.getNazov());
			dtoWS.setNadpis(dto.getNadpis());
			dtoWS.setDbTyp(dto.getDbTyp());
			dtoWS.setTyp(dto.getTyp());
			dtoWS.setDbDlzka(dto.getDbDlzka());
			dtoWS.setFkTabulka(dto.getFkTabulka());
			dtoWS.setFkPK(dto.getFkPk());
			dtoWS.setFkStlpec(dto.getFkStlpec());
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private ArrayList<DTOWSAtributZmeny> copy(DTOZmenaStlpecWS[] zsList) throws AppException {

		try {
			ArrayList<DTOWSAtributZmeny> listWS = null;
			if (StringUtils.isValid(zsList)) {
				listWS = new ArrayList<DTOWSAtributZmeny>();
				for (DTOZmenaStlpecWS dto : zsList) {
					listWS.add(copy(dto));
				}
			}
			return listWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private DTOWSZmena copy(DTOZmenaWS dto) throws AppException {

		try {
			DTOWSZmena dtoWS = new DTOWSZmena();
			dtoWS.setZmenaID(dto.getZmenaID());
			dtoWS.setRowID(dto.getRowID());
			dtoWS.setStav(dto.getStav());
			dtoWS.setOperacia(dto.getOperacia());
			dtoWS.setCiselnikID(dto.getCiselnikID());
			dtoWS.setTabulka(dto.getTabulka());
			dtoWS.setPlatnostOd(dto.getPlatnostOd());
			dtoWS.setAtributy(copy(dto.getZmenaStlpecList()));
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private ArrayList<DTOWSZmena> copy(DTOZmenaWrapperWS dtoWrap) throws AppException {

		try {
			ArrayList<DTOWSZmena> listWS = null;
			if (StringUtils.isValid(dtoWrap) && StringUtils.isValid(dtoWrap.getZmenaList())) {
				listWS = new ArrayList<DTOWSZmena>();
				for (DTOZmenaWS dto : dtoWrap.getZmenaList()) {
					listWS.add(copy(dto));
				}
			}
			return listWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	public ArrayList<DTOWSCiselnikMeta> getCiselnikyMeta(AuthInfo auth) throws AppException {

		try {
			DTOCiselnikMetaWS[] listDTO = getDelegate().getDataOldWS().ciselnikMetaList(auth, null);
			for (DTOCiselnikMetaWS dto : listDTO) {
				DTOCiselnikStlpecMetaWS[] csList = getDelegate().getDataOldWS().ciselnikStlpecMetaList(auth, dto.getCiselnikID(), null, null);
				if (StringUtils.isValid(csList)) {
					dto.setCiselnikStlpecList(csList);
				}

			}
			return copy(listDTO);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikyMeta.error");
			return null;
		}
	}

	public DTOWSCiselnikMeta getCiselnikMeta(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			DTOCiselnikMetaWS dto = getDelegate().getDataOldWS().ciselnikMetaRead(auth, ciselnikID, null, null, false);
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			handleException(e, "getCiselnikMeta.error");
			return null;
		}
	}

	public ArrayList<DTOWSStlpec> getStlpce(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			DTOCiselnikStlpecMetaWS[] listDTO = getDelegate().getDataOldWS().ciselnikStlpecMetaList(auth, ciselnikID, null, null);
			if (StringUtils.isValid(listDTO) && StringUtils.isValid(listDTO[0].getErrorMsg())) {
				log.error(listDTO[0].getErrorMsg());
			}
			return copy(listDTO);

		} catch (Exception e) {
			DBUtils.handleException(e, "getStlpce.error");
			return null;
		}
	}

	private DTOPageWS createDTOPageWS(Integer pageSize) throws AppException {

		try {
			DTOPageWS page = new DTOPageWS();
			page.setPage(1);
			page.setPageSize(pageSize);
			return page;

		} catch (Exception e) {
			DBUtils.handleException(e, "createDTOPageWS.error");
			return null;
		}
	}

	public DTOWSCiselnik getCiselnikData(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			Date d = DateUtils.removeTime(new Date());
			DTOCiselnikDataWS dto = getDelegate().getDataOldWS().ciselnikDataListOdDo(auth, ciselnikID, null, createDTOPageWS(_CudConsts.WS_MAX_POCET), null, d, d);
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikData.error");
			return null;
		}
	}

	public DTOWSCiselnik getCiselnikOdDo(AuthInfo auth, Integer ciselnikID, Date d) throws AppException {

		try {
			DTOCiselnikDataWS dto = getDelegate().getDataOldWS().ciselnikDataListOdDo(auth, ciselnikID, null, createDTOPageWS(_CudConsts.WS_MAX_POCET), null, d, d);
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikOdDo.error");
			return null;
		}
	}

	public DTOWSCiselnik getCiselnikToDate(ZSRAuthInfo auth, Integer ciselnikID, Date d) throws AppException {

		try {
			DTOCiselnikDataWS dto = getDelegate().getDataOldWS().getCiselnikDataToDate(auth, ciselnikID, d, null, createDTOPageWS(_CudConsts.WS_MAX_POCET));
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikToDate.error");
			return null;
		}
	}

	public ArrayList<DTOWSZmena> getZmenyListDatumOd(AuthInfo auth, Integer ciselnikID, Date d) throws AppException {

		try {
			DTOZmenaWrapperWS dto = getDelegate().getDataOldWS().getZmenyListDatumOd(auth, new Integer[] { ciselnikID }, d, null, createDTOPageWS(100));
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListDatumOd.error");
			return null;
		}
	}

	public ArrayList<DTOWSZmena> getZmenyListDatumOdDo(AuthInfo auth, Integer ciselnikID, Date datumOd, Date datumDo) throws AppException {

		try {
			DTOZmenaWrapperWS dto = getDelegate().getDataOldWS().getZmenyListDatumOdDo(auth, new Integer[] { ciselnikID }, datumOd, datumDo, null, createDTOPageWS(100));
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListDatumOdDo.error");
			return null;
		}
	}

	public ArrayList<DTOWSZmena> getZmenyListSchvaleneDatumOd(AuthInfo auth, Integer ciselnikID, Date d) throws AppException {

		try {
			DTOZmenaWrapperWS dto = getDelegate().getDataOldWS().getZmenyListSchvaleneDatumOd(auth, new Integer[] { ciselnikID }, d, null, createDTOPageWS(100));
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListSchvaleneDatumOd.error");
			return null;
		}
	}

	public ArrayList<DTOWSZmena> getZmenyListSchvaleneDatumOdDo(AuthInfo auth, Integer ciselnikID, Date datumOd, Date datumDo) throws AppException {

		try {
			DTOZmenaWrapperWS dto = getDelegate().getDataOldWS().getZmenyListSchvaleneDatumOdDo(auth, new Integer[] { ciselnikID }, datumOd, datumDo, null, createDTOPageWS(100));
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListSchvaleneDatumOdDo.error");
			return null;
		}
	}

	public ArrayList<DTOWSZmena> getZmeny(AuthInfo auth, Date datumOd) throws AppException {

		try {
			DTOZmenaWrapperWS dto = getDelegate().getDataOldWS().getZmeny(auth, datumOd, createDTOPageWS(100), _CudConsts.ZMENA_STAV_PAU);
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmeny.error");
			return null;
		}
	}

	public ArrayList<DTOWSZmena> getZmenyDatumDo(AuthInfo auth, Date datumOd, Date datumDo) throws AppException {

		try {
			DTOZmenaWrapperWS dto = getDelegate().getDataOldWS().getZmenyDatumDo(auth, datumOd, datumDo, createDTOPageWS(100), _CudConsts.ZMENA_STAV_PAU);
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyDatumDo.error");
			return null;
		}
	}

	public ArrayList<DTOWSZmena> getZmenySchvalene(AuthInfo auth, Date datumOd) throws AppException {

		try {
			DTOZmenaWrapperWS dto = getDelegate().getDataOldWS().getZmeny(auth, datumOd, createDTOPageWS(100), _CudConsts.ZMENA_STAV_SCH);
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenySchvalene.error");
			return null;
		}
	}

	public ArrayList<DTOWSZmena> getZmenySchvaleneDatumDo(AuthInfo auth, Date datumOd, Date datumDo) throws AppException {

		try {
			DTOZmenaWrapperWS dto = getDelegate().getDataOldWS().getZmenyDatumDo(auth, datumOd, datumDo, createDTOPageWS(100), _CudConsts.ZMENA_STAV_SCH);
			if (StringUtils.isValid(dto.getErrorMsg())) {
				log.error(dto.getErrorMsg());
			}
			return copy(dto);

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenySchvaleneDatumDo.error");
			return null;
		}
	}

}
