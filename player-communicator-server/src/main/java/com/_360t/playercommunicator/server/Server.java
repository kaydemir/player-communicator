package com._360t.playercommunicator.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * This class creates a Socket for the messaging on the given port and starts to listen inputStream from that port.
 * after clients are connected to server, saves and starts the connected users in a Thread.
 * for the incoming and outgoing messages, behaves as orchestarator between the clients to send and read the messages
 * -------------   SERVER -------------
 * CLIENT(PLAYER1)		   CLIENT(PLAYER2)
 * (INITIATOR) 			     (RECEIVER)
 * Player1 --- > Hello --> Player2
 * Player1 <-- Hello1 <-- Player2 (Player1's 1st message)
 * Player1 --> Nice to meet you --> Player2
 * Player1 <-- Nice to meet you2 <-- Player2 (Player1's 2nd message)
 * <p>
 * it also has responsibilities like;
 * - accepting Client Connections
 * - Managing UserThreads
 * - broadcasting messages to all connected clients
 * - removing disconnected clients
 */
public class Server {

    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public int getPort() {
        return port;
    }

    public Set<String> getUserNames() {
        return userNames;
    }

    public Set<UserThread> getUserThreads() {
        return userThreads;
    }

    public Server(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("SERVER: Missing argument");
            return;
        }
        // TODO: Do more validation
        int port = Integer.parseInt(args[0]);
        Server server = new Server(port);
        server.execute();
    }

    protected void execute() throws IOException {
        //set the maximum length of the queue to 2 as two players will be connected
        // close the socket automatically using try with resources
        try (ServerSocket serverSocket = new ServerSocket(port, 2)) {

            System.out.println("SERVER: listening on port: " + port);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("SERVER: A new client has connected");
                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }
        }
    }

    /**
     * this method broadcast message to other players if not self.
     */
    void broadcast(String message, UserThread excludeUser) {
        for (UserThread userThread : userThreads) {
            if (userThread != excludeUser) {
                userThread.sendMessage(message);
            }
        }
    }

    /**
     * Stores username of the newly connected client.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }

    /**
     * When a client is disconnected, removes the associated username and UserThread
     */
    void removeUser(String userName, UserThread userThread) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(userThread);
            System.out.println("The user " + userName + " has left the chat");
        }
    }
}
