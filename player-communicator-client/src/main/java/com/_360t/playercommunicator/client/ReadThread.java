package com._360t.playercommunicator.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * the primary responsibility of this class is continuously reading messages sent from the chat server to
 * the associated client through the socket's input stream (InputStream).
 * It accomplishes this by using a BufferedReader to read lines of text from the input stream.
 * <p>
 * it also has responsibilities like;
 * - after receiving a message from the server, prints the message to the console.
 * - increments retrieveMessageCount field of the assocaited player instance every time a message is
 * successfully retrieved from the server.
 * - sends ack message back to the server by increasing and concatting the value of retrieveMessageCount to the
 * received message
 * - disconnects the receiver if the retrieveMessageCount is reachted to {@link Player.MESSAGE_COUNT_LIMIT}
 * - gracefully terminates the application by closing all threads and associated socket.
 */
public class ReadThread extends Thread {
    private BufferedReader reader;
    private Socket socket;
    private Player client;

    private PrintWriter writer;

    public ReadThread(Socket socket, Player client) {
        this.socket = socket;
        this.client = client;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error getting input stream: " + ex.getMessage());
            ClientUtils.closeEverything(socket, reader, writer);
        }
    }

    public void run() {
        try {
            String response;
            while ((response = reader.readLine()) != null) {
                if (PlayerType.INITIATOR.equals(client.getPlayerType())) {
                    System.out.println("Manipulated response: " + response);
                }

                int count = client.retrieveMessageCount.incrementAndGet();

                if (PlayerType.RECEIVER.equals(client.getPlayerType())) {
                    System.out.println("Incoming message: " + response);
                    writer.println(response + count);
                    System.out.println("Manipulating message to: " + response + count);

                    if (count == Player.MESSAGE_COUNT_LIMIT) {
                        writer.println("!Disconnect");
                        break;
                    }
                }

            }
            System.exit(0);
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
        } finally {
            ClientUtils.closeEverything(socket, reader, writer);
        }

    }
}
