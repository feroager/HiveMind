package com.example.client;

import com.example.message.Message;
import com.example.utils.ConnectionHost;

import java.net.Socket;

class ClientHandler extends Thread {
    private String serverIp;
    private int serverPort;
    private MainController mainController;
    private FooterController footerController;
    private Message message;
    private Socket socket;
    private ConnectionHost connectionHost;

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
    }

    public void handleLogout() {

    }

    @Override
    public void run() {

    }
}

