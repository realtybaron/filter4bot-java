package com.socotech.filter4bot;

import com.google.common.base.Strings;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

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
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        boolean isBotAddress = this.botIdentifier.isBotIpAddress(request);
        boolean isBotUserAgent = this.botIdentifier.isBotUserAgent(request);
        boolean isSessionEncoded = request.isRequestedSessionIdFromURL();
        boolean isBot = isBotAddress || isBotUserAgent;
        if (isBot) {
            log.info(request.getRemoteAddr() + " is a bot");
        }
        if (isSessionEncoded) {
            log.info(request.getRemoteAddr() + " has session ID encoded on URL");
        }
        if (isBot && isSessionEncoded) {
            String jsessionid = ";jsessionid=" + request.getSession().getId();
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            StringBuffer url = request.getRequestURL();
            if (!Strings.isNullOrEmpty(request.getQueryString())) {
                url.append('?').append(request.getQueryString());
            }
            response.setHeader("Location", url.toString().toLowerCase().replace(jsessionid, ""));
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
