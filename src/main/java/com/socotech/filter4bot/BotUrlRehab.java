package com.socotech.filter4bot;

import java.io.IOException;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by IntelliJ IDEA. User: marc Date: Oct 4, 2006 Time: 5:37:41 AM
 */
@SuppressWarnings("unchecked")
public class BotUrlRehab implements Filter {
    /**
     * Identifies bots by their IP address and/or user agent
     */
    private BotIdentifier botIdentifier = new BotIdentifier();

    /**
     * Initialize the bot identifier
     *
     * @param config filter config
     * @throws ServletException
     */
    public void init(FilterConfig config) throws ServletException {
        this.botIdentifier.init();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = HttpServletRequest.class.cast(req);
        HttpServletResponse response = HttpServletResponse.class.cast(res);
        boolean isBotAddress = this.botIdentifier.isBotIpAddress(request);
        boolean isBotUserAgent = this.botIdentifier.isBotUserAgent(request);
        boolean isBot = isBotAddress || isBotUserAgent;
        log.info(request.getRemoteAddr() + " is a bot");
        boolean isSessionEncoded = request.isRequestedSessionIdFromURL();
        log.info(request.getRemoteAddr() + " has session ID encoded on URL");
        if (isBot && isSessionEncoded) {
            String jsessionid = ";jsessionid=" + request.getRequestedSessionId();
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            StringBuffer url = request.getRequestURL();
            if (StringUtils.isNotEmpty(request.getQueryString())) {
                url.append('?').append(request.getQueryString());
            }
            response.setHeader("Location", StringUtils.remove(url.toString(), jsessionid));
            response.setHeader("Connection", "close");
            response.flushBuffer();
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // noop
    }

    private static final Logger log = Logger.getLogger(BotUrlRehab.class.getName());
}
