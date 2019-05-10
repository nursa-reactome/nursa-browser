package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;

import org.reactome.gsea.model.GseaAnalysisResult;

import com.google.gwt.event.shared.EventBus;

public class GseaAnalysisResultPanel extends AnalysisResultPanel<GseaAnalysisResult, String> {

    public GseaAnalysisResultPanel(List<GseaAnalysisResult> results, double filter,
            EventBus eventBus) {
        super(results, filter, eventBus);
    }

    public GseaAnalysisResultPanel(List<GseaAnalysisResult> results, EventBus eventBus) {
        super(results, eventBus);
    }

    @Override
    protected double getFdr(GseaAnalysisResult result) {
        return result.getFdr();
    }

    @Override
    protected AnalysisResultTable<GseaAnalysisResult, String> createResultsTable(
            List<GseaAnalysisResult> results) {
        return new GseaExperimentTable(results);
    }

}
