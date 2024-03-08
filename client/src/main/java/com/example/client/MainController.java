package com.example.client;

import com.example.database.models.Message;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The MainController class controls the actions and events in the MainView.fxml.
 */
public class MainController
{
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);
    @FXML
    private FooterController footerController;

    @FXML
    private ServersController serversController;

    @FXML
    private ChannelsController channelsController;

    @FXML
    private MessagesController messagesController;

    @FXML
    private TextField textField;

    private String messageString;

    private ClientHandler clientHandler;

    /**
     * Retrieves the FooterController associated with this MainController.
     *
     * @return The FooterController instance.
     */
    public FooterController getFooterController() {
        return footerController;
    }

    /**
     * Retrieves the ServersController associated with this MainController.
     *
     * @return The ServersController instance.
     */
    public ServersController getServersController()
    {
        return serversController;
    }

    /**
     * Retrieves the ChannelsController associated with this MainController.
     *
     * @return The ChannelsController instance.
     */
    public ChannelsController getChannelsController()
    {
        return channelsController;
    }

    /**
     * Initializes the MainController.
     * This method is automatically called by JavaFX after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        logger.info("MainController initialization.");
        if (footerController == null) {
            logger.warn("MainController initialization.");
        } else {
            logger.warn("FooterController not is null.");
        }
    }

    /**
     * Sends the messages entered in the text field.
     * It sets the message string and notifies the client handler to send the message.
     */
    public void sendMessages()
    {
        this.messageString = textField.getText();
        this.clientHandler.setSendMessage(true);
    }

    /**
     * Retrieves the message string entered in the text field.
     *
     * @return The message string.
     */
    public String getMessageString()
    {
        return messageString;
    }

    /**
     * Sets the ClientHandler associated with this MainController.
     *
     * @param clientHandler The ClientHandler instance.
     */
    public void setClientHandler(ClientHandler clientHandler)
    {
        this.clientHandler = clientHandler;
    }

    /**
     * Retrieves the MessagesController associated with this MainController.
     *
     * @return The MessagesController instance.
     */
    public MessagesController getMessagesController()
    {
        return messagesController;
    }
}
