package com.hivemind.registration;

/**
 * Enum representing different registration statuses.
 */
public enum RegistrationStatus {
    /**
     * Registration was successful.
     */
    SUCCESS,

    /**
     * The chosen username is already taken.
     */
    USERNAME_TAKEN,

    /**
     * The chosen email is already taken.
     */
    EMAIL_TAKEN,

    /**
     * An error occurred while interacting with the database.
     */
    DATABASE_ERROR,

    /**
     * An internal error occurred during the registration process.
     */
    INTERNAL_ERROR
}

