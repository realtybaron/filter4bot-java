package com.socotech.filter4bot;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        try {
            HttpServletRequest request = HttpServletRequest.class.cast(req);
            HttpServletResponse response = HttpServletResponse.class.cast(res);
            boolean isBotAddress = this.botIdentifier.isBotIpAddress(request);
            boolean isBotUserAgent = this.botIdentifier.isBotUserAgent(request);
            boolean isBot = isBotAddress || isBotUserAgent;
            if (isBot && log.isDebugEnabled()) {
                log.debug(request.getRemoteAddr() + " is a bot");
            }
            boolean isSessionEncoded = request.isRequestedSessionIdFromURL();
            if (isSessionEncoded && log.isDebugEnabled()) {
                log.debug(request.getRemoteAddr() + " has session ID encoded on URL");
            }
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
        } catch (IOException | ServletException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void destroy() {
        // noop
    }

    private static Logger log = LoggerFactory.getLogger(BotUrlRehab.class);
}
