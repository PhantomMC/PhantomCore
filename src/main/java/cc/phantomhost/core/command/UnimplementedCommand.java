package cc.phantomhost.core.command;

import cc.phantomhost.core.PhantomCore;

import java.util.logging.Level;
import java.util.logging.Logger;

public class UnimplementedCommand implements PhantomCommand{
    private final Logger logger;

    public UnimplementedCommand(Logger logger){
        this.logger = logger;
    }
    @Override
    public void run() {
        logger.log(Level.INFO, "Not implemented yet :3");
    }
}
