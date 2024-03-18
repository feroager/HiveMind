package com.hivemind.login;

/**
 * Enum representing login status.
 */
public enum LoginStatus {
    /**
     * Successful login status.
     */
    SUCCESS("Login successful."),

    /**
     * User not found during login status.
     */
    USER_NOT_FOUND("User not found."),

    /**
     * Invalid password during login status.
     */
    INVALID_PASSWORD("Invalid password."),

    /**
     * Internal error during login status.
     */
    INTERNAL_ERROR("Internal error during login.");

    private final String message;

    /**
     * Constructor for LoginStatus enum.
     *
     * @param message The message associated with the login status.
     */
    LoginStatus(String message) {
        this.message = message;
    }

    /**
     * Gets the message associated with the login status.
     *
     * @return The message.
     */
    public String getMessage() {
        return message;
    }
}

