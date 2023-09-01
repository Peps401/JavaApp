package vjezbe.sortiranje;

import vjezbe.entitet.Student;

import java.util.Comparator;

public class StudentSorter implements Comparator<Student> {
    //0 vraca ako su dva stringa ista
    // uvjet ? vrijednost1 : vrijednost2
    @Override
    public int compare(Student o1, Student o2) {
        int comp = o1.getSurname().compareTo(o2.getSurname());
        return comp != 0 ? comp : o1.getName().compareTo(o2.getName());
    }
}
