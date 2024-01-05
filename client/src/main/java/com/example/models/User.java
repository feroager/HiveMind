package com.example.models;
/**
 * Represents the 'users' table in the database.
 */
public class User implements BaseModel {

    private int userId;
    private String username;
    private String password;
    private String email;

    public User(int userId, String username, String password, String email)
    {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Gets the unique identifier of the user.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param userId The user ID to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }


    /**
     * Gets the unique identifier of the user.
     *
     * @return The user ID.
     */
    public int getId() {
        return getUserId();
    }

    /**
     * Sets the unique identifier of the user.
     *
     * @param id The user ID to set.
     */
    public void setId(int id) {
        setUserId(id);
    }

}

