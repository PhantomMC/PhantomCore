package cc.phantomhost.core.command;

import cc.phantomhost.core.PhantomCore;
import cc.phantomhost.core.protocol.setting.Configuration;
import cc.phantomhost.core.protocol.setting.MongoDBConfiguration;
import cc.phantomhost.core.protocol.setting.Setting;

import java.io.IOException;
import java.util.logging.Level;

public class EditCommand implements PhantomCommand{

    private final String[] command;

    public EditCommand(String[] command){
        this.command = command;
    }
    @Override
    public void run(PhantomCore instance) throws IOException {
        if(command.length < 3){
            instance.logger.log(Level.INFO, "Command lacks input arguments");
        }
        Configuration configuration;
        int settingKeyPosition;
        if(command.length < 4){
            settingKeyPosition = 1;
            configuration = instance.defaultConfig;
        } else {
            settingKeyPosition = 2;
            configuration = new MongoDBConfiguration(instance.credentials, command[1]);
        }
        Setting setting = Setting.getFromKey(command[settingKeyPosition]);
        configuration.setSetting(setting,command[settingKeyPosition+1]);
    }
}
