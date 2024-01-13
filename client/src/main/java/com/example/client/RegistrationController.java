package com.example.client;

import com.example.message.Message;
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

import java.io.IOException;

public class RegistrationController {


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

    public void setResultLabelRegistartionText(Message result)
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

    @FXML
    void onActionBackRegistration(ActionEvent event) {
        setHelloView(BackRegistration);
    }
    @FXML
    void onActionSignUpRegistration(ActionEvent event) {
        ClientApplication clientApplication = new ClientApplication(ipRegistartion.getText(), Integer.parseInt(portRegistartion.getText()));
        clientApplication.setRegistrationController(this);
        clientApplication.register(userNameRegistartion.getText(), passwordRegistartion.getText(), emailRegistartion.getText());
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

}
