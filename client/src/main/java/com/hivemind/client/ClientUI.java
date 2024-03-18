package com.hivemind.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The ClientUI class is responsible for launching the user interface of the client application.
 */
public class ClientUI extends Application {

    /**
     * The start method, overridden from Application class, sets up the initial view of the client application.
     *
     * @param stage The primary stage for this application.
     * @throws IOException if an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        setHelloView(stage);
    }

    /**
     * Sets up the "Hello" view of the client application.
     *
     * @param stage The primary stage for this application.
     * @throws IOException if an error occurs while loading the FXML file.
     */
    public static void setHelloView(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientUI.class.getResource("HelloView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("HiveMind");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method of the ClientUI class, launching the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }

}
