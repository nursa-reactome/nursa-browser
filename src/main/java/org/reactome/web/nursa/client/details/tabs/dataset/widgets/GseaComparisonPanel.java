package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.Map;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.nursa.client.details.tabs.dataset.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaComparisonPartition;

import com.google.gwt.event.shared.EventBus;

public class GseaComparisonPanel extends ComparisonPathwayResultPanel<GseaAnalysisResult, String> {

    public GseaComparisonPanel(
            Comparison comparison,
            GseaComparisonPartition partition,
            EventBus eventBus) {
        super(comparison, partition, eventBus);
    }

    @Override
    protected AnalysisResultTable<GseaAnalysisResult, String>
    createUnsharedResultsTable(List<GseaAnalysisResult> results) {
        return new GseaExperimentTable(results);
    }

    @Override
    protected AnalysisResultTable<Map.Entry<String, List<GseaAnalysisResult>>, String>
    createSharedResultsTable(Map<String, List<GseaAnalysisResult>> map) {
        return new GseaComparisonTable(map);
    }

}
