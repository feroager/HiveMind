package com.example.login;

import com.example.database.dao.UserDao;
import com.example.database.models.User;

/**
 * Handles user login.
 */
public class LoginHandler {

    private final UserDao userDao;

    /**
     * Constructor for LoginHandler.
     *
     * @param userDao The UserDao instance for database interactions.
     */
    public LoginHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Logs in a user and returns the login status.
     *
     * @param user The user object containing login credentials.
     * @return The login status.
     */
    public LoginStatus loginUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        if (!isValidUsername(username) || !isValidPassword(password)) {
            return LoginStatus.INTERNAL_ERROR;
        }

        User retrievedUser = userDao.getUserByUsername(username);

        if (retrievedUser == null) {
            return LoginStatus.USER_NOT_FOUND;
        }

        if (!retrievedUser.getPassword().equals(password)) {
            return LoginStatus.INVALID_PASSWORD;
        }

        return LoginStatus.SUCCESS;
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
     * Checks if the password is valid.
     *
     * @param password The password to validate.
     * @return True if the password is valid, false otherwise.
     */
    private boolean isValidPassword(String password) {
        // Implement your password validation logic here
        return password != null && !password.isEmpty();
    }
}
