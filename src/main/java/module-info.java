module com.example.climatemonitoringserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.climatemonitoringserver to javafx.fxml;
    exports com.example.climatemonitoringserver;
}