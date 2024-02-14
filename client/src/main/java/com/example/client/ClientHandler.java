package com.example.client;

import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.database.models.Server;
import com.example.database.models.User;
import com.example.message.CommunicationMessage;
import com.example.message.MessageType;
import com.example.utils.ConnectionHost;
import com.example.utils.ConsoleHelper;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.List;

public class ClientHandler extends Thread {
    private final String serverIp;
    private final int serverPort;
    private final MainController mainController;
    private final FooterController footerController;
    private final ServersController serversController;
    private final ChannelsController channelsController;
    private final MessagesController messagesController;
    private final CommunicationMessage communicationMessage;
    private final Socket socket;
    private final ConnectionHost connectionHost;
    private volatile boolean isLogged;
    private volatile boolean isChannelsListRequest;
    private volatile boolean isMessagesListRequest;
    private volatile boolean isSendMessage;
    private User loggedUser;
    private List<Server> serverList;
    private Server selectedServer;
    private Channel selectedChannel;
    private List<Channel> userChannelList;
    private List<User> serverUserList;

    public ClientHandler(String serverIp, int serverPort, MainController mainController, CommunicationMessage communicationMessage, Socket socket, ConnectionHost connectionHost) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.mainController = mainController;
        this.footerController = mainController.getFooterController();
        this.serversController = mainController.getServersController();
        this.channelsController = mainController.getChannelsController();
        this.messagesController = mainController.getMessagesController();
        this.communicationMessage = communicationMessage;
        this.socket = socket;
        this.connectionHost = connectionHost;
        this.loggedUser = communicationMessage.getUser();
        this.serverList = communicationMessage.getServerList();
        mainController.setClientHandler(this);
        footerController.setClientHandler(this);
        footerController.setUsernameLabelFooter(communicationMessage);
        serversController.setClientHandler(this);
        serversController.setChannelsController(channelsController);
        serversController.initializeServersList();
        channelsController.setClientHandler(this);
        messagesController.setClientHandler(this);
        channelsController.setMessagesController(messagesController);
        isLogged = true;
        isChannelsListRequest = false;
        isMessagesListRequest = false;
    }

    public void handleLogout() {
        isLogged = false;
        CommunicationMessage logOutRequest = new CommunicationMessage(MessageType.LOGOUT_REQUEST, loggedUser);
        try {
            ConsoleHelper.writeMessage("Sent LOGOUT_REQUEST");
            connectionHost.send(logOutRequest);
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Failed to send LOGOUT_REQUEST: " + e.getMessage());
        }

        try {
            connectionHost.close();
            socket.close();
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error closing connection: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        Thread messageReceiverThread = new Thread(() -> {
            while (isLogged) {
                try {
                    CommunicationMessage receivedMessage = connectionHost.receive();
                    handleReceivedMessage(receivedMessage);
                } catch (IOException | ClassNotFoundException e) {
                    ConsoleHelper.writeMessage("Error receiving message: " + e.getMessage());
                    // Obsługa błędu, np. ponowne połączenie z serwerem
                }
            }
        });

        messageReceiverThread.start();

        while (isLogged) {
            if (isChannelsListRequest) {
                isChannelsListRequest = false;
                sendChannelListRequest();
            }

            if (isMessagesListRequest) {
                isMessagesListRequest = false;
                sendMessagesListRequest();
            }

            if (isSendMessage) {
                isSendMessage = false;
                sendMessage();
            }
        }

        messageReceiverThread.interrupt();
    }

    private void handleReceivedMessage(CommunicationMessage receivedMessage) {
        MessageType messageType = receivedMessage.getType();
        switch (messageType) {
            case CHANNEL_LIST_RESPONSE:
                handleChannelListResponse(receivedMessage);
                break;
            case MESSAGE_LIST_RESPONSE:
                handleMessageListResponse(receivedMessage);
                break;
            case MESSAGE_RESPONSE:
                handleMessageResponse(receivedMessage);
                break;
            // Obsługa innych typów wiadomości, jeśli są potrzebne
        }
    }

    private void handleChannelListResponse(CommunicationMessage response) {
        serverUserList = response.getUserList();
        serversController.handleLoaderChannels(response.getChannelList());
    }

    private void handleMessageListResponse(CommunicationMessage response) {
        userChannelList = response.getChannelList();
        channelsController.handleLoaderChannels(response.getMessageList());
    }

    private void handleMessageResponse(CommunicationMessage response) {
        if (selectedChannel != null) {
            List<Message> messageList = response.getMessageList();
            Platform.runLater(() -> messagesController.updateMessgaesList(messageList));
        }
    }

    private void sendChannelListRequest() {
        try {
            ConsoleHelper.writeMessage("CHANNEL_LIST_REQUEST");
            CommunicationMessage userChannelListRequest = new CommunicationMessage(MessageType.CHANNEL_LIST_REQUEST, selectedServer);
            connectionHost.send(userChannelListRequest);
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error sending CHANNEL_LIST_REQUEST: " + e.getMessage());
        }
    }

    private void sendMessagesListRequest() {
        try {
            ConsoleHelper.writeMessage("MESSAGE_LIST_REQUEST");
            CommunicationMessage userMessageListRequest = new CommunicationMessage(MessageType.MESSAGE_LIST_REQUEST, selectedChannel);
            connectionHost.send(userMessageListRequest);
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error sending MESSAGE_LIST_REQUEST: " + e.getMessage());
        }
    }

    private void sendMessage() {
        try {
            System.out.println(mainController.getMessageString());
            Message message = new Message(-1, loggedUser.getUserId(), selectedChannel.getChannelId(), mainController.getMessageString(), new Timestamp(System.currentTimeMillis()));
            CommunicationMessage sendMessageRequest = new CommunicationMessage(MessageType.MESSAGE_REQUEST, message, loggedUser);
            connectionHost.send(sendMessageRequest);
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error sending MESSAGE_REQUEST: " + e.getMessage());
        }
    }

    public List<Server> getServerList() {
        return serverList;
    }

    public List<Channel> getUserChannelList() {
        return userChannelList;
    }

    public void setSelectedServer(Server selectedServer) {
        this.selectedServer = selectedServer;
    }

    public void setChannelsListRequest(boolean channelsListRequest) {
        isChannelsListRequest = channelsListRequest;
    }

    public void setSelectedChannel(Channel selectedChannel) {
        this.selectedChannel = selectedChannel;
    }

    public void setMessagesListRequest(boolean messagesListRequest) {
        isMessagesListRequest = messagesListRequest;
    }

    public List<User> getServerUserList() {
        return serverUserList;
    }

    public void setSendMessage(boolean sendMessage) {
        isSendMessage = sendMessage;
    }
}
