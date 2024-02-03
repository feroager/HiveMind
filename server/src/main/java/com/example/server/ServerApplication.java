package com.example.server;

import com.example.database.dao.UserDao;
import com.example.database.dbutils.DbManager;
import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.database.models.Server;
import com.example.database.models.User;
import com.example.login.LoginHandler;
import com.example.login.LoginStatus;
import com.example.login.UserInfoRetrievalHandler;
import com.example.message.CommunicationMessage;
import com.example.message.MessageType;
import com.example.registration.RegistrationHandler;
import com.example.registration.RegistrationStatus;
import com.example.utils.ConnectionHost;
import com.example.utils.ConsoleHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerApplication {
    private int port;
    private ServerSettingsController serverSettingsController;
    private static Map<User, HandlerUser> connectionMap = new ConcurrentHashMap<>();

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
            try
            {
                ConnectionHost connectionHost = new ConnectionHost(socket);

                // Receive a message from the client
                CommunicationMessage request = connectionHost.receive();

                // Process the message based on its type
                if (request.getType() == MessageType.LOGIN_REQUEST)
                {
                    handleLoginRequest(connectionHost, request);
                }
                else if (request.getType() == MessageType.REGISTER_REQUEST) {
                    handleRegisterRequest(connectionHost, request);
                    try
                    {
                        socket.close();
                        connectionHost.close();
                    } catch(IOException e)
                    {
                        ConsoleHelper.writeMessage("Problem with close.");
                        throw new RuntimeException(e);
                    }
                }


            }
            catch (IOException | ClassNotFoundException | SQLException e) {
                ConsoleHelper.writeMessage("Error while communicating with " + socket.getRemoteSocketAddress());
                e.printStackTrace();
            }
        }

        private void handleRegisterRequest(ConnectionHost connection, CommunicationMessage request) throws IOException, SQLException {
            // Perform registration using RegistrationHandler
            UserDao userDao = new UserDao(DbManager.getConnection());
            RegistrationHandler registrationHandler = new RegistrationHandler(userDao);
            RegistrationStatus registrationStatus = registrationHandler.registerUser(request.getUser());

            CommunicationMessage response;
            if(registrationStatus == RegistrationStatus.INTERNAL_ERROR)
            {
                response = new CommunicationMessage(MessageType.REGISTER_RESPONSE,"Complete your login and email address.");
            }
            else if(registrationStatus == RegistrationStatus.USERNAME_TAKEN)
            {
                response = new CommunicationMessage(MessageType.REGISTER_RESPONSE,"This username is already taken.");
            }
            else if(registrationStatus == RegistrationStatus.EMAIL_TAKEN)
            {
                response = new CommunicationMessage(MessageType.REGISTER_RESPONSE,"This email is already taken");
            }
            else if(registrationStatus == RegistrationStatus.SUCCESS)
            {
                response = new CommunicationMessage(MessageType.REGISTER_RESPONSE,"Registration was successful, you can log in");
            }
            else if(registrationStatus == RegistrationStatus.DATABASE_ERROR)
            {
                response = new CommunicationMessage(MessageType.REGISTER_RESPONSE,"Technical problems on the server side. Please try later");
                throw new SQLException();
            }
            else
            {
                throw new IOException();
            }

            // Send a response back to the client
            connection.send(response);
        }

        private void handleLoginRequest(ConnectionHost connection, CommunicationMessage request) throws IOException, SQLException {
            CommunicationMessage response;

            // Perform registration using LoginHandler
            UserDao userDao = new UserDao(DbManager.getConnection());
            LoginHandler loginHandler = new LoginHandler(userDao);
            LoginStatus loginStatus = loginHandler.loginUser(request.getUser());
            userDao.closeConnection();

            if(connectionMap.containsKey(request.getUser()))
            {
                response = new CommunicationMessage(MessageType.LOGIN_RESPONSE,"This user is already logged in.");
            }
            else if(loginStatus == LoginStatus.INTERNAL_ERROR)
            {
                response = new CommunicationMessage(MessageType.LOGIN_RESPONSE,"Complete your login and password.");
            }
            else if(loginStatus == LoginStatus.USER_NOT_FOUND)
            {
                response = new CommunicationMessage(MessageType.LOGIN_RESPONSE,"Such a user does not exist.");
            }
            else if(loginStatus == LoginStatus.INVALID_PASSWORD)
            {
                response = new CommunicationMessage(MessageType.LOGIN_RESPONSE,"Invalid password");
            }
            else if(loginStatus == LoginStatus.SUCCESS)
            {
                UserDao userDaoLogin = new UserDao(DbManager.getConnection());
                User userLogin = userDaoLogin.getUserByUsername(request.getUser().getUsername());
                userDaoLogin.closeConnection();
                UserInfoRetrievalHandler userInfoRetrievalHandler = new UserInfoRetrievalHandler(DbManager.getConnection());
                List<Server> serverList = userInfoRetrievalHandler.getUserServerList(userLogin);
                userInfoRetrievalHandler.closeConnection();
                HandlerUser handlerUser = new HandlerUser(socket, connection, userLogin);
                handlerUser.start();
                connectionMap.put(userLogin, handlerUser);
                response = new CommunicationMessage(MessageType.LOGIN_RESPONSE, userLogin, serverList, "true");
                ConsoleHelper.writeMessage(request.getUser().getUsername() + " has logged in.");
            }
            else
            {
                throw new IOException();
            }

            // Send a response back to the client
            connection.send(response);
            if(!response.getData().equals("true"))
            {
                connection.close();
            }
        }

    }

    private static class HandlerUser extends Thread {
        private Socket socket;
        private ConnectionHost connectionHost;
        private User user;
        private Server serverSelected;
        private Channel channelSelected;

        public HandlerUser(Socket socket, ConnectionHost connectionHost, User user)
        {
            this.socket = socket;
            this.connectionHost = connectionHost;
            this.user = user;
        }
        @Override
        public void run()
        {
            try
            {
                while(true)
                {
                    CommunicationMessage request = connectionHost.receive();
                    if (request.getType() == MessageType.LOGOUT_REQUEST)
                    {
                        handleLogutRequest(connectionHost, request);
                        break;
                    }
                    else if(request.getType() == MessageType.CHANNEL_LIST_REQUEST)
                    {
                        ConsoleHelper.writeMessage("Recieve message CHANNEL_LIST_REQUEST");
                        handleChannelListRequest(connectionHost, request);
                    }
                    else
                    {
                        ConsoleHelper.writeMessage("Bad MessageType");
                    }
                }

            }
            catch (IOException | ClassNotFoundException  e) {
                ConsoleHelper.writeMessage("Error while communicating with " + socket.getRemoteSocketAddress());
                e.printStackTrace();
            }
        }

        private void handleChannelListRequest(ConnectionHost connectionHost, CommunicationMessage request)
        {
            serverSelected = request.getServer();
            try
            {
                UserInfoRetrievalHandler userInfoRetrievalHandler = new UserInfoRetrievalHandler(DbManager.getConnection());
                List<Channel> channelList = userInfoRetrievalHandler.getUserChannelList(serverSelected);
                userInfoRetrievalHandler.closeConnection();
                connectionHost.send(new CommunicationMessage(MessageType.CHANNEL_LIST_RESPONSE, channelList));
                ConsoleHelper.writeMessage("Sent CHANNEL_LIST_RESPONSE");
            } catch(SQLException | IOException e)
            {
                ConsoleHelper.writeMessage("Problem with load Channel list");
                e.printStackTrace();
            }


        }

        private void handleLogutRequest(ConnectionHost connectionHost, CommunicationMessage request)
        {
            User user = request.getUser();
            if(connectionMap.containsKey(user))
            {
                ConsoleHelper.writeMessage(request.getUser().getUsername() + " has logged out.");
                connectionMap.remove(user);
            }
            try
            {
                connectionHost.close();
            } catch(IOException e)
            {
                ConsoleHelper.writeMessage("Problem with conntectionHost close.");
                throw new RuntimeException(e);
            }
        }

    }
}
