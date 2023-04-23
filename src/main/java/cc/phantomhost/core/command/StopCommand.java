package cc.phantomhost.core.command;

import cc.phantomhost.core.PhantomCore;

public class StopCommand implements PhantomCommand{
    private final PhantomCore instance;

    public StopCommand(PhantomCore instance){
        this.instance = instance;
    }
    @Override
    public void run() throws InterruptedException {
        instance.server.interrupt();
        throw new InterruptedException();
    }
}
