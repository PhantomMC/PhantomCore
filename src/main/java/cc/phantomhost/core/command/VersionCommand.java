package cc.phantomhost.core.command;

import cc.phantomhost.core.FileName;
import cc.phantomhost.core.PhantomCore;
import cc.phantomhost.core.utils.FileUtils;

import java.io.IOException;
import java.util.logging.Level;

public class VersionCommand implements PhantomCommand {
    @Override
    public void run(PhantomCore instance) throws InterruptedException, IOException {
        instance.logger.log(Level.INFO, FileUtils.readInternalFileToString(FileName.VERSION));
    }
}
