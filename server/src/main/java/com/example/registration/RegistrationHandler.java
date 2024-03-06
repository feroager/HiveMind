package com.example.registration;

import com.example.database.dao.UserDao;
import com.example.database.models.User;
import com.example.login.LoginHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles user registration.
 */
public class RegistrationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationHandler.class);
    private final UserDao userDao;

    /**
     * Constructor for RegistrationHandler.
     *
     * @param userDao The UserDao instance for database interactions.
     */
    public RegistrationHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Registers a user and returns the registration status.
     *
     * @param user The user to register.
     * @return The registration status.
     */
    public RegistrationStatus registerUser(User user) {
        if (!isUserValid(user)) {
            logger.warn("An internal error occurred during the registration process.");
            return RegistrationStatus.INTERNAL_ERROR;
        }

        if (isUsernameTaken(user.getUsername())) {
            logger.warn("An attempt to create a user with a name existing in the database");
            return RegistrationStatus.USERNAME_TAKEN;
        }

        if (isEmailTaken(user.getEmail())) {
            logger.warn("An attempt to create a user with an existing email in the database");
            return RegistrationStatus.EMAIL_TAKEN;
        }

        try {
            userDao.addUser(user);
            logger.info("Created new user named " + user.getUsername());
            return RegistrationStatus.SUCCESS;
        } catch (Exception e) {
            logger.error("Database error when trying to register");
            logger.error("Error occurred:", e);
            return RegistrationStatus.DATABASE_ERROR;
        }
    }

    /**
     * Checks if the user object is valid.
     *
     * @param user The user object to validate.
     * @return True if the user is valid, false otherwise.
     */
    private boolean isUserValid(User user) {
        return isValidUsername(user.getUsername()) && isValidEmail(user.getEmail());
    }

    /**
     * Checks if the username is valid.
     *
     * @param username The username to validate.
     * @return True if the username is valid, false otherwise.
     */
    private boolean isValidUsername(String username) {
        return username != null && !username.isEmpty();
    }

    /**
     * Checks if the given username is already taken.
     *
     * @param username The username to check.
     * @return True if the username is taken, false otherwise.
     */
    private boolean isUsernameTaken(String username) {
        try {
            User existingUser = userDao.getUserByUsername(username);
            return existingUser != null;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Assume username is taken in case of an error
        }
    }

    /**
     * Checks if the given email is already taken.
     *
     * @param email The email to check.
     * @return True if the email is taken, false otherwise.
     */
    private boolean isEmailTaken(String email) {
        try {
            User existingUser = userDao.getUserByEmail(email);
            return existingUser != null;
        } catch (Exception e) {
            e.printStackTrace();
            return true; // Assume email is taken in case of an error
        }
    }

    /**
     * Checks if the email is valid.
     *
     * @param email The email to validate.
     * @return True if the email is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        return email != null && !email.isEmpty();
    }

}
