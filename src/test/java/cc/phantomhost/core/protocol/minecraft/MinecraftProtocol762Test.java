package cc.phantomhost.core.protocol.minecraft;

import cc.phantomhost.core.PhantomCore;
import cc.phantomhost.core.protocol.setting.Configuration;
import cc.phantomhost.core.utils.ConfigFactory;
import cc.phantomhost.core.utils.MinecraftProtocolUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.logging.Logger;

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
        Configuration config = ConfigFactory.loadConfigurationFromFile(new File("src/test/resources/testConfig.properties"));
        protocol = new MinecraftProtocol762(initialData,config, Logger.getLogger(PhantomCore.class.getName()));
    }

    @Test
    void statusRequestTest() throws IOException {
        client.writeStatusRequest(clientOut);
        client.writeExit(clientOut);
        protocol.handleClient(serverIn,serverOut);
        client.readStatusRequest(clientIn);
        assertEmptyStreams();
    }

    @Test
    void pongTest1() throws IOException {
        long time = System.currentTimeMillis();
        client.writePing(clientOut, time);
        protocol.handleClient(serverIn,serverOut);
        Assertions.assertEquals(time, client.getPongResponse(clientIn));
        assertEmptyStreams();
    }

    @Test
    void pongTest2() throws IOException {
        client.writeStatusRequest(clientOut);
        long time = System.currentTimeMillis();
        client.writePing(clientOut,time);
        protocol.handleClient(serverIn,serverOut);
        client.readStatusRequest(clientIn);
        Assertions.assertEquals(time, client.getPongResponse(clientIn));
        assertEmptyStreams();
    }

    @Test
    void abortedTest() throws IOException {
        client.writeExit(clientOut);
        protocol.handleClient(serverIn,serverOut);
        assertEmptyStreams();
    }

    void assertEmptyStreams() throws IOException {
        Assertions.assertEquals(0, clientIn.available());
        Assertions.assertEquals(0, serverIn.available());
    }
}
