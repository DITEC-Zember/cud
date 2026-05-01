package sk.ditec.cud.proc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AppException;
import sk.ditec.zsr.common.server._NovyPISBaseClass;

public class ProcessTestDB extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(ProcessTestDB.class);
	
	
	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
		main(null);
	}




	public static void main(String[] args) {

		Connection T = null;
		try {
			log.debug("samostatny subor ");
			_NovyPISBaseClass.init();

			for (int i = 0; i < 10000; i++) {
				T = DBUtils.getConnection();
				ResultSet rs =T.createStatement().executeQuery("select 1 from dual");
				rs.next();
				log.debug("got connection");
				rs.close();
				DBUtils.returnConnection(T);
				Thread.sleep(10);
			}

		} catch (Throwable e) {
			log.error("", e);
			try {
				DBUtils.rollbackConnection(T);
			} catch (AppException e1) {
				log.error("", e1);
			}
		}
	};

}
