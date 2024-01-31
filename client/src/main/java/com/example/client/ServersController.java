package com.example.client;

import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.database.models.Server;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

public class ServersController {

    @FXML
    private HBox serversContainer;
    private ClientHandler clientHandler;
    private ChannelsController channelsController;

    public void initializeServersList() {

        List<Server> serverList = clientHandler.getUserServerList();

        // Iterate through the server list and create buttons
        for (Server server : serverList) {
            Button serverButton = createServerButton(server);
            serversContainer.getChildren().add(serverButton);
        }
    }

    private Button createServerButton(Server server) {
        // Create a button with the letter "S"
        Button button = new Button("S");

        // Generate color based on the server name
        Color backgroundColor = generateColor(server.getName());

        // Set the button's background
        button.setStyle("-fx-background-color: " + toRGBCode(backgroundColor) + ";");

        // Set font properties
        Color textColor = determineTextColor(backgroundColor);
        Font font = Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 30);
        button.setFont(font);
        button.setTextFill(textColor);

        // Set button size
        button.setPrefWidth(60);
        button.setPrefHeight(60);
        button.setMinHeight(USE_COMPUTED_SIZE);
        button.setMinWidth(USE_COMPUTED_SIZE);
        button.setMaxHeight(USE_COMPUTED_SIZE);
        button.setMaxWidth(USE_COMPUTED_SIZE);

        // Set tooltip
        Tooltip tooltip = new Tooltip(server.getName());
        button.setTooltip(tooltip);

        button.setOnAction(event -> handleServerButtonClick(server));

        return button;
    }

    private void handleServerButtonClick(Server server) {

        System.out.println("Button for server clicked: " + server.getName());

        // Download the channel list for a given server
        //Map<Channel, List<Message>> channelsForServer = getChannelsForServer(server);

        // Update channel view
        // channelsController.updateChannelsList(channelsForServer);
    }

//    private Map<Channel, List<Message>> getChannelsForServer(Server server) {
//        Map<Channel, List<Message>> help = clientHandler.getUserServerInfo().get(server);
//        return help;
//    }


    private Color generateColor(String serverName) {
        // Simple algorithm to generate color based on the server name
        int hash = serverName.hashCode();
        double hue = (double) (hash & 0xFF) / 255.0;
        return Color.hsb(hue * 360, 0.8, 0.8); // Convert to color in HSB space
    }

    private Color determineTextColor(Color backgroundColor) {
        // Check the brightness of the background color and adjust the text color
        double brightness = backgroundColor.getBrightness();
        return (brightness > 0.5) ? Color.BLACK : Color.WHITE;
    }

    private String toRGBCode(Color color) {
        // Convert color to RGB code
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public void setChannelsController(ChannelsController channelsController)
    {
        this.channelsController = channelsController;
    }
}
