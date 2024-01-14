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
     * @param userLoginPassword User(Login and Password).
     * @return The login status.
     */
    public LoginStatus loginUser(User userLoginPassword) {
        String username = userLoginPassword.getUsername();
        String password = userLoginPassword.getPassword();

        if (!isValidUsername(username) || !isValidPassword(password)) {
            return LoginStatus.INTERNAL_ERROR;
        }

        User user = userDao.getUserByUsername(username);

        if (user == null) {
            return LoginStatus.USER_NOT_FOUND;
        }

        if (!user.getPassword().equals(password)) {
            return LoginStatus.INVALID_PASSWORD;
        }


        return LoginStatus.SUCCESS;
    }

    private boolean isValidUsername(String username) {
        return username != null && !username.isEmpty();
    }

    private boolean isValidPassword(String password) {
        // Implement your password validation logic here
        return password != null && !password.isEmpty();
    }
}
