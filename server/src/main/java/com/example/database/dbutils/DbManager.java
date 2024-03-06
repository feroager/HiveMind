package com.example.database.dbutils;

import com.example.server.ServerApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the database connection.
 */
public class DbManager {
    private static final Logger logger = LoggerFactory.getLogger(DbManager.class);
    private static String dbUrl;
    private static String dbUsername;
    private static String dbPassword;

    private DbManager() {
        // Private constructor to ensure the class is used as a singleton.
    }

    /**
     * Initializes the database connection parameters.
     *
     * @param url      The URL of the database.
     * @param username The username for the database connection.
     * @param password The password for the database connection.
     */
    public static void initialize(String url, String username, String password) {
        dbUrl = url;
        dbUsername = username;
        dbPassword = password;
    }

    /**
     * Gets a new connection to the database based on the provided information.
     *
     * @return A new database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
        } catch (ClassNotFoundException e) {
            logger.error("Error occurred:", e);
            throw new SQLException("Database driver not found.");
        }
    }
}
