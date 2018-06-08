package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.Map;

import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.nursa.client.details.tabs.dataset.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.ComparisonPartition;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;

public class BinomialComparisonPanel extends ComparisonPathwayResultPanel<PathwaySummary, Long> {

    public BinomialComparisonPanel(
            Comparison comparison,
            ComparisonPartition<PathwaySummary> partition,
            EventBus eventBus) {
        super(comparison, partition, eventBus);
    }

    @Override
    protected AnalysisResultTable<PathwaySummary, Long>
    createUnsharedResultsTable(List<PathwaySummary> results) {
        return new BinomialExperimentTable(results);
    }

    @Override
    protected AnalysisResultTable<Map.Entry<String, List<PathwaySummary>>, Long>
    createSharedResultsTable(Map<String, List<PathwaySummary>> map) {
        return new BinomialComparisonTable(map);
    }

}
