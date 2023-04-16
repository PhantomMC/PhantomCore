package cc.phantomhost.core;

public enum FileName {
    DEFAULT_CONFIG_FILE("config.yml"),
    CREDENTIALS("credentials.properties"),
    VERSION("version.txt"),
    LOGIN_MESSAGE("loginMessage.json"),
    RESPONSE_MESSAGE("responseMessage.json"),
    HELP("help.txt"),
    LOGGING_SETTINGS("logging.properties");
    private final String fileName;

    FileName(String name) {
        this.fileName = name;
    }

    public String getFileName(){
        return fileName;
    }
}
