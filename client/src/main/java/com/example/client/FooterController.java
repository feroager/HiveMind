package com.example.client;

import com.example.message.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class FooterController {

    @FXML
    private Button logoutButtonFooter;

    @FXML
    private Label usernameLabelFooter;
    private ClientHandler clientHandler;

    public void setClientHandler(ClientHandler clientHandler)
    {
        this.clientHandler = clientHandler;
    }

    @FXML
    void onActionLogoutButton(ActionEvent event) {
        if (clientHandler != null) {
            clientHandler.handleLogout();
        }
        closeStage();
    }
    public void setUsernameLabelFooter(Message message)
    {
        Platform.runLater(() -> {
            this.usernameLabelFooter.setText(message.getUser().getUsername());
        });
    }

    private void closeStage() {
        Scene scene = logoutButtonFooter.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }


}
