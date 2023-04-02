package cc.phantomhost.core.protocol.minecraft;

import cc.phantomhost.core.utils.MinecraftProtocolUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Fake762Client {

    private final String domainName;
    private final short port;

    public Fake762Client(String domainName, short port){
        this.domainName = domainName;
        this.port = port;
    }

    public void writeHandshake(DataOutputStream out) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        dataOutputStream.writeByte(0x00); // packet id
        MinecraftProtocolUtils.writeVarInt(dataOutputStream,4); // protocol version
        MinecraftProtocolUtils.writeString(dataOutputStream, domainName);
        dataOutputStream.writeShort(port);
        MinecraftProtocolUtils.writeVarInt(dataOutputStream,1); // state (1 for handshake)
        MinecraftProtocolUtils.writePacket(out,byteArrayOutputStream);
    }

    public void writeStatusRequest(DataOutputStream out) throws IOException {
        out.write(0x01);
        out.write(0x00);
    }

    public void writeExit(DataOutputStream clientOut) throws IOException {
        clientOut.write(0x00);
    }

    public String getPacketData(DataInputStream clientIn) throws IOException {
        int packetLength = MinecraftProtocolUtils.readVarInt(clientIn);
        int packetId = clientIn.read();
        return new String(clientIn.readNBytes(packetLength-1), StandardCharsets.UTF_8);
    }

    public void readStatusRequest(DataInputStream clientIn) throws IOException {
        int packetLength = MinecraftProtocolUtils.readVarInt(clientIn);
        int packetId = clientIn.readByte();
        if (packetId == -1) {
            throw new IOException("Premature end of stream.");
        }
        if (packetId != 0x00) {
            throw new IOException("Invalid packetID");
        }
        String statusRequest = MinecraftProtocolUtils.readString(clientIn);
        JsonElement jsonObject = JsonParser.parseString(statusRequest);
    }

    public void writePing(DataOutputStream clientOut, long number) throws IOException {
        clientOut.writeByte(0x09);
        clientOut.writeByte(0x01);
        clientOut.writeLong(number);
    }

    public long getPongResponse(DataInputStream clientIn) throws IOException {
        int packetLength = MinecraftProtocolUtils.readVarInt(clientIn);
        System.out.println(packetLength);
        int id = MinecraftProtocolUtils.readVarInt(clientIn);
        if (id == -1) {
            throw new IOException("Premature end of stream.");
        }
        if (id != 0x01) {
            throw new IOException("Invalid packetID");
        }
        return clientIn.readLong();
    }
}
