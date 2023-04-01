package cc.phantomhost.core;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
        Socket clientSocket = socket.accept();
        System.out.println(clientSocket.getInetAddress());
    }
}
