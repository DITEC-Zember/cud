package sk.ditec.cud.bi;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.zsr.common.server._PISBaseClass;

public class _CudBaseClass extends _PISBaseClass {

	private _CudDelegateBi dlg;

	public _CudDelegateBi getDelegate() {
		return dlg;
	}

	public void setDelegate(_CudDelegateBi dlgBi) {
		dlg = dlgBi;
	}

	protected void predVolanimDotazu(AuthInfo auth) throws SQLException {
		auth.T.createStatement().execute("ALTER SESSION SET NLS_COMP=LINGUISTIC");
		auth.T.createStatement().execute("ALTER SESSION SET NLS_SORT=SLOVAK_AI");
	}

	protected void poVolaniDotazu(AuthInfo auth) throws SQLException {
		auth.T.createStatement().execute("ALTER SESSION SET NLS_COMP=BINARY");
		auth.T.createStatement().execute("ALTER SESSION SET NLS_SORT=BINARY");
	}

	protected String messageLookup(String key) {

		if ("errors.unique".equals(key)) {
			return "Záznam ktorý sa pokúšate vložiť sa už v databáze nachádza.";
		} else if ("errors.deleteFK".equals(key)) {
			return "Na záznam, ktorý sa pokúšate zmazať sú naviazané iné záznamy. Vymažte najskôr väzobné záznamy a potom pokus opakujte.";
		}

		return null;
	}

	protected String messageLookup(ActionResult actionResult) {

		if (!StringUtils.isValid(actionResult)) {
			return null;
		}
		if (StringUtils.isValid(actionResult.getErrorMsg())) {
			return actionResult.getErrorMsg();
		}
		if (StringUtils.isValid(actionResult.getKeyErrorMsg())) {
			return messageLookup(actionResult.getKeyErrorMsg());
		}

		return null;
	}

	protected String trimColumnName(String colName) throws AppException {

		int index = colName.lastIndexOf(".");
		return colName.substring(index + 1);
	}

	public static AuthInfo cloneAuthInfoForNewTransaction(AuthInfo authInfo) {
		if (authInfo.getAccountId() == 0 && "System".equals(authInfo.getAccountName())) {
			return AuthInfo.system();
		}
		return FrameworkUtils.getAuthMod().accountRead(authInfo.getAccountId());
	}

	protected String toString(Set<String> set) throws AppException {

		try {
			if (!StringUtils.isValid(set) || set.isEmpty()) {
				return null;
			}

			String result = "";
			for (String value : set) {
				result += StringUtils.isValid(result) ? ", " + value : value;
			}

			return result;

		} catch (Throwable t) {
			DBUtils.handleException(t, "toString.error");
			return null;
		}
	}

	protected String toString(String... pole) throws AppException {

		try {
			if (!StringUtils.isValid(pole)) {
				return null;
			}

			return toString(new HashSet<String>(Arrays.asList(pole)));

		} catch (Throwable t) {
			DBUtils.handleException(t, "toString.error");
			return null;
		}
	}

}
