package sk.ditec.cud.procvys.dto;

import java.util.List;

public class DTOStlpecInValue {

    private final Integer ciselnikId;
    private final Integer stlpecId;
    private final String nazov;
    private final String dbTyp;
    private final List<String> hodnoty;


    public DTOStlpecInValue(Integer ciselnikId, Integer stlpecId, String nazov, String dbTyp, List<String> hodnoty) {
        this.ciselnikId = ciselnikId;
        this.stlpecId = stlpecId;
        this.nazov = nazov;
        this.dbTyp = dbTyp;
        this.hodnoty = hodnoty;
    }

    public Integer getCiselnikId() {
        return ciselnikId;
    }

    public Integer getStlpecId() {
        return stlpecId;
    }

    public String getNazov() {
        return nazov;
    }

    public String getDbTyp() {
        return dbTyp;
    }

    public List<String> getHodnoty() {
        return hodnoty;
    }
}
