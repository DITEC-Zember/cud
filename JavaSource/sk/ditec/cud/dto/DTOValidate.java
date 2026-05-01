package sk.ditec.cud.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.ditec.cud.utils._CudConsts;

public class DTOValidate {

	Map<String, String> oldValueMap;
	Map<String, String> newValueMap;

	Map<Integer, Set<String>> errorsMap;
	Map<Integer, Set<String>> warningMap;

	Integer ciselnikID;
	String ciselnikTabulka;

	DTOImportZmena importZmenaDTO;
	List<DTOImportZmenaStlpec> importZmenaStlpecList;

	DTOPlugin[] pluginList;

	String zdroj;

	String platnostOd;

	private DTOValidate() {
	}

	public static DTOValidate createDTO(DTOImport dtoImport, String zdroj, Date d, DTOPlugin[] pluginList, Map<String, Map<String, String>> pluginLookupMap) {

		DTOValidate dtoNew = new DTOValidate();

		dtoNew.setOldValueMap(new HashMap<String, String>());
		dtoNew.setNewValueMap(new HashMap<String, String>());
		dtoNew.setErrorsMap(new HashMap<Integer, Set<String>>());
		dtoNew.setWarningMap(new HashMap<Integer, Set<String>>());
		dtoNew.setImportZmenaDTO(new DTOImportZmena());
		dtoNew.setImportZmenaStlpecList(new ArrayList<DTOImportZmenaStlpec>());

		dtoNew.setCiselnikID(dtoImport.getIDCiselnik());
		dtoNew.setCiselnikTabulka(dtoImport.getCiselnikTabulka());
		dtoNew.setZdroj(zdroj);
		dtoNew.setPlatnostOd(_CudConsts.DATE_FORMAT.format(d));
		dtoNew.getImportZmenaDTO().setSpracovany("F");
		dtoNew.getImportZmenaDTO().setObnova("F");

		dtoNew.setPluginList(pluginList);

		return dtoNew;
	}

	public Map<String, String> getOldValueMap() {
		return oldValueMap;
	}

	public void setOldValueMap(Map<String, String> oldValueMap) {
		this.oldValueMap = oldValueMap;
	}

	public Map<String, String> getNewValueMap() {
		return newValueMap;
	}

	public void setNewValueMap(Map<String, String> newValueMap) {
		this.newValueMap = newValueMap;
	}

	public Map<Integer, Set<String>> getErrorsMap() {
		return errorsMap;
	}

	public void setErrorsMap(Map<Integer, Set<String>> errorsMap) {
		this.errorsMap = errorsMap;
	}

	public Map<Integer, Set<String>> getWarningMap() {
		return warningMap;
	}

	public void setWarningMap(Map<Integer, Set<String>> warningMap) {
		this.warningMap = warningMap;
	}

	public String getZdroj() {
		return zdroj;
	}

	public void setZdroj(String zdroj) {
		this.zdroj = zdroj;
	}

	public DTOImportZmena getImportZmenaDTO() {
		return importZmenaDTO;
	}

	public void setImportZmenaDTO(DTOImportZmena importZmenaDTO) {
		this.importZmenaDTO = importZmenaDTO;
	}

	public List<DTOImportZmenaStlpec> getImportZmenaStlpecList() {
		return importZmenaStlpecList;
	}

	public void setImportZmenaStlpecList(List<DTOImportZmenaStlpec> importZmenaStlpecList) {
		this.importZmenaStlpecList = importZmenaStlpecList;
	}

	public Integer getCiselnikID() {
		return ciselnikID;
	}

	public void setCiselnikID(Integer ciselnikID) {
		this.ciselnikID = ciselnikID;
	}

	public String getCiselnikTabulka() {
		return ciselnikTabulka;
	}

	public void setCiselnikTabulka(String ciselnikTabulka) {
		this.ciselnikTabulka = ciselnikTabulka;
	}

	public String getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(String platnostOd) {
		this.platnostOd = platnostOd;
	}

	public DTOPlugin[] getPluginList() {
		return pluginList;
	}

	public void setPluginList(DTOPlugin[] pluginList) {
		this.pluginList = pluginList;
	}

}
