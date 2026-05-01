package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
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
import sk.ditec.zsr.common.server.auth.ZSRAuthInfo;

public class CskmdDataWSClass extends _CudBaseClass {

	private sk.ditec.cskmd.data.ws.dto.DTOCiselnikStlpecWS copy(DTOCiselnikStlpecMetaWS dto) throws AppException {

		try {
			sk.ditec.cskmd.data.ws.dto.DTOCiselnikStlpecWS dtoWS = new sk.ditec.cskmd.data.ws.dto.DTOCiselnikStlpecWS();
			dtoWS.setCiselnikStlpecID(dto.getCiselnikStlpecID());
			dtoWS.setIDCiselnik(dto.getIDCiselnik());
			dtoWS.setNazov(dto.getNazov());
			dtoWS.setTyp(dto.getTyp());
			dtoWS.setNadpis(dto.getNadpis());
			dtoWS.setDlzka(dto.getDlzka());
			dtoWS.setDbTyp(dto.getDbTyp());
			dtoWS.setPovinny(dto.getPovinny());
			dtoWS.setJedinecny(dto.getJedinecny());
			dtoWS.setPopis(dto.getPopis());
			dtoWS.setFkIDCiselnik(dto.getFk1IDCiselnik());
			dtoWS.setFkTabulka(dto.getFk1Tabulka());
			dtoWS.setFkPkNazov(dto.getFk1PkNazov());
			dtoWS.setDecimals(dto.getDecimals());
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS copy(DTOCiselnikMetaWS dto) throws AppException {

		try {
			sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS dtoWS = new sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS();
			dtoWS.setCiselnikID(dto.getCiselnikID());
			dtoWS.setTabulka(dto.getTabulka());
			dtoWS.setNazov(dto.getNazov());
			dtoWS.setPopis(dto.getPopis());
			dtoWS.setCiselnikStlpecList(copy(dto.getCiselnikStlpecList()));
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS[] copy(DTOCiselnikMetaWS[] listDTO) throws AppException {

		try {
			List<sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS> listWS = new ArrayList<sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS>();
			if (StringUtils.isValid(listDTO)) {
				listWS = new ArrayList<sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS>();
				for (DTOCiselnikMetaWS dto : listDTO) {
					listWS.add(copy(dto));
				}
			}

			return listWS.toArray(new sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS[listWS.size()]);

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTOCiselnikStlpecWS[] copy(DTOCiselnikStlpecMetaWS[] listDTO) throws AppException {

		try {
			if (StringUtils.isValid(listDTO)) {
				List<sk.ditec.cskmd.data.ws.dto.DTOCiselnikStlpecWS> listWS = new ArrayList<sk.ditec.cskmd.data.ws.dto.DTOCiselnikStlpecWS>();
				for (DTOCiselnikStlpecMetaWS dto : listDTO) {
					listWS.add(copy(dto));
				}
				return listWS.toArray(new sk.ditec.cskmd.data.ws.dto.DTOCiselnikStlpecWS[listWS.size()]);
			}

			return null;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTORecordWS copy(DTORecordWS dto) throws AppException {

		try {
			sk.ditec.cskmd.data.ws.dto.DTORecordWS dtoWS = new sk.ditec.cskmd.data.ws.dto.DTORecordWS();
			if (StringUtils.isValid(dto.getValues())) {
				List<Object> listWS = new ArrayList<Object>();
				for (Object value : dto.getValues()) {
					listWS.add(value);
				}
				dtoWS.setValues(listWS.toArray(new Object[listWS.size()]));
			}
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTORecordWS[] copy(DTORecordWS[] recordList) throws AppException {

		try {
			if (StringUtils.isValid(recordList)) {
				List<sk.ditec.cskmd.data.ws.dto.DTORecordWS> listWS = new ArrayList<sk.ditec.cskmd.data.ws.dto.DTORecordWS>();
				for (DTORecordWS dto : recordList) {
					listWS.add(copy(dto));
				}
				return listWS.toArray(new sk.ditec.cskmd.data.ws.dto.DTORecordWS[listWS.size()]);
			}
			return null;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTOCiselnikDataWS copy(DTOCiselnikDataWS dto) throws AppException {

		try {
			sk.ditec.cskmd.data.ws.dto.DTOCiselnikDataWS dtoWS = new sk.ditec.cskmd.data.ws.dto.DTOCiselnikDataWS();
			dtoWS.setCiselnikID(dto.getCiselnikID());
			dtoWS.setCiselnikName(dto.getCiselnikName());
			dtoWS.setCiselnikNazov(dto.getCiselnikNazov());
			dtoWS.setTotalCount(dto.getTotalCount());
			dtoWS.setCiselnikStlpecList(copy(dto.getCiselnikStlpecList()));
			dtoWS.setData(copy(dto.getRecordList()));
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTOZmenaWrapperWS copy(DTOZmenaWrapperWS dto) throws AppException {

		try {
			sk.ditec.cskmd.data.ws.dto.DTOZmenaWrapperWS dtoWS = new sk.ditec.cskmd.data.ws.dto.DTOZmenaWrapperWS();
			dtoWS.setZmenaList(copy(dto.getZmenaList()));
			dtoWS.setTotalCount(dto.getTotalCount());
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTOZmenaWS[] copy(DTOZmenaWS[] listDTO) throws AppException {

		try {
			if (StringUtils.isValid(listDTO)) {
				List<sk.ditec.cskmd.data.ws.dto.DTOZmenaWS> listWS = new ArrayList<sk.ditec.cskmd.data.ws.dto.DTOZmenaWS>();
				for (DTOZmenaWS dto : listDTO) {
					listWS.add(copy(dto));
				}
				return listWS.toArray(new sk.ditec.cskmd.data.ws.dto.DTOZmenaWS[listWS.size()]);
			}
			return null;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTOZmenaWS copy(DTOZmenaWS dto) throws AppException {

		try {
			sk.ditec.cskmd.data.ws.dto.DTOZmenaWS dtoWS = new sk.ditec.cskmd.data.ws.dto.DTOZmenaWS();
			dtoWS.setZmenaID(dto.getZmenaID());
			dtoWS.setRowID(dto.getRowID());
			dtoWS.setStav(dto.getStav());
			dtoWS.setIDCiselnik(dto.getCiselnikID());
			dtoWS.setTabulka(dto.getTabulka());
			dtoWS.setPlatnostOd(dto.getPlatnostOd());
			dtoWS.setSchvalenie(dto.getSchvalenie());
			dtoWS.setPublikovanie(dto.getPublikovanie());
			dtoWS.setZmenaStlpecList(copy(dto.getZmenaStlpecList()));
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTOZmenaStlpecWS[] copy(DTOZmenaStlpecWS[] listDTO) throws AppException {

		try {
			if (StringUtils.isValid(listDTO)) {
				List<sk.ditec.cskmd.data.ws.dto.DTOZmenaStlpecWS> listWS = new ArrayList<sk.ditec.cskmd.data.ws.dto.DTOZmenaStlpecWS>();
				for (DTOZmenaStlpecWS dto : listDTO) {
					listWS.add(copy(dto));
				}
				return listWS.toArray(new sk.ditec.cskmd.data.ws.dto.DTOZmenaStlpecWS[listWS.size()]);
			}
			return null;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	private sk.ditec.cskmd.data.ws.dto.DTOZmenaStlpecWS copy(DTOZmenaStlpecWS dto) throws AppException {

		try {
			sk.ditec.cskmd.data.ws.dto.DTOZmenaStlpecWS dtoWS = new sk.ditec.cskmd.data.ws.dto.DTOZmenaStlpecWS();
			dtoWS.setZmenaStlpecID(dto.getZmenaStlpecID());
			dtoWS.setOldValue(dto.getOldValue());
			dtoWS.setNewValue(dto.getNewValue());
			dtoWS.setIDCiselnikStlpec(dto.getIDCiselnikStlpec());
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

	private DTOPageWS copy(sk.ditec.cskmd.data.ws.dto.DTOPageWS dto) throws AppException {

		try {
			DTOPageWS dtoWS = new DTOPageWS();
			dtoWS.setPage(StringUtils.isValid(dto) && StringUtils.isValid(dto.getPage()) ? dto.getPage() : 1);
			dtoWS.setPageSize(StringUtils.isValid(dto) && StringUtils.isValid(dto.getPage()) ? dto.getPageSize() : _CudConsts.WS_MAX_POCET);
			return dtoWS;

		} catch (Exception e) {
			DBUtils.handleException(e, "copy.error");
			return null;
		}
	}

	public sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS[] ciselnikMetaList(AuthInfo auth) throws AppException {

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
			DBUtils.handleException(e, "ciselnikMetaList.error");
			return null;
		}
	}

	public sk.ditec.cskmd.data.ws.dto.DTOCiselnikWS ciselnikMetaRead(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			return copy(getDelegate().getDataOldWS().ciselnikMetaRead(auth, ciselnikID, null, null, false));

		} catch (Exception e) {
			handleException(e, "ciselnikMetaRead.error");
			return null;
		}
	}

	public sk.ditec.cskmd.data.ws.dto.DTOCiselnikStlpecWS[] ciselnikStlpecMetaList(AuthInfo auth, Integer ciselnikID) throws AppException {

		try {
			return copy(getDelegate().getDataOldWS().ciselnikStlpecMetaList(auth, ciselnikID, null, null));

		} catch (Exception e) {
			DBUtils.handleException(e, "ciselnikStlpecMetaList.error");
			return null;
		}
	}

	public sk.ditec.cskmd.data.ws.dto.DTOCiselnikDataWS getCiselnikDataToDate(ZSRAuthInfo auth, Integer ciselnikID, Date d, sk.ditec.cskmd.data.ws.dto.DTOPageWS pageWS) throws AppException {

		try {
			return copy(getDelegate().getDataOldWS().getCiselnikDataToDate(auth, ciselnikID, d, null, copy(pageWS)));

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataToDate.error");
			return null;
		}
	}

	public sk.ditec.cskmd.data.ws.dto.DTOCiselnikDataWS getCiselnikDataDatumOdDo(ZSRAuthInfo auth, Integer ciselnikID, sk.ditec.cskmd.data.ws.dto.DTOPageWS pageWS) throws AppException {

		try {
			return copy(getDelegate().getDataOldWS().ciselnikDataListOdDo(auth, ciselnikID, null, copy(pageWS), null, null, null));

		} catch (Exception e) {
			DBUtils.handleException(e, "getCiselnikDataToDate.error");
			return null;
		}
	}

	public sk.ditec.cskmd.data.ws.dto.DTOZmenaWrapperWS getZmenyListDatumOd(AuthInfo auth, Integer[] ciselnikIDs, Date d, sk.ditec.cskmd.data.ws.dto.DTOPageWS pageWS) throws AppException {

		try {
			return copy(getDelegate().getDataOldWS().getZmenyListDatumOd(auth, ciselnikIDs, d, null, copy(pageWS)));

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListDatumOd.error");
			return null;
		}
	}

	public sk.ditec.cskmd.data.ws.dto.DTOZmenaWrapperWS getZmenyListDatumOdDo(AuthInfo auth, Integer[] ciselnikIDs, Date datumOd, Date datumDo, sk.ditec.cskmd.data.ws.dto.DTOPageWS pageWS) throws AppException {

		try {
			return copy(getDelegate().getDataOldWS().getZmenyListDatumOdDo(auth, ciselnikIDs, datumOd, datumDo, null, copy(pageWS)));

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListDatumOdDo.error");
			return null;
		}
	}

	public sk.ditec.cskmd.data.ws.dto.DTOZmenaWrapperWS getZmenyListSchvaleneDatumOd(AuthInfo auth, Integer[] ciselnikIDs, Date d, sk.ditec.cskmd.data.ws.dto.DTOPageWS pageWS) throws AppException {

		try {
			return copy(getDelegate().getDataOldWS().getZmenyListSchvaleneDatumOd(auth, ciselnikIDs, d, null, copy(pageWS)));

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListSchvaleneDatumOd.error");
			return null;
		}
	}

	public sk.ditec.cskmd.data.ws.dto.DTOZmenaWrapperWS getZmenyListSchvaleneDatumOdDo(AuthInfo auth, Integer[] ciselnikIDs, Date datumOd, Date datumDo, sk.ditec.cskmd.data.ws.dto.DTOPageWS pageWS) throws AppException {

		try {
			return copy(getDelegate().getDataOldWS().getZmenyListSchvaleneDatumOdDo(auth, ciselnikIDs, datumOd, datumDo, null, copy(pageWS)));

		} catch (Exception e) {
			DBUtils.handleException(e, "getZmenyListSchvaleneDatumOdDo.error");
			return null;
		}
	}

}
