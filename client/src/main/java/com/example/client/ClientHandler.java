package com.example.client;

import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.database.models.Server;
import com.example.database.models.User;
import com.example.message.CommunicationMessage;
import com.example.message.MessageType;
import com.example.utils.ConnectionHost;
import com.example.utils.ConsoleHelper;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

class ClientHandler extends Thread {
    private String serverIp;
    private int serverPort;
    private MainController mainController;
    private FooterController footerController;
    private ServersController serversController;
    private ChannelsController channelsController;
    private MessagesController messagesController;
    private CommunicationMessage communicationMessage;
    private Socket socket;
    private ConnectionHost connectionHost;
    private volatile boolean isLogged;
    private volatile boolean isChannelsListRequest;
    private User loggedUser;
    private List<Server> userServerList;
    private Server selectedServer;
    private Channel selectedChannel;
    private List<Channel> userChannelList;
    private int testVarable = 0;

    public ClientHandler(String serverIp, int serverPort, MainController mainController, CommunicationMessage communicationMessage, Socket socket, ConnectionHost connectionHost) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.mainController = mainController;
        this.footerController = mainController.getFooterController();
        this.serversController = mainController.getServersController();
        this.channelsController = mainController.getChannelsController();
        //this.messagesController = mainController.getMessagesController();
        this.communicationMessage = communicationMessage;
        this.socket = socket;
        this.connectionHost = connectionHost;
        this.loggedUser = communicationMessage.getUser();
        this.userServerList = communicationMessage.getServerList();
        footerController.setClientHandler(this);
        footerController.setUsernameLabelFooter(communicationMessage);
        serversController.setClientHandler(this);
        serversController.setChannelsController(channelsController);
        serversController.initializeServersList();
        channelsController.setClientHandler(this);
        //messagesController.setClientHandler(this);
        //channelsController.setMessagesController(messagesController);
        isLogged = true;
        isChannelsListRequest = false;

    }

    public void handleLogout() {
        isLogged = false;
    }

    @Override
    public void run() {
        while (true) {
            if (!isLogged) {
                CommunicationMessage logOutRequest = new CommunicationMessage(MessageType.LOGOUT_REQUEST, loggedUser);
                try {
                    ConsoleHelper.writeMessage("Sent LOGOUT_REQUEST");
                    connectionHost.send(logOutRequest);
                } catch (IOException e) {
                    ConsoleHelper.writeMessage("Didn't sent LOGOUT_REQUEST");
                    System.out.println();
                    throw new RuntimeException(e);
                }

                try
                {
                    connectionHost.close();
                    socket.close();
                } catch(IOException e)
                {
                    ConsoleHelper.writeMessage("Error connectionHost or socket close.");
                    throw new RuntimeException(e);
                }
                break;
            }

            if (isChannelsListRequest)
            {
                isChannelsListRequest = false;
                CommunicationMessage userChannelListRequest = new CommunicationMessage(MessageType.CHANNEL_LIST_REQUEST, loggedUser);
                try {
                    ConsoleHelper.writeMessage("Sent LOGOUT_REQUEST");
                    CommunicationMessage communicationMessage = new CommunicationMessage(MessageType.CHANNEL_LIST_REQUEST, selectedServer);
                    connectionHost.send(communicationMessage);
                    CommunicationMessage response = connectionHost.receive();
                    if(response.getType().equals(MessageType.CHANNEL_LIST_RESPONSE))
                    {
                        serversController.handleLoaderChannels(response.getChannelList());
                    }
                    else
                    {
                        ConsoleHelper.writeMessage("Bad type CommunicationMessage");
                    }

                } catch (IOException | ClassNotFoundException e) {
                    ConsoleHelper.writeMessage("Didn't sent LOGOUT_REQUEST");
                    System.out.println();
                    throw new RuntimeException(e);
                }

            }
        }
    }


    public List<Server> getUserServerList()
    {
        return userServerList;
    }

    public List<Channel> getUserChannelList()
    {
        return userChannelList;
    }

    public void setSelectedServer(Server selectedServer)
    {
        this.selectedServer = selectedServer;
    }
    public void setChannelsListRequest(boolean channelsListRequest)
    {
        isChannelsListRequest = channelsListRequest;
    }

    public void setSelectedChannel(Channel selectedChannel)
    {
        this.selectedChannel = selectedChannel;
    }
}

