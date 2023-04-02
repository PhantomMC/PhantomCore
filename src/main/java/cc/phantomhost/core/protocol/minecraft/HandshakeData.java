package cc.phantomhost.core.protocol.minecraft;

import cc.phantomhost.core.utils.MinecraftProtocolUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public class HandshakeData {

    private final int packetId;
    private final int protocolVersion;
    private final String hostAddress;
    private final int state;
    private final short port;

    public HandshakeData(DataInputStream stream) throws IOException {
        int packetLength = MinecraftProtocolUtils.readVarInt(stream);
        this.packetId = stream.read();
        this.protocolVersion = MinecraftProtocolUtils.readVarInt(stream);
        this.hostAddress = MinecraftProtocolUtils.readString(stream);
        this.port = stream.readShort();
        this.state = MinecraftProtocolUtils.readVarInt(stream);
    }

    public int getPacketId(){
        return packetId;
    }

    public int getProtocolVersion(){
        return protocolVersion;
    }

    public String getHostAddress(){
        return hostAddress;
    }

    public int getState(){
        return state;
    }

    public short getPort(){
        return port;
    }
}
