package vjezbe.niti;

import com.example.totar7.HelloApplication;
import hr.java.vjezbe.baza.BazaPodataka;
import vjezbe.entitet.Ispit;
import vjezbe.entitet.Student;
import vjezbe.iznimke.BazaPodatakaException;

import java.util.List;

public class NajboljiStudentNit implements Runnable {

    @Override
    public void run() {
        try {
            List l= BazaPodataka.dohvatiIspitePremaKriterijima();
            List<Student> ls= BazaPodataka.dohvatiStudentePremaKriterijima();
            HelloApplication.getStage().setTitle(ls.toString());
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }
}
