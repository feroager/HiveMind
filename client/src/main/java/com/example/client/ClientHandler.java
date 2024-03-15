package com.example.client;

import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.database.models.Server;
import com.example.database.models.User;
import com.example.message.CommunicationMessage;
import com.example.message.MessageType;
import com.example.utils.ConnectionHost;
import com.example.utils.ConsoleHelper;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.List;

/**
 * The ClientHandler class manages communication between the client and the server.
 * It handles sending and receiving messages, as well as processing responses from the server.
 */
public class ClientHandler extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
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
    private volatile boolean isCreateNewServerRequest;
    private User loggedUser;
    private List<Server> serverList;
    private Server selectedServer;
    private Channel selectedChannel;
    private List<Channel> userChannelList;
    private List<User> serverUserList;


    /**
     * Constructs a new ClientHandler instance.
     *
     * @param serverIp             The IP address of the server
     * @param serverPort           The port number of the server
     * @param mainController       The main controller of the client application
     * @param communicationMessage The initial communication message
     * @param socket               The socket for communication with the server
     * @param connectionHost       The connection host for sending and receiving messages
     */
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
        isCreateNewServerRequest = false;
    }

    /**
     * Handles the logout process by sending a logout request to the server.
     */
    public void handleLogout() {
        isLogged = false;
        CommunicationMessage logOutRequest = new CommunicationMessage(MessageType.LOGOUT_REQUEST, loggedUser);
        try {
            connectionHost.send(logOutRequest);
            logger.info("Sent LOGOUT_REQUEST");
        } catch (IOException e) {
            logger.warn("Failed to send LOGOUT_REQUEST: " + e.getMessage());
        }

        try {
            connectionHost.close();
            socket.close();
        } catch (IOException e) {
            logger.warn("Error closing connection: " + e.getMessage());
        }
    }

    /**
     * Runs the ClientHandler thread.
     * It manages the communication with the server and handles incoming messages.
     */
    @Override
    public void run() {
        Thread messageReceiverThread = new Thread(() -> {
            while (isLogged) {
                try {
                    CommunicationMessage receivedMessage = connectionHost.receive();
                    handleReceivedMessage(receivedMessage);
                } catch (IOException | ClassNotFoundException e) {
                    logger.warn("Error receiving message: " + e.getMessage());
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

            if (isCreateNewServerRequest) {
                isCreateNewServerRequest = false;
                sendCreateNewServerRequest();
            }
        }

        messageReceiverThread.interrupt();
    }

    /**
     * Handles a received message from the server.
     *
     * @param receivedMessage The received CommunicationMessage
     */
    private void handleReceivedMessage(CommunicationMessage receivedMessage) {
        MessageType messageType = receivedMessage.getType();
        switch (messageType) {
            case CHANNEL_LIST_RESPONSE:
                logger.info("Receive CHANNEL_LIST_RESPONSE");
                handleChannelListResponse(receivedMessage);
                break;
            case MESSAGE_LIST_RESPONSE:
                logger.info("Receive MESSAGE_LIST_RESPONSE");
                handleMessageListResponse(receivedMessage);
                break;
            case MESSAGE_RESPONSE:
                logger.info("Receive MESSAGE_RESPONSE");
                handleMessageResponse(receivedMessage);
                break;
            case CREATE_NEW_SERVER_RESPONSE:
                logger.info("Receive CREATE_NEW_SERVER_RESPONSE");
                handleCreateNewServerResponse(receivedMessage);
                break;
            // Handle other message types if needed
        }
    }

    private void handleCreateNewServerResponse(CommunicationMessage receivedMessage)
    {
        if(receivedMessage.getServer() != null)
        {
            logger.debug("The name new created server: {}", receivedMessage.getServer().getName());
            serverList.add(receivedMessage.getServer());
            serversController.initializeServersList();
            logger.info("Added new Server to serverList.");
        }
    }

    /**
     * Handles a response containing a list of channels from the server.
     *
     * @param response The received CommunicationMessage containing the channel list
     */
    private void handleChannelListResponse(CommunicationMessage response) {
        serverUserList = response.getUserList();
        serversController.handleLoaderChannels(response.getChannelList());
    }

    /**
     * Handles a response containing a list of messages from the server.
     *
     * @param response The received CommunicationMessage containing the message list
     */
    private void handleMessageListResponse(CommunicationMessage response) {
        userChannelList = response.getChannelList();
        channelsController.handleLoaderChannels(response.getMessageList());
    }

    /**
     * Handles a response containing a single message from the server.
     *
     * @param response The received CommunicationMessage containing the message
     */
    private void handleMessageResponse(CommunicationMessage response) {
        if (selectedChannel != null) {
            Message message = response.getMessage();
            messagesController.addMessageToListAndDisplay(message);
            mainController.scrollMessagesPaneToBottom();
        }
    }


    /**
     * Sends a request to the server to retrieve the list of channels.
     */
    private void sendChannelListRequest() {
        try {
            CommunicationMessage userChannelListRequest = new CommunicationMessage(MessageType.CHANNEL_LIST_REQUEST, selectedServer);
            connectionHost.send(userChannelListRequest);
            logger.info("Sent CHANNEL_LIST_REQUEST");
        } catch (IOException e) {
            logger.error("Error sending CHANNEL_LIST_REQUEST");
            logger.error("Error occurred:", e);
        }
    }

    /**
     * Sends a request to the server to retrieve the list of messages for the selected channel.
     */
    private void sendMessagesListRequest() {
        try {
            CommunicationMessage userMessageListRequest = new CommunicationMessage(MessageType.MESSAGE_LIST_REQUEST, selectedChannel);
            connectionHost.send(userMessageListRequest);
            logger.info("Sent MESSAGE_LIST_REQUEST");
        } catch (IOException e) {
            logger.error("Error sending MESSAGE_LIST_REQUEST");
            logger.error("Error occurred:", e);
        }
    }

    /**
     * Sends a message to the server.
     */
    private void sendMessage() {
        try {
            logger.info("Attempt to send a message with the following content: " + mainController.getMessageString());
            Message message = new Message(-1, loggedUser.getUserId(), selectedChannel.getChannelId(), mainController.getMessageString(), new Timestamp(System.currentTimeMillis()));
            CommunicationMessage sendMessageRequest = new CommunicationMessage(MessageType.MESSAGE_REQUEST, message, loggedUser);
            connectionHost.send(sendMessageRequest);
            logger.info("Sent MESSAGE_REQUEST");
        } catch (IOException e) {
            logger.error("Error sending MESSAGE_REQUEST");
            logger.error("Error occurred:", e);
        }
    }

    private void sendCreateNewServerRequest()
    {
        try {
            CommunicationMessage sendCreateNewServerRequest = new CommunicationMessage(MessageType.CREATE_NEW_SERVER_REQUEST, serversController.getNameNewlyCreatedServer());
            connectionHost.send(sendCreateNewServerRequest);
            logger.info("Sent CREATE_NEW_SERVER_REQUEST");
        } catch (IOException e) {
            logger.error("Error sending CREATE_NEW_SERVER_REQUEST");
            logger.error("Error occurred:", e);
        }
    }

    /**
     * Retrieves the list of servers.
     *
     * @return The list of servers
     */
    public List<Server> getServerList() {
        return serverList;
    }

    /**
     * Retrieves the list of user channels.
     *
     * @return The list of user channels
     */
    public List<Channel> getUserChannelList() {
        return userChannelList;
    }

    /**
     * Sets the selected server.
     *
     * @param selectedServer The selected server
     */
    public void setSelectedServer(Server selectedServer) {
        this.selectedServer = selectedServer;
    }

    /**
     * Sets the flag indicating whether a request for channel list is needed.
     *
     * @param channelsListRequest true if a request for channel list is needed, false otherwise
     */
    public void setChannelsListRequest(boolean channelsListRequest) {
        isChannelsListRequest = channelsListRequest;
    }

    /**
     * Sets the selected channel.
     *
     * @param selectedChannel The selected channel
     */
    public void setSelectedChannel(Channel selectedChannel) {
        this.selectedChannel = selectedChannel;
    }

    /**
     * Sets the flag indicating whether a request for message list is needed.
     *
     * @param messagesListRequest true if a request for message list is needed, false otherwise
     */
    public void setMessagesListRequest(boolean messagesListRequest) {
        isMessagesListRequest = messagesListRequest;
    }

    /**
     * Retrieves the list of users in the server.
     *
     * @return The list of users in the server
     */
    public List<User> getServerUserList() {
        return serverUserList;
    }

    /**
     * Sets the flag indicating whether a message needs to be sent.
     *
     * @param sendMessage true if a message needs to be sent, false otherwise
     */
    public void setSendMessage(boolean sendMessage) {
        isSendMessage = sendMessage;
    }

    public MainController getMainController()
    {
        return mainController;
    }

    public void setCreateNewServerRequest(boolean createNewServerRequest)
    {
        isCreateNewServerRequest = createNewServerRequest;
    }

    public Stage getStageWithMainContoller()
    {
        return mainController.getStage();
    }
}
