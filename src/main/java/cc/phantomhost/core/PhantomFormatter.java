package cc.phantomhost.core;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class PhantomFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        return String.format("[%s, %s]: %s",getTimeString(),record.getLevel(),record.getMessage());
    }

    private String getTimeString(){
        return String.valueOf(System.currentTimeMillis());
    }
}
