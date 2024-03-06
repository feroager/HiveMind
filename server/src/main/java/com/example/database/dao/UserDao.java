package com.example.database.dao;

import com.example.database.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User entity.
 */
public class UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    private final Connection connection;

    /**
     * Constructs a UserDao with a specified database connection.
     *
     * @param connection The database connection.
     */
    public UserDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves a user by ID.
     *
     * @param userId The ID of the user.
     * @return The user with the specified ID, or null if not found.
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return null;
    }

    /**
     * Retrieves a user by username.
     *
     * @param username The username of the user.
     * @return The user with the specified username, or null if not found.
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return null;
    }

    /**
     * Retrieves a user by email.
     *
     * @param email The email address of the user.
     * @return The user with the specified email, or null if not found.
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return null;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return A list of all users.
     */
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = mapUserFromResultSet(resultSet);
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return users;
    }

    /**
     * Adds a new user to the database.
     *
     * @param user The user to add.
     * @return The ID of the newly added user, or -1 if the insertion failed.
     */
    public int addUser(User user) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return -1;
    }

    /**
     * Updates an existing user in the database.
     *
     * @param user The user to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getEmail());
            statement.setInt(4, user.getUserId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return false;
    }

    /**
     * Deletes a user from the database.
     *
     * @param userId The ID of the user to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return false;
    }

    /**
     * Maps a ResultSet row to a User object.
     *
     * @param resultSet The ResultSet containing the user data.
     * @return The User object.
     * @throws SQLException If a database access error occurs.
     */
    private User mapUserFromResultSet(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("user_id");
        String username = resultSet.getString("username");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");

        return new User(userId, username, password, email);
    }

    /**
     * Retrieves all users belonging to a specific server from the database.
     *
     * @param serverId The ID of the server.
     * @return A list of users belonging to the specified server.
     */
    public List<User> getUsersByServerId(int serverId) {
        String sql = "SELECT u.user_id, u.username, u.password, u.email " +
                "FROM users u " +
                "INNER JOIN server_memberships sm ON u.user_id = sm.user_id " +
                "WHERE sm.server_id = ?";
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, serverId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = mapUserFromResultSet(resultSet);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
        return users;
    }


    /**
     * Closes the database connection.
     */
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            logger.error("Error occurred:", e);
        }
    }
}
