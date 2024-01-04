package com.example.database.dbutils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Manages the database connection.
 */
public class DbManager {
    private static Connection connection;

    private DbManager() {
        // Private constructor to ensure the class is used as a singleton.
    }

    /**
     * Gets a connection to the database based on the provided information.
     *
     * @param dbUrl      The URL of the database.
     * @param dbUsername The username for the database connection.
     * @param dbPassword The password for the database connection.
     * @return A database connection.
     * @throws SQLException If a database access error occurs.
     */
    public static Connection getConnection(String dbUrl, String dbUsername, String dbPassword) throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); // or handle it in a different way
                throw new SQLException("Database driver not found.");
            }
        }
        return connection;
    }

    /**
     * Closes the database connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // or handle it in a different way
            } finally {
                connection = null;
            }
        }
    }
}
