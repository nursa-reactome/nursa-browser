package org.reactome.web.nursa.client.details.tabs.dataset.widgets;

import java.util.List;
import java.util.Map;

import org.reactome.web.analysis.client.model.PathwaySummary;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.BinomialSelectedEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwayHoveredEvent;
import org.reactome.web.nursa.client.details.tabs.dataset.NursaPathwaySelectedEvent;

public class BinomialComparisonTable extends AnalysisComparisonTable<PathwaySummary, Long> {

    BinomialComparisonTable(Map<String, List<PathwaySummary>> results) {
        super(results);
    }

    @Override
    protected Double getPvalue(PathwaySummary result) {
        return result.getEntities().getpValue();
    }

    @Override
    protected Double getFdr(PathwaySummary result) {
        return result.getEntities().getFdr();
    }

    @Override
    protected NursaPathwayHoveredEvent<Long> createHoveredEvent(Long key) {
        return new BinomialHoveredEvent(key);
    }

    @Override
    protected NursaPathwaySelectedEvent<Long> createSelectedEvent(Long key) {
        return new BinomialSelectedEvent(key);
    }

    @Override
    protected Long getKey(Map.Entry<String, List<PathwaySummary>> result) {
        // The event key is the pathway db id, which is the same for
        // both summaries.
        return result.getValue().get(0).getDbId();
    }

}
