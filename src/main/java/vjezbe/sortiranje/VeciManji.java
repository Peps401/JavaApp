package vjezbe.sortiranje;

import vjezbe.entitet.ObrazovnaUstanova;

import java.util.Comparator;

public class VeciManji implements Comparator<ObrazovnaUstanova> {
    @Override
    public int compare(ObrazovnaUstanova o1, ObrazovnaUstanova o2) {
        if(o1.getNoOfStudents() < o2.getNoOfStudents()){
            return 1;
        }

        return 0;
    }
}
