package com.example.message;

import com.example.database.models.Channel;
import com.example.database.models.Message;
import com.example.database.models.Server;
import com.example.database.models.User;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private User user;
    private Server server;
    private  Channel channel;

    /**
     * Constructs a message with the specified type and no data.
     *
     * @param type The type of the message.
     */
    public CommunicationMessage(MessageType type) {
        this.type = type;
    }

    public CommunicationMessage(MessageType type, List<Channel> channelList) {
        this.type = type;
        this.channelList = channelList;
    }

    public CommunicationMessage(MessageType type, List<Message> messageList, Message message) {
        this.type = type;
        this.messageList = messageList;
    }

    public CommunicationMessage(MessageType type, User user)
    {
        this.type = type;
        this.user = user;
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


    public CommunicationMessage(MessageType type, User user, String data)
    {
        this.type = type;
        this.user = user;
        this.data = data;
    }

    public CommunicationMessage(MessageType type, User user, List<Server> serverList, String data) {
        this.type = type;
        this.user = user;
        this.data = data;
        this.serverList = serverList;
    }

    public CommunicationMessage(MessageType messageType, Server server)
    {
        this.type = messageType;
        this.server = server;
    }

    public CommunicationMessage(MessageType messageType, Channel channel)
    {
        this.type = messageType;
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

    public User getUser()
    {
        return user;
    }


    public List<Server> getServerList()
    {
        return serverList;
    }

    public List<Channel> getChannelList()
    {
        return channelList;
    }

    public Server getServer()
    {
        return server;
    }
}

