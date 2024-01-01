package com.example.database.models;

/**
 * Represents the 'channels' table in the database.
 */
public class Channel implements BaseModel {

    private int channelId;
    private int serverId;
    private String name;
    private int lastMessageId;

    public Channel(int channelId, int serverId, String name, int lastMessageId)
    {
        this.channelId = channelId;
        this.serverId = serverId;
        this.name = name;
        this.lastMessageId = lastMessageId;
    }

    /**
     * Gets the unique identifier of the channel.
     *
     * @return The channel ID.
     */
    public int getChannelId() {
        return channelId;
    }

    /**
     * Sets the unique identifier of the channel.
     *
     * @param channelId The channel ID to set.
     */
    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    /**
     * Gets the unique identifier of the server to which the channel belongs.
     *
     * @return The server ID.
     */
    public int getServerId() {
        return serverId;
    }

    /**
     * Sets the unique identifier of the server to which the channel belongs.
     *
     * @param serverId The server ID to set.
     */
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    /**
     * Gets the name of the channel.
     *
     * @return The channel name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the channel.
     *
     * @param name The channel name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier of the last message sent on the channel.
     *
     * @return The last message ID.
     */
    public int getLastMessageId() {
        return lastMessageId;
    }

    /**
     * Sets the unique identifier of the last message sent on the channel.
     *
     * @param lastMessageId The last message ID to set.
     */
    public void setLastMessageId(int lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    /**
     * Gets the unique identifier of the channel.
     *
     * @return The channel ID.
     */
    public int getId() {
        return getChannelId();
    }

    /**
     * Sets the unique identifier of the channel.
     *
     * @param id The channel ID to set.
     */
    public void setId(int id) {
        setChannelId(id);
    }
}
