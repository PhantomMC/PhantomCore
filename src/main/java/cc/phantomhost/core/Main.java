package cc.phantomhost.core;

import cc.phantomhost.core.config.Configuration;
import cc.phantomhost.core.config.Setting;
import cc.phantomhost.core.utils.ConfigFactory;
import cc.phantomhost.core.utils.OptionParser;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Configuration defaultConfig;
        try {
            generateDefaultConfig();
            defaultConfig = ConfigFactory.loadConfigurationFromFile(new File(FileName.DEFAULT_CONFIG_FILE.getFileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        OptionParser parser = new OptionParser(defaultConfig);
        parser.parse(args);

        Logger logger = createLogger(defaultConfig.get(Setting.LEVEL));
        try{
            PhantomCore core = new PhantomCore(args, logger, defaultConfig);
            core.run();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"An exception was thrown",e);
        }
    }
    private static Logger createLogger(String logLevelName){
        Level logLevel = Level.parse(logLevelName.toUpperCase());
        try(InputStream inputStream = PhantomCore.class.getResourceAsStream("/" + FileName.LOGGING_SETTINGS.getFileName())){
            LogManager.getLogManager().readConfiguration(inputStream);
            Logger logger = Logger.getLogger(PhantomCore.class.getName());
            logger.setLevel(logLevel);
            return logger;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void generateDefaultConfig() throws IOException {
        File configFile = new File(FileName.DEFAULT_CONFIG_FILE.getFileName());
        if(configFile.exists()){
            return;
        }
        try(InputStream inputStream = PhantomCore.class.getResourceAsStream("/" + FileName.DEFAULT_CONFIG_FILE.getFileName())) {
            if (inputStream == null) {
                throw new IOException("Invalid internal resource");
            }
            try (OutputStream outputStream = new FileOutputStream(configFile)) {
                inputStream.transferTo(outputStream);
            }
        }
    }
}
