package com.example.client;

import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.database.models.Server;
import com.example.database.models.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class MessagesController
{
    @FXML
    private VBox messagesContainer;
    private ClientHandler clientHandler;
    private List<Message> messagesList;

    public void setClientHandler(ClientHandler clientHandler)
    {
        this.clientHandler = clientHandler;
    }

    public void updateMessgaesList(List<Message> messagesList)
    {
        Platform.runLater(() -> {
            System.out.println("testUpdateMessgaesList");
            this.messagesList = messagesList;
            messagesContainer.getChildren().clear();

            for(Message message: this.messagesList)
            {
                Label messageLabel = createMessage(message);
                System.out.println(messageLabel);
                messagesContainer.getChildren().add(messageLabel);
            }
        });
    }

    private User associateUsernameWithIdMessage(Message message)
    {
        for(User user : clientHandler.getServerUserList())
        {
            if(user.getUserId() == message.getUserId())
                return user;
        }
        System.out.println("cos poszlo nie tak");
        return null;
    }



    private Label createMessage(Message message) {
        String userName = "Unknown";
        User user = associateUsernameWithIdMessage(message);
        if(user != null)
        {
            userName = user.getUsername();
        }
        Label label = new Label(userName + "\n"+message.getTimestamp()+"\n"+message.getContent()+"\n\n");
        label.setPrefWidth(300);

        return label;
    }


    public List<Message> getMessagesList()
    {
        return messagesList;
    }



}
