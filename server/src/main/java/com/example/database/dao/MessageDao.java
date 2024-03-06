package com.example.database.dao;

import com.example.database.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Message entity.
 */
public class MessageDao {
    private static final Logger logger = LoggerFactory.getLogger(MessageDao.class);
    private final Connection connection;

    /**
     * Constructs a MessageDao with a specified database connection.
     *
     * @param connection The database connection.
     */
    public MessageDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves a message by ID.
     *
     * @param messageId The ID of the message.
     * @return The message with the specified ID, or null if not found.
     */
    public Message getMessageById(int messageId) {
        String sql = "SELECT * FROM messages WHERE message_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, messageId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapMessageFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return null;
    }

    /**
     * Retrieves all messages for a specific channel from the database.
     *
     * @param channelId The ID of the channel.
     * @return A list of all messages for the specified channel.
     */
    public List<Message> getMessagesByChannelId(int channelId) {
        String sql = "SELECT * FROM messages WHERE channel_id = ?";
        List<Message> messages = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, channelId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Message message = mapMessageFromResultSet(resultSet);
                    messages.add(message);
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return messages;
    }

    /**
     * Adds a new message to the database.
     *
     * @param message The message to add.
     * @return The ID of the newly added message, or -1 if the insertion failed.
     */
    public int addMessage(Message message) {
        String sql = "INSERT INTO messages (user_id, channel_id, content, timestamp) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, message.getUserId());
            statement.setInt(2, message.getChannelId());
            statement.setString(3, message.getContent());
            statement.setTimestamp(4, message.getTimestamp());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return -1;
    }

    /**
     * Updates an existing message in the database.
     *
     * @param message The message to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateMessage(Message message) {
        String sql = "UPDATE messages SET user_id = ?, channel_id = ?, content = ?, timestamp = ? WHERE message_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, message.getUserId());
            statement.setInt(2, message.getChannelId());
            statement.setString(3, message.getContent());
            statement.setTimestamp(4, message.getTimestamp());
            statement.setInt(5, message.getMessageId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return false;
    }

    /**
     * Deletes a message from the database.
     *
     * @param messageId The ID of the message to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteMessage(int messageId) {
        String sql = "DELETE FROM messages WHERE message_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, messageId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return false;
    }

    /**
     * Maps a ResultSet row to a Message object.
     *
     * @param resultSet The ResultSet containing the message data.
     * @return The Message object.
     * @throws SQLException If a database access error occurs.
     */
    private Message mapMessageFromResultSet(ResultSet resultSet) throws SQLException {
        int messageId = resultSet.getInt("message_id");
        int userId = resultSet.getInt("user_id");
        int channelId = resultSet.getInt("channel_id");
        String content = resultSet.getString("content");
        Timestamp timestamp = resultSet.getTimestamp("timestamp");

        return new Message(messageId, userId, channelId, content, timestamp);
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
            logger.error("Error occurred:", e);
        }
    }
}
