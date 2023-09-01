package vjezbe.entitet;

import vjezbe.iznimke.NemoguceOdreditiProsjekStudentaException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public interface Visokoskolska {

    public BigDecimal izracunajKonacnuOcjenuStudijaZaStudenta(List<Ispit> examsArray, Integer grade, Integer secondGrade);

    public default Integer getGradeFromEnum(Enum<Ocjena> grade){
        Integer gradeInteger = 5;

        if (Ocjena.NEDOVOLJAN.equals(grade)) {
            gradeInteger = 1;
        } else if (Ocjena.DOVOLJAN.equals(grade)) {
            gradeInteger = 2;
        } else if (Ocjena.DOBAR.equals(grade)) {
            gradeInteger = 3;
        } else if (Ocjena.VRLO_DOBAR.equals(grade)) {
            gradeInteger = 4;
        }

        return gradeInteger;
    }

    /**odreduje prosjek ocjena na ispitima
     * @param examsList
     * @return
     * @throws NemoguceOdreditiProsjekStudentaException
     */
      public default BigDecimal odrediProsjekOcjenaNaIspitima(List<Ispit> examsList) throws NemoguceOdreditiProsjekStudentaException {

        BigDecimal avgGrade = new BigDecimal(0);

        for (int i = 0; i < examsList.size(); i++) {
            Integer grade = getGradeFromEnum(examsList.get(i).getGrade());
            avgGrade = BigDecimal.valueOf(grade).add(avgGrade);

            if(grade == 1){
                System.out.println("Student " + examsList.get(i).getStudent().getSurname() + " ima negativnu ocjenu");
                throw new NemoguceOdreditiProsjekStudentaException("Student " + examsList.get(i).getStudent().getSurname() + " ima negativnu ocjenu");
            }
        }
        avgGrade = avgGrade.divide(BigDecimal.valueOf(examsList.size()));
        return avgGrade;
    }

    private List<Ispit> filtrirajPolozeneIspite(List<Ispit> examsList){
          List<Ispit> filteredExamsList = new ArrayList<>(0);
          for (int i = 0; i < examsList.size(); i++) {
              Integer grade = getGradeFromEnum(examsList.get(i).getGrade());
              if(grade > 1){
                  filteredExamsList.add(examsList.get(i));
              }
          }
          return filteredExamsList;
      }

    /**u posebno polje sprema ispite od X studenta
     * @param examsList
     * @param student
     * @return
     */
    default List<Ispit> filtrirajIspitePoStudentu(List<Ispit> examsList, Student student){
        //Instant start = Instant.now();
        List<Ispit> examsDoneByXStudent;
        List<Ispit> examsDoneByXStudentStream = new ArrayList<>();

        int no_exams = 0 ;
        for (int i = 0; i < examsList.size(); i++) {
            if(student.equals(examsList.get(i).getStudent())){
                no_exams++;
            }
        }

        examsDoneByXStudent = new ArrayList<>(no_exams);
        int position = 0;
        for (int i = 0; i < examsList.size(); i++) {
            if(student.equals(examsList.get(i).getStudent())){
                examsDoneByXStudent.add(examsList.get(i));
                position++;
            }
        }
        //Instant end = Instant.now();

        //start = Instant.now();
        examsDoneByXStudentStream = examsList.stream().filter(ispit -> ispit.getStudent().equals(student))
                                                                             .collect(Collectors.toList());
        //end = Instant.now();
        return examsDoneByXStudent;
    }
}
