package sk.ditec.cud.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikGui;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOObjektStlpec;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOZmenaStlpec;

public class _CudLookupUtils {

	public static String[] lookupRegExp(String regExp) throws AppException {

		try {
			if (!StringUtils.isValid(regExp)) {
				return null;
			}

			List<String> list = new ArrayList<String>();

			if (!regExp.contains("|")) {
				list.add(regExp.trim());
			} else {
				String[] values = regExp.split("\\|");
				for (String value : values) {
					if (StringUtils.isValid(value)) {
						list.add(value.trim());
					}
				}
			}

			return list.toArray(new String[list.size()]);

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupRegExp.error");
			return null;
		}
	}

	public static String[] lookupEmailList(String emailList) throws AppException {

		try {
			if (!StringUtils.isValid(emailList)) {
				return new String[0];
			}

			emailList = StringUtils.replaceAll(emailList, ",", ";");

			Set<String> set = new HashSet<String>();
			if (!emailList.contains(";")) {
				set.add(emailList.trim());
			} else {
				String[] arr = emailList.split(";");
				for (String email : arr) {
					if (StringUtils.isValid(email)) {
						set.add(email.trim());
					}
				}
			}

			return set.toArray(new String[set.size()]);

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupEmailList.error");
			return null;
		}
	}

	public static String formatLookupValues(List<String> valueList) throws AppException {

		try {
			int index = 0;
			String resultStr = null;

			for (String value : valueList) {

				if (index == 0) {
					resultStr = value;

				} else if (index == 1) {
					resultStr += " (" + value;

				} else {
					resultStr += ", " + value;
				}

				index++;
			}

			if (index > 1) {
				resultStr += ")";
			}

			return resultStr;

		} catch (Throwable t) {
			DBUtils.handleException(t, "formatLookupValues.error");
			return null;
		}
	}

