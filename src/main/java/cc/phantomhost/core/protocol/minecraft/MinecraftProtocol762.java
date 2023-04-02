package cc.phantomhost.core.protocol.minecraft;

import cc.phantomhost.core.protocol.Protocol;
import cc.phantomhost.core.utils.MinecraftProtocolUtils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MinecraftProtocol762 implements Protocol {

    private final HandshakeData initialData;

    public MinecraftProtocol762(HandshakeData initialData){
        this.initialData = initialData;
    }

    @Override
    public void handleClient(DataInputStream in, DataOutputStream out) throws IOException {
        if(initialData.getPacketId() != 0){
            return;
        }
        switch (initialData.getState()) {
            case 1:
                handleStatusConnection(in, out);
                return;
            case 2:
                handleLoginConnection(in, out);
                return;
            default:
                System.out.println("Got an invalid state:");
                System.out.println(initialData.getState());
        }
    }

    protected void handleLoginConnection(DataInputStream in, DataOutputStream out) {

    }

    protected void handleStatusConnection(DataInputStream in, DataOutputStream out) throws IOException {
        while(true){
            int packetLength = MinecraftProtocolUtils.readVarInt(in);
            if(packetLength == 0){
                System.out.println("Connection aborted");
                return;
            }
            int packetId = in.read();
            switch (packetId) {
                case 0 -> {
                    sendStatusResponse(out);
                }
                case 1 -> {
                    long pingNumber = in.readLong();
                    sendPongResponse(out, pingNumber);
                    return;
                }
                default -> throw new IOException("Invalid packetID");
            }
        }
    }

    protected void sendPongResponse(DataOutputStream out, long pingNumber) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(byteArrayOutputStream);
        data.writeByte(0x01);
        data.writeLong(pingNumber);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        MinecraftProtocolUtils.writeVarInt(out,byteArray.length);
        out.write(byteArray);
    }

    protected void sendStatusResponse(DataOutputStream out) throws IOException {
        ByteArrayOutputStream dataArray = new ByteArrayOutputStream();
        DataOutputStream data = new DataOutputStream(dataArray);
        String msg = "{'version':{'name':'1.19.3','protocol':761},'players':{'max':100,'online':5,'sample':[]},'description':{'text':'Hello world'},'enforcesSecureChat':true}";
        data.writeByte(0x00);
        MinecraftProtocolUtils.writeString(data, msg);
        byte [] byteArray = dataArray.toByteArray();
        MinecraftProtocolUtils.writeVarInt(out,byteArray.length);
        out.write(byteArray);
    }
}
