module com.example.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.server to javafx.fxml;
    exports com.example.server;
    exports com.example.message;
    opens com.example.message to javafx.fxml;
    exports com.example.support;
    opens com.example.support to javafx.fxml;
}