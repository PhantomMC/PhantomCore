package cc.phantomhost.core.protocol.setting;

import java.io.IOException;
import java.io.OutputStream;

public interface Configuration {

    String getSetting(Setting key);

    void setSetting(Setting key, Object value);

    void saveToStream(OutputStream stream) throws IOException;
}
