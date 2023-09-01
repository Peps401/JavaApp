package vjezbe.entitet;

import java.io.Serializable;
import java.time.LocalDateTime;

public final class Ispit extends Entitet implements Online, Serializable {
    private Predmet predmet;
    private Student student;
    private  Enum<Ocjena> grade;
    private LocalDateTime dateNtime;

    private Dvorana room;

    /**Kreira objekt ispita koji sadrzi predmet, studenta, ocjenu, vrijeme i dvoranu
     * @param predmet
     * @param student
     * @param grade
     * @param dateNtime
     * @param room
     */
    public Ispit(Long id, Predmet predmet, Student student, Enum<Ocjena> grade, LocalDateTime dateNtime, Dvorana room) {
        super(id);
        this.predmet = predmet;
        this.student = student;
        this.grade = grade;
        this.dateNtime = dateNtime;
        this.room = room;
    }

    public Predmet getPredmet() {
        return predmet;
    }

    public void setPredmet(Predmet predmet) {
        this.predmet = predmet;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Enum<Ocjena> getGrade() {
        return grade;
    }

    public void setGrade(Enum<Ocjena> grade) {
        this.grade = grade;
    }

    public LocalDateTime getDateNtime() {
        return dateNtime;
    }

    public void setDateNtime(LocalDateTime dateNtime) {
        this.dateNtime = dateNtime;
    }

    public Dvorana getRoom() {
        return room;
    }

    public void setRoom(Dvorana room) {
        this.room = room;
    }

    @Override
    public void method(String nameSoftware) {
        System.out.println("Ispit se pise online pomocu softvera " + nameSoftware);
    }

    @Override
    public String toString() {
        return "Ispit{" +
                "predmet=" + predmet +
                ", student=" + student +
                ", grade=" + grade +
                ", dateNtime=" + dateNtime +
                ", room=" + room +
                '}';
    }
}
