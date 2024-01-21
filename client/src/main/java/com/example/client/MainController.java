package com.example.client;

import javafx.fxml.FXML;

public class MainController
{
    @FXML
    private FooterController footerController;

    public FooterController getFooterController() {
        return footerController;
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
