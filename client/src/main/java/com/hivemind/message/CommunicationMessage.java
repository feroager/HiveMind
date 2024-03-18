package com.hivemind.message;

import com.hivemind.database.models.Channel;
import com.hivemind.database.models.Message;
import com.hivemind.database.models.Server;
import com.hivemind.database.models.User;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a message exchanged between the server and the client.
 * Instances of this class are used to encapsulate communication details.
 */
public class CommunicationMessage implements Serializable {
    /**
     * Enumerates the different types of messages.
     */
    private MessageType type;

    /**
     * Stores the data associated with the message.
     */
    private String data;

    private List<Server> serverList;

    private List<Channel> channelList;

    private List<Message> messageList;

    private List<User> userList;
    private User user;
    private Server server;
    private Channel channel;
    private Message message;

    /**
     * Constructs a message with the specified type and no data.
     *
     * @param type The type of the message.
     */
    public CommunicationMessage(MessageType type) {
        this.type = type;
    }

    /**
     * Constructs a message with the specified type and data.
     *
     * @param type The type of the message.
     * @param data The data associated with the message.
     */
    public CommunicationMessage(MessageType type, String data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Constructs a message with the specified type, message, and user.
     *
     * @param type    The type of the message.
     * @param message The message object.
     * @param user    The user associated with the message.
     */
    public CommunicationMessage(MessageType type, Message message, User user) {
        this.type = type;
        this.message = message;
        this.user = user;
    }

    /**
     * Constructs a message with the specified type, channel list, and user list.
     *
     * @param type        The type of the message.
     * @param channelList The list of channels.
     * @param userList    The list of users.
     */
    public CommunicationMessage(MessageType type, List<Channel> channelList, List<User> userList) {
        this.type = type;
        this.channelList = channelList;
        this.userList = userList;
    }

    /**
     * Constructs a message with the specified type, message list, and message.
     *
     * @param type         The type of the message.
     * @param messageList  The list of messages.
     * @param latestMessage The latest message.
     */
    public CommunicationMessage(MessageType type, List<Message> messageList, Message latestMessage) {
        this.type = type;
        this.messageList = messageList;
        this.message = latestMessage;
    }

    /**
     * Constructs a message with the specified type and user.
     *
     * @param type The type of the message.
     * @param user The user associated with the message.
     */
    public CommunicationMessage(MessageType type, User user) {
        this.type = type;
        this.user = user;
    }

    /**
     * Constructs a message with the specified type, user, server list, and data.
     *
     * @param type       The type of the message.
     * @param user       The user associated with the message.
     * @param serverList The list of servers.
     * @param data       The data associated with the message.
     */
    public CommunicationMessage(MessageType type, User user, List<Server> serverList, String data) {
        this.type = type;
        this.user = user;
        this.serverList = serverList;
        this.data = data;
    }

    /**
     * Constructs a message with the specified type and server.
     *
     * @param type   The type of the message.
     * @param server The server associated with the message.
     */
    public CommunicationMessage(MessageType type, Server server) {
        this.type = type;
        this.server = server;
    }

    /**
     * Constructs a message with the specified type and channel.
     *
     * @param type    The type of the message.
     * @param channel The channel associated with the message.
     */
    public CommunicationMessage(MessageType type, Channel channel) {
        this.type = type;
        this.channel = channel;
    }

    /**
     * Gets the type of the message.
     *
     * @return The type of the message.
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Gets the data associated with the message.
     *
     * @return The data associated with the message.
     */
    public String getData() {
        return data;
    }

    /**
     * Gets the user associated with the message.
     *
     * @return The user associated with the message.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gets the list of servers.
     *
     * @return The list of servers.
     */
    public List<Server> getServerList() {
        return serverList;
    }

    /**
     * Gets the list of channels.
     *
     * @return The list of channels.
     */
    public List<Channel> getChannelList() {
        return channelList;
    }

    /**
     * Gets the server associated with the message.
     *
     * @return The server associated with the message.
     */
    public Server getServer() {
        return server;
    }

    /**
     * Gets the list of messages.
     *
     * @return The list of messages.
     */
    public List<Message> getMessageList() {
        return messageList;
    }

    /**
     * Gets the channel associated with the message.
     *
     * @return The channel associated with the message.
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Gets the list of users.
     *
     * @return The list of users.
     */
    public List<User> getUserList() {
        return userList;
    }

    /**
     * Gets the message object.
     *
     * @return The message object.
     */
    public Message getMessage() {
        return message;
    }
}
