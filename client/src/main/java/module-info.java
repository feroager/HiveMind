module com.example.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires slf4j.api;


    opens com.hivemind.client to javafx.fxml;
    exports com.hivemind.client;
}