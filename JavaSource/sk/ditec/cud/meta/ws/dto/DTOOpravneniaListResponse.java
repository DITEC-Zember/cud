package sk.ditec.cud.meta.ws.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "GetOpravneniaResponse", propOrder = {
        "vsetkyCiselniky",
        "opravnenieList"
})
public class DTOOpravneniaListResponse {

    private Boolean vsetkyCiselniky;

    private DTOOpravnenieWS[] opravnenieList;

    @XmlElement(required = true)
    public Boolean getVsetkyCiselniky() {
        return vsetkyCiselniky;
    }

    public void setVsetkyCiselniky(Boolean vsetkyCiselniky) {
        this.vsetkyCiselniky = vsetkyCiselniky;
    }

    @XmlElement(name = "Opravnenie")
    public DTOOpravnenieWS[] getOpravnenieList() {
        return opravnenieList;
    }

    public void setOpravnenieList(DTOOpravnenieWS[] opravnenieList) {
        this.opravnenieList = opravnenieList;
    }

}
