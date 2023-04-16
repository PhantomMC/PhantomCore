package cc.phantomhost.core.protocol.setting;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public enum Setting {
    PROTOCOL_VERSION_RESPONSE("type", new Object[]{1, 2, 3}),

    WARNING_LINE("warning", String.class),

    HOVER_MESSAGE("hover", String.class),

    MOTD("motd", String.class),

    LOGIN_MESSAGE("login", String.class),

    IMAGE_LOCATION("imageLocation", String.class);



    private final String key;
    private Object[] possibleValues = null;
    private Class<?> settingClassType = null;

    Setting(String key, Object[] possibleValues) {
        this.key  = key;
        this.possibleValues = possibleValues;
    }

    Setting(String key, Class<?> settingClassType) {
        this.key = key;
        this.settingClassType = settingClassType;
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

    public void assertRightConfigValue(Object configValue){
        if(possibleValues != null){
            for(Object possibleValue : possibleValues){
                if(possibleValue.toString().equals(configValue)){
                    return;
                }
            }
            throw new IllegalArgumentException(String.format("Invalid configuration value '%s' expected any of '%s'"
                    , configValue.toString(), Arrays.toString(possibleValues)));
        }
        if (settingClassType != null && !settingClassType.isInstance(configValue)) {
            throw new IllegalArgumentException(String.format("Expected value of class '%s' got '%s'"
                    ,settingClassType.getName(),configValue.toString()));
        }
        throw new RuntimeException("Invalid code bracket reached");
    }
}
