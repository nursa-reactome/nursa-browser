package org.reactome.web.nursa.analysis.client.model;

import java.security.MessageDigest;

/**
 * This class is adapted from
 * <a href="https://gist.github.com/foreground-voice/8822544"></a>.
 * Standard MD5 libraries, e.g. Apache Commons, can't be compiled by GWT.
 * Other utilities, e.g.
 * <a href="https://github.com/ManfredTremmel/gwt-commons-codec">gwt-commons-codec</a>,
 * fail for a variety of reasons, e.g. class precedence.
 */
public class TokenGenerator {

    public static String create(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = value.getBytes("UTF-8");
            byte[] digest = md.digest(bytes);
            return encodeBase16(digest);
        } catch (Exception e) {
            String msg = "Could not create the MD5 digest for " + value;
            throw new IllegalStateException(msg, e);
        }
    }

    private static String encodeBase16(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++)
        {
            byte b = bytes[i];
            // top 4 bits
            char c = (char) ((b >> 4) & 0xf);
            if (c > 9)
                c = (char) ((c - 10) + 'a');
            else
                c = (char) (c + '0');
            sb.append(c);
            // bottom 4 bits
            c = (char) (b & 0xf);
            if (c > 9)
                c = (char) ((c - 10) + 'a');
            else
                c = (char) (c + '0');
            sb.append(c);
        }
        return sb.toString();
    }

}
