package com.hivemind.client;

import com.hivemind.database.models.Channel;
import com.hivemind.database.models.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

/**
 * Controller class responsible for managing channels in the UI.
 */
public class ChannelsController {
    private static final Logger logger = LoggerFactory.getLogger(ChannelsController.class);
    @FXML
    private VBox channelsContainer;
    private ClientHandler clientHandler;
    private List<Channel> channelList;
    private MessagesController messagesController;
    private String nameNewlyCreatedChannel;

    /**
     * Sets the client handler for this controller.
     *
     * @param clientHandler The client handler to be set.
     */
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Updates the list of channels displayed in the UI.
     *
     * @param channelList The updated list of channels.
     */
    public void updateChannelsList(List<Channel> channelList) {
        Platform.runLater(() -> {
            this.channelList = channelList;
            channelsContainer.getChildren().clear();

            Button addChannelButton = new Button("Add channel");
            addChannelButton.setPrefWidth(200);
            addChannelButton.setMinHeight(USE_COMPUTED_SIZE);
            addChannelButton.setMinWidth(USE_COMPUTED_SIZE);
            addChannelButton.setMaxHeight(USE_COMPUTED_SIZE);
            addChannelButton.setMaxWidth(USE_COMPUTED_SIZE);
            addChannelButton.setOnAction(event -> showCreateChannelDialog());

            addChannelButton.getStylesheets().add(getClass().getResource("/com/hivemind/styles/AddChannelButtonStyle.css").toExternalForm());
            addChannelButton.getStyleClass().add("add-channel-button");
            addChannelButton.getStyleClass().add("add-channel-button:hover");
            addChannelButton.getStyleClass().add("add-channel-button:pressed");

            channelsContainer.getChildren().add(addChannelButton);

            for (Channel channel : this.channelList) {
                Button channelButton = createChannelButton(channel);
                channelsContainer.getChildren().add(channelButton);
            }
        });
    }

    private void showCreateChannelDialog()
    {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create a new channel");

        Label messageLabel = new Label("Please enter the name for the new channel:");
        messageLabel.setStyle("-fx-text-fill: white;");

        TextField chamnnelNameField = new TextField();
        chamnnelNameField.setPromptText("Enter channel name");
        chamnnelNameField.setEditable(true);
        chamnnelNameField.setVisible(true);
        chamnnelNameField.setManaged(true);

        VBox content = new VBox();
        content.getChildren().addAll(messageLabel, chamnnelNameField);
        content.layout();

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.valueOf("#343541"), CornerRadii.EMPTY, Insets.EMPTY)));

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        ButtonType backButtonType = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, backButtonType);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.get() == createButtonType)
        {
            String channelName = chamnnelNameField.getText();
            logger.info("Pressed createButtonType");
            createNewChannel(channelName);
        }
        else if(result.get() == backButtonType)
        {
            dialog.close();
            logger.info("Pressed backButtonType");
        }
    }

    private void createNewChannel(String channelName)
    {
        logger.info("Creating new channel: " + channelName);
        this.nameNewlyCreatedChannel = channelName;
        clientHandler.setCreateNewChannelRequest(true);
    }

    /**
     * Creates a button for the given channel.
     *
     * @param channel The channel for which the button is created.
     * @return The created button.
     */
    private Button createChannelButton(Channel channel) {
        Button button = new Button(channel.getName());

        button.setPrefWidth(200);
        button.setMinHeight(USE_COMPUTED_SIZE);
        button.setMinWidth(USE_COMPUTED_SIZE);
        button.setMaxHeight(USE_COMPUTED_SIZE);
        button.setMaxWidth(USE_COMPUTED_SIZE);
        button.getStylesheets().add(getClass().getResource("/com/hivemind/styles/ChannelButtonStyle.css").toExternalForm());
        button.getStyleClass().add("channel-button");
        button.getStyleClass().add("channel-button:hover");
        button.getStyleClass().add("channel-button:pressed");

        button.setOnAction(event -> handleChannelButtonClick(channel));

        return button;
    }

    /**
     * Handles the click event of a channel button.
     *
     * @param channel The channel associated with the clicked button.
     */
    private void handleChannelButtonClick(Channel channel) {
        logger.info("Button for channel clicked: " + channel.getName());

        clientHandler.setSelectedChannel(channel);
        clientHandler.setMessagesListRequest(true);
    }

    /**
     * Sets the messages controller for this controller.
     *
     * @param messagesController The messages controller to be set.
     */
    public void setMessagesController(MessagesController messagesController) {
        this.messagesController = messagesController;
    }

    /**
     * Handles the loading of messages for a selected channel.
     *
     * @param messageList The list of messages to be loaded.
     */
    public void handleLoaderChannels(List<Message> messageList) {
        Platform.runLater(() -> {
            logger.info("List of messages");
            for (Message message : messageList) {
                logger.info(message.getContent());
            }

            messagesController.updateMessagesList(messageList);
        });

    }

    public String getNameNewlyCreatedChannel()
    {
        return nameNewlyCreatedChannel;
    }
}
