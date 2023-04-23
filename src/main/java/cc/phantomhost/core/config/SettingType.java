package cc.phantomhost.core.config;

import java.util.function.Function;

public enum SettingType {

    StringSetting((value) -> true),
    IntegerSetting(SettingType::isValidInteger),
    BooleanSetting(SettingType::isValidBoolean);


    private final Function<String, Boolean> validationChecker;

    SettingType(Function<String,Boolean> validationChecker){
        this.validationChecker = validationChecker;
    }

    public boolean isValidValue(String value){
        return validationChecker.apply(value);
    }

    private static boolean isValidInteger(String string){
        try{
            Integer.parseInt(string);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isValidBoolean(String string){
        try{
            Boolean.parseBoolean(string);
            return true;
        } catch(IllegalArgumentException e) {
            return false;
        }
    }
}
