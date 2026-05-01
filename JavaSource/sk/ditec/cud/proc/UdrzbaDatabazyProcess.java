package sk.ditec.cud.proc;

import java.sql.CallableStatement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.process.BaseProcess;

public class UdrzbaDatabazyProcess extends BaseProcess {
	private Logger log = LoggerFactory.getLogger(UdrzbaDatabazyProcess.class);

	String unq;

	public void setUNQ(String unq) {
		this.unq = unq;
	}

	@Override
	protected void process() throws Throwable {

		AuthInfo auth = AuthInfo.system();
		log.debug("Bezim call PROCESS_UDRZBA_DB()");
		CallableStatement stmt = null;
		try {

			DBUtils.getConnection(auth);

			stmt = auth.T.prepareCall("{call PROCESS_UDRZBA_DB()}");
			stmt.execute();

			DBUtils.returnConnection(auth);
			log.debug("Skoncil call PROCESS_UDRZBA_DB()");

		} catch (Throwable t) {
			DBUtils.handleException(t, "UdrzbaDatabazyProcess", auth);

		} finally {
			DBUtils.cleanUp(stmt, null);
		}

	}
	@Override
	protected String getLogName() {
		return "udrzba";
	}
}
