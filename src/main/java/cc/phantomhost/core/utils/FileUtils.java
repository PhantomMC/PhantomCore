package cc.phantomhost.core.utils;

import cc.phantomhost.core.PhantomCore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static String readInternalFileToString(String filePath){
        try {
            return new String(PhantomCore.class.getResourceAsStream(filePath).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
