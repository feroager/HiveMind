package com.example.server;

import com.example.support.ConsoleHelper;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApplication {
    private static int serverNumberPort;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        ConsoleHelper.writeMessage("Insert server port: ");
        serverNumberPort = ConsoleHelper.readInt();

        new ServerApplication().startServer();
    }

    public void startServer() {
        try {
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
}
