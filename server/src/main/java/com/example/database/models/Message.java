package com.example.database.models;

import java.sql.Timestamp;

/**
 * Represents the 'messages' table in the database.
 */
public class Message implements BaseModel {

    private int messageId;
    private int userId;
    private int channelId;
    private String content;
    private Timestamp timestamp;

    public Message(int messageId, int userId, int channelId, String content, Timestamp timestamp)
    {
        this.messageId = messageId;
        this.userId = userId;
        this.channelId = channelId;
        this.content = content;
        this.timestamp = timestamp;
    }

    /**
     * Gets the unique identifier of the message.
     *
     * @return The message ID.
     */
    public int getMessageId() {
        return messageId;
    }

    /**
     * Sets the unique identifier of the message.
     *
     * @param messageId The message ID to set.
     */
    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    /**
     * Gets the unique identifier of the user who sent the message.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier of the user who sent the message.
     *
     * @param userId The user ID to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the unique identifier of the channel associated with the message.
     *
     * @return The channel ID.
     */
    public int getChannelId() {
        return channelId;
    }

    /**
     * Sets the unique identifier of the channel associated with the message.
     *
     * @param channelId The channel ID to set.
     */
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    /**
     * Gets the content of the message.
     *
     * @return The message content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the message.
     *
     * @param content The message content to set.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the timestamp indicating when the message was added.
     *
     * @return The timestamp.
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp indicating when the message was added.
     *
     * @param timestamp The timestamp to set.
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the unique identifier of the message.
     *
     * @return The message ID.
     */
    public int getId() {
        return getMessageId();
    }

    /**
     * Sets the unique identifier of the message.
     *
     * @param id The message ID to set.
     */
    public void setId(int id) {
        setMessageId(id);
    }
}
