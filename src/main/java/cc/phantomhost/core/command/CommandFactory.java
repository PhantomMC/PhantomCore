package cc.phantomhost.core.command;

import cc.phantomhost.core.PhantomCore;
import cc.phantomhost.core.PhantomServer;
import cc.phantomhost.core.utils.ConfigFactory;
import cc.phantomhost.core.utils.FileUtils;

import java.io.File;
import java.util.logging.Logger;

public class CommandFactory {

    public static PhantomCommand createCommand(String line, PhantomCore instance){
        String[] command = line.split(" ");
        Logger logger = instance.logger;
        return switch (command[0]) {
            case "help" -> new HelpCommand(logger);
            case "touch" , "info" -> new UnimplementedCommand(logger);
            case "stats" -> new UnimplementedCommand(logger);
            case "suspend" -> new UnimplementedCommand(logger);
            case "remove" -> new UnimplementedCommand(logger);
            case "toggle" -> new UnimplementedCommand(logger);
            case "ratelimit" -> new UnimplementedCommand(logger);
            case "reset" -> new UnimplementedCommand(logger);
            case "edit", "set" -> new EditCommand(command,instance);
            case "restart" -> new RestartCommand(instance);
            case "stop", "exit" -> new StopCommand(instance);
            case "alias" -> new UnimplementedCommand(logger);
            case "version" -> new VersionCommand(logger);
            default -> throw new IllegalArgumentException(String.format("Invalid command '%s'",command[0]));
        };
    }
}
