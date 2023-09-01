package vjezbe.entitet;

import java.io.Serializable;
import java.util.List;


public abstract class ObrazovnaUstanova extends Entitet implements Serializable {
    private Enum<FaksIliVele> typeOfCollege;
    private String name;
    private List<Predmet> subjectsList;
    private List<Profesor> profesorsList;
    private List<Student> studentsList;
    private List<Ispit> examsList;


    /**Konstruktor koji stvara objekt Obrazovne ustanove
     * @param name
     * @param subjectsList
     * @param profesorsList
     * @param studentsList
     * @param examsList
     */
    public ObrazovnaUstanova(Long id, Enum<FaksIliVele> typeOfCollege, String name, List<Predmet> subjectsList, List<Profesor> profesorsList, List<Student> studentsList, List<Ispit> examsList) {
        super(id);
        this.typeOfCollege = typeOfCollege;
        this.name = name;
        this.subjectsList = subjectsList;
        this.profesorsList = profesorsList;
        this.studentsList = studentsList;
        this.examsList = examsList;
    }

    public int getNoOfStudents() {
        return this.studentsList.size();
    }

    public Enum<FaksIliVele> getTypeOfCollege() {
        return typeOfCollege;
    }

    public void setTypeOfCollege(Enum<FaksIliVele> typeOfCollege) {
        this.typeOfCollege = typeOfCollege;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Predmet> getSubjectsList() {
        return subjectsList;
    }

    public void setSubjectsList(List<Predmet> subjectsList) {
        this.subjectsList = subjectsList;
    }

    public List<Profesor> getProfesorsList() {
        return profesorsList;
    }

    public void setProfesorsList(List<Profesor> profesorsList) {
        this.profesorsList = profesorsList;
    }

    public List<Student> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(List<Student> studentsList) {
        this.studentsList = studentsList;
    }

    public List<Ispit> getExamsList() {
        return examsList;
    }

    public void setExamsList(List<Ispit> examsList) {
        this.examsList = examsList;
    }


    public abstract Student najuspjesnijiStudentNaGodini(Integer year);

    @Override
    public String toString() {
        return "ObrazovnaUstanova{" +
                "name='" + name + '\'' +
                ", subjectsList=" + subjectsList +
                ", profesorsList=" + profesorsList +
                ", studentsList=" + studentsList +
                ", examsList=" + examsList +
                '}';
    }
}
