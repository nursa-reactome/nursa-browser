package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.Map;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaSelectedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwayHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;

public class GseaComparisonTable extends AnalysisComparisonTable<GseaAnalysisResult, String> {

    protected GseaComparisonTable(Map<String, List<GseaAnalysisResult>> results) {
        super(results);
    }

    @Override
    protected NursaPathwayHoveredEvent<String> createHoveredEvent(String stId) {
        return new GseaHoveredEvent(stId);
    }

    @Override
    protected NursaPathwaySelectedEvent<String> createSelectedEvent(String stId) {
        return new GseaSelectedEvent(stId);
    }

    @Override
    protected Double getPvalue(GseaAnalysisResult result) {
        return new Double(result.getPvalue());
    }

    @Override
    protected Double getFdr(GseaAnalysisResult result) {
        return new Double(result.getFdr());
    }

    @Override
    protected String getKey(Map.Entry<String, List<GseaAnalysisResult>> result) {
        // The event key is the pathway stable id, which is the same for
        // both summaries.
        return result.getValue().get(0).getPathway().getStId();
    }

}
