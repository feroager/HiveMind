package com.example.registration;

import com.example.database.dao.UserDao;
import com.example.database.models.User;

/**
 * Handles user registration.
 */
public class RegistrationHandler {

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
            return RegistrationStatus.INTERNAL_ERROR;
        }

        if (isUsernameTaken(user.getUsername())) {
            return RegistrationStatus.USERNAME_TAKEN;
        }

        if (isEmailTaken(user.getEmail())) {
            return RegistrationStatus.EMAIL_TAKEN;
        }

        try {
            userDao.addUser(user);
            return RegistrationStatus.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your needs
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
