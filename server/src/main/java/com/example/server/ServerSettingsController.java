package com.example.server;

import com.example.database.dbutils.DbManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Controller for the server settings UI.
 */
public class ServerSettingsController {
    private static ServerApplication serverApplication;
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

    /**
     * Starts the server based on the user-provided information.
     */
    @FXML
    void startServer(ActionEvent event) {
        try {
            // Get user-provided data
            String dbUrl = "jdbc:mysql://" + dbIpField.getText() + ":3306/" + dbNameField.getText();
            String dbUsername = dbUsernameField.getText();
            String dbPassword = dbPasswordField.getText();

            DbManager.initialize(dbUrl, dbUsername, dbPassword);

            // Get a connection to the database
            Connection connection = DbManager.getConnection();
            dataBaseOn();

            // Start the server in a new thread
            new Thread(() -> {
                new ServerApplication(Integer.parseInt(portField.getText()), this).startServer();
            }).start();


        } catch (SQLException e) {
            e.printStackTrace(); // or handle it in a different way
            // Update statuses in case of an error
            serverOff();
            dataBaseOff();
        }
    }

    public void dataBaseOff() {
        Platform.runLater(() -> {
            dbStatusLabel.setText("OFF");
            dbStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
        });
    }

    public void serverOff() {
        Platform.runLater(() -> {
            serverStatusLabel.setText("OFF");
            serverStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
        });
    }

    public void dataBaseOn() {
        Platform.runLater(() -> {
            dbStatusLabel.setText("ON");
            dbStatusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        });
    }

    public void serverOn() {
        Platform.runLater(() -> {
            serverStatusLabel.setText("ON");
            serverStatusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
        });
    }

}
