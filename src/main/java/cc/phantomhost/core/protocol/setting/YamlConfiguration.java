package cc.phantomhost.core.protocol.setting;

import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.Map;

public class YamlConfiguration implements Configuration {

    private final Map<String,Object> data;

    public YamlConfiguration(InputStream inputStream){
        Yaml yaml = new Yaml();
        data = yaml.loadAs(inputStream, Map.class);
    }

    @Override
    public String getSetting(Setting key) {
        return data.get(key.getKey()).toString();
    }

    @Override
    public void setSetting(Setting key, Object value) {
        data.put(key.getKey(),value);
    }

    @Override
    public void saveToStream(OutputStream stream) {
        Yaml yaml = new Yaml();
        Writer writer = new OutputStreamWriter(stream);
        yaml.dump(data, writer);
    }
}
