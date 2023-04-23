package cc.phantomhost.core.config;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class YamlConfigurationIO implements ConfigurationIO{
    private final File file;

    public YamlConfigurationIO(File file){
        this.file = file;
    }
    @Override
    public void loadConfiguration(Map<Setting,String> configuration) throws IOException {
        try(InputStream inputStream = new FileInputStream(file)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.loadAs(inputStream, Map.class);
            data.forEach((key, value) -> configuration.put(Setting.getFromKey(key), value.toString()));
        }
    }

    @Override
    public void saveConfiguration(Map<Setting,String> configuration) throws IOException {
        try(OutputStream outputStream = new FileOutputStream(file)) {
            Yaml yaml = new Yaml();
            Writer writer = new OutputStreamWriter(outputStream);
            yaml.dump(configuration, writer);
        }
    }
}
