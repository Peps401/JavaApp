package hr.java.vjezbe.baza;
import util.Datoteke;
import vjezbe.entitet.*;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BazaPodataka {
    private static final Logger logger = LoggerFactory.getLogger(BazaPodataka.class);

    private static Connection connectToDatabase() throws BazaPodatakaException {
        Connection connection;
        try {
        Properties properties = new Properties();
        properties.load(new FileReader("dat/petraDatabase.properties"));

        String databaseUrl = properties.getProperty("databaseUrl");
        String username = properties.getProperty("databaseUsername");
        String password = properties.getProperty("databasePassword");

        connection = DriverManager.getConnection(databaseUrl, username, password);
        }catch (IOException | SQLException e){
            throw new BazaPodatakaException(e);
        }
        return connection;
    }

    private static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Iz h2 baze podataka dohvaca profesore
     *
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public static List<Profesor> getProfesorsFromDB() throws BazaPodatakaException {
        List<Profesor> profesorsList = new ArrayList<>(0);
        try {

            Connection connection = connectToDatabase();

            Statement statement = connection.createStatement();

            String query = "SELECT * FROM PROFESOR";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String name = rs.getString("IME");
                String surname = rs.getString("PREZIME");
                String code = rs.getString("SIFRA");
                String title = rs.getString("TITULA");

                Profesor profesor = new Profesor(id, code, name, surname, title);
                profesorsList.add(profesor);
            }

            connection.close();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return profesorsList;
    }

    public static List<Profesor> getSearchedProffesor(Profesor profesor) throws
            BazaPodatakaException {

        List<Profesor> listaProfesora = new ArrayList<>();
        try ( Connection veza = connectToDatabase()) {
            StringBuilder sqlUpit = new StringBuilder(
                    "SELECT * FROM PROFESOR WHERE 1 = 1");
            if (Optional.ofNullable(profesor).isEmpty() == false) {
                if (Optional.ofNullable(profesor).map(
                        Profesor::getId).isPresent()) {
                    sqlUpit.append(" AND ID = " + profesor.getId());
                }
                if (Optional.ofNullable(profesor.getCode()).map(
                        String::isBlank).orElse(true) == false) {
                    sqlUpit.append(" AND SIFRA LIKE '%" +
                            profesor.getCode() + "%'");
                }
                if (Optional.ofNullable(profesor.getName()).map(
                        String::isBlank).orElse(true) == false) {
                    sqlUpit.append(" AND IME LIKE '%" +
                            profesor.getName() + "%'");
                }
                if (Optional.ofNullable(profesor.getSurname()).map(
                        String::isBlank).orElse(true) == false) {
                    sqlUpit.append(" AND PREZIME LIKE '%" +
                            profesor.getSurname() + "%'");
                }
                if (Optional.ofNullable(profesor.getTitle()).map(String::isBlank).orElse(true) == false) {
                    sqlUpit.append(" AND TITULA LIKE '%" +
                            profesor.getTitle() + "%'");
                }
            }
            Statement upit = veza.createStatement();
            ResultSet resultSet = upit.executeQuery(sqlUpit.toString());
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String sifra = resultSet.getString("sifra");
                String ime = resultSet.getString("ime");
                String prezime = resultSet.getString("prezime");
                String titula = resultSet.getString("titula");
                Profesor noviProfesor = new Profesor(id, sifra, ime, prezime,
                        titula);
                listaProfesora.add(noviProfesor);
            }
        } catch (SQLException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }

        return listaProfesora;
    }

    public static void insertNewProfToDB(Profesor profesor) throws BazaPodatakaException {
        try (Connection veza = connectToDatabase()) {
            PreparedStatement preparedStatement = veza
                    .prepareStatement(
                            "INSERT INTO PROFESOR(ime, prezime, sifra, titula) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, profesor.getName());
            preparedStatement.setString(2, profesor.getSurname());
            preparedStatement.setString(3, profesor.getCode());
            preparedStatement.setString(4, profesor.getTitle());
            preparedStatement.executeUpdate();
            veza.close();
        } catch (SQLException ex) {
            String poruka = "Došlo je do pogreške u radu s bazom podataka";
            logger.error(poruka, ex);
            throw new BazaPodatakaException(poruka, ex);
        }
    }

    public static Profesor getProfesorFromId_DB(Long id){
        Profesor prof;
        try {
            prof = getProfesorsFromDB().stream().filter(profesor -> profesor.getId().equals(id)).collect(toSingleton());
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

        return prof;
    }

    public static List<Student> getStudentsFromDB() throws BazaPodatakaException {
        List<Student> studentsList = new ArrayList<>(0);

        try {
            Connection connection = connectToDatabase();

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM STUDENT";
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Long id = rs.getLong("ID");
                String name = rs.getString("IME");
                String surname = rs.getString("PREZIME");
                String jmbag = rs.getString("JMBAG");
                Date date = rs.getDate("DATUM_RODJENJA");
                Student student = new Student(id, name, surname, jmbag, date.toLocalDate());
                studentsList.add(student);
            }
           connection.close();
        }catch (SQLException e){
            logger.info("Pogreska kod povezivanja s bazom kod getStudentsFromDB()", e);
            throw new BazaPodatakaException(e);
        }
        return studentsList;
    }

    public static void insertNewStudentnToDB(Student student) throws BazaPodatakaException {
        try {
            Connection connection = connectToDatabase();

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO STUDENT (IME, PREZIME, JMBAG, DATUM_RODJENJA) VALUES (?, ?, ?, ?)");

            ps.setString(1, student.getName());
            ps.setString(2, student.getSurname());
            ps.setString(3, student.getJmbag());
            ps.setString(4, String.valueOf(student.getDateOfBirth()));

            ps.executeUpdate();

            connection.close();
        }catch (SQLException e){
            logger.info("Pogreska pri spajanju na bazu");
            throw new BazaPodatakaException(e);
        }
    }

    public static void deleteStudent(Student student) throws Exception{
        try(Connection connection = connectToDatabase()){
            StringBuilder sql = new StringBuilder("DELETE FROM STUDENT WHERE id = ");
            sql.append(student.getId());
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql.toString());
        }
    }


    public static void editStudent(Student selected, String ime, String prezime, String jmbag, LocalDate datum) throws BazaPodatakaException {
        try {
            Connection connection = connectToDatabase();
            boolean flag = false;

            String query = "UPDATE STUDENT SET";
            if(!selected.getName().equals(ime)){
                query = query + " IME = '" + ime + "'";
                flag = true;
            }
            else flag = false;


            if (!selected.getSurname().equals(prezime)){
                if(flag) query = query + ",";
                query = query + " PREZIME = '" + prezime + "'";
                flag = true;
            }
            else flag = false;

            if (!selected.getJmbag().equals(jmbag)){
                if(flag) query = query + ",";
                query = query + " JMBAG = '" + jmbag + "'";
                flag = true;
            }
            else flag = false;

            if (!selected.getDateOfBirth().equals(datum)){
                if(flag) query = query + ",";
                query = query + " DATUM_RODJENJA = '" + datum.toString() + "'";
                flag = true;
            }
            else flag = false;

            query = query + " WHERE ID =" + selected.getId() + ';';
            PreparedStatement updateStudent = connection.prepareStatement(query);
            updateStudent.executeUpdate();

            connection.close();
        } catch (SQLException e) {
            throw new BazaPodatakaException(e);
        }

    }

    public static List<Student> bdayStudents() throws BazaPodatakaException {

        List<Student> bdayStudents = new ArrayList<>(0);

        for (Student s:
             getStudentsFromDB()) {
            if(s.getDateOfBirth().equals(LocalDate.now())){
                bdayStudents.add(s);
            }
        }
        return bdayStudents;
    }

    /**Zelimo vratiti samo jedan objekt iz stream liste
     * @param <T>
     * @return
     */
    public static <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1) {
                        logger.info("Nasao vise objekata student s istim id");
                        throw new IllegalStateException();
                    }
                    return list.get(0);
                }
        );
    }

    /**Pomocu streama prolazi kroz listu studenata i nalazi jednog studenta s tim id-em
     * @param studentId
     * @return
     */
    public static Student getStudentFromId_DB(Long studentId){

        Student student;

        try {
            student = getStudentsFromDB().stream().filter(student1 -> student1.getId().equals(studentId)).collect(toSingleton());
        } catch (BazaPodatakaException e) {
            logger.info("Pogreska kod getStudentsFromId-Db", e);
            throw new RuntimeException(e);
        }
        return student;
    }
    public static Set<Student> getSetStudentsFromId_DB(Long subjectId) throws BazaPodatakaException{
        Set<Student> set = new HashSet<>();
        Connection connection;
        try{
            connection = connectToDatabase();

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM PREDMET_STUDENT ";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                Long id = rs.getLong("PREDMET_ID");
                Long studentId = rs.getLong("STUDENT_ID");
                if(id.equals(subjectId)){
                    Student student = getStudentFromId_DB(studentId);
                    set.add(student);
                }
            }

            connection.close();
        }catch (SQLException e) {
            String msg = "Dogodila se pogreska pri spajanju na bazu podataka";
            logger.info(msg);
            throw new BazaPodatakaException(e);
        }
        closeConnection(connection);
        return set;
    }

    public static List<Predmet> getSubjectsFromDB() throws BazaPodatakaException {
        List<Predmet> subjectsList = new ArrayList<>(0);;

        try {
            Connection connection = connectToDatabase();

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM PREDMET";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                Long id = rs.getLong("ID");
                String code = rs.getString("SIFRA");
                String name = rs.getString("NAZIV");
                Integer ects = rs.getInt("BROJ_ECTS_BODOVA");
                Long profId = rs.getLong("PROFESOR_ID");
                Set<Student> set = getSetStudentsFromId_DB(id);
                Profesor prof = getProfesorFromId_DB(profId);

                Predmet subject = new Predmet(id, code, name, ects, prof, set);
                subjectsList.add(subject);
            }

            connection.close();

        } catch (SQLException e) {
            String msg = "Dogodila se pogreska pri spajanju na bazu podataka";
            logger.info(msg);
            throw new BazaPodatakaException(e);
        }

        return subjectsList;
    }

    public static void insertNewSubjectToDB(Predmet p) throws BazaPodatakaException {
        try{
            Connection connection = connectToDatabase();

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO PREDMET(SIFRA, NAZIV, BROJ_ECTS_BODOVA, PROFESOR_ID) VALUES (?, ?, ?, ?)");
            ps.setString(1, p.getCode());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getNoOfEcts());
            ps.setLong(4, p.getPerson().getId());

            List<Student> list = new ArrayList<>(p.getStudents());

            ps.executeUpdate();

            for (int i = 0; i < list.size(); i++) {
                insertNewPredmetStudent(p, list.get(i).getId());
            }

            connection.close();

        }catch (SQLException e) {
            logger.info("Pogreska pri spajanju na bazu kod predmeta", e);
            throw new BazaPodatakaException(e);
        }
    }

    public static void insertNewPredmetStudent(Predmet p, Long studentId) throws BazaPodatakaException {

        try {
            Connection con = connectToDatabase();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO PREDMET_STUDENT(PREDMET_ID, STUDENT_ID) VALUES (?, ?)");
            ps.setLong(1, p.getId());
            ps.setLong(2, studentId);
            ps.executeUpdate();
            con.close();
        } catch (SQLException e) {
            throw new BazaPodatakaException(e);
        }

    }

    public static List<Ispit> getExamsFromDB() throws BazaPodatakaException {
        List<Ispit> examsList = new ArrayList<>(0);
        try {
        List<Predmet> subjectsList = getSubjectsFromDB();
        List<Student> studentList = getStudentsFromDB();


            Connection connection = connectToDatabase();

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM ISPIT";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
                Long id = rs.getLong("ID");
                Long subjectId = rs.getLong("PREDMET_ID");
                Long studentId = rs.getLong("STUDENT_ID");
                int grade = rs.getInt("OCJENA");

                String date = rs.getString("DATUM_I_VRIJEME");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime ldt = LocalDateTime.parse(date, dtf);

                Predmet subject = subjectsList.get(0);
                for (int i = 0; i < subjectsList.size(); i++) {
                    if(subjectsList.get(i).getId().equals(subjectId)){
                        subject = subjectsList.get(i);
                    }
                }

                Student student = studentList.get(0);
                for (Student s:
                     studentList) {
                    if(s.getId().equals(studentId)){
                        student = s;
                        break;
                    }
                }

                Enum<Ocjena> enumGrade = Datoteke.getEnumGrade(grade);
                Dvorana room = getRoomFromId(id);
                Ispit i = new Ispit(id, subject, student, enumGrade, ldt, room);
                examsList.add(i);
            }
            connection.close();
        }catch (SQLException e){
            logger.info("Dogadaj", e);
            throw new BazaPodatakaException(e);
        }
        return examsList;
    }

    public static void editExam(Ispit selected, Predmet subject, Student student, Ocjena grade, String dateString, Dvorana room) throws BazaPodatakaException {

        try{
            Connection connection = connectToDatabase();

            String query = "UPDATE ISPIT SET";

            LocalDateTime localDateTime = LocalDateTime.parse(dateString);
            dateString = dateString.replace("T", " ");
            boolean flag = false;

            if(!selected.getPredmet().equals(subject)){
                query = query + " PREDMET_ID =" + subject.getId();
                flag = true;
            }
            else flag = false;

            if(!selected.getStudent().equals(student)){
                if(flag) query = query +",";
                query = query + " STUDENT_ID ="  + student.getId();
            }
            else flag = false;

            if(!selected.getGrade().equals(grade)){
                if(flag) query = query +",";
                query = query + " OCJENA = '" + grade.ocjenaBroj + "'";
            }
            else flag = false;

            if(!selected.getDateNtime().equals(localDateTime)){
                //2018-09-01T18:00
                if(flag) query = query +",";
                query = query + "DATUM_I_VRIJEME ='" + dateString + "'";
            }

            if(!query.equals("UPDATE ISPIT SET")){
                query = query + "WHERE ID = " + selected.getId() + ";";
                PreparedStatement updateExams = connection.prepareStatement(query);
                updateExams.executeUpdate();
            }

            if(!selected.getRoom().equals(room)){
                String secondQuery = "UPDATE ISPIT_DVORANE  SET DVORANA_ID = ? WHERE ISPIT_ID = ? ";
                PreparedStatement updateExamsRoom = connection.prepareStatement(secondQuery);
                updateExamsRoom.setLong(1, room.id());
                updateExamsRoom.setLong(2, selected.getId());
                updateExamsRoom.executeUpdate();
            }

            connection.close();
            }catch (SQLException e) {
                throw new BazaPodatakaException(e);
            }

    }
    public static Dvorana getRoomFromId(Long id) {
        List<Dvorana> rooms = getRoomsFromDB();
        Dvorana room = rooms.get(0);
        for (Dvorana dv:
             rooms) {
            if(dv.id().equals(id)){
                room = dv;
                break;
            }
        }
        return room;
    }

    public static List<Dvorana> getRoomsFromDB(){
        List<Dvorana> rooms = new ArrayList<>(0);
        try {
            Connection connection = connectToDatabase();

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM DVORANE";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                Long id = rs.getLong("ID");
                String name = rs.getString("NAZIV");
                String room = rs.getString("SOBA");

                rooms.add(new Dvorana(id, name, room));
            }
        connection.close();
        } catch (SQLException | BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

        return rooms;
    }

    public static void insertNewExamIntoDB(Ispit exam){
        try {
            Connection connection = connectToDatabase();

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO ISPIT(PREDMET_ID, STUDENT_ID, OCJENA, DATUM_I_VRIJEME) VALUES (?, ?, ?, ?)");

            ps.setLong(1, exam.getPredmet().getId());
            ps.setLong(2, exam.getStudent().getId());
            ps.setInt(3, exam.getGrade().ordinal()+1);
            ps.setTimestamp(4, Timestamp.valueOf(exam.getDateNtime()));

            ps.executeUpdate();
            connection.close();
        } catch (SQLException | BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Dvorana> getDvoraneFromDB(){
        List<Dvorana> list = new ArrayList<>(0);
        try {
            Connection connection = connectToDatabase();

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM DVORANE";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()){
                Long id = rs.getLong("ID");
                String name = rs.getString("NAZIV");
                String room = rs.getString("SOBA");
                Dvorana dv = new Dvorana(id, name, room);
                list.add(dv);
            }
            connection.close();
        } catch (SQLException | BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static void insertConnectExamRoom(Ispit exam){

        try {
            Connection connection = connectToDatabase();

            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO ISPIT_DVORANE(ISPIT_ID , DVORANA_ID) VALUES (?, ?)"
            );

            ps.setLong(1, exam.getId());
            ps.setLong(2, exam.getRoom().id());
            ps.executeUpdate();
            closeConnection(connection);

        } catch (SQLException | BazaPodatakaException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Student> dohvatiStudentePremaKriterijima() throws BazaPodatakaException {
        List<Student> list = new ArrayList<>();
        BigDecimal b;
        BigDecimal max = BigDecimal.valueOf(0);
        Map<Student, BigDecimal> map = new HashMap<>();
        List<Ispit> listss = getExamsFromDB();

        for (Student s:
             getStudentsFromDB()) {
            b = new BigDecimal(0);
            BigDecimal divider = BigDecimal.valueOf(0);
            for (Ispit i:
                 getExamsFromDB()) {
                if(s.equals(i.getStudent())){
                   b = b.add(BigDecimal.valueOf(i.getGrade().ordinal()+1));
                   divider = divider.add(new BigDecimal(1));
                }
            }

            if (divider.compareTo(BigDecimal.valueOf(0))>0){
                b = b.divide(divider);
            }

            map.put(s, b);
            if(b.compareTo(max)>0){
                max = b;
            }
        }

        for (Student s:
             map.keySet()) {
            if(map.get(s).equals(max)){
                list.add(s);
            }
        }

        System.out.println("Najbolji student" + list + " " + max);
        //OptionalDouble prosjek = listaIspitaZaStudenta.stream().mapToDouble(i -> i.getOcjena()).average();


        return list;
    }

    public static List dohvatiIspitePremaKriterijima() throws BazaPodatakaException {
        Map<String, BigDecimal> map = new HashMap<>();
        BigDecimal b = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.valueOf(0);
        List list = new ArrayList<>(0);
        for (Ispit i:
             getExamsFromDB()) {
            b = BigDecimal.valueOf(0);
            if(map.containsKey(i.getPredmet().getName())){
                b = b.add(map.get(i.getPredmet().getName()));
                b = b.add(BigDecimal.valueOf(i.getGrade().ordinal()+1));
                b = b.divide(BigDecimal.valueOf(2));
                map.put(i.getPredmet().getName(), b);
                if(b.compareTo(max)>0){
                    max = b;
                }
            }
            else map.put(i.getPredmet().getName(), BigDecimal.valueOf(i.getGrade().ordinal()+1));
        }

        for (String i:
                map.keySet()) {
            if (map.get(i).equals(max)){
                list.add(i);
            }
        }
        System.out.println("Najveci prosjek" + list);
        return list;
    }
}

