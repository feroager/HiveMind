package com.example.client;

import javafx.fxml.FXML;

public class MainController
{
    @FXML
    private FooterController footerController;

    @FXML
    private ServersController serversController;

    public FooterController getFooterController() {
        return footerController;
    }

    public ServersController getServersController()
    {
        return serversController;
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
