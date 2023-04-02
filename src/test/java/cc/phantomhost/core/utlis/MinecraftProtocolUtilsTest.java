package cc.phantomhost.core.utlis;

import cc.phantomhost.core.utils.MinecraftProtocolUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;

public class MinecraftProtocolUtilsTest {

    @Test
    void readWriteString() throws IOException {
        String message = "Hello world!";
        ByteArrayOutputStream out = new ByteArrayOutputStream(2048);
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        MinecraftProtocolUtils.writeString(dataOutputStream,message);

        InputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataInputStream = new DataInputStream(in);

        Assertions.assertEquals(message,MinecraftProtocolUtils.readString(dataInputStream));
    }

    @Test
    void readWriteStringTwice() throws IOException {
        String message1 = "Hello world!";
        String message2 = "Hello other world!";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        MinecraftProtocolUtils.writeString(dataOutputStream,message1);
        MinecraftProtocolUtils.writeString(dataOutputStream,message2);

        InputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataInputStream = new DataInputStream(in);

        Assertions.assertEquals(message1,MinecraftProtocolUtils.readString(dataInputStream));
        Assertions.assertEquals(message2,MinecraftProtocolUtils.readString(dataInputStream));
    }

    @Test
    void readWriteVarInt() throws IOException{
        int varInt = 42;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(out);
        MinecraftProtocolUtils.writeVarInt(dataOutputStream,varInt);

        InputStream in = new ByteArrayInputStream(out.toByteArray());
        DataInputStream dataInputStream = new DataInputStream(in);

        Assertions.assertEquals(varInt,MinecraftProtocolUtils.readVarInt(dataInputStream));
    }
}
