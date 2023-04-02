package cc.phantomhost.core.protocol.minecraft;

import cc.phantomhost.core.protocol.Protocol;
import cc.phantomhost.core.utils.FileUtils;
import cc.phantomhost.core.utils.MessageCompiler;
import cc.phantomhost.core.utils.MinecraftProtocolUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MinecraftProtocol762 implements Protocol {

    private static String LOGIN_MESSAGE_PATH = "/loginMessage.json";
    private static String UNCOMPILED_LOGIN_MESSAGE = FileUtils.readInternalFileToString(LOGIN_MESSAGE_PATH);
    private static String RESPONSE_MESSAGE_PATH = "/responseMessage.json";
    private static String UNCOMPILED_RESPONSE_MESSAGE = FileUtils.readInternalFileToString(RESPONSE_MESSAGE_PATH);

    private final HandshakeData initialData;

    String responseMsg;
    String loginMsg;
    public MinecraftProtocol762(HandshakeData initialData, File imageLocation){
        this.initialData = initialData;
        Map<DisplayData,String> displayDataMap = new HashMap<>();
        displayDataMap.put(DisplayData.WARNING_LINE,"Waarning");
        displayDataMap.put(DisplayData.PROTOCOL_VERSION,String.valueOf(initialData.getProtocolVersion()));
        displayDataMap.put(DisplayData.HOVER_MESSAGE,MessageCompiler.compileHoverMessage(new String[]{"Test"}));
        displayDataMap.put(DisplayData.UPPER_LINE,"Hello world1");
        displayDataMap.put(DisplayData.BOTTOM_LINE,"Hello world");
        try {
            displayDataMap.put(DisplayData.IMAGE_DATA,MessageCompiler.compileImage(imageLocation));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        displayDataMap.put(DisplayData.LOGIN_MESSAGE,"Hello %USERNAME%!");


        responseMsg = MessageCompiler.compileMessage(UNCOMPILED_RESPONSE_MESSAGE, displayDataMap);
        loginMsg = MessageCompiler.compileMessage(UNCOMPILED_LOGIN_MESSAGE,displayDataMap);

        System.out.println(responseMsg);
        System.out.println(loginMsg);
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
        ByteArrayOutputStream dataArray = new ByteArrayOutputStream(32000);
        DataOutputStream data = new DataOutputStream(dataArray);
        data.writeByte(0x00);
        MinecraftProtocolUtils.writeString(data, responseMsg);
        MinecraftProtocolUtils.writePacket(out,dataArray);
    }
}
