package org.reactome.web.nursa.client.details.tabs.dataset;

import org.reactome.web.nursa.model.Comparison;

import com.google.gwt.event.shared.EventHandler;

public interface ComparisonAnalysisCompletedHandler extends EventHandler {

    void onAnalysisCompleted(Comparison comparison, ComparisonPartition<?> partition);
    
}