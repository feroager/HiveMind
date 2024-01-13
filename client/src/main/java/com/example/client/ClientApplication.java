package com.example.client;

import com.example.message.Message;
import com.example.message.MessageType;
import com.example.database.models.User;
import com.example.utils.ConnectionHost;
import com.example.utils.ConsoleHelper;

import java.io.IOException;
import java.net.Socket;

public class ClientApplication {
    private String serverIp;
    private int serverPort;

    private RegistrationController registrationController;

    public ClientApplication(String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
    }

    public void startServer() {
        try(Socket socket = new Socket(serverIp, serverPort))
        {
            try (ConnectionHost connectionHost = new ConnectionHost(socket)) {
                while (true) {

                }
            }
            catch (IOException e) {
                ConsoleHelper.writeMessage("Error while communicating with " + socket.getRemoteSocketAddress());
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) {

    }

    public void register(String username, String password, String email) {
        User registerUser = new User(0, username, password, email);
        Message registerRequest = new Message(MessageType.REGISTER_REQUEST, registerUser);

        try (Socket socket = new Socket(serverIp, serverPort)) {
            try (ConnectionHost connectionHost = new ConnectionHost(socket)) {

                connectionHost.send(registerRequest);

                Message response = connectionHost.receive();

                if (response.getType() == MessageType.REGISTER_RESPONSE) {
                    if(registrationController!=null)
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

    public void setRegistrationController(RegistrationController registrationController)
    {
        this.registrationController = registrationController;
    }
}
