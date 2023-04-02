package cc.phantomhost.core.utils;

import cc.phantomhost.core.protocol.minecraft.DisplayData;
import cc.phantomhost.core.protocol.minecraft.DynamicVariable;

import java.io.*;
import java.util.Base64;
import java.util.Map;

public class MessageCompiler {
    private static String[] uuids = {"d2b440c3-edde-4443-899e-6825c31d0919"};
    private static int uuidCounter = 0;

    public static String compileMessage(String uncompiledMessage, Map<DisplayData,String> insertionData){
        for(DisplayData key : insertionData.keySet() ){
            uncompiledMessage = uncompiledMessage.replaceAll("%" + key + "%", insertionData.get(key));
        }
        return uncompiledMessage;
    }

    public static String insertDynamicData(String uncompiledMessage, Map<DynamicVariable,String> dynamicData){
        for(DynamicVariable key : dynamicData.keySet()){
            // TODO make this work with more dynamic things and stuff
            uncompiledMessage = uncompiledMessage.replaceAll("%" + key + "%",dynamicData.get(key));
        }
        return uncompiledMessage;
    }

    public static String compileHoverMessage(String[] lines){
        StringBuilder builder = new StringBuilder();
        for(String line : lines){
            String uuid = uuids[uuidCounter];
            builder.append(String.format("{\"name\":\"%s\",\"id\":\"%s\"}",line,uuid));
        }
        return builder.toString();
    }

    public static String compileImage(File imageFile) throws IOException {
        FileInputStream in = new FileInputStream(imageFile);
        byte[] fileContent = in.readAllBytes();
        String image = Base64.getEncoder().encodeToString(fileContent);
        return "\"favicon\":\"data:image/png;base64,<data>\",".replaceAll("<data>",image);
    }
}
