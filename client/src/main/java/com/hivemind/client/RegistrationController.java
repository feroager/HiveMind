package com.hivemind.client;

import com.hivemind.message.CommunicationMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The RegistrationController class controls the registration process in the GUI.
 */
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @FXML
    private Button BackRegistration;

    @FXML
    private Button SignUpRegistration;

    @FXML
    private TextField emailRegistartion;

    @FXML
    private TextField ipRegistartion;

    @FXML
    private TextField userNameRegistartion;

    @FXML
    private PasswordField passwordRegistartion;

    @FXML
    private TextField portRegistartion;

    @FXML
    private Label resultLabelRegistartion;

    /**
     * Sets the text of the result label for registration feedback.
     *
     * @param result The communication message containing the registration result.
     */
    public void setResultLabelRegistartionText(CommunicationMessage result)
    {
        Platform.runLater(() -> {
            if(result.getData().equals("Registration was successful, you can log in"))
            {
                resultLabelRegistartion.setTextFill(javafx.scene.paint.Color.GREEN);
            }
            else
            {
                resultLabelRegistartion.setTextFill(javafx.scene.paint.Color.RED);
            }
            resultLabelRegistartion.setText(result.getData());
        });
    }

    /**
     * Handles the action event for the back button during registration.
     *
     * @param event The action event triggered by clicking the back button.
     */
    @FXML
    void onActionBackRegistration(ActionEvent event) {
        setHelloView(BackRegistration);
    }

    /**
     * Handles the action event for the sign up button during registration.
     *
     * @param event The action event triggered by clicking the sign up button.
     */
    @FXML
    void onActionSignUpRegistration(ActionEvent event) {
        ClientApplication clientApplication = new ClientApplication(ipRegistartion.getText(), Integer.parseInt(portRegistartion.getText()));
        clientApplication.setRegistrationController(this);
        clientApplication.register(userNameRegistartion.getText(), passwordRegistartion.getText(), emailRegistartion.getText());
    }

    /**
     * Sets the hello view when clicking the back button.
     *
     * @param button The back button.
     */
    public static void setHelloView(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("/com/hivemind/client/HelloView.fxml"));
            Parent loginView = loader.load();
            Stage primaryStage = (Stage) button.getScene().getWindow();
            primaryStage.setScene(new Scene(loginView));
        } catch (IOException e) {
            logger.error("Error occurred:", e);
        }
    }

}
