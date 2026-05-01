package sk.ditec.crd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.DateUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.bi._CudDelegateBi;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOMeta;
import sk.ditec.cud.dto.DTOPau;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.proc.CudPauClass;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

public class CrdAktualizujCiselnikClass extends _NovyPISBaseClass {
	private CudPauClass cudPau = new CudPauClass();
	private _CudDelegateBi dlgcud = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);
	protected String aktualizujCiselnik(AuthInfo auth, DTOZmena dtoZmenaZap) throws Throwable {

		try {
			// AuthInfo auth = AuthInfo.system();

			// nacitava sa zoznam zmena
			// DTOZmena dtoF = new DTOZmena();
			// dtoF.setZmenaID();

			// List<DTOZmena> zmenaList = dlgcud.getZmenaRead().listLight(auth, dtoF);
			ArrayList<DTOZmena> zmenaList = new ArrayList<DTOZmena>();
			zmenaList.add(dtoZmenaZap);

			if (!StringUtils.isValid(zmenaList) || zmenaList.isEmpty()) {
				// log.info("V tabulke CUD_ZMENA nie su ziadne zmeny na spracovanie, vykonavanie procesu konci.");
				// return;
			}

			DTOMeta dtoMeta = new DTOMeta();

			// nacita sa zoznam vsetkych ciselnikov, tabulka CUD_CISELNIK
			dtoMeta.setCiselnikPole((new CudPauClass()).ciselnikList(auth));
			if (!StringUtils.isValid(dtoMeta.getCiselnikPole())) {
				// log.error("Tabulka CUD_CISELNIK je prazdna, vykonavanie procesu konci.");
				// return;
			}

			// nacitaju sa metadata z tabulky CUD_CISELNIK_STLPEC
			dtoMeta.setCiselnikStlpecMap(dlgcud.getCiselnikStlpecRead().mapLightForPau(auth));

			// ziskanie ID stlpcov, ktore ked sa menia, treba menit zaznamy v tabulke T_DOPRAVNY_NAZOV
			Set<String> ciselnikNazovSpecialSet = cudPau.getCiselnikNazovSpecialSet();
			// Set<Integer> ciselnikStlpecSpecialSet = getCiselnikStlpecSpecialSetID(dtoMeta);
			Set<Integer> ciselnikStlpecSpecialFkSet = cudPau.getCiselnikStlpecSpecialFkSetID(dtoMeta);
			Set<String> specialValuesSet = cudPau.getSpecialValueSet();

			// nacitaju sa vsetky zaznamy z tabulky CUD_WF_DEF
			Map<Integer, List<DTOWfDef>> wfDefMap = dlgcud.getWfDefRead().mapLight(auth, null);
			cudPau.wfDefSort(wfDefMap);

			List<DTOPau> paList = new ArrayList<DTOPau>();

			// Map<Integer, DTOUcet> ucetMap = new HashMap<Integer, DTOUcet>();

			Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> metaMapForData = new HashMap<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>();
			Map<Date, Map<Integer, List<DTOCiselnikStlpecGui>>> metaMapForLookup = new HashMap<Date, Map<Integer, List<DTOCiselnikStlpecGui>>>();

			for (DTOZmena dto : zmenaList) {

				// log.info("Spracovanie zmeny zmenaID=={}", dto.getZmenaID());

				// kontrola atributov, staci kontrolovat PLATNOST_OD, ostatne su NOT NULL
				if (!StringUtils.isValid(dto.getPlatnostOd())) {
					// log.info("Nie je definovana platnost od, setujem aktualny cas");
					dto.setPlatnostOd(DateUtils.removeTime(new Date()));
				}

				if (!dto.getPlatnostOd().equals(dtoMeta.getPlatnostOd())) {

					dtoMeta.setPlatnostOd(dto.getPlatnostOd());

					// nacita sa zoznam vsetkych ciselnikov z tabulky CUD_CISELNIK_GUI
					dtoMeta.setCiselnikGuiPole(dlgcud.getCiselnikGuiRead().listLight(auth, dto.getPlatnostOd()));
					if (!StringUtils.isValid(dtoMeta.getCiselnikGuiPole())) {
						// log.error("Tabulka CUD_CISELNIK_GUI je prazdna, vykonavanie procesu konci.");
						// return;
					}

					// nacitaju sa metadata z tabulky CUD_CISELNIK_STLPEC_GUI
					dtoMeta.setCiselnikStlpecGuiMap(dlgcud.getCiselnikStlpecGuiRead().map(auth, dto.getPlatnostOd()));
				}

				// Set<Integer> ciselnikStlpecSpecialSet =
				// dlg.getSpracujCrdCountryClass().getCiselnikStlpecSpecialSetID(
				// dtoMeta);

				// nacitaju za zaznamy z tabulky CUD_ZMENA_STLPEC
				List<DTOZmenaStlpec> zmenaStlpecList = dlgcud.getZmenaStlpecRead().listLight(auth, dto.getIDCiselnik(),
						dto.getZmenaID());
				if (!StringUtils.isValid(zmenaStlpecList) || zmenaStlpecList.isEmpty()) {
					throw new AppException("V tabulke CUD_ZMENA_STLPEC neexistuju zaznamy pre zmenu="
							+ dto.getZmenaID() + ", vykonavanie procesu konci.");
				}

				List<DTOWfDef> wfDefList = wfDefMap.get(dto.getIDCiselnik());
				if (!StringUtils.isValid(wfDefList) || wfDefList.isEmpty()) {
					throw new AppException("V tabulke CUD_WF_DEF nie je definovany workflow pre ciselniID="
							+ dto.getIDCiselnik() + ", vykonavanie procesu konci.");
				}

				// nacitaju za zaznamy z tabulky CUD_WF_TODO
				List<DTOWfTodo> wfTodoList = dlgcud.getWfTodoRead().listLight(auth, dto.getIDCiselnik(),
						dto.getZmenaID());
				cudPau.kontrolaWfTodoList(wfDefList, wfTodoList);

				paList.clear();
				paList.add(cudPau.createDTOPrimarnaAktualizacia(dto, zmenaStlpecList, new Date()));

				for (int i = 0; i < paList.size(); i++) {
					DTOPau dtoPA = paList.get(i);

					if ("T".equals(dtoPA.getLenPlatnostDo())) {
						continue;
					}

					cudPau.lookupAttributes(auth, dtoPA, dtoMeta);
					cudPau.kontrolaNaNadradenuDopravnu(dtoPA);
					// cudPau.kontrolaNaZmenuDopravnehoNazvu(auth, dtoPA, paList, dtoMeta, ciselnikNazovSpecialSet,
					// ciselnikStlpecSpecialSet, ciselnikStlpecSpecialFkSet);
					cudPau.kontrolaPredPublikovanimZmeny(auth, dtoPA, paList, dtoMeta, ciselnikStlpecSpecialFkSet,
							specialValuesSet);
				}

				// generovanie workflow
				cudPau.generujWorkFlow(auth, paList, wfDefList, wfTodoList, wfDefMap);

				// samotna primarnaAktualizacia - update
				Collections.reverse(paList);
				cudPau.primarnaAktualizaciaUpdate(auth, paList);

				// sendNotif(auth, paList, wfDefMap, ucetMap, metaMapForData, metaMapForLookup);
			}

		} catch (Exception e) {
			// dlg.getWfNotif().sendNotifError(_CudConsts.TEXT_NOTIF_SUBJ_PAU, getStackTraceToString(e));
			DBUtils.handleException(e, "process.error");

		}
		// log.info("End - Som proces CudPauProcess a koncim");

		return "OK";
	}


}