package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import util.Datoteke;
import vjezbe.entitet.Predmet;
import vjezbe.entitet.Profesor;
import vjezbe.entitet.Student;
import vjezbe.iznimke.BazaPodatakaException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SubjectController {
    @FXML
    private TableView<Predmet> subjectTableView;

    @FXML
    private TableColumn<Predmet, String> codeColumn;
    @FXML
    private TableColumn<Predmet, String> subjectNameColumn;
    @FXML
    private TableColumn<Predmet, Set> studentiColumn;
    @FXML
    private TableColumn<Predmet, Integer> noOfEctsColumn;
    @FXML
    private TableColumn<Predmet, String> profesorColumn;



    @FXML
    public void initialize() throws BazaPodatakaException {
        //List<Predmet> subjectList = Datoteke.getSubjectsFromFile();
        List<Predmet> subjectList = BazaPodataka.getSubjectsFromDB();

        codeColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getCode());
        });
        subjectNameColumn.setCellValueFactory(cell->{return new SimpleObjectProperty(cell.getValue().getName());
        });
        studentiColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getStudents());
        });
        noOfEctsColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getNoOfEcts());
        });
        profesorColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getPerson().toString());
        });

        ObservableList<Predmet> observableListSubject = FXCollections.observableList(subjectList);
        subjectTableView.setItems(observableListSubject);
    }

    @FXML
    protected void searchButtonSubject() throws BazaPodatakaException {
        //List<Predmet> subjectList = Datoteke.getSubjectsFromFile();
        List<Predmet> subjectList = BazaPodataka.getSubjectsFromDB();

        List<Student> filteredList = new ArrayList<>(0);

    }
}
