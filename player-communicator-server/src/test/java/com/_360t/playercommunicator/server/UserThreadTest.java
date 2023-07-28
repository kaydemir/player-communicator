package com._360t.playercommunicator.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UserThreadTest {
    @Mock
    private Socket mockSocket;
    @Mock
    private Server mockServer;
    @Mock
    private BufferedReader mockReader;
    @Mock
    private PrintWriter mockWriter;


    private UserThread subject;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        Mockito.when(mockSocket.getInputStream()).thenReturn(Mockito.mock(InputStream.class));
        Mockito.when(mockSocket.getOutputStream()).thenReturn(Mockito.mock(OutputStream.class));
        subject = new UserThread(mockSocket, mockServer);
    }

    @Test
    public void testRun() throws IOException {
        Mockito.when(mockReader.readLine()).thenReturn("Hi");

        //when
        subject.run();
    }

    @Test
    public void testSendMessage() {
        String message = "Hi!";

        // when
        subject.sendMessage(message);
        //verify
        Mockito.verify(mockSocket, Mockito.times(1));
    }


}
