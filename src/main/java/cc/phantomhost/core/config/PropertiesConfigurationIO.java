package cc.phantomhost.core.config;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class PropertiesConfigurationIO implements ConfigurationIO{

    private final File file;

    public PropertiesConfigurationIO(File file){
        this.file = file;
    }

    @Override
    public void loadConfiguration(Map<Setting,String> configuration) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            for (Object key : properties.keySet()) {
                configuration.put(Setting.getFromKey(key.toString()), properties.getProperty(key.toString()));
            }
        }
    }

    @Override
    public void saveConfiguration(Map<Setting,String> configuration) throws IOException {
        try(OutputStream outputStream = new FileOutputStream(file)) {
            Properties properties = new Properties();
            for (Object key : configuration.keySet()) {
                properties.setProperty(key.toString(), configuration.get(Setting.getFromKey(key.toString())));
            }
            properties.store(outputStream, "");
        }
    }
}
