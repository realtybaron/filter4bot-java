package com.socotech.filter4bot;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: marc Date: Oct 18, 2006 Time: 10:04:10 AM
 */
public class RequestUtils {

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(HttpServletRequest request, String name) {
        return (T) request.getAttribute(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAttribute(HttpServletRequest request, String name, T defaultValue) {
        Object value = getAttribute(request, name);
        return value == null ? defaultValue : (T) value;
    }

    /**
     * Return the "Referer" header in the request or an empty string if find.
     *
     * @param request form request
     * @return referer or empty string
     */
    public static String getRefererHeader(HttpServletRequest request) {
        String header = request.getHeader("Referer");
        return StringUtils.defaultString(header);
    }

    /**
     * Is this an include request?
     *
     * @param request current request
     * @return true, if include request URI attribute is not null
     */
    public static boolean isIncludeRequest(HttpServletRequest request) {
        return request.getAttribute(WebPaths.INCLUDE_REQUEST_URI_ATTRIBUTE) != null;
    }

    /**
     * Return the request URI for the given request, detecting an include request URL if called within a RequestDispatcher include. <p>As the value returned by
     * <code>request.getRequestURI()</code> is <i>not</i> decoded by the servlet container, this method will decode it.
     *
     * @param request current HTTP request
     * @return the request URI
     * @throws UnsupportedEncodingException if URL cannot be decoded
     */
    public static String getRequestUri(HttpServletRequest request) throws UnsupportedEncodingException {
        return getRequestUri(request, WebPaths.INCLUDE_REQUEST_URI_ATTRIBUTE);
    }

    /**
     * Append the query string to the request URL
     *
     * @param request web request
     * @return full url
     */
    public static String getAbsoluteURL(HttpServletRequest request) {
        String uri = RequestUtils.getAttribute(request, "javax.servlet.forward.request_uri");
        String query = RequestUtils.getAttribute(request, "javax.servlet.forward.query_string");
        StringBuilder buffer = new StringBuilder(uri);
        if (query != null) {
            buffer.append('?').append(request.getQueryString());
        }
        return buffer.toString();
    }

    /**
     * Return the request URI for root of the given request. If this is a forwarded request, correctly resolves to the request URI of the original request.
     *
     * @param request current HTTP request
     * @return the request URI
     * @throws UnsupportedEncodingException if URL cannot be decoded
     */
    public static String getOriginatingRequestUri(HttpServletRequest request) throws UnsupportedEncodingException {
        return getRequestUri(request, WebPaths.FORWARD_REQUEST_URI_ATTRIBUTE);
    }

    /**
     * Return the query string for the given request, detecting an include request URL if called within a RequestDispatcher include. <p>As the value returned by
     * <code>request.getRequestURI()</code> is <i>not</i> decoded by the servlet container, this method will decode it.
     *
     * @param request current HTTP request
     * @return the query string
     * @throws UnsupportedEncodingException if URL cannot be decoded
     */
    public static String getQueryString(HttpServletRequest request) throws UnsupportedEncodingException {
        return getQueryString(request, WebPaths.INCLUDE_QUERY_STRING_ATTRIBUTE);
    }

    /**
     * Return the query string for root of the given request. If this is a forwarded request, correctly resolves to the request URI of the original request.
     *
     * @param request current HTTP request
     * @return the query string
     * @throws UnsupportedEncodingException if URL cannot be decoded
     */
    public static String getOriginatingQueryString(HttpServletRequest request) throws UnsupportedEncodingException {
        return getQueryString(request, WebPaths.FORWARD_QUERY_STRING_ATTRIBUTE);
    }

    private static String getRequestUri(HttpServletRequest req, String attr) throws UnsupportedEncodingException {
        String uri = StringUtils.defaultIfEmpty((String) req.getAttribute(attr), req.getRequestURI());
        String encoding = StringUtils.defaultIfEmpty(req.getCharacterEncoding(), "UTF-8");
        return URLDecoder.decode(StringUtils.defaultString(uri), encoding);
    }

    private static String getQueryString(HttpServletRequest req, String attr) throws UnsupportedEncodingException {
        String query = StringUtils.defaultIfEmpty((String) req.getAttribute(attr), req.getQueryString());
        String encoding = StringUtils.defaultIfEmpty(req.getCharacterEncoding(), "UTF-8");
        return URLDecoder.decode(StringUtils.defaultString(query), encoding);
    }

    /**
     * Retrieve a String param from the request, but return default value if not found
     *
     * @param request      form request
     * @param name         param name
     * @param defaultValue default value
     * @return value in request or default value provided by caller
     */
    public static String getStringParameter(HttpServletRequest request, String name, String defaultValue) {
        String value = request.getParameter(name);
        return StringUtils.isEmpty(value) ? defaultValue : value;
    }

    /**
     * Retrieve a String param from the request, but return default value if not found
     *
     * @param request       form request
     * @param name          param name
     * @param defaultValues default value
     * @return value in request or default value provided by caller
     */
    public static String[] getStringParameters(HttpServletRequest request, String name, String[] defaultValues) {
        String[] values = request.getParameterValues(name);
        return values == null ? defaultValues : values;
    }

    /**
     * Retrieve a String param from the request, but throw exception if not found
     *
     * @param request form request
     * @param name    param name
     * @return value in request or default value provided by caller
     */
    public static String getRequiredStringParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        Preconditions.checkNotNull(name, "Required parameter not found in request: " + name);
        return value;
    }

    /**
     * Retrieve a boolean param from the request, but return default value if not found
     *
     * @param request      req
     * @param name         param name
     * @param defaultValue default value
     * @return value in request or default value provided by caller
     */
    public static boolean getBooleanParameter(HttpServletRequest request, String name, boolean defaultValue) {
        String value = request.getParameter(name);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return Boolean.parseBoolean(value);
        }
    }

