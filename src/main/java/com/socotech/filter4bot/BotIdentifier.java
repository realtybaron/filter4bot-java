package com.socotech.filter4bot;

import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA. User: marc Date: Oct 16, 2010 Time: 7:21:07 AM
 */
public class BotIdentifier {
    private List<String> botIpAddress = Lists.newArrayList();
    private List<String> botUserAgents = Lists.newArrayList();

    /**
     * Build list of bot IP addresses
     *
     * @return this for method chaining
     */
    public BotIdentifier init() {
        try {
            Pattern pattern = Pattern.compile("^# UA \"([^\"]*)\"$");
            for (BotResourceFile file : BotResourceFile.values()) {
                URL url = URLs.findClasspathResource(file.getFileName());
                LineIterator it = IOUtils.lineIterator(url.openStream(), "UTF-8");
                try {
                    while (it.hasNext()) {
                        String line = it.nextLine();
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.matches()) {
                            this.botUserAgents.add(matcher.group(1).trim());
                        } else {
                            this.botIpAddress.add(line.trim());
                        }
                    }
                } finally {
                    LineIterator.closeQuietly(it);
                }
                log.info(this.botIpAddress.size() + " IP addresses loaded from " + file);
                log.info(this.botUserAgents.size() + " user agents loaded from " + file);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return this;
    }

    public boolean isBot(HttpServletRequest request) {
        return this.isBotUserAgent(request) || this.isBotIpAddress(request);
    }

    public boolean isNotBot(HttpServletRequest request) {
        return !this.isBot(request);
    }

    public boolean isBotIpAddress(String ipAddress) {
        return RequestUtils.isIpAddressPresent(this.botIpAddress, ipAddress);
    }

    public boolean isBotIpAddress(HttpServletRequest request) {
        return RequestUtils.isIpAddressPresent(this.botIpAddress, request);
    }

    public boolean isBotUserAgent(String userAgent) {
        return this.botUserAgents.contains(userAgent);
    }

    public boolean isBotUserAgent(HttpServletRequest request) {
        return this.isBotUserAgent(request.getHeader("User-Agent"));
    }

    /**
     * Logger
     */
    private static Logger log = LoggerFactory.getLogger(BotUrlRehab.class);
}
