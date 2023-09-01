package vjezbe.entitet;

public enum FaksIliVele {
    FAKS(1, "faks"),
    VELE(2, "veleuciliste");

    private Integer no;
    private String type;
    FaksIliVele(int no, String type) {
        this.no = no;
        this.type = type;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
