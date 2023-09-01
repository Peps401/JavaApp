package vjezbe.entitet;

import java.time.LocalDate;
import java.util.Objects;


public class Student extends Osoba {
    private String jmbag;
    LocalDate dateOfBirth;


    /**Kreira objekt studenta koji sadrzi ime, prezime, jmbag i datum rodenja
     * @param name
     * @param surname
     * @param jmbag
     * @param dateOfBirth
     */
    public Student(Long id, String name, String surname, String jmbag, LocalDate dateOfBirth) {
        super(id, name, surname);
        this.jmbag = jmbag;
        this.dateOfBirth = dateOfBirth;
    }


    public String getJmbag() {
        return jmbag;
    }

    public void setJmbag(String jmbag) {
        this.jmbag = jmbag;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return getName() + " " + getSurname();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(getName(), student.getName()) && Objects.equals(getSurname(), student.getSurname()) && Objects.equals(jmbag, student.jmbag) && Objects.equals(dateOfBirth, student.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getSurname(), jmbag, dateOfBirth);
    }


}
