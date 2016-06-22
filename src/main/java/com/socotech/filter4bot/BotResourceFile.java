package com.socotech.filter4bot;

/**
 * User: marc Date: Jun 5, 2009 Time: 9:46:04 AM
 * <p/>
 * THIS SOFTWARE IS COPYRIGHTED.  THE SOFTWARE MAY NOT BE COPIED REPRODUCED, TRANSLATED, OR REDUCED TO ANY ELECTRONIC MEDIUM OR MACHINE READABLE FORM WITHOUT THE PRIOR WRITTEN CONSENT OF SOCO TECHNOLOGIES.
 */
public enum BotResourceFile {
    msn,
    bing,
    misc,
    lycos,
    excite,
    ezooms,
    google,
    sitebot,
    inktomi,
    wisenut,
    infoseek,
    altavista,
    askjeeves,
    non_engines,
    northernlight;

    /**
     * Resolve a file name for this bot resource file
     *
     * @return local file name
     */
    public String getFileName() {
        return this.name() + ".txt";
    }
}
