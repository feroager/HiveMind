package com.example.message;

/**
 * Enumeration representing message types for communication between the client and the server.
 * Each message type has an associated value that is used in the communication protocol.
 */
public enum MessageType {
    /**
     * Request for user login.
     */
    LOGIN_REQUEST("LOGIN_REQUEST"),

    /**
     * Response to a user login request.
     */
    LOGIN_RESPONSE("LOGIN_RESPONSE"),

    /**
     * Request for user registration.
     */
    REGISTER_REQUEST("REGISTER_REQUEST"),

    /**
     * Response to a user registration request.
     */
    REGISTER_RESPONSE("REGISTER_RESPONSE");

    private final String value;

    /**
     * Constructs a MessageType with the specified value.
     *
     * @param value The value associated with the message type.
     */
    MessageType(String value) {
        this.value = value;
    }

    /**
     * Gets the value associated with the message type.
     *
     * @return The value of the message type.
     */
    public String getValue() {
        return value;
    }
}

