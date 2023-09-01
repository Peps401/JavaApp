package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import util.Datoteke;
import vjezbe.entitet.*;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class NewExamController {
    @FXML
    ComboBox<Predmet> subjectComboBox;
    @FXML
    ComboBox<Student> studentComboBox;
    @FXML
    ComboBox<Ocjena> ocjenaComboBox;
    @FXML
    ComboBox<Dvorana> roomComboBox;
    @FXML
    TextField date;

    @FXML
    public void initialize() throws BazaPodatakaException, SQLException, IOException {
        ObservableList<Predmet> olp = FXCollections.observableList(BazaPodataka.getSubjectsFromDB());
        subjectComboBox.setItems(olp);
        subjectComboBox.getSelectionModel().select(0);

        ObservableList<Student> ols = FXCollections.observableList(BazaPodataka.getStudentsFromDB());
        studentComboBox.setItems(ols);
        studentComboBox.getSelectionModel().select(0);

        List<Ocjena> ocjene = new ArrayList<>();
        ocjene.add(Ocjena.NEDOVOLJAN);
        ocjene.add(Ocjena.DOVOLJAN);
        ocjene.add(Ocjena.DOBAR);
        ocjene.add(Ocjena.VRLO_DOBAR);
        ocjene.add(Ocjena.IZVRSTAN);
        ObservableList<Ocjena> olc = FXCollections.observableList(ocjene);
        ocjenaComboBox.setItems(olc);
        ocjenaComboBox.getSelectionModel().select(1);

        ObservableList<Dvorana> old = FXCollections.observableList(BazaPodataka.getRoomsFromDB());
        roomComboBox.setItems(old);
        roomComboBox.getSelectionModel().select(0);

    }
    @FXML
    protected void newExam() throws IOException {
        String stringDate = date.getText();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy.'T'HH:mm");
        LocalDateTime parsedDate = LocalDateTime.parse(stringDate, dtf);

        OptionalLong maksimalniId = Datoteke.getExamsFromFile().stream()
                .mapToLong(profesor -> profesor.getId()).max();
        maksimalniId = OptionalLong.of(maksimalniId.getAsLong() + 1);

        Ispit exam = new Ispit(maksimalniId.getAsLong(), subjectComboBox.getValue(),studentComboBox.getValue(), ocjenaComboBox.getValue(), parsedDate, roomComboBox.getValue());
        //Datoteke.writeNewExamToFile(exam);
        BazaPodataka.insertConnectExamRoom(exam);
        BazaPodataka.insertNewExamIntoDB(exam);


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("New Exam created");
        alert.setHeaderText("Results:");
        alert.setContentText(exam.toString());

        alert.showAndWait();
    }
}
