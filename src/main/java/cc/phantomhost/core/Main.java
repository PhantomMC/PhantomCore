package cc.phantomhost.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = createLogger();
        try{
            PhantomCore core = new PhantomCore(args, logger);
            core.run();
        } catch (Exception e) {
            logger.log(Level.SEVERE,"An exception was thrown",e);
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
