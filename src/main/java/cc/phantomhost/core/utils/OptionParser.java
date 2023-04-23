package cc.phantomhost.core.utils;

import cc.phantomhost.core.config.Setting;
import cc.phantomhost.core.config.SettingType;

import java.util.Map;

public class OptionParser {

    private final Map<Setting,String> configuration;

    public OptionParser(Map<Setting,String> configuration){
        this.configuration = configuration;
    }

    public void parse(String[] args){
        Setting key = null;
        for(String argument : args){
            if(key == null){
                Setting setting = Setting.valueOf(argument.toUpperCase().replaceAll("^--",""));
                if(setting.getSettingType().equals(SettingType.BooleanSetting)){
                    configuration.put(setting, String.valueOf(true));
                    continue;
                }
                key = setting;
            } else {
                configuration.put(key,argument);
                key = null;
            }
        }
    }
}
