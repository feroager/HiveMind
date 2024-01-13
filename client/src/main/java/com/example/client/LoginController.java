package com.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button BackLogin;

    @FXML
    private Button SignInLogin;

    @FXML
    void onActionBackLogin(ActionEvent event) {
        setHelloView(BackLogin);
    }

    @FXML
    void onActionSignInLogin(ActionEvent event) {

    }

    public static void setHelloView(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("/com/example/client/HelloView.fxml"));
            Parent loginView = loader.load();
            Stage primaryStage = (Stage) button.getScene().getWindow();
            primaryStage.setScene(new Scene(loginView));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setMainView() {

    }

}
