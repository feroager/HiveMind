package com.example.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        setHelloView(stage);
    }

    public static void setHelloView(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientUI.class.getResource("HelloView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}