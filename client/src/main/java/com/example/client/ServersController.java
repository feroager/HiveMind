package com.example.client;

import com.example.database.models.Channel;
import com.example.database.models.Server;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

/**
 * The ServersController class controls the display and interaction of servers in the GUI.
 */
public class ServersController {
    private static final Logger logger = LoggerFactory.getLogger(ServersController.class);
    @FXML
    public Button addServerButton;
    @FXML
    private HBox serversContainer;
    private ClientHandler clientHandler;
    private ChannelsController channelsController;
    private String nameNewlyCreatedServer;

    /**
     * Initializes the list of servers.
     */
    public void initializeServersList() {
        Platform.runLater(() -> {
            addServerButton.getStylesheets().add(getClass().getResource("/com/example/styles/AddServerButtonStyle.css").toExternalForm());
            addServerButton.getStyleClass().add("add-server-button");
            addServerButton.getStyleClass().add("add-server-button:hover");
            addServerButton.getStyleClass().add("add-server-button:pressed");

            List<Server> serverList = clientHandler.getServerList();

            // Clear all children of serversContainer except the add server button
            ObservableList<Node> children = serversContainer.getChildren();
            List<Node> toRemove = new ArrayList<>();

            for (Node child : children) {
                if (!(child instanceof Button && child == addServerButton))
                {
                    toRemove.add(child);
                }
            }

            children.removeAll(toRemove);

            // Iterate through the server list and create buttons
            for (Server server : serverList) {
                Button serverButton = createServerButton(server);
                serversContainer.getChildren().add(serverButton);
            }
        });
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

    private void showDialog() {

    }

    public void showDialogServer() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Choose option");

        Label headerLabel = new Label("Decide whether you want to create a new server or join an existing one.");
        headerLabel.setStyle("-fx-text-fill: white;");
        BorderPane header = new BorderPane();
        header.setStyle("-fx-background-color: #343541;");
        header.setPadding(new Insets(10));
        header.setCenter(headerLabel);

        dialog.getDialogPane().setContent(new VBox());
        dialog.getDialogPane().setContentText(null);
        dialog.getDialogPane().setHeader(header);
        dialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.valueOf("#343541"), CornerRadii.EMPTY, Insets.EMPTY)));

        ButtonType createServerButtonType = new ButtonType("Create");
        ButtonType joinServerButtonType = new ButtonType("Join");
        dialog.getDialogPane().getButtonTypes().addAll(createServerButtonType, joinServerButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == createServerButtonType) {
                dialog.close();
                logger.info("Pressed createServerButtonType");
                showCreateServerDialog();
            } else if (buttonType == joinServerButtonType) {
                dialog.close();
                logger.info("Pressed joinServerButtonType");
                showJoinServerDialog();
            }
            return null;
        });

        dialog.show();
    }


    private void showCreateServerDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create a new server");

        Label messageLabel = new Label("Please enter the name for the new server:");
        messageLabel.setStyle("-fx-text-fill: white;");

        TextField serverNameField = new TextField();
        serverNameField.setPromptText("Enter server name");
        serverNameField.setEditable(true);
        serverNameField.setVisible(true);
        serverNameField.setManaged(true);

        VBox content = new VBox();
        content.getChildren().addAll(messageLabel, serverNameField);
        content.layout();

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.valueOf("#343541"), CornerRadii.EMPTY, Insets.EMPTY)));

        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        ButtonType backButtonType = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, backButtonType);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.get() == createButtonType)
        {
            String serverName = serverNameField.getText();
            logger.info("Pressed createButtonType");
            createNewServer(serverName);
        }
        else if(result.get() == backButtonType)
        {
            dialog.close();
            logger.info("Pressed backButtonType");
            showDialogServer();
        }
    }

    private void showJoinServerDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Join to exists server");

        Label messageLabel = new Label("Enter the server code of an existing server:");
        messageLabel.setStyle("-fx-text-fill: white;");

        TextField serverCodeField = new TextField();
        serverCodeField.setPromptText("Enter server code");
        serverCodeField.setEditable(true);
        serverCodeField.setVisible(true);
        serverCodeField.setManaged(true);

        VBox content = new VBox();
        content.getChildren().addAll(messageLabel, serverCodeField);
        content.layout();

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().setBackground(new Background(new BackgroundFill(Color.valueOf("#343541"), CornerRadii.EMPTY, Insets.EMPTY)));

        ButtonType joinButtonType = new ButtonType("Join", ButtonBar.ButtonData.OK_DONE);
        ButtonType backButtonType = new ButtonType("Back", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().addAll(joinButtonType, backButtonType);

        Optional<ButtonType> result = dialog.showAndWait();

        if(result.get() == joinButtonType)
        {
            String serverCode = serverCodeField.getText();
            logger.info("Pressed joinButtonType");
            joinNewServer(serverCode);
        }
        else if(result.get() == backButtonType)
        {
            dialog.close();
            logger.info("Pressed backButtonType");
            showDialogServer();
        }
    }

    private void joinNewServer(String serverCode) {
        logger.info("Join to server. Server code: " + serverCode);
    }

    private void createNewServer(String serverName) {
        logger.info("Creating new server: " + serverName);
        this.nameNewlyCreatedServer = serverName;
        clientHandler.setCreateNewServerRequest(true);
    }

    public String getNameNewlyCreatedServer()
    {
        return nameNewlyCreatedServer;
    }



}
