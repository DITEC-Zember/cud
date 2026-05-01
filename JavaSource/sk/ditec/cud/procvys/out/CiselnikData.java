package sk.ditec.cud.procvys.out;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {
        "nazovCiselnika",
        "iDCiselnika",
        "menoDbTabulky",
        "celkovyPocetExpZaznamov",
        "stranka",
        "pocetZaznamovNaStranku",
        "rozsahExportu",
        "datumVytvoreniaExportu",
        "datumPredchadzajucehoExportu",
        "identifikatorSpravy",
        "ciselnikStlpecList",
        "zaznamList"
})
public class CiselnikData implements Serializable {

    @XmlElement(name = "NazovCiselnika")
    private String nazovCiselnika;

    @XmlElement(name = "IDCiselnika")
    private Integer iDCiselnika;

    @XmlElement(name = "MenoDBTabulky")
    private String menoDbTabulky;

    @XmlElement(name = "CelkovyPocetExportovanychZaznamov")
    private Integer celkovyPocetExpZaznamov;

    @XmlElement(name = "Stranka", required = true)
    private Integer stranka;

    @XmlElement(name = "PocetZaznamovNaStranku", required = true)
    private Integer pocetZaznamovNaStranku;

    @XmlElement(name = "RozsahExportu", required = true)
    private String rozsahExportu;

    @XmlElement(name = "DatumVytvoreniaExportu", required = true)
    private Date datumVytvoreniaExportu;

    @XmlElement(name = "DatumPredchadzajucehoExportu", required = true)
    private Date datumPredchadzajucehoExportu;

    @XmlElement(name = "IdentifikatorSpravy", required = true)
    private UUID identifikatorSpravy;

    @XmlElement(name = "ciselnikStlpecList", required = true)
    private List<CiselnikStlpec> ciselnikStlpecList = new ArrayList<CiselnikStlpec>();

    @XmlElement(name = "zaznamList", required = true)
    private List<Zaznam> zaznamList = new ArrayList<Zaznam>();

    public String getNazovCiselnika() {
        return nazovCiselnika;
    }

    public void setNazovCiselnika(String nazovCiselnika) {
        this.nazovCiselnika = nazovCiselnika;
    }

    public Integer getiDCiselnika() {
        return iDCiselnika;
    }

    public void setiDCiselnika(Integer iDCiselnika) {
        this.iDCiselnika = iDCiselnika;
    }

    public String getMenoDbTabulky() {
        return menoDbTabulky;
    }

    public void setMenoDbTabulky(String menoDbTabulky) {
        this.menoDbTabulky = menoDbTabulky;
    }

    public Integer getCelkovyPocetExpZaznamov() {
        return celkovyPocetExpZaznamov;
    }

    public void setCelkovyPocetExpZaznamov(Integer celkovyPocetExpZaznamov) {
        this.celkovyPocetExpZaznamov = celkovyPocetExpZaznamov;
    }

    public Integer getStranka() {
        return stranka;
    }

    public void setStranka(Integer stranka) {
        this.stranka = stranka;
    }

    public Integer getPocetZaznamovNaStranku() {
        return pocetZaznamovNaStranku;
    }

    public void setPocetZaznamovNaStranku(Integer pocetZaznamovNaStranku) {
        this.pocetZaznamovNaStranku = pocetZaznamovNaStranku;
    }

    public String getRozsahExportu() {
        return rozsahExportu;
    }

    public void setRozsahExportu(String rozsahExportu) {
        this.rozsahExportu = rozsahExportu;
    }

    public Date getDatumVytvoreniaExportu() {
        return datumVytvoreniaExportu;
    }

    public void setDatumVytvoreniaExportu(Date datumVytvoreniaExportu) {
        this.datumVytvoreniaExportu = datumVytvoreniaExportu;
    }

    public Date getDatumPredchadzajucehoExportu() {
        return datumPredchadzajucehoExportu;
    }

    public void setDatumPredchadzajucehoExportu(Date datumPredchadzajucehoExportu) {
        this.datumPredchadzajucehoExportu = datumPredchadzajucehoExportu;
    }

    public UUID getIdentifikatorSpravy() {
        return identifikatorSpravy;
    }

    public void setIdentifikatorSpravy(UUID identifikatorSpravy) {
        this.identifikatorSpravy = identifikatorSpravy;
    }

    public List<CiselnikStlpec> getCiselnikStlpecList() {
        return ciselnikStlpecList;
    }

    public void setCiselnikStlpecList(List<CiselnikStlpec> ciselnikStlpecList) {
        this.ciselnikStlpecList = ciselnikStlpecList;
    }

    public List<Zaznam> getZaznamList() {
        return zaznamList;
    }

    public void setZaznamList(List<Zaznam> zaznamList) {
        this.zaznamList = zaznamList;
    }
}
