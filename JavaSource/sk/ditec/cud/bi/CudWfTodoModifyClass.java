package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.torque.util.MyCriteria2;

import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOCiselnikStlpecGui;
import sk.ditec.cud.dto.DTODynCiselnikLD;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfNotif;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOWorkflow;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.dao.meta.CudWfTodo;
import sk.ditec.dao.meta.CudWfTodoPeer;

public class CudWfTodoModifyClass extends _CudBaseClass {

	private ActionResult deleteHard(AuthInfo auth, Set<Integer> set) throws AppException {

		try {
			// hard delete
			MyCriteria2 crit = new MyCriteria2();
			if (set.size() == 1) {
				crit.add(CudWfTodoPeer.WF_TODO_ID, set.iterator().next());
			} else {
				crit.addIn(CudWfTodoPeer.WF_TODO_ID, (Integer[]) set.toArray(new Integer[set.size()]));
			}

			CudWfTodoPeer.doDelete(crit, auth.T);

			return new ActionResult(null);

		} catch (Throwable t) {
			return handleException(t, "deleteHard.error", auth);
		}
	}

	public ActionResult delete(AuthInfo auth, Set<Integer> set) throws AppException {

		try {
			if (set.isEmpty()) {
				return new ActionResult(null);
			}

			return deleteHard(auth, set);

		} catch (Throwable t) {
			return handleException(t, "delete.error", auth);
		}
	}

	private ActionResult updateSoft(AuthInfo auth, DTOWfTodo dto) throws AppException {

		try {
			CudWfTodo dao = null;

			if (StringUtils.isValid(dto.getWfTodoID())) {
				dao = CudWfTodoPeer.retrieveByPK(dto.getWfTodoID(), auth.T);
			} else {
				dao = new CudWfTodo();
			}

			dao.setIdCiselnik(dto.getIDCiselnik());
			dao.setIdZmena(dto.getIDZmena());
			dao.setIdWfDef(dto.getIDWfDef());
			dao.setPotvrdeny(dto.getPotvrdeny());
			dao.setPoznamka(dto.getPoznamka());
			dao.setIdUcet(dto.getIDUcet());
			dao.setIdTransakciaZapisane(auth.getTransakciaID());

			dao.save(auth.T);

			dto.setWfTodoID(dao.getWfTodoId());

			return new ActionResult(dto);

		} catch (Throwable t) {
			return handleException(t, "updateSoft.error", auth);
		}
	}

	public ActionResult update(AuthInfo auth, List<DTOWfTodo> list, Integer zmenaID) throws AppException {

		try {
			if (!StringUtils.isValid(list) || list.isEmpty()) {
				return new ActionResult();
			}

			for (DTOWfTodo dto : list) {
				dto.setIDZmena(zmenaID);
				updateSoft(auth, dto);
			}

			return new ActionResult();

		} catch (Throwable t) {
			return handleException(t, "update.error", auth);
		}
	}

	public String ulohaUpdate(AuthInfo auth, DTOWfTodo dto, DTOZmenaStlpec[] zsPole, Integer histID) throws AppException {

		try {
			List<DTOWfDef> wfDefList = getDelegate().getWfDefRead().list(auth, dto.getIDCiselnik());

			List<DTOWfTodo> todoList = getDelegate().getWfTodoRead().listLight(auth, dto.getIDCiselnik(), dto.getIDZmena());

			DTOZmena dtoZmena = getDelegate().getZmenaRead().readLight(auth, dto.getIDZmena());

			DTOWorkflow dtoWf = getDelegate().getWorkflow().createDTOWorkflowSC(auth, dtoZmena, dto, wfDefList, todoList, new HashMap<Integer, DTOUcet[]>());

			getDelegate().getWorkflow().workflowUpdate(auth, dtoWf);

			sendNotif(auth, dtoWf.getDefActDTO(), dtoWf.getWfTodoUpdateList().get(0), dtoZmena, zsPole, histID);

			return null;

		} catch (Throwable t) {
			handleException(t, "ulohaUpdate.error", auth);
			return null;
		}
	}

	private DTODynCiselnikLD dynCiselnikRead(AuthInfo auth, Integer ciselnikID, String ciselnikTabulka, Integer histID, Date platnostOd) throws AppException {

		try {
			DTODynCiselnikLD dtoF = new DTODynCiselnikLD();
			dtoF.setHistID(histID);
			dtoF.setCiselnikID(ciselnikID);
			dtoF.setPlatnostOd(platnostOd);
			dtoF.setCiselnikTabulka(ciselnikTabulka);

			return getDelegate().getDynCiselnikRead().loadData(auth, dtoF);

		} catch (Exception t) {
			handleException(t, "dynCiselnikRead.error", auth);
			return null;
		}
	}

	private void sendNotif(AuthInfo auth, DTOWfDef dtoDef, DTOWfTodo dtoTodo, DTOZmena dtoZmena, DTOZmenaStlpec[] zsPole, Integer histID) throws AppException {

		try {
			if ("F".equals(dtoDef.getEmailSend())) {
				return;
			}

			DTOWfNotif dtoNotif = new DTOWfNotif();
			dtoNotif.setCiselnikID(dtoTodo.getIDCiselnik());
			dtoNotif.setCiselnikNazov(dtoTodo.getCiselnikNazov());
			dtoNotif.setZmenaOperacia(dtoZmena.getOperacia());
			dtoNotif.setPoznamka(dtoTodo.getPoznamka());
			dtoNotif.setPlatnostOd(dtoZmena.getPlatnostOd());

			DTODynCiselnikLD dtoDynLD = dynCiselnikRead(auth, dtoTodo.getIDCiselnik(), dtoTodo.getCiselnikTabulka(), histID, dtoZmena.getPlatnostOd());

			Map<String, String> rowMap = new HashMap<String, String>();
			for (int i = 0; i < dtoDynLD.getValueDTO().getValues().length; i++) {
				rowMap.put(dtoDynLD.getMetaList()[i].getCiselnikStlpecNazov(), dtoDynLD.getValueDTO().getValues()[i].getValueStr());
			}

			List<DTOCiselnikStlpecGui> guiList = new ArrayList<DTOCiselnikStlpecGui>(Arrays.asList(dtoDynLD.getMetaList()));

			List<DTOZmenaStlpec> zsList = new ArrayList<DTOZmenaStlpec>(Arrays.asList(zsPole));

			getDelegate().getWfNotif().sendNotif(auth, dtoNotif, dtoDef, dtoTodo, guiList, zsList, rowMap);

		} catch (Exception t) {
			handleException(t, "sendNotif.error", auth);
		}
	}
}
