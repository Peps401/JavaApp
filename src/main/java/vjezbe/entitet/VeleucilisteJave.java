package vjezbe.entitet;

import vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


//is-a
public class VeleucilisteJave extends ObrazovnaUstanova implements Visokoskolska, Serializable {

    /**Kreira objekt veleucilista koji sadrzi ime, polje studenata, profesora, predmeta i ispita
     * on je dijete Obrazovne ustanove
     * implementira interface Visokoskolska
     * @param name
     * @param subjectsList
     * @param profesorsList
     * @param studentsList
     * @param examsList
     */
    public VeleucilisteJave(Long id, Enum<FaksIliVele> typeOfCollege, String name, List<Predmet> subjectsList, List<Profesor> profesorsList, List<Student> studentsList, List<Ispit> examsList) {
        super(id, typeOfCollege, name, subjectsList, profesorsList, studentsList, examsList);
    }

    /**izracunavanje konacne ocjene studija za studenta
     * @param examsList
     * @param ocjenaZavrsnog
     * @param ocjenaObraneZavrsnog
     * @return
     */
    @Override
    public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> examsList, Integer ocjenaZavrsnog, Integer ocjenaObraneZavrsnog) {
        BigDecimal avgGrade = new BigDecimal(0);

        for (int i = 0; i < examsList.size(); i++) {
            Integer grade = getGradeFromEnum(examsList.get(i).getGrade());
            avgGrade = BigDecimal.valueOf(grade).add(avgGrade);
            if (grade == 1){
                BigDecimal finalGrade = BigDecimal.valueOf(1);
                return finalGrade;
            }
        }

        BigDecimal finalGrade = avgGrade.multiply(BigDecimal.valueOf(2)).add(BigDecimal.valueOf(ocjenaZavrsnog)).add(BigDecimal.valueOf(ocjenaObraneZavrsnog)).divide(BigDecimal.valueOf(4));
        return finalGrade;
    }


    /**odreduje se najuspjesniji student prema zadanoj godini
     * @param year
     * @return
     */
    @Override
    public Student najuspjesnijiStudentNaGodini(Integer year) {

        List<Student> studentsList = getStudentsList();
        List <Ispit> examsList = getExamsList();


        int noOf2022Exams = 0;

        for (int i = 0; i < examsList.size(); i++) {
            if(examsList.get(i).getDateNtime().getYear() == year){
                noOf2022Exams++;
            }
        }
        List <Ispit> examsby2022List = new ArrayList<>(noOf2022Exams);

        int counter = 0;
        int position = 0;

        for(int i = 0; i < examsList.size(); ++i) {
            if (examsList.get(i).getDateNtime().getYear() == year) {
                examsby2022List.add(examsList.get(i));
                ++counter;
                position = i;
            }
        }

        Student mostSc = getStudentsList().get(0);

        for (int i = 0; i < getStudentsList().size() - 1; i++) {

            List<Ispit> filteredExamsByStudents = filtrirajIspitePoStudentu(examsby2022List, studentsList.get(i));
            //if (x < y) then it returns a value less than zero
            //if (x > y) then it returns a value greater than zero.
            if (examsby2022List.size() > 1) {
                for (int j = 1; j < getStudentsList().size(); j++) {

                    List<Ispit> filteredExamsByStudentAfter = filtrirajIspitePoStudentu(examsby2022List, studentsList.get(j));

                    try {
                        if (odrediProsjekOcjenaNaIspitima(filteredExamsByStudents).compareTo(odrediProsjekOcjenaNaIspitima(filteredExamsByStudentAfter)) > 0) {
                            mostSc = getStudentsList().get(i);
                        }
                    } catch (NemoguceOdreditiProsjekStudentaException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        if (odrediProsjekOcjenaNaIspitima(filteredExamsByStudents).equals(odrediProsjekOcjenaNaIspitima(filteredExamsByStudentAfter))) {
                            mostSc = getStudentsList().get(j);
                        }
                    } catch (NemoguceOdreditiProsjekStudentaException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            if (examsby2022List.size() == 1) {
                mostSc = studentsList.get(position);
            }

        }

        return mostSc;
    }



}
