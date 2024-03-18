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

import java.io.IOException;

public class LoginController {

    @FXML
    private Button BackLogin;

    @FXML
    private Button SignInLogin;

    @FXML
    private TextField ipFieldLogin;

    @FXML
    private PasswordField passwordFieldLogin;

    @FXML
    private TextField portFieldLogin;

    @FXML
    private Label resultLabelLogin;

    @FXML
    private TextField usernameFieldLogin;

    private MainController mainController;

    @FXML
    void onActionBackLogin(ActionEvent event) {
        setHelloView(BackLogin);
    }

    @FXML
    void onActionSignInLogin(ActionEvent event) {
        ClientApplication clientApplication = new ClientApplication(ipFieldLogin.getText(), Integer.parseInt(portFieldLogin.getText()));
        clientApplication.setLoginController(this);
        clientApplication.login(usernameFieldLogin.getText(), passwordFieldLogin.getText());
    }

    public static void setHelloView(Button button) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloController.class.getResource("/com/hivemind/client/HelloView.fxml"));
            Parent loginView = loader.load();
            Stage primaryStage = (Stage) button.getScene().getWindow();
            primaryStage.setScene(new Scene(loginView));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMainView() {
        //Platform.runLater(() -> {
        try {
            FXMLLoader loader = new FXMLLoader(MainController.class.getResource("/com/hivemind/client/MainView.fxml"));
            Parent mainView = loader.load();
            Stage primaryStage = (Stage) BackLogin.getScene().getWindow();
            primaryStage.setResizable(true);
            primaryStage.setScene(new Scene(mainView));
            MainController mainController = loader.getController();
            this.mainController = mainController;
            this.mainController.setStage(primaryStage);
            // Setting the closing event handler
            primaryStage.setOnCloseRequest(event -> {
                mainController.getFooterController().onActionLogoutButton();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        //});
    }

    public void setResultLabelLogin(CommunicationMessage result)
    {
        Platform.runLater(() -> {
            resultLabelLogin.setTextFill(javafx.scene.paint.Color.RED);
            resultLabelLogin.setText(result.getData());
        });
    }

    public MainController getMainController()
    {
        return mainController;
    }
}
