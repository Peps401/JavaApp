package util;

import vjezbe.entitet.*;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Datoteke {
    private static List<Dvorana> crList = new ArrayList<>(0);
    private static List<Ispit> examsList = new ArrayList<>(0);
    private static List<ObrazovnaUstanova> ouList = new ArrayList<>(0);
    private static List<Predmet> subjectsList = new ArrayList<>(0);
    private static List<Profesor> profesorsList = new ArrayList<>(0);
    private static List<Student> studentsList = new ArrayList<>(0);

    private static List<ZavrsniRad> zavrsniRadList = new ArrayList<>(0);

    private static List<ObrazovnaUstanova> obrazovneUstanove = new ArrayList<>(0);


    private static Map<Profesor, List<Predmet>> mapa = new HashMap<>(0);

    //private static Logger logger = LoggerFactory.getLogger(Datoteke.class);

    public static List<ZavrsniRad> getZavrsniGrades(){
        try {
            List<String> lines = Files.readAllLines(Path.of("./dat/ObranaRadaOcjene"));
            for (int i = 0; i < lines.size(); i = i+3) {
                Long id = Long.valueOf(lines.get(i));
                Long studentId = Long.valueOf(lines.get(i));
                Integer gradeObrana = Integer.valueOf(lines.get(i + 1));
                Integer gradeZavrsni = Integer.valueOf(lines.get(i + 2));

                ZavrsniRad z = new ZavrsniRad(id, getStudentFromId(studentId), gradeObrana, gradeZavrsni);
                zavrsniRadList.add(z);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return zavrsniRadList;
    }

    public static void getAGrades(){
        List<Ispit> exams = getExamsFromFile();
        for (Ispit e:exams) {
            if(e.getGrade().equals(Ocjena.IZVRSTAN)){
                System.out.println("Student " + e.getStudent() +" je dobio ocjenu 'izvrstan' na predmetu " + e.getPredmet().getName());
            }
        }
    }

    public static void showStudentsPerSubj(){
        List<Predmet> subjects = getSubjectsFromFile();
        for (Predmet p:subjects) {
            List<Student> ls = p.getStudents().stream().toList();
            if(ls.isEmpty()){
                System.out.println("Nema studenata upisanih na predmetu " + p.getName() );
            }
            else {
                System.out.println("Studenti upisani na predmetu " + p.getName());

                for (int i = 0; i < ls.size(); i++) {
                    System.out.println(ls.get(i).toString());
                }
            }
        }
    }

    public static void getMapProfAndSubj(){
        List<Profesor> profesors = getProfesorsFromFile();
        List<Predmet> subjects = getSubjectsFromFile();
        for (Profesor p:profesors) {
            List<Predmet> subjectslistForMap = new ArrayList<>();
            for (Predmet s:subjects) {
                if(p.equals(s.getPerson())){
                    subjectslistForMap.add(s);
                }
            }
            mapa.put(p, subjectslistForMap);
        }

    }
    public static void showMapOfProfAndSubj(){
        Integer br = 0;
        for (Profesor p: mapa.keySet()) {
            System.out.println(p.getTitle() + " " + p.getName() + " " + p.getSurname() + " predaje sljedeće predmete:");
            for (List<Predmet> subjectsListFromMap: mapa.values()){
                for (Predmet s:subjectsListFromMap) {
                    if(p.equals(s.getPerson())) {
                        System.out.println((br + 1) + ") " + s.getName());
                        br++;
                    }
                }
            }
        }
    }

    public static List<Profesor> getProfesorsFromFile(){
        profesorsList.clear();
        try {
            List<String> linesOfProf = Files.readAllLines(Path.of("./dat/Profesori"));
            for (int i = 0; i < linesOfProf.size(); i = i+5) {
                Long id = Long.valueOf(linesOfProf.get(i));
                String code = linesOfProf.get(i+1);
                String name = linesOfProf.get(i+2);
                String surname = linesOfProf.get(i+3);
                String title = linesOfProf.get(i+4);

                //Profesor p = new Profesor(id, code, name, surname, title);

                Profesor p = new Profesor.Builder().withId(id).withCode(code).withName(name).withsurName(surname).withTitle(title).build();

                profesorsList.add(p);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return profesorsList;
    }

    public static void writeNewProfesorToFile(Profesor profesor) throws IOException {

        List<String> lines = Files.readAllLines(Path.of("./dat/Profesori"));
        PrintWriter pw = new PrintWriter("./dat/Profesori");

        lines.add(String.valueOf(profesor.getId()));
        lines.add(profesor.getCode());
        lines.add(profesor.getName());
        lines.add(profesor.getSurname());
        lines.add(profesor.getTitle());

        for (int i = 0; i < lines.size(); i++) {
            pw.write(lines.get(i));
            if(i!=lines.size()-1) {
                pw.write("\n");
            }
        }
        pw.close();
    }

    public static Set<Profesor> getSetFromStringOfProf(String profsId){
        Set<Profesor> set = new HashSet<>();
        List<Profesor> profesors = getProfesorsFromFile();
        for (int i = 0; i < profsId.length(); i++) {
            for (Profesor p: profesors) {
                Long id = Long.valueOf(profsId.charAt(i));
                if(p.getId().equals(id)){
                    set.add(p);
                }
            }
        }

        return set;
    }
    public static Set<Student> getSetFromStringOfStudents(String studentsId){
        Set<Student> set = new HashSet<>();
        List<Student> students = getStudentsFromFile();

        for (int i = 0; i < studentsId.length(); i++) {

            char charId = studentsId.charAt(i);
            String a = String.valueOf(charId);
            Integer idd = charId - '0';
            Long longId = Long.valueOf(idd);

            for (Student s:students) {
                if(s.getId().equals(longId)){
                    set.add(s);
                    break;
                }
            }
        }

        return set;
    }

    public static Profesor getProfFromId(Long id){
        List<Profesor> profesors = getProfesorsFromFile();
        Profesor prof = profesors.get(0);
        for (Profesor p:profesors) {
            if(p.getId().equals(id)){
                prof = p;
                break;
            }
        }
        return prof;
    }

    public static Ispit getExamFromId(Long id){
        List<Ispit> exams = getExamsFromFile();
        Ispit exam = exams.get(0);
        for (Ispit e:exams) {
            if(e.getId().equals(id)){
                exam = e;
                break;
            }
        }
        return exam;
    }
    public static List<Predmet> getSubjectsFromFile(){
        subjectsList.clear();
        try {
            List<String> linesOfSubjects = Files.readAllLines(Path.of("./dat/Predmeti"));
            for (int i = 0; i < linesOfSubjects.size(); i = i+6) {
                Long id = Long.valueOf(linesOfSubjects.get(i));
                String code = linesOfSubjects.get(i+1);
                String name = linesOfSubjects.get(i+2);
                Integer ects = Integer.valueOf(linesOfSubjects.get(i+3));

                Long profId = Long.valueOf(linesOfSubjects.get(i+4));
                Profesor p = getProfFromId(profId);

                String stringOfStudents = linesOfSubjects.get(i+5);
                stringOfStudents = stringOfStudents.replaceAll("\\s", "");

                Set<Student> set = new HashSet<>(0);
                if (!stringOfStudents.matches("[1-9]+")){
                    break;
                    //logger.info("Na predmetu " + name + " nema studenata");
                }
                else {
                    stringOfStudents = stringOfStudents.replaceAll("\\s", "");
                    Integer g = stringOfStudents.length();

                    set = getSetFromStringOfStudents(stringOfStudents);
                }

                Predmet s = new Predmet(id, code, name, ects,p, set);
                subjectsList.add(s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return subjectsList;
    }

    public static void writeNewSubjectToFile(Predmet subject) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("./dat/Predmeti"));
        PrintWriter pw = new PrintWriter("./dat/Predmeti");

        lines.add(String.valueOf(subject.getId()));
        lines.add(subject.getCode());
        lines.add(subject.getName());
        lines.add(String.valueOf(subject.getNoOfEcts()));
        lines.add(String.valueOf(subject.getPerson().getId()));

        List l = subject.getStudents().stream().map(student -> student.getId()).toList();
        String set = "";
        for (int i = 0; i < l.size(); i++) {
            set = set + l.get(i) + " ";
        }
        if(set.equals("")){
            set ="/";
        }
        lines.add(set);
        for (int i = 0; i < lines.size(); i++) {
            pw.write(lines.get(i));
            if(i != lines.size()-1){
                pw.write("\n");
            }
        }
        pw.close();
    }

    public static List<Student> getStudentsFromFile(){
        studentsList.clear();
        try {
            List<String> linesOfStudents = Files.readAllLines(Path.of("./dat/Studenti"));
            for (int i = 0; i < linesOfStudents.size(); i = i+5) {
                Long id = Long.valueOf(linesOfStudents.get(i));
                String name = linesOfStudents.get(i+1);
                String surname = linesOfStudents.get(i+2);
                String jmbag = linesOfStudents.get(i+3);
                String date = linesOfStudents.get(i+4);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                LocalDate ld = LocalDate.parse(date, dtf);

                Student s = new Student(id, name, surname, jmbag, ld);
                studentsList.add(s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return studentsList;
    }

    public static void writeNewStudentToFile(Student student) {

        try {
            List<String> lines = Files.readAllLines(Path.of("./dat/Studenti"));
            PrintWriter pw = new PrintWriter("./dat/Studenti");

            lines.add(String.valueOf(student.getId()));
            lines.add(student.getName());
            lines.add(student.getSurname());
            lines.add(student.getJmbag());

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

            String date = student.getDateOfBirth().format(dtf);
            lines.add(date);

            for (int i = 0; i < lines.size(); i++) {
                pw.write(lines.get(i));
                if(i!=lines.size()-1) {
                    pw.write("\n");
                }
            }
            pw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }


    public static List<Dvorana> getRoomsFromFile(){
        try {
            List<String> linesOfRooms = Files.readAllLines(Path.of("./dat/Dvorane"));
            for (int i = 0; i < linesOfRooms.size(); i = i+3) {
                Long id = Long.valueOf(linesOfRooms.get(0));
                String name = linesOfRooms.get(1);
                String classroom = linesOfRooms.get(2);

                Dvorana dv = new Dvorana(id, name, classroom);
                crList.add(dv);
            }

        }catch (IOException e){
            //logger.error("Neuspjesno procitana datoteka Dvorane", e);
        }

        return crList;
    }
    public static Predmet getSubjectfromId(Long id){
        List<Predmet> subjects = getSubjectsFromFile();
        Predmet subject = subjects.get(0);
        for (Predmet s:subjects) {
            if(s.getId().equals(id)){
                subject = s;
                break;
            }
        }
        return subject;
    }
    public static Student getStudentFromId(Long id){
        List<Student> students = getStudentsFromFile();
        Student student = students.get(0);

        for (Student s: students) {
            if(s.getId().equals(id)){
                student = s;
                break;
            }
        }
        return student;
    }

    public static Enum<Ocjena> getEnumGrade(Integer grade){
        Enum<Ocjena> enumGrade = Ocjena.NEDOVOLJAN;
        switch (grade){
            case 2 -> enumGrade = Ocjena.DOVOLJAN;
            case 3 -> enumGrade = Ocjena.DOBAR;
            case 4 -> enumGrade = Ocjena.VRLO_DOBAR;
            case 5 -> enumGrade = Ocjena.IZVRSTAN;
        }

        return enumGrade;
    }

    public static Dvorana getClassroomFromId(Long id){
        List<Dvorana> crs = getRoomsFromFile();
        Dvorana cr = crs.get(0);
        for (Dvorana d:crs) {
            if(d.id().equals(id)){
                cr = d;
            }
        }
        return cr;
    }

    public static List<Ispit> getExamsFromFile() {
        try {
            List<String> linesOfExams = Files.readAllLines(Path.of("./dat/Ispiti"));
            for (int i = 0; i < linesOfExams.size(); i = i+6) {
                Long id = Long.valueOf(linesOfExams.get(i));

                Long idSubject = Long.valueOf(linesOfExams.get(i + 1));
                Predmet subject = getSubjectfromId(idSubject);

                Long idStudent = Long.valueOf(linesOfExams.get(i + 2));
                Student student = getStudentFromId(idStudent);

                Integer grade = Integer.valueOf(linesOfExams.get(i + 3));
                Enum<Ocjena> gradeEnum = getEnumGrade(grade);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm");
                String date = linesOfExams.get(i + 4);
                LocalDateTime parsedDate = LocalDateTime.parse(date, dtf);

                Long idCr = Long.valueOf(linesOfExams.get(i + 5));
                Dvorana cr = getClassroomFromId(idCr);

                Ispit exam = new Ispit(id, subject, student, gradeEnum, parsedDate, cr);
                examsList.add(exam);
            }
        } catch (IOException e) {
            //logger.error("Pogreska pri citanju Ispita", e);
        }

        return examsList;
    }

    public static void getDataFromOu(){

        for (ObrazovnaUstanova o : obrazovneUstanove) {
            System.out.println("Ispisujemo podatke za " + o.getName());
            for (int j = 0; j < o.getStudentsList().size(); j++) {
                Student s = o.getStudentsList().get(j);
                System.out.println("Student " + s.toString());
                if (o.getTypeOfCollege().equals(FaksIliVele.VELE)) {
                    List<Ispit> examsForXStudent = ((VeleucilisteJave) (o)).filtrirajIspitePoStudentu(o.getExamsList(), s);
                    for (ZavrsniRad z : zavrsniRadList) {
                        if (z.getStudent().equals(s)) {
                            BigDecimal finalGrade = ((VeleucilisteJave) (o)).izracunajKonacnuOcjenuStudijaZaStudenta(examsForXStudent, z.getZavrsni(), z.getObrana());
                            System.out.println("Konacna ocjena " + finalGrade);
                        }
                    }
                }
                else {
                    List<Ispit> examsForXStudent = ((FakultetRacunarstva) (o)).filtrirajIspitePoStudentu(o.getExamsList(), s);
                    for (ZavrsniRad z : zavrsniRadList) {
                        if (z.getStudent().equals(s)) {
                            BigDecimal finalGrade = ((FakultetRacunarstva) (o)).izracunajKonacnuOcjenuStudijaZaStudenta(examsForXStudent, z.getZavrsni(), z.getObrana());
                            System.out.println("Konacna ocjena " + finalGrade);

                        }
                    }
                }
            }
            if(o.getTypeOfCollege().equals(FaksIliVele.FAKS)) {
                Student rektN = ((FakultetRacunarstva) (o)).odrediStudentaZaRektorovuNagradu();
                System.out.println("Student rektorove nagrade je; " + rektN.getName() + " " + rektN.getSurname());
            }
        }

        showMapOfProfAndSubj();
        showStudentsPerSubj();



        getAGrades();

        System.out.println();
    }

    public static List<ObrazovnaUstanova> getOuList(){
        try {
            List<String> linesOu = Files.readAllLines(Path.of("./dat/Obrazovne Ustanove"));
            for (int i = 0; i < linesOu.size(); i = i + 7) {
                Long id = Long.valueOf(linesOu.get(i));
                String enumFV = linesOu.get(i + 1);
                String name = linesOu.get(i + 2);

                String subjects = linesOu.get(i + 3);
                subjects = subjects.replaceAll("\\s","");
                List<Predmet> subjectsListForInstitution = new ArrayList<>();
                for (int j = 0; j < subjects.length(); j++) {
                    char c = subjects.charAt(j);
                    int no = c - '0';
                    Long idS = Long.valueOf(no);
                    subjectsListForInstitution.add(getSubjectfromId(idS));
                }

                String profesors = linesOu.get(i + 4);
                profesors = profesors.replaceAll("\\s", "");
                List<Profesor> profesorsListForInstitution = new ArrayList<>();
                for (int j = 0; j < profesors.length(); j++) {
                    char c = profesors.charAt(j);
                    int no = c - '0';
                    Long idP = Long.valueOf(no);
                    profesorsListForInstitution.add(getProfFromId(idP));
                }

                String students = linesOu.get(i + 5);
                students = students.replaceAll("\\s", "");
                List<Student> studentsListForInstitution = new ArrayList<>();
                for (int j = 0; j < students.length(); j++) {
                    char c = students.charAt(j);
                    int no = c - '0';
                    Long idS = Long.valueOf(no);
                    studentsListForInstitution.add(getStudentFromId(idS));
                }

                String exams = linesOu.get(i + 6);
                exams = exams.replaceAll("\\s", "");
                List<Ispit> examsListForInstitution = new ArrayList<>();
                for (int j = 0; j < exams.length(); j++) {
                    char c = exams.charAt(j);
                    int no = c - '0';
                    Long idE = Long.valueOf(no);
                    examsListForInstitution.add(getExamFromId(idE));
                }

                ObrazovnaUstanova ou = null;
                switch (enumFV){
                    case "1" -> ou = new FakultetRacunarstva(id, FaksIliVele.FAKS,  name, subjectsListForInstitution, profesorsListForInstitution, studentsListForInstitution, examsListForInstitution);
                    case "2" -> ou = new VeleucilisteJave(id, FaksIliVele.VELE,  name, subjectsListForInstitution, profesorsListForInstitution, studentsListForInstitution, examsListForInstitution);
                }

                obrazovneUstanove.add(ou);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return obrazovneUstanove;
    }

}
