package com.socotech.filter4bot;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * This is a wrapper that does not encode urls with JSESSIONs
 */
public class StripSessionIdWrapper extends HttpServletResponseWrapper {
    /**
     * Constructor
     *
     * @param response HTTP response
     */
    public StripSessionIdWrapper(HttpServletResponse response) {
        super(response);
    }

    public String encodeUrl(String url) {
        return url;
    }

    public String encodeURL(String url) {
        return url;
    }

    public String encodeRedirectURL(String url) {
        return url;
    }
}
