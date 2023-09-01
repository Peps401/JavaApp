package vjezbe.entitet;

import vjezbe.iznimke.PostojiViseNajmladjihStudenataException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

public class FakultetRacunarstva extends ObrazovnaUstanova implements Diplomski, Serializable {

    /**Kreira objekt Fakulteta koji sadrzi ime, polje studenata, profesora, predmeta i ispita
     * @param name
     * @param subjectsList
     * @param profesorsList
     * @param studentsList
     * @param examsList
     */
    public FakultetRacunarstva(Long id, Enum<FaksIliVele> typeOfCollege, String name, List<Predmet> subjectsList, List<Profesor> profesorsList, List<Student> studentsList, List<Ispit> examsList) {
        super(id, typeOfCollege, name, subjectsList, profesorsList, studentsList, examsList);
    }


    /**odreduje se najuspjesnijii student, ako postoje vise takvih osoba uzima se mladi
     * @return
     */
    @Override
    public Student odrediStudentaZaRektorovuNagradu() {
        List<Student> studentsList = getStudentsList();
        List <Ispit> examsList = getExamsList();

        BigDecimal prosjekOcjenaArray[] = new BigDecimal[studentsList.size()];

        BigDecimal avg = new BigDecimal(0);
        BigDecimal divider = new BigDecimal(0   );
        BigDecimal bestAvg = BigDecimal.valueOf(0);

        for (int i = 0; i < studentsList.size(); i++) {
            boolean flag = false;

            for (int j = 0; j < examsList.size(); j++) {
                if(studentsList.get(i).equals(examsList.get(j).getStudent())){
                    Integer grade = getGradeFromEnum(examsList.get(j).getGrade());
                    avg = BigDecimal.valueOf(grade).add(avg);
                    divider = divider.add(BigDecimal.valueOf(1));
                    flag = true;
                }
            }

            if (!flag) {
                divider = BigDecimal.valueOf(1);
            }

            //Set precision at 5
            MathContext mc = new MathContext(5);
            avg = avg.divide(divider, mc);

            prosjekOcjenaArray[i] = avg;

            if (avg.compareTo(bestAvg) > 0) {
                bestAvg = avg;
            }
        }

        int sameGrade = 0;
        Student youngestStudent = studentsList.get(0);

        int sameYear = 0;
        Student[] sameYearStudents = new Student[0];
        for (int i = 0; i < prosjekOcjenaArray.length - 1; i++) {
            if(prosjekOcjenaArray[i].equals(bestAvg)) {
                for (int j = 1; j < prosjekOcjenaArray.length; j++) {
                    if (prosjekOcjenaArray[i].equals(prosjekOcjenaArray[j])) {
                        sameGrade++;
                        if (studentsList.get(i).getDateOfBirth().isBefore(studentsList.get(j).getDateOfBirth())) {
                            youngestStudent = studentsList.get(j);
                        }
                    }
                }
            }
        }

        if (sameYear > 1) throw new PostojiViseNajmladjihStudenataException("Postoji vise studenata s istom godinom");
        return youngestStudent;
    }

    /**odreduje najuspjesnijeg studenta na godini
     * @param year
     * @return
     */
    @Override
    public Student najuspjesnijiStudentNaGodini(Integer year) {

        int highestA = 0;
        int position = 0;

        List<Student> studentsList = getStudentsList();
        List <Ispit> examsList = getExamsList();



        for (int i = 0; i < studentsList.size(); i++) {
            int brojac = 0;
                for (int j = 0; j < examsList.size(); j++) {
                    int yy = examsList.get(j).getDateNtime().getYear();
                    if(yy == year) {
                        Integer grade = getGradeFromEnum(examsList.get(j).getGrade());
                        if (studentsList.get(i).equals(examsList.get(j).getStudent()) && grade == 5) {
                            brojac++;
                        }
                    }
                }

                if (highestA < brojac) {
                    highestA = brojac;
                    position = i;
                }
            }

        return studentsList.get(position);
    }

    /**racunanje konacne ocjene studija za studenta
     * @param examsList
     * @param ocjenaDiplomskog
     * @param ocjenaObraneDiplomskog
     * @return
     */
    @Override
    public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> examsList, Integer ocjenaDiplomskog, Integer ocjenaObraneDiplomskog) {
        BigDecimal avgGrade = new BigDecimal(0);
        for (int i = 0; i < examsList.size(); i++) {
            Integer grade = getGradeFromEnum(examsList.get(i).getGrade());
            avgGrade = BigDecimal.valueOf(grade).add(avgGrade);
        }

        BigDecimal finalGrade = avgGrade.multiply(BigDecimal.valueOf(3)).add(BigDecimal.valueOf(ocjenaDiplomskog)).add(BigDecimal.valueOf(ocjenaObraneDiplomskog)).divide(BigDecimal.valueOf(5));
        return finalGrade;

    }

    /**odreduje se najmladi student
     * @param studentsList
     * @throws PostojiViseNajmladjihStudenataException
     */
    public void najmladiStudent(List<Student> studentsList) throws PostojiViseNajmladjihStudenataException {
        int sameYear = 0;
        Student[] sameYearStudents = new Student[0];
        Year year = Year.of(10);
        for (int i = 0; i < studentsList.size(); i++) {
            if(year.isBefore(Year.of(studentsList.get(i).getDateOfBirth().getYear()))){
                year = Year.of(studentsList.get(i).getDateOfBirth().getYear());
            }
        }
        for (int i = 0; i < studentsList.size(); i++) {
            Year bornYear = Year.of(studentsList.get(i).getDateOfBirth().getYear());
            if(bornYear.equals(year)){
                sameYear++;
                sameYearStudents = Arrays.copyOf(sameYearStudents, sameYear);
                sameYearStudents[sameYear-1] = studentsList.get(i);
            }
        }

        if(sameYear > 0){
            System.out.println("Pronađeno je više najmlađih studenata s istim datumom rođenja, a to su: ");
            for (int i = 0; i < sameYearStudents.length; i++) {
                System.out.println(sameYearStudents[i]);
            }
            throw new PostojiViseNajmladjihStudenataException("PRONASLI ");
        }

    }


//    @Override
//    public Ispit[] filtrirajIspitePoStudentu(Ispit[] examsArray, Student student) {
//        return Diplomski.super.filtrirajIspitePoStudentu(examsArray, student);
//    }
//ne treba jer se nalazi u visokoskolksom

}
