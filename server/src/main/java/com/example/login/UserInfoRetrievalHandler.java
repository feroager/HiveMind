package com.example.login;

import com.example.database.dao.ChannelDao;
import com.example.database.dao.MessageDao;
import com.example.database.dao.ServerDao;
import com.example.database.dao.ServerMembershipDao;
import com.example.database.dao.UserDao;
import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.database.models.Server;
import com.example.database.models.ServerMembership;
import com.example.database.models.User;
import com.example.registration.RegistrationHandler;
import com.example.utils.UniqueCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the retrieval of user information such as servers, channels, and messages.
 */
public class UserInfoRetrievalHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoRetrievalHandler.class);
    private final Connection connection;

    /**
     * Constructor for UserInfoRetrievalHandler.
     *
     * @param connection The database connection.
     */
    public UserInfoRetrievalHandler(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves the list of servers the user is a member of.
     *
     * @param user The user for whom to retrieve the server list.
     * @return The list of servers the user is a member of.
     * @throws IOException If an I/O error occurs.
     */
    public List<Server> getUserServerList(User user) throws IOException {
        ServerMembershipDao serverMembershipDao = new ServerMembershipDao(connection);
        ServerDao serverDao = new ServerDao(connection);

        List<ServerMembership> memberships = serverMembershipDao.getMembershipsByUserId(user.getUserId());

        List<Server> serverList = new ArrayList<>();

        for (ServerMembership membership : memberships) {
            Server server = serverDao.getServerById(membership.getServerId());
            serverList.add(server);
        }
        return serverList;
    }

    /**
     * Retrieves the list of channels in a server.
     *
     * @param server The server for which to retrieve the channel list.
     * @return The list of channels in the server.
     * @throws IOException If an I/O error occurs.
     */
    public List<Channel> getUserChannelList(Server server) throws IOException {
        ChannelDao channelDao = new ChannelDao(connection);
        return channelDao.getChannelsByServerId(server.getServerId());
    }

    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Problem with closing the database");
            logger.error("Error occurred:", e);
        }
    }

    /**
     * Retrieves the list of messages in a channel.
     *
     * @param channel The channel for which to retrieve the message list.
     * @return The list of messages in the channel.
     */
    public List<Message> getUserMessageList(Channel channel) {
        MessageDao messageDao = new MessageDao(connection);
        return messageDao.getMessagesByChannelId(channel.getChannelId());
    }

    /**
     * Retrieves the list of users in a server.
     *
     * @param serverSelected The server for which to retrieve the user list.
     * @return The list of users in the server.
     */
    public List<User> getListUserChoiceServer(Server serverSelected) {
        UserDao userDao = new UserDao(connection);
        List<User> userList = userDao.getUsersByServerId(serverSelected.getServerId());
        for (User user : userList) {
            user.setPassword(""); // Ensure passwords are not included
        }
        return userList;
    }

    public Server createNewServer(String nameNewServer, int idUserCreatingServer) throws SQLException
    {
        ServerDao serverDao = new ServerDao(connection);
        Server newServer;
        while(true)
        {
            newServer = new Server(-1, nameNewServer, idUserCreatingServer, UniqueCodeGenerator.generateUniqueCode(), null);
            if(serverDao.getServerByServerCode(newServer.getServerCode()) == null)
            {
                break;
            }
        }
        int resultAddServer = serverDao.addServer(newServer);
        if(resultAddServer == -1)
        {
            throw new SQLException();
        }

        ServerMembershipDao serverMembershipDao = new ServerMembershipDao(connection);

        ServerMembership serverMembership = new ServerMembership(-1, idUserCreatingServer, resultAddServer, "admin");

        int resultAddServerMembership = serverMembershipDao.addServerMembership(serverMembership);

        if(resultAddServerMembership == -1)
        {
            serverDao.deleteServer(resultAddServer);
            throw new SQLException();
        }

        newServer = serverDao.getServerById(resultAddServer);
        return newServer;

    }
}
