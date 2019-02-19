package com.socotech.filter4bot;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA. User: marc Date: Mar 5, 2007 Time: 7:56:14 AM
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class BotUrlRehabTest {
    @Test
    public void stripJSessionId() throws ServletException {
        BotUrlRehab filter = new BotUrlRehab();
        MockFilterConfig config = new MockFilterConfig();
        ServletContext context = config.getServletContext();
        filter.init(config);
        try {
            MockHttpSession session = new MockHttpSession(context);
            MockHttpServletRequest request = new MockHttpServletRequest(context);
            request.setSession(session);
            request.setRemoteAddr("66.249.72.72"); // google
            request.setServerName("www.realtybaron.com");
            request.setRequestURI("/search/foo;jsessionid=" + session.getId());
            request.setQueryString("bar=1");
            request.setRequestedSessionIdFromURL(true);
            MockHttpServletResponse response = new MockHttpServletResponse() {
                @Override
                public void flushBuffer() {
                    this.setCommitted(true);
                }
            };
            FilterChain chain = (request1, response1) -> Assert.fail("Filter did not detect spider with session ID on the URL");
            filter.doFilter(request, response, chain);
            Assert.assertEquals("Wrong status code", HttpServletResponse.SC_MOVED_PERMANENTLY, response.getStatus());
            Assert.assertEquals("Wrong redirect URL", "http://www.realtybaron.com/search/foo?bar=1", response.getHeader("Location"));
            Assert.assertTrue("Response was not committed", response.isCommitted());
        } catch (ServletException | IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}
