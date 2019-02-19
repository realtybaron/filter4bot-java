package com.socotech.filter4bot;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: marc Date: Mar 5, 2007 Time: 7:56:14 AM
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class BotUrlDecoderTest {
    @Test
    public void wrapResponse() throws ServletException {
        BotUrlDecoder filter = new BotUrlDecoder();
        MockFilterConfig config = new MockFilterConfig();
        ServletContext context = config.getServletContext();
        filter.init(config);
        try {
            MockHttpSession session = new MockHttpSession(context);
            MockHttpServletRequest request = new MockHttpServletRequest(context);
            request.setSession(session);
            request.setRemoteAddr("66.249.72.72"); // google
            request.setServerName("www.realtybaron.com");
            request.setRequestURI("/search/foo");
            request.setQueryString("bar=1");
            request.setRequestedSessionIdFromURL(true);
            MockHttpServletResponse response = new MockHttpServletResponse() {
                @Override
                public void flushBuffer() {
                    this.setCommitted(true);
                }
            };
            FilterChain chain = new FilterChain() {
                public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
                    Assert.assertEquals("Response is not wrapped", StripSessionIdWrapper.class, response.getClass());
                }
            };
            filter.doFilter(request, response, chain);
        } catch (ServletException | IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}