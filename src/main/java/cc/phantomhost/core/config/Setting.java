package cc.phantomhost.core.config;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.logging.Level;

public enum Setting {
    PROTOCOL_VERSION_RESPONSE("type", SettingType.IntegerSetting , 1, new Object[]{1, 2, 3}),

    WARNING_LINE("warning", SettingType.StringSetting, "",null),

    HOVER_MESSAGE("hover", SettingType.StringSetting, "",null),

    MOTD("motd", SettingType.StringSetting, "",null),

    LOGIN_MESSAGE("login", SettingType.StringSetting, "",null),

    IMAGE_LOCATION("imageLocation", SettingType.StringSetting, "favicon.png",null),

    LEVEL("logLevel", SettingType.StringSetting, Level.INFO,null);



    private final String key;
    private final String defaultValue;
    private final Object[] possibleValues;
    private final SettingType settingType;

    Setting(String key, SettingType settingType, Object defaultValue, @Nullable Object[] possibleValues) {
        this.key = key;
        this.settingType = settingType;
        this.defaultValue = defaultValue.toString();
        this.possibleValues = possibleValues;
    }

    public static Setting getFromKey(String key) {
        for (Setting setting: Setting.values()) {
            if(setting.key.equals(key)){
                return setting;
            }
        }
        throw new IllegalArgumentException(String.format("Unknown key '%s'",key));
    }

    public Object[] getPossibleValues(){
        return possibleValues;
    }

    public String getKey(){
        return key;
    }

    public SettingType getSettingType(){
        return settingType;
    }

    public void assertRightConfigValue(String configValue){
        if(possibleValues != null){
            for(Object possibleValue : possibleValues){
                if (possibleValue.toString().equals(configValue)){
                    return;
                }
            }
            throw  new IllegalArgumentException(String.format("Expected value out of '%s' got '%s'"
                    , Arrays.toString(possibleValues), configValue));
        }


        if(!settingType.isValidValue(configValue)) {
            throw new IllegalArgumentException(String.format("Expected value of setting type '%s' got '%s'"
                    , settingType, configValue));
        }
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
