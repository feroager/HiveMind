package com.example.client;

import com.example.database.models.Channel;
import com.example.database.models.Server;
import com.example.utils.ConsoleHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

/**
 * The ServersController class controls the display and interaction of servers in the GUI.
 */
public class ServersController {
    private static final Logger logger = LoggerFactory.getLogger(ServersController.class);
    @FXML
    private HBox serversContainer;
    private ClientHandler clientHandler;
    private ChannelsController channelsController;

    /**
     * Initializes the list of servers.
     */
    public void initializeServersList() {

        List<Server> serverList = clientHandler.getServerList();

        // Iterate through the server list and create buttons
        for (Server server : serverList) {
            Button serverButton = createServerButton(server);
            serversContainer.getChildren().add(serverButton);
        }
    }

    /**
     * Creates a button for a server.
     *
     * @param server The server for which to create the button.
     * @return The created server button.
     */
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

    /**
     * Handles the click event for a server button.
     *
     * @param server The server associated with the clicked button.
     */
    private void handleServerButtonClick(Server server) {
        logger.info("Button for server clicked: " + server.getName());

        clientHandler.setSelectedServer(server);
        clientHandler.setChannelsListRequest(true);
    }

    /**
     * Handles the list of channels received from the server.
     *
     * @param channelList The list of channels received from the server.
     */
    public void handleLoaderChannels(List<Channel> channelList)
    {
        logger.info("List of channels");
        for(Channel channel: channelList)
        {
            logger.info(channel.getName());
        }

        // Update channel view
        channelsController.updateChannelsList(channelList);
    }

    /**
     * Generates a color based on the server name.
     *
     * @param serverName The name of the server.
     * @return The generated color.
     */
    private Color generateColor(String serverName) {
        // Simple algorithm to generate color based on the server name
        int hash = serverName.hashCode();
        double hue = (double) (hash & 0xFF) / 255.0;
        return Color.hsb(hue * 360, 0.8, 0.8); // Convert to color in HSB space
    }

    /**
     * Determines the text color based on the background color.
     *
     * @param backgroundColor The background color.
     * @return The text color.
     */
    private Color determineTextColor(Color backgroundColor) {
        // Check the brightness of the background color and adjust the text color
        double brightness = backgroundColor.getBrightness();
        return (brightness > 0.5) ? Color.BLACK : Color.WHITE;
    }

    /**
     * Converts a color to its RGB code.
     *
     * @param color The color to convert.
     * @return The RGB code of the color.
     */
    private String toRGBCode(Color color) {
        // Convert color to RGB code
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * Sets the client handler for communication with the server.
     *
     * @param clientHandler The client handler to set.
     */
    public void setClientHandler(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Sets the channels controller.
     *
     * @param channelsController The channels controller to set.
     */
    public void setChannelsController(ChannelsController channelsController)
    {
        this.channelsController = channelsController;
    }
}
