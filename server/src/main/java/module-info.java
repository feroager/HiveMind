module com.example.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires slf4j.api;


    opens com.hivemind.server to javafx.fxml;
    exports com.hivemind.server;
    exports com.hivemind.message;
    opens com.hivemind.message to javafx.fxml;
    exports com.hivemind.utils;
    opens com.hivemind.utils to javafx.fxml;
}