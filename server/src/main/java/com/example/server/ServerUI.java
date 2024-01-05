package com.example.server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerUI extends Application {

    private static ServerApplication serverApplication;
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ServerSettingsView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        primaryStage.setTitle("Project Chat Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        serverApplication = new ServerApplication();



    }

    public static void main(String[] args) {
        launch(args);
    }
}
