package cc.phantomhost.core.command;

import cc.phantomhost.core.PhantomCore;
import cc.phantomhost.core.config.MongoDBConfigurationIO;
import cc.phantomhost.core.config.Setting;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class EditCommand implements PhantomCommand{

    private final String[] command;
    private final PhantomCore instance;

    public EditCommand(String[] command,PhantomCore instance){
        this.command = command;
        this.instance = instance;
    }
    @Override
    public void run() throws IOException {
        if(command.length < 3){
            instance.logger.log(Level.INFO, "Command requires 4 or 5 arguments");
        }
        Map<Setting,String> configuration;
        int settingKeyPosition;
        if(command.length < 4){
            settingKeyPosition = 1;
            configuration = instance.defaultConfig;
        } else {
            settingKeyPosition = 2;
            configuration = new HashMap<>();
            MongoDBConfigurationIO mongodbIO = new MongoDBConfigurationIO(instance.credentials, command[1]);
            mongodbIO.loadConfiguration(configuration);
        }
        Setting setting = Setting.getFromKey(command[settingKeyPosition]);
        String key = command[settingKeyPosition+1];
        configuration.put(setting,key);
    }
}
