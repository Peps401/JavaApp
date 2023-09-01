package vjezbe.niti;

import com.example.totar7.HelloApplication;
import com.example.totar7.MeniBar;
import hr.java.vjezbe.baza.BazaPodataka;
import vjezbe.entitet.Student;
import vjezbe.iznimke.BazaPodatakaException;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatumRodjenjaNit implements Runnable {
    public static List<Student> bdayStudentsList;

    public static List<Student> getBdayStudentsList() {
        return bdayStudentsList;
    }

    public static void setBdayStudentsList(List<Student> bdayStudentsList) {
        DatumRodjenjaNit.bdayStudentsList = bdayStudentsList;
    }

    @Override
    public void run() {
        List<Student> bdayStudentsList = null;
        try {
            bdayStudentsList = BazaPodataka.bdayStudents();
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

        System.out.println(bdayStudentsList);

        //HelloApplication.getStage().setTitle(bdayStudentsList.toString());

    }
}
