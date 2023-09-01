package com.example.totar7;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.util.Duration;
import vjezbe.niti.DatumRodjenjaNit;
import vjezbe.niti.NajboljiStudentNit;

import java.io.IOException;

public class MeniBar {

    public void initialize(){
        Timeline prikazSlavljenika = new Timeline(
                new KeyFrame(Duration.seconds(10), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater( new DatumRodjenjaNit());
                    }
                }));
        prikazSlavljenika.setCycleCount(Timeline.INDEFINITE);
        prikazSlavljenika.play();

        Timeline najboljiStudent = new Timeline(
                new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Platform.runLater(new NajboljiStudentNit());
                    }
                }));
        najboljiStudent.setCycleCount(Timeline.INDEFINITE);
        najboljiStudent.play();


    }

    public void searchProf (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ProfesorSearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    public void newProf(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewProfesor.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    public void searchStudent (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("StudentSearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    public void newStudent(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    public void deleteStudent(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("DeleteStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    public void updateStudent(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("UpdateStudent.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }
    public void searchSubject (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("SubjectSearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    public void newSubject(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewSubject.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    public void searchExam (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ExamSearch.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    public void newExam (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("NewExam.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }

    public void updateExam (ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("ExamUpdate.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        HelloApplication.getStage().setScene(scene);
        HelloApplication.getStage().show();

    }
}
