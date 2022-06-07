package com.socotech.filter4bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

/**
 * Created by IntelliJ IDEA. User: marc Date: Feb 8, 2007 Time: 4:13:37 PM
 */
public class BotResourceFileTest {
    @Test
    public void testBotResources() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            for (BotResourceFile file : BotResourceFile.values()) {
                File f = new File(loader.getResource(file.getFileName()).getFile());
                Assertions.assertTrue(f.exists(), file.getFileName() + " does not exist");
            }
        } catch (Exception e) {
            Assertions.fail(e.toString());
        }
    }
}