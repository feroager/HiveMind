package com.hivemind.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The HelloController class controls the actions and events in the HelloView.fxml.
 */
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @FXML
    private Button SignInHello;
    @FXML
    private Button SignUpHello;

    // ...

    /**
     * Handles the action event when the Sign In button is clicked.
     * It sets the login view by loading LoginView.fxml.
     *
     * @param actionEvent The action event triggered by the Sign In button.
     */
    @FXML
    public void onActionSignInHello(ActionEvent actionEvent) {
        setLoginView(SignInHello);
    }

    /**
     * Handles the action event when the Sign Up button is clicked.
     * It sets the registration view by loading RegistrationView.fxml.
     *
     * @param actionEvent The action event triggered by the Sign Up button.
     */
    @FXML
    public void onActionSignUpHello(ActionEvent actionEvent) {
        setRegistrationView(SignUpHello);
    }

    /**
     * Sets the login view by loading LoginView.fxml.
     *
     * @param button The button triggering the action (Sign In button).
     */
    public static void setLoginView(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("/com/hivemind/client/LoginView.fxml"));
            Parent loginView = loader.load();
            Stage primaryStage = (Stage) button.getScene().getWindow();
            primaryStage.setScene(new Scene(loginView));
        } catch (IOException e) {
            logger.error("Error occurred:", e);
        }
    }

    /**
     * Sets the registration view by loading RegistrationView.fxml.
     *
     * @param button The button triggering the action (Sign Up button).
     */
    public static void setRegistrationView(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("/com/hivemind/client/RegistrationView.fxml"));
            Parent loginView = loader.load();
            Stage primaryStage = (Stage) button.getScene().getWindow();
            primaryStage.setScene(new Scene(loginView));
        } catch (IOException e) {
            logger.error("Error occurred:", e);
        }
    }
}
