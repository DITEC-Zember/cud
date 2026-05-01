package sk.ditec.cud.plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynCiselnik;
import sk.ditec.cud.dto.DTODynValue;
import sk.ditec.cud.dto.DTOPlugin;
import sk.ditec.cud.dto.DTOPluginKontrolaRow;
import sk.ditec.cud.dto.DTOPluginStlpec;
import sk.ditec.cud.utils.CudCacheMap;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;
import sk.ditec.cud.utils._CudResultUtils;

public class CudPluginDoplnenieClass extends _CudBasePluginClass implements IPlugin {

	private String ALIAS_ZDROJ = "ZDROJ";
	private String ALIAS_LOOKUP = "LOOKUP";

	@Override
	public String updateKontrola(AuthInfo auth, DTOPluginStlpec[] pluginStlpecList, List<DTOCiselnikStlpec> csList) throws AppException {

		try {
			String s = kontrolaAlias(pluginStlpecList, ALIAS_ZDROJ);
			if (StringUtils.isValid(s)) {
				return s;
			}

			s = kontrolaAlias(pluginStlpecList, ALIAS_LOOKUP);
			if (StringUtils.isValid(s)) {
				return s;
			}

			DTOCiselnikStlpec dtoCSZdroj = lookupDTOCiselnikStlpec(pluginStlpecList, csList, ALIAS_ZDROJ);
			List<DTOCiselnikStlpec> zdrojList = getDelegate().getCiselnikStlpecRead().list(auth, dtoCSZdroj.getFk1IDCiselnik());

			DTOCiselnikStlpec dtoCSLookup = lookupDTOCiselnikStlpec(pluginStlpecList, csList, ALIAS_LOOKUP);

			boolean b = true;
			for (DTOCiselnikStlpec dtoCS : zdrojList) {
				if (StringUtils.isValid(dtoCS.getFk1IDCiselnik()) && StringUtils.isValid(dtoCSLookup.getFk1IDCiselnik()) && dtoCS.getFk1IDCiselnik().intValue() == dtoCSLookup.getFk1IDCiselnik()) {
					b = false;
				}
			}

			if (b) {
				return _CudResultUtils.returnMsg(_CudResultUtils.ERROR_CODE_3102, ALIAS_LOOKUP, dtoCSZdroj.getFk1CiselnikTabulka(), ALIAS_ZDROJ);
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "updateKontrola.error");
			return null;
		}
	}

	@Override
	public DTOPluginKontrolaRow[] validate(AuthInfo auth, DTOPlugin dtoPlg, Map<String, String> rowMap, List<DTOCiselnikStlpec> csList, CudCacheMap lookupMap) throws AppException {

		throw new AppException("Pouzitie neimplementovanej metody");
	}

	@Override
	public void setDelegat(_CudDelegateBi dlgBi) {
		this.setDelegate(dlgBi);
	}

	private Set<Integer> lookupCiselnikStlpecIDs(Integer ciselnikStlpecID, DTOPlugin[] pluginList) throws AppException {

		try {
			Set<Integer> set = new HashSet<Integer>();

			if (!StringUtils.isValid(pluginList)) {
				return set;
			}

			for (DTOPlugin dto : pluginList) {
				if (_CudConsts.PLUGIN_TYP_DOPLNENIE.equals(dto.getTyp())) {

					boolean zdroj = false;
					Integer lookupCiselnikStlpecID = null;

					for (DTOPluginStlpec dtoST : dto.getPluginStlpecList()) {

						if (ALIAS_ZDROJ.equals(dtoST.getPluginAliasNazovAliasu())) {
							if (ciselnikStlpecID.intValue() == dtoST.getIDCiselnikStlpec().intValue()) {
								zdroj = true;
							}
						}

						if (ALIAS_LOOKUP.equals(dtoST.getPluginAliasNazovAliasu())) {
							lookupCiselnikStlpecID = dtoST.getIDCiselnikStlpec();
						}
					}
					if (zdroj && StringUtils.isValid(lookupCiselnikStlpecID)) {
						set.add(lookupCiselnikStlpecID);
					}
				}
			}
			return set;
		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupCiselnikStlpecIDs.error");
			return null;
		}
	}

