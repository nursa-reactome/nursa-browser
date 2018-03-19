package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface NursaPathwayHoveredHandler extends EventHandler {

    void onPathwayHovered(Long dbId);

    void onPathwayHovered(String stId);

}
