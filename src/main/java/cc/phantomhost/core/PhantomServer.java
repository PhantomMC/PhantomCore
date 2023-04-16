package cc.phantomhost.core;

import cc.phantomhost.core.protocol.Protocol;
import cc.phantomhost.core.protocol.minecraft.HandshakeData;
import cc.phantomhost.core.protocol.minecraft.MinecraftProtocol762;
import cc.phantomhost.core.protocol.setting.Configuration;
import cc.phantomhost.core.utils.ConfigFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhantomServer extends Thread {
    private final Configuration defaultConfig;
    private final Logger logger;
    private ServerSocket socket;

    public PhantomServer(Configuration defaultConfig, Logger logger){
        this.defaultConfig = defaultConfig;
        this.logger = logger;
    }
    public void run(){
        try (ServerSocket socket = new ServerSocket(25565)){
            this.socket = socket;
            while(!this.isInterrupted()){
                accept(socket);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE,"Server closed",e);
        }

    }
    public void interrupt(){
        super.interrupt();
        if(this.socket == null){
            return;
        }
        try {
            this.socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void accept(ServerSocket socket) throws IOException {
        try (Socket clientSocket = socket.accept()) {
            InputStream in = clientSocket.getInputStream();
            DataInputStream dataIn = new DataInputStream(in);
            HandshakeData data = new HandshakeData(dataIn);
            Protocol protocol = new MinecraftProtocol762(data, defaultConfig, logger);
            OutputStream out = clientSocket.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);
            protocol.handleClient(dataIn, dataOut);
        } catch (IOException e) {
            if(!this.isInterrupted()){
                logger.log(Level.WARNING,"Client connection closed",e);
            }
        }
    }
}
