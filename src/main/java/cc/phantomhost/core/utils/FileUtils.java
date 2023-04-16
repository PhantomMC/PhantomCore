package cc.phantomhost.core.utils;

import cc.phantomhost.core.FileName;
import cc.phantomhost.core.PhantomCore;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static String readInternalFileToString(FileName filePath){
        try(InputStream inputStream = PhantomCore.class.getResourceAsStream("/" + filePath.getFileName())) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
