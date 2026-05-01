package sk.ditec.cud.bi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.ditec.common.db.DBUtils;
import sk.ditec.common.security.ActionResult;
import sk.ditec.common.security.AppException;
import sk.ditec.common.security.AuthInfo;
import sk.ditec.common.utils.StringUtils;
import sk.ditec.cud.dto.DTOImportZmena;
import sk.ditec.cud.dto.DTOImportZmenaStlpec;
import sk.ditec.cud.dto.DTOUcet;
import sk.ditec.cud.dto.DTOWfDef;
import sk.ditec.cud.dto.DTOWfTodo;
import sk.ditec.cud.dto.DTOWorkflow;
import sk.ditec.cud.dto.DTOZmena;
import sk.ditec.cud.dto.DTOZmenaStavHist;
import sk.ditec.cud.dto.DTOZmenaStlpec;
import sk.ditec.cud.utils._CudConsts;
import sk.ditec.cud.utils._CudLookupUtils;

public class CudWorkflowClass extends _CudBaseClass {

	private DTOWfTodo createDTOWfTodo(Integer ciselnikID, Integer wfDefID, String poznamka, Integer ucetID) throws AppException {

		try {
			DTOWfTodo dtoNew = new DTOWfTodo();
			dtoNew.setIDCiselnik(ciselnikID);
			dtoNew.setIDWfDef(wfDefID);
			dtoNew.setPoznamka(poznamka);
			dtoNew.setIDUcet(ucetID);
			dtoNew.setPotvrdeny("T");
			return dtoNew;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOWfTodo.error");
			return null;
		}
	}

	private DTOZmena createDTOZmena(Integer ciselnikID, DTOImportZmena dto) throws AppException {

		try {
			DTOZmena dtoNew = new DTOZmena();
			dtoNew.setIDCiselnik(ciselnikID);
			dtoNew.setRowID(dto.getRowID());
			dtoNew.setOperacia(dto.getOperacia());
			dtoNew.setStav(_CudConsts.ZMENA_STAV_VPO);
			dtoNew.setPlatnostOd(dto.getPlatnostOd());
			return dtoNew;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOWfTodo.error");
			return null;
		}
	}

	private List<DTOZmenaStlpec> createDTOZmenaStlpecList(Integer ciselnikID, DTOImportZmenaStlpec[] pole) throws AppException {

		try {
			List<DTOZmenaStlpec> resultList = new ArrayList<DTOZmenaStlpec>();

			for (DTOImportZmenaStlpec dtoZS : pole) {
				DTOZmenaStlpec dtoNew = new DTOZmenaStlpec();
				dtoNew.setIDCiselnik(ciselnikID);
				dtoNew.setIDCiselnikStlpec(dtoZS.getIDCiselnikStlpec());
				dtoNew.setOldValue(dtoZS.getOldValue());
				dtoNew.setNewValue(dtoZS.getNewValue());
				resultList.add(dtoNew);
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOZmenaStlpecList.error");
			return null;
		}
	}

	public DTOWorkflow createDTOWorkflowIn(AuthInfo auth, Integer ciselnikID, DTOImportZmena dto, List<DTOWfDef> wfDefList, Map<Integer, DTOUcet[]> ucetMap) throws AppException {

		try {
			DTOWorkflow resultDTO = DTOWorkflow.createDTO();

			resultDTO.setZmenaDTO(createDTOZmena(ciselnikID, dto));

			if (StringUtils.isValid(dto.getImportZmenaStlpecList())) {
				resultDTO.getZmenaStlpecList().addAll(createDTOZmenaStlpecList(ciselnikID, dto.getImportZmenaStlpecList()));
			}

			DTOWfDef dtoWfDefIn = _CudLookupUtils.lookupDTOWfDef(wfDefList, _CudConsts.WF_DEF_TYP_IN);

			DTOWfTodo dtoTodoAct = createDTOWfTodo(ciselnikID, dtoWfDefIn.getWfDefID(), dto.getPoznamka(), auth.getAccountId());
			resultDTO.getWfTodoUpdateList().add(dtoTodoAct);

			aktualizujWorkflow(auth, resultDTO, dtoTodoAct, wfDefList, new ArrayList<DTOWfTodo>(), _CudConsts.WF_DEF_TYP_IN, ucetMap);

			resultDTO.setSuborID(lookupSuborID(auth, dto.getImportZmenaStlpecList()));

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "createDTOWorkflowIn.error", auth);
			return null;
		}
	}

