package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface GseaComparisonCompletedHandler extends EventHandler {

    void onAnalysisCompleted(Comparison comparison, GseaComparisonPartition partition);
    
}
