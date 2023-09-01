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
import vjezbe.entitet.Ispit;
import vjezbe.entitet.Student;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StudentController {
    private static List<Student> studentList = new ArrayList<>(0);
    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private TableColumn<Student, String> jmbagColumn;
    @FXML
    private TableColumn<Student, String> studentNameColumn;
    @FXML
    private TableColumn<Student, String> studentSurnameColumn;
    @FXML
    private TableColumn<Student, String> studentTimeColumn;

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField jmbagTextField;
    @FXML
    private DatePicker datePicker;


    @FXML
    public void initialize() throws SQLException, IOException, BazaPodatakaException {
        studentList.clear();
        //studentList = Datoteke.getStudentsFromFile();
        studentList = BazaPodataka.getStudentsFromDB();


        jmbagColumn.setCellValueFactory(cell->{return new SimpleObjectProperty(cell.getValue().getJmbag());
        });
        studentNameColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getName());
        });
        studentSurnameColumn.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getSurname());
        });
        studentTimeColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<Student, String>,
                                        ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(
                            TableColumn.CellDataFeatures<Student, String> student) {
                        SimpleStringProperty property = new
                                SimpleStringProperty();
                        DateTimeFormatter formatter =
                                DateTimeFormatter.ofPattern("dd.MM.yyyy.");
                        property.setValue(
                                student.getValue().getDateOfBirth().format(formatter));
                        return property;
                    }
                });


        ObservableList<Student> observableListStudent = FXCollections.observableList(studentList);
        studentTableView.setItems(observableListStudent);
    }

    @FXML
    protected void searchButtonStudent() throws SQLException, IOException, BazaPodatakaException {
        //studentList = Datoteke.getStudentsFromFile();
        studentList = BazaPodataka.getStudentsFromDB();

        List<Student> filteredList = new ArrayList<>(0);

        String searchedName = nameTextField.getText();
        String searchedSurname = surnameTextField.getText();
        String searchedJmbag = jmbagTextField.getText();
        LocalDate searchedDate = datePicker.getValue();
        String dateString = String.valueOf(datePicker.getValue());


        if(!searchedName.isEmpty() && !searchedSurname.isEmpty() && !searchedJmbag.isEmpty()){
            filteredList = studentList.stream()
                    .filter(student -> student.getName().toLowerCase().contains(searchedName.toLowerCase()))
                    .filter(student -> student.getSurname().toLowerCase().contains(searchedSurname.toLowerCase()))
                    .filter(student -> student.getJmbag().toLowerCase().contains(searchedJmbag.toLowerCase()))
                    .filter(student -> student.getDateOfBirth().equals(searchedDate))
                    .collect(Collectors.toList());

            studentTableView.setItems(FXCollections.observableList(filteredList));
        }

        else {
            if(!searchedName.isEmpty()){
                filteredList = studentList.stream()
                        .filter(student -> student.getName().toLowerCase().contains(searchedName.toLowerCase())).collect(Collectors.toList());
            }
            if(!searchedSurname.isEmpty()){
                filteredList = studentList.stream()
                        .filter(student -> student.getSurname().toLowerCase().contains(searchedSurname.toLowerCase())).collect(Collectors.toList());
            }
            if(!searchedJmbag.isEmpty()){
                filteredList = studentList.stream()
                        .filter(student -> student.getJmbag().toLowerCase().contains(searchedJmbag.toLowerCase())).collect(Collectors.toList());
            }
            if(!dateString.isEmpty() && dateString!="null"){
                filteredList = studentList.stream()
                        .filter(student -> student.getDateOfBirth().equals(searchedDate)).collect(Collectors.toList());
            }

            int n = 1;
            if(filteredList.size()>1) {
                for (int i = 0; i < filteredList.size()-1; i++) {
                    Student a = filteredList.get(i);
                    Student b = filteredList.get(n);

                    if (a.getId().equals(b.getId())) {
                        filteredList.remove(n);
                    }
                    n++;
                }
            }
            studentTableView.setItems(FXCollections.observableList(filteredList));
        }

    }

}
