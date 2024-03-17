package com.example.server;

import com.example.database.dao.MessageDao;
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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ServerApplication class represents the main server application responsible for handling
 * client connections and communication.
 */
public class ServerApplication {
    private int port;
    private ServerSettingsController serverSettingsController;
    private static Map<User, HandlerUser> connectionMap = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(ServerApplication.class);

    /**
     * Constructs a new ServerApplication with the specified port and server settings controller.
     *
     * @param port The port number on which the server will listen for incoming connections.
     * @param serverSettingsController The controller for server settings.
     */
    public ServerApplication(int port, ServerSettingsController serverSettingsController) {
        this.port = port;
        this.serverSettingsController = serverSettingsController;
    }

    /**
     * Starts the server, listens for incoming connections, and handles them in separate threads.
     */
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

    /**
     * The Handler class represents a thread that handles communication with a specific client.
     */
    private static class Handler extends Thread {
        private Socket socket;

        /**
         * Constructs a new Handler instance with the specified socket.
         *
         * @param socket The socket representing the connection to a client.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Overrides the run method of the Thread class to handle communication with the client.
         */
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
                        logger.error("Problem with close.");
                        throw new RuntimeException(e);
                    }
                }


            }
            catch (IOException | ClassNotFoundException | SQLException e) {
                logger.error("Error while communicating with " + socket.getRemoteSocketAddress());
                logger.error("Error occurred:", e);
            }
        }

        /**
         * Handles a registration request from a client.
         *
         * @param connection The connection host for communicating with the client.
         * @param request The communication message containing the registration request.
         * @throws IOException If an I/O error occurs.
         * @throws SQLException If an SQL error occurs.
         */
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

        /**
         * Handles a login request from a client.
         *
         * @param connection The connection host for communicating with the client.
         * @param request The communication message containing the login request.
         * @throws IOException If an I/O error occurs.
         * @throws SQLException If an SQL error occurs.
         */
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
                logger.warn("Attempt to log in to an account of a user who is already logged in. User account: " + request.getUser().getUsername());
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
                logger.info(request.getUser().getUsername() + " has logged in.");
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

    /**
     * A thread handler for managing communication with a single user.
     */
    private static class HandlerUser extends Thread {
        private Socket socket;
        private ConnectionHost connectionHost;
        private User user;
        private Server serverSelected;
        private Channel channelSelected;

        /**
         * Constructs a new HandlerUser instance.
         *
         * @param socket The socket associated with the user.
         * @param connectionHost The connection host for communicating with the user.
         * @param user The user being handled.
         */
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
                        logger.info("Received message CHANNEL_LIST_REQUEST");
                        handleChannelListRequest(connectionHost, request);
                    }
                    else if(request.getType() == MessageType.MESSAGE_LIST_REQUEST)
                    {
                        logger.info("Received message MESSAGE_LIST_REQUEST");
                        handleMessageListRequest(connectionHost, request);
                    }
                    else if(request.getType() == MessageType.MESSAGE_REQUEST)
                    {
                        logger.info("Received message MESSAGE_REQUEST");
                        logger.debug("User selected server {}", (serverSelected != null) ? serverSelected.getName() : "null");
                        logger.debug("User selected channel {}", (channelSelected != null) ? channelSelected.getName() : "null");
                        handleMessageRequest(connectionHost, request);
                        logger.info("Received message CREATE_NEW_SERVER_REQUEST");
                        logger.debug("Name for the new server : {}", request.getData());
                        handleCreateNewServerRequest(connectionHost, request);
                    }
                    else if(request.getType() == MessageType.CREATE_NEW_SERVER_REQUEST)
                    {
                        logger.info("Received message CREATE_NEW_SERVER_REQUEST");
                        logger.debug("Name for the new server : {}", request.getData());
                        handleCreateNewServerRequest(connectionHost, request);
                    }
                    else if(request.getType() == MessageType.JOIN_TO_SERVER_REQUEST)
                    {
                        logger.info("Received message JOIN_TO_SERVER_REQUEST");
                        logger.debug("Received server code: {}", request.getData());
                        handleJoinToServerRequest(connectionHost, request);
                    }
                    else
                    {
                        logger.warn("Bad MessageType");
                    }

                }

            }
            catch (IOException | ClassNotFoundException  e) {
                logger.error("Error while communicating with " + socket.getRemoteSocketAddress());
                logger.error("Error occurred:", e);
            }
        }

        private void handleJoinToServerRequest(ConnectionHost connectionHost, CommunicationMessage request)
        {
            try
            {
                UserInfoRetrievalHandler userInfoRetrievalHandler = new UserInfoRetrievalHandler(DbManager.getConnection());
                Server server = null;
                server = userInfoRetrievalHandler.joinToServer(request.getData(), user.getUserId());
                userInfoRetrievalHandler.closeConnection();
                connectionHost.send(new CommunicationMessage(MessageType.JOIN_TO_SERVER_RESPONSE, server));
                logger.info("Sent JOIN_TO_SERVER_RESPONSE");

            }
            catch(SQLException | IOException e)
            {
                logger.error("Problem with handleJoinToServerRequest()");
                logger.error("Error occurred:", e);
            }
        }

        private void handleCreateNewServerRequest(ConnectionHost connectionHost, CommunicationMessage request)
        {
            try
            {
                UserInfoRetrievalHandler userInfoRetrievalHandler = new UserInfoRetrievalHandler(DbManager.getConnection());
                Server newServer = null;
                newServer = userInfoRetrievalHandler.createNewServer(request.getData(), user.getUserId());
                userInfoRetrievalHandler.closeConnection();
                connectionHost.send(new CommunicationMessage(MessageType.CREATE_NEW_SERVER_RESPONSE, newServer));
                logger.info("Sent CREATE_NEW_SERVER_RESPONSE");

            }
            catch(SQLException | IOException e)
            {
                logger.error("Problem with handleCreateNewServerRequest()");
                logger.error("Error occurred:", e);
            }

        }


        /**
         * Handles a message request from the user.
         *
         * @param connectionHost The connection host for communicating with the user.
         * @param request The communication message containing the message request.
         */
        private void handleMessageRequest(ConnectionHost connectionHost, CommunicationMessage request)
        {
            try
            {
                logger.info("User " + user.getUsername() + " wants send message.");
                MessageDao messageDao = new MessageDao(DbManager.getConnection());
                int numberNewMessage = messageDao.addMessage(request.getMessage());
                Message message = messageDao.getMessageById(numberNewMessage);
                messageDao.closeConnection();
                for (Map.Entry<User, HandlerUser> entry : connectionMap.entrySet()) {
                    HandlerUser handlerUser = entry.getValue();
                    logger.debug("Channel name user sending message: " + channelSelected.getName() + " and next logged user: " + handlerUser.channelSelected.getName());
                    if(handlerUser.channelSelected.getChannelId() == channelSelected.getChannelId())
                    {
                        handlerUser.connectionHost.send(new CommunicationMessage(MessageType.MESSAGE_RESPONSE, message, request.getUser()));
                        logger.info("A message has been sent to " + entry.getKey().getUsername());
                    }

                }

            }
            catch(SQLException | IOException e)
            {
                logger.error("Error HandleMessageRequest");
                logger.error("Error occurred:", e);
            }
        }

        /**
         * Handles a message list request from the user.
         *
         * @param connectionHost The connection host for communicating with the user.
         * @param request The communication message containing the message list request.
         */
        private void handleMessageListRequest(ConnectionHost connectionHost, CommunicationMessage request)
        {
            channelSelected = request.getChannel();
            try
            {
                UserInfoRetrievalHandler userInfoRetrievalHandler = new UserInfoRetrievalHandler(DbManager.getConnection());
                List<Message> messageList = userInfoRetrievalHandler.getUserMessageList(channelSelected);
                userInfoRetrievalHandler.closeConnection();
                connectionHost.send(new CommunicationMessage(MessageType.MESSAGE_LIST_RESPONSE, messageList, null));
                logger.info("Sent MESSAGE_LIST_RESPONSE");
            } catch(SQLException | IOException e)
            {
                logger.error("Problem with load Message list");
                logger.error("Error occurred:", e);
            }
        }

        /**
         * Handles a channel list request from the user.
         *
         * @param connectionHost The connection host for communicating with the user.
         * @param request The communication message containing the channel list request.
         */
        private void handleChannelListRequest(ConnectionHost connectionHost, CommunicationMessage request)
        {
            serverSelected = request.getServer();
            try
            {
                UserInfoRetrievalHandler userInfoRetrievalHandler = new UserInfoRetrievalHandler(DbManager.getConnection());
                List<Channel> channelList = userInfoRetrievalHandler.getUserChannelList(serverSelected);
                List<User> userList = userInfoRetrievalHandler.getListUserChoiceServer(serverSelected);
                userInfoRetrievalHandler.closeConnection();
                connectionHost.send(new CommunicationMessage(MessageType.CHANNEL_LIST_RESPONSE, channelList, userList));
                logger.info("Sent CHANNEL_LIST_RESPONSE");
            } catch(SQLException | IOException e)
            {
                logger.error("Problem with load Channel list");
                logger.error("Error occurred:", e);
            }


        }

        /**
         * Handles a logout request from the user.
         *
         * @param connectionHost The connection host for communicating with the user.
         * @param request The communication message containing the logout request.
         */
        private void handleLogutRequest(ConnectionHost connectionHost, CommunicationMessage request)
        {
            User user = request.getUser();
            if(connectionMap.containsKey(user))
            {
                logger.info(request.getUser().getUsername() + " has logged out.");
                connectionMap.remove(user);
            }
            try
            {
                connectionHost.close();
            } catch(IOException e)
            {
                logger.error("Problem with conntectionHost close.");
                throw new RuntimeException(e);
            }
        }

    }
}
