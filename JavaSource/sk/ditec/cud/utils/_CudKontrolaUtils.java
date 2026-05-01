package sk.ditec.cud.utils;

import java.util.Date;
import java.util.regex.Pattern;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;

public class _CudKontrolaUtils {

	public static boolean jeCiselnikSpecialny(String tabulka) throws AppException {

		try {
			if (_CudConsts.TABULKA_T_DOPRAVNY_BOD.equals(tabulka)) {
				return true;
			} else if (_CudConsts.TABULKA_T_HRANICNY_PRIECHOD.equals(tabulka)) {
				return true;
			}
			return false;

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeCiselnikSpecialny.error");
			return false;
		}
	}

	private static boolean jeAtributTechnicky(String nazov, String typ) throws AppException {

		try {
			if (_CudConsts.NAZOV_HIST_ID.equals(nazov)) {
				return true;
			} else if (_CudConsts.NAZOV_ID_ZMENA.equals(nazov)) {
				return true;
			} else if (_CudConsts.NAZOV_PLATNOST_OD.equals(nazov)) {
				return true;
			} else if (_CudConsts.NAZOV_PLATNOST_DO.equals(nazov)) {
				return true;
			} else if (_CudConsts.NAZOV_ZMAZ.equals(nazov)) {
				return true;
			} else if (_CudConsts.NAZOV_CAS_VYTVORENIA.equals(nazov)) {
				return true;
			} else if (_CudConsts.NAZOV_CAS_ZMENY.equals(nazov)) {
				return true;
			} else if (_CudConsts.CISELNIK_STLPEC_TYP_PK.equals(typ)) {
				return true;
			}

			return false;

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeAtributTechnicky.error");
			return false;
		}
	}

	public static boolean jeAtributTechnicky(String nazov) throws AppException {

		try {
			return jeAtributTechnicky(nazov, null);

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeAtributTechnicky.error");
			return false;
		}
	}

	public static boolean jeAtributTechnicky(DTOCiselnikStlpec dto) throws AppException {

		try {
			return jeAtributTechnicky(dto.getNazov(), dto.getTyp());

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeAtributTechnicky.error");
			return false;
		}
	}

	public static boolean jeAtributTechnicky(DTOCiselnikStlpecGui dto) throws AppException {

		try {
			return jeAtributTechnicky(dto.getCiselnikStlpecNazov(), dto.getCiselnikStlpecTyp());

		} catch (Throwable t) {
			DBUtils.handleException(t, "jeAtributTechnicky.error");
			return false;
		}
	}

	public static boolean equals(Integer value1, Integer value2) throws AppException {

		try {
			if (StringUtils.isValid(value1) && StringUtils.isValid(value2)) {
				return value1.intValue() == value2.intValue();
			} else if (StringUtils.isValid(value1) && !StringUtils.isValid(value2)) {
				return false;
			} else if (!StringUtils.isValid(value1) && StringUtils.isValid(value2)) {
				return false;
			}
			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "equals.error");
			return false;
		}
	}

	public static boolean equals(String value1, String value2) throws AppException {

		try {
			if (StringUtils.isValid(value1) && StringUtils.isValid(value2)) {
				return value1.equals(value2);
			} else if (StringUtils.isValid(value1) && !StringUtils.isValid(value2)) {
				return false;
			} else if (!StringUtils.isValid(value1) && StringUtils.isValid(value2)) {
				return false;
			}
			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "equals.error");
			return false;
		}
	}

	public static boolean equals(Date value1, Date value2) throws AppException {

		try {
			if (StringUtils.isValid(value1) && StringUtils.isValid(value2)) {
				return value1.equals(value2);
			} else if (StringUtils.isValid(value1) && !StringUtils.isValid(value2)) {
				return false;
			} else if (!StringUtils.isValid(value1) && StringUtils.isValid(value2)) {
				return false;
			}
			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "equals.error");
			return false;
		}
	}

	/**
	 * @param value1
	 * @param value2
	 * @return if value1 < value2 then TRUE other FALSE
	 * @throws AppException
	 */
	public static boolean lessThen(Integer value1, Integer value2) throws AppException {

		try {
			if (StringUtils.isValid(value1) && StringUtils.isValid(value2)) {
				return value1.intValue() < value2.intValue();
			}
			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "lessThen.error");
			return false;
		}
	}

	public static Integer biggest(Integer... values) throws AppException {

		try {
			if (!StringUtils.isValid(values)) {
				return null;
			}

			Integer bigValue = null;
			for (Integer value : values) {
				if (!StringUtils.isValid(bigValue) && StringUtils.isValid(value)) {
					bigValue = value;
				} else if (StringUtils.isValid(bigValue) && StringUtils.isValid(value)) {
					bigValue = value.intValue() > bigValue.intValue() ? value : bigValue;
				}
			}
			return bigValue;

		} catch (Throwable t) {
			DBUtils.handleException(t, "biggest.error");
			return null;
		}
	}

	public static Date biggest(Date... values) throws AppException {

		try {
			if (!StringUtils.isValid(values)) {
				return null;
			}

			Date resultValue = null;

			for (Date value : values) {
				if (StringUtils.isValid(resultValue) && StringUtils.isValid(value) && resultValue.before(value)) {
					resultValue = value;
				} else if (!StringUtils.isValid(resultValue) && StringUtils.isValid(value)) {
					resultValue = value;
				}
			}

			return resultValue;

		} catch (Throwable t) {
			DBUtils.handleException(t, "biggest.error");
			return null;
		}
	}

	public static boolean isValidEmailList(String emails) throws AppException {

		try {
			if (!StringUtils.isValid(emails)) {
				return true;
			}

			String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
			Pattern pattern = Pattern.compile(regex);

			for (String email : _CudLookupUtils.lookupEmailList(emails)) {
				if (!pattern.matcher(email).matches()) {
					return false;
				}
			}

			return true;

		} catch (Throwable t) {
			DBUtils.handleException(t, "isValidEmailList.error");
			return false;
		}
	}

}
