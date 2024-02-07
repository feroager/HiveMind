package com.example.login;

import com.example.database.dao.*;
import com.example.database.dbutils.DbManager;
import com.example.database.models.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoRetrievalHandler {

    private final Connection connection;

    public UserInfoRetrievalHandler(Connection connection) {
        this.connection = connection;
    }

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

    public List<Channel> getUserChannelList(Server server) throws IOException {
        ChannelDao channelDao = new ChannelDao(connection);

        List<Channel> channelList = channelDao.getChannelsByServerId(server.getServerId());

        return channelList;

    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getUserMessageList(Channel channel)
    {
        MessageDao messageDao = new MessageDao(connection);

        List<Message> messageList = messageDao.getMessagesByChannelId(channel.getChannelId());

        return messageList;

    }

    public List<User> getListUserChoiceServer(Server serverSelected)
    {
        UserDao userDao = new UserDao(connection);

        List<User> userList = userDao.getUsersByServerId(serverSelected.getServerId());
        for(User user : userList)
        {
            user.setPassword("");
        }

        return userList;

    }
}
