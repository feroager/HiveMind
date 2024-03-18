package com.hivemind.client;

import com.hivemind.message.CommunicationMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * The FooterController class is responsible for controlling the footer section of the user interface.
 */
public class FooterController {

    @FXML
    private Button logoutButtonFooter;

    @FXML
    private Label usernameLabelFooter;
    private ClientHandler clientHandler;

    /**
     * Sets the client handler for this controller.
     *
     * @param clientHandler The client handler to be set.
     */
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Handles the action event when the logout button is clicked.
     * It invokes the handleLogout method of the client handler.
     * Closes the stage after logout.
     */
    @FXML
    void onActionLogoutButton() {
        if (clientHandler != null) {
            clientHandler.handleLogout();
        }
        closeStage();
    }

    /**
     * Sets the username label in the footer based on the received communication message.
     *
     * @param communicationMessage The communication message containing user information.
     */
    public void setUsernameLabelFooter(CommunicationMessage communicationMessage) {
        Platform.runLater(() -> {
            this.usernameLabelFooter.setText(communicationMessage.getUser().getUsername());
        });
    }

    /**
     * Closes the stage associated with the footer controller.
     */
    private void closeStage() {
        Scene scene = logoutButtonFooter.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }
}
