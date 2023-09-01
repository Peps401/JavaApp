package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import vjezbe.entitet.Ispit;
import vjezbe.entitet.Predmet;
import vjezbe.entitet.Student;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class UpdateStudentController {
    @FXML
    TextField name;
    @FXML
    TextField surname;
    @FXML
    TextField jmbag;
    @FXML
    DatePicker datePicker;
    @FXML
    ListView<Student> studentListView;


    @FXML
    public void initialize() throws BazaPodatakaException, SQLException, IOException {
        List<Student> studentList = BazaPodataka.getStudentsFromDB();
        ObservableList<Student> ol = FXCollections.observableList(studentList);
        studentListView.setItems(ol);
    }

    @FXML
    protected void choseStudent(){
        Student s = studentListView.getSelectionModel().getSelectedItem();
        name.setText(s.getName());
        surname.setText(s.getSurname());
        jmbag.setText(s.getJmbag());
        datePicker.setValue(s.getDateOfBirth());

    }
    @FXML
    protected void updateStudent() throws Exception {
        Student s = null;
        List<Student> studentList = BazaPodataka.getStudentsFromDB();
        for (int i = 0; i < studentList.size(); i++) {
            if(name.getText().equals(studentList.get(i).getName())){
                s = studentList.get(i);
                break;
            }
        }

        BazaPodataka.editStudent(s, name.getText(), surname.getText(), jmbag.getText(), datePicker.getValue());

    }

}
