package com.socotech.filter4bot;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.File;

/**
 * Created by IntelliJ IDEA. User: marc Date: Feb 8, 2007 Time: 4:13:37 PM
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class BotResourceFileTest {
    @Test
    public void testBotResources() {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            for (BotResourceFile file : BotResourceFile.values()) {
                File f = new File(loader.getResource(file.getFileName()).getFile());
                Assert.assertTrue(file.getFileName() + " does not exist", f.exists());
            }
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }
}