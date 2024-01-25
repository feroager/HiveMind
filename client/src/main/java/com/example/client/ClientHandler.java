package com.example.client;

import com.example.message.Message;

class ClientHandler extends Thread
{

    private String serverIp;
    private int serverPort;
    private MainController mainController;
    private FooterController footerController;
    private Message message;

    public ClientHandler(String serverIp, int serverPort, MainController mainController, Message message)
    {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.mainController = mainController;
        this.footerController = mainController.getFooterController();
        this.message = message;
        footerController.setClientHandler(this);
    }

    public void handleLogout() {

    }


    @Override
    public void run()
    {
        footerController.setUsernameLabelFooter(message);
    }
}
