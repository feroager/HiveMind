package com.example.client;

import com.example.message.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FooterController {

    @FXML
    private Button logoutButtonFooter;

    @FXML
    private Label usernameLabelFooter;
    @FXML
    void onActionLogoutButton(ActionEvent event) {

    }
    public void setUsernameLabelFooter(Message message)
    {
        this.usernameLabelFooter.setText(message.getUser().getUsername());
    }
}
