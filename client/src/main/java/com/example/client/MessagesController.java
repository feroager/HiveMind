package com.example.client;

import com.example.database.models.Message;
import com.example.database.models.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * The MessagesController class controls the display of messages in the GUI.
 */
public class MessagesController
{
    private static final Logger logger = LoggerFactory.getLogger(MessagesController.class);
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
     * @param messageList The updated list of messages.
     */
    public void updateMessagesList(List<Message> messageList) {
        Platform.runLater(() -> {
            if (messageList != null) {
                this.messagesList = messageList;
                messagesContainer.getChildren().clear();

                for (Message message : messagesList) {
                    TextArea messageTextArea = createMessage(message);
                    messagesContainer.getChildren().add(messageTextArea);
                }

            } else {
                logger.info("Received null message list.");
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
     * Creates a textArea for displaying a message.
     *
     * @param message The message to be displayed.
     * @return The TextArea instance representing the message.
     */
    private TextArea createMessage(Message message)
    {
        String userName = "Unknown";
        User user = associateUsernameWithIdMessage(message);
        if (user != null) {
            userName = user.getUsername();
        }

        TextArea textArea = new TextArea(userName + "\n" + message.getTimestamp() + "\n" + message.getContent());
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setPrefWidth(Region.USE_COMPUTED_SIZE);
        textArea.setMinHeight(0);
        textArea.setPrefHeight(Region.USE_COMPUTED_SIZE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        textArea.setStyle("-fx-control-inner-background:#343541;");

        textArea.setPrefRowCount(2);

        // Listener to change the width of the TextArea
        textArea.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            // We calculate the number of lines needed to display the text
            double actualWidth = newWidth.doubleValue();
            double textWidth = textArea.getFont().getSize() * textArea.getText().length(); // Approximate text width
            int linesNeeded = (int) (Math.ceil(textWidth / actualWidth)) / 2;
            // We update the number of rows in the TextArea
            textArea.setPrefRowCount(2 + linesNeeded + 1);
        });

        Stage stage = clientHandler.getStageWithMainContoller();
        stage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (isNowMaximized)
            {
                textArea.setPrefRowCount(2);
            }
        });

        return textArea;
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

    /**
     * Add single message to messagesList and refresh GUI
     *
     * @param message The message to add to messagesList.
     */
    public void addMessageToListAndDisplay(Message message)
    {
        Platform.runLater(() -> {
            messagesList.add(message);
            updateMessagesList(this.messagesList);
            clientHandler.getMainController().scrollMessagesPaneToBottom();
        });
    }


}