    public static boolean getBooleanAttribute(HttpServletRequest request, String name, boolean defaultValue) {
        Object value = request.getAttribute(name);
        if (value == null) {
            return defaultValue;
        } else {
            return (Boolean) value;
        }
    }

    /**
     * Retrieve a required boolean param from the request, but throw exception if not found
     *
     * @param request form request
     * @param name    param name
     * @return value in request
     */
    public static boolean getRequiredBooleanParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return Boolean.parseBoolean(value);
    }

    /**
     * Retrieve a number param from the request, but return default value if not found
     *
     * @param request      form request
     * @param name         param name
     * @param defaultValue default value
     * @return value in request or default value provided by caller
     */
    public static Number getIdParameter(HttpServletRequest request, String name, Number defaultValue) {
        String value = request.getParameter(name);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return Integer.parseInt(value);
        }
    }

    /**
     * Retrieve a required int attribute from the request, but throw exception if not found
     *
     * @param request form request
     * @param name    param name
     * @return value in request
     */
    public static Number getRequiredIdParameter(HttpServletRequest request, String name) {
        Number value = getIdParameter(request, name, null);
        Preconditions.checkNotNull(value, "Required parameter not found in request: " + name);
        return value;
    }

    /**
     * Retrieve a int param from the request, but return default value if not found
     *
     * @param request      form request
     * @param name         param name
     * @param defaultValue default value
     * @return value in request or default value provided by caller
     */
    public static Integer getIntParameter(HttpServletRequest request, String name, Integer defaultValue) {
        String value = request.getParameter(name);
        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return Integer.parseInt(value);
        }
    }

    /**
     * Get int attribute from the request. but return default value if not found.
     *
     * @param request      form request
     * @param name         param name
     * @param defaultValue default value
     * @return value in request or default value provided by caller
     */
    public static Number getIdAttribute(HttpServletRequest request, String name, Number defaultValue) {
        Object value = request.getAttribute(name);
        if (value == null) {
            return defaultValue;
        } else {
            return Integer.parseInt(value.toString());
        }
    }

    /**
     * Get int attribute from the request. but return default value if not found.
     *
     * @param request      form request
     * @param name         param name
     * @param defaultValue default value
     * @return value in request or default value provided by caller
     */
    public static Integer getIntAttribute(HttpServletRequest request, String name, Integer defaultValue) {
        Object value = request.getAttribute(name);
        if (value == null) {
            return defaultValue;
        } else {
            return Integer.parseInt(value.toString());
        }
    }

    /**
     * Retrieve a required int attribute from the request, but throw exception if not found
     *
     * @param request form request
     * @param name    param name
     * @return value in request
     */
    public static Integer getRequiredIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        Preconditions.checkNotNull(value, "Required parameter not found in request: " + name);
        return Integer.parseInt(value);
    }

    /**
     * Retrieve a long param from the request, but return default value if not found
     *
     * @param request      form request
     * @param name         param name
     * @param defaultValue default value
     * @return value in request or default value provided by caller
     */
    public static Long getLongParameter(HttpServletRequest request, String name, Long defaultValue) {
        String value = request.getParameter(name);
        if (StringUtils.isEmpty(value) || !StringUtils.isNumeric(value)) {
            return defaultValue;
        } else {
            return Long.parseLong(value);
        }
    }

    /**
     * Retrieve a long attribute from the request, but return default value if not found
     *
     * @param req          form request
     * @param name         param name
     * @param defaultValue default value
     * @return value in request or default value provided by caller
     */
    public static Long getLongAttribute(HttpServletRequest req, String name, Long defaultValue) {
        Object value = req.getAttribute(name);
        if (value == null) {
            return defaultValue;
        } else {
            return Long.parseLong(value.toString());
        }
    }

    /**
     * Retrieve a required long param from the request, but throw exception if not found
     *
     * @param request form request
     * @param name    param name
     * @return value in request
     */
    public static Long getRequiredLongParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        Preconditions.checkNotNull(value, "Required parameter not found in request: " + name);
        return Long.parseLong(value);
    }

    /**
     * Retrieve a float attribute from the request, but return default value if not found
     *
     * @param req          form request
     * @param name         param name
     * @param defaultValue default value
     * @return value in request or default value provided by caller
     */
    public static Float getFloatAttribute(HttpServletRequest req, String name, Float defaultValue) {
        Object value = req.getAttribute(name);
        if (value == null) {
            return defaultValue;
        } else {
            return Float.parseFloat(value.toString());
        }
    }

    /**
     * Retrieve a required float param from the request, but throw exception if not found
     *
     * @param request form request
     * @param name    param name
     * @return value in request
     */
    public static Float getRequiredFloatParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        Preconditions.checkNotNull(value, "Required parameter not found in request: " + name);
        return Float.parseFloat(value);
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

}
