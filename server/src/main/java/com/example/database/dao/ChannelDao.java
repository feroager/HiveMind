package com.example.database.dao;

import com.example.database.models.Channel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Channel entity.
 */
public class ChannelDao {
    private final Connection connection;

    /**
     * Constructs a ChannelDao with a specified database connection.
     *
     * @param connection The database connection.
     */
    public ChannelDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves a channel by ID.
     *
     * @param channelId The ID of the channel.
     * @return The channel with the specified ID, or null if not found.
     */
    public Channel getChannelById(int channelId) {
        String sql = "SELECT * FROM channels WHERE channel_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, channelId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapChannelFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all channels for a specific server from the database.
     *
     * @param serverId The ID of the server.
     * @return A list of all channels for the specified server.
     */
    public List<Channel> getChannelsByServerId(int serverId) {
        String sql = "SELECT * FROM channels WHERE server_id = ?";
        List<Channel> channels = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, serverId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Channel channel = mapChannelFromResultSet(resultSet);
                    channels.add(channel);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channels;
    }

    /**
     * Adds a new channel to the database.
     *
     * @param channel The channel to add.
     * @return The ID of the newly added channel, or -1 if the insertion failed.
     */
    public int addChannel(Channel channel) {
        String sql = "INSERT INTO channels (server_id, name, last_message_id) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, channel.getServerId());
            statement.setString(2, channel.getName());
            statement.setInt(3, channel.getLastMessageId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Updates an existing channel in the database.
     *
     * @param channel The channel to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateChannel(Channel channel) {
        String sql = "UPDATE channels SET server_id = ?, name = ?, last_message_id = ? WHERE channel_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, channel.getServerId());
            statement.setString(2, channel.getName());
            statement.setInt(3, channel.getLastMessageId());
            statement.setInt(4, channel.getChannelId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a channel from the database.
     *
     * @param channelId The ID of the channel to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteChannel(int channelId) {
        String sql = "DELETE FROM channels WHERE channel_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, channelId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Maps a ResultSet row to a Channel object.
     *
     * @param resultSet The ResultSet containing the channel data.
     * @return The Channel object.
     * @throws SQLException If a database access error occurs.
     */
    private Channel mapChannelFromResultSet(ResultSet resultSet) throws SQLException {
        int channelId = resultSet.getInt("channel_id");
        int serverId = resultSet.getInt("server_id");
        String name = resultSet.getString("name");
        int lastMessageId = resultSet.getInt("last_message_id");

        return new Channel(channelId, serverId, name, lastMessageId);
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
}