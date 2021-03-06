package com.socotech.filter4bot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import javax.servlet.ServletException;

/**
 * Created by IntelliJ IDEA. User: marc Date: Mar 5, 2007 Time: 7:56:14 AM
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class BotIdentifierTest {

    private final BotIdentifier botIdentifier = new BotIdentifier();

    @Before
    public void setUp() {
        this.botIdentifier.init();
    }

    @Test
    public void ipAddressStrings() throws ServletException {
        Assert.assertTrue("Valid user agent \"209.85.238.11\" not found", this.botIdentifier.isBotIpAddress("209.85.238.11"));
    }

    @Test
    public void userAgentStrings() throws ServletException {
        Assert.assertTrue("Valid user agent not found", this.botIdentifier.isBotUserAgent("Googlebot/2.1 (+http://www.googlebot.com/bot.html)"));
        Assert.assertTrue("Valid user agent not found", this.botIdentifier.isBotUserAgent("Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)"));
        Assert.assertTrue("Valid user agent not found", this.botIdentifier.isBotUserAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"));
    }

}
