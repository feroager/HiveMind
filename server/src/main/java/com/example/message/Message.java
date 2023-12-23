package com.example.message;

import java.io.Serializable;

/**
 * Represents a message exchanged between the server and the client.
 * Instances of this class are used to encapsulate communication details.
 */
public class Message implements Serializable {
    /**
     * Enumerates the different types of messages.
     */
    private final MessageType type;

    /**
     * Stores the data associated with the message.
     */
    private final String data;

    /**
     * Constructs a message with the specified type and no data.
     *
     * @param type The type of the message.
     */
    public Message(MessageType type) {
        this.type = type;
        this.data = null;
    }

    /**
     * Constructs a message with the specified type and data.
     *
     * @param type The type of the message.
     * @param data The data associated with the message.
     */
    public Message(MessageType type, String data) {
        this.type = type;
        this.data = data;
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
}

