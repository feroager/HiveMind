package com.example.client;

import com.example.message.CommunicationMessage;
import com.example.database.models.User;
import com.example.message.MessageType;
import com.example.utils.ConnectionHost;
import com.example.utils.ConsoleHelper;

import java.io.IOException;
import java.net.Socket;

/**
 * The main application class responsible for client-server communication and user interaction.
 */
public class ClientApplication {
    private String serverIp;
    private int serverPort;
    private RegistrationController registrationController;
    private LoginController loginController;

    /**
     * Constructs a new ClientApplication with the specified server IP address and port.
     *
     * @param serverIp   The IP address of the server.
     * @param serverPort The port number of the server.
     */
    public ClientApplication(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }


    /**
     * Logs in the user with the provided username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public void login(String username, String password) {
        User loginUser = new User(0, username, password, null);
        CommunicationMessage loginRequest = new CommunicationMessage(MessageType.LOGIN_REQUEST, loginUser);

        Socket socket = null;
        ConnectionHost connectionHost = null;

        try {
            socket = new Socket(serverIp, serverPort);
            connectionHost = new ConnectionHost(socket);

            connectionHost.send(loginRequest);

            CommunicationMessage response = connectionHost.receive();

            if (response.getType() == MessageType.LOGIN_RESPONSE) {
                if (loginController != null && Boolean.parseBoolean(response.getData())) {
                    ConsoleHelper.writeMessage("Login");
                    loginController.setMainView();
                    MainController mainController = loginController.getMainController();
                    if (mainController == null)
                        System.out.println("mainContorller is null");
                    for (var helpMe : response.getServerList()) {
                        System.out.println(helpMe.getName());
                    }
                    new ClientHandler(serverIp, serverPort, mainController, response, socket, connectionHost).start();
                } else if (loginController != null) {
                    loginController.setResultLabelLogin(response);
                } else {
                    ConsoleHelper.writeMessage("LoginContoller error.");
                }
            } else {
                ConsoleHelper.writeMessage("Unexpected response type from the server.");
            }
        } catch (IOException | ClassNotFoundException e) {
            ConsoleHelper.writeMessage("Error while communicating with " + (socket != null ? socket.getRemoteSocketAddress() : "unknown"));
            e.printStackTrace();
        }
    }


    /**
     * Registers a new user with the provided username, password, and email.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param email    The email of the new user.
     */
    public void register(String username, String password, String email) {
        User registerUser = new User(0, username, password, email);
        CommunicationMessage registerRequest = new CommunicationMessage(MessageType.REGISTER_REQUEST, registerUser);

        try (Socket socket = new Socket(serverIp, serverPort)) {
            try (ConnectionHost connectionHost = new ConnectionHost(socket)) {

                connectionHost.send(registerRequest);

                CommunicationMessage response = connectionHost.receive();

                if (response.getType() == MessageType.REGISTER_RESPONSE) {
                    if (registrationController != null)
                        registrationController.setResultLabelRegistartionText(response);
                    ConsoleHelper.writeMessage(response.getData());
                } else {
                    ConsoleHelper.writeMessage("Unexpected response type from the server.");
                }
            } catch (IOException | ClassNotFoundException e) {
                ConsoleHelper.writeMessage("Error while communicating with " + socket.getRemoteSocketAddress());
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the registration controller for this client application.
     *
     * @param registrationController The registration controller to be set.
     */
    public void setRegistrationController(RegistrationController registrationController) {
        this.registrationController = registrationController;
    }

    /**
     * Sets the login controller for this client application.
     *
     * @param loginController The login controller to be set.
     */
    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }
}
