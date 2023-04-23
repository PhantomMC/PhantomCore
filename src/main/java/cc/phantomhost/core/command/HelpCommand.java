package cc.phantomhost.core.command;

import cc.phantomhost.core.FileName;
import cc.phantomhost.core.PhantomCore;
import cc.phantomhost.core.utils.FileUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class HelpCommand implements PhantomCommand{

    private final Logger logger;

    public HelpCommand(Logger logger){
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.log(Level.INFO, FileUtils.readInternalFileToString(FileName.HELP));
    }
}
