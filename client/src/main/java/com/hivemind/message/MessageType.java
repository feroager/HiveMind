package com.hivemind.message;

/**
 * Enumeration representing message types for communication between the client and the server.
 * Each message type has an associated value that is used in the communication protocol.
 */
public enum MessageType {
    /**
     * Request for user login.
     */
    LOGIN_REQUEST,

    /**
     * Response to a user login request.
     */
    LOGIN_RESPONSE,

    /**
     * Request for user registration.
     */
    REGISTER_REQUEST,

    /**
     * Response to a user registration request.
     */
    REGISTER_RESPONSE,

    /**
     * User logout request.
     */
    LOGOUT_REQUEST,

    /**
     * Enumeration representing a message type for requesting a list of channels.
     */
    CHANNEL_LIST_REQUEST,

    /**
     * Enumeration representing a message type for responding with a list of channels.
     */
    CHANNEL_LIST_RESPONSE,

    /**
     * Enumeration representing a message type for requesting a list of messages.
     */
    MESSAGE_LIST_REQUEST,

    /**
     * Enumeration representing a message type for responding with a list of messages.
     */
    MESSAGE_LIST_RESPONSE,

    /**
     * Representing requesting insert message in the channel.
     */
    MESSAGE_REQUEST,

    /**
     * Send new message to login users.
     */
    MESSAGE_RESPONSE,

    /**
     * Request create new server
     */
    CREATE_NEW_SERVER_REQUEST,

    /**
     * Response create new server
     */
    CREATE_NEW_SERVER_RESPONSE,

    /**
     * Request join to server
     */
    JOIN_TO_SERVER_REQUEST,

    /**
     * Response join to server
     */
    JOIN_TO_SERVER_RESPONSE,

    /**
     * Request create new channel
     */
    CREATE_NEW_CHANNEL_REQUEST,

    /**
     * Response create new channel
     */
    CREATE_NEW_CHANNEL_RESPONSE;




    /**
     * Converts the message type to its string representation.
     *
     * @return The string representation of the message type.
     */
    @Override
    public String toString() {
        return this.name(); // Returns the name of the enum constant (e.g., "LOGIN_REQUEST")
    }
}
