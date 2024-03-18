package com.hivemind.database.models;

import java.io.Serializable;

/**
 * Represents the 'server_memberships' table in the database.
 */
public class ServerMembership implements BaseModel, Serializable
{

    private int membershipId;
    private int userId;
    private int serverId;
    private String role;

    public ServerMembership(int membershipId, int userId, int serverId, String role)
    {
        this.membershipId = membershipId;
        this.userId = userId;
        this.serverId = serverId;
        this.role = role;
    }

    /**
     * Gets the unique identifier of the server membership.
     *
     * @return The membership ID.
     */
    public int getMembershipId() {
        return membershipId;
    }

    /**
     * Sets the unique identifier of the server membership.
     *
     * @param membershipId The membership ID to set.
     */
    public void setMembershipId(int membershipId) {
        this.membershipId = membershipId;
    }

    /**
     * Gets the unique identifier of the user associated with the membership.
     *
     * @return The user ID.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets the unique identifier of the user associated with the membership.
     *
     * @param userId The user ID to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets the unique identifier of the server associated with the membership.
     *
     * @return The server ID.
     */
    public int getServerId() {
        return serverId;
    }

    /**
     * Sets the unique identifier of the server associated with the membership.
     *
     * @param serverId The server ID to set.
     */
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    /**
     * Gets the role of the user in the context of the server.
     *
     * @return The user's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user in the context of the server.
     *
     * @param role The user's role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Gets the unique identifier of the server membership.
     *
     * @return The server membership ID.
     */
    public int getId() {
        return getMembershipId();
    }

    /**
     * Sets the unique identifier of the server membership.
     *
     * @param id The server membership ID to set.
     */
    public void setId(int id) {
        setMembershipId(id);
    }

}

