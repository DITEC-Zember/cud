package sk.ditec.cud.master;
import java.net.MalformedURLException;
import java.util.List;

import sk.ditec.common.bi.ListWraper;
import sk.ditec.common.bi.Page;
import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AccessDeniedException;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.security.AuthSkupina;
import sk.ditec.common.security.Modul;
import sk.ditec.common.security.Rola;
import sk.ditec.common.security.RolaByAplikacia;
import sk.ditec.common.utils.FrameworkUtils;

public class Test {

	public static void main(String[] args) throws MalformedURLException, AppException {

		DBUtils.init();

		// ListWraper<AuthInfo> listWS = FrameworkUtils.getAuthMod().groupAccountList(AuthInfo.system(), new sk.ditec.common.bi.Page(true), 2043);
		// if (listWS != null && listWS.getList() != null) {
		// for (AuthInfo dtoWS : listWS.getList()) {
		// if (dtoWS.getEmail() != null) {
		// System.out.println(dtoWS.getEmail());
		// }
		// }
		// }

		// AuthInfo authInfox = FrameworkUtils.getAuthMod().login("DITEC\\zember", null);
		// String[] listx = FrameworkUtils.getAuthMod().getPermissionList(authInfox.getAccountId());
		// for (String o : listx) {
		// System.out.println(o);
		// }
		
		
		AuthInfo.system();
		
		/**
		 * bod 1. Zoznam Roli (PIS bude natvrdo v kode)
		 */
		System.out.println("---------- Zoznam roli ---------------");
		List<RolaByAplikacia> rs = FrameworkUtils.getAuthMod().rolaListByApplication("PIS", "");
		for (RolaByAplikacia r : rs) {
			System.out.println(r.getKodRoly() + " " + r.getNazovRoly() + " " + r.getPopisRoly() + " *** " + r.getKodModulu());
		}
		System.out.println("------------------------------------");

		/**
		 * bod 2. Zoznam modulov (PIS bude natvrdo v kode)
		 */
		System.out.println("---------- Zoznam modulov ---------------");
		List<Modul> ms = FrameworkUtils.getAuthMod().modulListByApplication("CUD");
		for (Modul m : ms) {
			System.out.println(m.getKodModulu());
		}
		System.out.println("------------------------------");

		/**
		 * bod 3 roli a aplikacie na aktualne prihlasenom uzivatelovi
		 */
		System.out.println("---------- Zoznam roli podla pouzivatela ---------------");
		List<Rola> roly = FrameworkUtils.getAuthMod().rolaListByAccount("zember");
		for (Rola r : roly) {
			System.out.println(r.getNazovRoly() + " - " + r.getKodRoly());
		}
		System.out.println("------------------------------");

		System.out.println("---------- Zoznam modulov podla pouzivatela ---------------");
		List<String> moduly = FrameworkUtils.getAuthMod().modulListByAccount("zember");
		for (String m : moduly) {
			System.out.println(m);
		}
		System.out.println("------------------------------");

		/**
		 * bod 4. Zoznam schvalovatelov
		 */
		final int NOV_SCHVALOVATELIA_KOD = 243;

		System.out.println("---------- Zoznam schvalovatelov noviniek ---------------");
		ListWraper<AuthInfo> users = FrameworkUtils.getAuthMod().groupAccountList(AuthInfo.system(), new Page(true), 691);
		for (AuthInfo a : users.getList()) {
			System.out.println(a.getAccountName() + " - " + a.getEmail());
		}
		
		ListWraper<AuthSkupina> listWS = FrameworkUtils.getAuthMod().groupList(AuthInfo.system(), new Page(true), "CUD", "CUD ");
		for (AuthSkupina dtoWS : listWS.getList()) {
			System.out.println(dtoWS.getSkupinaID() + " - " + dtoWS.getNazov());
		}

		/**
		 * bod 5. posielanie mailov
		 */
		// String[] to = new String[] { "zember@ditec.sk" };
		// FrameworkUtils.getAuthMod().rolaListByAccount("zember");
		// NotifUtils.sendNotif(null, to, "Test", "Toto je test");

		// --------------
		/**
		 * Zistovanie opravneni
		 */
		AuthInfo authInfo = FrameworkUtils.getAuthMod().login("DITEC\\zember", "");
		System.out.println(authInfo.getAccountId());
		try {
			DBUtils.checkPermission(authInfo, "Zmena");
		} catch (AccessDeniedException e) {
			System.out.println("Nemam opravnenie");
		}

		authInfo = FrameworkUtils.getAuthMod().login("DITEC\\zember", "");
		String[] list = FrameworkUtils.getAuthMod().getPermissionList(authInfo.getAccountId());
		for (String o : list) {
			System.out.println(o);
		}

	}

}
