package com.example.utils;

import com.example.message.CommunicationMessage;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * The {@code Connection} class represents a network connection between a client and a server.
 * It provides methods for sending and receiving {@link CommunicationMessage} objects.
 *
 */
public class ConnectionHost implements Closeable {

    /**
     * The underlying socket for the connection.
     */
    private final Socket socket;

    /**
     * The object output stream for sending messages.
     */
    private final ObjectOutputStream out;

    /**
     * The object input stream for receiving messages.
     */
    private final ObjectInputStream in;

    /**
     * Creates a new {@code Connection} instance with the specified socket.
     *
     * @param socket The socket representing the connection.
     * @throws IOException If an I/O error occurs while creating the connection.
     */
    public ConnectionHost(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }

    /**
     * Sends a {@link CommunicationMessage} through the connection.
     *
     * @param communicationMessage The message to be sent.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    public void send(CommunicationMessage communicationMessage) throws IOException {
        synchronized (out) {
            out.writeObject(communicationMessage);
        }
    }

    /**
     * Receives a {@link CommunicationMessage} from the connection.
     *
     * @return The received message.
     * @throws IOException            If an I/O error occurs while receiving the message.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public CommunicationMessage receive() throws IOException, ClassNotFoundException {
        synchronized (in) {
            return (CommunicationMessage) in.readObject();
        }
    }

    /**
     * Gets the remote socket address of the connection.
     *
     * @return The remote socket address.
     */
    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    /**
     * Closes the connection by closing the input/output streams and the underlying socket.
     *
     * @throws IOException If an I/O error occurs while closing the connection.
     */
    @Override
    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }
}
