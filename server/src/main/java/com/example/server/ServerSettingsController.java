package com.example.server;

import com.example.database.dbutils.DbManager;
import javafx.application.Application;
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

            // Additional operations related to starting the server can be placed here
            // ...

            // Update statuses
            serverOn();
            dataBaseOn();
            new ServerApplication();
        } catch (SQLException e) {
            e.printStackTrace(); // or handle it in a different way
            // Update statuses in case of an error
            serverOff();
            dataBaseOff();
        }
    }

    private void dataBaseOff()
    {
        dbStatusLabel.setText("OFF");
        dbStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
    }

    private void serverOff()
    {
        serverStatusLabel.setText("OFF");
        serverStatusLabel.setTextFill(javafx.scene.paint.Color.RED);
    }

    private void dataBaseOn()
    {
        dbStatusLabel.setText("ON");
        dbStatusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
    }

    private void serverOn()
    {
        serverStatusLabel.setText("ON");
        serverStatusLabel.setTextFill(javafx.scene.paint.Color.GREEN);
    }

}
