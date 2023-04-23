package cc.phantomhost.core;

import cc.phantomhost.core.command.CommandFactory;
import cc.phantomhost.core.command.PhantomCommand;
import cc.phantomhost.core.config.Configuration;
import cc.phantomhost.core.config.Setting;
import cc.phantomhost.core.utils.FileUtils;
import cc.phantomhost.core.utils.credential.PhantomCredentials;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

public class PhantomCore{
    public PhantomServer server;
    public Map<Setting,String> defaultConfig;
    public final PhantomCredentials credentials;
    public final Logger logger;

    public PhantomCore(String[] args, Logger logger, Configuration defaultConfig) {
        this.logger = logger;
        this.defaultConfig = defaultConfig;
        credentials = new PhantomCredentials(new File(FileName.CREDENTIALS.getFileName()));
    }

    public void run() throws IOException {
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
                    PhantomCommand command = CommandFactory.createCommand(reader.readLine("\r>> "),this);
                    command.run();
                } catch (IllegalArgumentException e) {
                    logger.log(Level.INFO, e.getMessage());
                }
            }
        } catch (InterruptedException e){
            System.out.println("Bye!");
        }
    }
}
