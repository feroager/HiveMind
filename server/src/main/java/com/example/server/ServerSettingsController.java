package com.example.server;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ServerSettingsController {

    @FXML
    private TextField dbIpField;

    @FXML
    private TextField dbNameField;

    @FXML
    private TextField dbPasswordField;

    @FXML
    private Label dbStatusLabel;

    @FXML
    private TextField dbUsernameField;

    @FXML
    private TextField portField;

    @FXML
    private Label serverStatusLabel;

    @FXML
    void startServer(ActionEvent event) {

    }

}
