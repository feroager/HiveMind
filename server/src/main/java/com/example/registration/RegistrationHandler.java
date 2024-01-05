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

    private boolean isUserValid(User user) {
        return isValidUsername(user.getUsername()) && isValidEmail(user.getEmail());
    }

    private boolean isValidUsername(String username) {
        return username != null && !username.isEmpty();
    }

    private boolean isUsernameTaken(String username) {
        try {
            User existingUser = userDao.getUserByUsername(username);
            return existingUser != null;
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return true; // Assume username is taken in case of an error
        }
    }

    private boolean isEmailTaken(String email) {
        try {
            User existingUser = userDao.getUserByEmail(email);
            return existingUser != null;
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception according to your needs
            return true; // Assume email is taken in case of an error
        }
    }

    private boolean isValidEmail(String email) {
        // Implement your email validation logic here
        // You may use regular expressions or other validation methods
        // For simplicity, this example assumes any non-null and non-empty email is valid
        return email != null && !email.isEmpty();
    }

}
