package com.example.login;

import com.example.database.dao.ServerMembershipDao;
import com.example.database.dao.ServerDao;
import com.example.database.dao.ChannelDao;
import com.example.database.dao.MessageDao;
import com.example.database.dbutils.DbManager;
import com.example.database.models.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoRetrievalHandler {

    private final Connection connection;

    public UserInfoRetrievalHandler(Connection connection) {
        this.connection = connection;
    }

    public Map<Server, Map<Channel, List<Message>>> retrieveUserInfo(User user) throws IOException {
        ServerMembershipDao serverMembershipDao = new ServerMembershipDao(connection);
        ServerDao serverDao = new ServerDao(connection);
        ChannelDao channelDao = new ChannelDao(connection);
        MessageDao messageDao = new MessageDao(connection);

        List<ServerMembership> memberships = serverMembershipDao.getMembershipsByUserId(user.getUserId());

        Map<Server, Map<Channel, List<Message>>> userServerInfo = new HashMap<>();

        for (ServerMembership membership : memberships) {
            Server server = serverDao.getServerById(membership.getServerId());
            List<Channel> channels = channelDao.getChannelsByServerId(server.getServerId());

            Map<Channel, List<Message>> serverChannels = new HashMap<>();

            for (Channel channel : channels) {
                List<Message> messages = messageDao.getMessagesByChannelId(channel.getChannelId());
                serverChannels.put(channel, messages);
            }

            userServerInfo.put(server, serverChannels);
        }
        return userServerInfo;

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

}
