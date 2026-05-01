package sk.ditec.cud.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.ditec.common.bi.DTO;

public class DTOWorkflow extends DTO {

	DTOZmena zmenaDTO;
	List<DTOZmenaStlpec> zmenaStlpecList;
	List<DTOWfTodo> wfTodoUpdateList;
	Set<Integer> wfTodoDeleteSet;
	List<DTOZmenaStavHist> zmenaStavHistList;

	DTOWfDef defActDTO;

	String suborCiselnikTabulka;
	Integer suborID;

	private DTOWorkflow() {
	}

	public static DTOWorkflow createDTO() {

		DTOWorkflow dtoNew = new DTOWorkflow();
		dtoNew.setZmenaStlpecList(new ArrayList<DTOZmenaStlpec>());
		dtoNew.setWfTodoUpdateList(new ArrayList<DTOWfTodo>());
		dtoNew.setWfTodoDeleteSet(new HashSet<Integer>());
		dtoNew.setZmenaStavHistList(new ArrayList<DTOZmenaStavHist>());
		return dtoNew;
	}

	public DTOZmena getZmenaDTO() {
		return zmenaDTO;
	}

	public void setZmenaDTO(DTOZmena zmenaDTO) {
		this.zmenaDTO = zmenaDTO;
	}

	public List<DTOZmenaStlpec> getZmenaStlpecList() {
		return zmenaStlpecList;
	}

	public void setZmenaStlpecList(List<DTOZmenaStlpec> zmenaStlpecList) {
		this.zmenaStlpecList = zmenaStlpecList;
	}

	public List<DTOWfTodo> getWfTodoUpdateList() {
		return wfTodoUpdateList;
	}

	public void setWfTodoUpdateList(List<DTOWfTodo> wfTodoUpdateList) {
		this.wfTodoUpdateList = wfTodoUpdateList;
	}

	public Set<Integer> getWfTodoDeleteSet() {
		return wfTodoDeleteSet;
	}

	public void setWfTodoDeleteSet(Set<Integer> wfTodoDeleteSet) {
		this.wfTodoDeleteSet = wfTodoDeleteSet;
	}

	public List<DTOZmenaStavHist> getZmenaStavHistList() {
		return zmenaStavHistList;
	}

	public void setZmenaStavHistList(List<DTOZmenaStavHist> zmenaStavHistList) {
		this.zmenaStavHistList = zmenaStavHistList;
	}

	public DTOWfDef getDefActDTO() {
		return defActDTO;
	}

	public void setDefActDTO(DTOWfDef defActDTO) {
		this.defActDTO = defActDTO;
	}

	public Integer getSuborID() {
		return suborID;
	}

	public void setSuborID(Integer suborID) {
		this.suborID = suborID;
	}

	public String getSuborCiselnikTabulka() {
		return suborCiselnikTabulka;
	}

	public void setSuborCiselnikTabulka(String suborCiselnikTabulka) {
		this.suborCiselnikTabulka = suborCiselnikTabulka;
	}

}
