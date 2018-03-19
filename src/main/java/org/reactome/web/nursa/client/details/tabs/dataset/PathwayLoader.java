package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface PathwayLoader extends EventHandler {

    void load(String stId);

    void load(Long dbId);

}
