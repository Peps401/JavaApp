package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import util.Datoteke;
import vjezbe.entitet.Profesor;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProfesorController {
    private static List<Profesor> profesorList = new ArrayList<>(0);
    @FXML
    private TableView<Profesor> profTableView;

    @FXML
    private TableColumn<Profesor, String> profCode;
    @FXML
    private TableColumn<Profesor, String> profName;
    @FXML
    private TableColumn<Profesor, String> profSurname;
    @FXML
    private TableColumn<Profesor, String> profTitle;

    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField titulaTextField;
    @FXML
    private TextField codeTextField;


    @FXML
    public void initialize() throws BazaPodatakaException {
        profesorList.clear();

        //profesorList = Datoteke.getProfesorsFromFile();
        profesorList = BazaPodataka.getProfesorsFromDB();

        profCode.setCellValueFactory(cell->{return new SimpleObjectProperty(cell.getValue().getCode());
        });
        profName.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getName());
        });
        profSurname.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getSurname());
        });
        profTitle.setCellValueFactory(cell->{return new SimpleObjectProperty<>(cell.getValue().getTitle());
        });

        ObservableList<Profesor> observableListProf = FXCollections.observableList(profesorList);
        profTableView.setItems(observableListProf);
    }

    @FXML
    protected void searchButtonProf() throws BazaPodatakaException {
        //profesorList = Datoteke.getProfesorsFromFile();
        profesorList = BazaPodataka.getProfesorsFromDB();

        List<Profesor> filteredList = new ArrayList<>(0);


        String searchedName = nameTextField.getText();
        String searchedSurname = surnameTextField.getText();
        String searchedTitle = titulaTextField.getText();
        String searchedCode = codeTextField.getText();

        if(!searchedName.isEmpty() && !searchedSurname.isEmpty() && !searchedTitle.isEmpty() && !searchedCode.isEmpty()){
            filteredList = profesorList.stream()
                    .filter(profesor -> profesor.getName().toLowerCase().contains(searchedName.toLowerCase()))
                    .filter(profesor -> profesor.getSurname().toLowerCase().contains(searchedSurname.toLowerCase()))
                    .filter(profesor -> profesor.getTitle().toLowerCase().contains(searchedTitle.toLowerCase()))
                    .filter(profesor -> profesor.getCode().toLowerCase().contains(searchedCode.toLowerCase()))
                    .collect(Collectors.toList());

            profTableView.setItems(FXCollections.observableList(filteredList));
        }

        else {
            if(!searchedName.isEmpty()){
                filteredList = profesorList.stream()
                        .filter(profesor -> profesor.getName().toLowerCase().contains(searchedName.toLowerCase())).collect(Collectors.toList());
            }
            if(!searchedSurname.isEmpty()){
                filteredList = profesorList.stream()
                        .filter(profesor -> profesor.getSurname().toLowerCase().contains(searchedSurname.toLowerCase())).collect(Collectors.toList());
            }
            if(!searchedTitle.isEmpty()){
                filteredList = profesorList.stream()
                        .filter(profesor -> profesor.getTitle().toLowerCase().contains(searchedTitle.toLowerCase())).collect(Collectors.toList());
            }
            if(!searchedCode.isEmpty()){
                filteredList = profesorList.stream()
                        .filter(profesor -> profesor.getCode().toLowerCase().contains(searchedCode.toLowerCase())).collect(Collectors.toList());
            }

            int n = 1;
            if(filteredList.size()>1) {
                for (int i = 0; i < filteredList.size(); i++) {
                    Profesor a = filteredList.get(i);
                    Profesor b = filteredList.get(n);

                    if (a.getCode().equals(b.getCode())) {
                        filteredList.remove(n);
                    }
                    n++;
                }
            }
            profTableView.setItems(FXCollections.observableList(filteredList));
        }

    }

}