	public static String lookupZmenaStavKod(String wfDefTypKod) throws AppException {

		try {
			if (_CudConsts.WF_DEF_TYP_IN.equals(wfDefTypKod)) {
				return _CudConsts.ZMENA_STAV_VPO;
			} else if (_CudConsts.WF_DEF_TYP_SC.equals(wfDefTypKod)) {
				return _CudConsts.ZMENA_STAV_SCH;
			} else if (_CudConsts.WF_DEF_TYP_OV.equals(wfDefTypKod)) {
				return _CudConsts.ZMENA_STAV_PAU;
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupZmenaStavKod.error");
			return null;
		}
	}

	public static String lookupZmenaOperaciaNazov(String operaciaKod) throws AppException {

		try {
			if (_CudConsts.ZMENA_OPERACIA_N.equals(operaciaKod)) {
				return _CudConsts.TEXT_ZMENA_OPERACIA_N;
			} else if (_CudConsts.ZMENA_OPERACIA_U.equals(operaciaKod)) {
				return _CudConsts.TEXT_ZMENA_OPERACIA_U;
			} else if (_CudConsts.ZMENA_OPERACIA_D.equals(operaciaKod)) {
				return _CudConsts.TEXT_ZMENA_OPERACIA_D;
			} else if (_CudConsts.ZMENA_OPERACIA_Z.equals(operaciaKod)) {
				return _CudConsts.TEXT_ZMENA_OPERACIA_Z;
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupZmenaOperaciaNazov.error");
			return null;
		}
	}

	public static String lookupOdberatelTypPristupuNazov(String typPristupuKod) throws AppException {

		try {
			if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_WS.equals(typPristupuKod)) {
				return _CudConsts.TEXT_ODBERATEL_TYP_PRISTUPU_WS;
			} else if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_EXPORT.equals(typPristupuKod)) {
				return _CudConsts.TEXT_ODBERATEL_TYP_PRISTUPU_EXPORT;
			} else if (_CudConsts.ODBERATEL_OBJEKT_TYP_PRISTUPU_ZMENA.equals(typPristupuKod)) {
				return _CudConsts.TEXT_ODBERATEL_TYP_PRISTUPU_ZMENA;
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupOdberatelTypPristupuNazov.error");
			return null;
		}
	}

	public static DTOCiselnikStlpecGui lookupDTOCiselnikStlpecGui(List<DTOCiselnikStlpecGui> list, Integer ciselnikStlpecGuiID) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikStlpecGuiID) && StringUtils.isValid(list)) {
				for (DTOCiselnikStlpecGui dto : list) {
					if (ciselnikStlpecGuiID.intValue() == dto.getCiselnikStlpecGuiID().intValue()) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecGui.error");
			return null;
		}
	}

	public static DTOCiselnikStlpecGui lookupDTOCiselnikStlpecGuiPk(List<DTOCiselnikStlpecGui> list) throws AppException {

		try {
			for (DTOCiselnikStlpecGui dto : list) {
				if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getCiselnikStlpecTyp())) {
					return dto;
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecGuiPk.error");
			return null;
		}
	}

	public static DTOCiselnikStlpecGui lookupDTOCiselnikStlpecGuiJedinecny(List<DTOCiselnikStlpecGui> list) throws AppException {

		try {
			for (DTOCiselnikStlpecGui dto : list) {
				if ("T".equals(dto.getCiselnikStlpecJedinecny())) {
					return dto;
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecGuiJedinecny.error");
			return null;
		}
	}

	public static DTOCiselnikStlpecGui lookupDTOCiselnikStlpecGuiByFk(List<DTOCiselnikStlpecGui> list, Integer ciselnikStlpecID) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikStlpecID)) {
				for (DTOCiselnikStlpecGui dto : list) {
					if (ciselnikStlpecID.intValue() == dto.getIDCiselnikStlpec().intValue()) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecGuiByFk.error");
			return null;
		}
	}

	public static DTOCiselnikStlpecGui lookupDTOCiselnikStlpecGuiByFk(List<DTOCiselnikStlpecGui> list, String nazov) throws AppException {

		try {
			if (StringUtils.isValid(nazov)) {
				for (DTOCiselnikStlpecGui dto : list) {
					if (nazov.equals(dto.getCiselnikStlpecNazov())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecGuiByFk.error");
			return null;
		}
	}

	public static DTOCiselnikStlpecGui lookupDTOCiselnikStlpecGuiByEditControl(List<DTOCiselnikStlpecGui> list, String editControl) throws AppException {

		try {
			if (StringUtils.isValid(editControl)) {
				for (DTOCiselnikStlpecGui dto : list) {
					if (editControl.equals(dto.getEditControl())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecGuiByEditControl.error");
			return null;
		}
	}

	public static void lookupDTOCiselnikStlpecGui(DTOCiselnikStlpecGui dto) throws AppException {

		try {
			if (_CudConsts.CISELNIK_STLPEC_GUI_EDIT_CONTROL_COMBO.equals(dto.getEditControl())) {
				dto.setRegExpValues(lookupRegExp(dto.getRegExp()));
				dto.setRegExp(null);
			}

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecGui.error");
		}
	}

	public static DTOWfDef lookupDTOWfDef(List<DTOWfDef> wfDefList, Integer wfDefID) throws AppException {

		try {
			if (StringUtils.isValid(wfDefID)) {
				for (DTOWfDef dto : wfDefList) {
					if (wfDefID.intValue() == dto.getWfDefID().intValue()) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOWfDef.error");
			return null;
		}
	}

	public static DTOWfDef lookupDTOWfDef(List<DTOWfDef> wfDefList, String typ) throws AppException {

		try {
			if (StringUtils.isValid(typ)) {
				for (DTOWfDef dto : wfDefList) {
					if (typ.equals(dto.getTyp())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOWfDef.error");
			return null;
		}
	}

	public static DTOCiselnikStlpec lookupDTOCiselnikStlpec(List<DTOCiselnikStlpec> list, Integer ciselnikStlpecID) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikStlpecID)) {
				for (DTOCiselnikStlpec dto : list) {
					if (ciselnikStlpecID.intValue() == dto.getCiselnikStlpecID().intValue()) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpec.error");
			return null;
		}
	}

	public static DTOCiselnikStlpec lookupDTOCiselnikStlpec(List<DTOCiselnikStlpec> list, String nazov) throws AppException {

		try {
			if (StringUtils.isValid(nazov)) {
				for (DTOCiselnikStlpec dto : list) {
					if (nazov.equals(dto.getNazov())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpec.error");
			return null;
		}
	}

	public static DTOCiselnikStlpec lookupDTOCiselnikStlpec(List<DTOCiselnikStlpec> list, Integer ciselnikID, String nazov) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikID) && StringUtils.isValid(nazov)) {
				for (DTOCiselnikStlpec dto : list) {
					if (ciselnikID.intValue() == dto.getIDCiselnik() && nazov.equals(dto.getNazov())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpec.error");
			return null;
		}
	}

	public static DTOCiselnikStlpec lookupDTOCiselnikStlpecPk(List<DTOCiselnikStlpec> list) throws AppException {

		try {
			for (DTOCiselnikStlpec dto : list) {
				if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(dto.getTyp())) {
					return dto;
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecPk.error");
			return null;
		}
	}

	public static DTOCiselnikStlpec lookupDTOCiselnikStlpecJedinecny(List<DTOCiselnikStlpec> list) throws AppException {

		try {
			for (DTOCiselnikStlpec dto : list) {
				if ("T".equals(dto.getJedinecny())) {
					return dto;
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecJedinecny.error");
			return null;
		}
	}

	public static DTOCiselnikStlpec lookupDTOCiselnikStlpecByFk1Tabulka(List<DTOCiselnikStlpec> list, String fk1CiselnikTabulka) throws AppException {

		try {
			if (StringUtils.isValid(fk1CiselnikTabulka)) {
				for (DTOCiselnikStlpec dto : list) {
					if (fk1CiselnikTabulka.equals(dto.getFk1CiselnikTabulka())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikStlpecByFk1Tabulka.error");
			return null;
		}
	}

	public static DTOCiselnikGui lookupDTOCiselnikGuiByFk(DTOCiselnikGui[] list, Integer ciselnikID) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikID)) {
				for (DTOCiselnikGui dto : list) {
					if (ciselnikID.intValue() == dto.getIDCiselnik()) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnikGuiByFk.error");
			return null;
		}
	}

	public static DTOCiselnik lookupDTOCiselnik(DTOCiselnik[] list, Integer ciselnikID) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikID)) {
				for (DTOCiselnik dto : list) {
					if (ciselnikID.intValue() == dto.getCiselnikID()) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnik.error");
			return null;
		}
	}

	public static DTOCiselnik lookupDTOCiselnik(DTOCiselnik[] list, String tabulka) throws AppException {

		try {
			if (StringUtils.isValid(tabulka)) {
				for (DTOCiselnik dto : list) {
					if (tabulka.equals(dto.getTabulka())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOCiselnik.error");
			return null;
		}
	}

	public static DTOZmenaStlpec lookupDTOZmenaStlpecByFk(List<DTOZmenaStlpec> list, Integer ciselnikStlpecID) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikStlpecID)) {
				for (DTOZmenaStlpec dto : list) {
					if (ciselnikStlpecID.intValue() == dto.getIDCiselnikStlpec()) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOZmenaStlpecByFk.error");
			return null;
		}
	}

	public static DTOZmenaStlpec lookupDTOZmenaStlpecByFk(List<DTOZmenaStlpec> list, String ciselnikStlpecNazov) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikStlpecNazov)) {
				for (DTOZmenaStlpec dto : list) {
					if (ciselnikStlpecNazov.equals(dto.getCiselnikStlpecNazov())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOZmenaStlpecByFk.error");
			return null;
		}
	}

	public static DTOWfTodo lookupDTOWfTodo(List<DTOWfTodo> list, Integer wfDefID) throws AppException {

		try {
			if (StringUtils.isValid(wfDefID)) {
				for (DTOWfTodo dto : list) {
					if (wfDefID.intValue() == dto.getIDWfDef()) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOWfTodo.error");
			return null;
		}
	}

	public static DTOWfTodo lookupDTOWfTodo(List<DTOWfTodo> list, Integer wfDefID, Integer ucetID) throws AppException {

		try {
			for (DTOWfTodo dto : list) {
				if (dto.getIDWfDef().intValue() == wfDefID.intValue() && dto.getIDUcet().intValue() == ucetID) {
					return dto;
				}
			}

			for (DTOWfTodo dto : list) {
				if (dto.getIDWfDef().intValue() == wfDefID.intValue()) {
					return dto;
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOWfTodo.error");
			return null;

		}
	}

	public static DTOImportZmenaStlpec lookupDTOImportZmenaStlpecByFk(List<DTOImportZmenaStlpec> list, String ciselnikStlpecNazov) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikStlpecNazov)) {
				for (DTOImportZmenaStlpec dto : list) {
					if (ciselnikStlpecNazov.equals(dto.getCiselnikStlpecNazov())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOImportZmenaStlpecByFk.error");
			return null;
		}
	}

	public static DTOObjektStlpec lookupDTOObjektStlpec(DTOObjektStlpec[] pole, Integer ciselnikStlpecID) throws AppException {

		try {
			for (DTOObjektStlpec dto : pole) {
				if (ciselnikStlpecID.intValue() == dto.getIDCiselnikStlpec().intValue()) {
					return dto;
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOObjektStlpec.error");
			return null;
		}
	}

	public static DTOObjektStlpec lookupDTOObjektStlpec(DTOObjektStlpec[] pole, String ciselnikStlpecNazov) throws AppException {

		try {
			if (StringUtils.isValid(ciselnikStlpecNazov)) {
				for (DTOObjektStlpec dto : pole) {
					if (ciselnikStlpecNazov.equals(dto.getCiselnikStlpecNazov())) {
						return dto;
					}
				}
			}

			return null;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lookupDTOObjektStlpec.error");
			return null;
		}
	}

}
