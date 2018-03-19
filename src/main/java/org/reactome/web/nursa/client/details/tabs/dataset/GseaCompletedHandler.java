package org.reactome.web.nursa.client.details.tabs.dataset;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;

import com.google.gwt.event.shared.EventHandler;

/**
 * @author Fred Loney <loneyf@ohsu.edu>
 */
public interface GseaCompletedHandler extends EventHandler {

    void onAnalysisCompleted(List<GseaAnalysisResult> result);
    
}
