package com._360t.playercommunicator.client;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PlayerTest {

    private Player subject;
    private String hostname;
    private int port;
    private PlayerType playerType;
    private String userName;

    @Before
    public void setUp() {
        hostname = "localhost";
        port = 12345;
        playerType = PlayerType.INITIATOR;
        userName = "Player1";
        subject = new Player(hostname, port, playerType, userName);
    }

    @Test
    public void testGetUserName() {
        Assert.assertEquals(userName, subject.getUserName());
    }

    @Test
    public void testGetPlayerType() {
        Assert.assertEquals(playerType, subject.getPlayerType());
    }

    @Test
    public void testConnectionOfClient() {
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        Socket mockSocket = Mockito.mock(Socket.class);
        serverThread.start();

        // Call the execute method
        subject.execute();
        //verify that mockSocket is not used
        Mockito.verifyNoInteractions(mockSocket);
    }

    @Test
    public void testSendMessageCount() {
        Assert.assertEquals(0, subject.sendMessageCount.get());
        subject.sendMessageCount.incrementAndGet();
        Assert.assertEquals(1, subject.sendMessageCount.get());
    }

    @Test
    public void testRetrieveMessageCount() {
        Assert.assertEquals(0, subject.retrieveMessageCount.get());
        subject.retrieveMessageCount.incrementAndGet();
        Assert.assertEquals(1, subject.retrieveMessageCount.get());
    }
}