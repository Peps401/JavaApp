package vjezbe.entitet;

public class ZavrsniRad extends Entitet{
    Student student;
    Integer obrana,zavrsni;

    public ZavrsniRad(Long id, Student student, Integer obrana, Integer zavrsni) {
        super(id);
        this.student = student;
        this.obrana = obrana;
        this.zavrsni = zavrsni;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Integer getObrana() {
        return obrana;
    }

    public void setObrana(Integer obrana) {
        this.obrana = obrana;
    }

    public Integer getZavrsni() {
        return zavrsni;
    }

    public void setZavrsni(Integer zavrsni) {
        this.zavrsni = zavrsni;
    }
}
