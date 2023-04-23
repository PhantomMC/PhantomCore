package cc.phantomhost.core;

import cc.phantomhost.core.config.Setting;
import cc.phantomhost.core.protocol.Protocol;
import cc.phantomhost.core.protocol.minecraft.HandshakeData;
import cc.phantomhost.core.protocol.minecraft.MinecraftProtocol762;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhantomServer extends Thread {
    private final Map<Setting,String> defaultConfig;
    private final Logger logger;
    private ServerSocket socket;

    public PhantomServer(Map<Setting,String> defaultConfig, Logger logger){
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
            if(this.isInterrupted()){
                return;
            }
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
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in, 65536);
            try {
                bufferedInputStream.mark(65536);
                DataInputStream dataIn = new DataInputStream(bufferedInputStream);
                HandshakeData data = new HandshakeData(dataIn);
                Protocol protocol = new MinecraftProtocol762(data, defaultConfig, logger);
                OutputStream out = clientSocket.getOutputStream();
                DataOutputStream dataOut = new DataOutputStream(out);
                protocol.handleClient(dataIn, dataOut);
            } catch (IOException e) {
                if(this.isInterrupted()) {
                    return;
                }
                try {
                    logger.log(Level.FINE, "Client connection closed", e);
                    bufferedInputStream.reset();
                    byte[] byteArray = bufferedInputStream.readAllBytes();
                    logger.log(Level.FINE, new String(byteArray, StandardCharsets.UTF_8));
                } catch (SocketException ignored) {}
            }
        }
    }
}
