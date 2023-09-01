package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import vjezbe.entitet.Student;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DeleteStudentController {
    @FXML
    ComboBox<Student> studentComboBox;
    @FXML
    ListView<Student> studentListView;
    @FXML
    TextField chosenStudent;

    @FXML
    public void initialize() throws BazaPodatakaException, SQLException, IOException {
        List<Student> studentList = BazaPodataka.getStudentsFromDB();
        ObservableList<Student> ol = FXCollections.observableList(studentList);
        studentListView.setItems(ol);

        studentComboBox.setItems(ol);
    }


    @FXML
    protected void deleteButton() throws Exception {
        Student s = studentListView.getSelectionModel().getSelectedItem();
        chosenStudent.setText(s.getName());
        studentComboBox.getSelectionModel().select(studentListView.getSelectionModel().getSelectedItem());

        BazaPodataka.deleteStudent(s);
        List<Student> studentList = BazaPodataka.getStudentsFromDB();
        ObservableList<Student> ol = FXCollections.observableList(studentList);
        studentListView.setItems(ol);
    }
}
