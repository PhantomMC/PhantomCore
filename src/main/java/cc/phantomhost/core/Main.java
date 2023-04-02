package cc.phantomhost.core;

import cc.phantomhost.core.protocol.Protocol;
import cc.phantomhost.core.protocol.minecraft.HandshakeData;
import cc.phantomhost.core.protocol.minecraft.MinecraftProtocol762;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class Main {

    public static void main(String[] args) {
        try {
            ServerSocket  socket = null;
            socket = new ServerSocket(25565);
            while(true){
                accept(socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void accept(ServerSocket socket) throws IOException {
        try (Socket clientSocket = socket.accept()) {
            InputStream in = clientSocket.getInputStream();
            DataInputStream dataIn = new DataInputStream(in);
            HandshakeData data = new HandshakeData(dataIn);
            Protocol protocol = new MinecraftProtocol762(data);
            OutputStream out = clientSocket.getOutputStream();
            DataOutputStream dataOut = new DataOutputStream(out);
            protocol.handleClient(dataIn, dataOut);
        } catch (SocketException ignored) {
        }
    }
}
