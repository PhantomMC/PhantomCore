package cc.phantomhost.core.protocol.minecraft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MinecraftProtocol762Test {

    private MinecraftProtocol762 protocol;

    @BeforeEach
    void setUp(){
        this.protocol = new MinecraftProtocol762();
    }


    @Test
    void initialResponseTest(){
        protocol.getResponse("");
    }




}
