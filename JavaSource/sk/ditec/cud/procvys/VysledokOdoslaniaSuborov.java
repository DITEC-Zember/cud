package sk.ditec.cud.procvys;

public class VysledokOdoslaniaSuborov {
    private final boolean bolaChyba;

    private final String errorMsg;

    public static VysledokOdoslaniaSuborov nebolaChyba() {
        return new VysledokOdoslaniaSuborov(false, null);
    }

    public static VysledokOdoslaniaSuborov bolaChyba(String errorMsg) {
        return new VysledokOdoslaniaSuborov(true, errorMsg);
    }

    private VysledokOdoslaniaSuborov(boolean bolaChyba, String errorMsg) {
        this.bolaChyba = bolaChyba;
        this.errorMsg = errorMsg;
    }

    public boolean isBolaChyba() {
        return bolaChyba;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String toString() {
        return "VysledokOdoslaniaSuborov{" +
                "bolaChyba=" + bolaChyba +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }
}
