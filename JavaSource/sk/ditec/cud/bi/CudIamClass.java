package sk.ditec.cud.bi;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.bi.ListWraper;
import sk.ditec.common.bi.Page;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.security.AuthSkupina;
import sk.ditec.common.security.RolaByAplikacia;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTORola;
import sk.ditec.cud.dto.DTOSkupina;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.utils._CudConsts;

public class CudIamClass extends _CudBaseClass {

	private Logger log = LoggerFactory.getLogger(CudIamClass.class);

	public DTOSkupina[] skupinaList(AuthInfo auth, DTOSkupina dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (dtoF == null) {
				dtoF = new DTOSkupina();
			}

			Map<Integer, DTOSkupina> mapa = new HashMap<Integer, DTOSkupina>();

			ListWraper<AuthSkupina> listWS = FrameworkUtils.getAuthMod().groupList(auth, new Page(true), _CudConsts.ROLA_MODUL_CUD, null);
			for (AuthSkupina dtoWS : listWS.getList()) {

				boolean b = true;

				if (StringUtils.isValid(dtoF.getSkupinaID())) {
					b = dtoWS.getSkupinaID().toString().startsWith(dtoF.getSkupinaID().toString());
				}

				if (b && StringUtils.isValid(dtoF.getNazov())) {
					String filterValue = Normalizer.normalize(dtoF.getNazov(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
					String value = Normalizer.normalize(dtoWS.getNazov(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
					b = value.toUpperCase().startsWith(filterValue.toUpperCase());
				}

				if (b) {
					DTOSkupina dto = new DTOSkupina();
					dto.setNazov(dtoWS.getNazov());
					dto.setSkupinaID(dtoWS.getSkupinaID());
					dto.setBlokovanie(dtoWS.getBlokovanie());

					mapa.put(dto.getSkupinaID(), dto);
				}
			}

			List<DTOSkupina> listDTO = new ArrayList<DTOSkupina>();
			for (Integer skupinaID : new TreeSet<Integer>(mapa.keySet())) {
				listDTO.add(mapa.get(skupinaID));
			}

			return listDTO.toArray(new DTOSkupina[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "skupinaList.error", auth);
			return null;
		}
	}

	public DTOUcet[] ucetSkupinaList(AuthInfo auth, Integer skupinaID) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			ListWraper<AuthInfo> dataList = FrameworkUtils.getAuthMod().groupAccountList(auth, new Page(true), skupinaID);

			ArrayList<DTOUcet> listDTO = new ArrayList<DTOUcet>();

			for (AuthInfo dtoWS : dataList.getList()) {
				DTOUcet dtoNew = new DTOUcet();
				dtoNew.setUcetID(dtoWS.getAccountId());
				dtoNew.setUcetNazov(dtoWS.getAccountName());
				dtoNew.setPouzivatelNazov(dtoWS.getPouzivatel());
				dtoNew.setListSize(dataList.getCount());
				listDTO.add(dtoNew);
			}

			return (DTOUcet[]) listDTO.toArray(new DTOUcet[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "ucetSkupinaList.error", auth);
			return null;
		}
	}

	public DTOUcet[] ucetList(AuthInfo auth, Page page, DTOUcet dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(dtoF)) {
				dtoF = new DTOUcet();
			}

			ListWraper<AuthInfo> dataList = FrameworkUtils.getAuthMod().accountList(auth, page, dtoF.getPouzivatelNazov());

			ArrayList<DTOUcet> listDTO = new ArrayList<DTOUcet>();

			for (AuthInfo dtoWS : dataList.getList()) {
				DTOUcet dtoNew = new DTOUcet();
				dtoNew.setUcetID(dtoWS.getAccountId());
				dtoNew.setUcetNazov(dtoWS.getAccountName());
				dtoNew.setPouzivatelNazov(dtoWS.getPouzivatel());

				dtoNew.setListSize(dataList.getCount());

				listDTO.add(dtoNew);
			}

			return (DTOUcet[]) listDTO.toArray(new DTOUcet[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "ucetList.error", auth);
			return null;
		}
	}

	public DTORola[] rolaList(AuthInfo auth, DTORola dtoF) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			if (!StringUtils.isValid(dtoF)) {
				dtoF = new DTORola();
			}

			List<RolaByAplikacia> listWS = FrameworkUtils.getAuthMod().rolaListByApplication(dtoF.getModulKod(), null);

			Map<Integer, DTORola> mapa = new HashMap<Integer, DTORola>();

			int i = 1;
			for (RolaByAplikacia dtoWS : listWS) {
				DTORola dtoNew = new DTORola();
				dtoNew.setRolaID(i++);
				dtoNew.setRolaKod(dtoWS.getKodRoly());
				dtoNew.setRolaNazov(dtoWS.getNazovRoly());
				dtoNew.setModulKod(dtoF.getModulKod());

				boolean b = true;
				if (StringUtils.isValid(dtoF.getRolaKod()) && !dtoNew.getRolaKod().startsWith(dtoF.getRolaKod())) {
					b = false;
				}
				if (StringUtils.isValid(dtoF.getRolaNazov())) {
					String filterValue = Normalizer.normalize(dtoF.getRolaNazov(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
					String value = Normalizer.normalize(dtoNew.getRolaNazov(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
					if (!value.toUpperCase().startsWith(filterValue.toUpperCase())) {
						b = false;
					}
				}

				if (b) {
					mapa.put(Integer.parseInt(dtoNew.getRolaKod()), dtoNew);
				}
			}

			List<DTORola> listDTO = new ArrayList<DTORola>();
			for (Integer rolaKod : new TreeSet<Integer>(mapa.keySet())) {
				listDTO.add(mapa.get(rolaKod));
			}

			return (DTORola[]) listDTO.toArray(new DTORola[listDTO.size()]);

		} catch (Throwable t) {
			handleException(t, "rolaList.error", auth);
			return null;
		}
	}

	public String jeAdmin(AuthInfo auth) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			ListWraper<AuthSkupina> listWS = FrameworkUtils.getAuthMod().groupList(auth, new Page(true), _CudConsts.ROLA_MODUL_CUD, null);

			for (AuthSkupina dtoWS : listWS.getList()) {
				if (_CudConsts.IAM_CUD_ADMINISTATOR_ID == dtoWS.getSkupinaID()) {
					return "T";
				}
			}

			return "F";

		} catch (Throwable t) {
			handleException(t, "jeAdmin.error", auth);
			return null;
		}
	}

	public boolean jeUcetZoSkupiny(AuthInfo auth, Integer ciselnikID, String typ) throws AppException {

		checkPermission(auth, getDelegate().getReadPermission());

		try {
			DTOWfDef dtoF = new DTOWfDef();
			dtoF.setIDCiselnik(ciselnikID);
			dtoF.setTyp(typ);
			List<DTOWfDef> wfDefList = getDelegate().getWfDefRead().listLight(auth, dtoF);

			for (DTOWfDef dtoDef : wfDefList) {
				ListWraper<AuthInfo> iamList = FrameworkUtils.getAuthMod().groupAccountList(auth, new Page(true), dtoDef.getIDSkupina());
				if (StringUtils.isValid(iamList.getList())) {
					for (AuthInfo authInfo : iamList.getList()) {
						if (auth.getAccountName().toLowerCase().equals(authInfo.getAccountName().toLowerCase())) {
							log.info("Ucet {} sa nachadza v skupine typu {}", auth.getAccountName(), typ);
							return true;
						}
					}
				}

			}

			log.info("Ucet {} sa nenachadza v skupine typu {}", auth.getAccountName(), typ);

			return false;

		} catch (Throwable t) {
			handleException(t, "jeUcetZoSkupiny.error", auth);
			return false;
		}
	}

}
