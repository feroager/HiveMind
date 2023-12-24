package com.example.message;

/**
 * The enumeration defines the basic types of messages exchanged between the server and the client
 */
public enum MessageType {
    /**
     * The server requests a login and password
     */
    PASS_REQUEST,
    /**
     * The client sends the login and password.
     */
    USER_PASS,
    /**
     * The client requests registration and sends a login and password
     */
    REGISTER_REQUESTED,
    /**
     * The server informs that login was successful
     */
    PASS_ACCEPTED,
    /**
     * The server reports that login has failed
     */
    PASS_DENIED,
    /**
     * The server informs that the registration was successful
     */
    REGISTER_ACCEPTED,
    /**
     * The server reports that registration has failed
     */
    REGISTER_DENIED,
    /**
     * Regular text message
     */
    TEXT
}
