package cc.phantomhost.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface ConfigurationIO {

    void loadConfiguration(Map<Setting,String> configuration) throws IOException;

    void saveConfiguration(Map<Setting,String> configuration) throws IOException;
}
