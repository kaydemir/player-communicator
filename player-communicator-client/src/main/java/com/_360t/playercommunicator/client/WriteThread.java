package com._360t.playercommunicator.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * the primary responsibility of this class is managing the sending of messages from the associated client to the chat server.
 * It sends the client's username upon starting, reads user input from the console, a
 * nd sends messages to the server while counting the number of messages sent.
 * it operates as a separate thread, allowing the client to send messages concurrently with other tasks performed by the application.
 * <p>
 * it also has responsibilities like;
 * - reading the user input from console using BufferedReader and flushes to cleints OutputStream
 * - increments the sendMessageCount field of the associated player instance every time a message is successfully sent to the server.
 * - handles the disconnection process after sending and receiving {@link Player.MESSAGE_COUNT_LIMIT} messages from the server
 * - gracefully terminates the application by closing all threads and associated socket.
 */
public class WriteThread extends Thread {
    private PrintWriter writer;

    private Socket socket;
    private Player client;


    public WriteThread(Socket socket, Player client) {
        this.socket = socket;
        this.client = client;

        try {
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ClientUtils.closeEverything(socket, null, writer);
        }
    }

    public void run() {

        writer.println(client.getUserName());
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));
        if (PlayerType.INITIATOR.equals(client.getPlayerType())) {
            System.out.print("Type a message to send: ");
        }
        String text;

        try {
            do {
                text = reader.readLine();
                if ("!Disconnect".equals(text)) {
                    writer.println("!Disconnect");
                    break;
                }
                writer.println(text);
                int count = client.sendMessageCount.incrementAndGet();
                if(PlayerType.INITIATOR.equals(client.getPlayerType())){
                    if(count == Player.MESSAGE_COUNT_LIMIT){
                        while(client.retrieveMessageCount.get() != Player.MESSAGE_COUNT_LIMIT){
                            Thread.sleep(50);
                        }
                        System.out.println(count + " messages has been send and received by the initiator. Disconnecting from chat.");
                        writer.println("!Disconnect");
                        break;
                    }
                }
            } while (!text.equals("!Disconnect"));

            System.exit(0);

        } catch (IOException ex) {
            System.out.println("Error writing to server: " + ex.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            ClientUtils.closeEverything(socket, null, writer);
        }
    }
}