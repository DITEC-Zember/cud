package sk.ditec.cud.bi;

import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.torque.Torque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.security.RolaByAplikacia;
import sk.ditec.common.utils.FrameworkUtils;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnik;
import sk.ditec.cud.dto.DTOCiselnikStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.zsr.common.server.auth.ZSRAuthInfo;

public class CudInitializator extends HttpServlet {

	private Logger log = LoggerFactory.getLogger(CudInitializator.class);

	private _CudDelegateBi dlg = new _CudDelegateBi(_CudConsts.PERM_DATA_READ_WEB);

	private void initParams() {

		String s = FrameworkUtils.getConfigProperty("cud", "len_dopredne");
		if (StringUtils.isValid(s)) {
			_CudConsts.LEN_DOPREDNE = s;
		}

		s = FrameworkUtils.getConfigProperty("cud", "max_dni_dopredu");
		if (StringUtils.isValid(s)) {
			_CudConsts.MAX_DNI_DOPREDU = Integer.parseInt(s);
		}

		s = FrameworkUtils.getConfigProperty("cud", "den_uzavierky");
		if (StringUtils.isValid(s)) {
			_CudConsts.DEN_UZAVIERKY = Integer.parseInt(s);
		}

		s = FrameworkUtils.getConfigProperty("cud", "print.field");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_FIELD = s;
		}
		s = FrameworkUtils.getConfigProperty("cud", "print.header");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_HEADER = s;
		}
		s = FrameworkUtils.getConfigProperty("cud", "print.variable");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_VARIABLE = s;
		}
		s = FrameworkUtils.getConfigProperty("cud", "print.xls.column");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_XLS_COLUMN = s;
		}
		s = FrameworkUtils.getConfigProperty("cud", "print.xls.column.string");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_XLS_COLUMN_STRING = s;
		}
		s = FrameworkUtils.getConfigProperty("cud", "print.xls.column.integer");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_XLS_COLUMN_INTEGER = s;
		}
		s = FrameworkUtils.getConfigProperty("cud", "print.xls.column.double");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_XLS_COLUMN_DOUBLE = s;
		}
		s = FrameworkUtils.getConfigProperty("cud", "print.xls.column.date");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_XLS_COLUMN_DATE = s;
		}
		s = FrameworkUtils.getConfigProperty("cud", "print.xls.body");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_XLS_BODY = s;
			String printStyle = FrameworkUtils.getConfigProperty("cud", "print.xls.style");
			if (StringUtils.isValid(printStyle)) {
				_CudConsts.PRINT_XLS_BODY = StringUtils.replaceAll(_CudConsts.PRINT_XLS_BODY, "{cudStyles}", printStyle);
			}
		}

		s = FrameworkUtils.getConfigProperty("cud", "print.pdf.column");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_PDF_COLUMN = s;
		}
		s = FrameworkUtils.getConfigProperty("cud", "print.pdf.body");
		if (StringUtils.isValid(s)) {
			_CudConsts.PRINT_PDF_BODY = s;
			String printStyle = FrameworkUtils.getConfigProperty("cud", "print.pdf.style");
			if (StringUtils.isValid(printStyle)) {
				_CudConsts.PRINT_PDF_BODY = StringUtils.replaceAll(_CudConsts.PRINT_PDF_BODY, "{cudStyles}", printStyle);
			}
			String printHeader = FrameworkUtils.getConfigProperty("cud", "print.pdf.header");
			if (StringUtils.isValid(printHeader)) {
				_CudConsts.PRINT_PDF_BODY = StringUtils.replaceAll(_CudConsts.PRINT_PDF_BODY, "{cudPageHeader}", printHeader);
			}
		}

		AuthInfo auth = ZSRAuthInfo.system();

		try {
			DTOCiselnik dto = dlg.getCiselnikRead().readLight(auth, _CudConsts.TABULKA_T_DOPRAVNY_NAZOV);
			if (StringUtils.isValid(dto)) {
				_CudConsts.ID_T_DOPRAVNY_NAZOV = dto.getCiselnikID();
			}

			dto = dlg.getCiselnikRead().readLight(auth, _CudConsts.TABULKA_T_DOPRAVCA);
			if (StringUtils.isValid(dto)) {
				_CudConsts.ID_T_DOPRAVCA = dto.getCiselnikID();
			}

			DTOCiselnikStlpec dtoCS = dlg.getCiselnikStlpecRead().readLight(auth, _CudConsts.ID_T_DOPRAVNY_NAZOV, _CudConsts.NAZOV_NAZOV);
			if (StringUtils.isValid(dtoCS)) {
				_CudConsts.ID_T_DOPRAVNY_NAZOV_NAZOV = dtoCS.getCiselnikStlpecID();
			}

		} catch (AppException e) {
			throw new RuntimeException("Nepodarilo sa nacitat parametre z DB!");
		}

		try {
			List<RolaByAplikacia> listWS = FrameworkUtils.getAuthMod().rolaListByApplication(_CudConsts.ROLA_MODUL_PIS, null);
			for (RolaByAplikacia dtoWS : listWS) {
				_CudConsts.ROLA_MODUL_KODs.add(dtoWS.getKodRoly());
			}
			listWS = FrameworkUtils.getAuthMod().rolaListByApplication(_CudConsts.ROLA_MODUL_CUD, null);
			for (RolaByAplikacia dtoWS : listWS) {
				_CudConsts.ROLA_MODUL_KODs.add(dtoWS.getKodRoly());
			}
			listWS = FrameworkUtils.getAuthMod().rolaListByApplication(_CudConsts.ROLA_MODUL_KMD, null);
			for (RolaByAplikacia dtoWS : listWS) {
				_CudConsts.ROLA_MODUL_KODs.add(dtoWS.getKodRoly());
			}

		} catch (AppException e) {
			throw new RuntimeException("Nepodarilo sa nacitat parametre z IAM!");
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {

		// nacachujem si ich pri starte aplikacie
		new Thread() {

			public void run() {
				try {
					synchronized (this) {
						while (!Torque.isInit()) {
							Thread.currentThread().wait(1000);
						}
					}
				} catch (InterruptedException e) {
					log.error("init", e);
				}

				try {
					initParams();

				} catch (Throwable t) {
					log.error("init.error", t);
				}
			}

		}.start();
	}

}
