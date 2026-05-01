package sk.ditec.cud.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "DTOCudFutPocetnostDynCiselnikWS")
public class DTOFutPocetnostDynCiselnik extends DTOZmena {

	Integer pocet;

	DTOCiselnikStlpec[] ciselnikStlpecList;

	@Override
	public String toString() {
		String s = "DTOFutPocetnostDynCiselnik: {";
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
		s += "\n pocet=" + pocet;
		return s;
	}

	public Integer getPocet() {
		return pocet;
	}

	public void setPocet(Integer pocet) {
		this.pocet = pocet;
	}

	public DTOCiselnikStlpec[] getCiselnikStlpecList() {
		return ciselnikStlpecList;
	}

	public void setCiselnikStlpecList(DTOCiselnikStlpec[] ciselnikStlpecList) {
		this.ciselnikStlpecList = ciselnikStlpecList;
	}

}
