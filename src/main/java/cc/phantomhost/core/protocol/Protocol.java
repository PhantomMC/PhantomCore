package cc.phantomhost.core.protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Protocol {
    public void handleClient(DataInputStream in, DataOutputStream out) throws IOException;
}
