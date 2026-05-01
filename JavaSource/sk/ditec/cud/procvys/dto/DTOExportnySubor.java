package sk.ditec.cud.procvys.dto;

import java.util.Date;
import java.util.UUID;

public class DTOExportnySubor {

    private String subor;
    private Integer idCiselnik;
    private Integer rowIdExt;
    private String nazovSuboru;
    private Integer poradoveCislo;
    private UUID spravaUuid;
    private Date casVytvorenia;

    public String getSubor() {
        return subor;
    }

    public void setSubor(String subor) {
        this.subor = subor;
    }

    public Integer getIdCiselnik() {
        return idCiselnik;
    }

    public void setIdCiselnik(Integer idCiselnik) {
        this.idCiselnik = idCiselnik;
    }

    public Integer getRowIdExt() { return rowIdExt; }

    public void setRowIdExt(Integer rowIdExt) { this.rowIdExt = rowIdExt; }

    public String getNazovSuboru() {
        return nazovSuboru;
    }

    public void setNazovSuboru(String nazovSuboru) {
        this.nazovSuboru = nazovSuboru;
    }

    public Integer getPoradoveCislo() {
        return poradoveCislo;
    }

    public void setPoradoveCislo(Integer poradoveCislo) {
        this.poradoveCislo = poradoveCislo;
    }

    public UUID getSpravaUuid() {
        return spravaUuid;
    }

    public void setSpravaUuid(UUID spravaUuid) {
        this.spravaUuid = spravaUuid;
    }

    public Date getCasVytvorenia() {
        return casVytvorenia;
    }

    public void setCasVytvorenia(Date casVytvorenia) {
        this.casVytvorenia = casVytvorenia;
    }
}
