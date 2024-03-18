package com.hivemind.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
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

    @FXML
    private ScrollPane messagesScrollPane;
    private Stage stage;

    public Stage getStage()
    {
        return stage;
    }

    public void setStage(Stage stage)
    {
        this.stage = stage;
    }

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
        //stage = (Stage) messagesScrollPane.getScene().getWindow();
    }

    /**
     * Sends the messages entered in the text field.
     * It sets the message string and notifies the client handler to send the message.
     */
    public void sendMessages()
    {
        this.messageString = textField.getText();
        textField.setText("");
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

    public void scrollMessagesPaneToBottom() {
        Platform.runLater(() -> {
            logger.debug((messagesScrollPane != null) ? "not null" : "null");
            if (messagesScrollPane != null) {
                double scrollPosition = messagesScrollPane.getVvalue();
                logger.debug("Aktualna pozycja przewijania: " + scrollPosition);
                messagesScrollPane.applyCss();
                messagesScrollPane.layout();
                messagesScrollPane.setVvalue(1.0);
            }
        });
    }

    public void sendMessagesIfEnter(KeyEvent keyEvent)
    {
        if(keyEvent.getCode() == KeyCode.ENTER)
        {
            sendMessages();
        }
    }


}