	public DTOWorkflow createDTOWorkflowSC(AuthInfo auth, DTOZmena dtoZmena, DTOWfTodo dtoTodo, List<DTOWfDef> wfDefList, List<DTOWfTodo> todoList, Map<Integer, DTOUcet[]> ucetMap)
			throws AppException {

		try {
			DTOWorkflow resultDTO = DTOWorkflow.createDTO();

			if (StringUtils.isValid(dtoTodo.getZmenaCasSchvaleniaGr())) {
				dtoZmena.setCasSchvaleniaGr(dtoTodo.getZmenaCasSchvaleniaGr());
			}

			resultDTO.setZmenaDTO(dtoZmena);

			resultDTO.getWfTodoUpdateList().add(dtoTodo);

			aktualizujWorkflow(auth, resultDTO, dtoTodo, wfDefList, todoList, _CudConsts.WF_DEF_TYP_SC, ucetMap);

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "createDTOWorkflowSC.error", auth);
			return null;
		}
	}

	private Integer lookupSuborID(AuthInfo auth, DTOImportZmenaStlpec[] zmenaPole) throws AppException {

		try {
			DTOImportZmenaStlpec dtoZS = null;
			for (DTOImportZmenaStlpec dtoIZS : zmenaPole) {
				if ("T".equals(dtoIZS.getSubor())) {
					dtoZS = dtoIZS;
				}
			}
			if (!StringUtils.isValid(dtoZS) || !StringUtils.isValid(dtoZS.getNewValue())) {
				return null;
			}

			Integer suborID = Integer.parseInt(dtoZS.getNewValue());
			String spracovany = getDelegate().getDynCiselnikRead().suborReadSpracovany(auth, "T_" + dtoZS.getCiselnikStlpecNazov().substring(3), suborID);

			return "F".equals(spracovany) ? suborID : null;

		} catch (Exception t) {
			handleException(t, "lookupSuborID.error", auth);
			return null;
		}
	}

	public DTOWorkflow generujWorkflowAll(AuthInfo auth, Integer ciselnikID, DTOImportZmena dtoZmena, List<DTOWfDef> wfDefList, Map<Integer, DTOUcet[]> ucetMap)
			throws AppException {

		try {
			if (!StringUtils.isValid(dtoZmena.getImportZmenaStlpecList())) {
				return null;
			}

			DTOWorkflow resultDTO = createDTOWorkflowIn(auth, ciselnikID, dtoZmena, wfDefList, ucetMap);
			resultDTO.getWfTodoDeleteSet().clear();
			List<DTOWfTodo> todoList = resultDTO.getWfTodoUpdateList();
			resultDTO.setWfTodoUpdateList(new ArrayList<DTOWfTodo>());
			resultDTO.getWfTodoUpdateList().add(todoList.get(0));

			DTOWfTodo dtoTodoNext = _CudLookupUtils.lookupDTOWfTodo(todoList, resultDTO.getDefActDTO().getIDWfDefNasl(), auth.getAccountId());
			DTOWfDef dtoDefNext = _CudLookupUtils.lookupDTOWfDef(wfDefList, dtoTodoNext.getIDWfDef());

			while (StringUtils.isValid(dtoTodoNext) && StringUtils.isValid(dtoDefNext) && !_CudConsts.WF_DEF_TYP_OV.equals(dtoDefNext.getTyp())) {

				dtoTodoNext.setPotvrdeny("T");
				DTOWorkflow dtoNextWF = createDTOWorkflowSC(auth, resultDTO.getZmenaDTO(), dtoTodoNext, wfDefList, new ArrayList<DTOWfTodo>(), ucetMap);

				resultDTO.getWfTodoUpdateList().add(dtoNextWF.getWfTodoUpdateList().get(0));
				resultDTO.getZmenaStavHistList().add(dtoNextWF.getZmenaStavHistList().get(0));

				dtoTodoNext = _CudLookupUtils.lookupDTOWfTodo(dtoNextWF.getWfTodoUpdateList(), dtoNextWF.getDefActDTO().getIDWfDefNasl(), auth.getAccountId());
				dtoDefNext = _CudLookupUtils.lookupDTOWfDef(wfDefList, dtoTodoNext.getIDWfDef());
			}

			if (StringUtils.isValid(dtoDefNext)) {
				resultDTO.getWfTodoUpdateList().add(dtoTodoNext);
			}

			return resultDTO;

		} catch (Throwable t) {
			handleException(t, "generujWorkflowAll.error", auth);
			return null;
		}
	}

	private Set<Integer> nepotvrdeneTodoIDsSet(List<DTOWfTodo> todoList, Integer wfTodoID) throws AppException {

		try {
			Set<Integer> resultSet = new HashSet<Integer>();

			if (!StringUtils.isValid(todoList)) {
				return resultSet;
			}

			for (DTOWfTodo dto : todoList) {
				if (StringUtils.isValid(wfTodoID)) {
					if (wfTodoID.intValue() != dto.getWfTodoID().intValue() && !StringUtils.isValid(dto.getPotvrdeny())) {
						resultSet.add(dto.getWfTodoID());
					}
				} else {
					if (!StringUtils.isValid(dto.getPotvrdeny())) {
						resultSet.add(dto.getWfTodoID());
					}
				}
			}
			return resultSet;

		} catch (Throwable t) {
			DBUtils.handleException(t, "nepotvrdeneTodoIDsSet.error");
			return null;
		}
	}

	private List<DTOWfTodo> createTodoListBySkupina(AuthInfo auth, Integer ciselnikID, Integer zmenaID, Integer wfDefID, Integer skupinaID, Map<Integer, DTOUcet[]> ucetMap)
			throws AppException {

		try {
			List<DTOWfTodo> resultList = new ArrayList<DTOWfTodo>();

			DTOUcet[] ucetList = ucetMap.get(skupinaID);
			if (!StringUtils.isValid(ucetList)) {
				ucetList = getDelegate().getIam().ucetSkupinaList(auth, skupinaID);
				ucetMap.put(skupinaID, ucetList);
			}

			for (DTOUcet dto : ucetList) {
				DTOWfTodo dtoNew = new DTOWfTodo();
				dtoNew.setIDCiselnik(ciselnikID);
				dtoNew.setIDZmena(zmenaID);
				dtoNew.setIDWfDef(wfDefID);
				dtoNew.setIDUcet(dto.getUcetID());
				resultList.add(dtoNew);
			}

			return resultList;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createTodoListBySkupina.error");
			return null;
		}
	}

	private DTOZmenaStavHist createDTOZmenaStavHist(Integer ciselnikID, String stav) throws AppException {

		try {
			DTOZmenaStavHist dtoNew = new DTOZmenaStavHist();
			dtoNew.setIDCiselnik(ciselnikID);
			dtoNew.setStav(stav);
			return dtoNew;

		} catch (Throwable t) {
			DBUtils.handleException(t, "createDTOZmenaStavHist.error");
			return null;
		}
	}

	public void aktualizujWorkflow(AuthInfo auth, DTOWorkflow dtoWF, DTOWfTodo dtoTodoAct, List<DTOWfDef> defList, List<DTOWfTodo> todoList, String wfDefTyp,
			Map<Integer, DTOUcet[]> ucetMap) throws AppException {

		try {
			if (!StringUtils.isValid(dtoTodoAct)) {
				throw new AppException("Nekorektny krok WF");
			}

			dtoWF.setDefActDTO(_CudLookupUtils.lookupDTOWfDef(defList, dtoTodoAct.getIDWfDef()));
			if (!StringUtils.isValid(dtoWF.getDefActDTO()) || !dtoWF.getDefActDTO().getTyp().equals(wfDefTyp)) {
				throw new AppException("Nekorektny krok WF");
			}

			if (_CudConsts.WF_DEF_ZODPOVEDNOST_J.equals(dtoWF.getDefActDTO().getZodpovednost())) {
				Set<Integer> set = nepotvrdeneTodoIDsSet(todoList, dtoTodoAct.getWfTodoID());
				if (!set.isEmpty()) {
					dtoWF.getWfTodoDeleteSet().addAll(set);
				}

			} else if (_CudConsts.WF_DEF_ZODPOVEDNOST_V.equals(dtoWF.getDefActDTO().getZodpovednost())) {
				if ("T".equals(dtoTodoAct.getPotvrdeny())) {
					Set<Integer> set = nepotvrdeneTodoIDsSet(todoList, dtoTodoAct.getWfTodoID());
					if (!set.isEmpty()) {
						return;
					}
				}
			}

			if ("F".equals(dtoTodoAct.getPotvrdeny())) {
				dtoWF.getZmenaDTO().setStav(_CudConsts.ZMENA_STAV_ZAM);
				Set<Integer> set = nepotvrdeneTodoIDsSet(todoList, dtoTodoAct.getWfTodoID());
				if (!set.isEmpty()) {
					dtoWF.getWfTodoDeleteSet().addAll(set);
				}

			} else {

				if (_CudConsts.WF_DEF_TYP_IN.equals(dtoWF.getDefActDTO().getTyp())) {
					dtoWF.getZmenaDTO().setStav(_CudConsts.ZMENA_STAV_VPO);
				} else if (_CudConsts.WF_DEF_TYP_SC.equals(dtoWF.getDefActDTO().getTyp())) {
					dtoWF.getZmenaDTO().setStav(_CudConsts.ZMENA_STAV_SCH);
				} else if (_CudConsts.WF_DEF_TYP_OV.equals(dtoWF.getDefActDTO().getTyp())) {
					dtoWF.getZmenaDTO().setStav(_CudConsts.ZMENA_STAV_PAU);
				}

				if (StringUtils.isValid(dtoWF.getDefActDTO().getIDWfDefNasl())) {
					DTOWfDef dtoDefNext = _CudLookupUtils.lookupDTOWfDef(defList, dtoWF.getDefActDTO().getIDWfDefNasl());
					List<DTOWfTodo> list = createTodoListBySkupina(auth, dtoWF.getZmenaDTO().getIDCiselnik(), dtoWF.getZmenaDTO().getZmenaID(), dtoDefNext.getWfDefID(),
							dtoDefNext.getIDSkupina(), ucetMap);
					if (!list.isEmpty()) {
						dtoWF.getWfTodoUpdateList().addAll(list);
					}
				}
			}

			dtoWF.getZmenaStavHistList().add(createDTOZmenaStavHist(dtoWF.getZmenaDTO().getIDCiselnik(), dtoWF.getZmenaDTO().getStav()));

		} catch (Throwable t) {
			DBUtils.handleException(t, "aktualizujWorkflow.error");
		}
	}

	public ActionResult workflowUpdateSoft(AuthInfo auth, DTOWorkflow dto, DTOImportZmena dtoZmena, Date d) throws AppException {

		try {
			getDelegate().getZmenaModify().updateSoft(auth, dto.getZmenaDTO());
			getDelegate().getZmenaStlpecModify().update(auth, dto.getZmenaStlpecList(), dto.getZmenaDTO().getZmenaID());
			getDelegate().getWfTodoModify().delete(auth, dto.getWfTodoDeleteSet());
			getDelegate().getWfTodoModify().update(auth, dto.getWfTodoUpdateList(), dto.getZmenaDTO().getZmenaID());
			getDelegate().getZmenaStavHistModify().update(auth, dto.getZmenaStavHistList(), dto.getZmenaDTO().getZmenaID(), d);
			getDelegate().getDynCiselnikModify().suborUpdateSpracovany(auth, dto.getSuborCiselnikTabulka(), dto.getSuborID());

			if (StringUtils.isValid(dtoZmena)) {
				getDelegate().getImportZmenaModify().updateSpracovany(auth, dtoZmena.getIDImport(), dtoZmena.getImportZmenaID());
			}

			return new ActionResult(null);

		} catch (Throwable t) {
			return handleException(t, "workflowUpdateSoft.error", auth);
		}
	}

	public ActionResult workflowUpdateSoftCrd(AuthInfo auth, DTOWorkflow dto, DTOImportZmena dtoZmena, Date d) throws AppException {

		try {
			ActionResult res = getDelegate().getZmenaModify().updateSoft(auth, dto.getZmenaDTO());
			getDelegate().getZmenaStlpecModify().update(auth, dto.getZmenaStlpecList(), dto.getZmenaDTO().getZmenaID());
			getDelegate().getWfTodoModify().delete(auth, dto.getWfTodoDeleteSet());
			getDelegate().getWfTodoModify().update(auth, dto.getWfTodoUpdateList(), dto.getZmenaDTO().getZmenaID());
			getDelegate().getZmenaStavHistModify().update(auth, dto.getZmenaStavHistList(), dto.getZmenaDTO().getZmenaID(), d);

			if (StringUtils.isValid(dtoZmena)) {
				getDelegate().getImportZmenaModify().updateSpracovany(auth, dtoZmena.getIDImport(), dtoZmena.getImportZmenaID());
			}

			return new ActionResult(res.getResult());

		} catch (Throwable t) {
			return handleException(t, "workflowUpdateSoft.error", auth);
		}
	}

	public ActionResult workflowUpdate(AuthInfo auth, DTOWorkflow dto) throws AppException {

		startTransaction(auth, "CUDdataModify");

		try {
			Date d = new Date();

			getConnection(auth);

			ActionResult res = workflowUpdateSoft(auth, dto, null, d);

			returnConnection(auth);

			endTransaction(auth, true);

			return res;

		} catch (Throwable t) {
			return handleException(t, "workflowUpdate.error", auth);
		}
	}

}
