package com.example.server;

import java.sql.Connection;
import com.example.utils.ConsoleHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {
    private static int serverNumberPort;
    private static ServerSocket serverSocket;
    private static Connection connectionDb;
    private static ServerSettingsController serverSettingsController;

    public void startServer() {


        try {
            ConsoleHelper.writeMessage("Insert server port: ");
            serverNumberPort = ConsoleHelper.readInt();
            serverSocket = new ServerSocket(serverNumberPort);
            ConsoleHelper.writeMessage("Server's running");
            while (true) {
                Socket socket = serverSocket.accept();
                Handler handler = new Handler(socket);
                handler.start();
            }
        } catch (IOException e) {
            ConsoleHelper.writeMessage("An error occurred while starting or running the server.");
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

    }

    public static Connection getConnectionDb()
    {
        return connectionDb;
    }

    public static void setConnectionDb(Connection connectionDb)
    {
        ServerApplication.connectionDb = connectionDb;
    }

    public static ServerSettingsController getServerSettingsController()
    {
        return serverSettingsController;
    }

    public static void setServerSettingsController(ServerSettingsController serverSettingsController)
    {
        ServerApplication.serverSettingsController = serverSettingsController;
    }
}
