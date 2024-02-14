package com.example.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The ServerUI class represents the graphical user interface for the server application.
 */
public class ServerUI extends Application {

    private static ServerApplication serverApplication;
    private static ServerSettingsController serverSettingsController;

    /**
     * Starts the server UI by loading the ServerSettingsView.fxml.
     *
     * @param primaryStage The primary stage of the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerSettingsView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        primaryStage.setTitle("Project Chat Server");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * The main method of the server UI application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
