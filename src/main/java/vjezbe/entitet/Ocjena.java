package vjezbe.entitet;

import java.math.BigDecimal;

public enum Ocjena {
    NEDOVOLJAN(1, "nedovoljan"),
    DOVOLJAN(2, "dovoljan"),
    DOBAR(3, "dobar"),
    VRLO_DOBAR(4, "vrlo dobar"),
    IZVRSTAN(5, "izvrstan");

    public final int ocjenaBroj;
    public final String ocjenaString;
    Ocjena(int ocjenaBroj, String ocjenaString) {
        this.ocjenaBroj = ocjenaBroj;
        this.ocjenaString = ocjenaString;
    }

    public int getOcjenaBroj() {
        return ocjenaBroj;
    }

    public String getOcjenaString() {
        return ocjenaString;
    }

    public BigDecimal toBigDecimal() {
        return new BigDecimal(ocjenaString);
    }
}
