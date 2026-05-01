package sk.ditec.cud.procvys.out;

import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {
        "dbTyp",
        "typ",
        "povinne",
        "stlpecID",
        "popis",
        "nazov",
        "nadpis",
        "jedinecne",
        "fk1Tabulka",
        "IDCiselnik",
        "fk1PkNazov",
        "fk1IDCIselnik",
        "dlzka",
        "decimals"
})
public class CiselnikStlpec {

    private String dbTyp;
    private String typ;
    private String povinne;
    private Integer stlpecID;
    private String popis;
    private String nazov;
    private String nadpis;
    private String jedinecne;
    private String fk1Tabulka;
    private Integer IDCiselnik;
    private String fk1PkNazov;
    private Integer fk1IDCIselnik;
    private Integer dlzka;
    private Integer decimals;

    public String getDbTyp() {
        return dbTyp;
    }

    public void setDbTyp(String dbTyp) {
        this.dbTyp = dbTyp;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getPovinne() {
        return povinne;
    }

    public void setPovinne(String povinne) {
        this.povinne = povinne;
    }

    public Integer getStlpecID() {
        return stlpecID;
    }

    public void setStlpecID(Integer stlpecID) {
        this.stlpecID = stlpecID;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getNadpis() {
        return nadpis;
    }

    public void setNadpis(String nadpis) {
        this.nadpis = nadpis;
    }

    public String getJedinecne() {
        return jedinecne;
    }

    public void setJedinecne(String jedinecne) {
        this.jedinecne = jedinecne;
    }

    public String getFk1Tabulka() {
        return fk1Tabulka;
    }

    public void setFk1Tabulka(String fk1Tabulka) {
        this.fk1Tabulka = fk1Tabulka;
    }

    public Integer getIDCiselnik() {
        return IDCiselnik;
    }

    public void setIDCiselnik(Integer IDCiselnik) {
        this.IDCiselnik = IDCiselnik;
    }

    public String getFk1PkNazov() {
        return fk1PkNazov;
    }

    public void setFk1PkNazov(String fk1PkNazov) {
        this.fk1PkNazov = fk1PkNazov;
    }

    public Integer getFk1IDCIselnik() {
        return fk1IDCIselnik;
    }

    public void setFk1IDCIselnik(Integer fk1IDCIselnik) {
        this.fk1IDCIselnik = fk1IDCIselnik;
    }

    public Integer getDlzka() {
        return dlzka;
    }

    public void setDlzka(Integer dlzka) {
        this.dlzka = dlzka;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }
}
