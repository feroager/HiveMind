package com.example.client;

import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.utils.ConsoleHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.List;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

/**
 * Controller class responsible for managing channels in the UI.
 */
public class ChannelsController {

    @FXML
    private VBox channelsContainer;
    private ClientHandler clientHandler;
    private List<Channel> channelList;
    private MessagesController messagesController;

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

            for (Channel channel : this.channelList) {
                Button channelButton = createChannelButton(channel);
                channelsContainer.getChildren().add(channelButton);
            }
        });
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

        button.setOnAction(event -> handleChannelButtonClick(channel));

        return button;
    }

    /**
     * Handles the click event of a channel button.
     *
     * @param channel The channel associated with the clicked button.
     */
    private void handleChannelButtonClick(Channel channel) {
        System.out.println("Button for channel clicked: " + channel.getName());

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
        ConsoleHelper.writeMessage("List of messages");
        for (Message message : messageList) {
            System.out.println(message.getContent());
        }

        messagesController.updateMessagesList(messageList);
    }
}
