package vjezbe.entitet;

import java.io.Serializable;
import java.util.Set;

public class Predmet extends Entitet implements Serializable {
    private String code,name;
    private Integer noOfEcts;
    private Profesor profOfSubject;
    private Set<Student> students;
    private int counter;

    /**Kreira objekt predmeta koji sadrzi sifru, ime ,broj ects-a , profesora i polje studenata
     * @param code
     * @param name
     * @param noOfEcts
     * @param person
     * @param students
     */
    public Predmet(Long id, String code, String name, Integer noOfEcts, Profesor person, Set<Student> students) {
        super(id);
        this.code = code;
        this.name = name;
        this.noOfEcts = noOfEcts;
        this.profOfSubject = person;
        this.students = students;
        counter = 0;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNoOfEcts() {
        return noOfEcts;
    }

    public void setNoOfEcts(Integer noOfEcts) {
        this.noOfEcts = noOfEcts;
    }

    public Profesor getPerson() {
        return profOfSubject;
    }

    public void setPerson(Profesor person) {
        this.profOfSubject = person;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return name ;
    }
}
