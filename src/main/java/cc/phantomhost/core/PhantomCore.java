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

import org.jline.console.ConsoleEngine;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

public class PhantomCore{
    public PhantomServer server;
    public Configuration defaultConfig;
    public final PhantomCredentials credentials;
    public final Logger logger;

    public PhantomCore(String[] args, Logger logger) {
        this.logger = logger;
        credentials = new PhantomCredentials(new File(FileName.CREDENTIALS.getFileName()));
        try {
            defaultConfig = ConfigFactory.loadConfigurationFromFile(new File(FileName.DEFAULT_CONFIG_FILE.getFileName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() throws IOException {
        generateDefaultConfig();
        logger.log(Level.INFO,FileUtils.readInternalFileToString(FileName.VERSION));

        server = new PhantomServer(defaultConfig,logger);
        server.start();
        listenForCommands();
    }

    private void listenForCommands() throws IOException {
        LineReader reader = LineReaderBuilder.builder().build();
        try {
            while (true) {
                try {
                    PhantomCommand command = CommandFactory.createCommand(reader.readLine(""));
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
}
