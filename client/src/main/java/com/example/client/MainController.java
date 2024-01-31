package com.example.client;

import javafx.fxml.FXML;

public class MainController
{
    @FXML
    private FooterController footerController;

    @FXML
    private ServersController serversController;

    @FXML
    private ChannelsController channelsController;

    public MessagesController getMessagesController()
    {
        return messagesController;
    }

    @FXML
    private MessagesController messagesController;

    public FooterController getFooterController() {
        return footerController;
    }

    public ServersController getServersController()
    {
        return serversController;
    }

    public ChannelsController getChannelsController()
    {
        return channelsController;
    }

    @FXML
    public void initialize() {
        System.out.println("MainController inicialization.");
        if (footerController == null) {
            System.out.println("FooterController is null.");
        } else {
            System.out.println("FooterController not is null.");
        }
    }



}
