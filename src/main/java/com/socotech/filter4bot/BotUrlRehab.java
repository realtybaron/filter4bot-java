package com.socotech.filter4bot;

import com.google.common.base.Strings;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA. User: marc Date: Oct 4, 2006 Time: 5:37:41 AM
 */
public class BotUrlRehab implements Filter {
    /**
     * Identifies bots by their IP address and/or user agent
     */
    private BotIdentifier botIdentifier;

    /**
     * Initialize the bot identifier
     *
     * @param config filter config
     */
    public void init(FilterConfig config) {
        this.botIdentifier = new BotIdentifier().init();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        boolean isBotAddress = botIdentifier.isBotIpAddress(request);
        boolean isBotUserAgent = botIdentifier.isBotUserAgent(request);
        boolean isSessionEncoded = request.isRequestedSessionIdFromURL();
        boolean isBotAddressOrUserAgent = isBotAddress || isBotUserAgent;
        if (isBotAddressOrUserAgent && isSessionEncoded) {
            String jsessionid = ";jsessionid=" + request.getRequestedSessionId();
            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            StringBuffer url = request.getRequestURL();
            if (!Strings.isNullOrEmpty(request.getQueryString())) {
                url.append('?').append(request.getQueryString());
            }
            Locale locale = Locale.getDefault();
            response.setHeader("Location", url.toString().toLowerCase(locale).replace(jsessionid.toLowerCase(locale), ""));
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

}
