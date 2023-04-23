package cc.phantomhost.core.command;

import cc.phantomhost.core.PhantomCore;

import java.io.IOException;

public interface PhantomCommand {

    void run() throws InterruptedException, IOException;

}
