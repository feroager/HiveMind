package com.example.server;

import com.example.database.dao.UserDao;
import com.example.database.dbutils.DbManager;
import com.example.message.Message;
import com.example.message.MessageType;
import com.example.registration.RegistrationHandler;
import com.example.registration.RegistrationStatus;
import com.example.utils.ConnectionHost;
import com.example.utils.ConsoleHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;

public class ServerApplication {
    private int port;
    private ServerSettingsController serverSettingsController;

    public ServerApplication(int port, ServerSettingsController serverSettingsController) {
        this.port = port;
        this.serverSettingsController = serverSettingsController;
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSettingsController.serverOn();
            while (true) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.start();
            }
        } catch (Exception e) {
            serverSettingsController.serverOff();
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }
        @Override
        public void run()
        {
            try (ConnectionHost connectionHost = new ConnectionHost(socket)) {
                while (true) {
                    // Receive a message from the client
                    Message request = connectionHost.receive();

                    // Process the message based on its type
                    if (request.getType() == MessageType.LOGIN_REQUEST)
                    {
                        handleLoginRequest(connectionHost, request);
                    }
                    else if (request.getType() == MessageType.REGISTER_REQUEST) {
                        handleRegisterRequest(connectionHost, request);
                    }
                    else
                    {

                    }
                    // Add more cases for other message types as needed
                }
            }
            catch (IOException | ClassNotFoundException | SQLException e) {
                ConsoleHelper.writeMessage("Error while communicating with " + socket.getRemoteSocketAddress());
                e.printStackTrace();
            }
        }

        private void handleRegisterRequest(ConnectionHost connection, Message request) throws IOException, SQLException {
            // Perform registration using RegistrationHandler
            UserDao userDao = new UserDao(DbManager.getConnection());
            RegistrationHandler registrationHandler = new RegistrationHandler(userDao);
            RegistrationStatus registrationStatus = registrationHandler.registerUser(request.getUser());

            Message response;
            if(registrationStatus == RegistrationStatus.INTERNAL_ERROR)
            {
                response = new Message(MessageType.REGISTER_RESPONSE,"Complete your login and email address.");
            }
            else if(registrationStatus == RegistrationStatus.USERNAME_TAKEN)
            {
                response = new Message(MessageType.REGISTER_RESPONSE,"This username is already taken.");
            }
            else if(registrationStatus == RegistrationStatus.EMAIL_TAKEN)
            {
                response = new Message(MessageType.REGISTER_RESPONSE,"This email is already taken");
            }
            else if(registrationStatus == RegistrationStatus.SUCCESS)
            {
                response = new Message(MessageType.REGISTER_RESPONSE,"Registration was successful, you can log in");
            }
            else if(registrationStatus == RegistrationStatus.DATABASE_ERROR)
            {
                response = new Message(MessageType.REGISTER_RESPONSE,"Technical problems on the server side. Please try later");
                throw new SQLException();
            }
            else
            {
                throw new IOException();
            }

            // Send a response back to the client
            connection.send(response);
        }

        private void handleLoginRequest(ConnectionHost connection, Message request) throws IOException, SQLException {
            // You do later
        }

    }
}
