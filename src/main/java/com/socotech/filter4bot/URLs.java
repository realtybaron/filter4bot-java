package com.socotech.filter4bot;

import com.google.common.base.Preconditions;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * User: marc Date: Mar 9, 2010 Time: 7:02:01 PM
 * <p/>
 * THIS SOFTWARE IS COPYRIGHTED.  THE SOFTWARE MAY NOT BE COPIED REPRODUCED, TRANSLATED, OR REDUCED TO ANY ELECTRONIC MEDIUM OR MACHINE READABLE FORM WITHOUT THE PRIOR WRITTEN CONSENT OF SOCO
 * TECHNOLOGIES.
 */
public class URLs {
    /**
     * Resolve a file in the classpath
     *
     * @param name file's name
     * @return file reference
     */
    public static URL findClasspathResource(String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name);
    }

    /**
     * Resolve a file in the classpath and write it to a String
     *
     * @param name file's name
     * @return URL as byte array
     * @throws IOException i/o error
     */
    public static String toString(String name) throws IOException {
        Preconditions.checkNotNull(name, "Name cannot be empty");
        URL url = URLs.findClasspathResource(name);
        InputStream is = url.openStream();
        try {
            return IOUtils.toString(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

    /**
     * Resolve a file in the classpath and write it to a String
     *
     * @param url URL's location
     * @return URL as byte array
     * @throws IOException i/o error
     */
    public static byte[] toByteArray(URL url) throws IOException {
        Preconditions.checkNotNull(url, "URL cannot be empty");
        InputStream is = url.openStream();
        try {
            return IOUtils.toByteArray(is);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
}
