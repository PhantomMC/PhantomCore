package cc.phantomhost.core.command;

import cc.phantomhost.core.PhantomCore;

import java.util.logging.Level;

public class UnimplementedCommand implements PhantomCommand{
    @Override
    public void run(PhantomCore instance) {
        instance.logger.log(Level.INFO, "Not implemented yet :3");
    }
}
