package com.example.database.dao;

import com.example.database.models.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Server entity.
 */
public class ServerDao {
    private final Connection connection;

    /**
     * Constructs a ServerDao with a specified database connection.
     *
     * @param connection The database connection.
     */
    public ServerDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves a server by ID.
     *
     * @param serverId The ID of the server.
     * @return The server with the specified ID, or null if not found.
     */
    public Server getServerById(int serverId) {
        String sql = "SELECT * FROM servers WHERE server_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, serverId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapServerFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all servers from the database.
     *
     * @return A list of all servers.
     */
    public List<Server> getAllServers() {
        String sql = "SELECT * FROM servers";
        List<Server> servers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Server server = mapServerFromResultSet(resultSet);
                servers.add(server);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servers;
    }

    /**
     * Adds a new server to the database.
     *
     * @param server The server to add.
     * @return The ID of the newly added server, or -1 if the insertion failed.
     */
    public int addServer(Server server) {
        String sql = "INSERT INTO servers (name, admin_id, server_code) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, server.getName());
            statement.setInt(2, server.getAdminId());
            statement.setString(3, server.getServerCode()); // Zmiana na server.getServerCode()

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Updates an existing server in the database.
     *
     * @param server The server to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateServer(Server server) {
        String sql = "UPDATE servers SET name = ?, admin_id = ?, server_code = ? WHERE server_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, server.getName());
            statement.setInt(2, server.getAdminId());
            statement.setString(3, server.getServerCode()); // Zmiana na server.getServerCode()
            statement.setInt(4, server.getServerId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a server from the database.
     *
     * @param serverId The ID of the server to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteServer(int serverId) {
        String sql = "DELETE FROM servers WHERE server_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, serverId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Maps a ResultSet row to a Server object.
     *
     * @param resultSet The ResultSet containing the server data.
     * @return The Server object.
     * @throws SQLException If a database access error occurs.
     */
    private Server mapServerFromResultSet(ResultSet resultSet) throws SQLException {
        int serverId = resultSet.getInt("server_id");
        String name = resultSet.getString("name");
        int adminId = resultSet.getInt("admin_id");
        String serverCode = resultSet.getString("server_code"); // Zmiana na server_code
        Timestamp creationTimestamp = resultSet.getTimestamp("creation_timestamp");

        return new Server(serverId, name, adminId, serverCode, creationTimestamp);
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
            e.printStackTrace();
        }
    }
}
