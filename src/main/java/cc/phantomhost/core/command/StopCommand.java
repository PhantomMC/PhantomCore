package cc.phantomhost.core.command;

import cc.phantomhost.core.PhantomCore;

public class StopCommand implements PhantomCommand{
    @Override
    public void run(PhantomCore instance) throws InterruptedException {
        instance.server.interrupt();
        throw new InterruptedException();
    }
}
