package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlType;

@XmlType(name = "TupdCiselnikMetaResponse")
public class DTOUpdCiselnikMetaResponse {

    private int kod;

    private String sprava;

    public int getKod() {
        return kod;
    }

    public void setKod(int kod) {
        this.kod = kod;
    }

    public String getSprava() {
        return sprava;
    }

    public void setSprava(String sprava) {
        this.sprava = sprava;
    }

}
