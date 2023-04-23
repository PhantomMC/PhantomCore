package cc.phantomhost.core.utils;

import cc.phantomhost.core.config.*;

import java.io.*;
import java.util.Map;

public class ConfigFactory {

    public static Configuration loadConfigurationFromFile(File file) throws IOException {
        Configuration configuration = getDefaultConfiguration();
        String fileName = file.getName();

        ConfigurationIO configurationIO;
        if(fileName.endsWith(".properties")){
            configurationIO = new PropertiesConfigurationIO(file);
        }
        else if(fileName.endsWith(".yaml") || fileName.endsWith(".yml")){
            try(InputStream inputStream = new FileInputStream(file)) {
                configurationIO = new YamlConfigurationIO(file);
            }
        } else {
            throw new IOException("Invalid filename");
        }
        configurationIO.loadConfiguration(configuration);
        return configuration;
    }

    public static Configuration getDefaultConfiguration(){
        Configuration configuration = new Configuration();
        for(Setting setting : Setting.values()){
            configuration.put(setting,setting.getDefaultValue());
        }
        return configuration;
    }
}
