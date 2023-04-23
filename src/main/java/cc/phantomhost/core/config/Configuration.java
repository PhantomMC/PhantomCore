package cc.phantomhost.core.config;

import java.util.HashMap;
import java.util.Map;

public class Configuration extends HashMap<Setting,String> {

    @Override
    public String put(Setting setting, String key){
        setting.assertRightConfigValue(key);
        return super.put(setting,key);
    }

    @Override
    public void putAll(Map<? extends Setting, ? extends String> mappings){
        mappings.forEach(Setting::assertRightConfigValue);
        super.putAll(mappings);
    }
}
