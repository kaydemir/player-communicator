package com._360t.playercommunicator.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The UserThread class is responsible for handling communication with individial clients in this chat app.
 * It achieves this by reading messages sent by the client and broadcasting received messages to all connected clients
 * It extends the Thread class, allowing each connected client to be handled concurrently.
 * <p>
 * It also has responsibilities below:
 * - adding a user to the server
 * - sending a message to the client
 * - removing a user from the server
 * - gracefully disconnect the socket by checking "!Disconnect" command in the client's messages, indicating that the client wants to disconnect.
 * - exception handling for the I/O operations
 */
public class UserThread extends Thread {

    private Socket socket;
    private Server server;
    private PrintWriter writer;
    private BufferedReader reader;

    public UserThread(Socket socket, Server server) {
        try {
            this.socket = socket;
            this.server = server;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            closeEverything(socket, reader, writer);
        }

    }

    public void run() {
        try {

            String userName = reader.readLine();
            server.addUserName(userName);

            System.out.println("SERVER: "+ userName + " has joined");

            String serverMessage;
            String clientMessage;

            while (true){
                clientMessage = reader.readLine();
                if("!Disconnect".equals(clientMessage)){
                    break;
                }
                serverMessage = clientMessage;
                server.broadcast(serverMessage, this);
            }
            server.removeUser(userName, this);

        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
        } finally {
            closeEverything(socket, reader, writer);
        }
    }

    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }

    /**
     * closes all the sockets and open streams
     * @param socket
     * @param bufferedReader
     * @param printWriter
     */
    private void closeEverything(Socket socket, BufferedReader bufferedReader, PrintWriter printWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (printWriter != null) {
                printWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}