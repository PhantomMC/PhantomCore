package cc.phantomhost.core;

import cc.phantomhost.core.command.CommandFactory;
import cc.phantomhost.core.command.PhantomCommand;
import cc.phantomhost.core.protocol.setting.Configuration;
import cc.phantomhost.core.utils.ConfigFactory;
import cc.phantomhost.core.utils.FileUtils;
import cc.phantomhost.core.utils.credential.PhantomCredentials;

import java.io.*;
import java.net.URL;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PhantomCore{
    public PhantomServer server;
    public Configuration defaultConfig;
    public PhantomCredentials credentials;
    public static final Logger logger = createLogger();

    public static void main(String[] args) {
        try{
            PhantomCore core = new PhantomCore();
            core.run();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"An exception was thrown",e);
        }
    }

    private void run() throws IOException {
        generateDefaultConfig();
        credentials = new PhantomCredentials(new File(FileName.CREDENTIALS.getFileName()));
        logger.log(Level.INFO,FileUtils.readInternalFileToString(FileName.VERSION));

        defaultConfig = ConfigFactory.loadConfigurationFromFile(new File(FileName.DEFAULT_CONFIG_FILE.getFileName()));
        server = new PhantomServer(defaultConfig,logger);
        server.start();
        listenForCommands();
    }

    private void listenForCommands() throws IOException {
        try {
            while (true) {
                try {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(System.in));
                    PhantomCommand command = CommandFactory.createCommand(reader.readLine());
                    command.run(this);
                } catch (IllegalArgumentException e) {
                    logger.log(Level.INFO, e.getMessage());
                }
            }
        } catch (InterruptedException e){
            System.out.println("Bye!");
        }
    }

    private void generateDefaultConfig() throws IOException {
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
    private static Logger createLogger(){
        try(InputStream inputStream = PhantomCore.class.getResourceAsStream("/" + FileName.LOGGING_SETTINGS.getFileName())){
            LogManager.getLogManager().readConfiguration(inputStream);
            return Logger.getLogger(PhantomCore.class.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
