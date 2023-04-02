package cc.phantomhost.core.protocol.minecraft;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

public class MinecraftProtocol762Test {

    private MinecraftProtocol762 protocol;
    private DataOutputStream serverOut;

    private DataInputStream serverIn;

    private DataOutputStream clientOut;

    private DataInputStream clientIn;

    private Fake762Client client;

    @BeforeEach
    void setUp() throws IOException {
        PipedInputStream clientPipedInputStream = new PipedInputStream(65536);
        PipedOutputStream serverPipedOutputStream = new PipedOutputStream(clientPipedInputStream);
        PipedInputStream serverPipedInputStream = new PipedInputStream(65536);
        PipedOutputStream clientPipedOutputStream = new PipedOutputStream(serverPipedInputStream);

        serverIn = new DataInputStream(serverPipedInputStream);
        serverOut = new DataOutputStream(serverPipedOutputStream);
        clientIn = new DataInputStream(clientPipedInputStream);
        clientOut = new DataOutputStream(clientPipedOutputStream);

        client = new Fake762Client("test",(short)25565);
        client.writeHandshake(clientOut);
        HandshakeData initialData = new HandshakeData(serverIn);
        protocol = new MinecraftProtocol762(initialData,new File("src/test/resources/server-icon.png"));
    }

    @Test
    void statusRequestTest() throws IOException {
        client.writeStatusRequest(clientOut);
        client.writeExit(clientOut);
        protocol.handleClient(serverIn,serverOut);
        client.readStatusRequest(clientIn);
    }

    @Test
    void pongTest1() throws IOException {
        long time = System.currentTimeMillis();
        client.writePing(clientOut, time);
        protocol.handleClient(serverIn,serverOut);
        Assertions.assertEquals(time, client.getPongResponse(clientIn));
    }

    @Test
    void abortedTest() throws IOException {
        client.writeExit(clientOut);
        protocol.handleClient(serverIn,serverOut);

        Assertions.assertTrue(clientIn.available() == 0);
        Assertions.assertTrue(serverIn.available() == 0);
    }
}
