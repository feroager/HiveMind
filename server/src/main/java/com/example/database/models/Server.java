package com.example.database.models;

//import java.security.Timestamp;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represents the 'servers' table in the database.
 */
public class Server implements BaseModel, Serializable
{

    private int serverId;
    private String name;
    private int adminId;
    private String adminCode;
    private Timestamp creationTimestamp;

    public Server(int serverId, String name, int adminId, String adminCode, Timestamp creationTimestamp)
    {
        this.serverId = serverId;
        this.name = name;
        this.adminId = adminId;
        this.adminCode = adminCode;
        this.creationTimestamp = creationTimestamp;
    }

    /**
     * Gets the unique identifier of the server.
     *
     * @return The server ID.
     */
    public int getServerId() {
        return serverId;
    }

    /**
     * Sets the unique identifier of the server.
     *
     * @param serverId The server ID to set.
     */
    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    /**
     * Gets the name of the server.
     *
     * @return The server name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the server.
     *
     * @param name The server name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the unique identifier of the administrator of the server.
     *
     * @return The admin ID.
     */
    public int getAdminId() {
        return adminId;
    }

    /**
     * Sets the unique identifier of the administrator of the server.
     *
     * @param adminId The admin ID to set.
     */
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    /**
     * Gets the admin code of the server.
     *
     * @return The admin code.
     */
    public String getAdminCode() {
        return adminCode;
    }

    /**
     * Sets the admin code of the server.
     *
     * @param adminCode The admin code to set.
     */
    public void setAdminCode(String adminCode) {
        this.adminCode = adminCode;
    }

    /**
     * Gets the creation timestamp of the server.
     *
     * @return The creation timestamp.
     */
    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    /**
     * Sets the creation timestamp of the server.
     *
     * @param creationTimestamp The creation timestamp to set.
     */
    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    /**
     * Gets the unique identifier of the server.
     *
     * @return The server ID.
     */
    public int getId() {
        return getServerId();
    }

    /**
     * Sets the unique identifier of the server.
     *
     * @param id The server ID to set.
     */
    public void setId(int id) {
        setServerId(id);
    }
}
