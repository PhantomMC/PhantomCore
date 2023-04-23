package cc.phantomhost.core.command;

import cc.phantomhost.core.FileName;
import cc.phantomhost.core.PhantomCore;
import cc.phantomhost.core.utils.FileUtils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VersionCommand implements PhantomCommand {
    private final Logger logger;

    public VersionCommand(Logger logger){
        this.logger = logger;
    }
    @Override
    public void run() throws InterruptedException, IOException {
        logger.log(Level.INFO, FileUtils.readInternalFileToString(FileName.VERSION));
    }
}
