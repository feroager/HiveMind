package com.example.database.dao;

import com.example.database.models.ServerMembership;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for ServerMembership entity.
 */
public class ServerMembershipDao {
    private final Connection connection;

    /**
     * Constructs a ServerMembershipDao with a specified database connection.
     *
     * @param connection The database connection.
     */
    public ServerMembershipDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Retrieves a server membership by ID.
     *
     * @param membershipId The ID of the server membership.
     * @return The server membership with the specified ID, or null if not found.
     */
    public ServerMembership getServerMembershipById(int membershipId) {
        String sql = "SELECT * FROM server_memberships WHERE membership_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, membershipId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapServerMembershipFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all server memberships for a specific user from the database.
     *
     * @param userId The ID of the user.
     * @return A list of all server memberships for the specified user.
     */
    public List<ServerMembership> getMembershipsByUserId(int userId) {
        String sql = "SELECT * FROM server_memberships WHERE user_id = ?";
        List<ServerMembership> serverMemberships = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ServerMembership serverMembership = mapServerMembershipFromResultSet(resultSet);
                    serverMemberships.add(serverMembership);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serverMemberships;
    }



    /**
     * Retrieves all server memberships for a specific user from the database.
     *
     * @param userId The ID of the user.
     * @return A list of all server memberships for the specified user.
     */
    public List<ServerMembership> getServerMembershipsByUserId(int userId) {
        String sql = "SELECT * FROM server_memberships WHERE user_id = ?";
        List<ServerMembership> serverMemberships = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ServerMembership serverMembership = mapServerMembershipFromResultSet(resultSet);
                    serverMemberships.add(serverMembership);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serverMemberships;
    }

    /**
     * Adds a new server membership to the database.
     *
     * @param serverMembership The server membership to add.
     * @return The ID of the newly added server membership, or -1 if the insertion failed.
     */
    public int addServerMembership(ServerMembership serverMembership) {
        String sql = "INSERT INTO server_memberships (user_id, server_id, role) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, serverMembership.getUserId());
            statement.setInt(2, serverMembership.getServerId());
            statement.setString(3, serverMembership.getRole());

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
     * Updates an existing server membership in the database.
     *
     * @param serverMembership The server membership to update.
     * @return True if the update was successful, false otherwise.
     */
    public boolean updateServerMembership(ServerMembership serverMembership) {
        String sql = "UPDATE server_memberships SET user_id = ?, server_id = ?, role = ? WHERE membership_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, serverMembership.getUserId());
            statement.setInt(2, serverMembership.getServerId());
            statement.setString(3, serverMembership.getRole());
            statement.setInt(4, serverMembership.getMembershipId());

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes a server membership from the database.
     *
     * @param membershipId The ID of the server membership to delete.
     * @return True if the deletion was successful, false otherwise.
     */
    public boolean deleteServerMembership(int membershipId) {
        String sql = "DELETE FROM server_memberships WHERE membership_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, membershipId);

            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Maps a ResultSet row to a ServerMembership object.
     *
     * @param resultSet The ResultSet containing the server membership data.
     * @return The ServerMembership object.
     * @throws SQLException If a database access error occurs.
     */
    private ServerMembership mapServerMembershipFromResultSet(ResultSet resultSet) throws SQLException {
        int membershipId = resultSet.getInt("membership_id");
        int userId = resultSet.getInt("user_id");
        int serverId = resultSet.getInt("server_id");
        String role = resultSet.getString("role");

        return new ServerMembership(membershipId, userId, serverId, role);
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
