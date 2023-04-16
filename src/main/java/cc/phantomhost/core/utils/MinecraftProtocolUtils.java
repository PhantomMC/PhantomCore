package cc.phantomhost.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MinecraftProtocolUtils {

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public static int readVarInt(DataInputStream in) throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = in.readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }
        return value;
    }

    public static void writeVarInt(DataOutputStream out, int value) throws IOException {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                out.writeByte(value);
                return;
            }
            out.writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }

    public static long readVarLong(DataInputStream in) throws IOException {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = in.readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 64) throw new RuntimeException("VarLong is too big");
        }

        return value;
    }

    public static void writeVarLong(DataOutputStream out, long value) throws IOException {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                out.writeByte((int) value);
                return;
            }

            out.writeByte((int)((value & SEGMENT_BITS) | CONTINUE_BIT));

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }

    public static String readString(DataInputStream in) throws IOException {
        byte[] data = readFully(in);
        return new String(data, StandardCharsets.UTF_8);
    }

    public static void writeString(DataOutputStream out, String aString) throws IOException {
        byte[] byteArray = aString.getBytes(StandardCharsets.UTF_8);
        writeVarInt(out,byteArray.length);
        out.write(byteArray);
    }

    public static byte[] readFully(DataInputStream in) throws IOException {
        int length = readVarInt(in);
        return in.readNBytes(length);
    }

    public static void writePacket(DataOutputStream out, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        writeVarInt(out, byteArray.length);
        out.write(byteArray);
    }
}
