package sk.ditec.cud.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class DTOMeta {

	DTOCiselnik[] ciselnikPole;
	DTOCiselnikGui[] ciselnikGuiPole;

	Map<Integer, List<DTOCiselnikStlpec>> ciselnikStlpecMap;
	Map<Integer, List<DTOCiselnikStlpecGui>> ciselnikStlpecGuiMap;

	Date platnostOd;

	public DTOCiselnik[] getCiselnikPole() {
		return ciselnikPole;
	}

	public void setCiselnikPole(DTOCiselnik[] ciselnikPole) {
		this.ciselnikPole = ciselnikPole;
	}

	public DTOCiselnikGui[] getCiselnikGuiPole() {
		return ciselnikGuiPole;
	}

	public void setCiselnikGuiPole(DTOCiselnikGui[] ciselnikGuiPole) {
		this.ciselnikGuiPole = ciselnikGuiPole;
	}

	public Map<Integer, List<DTOCiselnikStlpec>> getCiselnikStlpecMap() {
		return ciselnikStlpecMap;
	}

	public void setCiselnikStlpecMap(Map<Integer, List<DTOCiselnikStlpec>> ciselnikStlpecMap) {
		this.ciselnikStlpecMap = ciselnikStlpecMap;
	}

	public Map<Integer, List<DTOCiselnikStlpecGui>> getCiselnikStlpecGuiMap() {
		return ciselnikStlpecGuiMap;
	}

	public void setCiselnikStlpecGuiMap(Map<Integer, List<DTOCiselnikStlpecGui>> ciselnikStlpecGuiMap) {
		this.ciselnikStlpecGuiMap = ciselnikStlpecGuiMap;
	}

	public Date getPlatnostOd() {
		return platnostOd;
	}

	public void setPlatnostOd(Date platnostOd) {
		this.platnostOd = platnostOd;
	}

}
