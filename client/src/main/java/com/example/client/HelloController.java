package com.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Button SignInHello;
    @FXML
    private Button SignUpHello;

    // ...

    @FXML
    public void onActionSignInHello(ActionEvent actionEvent) {
        setLoginView(SignInHello);
    }

    @FXML
    public void onActionSignUpHello(ActionEvent actionEvent) {
        setRegistrationView(SignUpHello);
    }

    public static void setLoginView(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("/com/example/client/LoginView.fxml"));
            Parent loginView = loader.load();
            Stage primaryStage = (Stage) button.getScene().getWindow();
            primaryStage.setScene(new Scene(loginView));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRegistrationView(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("/com/example/client/RegistrationView.fxml"));
            Parent loginView = loader.load();
            Stage primaryStage = (Stage) button.getScene().getWindow();
            primaryStage.setScene(new Scene(loginView));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}