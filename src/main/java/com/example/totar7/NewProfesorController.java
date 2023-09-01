package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import util.Datoteke;
import vjezbe.entitet.Profesor;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class NewProfesorController {
    @FXML
    TextField nameTextField;
    @FXML
    TextField surnameTextField;
    @FXML
    TextField titleTextField;
    @FXML
    TextField codeTextField;
    @FXML
    protected void createNewProfButton() throws IOException, SQLException, BazaPodatakaException {

        if(nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || titleTextField.getText().isEmpty() || codeTextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Couldnt create new Professor");
            alert.setHeaderText("Results:");
            alert.setContentText("One or all text fields are empty!");

            alert.showAndWait();
        }
        else{
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String title = titleTextField.getText();
            String code = codeTextField.getText();

            //List<Profesor> profesorsList = Datoteke.getProfesorsFromFile();
            List<Profesor> profesorsList = BazaPodataka.getProfesorsFromDB();
            OptionalLong maksimalniId = profesorsList.stream()
                    .mapToLong(profesor -> profesor.getId()).max();

            Profesor profesor = new Profesor(maksimalniId.getAsLong() + 1, code, name, surname, title);
            boolean alreadyExists = profesorsList.stream().anyMatch(p -> p.getCode().equalsIgnoreCase(profesor.getCode()) || (p.getName().equalsIgnoreCase(profesor.getName())&& p.getSurname().equalsIgnoreCase(profesor.getSurname())));
            if(alreadyExists){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Professor already exists");
                alert.setHeaderText("Results:");
                alert.setContentText(profesor.getName()  + " " + profesor.getSurname() +  " " + profesor.getCode() +  " " + profesor.getId());

                alert.showAndWait();
            }
            else {
                Datoteke.writeNewProfesorToFile(profesor);
                BazaPodataka.insertNewProfToDB(profesor);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("New Professor created");
                alert.setHeaderText("Results:");
                alert.setContentText(profesor.toString());

                alert.showAndWait();
            }
        }
    }
}
