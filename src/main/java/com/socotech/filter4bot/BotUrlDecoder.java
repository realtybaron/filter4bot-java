package com.socotech.filter4bot;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: marc Date: Oct 4, 2006 Time: 5:37:41 AM
 */
@SuppressWarnings("unchecked")
public class BotUrlDecoder implements Filter {
    /**
     * Identifies bots by their IP address and/or user agent
     */
    private BotIdentifier botIdentifier;

    /**
     * Initialize the bot identifier
     *
     * @param config filter config
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        this.botIdentifier = new BotIdentifier().init();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        boolean isBotAddress = this.botIdentifier.isBotIpAddress(request);
        boolean isBotUserAgent = this.botIdentifier.isBotUserAgent(request);
        boolean isBot = isBotAddress || isBotUserAgent;
        if (isBot) {
            chain.doFilter(request, new StripSessionIdWrapper(response));
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        // noop
    }
}
