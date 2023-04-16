package cc.phantomhost.core.utils.credential;

import java.io.*;
import java.util.Properties;

public class PhantomCredentials {

    private final Properties properties;

    public PhantomCredentials(File file){
        this.properties = new Properties();
        try(InputStream inputStream = new FileInputStream(file)) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public String getCredential(CredentialKey key){
        return properties.getProperty(key.toString().toLowerCase());
    }
}
