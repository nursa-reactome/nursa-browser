package org.reactome.web.nursa.client.search;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface ExperimentLoadedHandler extends EventHandler {

    void onExperimentLoaded(ExperimentLoadedEvent event);

}
