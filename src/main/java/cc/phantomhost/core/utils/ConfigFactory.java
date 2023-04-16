package cc.phantomhost.core.utils;

import cc.phantomhost.core.protocol.setting.Configuration;
import cc.phantomhost.core.protocol.setting.PropertiesConfiguration;
import cc.phantomhost.core.protocol.setting.YamlConfiguration;

import java.io.*;

public class ConfigFactory {

    public static Configuration loadConfigurationFromFile(File file) throws IOException {
        String fileName = file.getName();
        if(fileName.endsWith(".properties")){
            try(InputStream inputStream = new FileInputStream(file)) {
                return new PropertiesConfiguration(inputStream);
            }
        }
        if(fileName.endsWith(".yaml") || fileName.endsWith(".yml")){
            try(InputStream inputStream = new FileInputStream(file)) {
                return new YamlConfiguration(inputStream);
            }
        }
        throw new IOException("Invalid filename");
    }
}
