package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.reactome.gsea.model.GseaAnalysisResult;
import org.reactome.web.nursa.model.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.GseaComparisonPartition;

import com.google.gwt.event.shared.EventBus;

public class GseaComparisonPanel extends ComparisonPathwayResultPanel<GseaAnalysisResult, String> {

    public GseaComparisonPanel(Comparison comparison, GseaComparisonPartition partition,
            EventBus eventBus) {
        super(comparison, partition, eventBus);
    }

    @Override
    protected AnalysisResultPanel<GseaAnalysisResult, String>
    createUnsharedResultsPanel(List<GseaAnalysisResult> results, EventBus eventBus) {
        return new GseaAnalysisResultPanel(results, eventBus);
    }

    @Override
    protected AnalysisResultTable<Entry<String, List<GseaAnalysisResult>>, String>
    createSharedResultsTable(
            Map<String, List<GseaAnalysisResult>> results, EventBus eventBus) {
        return new GseaComparisonTable(results, eventBus);
    }

}
