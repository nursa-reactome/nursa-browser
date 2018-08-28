package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Nursa dataset details tab widget utility methods.
 * 
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public class NursaWidgetHelper {

    /**
     * @param index the zero-based superscript index
     * @param unqualifiedHeading the base header string
     * @return the header qualified by the superscript
     */
    static public SafeHtml superscriptHeader(int index, String unqualifiedHeading) {
        SafeHtmlBuilder shb = new SafeHtmlBuilder();
        shb.appendEscaped(unqualifiedHeading);
        shb.appendHtmlConstant("<sup>");
        shb.append(index + 1);
        shb.appendHtmlConstant("</sup>");
        SafeHtml html = shb.toSafeHtml();
        return html;
    }

}
