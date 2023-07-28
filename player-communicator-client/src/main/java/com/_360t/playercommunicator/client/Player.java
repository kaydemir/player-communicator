package com._360t.playercommunicator.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This client (player) class to hold information related to a player in the communication system
 * It stores data such as the player's hostname, port, username, and player type
 * <p>
 * it also has responsibilities like;
 * - tracking retrieved and send number of the messages count from the server
 * - creating the player instances before the connection to the server
 * - establishing a connection to the chat server using a socket. It starts two threads, ReadThread and WriteThread,
 * to handle reading and writing messages to/from the server.
 */
public class Player {

    private String hostname;
    private int port;
    private String userName;
    private PlayerType playerType;

    public AtomicInteger retrieveMessageCount = new AtomicInteger();
    public AtomicInteger sendMessageCount = new AtomicInteger();

    public static final int MESSAGE_COUNT_LIMIT = 10;


    public Player(String hostname, int port, PlayerType playerType, String userName) {
        this.hostname = hostname;
        this.port = port;
        this.playerType = playerType;
        this.userName = userName;
    }

    /**
     * returns the name of the player
     *
     * @return the player's name
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * returns the type of the player
     *
     * @return the player's type
     */
    public PlayerType getPlayerType() {
        return playerType;
    }

    public static void main(String[] args) {
        if (args.length < 4) {
            System.err.println("CLIENT: Missing argument");
            return;
        }

        // TODO: Do more validation
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        PlayerType playerType = PlayerType.valueOf(args[2]);
        String userName = args[3];

        Player client = new Player(hostname, port, playerType, userName);
        client.execute();
    }

    protected void execute() {
        try {
            Socket socket = new Socket(hostname, port);
            System.out.println("Connected to the server");
            new ReadThread(socket, this).start();
            new WriteThread(socket, this).start();
        } catch (UnknownHostException ex) {
            System.err.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("I/O Error: " + ex.getMessage());
        }

    }

}
