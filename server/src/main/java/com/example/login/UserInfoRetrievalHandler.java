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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles the retrieval of user information such as servers, channels, and messages.
 */
public class UserInfoRetrievalHandler {

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
            e.printStackTrace();
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
}
