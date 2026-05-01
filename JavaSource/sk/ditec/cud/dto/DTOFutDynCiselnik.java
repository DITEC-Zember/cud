package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "DTOCudFutDynCiselnikWS")
public class DTOFutDynCiselnik extends DTOZmena {

	Integer histID;

	// lookup field
	DTOCiselnikStlpec[] ciselnikStlpecList;

	@Override
	public String toString() {
		String s = "DTOFutDynCiselnik: {";
		s += "\n zmenaID=" + zmenaID;
		s += "\n IDCiselnik=" + IDCiselnik;
		s += "\n rowID=" + rowID;
		s += "\n operacia=" + operacia;
		s += "\n stav=" + stav;
		s += "\n platnostOd=" + platnostOd;
		s += "\n platnostDo=" + platnostDo;
		s += "\n casSchvaleniaGr=" + casSchvaleniaGr;
		s += "\n ciselnikNazov=" + ciselnikNazov;
		s += "\n ciselnikTabulka=" + ciselnikTabulka;
		s += "\n platnostOdOd=" + platnostOdOd;
		s += "\n platnostOdDo=" + platnostOdDo;
		s += "\n platnostDoOd=" + platnostDoOd;
		s += "\n platnostDoDo=" + platnostDoDo;
		s += "\n casSchvaleniaGrOd=" + casSchvaleniaGrOd;
		s += "\n casSchvaleniaGrDo=" + casSchvaleniaGrDo;
		s += "\n zmenaStavHistCasVytvorenia=" + zmenaStavHistCasVytvorenia;
		s += "\n zmenaEskalaciaCasVytvorenia=" + zmenaEskalaciaCasVytvorenia;
		s += "\n operaciaPrimLoc=" + operaciaPrimLoc;
		s += "\n operaciaSubLoc=" + operaciaSubLoc;
		s += "\n index=" + index;
		s += "\n histID=" + histID;
		return s;
	}

	public Integer getHistID() {
		return histID;
	}

	public void setHistID(Integer histID) {
		this.histID = histID;
	}

	public DTOCiselnikStlpec[] getCiselnikStlpecList() {
		return ciselnikStlpecList;
	}

	public void setCiselnikStlpecList(DTOCiselnikStlpec[] ciselnikStlpecList) {
		this.ciselnikStlpecList = ciselnikStlpecList;
	}
}
