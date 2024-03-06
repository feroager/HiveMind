module com.example.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires slf4j.api;


    opens com.example.server to javafx.fxml;
    exports com.example.server;
    exports com.example.message;
    opens com.example.message to javafx.fxml;
    exports com.example.utils;
    opens com.example.utils to javafx.fxml;
}