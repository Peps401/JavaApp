package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import vjezbe.entitet.*;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ExamUpdateController {
    @FXML
    ComboBox<Predmet> comboBoxSubjects;
    @FXML
    ComboBox<Student> studentComboBox;
    @FXML
    ComboBox<Ocjena> ocjenaComboBox;
    @FXML
    ComboBox<Dvorana> roomComboBox;
    @FXML
    ListView<Ispit> examsListView;
    @FXML
    TextField date;

    @FXML
    public void initialize() throws BazaPodatakaException, SQLException, IOException {
        List<Ispit> examsList = BazaPodataka.getExamsFromDB();
        ObservableList<Ispit> ol = FXCollections.observableList(examsList);
        examsListView.setItems(ol);

        List<Predmet> sl = BazaPodataka.getSubjectsFromDB();
        ObservableList<Predmet> olp = FXCollections.observableList(sl);
        comboBoxSubjects.setItems(olp);

        List<Student> ssl = BazaPodataka.getStudentsFromDB();
        ObservableList<Student> osl = FXCollections.observableList(ssl);
        studentComboBox.setItems(osl);

        List<Dvorana> d = BazaPodataka.getDvoraneFromDB();
        ObservableList<Dvorana> dol = FXCollections.observableList(d);
        roomComboBox.setItems(dol);

        List<Ocjena> ool = new ArrayList<>(0);
        ool.add(Ocjena.NEDOVOLJAN);
        ool.add(Ocjena.DOVOLJAN);
        ool.add(Ocjena.DOBAR);
        ool.add(Ocjena.VRLO_DOBAR);
        ool.add(Ocjena.IZVRSTAN);
        ObservableList<Ocjena> ocjOl = FXCollections.observableList(ool);
        ocjenaComboBox.setItems(ocjOl);
    }
    @FXML
    protected void choseExam(){
        Ispit s = examsListView.getSelectionModel().getSelectedItem();
        comboBoxSubjects.getSelectionModel().select(s.getPredmet());
        studentComboBox.getSelectionModel().select(s.getStudent());
        ocjenaComboBox.getSelectionModel().select((Ocjena) s.getGrade());
        roomComboBox.getSelectionModel().select(s.getRoom());
        date.setText(s.getDateNtime().toString());

    }

    @FXML
    protected void editExam() throws Exception {

        Ispit s = examsListView.getSelectionModel().getSelectedItem();

        BazaPodataka.editExam(s, comboBoxSubjects.getValue(), studentComboBox.getValue(), ocjenaComboBox.getValue(), date.getText(), roomComboBox.getValue());
    }
}
