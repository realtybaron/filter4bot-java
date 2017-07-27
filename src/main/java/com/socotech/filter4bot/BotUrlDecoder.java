package com.socotech.filter4bot;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        HttpServletRequest request = HttpServletRequest.class.cast(req);
        HttpServletResponse response = HttpServletResponse.class.cast(res);
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
