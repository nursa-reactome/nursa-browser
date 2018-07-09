package org.reactome.web.nursa.client.details.tabs.dataset;

import com.google.gwt.event.shared.EventHandler;

public interface ComparisonCompletedHandler extends EventHandler {

    void onAnalysisCompleted(Comparison comparison, ComparisonPartition<?> partition);
    
}