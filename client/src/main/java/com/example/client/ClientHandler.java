package com.example.client;

import com.example.database.models.User;
import com.example.message.Message;
import com.example.message.MessageType;
import com.example.utils.ConnectionHost;
import com.example.utils.ConsoleHelper;

import java.io.IOException;
import java.net.Socket;

class ClientHandler extends Thread {
    private String serverIp;
    private int serverPort;
    private MainController mainController;
    private FooterController footerController;
    private Message message;
    private Socket socket;
    private ConnectionHost connectionHost;
    private volatile boolean isLogged;
    private User loggedUser;

    private int testVarable = 0;

    public ClientHandler(String serverIp, int serverPort, MainController mainController, Message message, Socket socket, ConnectionHost connectionHost) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.mainController = mainController;
        this.footerController = mainController.getFooterController();
        this.message = message;
        this.socket = socket;
        this.connectionHost = connectionHost;
        footerController.setClientHandler(this);
        footerController.setUsernameLabelFooter(message);
        isLogged = true;
        loggedUser = message.getUser();
    }

    public void handleLogout() {
        isLogged = false;
    }

    @Override
    public void run() {
        while (true) {
            if (!isLogged) {
                Message logOutRequest = new Message(MessageType.LOGOUT_REQUEST, loggedUser);
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
        }
    }


}
