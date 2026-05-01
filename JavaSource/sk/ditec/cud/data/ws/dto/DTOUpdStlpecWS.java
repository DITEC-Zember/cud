package sk.ditec.cud.data.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "UdpStlpec")
public class DTOUpdStlpecWS {

	String nazovStlpca;
	String novaHodnota;

	public String getNazovStlpca() {
		return nazovStlpca;
	}

	public void setNazovStlpca(String nazovStlpca) {
		this.nazovStlpca = nazovStlpca;
	}

	public String getNovaHodnota() {
		return novaHodnota;
	}

	public void setNovaHodnota(String novaHodnota) {
		this.novaHodnota = novaHodnota;
	}

}
