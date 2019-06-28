package org.reactome.web.nursa.diagram.client;

import org.reactome.web.analysis.client.filter.ResultFilter;
import org.reactome.web.diagram.client.DiagramViewerImpl;
import org.reactome.web.nursa.analysis.client.model.PseudoResourceSummary;

public class NursaDiagramViewer extends DiagramViewerImpl {

    @Override
    public void setAnalysisToken(String token, ResultFilter filter) {
        // A GSEA or comparison pseudo-analysis diagram overlay is
        // not yet supported.
        if (PseudoResourceSummary.RESOURCE.equals(filter.getResource())) {
            clearAnalysisOverlay();
        } else {
            super.setAnalysisToken(token, filter);
        }
    }

}
