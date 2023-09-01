module com.example.totar7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    opens com.example.totar7 to javafx.fxml;
    exports com.example.totar7;
}