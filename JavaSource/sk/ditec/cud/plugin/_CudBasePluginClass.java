package sk.ditec.cud.plugin;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudBaseClass;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.cud.dto.DTOPluginStlpec;
import sk.ditec.cud.utils.CudCacheMap;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;

public class _CudBasePluginClass extends _CudBaseClass {

	private Integer getPocet(DTOPluginStlpec[] pole, String alias) throws AppException {

		try {
			int pocet = 0;
			for (DTOPluginStlpec dtoStlpec : pole) {
				if (alias.equals(dtoStlpec.getPluginAliasNazovAliasu())) {
					pocet++;
				}
			}
			return pocet;

		} catch (Throwable t) {
			DBUtils.handleException(t, "getPocet.error");
			return null;
		}
	}

	protected String kontrolaAlias(DTOPluginStlpec[] pole, String alias) throws AppException {

		try {
			int pocet = getPocet(pole, alias);

			if (pocet == 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3066, alias);
			}
			if (pocet > 1) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3065, alias);
			}
			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaAlias.error");
			return null;
		}
	}

	protected String kontrolaParam(DTOPluginStlpec[] pole, String alias) throws AppException {

		try {
			int pocet = getPocet(pole, alias);

			if (pocet == 0) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3066, alias);
			}
			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaParam.error");
			return null;
		}
	}

	protected String kontrolaDbTyp(DTOPluginStlpec[] pole, String alias, List<DTOCiselnikStlpec> csList, String dbTyp) throws AppException {

		try {
			DTOPluginStlpec dtoPS = lookupDTOPluginStlpec(pole, alias);
			DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpec(csList, dtoPS.getIDCiselnikStlpec());
			if (!dbTyp.equals(dtoCS.getDbTyp())) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3067, alias);
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "kontrolaDbTyp.error");
			return null;
		}
	}

	protected String lookupValue(DTOPluginStlpec[] pole, Map<String, String> rowMap, String alias) throws AppException {

		try {
			DTOPluginStlpec dtoPS = lookupDTOPluginStlpec(pole, alias);
			if (StringUtils.isValid(dtoPS)) {
				return rowMap.get(dtoPS.getCiselnikStlpecNazov());
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupValue.error");
			return null;
		}
	}

	protected Set<String> lookupParams(DTOPluginStlpec[] pole, String alias) throws AppException {

		try {
			Set<String> set = new HashSet<String>();
			for (DTOPluginStlpec dtoPS : pole) {
				if (alias.equals(dtoPS.getPluginAliasNazovAliasu())) {
					set.add(dtoPS.getHodnota());
				}
			}
			return set;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupParams.error");
			return null;
		}
	}

	private DTOPluginStlpec lookupDTOPluginStlpec(DTOPluginStlpec[] pole, String alias) throws AppException {

		try {
			for (DTOPluginStlpec dto : pole) {
				if (alias.equals(dto.getPluginAliasNazovAliasu())) {
					return dto;
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOPluginStlpec.error");
			return null;
		}
	}

	protected DTOCiselnikStlpec lookupDTOCiselnikStlpec(DTOPluginStlpec[] pole, List<DTOCiselnikStlpec> csList, String alias) throws AppException {

		try {
			DTOPluginStlpec dtoPS = lookupDTOPluginStlpec(pole, alias);
			if (StringUtils.isValid(dtoPS)) {
				return _CudLookupUtils.lookupDTOCiselnikStlpec(csList, dtoPS.getIDCiselnikStlpec());
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpec.error");
			return null;
		}
	}

	protected DTOPluginKontrolaRow createDTOPluginKontrolaRow(Integer pluginID, String stav, String msg, Integer ciselnikStlpecID) throws AppException {

		try {
			DTOPluginKontrolaRow dtoNew = new DTOPluginKontrolaRow();
			dtoNew.setIDPlugin(pluginID);
			dtoNew.setStav(stav);
			dtoNew.setPopis(msg);
			dtoNew.setIDCiselnikStlpec(ciselnikStlpecID);
			return dtoNew;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOPluginKontrolaRow.error");
			return null;
		}
	}

	protected DTOCiselnik lookupDTOCiselnik(AuthInfo auth, CudCacheMap lookupMap, String tabulka) throws AppException {

		try {
			DTOCiselnik dto = lookupMap.getRecord(tabulka, DTOCiselnik.class);
			if (!StringUtils.isValid(dto)) {
				dto = getDelegate().getCiselnikRead().readLight(auth, tabulka);
				lookupMap.addRecord(tabulka, dto);
			}
			return dto;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnik.error");
			return null;
		}
	}

	protected List<DTOCiselnikStlpec> lookupDTOCiselnikStlpecList(AuthInfo auth, CudCacheMap lookupMap, String tabulka) throws AppException {

		try {
			List<DTOCiselnikStlpec> listDTO = lookupMap.getList(tabulka, DTOCiselnikStlpec.class);
			if (!StringUtils.isValid(listDTO) || listDTO.isEmpty()) {
				DTOCiselnik dtoCis = lookupDTOCiselnik(auth, lookupMap, tabulka);
				listDTO = getDelegate().getCiselnikStlpecRead().listLight(auth, dtoCis.getCiselnikID());
				lookupMap.addArray(tabulka, listDTO.toArray(new DTOCiselnikStlpec[listDTO.size()]));
			}
			return listDTO;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecList.error");
			return null;
		}
	}

	protected Map<String, String> dynCiselnikRead(AuthInfo auth, CudCacheMap lookupMap, String tabulka, String rowID) throws AppException {

		try {
			if (!StringUtils.isValid(rowID)) {
				return new HashMap<String, String>();
			}

			List<DTOCiselnikStlpec> csList = lookupDTOCiselnikStlpecList(auth, lookupMap, tabulka);

			Date planPlatnostOd = lookupMap.getRecord(_CudConsts.NAZOV_PLG_PLATNOST_OD, Date.class);

			DTOCiselnikStlpec dtoCS = _CudLookupUtils.lookupDTOCiselnikStlpecPk(csList);

			return getDelegate().getDynCiselnikRead().readLight(auth, tabulka, csList, dtoCS.getNazov(), rowID, dtoCS.getDbTyp(), planPlatnostOd, "F");

		} catch (Throwable t) {
			DBUtils.handleException(t, "dynCiselnikRead.error");
			return null;
		}
	}

}
