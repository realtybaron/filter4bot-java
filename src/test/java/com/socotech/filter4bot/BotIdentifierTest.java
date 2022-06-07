package com.socotech.filter4bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by IntelliJ IDEA. User: marc Date: Mar 5, 2007 Time: 7:56:14 AM
 */
public class BotIdentifierTest {

    private final BotIdentifier botIdentifier = new BotIdentifier();

    @BeforeEach
    public void setUp() {
        this.botIdentifier.init();
    }

    @Test
    public void ipAddressStrings() {
        Assertions.assertTrue(this.botIdentifier.isBotIpAddress("209.85.238.11"), "Valid user agent \"209.85.238.11\" not found");
    }

    @Test
    public void userAgentStrings() {
        Assertions.assertTrue(this.botIdentifier.isBotUserAgent("Googlebot/2.1 (+http://www.googlebot.com/bot.html)"), "Valid user agent not found");
        Assertions.assertTrue(this.botIdentifier.isBotUserAgent("Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)"), "Valid user agent not found");
        Assertions.assertTrue(this.botIdentifier.isBotUserAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)"), "Valid user agent not found");
    }

}
