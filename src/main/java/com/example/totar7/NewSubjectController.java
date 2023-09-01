package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import util.Datoteke;
import vjezbe.entitet.Predmet;
import vjezbe.entitet.Profesor;
import vjezbe.entitet.Student;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class NewSubjectController {
    @FXML
    TextField code;
    @FXML
    TextField name;
    @FXML
    TextField ects;
    @FXML
    ComboBox<Profesor> profesorComboBox;

    @FXML
    ChoiceBox<Student> studentChoiceBox;
    @FXML
    ListView<Student> listViewChosenStudentChoiceBox;


    public static List<Student> listOfChosenStudents = new ArrayList<>(new LinkedHashSet<>(0));

    @FXML
    public void initialize() throws BazaPodatakaException, SQLException, IOException {
        listOfChosenStudents.clear();

        //List<Student> studentsList = Datoteke.getStudentsFromFile();
        List<Student> studentsList = BazaPodataka.getStudentsFromDB();
        ObservableList<Student> ol = FXCollections.observableList(studentsList);
        studentChoiceBox.setItems(ol);
        studentChoiceBox.getSelectionModel().select(0);

        ObservableList<Profesor> o = FXCollections.observableList(Datoteke.getProfesorsFromFile());
        profesorComboBox.setItems(o);
        profesorComboBox.getSelectionModel().select(0);

    }

    @FXML
    protected void addStudent(){
        Student student = studentChoiceBox.getValue();

        ObservableList<Student> ol = FXCollections.observableList(listOfChosenStudents);
        listViewChosenStudentChoiceBox.setItems(ol);

        if(listViewChosenStudentChoiceBox.getItems().contains(student)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Alert");
            alert.setContentText("That student is already chosen");
            alert.showAndWait();
        }
        else {
            listOfChosenStudents.add(student);
        }
    }

    @FXML
    protected void removeStudent(){

        int position = listViewChosenStudentChoiceBox.getSelectionModel().getSelectedIndex();
        listOfChosenStudents.remove(position);

        ObservableList<Student> ol = FXCollections.observableList(listOfChosenStudents);
        listViewChosenStudentChoiceBox.setItems(ol);

    }

    @FXML
    protected void createNewSubject() throws IOException, BazaPodatakaException {
        if (!(name.getText().isEmpty() || code.getText().isEmpty() || ects.getText().isEmpty())) {
            //OptionalLong maxId = Datoteke.getSubjectsFromFile().stream().mapToLong(subject -> subject.getId()).max();
            OptionalLong maxId = BazaPodataka.getSubjectsFromDB().stream().mapToLong(subject -> subject.getId()).max();
            boolean flag = true;
            Set<Student> students = new HashSet<>(listOfChosenStudents);
            try {
                Integer i = Integer.valueOf(ects.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alert");

                alert.setContentText("You put text instead of a number in text field for Number of ects");
                alert.showAndWait();
                flag = false;
            }
            if (flag) {
                Predmet p;
                if (listOfChosenStudents.isEmpty()) {
                    p = new Predmet(maxId.getAsLong() + 1, code.getText(), name.getText(), Integer.valueOf(ects.getText()), profesorComboBox.getValue(), new HashSet<>(0));
                } else {
                    p = new Predmet(maxId.getAsLong() + 1, code.getText(), name.getText(), Integer.valueOf(ects.getText()), profesorComboBox.getValue(), students);
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("New Subject created");
                alert.setHeaderText("Results:");
                alert.setContentText(p.getName());
                alert.showAndWait();

                listOfChosenStudents.clear();
                Datoteke.writeNewSubjectToFile(p);
                BazaPodataka.insertNewSubjectToDB(p);

            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("MISTAKE HAPPENED");
            alert.setHeaderText("Results:");
            alert.setContentText("One or more important text fields are empty");
            alert.showAndWait();
        }
    }
}
