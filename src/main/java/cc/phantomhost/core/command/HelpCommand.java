package cc.phantomhost.core.command;

import cc.phantomhost.core.FileName;
import cc.phantomhost.core.PhantomCore;
import cc.phantomhost.core.utils.FileUtils;

import java.util.logging.Level;

public class HelpCommand implements PhantomCommand{

    @Override
    public void run(PhantomCore instance) {
        instance.logger.log(Level.INFO, FileUtils.readInternalFileToString(FileName.HELP));
    }
}
