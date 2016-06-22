package com.socotech.filter4bot;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: marc Date: Oct 18, 2006 Time: 10:04:10 AM
 */
public class RequestUtils {
    /**
     * Get an attribute of some undetermined type
     *
     * @param request web request
     * @param name    attribute name
     * @param <T>     attribute type
     * @return attribute value
     */
    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(HttpServletRequest request, String name) {
        return (T) request.getAttribute(name);
    }

    /**
     * Append the query string to the request URL
     *
     * @param request web request
     * @return full url
     */
    public static String getAbsoluteURL(HttpServletRequest request) {
        StringBuffer buffer = request.getRequestURL();
        if (request.getQueryString() != null) {
            buffer.append('?').append(request.getQueryString());
        }
        return buffer.toString();
    }

    /**
     * Determine if an IP address is a member of a list
     *
     * @param list    the list
     * @param address the IP address
     * @return true, if IP address is list member
     */
    public static boolean isIpAddressPresent(List<String> list, String address) {
        return list.contains(address) || list.contains(StringUtils.substringBeforeLast(address, "."));
    }

    /**
     * Determine if an IP address is a member of a list
     *
     * @param list    the list
     * @param request the http request
     * @return true, if IP address is list member
     */
    public static boolean isIpAddressPresent(List<String> list, HttpServletRequest request) {
        return isIpAddressPresent(list, request.getRemoteAddr());
    }

    /**
     * @param req      web request
     * @param document document relative to the current request
     * @return URI with query string appended
     */
    public static String toAbsoluteURL(HttpServletRequest req, String document) {
        StringBuilder builder = new StringBuilder().append(req.getScheme()).append("://").append(req.getServerName()).append(':').append(req.getServerPort());
        if (!document.startsWith(req.getContextPath())) {
            builder.append(req.getContextPath());
        }
        return builder.append(document).toString();
    }
}
