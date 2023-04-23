package cc.phantomhost.core.command;

import cc.phantomhost.core.FileName;
import cc.phantomhost.core.PhantomCore;
import cc.phantomhost.core.PhantomServer;
import cc.phantomhost.core.utils.ConfigFactory;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class RestartCommand implements PhantomCommand{

    private final PhantomCore instance;

    public RestartCommand(PhantomCore instance){
        this.instance = instance;
    }
    @Override
    public void run() throws IOException {
        instance.defaultConfig = ConfigFactory.loadConfigurationFromFile(new File(FileName.DEFAULT_CONFIG_FILE.getFileName()));
        instance.server.interrupt();
        instance.server = new PhantomServer(instance.defaultConfig,instance.logger);
        instance.logger.log(Level.INFO, "Restarting...");
        instance.server.start();
    }
}
