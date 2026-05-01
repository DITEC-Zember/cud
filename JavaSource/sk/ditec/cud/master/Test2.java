package sk.ditec.cud.master;

import java.net.MalformedURLException;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.FrameworkUtils;

public class Test2 {

	public static void main(String[] args) throws MalformedURLException, AppException {

		DBUtils.init();

		AuthInfo auth = FrameworkUtils.getAuthMod().accountRead("zember");

		String[] perms = FrameworkUtils.getAuthMod().getPermissionList(auth.getAccountId());
		for (String s : perms) {
			System.out.println(s);
		}

	}
}
