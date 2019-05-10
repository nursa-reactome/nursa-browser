package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.nursa.model.Comparison;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialComparisonPartition;
import com.google.gwt.event.shared.EventBus;

public class BinomialComparisonPanel extends ComparisonPathwayResultPanel<PathwaySummary, Long> {
 
    public BinomialComparisonPanel(Comparison comparison, BinomialComparisonPartition partition,
            EventBus eventBus) {
        super(comparison, partition, eventBus);
    }

    @Override
    protected BinomialAnalysisResultPanel
    createUnsharedResultsPanel(List<PathwaySummary> results, EventBus eventBus) {
        return new BinomialAnalysisResultPanel(results, eventBus);
    }

    @Override
    protected AnalysisResultTable<Entry<String, List<PathwaySummary>>, Long>
    createSharedResultsTable(Map<String, List<PathwaySummary>> results, EventBus eventBus) {
        return new BinomialComparisonTable(results, eventBus);
    }

}
