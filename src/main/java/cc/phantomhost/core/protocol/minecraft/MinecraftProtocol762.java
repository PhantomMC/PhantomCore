package cc.phantomhost.core.protocol.minecraft;

import cc.phantomhost.core.FileName;
import cc.phantomhost.core.protocol.Protocol;
import cc.phantomhost.core.protocol.setting.Configuration;
import cc.phantomhost.core.protocol.setting.Setting;
import cc.phantomhost.core.utils.FileUtils;
import cc.phantomhost.core.utils.MessageCompiler;
import cc.phantomhost.core.utils.MinecraftProtocolUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinecraftProtocol762 implements Protocol {

    private static String UNCOMPILED_LOGIN_MESSAGE = FileUtils.readInternalFileToString(FileName.LOGIN_MESSAGE);
    private static String UNCOMPILED_RESPONSE_MESSAGE = FileUtils.readInternalFileToString(FileName.RESPONSE_MESSAGE);

    private final HandshakeData initialData;
    private final Logger logger;
    private final Configuration config;

    String responseMsg;
    String loginMsg;

    public MinecraftProtocol762(HandshakeData initialData, Configuration config, Logger logger){
        this.initialData = initialData;
        this.logger = logger;
        this.config = config;
        Map<DisplayData,String> displayDataMap = new HashMap<>();
        displayDataMap.put(DisplayData.WARNING_LINE,config.getSetting(Setting.WARNING_LINE));
        displayDataMap.put(DisplayData.PROTOCOL_VERSION,String.valueOf(getResponseProtocolVersion(initialData.getProtocolVersion(),config)));
        displayDataMap.put(DisplayData.HOVER_MESSAGE,MessageCompiler.compileHoverMessage(config.getSetting(Setting.HOVER_MESSAGE)));
        displayDataMap.put(DisplayData.MOTD,config.getSetting(Setting.MOTD));
        try {
            displayDataMap.put(DisplayData.IMAGE_DATA,MessageCompiler.compileImage(config.getSetting(Setting.IMAGE_LOCATION)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        displayDataMap.put(DisplayData.LOGIN_MESSAGE,config.getSetting(Setting.LOGIN_MESSAGE));


        responseMsg = MessageCompiler.compileMessage(UNCOMPILED_RESPONSE_MESSAGE, displayDataMap);
        loginMsg = MessageCompiler.compileMessage(UNCOMPILED_LOGIN_MESSAGE, displayDataMap);
    }

    private int getResponseProtocolVersion(int clientProtocolVersion, Configuration config) {
        return switch (Integer.parseInt(config.getSetting(Setting.PROTOCOL_VERSION_RESPONSE))) {
            case 1 -> clientProtocolVersion;
            case 2 -> clientProtocolVersion + 1;
            case 3 -> clientProtocolVersion - 1;
            default -> throw new RuntimeException("Hypothetically unreachable code reached");
        };
    }

    @Override
    public void handleClient(DataInputStream in, DataOutputStream out) throws IOException {
        if(initialData.getPacketId() != 0){
            return;
        }
        switch (initialData.getState()) {
            case 1 -> handleStatusConnection(in, out);
            case 2 -> handleLoginConnection(in, out);
            default -> logger.log(Level.INFO, ("Got an invalid state:"));
        }
    }

    protected void handleLoginConnection(DataInputStream in, DataOutputStream out) throws IOException {
        int packetLength = MinecraftProtocolUtils.readVarInt(in);
        int packetId = in.readByte();
        String username = MinecraftProtocolUtils.readString(in);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream outputStream = new DataOutputStream(byteArrayOutputStream);
        outputStream.writeByte(0x00);

        Map<DynamicVariable,String> dynamicDataMap = new HashMap<>();
        dynamicDataMap.put(DynamicVariable.USERNAME, username);
        String msg = MessageCompiler.insertDynamicData(loginMsg,dynamicDataMap);
        MinecraftProtocolUtils.writeString(outputStream, msg);
        MinecraftProtocolUtils.writePacket(out,byteArrayOutputStream);
    }

    protected void handleStatusConnection(DataInputStream in, DataOutputStream out) throws IOException {
        while(true){
            int packetLength = MinecraftProtocolUtils.readVarInt(in);
            if(packetLength == 0){
                logger.log(Level.FINEST,"Connection aborted");
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
        ByteArrayOutputStream dataArray = new ByteArrayOutputStream(32000);
        DataOutputStream data = new DataOutputStream(dataArray);
        data.writeByte(0x00);
        MinecraftProtocolUtils.writeString(data, responseMsg);
        MinecraftProtocolUtils.writePacket(out,dataArray);
    }
}
