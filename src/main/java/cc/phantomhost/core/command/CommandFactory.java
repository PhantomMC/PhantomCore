package cc.phantomhost.core.command;

import cc.phantomhost.core.PhantomServer;
import cc.phantomhost.core.utils.ConfigFactory;
import cc.phantomhost.core.utils.FileUtils;

import java.io.File;

public class CommandFactory {

    public static PhantomCommand createCommand(String line){
        String[] command = line.split(" ");
        return switch (command[0]) {
            case "help" -> new HelpCommand();
            case "touch" , "info" -> new UnimplementedCommand();
            case "stats" -> new UnimplementedCommand();
            case "suspend" -> new UnimplementedCommand();
            case "remove" -> new UnimplementedCommand();
            case "toggle" -> new UnimplementedCommand();
            case "ratelimit" -> new UnimplementedCommand();
            case "reset" -> new UnimplementedCommand();
            case "edit", "set" -> new EditCommand(command);
            case "restart" -> new RestartCommand();
            case "stop", "exit" -> new StopCommand();
            case "alias" -> new UnimplementedCommand();
            case "version" -> new VersionCommand();
            default -> throw new IllegalArgumentException(String.format("Invalid command '%s'",command[0]));
        };
    }
}
