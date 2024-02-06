package com.example.client;

import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.utils.ConsoleHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class ChannelsController
{
    @FXML
    private VBox channelsContainer;
    private ClientHandler clientHandler;
    private List<Channel> channelList;
    private MessagesController messagesController;

    public void setClientHandler(ClientHandler clientHandler)
    {
        this.clientHandler = clientHandler;
    }


    public void updateChannelsList(List<Channel> channelList)
    {
        Platform.runLater(() -> {
            this.channelList = channelList;
            channelsContainer.getChildren().clear();

            for(Channel channel: this.channelList)
            {
                Button channelButton = createChannelButton(channel);
                channelsContainer.getChildren().add(channelButton);
            }
        });
    }

    private Button createChannelButton(Channel channel) {
        // Create a button with the letter "S"
        Button button = new Button(channel.getName());

        // Set button size
        button.setPrefWidth(200);
        //button.setPrefHeight(60);
        button.setMinHeight(USE_COMPUTED_SIZE);
        button.setMinWidth(USE_COMPUTED_SIZE);
        button.setMaxHeight(USE_COMPUTED_SIZE);
        button.setMaxWidth(USE_COMPUTED_SIZE);


        button.setOnAction(event -> handleChannelButtonClick(channel));

        return button;
    }

    private void handleChannelButtonClick(Channel channel)
    {
        System.out.println("Button for channel clicked: " + channel.getName());

        clientHandler.setSelectedChannel(channel);
        clientHandler.setMessagessListRequest(true);
    }

    public void setMessagesController(MessagesController messagesController)
    {
        this.messagesController = messagesController;
    }

    public void handleLoaderChannels(List<Message> messageList)
    {
        ConsoleHelper.writeMessage("List of messages");
        for(Message message: messageList)
        {
            System.out.println(message.getContent());
        }

        // Update channel view
        messagesController.updateMessgaesList(messageList);
    }
}
