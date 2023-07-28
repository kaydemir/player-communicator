package com._360t.playercommunicator.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ServerTest {

    public static int PORT = 12345;


    @Mock
    private UserThread userThread;

    private Server subject;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        subject = new Server(PORT);
    }

    @Test
    public void testServerConnection() throws IOException {
        // Run the server in a separate thread
        Thread serverThread = new Thread(() -> {
            try {
                subject.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        serverThread.start();

        // Give the server some time to start listening for connections
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Socket socket = new Socket("localhost", PORT);

        // Assert that the connection is successful
        Assert.assertTrue(socket.isConnected());

        // Close the socket and stop the server thread
        socket.close();
        serverThread.interrupt();
    }

    @Test
    public void testRemoveUser() {
        // Create a mock instance of Server class
        Server server = mock(Server.class);

        // Define the username to be removed
        String userName = "User1";

        // Call the removeUser method on the server
        server.removeUser(userName, userThread);

        // Verify that the removeUser method was called with the correct arguments
        verify(server, Mockito.times(1)).removeUser(userName, userThread);
    }

    @Test
    public void testBroadcastMessageWhenNoUserThreadExist() {

        // Create mock UserThread instances
        UserThread userThread1 = mock(UserThread.class);
        UserThread userThread2 = mock(UserThread.class);

        // Define the message to be broadcasted
        String message = "Hi!";

        // Call the broadcast method on the server
        subject.broadcast(message, null);

        // Verify that the sendMessage method was called on both userThreads...
        verify(userThread1, Mockito.times(0)).sendMessage(message);
        verify(userThread2, Mockito.times(0)).sendMessage(message);
    }

    @Test
    public void testBroadcastMessage() {

        // Create mock UserThread instances
        UserThread userThread1 = mock(UserThread.class);
        UserThread userThread2 = mock(UserThread.class);
        subject.getUserThreads().add(userThread1);
        subject.getUserThreads().add(userThread2);

        // Define the message to be broadcasted
        String message = "Hi!";

        // Call the broadcast method on the server
        subject.broadcast(message, null); // Pass null as excludeUser since we don't want to exclude anyone in this test

        // Verify that the sendMessage method was called on both mock UserThreads with the correct message
        verify(userThread1, Mockito.times(1)).sendMessage(message);
        verify(userThread2, Mockito.times(1)).sendMessage(message);
    }

}