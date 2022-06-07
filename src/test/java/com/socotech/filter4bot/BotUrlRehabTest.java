package com.socotech.filter4bot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
public class BotUrlRehabTest {
    @Test
    public void stripJSessionId() {
        BotUrlRehab filter = new BotUrlRehab();
        MockFilterConfig config = new MockFilterConfig();
        ServletContext context = config.getServletContext();
        filter.init(config);
        try {
            MockHttpSession session = new MockHttpSession(context, "ABC123");
            MockHttpServletRequest request = new MockHttpServletRequest(context);
            request.setSession(session);
            request.setRemoteAddr("66.249.72.72"); // google
            request.setServerName("www.realtybaron.com");
            request.setRequestURI("/search/foo;jsessionid=" + session.getId());
            request.setQueryString("bar=1");
            request.setRequestedSessionId("ABC123");
            request.setRequestedSessionIdFromURL(true);
            request.setRequestedSessionIdFromCookie(false);
            MockHttpServletResponse response = new MockHttpServletResponse() {
                @Override
                public void flushBuffer() {
                    this.setCommitted(true);
                }
            };
            FilterChain chain = (request1, response1) -> Assertions.fail("Filter did not detect spider with session ID on the URL");
            filter.doFilter(request, response, chain);
            Assertions.assertEquals(HttpServletResponse.SC_MOVED_PERMANENTLY, response.getStatus(), "Wrong status code");
            Assertions.assertEquals("http://www.realtybaron.com/search/foo?bar=1", response.getHeader("Location"), "Wrong redirect URL");
            Assertions.assertTrue(response.isCommitted(), "Response was not committed");
        } catch (ServletException | IOException e) {
            Assertions.fail(e.getMessage());
        }
    }
}
