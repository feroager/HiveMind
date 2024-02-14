package com.example.client;

import com.example.database.models.Message;
import com.example.database.models.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * The MessagesController class controls the display of messages in the GUI.
 */
public class MessagesController
{
    @FXML
    private VBox messagesContainer;

    private ClientHandler clientHandler;
    private List<Message> messagesList;

    /**
     * Sets the ClientHandler associated with this MessagesController.
     *
     * @param clientHandler The ClientHandler instance.
     */
    public void setClientHandler(ClientHandler clientHandler)
    {
        this.clientHandler = clientHandler;
    }

    /**
     * Updates the list of messages displayed in the GUI.
     *
     * @param messagesList The updated list of messages.
     */
    public void updateMessagesList(List<Message> messagesList)
    {
        Platform.runLater(() -> {
            this.messagesList = messagesList;
            messagesContainer.getChildren().clear();

            for (Message message : this.messagesList)
            {
                Label messageLabel = createMessage(message);
                messagesContainer.getChildren().add(messageLabel);
            }
        });
    }

    /**
     * Associates the user ID with the corresponding username in the message.
     *
     * @param message The message containing the user ID.
     * @return The associated User instance with the username.
     */
    private User associateUsernameWithIdMessage(Message message)
    {
        for (User user : clientHandler.getServerUserList())
        {
            if (user.getUserId() == message.getUserId())
                return user;
        }
        return null;
    }

    /**
     * Creates a label for displaying a message.
     *
     * @param message The message to be displayed.
     * @return The Label instance representing the message.
     */
    private Label createMessage(Message message) {
        String userName = "Unknown";
        User user = associateUsernameWithIdMessage(message);
        if (user != null)
        {
            userName = user.getUsername();
        }
        Label label = new Label(userName + "\n" + message.getTimestamp() + "\n" + message.getContent() + "\n\n");
        label.setPrefWidth(300);
        return label;
    }

    /**
     * Retrieves the list of messages currently displayed.
     *
     * @return The list of messages.
     */
    public List<Message> getMessagesList()
    {
        return messagesList;
    }
}
