package cc.phantomhost.core.protocol.setting;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesConfiguration implements Configuration{

    private final Properties properties;

    /**
     *
     * @param inputStream <p> A input stream pointing to a properties file</p>
     * @throws IOException
     */
    public PropertiesConfiguration(InputStream inputStream) throws IOException {
        this.properties = new Properties();
        properties.load(inputStream);
    }

    @Override
    public String getSetting(Setting key) {
        return properties.getProperty(key.getKey());
    }

    @Override
    public void setSetting(Setting key, Object value) {
        key.assertRightConfigValue(value);
        properties.setProperty(key.getKey(), value.toString());
    }

    @Override
    public void saveToStream(OutputStream stream) throws IOException {
        properties.store(stream,"");
    }
}
