package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;

import com.google.gwt.event.shared.EventHandler;

public interface GseaAnalysisCompletedHandler extends EventHandler {

    void onAnalysisCompleted(List<GseaAnalysisResult> result);
    
}
