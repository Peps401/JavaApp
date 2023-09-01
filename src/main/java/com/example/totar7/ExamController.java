package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import util.Datoteke;
import vjezbe.entitet.Ispit;
import vjezbe.entitet.Predmet;
import vjezbe.entitet.Student;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class ExamController {
    @FXML
    private TableView<Ispit> examTableView;

    @FXML
    private TableColumn<Ispit, String> studentColumn;
    @FXML
    private TableColumn<Ispit, String> subjectNameColumn;
    @FXML
    private TableColumn<Ispit, Enum> gradeColumn;
    @FXML
    private TableColumn<Ispit, LocalDateTime> dateNTimeColumn;

    @FXML
    public void initialize(){
        //List<Ispit> examsList = Datoteke.getExamsFromFile();
        List<Ispit> examsList = null;
        try {
            examsList = BazaPodataka.getExamsFromDB();
        } catch (BazaPodatakaException e) {
            throw new RuntimeException(e);
        }

        studentColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getStudent().toString());
        });
        subjectNameColumn.setCellValueFactory(cell->{return new SimpleObjectProperty(cell.getValue().getPredmet().getName());
        });
        gradeColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getGrade());
        });

        dateNTimeColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getDateNtime());
        });

        ObservableList<Ispit> observableListExams = FXCollections.observableList(examsList);
        examTableView.setItems(observableListExams);
    }
}
