package sk.ditec.cud.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class DTOPau {

	DTOZmena zmena;
	List<DTOZmenaStlpec> zmenaStlpecList;

	List<DTOWfTodo> wfTodoList;
	List<DTOZmenaStavHist> zmenaStavHistList;
	String sql;

	Map<String, String> rowOldMap;

	DTOCiselnik ciselnik;
	List<DTOCiselnikStlpec> ciselnikStlpecList;
	DTOCiselnikStlpec ciselnikStlpecPk;
	DTOCiselnikStlpec ciselnikStlpecJedinecny;

	DTOCiselnikGui ciselnikGui;
	List<DTOCiselnikStlpecGui> ciselnikStlpecGuiList;
	DTOCiselnikStlpecGui ciselnikStlpecGuiPk;
	DTOCiselnikStlpecGui ciselnikStlpecGuiJedinecny;

	DTOPau prev;
	DTOPau next;

	String lenPlatnostDo;
	String obnovenieDopravnehoNazvu;

	Date casVytvorenia;

	public DTOZmena getZmena() {
		return zmena;
	}

	public void setZmena(DTOZmena zmena) {
		this.zmena = zmena;
	}

	public List<DTOZmenaStlpec> getZmenaStlpecList() {
		return zmenaStlpecList;
	}

	public void setZmenaStlpecList(List<DTOZmenaStlpec> zmenaStlpecList) {
		this.zmenaStlpecList = zmenaStlpecList;
	}

	public List<DTOWfTodo> getWfTodoList() {
		return wfTodoList;
	}

	public void setWfTodoList(List<DTOWfTodo> wfTodoList) {
		this.wfTodoList = wfTodoList;
	}

	public List<DTOZmenaStavHist> getZmenaStavHistList() {
		return zmenaStavHistList;
	}

	public void setZmenaStavHistList(List<DTOZmenaStavHist> zmenaStavHistList) {
		this.zmenaStavHistList = zmenaStavHistList;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, String> getRowOldMap() {
		return rowOldMap;
	}

	public void setRowOldMap(Map<String, String> rowOldMap) {
		this.rowOldMap = rowOldMap;
	}

	public DTOCiselnik getCiselnik() {
		return ciselnik;
	}

	public void setCiselnik(DTOCiselnik ciselnik) {
		this.ciselnik = ciselnik;
	}

	public List<DTOCiselnikStlpec> getCiselnikStlpecList() {
		return ciselnikStlpecList;
	}

	public void setCiselnikStlpecList(List<DTOCiselnikStlpec> ciselnikStlpecList) {
		this.ciselnikStlpecList = ciselnikStlpecList;
	}

	public DTOCiselnikStlpec getCiselnikStlpecPk() {
		return ciselnikStlpecPk;
	}

	public void setCiselnikStlpecPk(DTOCiselnikStlpec ciselnikStlpecPk) {
		this.ciselnikStlpecPk = ciselnikStlpecPk;
	}

	public DTOCiselnikStlpec getCiselnikStlpecJedinecny() {
		return ciselnikStlpecJedinecny;
	}

	public void setCiselnikStlpecJedinecny(DTOCiselnikStlpec ciselnikStlpecJedinecny) {
		this.ciselnikStlpecJedinecny = ciselnikStlpecJedinecny;
	}

	public DTOCiselnikGui getCiselnikGui() {
		return ciselnikGui;
	}

	public void setCiselnikGui(DTOCiselnikGui ciselnikGui) {
		this.ciselnikGui = ciselnikGui;
	}

	public List<DTOCiselnikStlpecGui> getCiselnikStlpecGuiList() {
		return ciselnikStlpecGuiList;
	}

	public void setCiselnikStlpecGuiList(List<DTOCiselnikStlpecGui> ciselnikStlpecGuiList) {
		this.ciselnikStlpecGuiList = ciselnikStlpecGuiList;
	}

	public DTOCiselnikStlpecGui getCiselnikStlpecGuiPk() {
		return ciselnikStlpecGuiPk;
	}

	public void setCiselnikStlpecGuiPk(DTOCiselnikStlpecGui ciselnikStlpecGuiPk) {
		this.ciselnikStlpecGuiPk = ciselnikStlpecGuiPk;
	}

	public DTOCiselnikStlpecGui getCiselnikStlpecGuiJedinecny() {
		return ciselnikStlpecGuiJedinecny;
	}

	public void setCiselnikStlpecGuiJedinecny(DTOCiselnikStlpecGui ciselnikStlpecGuiJedinecny) {
		this.ciselnikStlpecGuiJedinecny = ciselnikStlpecGuiJedinecny;
	}

	public DTOPau getPrev() {
		return prev;
	}

	public void setPrev(DTOPau prev) {
		this.prev = prev;
	}

	public DTOPau getNext() {
		return next;
	}

	public void setNext(DTOPau next) {
		this.next = next;
	}

	public String getLenPlatnostDo() {
		return lenPlatnostDo;
	}

	public void setLenPlatnostDo(String lenPlatnostDo) {
		this.lenPlatnostDo = lenPlatnostDo;
	}

	public String getObnovenieDopravnehoNazvu() {
		return obnovenieDopravnehoNazvu;
	}

	public void setObnovenieDopravnehoNazvu(String obnovenieDopravnehoNazvu) {
		this.obnovenieDopravnehoNazvu = obnovenieDopravnehoNazvu;
	}

	public Date getCasVytvorenia() {
		return casVytvorenia;
	}

	public void setCasVytvorenia(Date casVytvorenia) {
		this.casVytvorenia = casVytvorenia;
	}

}
