package org.reactome.web.nursa.client.details.tabs.dataset;

import org.reactome.web.analysis.client.model.AnalysisResult;

import com.google.gwt.event.shared.EventHandler;

public interface BinomialAnalysisCompletedHandler extends EventHandler {

    void onAnalysisCompleted(AnalysisResult result);
    
}