	@Override
	public DTODynCiselnik[] lookupValues(AuthInfo auth, DTOPlugin[] pluginList, DTODynCiselnik popValueDTO, DTOCiselnikStlpecGui dtoCS, DTOCiselnikStlpecGui[] metaArray) throws AppException {

		try {
			List<DTOCiselnikStlpec> csList = getDelegate().getCiselnikStlpecRead().list(auth, popValueDTO.getCiselnikID());
			Map<String, String> rowMap = getDelegate().getDynCiselnikRead().readLight(auth, popValueDTO.getTabulka(), csList, dtoCS.getCiselnikStlpecFk1PkNazov(), popValueDTO.getRowID().toString(), dtoCS.getCiselnikStlpecDbTyp(), popValueDTO.getPlatnostOd(), "F");

			Set<Integer> ciselnikStlpecIDs = lookupCiselnikStlpecIDs(dtoCS.getIDCiselnikStlpec(), pluginList);

			Set<Integer> ciselnikIDs = new HashSet<Integer>();
			for (DTOCiselnikStlpecGui dtoMeta : metaArray) {
				if (ciselnikStlpecIDs.contains(dtoMeta.getIDCiselnikStlpec())) {
					ciselnikIDs.add(dtoMeta.getCiselnikStlpecFk1IDCiselnik());
				}
			}

			Map<Integer, List<DTOCiselnikStlpecGui>> lookupMetaMap = getDelegate().getCiselnikStlpecGuiRead().mapForLookup(auth, ciselnikIDs, popValueDTO.getPlatnostOd());

			List<DTOCiselnikStlpecGui> metaList = new ArrayList<DTOCiselnikStlpecGui>(Arrays.asList(metaArray));

			List<DTODynCiselnik> resultList = new ArrayList<DTODynCiselnik>();

			for (Integer ciselnikStlpecID : ciselnikStlpecIDs) {

				DTOCiselnikStlpecGui dtoMetaLookup = _CudLookupUtils.lookupDTOCiselnikStlpecGuiByFk(metaList, ciselnikStlpecID);
				if (!StringUtils.isValid(dtoMetaLookup) || !StringUtils.isValid(dtoMetaLookup.getCiselnikStlpecFk1CiselnikTabulka())) {
					continue;
				}

				DTOCiselnikStlpec dtoCSLookup = _CudLookupUtils.lookupDTOCiselnikStlpecByFk1Tabulka(csList, dtoMetaLookup.getCiselnikStlpecFk1CiselnikTabulka());
				if (!StringUtils.isValid(dtoCSLookup)) {
					continue;
				}

				String pkValue = rowMap.get(dtoCSLookup.getNazov());
				if (!StringUtils.isValid(pkValue)) {
					continue;
				}

				String valueStr = getDelegate().getDynCiselnikRead().lookupValueFormat(auth, lookupMetaMap, dtoMetaLookup.getCiselnikStlpecFk1IDCiselnik(), pkValue, popValueDTO.getPlatnostOd());
				if (!StringUtils.isValid(valueStr)) {
					continue;
				}

				DTODynValue dynValueDTO = new DTODynValue();
				dynValueDTO.setValueID(Integer.parseInt(pkValue));
				dynValueDTO.setValueStr(valueStr);

				DTODynCiselnik dynCiselnikDTO = new DTODynCiselnik();
				dynCiselnikDTO.setValues(new DTODynValue[] { dynValueDTO });
				dynCiselnikDTO.setCiselnikStlpecGuiID(dtoMetaLookup.getCiselnikStlpecGuiID());

				resultList.add(dynCiselnikDTO);
			}

			return resultList.toArray(new DTODynCiselnik[resultList.size()]);

		} catch (Throwable t) {
			handleException(t, "lookupValues.error", auth);
			return null;
		}
	}

}
