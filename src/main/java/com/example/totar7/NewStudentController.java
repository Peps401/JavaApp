package com.example.totar7;

import hr.java.vjezbe.baza.BazaPodataka;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Datoteke;
import vjezbe.entitet.Profesor;
import vjezbe.entitet.Student;
import vjezbe.iznimke.BazaPodatakaException;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

public class NewStudentController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private TextField jmbagTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ImageView imageView;

    @FXML
    public void initialize() throws FileNotFoundException {
        imageView.setImage(new Image(new FileInputStream("src/images/student2.jpeg")));
    }
    @FXML
    protected void newStudentButton() throws SQLException, IOException, BazaPodatakaException {
        if(nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || jmbagTextField.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Couldnt create new Student");
            alert.setHeaderText("Results:");
            alert.setContentText("One or all text fields are empty!");

            alert.showAndWait();
        }
        else{
            String name = nameTextField.getText();
            String surname = surnameTextField.getText();
            String jmbag = jmbagTextField.getText();
            LocalDate date= datePicker.getValue();

            List<Student> studentsList = BazaPodataka.getStudentsFromDB();
            OptionalLong maksimalniId = studentsList.stream()
                    .mapToLong(student -> student.getId()).max();

            Student student = new Student(maksimalniId.getAsLong() + 1, name, surname, jmbag, date);
            boolean alreadyExists = studentsList.stream().anyMatch(s -> s.getJmbag().equalsIgnoreCase(student.getJmbag()) || (s.getName().equalsIgnoreCase(student.getName())&& s.getSurname().equalsIgnoreCase(student.getSurname())));
            if(alreadyExists){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Student already exists");
                alert.setHeaderText("Results:");
                alert.setContentText(student.getName()  + " " + student.getSurname() +  " " + student.getJmbag() +  " " + student.getId());

                alert.showAndWait();
            }
            else {
                Datoteke.writeNewStudentToFile(student);
                BazaPodataka.insertNewStudentnToDB(student);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("New Student created");
                alert.setHeaderText("Results:");
                alert.setContentText(student.getName()  + " " + student.getSurname() +  " " + student.getJmbag() +  " " + student.getId());

                alert.showAndWait();
            }
        }
    }
}
